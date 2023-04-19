package org.springframework.cache.interceptor;

import java.lang.reflect.Method;

public interface KeyGenerator {
  Object generate(Object paramObject, Method paramMethod, Object... paramVarArgs);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\interceptor\KeyGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */