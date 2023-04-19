package org.springframework.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;

public interface BeanInfoFactory {
  BeanInfo getBeanInfo(Class<?> paramClass) throws IntrospectionException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\BeanInfoFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */