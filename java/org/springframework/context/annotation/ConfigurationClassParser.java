/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Deque;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.parsing.Location;
/*     */ import org.springframework.beans.factory.parsing.Problem;
/*     */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionReader;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.core.NestedIOException;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.env.CompositePropertySource;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.MutablePropertySources;
/*     */ import org.springframework.core.env.PropertySource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.DefaultPropertySourceFactory;
/*     */ import org.springframework.core.io.support.EncodedResource;
/*     */ import org.springframework.core.io.support.PropertySourceFactory;
/*     */ import org.springframework.core.io.support.ResourcePropertySource;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.MethodMetadata;
/*     */ import org.springframework.core.type.StandardAnnotationMetadata;
/*     */ import org.springframework.core.type.classreading.MetadataReader;
/*     */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*     */ import org.springframework.core.type.filter.AssignableTypeFilter;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ class ConfigurationClassParser
/*     */ {
/* 104 */   private static final PropertySourceFactory DEFAULT_PROPERTY_SOURCE_FACTORY = (PropertySourceFactory)new DefaultPropertySourceFactory();
/*     */   
/* 106 */   private static final Comparator<DeferredImportSelectorHolder> DEFERRED_IMPORT_COMPARATOR = new Comparator<DeferredImportSelectorHolder>()
/*     */     {
/*     */       public int compare(ConfigurationClassParser.DeferredImportSelectorHolder o1, ConfigurationClassParser.DeferredImportSelectorHolder o2)
/*     */       {
/* 110 */         return AnnotationAwareOrderComparator.INSTANCE.compare(o1.getImportSelector(), o2.getImportSelector());
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 115 */   private final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private final MetadataReaderFactory metadataReaderFactory;
/*     */   
/*     */   private final ProblemReporter problemReporter;
/*     */   
/*     */   private final Environment environment;
/*     */   
/*     */   private final ResourceLoader resourceLoader;
/*     */   
/*     */   private final BeanDefinitionRegistry registry;
/*     */   
/*     */   private final ComponentScanAnnotationParser componentScanParser;
/*     */   
/*     */   private final ConditionEvaluator conditionEvaluator;
/*     */   
/* 131 */   private final Map<ConfigurationClass, ConfigurationClass> configurationClasses = new LinkedHashMap<ConfigurationClass, ConfigurationClass>();
/*     */ 
/*     */   
/* 134 */   private final Map<String, ConfigurationClass> knownSuperclasses = new HashMap<String, ConfigurationClass>();
/*     */   
/* 136 */   private final List<String> propertySourceNames = new ArrayList<String>();
/*     */   
/* 138 */   private final ImportStack importStack = new ImportStack();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<DeferredImportSelectorHolder> deferredImportSelectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationClassParser(MetadataReaderFactory metadataReaderFactory, ProblemReporter problemReporter, Environment environment, ResourceLoader resourceLoader, BeanNameGenerator componentScanBeanNameGenerator, BeanDefinitionRegistry registry) {
/* 151 */     this.metadataReaderFactory = metadataReaderFactory;
/* 152 */     this.problemReporter = problemReporter;
/* 153 */     this.environment = environment;
/* 154 */     this.resourceLoader = resourceLoader;
/* 155 */     this.registry = registry;
/* 156 */     this.componentScanParser = new ComponentScanAnnotationParser(environment, resourceLoader, componentScanBeanNameGenerator, registry);
/*     */     
/* 158 */     this.conditionEvaluator = new ConditionEvaluator(registry, environment, resourceLoader);
/*     */   }
/*     */ 
/*     */   
/*     */   public void parse(Set<BeanDefinitionHolder> configCandidates) {
/* 163 */     this.deferredImportSelectors = new LinkedList<DeferredImportSelectorHolder>();
/*     */     
/* 165 */     for (BeanDefinitionHolder holder : configCandidates) {
/* 166 */       BeanDefinition bd = holder.getBeanDefinition();
/*     */       try {
/* 168 */         if (bd instanceof AnnotatedBeanDefinition) {
/* 169 */           parse(((AnnotatedBeanDefinition)bd).getMetadata(), holder.getBeanName()); continue;
/*     */         } 
/* 171 */         if (bd instanceof AbstractBeanDefinition && ((AbstractBeanDefinition)bd).hasBeanClass()) {
/* 172 */           parse(((AbstractBeanDefinition)bd).getBeanClass(), holder.getBeanName());
/*     */           continue;
/*     */         } 
/* 175 */         parse(bd.getBeanClassName(), holder.getBeanName());
/*     */       
/*     */       }
/* 178 */       catch (BeanDefinitionStoreException ex) {
/* 179 */         throw ex;
/*     */       }
/* 181 */       catch (Throwable ex) {
/* 182 */         throw new BeanDefinitionStoreException("Failed to parse configuration class [" + bd
/* 183 */             .getBeanClassName() + "]", ex);
/*     */       } 
/*     */     } 
/*     */     
/* 187 */     processDeferredImportSelectors();
/*     */   }
/*     */   
/*     */   protected final void parse(String className, String beanName) throws IOException {
/* 191 */     MetadataReader reader = this.metadataReaderFactory.getMetadataReader(className);
/* 192 */     processConfigurationClass(new ConfigurationClass(reader, beanName));
/*     */   }
/*     */   
/*     */   protected final void parse(Class<?> clazz, String beanName) throws IOException {
/* 196 */     processConfigurationClass(new ConfigurationClass(clazz, beanName));
/*     */   }
/*     */   
/*     */   protected final void parse(AnnotationMetadata metadata, String beanName) throws IOException {
/* 200 */     processConfigurationClass(new ConfigurationClass(metadata, beanName));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validate() {
/* 208 */     for (ConfigurationClass configClass : this.configurationClasses.keySet()) {
/* 209 */       configClass.validate(this.problemReporter);
/*     */     }
/*     */   }
/*     */   
/*     */   public Set<ConfigurationClass> getConfigurationClasses() {
/* 214 */     return this.configurationClasses.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void processConfigurationClass(ConfigurationClass configClass) throws IOException {
/* 219 */     if (this.conditionEvaluator.shouldSkip((AnnotatedTypeMetadata)configClass.getMetadata(), ConfigurationCondition.ConfigurationPhase.PARSE_CONFIGURATION)) {
/*     */       return;
/*     */     }
/*     */     
/* 223 */     ConfigurationClass existingClass = this.configurationClasses.get(configClass);
/* 224 */     if (existingClass != null) {
/* 225 */       if (configClass.isImported()) {
/* 226 */         if (existingClass.isImported()) {
/* 227 */           existingClass.mergeImportedBy(configClass);
/*     */         }
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */ 
/*     */       
/* 235 */       this.configurationClasses.remove(configClass);
/* 236 */       for (Iterator<ConfigurationClass> it = this.knownSuperclasses.values().iterator(); it.hasNext();) {
/* 237 */         if (configClass.equals(it.next())) {
/* 238 */           it.remove();
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 245 */     SourceClass sourceClass = asSourceClass(configClass);
/*     */     do {
/* 247 */       sourceClass = doProcessConfigurationClass(configClass, sourceClass);
/*     */     }
/* 249 */     while (sourceClass != null);
/*     */     
/* 251 */     this.configurationClasses.put(configClass, configClass);
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
/*     */   protected final SourceClass doProcessConfigurationClass(ConfigurationClass configClass, SourceClass sourceClass) throws IOException {
/* 266 */     processMemberClasses(configClass, sourceClass);
/*     */ 
/*     */     
/* 269 */     for (AnnotationAttributes propertySource : AnnotationConfigUtils.attributesForRepeatable(sourceClass
/* 270 */         .getMetadata(), PropertySources.class, PropertySource.class)) {
/*     */       
/* 272 */       if (this.environment instanceof ConfigurableEnvironment) {
/* 273 */         processPropertySource(propertySource);
/*     */         continue;
/*     */       } 
/* 276 */       this.logger.warn("Ignoring @PropertySource annotation on [" + sourceClass.getMetadata().getClassName() + "]. Reason: Environment must implement ConfigurableEnvironment");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 282 */     Set<AnnotationAttributes> componentScans = AnnotationConfigUtils.attributesForRepeatable(sourceClass
/* 283 */         .getMetadata(), ComponentScans.class, ComponentScan.class);
/* 284 */     if (!componentScans.isEmpty() && 
/* 285 */       !this.conditionEvaluator.shouldSkip((AnnotatedTypeMetadata)sourceClass.getMetadata(), ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN)) {
/* 286 */       for (AnnotationAttributes componentScan : componentScans) {
/*     */ 
/*     */         
/* 289 */         Set<BeanDefinitionHolder> scannedBeanDefinitions = this.componentScanParser.parse(componentScan, sourceClass.getMetadata().getClassName());
/*     */         
/* 291 */         for (BeanDefinitionHolder holder : scannedBeanDefinitions) {
/* 292 */           BeanDefinition bdCand = holder.getBeanDefinition().getOriginatingBeanDefinition();
/* 293 */           if (bdCand == null) {
/* 294 */             bdCand = holder.getBeanDefinition();
/*     */           }
/* 296 */           if (ConfigurationClassUtils.checkConfigurationClassCandidate(bdCand, this.metadataReaderFactory)) {
/* 297 */             parse(bdCand.getBeanClassName(), holder.getBeanName());
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 304 */     processImports(configClass, sourceClass, getImports(sourceClass), true);
/*     */ 
/*     */     
/* 307 */     if (sourceClass.getMetadata().isAnnotated(ImportResource.class.getName())) {
/*     */       
/* 309 */       AnnotationAttributes importResource = AnnotationConfigUtils.attributesFor((AnnotatedTypeMetadata)sourceClass.getMetadata(), ImportResource.class);
/* 310 */       String[] resources = importResource.getStringArray("locations");
/* 311 */       Class<? extends BeanDefinitionReader> readerClass = importResource.getClass("reader");
/* 312 */       for (String resource : resources) {
/* 313 */         String resolvedResource = this.environment.resolveRequiredPlaceholders(resource);
/* 314 */         configClass.addImportedResource(resolvedResource, readerClass);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 319 */     Set<MethodMetadata> beanMethods = retrieveBeanMethodMetadata(sourceClass);
/* 320 */     for (MethodMetadata methodMetadata : beanMethods) {
/* 321 */       configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
/*     */     }
/*     */ 
/*     */     
/* 325 */     processInterfaces(configClass, sourceClass);
/*     */ 
/*     */     
/* 328 */     if (sourceClass.getMetadata().hasSuperClass()) {
/* 329 */       String superclass = sourceClass.getMetadata().getSuperClassName();
/* 330 */       if (!superclass.startsWith("java") && !this.knownSuperclasses.containsKey(superclass)) {
/* 331 */         this.knownSuperclasses.put(superclass, configClass);
/*     */         
/* 333 */         return sourceClass.getSuperClass();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 338 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processMemberClasses(ConfigurationClass configClass, SourceClass sourceClass) throws IOException {
/* 345 */     for (SourceClass memberClass : sourceClass.getMemberClasses()) {
/* 346 */       if (ConfigurationClassUtils.isConfigurationCandidate(memberClass.getMetadata()) && 
/* 347 */         !memberClass.getMetadata().getClassName().equals(configClass.getMetadata().getClassName())) {
/* 348 */         if (this.importStack.contains(configClass)) {
/* 349 */           this.problemReporter.error(new CircularImportProblem(configClass, this.importStack));
/*     */           continue;
/*     */         } 
/* 352 */         this.importStack.push(configClass);
/*     */         try {
/* 354 */           processConfigurationClass(memberClass.asConfigClass(configClass));
/*     */         } finally {
/*     */           
/* 357 */           this.importStack.pop();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processInterfaces(ConfigurationClass configClass, SourceClass sourceClass) throws IOException {
/* 368 */     for (SourceClass ifc : sourceClass.getInterfaces()) {
/* 369 */       Set<MethodMetadata> beanMethods = retrieveBeanMethodMetadata(ifc);
/* 370 */       for (MethodMetadata methodMetadata : beanMethods) {
/* 371 */         if (!methodMetadata.isAbstract())
/*     */         {
/* 373 */           configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
/*     */         }
/*     */       } 
/* 376 */       processInterfaces(configClass, ifc);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Set<MethodMetadata> retrieveBeanMethodMetadata(SourceClass sourceClass) {
/* 384 */     AnnotationMetadata original = sourceClass.getMetadata();
/* 385 */     Set<MethodMetadata> beanMethods = original.getAnnotatedMethods(Bean.class.getName());
/* 386 */     if (beanMethods.size() > 1 && original instanceof StandardAnnotationMetadata) {
/*     */       
/*     */       try {
/*     */ 
/*     */ 
/*     */         
/* 392 */         AnnotationMetadata asm = this.metadataReaderFactory.getMetadataReader(original.getClassName()).getAnnotationMetadata();
/* 393 */         Set<MethodMetadata> asmMethods = asm.getAnnotatedMethods(Bean.class.getName());
/* 394 */         if (asmMethods.size() >= beanMethods.size()) {
/* 395 */           Set<MethodMetadata> selectedMethods = new LinkedHashSet<MethodMetadata>(asmMethods.size());
/* 396 */           for (MethodMetadata asmMethod : asmMethods) {
/* 397 */             for (MethodMetadata beanMethod : beanMethods) {
/* 398 */               if (beanMethod.getMethodName().equals(asmMethod.getMethodName())) {
/* 399 */                 selectedMethods.add(beanMethod);
/*     */               }
/*     */             } 
/*     */           } 
/*     */           
/* 404 */           if (selectedMethods.size() == beanMethods.size())
/*     */           {
/* 406 */             beanMethods = selectedMethods;
/*     */           }
/*     */         }
/*     */       
/* 410 */       } catch (IOException ex) {
/* 411 */         this.logger.debug("Failed to read class file via ASM for determining @Bean method order", ex);
/*     */       } 
/*     */     }
/*     */     
/* 415 */     return beanMethods;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processPropertySource(AnnotationAttributes propertySource) throws IOException {
/* 425 */     String name = propertySource.getString("name");
/* 426 */     if (!StringUtils.hasLength(name)) {
/* 427 */       name = null;
/*     */     }
/* 429 */     String encoding = propertySource.getString("encoding");
/* 430 */     if (!StringUtils.hasLength(encoding)) {
/* 431 */       encoding = null;
/*     */     }
/* 433 */     String[] locations = propertySource.getStringArray("value");
/* 434 */     Assert.isTrue((locations.length > 0), "At least one @PropertySource(value) location is required");
/* 435 */     boolean ignoreResourceNotFound = propertySource.getBoolean("ignoreResourceNotFound");
/*     */     
/* 437 */     Class<? extends PropertySourceFactory> factoryClass = propertySource.getClass("factory");
/*     */     
/* 439 */     PropertySourceFactory factory = (factoryClass == PropertySourceFactory.class) ? DEFAULT_PROPERTY_SOURCE_FACTORY : (PropertySourceFactory)BeanUtils.instantiateClass(factoryClass);
/*     */     
/* 441 */     for (String location : locations) {
/*     */       try {
/* 443 */         String resolvedLocation = this.environment.resolveRequiredPlaceholders(location);
/* 444 */         Resource resource = this.resourceLoader.getResource(resolvedLocation);
/* 445 */         addPropertySource(factory.createPropertySource(name, new EncodedResource(resource, encoding)));
/*     */       }
/* 447 */       catch (IllegalArgumentException ex) {
/*     */         
/* 449 */         if (ignoreResourceNotFound) {
/* 450 */           if (this.logger.isInfoEnabled()) {
/* 451 */             this.logger.info("Properties location [" + location + "] not resolvable: " + ex.getMessage());
/*     */           }
/*     */         } else {
/*     */           
/* 455 */           throw ex;
/*     */         }
/*     */       
/* 458 */       } catch (IOException ex) {
/*     */         
/* 460 */         if (ignoreResourceNotFound && (ex instanceof java.io.FileNotFoundException || ex instanceof java.net.UnknownHostException)) {
/*     */           
/* 462 */           if (this.logger.isInfoEnabled()) {
/* 463 */             this.logger.info("Properties location [" + location + "] not resolvable: " + ex.getMessage());
/*     */           }
/*     */         } else {
/*     */           
/* 467 */           throw ex;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addPropertySource(PropertySource<?> propertySource) {
/* 474 */     String name = propertySource.getName();
/* 475 */     MutablePropertySources propertySources = ((ConfigurableEnvironment)this.environment).getPropertySources();
/* 476 */     if (propertySources.contains(name) && this.propertySourceNames.contains(name)) {
/*     */       
/* 478 */       PropertySource<?> existing = propertySources.get(name);
/*     */       
/* 480 */       PropertySource<?> newSource = (propertySource instanceof ResourcePropertySource) ? (PropertySource<?>)((ResourcePropertySource)propertySource).withResourceName() : propertySource;
/* 481 */       if (existing instanceof CompositePropertySource) {
/* 482 */         ((CompositePropertySource)existing).addFirstPropertySource(newSource);
/*     */       } else {
/*     */         ResourcePropertySource resourcePropertySource;
/* 485 */         if (existing instanceof ResourcePropertySource) {
/* 486 */           resourcePropertySource = ((ResourcePropertySource)existing).withResourceName();
/*     */         }
/* 488 */         CompositePropertySource composite = new CompositePropertySource(name);
/* 489 */         composite.addPropertySource(newSource);
/* 490 */         composite.addPropertySource((PropertySource)resourcePropertySource);
/* 491 */         propertySources.replace(name, (PropertySource)composite);
/*     */       }
/*     */     
/*     */     }
/* 495 */     else if (this.propertySourceNames.isEmpty()) {
/* 496 */       propertySources.addLast(propertySource);
/*     */     } else {
/*     */       
/* 499 */       String firstProcessed = this.propertySourceNames.get(this.propertySourceNames.size() - 1);
/* 500 */       propertySources.addBefore(firstProcessed, propertySource);
/*     */     } 
/*     */     
/* 503 */     this.propertySourceNames.add(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Set<SourceClass> getImports(SourceClass sourceClass) throws IOException {
/* 511 */     Set<SourceClass> imports = new LinkedHashSet<SourceClass>();
/* 512 */     Set<SourceClass> visited = new LinkedHashSet<SourceClass>();
/* 513 */     collectImports(sourceClass, imports, visited);
/* 514 */     return imports;
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
/*     */   private void collectImports(SourceClass sourceClass, Set<SourceClass> imports, Set<SourceClass> visited) throws IOException {
/* 533 */     if (visited.add(sourceClass)) {
/* 534 */       for (SourceClass annotation : sourceClass.getAnnotations()) {
/* 535 */         String annName = annotation.getMetadata().getClassName();
/* 536 */         if (!annName.startsWith("java") && !annName.equals(Import.class.getName())) {
/* 537 */           collectImports(annotation, imports, visited);
/*     */         }
/*     */       } 
/* 540 */       imports.addAll(sourceClass.getAnnotationAttributes(Import.class.getName(), "value"));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processDeferredImportSelectors() {
/* 545 */     List<DeferredImportSelectorHolder> deferredImports = this.deferredImportSelectors;
/* 546 */     this.deferredImportSelectors = null;
/* 547 */     Collections.sort(deferredImports, DEFERRED_IMPORT_COMPARATOR);
/*     */     
/* 549 */     for (DeferredImportSelectorHolder deferredImport : deferredImports) {
/* 550 */       ConfigurationClass configClass = deferredImport.getConfigurationClass();
/*     */       try {
/* 552 */         String[] imports = deferredImport.getImportSelector().selectImports(configClass.getMetadata());
/* 553 */         processImports(configClass, asSourceClass(configClass), asSourceClasses(imports), false);
/*     */       }
/* 555 */       catch (BeanDefinitionStoreException ex) {
/* 556 */         throw ex;
/*     */       }
/* 558 */       catch (Throwable ex) {
/* 559 */         throw new BeanDefinitionStoreException("Failed to process import candidates for configuration class [" + configClass
/*     */             
/* 561 */             .getMetadata().getClassName() + "]", ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void processImports(ConfigurationClass configClass, SourceClass currentSourceClass, Collection<SourceClass> importCandidates, boolean checkForCircularImports) {
/* 569 */     if (importCandidates.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 573 */     if (checkForCircularImports && isChainedImportOnStack(configClass)) {
/* 574 */       this.problemReporter.error(new CircularImportProblem(configClass, this.importStack));
/*     */     } else {
/*     */       
/* 577 */       this.importStack.push(configClass);
/*     */       try {
/* 579 */         for (SourceClass candidate : importCandidates) {
/* 580 */           if (candidate.isAssignable(ImportSelector.class)) {
/*     */             
/* 582 */             Class<?> candidateClass = candidate.loadClass();
/* 583 */             ImportSelector selector = (ImportSelector)BeanUtils.instantiateClass(candidateClass, ImportSelector.class);
/* 584 */             ParserStrategyUtils.invokeAwareMethods(selector, this.environment, this.resourceLoader, this.registry);
/*     */             
/* 586 */             if (this.deferredImportSelectors != null && selector instanceof DeferredImportSelector) {
/* 587 */               this.deferredImportSelectors.add(new DeferredImportSelectorHolder(configClass, (DeferredImportSelector)selector));
/*     */               
/*     */               continue;
/*     */             } 
/* 591 */             String[] importClassNames = selector.selectImports(currentSourceClass.getMetadata());
/* 592 */             Collection<SourceClass> importSourceClasses = asSourceClasses(importClassNames);
/* 593 */             processImports(configClass, currentSourceClass, importSourceClasses, false);
/*     */             continue;
/*     */           } 
/* 596 */           if (candidate.isAssignable(ImportBeanDefinitionRegistrar.class)) {
/*     */ 
/*     */             
/* 599 */             Class<?> candidateClass = candidate.loadClass();
/*     */             
/* 601 */             ImportBeanDefinitionRegistrar registrar = (ImportBeanDefinitionRegistrar)BeanUtils.instantiateClass(candidateClass, ImportBeanDefinitionRegistrar.class);
/* 602 */             ParserStrategyUtils.invokeAwareMethods(registrar, this.environment, this.resourceLoader, this.registry);
/*     */             
/* 604 */             configClass.addImportBeanDefinitionRegistrar(registrar, currentSourceClass.getMetadata());
/*     */             
/*     */             continue;
/*     */           } 
/*     */           
/* 609 */           this.importStack.registerImport(currentSourceClass
/* 610 */               .getMetadata(), candidate.getMetadata().getClassName());
/* 611 */           processConfigurationClass(candidate.asConfigClass(configClass));
/*     */         }
/*     */       
/*     */       }
/* 615 */       catch (BeanDefinitionStoreException ex) {
/* 616 */         throw ex;
/*     */       }
/* 618 */       catch (Throwable ex) {
/* 619 */         throw new BeanDefinitionStoreException("Failed to process import candidates for configuration class [" + configClass
/*     */             
/* 621 */             .getMetadata().getClassName() + "]", ex);
/*     */       } finally {
/*     */         
/* 624 */         this.importStack.pop();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isChainedImportOnStack(ConfigurationClass configClass) {
/* 630 */     if (this.importStack.contains(configClass)) {
/* 631 */       String configClassName = configClass.getMetadata().getClassName();
/* 632 */       AnnotationMetadata importingClass = this.importStack.getImportingClassFor(configClassName);
/* 633 */       while (importingClass != null) {
/* 634 */         if (configClassName.equals(importingClass.getClassName())) {
/* 635 */           return true;
/*     */         }
/* 637 */         importingClass = this.importStack.getImportingClassFor(importingClass.getClassName());
/*     */       } 
/*     */     } 
/* 640 */     return false;
/*     */   }
/*     */   
/*     */   ImportRegistry getImportRegistry() {
/* 644 */     return this.importStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SourceClass asSourceClass(ConfigurationClass configurationClass) throws IOException {
/* 652 */     AnnotationMetadata metadata = configurationClass.getMetadata();
/* 653 */     if (metadata instanceof StandardAnnotationMetadata) {
/* 654 */       return asSourceClass(((StandardAnnotationMetadata)metadata).getIntrospectedClass());
/*     */     }
/* 656 */     return asSourceClass(metadata.getClassName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SourceClass asSourceClass(Class<?> classType) throws IOException {
/*     */     try {
/* 666 */       for (Annotation ann : classType.getAnnotations()) {
/* 667 */         AnnotationUtils.validateAnnotation(ann);
/*     */       }
/* 669 */       return new SourceClass(classType);
/*     */     }
/* 671 */     catch (Throwable ex) {
/*     */       
/* 673 */       return asSourceClass(classType.getName());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Collection<SourceClass> asSourceClasses(String[] classNames) throws IOException {
/* 681 */     List<SourceClass> annotatedClasses = new ArrayList<SourceClass>(classNames.length);
/* 682 */     for (String className : classNames) {
/* 683 */       annotatedClasses.add(asSourceClass(className));
/*     */     }
/* 685 */     return annotatedClasses;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SourceClass asSourceClass(String className) throws IOException {
/* 692 */     if (className.startsWith("java")) {
/*     */       
/*     */       try {
/* 695 */         return new SourceClass(this.resourceLoader.getClassLoader().loadClass(className));
/*     */       }
/* 697 */       catch (ClassNotFoundException ex) {
/* 698 */         throw new NestedIOException("Failed to load class [" + className + "]", ex);
/*     */       } 
/*     */     }
/* 701 */     return new SourceClass(this.metadataReaderFactory.getMetadataReader(className));
/*     */   }
/*     */   
/*     */   private static class ImportStack
/*     */     extends ArrayDeque<ConfigurationClass>
/*     */     implements ImportRegistry
/*     */   {
/* 708 */     private final MultiValueMap<String, AnnotationMetadata> imports = (MultiValueMap<String, AnnotationMetadata>)new LinkedMultiValueMap();
/*     */ 
/*     */     
/*     */     public void registerImport(AnnotationMetadata importingClass, String importedClass) {
/* 712 */       this.imports.add(importedClass, importingClass);
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotationMetadata getImportingClassFor(String importedClass) {
/* 717 */       List<AnnotationMetadata> list = (List<AnnotationMetadata>)this.imports.get(importedClass);
/* 718 */       return !CollectionUtils.isEmpty(list) ? list.get(list.size() - 1) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void removeImportingClass(String importingClass) {
/* 723 */       for (List<AnnotationMetadata> list : (Iterable<List<AnnotationMetadata>>)this.imports.values()) {
/* 724 */         Iterator<AnnotationMetadata> iterator; for (iterator = list.iterator(); iterator.hasNext();) {
/* 725 */           if (((AnnotationMetadata)iterator.next()).getClassName().equals(importingClass)) {
/* 726 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 744 */       StringBuilder builder = new StringBuilder("[");
/* 745 */       Iterator<ConfigurationClass> iterator = iterator();
/* 746 */       while (iterator.hasNext()) {
/* 747 */         builder.append(((ConfigurationClass)iterator.next()).getSimpleName());
/* 748 */         if (iterator.hasNext()) {
/* 749 */           builder.append("->");
/*     */         }
/*     */       } 
/* 752 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     private ImportStack() {}
/*     */   }
/*     */   
/*     */   private static class DeferredImportSelectorHolder
/*     */   {
/*     */     private final ConfigurationClass configurationClass;
/*     */     private final DeferredImportSelector importSelector;
/*     */     
/*     */     public DeferredImportSelectorHolder(ConfigurationClass configClass, DeferredImportSelector selector) {
/* 764 */       this.configurationClass = configClass;
/* 765 */       this.importSelector = selector;
/*     */     }
/*     */     
/*     */     public ConfigurationClass getConfigurationClass() {
/* 769 */       return this.configurationClass;
/*     */     }
/*     */     
/*     */     public DeferredImportSelector getImportSelector() {
/* 773 */       return this.importSelector;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class SourceClass
/*     */   {
/*     */     private final Object source;
/*     */ 
/*     */     
/*     */     private final AnnotationMetadata metadata;
/*     */ 
/*     */ 
/*     */     
/*     */     public SourceClass(Object source) {
/* 789 */       this.source = source;
/* 790 */       if (source instanceof Class) {
/* 791 */         this.metadata = (AnnotationMetadata)new StandardAnnotationMetadata((Class)source, true);
/*     */       } else {
/*     */         
/* 794 */         this.metadata = ((MetadataReader)source).getAnnotationMetadata();
/*     */       } 
/*     */     }
/*     */     
/*     */     public final AnnotationMetadata getMetadata() {
/* 799 */       return this.metadata;
/*     */     }
/*     */     
/*     */     public Class<?> loadClass() throws ClassNotFoundException {
/* 803 */       if (this.source instanceof Class) {
/* 804 */         return (Class)this.source;
/*     */       }
/* 806 */       String className = ((MetadataReader)this.source).getClassMetadata().getClassName();
/* 807 */       return ConfigurationClassParser.this.resourceLoader.getClassLoader().loadClass(className);
/*     */     }
/*     */     
/*     */     public boolean isAssignable(Class<?> clazz) throws IOException {
/* 811 */       if (this.source instanceof Class) {
/* 812 */         return clazz.isAssignableFrom((Class)this.source);
/*     */       }
/* 814 */       return (new AssignableTypeFilter(clazz)).match((MetadataReader)this.source, ConfigurationClassParser.this.metadataReaderFactory);
/*     */     }
/*     */     
/*     */     public ConfigurationClass asConfigClass(ConfigurationClass importedBy) throws IOException {
/* 818 */       if (this.source instanceof Class) {
/* 819 */         return new ConfigurationClass((Class)this.source, importedBy);
/*     */       }
/* 821 */       return new ConfigurationClass((MetadataReader)this.source, importedBy);
/*     */     }
/*     */     
/*     */     public Collection<SourceClass> getMemberClasses() throws IOException {
/* 825 */       Object sourceToProcess = this.source;
/* 826 */       if (sourceToProcess instanceof Class) {
/* 827 */         Class<?> sourceClass = (Class)sourceToProcess;
/*     */         try {
/* 829 */           Class<?>[] declaredClasses = sourceClass.getDeclaredClasses();
/* 830 */           List<SourceClass> list = new ArrayList<SourceClass>(declaredClasses.length);
/* 831 */           for (Class<?> declaredClass : declaredClasses) {
/* 832 */             list.add(ConfigurationClassParser.this.asSourceClass(declaredClass));
/*     */           }
/* 834 */           return list;
/*     */         }
/* 836 */         catch (NoClassDefFoundError err) {
/*     */ 
/*     */           
/* 839 */           sourceToProcess = ConfigurationClassParser.this.metadataReaderFactory.getMetadataReader(sourceClass.getName());
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 844 */       MetadataReader sourceReader = (MetadataReader)sourceToProcess;
/* 845 */       String[] memberClassNames = sourceReader.getClassMetadata().getMemberClassNames();
/* 846 */       List<SourceClass> members = new ArrayList<SourceClass>(memberClassNames.length);
/* 847 */       for (String memberClassName : memberClassNames) {
/*     */         try {
/* 849 */           members.add(ConfigurationClassParser.this.asSourceClass(memberClassName));
/*     */         }
/* 851 */         catch (IOException ex) {
/*     */           
/* 853 */           if (ConfigurationClassParser.this.logger.isDebugEnabled()) {
/* 854 */             ConfigurationClassParser.this.logger.debug("Failed to resolve member class [" + memberClassName + "] - not considering it as a configuration class candidate");
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 859 */       return members;
/*     */     }
/*     */     
/*     */     public SourceClass getSuperClass() throws IOException {
/* 863 */       if (this.source instanceof Class) {
/* 864 */         return ConfigurationClassParser.this.asSourceClass(((Class)this.source).getSuperclass());
/*     */       }
/* 866 */       return ConfigurationClassParser.this.asSourceClass(((MetadataReader)this.source).getClassMetadata().getSuperClassName());
/*     */     }
/*     */     
/*     */     public Set<SourceClass> getInterfaces() throws IOException {
/* 870 */       Set<SourceClass> result = new LinkedHashSet<SourceClass>();
/* 871 */       if (this.source instanceof Class) {
/* 872 */         Class<?> sourceClass = (Class)this.source;
/* 873 */         for (Class<?> ifcClass : sourceClass.getInterfaces()) {
/* 874 */           result.add(ConfigurationClassParser.this.asSourceClass(ifcClass));
/*     */         }
/*     */       } else {
/*     */         
/* 878 */         for (String className : this.metadata.getInterfaceNames()) {
/* 879 */           result.add(ConfigurationClassParser.this.asSourceClass(className));
/*     */         }
/*     */       } 
/* 882 */       return result;
/*     */     }
/*     */     
/*     */     public Set<SourceClass> getAnnotations() throws IOException {
/* 886 */       Set<SourceClass> result = new LinkedHashSet<SourceClass>();
/* 887 */       for (String className : this.metadata.getAnnotationTypes()) {
/*     */         try {
/* 889 */           result.add(getRelated(className));
/*     */         }
/* 891 */         catch (Throwable throwable) {}
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 896 */       return result;
/*     */     }
/*     */     
/*     */     public Collection<SourceClass> getAnnotationAttributes(String annType, String attribute) throws IOException {
/* 900 */       Map<String, Object> annotationAttributes = this.metadata.getAnnotationAttributes(annType, true);
/* 901 */       if (annotationAttributes == null || !annotationAttributes.containsKey(attribute)) {
/* 902 */         return Collections.emptySet();
/*     */       }
/* 904 */       String[] classNames = (String[])annotationAttributes.get(attribute);
/* 905 */       Set<SourceClass> result = new LinkedHashSet<SourceClass>();
/* 906 */       for (String className : classNames) {
/* 907 */         result.add(getRelated(className));
/*     */       }
/* 909 */       return result;
/*     */     }
/*     */     
/*     */     private SourceClass getRelated(String className) throws IOException {
/* 913 */       if (this.source instanceof Class) {
/*     */         try {
/* 915 */           Class<?> clazz = ((Class)this.source).getClassLoader().loadClass(className);
/* 916 */           return ConfigurationClassParser.this.asSourceClass(clazz);
/*     */         }
/* 918 */         catch (ClassNotFoundException ex) {
/*     */           
/* 920 */           if (className.startsWith("java")) {
/* 921 */             throw new NestedIOException("Failed to load class [" + className + "]", ex);
/*     */           }
/* 923 */           return new SourceClass(ConfigurationClassParser.this.metadataReaderFactory.getMetadataReader(className));
/*     */         } 
/*     */       }
/* 926 */       return ConfigurationClassParser.this.asSourceClass(className);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 931 */       return (this == other || (other instanceof SourceClass && this.metadata
/* 932 */         .getClassName().equals(((SourceClass)other).metadata.getClassName())));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 937 */       return this.metadata.getClassName().hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 942 */       return this.metadata.getClassName();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CircularImportProblem
/*     */     extends Problem
/*     */   {
/*     */     public CircularImportProblem(ConfigurationClass attemptedImport, Deque<ConfigurationClass> importStack) {
/* 953 */       super(String.format("A circular @Import has been detected: Illegal attempt by @Configuration class '%s' to import class '%s' as '%s' is already present in the current import stack %s", new Object[] { ((ConfigurationClass)importStack
/*     */               
/* 955 */               .element()).getSimpleName(), attemptedImport
/* 956 */               .getSimpleName(), attemptedImport.getSimpleName(), importStack
/* 957 */             }), new Location(((ConfigurationClass)importStack.element()).getResource(), attemptedImport.getMetadata()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\ConfigurationClassParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */