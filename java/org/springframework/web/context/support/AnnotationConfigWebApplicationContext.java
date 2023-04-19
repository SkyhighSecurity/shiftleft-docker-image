/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
/*     */ import org.springframework.context.annotation.AnnotationConfigRegistry;
/*     */ import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
/*     */ import org.springframework.context.annotation.ScopeMetadataResolver;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationConfigWebApplicationContext
/*     */   extends AbstractRefreshableWebApplicationContext
/*     */   implements AnnotationConfigRegistry
/*     */ {
/*     */   private BeanNameGenerator beanNameGenerator;
/*     */   private ScopeMetadataResolver scopeMetadataResolver;
/*  90 */   private final Set<Class<?>> annotatedClasses = new LinkedHashSet<Class<?>>();
/*     */   
/*  92 */   private final Set<String> basePackages = new LinkedHashSet<String>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
/* 103 */     this.beanNameGenerator = beanNameGenerator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanNameGenerator getBeanNameGenerator() {
/* 111 */     return this.beanNameGenerator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScopeMetadataResolver(ScopeMetadataResolver scopeMetadataResolver) {
/* 122 */     this.scopeMetadataResolver = scopeMetadataResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ScopeMetadataResolver getScopeMetadataResolver() {
/* 130 */     return this.scopeMetadataResolver;
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
/*     */   public void register(Class<?>... annotatedClasses) {
/* 146 */     Assert.notEmpty((Object[])annotatedClasses, "At least one annotated class must be specified");
/* 147 */     this.annotatedClasses.addAll(Arrays.asList(annotatedClasses));
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
/*     */   public void scan(String... basePackages) {
/* 161 */     Assert.notEmpty((Object[])basePackages, "At least one base package must be specified");
/* 162 */     this.basePackages.addAll(Arrays.asList(basePackages));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
/* 190 */     AnnotatedBeanDefinitionReader reader = getAnnotatedBeanDefinitionReader(beanFactory);
/* 191 */     ClassPathBeanDefinitionScanner scanner = getClassPathBeanDefinitionScanner(beanFactory);
/*     */     
/* 193 */     BeanNameGenerator beanNameGenerator = getBeanNameGenerator();
/* 194 */     if (beanNameGenerator != null) {
/* 195 */       reader.setBeanNameGenerator(beanNameGenerator);
/* 196 */       scanner.setBeanNameGenerator(beanNameGenerator);
/* 197 */       beanFactory.registerSingleton("org.springframework.context.annotation.internalConfigurationBeanNameGenerator", beanNameGenerator);
/*     */     } 
/*     */     
/* 200 */     ScopeMetadataResolver scopeMetadataResolver = getScopeMetadataResolver();
/* 201 */     if (scopeMetadataResolver != null) {
/* 202 */       reader.setScopeMetadataResolver(scopeMetadataResolver);
/* 203 */       scanner.setScopeMetadataResolver(scopeMetadataResolver);
/*     */     } 
/*     */     
/* 206 */     if (!this.annotatedClasses.isEmpty()) {
/* 207 */       if (this.logger.isInfoEnabled()) {
/* 208 */         this.logger.info("Registering annotated classes: [" + 
/* 209 */             StringUtils.collectionToCommaDelimitedString(this.annotatedClasses) + "]");
/*     */       }
/* 211 */       reader.register(ClassUtils.toClassArray(this.annotatedClasses));
/*     */     } 
/*     */     
/* 214 */     if (!this.basePackages.isEmpty()) {
/* 215 */       if (this.logger.isInfoEnabled()) {
/* 216 */         this.logger.info("Scanning base packages: [" + 
/* 217 */             StringUtils.collectionToCommaDelimitedString(this.basePackages) + "]");
/*     */       }
/* 219 */       scanner.scan(StringUtils.toStringArray(this.basePackages));
/*     */     } 
/*     */     
/* 222 */     String[] configLocations = getConfigLocations();
/* 223 */     if (configLocations != null) {
/* 224 */       for (String configLocation : configLocations) {
/*     */         try {
/* 226 */           Class<?> clazz = getClassLoader().loadClass(configLocation);
/* 227 */           if (this.logger.isInfoEnabled()) {
/* 228 */             this.logger.info("Successfully resolved class for [" + configLocation + "]");
/*     */           }
/* 230 */           reader.register(new Class[] { clazz });
/*     */         }
/* 232 */         catch (ClassNotFoundException ex) {
/* 233 */           if (this.logger.isDebugEnabled()) {
/* 234 */             this.logger.debug("Could not load class for config location [" + configLocation + "] - trying package scan. " + ex);
/*     */           }
/*     */           
/* 237 */           int count = scanner.scan(new String[] { configLocation });
/* 238 */           if (this.logger.isInfoEnabled()) {
/* 239 */             if (count == 0) {
/* 240 */               this.logger.info("No annotated classes found for specified class/package [" + configLocation + "]");
/*     */             } else {
/*     */               
/* 243 */               this.logger.info("Found " + count + " annotated classes in package [" + configLocation + "]");
/*     */             } 
/*     */           }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AnnotatedBeanDefinitionReader getAnnotatedBeanDefinitionReader(DefaultListableBeanFactory beanFactory) {
/* 263 */     return new AnnotatedBeanDefinitionReader((BeanDefinitionRegistry)beanFactory, (Environment)getEnvironment());
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
/*     */   protected ClassPathBeanDefinitionScanner getClassPathBeanDefinitionScanner(DefaultListableBeanFactory beanFactory) {
/* 277 */     return new ClassPathBeanDefinitionScanner((BeanDefinitionRegistry)beanFactory, true, (Environment)getEnvironment());
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\support\AnnotationConfigWebApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */