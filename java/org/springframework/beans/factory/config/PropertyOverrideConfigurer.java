/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.PropertyValue;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
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
/*     */ public class PropertyOverrideConfigurer
/*     */   extends PropertyResourceConfigurer
/*     */ {
/*     */   public static final String DEFAULT_BEAN_NAME_SEPARATOR = ".";
/*  70 */   private String beanNameSeparator = ".";
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean ignoreInvalidKeys = false;
/*     */ 
/*     */   
/*  77 */   private final Set<String> beanNames = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>(16));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanNameSeparator(String beanNameSeparator) {
/*  85 */     this.beanNameSeparator = beanNameSeparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreInvalidKeys(boolean ignoreInvalidKeys) {
/*  95 */     this.ignoreInvalidKeys = ignoreInvalidKeys;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
/* 103 */     for (Enumeration<?> names = props.propertyNames(); names.hasMoreElements(); ) {
/* 104 */       String key = (String)names.nextElement();
/*     */       try {
/* 106 */         processKey(beanFactory, key, props.getProperty(key));
/*     */       }
/* 108 */       catch (BeansException ex) {
/* 109 */         String msg = "Could not process key '" + key + "' in PropertyOverrideConfigurer";
/* 110 */         if (!this.ignoreInvalidKeys) {
/* 111 */           throw new BeanInitializationException(msg, ex);
/*     */         }
/* 113 */         if (this.logger.isDebugEnabled()) {
/* 114 */           this.logger.debug(msg, (Throwable)ex);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processKey(ConfigurableListableBeanFactory factory, String key, String value) throws BeansException {
/* 126 */     int separatorIndex = key.indexOf(this.beanNameSeparator);
/* 127 */     if (separatorIndex == -1) {
/* 128 */       throw new BeanInitializationException("Invalid key '" + key + "': expected 'beanName" + this.beanNameSeparator + "property'");
/*     */     }
/*     */     
/* 131 */     String beanName = key.substring(0, separatorIndex);
/* 132 */     String beanProperty = key.substring(separatorIndex + 1);
/* 133 */     this.beanNames.add(beanName);
/* 134 */     applyPropertyValue(factory, beanName, beanProperty, value);
/* 135 */     if (this.logger.isDebugEnabled()) {
/* 136 */       this.logger.debug("Property '" + key + "' set to value [" + value + "]");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyPropertyValue(ConfigurableListableBeanFactory factory, String beanName, String property, String value) {
/* 146 */     BeanDefinition bd = factory.getBeanDefinition(beanName);
/* 147 */     while (bd.getOriginatingBeanDefinition() != null) {
/* 148 */       bd = bd.getOriginatingBeanDefinition();
/*     */     }
/* 150 */     PropertyValue pv = new PropertyValue(property, value);
/* 151 */     pv.setOptional(this.ignoreInvalidKeys);
/* 152 */     bd.getPropertyValues().addPropertyValue(pv);
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
/*     */   public boolean hasPropertyOverridesFor(String beanName) {
/* 164 */     return this.beanNames.contains(beanName);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\PropertyOverrideConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */