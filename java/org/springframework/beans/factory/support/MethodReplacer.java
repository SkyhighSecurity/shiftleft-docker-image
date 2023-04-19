package org.springframework.beans.factory.support;

import java.lang.reflect.Method;

public interface MethodReplacer {
  Object reimplement(Object paramObject, Method paramMethod, Object[] paramArrayOfObject) throws Throwable;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\MethodReplacer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */