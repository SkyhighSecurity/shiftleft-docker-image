/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Base64Variants;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BinaryNode
/*     */   extends ValueNode
/*     */ {
/*     */   private static final long serialVersionUID = 2L;
/*  19 */   static final BinaryNode EMPTY_BINARY_NODE = new BinaryNode(new byte[0]);
/*     */   
/*     */   protected final byte[] _data;
/*     */ 
/*     */   
/*     */   public BinaryNode(byte[] data) {
/*  25 */     this._data = data;
/*     */   }
/*     */ 
/*     */   
/*     */   public BinaryNode(byte[] data, int offset, int length) {
/*  30 */     if (offset == 0 && length == data.length) {
/*  31 */       this._data = data;
/*     */     } else {
/*  33 */       this._data = new byte[length];
/*  34 */       System.arraycopy(data, offset, this._data, 0, length);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static BinaryNode valueOf(byte[] data) {
/*  40 */     if (data == null) {
/*  41 */       return null;
/*     */     }
/*  43 */     if (data.length == 0) {
/*  44 */       return EMPTY_BINARY_NODE;
/*     */     }
/*  46 */     return new BinaryNode(data);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BinaryNode valueOf(byte[] data, int offset, int length) {
/*  51 */     if (data == null) {
/*  52 */       return null;
/*     */     }
/*  54 */     if (length == 0) {
/*  55 */       return EMPTY_BINARY_NODE;
/*     */     }
/*  57 */     return new BinaryNode(data, offset, length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonNodeType getNodeType() {
/*  63 */     return JsonNodeType.BINARY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonToken asToken() {
/*  72 */     return JsonToken.VALUE_EMBEDDED_OBJECT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] binaryValue() {
/*  81 */     return this._data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String asText() {
/*  89 */     return Base64Variants.getDefaultVariant().encode(this._data, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException, JsonProcessingException {
/*  96 */     jg.writeBinary(provider.getConfig().getBase64Variant(), this._data, 0, this._data.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 103 */     if (o == this) return true; 
/* 104 */     if (o == null) return false; 
/* 105 */     if (!(o instanceof BinaryNode)) {
/* 106 */       return false;
/*     */     }
/* 108 */     return Arrays.equals(((BinaryNode)o)._data, this._data);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 113 */     return (this._data == null) ? -1 : this._data.length;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\node\BinaryNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */