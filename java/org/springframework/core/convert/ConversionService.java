package org.springframework.core.convert;

public interface ConversionService {
  boolean canConvert(Class<?> paramClass1, Class<?> paramClass2);
  
  boolean canConvert(TypeDescriptor paramTypeDescriptor1, TypeDescriptor paramTypeDescriptor2);
  
  <T> T convert(Object paramObject, Class<T> paramClass);
  
  Object convert(Object paramObject, TypeDescriptor paramTypeDescriptor1, TypeDescriptor paramTypeDescriptor2);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\ConversionService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */