package org.springframework.web.client;

import java.io.IOException;
import org.springframework.http.client.AsyncClientHttpRequest;

public interface AsyncRequestCallback {
  void doWithRequest(AsyncClientHttpRequest paramAsyncClientHttpRequest) throws IOException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\client\AsyncRequestCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */