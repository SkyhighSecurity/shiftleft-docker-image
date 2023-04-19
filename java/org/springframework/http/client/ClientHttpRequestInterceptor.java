package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpRequest;

public interface ClientHttpRequestInterceptor {
  ClientHttpResponse intercept(HttpRequest paramHttpRequest, byte[] paramArrayOfbyte, ClientHttpRequestExecution paramClientHttpRequestExecution) throws IOException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\ClientHttpRequestInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */