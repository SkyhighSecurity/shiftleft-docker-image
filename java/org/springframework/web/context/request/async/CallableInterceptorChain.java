/*     */ package org.springframework.web.context.request.async;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Future;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
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
/*     */ class CallableInterceptorChain
/*     */ {
/*  37 */   private static final Log logger = LogFactory.getLog(CallableInterceptorChain.class);
/*     */   
/*     */   private final List<CallableProcessingInterceptor> interceptors;
/*     */   
/*  41 */   private int preProcessIndex = -1;
/*     */   
/*     */   private volatile Future<?> taskFuture;
/*     */ 
/*     */   
/*     */   public CallableInterceptorChain(List<CallableProcessingInterceptor> interceptors) {
/*  47 */     this.interceptors = interceptors;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTaskFuture(Future<?> taskFuture) {
/*  52 */     this.taskFuture = taskFuture;
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyBeforeConcurrentHandling(NativeWebRequest request, Callable<?> task) throws Exception {
/*  57 */     for (CallableProcessingInterceptor interceptor : this.interceptors) {
/*  58 */       interceptor.beforeConcurrentHandling(request, task);
/*     */     }
/*     */   }
/*     */   
/*     */   public void applyPreProcess(NativeWebRequest request, Callable<?> task) throws Exception {
/*  63 */     for (CallableProcessingInterceptor interceptor : this.interceptors) {
/*  64 */       interceptor.preProcess(request, task);
/*  65 */       this.preProcessIndex++;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object applyPostProcess(NativeWebRequest request, Callable<?> task, Object concurrentResult) {
/*  70 */     Throwable exceptionResult = null;
/*  71 */     for (int i = this.preProcessIndex; i >= 0; i--) {
/*     */       try {
/*  73 */         ((CallableProcessingInterceptor)this.interceptors.get(i)).postProcess(request, task, concurrentResult);
/*     */       }
/*  75 */       catch (Throwable t) {
/*     */         
/*  77 */         if (exceptionResult != null) {
/*  78 */           logger.error("postProcess error", t);
/*     */         } else {
/*     */           
/*  81 */           exceptionResult = t;
/*     */         } 
/*     */       } 
/*     */     } 
/*  85 */     return (exceptionResult != null) ? exceptionResult : concurrentResult;
/*     */   }
/*     */   
/*     */   public Object triggerAfterTimeout(NativeWebRequest request, Callable<?> task) {
/*  89 */     cancelTask();
/*  90 */     for (CallableProcessingInterceptor interceptor : this.interceptors) {
/*     */       try {
/*  92 */         Object result = interceptor.handleTimeout(request, task);
/*  93 */         if (result == CallableProcessingInterceptor.RESPONSE_HANDLED) {
/*     */           break;
/*     */         }
/*  96 */         if (result != CallableProcessingInterceptor.RESULT_NONE) {
/*  97 */           return result;
/*     */         }
/*     */       }
/* 100 */       catch (Throwable t) {
/* 101 */         return t;
/*     */       } 
/*     */     } 
/* 104 */     return CallableProcessingInterceptor.RESULT_NONE;
/*     */   }
/*     */   
/*     */   private void cancelTask() {
/* 108 */     Future<?> future = this.taskFuture;
/* 109 */     if (future != null) {
/*     */       try {
/* 111 */         future.cancel(true);
/*     */       }
/* 113 */       catch (Throwable throwable) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void triggerAfterCompletion(NativeWebRequest request, Callable<?> task) {
/* 120 */     for (int i = this.interceptors.size() - 1; i >= 0; i--) {
/*     */       try {
/* 122 */         ((CallableProcessingInterceptor)this.interceptors.get(i)).afterCompletion(request, task);
/*     */       }
/* 124 */       catch (Throwable t) {
/* 125 */         logger.error("afterCompletion error", t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\async\CallableInterceptorChain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */