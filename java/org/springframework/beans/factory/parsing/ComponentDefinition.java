package org.springframework.beans.factory.parsing;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;

public interface ComponentDefinition extends BeanMetadataElement {
  String getName();
  
  String getDescription();
  
  BeanDefinition[] getBeanDefinitions();
  
  BeanDefinition[] getInnerBeanDefinitions();
  
  BeanReference[] getBeanReferences();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\parsing\ComponentDefinition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */