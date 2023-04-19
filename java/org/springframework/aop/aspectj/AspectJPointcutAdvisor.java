/*    */ package org.springframework.aop.aspectj;
/*    */ 
/*    */ import org.aopalliance.aop.Advice;
/*    */ import org.springframework.aop.Pointcut;
/*    */ import org.springframework.aop.PointcutAdvisor;
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class AspectJPointcutAdvisor
/*    */   implements PointcutAdvisor, Ordered
/*    */ {
/*    */   private final AbstractAspectJAdvice advice;
/*    */   private final Pointcut pointcut;
/*    */   private Integer order;
/*    */   
/*    */   public AspectJPointcutAdvisor(AbstractAspectJAdvice advice) {
/* 48 */     Assert.notNull(advice, "Advice must not be null");
/* 49 */     this.advice = advice;
/* 50 */     this.pointcut = advice.buildSafePointcut();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setOrder(int order) {
/* 55 */     this.order = Integer.valueOf(order);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPerInstance() {
/* 60 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public Advice getAdvice() {
/* 65 */     return this.advice;
/*    */   }
/*    */ 
/*    */   
/*    */   public Pointcut getPointcut() {
/* 70 */     return this.pointcut;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getOrder() {
/* 75 */     if (this.order != null) {
/* 76 */       return this.order.intValue();
/*    */     }
/*    */     
/* 79 */     return this.advice.getOrder();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 86 */     if (this == other) {
/* 87 */       return true;
/*    */     }
/* 89 */     if (!(other instanceof AspectJPointcutAdvisor)) {
/* 90 */       return false;
/*    */     }
/* 92 */     AspectJPointcutAdvisor otherAdvisor = (AspectJPointcutAdvisor)other;
/* 93 */     return this.advice.equals(otherAdvisor.advice);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 98 */     return AspectJPointcutAdvisor.class.hashCode() * 29 + this.advice.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\AspectJPointcutAdvisor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */