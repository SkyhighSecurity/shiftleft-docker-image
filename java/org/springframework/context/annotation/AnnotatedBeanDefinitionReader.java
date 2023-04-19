/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.support.AutowireCandidateQualifier;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.EnvironmentCapable;
/*     */ import org.springframework.core.env.StandardEnvironment;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
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
/*     */ public class AnnotatedBeanDefinitionReader
/*     */ {
/*     */   private final BeanDefinitionRegistry registry;
/*  48 */   private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
/*     */   
/*  50 */   private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ConditionEvaluator conditionEvaluator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry) {
/*  66 */     this(registry, getOrCreateEnvironment(registry));
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
/*     */   public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry, Environment environment) {
/*  79 */     Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
/*  80 */     Assert.notNull(environment, "Environment must not be null");
/*  81 */     this.registry = registry;
/*  82 */     this.conditionEvaluator = new ConditionEvaluator(registry, environment, null);
/*  83 */     AnnotationConfigUtils.registerAnnotationConfigProcessors(this.registry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final BeanDefinitionRegistry getRegistry() {
/*  91 */     return this.registry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironment(Environment environment) {
/* 101 */     this.conditionEvaluator = new ConditionEvaluator(this.registry, environment, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
/* 109 */     this.beanNameGenerator = (beanNameGenerator != null) ? beanNameGenerator : new AnnotationBeanNameGenerator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScopeMetadataResolver(ScopeMetadataResolver scopeMetadataResolver) {
/* 117 */     this.scopeMetadataResolver = (scopeMetadataResolver != null) ? scopeMetadataResolver : new AnnotationScopeMetadataResolver();
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
/*     */   public void register(Class<?>... annotatedClasses) {
/* 130 */     for (Class<?> annotatedClass : annotatedClasses) {
/* 131 */       registerBean(annotatedClass);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerBean(Class<?> annotatedClass) {
/* 142 */     registerBean(annotatedClass, null, (Class<? extends Annotation>[])null);
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
/*     */   public void registerBean(Class<?> annotatedClass, Class<? extends Annotation>... qualifiers) {
/* 154 */     registerBean(annotatedClass, null, qualifiers);
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
/*     */   public void registerBean(Class<?> annotatedClass, String name, Class<? extends Annotation>... qualifiers) {
/* 167 */     AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(annotatedClass);
/* 168 */     if (this.conditionEvaluator.shouldSkip((AnnotatedTypeMetadata)abd.getMetadata())) {
/*     */       return;
/*     */     }
/*     */     
/* 172 */     ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata((BeanDefinition)abd);
/* 173 */     abd.setScope(scopeMetadata.getScopeName());
/* 174 */     String beanName = (name != null) ? name : this.beanNameGenerator.generateBeanName((BeanDefinition)abd, this.registry);
/* 175 */     AnnotationConfigUtils.processCommonDefinitionAnnotations((AnnotatedBeanDefinition)abd);
/* 176 */     if (qualifiers != null) {
/* 177 */       for (Class<? extends Annotation> qualifier : qualifiers) {
/* 178 */         if (Primary.class == qualifier) {
/* 179 */           abd.setPrimary(true);
/*     */         }
/* 181 */         else if (Lazy.class == qualifier) {
/* 182 */           abd.setLazyInit(true);
/*     */         } else {
/*     */           
/* 185 */           abd.addQualifier(new AutowireCandidateQualifier(qualifier));
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 190 */     BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder((BeanDefinition)abd, beanName);
/* 191 */     definitionHolder = AnnotationConfigUtils.applyScopedProxyMode(scopeMetadata, definitionHolder, this.registry);
/* 192 */     BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, this.registry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Environment getOrCreateEnvironment(BeanDefinitionRegistry registry) {
/* 201 */     Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
/* 202 */     if (registry instanceof EnvironmentCapable) {
/* 203 */       return ((EnvironmentCapable)registry).getEnvironment();
/*     */     }
/* 205 */     return (Environment)new StandardEnvironment();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\AnnotatedBeanDefinitionReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */