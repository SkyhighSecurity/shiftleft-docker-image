package org.springframework.web.util;

import java.net.URI;
import java.util.Map;

public interface UriTemplateHandler {
  URI expand(String paramString, Map<String, ?> paramMap);
  
  URI expand(String paramString, Object... paramVarArgs);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\UriTemplateHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */