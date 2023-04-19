package org.springframework.http.client;

import java.io.Closeable;
import java.io.IOException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;

public interface ClientHttpResponse extends HttpInputMessage, Closeable {
  HttpStatus getStatusCode() throws IOException;
  
  int getRawStatusCode() throws IOException;
  
  String getStatusText() throws IOException;
  
  void close();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\ClientHttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */