/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
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
/*     */ public class BooleanNode
/*     */   extends ValueNode
/*     */ {
/*     */   private static final long serialVersionUID = 2L;
/*  21 */   public static final BooleanNode TRUE = new BooleanNode(true);
/*  22 */   public static final BooleanNode FALSE = new BooleanNode(false);
/*     */ 
/*     */   
/*     */   private final boolean _value;
/*     */ 
/*     */ 
/*     */   
/*     */   protected BooleanNode(boolean v) {
/*  30 */     this._value = v;
/*     */   }
/*     */   
/*     */   protected Object readResolve() {
/*  34 */     return this._value ? TRUE : FALSE;
/*     */   }
/*     */   
/*  37 */   public static BooleanNode getTrue() { return TRUE; } public static BooleanNode getFalse() {
/*  38 */     return FALSE;
/*     */   } public static BooleanNode valueOf(boolean b) {
/*  40 */     return b ? TRUE : FALSE;
/*     */   }
/*     */   
/*     */   public JsonNodeType getNodeType() {
/*  44 */     return JsonNodeType.BOOLEAN;
/*     */   }
/*     */   
/*     */   public JsonToken asToken() {
/*  48 */     return this._value ? JsonToken.VALUE_TRUE : JsonToken.VALUE_FALSE;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean booleanValue() {
/*  53 */     return this._value;
/*     */   }
/*     */ 
/*     */   
/*     */   public String asText() {
/*  58 */     return this._value ? "true" : "false";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean asBoolean() {
/*  63 */     return this._value;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean asBoolean(boolean defaultValue) {
/*  68 */     return this._value;
/*     */   }
/*     */ 
/*     */   
/*     */   public int asInt(int defaultValue) {
/*  73 */     return this._value ? 1 : 0;
/*     */   }
/*     */   
/*     */   public long asLong(long defaultValue) {
/*  77 */     return this._value ? 1L : 0L;
/*     */   }
/*     */   
/*     */   public double asDouble(double defaultValue) {
/*  81 */     return this._value ? 1.0D : 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void serialize(JsonGenerator g, SerializerProvider provider) throws IOException {
/*  86 */     g.writeBoolean(this._value);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  91 */     return this._value ? 3 : 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 101 */     if (o == this) return true; 
/* 102 */     if (o == null) return false; 
/* 103 */     if (!(o instanceof BooleanNode)) {
/* 104 */       return false;
/*     */     }
/* 106 */     return (this._value == ((BooleanNode)o)._value);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\node\BooleanNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */