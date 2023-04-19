package com.fasterxml.jackson.databind.cfg;

public interface ConfigFeature {
  boolean enabledByDefault();
  
  int getMask();
  
  boolean enabledIn(int paramInt);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\cfg\ConfigFeature.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */