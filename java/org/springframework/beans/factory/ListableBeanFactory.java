package org.springframework.beans.factory;

import java.lang.annotation.Annotation;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.core.ResolvableType;

public interface ListableBeanFactory extends BeanFactory {
  boolean containsBeanDefinition(String paramString);
  
  int getBeanDefinitionCount();
  
  String[] getBeanDefinitionNames();
  
  String[] getBeanNamesForType(ResolvableType paramResolvableType);
  
  String[] getBeanNamesForType(Class<?> paramClass);
  
  String[] getBeanNamesForType(Class<?> paramClass, boolean paramBoolean1, boolean paramBoolean2);
  
  <T> Map<String, T> getBeansOfType(Class<T> paramClass) throws BeansException;
  
  <T> Map<String, T> getBeansOfType(Class<T> paramClass, boolean paramBoolean1, boolean paramBoolean2) throws BeansException;
  
  String[] getBeanNamesForAnnotation(Class<? extends Annotation> paramClass);
  
  Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> paramClass) throws BeansException;
  
  <A extends Annotation> A findAnnotationOnBean(String paramString, Class<A> paramClass) throws NoSuchBeanDefinitionException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\ListableBeanFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */