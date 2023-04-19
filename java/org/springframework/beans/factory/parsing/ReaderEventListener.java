package org.springframework.beans.factory.parsing;

import java.util.EventListener;

public interface ReaderEventListener extends EventListener {
  void defaultsRegistered(DefaultsDefinition paramDefaultsDefinition);
  
  void componentRegistered(ComponentDefinition paramComponentDefinition);
  
  void aliasRegistered(AliasDefinition paramAliasDefinition);
  
  void importProcessed(ImportDefinition paramImportDefinition);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\parsing\ReaderEventListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */