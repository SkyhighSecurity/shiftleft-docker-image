/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionDefaults;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.EnvironmentCapable;
/*     */ import org.springframework.core.env.StandardEnvironment;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.PatternMatchUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassPathBeanDefinitionScanner
/*     */   extends ClassPathScanningCandidateComponentProvider
/*     */ {
/*     */   private final BeanDefinitionRegistry registry;
/*  66 */   private BeanDefinitionDefaults beanDefinitionDefaults = new BeanDefinitionDefaults();
/*     */   
/*     */   private String[] autowireCandidatePatterns;
/*     */   
/*  70 */   private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
/*     */   
/*  72 */   private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean includeAnnotationConfig = true;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
/*  83 */     this(registry, true);
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
/*     */   public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
/* 111 */     this(registry, useDefaultFilters, getOrCreateEnvironment(registry));
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
/*     */   public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment) {
/* 138 */     this(registry, useDefaultFilters, environment, (registry instanceof ResourceLoader) ? (ResourceLoader)registry : null);
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
/*     */   public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment, ResourceLoader resourceLoader) {
/* 160 */     Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
/* 161 */     this.registry = registry;
/*     */     
/* 163 */     if (useDefaultFilters) {
/* 164 */       registerDefaultFilters();
/*     */     }
/* 166 */     setEnvironment(environment);
/* 167 */     setResourceLoader(resourceLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final BeanDefinitionRegistry getRegistry() {
/* 175 */     return this.registry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanDefinitionDefaults(BeanDefinitionDefaults beanDefinitionDefaults) {
/* 183 */     this.beanDefinitionDefaults = (beanDefinitionDefaults != null) ? beanDefinitionDefaults : new BeanDefinitionDefaults();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionDefaults getBeanDefinitionDefaults() {
/* 192 */     return this.beanDefinitionDefaults;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutowireCandidatePatterns(String... autowireCandidatePatterns) {
/* 200 */     this.autowireCandidatePatterns = autowireCandidatePatterns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
/* 208 */     this.beanNameGenerator = (beanNameGenerator != null) ? beanNameGenerator : new AnnotationBeanNameGenerator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScopeMetadataResolver(ScopeMetadataResolver scopeMetadataResolver) {
/* 218 */     this.scopeMetadataResolver = (scopeMetadataResolver != null) ? scopeMetadataResolver : new AnnotationScopeMetadataResolver();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScopedProxyMode(ScopedProxyMode scopedProxyMode) {
/* 229 */     this.scopeMetadataResolver = new AnnotationScopeMetadataResolver(scopedProxyMode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludeAnnotationConfig(boolean includeAnnotationConfig) {
/* 238 */     this.includeAnnotationConfig = includeAnnotationConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int scan(String... basePackages) {
/* 248 */     int beanCountAtScanStart = this.registry.getBeanDefinitionCount();
/*     */     
/* 250 */     doScan(basePackages);
/*     */ 
/*     */     
/* 253 */     if (this.includeAnnotationConfig) {
/* 254 */       AnnotationConfigUtils.registerAnnotationConfigProcessors(this.registry);
/*     */     }
/*     */     
/* 257 */     return this.registry.getBeanDefinitionCount() - beanCountAtScanStart;
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
/*     */   protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
/* 269 */     Assert.notEmpty((Object[])basePackages, "At least one base package must be specified");
/* 270 */     Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<BeanDefinitionHolder>();
/* 271 */     for (String basePackage : basePackages) {
/* 272 */       Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
/* 273 */       for (BeanDefinition candidate : candidates) {
/* 274 */         ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(candidate);
/* 275 */         candidate.setScope(scopeMetadata.getScopeName());
/* 276 */         String beanName = this.beanNameGenerator.generateBeanName(candidate, this.registry);
/* 277 */         if (candidate instanceof AbstractBeanDefinition) {
/* 278 */           postProcessBeanDefinition((AbstractBeanDefinition)candidate, beanName);
/*     */         }
/* 280 */         if (candidate instanceof AnnotatedBeanDefinition) {
/* 281 */           AnnotationConfigUtils.processCommonDefinitionAnnotations((AnnotatedBeanDefinition)candidate);
/*     */         }
/* 283 */         if (checkCandidate(beanName, candidate)) {
/* 284 */           BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(candidate, beanName);
/*     */           
/* 286 */           definitionHolder = AnnotationConfigUtils.applyScopedProxyMode(scopeMetadata, definitionHolder, this.registry);
/* 287 */           beanDefinitions.add(definitionHolder);
/* 288 */           registerBeanDefinition(definitionHolder, this.registry);
/*     */         } 
/*     */       } 
/*     */     } 
/* 292 */     return beanDefinitions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postProcessBeanDefinition(AbstractBeanDefinition beanDefinition, String beanName) {
/* 302 */     beanDefinition.applyDefaults(this.beanDefinitionDefaults);
/* 303 */     if (this.autowireCandidatePatterns != null) {
/* 304 */       beanDefinition.setAutowireCandidate(PatternMatchUtils.simpleMatch(this.autowireCandidatePatterns, beanName));
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
/*     */   protected void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) {
/* 316 */     BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
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
/*     */   protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
/* 332 */     if (!this.registry.containsBeanDefinition(beanName)) {
/* 333 */       return true;
/*     */     }
/* 335 */     BeanDefinition existingDef = this.registry.getBeanDefinition(beanName);
/* 336 */     BeanDefinition originatingDef = existingDef.getOriginatingBeanDefinition();
/* 337 */     if (originatingDef != null) {
/* 338 */       existingDef = originatingDef;
/*     */     }
/* 340 */     if (isCompatible(beanDefinition, existingDef)) {
/* 341 */       return false;
/*     */     }
/* 343 */     throw new ConflictingBeanDefinitionException("Annotation-specified bean name '" + beanName + "' for bean class [" + beanDefinition
/* 344 */         .getBeanClassName() + "] conflicts with existing, non-compatible bean definition of same name and class [" + existingDef
/* 345 */         .getBeanClassName() + "]");
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
/*     */   protected boolean isCompatible(BeanDefinition newDefinition, BeanDefinition existingDefinition) {
/* 360 */     return (!(existingDefinition instanceof ScannedGenericBeanDefinition) || newDefinition
/* 361 */       .getSource().equals(existingDefinition.getSource()) || newDefinition
/* 362 */       .equals(existingDefinition));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Environment getOrCreateEnvironment(BeanDefinitionRegistry registry) {
/* 371 */     Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
/* 372 */     if (registry instanceof EnvironmentCapable) {
/* 373 */       return ((EnvironmentCapable)registry).getEnvironment();
/*     */     }
/* 375 */     return (Environment)new StandardEnvironment();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\ClassPathBeanDefinitionScanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */