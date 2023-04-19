/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.type.WritableTypeId;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
/*     */ import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.PropertyFilter;
/*     */ import com.fasterxml.jackson.databind.ser.PropertyWriter;
/*     */ import com.fasterxml.jackson.databind.ser.impl.MapEntrySerializer;
/*     */ import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertyBasedObjectIdGenerator;
/*     */ import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class BeanSerializerBase extends StdSerializer<Object> implements ContextualSerializer, ResolvableSerializer, JsonFormatVisitable, SchemaAware {
/*  41 */   protected static final PropertyName NAME_FOR_OBJECT_REF = new PropertyName("#object-ref");
/*     */   
/*  43 */   protected static final BeanPropertyWriter[] NO_PROPS = new BeanPropertyWriter[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JavaType _beanType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final BeanPropertyWriter[] _props;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final BeanPropertyWriter[] _filteredProps;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final AnyGetterWriter _anyGetterWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Object _propertyFilterId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final AnnotatedMember _typeId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ObjectIdWriter _objectIdWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JsonFormat.Shape _serializationShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanSerializerBase(JavaType type, BeanSerializerBuilder builder, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties) {
/* 112 */     super(type);
/* 113 */     this._beanType = type;
/* 114 */     this._props = properties;
/* 115 */     this._filteredProps = filteredProperties;
/* 116 */     if (builder == null) {
/*     */ 
/*     */       
/* 119 */       this._typeId = null;
/* 120 */       this._anyGetterWriter = null;
/* 121 */       this._propertyFilterId = null;
/* 122 */       this._objectIdWriter = null;
/* 123 */       this._serializationShape = null;
/*     */     } else {
/* 125 */       this._typeId = builder.getTypeId();
/* 126 */       this._anyGetterWriter = builder.getAnyGetter();
/* 127 */       this._propertyFilterId = builder.getFilterId();
/* 128 */       this._objectIdWriter = builder.getObjectIdWriter();
/* 129 */       JsonFormat.Value format = builder.getBeanDescription().findExpectedFormat(null);
/* 130 */       this._serializationShape = (format == null) ? null : format.getShape();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanSerializerBase(BeanSerializerBase src, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties) {
/* 137 */     super(src._handledType);
/* 138 */     this._beanType = src._beanType;
/* 139 */     this._props = properties;
/* 140 */     this._filteredProps = filteredProperties;
/*     */     
/* 142 */     this._typeId = src._typeId;
/* 143 */     this._anyGetterWriter = src._anyGetterWriter;
/* 144 */     this._objectIdWriter = src._objectIdWriter;
/* 145 */     this._propertyFilterId = src._propertyFilterId;
/* 146 */     this._serializationShape = src._serializationShape;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanSerializerBase(BeanSerializerBase src, ObjectIdWriter objectIdWriter) {
/* 152 */     this(src, objectIdWriter, src._propertyFilterId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanSerializerBase(BeanSerializerBase src, ObjectIdWriter objectIdWriter, Object filterId) {
/* 161 */     super(src._handledType);
/* 162 */     this._beanType = src._beanType;
/* 163 */     this._props = src._props;
/* 164 */     this._filteredProps = src._filteredProps;
/*     */     
/* 166 */     this._typeId = src._typeId;
/* 167 */     this._anyGetterWriter = src._anyGetterWriter;
/* 168 */     this._objectIdWriter = objectIdWriter;
/* 169 */     this._propertyFilterId = filterId;
/* 170 */     this._serializationShape = src._serializationShape;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected BeanSerializerBase(BeanSerializerBase src, String[] toIgnore) {
/* 176 */     this(src, ArrayBuilders.arrayToSet((Object[])toIgnore));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BeanSerializerBase(BeanSerializerBase src, Set<String> toIgnore) {
/* 181 */     super(src._handledType);
/*     */     
/* 183 */     this._beanType = src._beanType;
/* 184 */     BeanPropertyWriter[] propsIn = src._props;
/* 185 */     BeanPropertyWriter[] fpropsIn = src._filteredProps;
/* 186 */     int len = propsIn.length;
/*     */     
/* 188 */     ArrayList<BeanPropertyWriter> propsOut = new ArrayList<>(len);
/* 189 */     ArrayList<BeanPropertyWriter> fpropsOut = (fpropsIn == null) ? null : new ArrayList<>(len);
/*     */     
/* 191 */     for (int i = 0; i < len; i++) {
/* 192 */       BeanPropertyWriter bpw = propsIn[i];
/*     */       
/* 194 */       if (toIgnore == null || !toIgnore.contains(bpw.getName())) {
/*     */ 
/*     */         
/* 197 */         propsOut.add(bpw);
/* 198 */         if (fpropsIn != null)
/* 199 */           fpropsOut.add(fpropsIn[i]); 
/*     */       } 
/*     */     } 
/* 202 */     this._props = propsOut.<BeanPropertyWriter>toArray(new BeanPropertyWriter[propsOut.size()]);
/* 203 */     this._filteredProps = (fpropsOut == null) ? null : fpropsOut.<BeanPropertyWriter>toArray(new BeanPropertyWriter[fpropsOut.size()]);
/*     */     
/* 205 */     this._typeId = src._typeId;
/* 206 */     this._anyGetterWriter = src._anyGetterWriter;
/* 207 */     this._objectIdWriter = src._objectIdWriter;
/* 208 */     this._propertyFilterId = src._propertyFilterId;
/* 209 */     this._serializationShape = src._serializationShape;
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
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected BeanSerializerBase withIgnorals(String[] toIgnore) {
/* 236 */     return withIgnorals(ArrayBuilders.arrayToSet((Object[])toIgnore));
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
/*     */ 
/*     */   
/*     */   protected BeanSerializerBase(BeanSerializerBase src) {
/* 262 */     this(src, src._props, src._filteredProps);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanSerializerBase(BeanSerializerBase src, NameTransformer unwrapper) {
/* 270 */     this(src, rename(src._props, unwrapper), rename(src._filteredProps, unwrapper));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final BeanPropertyWriter[] rename(BeanPropertyWriter[] props, NameTransformer transformer) {
/* 276 */     if (props == null || props.length == 0 || transformer == null || transformer == NameTransformer.NOP) {
/* 277 */       return props;
/*     */     }
/* 279 */     int len = props.length;
/* 280 */     BeanPropertyWriter[] result = new BeanPropertyWriter[len];
/* 281 */     for (int i = 0; i < len; i++) {
/* 282 */       BeanPropertyWriter bpw = props[i];
/* 283 */       if (bpw != null) {
/* 284 */         result[i] = bpw.rename(transformer);
/*     */       }
/*     */     } 
/* 287 */     return result;
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
/*     */   public void resolve(SerializerProvider provider) throws JsonMappingException {
/* 304 */     int filteredCount = (this._filteredProps == null) ? 0 : this._filteredProps.length;
/* 305 */     for (int i = 0, len = this._props.length; i < len; i++) {
/* 306 */       ContainerSerializer containerSerializer; BeanPropertyWriter prop = this._props[i];
/*     */       
/* 308 */       if (!prop.willSuppressNulls() && !prop.hasNullSerializer()) {
/* 309 */         JsonSerializer<Object> nullSer = provider.findNullValueSerializer((BeanProperty)prop);
/* 310 */         if (nullSer != null) {
/* 311 */           prop.assignNullSerializer(nullSer);
/*     */           
/* 313 */           if (i < filteredCount) {
/* 314 */             BeanPropertyWriter w2 = this._filteredProps[i];
/* 315 */             if (w2 != null) {
/* 316 */               w2.assignNullSerializer(nullSer);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 322 */       if (prop.hasSerializer()) {
/*     */         continue;
/*     */       }
/*     */       
/* 326 */       JsonSerializer<Object> ser = findConvertingSerializer(provider, prop);
/* 327 */       if (ser == null) {
/*     */         
/* 329 */         JavaType type = prop.getSerializationType();
/*     */ 
/*     */ 
/*     */         
/* 333 */         if (type == null) {
/* 334 */           type = prop.getType();
/* 335 */           if (!type.isFinal()) {
/* 336 */             if (type.isContainerType() || type.containedTypeCount() > 0) {
/* 337 */               prop.setNonTrivialBaseType(type);
/*     */             }
/*     */             continue;
/*     */           } 
/*     */         } 
/* 342 */         ser = provider.findValueSerializer(type, (BeanProperty)prop);
/*     */ 
/*     */ 
/*     */         
/* 346 */         if (type.isContainerType()) {
/* 347 */           TypeSerializer typeSer = (TypeSerializer)type.getContentType().getTypeHandler();
/* 348 */           if (typeSer != null)
/*     */           {
/* 350 */             if (ser instanceof ContainerSerializer) {
/*     */ 
/*     */               
/* 353 */               ContainerSerializer containerSerializer1 = ((ContainerSerializer)ser).withValueTypeSerializer(typeSer);
/* 354 */               containerSerializer = containerSerializer1;
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 360 */       if (i < filteredCount) {
/* 361 */         BeanPropertyWriter w2 = this._filteredProps[i];
/* 362 */         if (w2 != null) {
/* 363 */           w2.assignSerializer((JsonSerializer)containerSerializer);
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */       } 
/*     */       
/* 370 */       prop.assignSerializer((JsonSerializer)containerSerializer);
/*     */       
/*     */       continue;
/*     */     } 
/* 374 */     if (this._anyGetterWriter != null)
/*     */     {
/* 376 */       this._anyGetterWriter.resolve(provider);
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
/*     */   
/*     */   protected JsonSerializer<Object> findConvertingSerializer(SerializerProvider provider, BeanPropertyWriter prop) throws JsonMappingException {
/* 391 */     AnnotationIntrospector intr = provider.getAnnotationIntrospector();
/* 392 */     if (intr != null) {
/* 393 */       AnnotatedMember m = prop.getMember();
/* 394 */       if (m != null) {
/* 395 */         Object convDef = intr.findSerializationConverter((Annotated)m);
/* 396 */         if (convDef != null) {
/* 397 */           Converter<Object, Object> conv = provider.converterInstance((Annotated)prop.getMember(), convDef);
/* 398 */           JavaType delegateType = conv.getOutputType(provider.getTypeFactory());
/*     */ 
/*     */           
/* 401 */           JsonSerializer<?> ser = delegateType.isJavaLangObject() ? null : provider.findValueSerializer(delegateType, (BeanProperty)prop);
/* 402 */           return new StdDelegatingSerializer(conv, delegateType, ser);
/*     */         } 
/*     */       } 
/*     */     } 
/* 406 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
/* 415 */     AnnotationIntrospector intr = provider.getAnnotationIntrospector();
/*     */     
/* 417 */     AnnotatedMember accessor = (property == null || intr == null) ? null : property.getMember();
/* 418 */     SerializationConfig config = provider.getConfig();
/*     */ 
/*     */ 
/*     */     
/* 422 */     JsonFormat.Value format = findFormatOverrides(provider, property, handledType());
/* 423 */     JsonFormat.Shape shape = null;
/* 424 */     if (format != null && format.hasShape()) {
/* 425 */       shape = format.getShape();
/*     */       
/* 427 */       if (shape != JsonFormat.Shape.ANY && shape != this._serializationShape) {
/* 428 */         if (this._handledType.isEnum()) {
/* 429 */           BeanDescription desc; JsonSerializer<?> ser; switch (shape) {
/*     */ 
/*     */             
/*     */             case STRING:
/*     */             case NUMBER:
/*     */             case NUMBER_INT:
/* 435 */               desc = config.introspectClassAnnotations(this._beanType);
/* 436 */               ser = EnumSerializer.construct(this._beanType.getRawClass(), provider
/* 437 */                   .getConfig(), desc, format);
/* 438 */               return provider.handlePrimaryContextualization(ser, property);
/*     */           } 
/*     */         
/* 441 */         } else if (shape == JsonFormat.Shape.NATURAL && (
/* 442 */           !this._beanType.isMapLikeType() || !Map.class.isAssignableFrom(this._handledType))) {
/*     */           
/* 444 */           if (Map.Entry.class.isAssignableFrom(this._handledType)) {
/* 445 */             JavaType mapEntryType = this._beanType.findSuperType(Map.Entry.class);
/*     */             
/* 447 */             JavaType kt = mapEntryType.containedTypeOrUnknown(0);
/* 448 */             JavaType vt = mapEntryType.containedTypeOrUnknown(1);
/*     */ 
/*     */ 
/*     */             
/* 452 */             MapEntrySerializer mapEntrySerializer = new MapEntrySerializer(this._beanType, kt, vt, false, null, property);
/*     */             
/* 454 */             return provider.handlePrimaryContextualization((JsonSerializer)mapEntrySerializer, property);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 460 */     ObjectIdWriter oiw = this._objectIdWriter;
/* 461 */     Set<String> ignoredProps = null;
/* 462 */     Object newFilterId = null;
/*     */ 
/*     */     
/* 465 */     if (accessor != null) {
/* 466 */       JsonIgnoreProperties.Value ignorals = intr.findPropertyIgnorals((Annotated)accessor);
/* 467 */       if (ignorals != null) {
/* 468 */         ignoredProps = ignorals.findIgnoredForSerialization();
/*     */       }
/* 470 */       ObjectIdInfo objectIdInfo = intr.findObjectIdInfo((Annotated)accessor);
/* 471 */       if (objectIdInfo == null) {
/*     */         
/* 473 */         if (oiw != null) {
/* 474 */           objectIdInfo = intr.findObjectReferenceInfo((Annotated)accessor, null);
/* 475 */           if (objectIdInfo != null) {
/* 476 */             oiw = this._objectIdWriter.withAlwaysAsId(objectIdInfo.getAlwaysAsId());
/*     */           
/*     */           }
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 484 */         objectIdInfo = intr.findObjectReferenceInfo((Annotated)accessor, objectIdInfo);
/*     */         
/* 486 */         Class<?> implClass = objectIdInfo.getGeneratorType();
/* 487 */         JavaType type = provider.constructType(implClass);
/* 488 */         JavaType idType = provider.getTypeFactory().findTypeParameters(type, ObjectIdGenerator.class)[0];
/*     */         
/* 490 */         if (implClass == ObjectIdGenerators.PropertyGenerator.class) {
/* 491 */           String propName = objectIdInfo.getPropertyName().getSimpleName();
/* 492 */           BeanPropertyWriter idProp = null;
/*     */           
/* 494 */           for (int i = 0, len = this._props.length;; i++) {
/* 495 */             if (i == len)
/* 496 */               provider.reportBadDefinition(this._beanType, String.format("Invalid Object Id definition for %s: cannot find property with name '%s'", new Object[] {
/*     */                       
/* 498 */                       handledType().getName(), propName
/*     */                     })); 
/* 500 */             BeanPropertyWriter prop = this._props[i];
/* 501 */             if (propName.equals(prop.getName())) {
/* 502 */               idProp = prop;
/*     */ 
/*     */               
/* 505 */               if (i > 0) {
/* 506 */                 System.arraycopy(this._props, 0, this._props, 1, i);
/* 507 */                 this._props[0] = idProp;
/* 508 */                 if (this._filteredProps != null) {
/* 509 */                   BeanPropertyWriter fp = this._filteredProps[i];
/* 510 */                   System.arraycopy(this._filteredProps, 0, this._filteredProps, 1, i);
/* 511 */                   this._filteredProps[0] = fp;
/*     */                 } 
/*     */               } 
/*     */               break;
/*     */             } 
/*     */           } 
/* 517 */           idType = idProp.getType();
/* 518 */           PropertyBasedObjectIdGenerator propertyBasedObjectIdGenerator = new PropertyBasedObjectIdGenerator(objectIdInfo, idProp);
/* 519 */           oiw = ObjectIdWriter.construct(idType, (PropertyName)null, (ObjectIdGenerator)propertyBasedObjectIdGenerator, objectIdInfo.getAlwaysAsId());
/*     */         } else {
/* 521 */           ObjectIdGenerator<?> gen = provider.objectIdGeneratorInstance((Annotated)accessor, objectIdInfo);
/* 522 */           oiw = ObjectIdWriter.construct(idType, objectIdInfo.getPropertyName(), gen, objectIdInfo
/* 523 */               .getAlwaysAsId());
/*     */         } 
/*     */       } 
/*     */       
/* 527 */       Object filterId = intr.findFilterId((Annotated)accessor);
/* 528 */       if (filterId != null)
/*     */       {
/* 530 */         if (this._propertyFilterId == null || !filterId.equals(this._propertyFilterId)) {
/* 531 */           newFilterId = filterId;
/*     */         }
/*     */       }
/*     */     } 
/*     */     
/* 536 */     BeanSerializerBase contextual = this;
/* 537 */     if (oiw != null) {
/* 538 */       JsonSerializer<?> ser = provider.findValueSerializer(oiw.idType, property);
/* 539 */       oiw = oiw.withSerializer(ser);
/* 540 */       if (oiw != this._objectIdWriter) {
/* 541 */         contextual = contextual.withObjectIdWriter(oiw);
/*     */       }
/*     */     } 
/*     */     
/* 545 */     if (ignoredProps != null && !ignoredProps.isEmpty()) {
/* 546 */       contextual = contextual.withIgnorals(ignoredProps);
/*     */     }
/* 548 */     if (newFilterId != null) {
/* 549 */       contextual = contextual.withFilterId(newFilterId);
/*     */     }
/* 551 */     if (shape == null) {
/* 552 */       shape = this._serializationShape;
/*     */     }
/*     */     
/* 555 */     if (shape == JsonFormat.Shape.ARRAY) {
/* 556 */       return contextual.asArraySerializer();
/*     */     }
/* 558 */     return contextual;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<PropertyWriter> properties() {
/* 569 */     return Arrays.asList((Object[])this._props).iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean usesObjectId() {
/* 580 */     return (this._objectIdWriter != null);
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
/*     */   public void serializeWithType(Object bean, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/* 594 */     if (this._objectIdWriter != null) {
/* 595 */       gen.setCurrentValue(bean);
/* 596 */       _serializeWithObjectId(bean, gen, provider, typeSer);
/*     */       
/*     */       return;
/*     */     } 
/* 600 */     gen.setCurrentValue(bean);
/* 601 */     WritableTypeId typeIdDef = _typeIdDef(typeSer, bean, JsonToken.START_OBJECT);
/* 602 */     typeSer.writeTypePrefix(gen, typeIdDef);
/* 603 */     if (this._propertyFilterId != null) {
/* 604 */       serializeFieldsFiltered(bean, gen, provider);
/*     */     } else {
/* 606 */       serializeFields(bean, gen, provider);
/*     */     } 
/* 608 */     typeSer.writeTypeSuffix(gen, typeIdDef);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void _serializeWithObjectId(Object bean, JsonGenerator gen, SerializerProvider provider, boolean startEndObject) throws IOException {
/* 614 */     ObjectIdWriter w = this._objectIdWriter;
/* 615 */     WritableObjectId objectId = provider.findObjectId(bean, w.generator);
/*     */     
/* 617 */     if (objectId.writeAsId(gen, provider, w)) {
/*     */       return;
/*     */     }
/*     */     
/* 621 */     Object id = objectId.generateId(bean);
/* 622 */     if (w.alwaysAsId) {
/* 623 */       w.serializer.serialize(id, gen, provider);
/*     */       return;
/*     */     } 
/* 626 */     if (startEndObject) {
/* 627 */       gen.writeStartObject(bean);
/*     */     }
/* 629 */     objectId.writeAsField(gen, provider, w);
/* 630 */     if (this._propertyFilterId != null) {
/* 631 */       serializeFieldsFiltered(bean, gen, provider);
/*     */     } else {
/* 633 */       serializeFields(bean, gen, provider);
/*     */     } 
/* 635 */     if (startEndObject) {
/* 636 */       gen.writeEndObject();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void _serializeWithObjectId(Object bean, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/* 643 */     ObjectIdWriter w = this._objectIdWriter;
/* 644 */     WritableObjectId objectId = provider.findObjectId(bean, w.generator);
/*     */     
/* 646 */     if (objectId.writeAsId(gen, provider, w)) {
/*     */       return;
/*     */     }
/*     */     
/* 650 */     Object id = objectId.generateId(bean);
/* 651 */     if (w.alwaysAsId) {
/* 652 */       w.serializer.serialize(id, gen, provider);
/*     */       return;
/*     */     } 
/* 655 */     _serializeObjectId(bean, gen, provider, typeSer, objectId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _serializeObjectId(Object bean, JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer, WritableObjectId objectId) throws IOException {
/* 662 */     ObjectIdWriter w = this._objectIdWriter;
/* 663 */     WritableTypeId typeIdDef = _typeIdDef(typeSer, bean, JsonToken.START_OBJECT);
/*     */     
/* 665 */     typeSer.writeTypePrefix(g, typeIdDef);
/* 666 */     objectId.writeAsField(g, provider, w);
/* 667 */     if (this._propertyFilterId != null) {
/* 668 */       serializeFieldsFiltered(bean, g, provider);
/*     */     } else {
/* 670 */       serializeFields(bean, g, provider);
/*     */     } 
/* 672 */     typeSer.writeTypeSuffix(g, typeIdDef);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final WritableTypeId _typeIdDef(TypeSerializer typeSer, Object bean, JsonToken valueShape) {
/* 680 */     if (this._typeId == null) {
/* 681 */       return typeSer.typeId(bean, valueShape);
/*     */     }
/* 683 */     Object typeId = this._typeId.getValue(bean);
/* 684 */     if (typeId == null)
/*     */     {
/* 686 */       typeId = "";
/*     */     }
/* 688 */     return typeSer.typeId(bean, valueShape, typeId);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected final String _customTypeId(Object bean) {
/* 694 */     Object typeId = this._typeId.getValue(bean);
/* 695 */     if (typeId == null) {
/* 696 */       return "";
/*     */     }
/* 698 */     return (typeId instanceof String) ? (String)typeId : typeId.toString();
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
/*     */   protected void serializeFields(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException {
/*     */     BeanPropertyWriter[] props;
/* 711 */     if (this._filteredProps != null && provider.getActiveView() != null) {
/* 712 */       props = this._filteredProps;
/*     */     } else {
/* 714 */       props = this._props;
/*     */     } 
/* 716 */     int i = 0;
/*     */     try {
/* 718 */       for (int len = props.length; i < len; i++) {
/* 719 */         BeanPropertyWriter prop = props[i];
/* 720 */         if (prop != null) {
/* 721 */           prop.serializeAsField(bean, gen, provider);
/*     */         }
/*     */       } 
/* 724 */       if (this._anyGetterWriter != null) {
/* 725 */         this._anyGetterWriter.getAndSerialize(bean, gen, provider);
/*     */       }
/* 727 */     } catch (Exception e) {
/* 728 */       String name = (i == props.length) ? "[anySetter]" : props[i].getName();
/* 729 */       wrapAndThrow(provider, e, bean, name);
/* 730 */     } catch (StackOverflowError e) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 736 */       JsonMappingException mapE = new JsonMappingException((Closeable)gen, "Infinite recursion (StackOverflowError)", e);
/*     */       
/* 738 */       String name = (i == props.length) ? "[anySetter]" : props[i].getName();
/* 739 */       mapE.prependPath(new JsonMappingException.Reference(bean, name));
/* 740 */       throw mapE;
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
/*     */ 
/*     */   
/*     */   protected void serializeFieldsFiltered(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonGenerationException {
/*     */     BeanPropertyWriter[] props;
/* 757 */     if (this._filteredProps != null && provider.getActiveView() != null) {
/* 758 */       props = this._filteredProps;
/*     */     } else {
/* 760 */       props = this._props;
/*     */     } 
/* 762 */     PropertyFilter filter = findPropertyFilter(provider, this._propertyFilterId, bean);
/*     */     
/* 764 */     if (filter == null) {
/* 765 */       serializeFields(bean, gen, provider);
/*     */       return;
/*     */     } 
/* 768 */     int i = 0;
/*     */     try {
/* 770 */       for (int len = props.length; i < len; i++) {
/* 771 */         BeanPropertyWriter prop = props[i];
/* 772 */         if (prop != null) {
/* 773 */           filter.serializeAsField(bean, gen, provider, (PropertyWriter)prop);
/*     */         }
/*     */       } 
/* 776 */       if (this._anyGetterWriter != null) {
/* 777 */         this._anyGetterWriter.getAndFilter(bean, gen, provider, filter);
/*     */       }
/* 779 */     } catch (Exception e) {
/* 780 */       String name = (i == props.length) ? "[anySetter]" : props[i].getName();
/* 781 */       wrapAndThrow(provider, e, bean, name);
/* 782 */     } catch (StackOverflowError e) {
/*     */ 
/*     */       
/* 785 */       JsonMappingException mapE = new JsonMappingException((Closeable)gen, "Infinite recursion (StackOverflowError)", e);
/* 786 */       String name = (i == props.length) ? "[anySetter]" : props[i].getName();
/* 787 */       mapE.prependPath(new JsonMappingException.Reference(bean, name));
/* 788 */       throw mapE;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
/*     */     PropertyFilter filter;
/* 797 */     ObjectNode o = createSchemaNode("object", true);
/*     */ 
/*     */     
/* 800 */     JsonSerializableSchema ann = (JsonSerializableSchema)this._handledType.getAnnotation(JsonSerializableSchema.class);
/* 801 */     if (ann != null) {
/* 802 */       String id = ann.id();
/* 803 */       if (id != null && id.length() > 0) {
/* 804 */         o.put("id", id);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 810 */     ObjectNode propertiesNode = o.objectNode();
/*     */     
/* 812 */     if (this._propertyFilterId != null) {
/* 813 */       filter = findPropertyFilter(provider, this._propertyFilterId, null);
/*     */     } else {
/* 815 */       filter = null;
/*     */     } 
/*     */     
/* 818 */     for (int i = 0; i < this._props.length; i++) {
/* 819 */       BeanPropertyWriter prop = this._props[i];
/* 820 */       if (filter == null) {
/* 821 */         prop.depositSchemaProperty(propertiesNode, provider);
/*     */       } else {
/* 823 */         filter.depositSchemaProperty((PropertyWriter)prop, propertiesNode, provider);
/*     */       } 
/*     */     } 
/*     */     
/* 827 */     o.set("properties", (JsonNode)propertiesNode);
/* 828 */     return (JsonNode)o;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 836 */     if (visitor == null) {
/*     */       return;
/*     */     }
/* 839 */     JsonObjectFormatVisitor objectVisitor = visitor.expectObjectFormat(typeHint);
/* 840 */     if (objectVisitor == null) {
/*     */       return;
/*     */     }
/* 843 */     SerializerProvider provider = visitor.getProvider();
/* 844 */     if (this._propertyFilterId != null) {
/* 845 */       PropertyFilter filter = findPropertyFilter(visitor.getProvider(), this._propertyFilterId, null);
/*     */       
/* 847 */       for (int i = 0, end = this._props.length; i < end; i++) {
/* 848 */         filter.depositSchemaProperty((PropertyWriter)this._props[i], objectVisitor, provider);
/*     */       }
/*     */     } else {
/*     */       BeanPropertyWriter[] props;
/* 852 */       Class<?> view = (this._filteredProps == null || provider == null) ? null : provider.getActiveView();
/*     */       
/* 854 */       if (view != null) {
/* 855 */         props = this._filteredProps;
/*     */       } else {
/* 857 */         props = this._props;
/*     */       } 
/*     */       
/* 860 */       for (int i = 0, end = props.length; i < end; i++) {
/* 861 */         BeanPropertyWriter prop = props[i];
/* 862 */         if (prop != null)
/* 863 */           prop.depositSchemaProperty(objectVisitor, provider); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public abstract BeanSerializerBase withObjectIdWriter(ObjectIdWriter paramObjectIdWriter);
/*     */   
/*     */   protected abstract BeanSerializerBase withIgnorals(Set<String> paramSet);
/*     */   
/*     */   protected abstract BeanSerializerBase asArraySerializer();
/*     */   
/*     */   public abstract BeanSerializerBase withFilterId(Object paramObject);
/*     */   
/*     */   public abstract void serialize(Object paramObject, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider) throws IOException;
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\BeanSerializerBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */