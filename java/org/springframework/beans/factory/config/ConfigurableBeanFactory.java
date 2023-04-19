package org.springframework.beans.factory.config;

import java.beans.PropertyEditor;
import java.security.AccessControlContext;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringValueResolver;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {
  public static final String SCOPE_SINGLETON = "singleton";
  
  public static final String SCOPE_PROTOTYPE = "prototype";
  
  void setParentBeanFactory(BeanFactory paramBeanFactory) throws IllegalStateException;
  
  void setBeanClassLoader(ClassLoader paramClassLoader);
  
  ClassLoader getBeanClassLoader();
  
  void setTempClassLoader(ClassLoader paramClassLoader);
  
  ClassLoader getTempClassLoader();
  
  void setCacheBeanMetadata(boolean paramBoolean);
  
  boolean isCacheBeanMetadata();
  
  void setBeanExpressionResolver(BeanExpressionResolver paramBeanExpressionResolver);
  
  BeanExpressionResolver getBeanExpressionResolver();
  
  void setConversionService(ConversionService paramConversionService);
  
  ConversionService getConversionService();
  
  void addPropertyEditorRegistrar(PropertyEditorRegistrar paramPropertyEditorRegistrar);
  
  void registerCustomEditor(Class<?> paramClass, Class<? extends PropertyEditor> paramClass1);
  
  void copyRegisteredEditorsTo(PropertyEditorRegistry paramPropertyEditorRegistry);
  
  void setTypeConverter(TypeConverter paramTypeConverter);
  
  TypeConverter getTypeConverter();
  
  void addEmbeddedValueResolver(StringValueResolver paramStringValueResolver);
  
  boolean hasEmbeddedValueResolver();
  
  String resolveEmbeddedValue(String paramString);
  
  void addBeanPostProcessor(BeanPostProcessor paramBeanPostProcessor);
  
  int getBeanPostProcessorCount();
  
  void registerScope(String paramString, Scope paramScope);
  
  String[] getRegisteredScopeNames();
  
  Scope getRegisteredScope(String paramString);
  
  AccessControlContext getAccessControlContext();
  
  void copyConfigurationFrom(ConfigurableBeanFactory paramConfigurableBeanFactory);
  
  void registerAlias(String paramString1, String paramString2) throws BeanDefinitionStoreException;
  
  void resolveAliases(StringValueResolver paramStringValueResolver);
  
  BeanDefinition getMergedBeanDefinition(String paramString) throws NoSuchBeanDefinitionException;
  
  boolean isFactoryBean(String paramString) throws NoSuchBeanDefinitionException;
  
  void setCurrentlyInCreation(String paramString, boolean paramBoolean);
  
  boolean isCurrentlyInCreation(String paramString);
  
  void registerDependentBean(String paramString1, String paramString2);
  
  String[] getDependentBeans(String paramString);
  
  String[] getDependenciesForBean(String paramString);
  
  void destroyBean(String paramString, Object paramObject);
  
  void destroyScopedBean(String paramString);
  
  void destroySingletons();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\ConfigurableBeanFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */