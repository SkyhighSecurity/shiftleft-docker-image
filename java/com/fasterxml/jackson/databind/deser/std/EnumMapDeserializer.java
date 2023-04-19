/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.NullValueProvider;
/*     */ import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.IOException;
/*     */ import java.util.EnumMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EnumMapDeserializer
/*     */   extends ContainerDeserializerBase<EnumMap<?, ?>>
/*     */   implements ContextualDeserializer, ResolvableDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final Class<?> _enumClass;
/*     */   protected KeyDeserializer _keyDeserializer;
/*     */   protected JsonDeserializer<Object> _valueDeserializer;
/*     */   protected final TypeDeserializer _valueTypeDeserializer;
/*     */   protected final ValueInstantiator _valueInstantiator;
/*     */   protected JsonDeserializer<Object> _delegateDeserializer;
/*     */   protected PropertyBasedCreator _propertyBasedCreator;
/*     */   
/*     */   public EnumMapDeserializer(JavaType mapType, ValueInstantiator valueInst, KeyDeserializer keyDeser, JsonDeserializer<?> valueDeser, TypeDeserializer vtd, NullValueProvider nuller) {
/*  77 */     super(mapType, nuller, (Boolean)null);
/*  78 */     this._enumClass = mapType.getKeyType().getRawClass();
/*  79 */     this._keyDeserializer = keyDeser;
/*  80 */     this._valueDeserializer = (JsonDeserializer)valueDeser;
/*  81 */     this._valueTypeDeserializer = vtd;
/*  82 */     this._valueInstantiator = valueInst;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected EnumMapDeserializer(EnumMapDeserializer base, KeyDeserializer keyDeser, JsonDeserializer<?> valueDeser, TypeDeserializer vtd, NullValueProvider nuller) {
/*  92 */     super(base, nuller, base._unwrapSingle);
/*  93 */     this._enumClass = base._enumClass;
/*  94 */     this._keyDeserializer = keyDeser;
/*  95 */     this._valueDeserializer = (JsonDeserializer)valueDeser;
/*  96 */     this._valueTypeDeserializer = vtd;
/*     */     
/*  98 */     this._valueInstantiator = base._valueInstantiator;
/*  99 */     this._delegateDeserializer = base._delegateDeserializer;
/* 100 */     this._propertyBasedCreator = base._propertyBasedCreator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public EnumMapDeserializer(JavaType mapType, KeyDeserializer keyDeser, JsonDeserializer<?> valueDeser, TypeDeserializer vtd) {
/* 107 */     this(mapType, (ValueInstantiator)null, keyDeser, valueDeser, vtd, (NullValueProvider)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EnumMapDeserializer withResolved(KeyDeserializer keyDeserializer, JsonDeserializer<?> valueDeserializer, TypeDeserializer valueTypeDeser, NullValueProvider nuller) {
/* 114 */     if (keyDeserializer == this._keyDeserializer && nuller == this._nullProvider && valueDeserializer == this._valueDeserializer && valueTypeDeser == this._valueTypeDeserializer)
/*     */     {
/* 116 */       return this;
/*     */     }
/* 118 */     return new EnumMapDeserializer(this, keyDeserializer, valueDeserializer, valueTypeDeser, nuller);
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
/*     */   public void resolve(DeserializationContext ctxt) throws JsonMappingException {
/* 132 */     if (this._valueInstantiator != null) {
/* 133 */       if (this._valueInstantiator.canCreateUsingDelegate()) {
/* 134 */         JavaType delegateType = this._valueInstantiator.getDelegateType(ctxt.getConfig());
/* 135 */         if (delegateType == null) {
/* 136 */           ctxt.reportBadDefinition(this._containerType, String.format("Invalid delegate-creator definition for %s: value instantiator (%s) returned true for 'canCreateUsingDelegate()', but null for 'getDelegateType()'", new Object[] { this._containerType, this._valueInstantiator
/*     */ 
/*     */                   
/* 139 */                   .getClass().getName() }));
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 145 */         this._delegateDeserializer = findDeserializer(ctxt, delegateType, null);
/* 146 */       } else if (this._valueInstantiator.canCreateUsingArrayDelegate()) {
/* 147 */         JavaType delegateType = this._valueInstantiator.getArrayDelegateType(ctxt.getConfig());
/* 148 */         if (delegateType == null) {
/* 149 */           ctxt.reportBadDefinition(this._containerType, String.format("Invalid delegate-creator definition for %s: value instantiator (%s) returned true for 'canCreateUsingArrayDelegate()', but null for 'getArrayDelegateType()'", new Object[] { this._containerType, this._valueInstantiator
/*     */ 
/*     */                   
/* 152 */                   .getClass().getName() }));
/*     */         }
/* 154 */         this._delegateDeserializer = findDeserializer(ctxt, delegateType, null);
/* 155 */       } else if (this._valueInstantiator.canCreateFromObjectWith()) {
/* 156 */         SettableBeanProperty[] creatorProps = this._valueInstantiator.getFromObjectArguments(ctxt.getConfig());
/* 157 */         this._propertyBasedCreator = PropertyBasedCreator.construct(ctxt, this._valueInstantiator, creatorProps, ctxt
/* 158 */             .isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES));
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
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
/* 173 */     KeyDeserializer keyDeser = this._keyDeserializer;
/* 174 */     if (keyDeser == null) {
/* 175 */       keyDeser = ctxt.findKeyDeserializer(this._containerType.getKeyType(), property);
/*     */     }
/* 177 */     JsonDeserializer<?> valueDeser = this._valueDeserializer;
/* 178 */     JavaType vt = this._containerType.getContentType();
/* 179 */     if (valueDeser == null) {
/* 180 */       valueDeser = ctxt.findContextualValueDeserializer(vt, property);
/*     */     } else {
/* 182 */       valueDeser = ctxt.handleSecondaryContextualization(valueDeser, property, vt);
/*     */     } 
/* 184 */     TypeDeserializer vtd = this._valueTypeDeserializer;
/* 185 */     if (vtd != null) {
/* 186 */       vtd = vtd.forProperty(property);
/*     */     }
/* 188 */     return withResolved(keyDeser, valueDeser, vtd, findContentNullProvider(ctxt, property, valueDeser));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCachable() {
/* 198 */     return (this._valueDeserializer == null && this._keyDeserializer == null && this._valueTypeDeserializer == null);
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
/*     */   public JsonDeserializer<Object> getContentDeserializer() {
/* 211 */     return this._valueDeserializer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getEmptyValue(DeserializationContext ctxt) throws JsonMappingException {
/* 217 */     return constructMap(ctxt);
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
/*     */   public EnumMap<?, ?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 230 */     if (this._propertyBasedCreator != null) {
/* 231 */       return _deserializeUsingProperties(p, ctxt);
/*     */     }
/* 233 */     if (this._delegateDeserializer != null) {
/* 234 */       return (EnumMap<?, ?>)this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer
/* 235 */           .deserialize(p, ctxt));
/*     */     }
/*     */     
/* 238 */     JsonToken t = p.getCurrentToken();
/* 239 */     if (t != JsonToken.START_OBJECT && t != JsonToken.FIELD_NAME && t != JsonToken.END_OBJECT) {
/*     */       
/* 241 */       if (t == JsonToken.VALUE_STRING) {
/* 242 */         return (EnumMap<?, ?>)this._valueInstantiator.createFromString(ctxt, p.getText());
/*     */       }
/*     */       
/* 245 */       return _deserializeFromEmpty(p, ctxt);
/*     */     } 
/* 247 */     EnumMap<?, ?> result = constructMap(ctxt);
/* 248 */     return deserialize(p, ctxt, result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EnumMap<?, ?> deserialize(JsonParser p, DeserializationContext ctxt, EnumMap<?, ?> result) throws IOException {
/* 257 */     p.setCurrentValue(result);
/*     */     
/* 259 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/* 260 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/*     */ 
/*     */     
/* 263 */     if (p.isExpectedStartObjectToken()) {
/* 264 */       str = p.nextFieldName();
/*     */     } else {
/* 266 */       JsonToken t = p.getCurrentToken();
/* 267 */       if (t != JsonToken.FIELD_NAME) {
/* 268 */         if (t == JsonToken.END_OBJECT) {
/* 269 */           return result;
/*     */         }
/* 271 */         ctxt.reportWrongTokenException(this, JsonToken.FIELD_NAME, null, new Object[0]);
/*     */       } 
/* 273 */       str = p.getCurrentName();
/*     */     } 
/*     */     String str;
/* 276 */     for (; str != null; str = p.nextFieldName()) {
/*     */       Object value;
/* 278 */       Enum<?> key = (Enum)this._keyDeserializer.deserializeKey(str, ctxt);
/* 279 */       JsonToken t = p.nextToken();
/* 280 */       if (key == null) {
/* 281 */         if (!ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) {
/* 282 */           return (EnumMap<?, ?>)ctxt.handleWeirdStringValue(this._enumClass, str, "value not one of declared Enum instance names for %s", new Object[] { this._containerType
/*     */                 
/* 284 */                 .getKeyType() });
/*     */         }
/*     */ 
/*     */         
/* 288 */         p.skipChildren();
/*     */ 
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 297 */         if (t == JsonToken.VALUE_NULL) {
/* 298 */           if (this._skipNullValues) {
/*     */             continue;
/*     */           }
/* 301 */           value = this._nullProvider.getNullValue(ctxt);
/* 302 */         } else if (typeDeser == null) {
/* 303 */           value = valueDes.deserialize(p, ctxt);
/*     */         } else {
/* 305 */           value = valueDes.deserializeWithType(p, ctxt, typeDeser);
/*     */         } 
/* 307 */       } catch (Exception e) {
/* 308 */         return (EnumMap<?, ?>)wrapAndThrow(e, result, str);
/*     */       } 
/* 310 */       result.put(key, value); continue;
/*     */     } 
/* 312 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
/* 321 */     return typeDeserializer.deserializeTypedFromObject(p, ctxt);
/*     */   }
/*     */   
/*     */   protected EnumMap<?, ?> constructMap(DeserializationContext ctxt) throws JsonMappingException {
/* 325 */     if (this._valueInstantiator == null) {
/* 326 */       return new EnumMap<>(this._enumClass);
/*     */     }
/*     */     try {
/* 329 */       if (!this._valueInstantiator.canCreateUsingDefault()) {
/* 330 */         return (EnumMap<?, ?>)ctxt.handleMissingInstantiator(handledType(), 
/* 331 */             getValueInstantiator(), null, "no default constructor found", new Object[0]);
/*     */       }
/*     */       
/* 334 */       return (EnumMap<?, ?>)this._valueInstantiator.createUsingDefault(ctxt);
/* 335 */     } catch (IOException e) {
/* 336 */       return (EnumMap<?, ?>)ClassUtil.throwAsMappingException(ctxt, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumMap<?, ?> _deserializeUsingProperties(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 342 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/*     */     
/* 344 */     PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, null);
/*     */ 
/*     */     
/* 347 */     if (p.isExpectedStartObjectToken()) {
/* 348 */       str = p.nextFieldName();
/* 349 */     } else if (p.hasToken(JsonToken.FIELD_NAME)) {
/* 350 */       str = p.getCurrentName();
/*     */     } else {
/* 352 */       str = null;
/*     */     } 
/*     */     String str;
/* 355 */     for (; str != null; str = p.nextFieldName()) {
/* 356 */       Object value; JsonToken t = p.nextToken();
/*     */       
/* 358 */       SettableBeanProperty prop = creator.findCreatorProperty(str);
/* 359 */       if (prop != null) {
/*     */         
/* 361 */         if (buffer.assignParameter(prop, prop.deserialize(p, ctxt))) {
/* 362 */           EnumMap<?, ?> result; p.nextToken();
/*     */           
/*     */           try {
/* 365 */             result = (EnumMap<?, ?>)creator.build(ctxt, buffer);
/* 366 */           } catch (Exception e) {
/* 367 */             return (EnumMap<?, ?>)wrapAndThrow(e, this._containerType.getRawClass(), str);
/*     */           } 
/* 369 */           return deserialize(p, ctxt, result);
/*     */         } 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 375 */       Enum<?> key = (Enum)this._keyDeserializer.deserializeKey(str, ctxt);
/* 376 */       if (key == null) {
/* 377 */         if (!ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) {
/* 378 */           return (EnumMap<?, ?>)ctxt.handleWeirdStringValue(this._enumClass, str, "value not one of declared Enum instance names for %s", new Object[] { this._containerType
/*     */                 
/* 380 */                 .getKeyType() });
/*     */         }
/*     */ 
/*     */         
/* 384 */         p.nextToken();
/* 385 */         p.skipChildren();
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/*     */       try {
/* 391 */         if (t == JsonToken.VALUE_NULL) {
/* 392 */           if (this._skipNullValues) {
/*     */             continue;
/*     */           }
/* 395 */           value = this._nullProvider.getNullValue(ctxt);
/* 396 */         } else if (this._valueTypeDeserializer == null) {
/* 397 */           value = this._valueDeserializer.deserialize(p, ctxt);
/*     */         } else {
/* 399 */           value = this._valueDeserializer.deserializeWithType(p, ctxt, this._valueTypeDeserializer);
/*     */         } 
/* 401 */       } catch (Exception e) {
/* 402 */         wrapAndThrow(e, this._containerType.getRawClass(), str);
/* 403 */         return null;
/*     */       } 
/* 405 */       buffer.bufferMapProperty(key, value);
/*     */       
/*     */       continue;
/*     */     } 
/*     */     try {
/* 410 */       return (EnumMap<?, ?>)creator.build(ctxt, buffer);
/* 411 */     } catch (Exception e) {
/* 412 */       wrapAndThrow(e, this._containerType.getRawClass(), str);
/* 413 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\std\EnumMapDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */