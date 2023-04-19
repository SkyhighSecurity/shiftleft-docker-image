package org.springframework.http.server;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;

public interface ServerHttpResponse extends HttpOutputMessage, Flushable, Closeable {
  void setStatusCode(HttpStatus paramHttpStatus);
  
  void flush() throws IOException;
  
  void close();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\server\ServerHttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */