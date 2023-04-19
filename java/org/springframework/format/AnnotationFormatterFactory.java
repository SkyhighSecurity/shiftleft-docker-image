package org.springframework.format;

import java.util.Set;

public interface AnnotationFormatterFactory<A extends java.lang.annotation.Annotation> {
  Set<Class<?>> getFieldTypes();
  
  Printer<?> getPrinter(A paramA, Class<?> paramClass);
  
  Parser<?> getParser(A paramA, Class<?> paramClass);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\AnnotationFormatterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */