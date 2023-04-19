package org.springframework.web.context.request.async;

import org.springframework.web.context.request.NativeWebRequest;

public interface AsyncWebRequest extends NativeWebRequest {
  void setTimeout(Long paramLong);
  
  void addTimeoutHandler(Runnable paramRunnable);
  
  void addCompletionHandler(Runnable paramRunnable);
  
  void startAsync();
  
  boolean isAsyncStarted();
  
  void dispatch();
  
  boolean isAsyncComplete();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\async\AsyncWebRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */