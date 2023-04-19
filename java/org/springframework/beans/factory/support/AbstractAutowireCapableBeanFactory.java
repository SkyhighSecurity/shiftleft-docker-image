/*      */ package org.springframework.beans.factory.support;
/*      */ 
/*      */ import java.beans.PropertyDescriptor;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import org.springframework.beans.BeanUtils;
/*      */ import org.springframework.beans.BeanWrapper;
/*      */ import org.springframework.beans.BeanWrapperImpl;
/*      */ import org.springframework.beans.BeansException;
/*      */ import org.springframework.beans.MutablePropertyValues;
/*      */ import org.springframework.beans.PropertyAccessorUtils;
/*      */ import org.springframework.beans.PropertyValue;
/*      */ import org.springframework.beans.PropertyValues;
/*      */ import org.springframework.beans.TypeConverter;
/*      */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*      */ import org.springframework.beans.factory.BeanCreationException;
/*      */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*      */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*      */ import org.springframework.beans.factory.BeanFactory;
/*      */ import org.springframework.beans.factory.BeanFactoryAware;
/*      */ import org.springframework.beans.factory.BeanNameAware;
/*      */ import org.springframework.beans.factory.FactoryBean;
/*      */ import org.springframework.beans.factory.InitializingBean;
/*      */ import org.springframework.beans.factory.ObjectFactory;
/*      */ import org.springframework.beans.factory.UnsatisfiedDependencyException;
/*      */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*      */ import org.springframework.beans.factory.config.BeanDefinition;
/*      */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*      */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*      */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*      */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.TypedStringValue;
/*      */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*      */ import org.springframework.core.GenericTypeResolver;
/*      */ import org.springframework.core.MethodParameter;
/*      */ import org.springframework.core.ParameterNameDiscoverer;
/*      */ import org.springframework.core.PriorityOrdered;
/*      */ import org.springframework.core.ResolvableType;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.ReflectionUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractAutowireCapableBeanFactory
/*      */   extends AbstractBeanFactory
/*      */   implements AutowireCapableBeanFactory
/*      */ {
/*  120 */   private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();
/*      */ 
/*      */   
/*  123 */   private ParameterNameDiscoverer parameterNameDiscoverer = (ParameterNameDiscoverer)new DefaultParameterNameDiscoverer();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean allowCircularReferences = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean allowRawInjectionDespiteWrapping = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  138 */   private final Set<Class<?>> ignoredDependencyTypes = new HashSet<Class<?>>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  144 */   private final Set<Class<?>> ignoredDependencyInterfaces = new HashSet<Class<?>>();
/*      */ 
/*      */   
/*  147 */   private final ConcurrentMap<String, BeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<String, BeanWrapper>(16);
/*      */ 
/*      */ 
/*      */   
/*  151 */   private final ConcurrentMap<Class<?>, PropertyDescriptor[]> filteredPropertyDescriptorsCache = (ConcurrentMap)new ConcurrentHashMap<Class<?>, PropertyDescriptor>(256);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractAutowireCapableBeanFactory() {
/*  160 */     ignoreDependencyInterface(BeanNameAware.class);
/*  161 */     ignoreDependencyInterface(BeanFactoryAware.class);
/*  162 */     ignoreDependencyInterface(BeanClassLoaderAware.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractAutowireCapableBeanFactory(BeanFactory parentBeanFactory) {
/*  170 */     this();
/*  171 */     setParentBeanFactory(parentBeanFactory);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
/*  181 */     this.instantiationStrategy = instantiationStrategy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected InstantiationStrategy getInstantiationStrategy() {
/*  188 */     return this.instantiationStrategy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
/*  197 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ParameterNameDiscoverer getParameterNameDiscoverer() {
/*  205 */     return this.parameterNameDiscoverer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllowCircularReferences(boolean allowCircularReferences) {
/*  222 */     this.allowCircularReferences = allowCircularReferences;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllowRawInjectionDespiteWrapping(boolean allowRawInjectionDespiteWrapping) {
/*  240 */     this.allowRawInjectionDespiteWrapping = allowRawInjectionDespiteWrapping;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void ignoreDependencyType(Class<?> type) {
/*  248 */     this.ignoredDependencyTypes.add(type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void ignoreDependencyInterface(Class<?> ifc) {
/*  262 */     this.ignoredDependencyInterfaces.add(ifc);
/*      */   }
/*      */ 
/*      */   
/*      */   public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory) {
/*  267 */     super.copyConfigurationFrom(otherFactory);
/*  268 */     if (otherFactory instanceof AbstractAutowireCapableBeanFactory) {
/*  269 */       AbstractAutowireCapableBeanFactory otherAutowireFactory = (AbstractAutowireCapableBeanFactory)otherFactory;
/*      */       
/*  271 */       this.instantiationStrategy = otherAutowireFactory.instantiationStrategy;
/*  272 */       this.allowCircularReferences = otherAutowireFactory.allowCircularReferences;
/*  273 */       this.ignoredDependencyTypes.addAll(otherAutowireFactory.ignoredDependencyTypes);
/*  274 */       this.ignoredDependencyInterfaces.addAll(otherAutowireFactory.ignoredDependencyInterfaces);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T createBean(Class<T> beanClass) throws BeansException {
/*  287 */     RootBeanDefinition bd = new RootBeanDefinition(beanClass);
/*  288 */     bd.setScope("prototype");
/*  289 */     bd.allowCaching = ClassUtils.isCacheSafe(beanClass, getBeanClassLoader());
/*  290 */     return (T)createBean(beanClass.getName(), bd, (Object[])null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void autowireBean(Object existingBean) {
/*  296 */     RootBeanDefinition bd = new RootBeanDefinition(ClassUtils.getUserClass(existingBean));
/*  297 */     bd.setScope("prototype");
/*  298 */     bd.allowCaching = ClassUtils.isCacheSafe(bd.getBeanClass(), getBeanClassLoader());
/*  299 */     BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(existingBean);
/*  300 */     initBeanWrapper((BeanWrapper)beanWrapperImpl);
/*  301 */     populateBean(bd.getBeanClass().getName(), bd, (BeanWrapper)beanWrapperImpl);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object configureBean(Object existingBean, String beanName) throws BeansException {
/*  306 */     markBeanAsCreated(beanName);
/*  307 */     BeanDefinition mbd = getMergedBeanDefinition(beanName);
/*  308 */     RootBeanDefinition bd = null;
/*  309 */     if (mbd instanceof RootBeanDefinition) {
/*  310 */       RootBeanDefinition rbd = (RootBeanDefinition)mbd;
/*  311 */       bd = rbd.isPrototype() ? rbd : rbd.cloneBeanDefinition();
/*      */     } 
/*  313 */     if (!mbd.isPrototype()) {
/*  314 */       if (bd == null) {
/*  315 */         bd = new RootBeanDefinition(mbd);
/*      */       }
/*  317 */       bd.setScope("prototype");
/*  318 */       bd.allowCaching = ClassUtils.isCacheSafe(ClassUtils.getUserClass(existingBean), getBeanClassLoader());
/*      */     } 
/*  320 */     BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(existingBean);
/*  321 */     initBeanWrapper((BeanWrapper)beanWrapperImpl);
/*  322 */     populateBean(beanName, bd, (BeanWrapper)beanWrapperImpl);
/*  323 */     return initializeBean(beanName, existingBean, bd);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object resolveDependency(DependencyDescriptor descriptor, String requestingBeanName) throws BeansException {
/*  328 */     return resolveDependency(descriptor, requestingBeanName, null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException {
/*  339 */     RootBeanDefinition bd = new RootBeanDefinition(beanClass, autowireMode, dependencyCheck);
/*  340 */     bd.setScope("prototype");
/*  341 */     return createBean(beanClass.getName(), bd, (Object[])null);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException {
/*      */     Object bean;
/*  347 */     final RootBeanDefinition bd = new RootBeanDefinition(beanClass, autowireMode, dependencyCheck);
/*  348 */     bd.setScope("prototype");
/*  349 */     if (bd.getResolvedAutowireMode() == 3) {
/*  350 */       return autowireConstructor(beanClass.getName(), bd, (Constructor<?>[])null, (Object[])null).getWrappedInstance();
/*      */     }
/*      */ 
/*      */     
/*  354 */     final AbstractAutowireCapableBeanFactory parent = this;
/*  355 */     if (System.getSecurityManager() != null) {
/*  356 */       bean = AccessController.doPrivileged(new PrivilegedAction()
/*      */           {
/*      */             public Object run() {
/*  359 */               return AbstractAutowireCapableBeanFactory.this.getInstantiationStrategy().instantiate(bd, null, parent);
/*      */             }
/*  361 */           }getAccessControlContext());
/*      */     } else {
/*      */       
/*  364 */       bean = getInstantiationStrategy().instantiate(bd, null, (BeanFactory)abstractAutowireCapableBeanFactory);
/*      */     } 
/*  366 */     populateBean(beanClass.getName(), bd, (BeanWrapper)new BeanWrapperImpl(bean));
/*  367 */     return bean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck) throws BeansException {
/*  375 */     if (autowireMode == 3) {
/*  376 */       throw new IllegalArgumentException("AUTOWIRE_CONSTRUCTOR not supported for existing bean instance");
/*      */     }
/*      */ 
/*      */     
/*  380 */     RootBeanDefinition bd = new RootBeanDefinition(ClassUtils.getUserClass(existingBean), autowireMode, dependencyCheck);
/*  381 */     bd.setScope("prototype");
/*  382 */     BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(existingBean);
/*  383 */     initBeanWrapper((BeanWrapper)beanWrapperImpl);
/*  384 */     populateBean(bd.getBeanClass().getName(), bd, (BeanWrapper)beanWrapperImpl);
/*      */   }
/*      */ 
/*      */   
/*      */   public void applyBeanPropertyValues(Object existingBean, String beanName) throws BeansException {
/*  389 */     markBeanAsCreated(beanName);
/*  390 */     BeanDefinition bd = getMergedBeanDefinition(beanName);
/*  391 */     BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(existingBean);
/*  392 */     initBeanWrapper((BeanWrapper)beanWrapperImpl);
/*  393 */     applyPropertyValues(beanName, bd, (BeanWrapper)beanWrapperImpl, (PropertyValues)bd.getPropertyValues());
/*      */   }
/*      */ 
/*      */   
/*      */   public Object initializeBean(Object existingBean, String beanName) {
/*  398 */     return initializeBean(beanName, existingBean, (RootBeanDefinition)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
/*  405 */     Object result = existingBean;
/*  406 */     for (BeanPostProcessor processor : getBeanPostProcessors()) {
/*  407 */       result = processor.postProcessBeforeInitialization(result, beanName);
/*  408 */       if (result == null) {
/*  409 */         return result;
/*      */       }
/*      */     } 
/*  412 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
/*  419 */     Object result = existingBean;
/*  420 */     for (BeanPostProcessor processor : getBeanPostProcessors()) {
/*  421 */       result = processor.postProcessAfterInitialization(result, beanName);
/*  422 */       if (result == null) {
/*  423 */         return result;
/*      */       }
/*      */     } 
/*  426 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void destroyBean(Object existingBean) {
/*  431 */     (new DisposableBeanAdapter(existingBean, getBeanPostProcessors(), getAccessControlContext())).destroy();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object createBean(String beanName, RootBeanDefinition mbd, Object[] args) throws BeanCreationException {
/*  446 */     if (this.logger.isDebugEnabled()) {
/*  447 */       this.logger.debug("Creating instance of bean '" + beanName + "'");
/*      */     }
/*  449 */     RootBeanDefinition mbdToUse = mbd;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  454 */     Class<?> resolvedClass = resolveBeanClass(mbd, beanName, new Class[0]);
/*  455 */     if (resolvedClass != null && !mbd.hasBeanClass() && mbd.getBeanClassName() != null) {
/*  456 */       mbdToUse = new RootBeanDefinition(mbd);
/*  457 */       mbdToUse.setBeanClass(resolvedClass);
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/*  462 */       mbdToUse.prepareMethodOverrides();
/*      */     }
/*  464 */     catch (BeanDefinitionValidationException ex) {
/*  465 */       throw new BeanDefinitionStoreException(mbdToUse.getResourceDescription(), beanName, "Validation of method overrides failed", ex);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  471 */       Object bean = resolveBeforeInstantiation(beanName, mbdToUse);
/*  472 */       if (bean != null) {
/*  473 */         return bean;
/*      */       }
/*      */     }
/*  476 */     catch (Throwable ex) {
/*  477 */       throw new BeanCreationException(mbdToUse.getResourceDescription(), beanName, "BeanPostProcessor before instantiation of bean failed", ex);
/*      */     } 
/*      */ 
/*      */     
/*  481 */     Object beanInstance = doCreateBean(beanName, mbdToUse, args);
/*  482 */     if (this.logger.isDebugEnabled()) {
/*  483 */       this.logger.debug("Finished creating instance of bean '" + beanName + "'");
/*      */     }
/*  485 */     return beanInstance;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, Object[] args) throws BeanCreationException {
/*  506 */     BeanWrapper instanceWrapper = null;
/*  507 */     if (mbd.isSingleton()) {
/*  508 */       instanceWrapper = this.factoryBeanInstanceCache.remove(beanName);
/*      */     }
/*  510 */     if (instanceWrapper == null) {
/*  511 */       instanceWrapper = createBeanInstance(beanName, mbd, args);
/*      */     }
/*  513 */     final Object bean = (instanceWrapper != null) ? instanceWrapper.getWrappedInstance() : null;
/*  514 */     Class<?> beanType = (instanceWrapper != null) ? instanceWrapper.getWrappedClass() : null;
/*  515 */     mbd.resolvedTargetType = beanType;
/*      */ 
/*      */     
/*  518 */     synchronized (mbd.postProcessingLock) {
/*  519 */       if (!mbd.postProcessed) {
/*      */         try {
/*  521 */           applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName);
/*      */         }
/*  523 */         catch (Throwable ex) {
/*  524 */           throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Post-processing of merged bean definition failed", ex);
/*      */         } 
/*      */         
/*  527 */         mbd.postProcessed = true;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  534 */     boolean earlySingletonExposure = (mbd.isSingleton() && this.allowCircularReferences && isSingletonCurrentlyInCreation(beanName));
/*  535 */     if (earlySingletonExposure) {
/*  536 */       if (this.logger.isDebugEnabled()) {
/*  537 */         this.logger.debug("Eagerly caching bean '" + beanName + "' to allow for resolving potential circular references");
/*      */       }
/*      */       
/*  540 */       addSingletonFactory(beanName, new ObjectFactory<Object>()
/*      */           {
/*      */             public Object getObject() throws BeansException {
/*  543 */               return AbstractAutowireCapableBeanFactory.this.getEarlyBeanReference(beanName, mbd, bean);
/*      */             }
/*      */           });
/*      */     } 
/*      */ 
/*      */     
/*  549 */     Object exposedObject = bean;
/*      */     try {
/*  551 */       populateBean(beanName, mbd, instanceWrapper);
/*  552 */       if (exposedObject != null) {
/*  553 */         exposedObject = initializeBean(beanName, exposedObject, mbd);
/*      */       }
/*      */     }
/*  556 */     catch (Throwable ex) {
/*  557 */       if (ex instanceof BeanCreationException && beanName.equals(((BeanCreationException)ex).getBeanName())) {
/*  558 */         throw (BeanCreationException)ex;
/*      */       }
/*      */       
/*  561 */       throw new BeanCreationException(mbd
/*  562 */           .getResourceDescription(), beanName, "Initialization of bean failed", ex);
/*      */     } 
/*      */ 
/*      */     
/*  566 */     if (earlySingletonExposure) {
/*  567 */       Object earlySingletonReference = getSingleton(beanName, false);
/*  568 */       if (earlySingletonReference != null) {
/*  569 */         if (exposedObject == bean) {
/*  570 */           exposedObject = earlySingletonReference;
/*      */         }
/*  572 */         else if (!this.allowRawInjectionDespiteWrapping && hasDependentBean(beanName)) {
/*  573 */           String[] dependentBeans = getDependentBeans(beanName);
/*  574 */           Set<String> actualDependentBeans = new LinkedHashSet<String>(dependentBeans.length);
/*  575 */           for (String dependentBean : dependentBeans) {
/*  576 */             if (!removeSingletonIfCreatedForTypeCheckOnly(dependentBean)) {
/*  577 */               actualDependentBeans.add(dependentBean);
/*      */             }
/*      */           } 
/*  580 */           if (!actualDependentBeans.isEmpty()) {
/*  581 */             throw new BeanCurrentlyInCreationException(beanName, "Bean with name '" + beanName + "' has been injected into other beans [" + 
/*      */                 
/*  583 */                 StringUtils.collectionToCommaDelimitedString(actualDependentBeans) + "] in its raw version as part of a circular reference, but has eventually been wrapped. This means that said other beans do not use the final version of the bean. This is often the result of over-eager type matching - consider using 'getBeanNamesOfType' with the 'allowEagerInit' flag turned off, for example.");
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  595 */       registerDisposableBeanIfNecessary(beanName, bean, mbd);
/*      */     }
/*  597 */     catch (BeanDefinitionValidationException ex) {
/*  598 */       throw new BeanCreationException(mbd
/*  599 */           .getResourceDescription(), beanName, "Invalid destruction signature", ex);
/*      */     } 
/*      */     
/*  602 */     return exposedObject;
/*      */   }
/*      */ 
/*      */   
/*      */   protected Class<?> predictBeanType(String beanName, RootBeanDefinition mbd, Class<?>... typesToMatch) {
/*  607 */     Class<?> targetType = determineTargetType(beanName, mbd, typesToMatch);
/*      */ 
/*      */ 
/*      */     
/*  611 */     if (targetType != null && !mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
/*  612 */       for (BeanPostProcessor bp : getBeanPostProcessors()) {
/*  613 */         if (bp instanceof SmartInstantiationAwareBeanPostProcessor) {
/*  614 */           SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor)bp;
/*  615 */           Class<?> predicted = ibp.predictBeanType(targetType, beanName);
/*  616 */           if (predicted != null && (typesToMatch.length != 1 || FactoryBean.class != typesToMatch[0] || FactoryBean.class
/*  617 */             .isAssignableFrom(predicted))) {
/*  618 */             return predicted;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*  623 */     return targetType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Class<?> determineTargetType(String beanName, RootBeanDefinition mbd, Class<?>... typesToMatch) {
/*  635 */     Class<?> targetType = mbd.getTargetType();
/*  636 */     if (targetType == null) {
/*      */ 
/*      */       
/*  639 */       targetType = (mbd.getFactoryMethodName() != null) ? getTypeForFactoryMethod(beanName, mbd, typesToMatch) : resolveBeanClass(mbd, beanName, typesToMatch);
/*  640 */       if (ObjectUtils.isEmpty((Object[])typesToMatch) || getTempClassLoader() == null) {
/*  641 */         mbd.resolvedTargetType = targetType;
/*      */       }
/*      */     } 
/*  644 */     return targetType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Class<?> getTypeForFactoryMethod(String beanName, RootBeanDefinition mbd, Class<?>... typesToMatch) {
/*  662 */     ResolvableType cachedReturnType = mbd.factoryMethodReturnType;
/*  663 */     if (cachedReturnType != null) {
/*  664 */       return cachedReturnType.resolve();
/*      */     }
/*      */ 
/*      */     
/*  668 */     boolean isStatic = true;
/*      */     
/*  670 */     String factoryBeanName = mbd.getFactoryBeanName();
/*  671 */     if (factoryBeanName != null) {
/*  672 */       if (factoryBeanName.equals(beanName)) {
/*  673 */         throw new BeanDefinitionStoreException(mbd.getResourceDescription(), beanName, "factory-bean reference points back to the same bean definition");
/*      */       }
/*      */ 
/*      */       
/*  677 */       factoryClass = getType(factoryBeanName);
/*  678 */       isStatic = false;
/*      */     }
/*      */     else {
/*      */       
/*  682 */       factoryClass = resolveBeanClass(mbd, beanName, typesToMatch);
/*      */     } 
/*      */     
/*  685 */     if (factoryClass == null) {
/*  686 */       return null;
/*      */     }
/*  688 */     Class<?> factoryClass = ClassUtils.getUserClass(factoryClass);
/*      */ 
/*      */ 
/*      */     
/*  692 */     Class<?> commonType = null;
/*  693 */     Method uniqueCandidate = null;
/*  694 */     int minNrOfArgs = mbd.getConstructorArgumentValues().getArgumentCount();
/*  695 */     Method[] candidates = ReflectionUtils.getUniqueDeclaredMethods(factoryClass);
/*  696 */     for (Method candidate : candidates) {
/*  697 */       if (Modifier.isStatic(candidate.getModifiers()) == isStatic && mbd.isFactoryMethod(candidate) && (candidate
/*  698 */         .getParameterTypes()).length >= minNrOfArgs)
/*      */       {
/*  700 */         if ((candidate.getTypeParameters()).length > 0) {
/*      */           
/*      */           try {
/*  703 */             Class<?>[] paramTypes = candidate.getParameterTypes();
/*  704 */             String[] paramNames = null;
/*  705 */             ParameterNameDiscoverer pnd = getParameterNameDiscoverer();
/*  706 */             if (pnd != null) {
/*  707 */               paramNames = pnd.getParameterNames(candidate);
/*      */             }
/*  709 */             ConstructorArgumentValues cav = mbd.getConstructorArgumentValues();
/*  710 */             Set<ConstructorArgumentValues.ValueHolder> usedValueHolders = new HashSet<ConstructorArgumentValues.ValueHolder>(paramTypes.length);
/*      */             
/*  712 */             Object[] args = new Object[paramTypes.length];
/*  713 */             for (int i = 0; i < args.length; i++) {
/*  714 */               ConstructorArgumentValues.ValueHolder valueHolder = cav.getArgumentValue(i, paramTypes[i], (paramNames != null) ? paramNames[i] : null, usedValueHolders);
/*      */               
/*  716 */               if (valueHolder == null) {
/*  717 */                 valueHolder = cav.getGenericArgumentValue(null, null, usedValueHolders);
/*      */               }
/*  719 */               if (valueHolder != null) {
/*  720 */                 args[i] = valueHolder.getValue();
/*  721 */                 usedValueHolders.add(valueHolder);
/*      */               } 
/*      */             } 
/*  724 */             Class<?> returnType = AutowireUtils.resolveReturnTypeForFactoryMethod(candidate, args, 
/*  725 */                 getBeanClassLoader());
/*  726 */             if (returnType != null) {
/*  727 */               uniqueCandidate = (commonType == null && returnType == candidate.getReturnType()) ? candidate : null;
/*      */               
/*  729 */               commonType = ClassUtils.determineCommonAncestor(returnType, commonType);
/*  730 */               if (commonType == null)
/*      */               {
/*  732 */                 return null;
/*      */               }
/*      */             }
/*      */           
/*  736 */           } catch (Throwable ex) {
/*  737 */             if (this.logger.isDebugEnabled()) {
/*  738 */               this.logger.debug("Failed to resolve generic return type for factory method: " + ex);
/*      */             }
/*      */           } 
/*      */         } else {
/*      */           
/*  743 */           uniqueCandidate = (commonType == null) ? candidate : null;
/*  744 */           commonType = ClassUtils.determineCommonAncestor(candidate.getReturnType(), commonType);
/*  745 */           if (commonType == null)
/*      */           {
/*  747 */             return null;
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/*  753 */     if (commonType == null) {
/*  754 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  759 */     cachedReturnType = (uniqueCandidate != null) ? ResolvableType.forMethodReturnType(uniqueCandidate) : ResolvableType.forClass(commonType);
/*  760 */     mbd.factoryMethodReturnType = cachedReturnType;
/*  761 */     return cachedReturnType.resolve();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Class<?> getTypeForFactoryBean(String beanName, RootBeanDefinition mbd) {
/*  777 */     String factoryBeanName = mbd.getFactoryBeanName();
/*  778 */     String factoryMethodName = mbd.getFactoryMethodName();
/*      */     
/*  780 */     if (factoryBeanName != null) {
/*  781 */       if (factoryMethodName != null) {
/*      */ 
/*      */         
/*  784 */         BeanDefinition fbDef = getBeanDefinition(factoryBeanName);
/*  785 */         if (fbDef instanceof AbstractBeanDefinition) {
/*  786 */           AbstractBeanDefinition afbDef = (AbstractBeanDefinition)fbDef;
/*  787 */           if (afbDef.hasBeanClass()) {
/*  788 */             Class<?> result = getTypeForFactoryBeanFromMethod(afbDef.getBeanClass(), factoryMethodName);
/*  789 */             if (result != null) {
/*  790 */               return result;
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  798 */       if (!isBeanEligibleForMetadataCaching(factoryBeanName)) {
/*  799 */         return null;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  806 */     FactoryBean<?> fb = mbd.isSingleton() ? getSingletonFactoryBeanForTypeCheck(beanName, mbd) : getNonSingletonFactoryBeanForTypeCheck(beanName, mbd);
/*      */     
/*  808 */     if (fb != null) {
/*      */       
/*  810 */       Class<?> result = getTypeForFactoryBean(fb);
/*  811 */       if (result != null) {
/*  812 */         return result;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  817 */       return super.getTypeForFactoryBean(beanName, mbd);
/*      */     } 
/*      */ 
/*      */     
/*  821 */     if (factoryBeanName == null && mbd.hasBeanClass()) {
/*      */ 
/*      */       
/*  824 */       if (factoryMethodName != null) {
/*  825 */         return getTypeForFactoryBeanFromMethod(mbd.getBeanClass(), factoryMethodName);
/*      */       }
/*      */       
/*  828 */       return GenericTypeResolver.resolveTypeArgument(mbd.getBeanClass(), FactoryBean.class);
/*      */     } 
/*      */ 
/*      */     
/*  832 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Class<?> getTypeForFactoryBeanFromMethod(Class<?> beanClass, final String factoryMethodName) {
/*      */     class Holder
/*      */     {
/*  843 */       Class<?> value = null; };
/*  844 */     final Holder objectType = new Holder();
/*      */ 
/*      */     
/*  847 */     Class<?> fbClass = ClassUtils.getUserClass(beanClass);
/*      */ 
/*      */ 
/*      */     
/*  851 */     ReflectionUtils.doWithMethods(fbClass, new ReflectionUtils.MethodCallback()
/*      */         {
/*      */           public void doWith(Method method)
/*      */           {
/*  855 */             if (method.getName().equals(factoryMethodName) && FactoryBean.class
/*  856 */               .isAssignableFrom(method.getReturnType())) {
/*  857 */               Class<?> currentType = GenericTypeResolver.resolveReturnTypeArgument(method, FactoryBean.class);
/*      */               
/*  859 */               if (currentType != null) {
/*  860 */                 objectType.value = ClassUtils.determineCommonAncestor(currentType, objectType.value);
/*      */               }
/*      */             } 
/*      */           }
/*      */         });
/*      */     
/*  866 */     return (objectType.value != null && Object.class != objectType.value) ? objectType.value : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object getEarlyBeanReference(String beanName, RootBeanDefinition mbd, Object bean) {
/*  878 */     Object exposedObject = bean;
/*  879 */     if (bean != null && !mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
/*  880 */       for (BeanPostProcessor bp : getBeanPostProcessors()) {
/*  881 */         if (bp instanceof SmartInstantiationAwareBeanPostProcessor) {
/*  882 */           SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor)bp;
/*  883 */           exposedObject = ibp.getEarlyBeanReference(exposedObject, beanName);
/*  884 */           if (exposedObject == null) {
/*  885 */             return null;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*  890 */     return exposedObject;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private FactoryBean<?> getSingletonFactoryBeanForTypeCheck(String beanName, RootBeanDefinition mbd) {
/*  907 */     synchronized (getSingletonMutex()) {
/*  908 */       Object instance; BeanWrapper bw = this.factoryBeanInstanceCache.get(beanName);
/*  909 */       if (bw != null) {
/*  910 */         return (FactoryBean)bw.getWrappedInstance();
/*      */       }
/*  912 */       Object beanInstance = getSingleton(beanName, false);
/*  913 */       if (beanInstance instanceof FactoryBean) {
/*  914 */         return (FactoryBean)beanInstance;
/*      */       }
/*  916 */       if (isSingletonCurrentlyInCreation(beanName) || (mbd
/*  917 */         .getFactoryBeanName() != null && isSingletonCurrentlyInCreation(mbd.getFactoryBeanName()))) {
/*  918 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  924 */         beforeSingletonCreation(beanName);
/*      */         
/*  926 */         instance = resolveBeforeInstantiation(beanName, mbd);
/*  927 */         if (instance == null) {
/*  928 */           bw = createBeanInstance(beanName, mbd, (Object[])null);
/*  929 */           instance = bw.getWrappedInstance();
/*      */         }
/*      */       
/*      */       } finally {
/*      */         
/*  934 */         afterSingletonCreation(beanName);
/*      */       } 
/*      */       
/*  937 */       FactoryBean<?> fb = getFactoryBean(beanName, instance);
/*  938 */       if (bw != null) {
/*  939 */         this.factoryBeanInstanceCache.put(beanName, bw);
/*      */       }
/*  941 */       return fb;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private FactoryBean<?> getNonSingletonFactoryBeanForTypeCheck(String beanName, RootBeanDefinition mbd) {
/*  954 */     if (isPrototypeCurrentlyInCreation(beanName)) {
/*  955 */       return null;
/*      */     }
/*      */     
/*  958 */     Object instance = null;
/*      */     
/*      */     try {
/*  961 */       beforePrototypeCreation(beanName);
/*      */       
/*  963 */       instance = resolveBeforeInstantiation(beanName, mbd);
/*  964 */       if (instance == null) {
/*  965 */         BeanWrapper bw = createBeanInstance(beanName, mbd, (Object[])null);
/*  966 */         instance = bw.getWrappedInstance();
/*      */       }
/*      */     
/*  969 */     } catch (BeanCreationException ex) {
/*      */       
/*  971 */       if (this.logger.isDebugEnabled()) {
/*  972 */         this.logger.debug("Bean creation exception on non-singleton FactoryBean type check: " + ex);
/*      */       }
/*  974 */       onSuppressedException((Exception)ex);
/*  975 */       return null;
/*      */     }
/*      */     finally {
/*      */       
/*  979 */       afterPrototypeCreation(beanName);
/*      */     } 
/*      */     
/*  982 */     return getFactoryBean(beanName, instance);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void applyMergedBeanDefinitionPostProcessors(RootBeanDefinition mbd, Class<?> beanType, String beanName) {
/*  994 */     for (BeanPostProcessor bp : getBeanPostProcessors()) {
/*  995 */       if (bp instanceof MergedBeanDefinitionPostProcessor) {
/*  996 */         MergedBeanDefinitionPostProcessor bdp = (MergedBeanDefinitionPostProcessor)bp;
/*  997 */         bdp.postProcessMergedBeanDefinition(mbd, beanType, beanName);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object resolveBeforeInstantiation(String beanName, RootBeanDefinition mbd) {
/* 1010 */     Object bean = null;
/* 1011 */     if (!Boolean.FALSE.equals(mbd.beforeInstantiationResolved)) {
/*      */       
/* 1013 */       if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
/* 1014 */         Class<?> targetType = determineTargetType(beanName, mbd, new Class[0]);
/* 1015 */         if (targetType != null) {
/* 1016 */           bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
/* 1017 */           if (bean != null) {
/* 1018 */             bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
/*      */           }
/*      */         } 
/*      */       } 
/* 1022 */       mbd.beforeInstantiationResolved = Boolean.valueOf((bean != null));
/*      */     } 
/* 1024 */     return bean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) {
/* 1039 */     for (BeanPostProcessor bp : getBeanPostProcessors()) {
/* 1040 */       if (bp instanceof InstantiationAwareBeanPostProcessor) {
/* 1041 */         InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor)bp;
/* 1042 */         Object result = ibp.postProcessBeforeInstantiation(beanClass, beanName);
/* 1043 */         if (result != null) {
/* 1044 */           return result;
/*      */         }
/*      */       } 
/*      */     } 
/* 1048 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, Object[] args) {
/* 1064 */     Class<?> beanClass = resolveBeanClass(mbd, beanName, new Class[0]);
/*      */     
/* 1066 */     if (beanClass != null && !Modifier.isPublic(beanClass.getModifiers()) && !mbd.isNonPublicAccessAllowed()) {
/* 1067 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Bean class isn't public, and non-public access not allowed: " + beanClass
/* 1068 */           .getName());
/*      */     }
/*      */     
/* 1071 */     if (mbd.getFactoryMethodName() != null) {
/* 1072 */       return instantiateUsingFactoryMethod(beanName, mbd, args);
/*      */     }
/*      */ 
/*      */     
/* 1076 */     boolean resolved = false;
/* 1077 */     boolean autowireNecessary = false;
/* 1078 */     if (args == null) {
/* 1079 */       synchronized (mbd.constructorArgumentLock) {
/* 1080 */         if (mbd.resolvedConstructorOrFactoryMethod != null) {
/* 1081 */           resolved = true;
/* 1082 */           autowireNecessary = mbd.constructorArgumentsResolved;
/*      */         } 
/*      */       } 
/*      */     }
/* 1086 */     if (resolved) {
/* 1087 */       if (autowireNecessary) {
/* 1088 */         return autowireConstructor(beanName, mbd, (Constructor<?>[])null, (Object[])null);
/*      */       }
/*      */       
/* 1091 */       return instantiateBean(beanName, mbd);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1096 */     Constructor[] arrayOfConstructor = (Constructor[])determineConstructorsFromBeanPostProcessors(beanClass, beanName);
/* 1097 */     if (arrayOfConstructor != null || mbd
/* 1098 */       .getResolvedAutowireMode() == 3 || mbd
/* 1099 */       .hasConstructorArgumentValues() || !ObjectUtils.isEmpty(args)) {
/* 1100 */       return autowireConstructor(beanName, mbd, (Constructor<?>[])arrayOfConstructor, args);
/*      */     }
/*      */ 
/*      */     
/* 1104 */     return instantiateBean(beanName, mbd);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Constructor<?>[] determineConstructorsFromBeanPostProcessors(Class<?> beanClass, String beanName) throws BeansException {
/* 1119 */     if (beanClass != null && hasInstantiationAwareBeanPostProcessors()) {
/* 1120 */       for (BeanPostProcessor bp : getBeanPostProcessors()) {
/* 1121 */         if (bp instanceof SmartInstantiationAwareBeanPostProcessor) {
/* 1122 */           SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor)bp;
/* 1123 */           Constructor[] arrayOfConstructor = ibp.determineCandidateConstructors(beanClass, beanName);
/* 1124 */           if (arrayOfConstructor != null) {
/* 1125 */             return (Constructor<?>[])arrayOfConstructor;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/* 1130 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected BeanWrapper instantiateBean(final String beanName, final RootBeanDefinition mbd) {
/*      */     try {
/*      */       Object beanInstance;
/* 1142 */       final AbstractAutowireCapableBeanFactory parent = this;
/* 1143 */       if (System.getSecurityManager() != null) {
/* 1144 */         beanInstance = AccessController.doPrivileged(new PrivilegedAction()
/*      */             {
/*      */               public Object run() {
/* 1147 */                 return AbstractAutowireCapableBeanFactory.this.getInstantiationStrategy().instantiate(mbd, beanName, parent);
/*      */               }
/* 1149 */             }getAccessControlContext());
/*      */       } else {
/*      */         
/* 1152 */         beanInstance = getInstantiationStrategy().instantiate(mbd, beanName, (BeanFactory)abstractAutowireCapableBeanFactory);
/*      */       } 
/* 1154 */       BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(beanInstance);
/* 1155 */       initBeanWrapper((BeanWrapper)beanWrapperImpl);
/* 1156 */       return (BeanWrapper)beanWrapperImpl;
/*      */     }
/* 1158 */     catch (Throwable ex) {
/* 1159 */       throw new BeanCreationException(mbd
/* 1160 */           .getResourceDescription(), beanName, "Instantiation of bean failed", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected BeanWrapper instantiateUsingFactoryMethod(String beanName, RootBeanDefinition mbd, Object[] explicitArgs) {
/* 1178 */     return (new ConstructorResolver(this)).instantiateUsingFactoryMethod(beanName, mbd, explicitArgs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected BeanWrapper autowireConstructor(String beanName, RootBeanDefinition mbd, Constructor<?>[] ctors, Object[] explicitArgs) {
/* 1198 */     return (new ConstructorResolver(this)).autowireConstructor(beanName, mbd, ctors, explicitArgs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void populateBean(String beanName, RootBeanDefinition mbd, BeanWrapper bw) {
/*      */     PropertyValues propertyValues;
/* 1209 */     MutablePropertyValues mutablePropertyValues = mbd.getPropertyValues();
/*      */     
/* 1211 */     if (bw == null) {
/* 1212 */       if (!mutablePropertyValues.isEmpty()) {
/* 1213 */         throw new BeanCreationException(mbd
/* 1214 */             .getResourceDescription(), beanName, "Cannot apply property values to null instance");
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1225 */     boolean continueWithPropertyPopulation = true;
/*      */     
/* 1227 */     if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
/* 1228 */       for (BeanPostProcessor bp : getBeanPostProcessors()) {
/* 1229 */         if (bp instanceof InstantiationAwareBeanPostProcessor) {
/* 1230 */           InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor)bp;
/* 1231 */           if (!ibp.postProcessAfterInstantiation(bw.getWrappedInstance(), beanName)) {
/* 1232 */             continueWithPropertyPopulation = false;
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/* 1239 */     if (!continueWithPropertyPopulation) {
/*      */       return;
/*      */     }
/*      */     
/* 1243 */     if (mbd.getResolvedAutowireMode() == 1 || mbd
/* 1244 */       .getResolvedAutowireMode() == 2) {
/* 1245 */       MutablePropertyValues newPvs = new MutablePropertyValues((PropertyValues)mutablePropertyValues);
/*      */ 
/*      */       
/* 1248 */       if (mbd.getResolvedAutowireMode() == 1) {
/* 1249 */         autowireByName(beanName, mbd, bw, newPvs);
/*      */       }
/*      */ 
/*      */       
/* 1253 */       if (mbd.getResolvedAutowireMode() == 2) {
/* 1254 */         autowireByType(beanName, mbd, bw, newPvs);
/*      */       }
/*      */       
/* 1257 */       mutablePropertyValues = newPvs;
/*      */     } 
/*      */     
/* 1260 */     boolean hasInstAwareBpps = hasInstantiationAwareBeanPostProcessors();
/* 1261 */     boolean needsDepCheck = (mbd.getDependencyCheck() != 0);
/*      */     
/* 1263 */     if (hasInstAwareBpps || needsDepCheck) {
/* 1264 */       PropertyDescriptor[] filteredPds = filterPropertyDescriptorsForDependencyCheck(bw, mbd.allowCaching);
/* 1265 */       if (hasInstAwareBpps) {
/* 1266 */         for (BeanPostProcessor bp : getBeanPostProcessors()) {
/* 1267 */           if (bp instanceof InstantiationAwareBeanPostProcessor) {
/* 1268 */             InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor)bp;
/* 1269 */             propertyValues = ibp.postProcessPropertyValues((PropertyValues)mutablePropertyValues, filteredPds, bw.getWrappedInstance(), beanName);
/* 1270 */             if (propertyValues == null) {
/*      */               return;
/*      */             }
/*      */           } 
/*      */         } 
/*      */       }
/* 1276 */       if (needsDepCheck) {
/* 1277 */         checkDependencies(beanName, mbd, filteredPds, propertyValues);
/*      */       }
/*      */     } 
/*      */     
/* 1281 */     applyPropertyValues(beanName, mbd, bw, propertyValues);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void autowireByName(String beanName, AbstractBeanDefinition mbd, BeanWrapper bw, MutablePropertyValues pvs) {
/* 1296 */     String[] propertyNames = unsatisfiedNonSimpleProperties(mbd, bw);
/* 1297 */     for (String propertyName : propertyNames) {
/* 1298 */       if (containsBean(propertyName)) {
/* 1299 */         Object bean = getBean(propertyName);
/* 1300 */         pvs.add(propertyName, bean);
/* 1301 */         registerDependentBean(propertyName, beanName);
/* 1302 */         if (this.logger.isDebugEnabled()) {
/* 1303 */           this.logger.debug("Added autowiring by name from bean name '" + beanName + "' via property '" + propertyName + "' to bean named '" + propertyName + "'");
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 1308 */       else if (this.logger.isTraceEnabled()) {
/* 1309 */         this.logger.trace("Not autowiring property '" + propertyName + "' of bean '" + beanName + "' by name: no matching bean found");
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void autowireByType(String beanName, AbstractBeanDefinition mbd, BeanWrapper bw, MutablePropertyValues pvs) {
/*      */     BeanWrapper beanWrapper;
/* 1330 */     TypeConverter converter = getCustomTypeConverter();
/* 1331 */     if (converter == null) {
/* 1332 */       beanWrapper = bw;
/*      */     }
/*      */     
/* 1335 */     Set<String> autowiredBeanNames = new LinkedHashSet<String>(4);
/* 1336 */     String[] propertyNames = unsatisfiedNonSimpleProperties(mbd, bw);
/* 1337 */     for (String propertyName : propertyNames) {
/*      */       try {
/* 1339 */         PropertyDescriptor pd = bw.getPropertyDescriptor(propertyName);
/*      */ 
/*      */         
/* 1342 */         if (Object.class != pd.getPropertyType()) {
/* 1343 */           MethodParameter methodParam = BeanUtils.getWriteMethodParameter(pd);
/*      */           
/* 1345 */           boolean eager = !PriorityOrdered.class.isAssignableFrom(bw.getWrappedClass());
/* 1346 */           DependencyDescriptor desc = new AutowireByTypeDependencyDescriptor(methodParam, eager);
/* 1347 */           Object autowiredArgument = resolveDependency(desc, beanName, autowiredBeanNames, (TypeConverter)beanWrapper);
/* 1348 */           if (autowiredArgument != null) {
/* 1349 */             pvs.add(propertyName, autowiredArgument);
/*      */           }
/* 1351 */           for (String autowiredBeanName : autowiredBeanNames) {
/* 1352 */             registerDependentBean(autowiredBeanName, beanName);
/* 1353 */             if (this.logger.isDebugEnabled()) {
/* 1354 */               this.logger.debug("Autowiring by type from bean name '" + beanName + "' via property '" + propertyName + "' to bean named '" + autowiredBeanName + "'");
/*      */             }
/*      */           } 
/*      */           
/* 1358 */           autowiredBeanNames.clear();
/*      */         }
/*      */       
/* 1361 */       } catch (BeansException ex) {
/* 1362 */         throw new UnsatisfiedDependencyException(mbd.getResourceDescription(), beanName, propertyName, ex);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String[] unsatisfiedNonSimpleProperties(AbstractBeanDefinition mbd, BeanWrapper bw) {
/* 1378 */     Set<String> result = new TreeSet<String>();
/* 1379 */     MutablePropertyValues mutablePropertyValues = mbd.getPropertyValues();
/* 1380 */     PropertyDescriptor[] pds = bw.getPropertyDescriptors();
/* 1381 */     for (PropertyDescriptor pd : pds) {
/* 1382 */       if (pd.getWriteMethod() != null && !isExcludedFromDependencyCheck(pd) && !mutablePropertyValues.contains(pd.getName()) && 
/* 1383 */         !BeanUtils.isSimpleProperty(pd.getPropertyType())) {
/* 1384 */         result.add(pd.getName());
/*      */       }
/*      */     } 
/* 1387 */     return StringUtils.toStringArray(result);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PropertyDescriptor[] filterPropertyDescriptorsForDependencyCheck(BeanWrapper bw, boolean cache) {
/* 1400 */     PropertyDescriptor[] filtered = this.filteredPropertyDescriptorsCache.get(bw.getWrappedClass());
/* 1401 */     if (filtered == null) {
/* 1402 */       filtered = filterPropertyDescriptorsForDependencyCheck(bw);
/* 1403 */       if (cache) {
/*      */         
/* 1405 */         PropertyDescriptor[] existing = this.filteredPropertyDescriptorsCache.putIfAbsent(bw.getWrappedClass(), filtered);
/* 1406 */         if (existing != null) {
/* 1407 */           filtered = existing;
/*      */         }
/*      */       } 
/*      */     } 
/* 1411 */     return filtered;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PropertyDescriptor[] filterPropertyDescriptorsForDependencyCheck(BeanWrapper bw) {
/* 1423 */     List<PropertyDescriptor> pds = new ArrayList<PropertyDescriptor>(Arrays.asList(bw.getPropertyDescriptors()));
/* 1424 */     for (Iterator<PropertyDescriptor> it = pds.iterator(); it.hasNext(); ) {
/* 1425 */       PropertyDescriptor pd = it.next();
/* 1426 */       if (isExcludedFromDependencyCheck(pd)) {
/* 1427 */         it.remove();
/*      */       }
/*      */     } 
/* 1430 */     return pds.<PropertyDescriptor>toArray(new PropertyDescriptor[pds.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isExcludedFromDependencyCheck(PropertyDescriptor pd) {
/* 1444 */     return (AutowireUtils.isExcludedFromDependencyCheck(pd) || this.ignoredDependencyTypes
/* 1445 */       .contains(pd.getPropertyType()) || 
/* 1446 */       AutowireUtils.isSetterDefinedInInterface(pd, this.ignoredDependencyInterfaces));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkDependencies(String beanName, AbstractBeanDefinition mbd, PropertyDescriptor[] pds, PropertyValues pvs) throws UnsatisfiedDependencyException {
/* 1463 */     int dependencyCheck = mbd.getDependencyCheck();
/* 1464 */     for (PropertyDescriptor pd : pds) {
/* 1465 */       if (pd.getWriteMethod() != null && !pvs.contains(pd.getName())) {
/* 1466 */         boolean isSimple = BeanUtils.isSimpleProperty(pd.getPropertyType());
/* 1467 */         boolean unsatisfied = (dependencyCheck == 3 || (isSimple && dependencyCheck == 2) || (!isSimple && dependencyCheck == 1));
/*      */ 
/*      */         
/* 1470 */         if (unsatisfied) {
/* 1471 */           throw new UnsatisfiedDependencyException(mbd.getResourceDescription(), beanName, pd.getName(), "Set this property value or disable dependency checking for this bean.");
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void applyPropertyValues(String beanName, BeanDefinition mbd, BeanWrapper bw, PropertyValues pvs) {
/*      */     List<PropertyValue> original;
/*      */     BeanWrapper beanWrapper;
/* 1488 */     if (pvs == null || pvs.isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/* 1492 */     if (System.getSecurityManager() != null && bw instanceof BeanWrapperImpl) {
/* 1493 */       ((BeanWrapperImpl)bw).setSecurityContext(getAccessControlContext());
/*      */     }
/*      */     
/* 1496 */     MutablePropertyValues mpvs = null;
/*      */ 
/*      */     
/* 1499 */     if (pvs instanceof MutablePropertyValues) {
/* 1500 */       mpvs = (MutablePropertyValues)pvs;
/* 1501 */       if (mpvs.isConverted()) {
/*      */         
/*      */         try {
/* 1504 */           bw.setPropertyValues((PropertyValues)mpvs);
/*      */           
/*      */           return;
/* 1507 */         } catch (BeansException ex) {
/* 1508 */           throw new BeanCreationException(mbd
/* 1509 */               .getResourceDescription(), beanName, "Error setting property values", ex);
/*      */         } 
/*      */       }
/* 1512 */       original = mpvs.getPropertyValueList();
/*      */     } else {
/*      */       
/* 1515 */       original = Arrays.asList(pvs.getPropertyValues());
/*      */     } 
/*      */     
/* 1518 */     TypeConverter converter = getCustomTypeConverter();
/* 1519 */     if (converter == null) {
/* 1520 */       beanWrapper = bw;
/*      */     }
/* 1522 */     BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this, beanName, mbd, (TypeConverter)beanWrapper);
/*      */ 
/*      */     
/* 1525 */     List<PropertyValue> deepCopy = new ArrayList<PropertyValue>(original.size());
/* 1526 */     boolean resolveNecessary = false;
/* 1527 */     for (PropertyValue pv : original) {
/* 1528 */       if (pv.isConverted()) {
/* 1529 */         deepCopy.add(pv);
/*      */         continue;
/*      */       } 
/* 1532 */       String propertyName = pv.getName();
/* 1533 */       Object originalValue = pv.getValue();
/* 1534 */       Object resolvedValue = valueResolver.resolveValueIfNecessary(pv, originalValue);
/* 1535 */       Object convertedValue = resolvedValue;
/*      */       
/* 1537 */       boolean convertible = (bw.isWritableProperty(propertyName) && !PropertyAccessorUtils.isNestedOrIndexedProperty(propertyName));
/* 1538 */       if (convertible) {
/* 1539 */         convertedValue = convertForProperty(resolvedValue, propertyName, bw, (TypeConverter)beanWrapper);
/*      */       }
/*      */ 
/*      */       
/* 1543 */       if (resolvedValue == originalValue) {
/* 1544 */         if (convertible) {
/* 1545 */           pv.setConvertedValue(convertedValue);
/*      */         }
/* 1547 */         deepCopy.add(pv); continue;
/*      */       } 
/* 1549 */       if (convertible && originalValue instanceof TypedStringValue && 
/* 1550 */         !((TypedStringValue)originalValue).isDynamic() && !(convertedValue instanceof java.util.Collection) && 
/* 1551 */         !ObjectUtils.isArray(convertedValue)) {
/* 1552 */         pv.setConvertedValue(convertedValue);
/* 1553 */         deepCopy.add(pv);
/*      */         continue;
/*      */       } 
/* 1556 */       resolveNecessary = true;
/* 1557 */       deepCopy.add(new PropertyValue(pv, convertedValue));
/*      */     } 
/*      */ 
/*      */     
/* 1561 */     if (mpvs != null && !resolveNecessary) {
/* 1562 */       mpvs.setConverted();
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/* 1567 */       bw.setPropertyValues((PropertyValues)new MutablePropertyValues(deepCopy));
/*      */     }
/* 1569 */     catch (BeansException ex) {
/* 1570 */       throw new BeanCreationException(mbd
/* 1571 */           .getResourceDescription(), beanName, "Error setting property values", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object convertForProperty(Object value, String propertyName, BeanWrapper bw, TypeConverter converter) {
/* 1579 */     if (converter instanceof BeanWrapperImpl) {
/* 1580 */       return ((BeanWrapperImpl)converter).convertForProperty(value, propertyName);
/*      */     }
/*      */     
/* 1583 */     PropertyDescriptor pd = bw.getPropertyDescriptor(propertyName);
/* 1584 */     MethodParameter methodParam = BeanUtils.getWriteMethodParameter(pd);
/* 1585 */     return converter.convertIfNecessary(value, pd.getPropertyType(), methodParam);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object initializeBean(final String beanName, final Object bean, RootBeanDefinition mbd) {
/* 1608 */     if (System.getSecurityManager() != null) {
/* 1609 */       AccessController.doPrivileged(new PrivilegedAction()
/*      */           {
/*      */             public Object run() {
/* 1612 */               AbstractAutowireCapableBeanFactory.this.invokeAwareMethods(beanName, bean);
/* 1613 */               return null;
/*      */             }
/* 1615 */           }getAccessControlContext());
/*      */     } else {
/*      */       
/* 1618 */       invokeAwareMethods(beanName, bean);
/*      */     } 
/*      */     
/* 1621 */     Object wrappedBean = bean;
/* 1622 */     if (mbd == null || !mbd.isSynthetic()) {
/* 1623 */       wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
/*      */     }
/*      */     
/*      */     try {
/* 1627 */       invokeInitMethods(beanName, wrappedBean, mbd);
/*      */     }
/* 1629 */     catch (Throwable ex) {
/* 1630 */       throw new BeanCreationException((mbd != null) ? mbd
/* 1631 */           .getResourceDescription() : null, beanName, "Invocation of init method failed", ex);
/*      */     } 
/*      */     
/* 1634 */     if (mbd == null || !mbd.isSynthetic()) {
/* 1635 */       wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
/*      */     }
/* 1637 */     return wrappedBean;
/*      */   }
/*      */   
/*      */   private void invokeAwareMethods(String beanName, Object bean) {
/* 1641 */     if (bean instanceof org.springframework.beans.factory.Aware) {
/* 1642 */       if (bean instanceof BeanNameAware) {
/* 1643 */         ((BeanNameAware)bean).setBeanName(beanName);
/*      */       }
/* 1645 */       if (bean instanceof BeanClassLoaderAware) {
/* 1646 */         ((BeanClassLoaderAware)bean).setBeanClassLoader(getBeanClassLoader());
/*      */       }
/* 1648 */       if (bean instanceof BeanFactoryAware) {
/* 1649 */         ((BeanFactoryAware)bean).setBeanFactory((BeanFactory)this);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void invokeInitMethods(String beanName, final Object bean, RootBeanDefinition mbd) throws Throwable {
/* 1669 */     boolean isInitializingBean = bean instanceof InitializingBean;
/* 1670 */     if (isInitializingBean && (mbd == null || !mbd.isExternallyManagedInitMethod("afterPropertiesSet"))) {
/* 1671 */       if (this.logger.isDebugEnabled()) {
/* 1672 */         this.logger.debug("Invoking afterPropertiesSet() on bean with name '" + beanName + "'");
/*      */       }
/* 1674 */       if (System.getSecurityManager() != null) {
/*      */         try {
/* 1676 */           AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */               {
/*      */                 public Object run() throws Exception {
/* 1679 */                   ((InitializingBean)bean).afterPropertiesSet();
/* 1680 */                   return null;
/*      */                 }
/* 1682 */               },  getAccessControlContext());
/*      */         }
/* 1684 */         catch (PrivilegedActionException pae) {
/* 1685 */           throw pae.getException();
/*      */         } 
/*      */       } else {
/*      */         
/* 1689 */         ((InitializingBean)bean).afterPropertiesSet();
/*      */       } 
/*      */     } 
/*      */     
/* 1693 */     if (mbd != null) {
/* 1694 */       String initMethodName = mbd.getInitMethodName();
/* 1695 */       if (initMethodName != null && (!isInitializingBean || !"afterPropertiesSet".equals(initMethodName)) && 
/* 1696 */         !mbd.isExternallyManagedInitMethod(initMethodName)) {
/* 1697 */         invokeCustomInitMethod(beanName, bean, mbd);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void invokeCustomInitMethod(String beanName, final Object bean, RootBeanDefinition mbd) throws Throwable {
/* 1712 */     String initMethodName = mbd.getInitMethodName();
/*      */ 
/*      */     
/* 1715 */     final Method initMethod = mbd.isNonPublicAccessAllowed() ? BeanUtils.findMethod(bean.getClass(), initMethodName, new Class[0]) : ClassUtils.getMethodIfAvailable(bean.getClass(), initMethodName, new Class[0]);
/* 1716 */     if (initMethod == null) {
/* 1717 */       if (mbd.isEnforceInitMethod()) {
/* 1718 */         throw new BeanDefinitionValidationException("Couldn't find an init method named '" + initMethodName + "' on bean with name '" + beanName + "'");
/*      */       }
/*      */ 
/*      */       
/* 1722 */       if (this.logger.isDebugEnabled()) {
/* 1723 */         this.logger.debug("No default init method named '" + initMethodName + "' found on bean with name '" + beanName + "'");
/*      */       }
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */     
/* 1731 */     if (this.logger.isDebugEnabled()) {
/* 1732 */       this.logger.debug("Invoking init method  '" + initMethodName + "' on bean with name '" + beanName + "'");
/*      */     }
/*      */     
/* 1735 */     if (System.getSecurityManager() != null) {
/* 1736 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */           {
/*      */             public Object run() throws Exception {
/* 1739 */               ReflectionUtils.makeAccessible(initMethod);
/* 1740 */               return null;
/*      */             }
/*      */           });
/*      */       try {
/* 1744 */         AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */             {
/*      */               public Object run() throws Exception {
/* 1747 */                 initMethod.invoke(bean, new Object[0]);
/* 1748 */                 return null;
/*      */               }
/* 1750 */             }getAccessControlContext());
/*      */       }
/* 1752 */       catch (PrivilegedActionException pae) {
/* 1753 */         InvocationTargetException ex = (InvocationTargetException)pae.getException();
/* 1754 */         throw ex.getTargetException();
/*      */       } 
/*      */     } else {
/*      */       
/*      */       try {
/* 1759 */         ReflectionUtils.makeAccessible(initMethod);
/* 1760 */         initMethod.invoke(bean, new Object[0]);
/*      */       }
/* 1762 */       catch (InvocationTargetException ex) {
/* 1763 */         throw ex.getTargetException();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object postProcessObjectFromFactoryBean(Object object, String beanName) {
/* 1777 */     return applyBeanPostProcessorsAfterInitialization(object, beanName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void removeSingleton(String beanName) {
/* 1785 */     synchronized (getSingletonMutex()) {
/* 1786 */       super.removeSingleton(beanName);
/* 1787 */       this.factoryBeanInstanceCache.remove(beanName);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void clearSingletonCache() {
/* 1796 */     synchronized (getSingletonMutex()) {
/* 1797 */       super.clearSingletonCache();
/* 1798 */       this.factoryBeanInstanceCache.clear();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class AutowireByTypeDependencyDescriptor
/*      */     extends DependencyDescriptor
/*      */   {
/*      */     public AutowireByTypeDependencyDescriptor(MethodParameter methodParameter, boolean eager) {
/* 1811 */       super(methodParameter, false, eager);
/*      */     }
/*      */ 
/*      */     
/*      */     public String getDependencyName() {
/* 1816 */       return null;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\AbstractAutowireCapableBeanFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */