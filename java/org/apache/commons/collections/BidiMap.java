package org.apache.commons.collections;

public interface BidiMap extends IterableMap {
  MapIterator mapIterator();
  
  Object put(Object paramObject1, Object paramObject2);
  
  Object getKey(Object paramObject);
  
  Object removeValue(Object paramObject);
  
  BidiMap inverseBidiMap();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\BidiMap.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */