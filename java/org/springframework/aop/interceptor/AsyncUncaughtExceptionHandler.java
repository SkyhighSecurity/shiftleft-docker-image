package org.springframework.aop.interceptor;

import java.lang.reflect.Method;

public interface AsyncUncaughtExceptionHandler {
  void handleUncaughtException(Throwable paramThrowable, Method paramMethod, Object... paramVarArgs);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\interceptor\AsyncUncaughtExceptionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */