package org.springframework.beans.factory.xml;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

public interface DocumentLoader {
  Document loadDocument(InputSource paramInputSource, EntityResolver paramEntityResolver, ErrorHandler paramErrorHandler, int paramInt, boolean paramBoolean) throws Exception;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\xml\DocumentLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */