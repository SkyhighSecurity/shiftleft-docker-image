package org.springframework.cglib.core;

import org.springframework.asm.Label;

public interface ProcessSwitchCallback {
  void processCase(int paramInt, Label paramLabel) throws Exception;
  
  void processDefault() throws Exception;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cglib\core\ProcessSwitchCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */