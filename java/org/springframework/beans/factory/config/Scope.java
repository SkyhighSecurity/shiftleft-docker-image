package org.springframework.beans.factory.config;

import org.springframework.beans.factory.ObjectFactory;

public interface Scope {
  Object get(String paramString, ObjectFactory<?> paramObjectFactory);
  
  Object remove(String paramString);
  
  void registerDestructionCallback(String paramString, Runnable paramRunnable);
  
  Object resolveContextualObject(String paramString);
  
  String getConversationId();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\Scope.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */