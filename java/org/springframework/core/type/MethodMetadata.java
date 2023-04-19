package org.springframework.core.type;

public interface MethodMetadata extends AnnotatedTypeMetadata {
  String getMethodName();
  
  String getDeclaringClassName();
  
  String getReturnTypeName();
  
  boolean isAbstract();
  
  boolean isStatic();
  
  boolean isFinal();
  
  boolean isOverridable();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\type\MethodMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */