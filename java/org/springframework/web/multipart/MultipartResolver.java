package org.springframework.web.multipart;

import javax.servlet.http.HttpServletRequest;

public interface MultipartResolver {
  boolean isMultipart(HttpServletRequest paramHttpServletRequest);
  
  MultipartHttpServletRequest resolveMultipart(HttpServletRequest paramHttpServletRequest) throws MultipartException;
  
  void cleanupMultipart(MultipartHttpServletRequest paramMultipartHttpServletRequest);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\multipart\MultipartResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */