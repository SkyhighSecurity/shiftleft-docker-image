/*    */ package org.springframework.web.context.request.async;
/*    */ 
/*    */ import java.util.concurrent.Callable;
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
/*    */ public abstract class CallableProcessingInterceptorAdapter
/*    */   implements CallableProcessingInterceptor
/*    */ {
/*    */   public <T> void beforeConcurrentHandling(NativeWebRequest request, Callable<T> task) throws Exception {}
/*    */   
/*    */   public <T> void preProcess(NativeWebRequest request, Callable<T> task) throws Exception {}
/*    */   
/*    */   public <T> void postProcess(NativeWebRequest request, Callable<T> task, Object concurrentResult) throws Exception {}
/*    */   
/*    */   public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
/* 60 */     return RESULT_NONE;
/*    */   }
/*    */   
/*    */   public <T> void afterCompletion(NativeWebRequest request, Callable<T> task) throws Exception {}
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\async\CallableProcessingInterceptorAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */