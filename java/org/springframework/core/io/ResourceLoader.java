package org.springframework.core.io;

public interface ResourceLoader {
  public static final String CLASSPATH_URL_PREFIX = "classpath:";
  
  Resource getResource(String paramString);
  
  ClassLoader getClassLoader();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\ResourceLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */