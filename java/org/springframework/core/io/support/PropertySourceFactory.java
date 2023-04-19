package org.springframework.core.io.support;

import java.io.IOException;
import org.springframework.core.env.PropertySource;

public interface PropertySourceFactory {
  PropertySource<?> createPropertySource(String paramString, EncodedResource paramEncodedResource) throws IOException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\support\PropertySourceFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */