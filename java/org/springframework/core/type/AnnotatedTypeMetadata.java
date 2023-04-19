package org.springframework.core.type;

import java.util.Map;
import org.springframework.util.MultiValueMap;

public interface AnnotatedTypeMetadata {
  boolean isAnnotated(String paramString);
  
  Map<String, Object> getAnnotationAttributes(String paramString);
  
  Map<String, Object> getAnnotationAttributes(String paramString, boolean paramBoolean);
  
  MultiValueMap<String, Object> getAllAnnotationAttributes(String paramString);
  
  MultiValueMap<String, Object> getAllAnnotationAttributes(String paramString, boolean paramBoolean);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\type\AnnotatedTypeMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */