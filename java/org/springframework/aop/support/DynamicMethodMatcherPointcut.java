/*    */ package org.springframework.aop.support;
/*    */ 
/*    */ import org.springframework.aop.ClassFilter;
/*    */ import org.springframework.aop.MethodMatcher;
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
/*    */ public abstract class DynamicMethodMatcherPointcut
/*    */   extends DynamicMethodMatcher
/*    */   implements Pointcut
/*    */ {
/*    */   public ClassFilter getClassFilter() {
/* 35 */     return ClassFilter.TRUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public final MethodMatcher getMethodMatcher() {
/* 40 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\DynamicMethodMatcherPointcut.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */