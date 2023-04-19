package org.springframework.beans.factory;

public interface FactoryBean<T> {
  T getObject() throws Exception;
  
  Class<?> getObjectType();
  
  boolean isSingleton();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\FactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */