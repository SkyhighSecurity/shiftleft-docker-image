package org.springframework.beans.factory.access;

import org.springframework.beans.factory.BeanFactory;

public interface BeanFactoryReference {
  BeanFactory getFactory();
  
  void release();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\access\BeanFactoryReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */