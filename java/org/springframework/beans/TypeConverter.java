package org.springframework.beans;

import java.lang.reflect.Field;
import org.springframework.core.MethodParameter;

public interface TypeConverter {
  <T> T convertIfNecessary(Object paramObject, Class<T> paramClass) throws TypeMismatchException;
  
  <T> T convertIfNecessary(Object paramObject, Class<T> paramClass, MethodParameter paramMethodParameter) throws TypeMismatchException;
  
  <T> T convertIfNecessary(Object paramObject, Class<T> paramClass, Field paramField) throws TypeMismatchException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\TypeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */