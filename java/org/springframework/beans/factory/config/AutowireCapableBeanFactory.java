package org.springframework.beans.factory.config;

import java.util.Set;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanFactory;

public interface AutowireCapableBeanFactory extends BeanFactory {
  public static final int AUTOWIRE_NO = 0;
  
  public static final int AUTOWIRE_BY_NAME = 1;
  
  public static final int AUTOWIRE_BY_TYPE = 2;
  
  public static final int AUTOWIRE_CONSTRUCTOR = 3;
  
  @Deprecated
  public static final int AUTOWIRE_AUTODETECT = 4;
  
  <T> T createBean(Class<T> paramClass) throws BeansException;
  
  void autowireBean(Object paramObject) throws BeansException;
  
  Object configureBean(Object paramObject, String paramString) throws BeansException;
  
  Object createBean(Class<?> paramClass, int paramInt, boolean paramBoolean) throws BeansException;
  
  Object autowire(Class<?> paramClass, int paramInt, boolean paramBoolean) throws BeansException;
  
  void autowireBeanProperties(Object paramObject, int paramInt, boolean paramBoolean) throws BeansException;
  
  void applyBeanPropertyValues(Object paramObject, String paramString) throws BeansException;
  
  Object initializeBean(Object paramObject, String paramString) throws BeansException;
  
  Object applyBeanPostProcessorsBeforeInitialization(Object paramObject, String paramString) throws BeansException;
  
  Object applyBeanPostProcessorsAfterInitialization(Object paramObject, String paramString) throws BeansException;
  
  void destroyBean(Object paramObject);
  
  <T> NamedBeanHolder<T> resolveNamedBean(Class<T> paramClass) throws BeansException;
  
  Object resolveDependency(DependencyDescriptor paramDependencyDescriptor, String paramString) throws BeansException;
  
  Object resolveDependency(DependencyDescriptor paramDependencyDescriptor, String paramString, Set<String> paramSet, TypeConverter paramTypeConverter) throws BeansException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\AutowireCapableBeanFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */