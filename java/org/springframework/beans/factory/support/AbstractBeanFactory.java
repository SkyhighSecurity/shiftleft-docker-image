/*      */ package org.springframework.beans.factory.support;
/*      */ 
/*      */ import java.beans.PropertyEditor;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import org.springframework.beans.BeanUtils;
/*      */ import org.springframework.beans.BeanWrapper;
/*      */ import org.springframework.beans.BeansException;
/*      */ import org.springframework.beans.PropertyEditorRegistrar;
/*      */ import org.springframework.beans.PropertyEditorRegistry;
/*      */ import org.springframework.beans.PropertyEditorRegistrySupport;
/*      */ import org.springframework.beans.SimpleTypeConverter;
/*      */ import org.springframework.beans.TypeConverter;
/*      */ import org.springframework.beans.TypeMismatchException;
/*      */ import org.springframework.beans.factory.BeanCreationException;
/*      */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*      */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*      */ import org.springframework.beans.factory.BeanFactory;
/*      */ import org.springframework.beans.factory.BeanFactoryUtils;
/*      */ import org.springframework.beans.factory.BeanIsAbstractException;
/*      */ import org.springframework.beans.factory.BeanIsNotAFactoryException;
/*      */ import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
/*      */ import org.springframework.beans.factory.CannotLoadBeanClassException;
/*      */ import org.springframework.beans.factory.FactoryBean;
/*      */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*      */ import org.springframework.beans.factory.ObjectFactory;
/*      */ import org.springframework.beans.factory.SmartFactoryBean;
/*      */ import org.springframework.beans.factory.config.BeanDefinition;
/*      */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*      */ import org.springframework.beans.factory.config.BeanExpressionContext;
/*      */ import org.springframework.beans.factory.config.BeanExpressionResolver;
/*      */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*      */ import org.springframework.beans.factory.config.Scope;
/*      */ import org.springframework.core.DecoratingClassLoader;
/*      */ import org.springframework.core.NamedThreadLocal;
/*      */ import org.springframework.core.ResolvableType;
/*      */ import org.springframework.core.convert.ConversionService;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ import org.springframework.util.StringValueResolver;
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
/*      */ public abstract class AbstractBeanFactory
/*      */   extends FactoryBeanRegistrySupport
/*      */   implements ConfigurableBeanFactory
/*      */ {
/*      */   private BeanFactory parentBeanFactory;
/*  119 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*      */ 
/*      */   
/*      */   private ClassLoader tempClassLoader;
/*      */ 
/*      */   
/*      */   private boolean cacheBeanMetadata = true;
/*      */ 
/*      */   
/*      */   private BeanExpressionResolver beanExpressionResolver;
/*      */ 
/*      */   
/*      */   private ConversionService conversionService;
/*      */ 
/*      */   
/*  134 */   private final Set<PropertyEditorRegistrar> propertyEditorRegistrars = new LinkedHashSet<PropertyEditorRegistrar>(4);
/*      */ 
/*      */ 
/*      */   
/*  138 */   private final Map<Class<?>, Class<? extends PropertyEditor>> customEditors = new HashMap<Class<?>, Class<? extends PropertyEditor>>(4);
/*      */ 
/*      */ 
/*      */   
/*      */   private TypeConverter typeConverter;
/*      */ 
/*      */   
/*  145 */   private final List<StringValueResolver> embeddedValueResolvers = new LinkedList<StringValueResolver>();
/*      */ 
/*      */   
/*  148 */   private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();
/*      */ 
/*      */   
/*      */   private boolean hasInstantiationAwareBeanPostProcessors;
/*      */ 
/*      */   
/*      */   private boolean hasDestructionAwareBeanPostProcessors;
/*      */ 
/*      */   
/*  157 */   private final Map<String, Scope> scopes = new LinkedHashMap<String, Scope>(8);
/*      */ 
/*      */   
/*      */   private SecurityContextProvider securityContextProvider;
/*      */ 
/*      */   
/*  163 */   private final Map<String, RootBeanDefinition> mergedBeanDefinitions = new ConcurrentHashMap<String, RootBeanDefinition>(256);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  168 */   private final Set<String> alreadyCreated = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>(256));
/*      */ 
/*      */   
/*  171 */   private final ThreadLocal<Object> prototypesCurrentlyInCreation = (ThreadLocal<Object>)new NamedThreadLocal("Prototype beans currently in creation");
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
/*      */   public AbstractBeanFactory(BeanFactory parentBeanFactory) {
/*  187 */     this.parentBeanFactory = parentBeanFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getBean(String name) throws BeansException {
/*  197 */     return doGetBean(name, (Class<?>)null, (Object[])null, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
/*  202 */     return doGetBean(name, requiredType, (Object[])null, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getBean(String name, Object... args) throws BeansException {
/*  207 */     return doGetBean(name, (Class<?>)null, args, false);
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
/*      */   public <T> T getBean(String name, Class<T> requiredType, Object... args) throws BeansException {
/*  220 */     return doGetBean(name, requiredType, args, false);
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
/*      */   protected <T> T doGetBean(String name, Class<T> requiredType, final Object[] args, boolean typeCheckOnly) throws BeansException {
/*      */     Object bean;
/*  239 */     final String beanName = transformedBeanName(name);
/*      */ 
/*      */ 
/*      */     
/*  243 */     Object sharedInstance = getSingleton(beanName);
/*  244 */     if (sharedInstance != null && args == null) {
/*  245 */       if (this.logger.isDebugEnabled()) {
/*  246 */         if (isSingletonCurrentlyInCreation(beanName)) {
/*  247 */           this.logger.debug("Returning eagerly cached instance of singleton bean '" + beanName + "' that is not fully initialized yet - a consequence of a circular reference");
/*      */         }
/*      */         else {
/*      */           
/*  251 */           this.logger.debug("Returning cached instance of singleton bean '" + beanName + "'");
/*      */         } 
/*      */       }
/*  254 */       bean = getObjectForBeanInstance(sharedInstance, name, beanName, (RootBeanDefinition)null);
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/*  260 */       if (isPrototypeCurrentlyInCreation(beanName)) {
/*  261 */         throw new BeanCurrentlyInCreationException(beanName);
/*      */       }
/*      */ 
/*      */       
/*  265 */       BeanFactory parentBeanFactory = getParentBeanFactory();
/*  266 */       if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
/*      */         
/*  268 */         String nameToLookup = originalBeanName(name);
/*  269 */         if (args != null)
/*      */         {
/*  271 */           return (T)parentBeanFactory.getBean(nameToLookup, args);
/*      */         }
/*      */ 
/*      */         
/*  275 */         return (T)parentBeanFactory.getBean(nameToLookup, requiredType);
/*      */       } 
/*      */ 
/*      */       
/*  279 */       if (!typeCheckOnly) {
/*  280 */         markBeanAsCreated(beanName);
/*      */       }
/*      */       
/*      */       try {
/*  284 */         final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*  285 */         checkMergedBeanDefinition(mbd, beanName, args);
/*      */ 
/*      */         
/*  288 */         String[] dependsOn = mbd.getDependsOn();
/*  289 */         if (dependsOn != null) {
/*  290 */           for (String dep : dependsOn) {
/*  291 */             if (isDependent(beanName, dep)) {
/*  292 */               throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Circular depends-on relationship between '" + beanName + "' and '" + dep + "'");
/*      */             }
/*      */             
/*  295 */             registerDependentBean(dep, beanName);
/*      */             try {
/*  297 */               getBean(dep);
/*      */             }
/*  299 */             catch (NoSuchBeanDefinitionException ex) {
/*  300 */               throw new BeanCreationException(mbd.getResourceDescription(), beanName, "'" + beanName + "' depends on missing bean '" + dep + "'", ex);
/*      */             } 
/*      */           } 
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  307 */         if (mbd.isSingleton()) {
/*  308 */           sharedInstance = getSingleton(beanName, new ObjectFactory<Object>()
/*      */               {
/*      */                 public Object getObject() throws BeansException {
/*      */                   try {
/*  312 */                     return AbstractBeanFactory.this.createBean(beanName, mbd, args);
/*      */                   }
/*  314 */                   catch (BeansException ex) {
/*      */ 
/*      */ 
/*      */                     
/*  318 */                     AbstractBeanFactory.this.destroySingleton(beanName);
/*  319 */                     throw ex;
/*      */                   } 
/*      */                 }
/*      */               });
/*  323 */           bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
/*      */         
/*      */         }
/*  326 */         else if (mbd.isPrototype()) {
/*      */           
/*  328 */           Object prototypeInstance = null;
/*      */           try {
/*  330 */             beforePrototypeCreation(beanName);
/*  331 */             prototypeInstance = createBean(beanName, mbd, args);
/*      */           } finally {
/*      */             
/*  334 */             afterPrototypeCreation(beanName);
/*      */           } 
/*  336 */           bean = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
/*      */         }
/*      */         else {
/*      */           
/*  340 */           String scopeName = mbd.getScope();
/*  341 */           Scope scope = this.scopes.get(scopeName);
/*  342 */           if (scope == null) {
/*  343 */             throw new IllegalStateException("No Scope registered for scope name '" + scopeName + "'");
/*      */           }
/*      */           try {
/*  346 */             Object scopedInstance = scope.get(beanName, new ObjectFactory<Object>()
/*      */                 {
/*      */                   public Object getObject() throws BeansException {
/*  349 */                     AbstractBeanFactory.this.beforePrototypeCreation(beanName);
/*      */                     try {
/*  351 */                       return AbstractBeanFactory.this.createBean(beanName, mbd, args);
/*      */                     } finally {
/*      */                       
/*  354 */                       AbstractBeanFactory.this.afterPrototypeCreation(beanName);
/*      */                     } 
/*      */                   }
/*      */                 });
/*  358 */             bean = getObjectForBeanInstance(scopedInstance, name, beanName, mbd);
/*      */           }
/*  360 */           catch (IllegalStateException ex) {
/*  361 */             throw new BeanCreationException(beanName, "Scope '" + scopeName + "' is not active for the current thread; consider defining a scoped proxy for this bean if you intend to refer to it from a singleton", ex);
/*      */           
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       }
/*  368 */       catch (BeansException ex) {
/*  369 */         cleanupAfterBeanCreationFailure(beanName);
/*  370 */         throw ex;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  375 */     if (requiredType != null && bean != null && !requiredType.isInstance(bean)) {
/*      */       try {
/*  377 */         return (T)getTypeConverter().convertIfNecessary(bean, requiredType);
/*      */       }
/*  379 */       catch (TypeMismatchException ex) {
/*  380 */         if (this.logger.isDebugEnabled()) {
/*  381 */           this.logger.debug("Failed to convert bean '" + name + "' to required type '" + 
/*  382 */               ClassUtils.getQualifiedName(requiredType) + "'", (Throwable)ex);
/*      */         }
/*  384 */         throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
/*      */       } 
/*      */     }
/*  387 */     return (T)bean;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsBean(String name) {
/*  392 */     String beanName = transformedBeanName(name);
/*  393 */     if (containsSingleton(beanName) || containsBeanDefinition(beanName)) {
/*  394 */       return (!BeanFactoryUtils.isFactoryDereference(name) || isFactoryBean(name));
/*      */     }
/*      */     
/*  397 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  398 */     return (parentBeanFactory != null && parentBeanFactory.containsBean(originalBeanName(name)));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
/*  403 */     String beanName = transformedBeanName(name);
/*      */     
/*  405 */     Object beanInstance = getSingleton(beanName, false);
/*  406 */     if (beanInstance != null) {
/*  407 */       if (beanInstance instanceof FactoryBean) {
/*  408 */         return (BeanFactoryUtils.isFactoryDereference(name) || ((FactoryBean)beanInstance).isSingleton());
/*      */       }
/*      */       
/*  411 */       return !BeanFactoryUtils.isFactoryDereference(name);
/*      */     } 
/*      */     
/*  414 */     if (containsSingleton(beanName)) {
/*  415 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  419 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  420 */     if (parentBeanFactory != null && !containsBeanDefinition(beanName))
/*      */     {
/*  422 */       return parentBeanFactory.isSingleton(originalBeanName(name));
/*      */     }
/*      */     
/*  425 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*      */ 
/*      */     
/*  428 */     if (mbd.isSingleton()) {
/*  429 */       if (isFactoryBean(beanName, mbd)) {
/*  430 */         if (BeanFactoryUtils.isFactoryDereference(name)) {
/*  431 */           return true;
/*      */         }
/*  433 */         FactoryBean<?> factoryBean = (FactoryBean)getBean("&" + beanName);
/*  434 */         return factoryBean.isSingleton();
/*      */       } 
/*      */       
/*  437 */       return !BeanFactoryUtils.isFactoryDereference(name);
/*      */     } 
/*      */ 
/*      */     
/*  441 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
/*  447 */     String beanName = transformedBeanName(name);
/*      */     
/*  449 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  450 */     if (parentBeanFactory != null && !containsBeanDefinition(beanName))
/*      */     {
/*  452 */       return parentBeanFactory.isPrototype(originalBeanName(name));
/*      */     }
/*      */     
/*  455 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*  456 */     if (mbd.isPrototype())
/*      */     {
/*  458 */       return (!BeanFactoryUtils.isFactoryDereference(name) || isFactoryBean(beanName, mbd));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  463 */     if (BeanFactoryUtils.isFactoryDereference(name)) {
/*  464 */       return false;
/*      */     }
/*  466 */     if (isFactoryBean(beanName, mbd)) {
/*  467 */       final FactoryBean<?> fb = (FactoryBean)getBean("&" + beanName);
/*  468 */       if (System.getSecurityManager() != null) {
/*  469 */         return ((Boolean)AccessController.<Boolean>doPrivileged(new PrivilegedAction<Boolean>()
/*      */             {
/*      */               public Boolean run() {
/*  472 */                 return Boolean.valueOf(((fb instanceof SmartFactoryBean && ((SmartFactoryBean)fb).isPrototype()) || 
/*  473 */                     !fb.isSingleton()));
/*      */               }
/*  475 */             },  getAccessControlContext())).booleanValue();
/*      */       }
/*      */       
/*  478 */       return ((fb instanceof SmartFactoryBean && ((SmartFactoryBean)fb).isPrototype()) || 
/*  479 */         !fb.isSingleton());
/*      */     } 
/*      */ 
/*      */     
/*  483 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
/*  489 */     String beanName = transformedBeanName(name);
/*      */ 
/*      */     
/*  492 */     Object beanInstance = getSingleton(beanName, false);
/*  493 */     if (beanInstance != null) {
/*  494 */       if (beanInstance instanceof FactoryBean) {
/*  495 */         if (!BeanFactoryUtils.isFactoryDereference(name)) {
/*  496 */           Class<?> type = getTypeForFactoryBean((FactoryBean)beanInstance);
/*  497 */           return (type != null && typeToMatch.isAssignableFrom(type));
/*      */         } 
/*      */         
/*  500 */         return typeToMatch.isInstance(beanInstance);
/*      */       } 
/*      */       
/*  503 */       if (!BeanFactoryUtils.isFactoryDereference(name)) {
/*  504 */         if (typeToMatch.isInstance(beanInstance))
/*      */         {
/*  506 */           return true;
/*      */         }
/*  508 */         if (typeToMatch.hasGenerics() && containsBeanDefinition(beanName)) {
/*      */           
/*  510 */           RootBeanDefinition rootBeanDefinition = getMergedLocalBeanDefinition(beanName);
/*  511 */           Class<?> targetType = rootBeanDefinition.getTargetType();
/*  512 */           if (targetType != null && targetType != ClassUtils.getUserClass(beanInstance) && typeToMatch
/*  513 */             .isAssignableFrom(targetType)) {
/*      */             
/*  515 */             Class<?> clazz = typeToMatch.resolve();
/*  516 */             return (clazz == null || clazz.isInstance(beanInstance));
/*      */           } 
/*      */         } 
/*      */       } 
/*  520 */       return false;
/*      */     } 
/*  522 */     if (containsSingleton(beanName) && !containsBeanDefinition(beanName))
/*      */     {
/*  524 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  528 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  529 */     if (parentBeanFactory != null && !containsBeanDefinition(beanName))
/*      */     {
/*  531 */       return parentBeanFactory.isTypeMatch(originalBeanName(name), typeToMatch);
/*      */     }
/*      */ 
/*      */     
/*  535 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*      */     
/*  537 */     Class<?> classToMatch = typeToMatch.resolve();
/*  538 */     if (classToMatch == null) {
/*  539 */       classToMatch = FactoryBean.class;
/*      */     }
/*  541 */     (new Class[1])[0] = classToMatch; (new Class[2])[0] = FactoryBean.class; (new Class[2])[1] = classToMatch; Class<?>[] typesToMatch = (FactoryBean.class == classToMatch) ? new Class[1] : new Class[2];
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  546 */     BeanDefinitionHolder dbd = mbd.getDecoratedDefinition();
/*  547 */     if (dbd != null && !BeanFactoryUtils.isFactoryDereference(name)) {
/*  548 */       RootBeanDefinition tbd = getMergedBeanDefinition(dbd.getBeanName(), dbd.getBeanDefinition(), mbd);
/*  549 */       Class<?> targetClass = predictBeanType(dbd.getBeanName(), tbd, typesToMatch);
/*  550 */       if (targetClass != null && !FactoryBean.class.isAssignableFrom(targetClass)) {
/*  551 */         return typeToMatch.isAssignableFrom(targetClass);
/*      */       }
/*      */     } 
/*      */     
/*  555 */     Class<?> beanType = predictBeanType(beanName, mbd, typesToMatch);
/*  556 */     if (beanType == null) {
/*  557 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  561 */     if (FactoryBean.class.isAssignableFrom(beanType)) {
/*  562 */       if (!BeanFactoryUtils.isFactoryDereference(name))
/*      */       {
/*  564 */         beanType = getTypeForFactoryBean(beanName, mbd);
/*  565 */         if (beanType == null) {
/*  566 */           return false;
/*      */         }
/*      */       }
/*      */     
/*  570 */     } else if (BeanFactoryUtils.isFactoryDereference(name)) {
/*      */ 
/*      */ 
/*      */       
/*  574 */       beanType = predictBeanType(beanName, mbd, new Class[] { FactoryBean.class });
/*  575 */       if (beanType == null || !FactoryBean.class.isAssignableFrom(beanType)) {
/*  576 */         return false;
/*      */       }
/*      */     } 
/*      */     
/*  580 */     ResolvableType resolvableType = mbd.targetType;
/*  581 */     if (resolvableType == null) {
/*  582 */       resolvableType = mbd.factoryMethodReturnType;
/*      */     }
/*  584 */     if (resolvableType != null && resolvableType.resolve() == beanType) {
/*  585 */       return typeToMatch.isAssignableFrom(resolvableType);
/*      */     }
/*  587 */     return typeToMatch.isAssignableFrom(beanType);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
/*  592 */     return isTypeMatch(name, ResolvableType.forRawClass(typeToMatch));
/*      */   }
/*      */ 
/*      */   
/*      */   public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
/*  597 */     String beanName = transformedBeanName(name);
/*      */ 
/*      */     
/*  600 */     Object beanInstance = getSingleton(beanName, false);
/*  601 */     if (beanInstance != null) {
/*  602 */       if (beanInstance instanceof FactoryBean && !BeanFactoryUtils.isFactoryDereference(name)) {
/*  603 */         return getTypeForFactoryBean((FactoryBean)beanInstance);
/*      */       }
/*      */       
/*  606 */       return beanInstance.getClass();
/*      */     } 
/*      */     
/*  609 */     if (containsSingleton(beanName) && !containsBeanDefinition(beanName))
/*      */     {
/*  611 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  615 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  616 */     if (parentBeanFactory != null && !containsBeanDefinition(beanName))
/*      */     {
/*  618 */       return parentBeanFactory.getType(originalBeanName(name));
/*      */     }
/*      */     
/*  621 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*      */ 
/*      */ 
/*      */     
/*  625 */     BeanDefinitionHolder dbd = mbd.getDecoratedDefinition();
/*  626 */     if (dbd != null && !BeanFactoryUtils.isFactoryDereference(name)) {
/*  627 */       RootBeanDefinition tbd = getMergedBeanDefinition(dbd.getBeanName(), dbd.getBeanDefinition(), mbd);
/*  628 */       Class<?> targetClass = predictBeanType(dbd.getBeanName(), tbd, new Class[0]);
/*  629 */       if (targetClass != null && !FactoryBean.class.isAssignableFrom(targetClass)) {
/*  630 */         return targetClass;
/*      */       }
/*      */     } 
/*      */     
/*  634 */     Class<?> beanClass = predictBeanType(beanName, mbd, new Class[0]);
/*      */ 
/*      */     
/*  637 */     if (beanClass != null && FactoryBean.class.isAssignableFrom(beanClass)) {
/*  638 */       if (!BeanFactoryUtils.isFactoryDereference(name))
/*      */       {
/*  640 */         return getTypeForFactoryBean(beanName, mbd);
/*      */       }
/*      */       
/*  643 */       return beanClass;
/*      */     } 
/*      */ 
/*      */     
/*  647 */     return !BeanFactoryUtils.isFactoryDereference(name) ? beanClass : null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getAliases(String name) {
/*  653 */     String beanName = transformedBeanName(name);
/*  654 */     List<String> aliases = new ArrayList<String>();
/*  655 */     boolean factoryPrefix = name.startsWith("&");
/*  656 */     String fullBeanName = beanName;
/*  657 */     if (factoryPrefix) {
/*  658 */       fullBeanName = "&" + beanName;
/*      */     }
/*  660 */     if (!fullBeanName.equals(name)) {
/*  661 */       aliases.add(fullBeanName);
/*      */     }
/*  663 */     String[] retrievedAliases = super.getAliases(beanName);
/*  664 */     for (String retrievedAlias : retrievedAliases) {
/*  665 */       String alias = (factoryPrefix ? "&" : "") + retrievedAlias;
/*  666 */       if (!alias.equals(name)) {
/*  667 */         aliases.add(alias);
/*      */       }
/*      */     } 
/*  670 */     if (!containsSingleton(beanName) && !containsBeanDefinition(beanName)) {
/*  671 */       BeanFactory parentBeanFactory = getParentBeanFactory();
/*  672 */       if (parentBeanFactory != null) {
/*  673 */         aliases.addAll(Arrays.asList(parentBeanFactory.getAliases(fullBeanName)));
/*      */       }
/*      */     } 
/*  676 */     return StringUtils.toStringArray(aliases);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BeanFactory getParentBeanFactory() {
/*  686 */     return this.parentBeanFactory;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsLocalBean(String name) {
/*  691 */     String beanName = transformedBeanName(name);
/*  692 */     return ((containsSingleton(beanName) || containsBeanDefinition(beanName)) && (
/*  693 */       !BeanFactoryUtils.isFactoryDereference(name) || isFactoryBean(beanName)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setParentBeanFactory(BeanFactory parentBeanFactory) {
/*  703 */     if (this.parentBeanFactory != null && this.parentBeanFactory != parentBeanFactory) {
/*  704 */       throw new IllegalStateException("Already associated with parent BeanFactory: " + this.parentBeanFactory);
/*      */     }
/*  706 */     this.parentBeanFactory = parentBeanFactory;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBeanClassLoader(ClassLoader beanClassLoader) {
/*  711 */     this.beanClassLoader = (beanClassLoader != null) ? beanClassLoader : ClassUtils.getDefaultClassLoader();
/*      */   }
/*      */ 
/*      */   
/*      */   public ClassLoader getBeanClassLoader() {
/*  716 */     return this.beanClassLoader;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTempClassLoader(ClassLoader tempClassLoader) {
/*  721 */     this.tempClassLoader = tempClassLoader;
/*      */   }
/*      */ 
/*      */   
/*      */   public ClassLoader getTempClassLoader() {
/*  726 */     return this.tempClassLoader;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCacheBeanMetadata(boolean cacheBeanMetadata) {
/*  731 */     this.cacheBeanMetadata = cacheBeanMetadata;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCacheBeanMetadata() {
/*  736 */     return this.cacheBeanMetadata;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBeanExpressionResolver(BeanExpressionResolver resolver) {
/*  741 */     this.beanExpressionResolver = resolver;
/*      */   }
/*      */ 
/*      */   
/*      */   public BeanExpressionResolver getBeanExpressionResolver() {
/*  746 */     return this.beanExpressionResolver;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setConversionService(ConversionService conversionService) {
/*  751 */     this.conversionService = conversionService;
/*      */   }
/*      */ 
/*      */   
/*      */   public ConversionService getConversionService() {
/*  756 */     return this.conversionService;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar) {
/*  761 */     Assert.notNull(registrar, "PropertyEditorRegistrar must not be null");
/*  762 */     this.propertyEditorRegistrars.add(registrar);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<PropertyEditorRegistrar> getPropertyEditorRegistrars() {
/*  769 */     return this.propertyEditorRegistrars;
/*      */   }
/*      */ 
/*      */   
/*      */   public void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass) {
/*  774 */     Assert.notNull(requiredType, "Required type must not be null");
/*  775 */     Assert.notNull(propertyEditorClass, "PropertyEditor class must not be null");
/*  776 */     this.customEditors.put(requiredType, propertyEditorClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public void copyRegisteredEditorsTo(PropertyEditorRegistry registry) {
/*  781 */     registerCustomEditors(registry);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<Class<?>, Class<? extends PropertyEditor>> getCustomEditors() {
/*  788 */     return this.customEditors;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTypeConverter(TypeConverter typeConverter) {
/*  793 */     this.typeConverter = typeConverter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected TypeConverter getCustomTypeConverter() {
/*  801 */     return this.typeConverter;
/*      */   }
/*      */ 
/*      */   
/*      */   public TypeConverter getTypeConverter() {
/*  806 */     TypeConverter customConverter = getCustomTypeConverter();
/*  807 */     if (customConverter != null) {
/*  808 */       return customConverter;
/*      */     }
/*      */ 
/*      */     
/*  812 */     SimpleTypeConverter typeConverter = new SimpleTypeConverter();
/*  813 */     typeConverter.setConversionService(getConversionService());
/*  814 */     registerCustomEditors((PropertyEditorRegistry)typeConverter);
/*  815 */     return (TypeConverter)typeConverter;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
/*  821 */     Assert.notNull(valueResolver, "StringValueResolver must not be null");
/*  822 */     this.embeddedValueResolvers.add(valueResolver);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasEmbeddedValueResolver() {
/*  827 */     return !this.embeddedValueResolvers.isEmpty();
/*      */   }
/*      */ 
/*      */   
/*      */   public String resolveEmbeddedValue(String value) {
/*  832 */     if (value == null) {
/*  833 */       return null;
/*      */     }
/*  835 */     String result = value;
/*  836 */     for (StringValueResolver resolver : this.embeddedValueResolvers) {
/*  837 */       result = resolver.resolveStringValue(result);
/*  838 */       if (result == null) {
/*  839 */         return null;
/*      */       }
/*      */     } 
/*  842 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
/*  847 */     Assert.notNull(beanPostProcessor, "BeanPostProcessor must not be null");
/*  848 */     this.beanPostProcessors.remove(beanPostProcessor);
/*  849 */     this.beanPostProcessors.add(beanPostProcessor);
/*  850 */     if (beanPostProcessor instanceof org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor) {
/*  851 */       this.hasInstantiationAwareBeanPostProcessors = true;
/*      */     }
/*  853 */     if (beanPostProcessor instanceof org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor) {
/*  854 */       this.hasDestructionAwareBeanPostProcessors = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBeanPostProcessorCount() {
/*  860 */     return this.beanPostProcessors.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<BeanPostProcessor> getBeanPostProcessors() {
/*  868 */     return this.beanPostProcessors;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean hasInstantiationAwareBeanPostProcessors() {
/*  878 */     return this.hasInstantiationAwareBeanPostProcessors;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean hasDestructionAwareBeanPostProcessors() {
/*  888 */     return this.hasDestructionAwareBeanPostProcessors;
/*      */   }
/*      */ 
/*      */   
/*      */   public void registerScope(String scopeName, Scope scope) {
/*  893 */     Assert.notNull(scopeName, "Scope identifier must not be null");
/*  894 */     Assert.notNull(scope, "Scope must not be null");
/*  895 */     if ("singleton".equals(scopeName) || "prototype".equals(scopeName)) {
/*  896 */       throw new IllegalArgumentException("Cannot replace existing scopes 'singleton' and 'prototype'");
/*      */     }
/*  898 */     Scope previous = this.scopes.put(scopeName, scope);
/*  899 */     if (previous != null && previous != scope) {
/*  900 */       if (this.logger.isInfoEnabled()) {
/*  901 */         this.logger.info("Replacing scope '" + scopeName + "' from [" + previous + "] to [" + scope + "]");
/*      */       
/*      */       }
/*      */     }
/*  905 */     else if (this.logger.isDebugEnabled()) {
/*  906 */       this.logger.debug("Registering scope '" + scopeName + "' with implementation [" + scope + "]");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getRegisteredScopeNames() {
/*  913 */     return StringUtils.toStringArray(this.scopes.keySet());
/*      */   }
/*      */ 
/*      */   
/*      */   public Scope getRegisteredScope(String scopeName) {
/*  918 */     Assert.notNull(scopeName, "Scope identifier must not be null");
/*  919 */     return this.scopes.get(scopeName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSecurityContextProvider(SecurityContextProvider securityProvider) {
/*  928 */     this.securityContextProvider = securityProvider;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AccessControlContext getAccessControlContext() {
/*  937 */     return (this.securityContextProvider != null) ? this.securityContextProvider
/*  938 */       .getAccessControlContext() : 
/*  939 */       AccessController.getContext();
/*      */   }
/*      */ 
/*      */   
/*      */   public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory) {
/*  944 */     Assert.notNull(otherFactory, "BeanFactory must not be null");
/*  945 */     setBeanClassLoader(otherFactory.getBeanClassLoader());
/*  946 */     setCacheBeanMetadata(otherFactory.isCacheBeanMetadata());
/*  947 */     setBeanExpressionResolver(otherFactory.getBeanExpressionResolver());
/*  948 */     setConversionService(otherFactory.getConversionService());
/*  949 */     if (otherFactory instanceof AbstractBeanFactory) {
/*  950 */       AbstractBeanFactory otherAbstractFactory = (AbstractBeanFactory)otherFactory;
/*  951 */       this.propertyEditorRegistrars.addAll(otherAbstractFactory.propertyEditorRegistrars);
/*  952 */       this.customEditors.putAll(otherAbstractFactory.customEditors);
/*  953 */       this.typeConverter = otherAbstractFactory.typeConverter;
/*  954 */       this.beanPostProcessors.addAll(otherAbstractFactory.beanPostProcessors);
/*  955 */       this.hasInstantiationAwareBeanPostProcessors = (this.hasInstantiationAwareBeanPostProcessors || otherAbstractFactory.hasInstantiationAwareBeanPostProcessors);
/*      */       
/*  957 */       this.hasDestructionAwareBeanPostProcessors = (this.hasDestructionAwareBeanPostProcessors || otherAbstractFactory.hasDestructionAwareBeanPostProcessors);
/*      */       
/*  959 */       this.scopes.putAll(otherAbstractFactory.scopes);
/*  960 */       this.securityContextProvider = otherAbstractFactory.securityContextProvider;
/*      */     } else {
/*      */       
/*  963 */       setTypeConverter(otherFactory.getTypeConverter());
/*  964 */       String[] otherScopeNames = otherFactory.getRegisteredScopeNames();
/*  965 */       for (String scopeName : otherScopeNames) {
/*  966 */         this.scopes.put(scopeName, otherFactory.getRegisteredScope(scopeName));
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
/*      */   public BeanDefinition getMergedBeanDefinition(String name) throws BeansException {
/*  984 */     String beanName = transformedBeanName(name);
/*      */     
/*  986 */     if (!containsBeanDefinition(beanName) && getParentBeanFactory() instanceof ConfigurableBeanFactory) {
/*  987 */       return ((ConfigurableBeanFactory)getParentBeanFactory()).getMergedBeanDefinition(beanName);
/*      */     }
/*      */     
/*  990 */     return getMergedLocalBeanDefinition(beanName);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException {
/*  995 */     String beanName = transformedBeanName(name);
/*  996 */     Object beanInstance = getSingleton(beanName, false);
/*  997 */     if (beanInstance != null) {
/*  998 */       return beanInstance instanceof FactoryBean;
/*      */     }
/* 1000 */     if (containsSingleton(beanName))
/*      */     {
/* 1002 */       return false;
/*      */     }
/*      */     
/* 1005 */     if (!containsBeanDefinition(beanName) && getParentBeanFactory() instanceof ConfigurableBeanFactory)
/*      */     {
/* 1007 */       return ((ConfigurableBeanFactory)getParentBeanFactory()).isFactoryBean(name);
/*      */     }
/* 1009 */     return isFactoryBean(beanName, getMergedLocalBeanDefinition(beanName));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isActuallyInCreation(String beanName) {
/* 1014 */     return (isSingletonCurrentlyInCreation(beanName) || isPrototypeCurrentlyInCreation(beanName));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isPrototypeCurrentlyInCreation(String beanName) {
/* 1023 */     Object curVal = this.prototypesCurrentlyInCreation.get();
/* 1024 */     return (curVal != null && (curVal
/* 1025 */       .equals(beanName) || (curVal instanceof Set && ((Set)curVal).contains(beanName))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void beforePrototypeCreation(String beanName) {
/* 1036 */     Object curVal = this.prototypesCurrentlyInCreation.get();
/* 1037 */     if (curVal == null) {
/* 1038 */       this.prototypesCurrentlyInCreation.set(beanName);
/*      */     }
/* 1040 */     else if (curVal instanceof String) {
/* 1041 */       Set<String> beanNameSet = new HashSet<String>(2);
/* 1042 */       beanNameSet.add((String)curVal);
/* 1043 */       beanNameSet.add(beanName);
/* 1044 */       this.prototypesCurrentlyInCreation.set(beanNameSet);
/*      */     } else {
/*      */       
/* 1047 */       Set<String> beanNameSet = (Set<String>)curVal;
/* 1048 */       beanNameSet.add(beanName);
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
/*      */   protected void afterPrototypeCreation(String beanName) {
/* 1060 */     Object curVal = this.prototypesCurrentlyInCreation.get();
/* 1061 */     if (curVal instanceof String) {
/* 1062 */       this.prototypesCurrentlyInCreation.remove();
/*      */     }
/* 1064 */     else if (curVal instanceof Set) {
/* 1065 */       Set<String> beanNameSet = (Set<String>)curVal;
/* 1066 */       beanNameSet.remove(beanName);
/* 1067 */       if (beanNameSet.isEmpty()) {
/* 1068 */         this.prototypesCurrentlyInCreation.remove();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void destroyBean(String beanName, Object beanInstance) {
/* 1075 */     destroyBean(beanName, beanInstance, getMergedLocalBeanDefinition(beanName));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void destroyBean(String beanName, Object bean, RootBeanDefinition mbd) {
/* 1086 */     (new DisposableBeanAdapter(bean, beanName, mbd, getBeanPostProcessors(), getAccessControlContext())).destroy();
/*      */   }
/*      */ 
/*      */   
/*      */   public void destroyScopedBean(String beanName) {
/* 1091 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/* 1092 */     if (mbd.isSingleton() || mbd.isPrototype()) {
/* 1093 */       throw new IllegalArgumentException("Bean name '" + beanName + "' does not correspond to an object in a mutable scope");
/*      */     }
/*      */     
/* 1096 */     String scopeName = mbd.getScope();
/* 1097 */     Scope scope = this.scopes.get(scopeName);
/* 1098 */     if (scope == null) {
/* 1099 */       throw new IllegalStateException("No Scope SPI registered for scope name '" + scopeName + "'");
/*      */     }
/* 1101 */     Object bean = scope.remove(beanName);
/* 1102 */     if (bean != null) {
/* 1103 */       destroyBean(beanName, bean, mbd);
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
/*      */   protected String transformedBeanName(String name) {
/* 1119 */     return canonicalName(BeanFactoryUtils.transformedBeanName(name));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String originalBeanName(String name) {
/* 1128 */     String beanName = transformedBeanName(name);
/* 1129 */     if (name.startsWith("&")) {
/* 1130 */       beanName = "&" + beanName;
/*      */     }
/* 1132 */     return beanName;
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
/*      */   protected void initBeanWrapper(BeanWrapper bw) {
/* 1144 */     bw.setConversionService(getConversionService());
/* 1145 */     registerCustomEditors((PropertyEditorRegistry)bw);
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
/*      */   protected void registerCustomEditors(PropertyEditorRegistry registry) {
/* 1157 */     PropertyEditorRegistrySupport registrySupport = (registry instanceof PropertyEditorRegistrySupport) ? (PropertyEditorRegistrySupport)registry : null;
/*      */     
/* 1159 */     if (registrySupport != null) {
/* 1160 */       registrySupport.useConfigValueEditors();
/*      */     }
/* 1162 */     if (!this.propertyEditorRegistrars.isEmpty()) {
/* 1163 */       for (PropertyEditorRegistrar registrar : this.propertyEditorRegistrars) {
/*      */         try {
/* 1165 */           registrar.registerCustomEditors(registry);
/*      */         }
/* 1167 */         catch (BeanCreationException ex) {
/* 1168 */           Throwable rootCause = ex.getMostSpecificCause();
/* 1169 */           if (rootCause instanceof BeanCurrentlyInCreationException) {
/* 1170 */             BeanCreationException bce = (BeanCreationException)rootCause;
/* 1171 */             if (isCurrentlyInCreation(bce.getBeanName())) {
/* 1172 */               if (this.logger.isDebugEnabled()) {
/* 1173 */                 this.logger.debug("PropertyEditorRegistrar [" + registrar.getClass().getName() + "] failed because it tried to obtain currently created bean '" + ex
/*      */                     
/* 1175 */                     .getBeanName() + "': " + ex.getMessage());
/*      */               }
/* 1177 */               onSuppressedException((Exception)ex);
/*      */               continue;
/*      */             } 
/*      */           } 
/* 1181 */           throw ex;
/*      */         } 
/*      */       } 
/*      */     }
/* 1185 */     if (!this.customEditors.isEmpty()) {
/* 1186 */       for (Map.Entry<Class<?>, Class<? extends PropertyEditor>> entry : this.customEditors.entrySet()) {
/* 1187 */         Class<?> requiredType = entry.getKey();
/* 1188 */         Class<? extends PropertyEditor> editorClass = entry.getValue();
/* 1189 */         registry.registerCustomEditor(requiredType, (PropertyEditor)BeanUtils.instantiateClass(editorClass));
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
/*      */   protected RootBeanDefinition getMergedLocalBeanDefinition(String beanName) throws BeansException {
/* 1205 */     RootBeanDefinition mbd = this.mergedBeanDefinitions.get(beanName);
/* 1206 */     if (mbd != null) {
/* 1207 */       return mbd;
/*      */     }
/* 1209 */     return getMergedBeanDefinition(beanName, getBeanDefinition(beanName));
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
/*      */   protected RootBeanDefinition getMergedBeanDefinition(String beanName, BeanDefinition bd) throws BeanDefinitionStoreException {
/* 1223 */     return getMergedBeanDefinition(beanName, bd, (BeanDefinition)null);
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
/*      */   protected RootBeanDefinition getMergedBeanDefinition(String beanName, BeanDefinition bd, BeanDefinition containingBd) throws BeanDefinitionStoreException {
/* 1240 */     synchronized (this.mergedBeanDefinitions) {
/* 1241 */       RootBeanDefinition mbd = null;
/*      */ 
/*      */       
/* 1244 */       if (containingBd == null) {
/* 1245 */         mbd = this.mergedBeanDefinitions.get(beanName);
/*      */       }
/*      */       
/* 1248 */       if (mbd == null) {
/* 1249 */         if (bd.getParentName() == null) {
/*      */           
/* 1251 */           if (bd instanceof RootBeanDefinition) {
/* 1252 */             mbd = ((RootBeanDefinition)bd).cloneBeanDefinition();
/*      */           } else {
/*      */             
/* 1255 */             mbd = new RootBeanDefinition(bd);
/*      */           } 
/*      */         } else {
/*      */           BeanDefinition pbd;
/*      */ 
/*      */           
/*      */           try {
/* 1262 */             String parentBeanName = transformedBeanName(bd.getParentName());
/* 1263 */             if (!beanName.equals(parentBeanName)) {
/* 1264 */               pbd = getMergedBeanDefinition(parentBeanName);
/*      */             } else {
/*      */               
/* 1267 */               BeanFactory parent = getParentBeanFactory();
/* 1268 */               if (parent instanceof ConfigurableBeanFactory) {
/* 1269 */                 pbd = ((ConfigurableBeanFactory)parent).getMergedBeanDefinition(parentBeanName);
/*      */               } else {
/*      */                 
/* 1272 */                 throw new NoSuchBeanDefinitionException(parentBeanName, "Parent name '" + parentBeanName + "' is equal to bean name '" + beanName + "': cannot be resolved without an AbstractBeanFactory parent");
/*      */               }
/*      */             
/*      */             }
/*      */           
/*      */           }
/* 1278 */           catch (NoSuchBeanDefinitionException ex) {
/* 1279 */             throw new BeanDefinitionStoreException(bd.getResourceDescription(), beanName, "Could not resolve parent bean definition '" + bd
/* 1280 */                 .getParentName() + "'", ex);
/*      */           } 
/*      */           
/* 1283 */           mbd = new RootBeanDefinition(pbd);
/* 1284 */           mbd.overrideFrom(bd);
/*      */         } 
/*      */ 
/*      */         
/* 1288 */         if (!StringUtils.hasLength(mbd.getScope())) {
/* 1289 */           mbd.setScope("singleton");
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1296 */         if (containingBd != null && !containingBd.isSingleton() && mbd.isSingleton()) {
/* 1297 */           mbd.setScope(containingBd.getScope());
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1302 */         if (containingBd == null && isCacheBeanMetadata()) {
/* 1303 */           this.mergedBeanDefinitions.put(beanName, mbd);
/*      */         }
/*      */       } 
/*      */       
/* 1307 */       return mbd;
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
/*      */   protected void checkMergedBeanDefinition(RootBeanDefinition mbd, String beanName, Object[] args) throws BeanDefinitionStoreException {
/* 1322 */     if (mbd.isAbstract()) {
/* 1323 */       throw new BeanIsAbstractException(beanName);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void clearMergedBeanDefinition(String beanName) {
/* 1333 */     this.mergedBeanDefinitions.remove(beanName);
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
/*      */   public void clearMetadataCache() {
/* 1345 */     Iterator<String> mergedBeans = this.mergedBeanDefinitions.keySet().iterator();
/* 1346 */     while (mergedBeans.hasNext()) {
/* 1347 */       if (!isBeanEligibleForMetadataCaching(mergedBeans.next())) {
/* 1348 */         mergedBeans.remove();
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
/*      */   protected Class<?> resolveBeanClass(final RootBeanDefinition mbd, String beanName, Class<?>... typesToMatch) throws CannotLoadBeanClassException {
/*      */     try {
/* 1367 */       if (mbd.hasBeanClass()) {
/* 1368 */         return mbd.getBeanClass();
/*      */       }
/* 1370 */       if (System.getSecurityManager() != null) {
/* 1371 */         return AccessController.<Class<?>>doPrivileged(new PrivilegedExceptionAction<Class<?>>()
/*      */             {
/*      */               public Class<?> run() throws Exception {
/* 1374 */                 return AbstractBeanFactory.this.doResolveBeanClass(mbd, typesToMatch);
/*      */               }
/* 1376 */             }getAccessControlContext());
/*      */       }
/*      */       
/* 1379 */       return doResolveBeanClass(mbd, typesToMatch);
/*      */     
/*      */     }
/* 1382 */     catch (PrivilegedActionException pae) {
/* 1383 */       ClassNotFoundException ex = (ClassNotFoundException)pae.getException();
/* 1384 */       throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), ex);
/*      */     }
/* 1386 */     catch (ClassNotFoundException ex) {
/* 1387 */       throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), ex);
/*      */     }
/* 1389 */     catch (LinkageError err) {
/* 1390 */       throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), err);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Class<?> doResolveBeanClass(RootBeanDefinition mbd, Class<?>... typesToMatch) throws ClassNotFoundException {
/* 1397 */     ClassLoader beanClassLoader = getBeanClassLoader();
/* 1398 */     ClassLoader classLoaderToUse = beanClassLoader;
/* 1399 */     if (!ObjectUtils.isEmpty((Object[])typesToMatch)) {
/*      */ 
/*      */       
/* 1402 */       ClassLoader tempClassLoader = getTempClassLoader();
/* 1403 */       if (tempClassLoader != null) {
/* 1404 */         classLoaderToUse = tempClassLoader;
/* 1405 */         if (tempClassLoader instanceof DecoratingClassLoader) {
/* 1406 */           DecoratingClassLoader dcl = (DecoratingClassLoader)tempClassLoader;
/* 1407 */           for (Class<?> typeToMatch : typesToMatch) {
/* 1408 */             dcl.excludeClass(typeToMatch.getName());
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1413 */     String className = mbd.getBeanClassName();
/* 1414 */     if (className != null) {
/* 1415 */       Object evaluated = evaluateBeanDefinitionString(className, mbd);
/* 1416 */       if (!className.equals(evaluated)) {
/*      */         
/* 1418 */         if (evaluated instanceof Class) {
/* 1419 */           return (Class)evaluated;
/*      */         }
/* 1421 */         if (evaluated instanceof String) {
/* 1422 */           return ClassUtils.forName((String)evaluated, classLoaderToUse);
/*      */         }
/*      */         
/* 1425 */         throw new IllegalStateException("Invalid class name expression result: " + evaluated);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1430 */       if (classLoaderToUse != beanClassLoader) {
/* 1431 */         return ClassUtils.forName(className, classLoaderToUse);
/*      */       }
/*      */     } 
/* 1434 */     return mbd.resolveBeanClass(beanClassLoader);
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
/*      */   protected Object evaluateBeanDefinitionString(String value, BeanDefinition beanDefinition) {
/* 1446 */     if (this.beanExpressionResolver == null) {
/* 1447 */       return value;
/*      */     }
/* 1449 */     Scope scope = (beanDefinition != null) ? getRegisteredScope(beanDefinition.getScope()) : null;
/* 1450 */     return this.beanExpressionResolver.evaluate(value, new BeanExpressionContext(this, scope));
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
/*      */   protected Class<?> predictBeanType(String beanName, RootBeanDefinition mbd, Class<?>... typesToMatch) {
/* 1470 */     Class<?> targetType = mbd.getTargetType();
/* 1471 */     if (targetType != null) {
/* 1472 */       return targetType;
/*      */     }
/* 1474 */     if (mbd.getFactoryMethodName() != null) {
/* 1475 */       return null;
/*      */     }
/* 1477 */     return resolveBeanClass(mbd, beanName, typesToMatch);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isFactoryBean(String beanName, RootBeanDefinition mbd) {
/* 1486 */     Class<?> beanType = predictBeanType(beanName, mbd, new Class[] { FactoryBean.class });
/* 1487 */     return (beanType != null && FactoryBean.class.isAssignableFrom(beanType));
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
/*      */   protected Class<?> getTypeForFactoryBean(String beanName, RootBeanDefinition mbd) {
/* 1506 */     if (!mbd.isSingleton()) {
/* 1507 */       return null;
/*      */     }
/*      */     try {
/* 1510 */       FactoryBean<?> factoryBean = doGetBean("&" + beanName, FactoryBean.class, (Object[])null, true);
/* 1511 */       return getTypeForFactoryBean(factoryBean);
/*      */     }
/* 1513 */     catch (BeanCreationException ex) {
/* 1514 */       if (ex.contains(BeanCurrentlyInCreationException.class)) {
/* 1515 */         if (this.logger.isDebugEnabled()) {
/* 1516 */           this.logger.debug("Bean currently in creation on FactoryBean type check: " + ex);
/*      */         }
/*      */       }
/* 1519 */       else if (mbd.isLazyInit()) {
/* 1520 */         if (this.logger.isDebugEnabled()) {
/* 1521 */           this.logger.debug("Bean creation exception on lazy FactoryBean type check: " + ex);
/*      */         
/*      */         }
/*      */       }
/* 1525 */       else if (this.logger.isWarnEnabled()) {
/* 1526 */         this.logger.warn("Bean creation exception on non-lazy FactoryBean type check: " + ex);
/*      */       } 
/*      */       
/* 1529 */       onSuppressedException((Exception)ex);
/* 1530 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void markBeanAsCreated(String beanName) {
/* 1541 */     if (!this.alreadyCreated.contains(beanName)) {
/* 1542 */       synchronized (this.mergedBeanDefinitions) {
/* 1543 */         if (!this.alreadyCreated.contains(beanName)) {
/*      */ 
/*      */           
/* 1546 */           clearMergedBeanDefinition(beanName);
/* 1547 */           this.alreadyCreated.add(beanName);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void cleanupAfterBeanCreationFailure(String beanName) {
/* 1558 */     synchronized (this.mergedBeanDefinitions) {
/* 1559 */       this.alreadyCreated.remove(beanName);
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
/*      */   protected boolean isBeanEligibleForMetadataCaching(String beanName) {
/* 1571 */     return this.alreadyCreated.contains(beanName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean removeSingletonIfCreatedForTypeCheckOnly(String beanName) {
/* 1581 */     if (!this.alreadyCreated.contains(beanName)) {
/* 1582 */       removeSingleton(beanName);
/* 1583 */       return true;
/*      */     } 
/*      */     
/* 1586 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean hasBeanCreationStarted() {
/* 1597 */     return !this.alreadyCreated.isEmpty();
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
/*      */   protected Object getObjectForBeanInstance(Object beanInstance, String name, String beanName, RootBeanDefinition mbd) {
/* 1613 */     if (BeanFactoryUtils.isFactoryDereference(name) && !(beanInstance instanceof FactoryBean)) {
/* 1614 */       throw new BeanIsNotAFactoryException(transformedBeanName(name), beanInstance.getClass());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1620 */     if (!(beanInstance instanceof FactoryBean) || BeanFactoryUtils.isFactoryDereference(name)) {
/* 1621 */       return beanInstance;
/*      */     }
/*      */     
/* 1624 */     Object object = null;
/* 1625 */     if (mbd == null) {
/* 1626 */       object = getCachedObjectForFactoryBean(beanName);
/*      */     }
/* 1628 */     if (object == null) {
/*      */       
/* 1630 */       FactoryBean<?> factory = (FactoryBean)beanInstance;
/*      */       
/* 1632 */       if (mbd == null && containsBeanDefinition(beanName)) {
/* 1633 */         mbd = getMergedLocalBeanDefinition(beanName);
/*      */       }
/* 1635 */       boolean synthetic = (mbd != null && mbd.isSynthetic());
/* 1636 */       object = getObjectFromFactoryBean(factory, beanName, !synthetic);
/*      */     } 
/* 1638 */     return object;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isBeanNameInUse(String beanName) {
/* 1648 */     return (isAlias(beanName) || containsLocalBean(beanName) || hasDependentBean(beanName));
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
/*      */   protected boolean requiresDestruction(Object bean, RootBeanDefinition mbd) {
/* 1662 */     return (bean != null && (
/* 1663 */       DisposableBeanAdapter.hasDestroyMethod(bean, mbd) || (hasDestructionAwareBeanPostProcessors() && 
/* 1664 */       DisposableBeanAdapter.hasApplicableProcessors(bean, getBeanPostProcessors()))));
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
/*      */   protected void registerDisposableBeanIfNecessary(String beanName, Object bean, RootBeanDefinition mbd) {
/* 1680 */     AccessControlContext acc = (System.getSecurityManager() != null) ? getAccessControlContext() : null;
/* 1681 */     if (!mbd.isPrototype() && requiresDestruction(bean, mbd))
/* 1682 */       if (mbd.isSingleton()) {
/*      */ 
/*      */ 
/*      */         
/* 1686 */         registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, mbd, 
/* 1687 */               getBeanPostProcessors(), acc));
/*      */       }
/*      */       else {
/*      */         
/* 1691 */         Scope scope = this.scopes.get(mbd.getScope());
/* 1692 */         if (scope == null) {
/* 1693 */           throw new IllegalStateException("No Scope registered for scope name '" + mbd.getScope() + "'");
/*      */         }
/* 1695 */         scope.registerDestructionCallback(beanName, new DisposableBeanAdapter(bean, beanName, mbd, 
/* 1696 */               getBeanPostProcessors(), acc));
/*      */       }  
/*      */   }
/*      */   
/*      */   public AbstractBeanFactory() {}
/*      */   
/*      */   protected abstract boolean containsBeanDefinition(String paramString);
/*      */   
/*      */   protected abstract BeanDefinition getBeanDefinition(String paramString) throws BeansException;
/*      */   
/*      */   protected abstract Object createBean(String paramString, RootBeanDefinition paramRootBeanDefinition, Object[] paramArrayOfObject) throws BeanCreationException;
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\AbstractBeanFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */