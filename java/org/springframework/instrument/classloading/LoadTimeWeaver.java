package org.springframework.instrument.classloading;

import java.lang.instrument.ClassFileTransformer;

public interface LoadTimeWeaver {
  void addTransformer(ClassFileTransformer paramClassFileTransformer);
  
  ClassLoader getInstrumentableClassLoader();
  
  ClassLoader getThrowawayClassLoader();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\instrument\classloading\LoadTimeWeaver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */