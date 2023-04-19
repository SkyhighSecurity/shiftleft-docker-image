package org.springframework.aop;

import org.aopalliance.intercept.MethodInvocation;

public interface ProxyMethodInvocation extends MethodInvocation {
  Object getProxy();
  
  MethodInvocation invocableClone();
  
  MethodInvocation invocableClone(Object... paramVarArgs);
  
  void setArguments(Object... paramVarArgs);
  
  void setUserAttribute(String paramString, Object paramObject);
  
  Object getUserAttribute(String paramString);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\ProxyMethodInvocation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */