package org.springframework.beans;

import java.beans.PropertyEditor;

public interface PropertyEditorRegistry {
  void registerCustomEditor(Class<?> paramClass, PropertyEditor paramPropertyEditor);
  
  void registerCustomEditor(Class<?> paramClass, String paramString, PropertyEditor paramPropertyEditor);
  
  PropertyEditor findCustomEditor(Class<?> paramClass, String paramString);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\PropertyEditorRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */