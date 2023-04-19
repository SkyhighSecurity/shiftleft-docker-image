package org.springframework.core.type.classreading;

import java.io.IOException;
import org.springframework.core.io.Resource;

public interface MetadataReaderFactory {
  MetadataReader getMetadataReader(String paramString) throws IOException;
  
  MetadataReader getMetadataReader(Resource paramResource) throws IOException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\type\classreading\MetadataReaderFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */