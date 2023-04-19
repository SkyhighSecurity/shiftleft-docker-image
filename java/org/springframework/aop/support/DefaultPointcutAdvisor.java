/*    */ package org.springframework.aop.support;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.aopalliance.aop.Advice;
/*    */ import org.springframework.aop.Pointcut;
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
/*    */ public class DefaultPointcutAdvisor
/*    */   extends AbstractGenericPointcutAdvisor
/*    */   implements Serializable
/*    */ {
/* 40 */   private Pointcut pointcut = Pointcut.TRUE;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultPointcutAdvisor() {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultPointcutAdvisor(Advice advice) {
/* 57 */     this(Pointcut.TRUE, advice);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultPointcutAdvisor(Pointcut pointcut, Advice advice) {
/* 66 */     this.pointcut = pointcut;
/* 67 */     setAdvice(advice);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPointcut(Pointcut pointcut) {
/* 77 */     this.pointcut = (pointcut != null) ? pointcut : Pointcut.TRUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public Pointcut getPointcut() {
/* 82 */     return this.pointcut;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 88 */     return getClass().getName() + ": pointcut [" + getPointcut() + "]; advice [" + getAdvice() + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\DefaultPointcutAdvisor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */