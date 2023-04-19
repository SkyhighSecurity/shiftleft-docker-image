package org.springframework.web.context;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.springframework.core.env.ConfigurableEnvironment;

public interface ConfigurableWebEnvironment extends ConfigurableEnvironment {
  void initPropertySources(ServletContext paramServletContext, ServletConfig paramServletConfig);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\ConfigurableWebEnvironment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */