package org.springframework.aop.scope;

import org.springframework.aop.RawTargetAccess;

public interface ScopedObject extends RawTargetAccess {
  Object getTargetObject();
  
  void removeFromScope();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\scope\ScopedObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */