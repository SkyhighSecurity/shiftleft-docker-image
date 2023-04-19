package org.springframework.ui;

import java.util.Collection;
import java.util.Map;

public interface Model {
  Model addAttribute(String paramString, Object paramObject);
  
  Model addAttribute(Object paramObject);
  
  Model addAllAttributes(Collection<?> paramCollection);
  
  Model addAllAttributes(Map<String, ?> paramMap);
  
  Model mergeAttributes(Map<String, ?> paramMap);
  
  boolean containsAttribute(String paramString);
  
  Map<String, Object> asMap();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\ui\Model.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */