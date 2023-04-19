package org.springframework.context.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;

public interface GenericApplicationListener extends ApplicationListener<ApplicationEvent>, Ordered {
  boolean supportsEventType(ResolvableType paramResolvableType);
  
  boolean supportsSourceType(Class<?> paramClass);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\event\GenericApplicationListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */