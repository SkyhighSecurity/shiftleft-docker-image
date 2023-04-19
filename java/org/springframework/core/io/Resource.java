package org.springframework.core.io;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

public interface Resource extends InputStreamSource {
  boolean exists();
  
  boolean isReadable();
  
  boolean isOpen();
  
  URL getURL() throws IOException;
  
  URI getURI() throws IOException;
  
  File getFile() throws IOException;
  
  long contentLength() throws IOException;
  
  long lastModified() throws IOException;
  
  Resource createRelative(String paramString) throws IOException;
  
  String getFilename();
  
  String getDescription();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\Resource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */