/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.util.EnumSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EnumSetDeserializer
/*     */   extends StdDeserializer<EnumSet<?>>
/*     */   implements ContextualDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final JavaType _enumType;
/*     */   protected final Class<Enum> _enumClass;
/*     */   protected JsonDeserializer<Enum<?>> _enumDeserializer;
/*     */   protected final Boolean _unwrapSingle;
/*     */   
/*     */   public EnumSetDeserializer(JavaType enumType, JsonDeserializer<?> deser) {
/*  49 */     super(EnumSet.class);
/*  50 */     this._enumType = enumType;
/*  51 */     this._enumClass = enumType.getRawClass();
/*     */     
/*  53 */     if (!this._enumClass.isEnum()) {
/*  54 */       throw new IllegalArgumentException("Type " + enumType + " not Java Enum type");
/*     */     }
/*  56 */     this._enumDeserializer = (JsonDeserializer)deser;
/*  57 */     this._unwrapSingle = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected EnumSetDeserializer(EnumSetDeserializer base, JsonDeserializer<?> deser, Boolean unwrapSingle) {
/*  66 */     super(base);
/*  67 */     this._enumType = base._enumType;
/*  68 */     this._enumClass = base._enumClass;
/*  69 */     this._enumDeserializer = (JsonDeserializer)deser;
/*  70 */     this._unwrapSingle = unwrapSingle;
/*     */   }
/*     */   
/*     */   public EnumSetDeserializer withDeserializer(JsonDeserializer<?> deser) {
/*  74 */     if (this._enumDeserializer == deser) {
/*  75 */       return this;
/*     */     }
/*  77 */     return new EnumSetDeserializer(this, deser, this._unwrapSingle);
/*     */   }
/*     */   
/*     */   public EnumSetDeserializer withResolved(JsonDeserializer<?> deser, Boolean unwrapSingle) {
/*  81 */     if (this._unwrapSingle == unwrapSingle && this._enumDeserializer == deser) {
/*  82 */       return this;
/*     */     }
/*  84 */     return new EnumSetDeserializer(this, deser, unwrapSingle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCachable() {
/*  94 */     if (this._enumType.getValueHandler() != null) {
/*  95 */       return false;
/*     */     }
/*  97 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean supportsUpdate(DeserializationConfig config) {
/* 102 */     return Boolean.TRUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
/* 109 */     Boolean unwrapSingle = findFormatFeature(ctxt, property, EnumSet.class, JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
/*     */     
/* 111 */     JsonDeserializer<?> deser = this._enumDeserializer;
/* 112 */     if (deser == null) {
/* 113 */       deser = ctxt.findContextualValueDeserializer(this._enumType, property);
/*     */     } else {
/* 115 */       deser = ctxt.handleSecondaryContextualization(deser, property, this._enumType);
/*     */     } 
/* 117 */     return withResolved(deser, unwrapSingle);
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
/*     */   public EnumSet<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 129 */     EnumSet result = constructSet();
/*     */     
/* 131 */     if (!p.isExpectedStartArrayToken()) {
/* 132 */       return handleNonArray(p, ctxt, result);
/*     */     }
/* 134 */     return _deserialize(p, ctxt, result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EnumSet<?> deserialize(JsonParser p, DeserializationContext ctxt, EnumSet<?> result) throws IOException {
/* 142 */     if (!p.isExpectedStartArrayToken()) {
/* 143 */       return handleNonArray(p, ctxt, result);
/*     */     }
/* 145 */     return _deserialize(p, ctxt, result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final EnumSet<?> _deserialize(JsonParser p, DeserializationContext ctxt, EnumSet<Enum<?>> result) throws IOException {
/*     */     try {
/*     */       JsonToken t;
/* 155 */       while ((t = p.nextToken()) != JsonToken.END_ARRAY) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 161 */         if (t == JsonToken.VALUE_NULL) {
/* 162 */           return (EnumSet)ctxt.handleUnexpectedToken(this._enumClass, p);
/*     */         }
/* 164 */         Enum<?> value = (Enum)this._enumDeserializer.deserialize(p, ctxt);
/*     */ 
/*     */ 
/*     */         
/* 168 */         if (value != null) {
/* 169 */           result.add(value);
/*     */         }
/*     */       } 
/* 172 */     } catch (Exception e) {
/* 173 */       throw JsonMappingException.wrapWithPath(e, result, result.size());
/*     */     } 
/* 175 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
/* 183 */     return typeDeserializer.deserializeTypedFromArray(p, ctxt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private EnumSet constructSet() {
/* 189 */     return EnumSet.noneOf(this._enumClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected EnumSet<?> handleNonArray(JsonParser p, DeserializationContext ctxt, EnumSet<Enum<?>> result) throws IOException {
/* 199 */     boolean canWrap = (this._unwrapSingle == Boolean.TRUE || (this._unwrapSingle == null && ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)));
/*     */     
/* 201 */     if (!canWrap) {
/* 202 */       return (EnumSet)ctxt.handleUnexpectedToken(EnumSet.class, p);
/*     */     }
/*     */     
/* 205 */     if (p.hasToken(JsonToken.VALUE_NULL)) {
/* 206 */       return (EnumSet)ctxt.handleUnexpectedToken(this._enumClass, p);
/*     */     }
/*     */     try {
/* 209 */       Enum<?> value = (Enum)this._enumDeserializer.deserialize(p, ctxt);
/* 210 */       if (value != null) {
/* 211 */         result.add(value);
/*     */       }
/* 213 */     } catch (Exception e) {
/* 214 */       throw JsonMappingException.wrapWithPath(e, result, result.size());
/*     */     } 
/* 216 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\std\EnumSetDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */