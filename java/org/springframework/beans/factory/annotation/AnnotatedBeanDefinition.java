package org.springframework.beans.factory.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;

public interface AnnotatedBeanDefinition extends BeanDefinition {
  AnnotationMetadata getMetadata();
  
  MethodMetadata getFactoryMethodMetadata();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\annotation\AnnotatedBeanDefinition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */