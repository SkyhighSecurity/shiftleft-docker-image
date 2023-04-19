package org.springframework.core.serializer;

import java.io.IOException;
import java.io.InputStream;

public interface Deserializer<T> {
  T deserialize(InputStream paramInputStream) throws IOException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\serializer\Deserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */