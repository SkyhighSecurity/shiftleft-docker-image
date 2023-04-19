package org.springframework.core.convert.converter;

public interface ConverterRegistry {
  void addConverter(Converter<?, ?> paramConverter);
  
  <S, T> void addConverter(Class<S> paramClass, Class<T> paramClass1, Converter<? super S, ? extends T> paramConverter);
  
  void addConverter(GenericConverter paramGenericConverter);
  
  void addConverterFactory(ConverterFactory<?, ?> paramConverterFactory);
  
  void removeConvertible(Class<?> paramClass1, Class<?> paramClass2);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\converter\ConverterRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */