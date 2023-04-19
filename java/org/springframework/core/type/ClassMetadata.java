package org.springframework.core.type;

public interface ClassMetadata {
  String getClassName();
  
  boolean isInterface();
  
  boolean isAnnotation();
  
  boolean isAbstract();
  
  boolean isConcrete();
  
  boolean isFinal();
  
  boolean isIndependent();
  
  boolean hasEnclosingClass();
  
  String getEnclosingClassName();
  
  boolean hasSuperClass();
  
  String getSuperClassName();
  
  String[] getInterfaceNames();
  
  String[] getMemberClassNames();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\type\ClassMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */