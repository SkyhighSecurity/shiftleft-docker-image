package org.apache.commons.collections;

public interface OrderedMap extends IterableMap {
  OrderedMapIterator orderedMapIterator();
  
  Object firstKey();
  
  Object lastKey();
  
  Object nextKey(Object paramObject);
  
  Object previousKey(Object paramObject);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\OrderedMap.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */