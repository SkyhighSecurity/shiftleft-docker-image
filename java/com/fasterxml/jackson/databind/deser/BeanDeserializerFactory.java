/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
/*     */ import com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
/*     */ import com.fasterxml.jackson.databind.deser.impl.FieldProperty;
/*     */ import com.fasterxml.jackson.databind.deser.impl.SetterlessProperty;
/*     */ import com.fasterxml.jackson.databind.deser.std.ThrowableDeserializer;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedField;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.SimpleBeanPropertyDefinition;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class BeanDeserializerFactory extends BasicDeserializerFactory implements Serializable {
/*  38 */   private static final Class<?>[] INIT_CAUSE_PARAMS = new Class[] { Throwable.class };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   public static final BeanDeserializerFactory instance = new BeanDeserializerFactory(new DeserializerFactoryConfig());
/*     */ 
/*     */   
/*     */   public BeanDeserializerFactory(DeserializerFactoryConfig config) {
/*  54 */     super(config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeserializerFactory withConfig(DeserializerFactoryConfig config) {
/*  65 */     if (this._factoryConfig == config) {
/*  66 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     ClassUtil.verifyMustOverride(BeanDeserializerFactory.class, this, "withConfig");
/*  75 */     return new BeanDeserializerFactory(config);
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
/*     */   public JsonDeserializer<Object> createBeanDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
/*  95 */     DeserializationConfig config = ctxt.getConfig();
/*     */     
/*  97 */     JsonDeserializer<?> deser = _findCustomBeanDeserializer(type, config, beanDesc);
/*  98 */     if (deser != null) {
/*     */       
/* 100 */       if (this._factoryConfig.hasDeserializerModifiers()) {
/* 101 */         for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 102 */           deser = mod.modifyDeserializer(ctxt.getConfig(), beanDesc, deser);
/*     */         }
/*     */       }
/* 105 */       return (JsonDeserializer)deser;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 111 */     if (type.isThrowable()) {
/* 112 */       return buildThrowableDeserializer(ctxt, type, beanDesc);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 119 */     if (type.isAbstract() && !type.isPrimitive() && !type.isEnumType()) {
/*     */       
/* 121 */       JavaType concreteType = materializeAbstractType(ctxt, type, beanDesc);
/* 122 */       if (concreteType != null) {
/*     */ 
/*     */ 
/*     */         
/* 126 */         beanDesc = config.introspect(concreteType);
/* 127 */         return buildBeanDeserializer(ctxt, concreteType, beanDesc);
/*     */       } 
/*     */     } 
/*     */     
/* 131 */     deser = findStdDeserializer(ctxt, type, beanDesc);
/* 132 */     if (deser != null) {
/* 133 */       return (JsonDeserializer)deser;
/*     */     }
/*     */ 
/*     */     
/* 137 */     if (!isPotentialBeanType(type.getRawClass())) {
/* 138 */       return null;
/*     */     }
/*     */     
/* 141 */     _validateSubType(ctxt, type, beanDesc);
/*     */     
/* 143 */     return buildBeanDeserializer(ctxt, type, beanDesc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonDeserializer<Object> createBuilderBasedDeserializer(DeserializationContext ctxt, JavaType valueType, BeanDescription beanDesc, Class<?> builderClass) throws JsonMappingException {
/* 152 */     JavaType builderType = ctxt.constructType(builderClass);
/* 153 */     BeanDescription builderDesc = ctxt.getConfig().introspectForBuilder(builderType);
/* 154 */     return buildBuilderBasedDeserializer(ctxt, valueType, builderDesc);
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
/*     */   protected JsonDeserializer<?> findStdDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
/* 167 */     JsonDeserializer<?> deser = findDefaultDeserializer(ctxt, type, beanDesc);
/*     */     
/* 169 */     if (deser != null && 
/* 170 */       this._factoryConfig.hasDeserializerModifiers()) {
/* 171 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 172 */         deser = mod.modifyDeserializer(ctxt.getConfig(), beanDesc, deser);
/*     */       }
/*     */     }
/*     */     
/* 176 */     return deser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JavaType materializeAbstractType(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
/* 184 */     for (AbstractTypeResolver r : this._factoryConfig.abstractTypeResolvers()) {
/* 185 */       JavaType concrete = r.resolveAbstractType(ctxt.getConfig(), beanDesc);
/* 186 */       if (concrete != null) {
/* 187 */         return concrete;
/*     */       }
/*     */     } 
/* 190 */     return null;
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
/*     */   
/*     */   public JsonDeserializer<Object> buildBeanDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
/*     */     ValueInstantiator valueInstantiator;
/*     */     JsonDeserializer<?> deserializer;
/*     */     try {
/* 220 */       valueInstantiator = findValueInstantiator(ctxt, beanDesc);
/* 221 */     } catch (NoClassDefFoundError error) {
/* 222 */       return (JsonDeserializer<Object>)new ErrorThrowingDeserializer(error);
/* 223 */     } catch (IllegalArgumentException e) {
/*     */ 
/*     */ 
/*     */       
/* 227 */       throw InvalidDefinitionException.from(ctxt.getParser(), 
/* 228 */           ClassUtil.exceptionMessage(e), beanDesc, null);
/*     */     } 
/*     */     
/* 231 */     BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(ctxt, beanDesc);
/* 232 */     builder.setValueInstantiator(valueInstantiator);
/*     */     
/* 234 */     addBeanProps(ctxt, beanDesc, builder);
/* 235 */     addObjectIdReader(ctxt, beanDesc, builder);
/*     */ 
/*     */     
/* 238 */     addBackReferenceProperties(ctxt, beanDesc, builder);
/* 239 */     addInjectables(ctxt, beanDesc, builder);
/*     */     
/* 241 */     DeserializationConfig config = ctxt.getConfig();
/* 242 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 243 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 244 */         builder = mod.updateBuilder(config, beanDesc, builder);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 249 */     if (type.isAbstract() && !valueInstantiator.canInstantiate()) {
/* 250 */       deserializer = builder.buildAbstract();
/*     */     } else {
/* 252 */       deserializer = builder.build();
/*     */     } 
/*     */ 
/*     */     
/* 256 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 257 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 258 */         deserializer = mod.modifyDeserializer(config, beanDesc, deserializer);
/*     */       }
/*     */     }
/* 261 */     return (JsonDeserializer)deserializer;
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
/*     */   protected JsonDeserializer<Object> buildBuilderBasedDeserializer(DeserializationContext ctxt, JavaType valueType, BeanDescription builderDesc) throws JsonMappingException {
/*     */     ValueInstantiator valueInstantiator;
/*     */     try {
/* 279 */       valueInstantiator = findValueInstantiator(ctxt, builderDesc);
/* 280 */     } catch (NoClassDefFoundError error) {
/* 281 */       return (JsonDeserializer<Object>)new ErrorThrowingDeserializer(error);
/* 282 */     } catch (IllegalArgumentException e) {
/*     */ 
/*     */ 
/*     */       
/* 286 */       throw InvalidDefinitionException.from(ctxt.getParser(), 
/* 287 */           ClassUtil.exceptionMessage(e), builderDesc, null);
/*     */     } 
/*     */     
/* 290 */     DeserializationConfig config = ctxt.getConfig();
/* 291 */     BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(ctxt, builderDesc);
/* 292 */     builder.setValueInstantiator(valueInstantiator);
/*     */     
/* 294 */     addBeanProps(ctxt, builderDesc, builder);
/* 295 */     addObjectIdReader(ctxt, builderDesc, builder);
/*     */ 
/*     */     
/* 298 */     addBackReferenceProperties(ctxt, builderDesc, builder);
/* 299 */     addInjectables(ctxt, builderDesc, builder);
/*     */     
/* 301 */     JsonPOJOBuilder.Value builderConfig = builderDesc.findPOJOBuilderConfig();
/* 302 */     String buildMethodName = (builderConfig == null) ? "build" : builderConfig.buildMethodName;
/*     */ 
/*     */ 
/*     */     
/* 306 */     AnnotatedMethod buildMethod = builderDesc.findMethod(buildMethodName, null);
/* 307 */     if (buildMethod != null && 
/* 308 */       config.canOverrideAccessModifiers()) {
/* 309 */       ClassUtil.checkAndFixAccess(buildMethod.getMember(), config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*     */     }
/*     */     
/* 312 */     builder.setPOJOBuilder(buildMethod, builderConfig);
/*     */     
/* 314 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 315 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 316 */         builder = mod.updateBuilder(config, builderDesc, builder);
/*     */       }
/*     */     }
/* 319 */     JsonDeserializer<?> deserializer = builder.buildBuilderBased(valueType, buildMethodName);
/*     */ 
/*     */ 
/*     */     
/* 323 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 324 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 325 */         deserializer = mod.modifyDeserializer(config, builderDesc, deserializer);
/*     */       }
/*     */     }
/* 328 */     return (JsonDeserializer)deserializer;
/*     */   }
/*     */   
/*     */   protected void addObjectIdReader(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
/*     */     JavaType idType;
/*     */     SettableBeanProperty idProp;
/*     */     ObjectIdGenerator<?> gen;
/* 335 */     ObjectIdInfo objectIdInfo = beanDesc.getObjectIdInfo();
/* 336 */     if (objectIdInfo == null) {
/*     */       return;
/*     */     }
/* 339 */     Class<?> implClass = objectIdInfo.getGeneratorType();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 344 */     ObjectIdResolver resolver = ctxt.objectIdResolverInstance((Annotated)beanDesc.getClassInfo(), objectIdInfo);
/*     */ 
/*     */     
/* 347 */     if (implClass == ObjectIdGenerators.PropertyGenerator.class) {
/* 348 */       PropertyName propName = objectIdInfo.getPropertyName();
/* 349 */       idProp = builder.findProperty(propName);
/* 350 */       if (idProp == null) {
/* 351 */         throw new IllegalArgumentException("Invalid Object Id definition for " + beanDesc
/* 352 */             .getBeanClass().getName() + ": cannot find property with name '" + propName + "'");
/*     */       }
/* 354 */       idType = idProp.getType();
/* 355 */       PropertyBasedObjectIdGenerator propertyBasedObjectIdGenerator = new PropertyBasedObjectIdGenerator(objectIdInfo.getScope());
/*     */     } else {
/* 357 */       JavaType type = ctxt.constructType(implClass);
/* 358 */       idType = ctxt.getTypeFactory().findTypeParameters(type, ObjectIdGenerator.class)[0];
/* 359 */       idProp = null;
/* 360 */       gen = ctxt.objectIdGeneratorInstance((Annotated)beanDesc.getClassInfo(), objectIdInfo);
/*     */     } 
/*     */     
/* 363 */     JsonDeserializer<?> deser = ctxt.findRootValueDeserializer(idType);
/* 364 */     builder.setObjectIdReader(ObjectIdReader.construct(idType, objectIdInfo
/* 365 */           .getPropertyName(), gen, deser, idProp, resolver));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonDeserializer<Object> buildThrowableDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
/*     */     ThrowableDeserializer throwableDeserializer;
/*     */     JsonDeserializer<?> jsonDeserializer1;
/* 373 */     DeserializationConfig config = ctxt.getConfig();
/*     */     
/* 375 */     BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(ctxt, beanDesc);
/* 376 */     builder.setValueInstantiator(findValueInstantiator(ctxt, beanDesc));
/*     */     
/* 378 */     addBeanProps(ctxt, beanDesc, builder);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 383 */     AnnotatedMethod am = beanDesc.findMethod("initCause", INIT_CAUSE_PARAMS);
/* 384 */     if (am != null) {
/* 385 */       SimpleBeanPropertyDefinition propDef = SimpleBeanPropertyDefinition.construct((MapperConfig)ctxt.getConfig(), (AnnotatedMember)am, new PropertyName("cause"));
/*     */       
/* 387 */       SettableBeanProperty prop = constructSettableProperty(ctxt, beanDesc, (BeanPropertyDefinition)propDef, am
/* 388 */           .getParameterType(0));
/* 389 */       if (prop != null)
/*     */       {
/*     */         
/* 392 */         builder.addOrReplaceProperty(prop, true);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 397 */     builder.addIgnorable("localizedMessage");
/*     */     
/* 399 */     builder.addIgnorable("suppressed");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 406 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 407 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 408 */         builder = mod.updateBuilder(config, beanDesc, builder);
/*     */       }
/*     */     }
/* 411 */     JsonDeserializer<?> deserializer = builder.build();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 416 */     if (deserializer instanceof BeanDeserializer) {
/* 417 */       throwableDeserializer = new ThrowableDeserializer((BeanDeserializer)deserializer);
/*     */     }
/*     */ 
/*     */     
/* 421 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 422 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 423 */         jsonDeserializer1 = mod.modifyDeserializer(config, beanDesc, (JsonDeserializer<?>)throwableDeserializer);
/*     */       }
/*     */     }
/* 426 */     return (JsonDeserializer)jsonDeserializer1;
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
/*     */   protected BeanDeserializerBuilder constructBeanDeserializerBuilder(DeserializationContext ctxt, BeanDescription beanDesc) {
/* 443 */     return new BeanDeserializerBuilder(beanDesc, ctxt);
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
/*     */   protected void addBeanProps(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
/*     */     Set<String> ignored;
/* 457 */     boolean isConcrete = !beanDesc.getType().isAbstract();
/*     */     
/* 459 */     SettableBeanProperty[] creatorProps = isConcrete ? builder.getValueInstantiator().getFromObjectArguments(ctxt.getConfig()) : null;
/*     */     
/* 461 */     boolean hasCreatorProps = (creatorProps != null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 468 */     JsonIgnoreProperties.Value ignorals = ctxt.getConfig().getDefaultPropertyIgnorals(beanDesc.getBeanClass(), beanDesc
/* 469 */         .getClassInfo());
/*     */     
/* 471 */     if (ignorals != null) {
/* 472 */       boolean ignoreAny = ignorals.getIgnoreUnknown();
/* 473 */       builder.setIgnoreUnknownProperties(ignoreAny);
/*     */       
/* 475 */       ignored = ignorals.findIgnoredForDeserialization();
/* 476 */       for (String propName : ignored) {
/* 477 */         builder.addIgnorable(propName);
/*     */       }
/*     */     } else {
/* 480 */       ignored = Collections.emptySet();
/*     */     } 
/*     */ 
/*     */     
/* 484 */     AnnotatedMember anySetter = beanDesc.findAnySetterAccessor();
/* 485 */     if (anySetter != null) {
/* 486 */       builder.setAnySetter(constructAnySetter(ctxt, beanDesc, anySetter));
/*     */     }
/*     */     else {
/*     */       
/* 490 */       Collection<String> ignored2 = beanDesc.getIgnoredPropertyNames();
/* 491 */       if (ignored2 != null) {
/* 492 */         for (String propName : ignored2)
/*     */         {
/*     */           
/* 495 */           builder.addIgnorable(propName);
/*     */         }
/*     */       }
/*     */     } 
/*     */     
/* 500 */     boolean useGettersAsSetters = (ctxt.isEnabled(MapperFeature.USE_GETTERS_AS_SETTERS) && ctxt.isEnabled(MapperFeature.AUTO_DETECT_GETTERS));
/*     */ 
/*     */     
/* 503 */     List<BeanPropertyDefinition> propDefs = filterBeanProps(ctxt, beanDesc, builder, beanDesc
/* 504 */         .findProperties(), ignored);
/*     */     
/* 506 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 507 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 508 */         propDefs = mod.updateProperties(ctxt.getConfig(), beanDesc, propDefs);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 513 */     for (BeanPropertyDefinition propDef : propDefs) {
/* 514 */       SettableBeanProperty prop = null;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 519 */       if (propDef.hasSetter()) {
/* 520 */         AnnotatedMethod setter = propDef.getSetter();
/* 521 */         JavaType propertyType = setter.getParameterType(0);
/* 522 */         prop = constructSettableProperty(ctxt, beanDesc, propDef, propertyType);
/* 523 */       } else if (propDef.hasField()) {
/* 524 */         AnnotatedField field = propDef.getField();
/* 525 */         JavaType propertyType = field.getType();
/* 526 */         prop = constructSettableProperty(ctxt, beanDesc, propDef, propertyType);
/*     */       } else {
/*     */         
/* 529 */         AnnotatedMethod getter = propDef.getGetter();
/* 530 */         if (getter != null) {
/* 531 */           if (useGettersAsSetters && _isSetterlessType(getter.getRawType())) {
/*     */ 
/*     */             
/* 534 */             if (!builder.hasIgnorable(propDef.getName()))
/*     */             {
/*     */               
/* 537 */               prop = constructSetterlessProperty(ctxt, beanDesc, propDef);
/*     */             }
/* 539 */           } else if (!propDef.hasConstructorParameter()) {
/* 540 */             PropertyMetadata md = propDef.getMetadata();
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 545 */             if (md.getMergeInfo() != null) {
/* 546 */               prop = constructSetterlessProperty(ctxt, beanDesc, propDef);
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 554 */       if (hasCreatorProps && propDef.hasConstructorParameter()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 560 */         String name = propDef.getName();
/* 561 */         CreatorProperty cprop = null;
/* 562 */         if (creatorProps != null) {
/* 563 */           for (SettableBeanProperty cp : creatorProps) {
/* 564 */             if (name.equals(cp.getName()) && cp instanceof CreatorProperty) {
/* 565 */               cprop = (CreatorProperty)cp;
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         }
/* 570 */         if (cprop == null) {
/* 571 */           List<String> n = new ArrayList<>();
/* 572 */           for (SettableBeanProperty cp : creatorProps) {
/* 573 */             n.add(cp.getName());
/*     */           }
/* 575 */           ctxt.reportBadPropertyDefinition(beanDesc, propDef, "Could not find creator property with name '%s' (known Creator properties: %s)", new Object[] { name, n });
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 580 */         if (prop != null) {
/* 581 */           cprop.setFallbackSetter(prop);
/*     */         }
/* 583 */         Class<?>[] views = propDef.findViews();
/* 584 */         if (views == null) {
/* 585 */           views = beanDesc.findDefaultViews();
/*     */         }
/* 587 */         cprop.setViews(views);
/* 588 */         builder.addCreatorProperty(cprop);
/*     */         continue;
/*     */       } 
/* 591 */       if (prop != null) {
/*     */         
/* 593 */         Class<?>[] views = propDef.findViews();
/* 594 */         if (views == null) {
/* 595 */           views = beanDesc.findDefaultViews();
/*     */         }
/* 597 */         prop.setViews(views);
/* 598 */         builder.addProperty(prop);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean _isSetterlessType(Class<?> rawType) {
/* 607 */     return (Collection.class.isAssignableFrom(rawType) || Map.class
/* 608 */       .isAssignableFrom(rawType));
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
/*     */   protected List<BeanPropertyDefinition> filterBeanProps(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder, List<BeanPropertyDefinition> propDefsIn, Set<String> ignored) throws JsonMappingException {
/* 624 */     ArrayList<BeanPropertyDefinition> result = new ArrayList<>(Math.max(4, propDefsIn.size()));
/* 625 */     HashMap<Class<?>, Boolean> ignoredTypes = new HashMap<>();
/*     */     
/* 627 */     for (BeanPropertyDefinition property : propDefsIn) {
/* 628 */       String name = property.getName();
/* 629 */       if (ignored.contains(name)) {
/*     */         continue;
/*     */       }
/* 632 */       if (!property.hasConstructorParameter()) {
/* 633 */         Class<?> rawPropertyType = property.getRawPrimaryType();
/*     */         
/* 635 */         if (rawPropertyType != null && 
/* 636 */           isIgnorableType(ctxt.getConfig(), property, rawPropertyType, ignoredTypes)) {
/*     */           
/* 638 */           builder.addIgnorable(name);
/*     */           continue;
/*     */         } 
/*     */       } 
/* 642 */       result.add(property);
/*     */     } 
/* 644 */     return result;
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
/*     */   protected void addBackReferenceProperties(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
/* 658 */     List<BeanPropertyDefinition> refProps = beanDesc.findBackReferences();
/* 659 */     if (refProps != null) {
/* 660 */       for (BeanPropertyDefinition refProp : refProps) {
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
/* 677 */         String refName = refProp.findReferenceName();
/* 678 */         builder.addBackReferenceProperty(refName, constructSettableProperty(ctxt, beanDesc, refProp, refProp
/* 679 */               .getPrimaryType()));
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected void addReferenceProperties(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
/* 689 */     addBackReferenceProperties(ctxt, beanDesc, builder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addInjectables(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
/* 700 */     Map<Object, AnnotatedMember> raw = beanDesc.findInjectables();
/* 701 */     if (raw != null) {
/* 702 */       for (Map.Entry<Object, AnnotatedMember> entry : raw.entrySet()) {
/* 703 */         AnnotatedMember m = entry.getValue();
/* 704 */         builder.addInjectable(PropertyName.construct(m.getName()), m
/* 705 */             .getType(), beanDesc
/* 706 */             .getClassAnnotations(), m, entry.getKey());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SettableAnyProperty constructAnySetter(DeserializationContext ctxt, BeanDescription beanDesc, AnnotatedMember mutator) throws JsonMappingException {
/*     */     BeanProperty.Std std;
/*     */     JavaType keyType, valueType;
/* 729 */     if (mutator instanceof AnnotatedMethod) {
/*     */       
/* 731 */       AnnotatedMethod am = (AnnotatedMethod)mutator;
/* 732 */       keyType = am.getParameterType(0);
/* 733 */       valueType = am.getParameterType(1);
/* 734 */       valueType = resolveMemberAndTypeAnnotations(ctxt, mutator, valueType);
/* 735 */       std = new BeanProperty.Std(PropertyName.construct(mutator.getName()), valueType, null, mutator, PropertyMetadata.STD_OPTIONAL);
/*     */ 
/*     */     
/*     */     }
/* 739 */     else if (mutator instanceof AnnotatedField) {
/* 740 */       AnnotatedField af = (AnnotatedField)mutator;
/*     */       
/* 742 */       JavaType mapType = af.getType();
/* 743 */       mapType = resolveMemberAndTypeAnnotations(ctxt, mutator, mapType);
/* 744 */       keyType = mapType.getKeyType();
/* 745 */       valueType = mapType.getContentType();
/* 746 */       std = new BeanProperty.Std(PropertyName.construct(mutator.getName()), mapType, null, mutator, PropertyMetadata.STD_OPTIONAL);
/*     */     } else {
/*     */       
/* 749 */       return (SettableAnyProperty)ctxt.reportBadDefinition(beanDesc.getType(), String.format("Unrecognized mutator type for any setter: %s", new Object[] { mutator
/* 750 */               .getClass() }));
/*     */     } 
/*     */ 
/*     */     
/* 754 */     KeyDeserializer keyDeser = findKeyDeserializerFromAnnotation(ctxt, (Annotated)mutator);
/* 755 */     if (keyDeser == null) {
/* 756 */       keyDeser = (KeyDeserializer)keyType.getValueHandler();
/*     */     }
/* 758 */     if (keyDeser == null) {
/* 759 */       keyDeser = ctxt.findKeyDeserializer(keyType, (BeanProperty)std);
/*     */     }
/* 761 */     else if (keyDeser instanceof ContextualKeyDeserializer) {
/*     */       
/* 763 */       keyDeser = ((ContextualKeyDeserializer)keyDeser).createContextual(ctxt, (BeanProperty)std);
/*     */     } 
/*     */     
/* 766 */     JsonDeserializer<Object> deser = findContentDeserializerFromAnnotation(ctxt, (Annotated)mutator);
/* 767 */     if (deser == null) {
/* 768 */       deser = (JsonDeserializer<Object>)valueType.getValueHandler();
/*     */     }
/* 770 */     if (deser != null)
/*     */     {
/* 772 */       deser = ctxt.handlePrimaryContextualization(deser, (BeanProperty)std, valueType);
/*     */     }
/* 774 */     TypeDeserializer typeDeser = (TypeDeserializer)valueType.getTypeHandler();
/* 775 */     return new SettableAnyProperty((BeanProperty)std, mutator, valueType, keyDeser, deser, typeDeser);
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
/*     */   protected SettableBeanProperty constructSettableProperty(DeserializationContext ctxt, BeanDescription beanDesc, BeanPropertyDefinition propDef, JavaType propType0) throws JsonMappingException {
/*     */     FieldProperty fieldProperty;
/*     */     SettableBeanProperty settableBeanProperty;
/* 792 */     AnnotatedMember mutator = propDef.getNonConstructorMutator();
/*     */ 
/*     */ 
/*     */     
/* 796 */     if (mutator == null) {
/* 797 */       ctxt.reportBadPropertyDefinition(beanDesc, propDef, "No non-constructor mutator available", new Object[0]);
/*     */     }
/* 799 */     JavaType type = resolveMemberAndTypeAnnotations(ctxt, mutator, propType0);
/*     */     
/* 801 */     TypeDeserializer typeDeser = (TypeDeserializer)type.getTypeHandler();
/*     */     
/* 803 */     if (mutator instanceof AnnotatedMethod) {
/*     */       
/* 805 */       MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, beanDesc.getClassAnnotations(), (AnnotatedMethod)mutator);
/*     */     }
/*     */     else {
/*     */       
/* 809 */       fieldProperty = new FieldProperty(propDef, type, typeDeser, beanDesc.getClassAnnotations(), (AnnotatedField)mutator);
/*     */     } 
/* 811 */     JsonDeserializer<?> deser = findDeserializerFromAnnotation(ctxt, (Annotated)mutator);
/* 812 */     if (deser == null) {
/* 813 */       deser = (JsonDeserializer)type.getValueHandler();
/*     */     }
/* 815 */     if (deser != null) {
/* 816 */       deser = ctxt.handlePrimaryContextualization(deser, (BeanProperty)fieldProperty, type);
/* 817 */       settableBeanProperty = fieldProperty.withValueDeserializer(deser);
/*     */     } 
/*     */     
/* 820 */     AnnotationIntrospector.ReferenceProperty ref = propDef.findReferenceType();
/* 821 */     if (ref != null && ref.isManagedReference()) {
/* 822 */       settableBeanProperty.setManagedReferenceName(ref.getName());
/*     */     }
/* 824 */     ObjectIdInfo objectIdInfo = propDef.findObjectIdInfo();
/* 825 */     if (objectIdInfo != null) {
/* 826 */       settableBeanProperty.setObjectIdInfo(objectIdInfo);
/*     */     }
/* 828 */     return settableBeanProperty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SettableBeanProperty constructSetterlessProperty(DeserializationContext ctxt, BeanDescription beanDesc, BeanPropertyDefinition propDef) throws JsonMappingException {
/*     */     SettableBeanProperty settableBeanProperty;
/* 839 */     AnnotatedMethod getter = propDef.getGetter();
/* 840 */     JavaType type = resolveMemberAndTypeAnnotations(ctxt, (AnnotatedMember)getter, getter.getType());
/* 841 */     TypeDeserializer typeDeser = (TypeDeserializer)type.getTypeHandler();
/*     */     
/* 843 */     SetterlessProperty setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, beanDesc.getClassAnnotations(), getter);
/* 844 */     JsonDeserializer<?> deser = findDeserializerFromAnnotation(ctxt, (Annotated)getter);
/* 845 */     if (deser == null) {
/* 846 */       deser = (JsonDeserializer)type.getValueHandler();
/*     */     }
/* 848 */     if (deser != null) {
/* 849 */       deser = ctxt.handlePrimaryContextualization(deser, (BeanProperty)setterlessProperty, type);
/* 850 */       settableBeanProperty = setterlessProperty.withValueDeserializer(deser);
/*     */     } 
/* 852 */     return settableBeanProperty;
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
/* 871 */     String typeStr = ClassUtil.canBeABeanType(type);
/* 872 */     if (typeStr != null) {
/* 873 */       throw new IllegalArgumentException("Cannot deserialize Class " + type.getName() + " (of type " + typeStr + ") as a Bean");
/*     */     }
/* 875 */     if (ClassUtil.isProxyType(type)) {
/* 876 */       throw new IllegalArgumentException("Cannot deserialize Proxy class " + type.getName() + " as a Bean");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 881 */     typeStr = ClassUtil.isLocalType(type, true);
/* 882 */     if (typeStr != null) {
/* 883 */       throw new IllegalArgumentException("Cannot deserialize Class " + type.getName() + " (of type " + typeStr + ") as a Bean");
/*     */     }
/* 885 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isIgnorableType(DeserializationConfig config, BeanPropertyDefinition propDef, Class<?> type, Map<Class<?>, Boolean> ignoredTypes) {
/* 895 */     Boolean status = ignoredTypes.get(type);
/* 896 */     if (status != null) {
/* 897 */       return status.booleanValue();
/*     */     }
/*     */     
/* 900 */     if (type == String.class || type.isPrimitive()) {
/* 901 */       status = Boolean.FALSE;
/*     */     } else {
/*     */       
/* 904 */       status = config.getConfigOverride(type).getIsIgnoredType();
/* 905 */       if (status == null) {
/* 906 */         BeanDescription desc = config.introspectClassAnnotations(type);
/* 907 */         status = config.getAnnotationIntrospector().isIgnorableType(desc.getClassInfo());
/*     */         
/* 909 */         if (status == null) {
/* 910 */           status = Boolean.FALSE;
/*     */         }
/*     */       } 
/*     */     } 
/* 914 */     ignoredTypes.put(type, status);
/* 915 */     return status.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _validateSubType(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
/* 925 */     SubTypeValidator.instance().validateSubType(ctxt, type, beanDesc);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\BeanDeserializerFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */