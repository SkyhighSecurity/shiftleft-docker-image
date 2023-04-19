/*    */ package com.fasterxml.jackson.databind.node;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import java.math.BigDecimal;
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class NumericNode
/*    */   extends ValueNode
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public final JsonNodeType getNodeType() {
/* 21 */     return JsonNodeType.NUMBER;
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract JsonParser.NumberType numberType();
/*    */ 
/*    */   
/*    */   public abstract Number numberValue();
/*    */ 
/*    */   
/*    */   public abstract int intValue();
/*    */ 
/*    */   
/*    */   public abstract long longValue();
/*    */ 
/*    */   
/*    */   public abstract double doubleValue();
/*    */   
/*    */   public abstract BigDecimal decimalValue();
/*    */   
/*    */   public abstract BigInteger bigIntegerValue();
/*    */   
/*    */   public abstract boolean canConvertToInt();
/*    */   
/*    */   public abstract boolean canConvertToLong();
/*    */   
/*    */   public abstract String asText();
/*    */   
/*    */   public final int asInt() {
/* 50 */     return intValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public final int asInt(int defaultValue) {
/* 55 */     return intValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public final long asLong() {
/* 60 */     return longValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public final long asLong(long defaultValue) {
/* 65 */     return longValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public final double asDouble() {
/* 70 */     return doubleValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public final double asDouble(double defaultValue) {
/* 75 */     return doubleValue();
/*    */   }
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
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isNaN() {
/* 92 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\node\NumericNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */