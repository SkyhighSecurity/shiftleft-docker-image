/*    */ package org.springframework.aop.interceptor;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ public class SimpleAsyncUncaughtExceptionHandler
/*    */   implements AsyncUncaughtExceptionHandler
/*    */ {
/* 32 */   private static final Log logger = LogFactory.getLog(SimpleAsyncUncaughtExceptionHandler.class);
/*    */ 
/*    */ 
/*    */   
/*    */   public void handleUncaughtException(Throwable ex, Method method, Object... params) {
/* 37 */     if (logger.isErrorEnabled())
/* 38 */       logger.error("Unexpected error occurred invoking async method: " + method, ex); 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\interceptor\SimpleAsyncUncaughtExceptionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */