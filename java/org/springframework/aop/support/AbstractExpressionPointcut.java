/*    */ package org.springframework.aop.support;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractExpressionPointcut
/*    */   implements ExpressionPointcut, Serializable
/*    */ {
/*    */   private String location;
/*    */   private String expression;
/*    */   
/*    */   public void setLocation(String location) {
/* 43 */     this.location = location;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getLocation() {
/* 53 */     return this.location;
/*    */   }
/*    */   
/*    */   public void setExpression(String expression) {
/* 57 */     this.expression = expression;
/*    */     try {
/* 59 */       onSetExpression(expression);
/*    */     }
/* 61 */     catch (IllegalArgumentException ex) {
/*    */       
/* 63 */       if (this.location != null) {
/* 64 */         throw new IllegalArgumentException("Invalid expression at location [" + this.location + "]: " + ex);
/*    */       }
/*    */       
/* 67 */       throw ex;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onSetExpression(String expression) throws IllegalArgumentException {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getExpression() {
/* 88 */     return this.expression;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\AbstractExpressionPointcut.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */