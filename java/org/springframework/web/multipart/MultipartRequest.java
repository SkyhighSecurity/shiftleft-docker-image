package org.springframework.web.multipart;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.util.MultiValueMap;

public interface MultipartRequest {
  Iterator<String> getFileNames();
  
  MultipartFile getFile(String paramString);
  
  List<MultipartFile> getFiles(String paramString);
  
  Map<String, MultipartFile> getFileMap();
  
  MultiValueMap<String, MultipartFile> getMultiFileMap();
  
  String getMultipartContentType(String paramString);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\multipart\MultipartRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */