/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.type.ArrayType;
/*     */ import com.fasterxml.jackson.databind.type.CollectionLikeType;
/*     */ import com.fasterxml.jackson.databind.type.CollectionType;
/*     */ import com.fasterxml.jackson.databind.type.MapLikeType;
/*     */ import com.fasterxml.jackson.databind.type.MapType;
/*     */ import com.fasterxml.jackson.databind.type.ReferenceType;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import com.fasterxml.jackson.databind.util.LRUMap;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
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
/*     */ public final class DeserializerCache
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final LRUMap<JavaType, JsonDeserializer<Object>> _cachedDeserializers;
/*  44 */   protected final HashMap<JavaType, JsonDeserializer<Object>> _incompleteDeserializers = new HashMap<>(8);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeserializerCache() {
/*  54 */     this(2000);
/*     */   }
/*     */   
/*     */   public DeserializerCache(int maxSize) {
/*  58 */     int initial = Math.min(64, maxSize >> 2);
/*  59 */     this._cachedDeserializers = new LRUMap(initial, maxSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/*  70 */     this._incompleteDeserializers.clear();
/*  71 */     return this;
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
/*     */   public int cachedDeserializersCount() {
/*  93 */     return this._cachedDeserializers.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flushCachedDeserializers() {
/* 104 */     this._cachedDeserializers.clear();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonDeserializer<Object> findValueDeserializer(DeserializationContext ctxt, DeserializerFactory factory, JavaType propertyType) throws JsonMappingException {
/* 139 */     JsonDeserializer<Object> deser = _findCachedDeserializer(propertyType);
/* 140 */     if (deser == null) {
/*     */       
/* 142 */       deser = _createAndCacheValueDeserializer(ctxt, factory, propertyType);
/* 143 */       if (deser == null)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 148 */         deser = _handleUnknownValueDeserializer(ctxt, propertyType);
/*     */       }
/*     */     } 
/* 151 */     return deser;
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
/*     */   public KeyDeserializer findKeyDeserializer(DeserializationContext ctxt, DeserializerFactory factory, JavaType type) throws JsonMappingException {
/* 166 */     KeyDeserializer kd = factory.createKeyDeserializer(ctxt, type);
/* 167 */     if (kd == null) {
/* 168 */       return _handleUnknownKeyDeserializer(ctxt, type);
/*     */     }
/*     */     
/* 171 */     if (kd instanceof ResolvableDeserializer) {
/* 172 */       ((ResolvableDeserializer)kd).resolve(ctxt);
/*     */     }
/* 174 */     return kd;
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
/*     */   public boolean hasValueDeserializerFor(DeserializationContext ctxt, DeserializerFactory factory, JavaType type) throws JsonMappingException {
/* 189 */     JsonDeserializer<Object> deser = _findCachedDeserializer(type);
/* 190 */     if (deser == null) {
/* 191 */       deser = _createAndCacheValueDeserializer(ctxt, factory, type);
/*     */     }
/* 193 */     return (deser != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JsonDeserializer<Object> _findCachedDeserializer(JavaType type) {
/* 204 */     if (type == null) {
/* 205 */       throw new IllegalArgumentException("Null JavaType passed");
/*     */     }
/* 207 */     if (_hasCustomHandlers(type)) {
/* 208 */       return null;
/*     */     }
/* 210 */     return (JsonDeserializer<Object>)this._cachedDeserializers.get(type);
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
/*     */   protected JsonDeserializer<Object> _createAndCacheValueDeserializer(DeserializationContext ctxt, DeserializerFactory factory, JavaType type) throws JsonMappingException {
/* 228 */     synchronized (this._incompleteDeserializers) {
/*     */       
/* 230 */       JsonDeserializer<Object> deser = _findCachedDeserializer(type);
/* 231 */       if (deser != null) {
/* 232 */         return deser;
/*     */       }
/* 234 */       int count = this._incompleteDeserializers.size();
/*     */       
/* 236 */       if (count > 0) {
/* 237 */         deser = this._incompleteDeserializers.get(type);
/* 238 */         if (deser != null) {
/* 239 */           return deser;
/*     */         }
/*     */       } 
/*     */       
/*     */       try {
/* 244 */         return _createAndCache2(ctxt, factory, type);
/*     */       } finally {
/*     */         
/* 247 */         if (count == 0 && this._incompleteDeserializers.size() > 0) {
/* 248 */           this._incompleteDeserializers.clear();
/*     */         }
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
/*     */   protected JsonDeserializer<Object> _createAndCache2(DeserializationContext ctxt, DeserializerFactory factory, JavaType type) throws JsonMappingException {
/*     */     JsonDeserializer<Object> deser;
/*     */     try {
/* 264 */       deser = _createDeserializer(ctxt, factory, type);
/* 265 */     } catch (IllegalArgumentException iae) {
/*     */ 
/*     */       
/* 268 */       throw JsonMappingException.from(ctxt, ClassUtil.exceptionMessage(iae), iae);
/*     */     } 
/* 270 */     if (deser == null) {
/* 271 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 277 */     boolean addToCache = (!_hasCustomHandlers(type) && deser.isCachable());
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
/* 291 */     if (deser instanceof ResolvableDeserializer) {
/* 292 */       this._incompleteDeserializers.put(type, deser);
/* 293 */       ((ResolvableDeserializer)deser).resolve(ctxt);
/* 294 */       this._incompleteDeserializers.remove(type);
/*     */     } 
/* 296 */     if (addToCache) {
/* 297 */       this._cachedDeserializers.put(type, deser);
/*     */     }
/* 299 */     return deser;
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
/*     */   protected JsonDeserializer<Object> _createDeserializer(DeserializationContext ctxt, DeserializerFactory factory, JavaType type) throws JsonMappingException {
/* 318 */     DeserializationConfig config = ctxt.getConfig();
/*     */ 
/*     */     
/* 321 */     if (type.isAbstract() || type.isMapLikeType() || type.isCollectionLikeType()) {
/* 322 */       type = factory.mapAbstractType(config, type);
/*     */     }
/* 324 */     BeanDescription beanDesc = config.introspect(type);
/*     */     
/* 326 */     JsonDeserializer<Object> deser = findDeserializerFromAnnotation(ctxt, (Annotated)beanDesc
/* 327 */         .getClassInfo());
/* 328 */     if (deser != null) {
/* 329 */       return deser;
/*     */     }
/*     */ 
/*     */     
/* 333 */     JavaType newType = modifyTypeByAnnotation(ctxt, (Annotated)beanDesc.getClassInfo(), type);
/* 334 */     if (newType != type) {
/* 335 */       type = newType;
/* 336 */       beanDesc = config.introspect(newType);
/*     */     } 
/*     */ 
/*     */     
/* 340 */     Class<?> builder = beanDesc.findPOJOBuilder();
/* 341 */     if (builder != null) {
/* 342 */       return factory.createBuilderBasedDeserializer(ctxt, type, beanDesc, builder);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 347 */     Converter<Object, Object> conv = beanDesc.findDeserializationConverter();
/* 348 */     if (conv == null) {
/* 349 */       return (JsonDeserializer)_createDeserializer2(ctxt, factory, type, beanDesc);
/*     */     }
/*     */     
/* 352 */     JavaType delegateType = conv.getInputType(ctxt.getTypeFactory());
/*     */     
/* 354 */     if (!delegateType.hasRawClass(type.getRawClass())) {
/* 355 */       beanDesc = config.introspect(delegateType);
/*     */     }
/* 357 */     return (JsonDeserializer<Object>)new StdDelegatingDeserializer(conv, delegateType, 
/* 358 */         _createDeserializer2(ctxt, factory, delegateType, beanDesc));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JsonDeserializer<?> _createDeserializer2(DeserializationContext ctxt, DeserializerFactory factory, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
/* 365 */     DeserializationConfig config = ctxt.getConfig();
/*     */     
/* 367 */     if (type.isEnumType()) {
/* 368 */       return factory.createEnumDeserializer(ctxt, type, beanDesc);
/*     */     }
/* 370 */     if (type.isContainerType()) {
/* 371 */       if (type.isArrayType()) {
/* 372 */         return factory.createArrayDeserializer(ctxt, (ArrayType)type, beanDesc);
/*     */       }
/* 374 */       if (type.isMapLikeType()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 380 */         JsonFormat.Value format = beanDesc.findExpectedFormat(null);
/* 381 */         if (format == null || format.getShape() != JsonFormat.Shape.OBJECT) {
/* 382 */           MapLikeType mlt = (MapLikeType)type;
/* 383 */           if (mlt.isTrueMapType()) {
/* 384 */             return factory.createMapDeserializer(ctxt, (MapType)mlt, beanDesc);
/*     */           }
/* 386 */           return factory.createMapLikeDeserializer(ctxt, mlt, beanDesc);
/*     */         } 
/*     */       } 
/* 389 */       if (type.isCollectionLikeType()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 395 */         JsonFormat.Value format = beanDesc.findExpectedFormat(null);
/* 396 */         if (format == null || format.getShape() != JsonFormat.Shape.OBJECT) {
/* 397 */           CollectionLikeType clt = (CollectionLikeType)type;
/* 398 */           if (clt.isTrueCollectionType()) {
/* 399 */             return factory.createCollectionDeserializer(ctxt, (CollectionType)clt, beanDesc);
/*     */           }
/* 401 */           return factory.createCollectionLikeDeserializer(ctxt, clt, beanDesc);
/*     */         } 
/*     */       } 
/*     */     } 
/* 405 */     if (type.isReferenceType()) {
/* 406 */       return factory.createReferenceDeserializer(ctxt, (ReferenceType)type, beanDesc);
/*     */     }
/* 408 */     if (JsonNode.class.isAssignableFrom(type.getRawClass())) {
/* 409 */       return factory.createTreeDeserializer(config, type, beanDesc);
/*     */     }
/* 411 */     return factory.createBeanDeserializer(ctxt, type, beanDesc);
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
/*     */   protected JsonDeserializer<Object> findDeserializerFromAnnotation(DeserializationContext ctxt, Annotated ann) throws JsonMappingException {
/* 423 */     Object deserDef = ctxt.getAnnotationIntrospector().findDeserializer(ann);
/* 424 */     if (deserDef == null) {
/* 425 */       return null;
/*     */     }
/* 427 */     JsonDeserializer<Object> deser = ctxt.deserializerInstance(ann, deserDef);
/*     */     
/* 429 */     return findConvertingDeserializer(ctxt, ann, deser);
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
/*     */   protected JsonDeserializer<Object> findConvertingDeserializer(DeserializationContext ctxt, Annotated a, JsonDeserializer<Object> deser) throws JsonMappingException {
/* 442 */     Converter<Object, Object> conv = findConverter(ctxt, a);
/* 443 */     if (conv == null) {
/* 444 */       return deser;
/*     */     }
/* 446 */     JavaType delegateType = conv.getInputType(ctxt.getTypeFactory());
/* 447 */     return (JsonDeserializer<Object>)new StdDelegatingDeserializer(conv, delegateType, deser);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Converter<Object, Object> findConverter(DeserializationContext ctxt, Annotated a) throws JsonMappingException {
/* 454 */     Object convDef = ctxt.getAnnotationIntrospector().findDeserializationConverter(a);
/* 455 */     if (convDef == null) {
/* 456 */       return null;
/*     */     }
/* 458 */     return ctxt.converterInstance(a, convDef);
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
/*     */   private JavaType modifyTypeByAnnotation(DeserializationContext ctxt, Annotated a, JavaType type) throws JsonMappingException {
/*     */     MapLikeType mapLikeType;
/* 480 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/* 481 */     if (intr == null) {
/* 482 */       return type;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 488 */     if (type.isMapLikeType()) {
/* 489 */       JavaType keyType = type.getKeyType();
/*     */ 
/*     */ 
/*     */       
/* 493 */       if (keyType != null && keyType.getValueHandler() == null) {
/* 494 */         Object kdDef = intr.findKeyDeserializer(a);
/* 495 */         if (kdDef != null) {
/* 496 */           KeyDeserializer kd = ctxt.keyDeserializerInstance(a, kdDef);
/* 497 */           if (kd != null) {
/* 498 */             mapLikeType = ((MapLikeType)type).withKeyValueHandler(kd);
/* 499 */             keyType = mapLikeType.getKeyType();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 504 */     JavaType contentType = mapLikeType.getContentType();
/* 505 */     if (contentType != null && 
/* 506 */       contentType.getValueHandler() == null) {
/* 507 */       Object cdDef = intr.findContentDeserializer(a);
/* 508 */       if (cdDef != null) {
/* 509 */         JsonDeserializer<?> cd = null;
/* 510 */         if (cdDef instanceof JsonDeserializer) {
/* 511 */           cdDef = cdDef;
/*     */         } else {
/* 513 */           Class<?> cdClass = _verifyAsClass(cdDef, "findContentDeserializer", JsonDeserializer.None.class);
/* 514 */           if (cdClass != null) {
/* 515 */             cd = ctxt.deserializerInstance(a, cdClass);
/*     */           }
/*     */         } 
/* 518 */         if (cd != null) {
/* 519 */           javaType1 = mapLikeType.withContentValueHandler(cd);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 527 */     JavaType javaType1 = intr.refineDeserializationType((MapperConfig)ctxt.getConfig(), a, javaType1);
/*     */     
/* 529 */     return javaType1;
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
/*     */   private boolean _hasCustomHandlers(JavaType t) {
/* 545 */     if (t.isContainerType()) {
/*     */       
/* 547 */       JavaType ct = t.getContentType();
/* 548 */       if (ct != null && (
/* 549 */         ct.getValueHandler() != null || ct.getTypeHandler() != null)) {
/* 550 */         return true;
/*     */       }
/*     */ 
/*     */       
/* 554 */       if (t.isMapLikeType()) {
/* 555 */         JavaType kt = t.getKeyType();
/* 556 */         if (kt.getValueHandler() != null) {
/* 557 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 561 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private Class<?> _verifyAsClass(Object src, String methodName, Class<?> noneClass) {
/* 566 */     if (src == null) {
/* 567 */       return null;
/*     */     }
/* 569 */     if (!(src instanceof Class)) {
/* 570 */       throw new IllegalStateException("AnnotationIntrospector." + methodName + "() returned value of type " + src.getClass().getName() + ": expected type JsonSerializer or Class<JsonSerializer> instead");
/*     */     }
/* 572 */     Class<?> cls = (Class)src;
/* 573 */     if (cls == noneClass || ClassUtil.isBogusClass(cls)) {
/* 574 */       return null;
/*     */     }
/* 576 */     return cls;
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
/*     */   protected JsonDeserializer<Object> _handleUnknownValueDeserializer(DeserializationContext ctxt, JavaType type) throws JsonMappingException {
/* 589 */     Class<?> rawClass = type.getRawClass();
/* 590 */     if (!ClassUtil.isConcrete(rawClass)) {
/* 591 */       return (JsonDeserializer<Object>)ctxt.reportBadDefinition(type, "Cannot find a Value deserializer for abstract type " + type);
/*     */     }
/* 593 */     return (JsonDeserializer<Object>)ctxt.reportBadDefinition(type, "Cannot find a Value deserializer for type " + type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected KeyDeserializer _handleUnknownKeyDeserializer(DeserializationContext ctxt, JavaType type) throws JsonMappingException {
/* 599 */     return (KeyDeserializer)ctxt.reportBadDefinition(type, "Cannot find a (Map) Key deserializer for type " + type);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\DeserializerCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */