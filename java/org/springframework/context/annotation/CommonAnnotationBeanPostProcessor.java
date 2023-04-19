/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.annotation.PreDestroy;
/*     */ import javax.annotation.Resource;
/*     */ import javax.ejb.EJB;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.Service;
/*     */ import javax.xml.ws.WebServiceClient;
/*     */ import javax.xml.ws.WebServiceRef;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.annotation.InitDestroyAnnotationBeanPostProcessor;
/*     */ import org.springframework.beans.factory.annotation.InjectionMetadata;
/*     */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*     */ import org.springframework.beans.factory.config.EmbeddedValueResolver;
/*     */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.jndi.support.SimpleJndiBeanFactory;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.StringValueResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommonAnnotationBeanPostProcessor
/*     */   extends InitDestroyAnnotationBeanPostProcessor
/*     */   implements InstantiationAwareBeanPostProcessor, BeanFactoryAware, Serializable
/*     */ {
/* 146 */   private static final Method lookupAttribute = ClassUtils.getMethodIfAvailable(Resource.class, "lookup", new Class[0]);
/*     */   
/* 148 */   private static Class<? extends Annotation> webServiceRefClass = null;
/*     */   
/* 150 */   private static Class<? extends Annotation> ejbRefClass = null;
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/* 156 */       Class<? extends Annotation> clazz = ClassUtils.forName("javax.xml.ws.WebServiceRef", CommonAnnotationBeanPostProcessor.class.getClassLoader());
/* 157 */       webServiceRefClass = clazz;
/*     */     }
/* 159 */     catch (ClassNotFoundException ex) {
/* 160 */       webServiceRefClass = null;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 165 */       Class<? extends Annotation> clazz = ClassUtils.forName("javax.ejb.EJB", CommonAnnotationBeanPostProcessor.class.getClassLoader());
/* 166 */       ejbRefClass = clazz;
/*     */     }
/* 168 */     catch (ClassNotFoundException ex) {
/* 169 */       ejbRefClass = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 174 */   private final Set<String> ignoredResourceTypes = new HashSet<String>(1);
/*     */   
/*     */   private boolean fallbackToDefaultTypeMatch = true;
/*     */   
/*     */   private boolean alwaysUseJndiLookup = false;
/*     */   
/* 180 */   private transient BeanFactory jndiFactory = (BeanFactory)new SimpleJndiBeanFactory();
/*     */   
/*     */   private transient BeanFactory resourceFactory;
/*     */   
/*     */   private transient BeanFactory beanFactory;
/*     */   
/*     */   private transient StringValueResolver embeddedValueResolver;
/*     */   
/* 188 */   private final transient Map<String, InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap<String, InjectionMetadata>(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CommonAnnotationBeanPostProcessor() {
/* 199 */     setOrder(2147483644);
/* 200 */     setInitAnnotationType(PostConstruct.class);
/* 201 */     setDestroyAnnotationType(PreDestroy.class);
/* 202 */     ignoreResourceType("javax.xml.ws.WebServiceContext");
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
/*     */   public void ignoreResourceType(String resourceType) {
/* 214 */     Assert.notNull(resourceType, "Ignored resource type must not be null");
/* 215 */     this.ignoredResourceTypes.add(resourceType);
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
/*     */   public void setFallbackToDefaultTypeMatch(boolean fallbackToDefaultTypeMatch) {
/* 229 */     this.fallbackToDefaultTypeMatch = fallbackToDefaultTypeMatch;
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
/*     */   public void setAlwaysUseJndiLookup(boolean alwaysUseJndiLookup) {
/* 243 */     this.alwaysUseJndiLookup = alwaysUseJndiLookup;
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
/*     */   public void setJndiFactory(BeanFactory jndiFactory) {
/* 258 */     Assert.notNull(jndiFactory, "BeanFactory must not be null");
/* 259 */     this.jndiFactory = jndiFactory;
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
/*     */   public void setResourceFactory(BeanFactory resourceFactory) {
/* 276 */     Assert.notNull(resourceFactory, "BeanFactory must not be null");
/* 277 */     this.resourceFactory = resourceFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 282 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/* 283 */     this.beanFactory = beanFactory;
/* 284 */     if (this.resourceFactory == null) {
/* 285 */       this.resourceFactory = beanFactory;
/*     */     }
/* 287 */     if (beanFactory instanceof ConfigurableBeanFactory) {
/* 288 */       this.embeddedValueResolver = (StringValueResolver)new EmbeddedValueResolver((ConfigurableBeanFactory)beanFactory);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
/* 295 */     super.postProcessMergedBeanDefinition(beanDefinition, beanType, beanName);
/* 296 */     if (beanType != null) {
/* 297 */       InjectionMetadata metadata = findResourceMetadata(beanName, beanType, (PropertyValues)null);
/* 298 */       metadata.checkConfigMembers(beanDefinition);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
/* 304 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
/* 309 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
/* 316 */     InjectionMetadata metadata = findResourceMetadata(beanName, bean.getClass(), pvs);
/*     */     try {
/* 318 */       metadata.inject(bean, beanName, pvs);
/*     */     }
/* 320 */     catch (Throwable ex) {
/* 321 */       throw new BeanCreationException(beanName, "Injection of resource dependencies failed", ex);
/*     */     } 
/* 323 */     return pvs;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private InjectionMetadata findResourceMetadata(String beanName, Class<?> clazz, PropertyValues pvs) {
/* 329 */     String cacheKey = StringUtils.hasLength(beanName) ? beanName : clazz.getName();
/*     */     
/* 331 */     InjectionMetadata metadata = this.injectionMetadataCache.get(cacheKey);
/* 332 */     if (InjectionMetadata.needsRefresh(metadata, clazz)) {
/* 333 */       synchronized (this.injectionMetadataCache) {
/* 334 */         metadata = this.injectionMetadataCache.get(cacheKey);
/* 335 */         if (InjectionMetadata.needsRefresh(metadata, clazz)) {
/* 336 */           if (metadata != null) {
/* 337 */             metadata.clear(pvs);
/*     */           }
/*     */           try {
/* 340 */             metadata = buildResourceMetadata(clazz);
/* 341 */             this.injectionMetadataCache.put(cacheKey, metadata);
/*     */           }
/* 343 */           catch (NoClassDefFoundError err) {
/* 344 */             throw new IllegalStateException("Failed to introspect bean class [" + clazz.getName() + "] for resource metadata: could not find class that it depends on", err);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 350 */     return metadata;
/*     */   }
/*     */   
/*     */   private InjectionMetadata buildResourceMetadata(final Class<?> clazz) {
/* 354 */     LinkedList<InjectionMetadata.InjectedElement> elements = new LinkedList<InjectionMetadata.InjectedElement>();
/* 355 */     Class<?> targetClass = clazz;
/*     */     
/*     */     do {
/* 358 */       final LinkedList<InjectionMetadata.InjectedElement> currElements = new LinkedList<InjectionMetadata.InjectedElement>();
/*     */ 
/*     */       
/* 361 */       ReflectionUtils.doWithLocalFields(targetClass, new ReflectionUtils.FieldCallback()
/*     */           {
/*     */             public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
/* 364 */               if (CommonAnnotationBeanPostProcessor.webServiceRefClass != null && field.isAnnotationPresent(CommonAnnotationBeanPostProcessor.webServiceRefClass)) {
/* 365 */                 if (Modifier.isStatic(field.getModifiers())) {
/* 366 */                   throw new IllegalStateException("@WebServiceRef annotation is not supported on static fields");
/*     */                 }
/* 368 */                 currElements.add(new CommonAnnotationBeanPostProcessor.WebServiceRefElement(field, field, null));
/*     */               }
/* 370 */               else if (CommonAnnotationBeanPostProcessor.ejbRefClass != null && field.isAnnotationPresent(CommonAnnotationBeanPostProcessor.ejbRefClass)) {
/* 371 */                 if (Modifier.isStatic(field.getModifiers())) {
/* 372 */                   throw new IllegalStateException("@EJB annotation is not supported on static fields");
/*     */                 }
/* 374 */                 currElements.add(new CommonAnnotationBeanPostProcessor.EjbRefElement(field, field, null));
/*     */               }
/* 376 */               else if (field.isAnnotationPresent((Class)Resource.class)) {
/* 377 */                 if (Modifier.isStatic(field.getModifiers())) {
/* 378 */                   throw new IllegalStateException("@Resource annotation is not supported on static fields");
/*     */                 }
/* 380 */                 if (!CommonAnnotationBeanPostProcessor.this.ignoredResourceTypes.contains(field.getType().getName())) {
/* 381 */                   currElements.add(new CommonAnnotationBeanPostProcessor.ResourceElement(field, field, null));
/*     */                 }
/*     */               } 
/*     */             }
/*     */           });
/*     */       
/* 387 */       ReflectionUtils.doWithLocalMethods(targetClass, new ReflectionUtils.MethodCallback()
/*     */           {
/*     */             public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
/* 390 */               Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
/* 391 */               if (!BridgeMethodResolver.isVisibilityBridgeMethodPair(method, bridgedMethod)) {
/*     */                 return;
/*     */               }
/* 394 */               if (method.equals(ClassUtils.getMostSpecificMethod(method, clazz))) {
/* 395 */                 if (CommonAnnotationBeanPostProcessor.webServiceRefClass != null && bridgedMethod.isAnnotationPresent(CommonAnnotationBeanPostProcessor.webServiceRefClass)) {
/* 396 */                   if (Modifier.isStatic(method.getModifiers())) {
/* 397 */                     throw new IllegalStateException("@WebServiceRef annotation is not supported on static methods");
/*     */                   }
/* 399 */                   if ((method.getParameterTypes()).length != 1) {
/* 400 */                     throw new IllegalStateException("@WebServiceRef annotation requires a single-arg method: " + method);
/*     */                   }
/* 402 */                   PropertyDescriptor pd = BeanUtils.findPropertyForMethod(bridgedMethod, clazz);
/* 403 */                   currElements.add(new CommonAnnotationBeanPostProcessor.WebServiceRefElement(method, bridgedMethod, pd));
/*     */                 }
/* 405 */                 else if (CommonAnnotationBeanPostProcessor.ejbRefClass != null && bridgedMethod.isAnnotationPresent(CommonAnnotationBeanPostProcessor.ejbRefClass)) {
/* 406 */                   if (Modifier.isStatic(method.getModifiers())) {
/* 407 */                     throw new IllegalStateException("@EJB annotation is not supported on static methods");
/*     */                   }
/* 409 */                   if ((method.getParameterTypes()).length != 1) {
/* 410 */                     throw new IllegalStateException("@EJB annotation requires a single-arg method: " + method);
/*     */                   }
/* 412 */                   PropertyDescriptor pd = BeanUtils.findPropertyForMethod(bridgedMethod, clazz);
/* 413 */                   currElements.add(new CommonAnnotationBeanPostProcessor.EjbRefElement(method, bridgedMethod, pd));
/*     */                 }
/* 415 */                 else if (bridgedMethod.isAnnotationPresent((Class)Resource.class)) {
/* 416 */                   if (Modifier.isStatic(method.getModifiers())) {
/* 417 */                     throw new IllegalStateException("@Resource annotation is not supported on static methods");
/*     */                   }
/* 419 */                   Class<?>[] paramTypes = method.getParameterTypes();
/* 420 */                   if (paramTypes.length != 1) {
/* 421 */                     throw new IllegalStateException("@Resource annotation requires a single-arg method: " + method);
/*     */                   }
/* 423 */                   if (!CommonAnnotationBeanPostProcessor.this.ignoredResourceTypes.contains(paramTypes[0].getName())) {
/* 424 */                     PropertyDescriptor pd = BeanUtils.findPropertyForMethod(bridgedMethod, clazz);
/* 425 */                     currElements.add(new CommonAnnotationBeanPostProcessor.ResourceElement(method, bridgedMethod, pd));
/*     */                   } 
/*     */                 } 
/*     */               }
/*     */             }
/*     */           });
/*     */       
/* 432 */       elements.addAll(0, currElements);
/* 433 */       targetClass = targetClass.getSuperclass();
/*     */     }
/* 435 */     while (targetClass != null && targetClass != Object.class);
/*     */     
/* 437 */     return new InjectionMetadata(clazz, elements);
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
/*     */   protected Object buildLazyResourceProxy(final LookupElement element, final String requestingBeanName) {
/* 451 */     TargetSource ts = new TargetSource()
/*     */       {
/*     */         public Class<?> getTargetClass() {
/* 454 */           return element.lookupType;
/*     */         }
/*     */         
/*     */         public boolean isStatic() {
/* 458 */           return false;
/*     */         }
/*     */         
/*     */         public Object getTarget() {
/* 462 */           return CommonAnnotationBeanPostProcessor.this.getResource(element, requestingBeanName);
/*     */         }
/*     */ 
/*     */         
/*     */         public void releaseTarget(Object target) {}
/*     */       };
/* 468 */     ProxyFactory pf = new ProxyFactory();
/* 469 */     pf.setTargetSource(ts);
/* 470 */     if (element.lookupType.isInterface()) {
/* 471 */       pf.addInterface(element.lookupType);
/*     */     }
/*     */     
/* 474 */     ClassLoader classLoader = (this.beanFactory instanceof ConfigurableBeanFactory) ? ((ConfigurableBeanFactory)this.beanFactory).getBeanClassLoader() : null;
/* 475 */     return pf.getProxy(classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getResource(LookupElement element, String requestingBeanName) throws BeansException {
/* 486 */     if (StringUtils.hasLength(element.mappedName)) {
/* 487 */       return this.jndiFactory.getBean(element.mappedName, element.lookupType);
/*     */     }
/* 489 */     if (this.alwaysUseJndiLookup) {
/* 490 */       return this.jndiFactory.getBean(element.name, element.lookupType);
/*     */     }
/* 492 */     if (this.resourceFactory == null) {
/* 493 */       throw new NoSuchBeanDefinitionException(element.lookupType, "No resource factory configured - specify the 'resourceFactory' property");
/*     */     }
/*     */     
/* 496 */     return autowireResource(this.resourceFactory, element, requestingBeanName);
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
/*     */   protected Object autowireResource(BeanFactory factory, LookupElement element, String requestingBeanName) throws BeansException {
/*     */     Object resource;
/*     */     Set<String> autowiredBeanNames;
/* 513 */     String name = element.name;
/*     */     
/* 515 */     if (this.fallbackToDefaultTypeMatch && element.isDefaultName && factory instanceof AutowireCapableBeanFactory && 
/* 516 */       !factory.containsBean(name)) {
/* 517 */       autowiredBeanNames = new LinkedHashSet<String>();
/* 518 */       resource = ((AutowireCapableBeanFactory)factory).resolveDependency(element
/* 519 */           .getDependencyDescriptor(), requestingBeanName, autowiredBeanNames, null);
/*     */     } else {
/*     */       
/* 522 */       resource = factory.getBean(name, element.lookupType);
/* 523 */       autowiredBeanNames = Collections.singleton(name);
/*     */     } 
/*     */     
/* 526 */     if (factory instanceof ConfigurableBeanFactory) {
/* 527 */       ConfigurableBeanFactory beanFactory = (ConfigurableBeanFactory)factory;
/* 528 */       for (String autowiredBeanName : autowiredBeanNames) {
/* 529 */         if (beanFactory.containsBean(autowiredBeanName)) {
/* 530 */           beanFactory.registerDependentBean(autowiredBeanName, requestingBeanName);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 535 */     return resource;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract class LookupElement
/*     */     extends InjectionMetadata.InjectedElement
/*     */   {
/*     */     protected String name;
/*     */ 
/*     */     
/*     */     protected boolean isDefaultName = false;
/*     */     
/*     */     protected Class<?> lookupType;
/*     */     
/*     */     protected String mappedName;
/*     */ 
/*     */     
/*     */     public LookupElement(Member member, PropertyDescriptor pd) {
/* 554 */       super(member, pd);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final String getName() {
/* 561 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final Class<?> getLookupType() {
/* 568 */       return this.lookupType;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final DependencyDescriptor getDependencyDescriptor() {
/* 575 */       if (this.isField) {
/* 576 */         return new CommonAnnotationBeanPostProcessor.LookupDependencyDescriptor((Field)this.member, this.lookupType);
/*     */       }
/*     */       
/* 579 */       return new CommonAnnotationBeanPostProcessor.LookupDependencyDescriptor((Method)this.member, this.lookupType);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ResourceElement
/*     */     extends LookupElement
/*     */   {
/*     */     private final boolean lazyLookup;
/*     */ 
/*     */ 
/*     */     
/*     */     public ResourceElement(Member member, AnnotatedElement ae, PropertyDescriptor pd) {
/* 594 */       super(member, pd);
/* 595 */       Resource resource = ae.<Resource>getAnnotation(Resource.class);
/* 596 */       String resourceName = resource.name();
/* 597 */       Class<?> resourceType = resource.type();
/* 598 */       this.isDefaultName = !StringUtils.hasLength(resourceName);
/* 599 */       if (this.isDefaultName) {
/* 600 */         resourceName = this.member.getName();
/* 601 */         if (this.member instanceof Method && resourceName.startsWith("set") && resourceName.length() > 3) {
/* 602 */           resourceName = Introspector.decapitalize(resourceName.substring(3));
/*     */         }
/*     */       }
/* 605 */       else if (CommonAnnotationBeanPostProcessor.this.embeddedValueResolver != null) {
/* 606 */         resourceName = CommonAnnotationBeanPostProcessor.this.embeddedValueResolver.resolveStringValue(resourceName);
/*     */       } 
/* 608 */       if (resourceType != null && Object.class != resourceType) {
/* 609 */         checkResourceType(resourceType);
/*     */       }
/*     */       else {
/*     */         
/* 613 */         resourceType = getResourceType();
/*     */       } 
/* 615 */       this.name = resourceName;
/* 616 */       this.lookupType = resourceType;
/*     */       
/* 618 */       String lookupValue = (CommonAnnotationBeanPostProcessor.lookupAttribute != null) ? (String)ReflectionUtils.invokeMethod(CommonAnnotationBeanPostProcessor.lookupAttribute, resource) : null;
/* 619 */       this.mappedName = StringUtils.hasLength(lookupValue) ? lookupValue : resource.mappedName();
/* 620 */       Lazy lazy = ae.<Lazy>getAnnotation(Lazy.class);
/* 621 */       this.lazyLookup = (lazy != null && lazy.value());
/*     */     }
/*     */ 
/*     */     
/*     */     protected Object getResourceToInject(Object target, String requestingBeanName) {
/* 626 */       return this.lazyLookup ? CommonAnnotationBeanPostProcessor.this.buildLazyResourceProxy(this, requestingBeanName) : CommonAnnotationBeanPostProcessor.this
/* 627 */         .getResource(this, requestingBeanName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class WebServiceRefElement
/*     */     extends LookupElement
/*     */   {
/*     */     private final Class<?> elementType;
/*     */ 
/*     */     
/*     */     private final String wsdlLocation;
/*     */ 
/*     */     
/*     */     public WebServiceRefElement(Member member, AnnotatedElement ae, PropertyDescriptor pd) {
/* 643 */       super(member, pd);
/* 644 */       WebServiceRef resource = ae.<WebServiceRef>getAnnotation(WebServiceRef.class);
/* 645 */       String resourceName = resource.name();
/* 646 */       Class<?> resourceType = resource.type();
/* 647 */       this.isDefaultName = !StringUtils.hasLength(resourceName);
/* 648 */       if (this.isDefaultName) {
/* 649 */         resourceName = this.member.getName();
/* 650 */         if (this.member instanceof Method && resourceName.startsWith("set") && resourceName.length() > 3) {
/* 651 */           resourceName = Introspector.decapitalize(resourceName.substring(3));
/*     */         }
/*     */       } 
/* 654 */       if (resourceType != null && Object.class != resourceType) {
/* 655 */         checkResourceType(resourceType);
/*     */       }
/*     */       else {
/*     */         
/* 659 */         resourceType = getResourceType();
/*     */       } 
/* 661 */       this.name = resourceName;
/* 662 */       this.elementType = resourceType;
/* 663 */       if (Service.class.isAssignableFrom(resourceType)) {
/* 664 */         this.lookupType = resourceType;
/*     */       } else {
/*     */         
/* 667 */         this.lookupType = resource.value();
/*     */       } 
/* 669 */       this.mappedName = resource.mappedName();
/* 670 */       this.wsdlLocation = resource.wsdlLocation();
/*     */     }
/*     */ 
/*     */     
/*     */     protected Object getResourceToInject(Object target, String requestingBeanName) {
/*     */       Service service;
/*     */       try {
/* 677 */         service = (Service)CommonAnnotationBeanPostProcessor.this.getResource(this, requestingBeanName);
/*     */       }
/* 679 */       catch (NoSuchBeanDefinitionException notFound) {
/*     */         
/* 681 */         if (Service.class == this.lookupType) {
/* 682 */           throw new IllegalStateException("No resource with name '" + this.name + "' found in context, and no specific JAX-WS Service subclass specified. The typical solution is to either specify a LocalJaxWsServiceFactoryBean with the given name or to specify the (generated) Service subclass as @WebServiceRef(...) value.");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 687 */         if (StringUtils.hasLength(this.wsdlLocation)) {
/*     */           try {
/* 689 */             Constructor<?> ctor = this.lookupType.getConstructor(new Class[] { URL.class, QName.class });
/* 690 */             WebServiceClient clientAnn = this.lookupType.<WebServiceClient>getAnnotation(WebServiceClient.class);
/* 691 */             if (clientAnn == null) {
/* 692 */               throw new IllegalStateException("JAX-WS Service class [" + this.lookupType.getName() + "] does not carry a WebServiceClient annotation");
/*     */             }
/*     */             
/* 695 */             service = (Service)BeanUtils.instantiateClass(ctor, new Object[] { new URL(this.wsdlLocation), new QName(clientAnn
/* 696 */                     .targetNamespace(), clientAnn.name()) });
/*     */           }
/* 698 */           catch (NoSuchMethodException ex) {
/* 699 */             throw new IllegalStateException("JAX-WS Service class [" + this.lookupType.getName() + "] does not have a (URL, QName) constructor. Cannot apply specified WSDL location [" + this.wsdlLocation + "].");
/*     */ 
/*     */           
/*     */           }
/* 703 */           catch (MalformedURLException ex) {
/* 704 */             throw new IllegalArgumentException("Specified WSDL location [" + this.wsdlLocation + "] isn't a valid URL");
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 709 */           service = (Service)BeanUtils.instantiateClass(this.lookupType);
/*     */         } 
/*     */       } 
/* 712 */       return service.getPort(this.elementType);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class EjbRefElement
/*     */     extends LookupElement
/*     */   {
/*     */     private final String beanName;
/*     */ 
/*     */ 
/*     */     
/*     */     public EjbRefElement(Member member, AnnotatedElement ae, PropertyDescriptor pd) {
/* 726 */       super(member, pd);
/* 727 */       EJB resource = ae.<EJB>getAnnotation(EJB.class);
/* 728 */       String resourceBeanName = resource.beanName();
/* 729 */       String resourceName = resource.name();
/* 730 */       this.isDefaultName = !StringUtils.hasLength(resourceName);
/* 731 */       if (this.isDefaultName) {
/* 732 */         resourceName = this.member.getName();
/* 733 */         if (this.member instanceof Method && resourceName.startsWith("set") && resourceName.length() > 3) {
/* 734 */           resourceName = Introspector.decapitalize(resourceName.substring(3));
/*     */         }
/*     */       } 
/* 737 */       Class<?> resourceType = resource.beanInterface();
/* 738 */       if (resourceType != null && Object.class != resourceType) {
/* 739 */         checkResourceType(resourceType);
/*     */       }
/*     */       else {
/*     */         
/* 743 */         resourceType = getResourceType();
/*     */       } 
/* 745 */       this.beanName = resourceBeanName;
/* 746 */       this.name = resourceName;
/* 747 */       this.lookupType = resourceType;
/* 748 */       this.mappedName = resource.mappedName();
/*     */     }
/*     */ 
/*     */     
/*     */     protected Object getResourceToInject(Object target, String requestingBeanName) {
/* 753 */       if (StringUtils.hasLength(this.beanName)) {
/* 754 */         if (CommonAnnotationBeanPostProcessor.this.beanFactory != null && CommonAnnotationBeanPostProcessor.this.beanFactory.containsBean(this.beanName)) {
/*     */           
/* 756 */           Object bean = CommonAnnotationBeanPostProcessor.this.beanFactory.getBean(this.beanName, this.lookupType);
/* 757 */           if (CommonAnnotationBeanPostProcessor.this.beanFactory instanceof ConfigurableBeanFactory) {
/* 758 */             ((ConfigurableBeanFactory)CommonAnnotationBeanPostProcessor.this.beanFactory).registerDependentBean(this.beanName, requestingBeanName);
/*     */           }
/* 760 */           return bean;
/*     */         } 
/* 762 */         if (this.isDefaultName && !StringUtils.hasLength(this.mappedName)) {
/* 763 */           throw new NoSuchBeanDefinitionException(this.beanName, "Cannot resolve 'beanName' in local BeanFactory. Consider specifying a general 'name' value instead.");
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 768 */       return CommonAnnotationBeanPostProcessor.this.getResource(this, requestingBeanName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class LookupDependencyDescriptor
/*     */     extends DependencyDescriptor
/*     */   {
/*     */     private final Class<?> lookupType;
/*     */ 
/*     */ 
/*     */     
/*     */     public LookupDependencyDescriptor(Field field, Class<?> lookupType) {
/* 782 */       super(field, true);
/* 783 */       this.lookupType = lookupType;
/*     */     }
/*     */     
/*     */     public LookupDependencyDescriptor(Method method, Class<?> lookupType) {
/* 787 */       super(new MethodParameter(method, 0), true);
/* 788 */       this.lookupType = lookupType;
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getDependencyType() {
/* 793 */       return this.lookupType;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\CommonAnnotationBeanPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */