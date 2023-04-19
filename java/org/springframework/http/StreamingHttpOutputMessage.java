package org.springframework.http;

import java.io.IOException;
import java.io.OutputStream;

public interface StreamingHttpOutputMessage extends HttpOutputMessage {
  void setBody(Body paramBody);
  
  public static interface Body {
    void writeTo(OutputStream param1OutputStream) throws IOException;
  }
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\StreamingHttpOutputMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */