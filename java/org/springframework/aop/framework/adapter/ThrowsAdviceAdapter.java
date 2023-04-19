/*    */ package org.springframework.aop.framework.adapter;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.aopalliance.aop.Advice;
/*    */ import org.aopalliance.intercept.MethodInterceptor;
/*    */ import org.springframework.aop.Advisor;
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
/*    */ class ThrowsAdviceAdapter
/*    */   implements AdvisorAdapter, Serializable
/*    */ {
/*    */   public boolean supportsAdvice(Advice advice) {
/* 39 */     return advice instanceof org.springframework.aop.ThrowsAdvice;
/*    */   }
/*    */ 
/*    */   
/*    */   public MethodInterceptor getInterceptor(Advisor advisor) {
/* 44 */     return new ThrowsAdviceInterceptor(advisor.getAdvice());
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\adapter\ThrowsAdviceAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */