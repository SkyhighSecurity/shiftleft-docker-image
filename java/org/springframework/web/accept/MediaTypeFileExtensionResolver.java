package org.springframework.web.accept;

import java.util.List;
import org.springframework.http.MediaType;

public interface MediaTypeFileExtensionResolver {
  List<String> resolveFileExtensions(MediaType paramMediaType);
  
  List<String> getAllFileExtensions();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\accept\MediaTypeFileExtensionResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */