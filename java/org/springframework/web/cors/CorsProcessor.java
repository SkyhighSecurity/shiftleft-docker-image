package org.springframework.web.cors;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CorsProcessor {
  boolean processRequest(CorsConfiguration paramCorsConfiguration, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse) throws IOException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\cors\CorsProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */