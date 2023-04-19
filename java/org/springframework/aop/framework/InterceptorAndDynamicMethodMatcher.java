/*    */ package org.springframework.aop.framework;
/*    */ 
/*    */ import org.aopalliance.intercept.MethodInterceptor;
/*    */ import org.springframework.aop.MethodMatcher;
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
/*    */ class InterceptorAndDynamicMethodMatcher
/*    */ {
/*    */   final MethodInterceptor interceptor;
/*    */   final MethodMatcher methodMatcher;
/*    */   
/*    */   public InterceptorAndDynamicMethodMatcher(MethodInterceptor interceptor, MethodMatcher methodMatcher) {
/* 36 */     this.interceptor = interceptor;
/* 37 */     this.methodMatcher = methodMatcher;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\InterceptorAndDynamicMethodMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */