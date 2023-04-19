package org.springframework.cglib.proxy;

import java.lang.reflect.Method;

public interface CallbackFilter {
  int accept(Method paramMethod);
  
  boolean equals(Object paramObject);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cglib\proxy\CallbackFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */