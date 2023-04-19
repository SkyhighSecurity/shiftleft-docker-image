package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public interface BeanDefinitionReader {
  BeanDefinitionRegistry getRegistry();
  
  ResourceLoader getResourceLoader();
  
  ClassLoader getBeanClassLoader();
  
  BeanNameGenerator getBeanNameGenerator();
  
  int loadBeanDefinitions(Resource paramResource) throws BeanDefinitionStoreException;
  
  int loadBeanDefinitions(Resource... paramVarArgs) throws BeanDefinitionStoreException;
  
  int loadBeanDefinitions(String paramString) throws BeanDefinitionStoreException;
  
  int loadBeanDefinitions(String... paramVarArgs) throws BeanDefinitionStoreException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\BeanDefinitionReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */