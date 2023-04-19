package org.springframework.web.method.support;

import org.springframework.core.MethodParameter;

public interface AsyncHandlerMethodReturnValueHandler extends HandlerMethodReturnValueHandler {
  boolean isAsyncReturnValue(Object paramObject, MethodParameter paramMethodParameter);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\method\support\AsyncHandlerMethodReturnValueHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */