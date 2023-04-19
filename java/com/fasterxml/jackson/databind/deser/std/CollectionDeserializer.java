/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.NullValueProvider;
/*     */ import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class CollectionDeserializer
/*     */   extends ContainerDeserializerBase<Collection<Object>>
/*     */   implements ContextualDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = -1L;
/*     */   protected final JsonDeserializer<Object> _valueDeserializer;
/*     */   protected final TypeDeserializer _valueTypeDeserializer;
/*     */   protected final ValueInstantiator _valueInstantiator;
/*     */   protected final JsonDeserializer<Object> _delegateDeserializer;
/*     */   
/*     */   public CollectionDeserializer(JavaType collectionType, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser, ValueInstantiator valueInstantiator) {
/*  73 */     this(collectionType, valueDeser, valueTypeDeser, valueInstantiator, (JsonDeserializer<Object>)null, (NullValueProvider)null, (Boolean)null);
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
/*     */   protected CollectionDeserializer(JavaType collectionType, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser, ValueInstantiator valueInstantiator, JsonDeserializer<Object> delegateDeser, NullValueProvider nuller, Boolean unwrapSingle) {
/*  86 */     super(collectionType, nuller, unwrapSingle);
/*  87 */     this._valueDeserializer = valueDeser;
/*  88 */     this._valueTypeDeserializer = valueTypeDeser;
/*  89 */     this._valueInstantiator = valueInstantiator;
/*  90 */     this._delegateDeserializer = delegateDeser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CollectionDeserializer(CollectionDeserializer src) {
/*  99 */     super(src);
/* 100 */     this._valueDeserializer = src._valueDeserializer;
/* 101 */     this._valueTypeDeserializer = src._valueTypeDeserializer;
/* 102 */     this._valueInstantiator = src._valueInstantiator;
/* 103 */     this._delegateDeserializer = src._delegateDeserializer;
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
/*     */   protected CollectionDeserializer withResolved(JsonDeserializer<?> dd, JsonDeserializer<?> vd, TypeDeserializer vtd, NullValueProvider nuller, Boolean unwrapSingle) {
/* 117 */     return new CollectionDeserializer(this._containerType, (JsonDeserializer)vd, vtd, this._valueInstantiator, (JsonDeserializer)dd, nuller, unwrapSingle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCachable() {
/* 127 */     return (this._valueDeserializer == null && this._valueTypeDeserializer == null && this._delegateDeserializer == null);
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
/*     */   public CollectionDeserializer createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
/* 149 */     JsonDeserializer<Object> delegateDeser = null;
/* 150 */     if (this._valueInstantiator != null) {
/* 151 */       if (this._valueInstantiator.canCreateUsingDelegate()) {
/* 152 */         JavaType delegateType = this._valueInstantiator.getDelegateType(ctxt.getConfig());
/* 153 */         if (delegateType == null) {
/* 154 */           ctxt.reportBadDefinition(this._containerType, String.format("Invalid delegate-creator definition for %s: value instantiator (%s) returned true for 'canCreateUsingDelegate()', but null for 'getDelegateType()'", new Object[] { this._containerType, this._valueInstantiator
/*     */ 
/*     */                   
/* 157 */                   .getClass().getName() }));
/*     */         }
/* 159 */         delegateDeser = findDeserializer(ctxt, delegateType, property);
/* 160 */       } else if (this._valueInstantiator.canCreateUsingArrayDelegate()) {
/* 161 */         JavaType delegateType = this._valueInstantiator.getArrayDelegateType(ctxt.getConfig());
/* 162 */         if (delegateType == null) {
/* 163 */           ctxt.reportBadDefinition(this._containerType, String.format("Invalid delegate-creator definition for %s: value instantiator (%s) returned true for 'canCreateUsingArrayDelegate()', but null for 'getArrayDelegateType()'", new Object[] { this._containerType, this._valueInstantiator
/*     */ 
/*     */                   
/* 166 */                   .getClass().getName() }));
/*     */         }
/* 168 */         delegateDeser = findDeserializer(ctxt, delegateType, property);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 174 */     Boolean unwrapSingle = findFormatFeature(ctxt, property, Collection.class, JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
/*     */ 
/*     */     
/* 177 */     JsonDeserializer<?> valueDeser = this._valueDeserializer;
/*     */ 
/*     */     
/* 180 */     valueDeser = findConvertingContentDeserializer(ctxt, property, valueDeser);
/* 181 */     JavaType vt = this._containerType.getContentType();
/* 182 */     if (valueDeser == null) {
/* 183 */       valueDeser = ctxt.findContextualValueDeserializer(vt, property);
/*     */     } else {
/* 185 */       valueDeser = ctxt.handleSecondaryContextualization(valueDeser, property, vt);
/*     */     } 
/*     */     
/* 188 */     TypeDeserializer valueTypeDeser = this._valueTypeDeserializer;
/* 189 */     if (valueTypeDeser != null) {
/* 190 */       valueTypeDeser = valueTypeDeser.forProperty(property);
/*     */     }
/* 192 */     NullValueProvider nuller = findContentNullProvider(ctxt, property, valueDeser);
/* 193 */     if (unwrapSingle != this._unwrapSingle || nuller != this._nullProvider || delegateDeser != this._delegateDeserializer || valueDeser != this._valueDeserializer || valueTypeDeser != this._valueTypeDeserializer)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 199 */       return withResolved(delegateDeser, valueDeser, valueTypeDeser, nuller, unwrapSingle);
/*     */     }
/*     */     
/* 202 */     return this;
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
/* 213 */     return this._valueDeserializer;
/*     */   }
/*     */ 
/*     */   
/*     */   public ValueInstantiator getValueInstantiator() {
/* 218 */     return this._valueInstantiator;
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
/*     */   public Collection<Object> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 232 */     if (this._delegateDeserializer != null) {
/* 233 */       return (Collection<Object>)this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer
/* 234 */           .deserialize(p, ctxt));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 239 */     if (p.hasToken(JsonToken.VALUE_STRING)) {
/* 240 */       String str = p.getText();
/* 241 */       if (str.length() == 0) {
/* 242 */         return (Collection<Object>)this._valueInstantiator.createFromString(ctxt, str);
/*     */       }
/*     */     } 
/* 245 */     return deserialize(p, ctxt, createDefaultInstance(ctxt));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Collection<Object> createDefaultInstance(DeserializationContext ctxt) throws IOException {
/* 255 */     return (Collection<Object>)this._valueInstantiator.createUsingDefault(ctxt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<Object> deserialize(JsonParser p, DeserializationContext ctxt, Collection<Object> result) throws IOException {
/* 264 */     if (!p.isExpectedStartArrayToken()) {
/* 265 */       return handleNonArray(p, ctxt, result);
/*     */     }
/*     */     
/* 268 */     p.setCurrentValue(result);
/*     */     
/* 270 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/*     */     
/* 272 */     if (valueDes.getObjectIdReader() != null) {
/* 273 */       return _deserializeWithObjectId(p, ctxt, result);
/*     */     }
/* 275 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/*     */     JsonToken t;
/* 277 */     while ((t = p.nextToken()) != JsonToken.END_ARRAY) {
/*     */       try {
/*     */         Object value;
/* 280 */         if (t == JsonToken.VALUE_NULL) {
/* 281 */           if (this._skipNullValues) {
/*     */             continue;
/*     */           }
/* 284 */           value = this._nullProvider.getNullValue(ctxt);
/* 285 */         } else if (typeDeser == null) {
/* 286 */           value = valueDes.deserialize(p, ctxt);
/*     */         } else {
/* 288 */           value = valueDes.deserializeWithType(p, ctxt, typeDeser);
/*     */         } 
/* 290 */         result.add(value);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 297 */       catch (Exception e) {
/* 298 */         boolean wrap = (ctxt == null || ctxt.isEnabled(DeserializationFeature.WRAP_EXCEPTIONS));
/* 299 */         if (!wrap) {
/* 300 */           ClassUtil.throwIfRTE(e);
/*     */         }
/* 302 */         throw JsonMappingException.wrapWithPath(e, result, result.size());
/*     */       } 
/*     */     } 
/* 305 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
/* 314 */     return typeDeserializer.deserializeTypedFromArray(p, ctxt);
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
/*     */   protected final Collection<Object> handleNonArray(JsonParser p, DeserializationContext ctxt, Collection<Object> result) throws IOException {
/*     */     Object value;
/* 330 */     boolean canWrap = (this._unwrapSingle == Boolean.TRUE || (this._unwrapSingle == null && ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)));
/* 331 */     if (!canWrap) {
/* 332 */       return (Collection<Object>)ctxt.handleUnexpectedToken(this._containerType, p);
/*     */     }
/* 334 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/* 335 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/* 336 */     JsonToken t = p.getCurrentToken();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 341 */       if (t == JsonToken.VALUE_NULL) {
/*     */         
/* 343 */         if (this._skipNullValues) {
/* 344 */           return result;
/*     */         }
/* 346 */         value = this._nullProvider.getNullValue(ctxt);
/* 347 */       } else if (typeDeser == null) {
/* 348 */         value = valueDes.deserialize(p, ctxt);
/*     */       } else {
/* 350 */         value = valueDes.deserializeWithType(p, ctxt, typeDeser);
/*     */       } 
/* 352 */     } catch (Exception e) {
/*     */       
/* 354 */       throw JsonMappingException.wrapWithPath(e, Object.class, result.size());
/*     */     } 
/* 356 */     result.add(value);
/* 357 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Collection<Object> _deserializeWithObjectId(JsonParser p, DeserializationContext ctxt, Collection<Object> result) throws IOException {
/* 365 */     if (!p.isExpectedStartArrayToken()) {
/* 366 */       return handleNonArray(p, ctxt, result);
/*     */     }
/*     */     
/* 369 */     p.setCurrentValue(result);
/*     */     
/* 371 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/* 372 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/*     */     
/* 374 */     CollectionReferringAccumulator referringAccumulator = new CollectionReferringAccumulator(this._containerType.getContentType().getRawClass(), result);
/*     */     
/*     */     JsonToken t;
/* 377 */     while ((t = p.nextToken()) != JsonToken.END_ARRAY) {
/*     */       try {
/*     */         Object value;
/* 380 */         if (t == JsonToken.VALUE_NULL) {
/* 381 */           if (this._skipNullValues) {
/*     */             continue;
/*     */           }
/* 384 */           value = this._nullProvider.getNullValue(ctxt);
/* 385 */         } else if (typeDeser == null) {
/* 386 */           value = valueDes.deserialize(p, ctxt);
/*     */         } else {
/* 388 */           value = valueDes.deserializeWithType(p, ctxt, typeDeser);
/*     */         } 
/* 390 */         referringAccumulator.add(value);
/* 391 */       } catch (UnresolvedForwardReference reference) {
/* 392 */         ReadableObjectId.Referring ref = referringAccumulator.handleUnresolvedReference(reference);
/* 393 */         reference.getRoid().appendReferring(ref);
/* 394 */       } catch (Exception e) {
/* 395 */         boolean wrap = (ctxt == null || ctxt.isEnabled(DeserializationFeature.WRAP_EXCEPTIONS));
/* 396 */         if (!wrap) {
/* 397 */           ClassUtil.throwIfRTE(e);
/*     */         }
/* 399 */         throw JsonMappingException.wrapWithPath(e, result, result.size());
/*     */       } 
/*     */     } 
/* 402 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class CollectionReferringAccumulator
/*     */   {
/*     */     private final Class<?> _elementType;
/*     */ 
/*     */     
/*     */     private final Collection<Object> _result;
/*     */ 
/*     */     
/* 416 */     private List<CollectionDeserializer.CollectionReferring> _accumulator = new ArrayList<>();
/*     */     
/*     */     public CollectionReferringAccumulator(Class<?> elementType, Collection<Object> result) {
/* 419 */       this._elementType = elementType;
/* 420 */       this._result = result;
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(Object value) {
/* 425 */       if (this._accumulator.isEmpty()) {
/* 426 */         this._result.add(value);
/*     */       } else {
/* 428 */         CollectionDeserializer.CollectionReferring ref = this._accumulator.get(this._accumulator.size() - 1);
/* 429 */         ref.next.add(value);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public ReadableObjectId.Referring handleUnresolvedReference(UnresolvedForwardReference reference) {
/* 435 */       CollectionDeserializer.CollectionReferring id = new CollectionDeserializer.CollectionReferring(this, reference, this._elementType);
/* 436 */       this._accumulator.add(id);
/* 437 */       return id;
/*     */     }
/*     */ 
/*     */     
/*     */     public void resolveForwardReference(Object id, Object value) throws IOException {
/* 442 */       Iterator<CollectionDeserializer.CollectionReferring> iterator = this._accumulator.iterator();
/*     */ 
/*     */ 
/*     */       
/* 446 */       Collection<Object> previous = this._result;
/* 447 */       while (iterator.hasNext()) {
/* 448 */         CollectionDeserializer.CollectionReferring ref = iterator.next();
/* 449 */         if (ref.hasId(id)) {
/* 450 */           iterator.remove();
/* 451 */           previous.add(value);
/* 452 */           previous.addAll(ref.next);
/*     */           return;
/*     */         } 
/* 455 */         previous = ref.next;
/*     */       } 
/*     */       
/* 458 */       throw new IllegalArgumentException("Trying to resolve a forward reference with id [" + id + "] that wasn't previously seen as unresolved.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class CollectionReferring
/*     */     extends ReadableObjectId.Referring
/*     */   {
/*     */     private final CollectionDeserializer.CollectionReferringAccumulator _parent;
/*     */ 
/*     */     
/* 470 */     public final List<Object> next = new ArrayList();
/*     */ 
/*     */ 
/*     */     
/*     */     CollectionReferring(CollectionDeserializer.CollectionReferringAccumulator parent, UnresolvedForwardReference reference, Class<?> contentType) {
/* 475 */       super(reference, contentType);
/* 476 */       this._parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void handleResolvedForwardReference(Object id, Object value) throws IOException {
/* 481 */       this._parent.resolveForwardReference(id, value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\std\CollectionDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */