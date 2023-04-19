/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.BeanNameAware;
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
/*     */ public abstract class PlaceholderConfigurerSupport
/*     */   extends PropertyResourceConfigurer
/*     */   implements BeanNameAware, BeanFactoryAware
/*     */ {
/*     */   public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";
/*     */   public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";
/*     */   public static final String DEFAULT_VALUE_SEPARATOR = ":";
/* 102 */   protected String placeholderPrefix = "${";
/*     */ 
/*     */   
/* 105 */   protected String placeholderSuffix = "}";
/*     */ 
/*     */   
/* 108 */   protected String valueSeparator = ":";
/*     */ 
/*     */   
/*     */   protected boolean trimValues = false;
/*     */ 
/*     */   
/*     */   protected String nullValue;
/*     */ 
/*     */   
/*     */   protected boolean ignoreUnresolvablePlaceholders = false;
/*     */ 
/*     */   
/*     */   private String beanName;
/*     */   
/*     */   private BeanFactory beanFactory;
/*     */ 
/*     */   
/*     */   public void setPlaceholderPrefix(String placeholderPrefix) {
/* 126 */     this.placeholderPrefix = placeholderPrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlaceholderSuffix(String placeholderSuffix) {
/* 134 */     this.placeholderSuffix = placeholderSuffix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValueSeparator(String valueSeparator) {
/* 144 */     this.valueSeparator = valueSeparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrimValues(boolean trimValues) {
/* 154 */     this.trimValues = trimValues;
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
/*     */   public void setNullValue(String nullValue) {
/* 167 */     this.nullValue = nullValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreUnresolvablePlaceholders(boolean ignoreUnresolvablePlaceholders) {
/* 178 */     this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
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
/*     */   public void setBeanName(String beanName) {
/* 191 */     this.beanName = beanName;
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
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 204 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doProcessProperties(ConfigurableListableBeanFactory beanFactoryToProcess, StringValueResolver valueResolver) {
/* 211 */     BeanDefinitionVisitor visitor = new BeanDefinitionVisitor(valueResolver);
/*     */     
/* 213 */     String[] beanNames = beanFactoryToProcess.getBeanDefinitionNames();
/* 214 */     for (String curName : beanNames) {
/*     */ 
/*     */       
/* 217 */       if (!curName.equals(this.beanName) || !beanFactoryToProcess.equals(this.beanFactory)) {
/* 218 */         BeanDefinition bd = beanFactoryToProcess.getBeanDefinition(curName);
/*     */         try {
/* 220 */           visitor.visitBeanDefinition(bd);
/*     */         }
/* 222 */         catch (Exception ex) {
/* 223 */           throw new BeanDefinitionStoreException(bd.getResourceDescription(), curName, ex.getMessage(), ex);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 229 */     beanFactoryToProcess.resolveAliases(valueResolver);
/*     */ 
/*     */     
/* 232 */     beanFactoryToProcess.addEmbeddedValueResolver(valueResolver);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\PlaceholderConfigurerSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */