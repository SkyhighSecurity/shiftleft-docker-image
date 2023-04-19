/*    */ package org.springframework.aop.aspectj;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.lang.reflect.Method;
/*    */ import org.aopalliance.intercept.MethodInterceptor;
/*    */ import org.aopalliance.intercept.MethodInvocation;
/*    */ import org.springframework.aop.AfterAdvice;
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
/*    */ public class AspectJAfterAdvice
/*    */   extends AbstractAspectJAdvice
/*    */   implements MethodInterceptor, AfterAdvice, Serializable
/*    */ {
/*    */   public AspectJAfterAdvice(Method aspectJBeforeAdviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory aif) {
/* 40 */     super(aspectJBeforeAdviceMethod, pointcut, aif);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object invoke(MethodInvocation mi) throws Throwable {
/*    */     try {
/* 47 */       return mi.proceed();
/*    */     } finally {
/*    */       
/* 50 */       invokeAdviceMethod(getJoinPointMatch(), null, null);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBeforeAdvice() {
/* 56 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAfterAdvice() {
/* 61 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\AspectJAfterAdvice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */