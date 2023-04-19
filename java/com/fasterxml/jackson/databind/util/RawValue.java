/*     */ package com.fasterxml.jackson.databind.util;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.SerializableString;
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
/*     */ public class RawValue
/*     */   implements JsonSerializable
/*     */ {
/*     */   protected Object _value;
/*     */   
/*     */   public RawValue(String v) {
/*  30 */     this._value = v;
/*     */   }
/*     */   
/*     */   public RawValue(SerializableString v) {
/*  34 */     this._value = v;
/*     */   }
/*     */   
/*     */   public RawValue(JsonSerializable v) {
/*  38 */     this._value = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RawValue(Object value, boolean bogus) {
/*  49 */     this._value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object rawValue() {
/*  57 */     return this._value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
/*  63 */     if (this._value instanceof JsonSerializable) {
/*  64 */       ((JsonSerializable)this._value).serialize(gen, serializers);
/*     */     } else {
/*  66 */       _serialize(gen);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
/*  74 */     if (this._value instanceof JsonSerializable) {
/*  75 */       ((JsonSerializable)this._value).serializeWithType(gen, serializers, typeSer);
/*  76 */     } else if (this._value instanceof SerializableString) {
/*     */ 
/*     */ 
/*     */       
/*  80 */       serialize(gen, serializers);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void serialize(JsonGenerator gen) throws IOException {
/*  86 */     if (this._value instanceof JsonSerializable) {
/*     */       
/*  88 */       gen.writeObject(this._value);
/*     */     } else {
/*  90 */       _serialize(gen);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _serialize(JsonGenerator gen) throws IOException {
/*  96 */     if (this._value instanceof SerializableString) {
/*  97 */       gen.writeRawValue((SerializableString)this._value);
/*     */     } else {
/*  99 */       gen.writeRawValue(String.valueOf(this._value));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 105 */     if (o == this) return true; 
/* 106 */     if (!(o instanceof RawValue)) return false; 
/* 107 */     RawValue other = (RawValue)o;
/*     */     
/* 109 */     if (this._value == other._value) {
/* 110 */       return true;
/*     */     }
/* 112 */     return (this._value != null && this._value.equals(other._value));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 117 */     return (this._value == null) ? 0 : this._value.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 122 */     return String.format("[RawValue of type %s]", new Object[] { ClassUtil.classNameOf(this._value) });
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databin\\util\RawValue.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */