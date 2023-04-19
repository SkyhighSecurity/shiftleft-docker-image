package org.springframework.core.env;

import java.util.Map;

public interface ConfigurableEnvironment extends Environment, ConfigurablePropertyResolver {
  void setActiveProfiles(String... paramVarArgs);
  
  void addActiveProfile(String paramString);
  
  void setDefaultProfiles(String... paramVarArgs);
  
  MutablePropertySources getPropertySources();
  
  Map<String, Object> getSystemProperties();
  
  Map<String, Object> getSystemEnvironment();
  
  void merge(ConfigurableEnvironment paramConfigurableEnvironment);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\env\ConfigurableEnvironment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */