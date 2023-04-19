package org.springframework.cache.annotation;

import java.lang.reflect.Method;
import java.util.Collection;
import org.springframework.cache.interceptor.CacheOperation;

public interface CacheAnnotationParser {
  Collection<CacheOperation> parseCacheAnnotations(Class<?> paramClass);
  
  Collection<CacheOperation> parseCacheAnnotations(Method paramMethod);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\annotation\CacheAnnotationParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */