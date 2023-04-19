package org.springframework.beans;

import java.beans.PropertyDescriptor;

public interface BeanWrapper extends ConfigurablePropertyAccessor {
  void setAutoGrowCollectionLimit(int paramInt);
  
  int getAutoGrowCollectionLimit();
  
  Object getWrappedInstance();
  
  Class<?> getWrappedClass();
  
  PropertyDescriptor[] getPropertyDescriptors();
  
  PropertyDescriptor getPropertyDescriptor(String paramString) throws InvalidPropertyException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\BeanWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */