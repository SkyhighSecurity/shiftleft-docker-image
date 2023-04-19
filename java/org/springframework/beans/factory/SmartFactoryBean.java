package org.springframework.beans.factory;

public interface SmartFactoryBean<T> extends FactoryBean<T> {
  boolean isPrototype();
  
  boolean isEagerInit();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\SmartFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */