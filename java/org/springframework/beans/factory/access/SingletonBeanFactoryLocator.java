/*     */ package org.springframework.beans.factory.access;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.FatalBeanException;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
/*     */ import org.springframework.core.io.support.ResourcePatternUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SingletonBeanFactoryLocator
/*     */   implements BeanFactoryLocator
/*     */ {
/*     */   private static final String DEFAULT_RESOURCE_LOCATION = "classpath*:beanRefFactory.xml";
/* 275 */   protected static final Log logger = LogFactory.getLog(SingletonBeanFactoryLocator.class);
/*     */ 
/*     */   
/* 278 */   private static final Map<String, BeanFactoryLocator> instances = new HashMap<String, BeanFactoryLocator>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BeanFactoryLocator getInstance() throws BeansException {
/* 290 */     return getInstance(null);
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
/*     */   public static BeanFactoryLocator getInstance(String selector) throws BeansException {
/* 309 */     String resourceLocation = selector;
/* 310 */     if (resourceLocation == null) {
/* 311 */       resourceLocation = "classpath*:beanRefFactory.xml";
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 316 */     if (!ResourcePatternUtils.isUrl(resourceLocation)) {
/* 317 */       resourceLocation = "classpath*:" + resourceLocation;
/*     */     }
/*     */     
/* 320 */     synchronized (instances) {
/* 321 */       if (logger.isTraceEnabled()) {
/* 322 */         logger.trace("SingletonBeanFactoryLocator.getInstance(): instances.hashCode=" + instances
/* 323 */             .hashCode() + ", instances=" + instances);
/*     */       }
/* 325 */       BeanFactoryLocator bfl = instances.get(resourceLocation);
/* 326 */       if (bfl == null) {
/* 327 */         bfl = new SingletonBeanFactoryLocator(resourceLocation);
/* 328 */         instances.put(resourceLocation, bfl);
/*     */       } 
/* 330 */       return bfl;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 336 */   private final Map<String, BeanFactoryGroup> bfgInstancesByKey = new HashMap<String, BeanFactoryGroup>();
/*     */   
/* 338 */   private final Map<BeanFactory, BeanFactoryGroup> bfgInstancesByObj = new HashMap<BeanFactory, BeanFactoryGroup>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String resourceLocation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SingletonBeanFactoryLocator(String resourceLocation) {
/* 350 */     this.resourceLocation = resourceLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanFactoryReference useBeanFactory(String factoryKey) throws BeansException {
/* 355 */     synchronized (this.bfgInstancesByKey) {
/* 356 */       BeanFactoryGroup bfg = this.bfgInstancesByKey.get(this.resourceLocation);
/*     */       
/* 358 */       if (bfg != null) {
/* 359 */         bfg.refCount++;
/*     */       }
/*     */       else {
/*     */         
/* 363 */         if (logger.isTraceEnabled()) {
/* 364 */           logger.trace("Factory group with resource name [" + this.resourceLocation + "] requested. Creating new instance.");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 369 */         BeanFactory groupContext = createDefinition(this.resourceLocation, factoryKey);
/*     */ 
/*     */         
/* 372 */         bfg = new BeanFactoryGroup();
/* 373 */         bfg.definition = groupContext;
/* 374 */         bfg.refCount = 1;
/* 375 */         this.bfgInstancesByKey.put(this.resourceLocation, bfg);
/* 376 */         this.bfgInstancesByObj.put(groupContext, bfg);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 383 */           initializeDefinition(groupContext);
/*     */         }
/* 385 */         catch (BeansException ex) {
/* 386 */           this.bfgInstancesByKey.remove(this.resourceLocation);
/* 387 */           this.bfgInstancesByObj.remove(groupContext);
/* 388 */           throw new BootstrapException("Unable to initialize group definition. Group resource name [" + this.resourceLocation + "], factory key [" + factoryKey + "]", ex);
/*     */         } 
/*     */       } 
/*     */       
/*     */       try {
/*     */         BeanFactory beanFactory;
/*     */         
/* 395 */         if (factoryKey != null) {
/* 396 */           beanFactory = (BeanFactory)bfg.definition.getBean(factoryKey, BeanFactory.class);
/*     */         } else {
/*     */           
/* 399 */           beanFactory = (BeanFactory)bfg.definition.getBean(BeanFactory.class);
/*     */         } 
/* 401 */         return new CountingBeanFactoryReference(beanFactory, bfg.definition);
/*     */       }
/* 403 */       catch (BeansException ex) {
/* 404 */         throw new BootstrapException("Unable to return specified BeanFactory instance: factory key [" + factoryKey + "], from group with resource name [" + this.resourceLocation + "]", ex);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanFactory createDefinition(String resourceLocation, String factoryKey) {
/* 428 */     DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
/* 429 */     XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader((BeanDefinitionRegistry)factory);
/* 430 */     PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
/*     */     
/*     */     try {
/* 433 */       Resource[] configResources = pathMatchingResourcePatternResolver.getResources(resourceLocation);
/* 434 */       if (configResources.length == 0) {
/* 435 */         throw new FatalBeanException("Unable to find resource for specified definition. Group resource name [" + this.resourceLocation + "], factory key [" + factoryKey + "]");
/*     */       }
/*     */       
/* 438 */       reader.loadBeanDefinitions(configResources);
/*     */     }
/* 440 */     catch (IOException ex) {
/* 441 */       throw new BeanDefinitionStoreException("Error accessing bean definition resource [" + this.resourceLocation + "]", ex);
/*     */     
/*     */     }
/* 444 */     catch (BeanDefinitionStoreException ex) {
/* 445 */       throw new FatalBeanException("Unable to load group definition: group resource name [" + this.resourceLocation + "], factory key [" + factoryKey + "]", ex);
/*     */     } 
/*     */ 
/*     */     
/* 449 */     return (BeanFactory)factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initializeDefinition(BeanFactory groupDef) {
/* 459 */     if (groupDef instanceof ConfigurableListableBeanFactory) {
/* 460 */       ((ConfigurableListableBeanFactory)groupDef).preInstantiateSingletons();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void destroyDefinition(BeanFactory groupDef, String selector) {
/* 470 */     if (groupDef instanceof ConfigurableBeanFactory) {
/* 471 */       if (logger.isTraceEnabled()) {
/* 472 */         logger.trace("Factory group with selector '" + selector + "' being released, as there are no more references to it");
/*     */       }
/*     */       
/* 475 */       ((ConfigurableBeanFactory)groupDef).destroySingletons();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static class BeanFactoryGroup
/*     */   {
/*     */     private BeanFactory definition;
/*     */ 
/*     */     
/*     */     private BeanFactoryGroup() {}
/*     */     
/* 487 */     private int refCount = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class CountingBeanFactoryReference
/*     */     implements BeanFactoryReference
/*     */   {
/*     */     private BeanFactory beanFactory;
/*     */     
/*     */     private BeanFactory groupContextRef;
/*     */ 
/*     */     
/*     */     public CountingBeanFactoryReference(BeanFactory beanFactory, BeanFactory groupContext) {
/* 501 */       this.beanFactory = beanFactory;
/* 502 */       this.groupContextRef = groupContext;
/*     */     }
/*     */ 
/*     */     
/*     */     public BeanFactory getFactory() {
/* 507 */       return this.beanFactory;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void release() throws FatalBeanException {
/* 513 */       synchronized (SingletonBeanFactoryLocator.this.bfgInstancesByKey) {
/* 514 */         BeanFactory savedRef = this.groupContextRef;
/* 515 */         if (savedRef != null) {
/* 516 */           this.groupContextRef = null;
/* 517 */           SingletonBeanFactoryLocator.BeanFactoryGroup bfg = (SingletonBeanFactoryLocator.BeanFactoryGroup)SingletonBeanFactoryLocator.this.bfgInstancesByObj.get(savedRef);
/* 518 */           if (bfg != null) {
/* 519 */             bfg.refCount--;
/* 520 */             if (bfg.refCount == 0) {
/* 521 */               SingletonBeanFactoryLocator.this.destroyDefinition(savedRef, SingletonBeanFactoryLocator.this.resourceLocation);
/* 522 */               SingletonBeanFactoryLocator.this.bfgInstancesByKey.remove(SingletonBeanFactoryLocator.this.resourceLocation);
/* 523 */               SingletonBeanFactoryLocator.this.bfgInstancesByObj.remove(savedRef);
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/* 528 */             SingletonBeanFactoryLocator.logger.warn("Tried to release a SingletonBeanFactoryLocator group definition more times than it has actually been used. Resource name [" + SingletonBeanFactoryLocator.this
/* 529 */                 .resourceLocation + "]");
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\access\SingletonBeanFactoryLocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */