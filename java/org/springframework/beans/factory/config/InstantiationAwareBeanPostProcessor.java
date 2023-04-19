package org.springframework.beans.factory.config;

import java.beans.PropertyDescriptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {
  Object postProcessBeforeInstantiation(Class<?> paramClass, String paramString) throws BeansException;
  
  boolean postProcessAfterInstantiation(Object paramObject, String paramString) throws BeansException;
  
  PropertyValues postProcessPropertyValues(PropertyValues paramPropertyValues, PropertyDescriptor[] paramArrayOfPropertyDescriptor, Object paramObject, String paramString) throws BeansException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\InstantiationAwareBeanPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */