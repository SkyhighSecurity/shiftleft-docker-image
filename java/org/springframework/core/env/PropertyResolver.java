package org.springframework.core.env;

public interface PropertyResolver {
  boolean containsProperty(String paramString);
  
  String getProperty(String paramString);
  
  String getProperty(String paramString1, String paramString2);
  
  <T> T getProperty(String paramString, Class<T> paramClass);
  
  <T> T getProperty(String paramString, Class<T> paramClass, T paramT);
  
  @Deprecated
  <T> Class<T> getPropertyAsClass(String paramString, Class<T> paramClass);
  
  String getRequiredProperty(String paramString) throws IllegalStateException;
  
  <T> T getRequiredProperty(String paramString, Class<T> paramClass) throws IllegalStateException;
  
  String resolvePlaceholders(String paramString);
  
  String resolveRequiredPlaceholders(String paramString) throws IllegalArgumentException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\env\PropertyResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */