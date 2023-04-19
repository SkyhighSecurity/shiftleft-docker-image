package org.springframework.web.method.support;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;

public interface HandlerMethodReturnValueHandler {
  boolean supportsReturnType(MethodParameter paramMethodParameter);
  
  void handleReturnValue(Object paramObject, MethodParameter paramMethodParameter, ModelAndViewContainer paramModelAndViewContainer, NativeWebRequest paramNativeWebRequest) throws Exception;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\method\support\HandlerMethodReturnValueHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */