package org.springframework.cache.annotation;

import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;

public interface CachingConfigurer {
  CacheManager cacheManager();
  
  CacheResolver cacheResolver();
  
  KeyGenerator keyGenerator();
  
  CacheErrorHandler errorHandler();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\annotation\CachingConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */