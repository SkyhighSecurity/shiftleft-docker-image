/*    */ package org.springframework.aop.aspectj;
/*    */ 
/*    */ import org.springframework.aop.Pointcut;
/*    */ import org.springframework.aop.support.AbstractGenericPointcutAdvisor;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.BeanFactoryAware;
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
/*    */ public class AspectJExpressionPointcutAdvisor
/*    */   extends AbstractGenericPointcutAdvisor
/*    */   implements BeanFactoryAware
/*    */ {
/* 33 */   private final AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
/*    */ 
/*    */   
/*    */   public void setExpression(String expression) {
/* 37 */     this.pointcut.setExpression(expression);
/*    */   }
/*    */   
/*    */   public String getExpression() {
/* 41 */     return this.pointcut.getExpression();
/*    */   }
/*    */   
/*    */   public void setLocation(String location) {
/* 45 */     this.pointcut.setLocation(location);
/*    */   }
/*    */   
/*    */   public String getLocation() {
/* 49 */     return this.pointcut.getLocation();
/*    */   }
/*    */   
/*    */   public void setParameterNames(String... names) {
/* 53 */     this.pointcut.setParameterNames(names);
/*    */   }
/*    */   
/*    */   public void setParameterTypes(Class<?>... types) {
/* 57 */     this.pointcut.setParameterTypes(types);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setBeanFactory(BeanFactory beanFactory) {
/* 62 */     this.pointcut.setBeanFactory(beanFactory);
/*    */   }
/*    */ 
/*    */   
/*    */   public Pointcut getPointcut() {
/* 67 */     return (Pointcut)this.pointcut;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\AspectJExpressionPointcutAdvisor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */