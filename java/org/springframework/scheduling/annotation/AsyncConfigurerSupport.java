/*    */ package org.springframework.scheduling.annotation;
/*    */ 
/*    */ import java.util.concurrent.Executor;
/*    */ import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
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
/*    */ public class AsyncConfigurerSupport
/*    */   implements AsyncConfigurer
/*    */ {
/*    */   public Executor getAsyncExecutor() {
/* 35 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
/* 40 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\annotation\AsyncConfigurerSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */