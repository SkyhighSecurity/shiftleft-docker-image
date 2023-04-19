package org.springframework.context.annotation;

public interface AnnotationConfigRegistry {
  void register(Class<?>... paramVarArgs);
  
  void scan(String... paramVarArgs);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\AnnotationConfigRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */