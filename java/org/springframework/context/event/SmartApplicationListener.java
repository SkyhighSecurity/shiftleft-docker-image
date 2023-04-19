package org.springframework.context.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

public interface SmartApplicationListener extends ApplicationListener<ApplicationEvent>, Ordered {
  boolean supportsEventType(Class<? extends ApplicationEvent> paramClass);
  
  boolean supportsSourceType(Class<?> paramClass);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\event\SmartApplicationListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */