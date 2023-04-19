/*     */ package com.fasterxml.jackson.databind.ser;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerators;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*     */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.impl.FilteredBeanPropertyWriter;
/*     */ import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertyBasedObjectIdGenerator;
/*     */ import com.fasterxml.jackson.databind.ser.std.MapSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
/*     */ import com.fasterxml.jackson.databind.type.ReferenceType;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanSerializerFactory
/*     */   extends BasicSerializerFactory
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  64 */   public static final BeanSerializerFactory instance = new BeanSerializerFactory(null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanSerializerFactory(SerializerFactoryConfig config) {
/*  77 */     super(config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializerFactory withConfig(SerializerFactoryConfig config) {
/*  89 */     if (this._factoryConfig == config) {
/*  90 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     if (getClass() != BeanSerializerFactory.class) {
/*  99 */       throw new IllegalStateException("Subtype of BeanSerializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalSerializers': cannot instantiate subtype with additional serializer definitions");
/*     */     }
/*     */ 
/*     */     
/* 103 */     return new BeanSerializerFactory(config);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Iterable<Serializers> customSerializers() {
/* 108 */     return this._factoryConfig.serializers();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonSerializer<Object> createSerializer(SerializerProvider prov, JavaType origType) throws JsonMappingException {
/*     */     boolean staticTyping;
/*     */     JavaType type;
/* 134 */     SerializationConfig config = prov.getConfig();
/* 135 */     BeanDescription beanDesc = config.introspect(origType);
/* 136 */     JsonSerializer<?> ser = findSerializerFromAnnotation(prov, (Annotated)beanDesc.getClassInfo());
/* 137 */     if (ser != null) {
/* 138 */       return (JsonSerializer)ser;
/*     */     }
/*     */ 
/*     */     
/* 142 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/*     */ 
/*     */     
/* 145 */     if (intr == null) {
/* 146 */       type = origType;
/*     */     } else {
/*     */       try {
/* 149 */         type = intr.refineSerializationType((MapperConfig)config, (Annotated)beanDesc.getClassInfo(), origType);
/* 150 */       } catch (JsonMappingException e) {
/* 151 */         return (JsonSerializer<Object>)prov.reportBadTypeDefinition(beanDesc, e.getMessage(), new Object[0]);
/*     */       } 
/*     */     } 
/* 154 */     if (type == origType) {
/* 155 */       staticTyping = false;
/*     */     } else {
/* 157 */       staticTyping = true;
/* 158 */       if (!type.hasRawClass(origType.getRawClass())) {
/* 159 */         beanDesc = config.introspect(type);
/*     */       }
/*     */     } 
/*     */     
/* 163 */     Converter<Object, Object> conv = beanDesc.findSerializationConverter();
/* 164 */     if (conv == null) {
/* 165 */       return (JsonSerializer)_createSerializer2(prov, type, beanDesc, staticTyping);
/*     */     }
/* 167 */     JavaType delegateType = conv.getOutputType(prov.getTypeFactory());
/*     */ 
/*     */     
/* 170 */     if (!delegateType.hasRawClass(type.getRawClass())) {
/* 171 */       beanDesc = config.introspect(delegateType);
/*     */       
/* 173 */       ser = findSerializerFromAnnotation(prov, (Annotated)beanDesc.getClassInfo());
/*     */     } 
/*     */     
/* 176 */     if (ser == null && !delegateType.isJavaLangObject()) {
/* 177 */       ser = _createSerializer2(prov, delegateType, beanDesc, true);
/*     */     }
/* 179 */     return (JsonSerializer<Object>)new StdDelegatingSerializer(conv, delegateType, ser);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JsonSerializer<?> _createSerializer2(SerializerProvider prov, JavaType type, BeanDescription beanDesc, boolean staticTyping) throws JsonMappingException {
/* 186 */     JsonSerializer<?> ser = null;
/* 187 */     SerializationConfig config = prov.getConfig();
/*     */ 
/*     */ 
/*     */     
/* 191 */     if (type.isContainerType()) {
/* 192 */       if (!staticTyping) {
/* 193 */         staticTyping = usesStaticTyping(config, beanDesc, null);
/*     */       }
/*     */       
/* 196 */       ser = buildContainerSerializer(prov, type, beanDesc, staticTyping);
/*     */       
/* 198 */       if (ser != null) {
/* 199 */         return ser;
/*     */       }
/*     */     } else {
/* 202 */       if (type.isReferenceType()) {
/* 203 */         ser = findReferenceSerializer(prov, (ReferenceType)type, beanDesc, staticTyping);
/*     */       } else {
/*     */         
/* 206 */         for (Serializers serializers : customSerializers()) {
/* 207 */           ser = serializers.findSerializer(config, type, beanDesc);
/* 208 */           if (ser != null) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 215 */       if (ser == null) {
/* 216 */         ser = findSerializerByAnnotations(prov, type, beanDesc);
/*     */       }
/*     */     } 
/*     */     
/* 220 */     if (ser == null) {
/*     */ 
/*     */ 
/*     */       
/* 224 */       ser = findSerializerByLookup(type, config, beanDesc, staticTyping);
/* 225 */       if (ser == null) {
/* 226 */         ser = findSerializerByPrimaryType(prov, type, beanDesc, staticTyping);
/* 227 */         if (ser == null) {
/*     */ 
/*     */ 
/*     */           
/* 231 */           ser = findBeanOrAddOnSerializer(prov, type, beanDesc, staticTyping);
/*     */ 
/*     */ 
/*     */           
/* 235 */           if (ser == null) {
/* 236 */             ser = prov.getUnknownTypeSerializer(beanDesc.getBeanClass());
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 241 */     if (ser != null)
/*     */     {
/* 243 */       if (this._factoryConfig.hasSerializerModifiers()) {
/* 244 */         for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/* 245 */           ser = mod.modifySerializer(config, beanDesc, ser);
/*     */         }
/*     */       }
/*     */     }
/* 249 */     return ser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public JsonSerializer<Object> findBeanSerializer(SerializerProvider prov, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
/* 264 */     return findBeanOrAddOnSerializer(prov, type, beanDesc, prov.isEnabled(MapperFeature.USE_STATIC_TYPING));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonSerializer<Object> findBeanOrAddOnSerializer(SerializerProvider prov, JavaType type, BeanDescription beanDesc, boolean staticTyping) throws JsonMappingException {
/* 279 */     if (!isPotentialBeanType(type.getRawClass()))
/*     */     {
/*     */       
/* 282 */       if (!type.isEnumType()) {
/* 283 */         return null;
/*     */       }
/*     */     }
/* 286 */     return constructBeanOrAddOnSerializer(prov, type, beanDesc, staticTyping);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeSerializer findPropertyTypeSerializer(JavaType baseType, SerializationConfig config, AnnotatedMember accessor) throws JsonMappingException {
/*     */     TypeSerializer typeSer;
/* 303 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/* 304 */     TypeResolverBuilder<?> b = ai.findPropertyTypeResolver((MapperConfig)config, accessor, baseType);
/*     */ 
/*     */ 
/*     */     
/* 308 */     if (b == null) {
/* 309 */       typeSer = createTypeSerializer(config, baseType);
/*     */     } else {
/* 311 */       Collection<NamedType> subtypes = config.getSubtypeResolver().collectAndResolveSubtypesByClass((MapperConfig)config, accessor, baseType);
/*     */       
/* 313 */       typeSer = b.buildTypeSerializer(config, baseType, subtypes);
/*     */     } 
/* 315 */     return typeSer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeSerializer findPropertyContentTypeSerializer(JavaType containerType, SerializationConfig config, AnnotatedMember accessor) throws JsonMappingException {
/*     */     TypeSerializer typeSer;
/* 332 */     JavaType contentType = containerType.getContentType();
/* 333 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/* 334 */     TypeResolverBuilder<?> b = ai.findPropertyContentTypeResolver((MapperConfig)config, accessor, containerType);
/*     */ 
/*     */ 
/*     */     
/* 338 */     if (b == null) {
/* 339 */       typeSer = createTypeSerializer(config, contentType);
/*     */     } else {
/* 341 */       Collection<NamedType> subtypes = config.getSubtypeResolver().collectAndResolveSubtypesByClass((MapperConfig)config, accessor, contentType);
/*     */       
/* 343 */       typeSer = b.buildTypeSerializer(config, contentType, subtypes);
/*     */     } 
/* 345 */     return typeSer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected JsonSerializer<Object> constructBeanSerializer(SerializerProvider prov, BeanDescription beanDesc) throws JsonMappingException {
/* 359 */     return constructBeanOrAddOnSerializer(prov, beanDesc.getType(), beanDesc, prov.isEnabled(MapperFeature.USE_STATIC_TYPING));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JsonSerializer<Object> constructBeanOrAddOnSerializer(SerializerProvider prov, JavaType type, BeanDescription beanDesc, boolean staticTyping) throws JsonMappingException {
/* 375 */     if (beanDesc.getBeanClass() == Object.class) {
/* 376 */       return prov.getUnknownTypeSerializer(Object.class);
/*     */     }
/*     */     
/* 379 */     SerializationConfig config = prov.getConfig();
/* 380 */     BeanSerializerBuilder builder = constructBeanSerializerBuilder(beanDesc);
/* 381 */     builder.setConfig(config);
/*     */ 
/*     */     
/* 384 */     List<BeanPropertyWriter> props = findBeanProperties(prov, beanDesc, builder);
/* 385 */     if (props == null) {
/* 386 */       props = new ArrayList<>();
/*     */     } else {
/* 388 */       props = removeOverlappingTypeIds(prov, beanDesc, builder, props);
/*     */     } 
/*     */ 
/*     */     
/* 392 */     prov.getAnnotationIntrospector().findAndAddVirtualProperties((MapperConfig)config, beanDesc.getClassInfo(), props);
/*     */ 
/*     */     
/* 395 */     if (this._factoryConfig.hasSerializerModifiers()) {
/* 396 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/* 397 */         props = mod.changeProperties(config, beanDesc, props);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 402 */     props = filterBeanProperties(config, beanDesc, props);
/*     */ 
/*     */     
/* 405 */     if (this._factoryConfig.hasSerializerModifiers()) {
/* 406 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/* 407 */         props = mod.orderProperties(config, beanDesc, props);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 414 */     builder.setObjectIdWriter(constructObjectIdHandler(prov, beanDesc, props));
/*     */     
/* 416 */     builder.setProperties(props);
/* 417 */     builder.setFilterId(findFilterId(config, beanDesc));
/*     */     
/* 419 */     AnnotatedMember anyGetter = beanDesc.findAnyGetter();
/* 420 */     if (anyGetter != null) {
/* 421 */       MapSerializer mapSerializer; JavaType anyType = anyGetter.getType();
/*     */       
/* 423 */       JavaType valueType = anyType.getContentType();
/* 424 */       TypeSerializer typeSer = createTypeSerializer(config, valueType);
/*     */ 
/*     */       
/* 427 */       JsonSerializer<?> anySer = findSerializerFromAnnotation(prov, (Annotated)anyGetter);
/* 428 */       if (anySer == null)
/*     */       {
/* 430 */         mapSerializer = MapSerializer.construct((Set)null, anyType, config
/* 431 */             .isEnabled(MapperFeature.USE_STATIC_TYPING), typeSer, null, null, null);
/*     */       }
/*     */ 
/*     */       
/* 435 */       PropertyName name = PropertyName.construct(anyGetter.getName());
/* 436 */       BeanProperty.Std anyProp = new BeanProperty.Std(name, valueType, null, anyGetter, PropertyMetadata.STD_OPTIONAL);
/*     */       
/* 438 */       builder.setAnyGetter(new AnyGetterWriter((BeanProperty)anyProp, anyGetter, (JsonSerializer<?>)mapSerializer));
/*     */     } 
/*     */     
/* 441 */     processViews(config, builder);
/*     */ 
/*     */     
/* 444 */     if (this._factoryConfig.hasSerializerModifiers()) {
/* 445 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/* 446 */         builder = mod.updateBuilder(config, beanDesc, builder);
/*     */       }
/*     */     }
/*     */     
/* 450 */     JsonSerializer<Object> ser = null;
/*     */     try {
/* 452 */       ser = (JsonSerializer)builder.build();
/* 453 */     } catch (RuntimeException e) {
/* 454 */       return (JsonSerializer<Object>)prov.reportBadTypeDefinition(beanDesc, "Failed to construct BeanSerializer for %s: (%s) %s", new Object[] { beanDesc
/* 455 */             .getType(), e.getClass().getName(), e.getMessage() });
/*     */     } 
/* 457 */     if (ser == null) {
/*     */ 
/*     */       
/* 460 */       ser = (JsonSerializer)findSerializerByAddonType(config, type, beanDesc, staticTyping);
/* 461 */       if (ser == null)
/*     */       {
/*     */ 
/*     */         
/* 465 */         if (beanDesc.hasKnownClassAnnotations()) {
/* 466 */           return (JsonSerializer<Object>)builder.createDummy();
/*     */         }
/*     */       }
/*     */     } 
/* 470 */     return ser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ObjectIdWriter constructObjectIdHandler(SerializerProvider prov, BeanDescription beanDesc, List<BeanPropertyWriter> props) throws JsonMappingException {
/* 477 */     ObjectIdInfo objectIdInfo = beanDesc.getObjectIdInfo();
/* 478 */     if (objectIdInfo == null) {
/* 479 */       return null;
/*     */     }
/*     */     
/* 482 */     Class<?> implClass = objectIdInfo.getGeneratorType();
/*     */ 
/*     */     
/* 485 */     if (implClass == ObjectIdGenerators.PropertyGenerator.class) {
/* 486 */       String propName = objectIdInfo.getPropertyName().getSimpleName();
/* 487 */       BeanPropertyWriter idProp = null;
/*     */       
/* 489 */       for (int i = 0, len = props.size();; i++) {
/* 490 */         if (i == len) {
/* 491 */           throw new IllegalArgumentException("Invalid Object Id definition for " + beanDesc.getBeanClass().getName() + ": cannot find property with name '" + propName + "'");
/*     */         }
/*     */         
/* 494 */         BeanPropertyWriter prop = props.get(i);
/* 495 */         if (propName.equals(prop.getName())) {
/* 496 */           idProp = prop;
/*     */ 
/*     */           
/* 499 */           if (i > 0) {
/* 500 */             props.remove(i);
/* 501 */             props.add(0, idProp);
/*     */           } 
/*     */           break;
/*     */         } 
/*     */       } 
/* 506 */       JavaType javaType = idProp.getType();
/* 507 */       PropertyBasedObjectIdGenerator propertyBasedObjectIdGenerator = new PropertyBasedObjectIdGenerator(objectIdInfo, idProp);
/*     */       
/* 509 */       return ObjectIdWriter.construct(javaType, (PropertyName)null, (ObjectIdGenerator)propertyBasedObjectIdGenerator, objectIdInfo.getAlwaysAsId());
/*     */     } 
/*     */ 
/*     */     
/* 513 */     JavaType type = prov.constructType(implClass);
/*     */     
/* 515 */     JavaType idType = prov.getTypeFactory().findTypeParameters(type, ObjectIdGenerator.class)[0];
/* 516 */     ObjectIdGenerator<?> gen = prov.objectIdGeneratorInstance((Annotated)beanDesc.getClassInfo(), objectIdInfo);
/* 517 */     return ObjectIdWriter.construct(idType, objectIdInfo.getPropertyName(), gen, objectIdInfo
/* 518 */         .getAlwaysAsId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanPropertyWriter constructFilteredBeanWriter(BeanPropertyWriter writer, Class<?>[] inViews) {
/* 529 */     return FilteredBeanPropertyWriter.constructViewBased(writer, inViews);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected PropertyBuilder constructPropertyBuilder(SerializationConfig config, BeanDescription beanDesc) {
/* 535 */     return new PropertyBuilder(config, beanDesc);
/*     */   }
/*     */   
/*     */   protected BeanSerializerBuilder constructBeanSerializerBuilder(BeanDescription beanDesc) {
/* 539 */     return new BeanSerializerBuilder(beanDesc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isPotentialBeanType(Class<?> type) {
/* 558 */     return (ClassUtil.canBeABeanType(type) == null && !ClassUtil.isProxyType(type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<BeanPropertyWriter> findBeanProperties(SerializerProvider prov, BeanDescription beanDesc, BeanSerializerBuilder builder) throws JsonMappingException {
/* 569 */     List<BeanPropertyDefinition> properties = beanDesc.findProperties();
/* 570 */     SerializationConfig config = prov.getConfig();
/*     */ 
/*     */     
/* 573 */     removeIgnorableTypes(config, beanDesc, properties);
/*     */ 
/*     */     
/* 576 */     if (config.isEnabled(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS)) {
/* 577 */       removeSetterlessGetters(config, beanDesc, properties);
/*     */     }
/*     */ 
/*     */     
/* 581 */     if (properties.isEmpty()) {
/* 582 */       return null;
/*     */     }
/*     */     
/* 585 */     boolean staticTyping = usesStaticTyping(config, beanDesc, null);
/* 586 */     PropertyBuilder pb = constructPropertyBuilder(config, beanDesc);
/*     */     
/* 588 */     ArrayList<BeanPropertyWriter> result = new ArrayList<>(properties.size());
/* 589 */     for (BeanPropertyDefinition property : properties) {
/* 590 */       AnnotatedMember accessor = property.getAccessor();
/*     */       
/* 592 */       if (property.isTypeId()) {
/* 593 */         if (accessor != null) {
/* 594 */           builder.setTypeId(accessor);
/*     */         }
/*     */         
/*     */         continue;
/*     */       } 
/* 599 */       AnnotationIntrospector.ReferenceProperty refType = property.findReferenceType();
/* 600 */       if (refType != null && refType.isBackReference()) {
/*     */         continue;
/*     */       }
/* 603 */       if (accessor instanceof com.fasterxml.jackson.databind.introspect.AnnotatedMethod) {
/* 604 */         result.add(_constructWriter(prov, property, pb, staticTyping, accessor)); continue;
/*     */       } 
/* 606 */       result.add(_constructWriter(prov, property, pb, staticTyping, accessor));
/*     */     } 
/*     */     
/* 609 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<BeanPropertyWriter> filterBeanProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> props) {
/* 629 */     JsonIgnoreProperties.Value ignorals = config.getDefaultPropertyIgnorals(beanDesc.getBeanClass(), beanDesc
/* 630 */         .getClassInfo());
/* 631 */     if (ignorals != null) {
/* 632 */       Set<String> ignored = ignorals.findIgnoredForSerialization();
/* 633 */       if (!ignored.isEmpty()) {
/* 634 */         Iterator<BeanPropertyWriter> it = props.iterator();
/* 635 */         while (it.hasNext()) {
/* 636 */           if (ignored.contains(((BeanPropertyWriter)it.next()).getName())) {
/* 637 */             it.remove();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 642 */     return props;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processViews(SerializationConfig config, BeanSerializerBuilder builder) {
/* 657 */     List<BeanPropertyWriter> props = builder.getProperties();
/* 658 */     boolean includeByDefault = config.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION);
/* 659 */     int propCount = props.size();
/* 660 */     int viewsFound = 0;
/* 661 */     BeanPropertyWriter[] filtered = new BeanPropertyWriter[propCount];
/*     */     
/* 663 */     for (int i = 0; i < propCount; i++) {
/* 664 */       BeanPropertyWriter bpw = props.get(i);
/* 665 */       Class<?>[] views = bpw.getViews();
/* 666 */       if (views == null || views.length == 0) {
/*     */ 
/*     */         
/* 669 */         if (includeByDefault) {
/* 670 */           filtered[i] = bpw;
/*     */         }
/*     */       } else {
/* 673 */         viewsFound++;
/* 674 */         filtered[i] = constructFilteredBeanWriter(bpw, views);
/*     */       } 
/*     */     } 
/*     */     
/* 678 */     if (includeByDefault && viewsFound == 0) {
/*     */       return;
/*     */     }
/* 681 */     builder.setFilteredProperties(filtered);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void removeIgnorableTypes(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyDefinition> properties) {
/* 693 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 694 */     HashMap<Class<?>, Boolean> ignores = new HashMap<>();
/* 695 */     Iterator<BeanPropertyDefinition> it = properties.iterator();
/* 696 */     while (it.hasNext()) {
/* 697 */       BeanPropertyDefinition property = it.next();
/* 698 */       AnnotatedMember accessor = property.getAccessor();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 703 */       if (accessor == null) {
/* 704 */         it.remove();
/*     */         continue;
/*     */       } 
/* 707 */       Class<?> type = property.getRawPrimaryType();
/* 708 */       Boolean result = ignores.get(type);
/* 709 */       if (result == null) {
/*     */         
/* 711 */         result = config.getConfigOverride(type).getIsIgnoredType();
/* 712 */         if (result == null) {
/* 713 */           BeanDescription desc = config.introspectClassAnnotations(type);
/* 714 */           AnnotatedClass ac = desc.getClassInfo();
/* 715 */           result = intr.isIgnorableType(ac);
/*     */           
/* 717 */           if (result == null) {
/* 718 */             result = Boolean.FALSE;
/*     */           }
/*     */         } 
/* 721 */         ignores.put(type, result);
/*     */       } 
/*     */       
/* 724 */       if (result.booleanValue()) {
/* 725 */         it.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void removeSetterlessGetters(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyDefinition> properties) {
/* 736 */     Iterator<BeanPropertyDefinition> it = properties.iterator();
/* 737 */     while (it.hasNext()) {
/* 738 */       BeanPropertyDefinition property = it.next();
/*     */ 
/*     */       
/* 741 */       if (!property.couldDeserialize() && !property.isExplicitlyIncluded()) {
/* 742 */         it.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<BeanPropertyWriter> removeOverlappingTypeIds(SerializerProvider prov, BeanDescription beanDesc, BeanSerializerBuilder builder, List<BeanPropertyWriter> props) {
/* 757 */     for (int i = 0, end = props.size(); i < end; i++) {
/* 758 */       BeanPropertyWriter bpw = props.get(i);
/* 759 */       TypeSerializer td = bpw.getTypeSerializer();
/* 760 */       if (td != null && td.getTypeInclusion() == JsonTypeInfo.As.EXTERNAL_PROPERTY) {
/*     */ 
/*     */         
/* 763 */         String n = td.getPropertyName();
/* 764 */         PropertyName typePropName = PropertyName.construct(n);
/*     */         
/* 766 */         for (BeanPropertyWriter w2 : props) {
/* 767 */           if (w2 != bpw && w2.wouldConflictWithName(typePropName)) {
/* 768 */             bpw.assignTypeSerializer(null); break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 773 */     return props;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanPropertyWriter _constructWriter(SerializerProvider prov, BeanPropertyDefinition propDef, PropertyBuilder pb, boolean staticTyping, AnnotatedMember accessor) throws JsonMappingException {
/* 791 */     PropertyName name = propDef.getFullName();
/* 792 */     JavaType type = accessor.getType();
/*     */     
/* 794 */     BeanProperty.Std property = new BeanProperty.Std(name, type, propDef.getWrapperName(), accessor, propDef.getMetadata());
/*     */ 
/*     */     
/* 797 */     JsonSerializer<?> annotatedSerializer = findSerializerFromAnnotation(prov, (Annotated)accessor);
/*     */ 
/*     */ 
/*     */     
/* 801 */     if (annotatedSerializer instanceof ResolvableSerializer) {
/* 802 */       ((ResolvableSerializer)annotatedSerializer).resolve(prov);
/*     */     }
/*     */     
/* 805 */     annotatedSerializer = prov.handlePrimaryContextualization(annotatedSerializer, (BeanProperty)property);
/*     */     
/* 807 */     TypeSerializer contentTypeSer = null;
/*     */     
/* 809 */     if (type.isContainerType() || type.isReferenceType()) {
/* 810 */       contentTypeSer = findPropertyContentTypeSerializer(type, prov.getConfig(), accessor);
/*     */     }
/*     */     
/* 813 */     TypeSerializer typeSer = findPropertyTypeSerializer(type, prov.getConfig(), accessor);
/* 814 */     return pb.buildWriter(prov, propDef, type, annotatedSerializer, typeSer, contentTypeSer, accessor, staticTyping);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\BeanSerializerFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */