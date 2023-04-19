/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonSerialize;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
/*     */ import com.fasterxml.jackson.databind.type.ReferenceType;
/*     */ import com.fasterxml.jackson.databind.util.ArrayBuilders;
/*     */ import com.fasterxml.jackson.databind.util.BeanUtil;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ReferenceTypeSerializer<T>
/*     */   extends StdSerializer<T>
/*     */   implements ContextualSerializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  35 */   public static final Object MARKER_FOR_EMPTY = JsonInclude.Include.NON_EMPTY;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JavaType _referredType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final BeanProperty _property;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final TypeSerializer _valueTypeSerializer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JsonSerializer<Object> _valueSerializer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final NameTransformer _unwrapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected transient PropertySerializerMap _dynamicSerializers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Object _suppressableValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean _suppressNulls;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceTypeSerializer(ReferenceType fullType, boolean staticTyping, TypeSerializer vts, JsonSerializer<Object> ser) {
/* 100 */     super((JavaType)fullType);
/* 101 */     this._referredType = fullType.getReferencedType();
/* 102 */     this._property = null;
/* 103 */     this._valueTypeSerializer = vts;
/* 104 */     this._valueSerializer = ser;
/* 105 */     this._unwrapper = null;
/* 106 */     this._suppressableValue = null;
/* 107 */     this._suppressNulls = false;
/* 108 */     this._dynamicSerializers = PropertySerializerMap.emptyForProperties();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ReferenceTypeSerializer(ReferenceTypeSerializer<?> base, BeanProperty property, TypeSerializer vts, JsonSerializer<?> valueSer, NameTransformer unwrapper, Object suppressableValue, boolean suppressNulls) {
/* 117 */     super(base);
/* 118 */     this._referredType = base._referredType;
/*     */     
/* 120 */     this._dynamicSerializers = PropertySerializerMap.emptyForProperties();
/* 121 */     this._property = property;
/* 122 */     this._valueTypeSerializer = vts;
/* 123 */     this._valueSerializer = (JsonSerializer)valueSer;
/* 124 */     this._unwrapper = unwrapper;
/* 125 */     this._suppressableValue = suppressableValue;
/* 126 */     this._suppressNulls = suppressNulls;
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonSerializer<T> unwrappingSerializer(NameTransformer transformer) {
/* 131 */     JsonSerializer<Object> valueSer = this._valueSerializer;
/* 132 */     if (valueSer != null) {
/* 133 */       valueSer = valueSer.unwrappingSerializer(transformer);
/*     */     }
/*     */     
/* 136 */     NameTransformer unwrapper = (this._unwrapper == null) ? transformer : NameTransformer.chainedTransformer(transformer, this._unwrapper);
/* 137 */     if (this._valueSerializer == valueSer && this._unwrapper == unwrapper) {
/* 138 */       return this;
/*     */     }
/* 140 */     return withResolved(this._property, this._valueTypeSerializer, valueSer, unwrapper);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract ReferenceTypeSerializer<T> withResolved(BeanProperty paramBeanProperty, TypeSerializer paramTypeSerializer, JsonSerializer<?> paramJsonSerializer, NameTransformer paramNameTransformer);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract ReferenceTypeSerializer<T> withContentInclusion(Object paramObject, boolean paramBoolean);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean _isValuePresent(T paramT);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Object _getReferenced(T paramT);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Object _getReferencedIfPresent(T paramT);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
/*     */     ReferenceTypeSerializer<?> refSer;
/* 196 */     TypeSerializer typeSer = this._valueTypeSerializer;
/* 197 */     if (typeSer != null) {
/* 198 */       typeSer = typeSer.forProperty(property);
/*     */     }
/*     */     
/* 201 */     JsonSerializer<?> ser = findAnnotatedContentSerializer(provider, property);
/* 202 */     if (ser == null) {
/*     */       
/* 204 */       ser = this._valueSerializer;
/* 205 */       if (ser == null) {
/*     */         
/* 207 */         if (_useStatic(provider, property, this._referredType)) {
/* 208 */           ser = _findSerializer(provider, this._referredType, property);
/*     */         }
/*     */       } else {
/* 211 */         ser = provider.handlePrimaryContextualization(ser, property);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 216 */     if (this._property == property && this._valueTypeSerializer == typeSer && this._valueSerializer == ser) {
/*     */       
/* 218 */       refSer = this;
/*     */     } else {
/* 220 */       refSer = withResolved(property, typeSer, ser, this._unwrapper);
/*     */     } 
/*     */ 
/*     */     
/* 224 */     if (property != null) {
/* 225 */       JsonInclude.Value inclV = property.findPropertyInclusion((MapperConfig)provider.getConfig(), handledType());
/* 226 */       if (inclV != null) {
/* 227 */         JsonInclude.Include incl = inclV.getContentInclusion();
/*     */         
/* 229 */         if (incl != JsonInclude.Include.USE_DEFAULTS) {
/*     */           Object valueToSuppress;
/*     */           boolean suppressNulls;
/* 232 */           switch (incl) {
/*     */             case NON_DEFAULT:
/* 234 */               valueToSuppress = BeanUtil.getDefaultValue(this._referredType);
/* 235 */               suppressNulls = true;
/* 236 */               if (valueToSuppress != null && 
/* 237 */                 valueToSuppress.getClass().isArray()) {
/* 238 */                 valueToSuppress = ArrayBuilders.getArrayComparator(valueToSuppress);
/*     */               }
/*     */               break;
/*     */             
/*     */             case NON_ABSENT:
/* 243 */               suppressNulls = true;
/* 244 */               valueToSuppress = this._referredType.isReferenceType() ? MARKER_FOR_EMPTY : null;
/*     */               break;
/*     */             case NON_EMPTY:
/* 247 */               suppressNulls = true;
/* 248 */               valueToSuppress = MARKER_FOR_EMPTY;
/*     */               break;
/*     */             case CUSTOM:
/* 251 */               valueToSuppress = provider.includeFilterInstance(null, inclV.getContentFilter());
/* 252 */               if (valueToSuppress == null) {
/* 253 */                 suppressNulls = true; break;
/*     */               } 
/* 255 */               suppressNulls = provider.includeFilterSuppressNulls(valueToSuppress);
/*     */               break;
/*     */             
/*     */             case NON_NULL:
/* 259 */               valueToSuppress = null;
/* 260 */               suppressNulls = true;
/*     */               break;
/*     */             
/*     */             default:
/* 264 */               valueToSuppress = null;
/* 265 */               suppressNulls = false;
/*     */               break;
/*     */           } 
/* 268 */           if (this._suppressableValue != valueToSuppress || this._suppressNulls != suppressNulls)
/*     */           {
/* 270 */             refSer = refSer.withContentInclusion(valueToSuppress, suppressNulls);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 275 */     return refSer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean _useStatic(SerializerProvider provider, BeanProperty property, JavaType referredType) {
/* 282 */     if (referredType.isJavaLangObject()) {
/* 283 */       return false;
/*     */     }
/*     */     
/* 286 */     if (referredType.isFinal()) {
/* 287 */       return true;
/*     */     }
/*     */     
/* 290 */     if (referredType.useStaticType()) {
/* 291 */       return true;
/*     */     }
/*     */     
/* 294 */     AnnotationIntrospector intr = provider.getAnnotationIntrospector();
/* 295 */     if (intr != null && property != null) {
/* 296 */       AnnotatedMember annotatedMember = property.getMember();
/* 297 */       if (annotatedMember != null) {
/* 298 */         JsonSerialize.Typing t = intr.findSerializationTyping((Annotated)property.getMember());
/* 299 */         if (t == JsonSerialize.Typing.STATIC) {
/* 300 */           return true;
/*     */         }
/* 302 */         if (t == JsonSerialize.Typing.DYNAMIC) {
/* 303 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 308 */     return provider.isEnabled(MapperFeature.USE_STATIC_TYPING);
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
/*     */   public boolean isEmpty(SerializerProvider provider, T value) {
/* 321 */     if (!_isValuePresent(value)) {
/* 322 */       return true;
/*     */     }
/* 324 */     Object contents = _getReferenced(value);
/* 325 */     if (contents == null) {
/* 326 */       return this._suppressNulls;
/*     */     }
/* 328 */     if (this._suppressableValue == null) {
/* 329 */       return false;
/*     */     }
/* 331 */     JsonSerializer<Object> ser = this._valueSerializer;
/* 332 */     if (ser == null) {
/*     */       try {
/* 334 */         ser = _findCachedSerializer(provider, contents.getClass());
/* 335 */       } catch (JsonMappingException e) {
/* 336 */         throw new RuntimeJsonMappingException(e);
/*     */       } 
/*     */     }
/* 339 */     if (this._suppressableValue == MARKER_FOR_EMPTY) {
/* 340 */       return ser.isEmpty(provider, contents);
/*     */     }
/* 342 */     return this._suppressableValue.equals(contents);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUnwrappingSerializer() {
/* 347 */     return (this._unwrapper != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaType getReferredType() {
/* 354 */     return this._referredType;
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
/*     */   public void serialize(T ref, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 367 */     Object value = _getReferencedIfPresent(ref);
/* 368 */     if (value == null) {
/* 369 */       if (this._unwrapper == null) {
/* 370 */         provider.defaultSerializeNull(g);
/*     */       }
/*     */       return;
/*     */     } 
/* 374 */     JsonSerializer<Object> ser = this._valueSerializer;
/* 375 */     if (ser == null) {
/* 376 */       ser = _findCachedSerializer(provider, value.getClass());
/*     */     }
/* 378 */     if (this._valueTypeSerializer != null) {
/* 379 */       ser.serializeWithType(value, g, provider, this._valueTypeSerializer);
/*     */     } else {
/* 381 */       ser.serialize(value, g, provider);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeWithType(T ref, JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/* 390 */     Object value = _getReferencedIfPresent(ref);
/* 391 */     if (value == null) {
/* 392 */       if (this._unwrapper == null) {
/* 393 */         provider.defaultSerializeNull(g);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 408 */     JsonSerializer<Object> ser = this._valueSerializer;
/* 409 */     if (ser == null) {
/* 410 */       ser = _findCachedSerializer(provider, value.getClass());
/*     */     }
/* 412 */     ser.serializeWithType(value, g, provider, typeSer);
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
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 425 */     JsonSerializer<?> ser = this._valueSerializer;
/* 426 */     if (ser == null) {
/* 427 */       ser = _findSerializer(visitor.getProvider(), this._referredType, this._property);
/* 428 */       if (this._unwrapper != null) {
/* 429 */         ser = ser.unwrappingSerializer(this._unwrapper);
/*     */       }
/*     */     } 
/* 432 */     ser.acceptJsonFormatVisitor(visitor, this._referredType);
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
/*     */   private final JsonSerializer<Object> _findCachedSerializer(SerializerProvider provider, Class<?> rawType) throws JsonMappingException {
/* 448 */     JsonSerializer<Object> ser = this._dynamicSerializers.serializerFor(rawType);
/* 449 */     if (ser == null) {
/*     */ 
/*     */ 
/*     */       
/* 453 */       if (this._referredType.hasGenericTypes()) {
/*     */ 
/*     */         
/* 456 */         JavaType fullType = provider.constructSpecializedType(this._referredType, rawType);
/* 457 */         ser = provider.findValueSerializer(fullType, this._property);
/*     */       } else {
/* 459 */         ser = provider.findValueSerializer(rawType, this._property);
/*     */       } 
/* 461 */       if (this._unwrapper != null) {
/* 462 */         ser = ser.unwrappingSerializer(this._unwrapper);
/*     */       }
/* 464 */       this._dynamicSerializers = this._dynamicSerializers.newWith(rawType, ser);
/*     */     } 
/* 466 */     return ser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final JsonSerializer<Object> _findSerializer(SerializerProvider provider, JavaType type, BeanProperty prop) throws JsonMappingException {
/* 477 */     return provider.findValueSerializer(type, prop);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\ReferenceTypeSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */