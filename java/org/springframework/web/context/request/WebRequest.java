package org.springframework.web.context.request;

import java.security.Principal;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public interface WebRequest extends RequestAttributes {
  String getHeader(String paramString);
  
  String[] getHeaderValues(String paramString);
  
  Iterator<String> getHeaderNames();
  
  String getParameter(String paramString);
  
  String[] getParameterValues(String paramString);
  
  Iterator<String> getParameterNames();
  
  Map<String, String[]> getParameterMap();
  
  Locale getLocale();
  
  String getContextPath();
  
  String getRemoteUser();
  
  Principal getUserPrincipal();
  
  boolean isUserInRole(String paramString);
  
  boolean isSecure();
  
  boolean checkNotModified(long paramLong);
  
  boolean checkNotModified(String paramString);
  
  boolean checkNotModified(String paramString, long paramLong);
  
  String getDescription(boolean paramBoolean);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\WebRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */