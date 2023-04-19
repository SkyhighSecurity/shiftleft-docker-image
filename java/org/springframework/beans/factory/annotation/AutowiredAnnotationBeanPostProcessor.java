/*     */ package org.springframework.beans.factory.annotation;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.InjectionPoint;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.UnsatisfiedDependencyException;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*     */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
/*     */ import org.springframework.beans.factory.support.LookupOverride;
/*     */ import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
/*     */ import org.springframework.beans.factory.support.MethodOverride;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.PriorityOrdered;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public class AutowiredAnnotationBeanPostProcessor
/*     */   extends InstantiationAwareBeanPostProcessorAdapter
/*     */   implements MergedBeanDefinitionPostProcessor, PriorityOrdered, BeanFactoryAware
/*     */ {
/* 120 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/* 122 */   private final Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<Class<? extends Annotation>>(4);
/*     */ 
/*     */   
/* 125 */   private String requiredParameterName = "required";
/*     */   
/*     */   private boolean requiredParameterValue = true;
/*     */   
/* 129 */   private int order = 2147483645;
/*     */ 
/*     */   
/*     */   private ConfigurableListableBeanFactory beanFactory;
/*     */   
/* 134 */   private final Set<String> lookupMethodsChecked = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>(256));
/*     */   
/* 136 */   private final Map<Class<?>, Constructor<?>[]> candidateConstructorsCache = (Map)new ConcurrentHashMap<Class<?>, Constructor<?>>(256);
/*     */ 
/*     */   
/* 139 */   private final Map<String, InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap<String, InjectionMetadata>(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AutowiredAnnotationBeanPostProcessor() {
/* 150 */     this.autowiredAnnotationTypes.add(Autowired.class);
/* 151 */     this.autowiredAnnotationTypes.add(Value.class);
/*     */     try {
/* 153 */       this.autowiredAnnotationTypes.add(
/* 154 */           ClassUtils.forName("javax.inject.Inject", AutowiredAnnotationBeanPostProcessor.class.getClassLoader()));
/* 155 */       this.logger.info("JSR-330 'javax.inject.Inject' annotation found and supported for autowiring");
/*     */     }
/* 157 */     catch (ClassNotFoundException classNotFoundException) {}
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
/*     */   public void setAutowiredAnnotationType(Class<? extends Annotation> autowiredAnnotationType) {
/* 173 */     Assert.notNull(autowiredAnnotationType, "'autowiredAnnotationType' must not be null");
/* 174 */     this.autowiredAnnotationTypes.clear();
/* 175 */     this.autowiredAnnotationTypes.add(autowiredAnnotationType);
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
/*     */   public void setAutowiredAnnotationTypes(Set<Class<? extends Annotation>> autowiredAnnotationTypes) {
/* 188 */     Assert.notEmpty(autowiredAnnotationTypes, "'autowiredAnnotationTypes' must not be empty");
/* 189 */     this.autowiredAnnotationTypes.clear();
/* 190 */     this.autowiredAnnotationTypes.addAll(autowiredAnnotationTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRequiredParameterName(String requiredParameterName) {
/* 198 */     this.requiredParameterName = requiredParameterName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRequiredParameterValue(boolean requiredParameterValue) {
/* 208 */     this.requiredParameterValue = requiredParameterValue;
/*     */   }
/*     */   
/*     */   public void setOrder(int order) {
/* 212 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 217 */     return this.order;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 222 */     if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
/* 223 */       throw new IllegalArgumentException("AutowiredAnnotationBeanPostProcessor requires a ConfigurableListableBeanFactory: " + beanFactory);
/*     */     }
/*     */     
/* 226 */     this.beanFactory = (ConfigurableListableBeanFactory)beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
/* 232 */     if (beanType != null) {
/* 233 */       InjectionMetadata metadata = findAutowiringMetadata(beanName, beanType, null);
/* 234 */       metadata.checkConfigMembers(beanDefinition);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, final String beanName) throws BeanCreationException {
/* 243 */     if (!this.lookupMethodsChecked.contains(beanName)) {
/*     */       try {
/* 245 */         ReflectionUtils.doWithMethods(beanClass, new ReflectionUtils.MethodCallback()
/*     */             {
/*     */               public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
/* 248 */                 Lookup lookup = method.<Lookup>getAnnotation(Lookup.class);
/* 249 */                 if (lookup != null) {
/* 250 */                   LookupOverride override = new LookupOverride(method, lookup.value());
/*     */                   try {
/* 252 */                     RootBeanDefinition mbd = (RootBeanDefinition)AutowiredAnnotationBeanPostProcessor.this.beanFactory.getMergedBeanDefinition(beanName);
/* 253 */                     mbd.getMethodOverrides().addOverride((MethodOverride)override);
/*     */                   }
/* 255 */                   catch (NoSuchBeanDefinitionException ex) {
/* 256 */                     throw new BeanCreationException(beanName, "Cannot apply @Lookup to beans without corresponding bean definition");
/*     */                   }
/*     */                 
/*     */                 }
/*     */               
/*     */               }
/*     */             });
/* 263 */       } catch (IllegalStateException ex) {
/* 264 */         throw new BeanCreationException(beanName, "Lookup method resolution failed", ex);
/*     */       }
/* 266 */       catch (NoClassDefFoundError err) {
/* 267 */         throw new BeanCreationException(beanName, "Failed to introspect bean class [" + beanClass.getName() + "] for lookup method metadata: could not find class that it depends on", err);
/*     */       } 
/*     */       
/* 270 */       this.lookupMethodsChecked.add(beanName);
/*     */     } 
/*     */ 
/*     */     
/* 274 */     Constructor[] arrayOfConstructor = (Constructor[])this.candidateConstructorsCache.get(beanClass);
/* 275 */     if (arrayOfConstructor == null)
/*     */     {
/* 277 */       synchronized (this.candidateConstructorsCache) {
/* 278 */         arrayOfConstructor = (Constructor[])this.candidateConstructorsCache.get(beanClass);
/* 279 */         if (arrayOfConstructor == null) {
/*     */           Constructor[] arrayOfConstructor1;
/*     */           try {
/* 282 */             arrayOfConstructor1 = (Constructor[])beanClass.getDeclaredConstructors();
/*     */           }
/* 284 */           catch (Throwable ex) {
/* 285 */             throw new BeanCreationException(beanName, "Resolution of declared constructors on bean Class [" + beanClass
/* 286 */                 .getName() + "] from ClassLoader [" + beanClass
/* 287 */                 .getClassLoader() + "] failed", ex);
/*     */           } 
/* 289 */           List<Constructor<?>> candidates = new ArrayList<Constructor<?>>(arrayOfConstructor1.length);
/* 290 */           Constructor<?> requiredConstructor = null;
/* 291 */           Constructor<?> defaultConstructor = null;
/* 292 */           for (Constructor<?> candidate : arrayOfConstructor1) {
/* 293 */             AnnotationAttributes ann = findAutowiredAnnotation(candidate);
/* 294 */             if (ann == null) {
/* 295 */               Class<?> userClass = ClassUtils.getUserClass(beanClass);
/* 296 */               if (userClass != beanClass) {
/*     */                 
/*     */                 try {
/* 299 */                   Constructor<?> superCtor = userClass.getDeclaredConstructor(candidate.getParameterTypes());
/* 300 */                   ann = findAutowiredAnnotation(superCtor);
/*     */                 }
/* 302 */                 catch (NoSuchMethodException noSuchMethodException) {}
/*     */               }
/*     */             } 
/*     */ 
/*     */             
/* 307 */             if (ann != null) {
/* 308 */               if (requiredConstructor != null) {
/* 309 */                 throw new BeanCreationException(beanName, "Invalid autowire-marked constructor: " + candidate + ". Found constructor with 'required' Autowired annotation already: " + requiredConstructor);
/*     */               }
/*     */ 
/*     */ 
/*     */               
/* 314 */               boolean required = determineRequiredStatus(ann);
/* 315 */               if (required) {
/* 316 */                 if (!candidates.isEmpty()) {
/* 317 */                   throw new BeanCreationException(beanName, "Invalid autowire-marked constructors: " + candidates + ". Found constructor with 'required' Autowired annotation: " + candidate);
/*     */                 }
/*     */ 
/*     */ 
/*     */                 
/* 322 */                 requiredConstructor = candidate;
/*     */               } 
/* 324 */               candidates.add(candidate);
/*     */             }
/* 326 */             else if ((candidate.getParameterTypes()).length == 0) {
/* 327 */               defaultConstructor = candidate;
/*     */             } 
/*     */           } 
/* 330 */           if (!candidates.isEmpty()) {
/*     */             
/* 332 */             if (requiredConstructor == null) {
/* 333 */               if (defaultConstructor != null) {
/* 334 */                 candidates.add(defaultConstructor);
/*     */               }
/* 336 */               else if (candidates.size() == 1 && this.logger.isWarnEnabled()) {
/* 337 */                 this.logger.warn("Inconsistent constructor declaration on bean with name '" + beanName + "': single autowire-marked constructor flagged as optional - this constructor is effectively required since there is no default constructor to fall back to: " + candidates
/*     */ 
/*     */                     
/* 340 */                     .get(0));
/*     */               } 
/*     */             }
/* 343 */             arrayOfConstructor = candidates.<Constructor>toArray(new Constructor[candidates.size()]);
/*     */           }
/* 345 */           else if (arrayOfConstructor1.length == 1 && (arrayOfConstructor1[0].getParameterTypes()).length > 0) {
/* 346 */             arrayOfConstructor = new Constructor[] { arrayOfConstructor1[0] };
/*     */           } else {
/*     */             
/* 349 */             arrayOfConstructor = new Constructor[0];
/*     */           } 
/* 351 */           this.candidateConstructorsCache.put(beanClass, arrayOfConstructor);
/*     */         } 
/*     */       } 
/*     */     }
/* 355 */     return (arrayOfConstructor.length > 0) ? (Constructor<?>[])arrayOfConstructor : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeanCreationException {
/* 362 */     InjectionMetadata metadata = findAutowiringMetadata(beanName, bean.getClass(), pvs);
/*     */     try {
/* 364 */       metadata.inject(bean, beanName, pvs);
/*     */     }
/* 366 */     catch (BeanCreationException ex) {
/* 367 */       throw ex;
/*     */     }
/* 369 */     catch (Throwable ex) {
/* 370 */       throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", ex);
/*     */     } 
/* 372 */     return pvs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processInjection(Object bean) throws BeanCreationException {
/* 382 */     Class<?> clazz = bean.getClass();
/* 383 */     InjectionMetadata metadata = findAutowiringMetadata(clazz.getName(), clazz, null);
/*     */     try {
/* 385 */       metadata.inject(bean, null, null);
/*     */     }
/* 387 */     catch (BeanCreationException ex) {
/* 388 */       throw ex;
/*     */     }
/* 390 */     catch (Throwable ex) {
/* 391 */       throw new BeanCreationException("Injection of autowired dependencies failed for class [" + clazz + "]", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InjectionMetadata findAutowiringMetadata(String beanName, Class<?> clazz, PropertyValues pvs) {
/* 399 */     String cacheKey = StringUtils.hasLength(beanName) ? beanName : clazz.getName();
/*     */     
/* 401 */     InjectionMetadata metadata = this.injectionMetadataCache.get(cacheKey);
/* 402 */     if (InjectionMetadata.needsRefresh(metadata, clazz)) {
/* 403 */       synchronized (this.injectionMetadataCache) {
/* 404 */         metadata = this.injectionMetadataCache.get(cacheKey);
/* 405 */         if (InjectionMetadata.needsRefresh(metadata, clazz)) {
/* 406 */           if (metadata != null) {
/* 407 */             metadata.clear(pvs);
/*     */           }
/*     */           try {
/* 410 */             metadata = buildAutowiringMetadata(clazz);
/* 411 */             this.injectionMetadataCache.put(cacheKey, metadata);
/*     */           }
/* 413 */           catch (NoClassDefFoundError err) {
/* 414 */             throw new IllegalStateException("Failed to introspect bean class [" + clazz.getName() + "] for autowiring metadata: could not find class that it depends on", err);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 420 */     return metadata;
/*     */   }
/*     */   
/*     */   private InjectionMetadata buildAutowiringMetadata(final Class<?> clazz) {
/* 424 */     LinkedList<InjectionMetadata.InjectedElement> elements = new LinkedList<InjectionMetadata.InjectedElement>();
/* 425 */     Class<?> targetClass = clazz;
/*     */     
/*     */     do {
/* 428 */       final LinkedList<InjectionMetadata.InjectedElement> currElements = new LinkedList<InjectionMetadata.InjectedElement>();
/*     */ 
/*     */       
/* 431 */       ReflectionUtils.doWithLocalFields(targetClass, new ReflectionUtils.FieldCallback()
/*     */           {
/*     */             public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
/* 434 */               AnnotationAttributes ann = AutowiredAnnotationBeanPostProcessor.this.findAutowiredAnnotation(field);
/* 435 */               if (ann != null) {
/* 436 */                 if (Modifier.isStatic(field.getModifiers())) {
/* 437 */                   if (AutowiredAnnotationBeanPostProcessor.this.logger.isWarnEnabled()) {
/* 438 */                     AutowiredAnnotationBeanPostProcessor.this.logger.warn("Autowired annotation is not supported on static fields: " + field);
/*     */                   }
/*     */                   return;
/*     */                 } 
/* 442 */                 boolean required = AutowiredAnnotationBeanPostProcessor.this.determineRequiredStatus(ann);
/* 443 */                 currElements.add(new AutowiredAnnotationBeanPostProcessor.AutowiredFieldElement(field, required));
/*     */               } 
/*     */             }
/*     */           });
/*     */       
/* 448 */       ReflectionUtils.doWithLocalMethods(targetClass, new ReflectionUtils.MethodCallback()
/*     */           {
/*     */             public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
/* 451 */               Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
/* 452 */               if (!BridgeMethodResolver.isVisibilityBridgeMethodPair(method, bridgedMethod)) {
/*     */                 return;
/*     */               }
/* 455 */               AnnotationAttributes ann = AutowiredAnnotationBeanPostProcessor.this.findAutowiredAnnotation(bridgedMethod);
/* 456 */               if (ann != null && method.equals(ClassUtils.getMostSpecificMethod(method, clazz))) {
/* 457 */                 if (Modifier.isStatic(method.getModifiers())) {
/* 458 */                   if (AutowiredAnnotationBeanPostProcessor.this.logger.isWarnEnabled()) {
/* 459 */                     AutowiredAnnotationBeanPostProcessor.this.logger.warn("Autowired annotation is not supported on static methods: " + method);
/*     */                   }
/*     */                   return;
/*     */                 } 
/* 463 */                 if ((method.getParameterTypes()).length == 0 && 
/* 464 */                   AutowiredAnnotationBeanPostProcessor.this.logger.isWarnEnabled()) {
/* 465 */                   AutowiredAnnotationBeanPostProcessor.this.logger.warn("Autowired annotation should only be used on methods with parameters: " + method);
/*     */                 }
/*     */ 
/*     */                 
/* 469 */                 boolean required = AutowiredAnnotationBeanPostProcessor.this.determineRequiredStatus(ann);
/* 470 */                 PropertyDescriptor pd = BeanUtils.findPropertyForMethod(bridgedMethod, clazz);
/* 471 */                 currElements.add(new AutowiredAnnotationBeanPostProcessor.AutowiredMethodElement(method, required, pd));
/*     */               } 
/*     */             }
/*     */           });
/*     */       
/* 476 */       elements.addAll(0, currElements);
/* 477 */       targetClass = targetClass.getSuperclass();
/*     */     }
/* 479 */     while (targetClass != null && targetClass != Object.class);
/*     */     
/* 481 */     return new InjectionMetadata(clazz, elements);
/*     */   }
/*     */   
/*     */   private AnnotationAttributes findAutowiredAnnotation(AccessibleObject ao) {
/* 485 */     if ((ao.getAnnotations()).length > 0) {
/* 486 */       for (Class<? extends Annotation> type : this.autowiredAnnotationTypes) {
/* 487 */         AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(ao, type);
/* 488 */         if (attributes != null) {
/* 489 */           return attributes;
/*     */         }
/*     */       } 
/*     */     }
/* 493 */     return null;
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
/*     */   protected boolean determineRequiredStatus(AnnotationAttributes ann) {
/* 505 */     return (!ann.containsKey(this.requiredParameterName) || this.requiredParameterValue == ann
/* 506 */       .getBoolean(this.requiredParameterName));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> Map<String, T> findAutowireCandidates(Class<T> type) throws BeansException {
/* 516 */     if (this.beanFactory == null) {
/* 517 */       throw new IllegalStateException("No BeanFactory configured - override the getBeanOfType method or specify the 'beanFactory' property");
/*     */     }
/*     */     
/* 520 */     return BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory)this.beanFactory, type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void registerDependentBeans(String beanName, Set<String> autowiredBeanNames) {
/* 527 */     if (beanName != null) {
/* 528 */       for (String autowiredBeanName : autowiredBeanNames) {
/* 529 */         if (this.beanFactory.containsBean(autowiredBeanName)) {
/* 530 */           this.beanFactory.registerDependentBean(autowiredBeanName, beanName);
/*     */         }
/* 532 */         if (this.logger.isDebugEnabled()) {
/* 533 */           this.logger.debug("Autowiring by type from bean name '" + beanName + "' to bean named '" + autowiredBeanName + "'");
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object resolvedCachedArgument(String beanName, Object cachedArgument) {
/* 544 */     if (cachedArgument instanceof DependencyDescriptor) {
/* 545 */       DependencyDescriptor descriptor = (DependencyDescriptor)cachedArgument;
/* 546 */       return this.beanFactory.resolveDependency(descriptor, beanName, null, null);
/*     */     } 
/*     */     
/* 549 */     return cachedArgument;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class AutowiredFieldElement
/*     */     extends InjectionMetadata.InjectedElement
/*     */   {
/*     */     private final boolean required;
/*     */ 
/*     */     
/*     */     private volatile boolean cached = false;
/*     */     
/*     */     private volatile Object cachedFieldValue;
/*     */ 
/*     */     
/*     */     public AutowiredFieldElement(Field field, boolean required) {
/* 566 */       super(field, null);
/* 567 */       this.required = required;
/*     */     }
/*     */     
/*     */     protected void inject(Object bean, String beanName, PropertyValues pvs) throws Throwable {
/*     */       Object value;
/* 572 */       Field field = (Field)this.member;
/*     */       
/* 574 */       if (this.cached) {
/* 575 */         value = AutowiredAnnotationBeanPostProcessor.this.resolvedCachedArgument(beanName, this.cachedFieldValue);
/*     */       } else {
/*     */         
/* 578 */         DependencyDescriptor desc = new DependencyDescriptor(field, this.required);
/* 579 */         desc.setContainingClass(bean.getClass());
/* 580 */         Set<String> autowiredBeanNames = new LinkedHashSet<String>(1);
/* 581 */         TypeConverter typeConverter = AutowiredAnnotationBeanPostProcessor.this.beanFactory.getTypeConverter();
/*     */         try {
/* 583 */           value = AutowiredAnnotationBeanPostProcessor.this.beanFactory.resolveDependency(desc, beanName, autowiredBeanNames, typeConverter);
/*     */         }
/* 585 */         catch (BeansException ex) {
/* 586 */           throw new UnsatisfiedDependencyException(null, beanName, new InjectionPoint(field), ex);
/*     */         } 
/* 588 */         synchronized (this) {
/* 589 */           if (!this.cached) {
/* 590 */             if (value != null || this.required) {
/* 591 */               this.cachedFieldValue = desc;
/* 592 */               AutowiredAnnotationBeanPostProcessor.this.registerDependentBeans(beanName, autowiredBeanNames);
/* 593 */               if (autowiredBeanNames.size() == 1) {
/* 594 */                 String autowiredBeanName = autowiredBeanNames.iterator().next();
/* 595 */                 if (AutowiredAnnotationBeanPostProcessor.this.beanFactory.containsBean(autowiredBeanName) && AutowiredAnnotationBeanPostProcessor.this
/* 596 */                   .beanFactory.isTypeMatch(autowiredBeanName, field.getType())) {
/* 597 */                   this
/* 598 */                     .cachedFieldValue = new AutowiredAnnotationBeanPostProcessor.ShortcutDependencyDescriptor(desc, autowiredBeanName, field.getType());
/*     */                 }
/*     */               } 
/*     */             } else {
/*     */               
/* 603 */               this.cachedFieldValue = null;
/*     */             } 
/* 605 */             this.cached = true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 609 */       if (value != null) {
/* 610 */         ReflectionUtils.makeAccessible(field);
/* 611 */         field.set(bean, value);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class AutowiredMethodElement
/*     */     extends InjectionMetadata.InjectedElement
/*     */   {
/*     */     private final boolean required;
/*     */     
/*     */     private volatile boolean cached = false;
/*     */     
/*     */     private volatile Object[] cachedMethodArguments;
/*     */ 
/*     */     
/*     */     public AutowiredMethodElement(Method method, boolean required, PropertyDescriptor pd) {
/* 629 */       super(method, pd);
/* 630 */       this.required = required;
/*     */     }
/*     */     
/*     */     protected void inject(Object bean, String beanName, PropertyValues pvs) throws Throwable {
/*     */       Object[] arguments;
/* 635 */       if (checkPropertySkipping(pvs)) {
/*     */         return;
/*     */       }
/* 638 */       Method method = (Method)this.member;
/*     */       
/* 640 */       if (this.cached) {
/*     */         
/* 642 */         arguments = resolveCachedArguments(beanName);
/*     */       } else {
/*     */         
/* 645 */         Class<?>[] paramTypes = method.getParameterTypes();
/* 646 */         arguments = new Object[paramTypes.length];
/* 647 */         DependencyDescriptor[] descriptors = new DependencyDescriptor[paramTypes.length];
/* 648 */         Set<String> autowiredBeans = new LinkedHashSet<String>(paramTypes.length);
/* 649 */         TypeConverter typeConverter = AutowiredAnnotationBeanPostProcessor.this.beanFactory.getTypeConverter();
/* 650 */         for (int i = 0; i < arguments.length; i++) {
/* 651 */           MethodParameter methodParam = new MethodParameter(method, i);
/* 652 */           DependencyDescriptor currDesc = new DependencyDescriptor(methodParam, this.required);
/* 653 */           currDesc.setContainingClass(bean.getClass());
/* 654 */           descriptors[i] = currDesc;
/*     */           try {
/* 656 */             Object arg = AutowiredAnnotationBeanPostProcessor.this.beanFactory.resolveDependency(currDesc, beanName, autowiredBeans, typeConverter);
/* 657 */             if (arg == null && !this.required) {
/* 658 */               arguments = null;
/*     */               break;
/*     */             } 
/* 661 */             arguments[i] = arg;
/*     */           }
/* 663 */           catch (BeansException ex) {
/* 664 */             throw new UnsatisfiedDependencyException(null, beanName, new InjectionPoint(methodParam), ex);
/*     */           } 
/*     */         } 
/* 667 */         synchronized (this) {
/* 668 */           if (!this.cached) {
/* 669 */             if (arguments != null) {
/* 670 */               this.cachedMethodArguments = new Object[paramTypes.length];
/* 671 */               for (int j = 0; j < arguments.length; j++) {
/* 672 */                 this.cachedMethodArguments[j] = descriptors[j];
/*     */               }
/* 674 */               AutowiredAnnotationBeanPostProcessor.this.registerDependentBeans(beanName, autowiredBeans);
/* 675 */               if (autowiredBeans.size() == paramTypes.length) {
/* 676 */                 Iterator<String> it = autowiredBeans.iterator();
/* 677 */                 for (int k = 0; k < paramTypes.length; k++) {
/* 678 */                   String autowiredBeanName = it.next();
/* 679 */                   if (AutowiredAnnotationBeanPostProcessor.this.beanFactory.containsBean(autowiredBeanName) && 
/* 680 */                     AutowiredAnnotationBeanPostProcessor.this.beanFactory.isTypeMatch(autowiredBeanName, paramTypes[k])) {
/* 681 */                     this.cachedMethodArguments[k] = new AutowiredAnnotationBeanPostProcessor.ShortcutDependencyDescriptor(descriptors[k], autowiredBeanName, paramTypes[k]);
/*     */                   }
/*     */                 }
/*     */               
/*     */               }
/*     */             
/*     */             } else {
/*     */               
/* 689 */               this.cachedMethodArguments = null;
/*     */             } 
/* 691 */             this.cached = true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 695 */       if (arguments != null) {
/*     */         try {
/* 697 */           ReflectionUtils.makeAccessible(method);
/* 698 */           method.invoke(bean, arguments);
/*     */         }
/* 700 */         catch (InvocationTargetException ex) {
/* 701 */           throw ex.getTargetException();
/*     */         } 
/*     */       }
/*     */     }
/*     */     
/*     */     private Object[] resolveCachedArguments(String beanName) {
/* 707 */       if (this.cachedMethodArguments == null) {
/* 708 */         return null;
/*     */       }
/* 710 */       Object[] arguments = new Object[this.cachedMethodArguments.length];
/* 711 */       for (int i = 0; i < arguments.length; i++) {
/* 712 */         arguments[i] = AutowiredAnnotationBeanPostProcessor.this.resolvedCachedArgument(beanName, this.cachedMethodArguments[i]);
/*     */       }
/* 714 */       return arguments;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ShortcutDependencyDescriptor
/*     */     extends DependencyDescriptor
/*     */   {
/*     */     private final String shortcut;
/*     */ 
/*     */     
/*     */     private final Class<?> requiredType;
/*     */ 
/*     */     
/*     */     public ShortcutDependencyDescriptor(DependencyDescriptor original, String shortcut, Class<?> requiredType) {
/* 730 */       super(original);
/* 731 */       this.shortcut = shortcut;
/* 732 */       this.requiredType = requiredType;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object resolveShortcut(BeanFactory beanFactory) {
/* 737 */       return resolveCandidate(this.shortcut, this.requiredType, beanFactory);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\annotation\AutowiredAnnotationBeanPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */