package org.springframework.http;

import java.io.IOException;
import java.io.OutputStream;

public interface HttpOutputMessage extends HttpMessage {
  OutputStream getBody() throws IOException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\HttpOutputMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */