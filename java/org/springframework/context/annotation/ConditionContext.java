package org.springframework.context.annotation;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

public interface ConditionContext {
  BeanDefinitionRegistry getRegistry();
  
  ConfigurableListableBeanFactory getBeanFactory();
  
  Environment getEnvironment();
  
  ResourceLoader getResourceLoader();
  
  ClassLoader getClassLoader();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\ConditionContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */