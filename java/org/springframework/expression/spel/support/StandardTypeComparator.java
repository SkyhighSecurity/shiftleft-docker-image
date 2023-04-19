/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import org.springframework.expression.TypeComparator;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.util.NumberUtils;
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
/*     */ public class StandardTypeComparator
/*     */   implements TypeComparator
/*     */ {
/*     */   public boolean canCompare(Object left, Object right) {
/*  40 */     if (left == null || right == null) {
/*  41 */       return true;
/*     */     }
/*  43 */     if (left instanceof Number && right instanceof Number) {
/*  44 */       return true;
/*     */     }
/*  46 */     if (left instanceof Comparable) {
/*  47 */       return true;
/*     */     }
/*  49 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compare(Object left, Object right) throws SpelEvaluationException {
/*  56 */     if (left == null) {
/*  57 */       return (right == null) ? 0 : -1;
/*     */     }
/*  59 */     if (right == null) {
/*  60 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*  64 */     if (left instanceof Number && right instanceof Number) {
/*  65 */       Number leftNumber = (Number)left;
/*  66 */       Number rightNumber = (Number)right;
/*     */       
/*  68 */       if (leftNumber instanceof BigDecimal || rightNumber instanceof BigDecimal) {
/*  69 */         BigDecimal leftBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
/*  70 */         BigDecimal rightBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
/*  71 */         return leftBigDecimal.compareTo(rightBigDecimal);
/*     */       } 
/*  73 */       if (leftNumber instanceof Double || rightNumber instanceof Double) {
/*  74 */         return Double.compare(leftNumber.doubleValue(), rightNumber.doubleValue());
/*     */       }
/*  76 */       if (leftNumber instanceof Float || rightNumber instanceof Float) {
/*  77 */         return Float.compare(leftNumber.floatValue(), rightNumber.floatValue());
/*     */       }
/*  79 */       if (leftNumber instanceof BigInteger || rightNumber instanceof BigInteger) {
/*  80 */         BigInteger leftBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
/*  81 */         BigInteger rightBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
/*  82 */         return leftBigInteger.compareTo(rightBigInteger);
/*     */       } 
/*  84 */       if (leftNumber instanceof Long || rightNumber instanceof Long)
/*     */       {
/*  86 */         return compare(leftNumber.longValue(), rightNumber.longValue());
/*     */       }
/*  88 */       if (leftNumber instanceof Integer || rightNumber instanceof Integer)
/*     */       {
/*  90 */         return compare(leftNumber.intValue(), rightNumber.intValue());
/*     */       }
/*  92 */       if (leftNumber instanceof Short || rightNumber instanceof Short)
/*     */       {
/*  94 */         return compare(leftNumber.shortValue(), rightNumber.shortValue());
/*     */       }
/*  96 */       if (leftNumber instanceof Byte || rightNumber instanceof Byte)
/*     */       {
/*  98 */         return compare(leftNumber.byteValue(), rightNumber.byteValue());
/*     */       }
/*     */ 
/*     */       
/* 102 */       return Double.compare(leftNumber.doubleValue(), rightNumber.doubleValue());
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 107 */       if (left instanceof Comparable) {
/* 108 */         return ((Comparable<Object>)left).compareTo(right);
/*     */       }
/*     */     }
/* 111 */     catch (ClassCastException ex) {
/* 112 */       throw new SpelEvaluationException(ex, SpelMessage.NOT_COMPARABLE, new Object[] { left.getClass(), right.getClass() });
/*     */     } 
/*     */     
/* 115 */     throw new SpelEvaluationException(SpelMessage.NOT_COMPARABLE, new Object[] { left.getClass(), right.getClass() });
/*     */   }
/*     */ 
/*     */   
/*     */   private static int compare(long x, long y) {
/* 120 */     return (x < y) ? -1 : ((x > y) ? 1 : 0);
/*     */   }
/*     */   
/*     */   private static int compare(int x, int y) {
/* 124 */     return (x < y) ? -1 : ((x > y) ? 1 : 0);
/*     */   }
/*     */   
/*     */   private static int compare(short x, short y) {
/* 128 */     return x - y;
/*     */   }
/*     */   
/*     */   private static int compare(byte x, byte y) {
/* 132 */     return x - y;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\support\StandardTypeComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */