package org.springframework.core.type;

import java.util.Set;

public interface AnnotationMetadata extends ClassMetadata, AnnotatedTypeMetadata {
  Set<String> getAnnotationTypes();
  
  Set<String> getMetaAnnotationTypes(String paramString);
  
  boolean hasAnnotation(String paramString);
  
  boolean hasMetaAnnotation(String paramString);
  
  boolean hasAnnotatedMethods(String paramString);
  
  Set<MethodMetadata> getAnnotatedMethods(String paramString);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\type\AnnotationMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */