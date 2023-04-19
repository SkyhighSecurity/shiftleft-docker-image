/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
/*     */ import org.springframework.beans.factory.annotation.Autowire;
/*     */ import org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader;
/*     */ import org.springframework.beans.factory.parsing.SourceExtractor;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionReader;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.MethodMetadata;
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
/*     */ class ConfigurationClassBeanDefinitionReader
/*     */ {
/*  72 */   private static final Log logger = LogFactory.getLog(ConfigurationClassBeanDefinitionReader.class);
/*     */   
/*  74 */   private static final ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
/*     */ 
/*     */   
/*     */   private final BeanDefinitionRegistry registry;
/*     */ 
/*     */   
/*     */   private final SourceExtractor sourceExtractor;
/*     */ 
/*     */   
/*     */   private final ResourceLoader resourceLoader;
/*     */ 
/*     */   
/*     */   private final Environment environment;
/*     */ 
/*     */   
/*     */   private final BeanNameGenerator importBeanNameGenerator;
/*     */ 
/*     */   
/*     */   private final ImportRegistry importRegistry;
/*     */ 
/*     */   
/*     */   private final ConditionEvaluator conditionEvaluator;
/*     */ 
/*     */   
/*     */   ConfigurationClassBeanDefinitionReader(BeanDefinitionRegistry registry, SourceExtractor sourceExtractor, ResourceLoader resourceLoader, Environment environment, BeanNameGenerator importBeanNameGenerator, ImportRegistry importRegistry) {
/*  99 */     this.registry = registry;
/* 100 */     this.sourceExtractor = sourceExtractor;
/* 101 */     this.resourceLoader = resourceLoader;
/* 102 */     this.environment = environment;
/* 103 */     this.importBeanNameGenerator = importBeanNameGenerator;
/* 104 */     this.importRegistry = importRegistry;
/* 105 */     this.conditionEvaluator = new ConditionEvaluator(registry, environment, resourceLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadBeanDefinitions(Set<ConfigurationClass> configurationModel) {
/* 114 */     TrackedConditionEvaluator trackedConditionEvaluator = new TrackedConditionEvaluator();
/* 115 */     for (ConfigurationClass configClass : configurationModel) {
/* 116 */       loadBeanDefinitionsForConfigurationClass(configClass, trackedConditionEvaluator);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadBeanDefinitionsForConfigurationClass(ConfigurationClass configClass, TrackedConditionEvaluator trackedConditionEvaluator) {
/* 127 */     if (trackedConditionEvaluator.shouldSkip(configClass)) {
/* 128 */       String beanName = configClass.getBeanName();
/* 129 */       if (StringUtils.hasLength(beanName) && this.registry.containsBeanDefinition(beanName)) {
/* 130 */         this.registry.removeBeanDefinition(beanName);
/*     */       }
/* 132 */       this.importRegistry.removeImportingClass(configClass.getMetadata().getClassName());
/*     */       
/*     */       return;
/*     */     } 
/* 136 */     if (configClass.isImported()) {
/* 137 */       registerBeanDefinitionForImportedConfigurationClass(configClass);
/*     */     }
/* 139 */     for (BeanMethod beanMethod : configClass.getBeanMethods()) {
/* 140 */       loadBeanDefinitionsForBeanMethod(beanMethod);
/*     */     }
/*     */     
/* 143 */     loadBeanDefinitionsFromImportedResources(configClass.getImportedResources());
/* 144 */     loadBeanDefinitionsFromRegistrars(configClass.getImportBeanDefinitionRegistrars());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void registerBeanDefinitionForImportedConfigurationClass(ConfigurationClass configClass) {
/* 151 */     AnnotationMetadata metadata = configClass.getMetadata();
/* 152 */     AnnotatedGenericBeanDefinition configBeanDef = new AnnotatedGenericBeanDefinition(metadata);
/*     */     
/* 154 */     ScopeMetadata scopeMetadata = scopeMetadataResolver.resolveScopeMetadata((BeanDefinition)configBeanDef);
/* 155 */     configBeanDef.setScope(scopeMetadata.getScopeName());
/* 156 */     String configBeanName = this.importBeanNameGenerator.generateBeanName((BeanDefinition)configBeanDef, this.registry);
/* 157 */     AnnotationConfigUtils.processCommonDefinitionAnnotations((AnnotatedBeanDefinition)configBeanDef, (AnnotatedTypeMetadata)metadata);
/*     */     
/* 159 */     BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder((BeanDefinition)configBeanDef, configBeanName);
/* 160 */     definitionHolder = AnnotationConfigUtils.applyScopedProxyMode(scopeMetadata, definitionHolder, this.registry);
/* 161 */     this.registry.registerBeanDefinition(definitionHolder.getBeanName(), definitionHolder.getBeanDefinition());
/* 162 */     configClass.setBeanName(configBeanName);
/*     */     
/* 164 */     if (logger.isDebugEnabled()) {
/* 165 */       logger.debug("Registered bean definition for imported class '" + configBeanName + "'");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadBeanDefinitionsForBeanMethod(BeanMethod beanMethod) {
/* 174 */     ConfigurationClass configClass = beanMethod.getConfigurationClass();
/* 175 */     MethodMetadata metadata = beanMethod.getMetadata();
/* 176 */     String methodName = metadata.getMethodName();
/*     */ 
/*     */     
/* 179 */     if (this.conditionEvaluator.shouldSkip((AnnotatedTypeMetadata)metadata, ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN)) {
/* 180 */       configClass.skippedBeanMethods.add(methodName);
/*     */       return;
/*     */     } 
/* 183 */     if (configClass.skippedBeanMethods.contains(methodName)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 188 */     AnnotationAttributes bean = AnnotationConfigUtils.attributesFor((AnnotatedTypeMetadata)metadata, Bean.class);
/* 189 */     List<String> names = new ArrayList<String>(Arrays.asList(bean.getStringArray("name")));
/* 190 */     String beanName = !names.isEmpty() ? names.remove(0) : methodName;
/*     */ 
/*     */     
/* 193 */     for (String alias : names) {
/* 194 */       this.registry.registerAlias(beanName, alias);
/*     */     }
/*     */ 
/*     */     
/* 198 */     if (isOverriddenByExistingDefinition(beanMethod, beanName)) {
/* 199 */       if (beanName.equals(beanMethod.getConfigurationClass().getBeanName())) {
/* 200 */         throw new BeanDefinitionStoreException(beanMethod.getConfigurationClass().getResource().getDescription(), beanName, "Bean name derived from @Bean method '" + beanMethod
/* 201 */             .getMetadata().getMethodName() + "' clashes with bean name for containing configuration class; please make those names unique!");
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 207 */     ConfigurationClassBeanDefinition beanDef = new ConfigurationClassBeanDefinition(configClass, metadata);
/* 208 */     beanDef.setResource(configClass.getResource());
/* 209 */     beanDef.setSource(this.sourceExtractor.extractSource(metadata, configClass.getResource()));
/*     */     
/* 211 */     if (metadata.isStatic()) {
/*     */       
/* 213 */       beanDef.setBeanClassName(configClass.getMetadata().getClassName());
/* 214 */       beanDef.setFactoryMethodName(methodName);
/*     */     }
/*     */     else {
/*     */       
/* 218 */       beanDef.setFactoryBeanName(configClass.getBeanName());
/* 219 */       beanDef.setUniqueFactoryMethodName(methodName);
/*     */     } 
/* 221 */     beanDef.setAutowireMode(3);
/* 222 */     beanDef.setAttribute(RequiredAnnotationBeanPostProcessor.SKIP_REQUIRED_CHECK_ATTRIBUTE, Boolean.TRUE);
/*     */     
/* 224 */     AnnotationConfigUtils.processCommonDefinitionAnnotations(beanDef, (AnnotatedTypeMetadata)metadata);
/*     */     
/* 226 */     Autowire autowire = (Autowire)bean.getEnum("autowire");
/* 227 */     if (autowire.isAutowire()) {
/* 228 */       beanDef.setAutowireMode(autowire.value());
/*     */     }
/*     */     
/* 231 */     String initMethodName = bean.getString("initMethod");
/* 232 */     if (StringUtils.hasText(initMethodName)) {
/* 233 */       beanDef.setInitMethodName(initMethodName);
/*     */     }
/*     */     
/* 236 */     String destroyMethodName = bean.getString("destroyMethod");
/* 237 */     if (destroyMethodName != null) {
/* 238 */       beanDef.setDestroyMethodName(destroyMethodName);
/*     */     }
/*     */ 
/*     */     
/* 242 */     ScopedProxyMode proxyMode = ScopedProxyMode.NO;
/* 243 */     AnnotationAttributes attributes = AnnotationConfigUtils.attributesFor((AnnotatedTypeMetadata)metadata, Scope.class);
/* 244 */     if (attributes != null) {
/* 245 */       beanDef.setScope(attributes.getString("value"));
/* 246 */       proxyMode = (ScopedProxyMode)attributes.getEnum("proxyMode");
/* 247 */       if (proxyMode == ScopedProxyMode.DEFAULT) {
/* 248 */         proxyMode = ScopedProxyMode.NO;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 253 */     ConfigurationClassBeanDefinition configurationClassBeanDefinition1 = beanDef;
/* 254 */     if (proxyMode != ScopedProxyMode.NO) {
/* 255 */       BeanDefinitionHolder proxyDef = ScopedProxyCreator.createScopedProxy(new BeanDefinitionHolder((BeanDefinition)beanDef, beanName), this.registry, (proxyMode == ScopedProxyMode.TARGET_CLASS));
/*     */ 
/*     */ 
/*     */       
/* 259 */       configurationClassBeanDefinition1 = new ConfigurationClassBeanDefinition((RootBeanDefinition)proxyDef.getBeanDefinition(), configClass, metadata);
/*     */     } 
/*     */     
/* 262 */     if (logger.isDebugEnabled()) {
/* 263 */       logger.debug(String.format("Registering bean definition for @Bean method %s.%s()", new Object[] { configClass
/* 264 */               .getMetadata().getClassName(), beanName }));
/*     */     }
/*     */     
/* 267 */     this.registry.registerBeanDefinition(beanName, (BeanDefinition)configurationClassBeanDefinition1);
/*     */   }
/*     */   
/*     */   protected boolean isOverriddenByExistingDefinition(BeanMethod beanMethod, String beanName) {
/* 271 */     if (!this.registry.containsBeanDefinition(beanName)) {
/* 272 */       return false;
/*     */     }
/* 274 */     BeanDefinition existingBeanDef = this.registry.getBeanDefinition(beanName);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 280 */     if (existingBeanDef instanceof ConfigurationClassBeanDefinition) {
/* 281 */       ConfigurationClassBeanDefinition ccbd = (ConfigurationClassBeanDefinition)existingBeanDef;
/* 282 */       return ccbd.getMetadata().getClassName().equals(beanMethod
/* 283 */           .getConfigurationClass().getMetadata().getClassName());
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 288 */     if (existingBeanDef instanceof ScannedGenericBeanDefinition) {
/* 289 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 294 */     if (existingBeanDef.getRole() > 0) {
/* 295 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 300 */     if (this.registry instanceof DefaultListableBeanFactory && 
/* 301 */       !((DefaultListableBeanFactory)this.registry).isAllowBeanDefinitionOverriding()) {
/* 302 */       throw new BeanDefinitionStoreException(beanMethod.getConfigurationClass().getResource().getDescription(), beanName, "@Bean definition illegally overridden by existing bean definition: " + existingBeanDef);
/*     */     }
/*     */     
/* 305 */     if (logger.isInfoEnabled()) {
/* 306 */       logger.info(String.format("Skipping bean definition for %s: a definition for bean '%s' already exists. This top-level bean definition is considered as an override.", new Object[] { beanMethod, beanName }));
/*     */     }
/*     */ 
/*     */     
/* 310 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadBeanDefinitionsFromImportedResources(Map<String, Class<? extends BeanDefinitionReader>> importedResources) {
/* 316 */     Map<Class<?>, BeanDefinitionReader> readerInstanceCache = new HashMap<Class<?>, BeanDefinitionReader>();
/*     */     
/* 318 */     for (Map.Entry<String, Class<? extends BeanDefinitionReader>> entry : importedResources.entrySet()) {
/* 319 */       Class<XmlBeanDefinitionReader> clazz; String resource = entry.getKey();
/* 320 */       Class<? extends BeanDefinitionReader> readerClass = entry.getValue();
/*     */ 
/*     */       
/* 323 */       if (BeanDefinitionReader.class == readerClass) {
/* 324 */         if (StringUtils.endsWithIgnoreCase(resource, ".groovy")) {
/*     */           
/* 326 */           Class<GroovyBeanDefinitionReader> clazz1 = GroovyBeanDefinitionReader.class;
/*     */         }
/*     */         else {
/*     */           
/* 330 */           clazz = XmlBeanDefinitionReader.class;
/*     */         } 
/*     */       }
/*     */       
/* 334 */       BeanDefinitionReader reader = readerInstanceCache.get(clazz);
/* 335 */       if (reader == null) {
/*     */         
/*     */         try {
/* 338 */           reader = clazz.getConstructor(new Class[] { BeanDefinitionRegistry.class }).newInstance(new Object[] { this.registry });
/*     */           
/* 340 */           if (reader instanceof AbstractBeanDefinitionReader) {
/* 341 */             AbstractBeanDefinitionReader abdr = (AbstractBeanDefinitionReader)reader;
/* 342 */             abdr.setResourceLoader(this.resourceLoader);
/* 343 */             abdr.setEnvironment(this.environment);
/*     */           } 
/* 345 */           readerInstanceCache.put(clazz, reader);
/*     */         }
/* 347 */         catch (Throwable ex) {
/* 348 */           throw new IllegalStateException("Could not instantiate BeanDefinitionReader class [" + clazz
/* 349 */               .getName() + "]");
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 354 */       reader.loadBeanDefinitions(resource);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadBeanDefinitionsFromRegistrars(Map<ImportBeanDefinitionRegistrar, AnnotationMetadata> registrars) {
/* 359 */     for (Map.Entry<ImportBeanDefinitionRegistrar, AnnotationMetadata> entry : registrars.entrySet()) {
/* 360 */       ((ImportBeanDefinitionRegistrar)entry.getKey()).registerBeanDefinitions(entry.getValue(), this.registry);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ConfigurationClassBeanDefinition
/*     */     extends RootBeanDefinition
/*     */     implements AnnotatedBeanDefinition
/*     */   {
/*     */     private final AnnotationMetadata annotationMetadata;
/*     */ 
/*     */     
/*     */     private final MethodMetadata factoryMethodMetadata;
/*     */ 
/*     */ 
/*     */     
/*     */     public ConfigurationClassBeanDefinition(ConfigurationClass configClass, MethodMetadata beanMethodMetadata) {
/* 379 */       this.annotationMetadata = configClass.getMetadata();
/* 380 */       this.factoryMethodMetadata = beanMethodMetadata;
/* 381 */       setLenientConstructorResolution(false);
/*     */     }
/*     */ 
/*     */     
/*     */     public ConfigurationClassBeanDefinition(RootBeanDefinition original, ConfigurationClass configClass, MethodMetadata beanMethodMetadata) {
/* 386 */       super(original);
/* 387 */       this.annotationMetadata = configClass.getMetadata();
/* 388 */       this.factoryMethodMetadata = beanMethodMetadata;
/*     */     }
/*     */     
/*     */     private ConfigurationClassBeanDefinition(ConfigurationClassBeanDefinition original) {
/* 392 */       super(original);
/* 393 */       this.annotationMetadata = original.annotationMetadata;
/* 394 */       this.factoryMethodMetadata = original.factoryMethodMetadata;
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotationMetadata getMetadata() {
/* 399 */       return this.annotationMetadata;
/*     */     }
/*     */ 
/*     */     
/*     */     public MethodMetadata getFactoryMethodMetadata() {
/* 404 */       return this.factoryMethodMetadata;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isFactoryMethod(Method candidate) {
/* 409 */       return (super.isFactoryMethod(candidate) && BeanAnnotationHelper.isBeanAnnotated(candidate));
/*     */     }
/*     */ 
/*     */     
/*     */     public ConfigurationClassBeanDefinition cloneBeanDefinition() {
/* 414 */       return new ConfigurationClassBeanDefinition(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class TrackedConditionEvaluator
/*     */   {
/* 425 */     private final Map<ConfigurationClass, Boolean> skipped = new HashMap<ConfigurationClass, Boolean>();
/*     */     
/*     */     public boolean shouldSkip(ConfigurationClass configClass) {
/* 428 */       Boolean skip = this.skipped.get(configClass);
/* 429 */       if (skip == null) {
/* 430 */         if (configClass.isImported()) {
/* 431 */           boolean allSkipped = true;
/* 432 */           for (ConfigurationClass importedBy : configClass.getImportedBy()) {
/* 433 */             if (!shouldSkip(importedBy)) {
/* 434 */               allSkipped = false;
/*     */               break;
/*     */             } 
/*     */           } 
/* 438 */           if (allSkipped)
/*     */           {
/* 440 */             skip = Boolean.valueOf(true);
/*     */           }
/*     */         } 
/* 443 */         if (skip == null) {
/* 444 */           skip = Boolean.valueOf(ConfigurationClassBeanDefinitionReader.this.conditionEvaluator.shouldSkip((AnnotatedTypeMetadata)configClass.getMetadata(), ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN));
/*     */         }
/* 446 */         this.skipped.put(configClass, skip);
/*     */       } 
/* 448 */       return skip.booleanValue();
/*     */     }
/*     */     
/*     */     private TrackedConditionEvaluator() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\ConfigurationClassBeanDefinitionReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */