package org.springframework.cglib.proxy;

import java.lang.reflect.Method;

public interface InvocationHandler extends Callback {
  Object invoke(Object paramObject, Method paramMethod, Object[] paramArrayOfObject) throws Throwable;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cglib\proxy\InvocationHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */