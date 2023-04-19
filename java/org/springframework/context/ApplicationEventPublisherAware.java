package org.springframework.context;

import org.springframework.beans.factory.Aware;

public interface ApplicationEventPublisherAware extends Aware {
  void setApplicationEventPublisher(ApplicationEventPublisher paramApplicationEventPublisher);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\ApplicationEventPublisherAware.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */