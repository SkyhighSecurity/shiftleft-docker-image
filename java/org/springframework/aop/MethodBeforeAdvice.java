package org.springframework.aop;

import java.lang.reflect.Method;

public interface MethodBeforeAdvice extends BeforeAdvice {
  void before(Method paramMethod, Object[] paramArrayOfObject, Object paramObject) throws Throwable;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\MethodBeforeAdvice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */