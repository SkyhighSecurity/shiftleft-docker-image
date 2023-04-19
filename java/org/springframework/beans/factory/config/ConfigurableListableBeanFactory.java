package org.springframework.beans.factory.config;

import java.util.Iterator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {
  void ignoreDependencyType(Class<?> paramClass);
  
  void ignoreDependencyInterface(Class<?> paramClass);
  
  void registerResolvableDependency(Class<?> paramClass, Object paramObject);
  
  boolean isAutowireCandidate(String paramString, DependencyDescriptor paramDependencyDescriptor) throws NoSuchBeanDefinitionException;
  
  BeanDefinition getBeanDefinition(String paramString) throws NoSuchBeanDefinitionException;
  
  Iterator<String> getBeanNamesIterator();
  
  void clearMetadataCache();
  
  void freezeConfiguration();
  
  boolean isConfigurationFrozen();
  
  void preInstantiateSingletons() throws BeansException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\ConfigurableListableBeanFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */