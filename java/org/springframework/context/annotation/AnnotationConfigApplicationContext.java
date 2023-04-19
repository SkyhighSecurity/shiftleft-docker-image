/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.context.support.GenericApplicationContext;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.Environment;
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
/*     */ public class AnnotationConfigApplicationContext
/*     */   extends GenericApplicationContext
/*     */   implements AnnotationConfigRegistry
/*     */ {
/*     */   private final AnnotatedBeanDefinitionReader reader;
/*     */   private final ClassPathBeanDefinitionScanner scanner;
/*     */   
/*     */   public AnnotationConfigApplicationContext() {
/*  61 */     this.reader = new AnnotatedBeanDefinitionReader((BeanDefinitionRegistry)this);
/*  62 */     this.scanner = new ClassPathBeanDefinitionScanner((BeanDefinitionRegistry)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationConfigApplicationContext(DefaultListableBeanFactory beanFactory) {
/*  70 */     super(beanFactory);
/*  71 */     this.reader = new AnnotatedBeanDefinitionReader((BeanDefinitionRegistry)this);
/*  72 */     this.scanner = new ClassPathBeanDefinitionScanner((BeanDefinitionRegistry)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationConfigApplicationContext(Class<?>... annotatedClasses) {
/*  82 */     this();
/*  83 */     register(annotatedClasses);
/*  84 */     refresh();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationConfigApplicationContext(String... basePackages) {
/*  93 */     this();
/*  94 */     scan(basePackages);
/*  95 */     refresh();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironment(ConfigurableEnvironment environment) {
/* 105 */     super.setEnvironment(environment);
/* 106 */     this.reader.setEnvironment((Environment)environment);
/* 107 */     this.scanner.setEnvironment((Environment)environment);
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
/*     */   public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
/* 120 */     this.reader.setBeanNameGenerator(beanNameGenerator);
/* 121 */     this.scanner.setBeanNameGenerator(beanNameGenerator);
/* 122 */     getBeanFactory().registerSingleton("org.springframework.context.annotation.internalConfigurationBeanNameGenerator", beanNameGenerator);
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
/* 133 */     this.reader.setScopeMetadataResolver(scopeMetadataResolver);
/* 134 */     this.scanner.setScopeMetadataResolver(scopeMetadataResolver);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void prepareRefresh() {
/* 139 */     this.scanner.clearCache();
/* 140 */     super.prepareRefresh();
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
/*     */   public void register(Class<?>... annotatedClasses) {
/* 158 */     Assert.notEmpty((Object[])annotatedClasses, "At least one annotated class must be specified");
/* 159 */     this.reader.register(annotatedClasses);
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
/*     */   public void scan(String... basePackages) {
/* 171 */     Assert.notEmpty((Object[])basePackages, "At least one base package must be specified");
/* 172 */     this.scanner.scan(basePackages);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\AnnotationConfigApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */