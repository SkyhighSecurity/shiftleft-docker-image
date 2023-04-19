/*    */ package org.springframework.aop.support;
/*    */ 
/*    */ import org.aopalliance.aop.Advice;
/*    */ import org.springframework.aop.ClassFilter;
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
/*    */ public class NameMatchMethodPointcutAdvisor
/*    */   extends AbstractGenericPointcutAdvisor
/*    */ {
/* 35 */   private final NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
/*    */ 
/*    */   
/*    */   public NameMatchMethodPointcutAdvisor() {}
/*    */ 
/*    */   
/*    */   public NameMatchMethodPointcutAdvisor(Advice advice) {
/* 42 */     setAdvice(advice);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setClassFilter(ClassFilter classFilter) {
/* 52 */     this.pointcut.setClassFilter(classFilter);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setMappedName(String mappedName) {
/* 62 */     this.pointcut.setMappedName(mappedName);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setMappedNames(String... mappedNames) {
/* 72 */     this.pointcut.setMappedNames(mappedNames);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NameMatchMethodPointcut addMethodName(String name) {
/* 84 */     return this.pointcut.addMethodName(name);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Pointcut getPointcut() {
/* 90 */     return this.pointcut;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\NameMatchMethodPointcutAdvisor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */