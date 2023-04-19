/*     */ package org.springframework.aop.framework.autoproxy.target;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.framework.autoproxy.TargetSourceCreator;
/*     */ import org.springframework.aop.target.AbstractBeanFactoryBasedTargetSource;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractBeanFactoryBasedTargetSourceCreator
/*     */   implements TargetSourceCreator, BeanFactoryAware, DisposableBean
/*     */ {
/*  60 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   private ConfigurableBeanFactory beanFactory;
/*     */   
/*  65 */   private final Map<String, DefaultListableBeanFactory> internalBeanFactories = new HashMap<String, DefaultListableBeanFactory>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setBeanFactory(BeanFactory beanFactory) {
/*  71 */     if (!(beanFactory instanceof ConfigurableBeanFactory)) {
/*  72 */       throw new IllegalStateException("Cannot do auto-TargetSource creation with a BeanFactory that doesn't implement ConfigurableBeanFactory: " + beanFactory
/*  73 */           .getClass());
/*     */     }
/*  75 */     this.beanFactory = (ConfigurableBeanFactory)beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final BeanFactory getBeanFactory() {
/*  82 */     return (BeanFactory)this.beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TargetSource getTargetSource(Class<?> beanClass, String beanName) {
/*  93 */     AbstractBeanFactoryBasedTargetSource targetSource = createBeanFactoryBasedTargetSource(beanClass, beanName);
/*  94 */     if (targetSource == null) {
/*  95 */       return null;
/*     */     }
/*     */     
/*  98 */     if (this.logger.isDebugEnabled()) {
/*  99 */       this.logger.debug("Configuring AbstractBeanFactoryBasedTargetSource: " + targetSource);
/*     */     }
/*     */     
/* 102 */     DefaultListableBeanFactory internalBeanFactory = getInternalBeanFactoryForBean(beanName);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     BeanDefinition bd = this.beanFactory.getMergedBeanDefinition(beanName);
/* 108 */     GenericBeanDefinition bdCopy = new GenericBeanDefinition(bd);
/* 109 */     if (isPrototypeBased()) {
/* 110 */       bdCopy.setScope("prototype");
/*     */     }
/* 112 */     internalBeanFactory.registerBeanDefinition(beanName, (BeanDefinition)bdCopy);
/*     */ 
/*     */     
/* 115 */     targetSource.setTargetBeanName(beanName);
/* 116 */     targetSource.setBeanFactory((BeanFactory)internalBeanFactory);
/*     */     
/* 118 */     return (TargetSource)targetSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DefaultListableBeanFactory getInternalBeanFactoryForBean(String beanName) {
/* 127 */     synchronized (this.internalBeanFactories) {
/* 128 */       DefaultListableBeanFactory internalBeanFactory = this.internalBeanFactories.get(beanName);
/* 129 */       if (internalBeanFactory == null) {
/* 130 */         internalBeanFactory = buildInternalBeanFactory(this.beanFactory);
/* 131 */         this.internalBeanFactories.put(beanName, internalBeanFactory);
/*     */       } 
/* 133 */       return internalBeanFactory;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DefaultListableBeanFactory buildInternalBeanFactory(ConfigurableBeanFactory containingFactory) {
/* 144 */     DefaultListableBeanFactory internalBeanFactory = new DefaultListableBeanFactory((BeanFactory)containingFactory);
/*     */ 
/*     */     
/* 147 */     internalBeanFactory.copyConfigurationFrom(containingFactory);
/*     */ 
/*     */ 
/*     */     
/* 151 */     for (Iterator<BeanPostProcessor> it = internalBeanFactory.getBeanPostProcessors().iterator(); it.hasNext();) {
/* 152 */       if (it.next() instanceof org.springframework.aop.framework.AopInfrastructureBean) {
/* 153 */         it.remove();
/*     */       }
/*     */     } 
/*     */     
/* 157 */     return internalBeanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 166 */     synchronized (this.internalBeanFactories) {
/* 167 */       for (DefaultListableBeanFactory bf : this.internalBeanFactories.values()) {
/* 168 */         bf.destroySingletons();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isPrototypeBased() {
/* 185 */     return true;
/*     */   }
/*     */   
/*     */   protected abstract AbstractBeanFactoryBasedTargetSource createBeanFactoryBasedTargetSource(Class<?> paramClass, String paramString);
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\autoproxy\target\AbstractBeanFactoryBasedTargetSourceCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */