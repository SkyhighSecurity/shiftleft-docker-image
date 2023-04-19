package org.springframework.beans.factory.config;

public interface SingletonBeanRegistry {
  void registerSingleton(String paramString, Object paramObject);
  
  Object getSingleton(String paramString);
  
  boolean containsSingleton(String paramString);
  
  String[] getSingletonNames();
  
  int getSingletonCount();
  
  Object getSingletonMutex();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\SingletonBeanRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */