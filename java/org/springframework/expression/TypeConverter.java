package org.springframework.expression;

import org.springframework.core.convert.TypeDescriptor;

public interface TypeConverter {
  boolean canConvert(TypeDescriptor paramTypeDescriptor1, TypeDescriptor paramTypeDescriptor2);
  
  Object convertValue(Object paramObject, TypeDescriptor paramTypeDescriptor1, TypeDescriptor paramTypeDescriptor2);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\TypeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */