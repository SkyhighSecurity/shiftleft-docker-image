/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.InternalParseException;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.SpelParseException;
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
/*     */ public abstract class Literal
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private final String originalValue;
/*     */   
/*     */   public Literal(String originalValue, int pos) {
/*  38 */     super(pos, new SpelNodeImpl[0]);
/*  39 */     this.originalValue = originalValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getOriginalValue() {
/*  44 */     return this.originalValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public final TypedValue getValueInternal(ExpressionState state) throws SpelEvaluationException {
/*  49 */     return getLiteralValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  54 */     return getLiteralValue().getValue().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/*  59 */     return toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract TypedValue getLiteralValue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Literal getIntLiteral(String numberToken, int pos, int radix) {
/*     */     try {
/*  76 */       int value = Integer.parseInt(numberToken, radix);
/*  77 */       return new IntLiteral(numberToken, pos, value);
/*     */     }
/*  79 */     catch (NumberFormatException ex) {
/*  80 */       throw new InternalParseException(new SpelParseException(pos >> 16, ex, SpelMessage.NOT_AN_INTEGER, new Object[] { numberToken }));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Literal getLongLiteral(String numberToken, int pos, int radix) {
/*     */     try {
/*  86 */       long value = Long.parseLong(numberToken, radix);
/*  87 */       return new LongLiteral(numberToken, pos, value);
/*     */     }
/*  89 */     catch (NumberFormatException ex) {
/*  90 */       throw new InternalParseException(new SpelParseException(pos >> 16, ex, SpelMessage.NOT_A_LONG, new Object[] { numberToken }));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Literal getRealLiteral(String numberToken, int pos, boolean isFloat) {
/*     */     try {
/*  96 */       if (isFloat) {
/*  97 */         float f = Float.parseFloat(numberToken);
/*  98 */         return new FloatLiteral(numberToken, pos, f);
/*     */       } 
/*     */       
/* 101 */       double value = Double.parseDouble(numberToken);
/* 102 */       return new RealLiteral(numberToken, pos, value);
/*     */     
/*     */     }
/* 105 */     catch (NumberFormatException ex) {
/* 106 */       throw new InternalParseException(new SpelParseException(pos >> 16, ex, SpelMessage.NOT_A_REAL, new Object[] { numberToken }));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\Literal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */