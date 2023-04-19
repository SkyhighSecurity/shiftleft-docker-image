package org.springframework.web.multipart;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public interface MultipartHttpServletRequest extends HttpServletRequest, MultipartRequest {
  HttpMethod getRequestMethod();
  
  HttpHeaders getRequestHeaders();
  
  HttpHeaders getMultipartHeaders(String paramString);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\multipart\MultipartHttpServletRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */