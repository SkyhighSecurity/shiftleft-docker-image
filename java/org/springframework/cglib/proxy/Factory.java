package org.springframework.cglib.proxy;

public interface Factory {
  Object newInstance(Callback paramCallback);
  
  Object newInstance(Callback[] paramArrayOfCallback);
  
  Object newInstance(Class[] paramArrayOfClass, Object[] paramArrayOfObject, Callback[] paramArrayOfCallback);
  
  Callback getCallback(int paramInt);
  
  void setCallback(int paramInt, Callback paramCallback);
  
  void setCallbacks(Callback[] paramArrayOfCallback);
  
  Callback[] getCallbacks();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cglib\proxy\Factory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */