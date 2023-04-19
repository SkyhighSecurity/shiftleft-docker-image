/*    */ package org.springframework.web.context.request.async;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
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
/*    */ class DeferredResultInterceptorChain
/*    */ {
/* 34 */   private static final Log logger = LogFactory.getLog(DeferredResultInterceptorChain.class);
/*    */   
/*    */   private final List<DeferredResultProcessingInterceptor> interceptors;
/*    */   
/* 38 */   private int preProcessingIndex = -1;
/*    */ 
/*    */   
/*    */   public DeferredResultInterceptorChain(List<DeferredResultProcessingInterceptor> interceptors) {
/* 42 */     this.interceptors = interceptors;
/*    */   }
/*    */   
/*    */   public void applyBeforeConcurrentHandling(NativeWebRequest request, DeferredResult<?> deferredResult) throws Exception {
/* 46 */     for (DeferredResultProcessingInterceptor interceptor : this.interceptors) {
/* 47 */       interceptor.beforeConcurrentHandling(request, deferredResult);
/*    */     }
/*    */   }
/*    */   
/*    */   public void applyPreProcess(NativeWebRequest request, DeferredResult<?> deferredResult) throws Exception {
/* 52 */     for (DeferredResultProcessingInterceptor interceptor : this.interceptors) {
/* 53 */       interceptor.preProcess(request, deferredResult);
/* 54 */       this.preProcessingIndex++;
/*    */     } 
/*    */   }
/*    */   
/*    */   public Object applyPostProcess(NativeWebRequest request, DeferredResult<?> deferredResult, Object concurrentResult) {
/*    */     try {
/* 60 */       for (int i = this.preProcessingIndex; i >= 0; i--) {
/* 61 */         ((DeferredResultProcessingInterceptor)this.interceptors.get(i)).postProcess(request, deferredResult, concurrentResult);
/*    */       }
/*    */     }
/* 64 */     catch (Throwable t) {
/* 65 */       return t;
/*    */     } 
/* 67 */     return concurrentResult;
/*    */   }
/*    */   
/*    */   public void triggerAfterTimeout(NativeWebRequest request, DeferredResult<?> deferredResult) throws Exception {
/* 71 */     for (DeferredResultProcessingInterceptor interceptor : this.interceptors) {
/* 72 */       if (deferredResult.isSetOrExpired()) {
/*    */         return;
/*    */       }
/* 75 */       if (!interceptor.handleTimeout(request, deferredResult)) {
/*    */         break;
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public void triggerAfterCompletion(NativeWebRequest request, DeferredResult<?> deferredResult) {
/* 82 */     for (int i = this.preProcessingIndex; i >= 0; i--) {
/*    */       try {
/* 84 */         ((DeferredResultProcessingInterceptor)this.interceptors.get(i)).afterCompletion(request, deferredResult);
/*    */       }
/* 86 */       catch (Throwable t) {
/* 87 */         logger.error("afterCompletion error", t);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\async\DeferredResultInterceptorChain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */