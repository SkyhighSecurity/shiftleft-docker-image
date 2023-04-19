/*     */ package com.fasterxml.jackson.databind.util;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonSerializable;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JSONWrappedObject
/*     */   implements JsonSerializable
/*     */ {
/*     */   protected final String _prefix;
/*     */   protected final String _suffix;
/*     */   protected final Object _value;
/*     */   protected final JavaType _serializationType;
/*     */   
/*     */   public JSONWrappedObject(String prefix, String suffix, Object value) {
/*  46 */     this(prefix, suffix, value, (JavaType)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONWrappedObject(String prefix, String suffix, Object value, JavaType asType) {
/*  56 */     this._prefix = prefix;
/*  57 */     this._suffix = suffix;
/*  58 */     this._value = value;
/*  59 */     this._serializationType = asType;
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
/*     */   public void serializeWithType(JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonProcessingException {
/*  73 */     serialize(jgen, provider);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serialize(JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
/*  81 */     if (this._prefix != null) jgen.writeRaw(this._prefix); 
/*  82 */     if (this._value == null) {
/*  83 */       provider.defaultSerializeNull(jgen);
/*  84 */     } else if (this._serializationType != null) {
/*  85 */       provider.findTypedValueSerializer(this._serializationType, true, null).serialize(this._value, jgen, provider);
/*     */     } else {
/*  87 */       Class<?> cls = this._value.getClass();
/*  88 */       provider.findTypedValueSerializer(cls, true, null).serialize(this._value, jgen, provider);
/*     */     } 
/*  90 */     if (this._suffix != null) jgen.writeRaw(this._suffix);
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPrefix() {
/*  99 */     return this._prefix;
/* 100 */   } public String getSuffix() { return this._suffix; }
/* 101 */   public Object getValue() { return this._value; } public JavaType getSerializationType() {
/* 102 */     return this._serializationType;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databin\\util\JSONWrappedObject.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */