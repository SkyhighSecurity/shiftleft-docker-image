/*     */ package org.springframework.beans.factory.wiring;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanConfigurerSupport
/*     */   implements BeanFactoryAware, InitializingBean, DisposableBean
/*     */ {
/*  52 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile BeanWiringInfoResolver beanWiringInfoResolver;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile ConfigurableListableBeanFactory beanFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanWiringInfoResolver(BeanWiringInfoResolver beanWiringInfoResolver) {
/*  67 */     Assert.notNull(beanWiringInfoResolver, "BeanWiringInfoResolver must not be null");
/*  68 */     this.beanWiringInfoResolver = beanWiringInfoResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/*  76 */     if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
/*  77 */       throw new IllegalArgumentException("Bean configurer aspect needs to run in a ConfigurableListableBeanFactory: " + beanFactory);
/*     */     }
/*     */     
/*  80 */     this.beanFactory = (ConfigurableListableBeanFactory)beanFactory;
/*  81 */     if (this.beanWiringInfoResolver == null) {
/*  82 */       this.beanWiringInfoResolver = createDefaultBeanWiringInfoResolver();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanWiringInfoResolver createDefaultBeanWiringInfoResolver() {
/*  93 */     return new ClassNameBeanWiringInfoResolver();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 101 */     Assert.notNull(this.beanFactory, "BeanFactory must be set");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 110 */     this.beanFactory = null;
/* 111 */     this.beanWiringInfoResolver = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void configureBean(Object beanInstance) {
/* 122 */     if (this.beanFactory == null) {
/* 123 */       if (this.logger.isDebugEnabled()) {
/* 124 */         this.logger.debug("BeanFactory has not been set on " + ClassUtils.getShortName(getClass()) + ": Make sure this configurer runs in a Spring container. Unable to configure bean of type [" + 
/*     */             
/* 126 */             ClassUtils.getDescriptiveType(beanInstance) + "]. Proceeding without injection.");
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 131 */     BeanWiringInfo bwi = this.beanWiringInfoResolver.resolveWiringInfo(beanInstance);
/* 132 */     if (bwi == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 138 */       if (bwi.indicatesAutowiring() || (bwi
/* 139 */         .isDefaultBeanName() && !this.beanFactory.containsBean(bwi.getBeanName())))
/*     */       {
/* 141 */         this.beanFactory.autowireBeanProperties(beanInstance, bwi.getAutowireMode(), bwi.getDependencyCheck());
/* 142 */         Object result = this.beanFactory.initializeBean(beanInstance, bwi.getBeanName());
/* 143 */         checkExposedObject(result, beanInstance);
/*     */       }
/*     */       else
/*     */       {
/* 147 */         Object result = this.beanFactory.configureBean(beanInstance, bwi.getBeanName());
/* 148 */         checkExposedObject(result, beanInstance);
/*     */       }
/*     */     
/* 151 */     } catch (BeanCreationException ex) {
/* 152 */       Throwable rootCause = ex.getMostSpecificCause();
/* 153 */       if (rootCause instanceof org.springframework.beans.factory.BeanCurrentlyInCreationException) {
/* 154 */         BeanCreationException bce = (BeanCreationException)rootCause;
/* 155 */         if (this.beanFactory.isCurrentlyInCreation(bce.getBeanName())) {
/* 156 */           if (this.logger.isDebugEnabled()) {
/* 157 */             this.logger.debug("Failed to create target bean '" + bce.getBeanName() + "' while configuring object of type [" + beanInstance
/* 158 */                 .getClass().getName() + "] - probably due to a circular reference. This is a common startup situation and usually not fatal. Proceeding without injection. Original exception: " + ex);
/*     */           }
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */       
/* 165 */       throw ex;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkExposedObject(Object exposedObject, Object originalBeanInstance) {
/* 170 */     if (exposedObject != originalBeanInstance)
/* 171 */       throw new IllegalStateException("Post-processor tried to replace bean instance of type [" + originalBeanInstance
/* 172 */           .getClass().getName() + "] with (proxy) object of type [" + exposedObject
/* 173 */           .getClass().getName() + "] - not supported for aspect-configured classes!"); 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\wiring\BeanConfigurerSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */