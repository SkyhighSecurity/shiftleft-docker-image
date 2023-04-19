package org.springframework.web.bind.support;

import org.springframework.web.context.request.WebRequest;

public interface SessionAttributeStore {
  void storeAttribute(WebRequest paramWebRequest, String paramString, Object paramObject);
  
  Object retrieveAttribute(WebRequest paramWebRequest, String paramString);
  
  void cleanupAttribute(WebRequest paramWebRequest, String paramString);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\bind\support\SessionAttributeStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */