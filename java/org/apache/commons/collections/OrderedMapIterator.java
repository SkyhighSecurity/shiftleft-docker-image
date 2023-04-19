package org.apache.commons.collections;

public interface OrderedMapIterator extends MapIterator, OrderedIterator {
  boolean hasPrevious();
  
  Object previous();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\OrderedMapIterator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */