package org.springframework.core.serializer;

import java.io.IOException;
import java.io.OutputStream;

public interface Serializer<T> {
  void serialize(T paramT, OutputStream paramOutputStream) throws IOException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\serializer\Serializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */