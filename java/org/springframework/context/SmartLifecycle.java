package org.springframework.context;

public interface SmartLifecycle extends Lifecycle, Phased {
  boolean isAutoStartup();
  
  void stop(Runnable paramRunnable);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\SmartLifecycle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */