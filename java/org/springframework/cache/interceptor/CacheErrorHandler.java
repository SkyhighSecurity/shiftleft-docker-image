package org.springframework.cache.interceptor;

import org.springframework.cache.Cache;

public interface CacheErrorHandler {
  void handleCacheGetError(RuntimeException paramRuntimeException, Cache paramCache, Object paramObject);
  
  void handleCachePutError(RuntimeException paramRuntimeException, Cache paramCache, Object paramObject1, Object paramObject2);
  
  void handleCacheEvictError(RuntimeException paramRuntimeException, Cache paramCache, Object paramObject);
  
  void handleCacheClearError(RuntimeException paramRuntimeException, Cache paramCache);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\interceptor\CacheErrorHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */