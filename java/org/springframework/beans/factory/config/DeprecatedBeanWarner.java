/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class DeprecatedBeanWarner
/*     */   implements BeanFactoryPostProcessor
/*     */ {
/*  38 */   protected transient Log logger = LogFactory.getLog(getClass());
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
/*     */   public void setLoggerName(String loggerName) {
/*  51 */     this.logger = LogFactory.getLog(loggerName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
/*  57 */     if (isLogEnabled()) {
/*  58 */       String[] beanNames = beanFactory.getBeanDefinitionNames();
/*  59 */       for (String beanName : beanNames) {
/*  60 */         String nameToLookup = beanName;
/*  61 */         if (beanFactory.isFactoryBean(beanName)) {
/*  62 */           nameToLookup = "&" + beanName;
/*     */         }
/*  64 */         Class<?> beanType = ClassUtils.getUserClass(beanFactory.getType(nameToLookup));
/*  65 */         if (beanType != null && beanType.isAnnotationPresent((Class)Deprecated.class)) {
/*  66 */           BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
/*  67 */           logDeprecatedBean(beanName, beanType, beanDefinition);
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
/*     */   
/*     */   protected void logDeprecatedBean(String beanName, Class<?> beanType, BeanDefinition beanDefinition) {
/*  80 */     StringBuilder builder = new StringBuilder();
/*  81 */     builder.append(beanType);
/*  82 */     builder.append(" ['");
/*  83 */     builder.append(beanName);
/*  84 */     builder.append('\'');
/*  85 */     String resourceDescription = beanDefinition.getResourceDescription();
/*  86 */     if (StringUtils.hasLength(resourceDescription)) {
/*  87 */       builder.append(" in ");
/*  88 */       builder.append(resourceDescription);
/*     */     } 
/*  90 */     builder.append("] has been deprecated");
/*  91 */     writeToLog(builder.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeToLog(String message) {
/* 100 */     this.logger.warn(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isLogEnabled() {
/* 109 */     return this.logger.isWarnEnabled();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\DeprecatedBeanWarner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */