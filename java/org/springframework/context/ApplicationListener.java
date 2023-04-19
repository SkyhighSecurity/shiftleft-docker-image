package org.springframework.context;

import java.util.EventListener;

public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {
  void onApplicationEvent(E paramE);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\ApplicationListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */