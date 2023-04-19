package org.springframework.scripting;

import java.util.Map;

public interface ScriptEvaluator {
  Object evaluate(ScriptSource paramScriptSource) throws ScriptCompilationException;
  
  Object evaluate(ScriptSource paramScriptSource, Map<String, Object> paramMap) throws ScriptCompilationException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scripting\ScriptEvaluator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */