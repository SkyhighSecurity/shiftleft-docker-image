package org.springframework.web.client;

import java.io.IOException;
import org.springframework.http.client.ClientHttpResponse;

public interface ResponseExtractor<T> {
  T extractData(ClientHttpResponse paramClientHttpResponse) throws IOException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\client\ResponseExtractor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */