/*    */ package org.springframework.aop.interceptor;
/*    */ 
/*    */ import org.aopalliance.intercept.MethodInvocation;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DebugInterceptor
/*    */   extends SimpleTraceInterceptor
/*    */ {
/*    */   private volatile long count;
/*    */   
/*    */   public DebugInterceptor() {}
/*    */   
/*    */   public DebugInterceptor(boolean useDynamicLogger) {
/* 54 */     setUseDynamicLogger(useDynamicLogger);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object invoke(MethodInvocation invocation) throws Throwable {
/* 60 */     synchronized (this) {
/* 61 */       this.count++;
/*    */     } 
/* 63 */     return super.invoke(invocation);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getInvocationDescription(MethodInvocation invocation) {
/* 68 */     return invocation + "; count=" + this.count;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getCount() {
/* 76 */     return this.count;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void resetCount() {
/* 83 */     this.count = 0L;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\interceptor\DebugInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */