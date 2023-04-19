package org.springframework.web.client;

import java.io.IOException;
import org.springframework.http.client.ClientHttpResponse;

public interface ResponseErrorHandler {
  boolean hasError(ClientHttpResponse paramClientHttpResponse) throws IOException;
  
  void handleError(ClientHttpResponse paramClientHttpResponse) throws IOException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\client\ResponseErrorHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */