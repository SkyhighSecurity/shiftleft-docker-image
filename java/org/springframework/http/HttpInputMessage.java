package org.springframework.http;

import java.io.IOException;
import java.io.InputStream;

public interface HttpInputMessage extends HttpMessage {
  InputStream getBody() throws IOException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\HttpInputMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */