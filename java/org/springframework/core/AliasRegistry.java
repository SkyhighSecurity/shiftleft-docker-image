package org.springframework.core;

public interface AliasRegistry {
  void registerAlias(String paramString1, String paramString2);
  
  void removeAlias(String paramString);
  
  boolean isAlias(String paramString);
  
  String[] getAliases(String paramString);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\AliasRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */