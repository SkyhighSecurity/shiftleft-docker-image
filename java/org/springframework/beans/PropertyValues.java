package org.springframework.beans;

public interface PropertyValues {
  PropertyValue[] getPropertyValues();
  
  PropertyValue getPropertyValue(String paramString);
  
  PropertyValues changesSince(PropertyValues paramPropertyValues);
  
  boolean contains(String paramString);
  
  boolean isEmpty();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\PropertyValues.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */