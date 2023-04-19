package org.springframework.web.context.request;

public interface NativeWebRequest extends WebRequest {
  Object getNativeRequest();
  
  Object getNativeResponse();
  
  <T> T getNativeRequest(Class<T> paramClass);
  
  <T> T getNativeResponse(Class<T> paramClass);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\NativeWebRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */