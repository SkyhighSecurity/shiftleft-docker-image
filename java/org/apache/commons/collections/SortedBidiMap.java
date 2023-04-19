package org.apache.commons.collections;

import java.util.SortedMap;

public interface SortedBidiMap extends OrderedBidiMap, SortedMap {
  BidiMap inverseBidiMap();
  
  SortedBidiMap inverseSortedBidiMap();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\SortedBidiMap.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */