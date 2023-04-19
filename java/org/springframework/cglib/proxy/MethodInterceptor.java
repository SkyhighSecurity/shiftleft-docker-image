package org.springframework.cglib.proxy;

import java.lang.reflect.Method;

public interface MethodInterceptor extends Callback {
  Object intercept(Object paramObject, Method paramMethod, Object[] paramArrayOfObject, MethodProxy paramMethodProxy) throws Throwable;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cglib\proxy\MethodInterceptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */