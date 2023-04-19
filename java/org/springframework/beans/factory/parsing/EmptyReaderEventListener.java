package org.springframework.beans.factory.parsing;

public class EmptyReaderEventListener implements ReaderEventListener {
  public void defaultsRegistered(DefaultsDefinition defaultsDefinition) {}
  
  public void componentRegistered(ComponentDefinition componentDefinition) {}
  
  public void aliasRegistered(AliasDefinition aliasDefinition) {}
  
  public void importProcessed(ImportDefinition importDefinition) {}
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\parsing\EmptyReaderEventListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */