package org.springframework.context.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.ResolvableType;

public interface ApplicationEventMulticaster {
  void addApplicationListener(ApplicationListener<?> paramApplicationListener);
  
  void addApplicationListenerBean(String paramString);
  
  void removeApplicationListener(ApplicationListener<?> paramApplicationListener);
  
  void removeApplicationListenerBean(String paramString);
  
  void removeAllListeners();
  
  void multicastEvent(ApplicationEvent paramApplicationEvent);
  
  void multicastEvent(ApplicationEvent paramApplicationEvent, ResolvableType paramResolvableType);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\event\ApplicationEventMulticaster.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */