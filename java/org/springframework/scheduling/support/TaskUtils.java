/*     */ package org.springframework.scheduling.support;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.ErrorHandler;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TaskUtils
/*     */ {
/*  45 */   public static final ErrorHandler LOG_AND_SUPPRESS_ERROR_HANDLER = new LoggingErrorHandler();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   public static final ErrorHandler LOG_AND_PROPAGATE_ERROR_HANDLER = new PropagatingErrorHandler();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DelegatingErrorHandlingRunnable decorateTaskWithErrorHandler(Runnable task, ErrorHandler errorHandler, boolean isRepeatingTask) {
/*  65 */     if (task instanceof DelegatingErrorHandlingRunnable) {
/*  66 */       return (DelegatingErrorHandlingRunnable)task;
/*     */     }
/*  68 */     ErrorHandler eh = (errorHandler != null) ? errorHandler : getDefaultErrorHandler(isRepeatingTask);
/*  69 */     return new DelegatingErrorHandlingRunnable(task, eh);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ErrorHandler getDefaultErrorHandler(boolean isRepeatingTask) {
/*  79 */     return isRepeatingTask ? LOG_AND_SUPPRESS_ERROR_HANDLER : LOG_AND_PROPAGATE_ERROR_HANDLER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class LoggingErrorHandler
/*     */     implements ErrorHandler
/*     */   {
/*  90 */     private final Log logger = LogFactory.getLog(LoggingErrorHandler.class);
/*     */ 
/*     */     
/*     */     public void handleError(Throwable t) {
/*  94 */       if (this.logger.isErrorEnabled()) {
/*  95 */         this.logger.error("Unexpected error occurred in scheduled task.", t);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     private LoggingErrorHandler() {}
/*     */   }
/*     */   
/*     */   private static class PropagatingErrorHandler
/*     */     extends LoggingErrorHandler
/*     */   {
/*     */     private PropagatingErrorHandler() {}
/*     */     
/*     */     public void handleError(Throwable t) {
/* 109 */       super.handleError(t);
/* 110 */       ReflectionUtils.rethrowRuntimeException(t);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\support\TaskUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */