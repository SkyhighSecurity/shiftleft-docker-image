package org.springframework.beans.factory.xml;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.w3c.dom.Document;

public interface BeanDefinitionDocumentReader {
  void registerBeanDefinitions(Document paramDocument, XmlReaderContext paramXmlReaderContext) throws BeanDefinitionStoreException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\xml\BeanDefinitionDocumentReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */