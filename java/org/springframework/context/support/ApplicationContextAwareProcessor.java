/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.config.EmbeddedValueResolver;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.ApplicationEventPublisher;
/*     */ import org.springframework.context.ApplicationEventPublisherAware;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.context.EmbeddedValueResolverAware;
/*     */ import org.springframework.context.EnvironmentAware;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.MessageSourceAware;
/*     */ import org.springframework.context.ResourceLoaderAware;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.ResourceLoader;
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
/*     */ class ApplicationContextAwareProcessor
/*     */   implements BeanPostProcessor
/*     */ {
/*     */   private final ConfigurableApplicationContext applicationContext;
/*     */   private final StringValueResolver embeddedValueResolver;
/*     */   
/*     */   public ApplicationContextAwareProcessor(ConfigurableApplicationContext applicationContext) {
/*  71 */     this.applicationContext = applicationContext;
/*  72 */     this.embeddedValueResolver = (StringValueResolver)new EmbeddedValueResolver((ConfigurableBeanFactory)applicationContext.getBeanFactory());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInitialization(final Object bean, String beanName) throws BeansException {
/*  78 */     AccessControlContext acc = null;
/*     */     
/*  80 */     if (System.getSecurityManager() != null && (bean instanceof EnvironmentAware || bean instanceof EmbeddedValueResolverAware || bean instanceof ResourceLoaderAware || bean instanceof ApplicationEventPublisherAware || bean instanceof MessageSourceAware || bean instanceof ApplicationContextAware))
/*     */     {
/*     */ 
/*     */       
/*  84 */       acc = this.applicationContext.getBeanFactory().getAccessControlContext();
/*     */     }
/*     */     
/*  87 */     if (acc != null) {
/*  88 */       AccessController.doPrivileged(new PrivilegedAction()
/*     */           {
/*     */             public Object run() {
/*  91 */               ApplicationContextAwareProcessor.this.invokeAwareInterfaces(bean);
/*  92 */               return null;
/*     */             }
/*     */           }, 
/*     */           acc);
/*     */     } else {
/*  97 */       invokeAwareInterfaces(bean);
/*     */     } 
/*     */     
/* 100 */     return bean;
/*     */   }
/*     */   
/*     */   private void invokeAwareInterfaces(Object bean) {
/* 104 */     if (bean instanceof org.springframework.beans.factory.Aware) {
/* 105 */       if (bean instanceof EnvironmentAware) {
/* 106 */         ((EnvironmentAware)bean).setEnvironment((Environment)this.applicationContext.getEnvironment());
/*     */       }
/* 108 */       if (bean instanceof EmbeddedValueResolverAware) {
/* 109 */         ((EmbeddedValueResolverAware)bean).setEmbeddedValueResolver(this.embeddedValueResolver);
/*     */       }
/* 111 */       if (bean instanceof ResourceLoaderAware) {
/* 112 */         ((ResourceLoaderAware)bean).setResourceLoader((ResourceLoader)this.applicationContext);
/*     */       }
/* 114 */       if (bean instanceof ApplicationEventPublisherAware) {
/* 115 */         ((ApplicationEventPublisherAware)bean).setApplicationEventPublisher((ApplicationEventPublisher)this.applicationContext);
/*     */       }
/* 117 */       if (bean instanceof MessageSourceAware) {
/* 118 */         ((MessageSourceAware)bean).setMessageSource((MessageSource)this.applicationContext);
/*     */       }
/* 120 */       if (bean instanceof ApplicationContextAware) {
/* 121 */         ((ApplicationContextAware)bean).setApplicationContext((ApplicationContext)this.applicationContext);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName) {
/* 128 */     return bean;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\ApplicationContextAwareProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */