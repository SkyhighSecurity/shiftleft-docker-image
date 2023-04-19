/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.JsonSerializable;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class POJONode
/*     */   extends ValueNode
/*     */ {
/*     */   private static final long serialVersionUID = 2L;
/*     */   protected final Object _value;
/*     */   
/*     */   public POJONode(Object v) {
/*  21 */     this._value = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonNodeType getNodeType() {
/*  31 */     return JsonNodeType.POJO;
/*     */   }
/*     */   public JsonToken asToken() {
/*  34 */     return JsonToken.VALUE_EMBEDDED_OBJECT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] binaryValue() throws IOException {
/*  44 */     if (this._value instanceof byte[]) {
/*  45 */       return (byte[])this._value;
/*     */     }
/*  47 */     return super.binaryValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String asText() {
/*  57 */     return (this._value == null) ? "null" : this._value.toString();
/*     */   }
/*     */   public String asText(String defaultValue) {
/*  60 */     return (this._value == null) ? defaultValue : this._value.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean asBoolean(boolean defaultValue) {
/*  66 */     if (this._value != null && this._value instanceof Boolean) {
/*  67 */       return ((Boolean)this._value).booleanValue();
/*     */     }
/*  69 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int asInt(int defaultValue) {
/*  75 */     if (this._value instanceof Number) {
/*  76 */       return ((Number)this._value).intValue();
/*     */     }
/*  78 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long asLong(long defaultValue) {
/*  84 */     if (this._value instanceof Number) {
/*  85 */       return ((Number)this._value).longValue();
/*     */     }
/*  87 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double asDouble(double defaultValue) {
/*  93 */     if (this._value instanceof Number) {
/*  94 */       return ((Number)this._value).doubleValue();
/*     */     }
/*  96 */     return defaultValue;
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
/*     */   public final void serialize(JsonGenerator gen, SerializerProvider ctxt) throws IOException {
/* 108 */     if (this._value == null) {
/* 109 */       ctxt.defaultSerializeNull(gen);
/* 110 */     } else if (this._value instanceof JsonSerializable) {
/* 111 */       ((JsonSerializable)this._value).serialize(gen, ctxt);
/*     */     }
/*     */     else {
/*     */       
/* 115 */       ctxt.defaultSerializeValue(this._value, gen);
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
/*     */   public Object getPojo() {
/* 128 */     return this._value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 139 */     if (o == this) return true; 
/* 140 */     if (o == null) return false; 
/* 141 */     if (o instanceof POJONode) {
/* 142 */       return _pojoEquals((POJONode)o);
/*     */     }
/* 144 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean _pojoEquals(POJONode other) {
/* 152 */     if (this._value == null) {
/* 153 */       return (other._value == null);
/*     */     }
/* 155 */     return this._value.equals(other._value);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 159 */     return this._value.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\node\POJONode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */