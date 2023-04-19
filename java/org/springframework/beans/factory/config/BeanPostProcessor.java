package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

public interface BeanPostProcessor {
  Object postProcessBeforeInitialization(Object paramObject, String paramString) throws BeansException;
  
  Object postProcessAfterInitialization(Object paramObject, String paramString) throws BeansException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\BeanPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */