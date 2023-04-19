/*    */ package com.fasterxml.jackson.databind.deser.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*    */ import java.io.IOException;
/*    */ import java.io.Serializable;
/*    */ import java.util.Collection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class TypeWrappedDeserializer
/*    */   extends JsonDeserializer<Object>
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected final TypeDeserializer _typeDeserializer;
/*    */   protected final JsonDeserializer<Object> _deserializer;
/*    */   
/*    */   public TypeWrappedDeserializer(TypeDeserializer typeDeser, JsonDeserializer<?> deser) {
/* 31 */     this._typeDeserializer = typeDeser;
/* 32 */     this._deserializer = (JsonDeserializer)deser;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<?> handledType() {
/* 37 */     return this._deserializer.handledType();
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean supportsUpdate(DeserializationConfig config) {
/* 42 */     return this._deserializer.supportsUpdate(config);
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonDeserializer<?> getDelegatee() {
/* 47 */     return this._deserializer.getDelegatee();
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<Object> getKnownPropertyNames() {
/* 52 */     return this._deserializer.getKnownPropertyNames();
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
/* 57 */     return this._deserializer.getNullValue(ctxt);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getEmptyValue(DeserializationContext ctxt) throws JsonMappingException {
/* 62 */     return this._deserializer.getEmptyValue(ctxt);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 68 */     return this._deserializer.deserializeWithType(p, ctxt, this._typeDeserializer);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
/* 76 */     throw new IllegalStateException("Type-wrapped deserializer's deserializeWithType should never get called");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object deserialize(JsonParser p, DeserializationContext ctxt, Object intoValue) throws IOException {
/* 86 */     return this._deserializer.deserialize(p, ctxt, intoValue);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\impl\TypeWrappedDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */