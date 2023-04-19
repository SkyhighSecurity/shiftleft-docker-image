package org.springframework.web.context.request;

public interface AsyncWebRequestInterceptor extends WebRequestInterceptor {
  void afterConcurrentHandlingStarted(WebRequest paramWebRequest);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\AsyncWebRequestInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */