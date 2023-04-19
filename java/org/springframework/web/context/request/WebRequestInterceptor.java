package org.springframework.web.context.request;

import org.springframework.ui.ModelMap;

public interface WebRequestInterceptor {
  void preHandle(WebRequest paramWebRequest) throws Exception;
  
  void postHandle(WebRequest paramWebRequest, ModelMap paramModelMap) throws Exception;
  
  void afterCompletion(WebRequest paramWebRequest, Exception paramException) throws Exception;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\WebRequestInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */