package org.springframework.aop;

import java.lang.reflect.Method;

public interface AfterReturningAdvice extends AfterAdvice {
  void afterReturning(Object paramObject1, Method paramMethod, Object[] paramArrayOfObject, Object paramObject2) throws Throwable;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\AfterReturningAdvice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */