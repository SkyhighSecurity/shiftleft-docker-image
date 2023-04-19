package org.springframework.web.context;

import javax.servlet.ServletContext;
import org.springframework.beans.factory.Aware;

public interface ServletContextAware extends Aware {
  void setServletContext(ServletContext paramServletContext);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\ServletContextAware.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */