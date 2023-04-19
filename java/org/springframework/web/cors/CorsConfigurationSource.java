package org.springframework.web.cors;

import javax.servlet.http.HttpServletRequest;

public interface CorsConfigurationSource {
  CorsConfiguration getCorsConfiguration(HttpServletRequest paramHttpServletRequest);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\cors\CorsConfigurationSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */