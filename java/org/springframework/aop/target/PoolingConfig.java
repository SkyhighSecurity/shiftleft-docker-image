package org.springframework.aop.target;

public interface PoolingConfig {
  int getMaxSize();
  
  int getActiveCount() throws UnsupportedOperationException;
  
  int getIdleCount() throws UnsupportedOperationException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\target\PoolingConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */