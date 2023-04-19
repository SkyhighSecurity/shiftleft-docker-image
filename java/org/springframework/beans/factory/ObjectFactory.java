package org.springframework.beans.factory;

import org.springframework.beans.BeansException;

public interface ObjectFactory<T> {
  T getObject() throws BeansException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\ObjectFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */