package org.springframework.aop;

public interface TargetSource extends TargetClassAware {
  Class<?> getTargetClass();
  
  boolean isStatic();
  
  Object getTarget() throws Exception;
  
  void releaseTarget(Object paramObject) throws Exception;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\TargetSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */