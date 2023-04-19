/*     */ package org.springframework.context.weaving;
/*     */ 
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.instrument.classloading.LoadTimeWeaver;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoadTimeWeaverAwareProcessor
/*     */   implements BeanPostProcessor, BeanFactoryAware
/*     */ {
/*     */   private LoadTimeWeaver loadTimeWeaver;
/*     */   private BeanFactory beanFactory;
/*     */   
/*     */   public LoadTimeWeaverAwareProcessor() {}
/*     */   
/*     */   public LoadTimeWeaverAwareProcessor(LoadTimeWeaver loadTimeWeaver) {
/*  69 */     this.loadTimeWeaver = loadTimeWeaver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoadTimeWeaverAwareProcessor(BeanFactory beanFactory) {
/*  80 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/*  86 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
/*  92 */     if (bean instanceof LoadTimeWeaverAware) {
/*  93 */       LoadTimeWeaver ltw = this.loadTimeWeaver;
/*  94 */       if (ltw == null) {
/*  95 */         Assert.state((this.beanFactory != null), "BeanFactory required if no LoadTimeWeaver explicitly specified");
/*     */         
/*  97 */         ltw = (LoadTimeWeaver)this.beanFactory.getBean("loadTimeWeaver", LoadTimeWeaver.class);
/*     */       } 
/*     */       
/* 100 */       ((LoadTimeWeaverAware)bean).setLoadTimeWeaver(ltw);
/*     */     } 
/* 102 */     return bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String name) {
/* 107 */     return bean;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\weaving\LoadTimeWeaverAwareProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */