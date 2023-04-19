package org.springframework.web.client;

import java.io.IOException;
import org.springframework.http.client.ClientHttpRequest;

public interface RequestCallback {
  void doWithRequest(ClientHttpRequest paramClientHttpRequest) throws IOException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\client\RequestCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */