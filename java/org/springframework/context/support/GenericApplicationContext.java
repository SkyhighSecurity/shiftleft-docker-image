/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.ResourcePatternResolver;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GenericApplicationContext
/*     */   extends AbstractApplicationContext
/*     */   implements BeanDefinitionRegistry
/*     */ {
/*     */   private final DefaultListableBeanFactory beanFactory;
/*     */   private ResourceLoader resourceLoader;
/*     */   private boolean customClassLoader = false;
/*  96 */   private final AtomicBoolean refreshed = new AtomicBoolean();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericApplicationContext() {
/* 105 */     this.beanFactory = new DefaultListableBeanFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericApplicationContext(DefaultListableBeanFactory beanFactory) {
/* 115 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/* 116 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericApplicationContext(ApplicationContext parent) {
/* 126 */     this();
/* 127 */     setParent(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericApplicationContext(DefaultListableBeanFactory beanFactory, ApplicationContext parent) {
/* 138 */     this(beanFactory);
/* 139 */     setParent(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParent(ApplicationContext parent) {
/* 150 */     super.setParent(parent);
/* 151 */     this.beanFactory.setParentBeanFactory(getInternalParentBeanFactory());
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
/* 162 */     this.beanFactory.setAllowBeanDefinitionOverriding(allowBeanDefinitionOverriding);
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
/*     */   public void setAllowCircularReferences(boolean allowCircularReferences) {
/* 174 */     this.beanFactory.setAllowCircularReferences(allowCircularReferences);
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
/*     */   public void setResourceLoader(ResourceLoader resourceLoader) {
/* 196 */     this.resourceLoader = resourceLoader;
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
/*     */   public Resource getResource(String location) {
/* 211 */     if (this.resourceLoader != null) {
/* 212 */       return this.resourceLoader.getResource(location);
/*     */     }
/* 214 */     return super.getResource(location);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource[] getResources(String locationPattern) throws IOException {
/* 225 */     if (this.resourceLoader instanceof ResourcePatternResolver) {
/* 226 */       return ((ResourcePatternResolver)this.resourceLoader).getResources(locationPattern);
/*     */     }
/* 228 */     return super.getResources(locationPattern);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setClassLoader(ClassLoader classLoader) {
/* 233 */     super.setClassLoader(classLoader);
/* 234 */     this.customClassLoader = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getClassLoader() {
/* 239 */     if (this.resourceLoader != null && !this.customClassLoader) {
/* 240 */       return this.resourceLoader.getClassLoader();
/*     */     }
/* 242 */     return super.getClassLoader();
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
/*     */   protected final void refreshBeanFactory() throws IllegalStateException {
/* 257 */     if (!this.refreshed.compareAndSet(false, true)) {
/* 258 */       throw new IllegalStateException("GenericApplicationContext does not support multiple refresh attempts: just call 'refresh' once");
/*     */     }
/*     */     
/* 261 */     this.beanFactory.setSerializationId(getId());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void cancelRefresh(BeansException ex) {
/* 266 */     this.beanFactory.setSerializationId(null);
/* 267 */     super.cancelRefresh(ex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void closeBeanFactory() {
/* 276 */     this.beanFactory.setSerializationId(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ConfigurableListableBeanFactory getBeanFactory() {
/* 285 */     return (ConfigurableListableBeanFactory)this.beanFactory;
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
/*     */   public final DefaultListableBeanFactory getDefaultListableBeanFactory() {
/* 297 */     return this.beanFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
/* 302 */     assertBeanFactoryActive();
/* 303 */     return (AutowireCapableBeanFactory)this.beanFactory;
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
/*     */   public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
/* 315 */     this.beanFactory.registerBeanDefinition(beanName, beanDefinition);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
/* 320 */     this.beanFactory.removeBeanDefinition(beanName);
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
/* 325 */     return this.beanFactory.getBeanDefinition(beanName);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBeanNameInUse(String beanName) {
/* 330 */     return this.beanFactory.isBeanNameInUse(beanName);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerAlias(String beanName, String alias) {
/* 335 */     this.beanFactory.registerAlias(beanName, alias);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAlias(String alias) {
/* 340 */     this.beanFactory.removeAlias(alias);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAlias(String beanName) {
/* 345 */     return this.beanFactory.isAlias(beanName);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\GenericApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */