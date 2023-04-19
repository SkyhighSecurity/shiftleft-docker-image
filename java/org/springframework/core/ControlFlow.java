package org.springframework.core;

@Deprecated
public interface ControlFlow {
  boolean under(Class<?> paramClass);
  
  boolean under(Class<?> paramClass, String paramString);
  
  boolean underToken(String paramString);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\ControlFlow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */