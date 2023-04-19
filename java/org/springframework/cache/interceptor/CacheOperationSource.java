package org.springframework.cache.interceptor;

import java.lang.reflect.Method;
import java.util.Collection;

public interface CacheOperationSource {
  Collection<CacheOperation> getCacheOperations(Method paramMethod, Class<?> paramClass);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\interceptor\CacheOperationSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */