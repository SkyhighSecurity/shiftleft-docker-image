package org.springframework.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public interface WebApplicationInitializer {
  void onStartup(ServletContext paramServletContext) throws ServletException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\WebApplicationInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */