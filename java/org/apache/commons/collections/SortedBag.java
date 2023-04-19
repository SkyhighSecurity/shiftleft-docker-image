package org.apache.commons.collections;

import java.util.Comparator;

public interface SortedBag extends Bag {
  Comparator comparator();
  
  Object first();
  
  Object last();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\SortedBag.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */