package org.springframework.core.type.filter;

import java.io.IOException;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

public interface TypeFilter {
  boolean match(MetadataReader paramMetadataReader, MetadataReaderFactory paramMetadataReaderFactory) throws IOException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\type\filter\TypeFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */