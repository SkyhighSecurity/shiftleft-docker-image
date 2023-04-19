package org.springframework.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.Aware;

public interface ApplicationContextAware extends Aware {
  void setApplicationContext(ApplicationContext paramApplicationContext) throws BeansException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\ApplicationContextAware.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */