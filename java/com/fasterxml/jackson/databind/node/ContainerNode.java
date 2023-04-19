/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.TreeNode;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.util.RawValue;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
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
/*     */ public abstract class ContainerNode<T extends ContainerNode<T>>
/*     */   extends BaseJsonNode
/*     */   implements JsonNodeCreator
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final JsonNodeFactory _nodeFactory;
/*     */   
/*     */   protected ContainerNode(JsonNodeFactory nc) {
/*  28 */     this._nodeFactory = nc;
/*     */   }
/*     */   protected ContainerNode() {
/*  31 */     this._nodeFactory = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String asText() {
/*  40 */     return "";
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final BooleanNode booleanNode(boolean v) {
/*  64 */     return this._nodeFactory.booleanNode(v);
/*     */   }
/*     */   public JsonNode missingNode() {
/*  67 */     return this._nodeFactory.missingNode();
/*     */   }
/*     */   
/*     */   public final NullNode nullNode() {
/*  71 */     return this._nodeFactory.nullNode();
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
/*     */   public final ArrayNode arrayNode() {
/*  84 */     return this._nodeFactory.arrayNode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ArrayNode arrayNode(int capacity) {
/*  92 */     return this._nodeFactory.arrayNode(capacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ObjectNode objectNode() {
/*  99 */     return this._nodeFactory.objectNode();
/*     */   }
/*     */   public final NumericNode numberNode(byte v) {
/* 102 */     return this._nodeFactory.numberNode(v);
/*     */   } public final NumericNode numberNode(short v) {
/* 104 */     return this._nodeFactory.numberNode(v);
/*     */   } public final NumericNode numberNode(int v) {
/* 106 */     return this._nodeFactory.numberNode(v);
/*     */   }
/*     */   public final NumericNode numberNode(long v) {
/* 109 */     return this._nodeFactory.numberNode(v);
/*     */   }
/*     */   
/*     */   public final NumericNode numberNode(float v) {
/* 113 */     return this._nodeFactory.numberNode(v);
/*     */   } public final NumericNode numberNode(double v) {
/* 115 */     return this._nodeFactory.numberNode(v);
/*     */   }
/*     */   public final ValueNode numberNode(BigInteger v) {
/* 118 */     return this._nodeFactory.numberNode(v);
/*     */   } public final ValueNode numberNode(BigDecimal v) {
/* 120 */     return this._nodeFactory.numberNode(v);
/*     */   }
/*     */   public final ValueNode numberNode(Byte v) {
/* 123 */     return this._nodeFactory.numberNode(v);
/*     */   } public final ValueNode numberNode(Short v) {
/* 125 */     return this._nodeFactory.numberNode(v);
/*     */   } public final ValueNode numberNode(Integer v) {
/* 127 */     return this._nodeFactory.numberNode(v);
/*     */   } public final ValueNode numberNode(Long v) {
/* 129 */     return this._nodeFactory.numberNode(v);
/*     */   }
/*     */   public final ValueNode numberNode(Float v) {
/* 132 */     return this._nodeFactory.numberNode(v);
/*     */   } public final ValueNode numberNode(Double v) {
/* 134 */     return this._nodeFactory.numberNode(v);
/*     */   }
/*     */   public final TextNode textNode(String text) {
/* 137 */     return this._nodeFactory.textNode(text);
/*     */   }
/*     */   public final BinaryNode binaryNode(byte[] data) {
/* 140 */     return this._nodeFactory.binaryNode(data);
/*     */   } public final BinaryNode binaryNode(byte[] data, int offset, int length) {
/* 142 */     return this._nodeFactory.binaryNode(data, offset, length);
/*     */   }
/*     */   public final ValueNode pojoNode(Object pojo) {
/* 145 */     return this._nodeFactory.pojoNode(pojo);
/*     */   }
/*     */   public final ValueNode rawValueNode(RawValue value) {
/* 148 */     return this._nodeFactory.rawValueNode(value);
/*     */   }
/*     */   
/*     */   public abstract JsonToken asToken();
/*     */   
/*     */   public abstract int size();
/*     */   
/*     */   public abstract JsonNode get(int paramInt);
/*     */   
/*     */   public abstract JsonNode get(String paramString);
/*     */   
/*     */   public abstract T removeAll();
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\node\ContainerNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */