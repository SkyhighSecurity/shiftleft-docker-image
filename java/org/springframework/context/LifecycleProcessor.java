package org.springframework.context;

public interface LifecycleProcessor extends Lifecycle {
  void onRefresh();
  
  void onClose();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\LifecycleProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */