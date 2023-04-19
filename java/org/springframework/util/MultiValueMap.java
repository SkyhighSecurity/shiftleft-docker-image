package org.springframework.util;

import java.util.List;
import java.util.Map;

public interface MultiValueMap<K, V> extends Map<K, List<V>> {
  V getFirst(K paramK);
  
  void add(K paramK, V paramV);
  
  void set(K paramK, V paramV);
  
  void setAll(Map<K, V> paramMap);
  
  Map<K, V> toSingleValueMap();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\MultiValueMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */