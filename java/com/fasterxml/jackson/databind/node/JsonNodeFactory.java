/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.util.RawValue;
/*     */ import java.io.Serializable;
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
/*     */ 
/*     */ public class JsonNodeFactory
/*     */   implements Serializable, JsonNodeCreator
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final boolean _cfgBigDecimalExact;
/*  25 */   private static final JsonNodeFactory decimalsNormalized = new JsonNodeFactory(false);
/*     */   
/*  27 */   private static final JsonNodeFactory decimalsAsIs = new JsonNodeFactory(true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  35 */   public static final JsonNodeFactory instance = decimalsNormalized;
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
/*     */   public JsonNodeFactory(boolean bigDecimalExact) {
/*  64 */     this._cfgBigDecimalExact = bigDecimalExact;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JsonNodeFactory() {
/*  75 */     this(false);
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
/*     */   public static JsonNodeFactory withExactBigDecimals(boolean bigDecimalExact) {
/*  87 */     return bigDecimalExact ? decimalsAsIs : decimalsNormalized;
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
/*     */   public BooleanNode booleanNode(boolean v) {
/* 102 */     return v ? BooleanNode.getTrue() : BooleanNode.getFalse();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NullNode nullNode() {
/* 110 */     return NullNode.getInstance();
/*     */   }
/*     */   public JsonNode missingNode() {
/* 113 */     return MissingNode.getInstance();
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
/*     */   public NumericNode numberNode(byte v) {
/* 127 */     return IntNode.valueOf(v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueNode numberNode(Byte value) {
/* 137 */     return (value == null) ? nullNode() : IntNode.valueOf(value.intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NumericNode numberNode(short v) {
/* 145 */     return ShortNode.valueOf(v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueNode numberNode(Short value) {
/* 155 */     return (value == null) ? nullNode() : ShortNode.valueOf(value.shortValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NumericNode numberNode(int v) {
/* 163 */     return IntNode.valueOf(v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueNode numberNode(Integer value) {
/* 173 */     return (value == null) ? nullNode() : IntNode.valueOf(value.intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NumericNode numberNode(long v) {
/* 182 */     return LongNode.valueOf(v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueNode numberNode(Long v) {
/* 192 */     if (v == null) {
/* 193 */       return nullNode();
/*     */     }
/* 195 */     return LongNode.valueOf(v.longValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueNode numberNode(BigInteger v) {
/* 204 */     if (v == null) {
/* 205 */       return nullNode();
/*     */     }
/* 207 */     return BigIntegerNode.valueOf(v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NumericNode numberNode(float v) {
/* 215 */     return FloatNode.valueOf(v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueNode numberNode(Float value) {
/* 225 */     return (value == null) ? nullNode() : FloatNode.valueOf(value.floatValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NumericNode numberNode(double v) {
/* 233 */     return DoubleNode.valueOf(v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueNode numberNode(Double value) {
/* 243 */     return (value == null) ? nullNode() : DoubleNode.valueOf(value.doubleValue());
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
/*     */   public ValueNode numberNode(BigDecimal v) {
/* 259 */     if (v == null) {
/* 260 */       return nullNode();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 267 */     if (this._cfgBigDecimalExact) {
/* 268 */       return DecimalNode.valueOf(v);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 279 */     return (v.compareTo(BigDecimal.ZERO) == 0) ? DecimalNode.ZERO : 
/* 280 */       DecimalNode.valueOf(v.stripTrailingZeros());
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
/*     */   public TextNode textNode(String text) {
/* 294 */     return TextNode.valueOf(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BinaryNode binaryNode(byte[] data) {
/* 302 */     return BinaryNode.valueOf(data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BinaryNode binaryNode(byte[] data, int offset, int length) {
/* 311 */     return BinaryNode.valueOf(data, offset, length);
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
/*     */   public ArrayNode arrayNode() {
/* 324 */     return new ArrayNode(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayNode arrayNode(int capacity) {
/* 332 */     return new ArrayNode(this, capacity);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectNode objectNode() {
/* 338 */     return new ObjectNode(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueNode pojoNode(Object pojo) {
/* 347 */     return new POJONode(pojo);
/*     */   }
/*     */   
/*     */   public ValueNode rawValueNode(RawValue value) {
/* 351 */     return new POJONode(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean _inIntRange(long l) {
/* 362 */     int i = (int)l;
/* 363 */     long l2 = i;
/* 364 */     return (l2 == l);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\node\JsonNodeFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */