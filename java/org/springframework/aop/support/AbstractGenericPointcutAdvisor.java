/*    */ package org.springframework.aop.support;
/*    */ 
/*    */ import org.aopalliance.aop.Advice;
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
/*    */ public abstract class AbstractGenericPointcutAdvisor
/*    */   extends AbstractPointcutAdvisor
/*    */ {
/*    */   private Advice advice;
/*    */   
/*    */   public void setAdvice(Advice advice) {
/* 39 */     this.advice = advice;
/*    */   }
/*    */ 
/*    */   
/*    */   public Advice getAdvice() {
/* 44 */     return this.advice;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 50 */     return getClass().getName() + ": advice [" + getAdvice() + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\AbstractGenericPointcutAdvisor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */