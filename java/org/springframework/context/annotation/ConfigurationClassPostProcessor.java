/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.framework.autoproxy.AutoProxyUtils;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
/*     */ import org.springframework.beans.factory.config.SingletonBeanRegistry;
/*     */ import org.springframework.beans.factory.parsing.FailFastProblemReporter;
/*     */ import org.springframework.beans.factory.parsing.PassThroughSourceExtractor;
/*     */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*     */ import org.springframework.beans.factory.parsing.SourceExtractor;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.context.EnvironmentAware;
/*     */ import org.springframework.context.ResourceLoaderAware;
/*     */ import org.springframework.core.PriorityOrdered;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.DefaultResourceLoader;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
/*     */ import org.springframework.core.type.classreading.MetadataReaderFactory;
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
/*     */ 
/*     */ public class ConfigurationClassPostProcessor
/*     */   implements BeanDefinitionRegistryPostProcessor, PriorityOrdered, ResourceLoaderAware, BeanClassLoaderAware, EnvironmentAware
/*     */ {
/*  90 */   private static final String IMPORT_REGISTRY_BEAN_NAME = ConfigurationClassPostProcessor.class
/*  91 */     .getName() + ".importRegistry";
/*     */ 
/*     */   
/*  94 */   private final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  96 */   private SourceExtractor sourceExtractor = (SourceExtractor)new PassThroughSourceExtractor();
/*     */   
/*  98 */   private ProblemReporter problemReporter = (ProblemReporter)new FailFastProblemReporter();
/*     */   
/*     */   private Environment environment;
/*     */   
/* 102 */   private ResourceLoader resourceLoader = (ResourceLoader)new DefaultResourceLoader();
/*     */   
/* 104 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */   
/* 106 */   private MetadataReaderFactory metadataReaderFactory = (MetadataReaderFactory)new CachingMetadataReaderFactory();
/*     */   
/*     */   private boolean setMetadataReaderFactoryCalled = false;
/*     */   
/* 110 */   private final Set<Integer> registriesPostProcessed = new HashSet<Integer>();
/*     */   
/* 112 */   private final Set<Integer> factoriesPostProcessed = new HashSet<Integer>();
/*     */ 
/*     */   
/*     */   private ConfigurationClassBeanDefinitionReader reader;
/*     */   
/*     */   private boolean localBeanNameGeneratorSet = false;
/*     */   
/* 119 */   private BeanNameGenerator componentScanBeanNameGenerator = new AnnotationBeanNameGenerator();
/*     */ 
/*     */   
/* 122 */   private BeanNameGenerator importBeanNameGenerator = new AnnotationBeanNameGenerator()
/*     */     {
/*     */       protected String buildDefaultBeanName(BeanDefinition definition) {
/* 125 */         return definition.getBeanClassName();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 132 */     return Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSourceExtractor(SourceExtractor sourceExtractor) {
/* 140 */     this.sourceExtractor = (sourceExtractor != null) ? sourceExtractor : (SourceExtractor)new PassThroughSourceExtractor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProblemReporter(ProblemReporter problemReporter) {
/* 150 */     this.problemReporter = (problemReporter != null) ? problemReporter : (ProblemReporter)new FailFastProblemReporter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMetadataReaderFactory(MetadataReaderFactory metadataReaderFactory) {
/* 159 */     Assert.notNull(metadataReaderFactory, "MetadataReaderFactory must not be null");
/* 160 */     this.metadataReaderFactory = metadataReaderFactory;
/* 161 */     this.setMetadataReaderFactoryCalled = true;
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
/*     */   public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
/* 182 */     Assert.notNull(beanNameGenerator, "BeanNameGenerator must not be null");
/* 183 */     this.localBeanNameGeneratorSet = true;
/* 184 */     this.componentScanBeanNameGenerator = beanNameGenerator;
/* 185 */     this.importBeanNameGenerator = beanNameGenerator;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEnvironment(Environment environment) {
/* 190 */     Assert.notNull(environment, "Environment must not be null");
/* 191 */     this.environment = environment;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setResourceLoader(ResourceLoader resourceLoader) {
/* 196 */     Assert.notNull(resourceLoader, "ResourceLoader must not be null");
/* 197 */     this.resourceLoader = resourceLoader;
/* 198 */     if (!this.setMetadataReaderFactoryCalled) {
/* 199 */       this.metadataReaderFactory = (MetadataReaderFactory)new CachingMetadataReaderFactory(resourceLoader);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader beanClassLoader) {
/* 205 */     this.beanClassLoader = beanClassLoader;
/* 206 */     if (!this.setMetadataReaderFactoryCalled) {
/* 207 */       this.metadataReaderFactory = (MetadataReaderFactory)new CachingMetadataReaderFactory(beanClassLoader);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
/* 217 */     int registryId = System.identityHashCode(registry);
/* 218 */     if (this.registriesPostProcessed.contains(Integer.valueOf(registryId))) {
/* 219 */       throw new IllegalStateException("postProcessBeanDefinitionRegistry already called on this post-processor against " + registry);
/*     */     }
/*     */     
/* 222 */     if (this.factoriesPostProcessed.contains(Integer.valueOf(registryId))) {
/* 223 */       throw new IllegalStateException("postProcessBeanFactory already called on this post-processor against " + registry);
/*     */     }
/*     */     
/* 226 */     this.registriesPostProcessed.add(Integer.valueOf(registryId));
/*     */     
/* 228 */     processConfigBeanDefinitions(registry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
/* 237 */     int factoryId = System.identityHashCode(beanFactory);
/* 238 */     if (this.factoriesPostProcessed.contains(Integer.valueOf(factoryId))) {
/* 239 */       throw new IllegalStateException("postProcessBeanFactory already called on this post-processor against " + beanFactory);
/*     */     }
/*     */     
/* 242 */     this.factoriesPostProcessed.add(Integer.valueOf(factoryId));
/* 243 */     if (!this.registriesPostProcessed.contains(Integer.valueOf(factoryId)))
/*     */     {
/*     */       
/* 246 */       processConfigBeanDefinitions((BeanDefinitionRegistry)beanFactory);
/*     */     }
/*     */     
/* 249 */     enhanceConfigurationClasses(beanFactory);
/* 250 */     beanFactory.addBeanPostProcessor((BeanPostProcessor)new ImportAwareBeanPostProcessor((BeanFactory)beanFactory));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processConfigBeanDefinitions(BeanDefinitionRegistry registry) {
/* 258 */     List<BeanDefinitionHolder> configCandidates = new ArrayList<BeanDefinitionHolder>();
/* 259 */     String[] candidateNames = registry.getBeanDefinitionNames();
/*     */     
/* 261 */     for (String beanName : candidateNames) {
/* 262 */       BeanDefinition beanDef = registry.getBeanDefinition(beanName);
/* 263 */       if (ConfigurationClassUtils.isFullConfigurationClass(beanDef) || 
/* 264 */         ConfigurationClassUtils.isLiteConfigurationClass(beanDef)) {
/* 265 */         if (this.logger.isDebugEnabled()) {
/* 266 */           this.logger.debug("Bean definition has already been processed as a configuration class: " + beanDef);
/*     */         }
/*     */       }
/* 269 */       else if (ConfigurationClassUtils.checkConfigurationClassCandidate(beanDef, this.metadataReaderFactory)) {
/* 270 */         configCandidates.add(new BeanDefinitionHolder(beanDef, beanName));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 275 */     if (configCandidates.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 280 */     Collections.sort(configCandidates, new Comparator<BeanDefinitionHolder>()
/*     */         {
/*     */           public int compare(BeanDefinitionHolder bd1, BeanDefinitionHolder bd2) {
/* 283 */             int i1 = ConfigurationClassUtils.getOrder(bd1.getBeanDefinition());
/* 284 */             int i2 = ConfigurationClassUtils.getOrder(bd2.getBeanDefinition());
/* 285 */             return (i1 < i2) ? -1 : ((i1 > i2) ? 1 : 0);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 290 */     SingletonBeanRegistry sbr = null;
/* 291 */     if (registry instanceof SingletonBeanRegistry) {
/* 292 */       sbr = (SingletonBeanRegistry)registry;
/* 293 */       if (!this.localBeanNameGeneratorSet && sbr.containsSingleton("org.springframework.context.annotation.internalConfigurationBeanNameGenerator")) {
/* 294 */         BeanNameGenerator generator = (BeanNameGenerator)sbr.getSingleton("org.springframework.context.annotation.internalConfigurationBeanNameGenerator");
/* 295 */         this.componentScanBeanNameGenerator = generator;
/* 296 */         this.importBeanNameGenerator = generator;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 301 */     ConfigurationClassParser parser = new ConfigurationClassParser(this.metadataReaderFactory, this.problemReporter, this.environment, this.resourceLoader, this.componentScanBeanNameGenerator, registry);
/*     */ 
/*     */ 
/*     */     
/* 305 */     Set<BeanDefinitionHolder> candidates = new LinkedHashSet<BeanDefinitionHolder>(configCandidates);
/* 306 */     Set<ConfigurationClass> alreadyParsed = new HashSet<ConfigurationClass>(configCandidates.size());
/*     */     do {
/* 308 */       parser.parse(candidates);
/* 309 */       parser.validate();
/*     */       
/* 311 */       Set<ConfigurationClass> configClasses = new LinkedHashSet<ConfigurationClass>(parser.getConfigurationClasses());
/* 312 */       configClasses.removeAll(alreadyParsed);
/*     */ 
/*     */       
/* 315 */       if (this.reader == null) {
/* 316 */         this
/*     */           
/* 318 */           .reader = new ConfigurationClassBeanDefinitionReader(registry, this.sourceExtractor, this.resourceLoader, this.environment, this.importBeanNameGenerator, parser.getImportRegistry());
/*     */       }
/* 320 */       this.reader.loadBeanDefinitions(configClasses);
/* 321 */       alreadyParsed.addAll(configClasses);
/*     */       
/* 323 */       candidates.clear();
/* 324 */       if (registry.getBeanDefinitionCount() <= candidateNames.length)
/* 325 */         continue;  String[] newCandidateNames = registry.getBeanDefinitionNames();
/* 326 */       Set<String> oldCandidateNames = new HashSet<String>(Arrays.asList(candidateNames));
/* 327 */       Set<String> alreadyParsedClasses = new HashSet<String>();
/* 328 */       for (ConfigurationClass configurationClass : alreadyParsed) {
/* 329 */         alreadyParsedClasses.add(configurationClass.getMetadata().getClassName());
/*     */       }
/* 331 */       for (String candidateName : newCandidateNames) {
/* 332 */         if (!oldCandidateNames.contains(candidateName)) {
/* 333 */           BeanDefinition bd = registry.getBeanDefinition(candidateName);
/* 334 */           if (ConfigurationClassUtils.checkConfigurationClassCandidate(bd, this.metadataReaderFactory) && 
/* 335 */             !alreadyParsedClasses.contains(bd.getBeanClassName())) {
/* 336 */             candidates.add(new BeanDefinitionHolder(bd, candidateName));
/*     */           }
/*     */         } 
/*     */       } 
/* 340 */       candidateNames = newCandidateNames;
/*     */     
/*     */     }
/* 343 */     while (!candidates.isEmpty());
/*     */ 
/*     */     
/* 346 */     if (sbr != null && 
/* 347 */       !sbr.containsSingleton(IMPORT_REGISTRY_BEAN_NAME)) {
/* 348 */       sbr.registerSingleton(IMPORT_REGISTRY_BEAN_NAME, parser.getImportRegistry());
/*     */     }
/*     */ 
/*     */     
/* 352 */     if (this.metadataReaderFactory instanceof CachingMetadataReaderFactory) {
/* 353 */       ((CachingMetadataReaderFactory)this.metadataReaderFactory).clearCache();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enhanceConfigurationClasses(ConfigurableListableBeanFactory beanFactory) {
/* 364 */     Map<String, AbstractBeanDefinition> configBeanDefs = new LinkedHashMap<String, AbstractBeanDefinition>();
/* 365 */     for (String beanName : beanFactory.getBeanDefinitionNames()) {
/* 366 */       BeanDefinition beanDef = beanFactory.getBeanDefinition(beanName);
/* 367 */       if (ConfigurationClassUtils.isFullConfigurationClass(beanDef)) {
/* 368 */         if (!(beanDef instanceof AbstractBeanDefinition)) {
/* 369 */           throw new BeanDefinitionStoreException("Cannot enhance @Configuration bean definition '" + beanName + "' since it is not stored in an AbstractBeanDefinition subclass");
/*     */         }
/*     */         
/* 372 */         if (this.logger.isWarnEnabled() && beanFactory.containsSingleton(beanName)) {
/* 373 */           this.logger.warn("Cannot enhance @Configuration bean definition '" + beanName + "' since its singleton instance has been created too early. The typical cause is a non-static @Bean method with a BeanDefinitionRegistryPostProcessor return type: Consider declaring such methods as 'static'.");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 378 */         configBeanDefs.put(beanName, (AbstractBeanDefinition)beanDef);
/*     */       } 
/*     */     } 
/* 381 */     if (configBeanDefs.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 386 */     ConfigurationClassEnhancer enhancer = new ConfigurationClassEnhancer();
/* 387 */     for (Map.Entry<String, AbstractBeanDefinition> entry : configBeanDefs.entrySet()) {
/* 388 */       AbstractBeanDefinition beanDef = entry.getValue();
/*     */       
/* 390 */       beanDef.setAttribute(AutoProxyUtils.PRESERVE_TARGET_CLASS_ATTRIBUTE, Boolean.TRUE);
/*     */       
/*     */       try {
/* 393 */         Class<?> configClass = beanDef.resolveBeanClass(this.beanClassLoader);
/* 394 */         Class<?> enhancedClass = enhancer.enhance(configClass, this.beanClassLoader);
/* 395 */         if (configClass != enhancedClass) {
/* 396 */           if (this.logger.isDebugEnabled()) {
/* 397 */             this.logger.debug(String.format("Replacing bean definition '%s' existing class '%s' with enhanced class '%s'", new Object[] { entry
/* 398 */                     .getKey(), configClass.getName(), enhancedClass.getName() }));
/*     */           }
/* 400 */           beanDef.setBeanClass(enhancedClass);
/*     */         }
/*     */       
/* 403 */       } catch (Throwable ex) {
/* 404 */         throw new IllegalStateException("Cannot load configuration class: " + beanDef.getBeanClassName(), ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class ImportAwareBeanPostProcessor
/*     */     extends InstantiationAwareBeanPostProcessorAdapter
/*     */   {
/*     */     private final BeanFactory beanFactory;
/*     */     
/*     */     public ImportAwareBeanPostProcessor(BeanFactory beanFactory) {
/* 415 */       this.beanFactory = beanFactory;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) {
/* 424 */       if (bean instanceof ConfigurationClassEnhancer.EnhancedConfiguration) {
/* 425 */         ((ConfigurationClassEnhancer.EnhancedConfiguration)bean).setBeanFactory(this.beanFactory);
/*     */       }
/* 427 */       return pvs;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object postProcessBeforeInitialization(Object bean, String beanName) {
/* 432 */       if (bean instanceof ImportAware) {
/* 433 */         ImportRegistry ir = (ImportRegistry)this.beanFactory.getBean(ConfigurationClassPostProcessor.IMPORT_REGISTRY_BEAN_NAME, ImportRegistry.class);
/* 434 */         AnnotationMetadata importingClass = ir.getImportingClassFor(bean.getClass().getSuperclass().getName());
/* 435 */         if (importingClass != null) {
/* 436 */           ((ImportAware)bean).setImportMetadata(importingClass);
/*     */         }
/*     */       } 
/* 439 */       return bean;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\ConfigurationClassPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */