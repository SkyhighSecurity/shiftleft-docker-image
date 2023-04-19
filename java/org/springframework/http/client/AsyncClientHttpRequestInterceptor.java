package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.util.concurrent.ListenableFuture;

public interface AsyncClientHttpRequestInterceptor {
  ListenableFuture<ClientHttpResponse> intercept(HttpRequest paramHttpRequest, byte[] paramArrayOfbyte, AsyncClientHttpRequestExecution paramAsyncClientHttpRequestExecution) throws IOException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\AsyncClientHttpRequestInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */