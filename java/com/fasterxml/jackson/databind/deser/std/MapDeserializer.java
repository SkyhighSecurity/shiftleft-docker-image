/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualKeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.NullValueProvider;
/*     */ import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.ArrayBuilders;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class MapDeserializer
/*     */   extends ContainerDeserializerBase<Map<Object, Object>>
/*     */   implements ContextualDeserializer, ResolvableDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final KeyDeserializer _keyDeserializer;
/*     */   protected boolean _standardStringKey;
/*     */   protected final JsonDeserializer<Object> _valueDeserializer;
/*     */   protected final TypeDeserializer _valueTypeDeserializer;
/*     */   protected final ValueInstantiator _valueInstantiator;
/*     */   protected JsonDeserializer<Object> _delegateDeserializer;
/*     */   protected PropertyBasedCreator _propertyBasedCreator;
/*     */   protected final boolean _hasDefaultCreator;
/*     */   protected Set<String> _ignorableProperties;
/*     */   
/*     */   public MapDeserializer(JavaType mapType, ValueInstantiator valueInstantiator, KeyDeserializer keyDeser, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser) {
/*  99 */     super(mapType, (NullValueProvider)null, (Boolean)null);
/* 100 */     this._keyDeserializer = keyDeser;
/* 101 */     this._valueDeserializer = valueDeser;
/* 102 */     this._valueTypeDeserializer = valueTypeDeser;
/* 103 */     this._valueInstantiator = valueInstantiator;
/* 104 */     this._hasDefaultCreator = valueInstantiator.canCreateUsingDefault();
/* 105 */     this._delegateDeserializer = null;
/* 106 */     this._propertyBasedCreator = null;
/* 107 */     this._standardStringKey = _isStdKeyDeser(mapType, keyDeser);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MapDeserializer(MapDeserializer src) {
/* 116 */     super(src);
/* 117 */     this._keyDeserializer = src._keyDeserializer;
/* 118 */     this._valueDeserializer = src._valueDeserializer;
/* 119 */     this._valueTypeDeserializer = src._valueTypeDeserializer;
/* 120 */     this._valueInstantiator = src._valueInstantiator;
/* 121 */     this._propertyBasedCreator = src._propertyBasedCreator;
/* 122 */     this._delegateDeserializer = src._delegateDeserializer;
/* 123 */     this._hasDefaultCreator = src._hasDefaultCreator;
/*     */     
/* 125 */     this._ignorableProperties = src._ignorableProperties;
/*     */     
/* 127 */     this._standardStringKey = src._standardStringKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MapDeserializer(MapDeserializer src, KeyDeserializer keyDeser, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser, NullValueProvider nuller, Set<String> ignorable) {
/* 136 */     super(src, nuller, src._unwrapSingle);
/* 137 */     this._keyDeserializer = keyDeser;
/* 138 */     this._valueDeserializer = valueDeser;
/* 139 */     this._valueTypeDeserializer = valueTypeDeser;
/* 140 */     this._valueInstantiator = src._valueInstantiator;
/* 141 */     this._propertyBasedCreator = src._propertyBasedCreator;
/* 142 */     this._delegateDeserializer = src._delegateDeserializer;
/* 143 */     this._hasDefaultCreator = src._hasDefaultCreator;
/* 144 */     this._ignorableProperties = ignorable;
/*     */     
/* 146 */     this._standardStringKey = _isStdKeyDeser(this._containerType, keyDeser);
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
/*     */   protected MapDeserializer withResolved(KeyDeserializer keyDeser, TypeDeserializer valueTypeDeser, JsonDeserializer<?> valueDeser, NullValueProvider nuller, Set<String> ignorable) {
/* 160 */     if (this._keyDeserializer == keyDeser && this._valueDeserializer == valueDeser && this._valueTypeDeserializer == valueTypeDeser && this._nullProvider == nuller && this._ignorableProperties == ignorable)
/*     */     {
/*     */       
/* 163 */       return this;
/*     */     }
/* 165 */     return new MapDeserializer(this, keyDeser, (JsonDeserializer)valueDeser, valueTypeDeser, nuller, ignorable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean _isStdKeyDeser(JavaType mapType, KeyDeserializer keyDeser) {
/* 176 */     if (keyDeser == null) {
/* 177 */       return true;
/*     */     }
/* 179 */     JavaType keyType = mapType.getKeyType();
/* 180 */     if (keyType == null) {
/* 181 */       return true;
/*     */     }
/* 183 */     Class<?> rawKeyType = keyType.getRawClass();
/* 184 */     return ((rawKeyType == String.class || rawKeyType == Object.class) && 
/* 185 */       isDefaultKeyDeserializer(keyDeser));
/*     */   }
/*     */   
/*     */   public void setIgnorableProperties(String[] ignorable) {
/* 189 */     this
/* 190 */       ._ignorableProperties = (ignorable == null || ignorable.length == 0) ? null : ArrayBuilders.arrayToSet((Object[])ignorable);
/*     */   }
/*     */   
/*     */   public void setIgnorableProperties(Set<String> ignorable) {
/* 194 */     this._ignorableProperties = (ignorable == null || ignorable.size() == 0) ? null : ignorable;
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
/* 208 */     if (this._valueInstantiator.canCreateUsingDelegate()) {
/* 209 */       JavaType delegateType = this._valueInstantiator.getDelegateType(ctxt.getConfig());
/* 210 */       if (delegateType == null) {
/* 211 */         ctxt.reportBadDefinition(this._containerType, String.format("Invalid delegate-creator definition for %s: value instantiator (%s) returned true for 'canCreateUsingDelegate()', but null for 'getDelegateType()'", new Object[] { this._containerType, this._valueInstantiator
/*     */ 
/*     */                 
/* 214 */                 .getClass().getName() }));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 219 */       this._delegateDeserializer = findDeserializer(ctxt, delegateType, null);
/* 220 */     } else if (this._valueInstantiator.canCreateUsingArrayDelegate()) {
/* 221 */       JavaType delegateType = this._valueInstantiator.getArrayDelegateType(ctxt.getConfig());
/* 222 */       if (delegateType == null) {
/* 223 */         ctxt.reportBadDefinition(this._containerType, String.format("Invalid delegate-creator definition for %s: value instantiator (%s) returned true for 'canCreateUsingArrayDelegate()', but null for 'getArrayDelegateType()'", new Object[] { this._containerType, this._valueInstantiator
/*     */ 
/*     */                 
/* 226 */                 .getClass().getName() }));
/*     */       }
/* 228 */       this._delegateDeserializer = findDeserializer(ctxt, delegateType, null);
/*     */     } 
/* 230 */     if (this._valueInstantiator.canCreateFromObjectWith()) {
/* 231 */       SettableBeanProperty[] creatorProps = this._valueInstantiator.getFromObjectArguments(ctxt.getConfig());
/* 232 */       this._propertyBasedCreator = PropertyBasedCreator.construct(ctxt, this._valueInstantiator, creatorProps, ctxt
/* 233 */           .isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES));
/*     */     } 
/* 235 */     this._standardStringKey = _isStdKeyDeser(this._containerType, this._keyDeserializer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
/* 246 */     KeyDeserializer keyDeser = this._keyDeserializer;
/* 247 */     if (keyDeser == null) {
/* 248 */       keyDeser = ctxt.findKeyDeserializer(this._containerType.getKeyType(), property);
/*     */     }
/* 250 */     else if (keyDeser instanceof ContextualKeyDeserializer) {
/* 251 */       keyDeser = ((ContextualKeyDeserializer)keyDeser).createContextual(ctxt, property);
/*     */     } 
/*     */ 
/*     */     
/* 255 */     JsonDeserializer<?> valueDeser = this._valueDeserializer;
/*     */     
/* 257 */     if (property != null) {
/* 258 */       valueDeser = findConvertingContentDeserializer(ctxt, property, valueDeser);
/*     */     }
/* 260 */     JavaType vt = this._containerType.getContentType();
/* 261 */     if (valueDeser == null) {
/* 262 */       valueDeser = ctxt.findContextualValueDeserializer(vt, property);
/*     */     } else {
/* 264 */       valueDeser = ctxt.handleSecondaryContextualization(valueDeser, property, vt);
/*     */     } 
/* 266 */     TypeDeserializer vtd = this._valueTypeDeserializer;
/* 267 */     if (vtd != null) {
/* 268 */       vtd = vtd.forProperty(property);
/*     */     }
/* 270 */     Set<String> ignored = this._ignorableProperties;
/* 271 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/* 272 */     if (_neitherNull(intr, property)) {
/* 273 */       AnnotatedMember member = property.getMember();
/* 274 */       if (member != null) {
/* 275 */         JsonIgnoreProperties.Value ignorals = intr.findPropertyIgnorals((Annotated)member);
/* 276 */         if (ignorals != null) {
/* 277 */           Set<String> ignoresToAdd = ignorals.findIgnoredForDeserialization();
/* 278 */           if (!ignoresToAdd.isEmpty()) {
/* 279 */             ignored = (ignored == null) ? new HashSet<>() : new HashSet<>(ignored);
/* 280 */             for (String str : ignoresToAdd) {
/* 281 */               ignored.add(str);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 287 */     return withResolved(keyDeser, vtd, valueDeser, 
/* 288 */         findContentNullProvider(ctxt, property, valueDeser), ignored);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonDeserializer<Object> getContentDeserializer() {
/* 299 */     return this._valueDeserializer;
/*     */   }
/*     */ 
/*     */   
/*     */   public ValueInstantiator getValueInstantiator() {
/* 304 */     return this._valueInstantiator;
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
/*     */   public boolean isCachable() {
/* 330 */     return (this._valueDeserializer == null && this._keyDeserializer == null && this._valueTypeDeserializer == null && this._ignorableProperties == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<Object, Object> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 340 */     if (this._propertyBasedCreator != null) {
/* 341 */       return _deserializeUsingCreator(p, ctxt);
/*     */     }
/* 343 */     if (this._delegateDeserializer != null) {
/* 344 */       return (Map<Object, Object>)this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer
/* 345 */           .deserialize(p, ctxt));
/*     */     }
/* 347 */     if (!this._hasDefaultCreator) {
/* 348 */       return (Map<Object, Object>)ctxt.handleMissingInstantiator(getMapClass(), 
/* 349 */           getValueInstantiator(), p, "no default constructor found", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/* 353 */     JsonToken t = p.getCurrentToken();
/* 354 */     if (t != JsonToken.START_OBJECT && t != JsonToken.FIELD_NAME && t != JsonToken.END_OBJECT) {
/*     */       
/* 356 */       if (t == JsonToken.VALUE_STRING) {
/* 357 */         return (Map<Object, Object>)this._valueInstantiator.createFromString(ctxt, p.getText());
/*     */       }
/*     */       
/* 360 */       return _deserializeFromEmpty(p, ctxt);
/*     */     } 
/* 362 */     Map<Object, Object> result = (Map<Object, Object>)this._valueInstantiator.createUsingDefault(ctxt);
/* 363 */     if (this._standardStringKey) {
/* 364 */       _readAndBindStringKeyMap(p, ctxt, result);
/* 365 */       return result;
/*     */     } 
/* 367 */     _readAndBind(p, ctxt, result);
/* 368 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<Object, Object> deserialize(JsonParser p, DeserializationContext ctxt, Map<Object, Object> result) throws IOException {
/* 378 */     p.setCurrentValue(result);
/*     */ 
/*     */     
/* 381 */     JsonToken t = p.getCurrentToken();
/* 382 */     if (t != JsonToken.START_OBJECT && t != JsonToken.FIELD_NAME) {
/* 383 */       return (Map<Object, Object>)ctxt.handleUnexpectedToken(getMapClass(), p);
/*     */     }
/*     */     
/* 386 */     if (this._standardStringKey) {
/* 387 */       _readAndUpdateStringKeyMap(p, ctxt, result);
/* 388 */       return result;
/*     */     } 
/* 390 */     _readAndUpdate(p, ctxt, result);
/* 391 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
/* 400 */     return typeDeserializer.deserializeTypedFromObject(p, ctxt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class<?> getMapClass() {
/* 410 */     return this._containerType.getRawClass();
/*     */   } public JavaType getValueType() {
/* 412 */     return this._containerType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void _readAndBind(JsonParser p, DeserializationContext ctxt, Map<Object, Object> result) throws IOException {
/* 423 */     KeyDeserializer keyDes = this._keyDeserializer;
/* 424 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/* 425 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/*     */     
/* 427 */     MapReferringAccumulator referringAccumulator = null;
/* 428 */     boolean useObjectId = (valueDes.getObjectIdReader() != null);
/* 429 */     if (useObjectId) {
/* 430 */       referringAccumulator = new MapReferringAccumulator(this._containerType.getContentType().getRawClass(), result);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 435 */     if (p.isExpectedStartObjectToken()) {
/* 436 */       str = p.nextFieldName();
/*     */     } else {
/* 438 */       JsonToken t = p.getCurrentToken();
/* 439 */       if (t != JsonToken.FIELD_NAME) {
/* 440 */         if (t == JsonToken.END_OBJECT) {
/*     */           return;
/*     */         }
/* 443 */         ctxt.reportWrongTokenException(this, JsonToken.FIELD_NAME, null, new Object[0]);
/*     */       } 
/* 445 */       str = p.getCurrentName();
/*     */     } 
/*     */     String str;
/* 448 */     for (; str != null; str = p.nextFieldName()) {
/* 449 */       Object key = keyDes.deserializeKey(str, ctxt);
/*     */       
/* 451 */       JsonToken t = p.nextToken();
/* 452 */       if (this._ignorableProperties != null && this._ignorableProperties.contains(str)) {
/* 453 */         p.skipChildren();
/*     */         
/*     */         continue;
/*     */       } 
/*     */       try {
/*     */         Object value;
/* 459 */         if (t == JsonToken.VALUE_NULL) {
/* 460 */           if (this._skipNullValues) {
/*     */             continue;
/*     */           }
/* 463 */           value = this._nullProvider.getNullValue(ctxt);
/* 464 */         } else if (typeDeser == null) {
/* 465 */           value = valueDes.deserialize(p, ctxt);
/*     */         } else {
/* 467 */           value = valueDes.deserializeWithType(p, ctxt, typeDeser);
/*     */         } 
/* 469 */         if (useObjectId) {
/* 470 */           referringAccumulator.put(key, value);
/*     */         } else {
/* 472 */           result.put(key, value);
/*     */         } 
/* 474 */       } catch (UnresolvedForwardReference reference) {
/* 475 */         handleUnresolvedReference(ctxt, referringAccumulator, key, reference);
/* 476 */       } catch (Exception e) {
/* 477 */         wrapAndThrow(e, result, str);
/*     */       } 
/*     */       continue;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void _readAndBindStringKeyMap(JsonParser p, DeserializationContext ctxt, Map<Object, Object> result) throws IOException {
/* 490 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/* 491 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/* 492 */     MapReferringAccumulator referringAccumulator = null;
/* 493 */     boolean useObjectId = (valueDes.getObjectIdReader() != null);
/* 494 */     if (useObjectId) {
/* 495 */       referringAccumulator = new MapReferringAccumulator(this._containerType.getContentType().getRawClass(), result);
/*     */     }
/*     */ 
/*     */     
/* 499 */     if (p.isExpectedStartObjectToken()) {
/* 500 */       str = p.nextFieldName();
/*     */     } else {
/* 502 */       JsonToken t = p.getCurrentToken();
/* 503 */       if (t == JsonToken.END_OBJECT) {
/*     */         return;
/*     */       }
/* 506 */       if (t != JsonToken.FIELD_NAME) {
/* 507 */         ctxt.reportWrongTokenException(this, JsonToken.FIELD_NAME, null, new Object[0]);
/*     */       }
/* 509 */       str = p.getCurrentName();
/*     */     } 
/*     */     String str;
/* 512 */     for (; str != null; str = p.nextFieldName()) {
/* 513 */       JsonToken t = p.nextToken();
/* 514 */       if (this._ignorableProperties != null && this._ignorableProperties.contains(str)) {
/* 515 */         p.skipChildren();
/*     */         
/*     */         continue;
/*     */       } 
/*     */       try {
/*     */         Object value;
/* 521 */         if (t == JsonToken.VALUE_NULL) {
/* 522 */           if (this._skipNullValues) {
/*     */             continue;
/*     */           }
/* 525 */           value = this._nullProvider.getNullValue(ctxt);
/* 526 */         } else if (typeDeser == null) {
/* 527 */           value = valueDes.deserialize(p, ctxt);
/*     */         } else {
/* 529 */           value = valueDes.deserializeWithType(p, ctxt, typeDeser);
/*     */         } 
/* 531 */         if (useObjectId) {
/* 532 */           referringAccumulator.put(str, value);
/*     */         } else {
/* 534 */           result.put(str, value);
/*     */         } 
/* 536 */       } catch (UnresolvedForwardReference reference) {
/* 537 */         handleUnresolvedReference(ctxt, referringAccumulator, str, reference);
/* 538 */       } catch (Exception e) {
/* 539 */         wrapAndThrow(e, result, str);
/*     */       } 
/*     */       continue;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<Object, Object> _deserializeUsingCreator(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 548 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/*     */     
/* 550 */     PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, null);
/*     */     
/* 552 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/* 553 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/*     */ 
/*     */     
/* 556 */     if (p.isExpectedStartObjectToken()) {
/* 557 */       str = p.nextFieldName();
/* 558 */     } else if (p.hasToken(JsonToken.FIELD_NAME)) {
/* 559 */       str = p.getCurrentName();
/*     */     } else {
/* 561 */       str = null;
/*     */     } 
/*     */     String str;
/* 564 */     for (; str != null; str = p.nextFieldName()) {
/* 565 */       Object value; JsonToken t = p.nextToken();
/* 566 */       if (this._ignorableProperties != null && this._ignorableProperties.contains(str)) {
/* 567 */         p.skipChildren();
/*     */         
/*     */         continue;
/*     */       } 
/* 571 */       SettableBeanProperty prop = creator.findCreatorProperty(str);
/* 572 */       if (prop != null) {
/*     */         
/* 574 */         if (buffer.assignParameter(prop, prop.deserialize(p, ctxt))) {
/* 575 */           Map<Object, Object> result; p.nextToken();
/*     */           
/*     */           try {
/* 578 */             result = (Map<Object, Object>)creator.build(ctxt, buffer);
/* 579 */           } catch (Exception e) {
/* 580 */             return (Map<Object, Object>)wrapAndThrow(e, this._containerType.getRawClass(), str);
/*     */           } 
/* 582 */           _readAndBind(p, ctxt, result);
/* 583 */           return result;
/*     */         } 
/*     */         
/*     */         continue;
/*     */       } 
/* 588 */       Object actualKey = this._keyDeserializer.deserializeKey(str, ctxt);
/*     */ 
/*     */       
/*     */       try {
/* 592 */         if (t == JsonToken.VALUE_NULL) {
/* 593 */           if (this._skipNullValues) {
/*     */             continue;
/*     */           }
/* 596 */           value = this._nullProvider.getNullValue(ctxt);
/* 597 */         } else if (typeDeser == null) {
/* 598 */           value = valueDes.deserialize(p, ctxt);
/*     */         } else {
/* 600 */           value = valueDes.deserializeWithType(p, ctxt, typeDeser);
/*     */         } 
/* 602 */       } catch (Exception e) {
/* 603 */         wrapAndThrow(e, this._containerType.getRawClass(), str);
/* 604 */         return null;
/*     */       } 
/* 606 */       buffer.bufferMapProperty(actualKey, value);
/*     */       
/*     */       continue;
/*     */     } 
/*     */     try {
/* 611 */       return (Map<Object, Object>)creator.build(ctxt, buffer);
/* 612 */     } catch (Exception e) {
/* 613 */       wrapAndThrow(e, this._containerType.getRawClass(), str);
/* 614 */       return null;
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
/*     */   protected final void _readAndUpdate(JsonParser p, DeserializationContext ctxt, Map<Object, Object> result) throws IOException {
/* 630 */     KeyDeserializer keyDes = this._keyDeserializer;
/* 631 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/* 632 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 638 */     if (p.isExpectedStartObjectToken()) {
/* 639 */       keyStr = p.nextFieldName();
/*     */     } else {
/* 641 */       JsonToken t = p.getCurrentToken();
/* 642 */       if (t == JsonToken.END_OBJECT) {
/*     */         return;
/*     */       }
/* 645 */       if (t != JsonToken.FIELD_NAME) {
/* 646 */         ctxt.reportWrongTokenException(this, JsonToken.FIELD_NAME, null, new Object[0]);
/*     */       }
/* 648 */       keyStr = p.getCurrentName();
/*     */     } 
/*     */     String keyStr;
/* 651 */     for (; keyStr != null; keyStr = p.nextFieldName()) {
/* 652 */       Object key = keyDes.deserializeKey(keyStr, ctxt);
/*     */       
/* 654 */       JsonToken t = p.nextToken();
/* 655 */       if (this._ignorableProperties != null && this._ignorableProperties.contains(keyStr)) {
/* 656 */         p.skipChildren();
/*     */       } else {
/*     */ 
/*     */         
/*     */         try {
/* 661 */           if (t == JsonToken.VALUE_NULL) {
/* 662 */             if (!this._skipNullValues)
/*     */             {
/*     */               
/* 665 */               result.put(key, this._nullProvider.getNullValue(ctxt));
/*     */             }
/*     */           } else {
/* 668 */             Object value, old = result.get(key);
/*     */             
/* 670 */             if (old != null) {
/* 671 */               if (typeDeser == null) {
/* 672 */                 value = valueDes.deserialize(p, ctxt, old);
/*     */               } else {
/* 674 */                 value = valueDes.deserializeWithType(p, ctxt, typeDeser, old);
/*     */               } 
/* 676 */             } else if (typeDeser == null) {
/* 677 */               value = valueDes.deserialize(p, ctxt);
/*     */             } else {
/* 679 */               value = valueDes.deserializeWithType(p, ctxt, typeDeser);
/*     */             } 
/* 681 */             if (value != old)
/* 682 */               result.put(key, value); 
/*     */           } 
/* 684 */         } catch (Exception e) {
/* 685 */           wrapAndThrow(e, result, keyStr);
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
/*     */   
/*     */   protected final void _readAndUpdateStringKeyMap(JsonParser p, DeserializationContext ctxt, Map<Object, Object> result) throws IOException {
/* 700 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/* 701 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 707 */     if (p.isExpectedStartObjectToken()) {
/* 708 */       key = p.nextFieldName();
/*     */     } else {
/* 710 */       JsonToken t = p.getCurrentToken();
/* 711 */       if (t == JsonToken.END_OBJECT) {
/*     */         return;
/*     */       }
/* 714 */       if (t != JsonToken.FIELD_NAME) {
/* 715 */         ctxt.reportWrongTokenException(this, JsonToken.FIELD_NAME, null, new Object[0]);
/*     */       }
/* 717 */       key = p.getCurrentName();
/*     */     } 
/*     */     String key;
/* 720 */     for (; key != null; key = p.nextFieldName()) {
/* 721 */       JsonToken t = p.nextToken();
/* 722 */       if (this._ignorableProperties != null && this._ignorableProperties.contains(key)) {
/* 723 */         p.skipChildren();
/*     */       } else {
/*     */ 
/*     */         
/*     */         try {
/* 728 */           if (t == JsonToken.VALUE_NULL) {
/* 729 */             if (!this._skipNullValues)
/*     */             {
/*     */               
/* 732 */               result.put(key, this._nullProvider.getNullValue(ctxt));
/*     */             }
/*     */           } else {
/* 735 */             Object value, old = result.get(key);
/*     */             
/* 737 */             if (old != null) {
/* 738 */               if (typeDeser == null) {
/* 739 */                 value = valueDes.deserialize(p, ctxt, old);
/*     */               } else {
/* 741 */                 value = valueDes.deserializeWithType(p, ctxt, typeDeser, old);
/*     */               } 
/* 743 */             } else if (typeDeser == null) {
/* 744 */               value = valueDes.deserialize(p, ctxt);
/*     */             } else {
/* 746 */               value = valueDes.deserializeWithType(p, ctxt, typeDeser);
/*     */             } 
/* 748 */             if (value != old)
/* 749 */               result.put(key, value); 
/*     */           } 
/* 751 */         } catch (Exception e) {
/* 752 */           wrapAndThrow(e, result, key);
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
/*     */ 
/*     */   
/*     */   private void handleUnresolvedReference(DeserializationContext ctxt, MapReferringAccumulator accumulator, Object key, UnresolvedForwardReference reference) throws JsonMappingException {
/* 768 */     if (accumulator == null) {
/* 769 */       ctxt.reportInputMismatch(this, "Unresolved forward reference but no identity info: " + reference, new Object[0]);
/*     */     }
/*     */     
/* 772 */     ReadableObjectId.Referring referring = accumulator.handleUnresolvedReference(reference, key);
/* 773 */     reference.getRoid().appendReferring(referring);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class MapReferringAccumulator
/*     */   {
/*     */     private final Class<?> _valueType;
/*     */     
/*     */     private Map<Object, Object> _result;
/* 782 */     private List<MapDeserializer.MapReferring> _accumulator = new ArrayList<>();
/*     */     
/*     */     public MapReferringAccumulator(Class<?> valueType, Map<Object, Object> result) {
/* 785 */       this._valueType = valueType;
/* 786 */       this._result = result;
/*     */     }
/*     */ 
/*     */     
/*     */     public void put(Object key, Object value) {
/* 791 */       if (this._accumulator.isEmpty()) {
/* 792 */         this._result.put(key, value);
/*     */       } else {
/* 794 */         MapDeserializer.MapReferring ref = this._accumulator.get(this._accumulator.size() - 1);
/* 795 */         ref.next.put(key, value);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public ReadableObjectId.Referring handleUnresolvedReference(UnresolvedForwardReference reference, Object key) {
/* 801 */       MapDeserializer.MapReferring id = new MapDeserializer.MapReferring(this, reference, this._valueType, key);
/* 802 */       this._accumulator.add(id);
/* 803 */       return id;
/*     */     }
/*     */ 
/*     */     
/*     */     public void resolveForwardReference(Object id, Object value) throws IOException {
/* 808 */       Iterator<MapDeserializer.MapReferring> iterator = this._accumulator.iterator();
/*     */ 
/*     */ 
/*     */       
/* 812 */       Map<Object, Object> previous = this._result;
/* 813 */       while (iterator.hasNext()) {
/* 814 */         MapDeserializer.MapReferring ref = iterator.next();
/* 815 */         if (ref.hasId(id)) {
/* 816 */           iterator.remove();
/* 817 */           previous.put(ref.key, value);
/* 818 */           previous.putAll(ref.next);
/*     */           return;
/*     */         } 
/* 821 */         previous = ref.next;
/*     */       } 
/*     */       
/* 824 */       throw new IllegalArgumentException("Trying to resolve a forward reference with id [" + id + "] that wasn't previously seen as unresolved.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class MapReferring
/*     */     extends ReadableObjectId.Referring
/*     */   {
/*     */     private final MapDeserializer.MapReferringAccumulator _parent;
/*     */ 
/*     */     
/* 837 */     public final Map<Object, Object> next = new LinkedHashMap<>();
/*     */     
/*     */     public final Object key;
/*     */ 
/*     */     
/*     */     MapReferring(MapDeserializer.MapReferringAccumulator parent, UnresolvedForwardReference ref, Class<?> valueType, Object key) {
/* 843 */       super(ref, valueType);
/* 844 */       this._parent = parent;
/* 845 */       this.key = key;
/*     */     }
/*     */ 
/*     */     
/*     */     public void handleResolvedForwardReference(Object id, Object value) throws IOException {
/* 850 */       this._parent.resolveForwardReference(id, value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\std\MapDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */