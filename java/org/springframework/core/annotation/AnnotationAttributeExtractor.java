package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

interface AnnotationAttributeExtractor<S> {
  Class<? extends Annotation> getAnnotationType();
  
  Object getAnnotatedElement();
  
  S getSource();
  
  Object getAttributeValue(Method paramMethod);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\annotation\AnnotationAttributeExtractor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */