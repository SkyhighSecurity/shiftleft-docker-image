/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*     */ import org.springframework.beans.factory.annotation.Lookup;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.context.ResourceLoaderAware;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.EnvironmentCapable;
/*     */ import org.springframework.core.env.StandardEnvironment;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.ResourcePatternResolver;
/*     */ import org.springframework.core.io.support.ResourcePatternUtils;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
/*     */ import org.springframework.core.type.classreading.MetadataReader;
/*     */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*     */ import org.springframework.core.type.filter.AnnotationTypeFilter;
/*     */ import org.springframework.core.type.filter.TypeFilter;
/*     */ import org.springframework.stereotype.Component;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassPathScanningCandidateComponentProvider
/*     */   implements EnvironmentCapable, ResourceLoaderAware
/*     */ {
/*     */   static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
/*  77 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  79 */   private String resourcePattern = "**/*.class";
/*     */   
/*  81 */   private final List<TypeFilter> includeFilters = new LinkedList<TypeFilter>();
/*     */   
/*  83 */   private final List<TypeFilter> excludeFilters = new LinkedList<TypeFilter>();
/*     */ 
/*     */ 
/*     */   
/*     */   private Environment environment;
/*     */ 
/*     */ 
/*     */   
/*     */   private ConditionEvaluator conditionEvaluator;
/*     */ 
/*     */ 
/*     */   
/*     */   private ResourcePatternResolver resourcePatternResolver;
/*     */ 
/*     */ 
/*     */   
/*     */   private MetadataReaderFactory metadataReaderFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassPathScanningCandidateComponentProvider() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassPathScanningCandidateComponentProvider(boolean useDefaultFilters) {
/* 110 */     this(useDefaultFilters, (Environment)new StandardEnvironment());
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
/*     */   public ClassPathScanningCandidateComponentProvider(boolean useDefaultFilters, Environment environment) {
/* 123 */     if (useDefaultFilters) {
/* 124 */       registerDefaultFilters();
/*     */     }
/* 126 */     setEnvironment(environment);
/* 127 */     setResourceLoader(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResourcePattern(String resourcePattern) {
/* 138 */     Assert.notNull(resourcePattern, "'resourcePattern' must not be null");
/* 139 */     this.resourcePattern = resourcePattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addIncludeFilter(TypeFilter includeFilter) {
/* 146 */     this.includeFilters.add(includeFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addExcludeFilter(TypeFilter excludeFilter) {
/* 153 */     this.excludeFilters.add(0, excludeFilter);
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
/*     */   public void resetFilters(boolean useDefaultFilters) {
/* 165 */     this.includeFilters.clear();
/* 166 */     this.excludeFilters.clear();
/* 167 */     if (useDefaultFilters) {
/* 168 */       registerDefaultFilters();
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
/*     */   protected void registerDefaultFilters() {
/* 184 */     this.includeFilters.add(new AnnotationTypeFilter(Component.class));
/* 185 */     ClassLoader cl = ClassPathScanningCandidateComponentProvider.class.getClassLoader();
/*     */     try {
/* 187 */       this.includeFilters.add(new AnnotationTypeFilter(
/* 188 */             ClassUtils.forName("javax.annotation.ManagedBean", cl), false));
/* 189 */       this.logger.debug("JSR-250 'javax.annotation.ManagedBean' found and supported for component scanning");
/*     */     }
/* 191 */     catch (ClassNotFoundException classNotFoundException) {}
/*     */ 
/*     */     
/*     */     try {
/* 195 */       this.includeFilters.add(new AnnotationTypeFilter(
/* 196 */             ClassUtils.forName("javax.inject.Named", cl), false));
/* 197 */       this.logger.debug("JSR-330 'javax.inject.Named' annotation found and supported for component scanning");
/*     */     }
/* 199 */     catch (ClassNotFoundException classNotFoundException) {}
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
/*     */   public void setEnvironment(Environment environment) {
/* 211 */     Assert.notNull(environment, "Environment must not be null");
/* 212 */     this.environment = environment;
/* 213 */     this.conditionEvaluator = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Environment getEnvironment() {
/* 218 */     return this.environment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanDefinitionRegistry getRegistry() {
/* 225 */     return null;
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
/*     */   public void setResourceLoader(ResourceLoader resourceLoader) {
/* 238 */     this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
/* 239 */     this.metadataReaderFactory = (MetadataReaderFactory)new CachingMetadataReaderFactory(resourceLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ResourceLoader getResourceLoader() {
/* 246 */     return (ResourceLoader)this.resourcePatternResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMetadataReaderFactory(MetadataReaderFactory metadataReaderFactory) {
/* 257 */     this.metadataReaderFactory = metadataReaderFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final MetadataReaderFactory getMetadataReaderFactory() {
/* 264 */     return this.metadataReaderFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<BeanDefinition> findCandidateComponents(String basePackage) {
/* 274 */     Set<BeanDefinition> candidates = new LinkedHashSet<BeanDefinition>();
/*     */     
/*     */     try {
/* 277 */       String packageSearchPath = "classpath*:" + resolveBasePackage(basePackage) + '/' + this.resourcePattern;
/* 278 */       Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
/* 279 */       boolean traceEnabled = this.logger.isTraceEnabled();
/* 280 */       boolean debugEnabled = this.logger.isDebugEnabled();
/* 281 */       for (Resource resource : resources) {
/* 282 */         if (traceEnabled) {
/* 283 */           this.logger.trace("Scanning " + resource);
/*     */         }
/* 285 */         if (resource.isReadable()) {
/*     */           try {
/* 287 */             MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
/* 288 */             if (isCandidateComponent(metadataReader)) {
/* 289 */               ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
/* 290 */               sbd.setResource(resource);
/* 291 */               sbd.setSource(resource);
/* 292 */               if (isCandidateComponent(sbd)) {
/* 293 */                 if (debugEnabled) {
/* 294 */                   this.logger.debug("Identified candidate component class: " + resource);
/*     */                 }
/* 296 */                 candidates.add(sbd);
/*     */               
/*     */               }
/* 299 */               else if (debugEnabled) {
/* 300 */                 this.logger.debug("Ignored because not a concrete top-level class: " + resource);
/*     */               
/*     */               }
/*     */             
/*     */             }
/* 305 */             else if (traceEnabled) {
/* 306 */               this.logger.trace("Ignored because not matching any filter: " + resource);
/*     */             }
/*     */           
/*     */           }
/* 310 */           catch (Throwable ex) {
/* 311 */             throw new BeanDefinitionStoreException("Failed to read candidate component class: " + resource, ex);
/*     */           
/*     */           }
/*     */         
/*     */         }
/* 316 */         else if (traceEnabled) {
/* 317 */           this.logger.trace("Ignored because not readable: " + resource);
/*     */         }
/*     */       
/*     */       }
/*     */     
/* 322 */     } catch (IOException ex) {
/* 323 */       throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
/*     */     } 
/* 325 */     return candidates;
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
/*     */   protected String resolveBasePackage(String basePackage) {
/* 338 */     return ClassUtils.convertClassNameToResourcePath(this.environment.resolveRequiredPlaceholders(basePackage));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
/* 348 */     for (TypeFilter tf : this.excludeFilters) {
/* 349 */       if (tf.match(metadataReader, this.metadataReaderFactory)) {
/* 350 */         return false;
/*     */       }
/*     */     } 
/* 353 */     for (TypeFilter tf : this.includeFilters) {
/* 354 */       if (tf.match(metadataReader, this.metadataReaderFactory)) {
/* 355 */         return isConditionMatch(metadataReader);
/*     */       }
/*     */     } 
/* 358 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isConditionMatch(MetadataReader metadataReader) {
/* 368 */     if (this.conditionEvaluator == null) {
/* 369 */       this.conditionEvaluator = new ConditionEvaluator(getRegistry(), getEnvironment(), getResourceLoader());
/*     */     }
/* 371 */     return !this.conditionEvaluator.shouldSkip((AnnotatedTypeMetadata)metadataReader.getAnnotationMetadata());
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
/*     */   protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
/* 383 */     AnnotationMetadata metadata = beanDefinition.getMetadata();
/* 384 */     return (metadata.isIndependent() && (metadata.isConcrete() || (metadata
/* 385 */       .isAbstract() && metadata.hasAnnotatedMethods(Lookup.class.getName()))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearCache() {
/* 393 */     if (this.metadataReaderFactory instanceof CachingMetadataReaderFactory)
/* 394 */       ((CachingMetadataReaderFactory)this.metadataReaderFactory).clearCache(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\ClassPathScanningCandidateComponentProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */