package org.springframework.beans;

import org.springframework.core.convert.ConversionService;

public interface ConfigurablePropertyAccessor extends PropertyAccessor, PropertyEditorRegistry, TypeConverter {
  void setConversionService(ConversionService paramConversionService);
  
  ConversionService getConversionService();
  
  void setExtractOldValueForEditor(boolean paramBoolean);
  
  boolean isExtractOldValueForEditor();
  
  void setAutoGrowNestedPaths(boolean paramBoolean);
  
  boolean isAutoGrowNestedPaths();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\ConfigurablePropertyAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */