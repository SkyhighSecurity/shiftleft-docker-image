package org.springframework.objenesis.strategy;

import org.springframework.objenesis.instantiator.ObjectInstantiator;

public interface InstantiatorStrategy {
  <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> paramClass);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\objenesis\strategy\InstantiatorStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */