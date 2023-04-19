package org.springframework.beans.factory;

import org.springframework.beans.BeansException;

public interface BeanFactoryAware extends Aware {
  void setBeanFactory(BeanFactory paramBeanFactory) throws BeansException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\BeanFactoryAware.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */