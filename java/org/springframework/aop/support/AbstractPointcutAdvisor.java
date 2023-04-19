/*    */ package org.springframework.aop.support;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.aopalliance.aop.Advice;
/*    */ import org.springframework.aop.PointcutAdvisor;
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.util.ObjectUtils;
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
/*    */ public abstract class AbstractPointcutAdvisor
/*    */   implements PointcutAdvisor, Ordered, Serializable
/*    */ {
/*    */   private Integer order;
/*    */   
/*    */   public void setOrder(int order) {
/* 44 */     this.order = Integer.valueOf(order);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getOrder() {
/* 49 */     if (this.order != null) {
/* 50 */       return this.order.intValue();
/*    */     }
/* 52 */     Advice advice = getAdvice();
/* 53 */     if (advice instanceof Ordered) {
/* 54 */       return ((Ordered)advice).getOrder();
/*    */     }
/* 56 */     return Integer.MAX_VALUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPerInstance() {
/* 61 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 67 */     if (this == other) {
/* 68 */       return true;
/*    */     }
/* 70 */     if (!(other instanceof PointcutAdvisor)) {
/* 71 */       return false;
/*    */     }
/* 73 */     PointcutAdvisor otherAdvisor = (PointcutAdvisor)other;
/* 74 */     return (ObjectUtils.nullSafeEquals(getAdvice(), otherAdvisor.getAdvice()) && 
/* 75 */       ObjectUtils.nullSafeEquals(getPointcut(), otherAdvisor.getPointcut()));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 80 */     return PointcutAdvisor.class.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\AbstractPointcutAdvisor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */