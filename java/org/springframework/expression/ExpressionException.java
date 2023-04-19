/*     */ package org.springframework.expression;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExpressionException
/*     */   extends RuntimeException
/*     */ {
/*     */   protected String expressionString;
/*     */   protected int position;
/*     */   
/*     */   public ExpressionException(String message) {
/*  39 */     super(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExpressionException(String message, Throwable cause) {
/*  48 */     super(message, cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExpressionException(String expressionString, String message) {
/*  57 */     super(message);
/*  58 */     this.expressionString = expressionString;
/*  59 */     this.position = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExpressionException(String expressionString, int position, String message) {
/*  69 */     super(message);
/*  70 */     this.expressionString = expressionString;
/*  71 */     this.position = position;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExpressionException(int position, String message) {
/*  80 */     super(message);
/*  81 */     this.position = position;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExpressionException(int position, String message, Throwable cause) {
/*  91 */     super(message, cause);
/*  92 */     this.position = position;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getExpressionString() {
/* 100 */     return this.expressionString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getPosition() {
/* 107 */     return this.position;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/* 118 */     return toDetailedString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toDetailedString() {
/* 126 */     if (this.expressionString != null) {
/* 127 */       StringBuilder output = new StringBuilder();
/* 128 */       output.append("Expression [");
/* 129 */       output.append(this.expressionString);
/* 130 */       output.append("]");
/* 131 */       if (this.position >= 0) {
/* 132 */         output.append(" @");
/* 133 */         output.append(this.position);
/*     */       } 
/* 135 */       output.append(": ");
/* 136 */       output.append(getSimpleMessage());
/* 137 */       return output.toString();
/*     */     } 
/*     */     
/* 140 */     return getSimpleMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSimpleMessage() {
/* 150 */     return super.getMessage();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\ExpressionException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */