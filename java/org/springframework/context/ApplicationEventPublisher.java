package org.springframework.context;

public interface ApplicationEventPublisher {
  void publishEvent(ApplicationEvent paramApplicationEvent);
  
  void publishEvent(Object paramObject);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\ApplicationEventPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */