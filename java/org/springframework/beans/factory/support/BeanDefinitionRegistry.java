package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.AliasRegistry;

public interface BeanDefinitionRegistry extends AliasRegistry {
  void registerBeanDefinition(String paramString, BeanDefinition paramBeanDefinition) throws BeanDefinitionStoreException;
  
  void removeBeanDefinition(String paramString) throws NoSuchBeanDefinitionException;
  
  BeanDefinition getBeanDefinition(String paramString) throws NoSuchBeanDefinitionException;
  
  boolean containsBeanDefinition(String paramString);
  
  String[] getBeanDefinitionNames();
  
  int getBeanDefinitionCount();
  
  boolean isBeanNameInUse(String paramString);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\BeanDefinitionRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */