package org.springframework.context;

import org.springframework.beans.factory.Aware;
import org.springframework.core.io.ResourceLoader;

public interface ResourceLoaderAware extends Aware {
  void setResourceLoader(ResourceLoader paramResourceLoader);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\ResourceLoaderAware.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */