/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
/*     */ import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.context.event.ApplicationEventMulticaster;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ class ApplicationListenerDetector
/*     */   implements DestructionAwareBeanPostProcessor, MergedBeanDefinitionPostProcessor
/*     */ {
/*  47 */   private static final Log logger = LogFactory.getLog(ApplicationListenerDetector.class);
/*     */   
/*     */   private final transient AbstractApplicationContext applicationContext;
/*     */   
/*  51 */   private final transient Map<String, Boolean> singletonNames = new ConcurrentHashMap<String, Boolean>(256);
/*     */ 
/*     */   
/*     */   public ApplicationListenerDetector(AbstractApplicationContext applicationContext) {
/*  55 */     this.applicationContext = applicationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
/*  61 */     if (this.applicationContext != null) {
/*  62 */       this.singletonNames.put(beanName, Boolean.valueOf(beanDefinition.isSingleton()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName) {
/*  68 */     return bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName) {
/*  73 */     if (this.applicationContext != null && bean instanceof ApplicationListener) {
/*     */       
/*  75 */       Boolean flag = this.singletonNames.get(beanName);
/*  76 */       if (Boolean.TRUE.equals(flag)) {
/*     */         
/*  78 */         this.applicationContext.addApplicationListener((ApplicationListener)bean);
/*     */       }
/*  80 */       else if (Boolean.FALSE.equals(flag)) {
/*  81 */         if (logger.isWarnEnabled() && !this.applicationContext.containsBean(beanName))
/*     */         {
/*  83 */           logger.warn("Inner bean '" + beanName + "' implements ApplicationListener interface but is not reachable for event multicasting by its containing ApplicationContext because it does not have singleton scope. Only top-level listener beans are allowed to be of non-singleton scope.");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*  88 */         this.singletonNames.remove(beanName);
/*     */       } 
/*     */     } 
/*  91 */     return bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcessBeforeDestruction(Object bean, String beanName) {
/*  96 */     if (this.applicationContext != null && bean instanceof ApplicationListener) {
/*     */       try {
/*  98 */         ApplicationEventMulticaster multicaster = this.applicationContext.getApplicationEventMulticaster();
/*  99 */         multicaster.removeApplicationListener((ApplicationListener)bean);
/* 100 */         multicaster.removeApplicationListenerBean(beanName);
/*     */       }
/* 102 */       catch (IllegalStateException illegalStateException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresDestruction(Object bean) {
/* 110 */     return bean instanceof ApplicationListener;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 116 */     return (this == other || (other instanceof ApplicationListenerDetector && this.applicationContext == ((ApplicationListenerDetector)other).applicationContext));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 122 */     return ObjectUtils.nullSafeHashCode(this.applicationContext);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\ApplicationListenerDetector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */