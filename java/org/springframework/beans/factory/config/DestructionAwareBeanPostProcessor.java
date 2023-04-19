package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

public interface DestructionAwareBeanPostProcessor extends BeanPostProcessor {
  void postProcessBeforeDestruction(Object paramObject, String paramString) throws BeansException;
  
  boolean requiresDestruction(Object paramObject);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\DestructionAwareBeanPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */