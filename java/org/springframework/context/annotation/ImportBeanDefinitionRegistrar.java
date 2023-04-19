package org.springframework.context.annotation;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.type.AnnotationMetadata;

public interface ImportBeanDefinitionRegistrar {
  void registerBeanDefinitions(AnnotationMetadata paramAnnotationMetadata, BeanDefinitionRegistry paramBeanDefinitionRegistry);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\ImportBeanDefinitionRegistrar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */