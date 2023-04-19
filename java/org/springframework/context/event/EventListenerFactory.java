package org.springframework.context.event;

import java.lang.reflect.Method;
import org.springframework.context.ApplicationListener;

public interface EventListenerFactory {
  boolean supportsMethod(Method paramMethod);
  
  ApplicationListener<?> createApplicationListener(String paramString, Class<?> paramClass, Method paramMethod);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\event\EventListenerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */