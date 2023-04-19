package org.springframework.beans;

import java.util.Map;
import org.springframework.core.convert.TypeDescriptor;

public interface PropertyAccessor {
  public static final String NESTED_PROPERTY_SEPARATOR = ".";
  
  public static final char NESTED_PROPERTY_SEPARATOR_CHAR = '.';
  
  public static final String PROPERTY_KEY_PREFIX = "[";
  
  public static final char PROPERTY_KEY_PREFIX_CHAR = '[';
  
  public static final String PROPERTY_KEY_SUFFIX = "]";
  
  public static final char PROPERTY_KEY_SUFFIX_CHAR = ']';
  
  boolean isReadableProperty(String paramString);
  
  boolean isWritableProperty(String paramString);
  
  Class<?> getPropertyType(String paramString) throws BeansException;
  
  TypeDescriptor getPropertyTypeDescriptor(String paramString) throws BeansException;
  
  Object getPropertyValue(String paramString) throws BeansException;
  
  void setPropertyValue(String paramString, Object paramObject) throws BeansException;
  
  void setPropertyValue(PropertyValue paramPropertyValue) throws BeansException;
  
  void setPropertyValues(Map<?, ?> paramMap) throws BeansException;
  
  void setPropertyValues(PropertyValues paramPropertyValues) throws BeansException;
  
  void setPropertyValues(PropertyValues paramPropertyValues, boolean paramBoolean) throws BeansException;
  
  void setPropertyValues(PropertyValues paramPropertyValues, boolean paramBoolean1, boolean paramBoolean2) throws BeansException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\PropertyAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */