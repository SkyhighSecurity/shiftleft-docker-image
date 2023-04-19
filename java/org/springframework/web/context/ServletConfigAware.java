package org.springframework.web.context;

import javax.servlet.ServletConfig;
import org.springframework.beans.factory.Aware;

public interface ServletConfigAware extends Aware {
  void setServletConfig(ServletConfig paramServletConfig);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\ServletConfigAware.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */