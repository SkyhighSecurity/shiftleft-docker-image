package org.springframework.scripting;

import java.io.IOException;

public interface ScriptFactory {
  String getScriptSourceLocator();
  
  Class<?>[] getScriptInterfaces();
  
  boolean requiresConfigInterface();
  
  Object getScriptedObject(ScriptSource paramScriptSource, Class<?>... paramVarArgs) throws IOException, ScriptCompilationException;
  
  Class<?> getScriptedObjectType(ScriptSource paramScriptSource) throws IOException, ScriptCompilationException;
  
  boolean requiresScriptedObjectRefresh(ScriptSource paramScriptSource);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scripting\ScriptFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */