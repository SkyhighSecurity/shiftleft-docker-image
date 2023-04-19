package org.springframework.context.weaving;

import org.springframework.beans.factory.Aware;
import org.springframework.instrument.classloading.LoadTimeWeaver;

public interface LoadTimeWeaverAware extends Aware {
  void setLoadTimeWeaver(LoadTimeWeaver paramLoadTimeWeaver);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\weaving\LoadTimeWeaverAware.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */