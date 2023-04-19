/*    */ package org.springframework.aop.interceptor;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.aopalliance.intercept.MethodInterceptor;
/*    */ import org.aopalliance.intercept.MethodInvocation;
/*    */ import org.springframework.util.ConcurrencyThrottleSupport;
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
/*    */ 
/*    */ 
/*    */ public class ConcurrencyThrottleInterceptor
/*    */   extends ConcurrencyThrottleSupport
/*    */   implements MethodInterceptor, Serializable
/*    */ {
/*    */   public ConcurrencyThrottleInterceptor() {
/* 47 */     setConcurrencyLimit(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object invoke(MethodInvocation methodInvocation) throws Throwable {
/* 52 */     beforeAccess();
/*    */     try {
/* 54 */       return methodInvocation.proceed();
/*    */     } finally {
/*    */       
/* 57 */       afterAccess();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\interceptor\ConcurrencyThrottleInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */