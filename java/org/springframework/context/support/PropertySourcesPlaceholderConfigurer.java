/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;
/*     */ import org.springframework.context.EnvironmentAware;
/*     */ import org.springframework.core.env.ConfigurablePropertyResolver;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.MutablePropertySources;
/*     */ import org.springframework.core.env.PropertiesPropertySource;
/*     */ import org.springframework.core.env.PropertySource;
/*     */ import org.springframework.core.env.PropertySources;
/*     */ import org.springframework.core.env.PropertySourcesPropertyResolver;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringValueResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertySourcesPlaceholderConfigurer
/*     */   extends PlaceholderConfigurerSupport
/*     */   implements EnvironmentAware
/*     */ {
/*     */   public static final String LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME = "localProperties";
/*     */   public static final String ENVIRONMENT_PROPERTIES_PROPERTY_SOURCE_NAME = "environmentProperties";
/*     */   private MutablePropertySources propertySources;
/*     */   private PropertySources appliedPropertySources;
/*     */   private Environment environment;
/*     */   
/*     */   public void setPropertySources(PropertySources propertySources) {
/*  93 */     this.propertySources = new MutablePropertySources(propertySources);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironment(Environment environment) {
/* 104 */     this.environment = environment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
/* 125 */     if (this.propertySources == null) {
/* 126 */       this.propertySources = new MutablePropertySources();
/* 127 */       if (this.environment != null) {
/* 128 */         this.propertySources.addLast(new PropertySource<Environment>("environmentProperties", this.environment)
/*     */             {
/*     */               public String getProperty(String key)
/*     */               {
/* 132 */                 return ((Environment)this.source).getProperty(key);
/*     */               }
/*     */             });
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 139 */         PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource("localProperties", mergeProperties());
/* 140 */         if (this.localOverride) {
/* 141 */           this.propertySources.addFirst((PropertySource)propertiesPropertySource);
/*     */         } else {
/*     */           
/* 144 */           this.propertySources.addLast((PropertySource)propertiesPropertySource);
/*     */         }
/*     */       
/* 147 */       } catch (IOException ex) {
/* 148 */         throw new BeanInitializationException("Could not load properties", ex);
/*     */       } 
/*     */     } 
/*     */     
/* 152 */     processProperties(beanFactory, (ConfigurablePropertyResolver)new PropertySourcesPropertyResolver((PropertySources)this.propertySources));
/* 153 */     this.appliedPropertySources = (PropertySources)this.propertySources;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, final ConfigurablePropertyResolver propertyResolver) throws BeansException {
/* 163 */     propertyResolver.setPlaceholderPrefix(this.placeholderPrefix);
/* 164 */     propertyResolver.setPlaceholderSuffix(this.placeholderSuffix);
/* 165 */     propertyResolver.setValueSeparator(this.valueSeparator);
/*     */     
/* 167 */     StringValueResolver valueResolver = new StringValueResolver()
/*     */       {
/*     */         
/*     */         public String resolveStringValue(String strVal)
/*     */         {
/* 172 */           String resolved = PropertySourcesPlaceholderConfigurer.this.ignoreUnresolvablePlaceholders ? propertyResolver.resolvePlaceholders(strVal) : propertyResolver.resolveRequiredPlaceholders(strVal);
/* 173 */           if (PropertySourcesPlaceholderConfigurer.this.trimValues) {
/* 174 */             resolved = resolved.trim();
/*     */           }
/* 176 */           return resolved.equals(PropertySourcesPlaceholderConfigurer.this.nullValue) ? null : resolved;
/*     */         }
/*     */       };
/*     */     
/* 180 */     doProcessProperties(beanFactoryToProcess, valueResolver);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) {
/* 193 */     throw new UnsupportedOperationException("Call processProperties(ConfigurableListableBeanFactory, ConfigurablePropertyResolver) instead");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertySources getAppliedPropertySources() throws IllegalStateException {
/* 205 */     Assert.state((this.appliedPropertySources != null), "PropertySources have not yet been applied");
/* 206 */     return this.appliedPropertySources;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\PropertySourcesPlaceholderConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */