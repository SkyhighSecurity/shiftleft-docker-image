/*    */ package org.springframework.aop.framework.adapter;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.aopalliance.intercept.MethodInterceptor;
/*    */ import org.aopalliance.intercept.MethodInvocation;
/*    */ import org.springframework.aop.AfterAdvice;
/*    */ import org.springframework.aop.AfterReturningAdvice;
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
/*    */ 
/*    */ public class AfterReturningAdviceInterceptor
/*    */   implements MethodInterceptor, AfterAdvice, Serializable
/*    */ {
/*    */   private final AfterReturningAdvice advice;
/*    */   
/*    */   public AfterReturningAdviceInterceptor(AfterReturningAdvice advice) {
/* 48 */     Assert.notNull(advice, "Advice must not be null");
/* 49 */     this.advice = advice;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object invoke(MethodInvocation mi) throws Throwable {
/* 55 */     Object retVal = mi.proceed();
/* 56 */     this.advice.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
/* 57 */     return retVal;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\adapter\AfterReturningAdviceInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */