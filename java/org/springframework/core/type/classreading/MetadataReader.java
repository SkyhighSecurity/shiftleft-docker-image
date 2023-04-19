package org.springframework.core.type.classreading;

import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;

public interface MetadataReader {
  Resource getResource();
  
  ClassMetadata getClassMetadata();
  
  AnnotationMetadata getAnnotationMetadata();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\type\classreading\MetadataReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */