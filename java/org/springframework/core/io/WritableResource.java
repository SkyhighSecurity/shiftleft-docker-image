package org.springframework.core.io;

import java.io.IOException;
import java.io.OutputStream;

public interface WritableResource extends Resource {
  boolean isWritable();
  
  OutputStream getOutputStream() throws IOException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\WritableResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */