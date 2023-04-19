/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractRefreshableApplicationContext
/*     */   extends AbstractApplicationContext
/*     */ {
/*     */   private Boolean allowBeanDefinitionOverriding;
/*     */   private Boolean allowCircularReferences;
/*     */   private DefaultListableBeanFactory beanFactory;
/*  74 */   private final Object beanFactoryMonitor = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractRefreshableApplicationContext() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractRefreshableApplicationContext(ApplicationContext parent) {
/*  88 */     super(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowBeanDefinitionOverriding(boolean allowBeanDefinitionOverriding) {
/*  99 */     this.allowBeanDefinitionOverriding = Boolean.valueOf(allowBeanDefinitionOverriding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowCircularReferences(boolean allowCircularReferences) {
/* 110 */     this.allowCircularReferences = Boolean.valueOf(allowCircularReferences);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void refreshBeanFactory() throws BeansException {
/* 121 */     if (hasBeanFactory()) {
/* 122 */       destroyBeans();
/* 123 */       closeBeanFactory();
/*     */     } 
/*     */     try {
/* 126 */       DefaultListableBeanFactory beanFactory = createBeanFactory();
/* 127 */       beanFactory.setSerializationId(getId());
/* 128 */       customizeBeanFactory(beanFactory);
/* 129 */       loadBeanDefinitions(beanFactory);
/* 130 */       synchronized (this.beanFactoryMonitor) {
/* 131 */         this.beanFactory = beanFactory;
/*     */       }
/*     */     
/* 134 */     } catch (IOException ex) {
/* 135 */       throw new ApplicationContextException("I/O error parsing bean definition source for " + getDisplayName(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void cancelRefresh(BeansException ex) {
/* 141 */     synchronized (this.beanFactoryMonitor) {
/* 142 */       if (this.beanFactory != null)
/* 143 */         this.beanFactory.setSerializationId(null); 
/*     */     } 
/* 145 */     super.cancelRefresh(ex);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void closeBeanFactory() {
/* 150 */     synchronized (this.beanFactoryMonitor) {
/* 151 */       this.beanFactory.setSerializationId(null);
/* 152 */       this.beanFactory = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean hasBeanFactory() {
/* 161 */     synchronized (this.beanFactoryMonitor) {
/* 162 */       return (this.beanFactory != null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final ConfigurableListableBeanFactory getBeanFactory() {
/* 168 */     synchronized (this.beanFactoryMonitor) {
/* 169 */       if (this.beanFactory == null) {
/* 170 */         throw new IllegalStateException("BeanFactory not initialized or already closed - call 'refresh' before accessing beans via the ApplicationContext");
/*     */       }
/*     */       
/* 173 */       return (ConfigurableListableBeanFactory)this.beanFactory;
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
/*     */   protected void assertBeanFactoryActive() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DefaultListableBeanFactory createBeanFactory() {
/* 200 */     return new DefaultListableBeanFactory(getInternalParentBeanFactory());
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
/*     */   protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
/* 218 */     if (this.allowBeanDefinitionOverriding != null) {
/* 219 */       beanFactory.setAllowBeanDefinitionOverriding(this.allowBeanDefinitionOverriding.booleanValue());
/*     */     }
/* 221 */     if (this.allowCircularReferences != null)
/* 222 */       beanFactory.setAllowCircularReferences(this.allowCircularReferences.booleanValue()); 
/*     */   }
/*     */   
/*     */   protected abstract void loadBeanDefinitions(DefaultListableBeanFactory paramDefaultListableBeanFactory) throws BeansException, IOException;
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\AbstractRefreshableApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */