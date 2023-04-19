package org.springframework.web.context.request;

public interface RequestAttributes {
  public static final int SCOPE_REQUEST = 0;
  
  public static final int SCOPE_SESSION = 1;
  
  public static final int SCOPE_GLOBAL_SESSION = 2;
  
  public static final String REFERENCE_REQUEST = "request";
  
  public static final String REFERENCE_SESSION = "session";
  
  Object getAttribute(String paramString, int paramInt);
  
  void setAttribute(String paramString, Object paramObject, int paramInt);
  
  void removeAttribute(String paramString, int paramInt);
  
  String[] getAttributeNames(int paramInt);
  
  void registerDestructionCallback(String paramString, Runnable paramRunnable, int paramInt);
  
  Object resolveReference(String paramString);
  
  String getSessionId();
  
  Object getSessionMutex();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\RequestAttributes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */