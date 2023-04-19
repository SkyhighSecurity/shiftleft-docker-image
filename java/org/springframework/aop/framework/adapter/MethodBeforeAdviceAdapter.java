/*    */ package org.springframework.aop.framework.adapter;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.aopalliance.aop.Advice;
/*    */ import org.aopalliance.intercept.MethodInterceptor;
/*    */ import org.springframework.aop.Advisor;
/*    */ import org.springframework.aop.MethodBeforeAdvice;
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
/*    */ class MethodBeforeAdviceAdapter
/*    */   implements AdvisorAdapter, Serializable
/*    */ {
/*    */   public boolean supportsAdvice(Advice advice) {
/* 39 */     return advice instanceof MethodBeforeAdvice;
/*    */   }
/*    */ 
/*    */   
/*    */   public MethodInterceptor getInterceptor(Advisor advisor) {
/* 44 */     MethodBeforeAdvice advice = (MethodBeforeAdvice)advisor.getAdvice();
/* 45 */     return new MethodBeforeAdviceInterceptor(advice);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\adapter\MethodBeforeAdviceAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */