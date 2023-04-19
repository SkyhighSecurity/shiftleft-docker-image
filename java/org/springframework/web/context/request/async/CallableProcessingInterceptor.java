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
/*    */ 
/*    */ public interface CallableProcessingInterceptor
/*    */ {
/* 53 */   public static final Object RESULT_NONE = new Object();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   public static final Object RESPONSE_HANDLED = new Object();
/*    */   
/*    */   <T> void beforeConcurrentHandling(NativeWebRequest paramNativeWebRequest, Callable<T> paramCallable) throws Exception;
/*    */   
/*    */   <T> void preProcess(NativeWebRequest paramNativeWebRequest, Callable<T> paramCallable) throws Exception;
/*    */   
/*    */   <T> void postProcess(NativeWebRequest paramNativeWebRequest, Callable<T> paramCallable, Object paramObject) throws Exception;
/*    */   
/*    */   <T> Object handleTimeout(NativeWebRequest paramNativeWebRequest, Callable<T> paramCallable) throws Exception;
/*    */   
/*    */   <T> void afterCompletion(NativeWebRequest paramNativeWebRequest, Callable<T> paramCallable) throws Exception;
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\async\CallableProcessingInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */