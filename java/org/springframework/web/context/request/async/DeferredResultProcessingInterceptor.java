package org.springframework.web.context.request.async;

import org.springframework.web.context.request.NativeWebRequest;

public interface DeferredResultProcessingInterceptor {
  <T> void beforeConcurrentHandling(NativeWebRequest paramNativeWebRequest, DeferredResult<T> paramDeferredResult) throws Exception;
  
  <T> void preProcess(NativeWebRequest paramNativeWebRequest, DeferredResult<T> paramDeferredResult) throws Exception;
  
  <T> void postProcess(NativeWebRequest paramNativeWebRequest, DeferredResult<T> paramDeferredResult, Object paramObject) throws Exception;
  
  <T> boolean handleTimeout(NativeWebRequest paramNativeWebRequest, DeferredResult<T> paramDeferredResult) throws Exception;
  
  <T> void afterCompletion(NativeWebRequest paramNativeWebRequest, DeferredResult<T> paramDeferredResult) throws Exception;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\async\DeferredResultProcessingInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */