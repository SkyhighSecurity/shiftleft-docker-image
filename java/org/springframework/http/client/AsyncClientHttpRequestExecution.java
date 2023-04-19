package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.util.concurrent.ListenableFuture;

public interface AsyncClientHttpRequestExecution {
  ListenableFuture<ClientHttpResponse> executeAsync(HttpRequest paramHttpRequest, byte[] paramArrayOfbyte) throws IOException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\AsyncClientHttpRequestExecution.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */