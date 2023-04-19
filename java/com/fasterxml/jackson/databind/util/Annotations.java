package com.fasterxml.jackson.databind.util;

import java.lang.annotation.Annotation;

public interface Annotations {
  <A extends Annotation> A get(Class<A> paramClass);
  
  boolean has(Class<?> paramClass);
  
  boolean hasOneOf(Class<? extends Annotation>[] paramArrayOfClass);
  
  int size();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databin\\util\Annotations.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */