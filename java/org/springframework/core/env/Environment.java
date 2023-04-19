package org.springframework.core.env;

public interface Environment extends PropertyResolver {
  String[] getActiveProfiles();
  
  String[] getDefaultProfiles();
  
  boolean acceptsProfiles(String... paramVarArgs);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\env\Environment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */