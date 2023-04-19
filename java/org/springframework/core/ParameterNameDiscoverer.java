package org.springframework.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public interface ParameterNameDiscoverer {
  String[] getParameterNames(Method paramMethod);
  
  String[] getParameterNames(Constructor<?> paramConstructor);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\ParameterNameDiscoverer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */