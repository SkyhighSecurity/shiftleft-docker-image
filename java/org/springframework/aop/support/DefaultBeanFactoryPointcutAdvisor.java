/*    */ package org.springframework.aop.support;
/*    */ 
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
/*    */ 
/*    */ public class DefaultBeanFactoryPointcutAdvisor
/*    */   extends AbstractBeanFactoryPointcutAdvisor
/*    */ {
/* 38 */   private Pointcut pointcut = Pointcut.TRUE;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPointcut(Pointcut pointcut) {
/* 47 */     this.pointcut = (pointcut != null) ? pointcut : Pointcut.TRUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public Pointcut getPointcut() {
/* 52 */     return this.pointcut;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 58 */     return getClass().getName() + ": pointcut [" + getPointcut() + "]; advice bean '" + getAdviceBeanName() + "'";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\DefaultBeanFactoryPointcutAdvisor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */