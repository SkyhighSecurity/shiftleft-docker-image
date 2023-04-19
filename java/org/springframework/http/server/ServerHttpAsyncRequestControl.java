package org.springframework.http.server;

public interface ServerHttpAsyncRequestControl {
  void start();
  
  void start(long paramLong);
  
  boolean isStarted();
  
  void complete();
  
  boolean isCompleted();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\server\ServerHttpAsyncRequestControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */