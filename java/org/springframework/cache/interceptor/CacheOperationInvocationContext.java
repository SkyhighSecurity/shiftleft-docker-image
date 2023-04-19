package org.springframework.cache.interceptor;

import java.lang.reflect.Method;

public interface CacheOperationInvocationContext<O extends BasicOperation> {
  O getOperation();
  
  Object getTarget();
  
  Method getMethod();
  
  Object[] getArgs();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\interceptor\CacheOperationInvocationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */