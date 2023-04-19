package org.springframework.http;

import java.net.URI;

public interface HttpRequest extends HttpMessage {
  HttpMethod getMethod();
  
  URI getURI();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\HttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */