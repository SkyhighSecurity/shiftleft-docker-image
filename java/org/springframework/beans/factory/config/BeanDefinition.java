package org.springframework.beans.factory.config;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.AttributeAccessor;

public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {
  public static final String SCOPE_SINGLETON = "singleton";
  
  public static final String SCOPE_PROTOTYPE = "prototype";
  
  public static final int ROLE_APPLICATION = 0;
  
  public static final int ROLE_SUPPORT = 1;
  
  public static final int ROLE_INFRASTRUCTURE = 2;
  
  void setParentName(String paramString);
  
  String getParentName();
  
  void setBeanClassName(String paramString);
  
  String getBeanClassName();
  
  void setScope(String paramString);
  
  String getScope();
  
  void setLazyInit(boolean paramBoolean);
  
  boolean isLazyInit();
  
  void setDependsOn(String... paramVarArgs);
  
  String[] getDependsOn();
  
  void setAutowireCandidate(boolean paramBoolean);
  
  boolean isAutowireCandidate();
  
  void setPrimary(boolean paramBoolean);
  
  boolean isPrimary();
  
  void setFactoryBeanName(String paramString);
  
  String getFactoryBeanName();
  
  void setFactoryMethodName(String paramString);
  
  String getFactoryMethodName();
  
  ConstructorArgumentValues getConstructorArgumentValues();
  
  MutablePropertyValues getPropertyValues();
  
  boolean isSingleton();
  
  boolean isPrototype();
  
  boolean isAbstract();
  
  int getRole();
  
  String getDescription();
  
  String getResourceDescription();
  
  BeanDefinition getOriginatingBeanDefinition();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\BeanDefinition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */