package org.springframework.core;

public interface AttributeAccessor {
  void setAttribute(String paramString, Object paramObject);
  
  Object getAttribute(String paramString);
  
  Object removeAttribute(String paramString);
  
  boolean hasAttribute(String paramString);
  
  String[] attributeNames();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\AttributeAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */