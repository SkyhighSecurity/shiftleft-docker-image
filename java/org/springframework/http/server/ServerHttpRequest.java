package org.springframework.http.server;

import java.net.InetSocketAddress;
import java.security.Principal;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpRequest;

public interface ServerHttpRequest extends HttpRequest, HttpInputMessage {
  Principal getPrincipal();
  
  InetSocketAddress getLocalAddress();
  
  InetSocketAddress getRemoteAddress();
  
  ServerHttpAsyncRequestControl getAsyncRequestControl(ServerHttpResponse paramServerHttpResponse);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\server\ServerHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */