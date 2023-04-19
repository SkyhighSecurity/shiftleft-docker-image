package org.springframework.scripting;

import java.io.IOException;

public interface ScriptSource {
  String getScriptAsString() throws IOException;
  
  boolean isModified();
  
  String suggestedClassName();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scripting\ScriptSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */