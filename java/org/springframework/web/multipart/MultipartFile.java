package org.springframework.web.multipart;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.core.io.InputStreamSource;

public interface MultipartFile extends InputStreamSource {
  String getName();
  
  String getOriginalFilename();
  
  String getContentType();
  
  boolean isEmpty();
  
  long getSize();
  
  byte[] getBytes() throws IOException;
  
  InputStream getInputStream() throws IOException;
  
  void transferTo(File paramFile) throws IOException, IllegalStateException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\multipart\MultipartFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */