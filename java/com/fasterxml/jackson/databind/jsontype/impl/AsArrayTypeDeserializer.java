/*     */ package com.fasterxml.jackson.databind.jsontype.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.util.JsonParserSequence;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*     */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsArrayTypeDeserializer
/*     */   extends TypeDeserializerBase
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public AsArrayTypeDeserializer(JavaType bt, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
/*  32 */     super(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
/*     */   }
/*     */   
/*     */   public AsArrayTypeDeserializer(AsArrayTypeDeserializer src, BeanProperty property) {
/*  36 */     super(src, property);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDeserializer forProperty(BeanProperty prop) {
/*  42 */     return (prop == this._property) ? this : new AsArrayTypeDeserializer(this, prop);
/*     */   }
/*     */   
/*     */   public JsonTypeInfo.As getTypeInclusion() {
/*  46 */     return JsonTypeInfo.As.WRAPPER_ARRAY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
/*  53 */     return _deserialize(jp, ctxt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object deserializeTypedFromObject(JsonParser jp, DeserializationContext ctxt) throws IOException {
/*  61 */     return _deserialize(jp, ctxt);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object deserializeTypedFromScalar(JsonParser jp, DeserializationContext ctxt) throws IOException {
/*  66 */     return _deserialize(jp, ctxt);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object deserializeTypedFromAny(JsonParser jp, DeserializationContext ctxt) throws IOException {
/*  71 */     return _deserialize(jp, ctxt);
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
/*     */   protected Object _deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/*     */     JsonParserSequence jsonParserSequence;
/*  89 */     if (p.canReadTypeId()) {
/*  90 */       Object object = p.getTypeId();
/*  91 */       if (object != null) {
/*  92 */         return _deserializeWithNativeTypeId(p, ctxt, object);
/*     */       }
/*     */     } 
/*  95 */     boolean hadStartArray = p.isExpectedStartArrayToken();
/*  96 */     String typeId = _locateTypeId(p, ctxt);
/*  97 */     JsonDeserializer<Object> deser = _findDeserializer(ctxt, typeId);
/*     */     
/*  99 */     if (this._typeIdVisible && 
/*     */ 
/*     */ 
/*     */       
/* 103 */       !_usesExternalId() && p
/* 104 */       .getCurrentToken() == JsonToken.START_OBJECT) {
/*     */       
/* 106 */       TokenBuffer tb = new TokenBuffer(null, false);
/* 107 */       tb.writeStartObject();
/* 108 */       tb.writeFieldName(this._typePropertyName);
/* 109 */       tb.writeString(typeId);
/*     */ 
/*     */       
/* 112 */       p.clearCurrentToken();
/* 113 */       jsonParserSequence = JsonParserSequence.createFlattened(false, tb.asParser(p), p);
/* 114 */       jsonParserSequence.nextToken();
/*     */     } 
/*     */     
/* 117 */     if (hadStartArray && jsonParserSequence.currentToken() == JsonToken.END_ARRAY) {
/* 118 */       return deser.getNullValue(ctxt);
/*     */     }
/* 120 */     Object value = deser.deserialize((JsonParser)jsonParserSequence, ctxt);
/*     */     
/* 122 */     if (hadStartArray && jsonParserSequence.nextToken() != JsonToken.END_ARRAY) {
/* 123 */       ctxt.reportWrongTokenException(baseType(), JsonToken.END_ARRAY, "expected closing END_ARRAY after type information and deserialized value", new Object[0]);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String _locateTypeId(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 135 */     if (!p.isExpectedStartArrayToken()) {
/*     */ 
/*     */       
/* 138 */       if (this._defaultImpl != null) {
/* 139 */         return this._idResolver.idFromBaseType();
/*     */       }
/* 141 */       ctxt.reportWrongTokenException(baseType(), JsonToken.START_ARRAY, "need JSON Array to contain As.WRAPPER_ARRAY type information for class " + 
/* 142 */           baseTypeName(), new Object[0]);
/* 143 */       return null;
/*     */     } 
/*     */     
/* 146 */     JsonToken t = p.nextToken();
/* 147 */     if (t == JsonToken.VALUE_STRING) {
/* 148 */       String result = p.getText();
/* 149 */       p.nextToken();
/* 150 */       return result;
/*     */     } 
/* 152 */     if (this._defaultImpl != null) {
/* 153 */       return this._idResolver.idFromBaseType();
/*     */     }
/* 155 */     ctxt.reportWrongTokenException(baseType(), JsonToken.VALUE_STRING, "need JSON String that contains type id (for subtype of %s)", new Object[] {
/* 156 */           baseTypeName() });
/* 157 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean _usesExternalId() {
/* 164 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\jsontype\impl\AsArrayTypeDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */