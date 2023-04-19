package org.springframework.beans.factory.config;

import java.lang.reflect.Constructor;
import org.springframework.beans.BeansException;

public interface SmartInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessor {
  Class<?> predictBeanType(Class<?> paramClass, String paramString) throws BeansException;
  
  Constructor<?>[] determineCandidateConstructors(Class<?> paramClass, String paramString) throws BeansException;
  
  Object getEarlyBeanReference(Object paramObject, String paramString) throws BeansException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\SmartInstantiationAwareBeanPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */