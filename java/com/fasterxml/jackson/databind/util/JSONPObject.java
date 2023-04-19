/*     */ package com.fasterxml.jackson.databind.util;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonpCharacterEscapes;
/*     */ import com.fasterxml.jackson.core.io.CharacterEscapes;
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
/*     */ public class JSONPObject
/*     */   implements JsonSerializable
/*     */ {
/*     */   protected final String _function;
/*     */   protected final Object _value;
/*     */   protected final JavaType _serializationType;
/*     */   
/*     */   public JSONPObject(String function, Object value) {
/*  39 */     this(function, value, (JavaType)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public JSONPObject(String function, Object value, JavaType asType) {
/*  44 */     this._function = function;
/*  45 */     this._value = value;
/*  46 */     this._serializationType = asType;
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
/*     */   public void serializeWithType(JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/*  60 */     serialize(gen, provider);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serialize(JsonGenerator gen, SerializerProvider provider) throws IOException {
/*  68 */     gen.writeRaw(this._function);
/*  69 */     gen.writeRaw('(');
/*     */     
/*  71 */     if (this._value == null) {
/*  72 */       provider.defaultSerializeNull(gen);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/*  77 */       boolean override = (gen.getCharacterEscapes() == null);
/*  78 */       if (override) {
/*  79 */         gen.setCharacterEscapes((CharacterEscapes)JsonpCharacterEscapes.instance());
/*     */       }
/*     */       
/*     */       try {
/*  83 */         if (this._serializationType != null) {
/*  84 */           provider.findTypedValueSerializer(this._serializationType, true, null).serialize(this._value, gen, provider);
/*     */         } else {
/*  86 */           provider.findTypedValueSerializer(this._value.getClass(), true, null).serialize(this._value, gen, provider);
/*     */         } 
/*     */       } finally {
/*  89 */         if (override) {
/*  90 */           gen.setCharacterEscapes(null);
/*     */         }
/*     */       } 
/*     */     } 
/*  94 */     gen.writeRaw(')');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFunction() {
/* 103 */     return this._function;
/* 104 */   } public Object getValue() { return this._value; } public JavaType getSerializationType() {
/* 105 */     return this._serializationType;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databin\\util\JSONPObject.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */