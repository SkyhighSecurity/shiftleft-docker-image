package org.aopalliance.intercept;

import java.lang.reflect.AccessibleObject;

public interface Joinpoint {
  Object proceed() throws Throwable;
  
  Object getThis();
  
  AccessibleObject getStaticPart();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\aopalliance\intercept\Joinpoint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */