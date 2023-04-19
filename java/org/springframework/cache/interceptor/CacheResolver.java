package org.springframework.cache.interceptor;

import java.util.Collection;
import org.springframework.cache.Cache;

public interface CacheResolver {
  Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> paramCacheOperationInvocationContext);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\interceptor\CacheResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */