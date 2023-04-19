/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.Base64Variants;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.io.CharTypes;
/*     */ import com.fasterxml.jackson.core.io.NumberInput;
/*     */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.exc.InvalidFormatException;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TextNode
/*     */   extends ValueNode
/*     */ {
/*     */   private static final long serialVersionUID = 2L;
/*  21 */   static final TextNode EMPTY_STRING_NODE = new TextNode("");
/*     */   protected final String _value;
/*     */   
/*     */   public TextNode(String v) {
/*  25 */     this._value = v;
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
/*     */   public static TextNode valueOf(String v) {
/*  38 */     if (v == null) {
/*  39 */       return null;
/*     */     }
/*  41 */     if (v.length() == 0) {
/*  42 */       return EMPTY_STRING_NODE;
/*     */     }
/*  44 */     return new TextNode(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonNodeType getNodeType() {
/*  49 */     return JsonNodeType.STRING;
/*     */   }
/*     */   public JsonToken asToken() {
/*  52 */     return JsonToken.VALUE_STRING;
/*     */   }
/*     */   
/*     */   public String textValue() {
/*  56 */     return this._value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getBinaryValue(Base64Variant b64variant) throws IOException {
/*  67 */     String str = this._value.trim();
/*  68 */     ByteArrayBuilder builder = new ByteArrayBuilder(4 + (str.length() * 3 >> 2));
/*     */     try {
/*  70 */       b64variant.decode(str, builder);
/*  71 */     } catch (IllegalArgumentException e) {
/*  72 */       throw InvalidFormatException.from(null, 
/*  73 */           String.format("Cannot access contents of TextNode as binary due to broken Base64 encoding: %s", new Object[] {
/*     */               
/*  75 */               e.getMessage()
/*     */             }), str, byte[].class);
/*     */     } 
/*  78 */     return builder.toByteArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] binaryValue() throws IOException {
/*  83 */     return getBinaryValue(Base64Variants.getDefaultVariant());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String asText() {
/*  94 */     return this._value;
/*     */   }
/*     */ 
/*     */   
/*     */   public String asText(String defaultValue) {
/*  99 */     return (this._value == null) ? defaultValue : this._value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean asBoolean(boolean defaultValue) {
/* 106 */     if (this._value != null) {
/* 107 */       String v = this._value.trim();
/* 108 */       if ("true".equals(v)) {
/* 109 */         return true;
/*     */       }
/* 111 */       if ("false".equals(v)) {
/* 112 */         return false;
/*     */       }
/*     */     } 
/* 115 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public int asInt(int defaultValue) {
/* 120 */     return NumberInput.parseAsInt(this._value, defaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public long asLong(long defaultValue) {
/* 125 */     return NumberInput.parseAsLong(this._value, defaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public double asDouble(double defaultValue) {
/* 130 */     return NumberInput.parseAsDouble(this._value, defaultValue);
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
/*     */   public final void serialize(JsonGenerator g, SerializerProvider provider) throws IOException {
/* 142 */     if (this._value == null) {
/* 143 */       g.writeNull();
/*     */     } else {
/* 145 */       g.writeString(this._value);
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
/*     */   public boolean equals(Object o) {
/* 158 */     if (o == this) return true; 
/* 159 */     if (o == null) return false; 
/* 160 */     if (o instanceof TextNode) {
/* 161 */       return ((TextNode)o)._value.equals(this._value);
/*     */     }
/* 163 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 167 */     return this._value.hashCode();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected static void appendQuoted(StringBuilder sb, String content) {
/* 172 */     sb.append('"');
/* 173 */     CharTypes.appendQuoted(sb, content);
/* 174 */     sb.append('"');
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\node\TextNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */