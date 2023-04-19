package org.springframework.instrument.classloading.jboss;

import java.lang.instrument.ClassFileTransformer;

interface JBossClassLoaderAdapter {
  void addTransformer(ClassFileTransformer paramClassFileTransformer);
  
  ClassLoader getInstrumentableClassLoader();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\instrument\classloading\jboss\JBossClassLoaderAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */