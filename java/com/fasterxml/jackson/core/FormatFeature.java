package com.fasterxml.jackson.core;

public interface FormatFeature {
  boolean enabledByDefault();
  
  int getMask();
  
  boolean enabledIn(int paramInt);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\core\FormatFeature.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */