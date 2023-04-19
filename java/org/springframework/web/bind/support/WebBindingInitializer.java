package org.springframework.web.bind.support;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.WebRequest;

public interface WebBindingInitializer {
  void initBinder(WebDataBinder paramWebDataBinder, WebRequest paramWebRequest);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\bind\support\WebBindingInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */