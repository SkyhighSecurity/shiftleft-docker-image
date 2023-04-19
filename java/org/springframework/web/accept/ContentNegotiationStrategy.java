package org.springframework.web.accept;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;

public interface ContentNegotiationStrategy {
  List<MediaType> resolveMediaTypes(NativeWebRequest paramNativeWebRequest) throws HttpMediaTypeNotAcceptableException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\accept\ContentNegotiationStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */