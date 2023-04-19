package org.springframework.beans.factory.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface NamespaceHandler {
  void init();
  
  BeanDefinition parse(Element paramElement, ParserContext paramParserContext);
  
  BeanDefinitionHolder decorate(Node paramNode, BeanDefinitionHolder paramBeanDefinitionHolder, ParserContext paramParserContext);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\xml\NamespaceHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */