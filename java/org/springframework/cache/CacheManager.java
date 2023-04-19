package org.springframework.cache;

import java.util.Collection;

public interface CacheManager {
  Cache getCache(String paramString);
  
  Collection<String> getCacheNames();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\CacheManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */