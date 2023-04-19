package org.springframework.core.env;

import org.springframework.core.convert.support.ConfigurableConversionService;

public interface ConfigurablePropertyResolver extends PropertyResolver {
  ConfigurableConversionService getConversionService();
  
  void setConversionService(ConfigurableConversionService paramConfigurableConversionService);
  
  void setPlaceholderPrefix(String paramString);
  
  void setPlaceholderSuffix(String paramString);
  
  void setValueSeparator(String paramString);
  
  void setIgnoreUnresolvableNestedPlaceholders(boolean paramBoolean);
  
  void setRequiredProperties(String... paramVarArgs);
  
  void validateRequiredProperties() throws MissingRequiredPropertiesException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\env\ConfigurablePropertyResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */