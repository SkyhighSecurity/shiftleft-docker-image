package org.apache.commons.collections;

import java.util.Collection;

public interface BoundedCollection extends Collection {
  boolean isFull();
  
  int maxSize();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\BoundedCollection.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */