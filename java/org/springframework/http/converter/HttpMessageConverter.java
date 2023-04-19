package org.springframework.http.converter;

import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;

public interface HttpMessageConverter<T> {
  boolean canRead(Class<?> paramClass, MediaType paramMediaType);
  
  boolean canWrite(Class<?> paramClass, MediaType paramMediaType);
  
  List<MediaType> getSupportedMediaTypes();
  
  T read(Class<? extends T> paramClass, HttpInputMessage paramHttpInputMessage) throws IOException, HttpMessageNotReadableException;
  
  void write(T paramT, MediaType paramMediaType, HttpOutputMessage paramHttpOutputMessage) throws IOException, HttpMessageNotWritableException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\HttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */