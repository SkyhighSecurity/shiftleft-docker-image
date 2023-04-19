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
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class StringCollectionDeserializer
/*     */   extends ContainerDeserializerBase<Collection<String>>
/*     */   implements ContextualDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final JsonDeserializer<String> _valueDeserializer;
/*     */   protected final ValueInstantiator _valueInstantiator;
/*     */   protected final JsonDeserializer<Object> _delegateDeserializer;
/*     */   
/*     */   public StringCollectionDeserializer(JavaType collectionType, JsonDeserializer<?> valueDeser, ValueInstantiator valueInstantiator) {
/*  60 */     this(collectionType, valueInstantiator, (JsonDeserializer<?>)null, valueDeser, (NullValueProvider)valueDeser, (Boolean)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StringCollectionDeserializer(JavaType collectionType, ValueInstantiator valueInstantiator, JsonDeserializer<?> delegateDeser, JsonDeserializer<?> valueDeser, NullValueProvider nuller, Boolean unwrapSingle) {
/*  69 */     super(collectionType, nuller, unwrapSingle);
/*  70 */     this._valueDeserializer = (JsonDeserializer)valueDeser;
/*  71 */     this._valueInstantiator = valueInstantiator;
/*  72 */     this._delegateDeserializer = (JsonDeserializer)delegateDeser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StringCollectionDeserializer withResolved(JsonDeserializer<?> delegateDeser, JsonDeserializer<?> valueDeser, NullValueProvider nuller, Boolean unwrapSingle) {
/*  79 */     if (this._unwrapSingle == unwrapSingle && this._nullProvider == nuller && this._valueDeserializer == valueDeser && this._delegateDeserializer == delegateDeser)
/*     */     {
/*  81 */       return this;
/*     */     }
/*  83 */     return new StringCollectionDeserializer(this._containerType, this._valueInstantiator, delegateDeser, valueDeser, nuller, unwrapSingle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCachable() {
/*  91 */     return (this._valueDeserializer == null && this._delegateDeserializer == null);
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
/* 104 */     JsonDeserializer<Object> delegate = null;
/* 105 */     if (this._valueInstantiator != null) {
/*     */       
/* 107 */       AnnotatedWithParams delegateCreator = this._valueInstantiator.getArrayDelegateCreator();
/* 108 */       if (delegateCreator != null) {
/* 109 */         JavaType delegateType = this._valueInstantiator.getArrayDelegateType(ctxt.getConfig());
/* 110 */         delegate = findDeserializer(ctxt, delegateType, property);
/* 111 */       } else if ((delegateCreator = this._valueInstantiator.getDelegateCreator()) != null) {
/* 112 */         JavaType delegateType = this._valueInstantiator.getDelegateType(ctxt.getConfig());
/* 113 */         delegate = findDeserializer(ctxt, delegateType, property);
/*     */       } 
/*     */     } 
/* 116 */     JsonDeserializer<?> valueDeser = this._valueDeserializer;
/* 117 */     JavaType valueType = this._containerType.getContentType();
/* 118 */     if (valueDeser == null) {
/*     */       
/* 120 */       valueDeser = findConvertingContentDeserializer(ctxt, property, valueDeser);
/* 121 */       if (valueDeser == null)
/*     */       {
/* 123 */         valueDeser = ctxt.findContextualValueDeserializer(valueType, property);
/*     */       }
/*     */     } else {
/* 126 */       valueDeser = ctxt.handleSecondaryContextualization(valueDeser, property, valueType);
/*     */     } 
/*     */ 
/*     */     
/* 130 */     Boolean unwrapSingle = findFormatFeature(ctxt, property, Collection.class, JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
/*     */     
/* 132 */     NullValueProvider nuller = findContentNullProvider(ctxt, property, valueDeser);
/* 133 */     if (isDefaultDeserializer(valueDeser)) {
/* 134 */       valueDeser = null;
/*     */     }
/* 136 */     return withResolved(delegate, valueDeser, nuller, unwrapSingle);
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
/*     */   public JsonDeserializer<Object> getContentDeserializer() {
/* 148 */     JsonDeserializer<?> deser = this._valueDeserializer;
/* 149 */     return (JsonDeserializer)deser;
/*     */   }
/*     */ 
/*     */   
/*     */   public ValueInstantiator getValueInstantiator() {
/* 154 */     return this._valueInstantiator;
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
/*     */   public Collection<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 168 */     if (this._delegateDeserializer != null) {
/* 169 */       return (Collection<String>)this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer
/* 170 */           .deserialize(p, ctxt));
/*     */     }
/* 172 */     Collection<String> result = (Collection<String>)this._valueInstantiator.createUsingDefault(ctxt);
/* 173 */     return deserialize(p, ctxt, result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<String> deserialize(JsonParser p, DeserializationContext ctxt, Collection<String> result) throws IOException {
/* 182 */     if (!p.isExpectedStartArrayToken()) {
/* 183 */       return handleNonArray(p, ctxt, result);
/*     */     }
/*     */     
/* 186 */     if (this._valueDeserializer != null) {
/* 187 */       return deserializeUsingCustom(p, ctxt, result, this._valueDeserializer);
/*     */     }
/*     */     
/*     */     try {
/*     */       while (true) {
/* 192 */         String value = p.nextTextValue();
/* 193 */         if (value != null) {
/* 194 */           result.add(value);
/*     */           continue;
/*     */         } 
/* 197 */         JsonToken t = p.getCurrentToken();
/* 198 */         if (t == JsonToken.END_ARRAY) {
/*     */           break;
/*     */         }
/* 201 */         if (t == JsonToken.VALUE_NULL) {
/* 202 */           if (this._skipNullValues) {
/*     */             continue;
/*     */           }
/* 205 */           value = (String)this._nullProvider.getNullValue(ctxt);
/*     */         } else {
/* 207 */           value = _parseString(p, ctxt);
/*     */         } 
/* 209 */         result.add(value);
/*     */       } 
/* 211 */     } catch (Exception e) {
/* 212 */       throw JsonMappingException.wrapWithPath(e, result, result.size());
/*     */     } 
/* 214 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Collection<String> deserializeUsingCustom(JsonParser p, DeserializationContext ctxt, Collection<String> result, JsonDeserializer<String> deser) throws IOException {
/*     */     while (true) {
/*     */       String value;
/* 227 */       if (p.nextTextValue() == null) {
/* 228 */         JsonToken t = p.getCurrentToken();
/* 229 */         if (t == JsonToken.END_ARRAY) {
/*     */           break;
/*     */         }
/*     */         
/* 233 */         if (t == JsonToken.VALUE_NULL) {
/* 234 */           if (this._skipNullValues) {
/*     */             continue;
/*     */           }
/* 237 */           value = (String)this._nullProvider.getNullValue(ctxt);
/*     */         } else {
/* 239 */           value = (String)deser.deserialize(p, ctxt);
/*     */         } 
/*     */       } else {
/* 242 */         value = (String)deser.deserialize(p, ctxt);
/*     */       } 
/* 244 */       result.add(value);
/*     */     } 
/* 246 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
/* 253 */     return typeDeserializer.deserializeTypedFromArray(p, ctxt);
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
/*     */   private final Collection<String> handleNonArray(JsonParser p, DeserializationContext ctxt, Collection<String> result) throws IOException {
/*     */     String value;
/* 268 */     boolean canWrap = (this._unwrapSingle == Boolean.TRUE || (this._unwrapSingle == null && ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)));
/* 269 */     if (!canWrap) {
/* 270 */       return (Collection<String>)ctxt.handleUnexpectedToken(this._containerType.getRawClass(), p);
/*     */     }
/*     */     
/* 273 */     JsonDeserializer<String> valueDes = this._valueDeserializer;
/* 274 */     JsonToken t = p.getCurrentToken();
/*     */ 
/*     */ 
/*     */     
/* 278 */     if (t == JsonToken.VALUE_NULL) {
/*     */       
/* 280 */       if (this._skipNullValues) {
/* 281 */         return result;
/*     */       }
/* 283 */       value = (String)this._nullProvider.getNullValue(ctxt);
/*     */     } else {
/* 285 */       value = (valueDes == null) ? _parseString(p, ctxt) : (String)valueDes.deserialize(p, ctxt);
/*     */     } 
/* 287 */     result.add(value);
/* 288 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\std\StringCollectionDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */