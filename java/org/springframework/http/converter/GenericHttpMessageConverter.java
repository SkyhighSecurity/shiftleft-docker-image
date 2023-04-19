package org.springframework.http.converter;

import java.io.IOException;
import java.lang.reflect.Type;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;

public interface GenericHttpMessageConverter<T> extends HttpMessageConverter<T> {
  boolean canRead(Type paramType, Class<?> paramClass, MediaType paramMediaType);
  
  T read(Type paramType, Class<?> paramClass, HttpInputMessage paramHttpInputMessage) throws IOException, HttpMessageNotReadableException;
  
  boolean canWrite(Type paramType, Class<?> paramClass, MediaType paramMediaType);
  
  void write(T paramT, Type paramType, MediaType paramMediaType, HttpOutputMessage paramHttpOutputMessage) throws IOException, HttpMessageNotWritableException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\GenericHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */