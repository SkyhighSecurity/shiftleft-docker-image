package org.springframework.core.convert.converter;

public interface ConverterFactory<S, R> {
  <T extends R> Converter<S, T> getConverter(Class<T> paramClass);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\converter\ConverterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */