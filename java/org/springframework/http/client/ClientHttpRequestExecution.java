package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpRequest;

public interface ClientHttpRequestExecution {
  ClientHttpResponse execute(HttpRequest paramHttpRequest, byte[] paramArrayOfbyte) throws IOException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\ClientHttpRequestExecution.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */