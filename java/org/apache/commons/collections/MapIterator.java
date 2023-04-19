package org.apache.commons.collections;

import java.util.Iterator;

public interface MapIterator extends Iterator {
  boolean hasNext();
  
  Object next();
  
  Object getKey();
  
  Object getValue();
  
  void remove();
  
  Object setValue(Object paramObject);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\MapIterator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */