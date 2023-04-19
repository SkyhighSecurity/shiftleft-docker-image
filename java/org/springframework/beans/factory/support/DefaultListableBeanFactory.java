/*      */ package org.springframework.beans.factory.support;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.NotSerializableException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectStreamException;
/*      */ import java.io.Serializable;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Method;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import javax.inject.Provider;
/*      */ import org.springframework.beans.BeanUtils;
/*      */ import org.springframework.beans.BeansException;
/*      */ import org.springframework.beans.TypeConverter;
/*      */ import org.springframework.beans.factory.BeanCreationException;
/*      */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*      */ import org.springframework.beans.factory.BeanFactory;
/*      */ import org.springframework.beans.factory.BeanFactoryAware;
/*      */ import org.springframework.beans.factory.BeanFactoryUtils;
/*      */ import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
/*      */ import org.springframework.beans.factory.FactoryBean;
/*      */ import org.springframework.beans.factory.InjectionPoint;
/*      */ import org.springframework.beans.factory.ListableBeanFactory;
/*      */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*      */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*      */ import org.springframework.beans.factory.ObjectFactory;
/*      */ import org.springframework.beans.factory.ObjectProvider;
/*      */ import org.springframework.beans.factory.SmartFactoryBean;
/*      */ import org.springframework.beans.factory.SmartInitializingSingleton;
/*      */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*      */ import org.springframework.beans.factory.config.BeanDefinition;
/*      */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*      */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*      */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*      */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*      */ import org.springframework.beans.factory.config.NamedBeanHolder;
/*      */ import org.springframework.core.OrderComparator;
/*      */ import org.springframework.core.ResolvableType;
/*      */ import org.springframework.core.annotation.AnnotationUtils;
/*      */ import org.springframework.lang.UsesJava8;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.CompositeIterator;
/*      */ import org.springframework.util.ObjectUtils;
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
/*      */ public class DefaultListableBeanFactory
/*      */   extends AbstractAutowireCapableBeanFactory
/*      */   implements ConfigurableListableBeanFactory, BeanDefinitionRegistry, Serializable
/*      */ {
/*  119 */   private static Class<?> javaUtilOptionalClass = null;
/*      */   
/*  121 */   private static Class<?> javaxInjectProviderClass = null;
/*      */ 
/*      */   
/*      */   static {
/*      */     try {
/*  126 */       javaUtilOptionalClass = ClassUtils.forName("java.util.Optional", DefaultListableBeanFactory.class.getClassLoader());
/*      */     }
/*  128 */     catch (ClassNotFoundException classNotFoundException) {}
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  133 */       javaxInjectProviderClass = ClassUtils.forName("javax.inject.Provider", DefaultListableBeanFactory.class.getClassLoader());
/*      */     }
/*  135 */     catch (ClassNotFoundException classNotFoundException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  142 */   private static final Map<String, Reference<DefaultListableBeanFactory>> serializableFactories = new ConcurrentHashMap<String, Reference<DefaultListableBeanFactory>>(8);
/*      */ 
/*      */ 
/*      */   
/*      */   private String serializationId;
/*      */ 
/*      */   
/*      */   private boolean allowBeanDefinitionOverriding = true;
/*      */ 
/*      */   
/*      */   private boolean allowEagerClassLoading = true;
/*      */ 
/*      */   
/*      */   private Comparator<Object> dependencyComparator;
/*      */ 
/*      */   
/*  158 */   private AutowireCandidateResolver autowireCandidateResolver = new SimpleAutowireCandidateResolver();
/*      */ 
/*      */   
/*  161 */   private final Map<Class<?>, Object> resolvableDependencies = new ConcurrentHashMap<Class<?>, Object>(16);
/*      */ 
/*      */   
/*  164 */   private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>(256);
/*      */ 
/*      */   
/*  167 */   private final Map<Class<?>, String[]> allBeanNamesByType = (Map)new ConcurrentHashMap<Class<?>, String>(64);
/*      */ 
/*      */   
/*  170 */   private final Map<Class<?>, String[]> singletonBeanNamesByType = (Map)new ConcurrentHashMap<Class<?>, String>(64);
/*      */ 
/*      */   
/*  173 */   private volatile List<String> beanDefinitionNames = new ArrayList<String>(256);
/*      */ 
/*      */   
/*  176 */   private volatile Set<String> manualSingletonNames = new LinkedHashSet<String>(16);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private volatile String[] frozenBeanDefinitionNames;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private volatile boolean configurationFrozen = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DefaultListableBeanFactory(BeanFactory parentBeanFactory) {
/*  197 */     super(parentBeanFactory);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSerializationId(String serializationId) {
/*  206 */     if (serializationId != null) {
/*  207 */       serializableFactories.put(serializationId, new WeakReference<DefaultListableBeanFactory>(this));
/*      */     }
/*  209 */     else if (this.serializationId != null) {
/*  210 */       serializableFactories.remove(this.serializationId);
/*      */     } 
/*  212 */     this.serializationId = serializationId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSerializationId() {
/*  221 */     return this.serializationId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllowBeanDefinitionOverriding(boolean allowBeanDefinitionOverriding) {
/*  232 */     this.allowBeanDefinitionOverriding = allowBeanDefinitionOverriding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAllowBeanDefinitionOverriding() {
/*  241 */     return this.allowBeanDefinitionOverriding;
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
/*      */   public void setAllowEagerClassLoading(boolean allowEagerClassLoading) {
/*  255 */     this.allowEagerClassLoading = allowEagerClassLoading;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAllowEagerClassLoading() {
/*  264 */     return this.allowEagerClassLoading;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDependencyComparator(Comparator<Object> dependencyComparator) {
/*  274 */     this.dependencyComparator = dependencyComparator;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Comparator<Object> getDependencyComparator() {
/*  282 */     return this.dependencyComparator;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutowireCandidateResolver(final AutowireCandidateResolver autowireCandidateResolver) {
/*  291 */     Assert.notNull(autowireCandidateResolver, "AutowireCandidateResolver must not be null");
/*  292 */     if (autowireCandidateResolver instanceof BeanFactoryAware) {
/*  293 */       if (System.getSecurityManager() != null) {
/*  294 */         AccessController.doPrivileged(new PrivilegedAction()
/*      */             {
/*      */               public Object run() {
/*  297 */                 ((BeanFactoryAware)autowireCandidateResolver).setBeanFactory((BeanFactory)DefaultListableBeanFactory.this);
/*  298 */                 return null;
/*      */               }
/*  300 */             },  getAccessControlContext());
/*      */       } else {
/*      */         
/*  303 */         ((BeanFactoryAware)autowireCandidateResolver).setBeanFactory((BeanFactory)this);
/*      */       } 
/*      */     }
/*  306 */     this.autowireCandidateResolver = autowireCandidateResolver;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AutowireCandidateResolver getAutowireCandidateResolver() {
/*  313 */     return this.autowireCandidateResolver;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory) {
/*  319 */     super.copyConfigurationFrom(otherFactory);
/*  320 */     if (otherFactory instanceof DefaultListableBeanFactory) {
/*  321 */       DefaultListableBeanFactory otherListableFactory = (DefaultListableBeanFactory)otherFactory;
/*  322 */       this.allowBeanDefinitionOverriding = otherListableFactory.allowBeanDefinitionOverriding;
/*  323 */       this.allowEagerClassLoading = otherListableFactory.allowEagerClassLoading;
/*  324 */       this.dependencyComparator = otherListableFactory.dependencyComparator;
/*      */       
/*  326 */       setAutowireCandidateResolver((AutowireCandidateResolver)BeanUtils.instantiateClass(getAutowireCandidateResolver().getClass()));
/*      */       
/*  328 */       this.resolvableDependencies.putAll(otherListableFactory.resolvableDependencies);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T getBean(Class<T> requiredType) throws BeansException {
/*  339 */     return getBean(requiredType, (Object[])null);
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
/*  344 */     NamedBeanHolder<T> namedBean = resolveNamedBean(requiredType, args);
/*  345 */     if (namedBean != null) {
/*  346 */       return (T)namedBean.getBeanInstance();
/*      */     }
/*  348 */     BeanFactory parent = getParentBeanFactory();
/*  349 */     if (parent != null) {
/*  350 */       return (T)parent.getBean(requiredType, args);
/*      */     }
/*  352 */     throw new NoSuchBeanDefinitionException(requiredType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean containsBeanDefinition(String beanName) {
/*  362 */     Assert.notNull(beanName, "Bean name must not be null");
/*  363 */     return this.beanDefinitionMap.containsKey(beanName);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBeanDefinitionCount() {
/*  368 */     return this.beanDefinitionMap.size();
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getBeanDefinitionNames() {
/*  373 */     String[] frozenNames = this.frozenBeanDefinitionNames;
/*  374 */     if (frozenNames != null) {
/*  375 */       return (String[])frozenNames.clone();
/*      */     }
/*      */     
/*  378 */     return StringUtils.toStringArray(this.beanDefinitionNames);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getBeanNamesForType(ResolvableType type) {
/*  384 */     return doGetBeanNamesForType(type, true, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getBeanNamesForType(Class<?> type) {
/*  389 */     return getBeanNamesForType(type, true, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
/*  394 */     if (!isConfigurationFrozen() || type == null || !allowEagerInit) {
/*  395 */       return doGetBeanNamesForType(ResolvableType.forRawClass(type), includeNonSingletons, allowEagerInit);
/*      */     }
/*  397 */     Map<Class<?>, String[]> cache = includeNonSingletons ? this.allBeanNamesByType : this.singletonBeanNamesByType;
/*      */     
/*  399 */     String[] resolvedBeanNames = cache.get(type);
/*  400 */     if (resolvedBeanNames != null) {
/*  401 */       return resolvedBeanNames;
/*      */     }
/*  403 */     resolvedBeanNames = doGetBeanNamesForType(ResolvableType.forRawClass(type), includeNonSingletons, true);
/*  404 */     if (ClassUtils.isCacheSafe(type, getBeanClassLoader())) {
/*  405 */       cache.put(type, resolvedBeanNames);
/*      */     }
/*  407 */     return resolvedBeanNames;
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
/*      */   private String[] doGetBeanNamesForType(ResolvableType type, boolean includeNonSingletons, boolean allowEagerInit) {
/*      */     // Byte code:
/*      */     //   0: new java/util/ArrayList
/*      */     //   3: dup
/*      */     //   4: invokespecial <init> : ()V
/*      */     //   7: astore #4
/*      */     //   9: aload_0
/*      */     //   10: getfield beanDefinitionNames : Ljava/util/List;
/*      */     //   13: invokeinterface iterator : ()Ljava/util/Iterator;
/*      */     //   18: astore #5
/*      */     //   20: aload #5
/*      */     //   22: invokeinterface hasNext : ()Z
/*      */     //   27: ifeq -> 411
/*      */     //   30: aload #5
/*      */     //   32: invokeinterface next : ()Ljava/lang/Object;
/*      */     //   37: checkcast java/lang/String
/*      */     //   40: astore #6
/*      */     //   42: aload_0
/*      */     //   43: aload #6
/*      */     //   45: invokevirtual isAlias : (Ljava/lang/String;)Z
/*      */     //   48: ifne -> 408
/*      */     //   51: aload_0
/*      */     //   52: aload #6
/*      */     //   54: invokevirtual getMergedLocalBeanDefinition : (Ljava/lang/String;)Lorg/springframework/beans/factory/support/RootBeanDefinition;
/*      */     //   57: astore #7
/*      */     //   59: aload #7
/*      */     //   61: invokevirtual isAbstract : ()Z
/*      */     //   64: ifne -> 276
/*      */     //   67: iload_3
/*      */     //   68: ifne -> 106
/*      */     //   71: aload #7
/*      */     //   73: invokevirtual hasBeanClass : ()Z
/*      */     //   76: ifne -> 94
/*      */     //   79: aload #7
/*      */     //   81: invokevirtual isLazyInit : ()Z
/*      */     //   84: ifeq -> 94
/*      */     //   87: aload_0
/*      */     //   88: invokevirtual isAllowEagerClassLoading : ()Z
/*      */     //   91: ifeq -> 276
/*      */     //   94: aload_0
/*      */     //   95: aload #7
/*      */     //   97: invokevirtual getFactoryBeanName : ()Ljava/lang/String;
/*      */     //   100: invokespecial requiresEagerInitForType : (Ljava/lang/String;)Z
/*      */     //   103: ifne -> 276
/*      */     //   106: aload_0
/*      */     //   107: aload #6
/*      */     //   109: aload #7
/*      */     //   111: invokevirtual isFactoryBean : (Ljava/lang/String;Lorg/springframework/beans/factory/support/RootBeanDefinition;)Z
/*      */     //   114: istore #8
/*      */     //   116: aload #7
/*      */     //   118: invokevirtual getDecoratedDefinition : ()Lorg/springframework/beans/factory/config/BeanDefinitionHolder;
/*      */     //   121: astore #9
/*      */     //   123: iload_3
/*      */     //   124: ifne -> 154
/*      */     //   127: iload #8
/*      */     //   129: ifeq -> 154
/*      */     //   132: aload #9
/*      */     //   134: ifnull -> 145
/*      */     //   137: aload #7
/*      */     //   139: invokevirtual isLazyInit : ()Z
/*      */     //   142: ifeq -> 154
/*      */     //   145: aload_0
/*      */     //   146: aload #6
/*      */     //   148: invokevirtual containsSingleton : (Ljava/lang/String;)Z
/*      */     //   151: ifeq -> 197
/*      */     //   154: iload_2
/*      */     //   155: ifne -> 183
/*      */     //   158: aload #9
/*      */     //   160: ifnull -> 174
/*      */     //   163: aload #7
/*      */     //   165: invokevirtual isSingleton : ()Z
/*      */     //   168: ifeq -> 197
/*      */     //   171: goto -> 183
/*      */     //   174: aload_0
/*      */     //   175: aload #6
/*      */     //   177: invokevirtual isSingleton : (Ljava/lang/String;)Z
/*      */     //   180: ifeq -> 197
/*      */     //   183: aload_0
/*      */     //   184: aload #6
/*      */     //   186: aload_1
/*      */     //   187: invokevirtual isTypeMatch : (Ljava/lang/String;Lorg/springframework/core/ResolvableType;)Z
/*      */     //   190: ifeq -> 197
/*      */     //   193: iconst_1
/*      */     //   194: goto -> 198
/*      */     //   197: iconst_0
/*      */     //   198: istore #10
/*      */     //   200: iload #10
/*      */     //   202: ifne -> 261
/*      */     //   205: iload #8
/*      */     //   207: ifeq -> 261
/*      */     //   210: new java/lang/StringBuilder
/*      */     //   213: dup
/*      */     //   214: invokespecial <init> : ()V
/*      */     //   217: ldc '&'
/*      */     //   219: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   222: aload #6
/*      */     //   224: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   227: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   230: astore #6
/*      */     //   232: iload_2
/*      */     //   233: ifne -> 244
/*      */     //   236: aload #7
/*      */     //   238: invokevirtual isSingleton : ()Z
/*      */     //   241: ifeq -> 258
/*      */     //   244: aload_0
/*      */     //   245: aload #6
/*      */     //   247: aload_1
/*      */     //   248: invokevirtual isTypeMatch : (Ljava/lang/String;Lorg/springframework/core/ResolvableType;)Z
/*      */     //   251: ifeq -> 258
/*      */     //   254: iconst_1
/*      */     //   255: goto -> 259
/*      */     //   258: iconst_0
/*      */     //   259: istore #10
/*      */     //   261: iload #10
/*      */     //   263: ifeq -> 276
/*      */     //   266: aload #4
/*      */     //   268: aload #6
/*      */     //   270: invokeinterface add : (Ljava/lang/Object;)Z
/*      */     //   275: pop
/*      */     //   276: goto -> 408
/*      */     //   279: astore #7
/*      */     //   281: iload_3
/*      */     //   282: ifeq -> 288
/*      */     //   285: aload #7
/*      */     //   287: athrow
/*      */     //   288: aload_0
/*      */     //   289: getfield logger : Lorg/apache/commons/logging/Log;
/*      */     //   292: invokeinterface isDebugEnabled : ()Z
/*      */     //   297: ifeq -> 336
/*      */     //   300: aload_0
/*      */     //   301: getfield logger : Lorg/apache/commons/logging/Log;
/*      */     //   304: new java/lang/StringBuilder
/*      */     //   307: dup
/*      */     //   308: invokespecial <init> : ()V
/*      */     //   311: ldc 'Ignoring bean class loading failure for bean ''
/*      */     //   313: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   316: aload #6
/*      */     //   318: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   321: ldc '''
/*      */     //   323: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   326: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   329: aload #7
/*      */     //   331: invokeinterface debug : (Ljava/lang/Object;Ljava/lang/Throwable;)V
/*      */     //   336: aload_0
/*      */     //   337: aload #7
/*      */     //   339: invokevirtual onSuppressedException : (Ljava/lang/Exception;)V
/*      */     //   342: goto -> 408
/*      */     //   345: astore #7
/*      */     //   347: iload_3
/*      */     //   348: ifeq -> 354
/*      */     //   351: aload #7
/*      */     //   353: athrow
/*      */     //   354: aload_0
/*      */     //   355: getfield logger : Lorg/apache/commons/logging/Log;
/*      */     //   358: invokeinterface isDebugEnabled : ()Z
/*      */     //   363: ifeq -> 402
/*      */     //   366: aload_0
/*      */     //   367: getfield logger : Lorg/apache/commons/logging/Log;
/*      */     //   370: new java/lang/StringBuilder
/*      */     //   373: dup
/*      */     //   374: invokespecial <init> : ()V
/*      */     //   377: ldc 'Ignoring unresolvable metadata in bean definition ''
/*      */     //   379: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   382: aload #6
/*      */     //   384: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   387: ldc '''
/*      */     //   389: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   392: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   395: aload #7
/*      */     //   397: invokeinterface debug : (Ljava/lang/Object;Ljava/lang/Throwable;)V
/*      */     //   402: aload_0
/*      */     //   403: aload #7
/*      */     //   405: invokevirtual onSuppressedException : (Ljava/lang/Exception;)V
/*      */     //   408: goto -> 20
/*      */     //   411: aload_0
/*      */     //   412: getfield manualSingletonNames : Ljava/util/Set;
/*      */     //   415: invokeinterface iterator : ()Ljava/util/Iterator;
/*      */     //   420: astore #5
/*      */     //   422: aload #5
/*      */     //   424: invokeinterface hasNext : ()Z
/*      */     //   429: ifeq -> 587
/*      */     //   432: aload #5
/*      */     //   434: invokeinterface next : ()Ljava/lang/Object;
/*      */     //   439: checkcast java/lang/String
/*      */     //   442: astore #6
/*      */     //   444: aload_0
/*      */     //   445: aload #6
/*      */     //   447: invokevirtual isFactoryBean : (Ljava/lang/String;)Z
/*      */     //   450: ifeq -> 511
/*      */     //   453: iload_2
/*      */     //   454: ifne -> 466
/*      */     //   457: aload_0
/*      */     //   458: aload #6
/*      */     //   460: invokevirtual isSingleton : (Ljava/lang/String;)Z
/*      */     //   463: ifeq -> 489
/*      */     //   466: aload_0
/*      */     //   467: aload #6
/*      */     //   469: aload_1
/*      */     //   470: invokevirtual isTypeMatch : (Ljava/lang/String;Lorg/springframework/core/ResolvableType;)Z
/*      */     //   473: ifeq -> 489
/*      */     //   476: aload #4
/*      */     //   478: aload #6
/*      */     //   480: invokeinterface add : (Ljava/lang/Object;)Z
/*      */     //   485: pop
/*      */     //   486: goto -> 422
/*      */     //   489: new java/lang/StringBuilder
/*      */     //   492: dup
/*      */     //   493: invokespecial <init> : ()V
/*      */     //   496: ldc '&'
/*      */     //   498: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   501: aload #6
/*      */     //   503: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   506: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   509: astore #6
/*      */     //   511: aload_0
/*      */     //   512: aload #6
/*      */     //   514: aload_1
/*      */     //   515: invokevirtual isTypeMatch : (Ljava/lang/String;Lorg/springframework/core/ResolvableType;)Z
/*      */     //   518: ifeq -> 531
/*      */     //   521: aload #4
/*      */     //   523: aload #6
/*      */     //   525: invokeinterface add : (Ljava/lang/Object;)Z
/*      */     //   530: pop
/*      */     //   531: goto -> 584
/*      */     //   534: astore #7
/*      */     //   536: aload_0
/*      */     //   537: getfield logger : Lorg/apache/commons/logging/Log;
/*      */     //   540: invokeinterface isDebugEnabled : ()Z
/*      */     //   545: ifeq -> 584
/*      */     //   548: aload_0
/*      */     //   549: getfield logger : Lorg/apache/commons/logging/Log;
/*      */     //   552: new java/lang/StringBuilder
/*      */     //   555: dup
/*      */     //   556: invokespecial <init> : ()V
/*      */     //   559: ldc 'Failed to check manually registered singleton with name ''
/*      */     //   561: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   564: aload #6
/*      */     //   566: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   569: ldc '''
/*      */     //   571: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   574: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   577: aload #7
/*      */     //   579: invokeinterface debug : (Ljava/lang/Object;Ljava/lang/Throwable;)V
/*      */     //   584: goto -> 422
/*      */     //   587: aload #4
/*      */     //   589: invokestatic toStringArray : (Ljava/util/Collection;)[Ljava/lang/String;
/*      */     //   592: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #411	-> 0
/*      */     //   #414	-> 9
/*      */     //   #417	-> 42
/*      */     //   #419	-> 51
/*      */     //   #421	-> 59
/*      */     //   #422	-> 73
/*      */     //   #423	-> 97
/*      */     //   #425	-> 106
/*      */     //   #426	-> 116
/*      */     //   #427	-> 123
/*      */     //   #429	-> 139
/*      */     //   #431	-> 165
/*      */     //   #432	-> 187
/*      */     //   #433	-> 200
/*      */     //   #435	-> 210
/*      */     //   #436	-> 232
/*      */     //   #438	-> 261
/*      */     //   #439	-> 266
/*      */     //   #462	-> 276
/*      */     //   #443	-> 279
/*      */     //   #444	-> 281
/*      */     //   #445	-> 285
/*      */     //   #448	-> 288
/*      */     //   #449	-> 300
/*      */     //   #451	-> 336
/*      */     //   #462	-> 342
/*      */     //   #453	-> 345
/*      */     //   #454	-> 347
/*      */     //   #455	-> 351
/*      */     //   #458	-> 354
/*      */     //   #459	-> 366
/*      */     //   #461	-> 402
/*      */     //   #464	-> 408
/*      */     //   #467	-> 411
/*      */     //   #470	-> 444
/*      */     //   #471	-> 453
/*      */     //   #472	-> 476
/*      */     //   #474	-> 486
/*      */     //   #477	-> 489
/*      */     //   #480	-> 511
/*      */     //   #481	-> 521
/*      */     //   #489	-> 531
/*      */     //   #484	-> 534
/*      */     //   #486	-> 536
/*      */     //   #487	-> 548
/*      */     //   #490	-> 584
/*      */     //   #492	-> 587
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   116	160	8	isFactoryBean	Z
/*      */     //   123	153	9	dbd	Lorg/springframework/beans/factory/config/BeanDefinitionHolder;
/*      */     //   200	76	10	matchFound	Z
/*      */     //   59	217	7	mbd	Lorg/springframework/beans/factory/support/RootBeanDefinition;
/*      */     //   281	61	7	ex	Lorg/springframework/beans/factory/CannotLoadBeanClassException;
/*      */     //   347	61	7	ex	Lorg/springframework/beans/factory/BeanDefinitionStoreException;
/*      */     //   42	366	6	beanName	Ljava/lang/String;
/*      */     //   536	48	7	ex	Lorg/springframework/beans/factory/NoSuchBeanDefinitionException;
/*      */     //   444	140	6	beanName	Ljava/lang/String;
/*      */     //   0	593	0	this	Lorg/springframework/beans/factory/support/DefaultListableBeanFactory;
/*      */     //   0	593	1	type	Lorg/springframework/core/ResolvableType;
/*      */     //   0	593	2	includeNonSingletons	Z
/*      */     //   0	593	3	allowEagerInit	Z
/*      */     //   9	584	4	result	Ljava/util/List;
/*      */     // Local variable type table:
/*      */     //   start	length	slot	name	signature
/*      */     //   9	584	4	result	Ljava/util/List<Ljava/lang/String;>;
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   51	276	279	org/springframework/beans/factory/CannotLoadBeanClassException
/*      */     //   51	276	345	org/springframework/beans/factory/BeanDefinitionStoreException
/*      */     //   444	486	534	org/springframework/beans/factory/NoSuchBeanDefinitionException
/*      */     //   489	531	534	org/springframework/beans/factory/NoSuchBeanDefinitionException
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
/*      */   private boolean requiresEagerInitForType(String factoryBeanName) {
/*  503 */     return (factoryBeanName != null && isFactoryBean(factoryBeanName) && !containsSingleton(factoryBeanName));
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
/*  508 */     return getBeansOfType(type, true, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
/*  515 */     String[] beanNames = getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
/*  516 */     Map<String, T> result = new LinkedHashMap<String, T>(beanNames.length);
/*  517 */     for (String beanName : beanNames) {
/*      */       try {
/*  519 */         result.put(beanName, getBean(beanName, type));
/*      */       }
/*  521 */       catch (BeanCreationException ex) {
/*  522 */         Throwable rootCause = ex.getMostSpecificCause();
/*  523 */         if (rootCause instanceof org.springframework.beans.factory.BeanCurrentlyInCreationException)
/*  524 */         { BeanCreationException bce = (BeanCreationException)rootCause;
/*  525 */           if (isCurrentlyInCreation(bce.getBeanName()))
/*  526 */           { if (this.logger.isDebugEnabled()) {
/*  527 */               this.logger.debug("Ignoring match to currently created bean '" + beanName + "': " + ex
/*  528 */                   .getMessage());
/*      */             }
/*  530 */             onSuppressedException((Exception)ex);
/*      */              }
/*      */           
/*      */           else
/*      */           
/*      */           { 
/*  536 */             throw ex; }  } else { throw ex; }
/*      */       
/*      */       } 
/*  539 */     }  return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
/*  544 */     List<String> result = new ArrayList<String>();
/*  545 */     for (String beanName : this.beanDefinitionNames) {
/*  546 */       BeanDefinition beanDefinition = getBeanDefinition(beanName);
/*  547 */       if (!beanDefinition.isAbstract() && findAnnotationOnBean(beanName, annotationType) != null) {
/*  548 */         result.add(beanName);
/*      */       }
/*      */     } 
/*  551 */     for (String beanName : this.manualSingletonNames) {
/*  552 */       if (!result.contains(beanName) && findAnnotationOnBean(beanName, annotationType) != null) {
/*  553 */         result.add(beanName);
/*      */       }
/*      */     } 
/*  556 */     return StringUtils.toStringArray(result);
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
/*  561 */     String[] beanNames = getBeanNamesForAnnotation(annotationType);
/*  562 */     Map<String, Object> result = new LinkedHashMap<String, Object>(beanNames.length);
/*  563 */     for (String beanName : beanNames) {
/*  564 */       result.put(beanName, getBean(beanName));
/*      */     }
/*  566 */     return result;
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
/*      */   public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) throws NoSuchBeanDefinitionException {
/*      */     Annotation annotation;
/*  579 */     A ann = null;
/*  580 */     Class<?> beanType = getType(beanName);
/*  581 */     if (beanType != null) {
/*  582 */       annotation = AnnotationUtils.findAnnotation(beanType, annotationType);
/*      */     }
/*  584 */     if (annotation == null && containsBeanDefinition(beanName)) {
/*  585 */       BeanDefinition bd = getMergedBeanDefinition(beanName);
/*  586 */       if (bd instanceof AbstractBeanDefinition) {
/*  587 */         AbstractBeanDefinition abd = (AbstractBeanDefinition)bd;
/*  588 */         if (abd.hasBeanClass()) {
/*  589 */           annotation = AnnotationUtils.findAnnotation(abd.getBeanClass(), annotationType);
/*      */         }
/*      */       } 
/*      */     } 
/*  593 */     return (A)annotation;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void registerResolvableDependency(Class<?> dependencyType, Object autowiredValue) {
/*  603 */     Assert.notNull(dependencyType, "Dependency type must not be null");
/*  604 */     if (autowiredValue != null) {
/*  605 */       if (!(autowiredValue instanceof ObjectFactory) && !dependencyType.isInstance(autowiredValue)) {
/*  606 */         throw new IllegalArgumentException("Value [" + autowiredValue + "] does not implement specified dependency type [" + dependencyType
/*  607 */             .getName() + "]");
/*      */       }
/*  609 */       this.resolvableDependencies.put(dependencyType, autowiredValue);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAutowireCandidate(String beanName, DependencyDescriptor descriptor) throws NoSuchBeanDefinitionException {
/*  617 */     return isAutowireCandidate(beanName, descriptor, getAutowireCandidateResolver());
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
/*      */   protected boolean isAutowireCandidate(String beanName, DependencyDescriptor descriptor, AutowireCandidateResolver resolver) throws NoSuchBeanDefinitionException {
/*  631 */     String beanDefinitionName = BeanFactoryUtils.transformedBeanName(beanName);
/*  632 */     if (containsBeanDefinition(beanDefinitionName)) {
/*  633 */       return isAutowireCandidate(beanName, getMergedLocalBeanDefinition(beanDefinitionName), descriptor, resolver);
/*      */     }
/*  635 */     if (containsSingleton(beanName)) {
/*  636 */       return isAutowireCandidate(beanName, new RootBeanDefinition(getType(beanName)), descriptor, resolver);
/*      */     }
/*      */     
/*  639 */     BeanFactory parent = getParentBeanFactory();
/*  640 */     if (parent instanceof DefaultListableBeanFactory)
/*      */     {
/*  642 */       return ((DefaultListableBeanFactory)parent).isAutowireCandidate(beanName, descriptor, resolver);
/*      */     }
/*  644 */     if (parent instanceof ConfigurableListableBeanFactory)
/*      */     {
/*  646 */       return ((ConfigurableListableBeanFactory)parent).isAutowireCandidate(beanName, descriptor);
/*      */     }
/*      */     
/*  649 */     return true;
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
/*      */   protected boolean isAutowireCandidate(String beanName, RootBeanDefinition mbd, DependencyDescriptor descriptor, AutowireCandidateResolver resolver) {
/*  665 */     String beanDefinitionName = BeanFactoryUtils.transformedBeanName(beanName);
/*  666 */     resolveBeanClass(mbd, beanDefinitionName, new Class[0]);
/*  667 */     if (mbd.isFactoryMethodUnique) {
/*      */       boolean resolve;
/*  669 */       synchronized (mbd.constructorArgumentLock) {
/*  670 */         resolve = (mbd.resolvedConstructorOrFactoryMethod == null);
/*      */       } 
/*  672 */       if (resolve) {
/*  673 */         (new ConstructorResolver(this)).resolveFactoryMethodIfPossible(mbd);
/*      */       }
/*      */     } 
/*  676 */     return resolver.isAutowireCandidate(new BeanDefinitionHolder(mbd, beanName, 
/*  677 */           getAliases(beanDefinitionName)), descriptor);
/*      */   }
/*      */ 
/*      */   
/*      */   public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
/*  682 */     BeanDefinition bd = this.beanDefinitionMap.get(beanName);
/*  683 */     if (bd == null) {
/*  684 */       if (this.logger.isTraceEnabled()) {
/*  685 */         this.logger.trace("No bean named '" + beanName + "' found in " + this);
/*      */       }
/*  687 */       throw new NoSuchBeanDefinitionException(beanName);
/*      */     } 
/*  689 */     return bd;
/*      */   }
/*      */ 
/*      */   
/*      */   public Iterator<String> getBeanNamesIterator() {
/*  694 */     CompositeIterator<String> iterator = new CompositeIterator();
/*  695 */     iterator.add(this.beanDefinitionNames.iterator());
/*  696 */     iterator.add(this.manualSingletonNames.iterator());
/*  697 */     return (Iterator<String>)iterator;
/*      */   }
/*      */ 
/*      */   
/*      */   public void clearMetadataCache() {
/*  702 */     super.clearMetadataCache();
/*  703 */     clearByTypeCache();
/*      */   }
/*      */ 
/*      */   
/*      */   public void freezeConfiguration() {
/*  708 */     this.configurationFrozen = true;
/*  709 */     this.frozenBeanDefinitionNames = StringUtils.toStringArray(this.beanDefinitionNames);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isConfigurationFrozen() {
/*  714 */     return this.configurationFrozen;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isBeanEligibleForMetadataCaching(String beanName) {
/*  724 */     return (this.configurationFrozen || super.isBeanEligibleForMetadataCaching(beanName));
/*      */   }
/*      */ 
/*      */   
/*      */   public void preInstantiateSingletons() throws BeansException {
/*  729 */     if (this.logger.isDebugEnabled()) {
/*  730 */       this.logger.debug("Pre-instantiating singletons in " + this);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  735 */     List<String> beanNames = new ArrayList<String>(this.beanDefinitionNames);
/*      */ 
/*      */     
/*  738 */     for (String beanName : beanNames) {
/*  739 */       RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName);
/*  740 */       if (!bd.isAbstract() && bd.isSingleton() && !bd.isLazyInit()) {
/*  741 */         if (isFactoryBean(beanName)) {
/*  742 */           boolean isEagerInit; final FactoryBean<?> factory = (FactoryBean)getBean("&" + beanName);
/*      */           
/*  744 */           if (System.getSecurityManager() != null && factory instanceof SmartFactoryBean) {
/*  745 */             isEagerInit = ((Boolean)AccessController.<Boolean>doPrivileged(new PrivilegedAction<Boolean>()
/*      */                 {
/*      */                   public Boolean run() {
/*  748 */                     return Boolean.valueOf(((SmartFactoryBean)factory).isEagerInit());
/*      */                   }
/*  750 */                 },  getAccessControlContext())).booleanValue();
/*      */           }
/*      */           else {
/*      */             
/*  754 */             isEagerInit = (factory instanceof SmartFactoryBean && ((SmartFactoryBean)factory).isEagerInit());
/*      */           } 
/*  756 */           if (isEagerInit) {
/*  757 */             getBean(beanName);
/*      */           }
/*      */           continue;
/*      */         } 
/*  761 */         getBean(beanName);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  767 */     for (String beanName : beanNames) {
/*  768 */       Object singletonInstance = getSingleton(beanName);
/*  769 */       if (singletonInstance instanceof SmartInitializingSingleton) {
/*  770 */         final SmartInitializingSingleton smartSingleton = (SmartInitializingSingleton)singletonInstance;
/*  771 */         if (System.getSecurityManager() != null) {
/*  772 */           AccessController.doPrivileged(new PrivilegedAction()
/*      */               {
/*      */                 public Object run() {
/*  775 */                   smartSingleton.afterSingletonsInstantiated();
/*  776 */                   return null;
/*      */                 }
/*  778 */               },  getAccessControlContext());
/*      */           continue;
/*      */         } 
/*  781 */         smartSingleton.afterSingletonsInstantiated();
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
/*      */   public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
/*  796 */     Assert.hasText(beanName, "Bean name must not be empty");
/*  797 */     Assert.notNull(beanDefinition, "BeanDefinition must not be null");
/*      */     
/*  799 */     if (beanDefinition instanceof AbstractBeanDefinition) {
/*      */       try {
/*  801 */         ((AbstractBeanDefinition)beanDefinition).validate();
/*      */       }
/*  803 */       catch (BeanDefinitionValidationException ex) {
/*  804 */         throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName, "Validation of bean definition failed", ex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  809 */     BeanDefinition existingDefinition = this.beanDefinitionMap.get(beanName);
/*  810 */     if (existingDefinition != null) {
/*  811 */       if (!isAllowBeanDefinitionOverriding()) {
/*  812 */         throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName, "Cannot register bean definition [" + beanDefinition + "] for bean '" + beanName + "': There is already [" + existingDefinition + "] bound.");
/*      */       }
/*      */ 
/*      */       
/*  816 */       if (existingDefinition.getRole() < beanDefinition.getRole()) {
/*      */         
/*  818 */         if (this.logger.isWarnEnabled()) {
/*  819 */           this.logger.warn("Overriding user-defined bean definition for bean '" + beanName + "' with a framework-generated bean definition: replacing [" + existingDefinition + "] with [" + beanDefinition + "]");
/*      */         
/*      */         }
/*      */       
/*      */       }
/*  824 */       else if (!beanDefinition.equals(existingDefinition)) {
/*  825 */         if (this.logger.isInfoEnabled()) {
/*  826 */           this.logger.info("Overriding bean definition for bean '" + beanName + "' with a different definition: replacing [" + existingDefinition + "] with [" + beanDefinition + "]");
/*      */ 
/*      */         
/*      */         }
/*      */       
/*      */       }
/*  832 */       else if (this.logger.isDebugEnabled()) {
/*  833 */         this.logger.debug("Overriding bean definition for bean '" + beanName + "' with an equivalent definition: replacing [" + existingDefinition + "] with [" + beanDefinition + "]");
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  838 */       this.beanDefinitionMap.put(beanName, beanDefinition);
/*      */     } else {
/*      */       
/*  841 */       if (hasBeanCreationStarted()) {
/*      */         
/*  843 */         synchronized (this.beanDefinitionMap) {
/*  844 */           this.beanDefinitionMap.put(beanName, beanDefinition);
/*  845 */           List<String> updatedDefinitions = new ArrayList<String>(this.beanDefinitionNames.size() + 1);
/*  846 */           updatedDefinitions.addAll(this.beanDefinitionNames);
/*  847 */           updatedDefinitions.add(beanName);
/*  848 */           this.beanDefinitionNames = updatedDefinitions;
/*  849 */           if (this.manualSingletonNames.contains(beanName)) {
/*  850 */             Set<String> updatedSingletons = new LinkedHashSet<String>(this.manualSingletonNames);
/*  851 */             updatedSingletons.remove(beanName);
/*  852 */             this.manualSingletonNames = updatedSingletons;
/*      */           }
/*      */         
/*      */         } 
/*      */       } else {
/*      */         
/*  858 */         this.beanDefinitionMap.put(beanName, beanDefinition);
/*  859 */         this.beanDefinitionNames.add(beanName);
/*  860 */         this.manualSingletonNames.remove(beanName);
/*      */       } 
/*  862 */       this.frozenBeanDefinitionNames = null;
/*      */     } 
/*      */     
/*  865 */     if (existingDefinition != null || containsSingleton(beanName)) {
/*  866 */       resetBeanDefinition(beanName);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
/*  872 */     Assert.hasText(beanName, "'beanName' must not be empty");
/*      */     
/*  874 */     BeanDefinition bd = this.beanDefinitionMap.remove(beanName);
/*  875 */     if (bd == null) {
/*  876 */       if (this.logger.isTraceEnabled()) {
/*  877 */         this.logger.trace("No bean named '" + beanName + "' found in " + this);
/*      */       }
/*  879 */       throw new NoSuchBeanDefinitionException(beanName);
/*      */     } 
/*      */     
/*  882 */     if (hasBeanCreationStarted()) {
/*      */       
/*  884 */       synchronized (this.beanDefinitionMap) {
/*  885 */         List<String> updatedDefinitions = new ArrayList<String>(this.beanDefinitionNames);
/*  886 */         updatedDefinitions.remove(beanName);
/*  887 */         this.beanDefinitionNames = updatedDefinitions;
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  892 */       this.beanDefinitionNames.remove(beanName);
/*      */     } 
/*  894 */     this.frozenBeanDefinitionNames = null;
/*      */     
/*  896 */     resetBeanDefinition(beanName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void resetBeanDefinition(String beanName) {
/*  906 */     clearMergedBeanDefinition(beanName);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  911 */     destroySingleton(beanName);
/*      */ 
/*      */     
/*  914 */     for (String bdName : this.beanDefinitionNames) {
/*  915 */       if (!beanName.equals(bdName)) {
/*  916 */         BeanDefinition bd = this.beanDefinitionMap.get(bdName);
/*  917 */         if (beanName.equals(bd.getParentName())) {
/*  918 */           resetBeanDefinition(bdName);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean allowAliasOverriding() {
/*  929 */     return isAllowBeanDefinitionOverriding();
/*      */   }
/*      */ 
/*      */   
/*      */   public void registerSingleton(String beanName, Object singletonObject) throws IllegalStateException {
/*  934 */     super.registerSingleton(beanName, singletonObject);
/*      */     
/*  936 */     if (hasBeanCreationStarted()) {
/*      */       
/*  938 */       synchronized (this.beanDefinitionMap) {
/*  939 */         if (!this.beanDefinitionMap.containsKey(beanName)) {
/*  940 */           Set<String> updatedSingletons = new LinkedHashSet<String>(this.manualSingletonNames.size() + 1);
/*  941 */           updatedSingletons.addAll(this.manualSingletonNames);
/*  942 */           updatedSingletons.add(beanName);
/*  943 */           this.manualSingletonNames = updatedSingletons;
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  949 */     else if (!this.beanDefinitionMap.containsKey(beanName)) {
/*  950 */       this.manualSingletonNames.add(beanName);
/*      */     } 
/*      */ 
/*      */     
/*  954 */     clearByTypeCache();
/*      */   }
/*      */ 
/*      */   
/*      */   public void destroySingleton(String beanName) {
/*  959 */     super.destroySingleton(beanName);
/*  960 */     this.manualSingletonNames.remove(beanName);
/*  961 */     clearByTypeCache();
/*      */   }
/*      */ 
/*      */   
/*      */   public void destroySingletons() {
/*  966 */     super.destroySingletons();
/*  967 */     this.manualSingletonNames.clear();
/*  968 */     clearByTypeCache();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void clearByTypeCache() {
/*  975 */     this.allBeanNamesByType.clear();
/*  976 */     this.singletonBeanNamesByType.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> NamedBeanHolder<T> resolveNamedBean(Class<T> requiredType) throws BeansException {
/*  986 */     NamedBeanHolder<T> namedBean = resolveNamedBean(requiredType, (Object[])null);
/*  987 */     if (namedBean != null) {
/*  988 */       return namedBean;
/*      */     }
/*  990 */     BeanFactory parent = getParentBeanFactory();
/*  991 */     if (parent instanceof AutowireCapableBeanFactory) {
/*  992 */       return ((AutowireCapableBeanFactory)parent).resolveNamedBean(requiredType);
/*      */     }
/*  994 */     throw new NoSuchBeanDefinitionException(requiredType);
/*      */   }
/*      */ 
/*      */   
/*      */   private <T> NamedBeanHolder<T> resolveNamedBean(Class<T> requiredType, Object... args) throws BeansException {
/*  999 */     Assert.notNull(requiredType, "Required type must not be null");
/* 1000 */     String[] candidateNames = getBeanNamesForType(requiredType);
/*      */     
/* 1002 */     if (candidateNames.length > 1) {
/* 1003 */       List<String> autowireCandidates = new ArrayList<String>(candidateNames.length);
/* 1004 */       for (String beanName : candidateNames) {
/* 1005 */         if (!containsBeanDefinition(beanName) || getBeanDefinition(beanName).isAutowireCandidate()) {
/* 1006 */           autowireCandidates.add(beanName);
/*      */         }
/*      */       } 
/* 1009 */       if (!autowireCandidates.isEmpty()) {
/* 1010 */         candidateNames = StringUtils.toStringArray(autowireCandidates);
/*      */       }
/*      */     } 
/*      */     
/* 1014 */     if (candidateNames.length == 1) {
/* 1015 */       String beanName = candidateNames[0];
/* 1016 */       return new NamedBeanHolder(beanName, getBean(beanName, requiredType, args));
/*      */     } 
/* 1018 */     if (candidateNames.length > 1) {
/* 1019 */       Map<String, Object> candidates = new LinkedHashMap<String, Object>(candidateNames.length);
/* 1020 */       for (String beanName : candidateNames) {
/* 1021 */         if (containsSingleton(beanName)) {
/* 1022 */           candidates.put(beanName, getBean(beanName, requiredType, args));
/*      */         } else {
/*      */           
/* 1025 */           candidates.put(beanName, getType(beanName));
/*      */         } 
/*      */       } 
/* 1028 */       String candidateName = determinePrimaryCandidate(candidates, requiredType);
/* 1029 */       if (candidateName == null) {
/* 1030 */         candidateName = determineHighestPriorityCandidate(candidates, requiredType);
/*      */       }
/* 1032 */       if (candidateName != null) {
/* 1033 */         Object beanInstance = candidates.get(candidateName);
/* 1034 */         if (beanInstance instanceof Class) {
/* 1035 */           beanInstance = getBean(candidateName, requiredType, args);
/*      */         }
/* 1037 */         return new NamedBeanHolder(candidateName, beanInstance);
/*      */       } 
/* 1039 */       throw new NoUniqueBeanDefinitionException(requiredType, candidates.keySet());
/*      */     } 
/*      */     
/* 1042 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object resolveDependency(DependencyDescriptor descriptor, String requestingBeanName, Set<String> autowiredBeanNames, TypeConverter typeConverter) throws BeansException {
/* 1049 */     descriptor.initParameterNameDiscovery(getParameterNameDiscoverer());
/* 1050 */     if (javaUtilOptionalClass == descriptor.getDependencyType()) {
/* 1051 */       return (new OptionalDependencyFactory()).createOptionalDependency(descriptor, requestingBeanName, new Object[0]);
/*      */     }
/* 1053 */     if (ObjectFactory.class == descriptor.getDependencyType() || ObjectProvider.class == descriptor
/* 1054 */       .getDependencyType()) {
/* 1055 */       return new DependencyObjectProvider(descriptor, requestingBeanName);
/*      */     }
/* 1057 */     if (javaxInjectProviderClass == descriptor.getDependencyType()) {
/* 1058 */       return (new Jsr330ProviderFactory()).createDependencyProvider(descriptor, requestingBeanName);
/*      */     }
/*      */     
/* 1061 */     Object result = getAutowireCandidateResolver().getLazyResolutionProxyIfNecessary(descriptor, requestingBeanName);
/*      */     
/* 1063 */     if (result == null) {
/* 1064 */       result = doResolveDependency(descriptor, requestingBeanName, autowiredBeanNames, typeConverter);
/*      */     }
/* 1066 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object doResolveDependency(DependencyDescriptor descriptor, String beanName, Set<String> autowiredBeanNames, TypeConverter typeConverter) throws BeansException {
/* 1073 */     InjectionPoint previousInjectionPoint = ConstructorResolver.setCurrentInjectionPoint((InjectionPoint)descriptor); try {
/*      */       String autowiredBeanName;
/* 1075 */       Object instanceCandidate, shortcut = descriptor.resolveShortcut((BeanFactory)this);
/* 1076 */       if (shortcut != null) {
/* 1077 */         return shortcut;
/*      */       }
/*      */       
/* 1080 */       Class<?> type = descriptor.getDependencyType();
/* 1081 */       Object value = getAutowireCandidateResolver().getSuggestedValue(descriptor);
/* 1082 */       if (value != null) {
/* 1083 */         if (value instanceof String) {
/* 1084 */           String strVal = resolveEmbeddedValue((String)value);
/* 1085 */           BeanDefinition bd = (beanName != null && containsBean(beanName)) ? getMergedBeanDefinition(beanName) : null;
/* 1086 */           value = evaluateBeanDefinitionString(strVal, bd);
/*      */         } 
/* 1088 */         TypeConverter converter = (typeConverter != null) ? typeConverter : getTypeConverter();
/* 1089 */         return (descriptor.getField() != null) ? converter
/* 1090 */           .convertIfNecessary(value, type, descriptor.getField()) : converter
/* 1091 */           .convertIfNecessary(value, type, descriptor.getMethodParameter());
/*      */       } 
/*      */       
/* 1094 */       Object multipleBeans = resolveMultipleBeans(descriptor, beanName, autowiredBeanNames, typeConverter);
/* 1095 */       if (multipleBeans != null) {
/* 1096 */         return multipleBeans;
/*      */       }
/*      */       
/* 1099 */       Map<String, Object> matchingBeans = findAutowireCandidates(beanName, type, descriptor);
/* 1100 */       if (matchingBeans.isEmpty()) {
/* 1101 */         if (isRequired(descriptor)) {
/* 1102 */           raiseNoMatchingBeanFound(type, descriptor.getResolvableType(), descriptor);
/*      */         }
/* 1104 */         autowiredBeanName = null; return autowiredBeanName;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1110 */       if (matchingBeans.size() > 1) {
/* 1111 */         autowiredBeanName = determineAutowireCandidate(matchingBeans, descriptor);
/* 1112 */         if (autowiredBeanName == null) {
/* 1113 */           if (isRequired(descriptor) || !indicatesMultipleBeans(type)) {
/* 1114 */             return descriptor.resolveNotUnique(type, matchingBeans);
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1120 */           return null;
/*      */         } 
/*      */         
/* 1123 */         instanceCandidate = matchingBeans.get(autowiredBeanName);
/*      */       }
/*      */       else {
/*      */         
/* 1127 */         Map.Entry<String, Object> entry = matchingBeans.entrySet().iterator().next();
/* 1128 */         autowiredBeanName = entry.getKey();
/* 1129 */         instanceCandidate = entry.getValue();
/*      */       } 
/*      */       
/* 1132 */       if (autowiredBeanNames != null) {
/* 1133 */         autowiredBeanNames.add(autowiredBeanName);
/*      */       }
/* 1135 */       return (instanceCandidate instanceof Class) ? descriptor
/* 1136 */         .resolveCandidate(autowiredBeanName, type, (BeanFactory)this) : instanceCandidate;
/*      */     } finally {
/*      */       
/* 1139 */       ConstructorResolver.setCurrentInjectionPoint(previousInjectionPoint);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Object resolveMultipleBeans(DependencyDescriptor descriptor, String beanName, Set<String> autowiredBeanNames, TypeConverter typeConverter) {
/* 1146 */     Class<?> type = descriptor.getDependencyType();
/* 1147 */     if (type.isArray()) {
/* 1148 */       Class<?> componentType = type.getComponentType();
/* 1149 */       ResolvableType resolvableType = descriptor.getResolvableType();
/* 1150 */       Class<?> resolvedArrayType = resolvableType.resolve();
/* 1151 */       if (resolvedArrayType != null && resolvedArrayType != type) {
/* 1152 */         type = resolvedArrayType;
/* 1153 */         componentType = resolvableType.getComponentType().resolve();
/*      */       } 
/* 1155 */       if (componentType == null) {
/* 1156 */         return null;
/*      */       }
/* 1158 */       Map<String, Object> matchingBeans = findAutowireCandidates(beanName, componentType, new MultiElementDescriptor(descriptor));
/*      */       
/* 1160 */       if (matchingBeans.isEmpty()) {
/* 1161 */         return null;
/*      */       }
/* 1163 */       if (autowiredBeanNames != null) {
/* 1164 */         autowiredBeanNames.addAll(matchingBeans.keySet());
/*      */       }
/* 1166 */       TypeConverter converter = (typeConverter != null) ? typeConverter : getTypeConverter();
/* 1167 */       Object result = converter.convertIfNecessary(matchingBeans.values(), type);
/* 1168 */       if (getDependencyComparator() != null && result instanceof Object[]) {
/* 1169 */         Arrays.sort((Object[])result, adaptDependencyComparator(matchingBeans));
/*      */       }
/* 1171 */       return result;
/*      */     } 
/* 1173 */     if (Collection.class.isAssignableFrom(type) && type.isInterface()) {
/* 1174 */       Class<?> elementType = descriptor.getResolvableType().asCollection().resolveGeneric(new int[0]);
/* 1175 */       if (elementType == null) {
/* 1176 */         return null;
/*      */       }
/* 1178 */       Map<String, Object> matchingBeans = findAutowireCandidates(beanName, elementType, new MultiElementDescriptor(descriptor));
/*      */       
/* 1180 */       if (matchingBeans.isEmpty()) {
/* 1181 */         return null;
/*      */       }
/* 1183 */       if (autowiredBeanNames != null) {
/* 1184 */         autowiredBeanNames.addAll(matchingBeans.keySet());
/*      */       }
/* 1186 */       TypeConverter converter = (typeConverter != null) ? typeConverter : getTypeConverter();
/* 1187 */       Object result = converter.convertIfNecessary(matchingBeans.values(), type);
/* 1188 */       if (getDependencyComparator() != null && result instanceof List) {
/* 1189 */         Collections.sort((List)result, adaptDependencyComparator(matchingBeans));
/*      */       }
/* 1191 */       return result;
/*      */     } 
/* 1193 */     if (Map.class == type) {
/* 1194 */       ResolvableType mapType = descriptor.getResolvableType().asMap();
/* 1195 */       Class<?> keyType = mapType.resolveGeneric(new int[] { 0 });
/* 1196 */       if (String.class != keyType) {
/* 1197 */         return null;
/*      */       }
/* 1199 */       Class<?> valueType = mapType.resolveGeneric(new int[] { 1 });
/* 1200 */       if (valueType == null) {
/* 1201 */         return null;
/*      */       }
/* 1203 */       Map<String, Object> matchingBeans = findAutowireCandidates(beanName, valueType, new MultiElementDescriptor(descriptor));
/*      */       
/* 1205 */       if (matchingBeans.isEmpty()) {
/* 1206 */         return null;
/*      */       }
/* 1208 */       if (autowiredBeanNames != null) {
/* 1209 */         autowiredBeanNames.addAll(matchingBeans.keySet());
/*      */       }
/* 1211 */       return matchingBeans;
/*      */     } 
/*      */     
/* 1214 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isRequired(DependencyDescriptor descriptor) {
/* 1219 */     AutowireCandidateResolver resolver = getAutowireCandidateResolver();
/* 1220 */     return (resolver instanceof SimpleAutowireCandidateResolver) ? ((SimpleAutowireCandidateResolver)resolver)
/* 1221 */       .isRequired(descriptor) : descriptor
/* 1222 */       .isRequired();
/*      */   }
/*      */   
/*      */   private boolean indicatesMultipleBeans(Class<?> type) {
/* 1226 */     return (type.isArray() || (type.isInterface() && (Collection.class
/* 1227 */       .isAssignableFrom(type) || Map.class.isAssignableFrom(type))));
/*      */   }
/*      */   
/*      */   private Comparator<Object> adaptDependencyComparator(Map<String, Object> matchingBeans) {
/* 1231 */     Comparator<Object> comparator = getDependencyComparator();
/* 1232 */     if (comparator instanceof OrderComparator) {
/* 1233 */       return ((OrderComparator)comparator).withSourceProvider(
/* 1234 */           createFactoryAwareOrderSourceProvider(matchingBeans));
/*      */     }
/*      */     
/* 1237 */     return comparator;
/*      */   }
/*      */ 
/*      */   
/*      */   private OrderComparator.OrderSourceProvider createFactoryAwareOrderSourceProvider(Map<String, Object> beans) {
/* 1242 */     IdentityHashMap<Object, String> instancesToBeanNames = new IdentityHashMap<Object, String>();
/* 1243 */     for (Map.Entry<String, Object> entry : beans.entrySet()) {
/* 1244 */       instancesToBeanNames.put(entry.getValue(), entry.getKey());
/*      */     }
/* 1246 */     return new FactoryAwareOrderSourceProvider(instancesToBeanNames);
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
/*      */   protected Map<String, Object> findAutowireCandidates(String beanName, Class<?> requiredType, DependencyDescriptor descriptor) {
/* 1265 */     String[] candidateNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors((ListableBeanFactory)this, requiredType, true, descriptor
/* 1266 */         .isEager());
/* 1267 */     Map<String, Object> result = new LinkedHashMap<String, Object>(candidateNames.length);
/* 1268 */     for (Class<?> autowiringType : this.resolvableDependencies.keySet()) {
/* 1269 */       if (autowiringType.isAssignableFrom(requiredType)) {
/* 1270 */         Object autowiringValue = this.resolvableDependencies.get(autowiringType);
/* 1271 */         autowiringValue = AutowireUtils.resolveAutowiringValue(autowiringValue, requiredType);
/* 1272 */         if (requiredType.isInstance(autowiringValue)) {
/* 1273 */           result.put(ObjectUtils.identityToString(autowiringValue), autowiringValue);
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1278 */     for (String candidate : candidateNames) {
/* 1279 */       if (!isSelfReference(beanName, candidate) && isAutowireCandidate(candidate, descriptor)) {
/* 1280 */         addCandidateEntry(result, candidate, descriptor, requiredType);
/*      */       }
/*      */     } 
/* 1283 */     if (result.isEmpty() && !indicatesMultipleBeans(requiredType)) {
/*      */       
/* 1285 */       DependencyDescriptor fallbackDescriptor = descriptor.forFallbackMatch();
/* 1286 */       for (String candidate : candidateNames) {
/* 1287 */         if (!isSelfReference(beanName, candidate) && isAutowireCandidate(candidate, fallbackDescriptor)) {
/* 1288 */           addCandidateEntry(result, candidate, descriptor, requiredType);
/*      */         }
/*      */       } 
/* 1291 */       if (result.isEmpty())
/*      */       {
/*      */         
/* 1294 */         for (String candidate : candidateNames) {
/* 1295 */           if (isSelfReference(beanName, candidate) && (!(descriptor instanceof MultiElementDescriptor) || 
/* 1296 */             !beanName.equals(candidate)) && 
/* 1297 */             isAutowireCandidate(candidate, fallbackDescriptor)) {
/* 1298 */             addCandidateEntry(result, candidate, descriptor, requiredType);
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/* 1303 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addCandidateEntry(Map<String, Object> candidates, String candidateName, DependencyDescriptor descriptor, Class<?> requiredType) {
/* 1313 */     if (descriptor instanceof MultiElementDescriptor || containsSingleton(candidateName)) {
/* 1314 */       candidates.put(candidateName, descriptor.resolveCandidate(candidateName, requiredType, (BeanFactory)this));
/*      */     } else {
/*      */       
/* 1317 */       candidates.put(candidateName, getType(candidateName));
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
/*      */   protected String determineAutowireCandidate(Map<String, Object> candidates, DependencyDescriptor descriptor) {
/* 1330 */     Class<?> requiredType = descriptor.getDependencyType();
/* 1331 */     String primaryCandidate = determinePrimaryCandidate(candidates, requiredType);
/* 1332 */     if (primaryCandidate != null) {
/* 1333 */       return primaryCandidate;
/*      */     }
/* 1335 */     String priorityCandidate = determineHighestPriorityCandidate(candidates, requiredType);
/* 1336 */     if (priorityCandidate != null) {
/* 1337 */       return priorityCandidate;
/*      */     }
/*      */     
/* 1340 */     for (Map.Entry<String, Object> entry : candidates.entrySet()) {
/* 1341 */       String candidateName = entry.getKey();
/* 1342 */       Object beanInstance = entry.getValue();
/* 1343 */       if ((beanInstance != null && this.resolvableDependencies.containsValue(beanInstance)) || 
/* 1344 */         matchesBeanName(candidateName, descriptor.getDependencyName())) {
/* 1345 */         return candidateName;
/*      */       }
/*      */     } 
/* 1348 */     return null;
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
/*      */   protected String determinePrimaryCandidate(Map<String, Object> candidates, Class<?> requiredType) {
/* 1360 */     String primaryBeanName = null;
/* 1361 */     for (Map.Entry<String, Object> entry : candidates.entrySet()) {
/* 1362 */       String candidateBeanName = entry.getKey();
/* 1363 */       Object beanInstance = entry.getValue();
/* 1364 */       if (isPrimary(candidateBeanName, beanInstance)) {
/* 1365 */         if (primaryBeanName != null) {
/* 1366 */           boolean candidateLocal = containsBeanDefinition(candidateBeanName);
/* 1367 */           boolean primaryLocal = containsBeanDefinition(primaryBeanName);
/* 1368 */           if (candidateLocal && primaryLocal) {
/* 1369 */             throw new NoUniqueBeanDefinitionException(requiredType, candidates.size(), "more than one 'primary' bean found among candidates: " + candidates
/* 1370 */                 .keySet());
/*      */           }
/* 1372 */           if (candidateLocal) {
/* 1373 */             primaryBeanName = candidateBeanName;
/*      */           }
/*      */           continue;
/*      */         } 
/* 1377 */         primaryBeanName = candidateBeanName;
/*      */       } 
/*      */     } 
/*      */     
/* 1381 */     return primaryBeanName;
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
/*      */   protected String determineHighestPriorityCandidate(Map<String, Object> candidates, Class<?> requiredType) {
/* 1397 */     String highestPriorityBeanName = null;
/* 1398 */     Integer highestPriority = null;
/* 1399 */     for (Map.Entry<String, Object> entry : candidates.entrySet()) {
/* 1400 */       String candidateBeanName = entry.getKey();
/* 1401 */       Object beanInstance = entry.getValue();
/* 1402 */       Integer candidatePriority = getPriority(beanInstance);
/* 1403 */       if (candidatePriority != null) {
/* 1404 */         if (highestPriorityBeanName != null) {
/* 1405 */           if (candidatePriority.equals(highestPriority)) {
/* 1406 */             throw new NoUniqueBeanDefinitionException(requiredType, candidates.size(), "Multiple beans found with the same priority ('" + highestPriority + "') among candidates: " + candidates
/*      */                 
/* 1408 */                 .keySet());
/*      */           }
/* 1410 */           if (candidatePriority.intValue() < highestPriority.intValue()) {
/* 1411 */             highestPriorityBeanName = candidateBeanName;
/* 1412 */             highestPriority = candidatePriority;
/*      */           } 
/*      */           continue;
/*      */         } 
/* 1416 */         highestPriorityBeanName = candidateBeanName;
/* 1417 */         highestPriority = candidatePriority;
/*      */       } 
/*      */     } 
/*      */     
/* 1421 */     return highestPriorityBeanName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isPrimary(String beanName, Object beanInstance) {
/* 1432 */     if (containsBeanDefinition(beanName)) {
/* 1433 */       return getMergedLocalBeanDefinition(beanName).isPrimary();
/*      */     }
/* 1435 */     BeanFactory parent = getParentBeanFactory();
/* 1436 */     return (parent instanceof DefaultListableBeanFactory && ((DefaultListableBeanFactory)parent)
/* 1437 */       .isPrimary(beanName, beanInstance));
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
/*      */   protected Integer getPriority(Object beanInstance) {
/* 1453 */     Comparator<Object> comparator = getDependencyComparator();
/* 1454 */     if (comparator instanceof OrderComparator) {
/* 1455 */       return ((OrderComparator)comparator).getPriority(beanInstance);
/*      */     }
/* 1457 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean matchesBeanName(String beanName, String candidateName) {
/* 1465 */     return (candidateName != null && (candidateName
/* 1466 */       .equals(beanName) || ObjectUtils.containsElement((Object[])getAliases(beanName), candidateName)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isSelfReference(String beanName, String candidateName) {
/* 1475 */     return (beanName != null && candidateName != null && (beanName
/* 1476 */       .equals(candidateName) || (containsBeanDefinition(candidateName) && beanName
/* 1477 */       .equals(getMergedLocalBeanDefinition(candidateName).getFactoryBeanName()))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void raiseNoMatchingBeanFound(Class<?> type, ResolvableType resolvableType, DependencyDescriptor descriptor) throws BeansException {
/* 1487 */     checkBeanNotOfRequiredType(type, descriptor);
/*      */     
/* 1489 */     throw new NoSuchBeanDefinitionException(resolvableType, "expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: " + 
/*      */         
/* 1491 */         ObjectUtils.nullSafeToString(descriptor.getAnnotations()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkBeanNotOfRequiredType(Class<?> type, DependencyDescriptor descriptor) {
/* 1499 */     for (String beanName : this.beanDefinitionNames) {
/* 1500 */       RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/* 1501 */       Class<?> targetType = mbd.getTargetType();
/* 1502 */       if (targetType != null && type.isAssignableFrom(targetType) && 
/* 1503 */         isAutowireCandidate(beanName, mbd, descriptor, getAutowireCandidateResolver())) {
/*      */         
/* 1505 */         Object beanInstance = getSingleton(beanName, false);
/* 1506 */         Class<?> beanType = (beanInstance != null) ? beanInstance.getClass() : predictBeanType(beanName, mbd, new Class[0]);
/* 1507 */         if (!type.isAssignableFrom(beanType)) {
/* 1508 */           throw new BeanNotOfRequiredTypeException(beanName, type, beanType);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1513 */     BeanFactory parent = getParentBeanFactory();
/* 1514 */     if (parent instanceof DefaultListableBeanFactory) {
/* 1515 */       ((DefaultListableBeanFactory)parent).checkBeanNotOfRequiredType(type, descriptor);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1522 */     StringBuilder sb = new StringBuilder(ObjectUtils.identityToString(this));
/* 1523 */     sb.append(": defining beans [");
/* 1524 */     sb.append(StringUtils.collectionToCommaDelimitedString(this.beanDefinitionNames));
/* 1525 */     sb.append("]; ");
/* 1526 */     BeanFactory parent = getParentBeanFactory();
/* 1527 */     if (parent == null) {
/* 1528 */       sb.append("root of factory hierarchy");
/*      */     } else {
/*      */       
/* 1531 */       sb.append("parent: ").append(ObjectUtils.identityToString(parent));
/*      */     } 
/* 1533 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 1542 */     throw new NotSerializableException("DefaultListableBeanFactory itself is not deserializable - just a SerializedBeanFactoryReference is");
/*      */   }
/*      */ 
/*      */   
/*      */   protected Object writeReplace() throws ObjectStreamException {
/* 1547 */     if (this.serializationId != null) {
/* 1548 */       return new SerializedBeanFactoryReference(this.serializationId);
/*      */     }
/*      */     
/* 1551 */     throw new NotSerializableException("DefaultListableBeanFactory has no serialization id");
/*      */   }
/*      */ 
/*      */   
/*      */   public DefaultListableBeanFactory() {}
/*      */ 
/*      */   
/*      */   private static class SerializedBeanFactoryReference
/*      */     implements Serializable
/*      */   {
/*      */     private final String id;
/*      */ 
/*      */     
/*      */     public SerializedBeanFactoryReference(String id) {
/* 1565 */       this.id = id;
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/* 1569 */       Reference<?> ref = (Reference)DefaultListableBeanFactory.serializableFactories.get(this.id);
/* 1570 */       if (ref != null) {
/* 1571 */         Object result = ref.get();
/* 1572 */         if (result != null) {
/* 1573 */           return result;
/*      */         }
/*      */       } 
/*      */       
/* 1577 */       return new DefaultListableBeanFactory();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class NestedDependencyDescriptor
/*      */     extends DependencyDescriptor
/*      */   {
/*      */     public NestedDependencyDescriptor(DependencyDescriptor original) {
/* 1588 */       super(original);
/* 1589 */       increaseNestingLevel();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class MultiElementDescriptor
/*      */     extends NestedDependencyDescriptor
/*      */   {
/*      */     public MultiElementDescriptor(DependencyDescriptor original) {
/* 1600 */       super(original);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   @UsesJava8
/*      */   private class OptionalDependencyFactory
/*      */   {
/*      */     private OptionalDependencyFactory() {}
/*      */ 
/*      */     
/*      */     public Object createOptionalDependency(DependencyDescriptor descriptor, String beanName, Object... args) {
/* 1612 */       DependencyDescriptor descriptorToUse = new DefaultListableBeanFactory.NestedDependencyDescriptor(descriptor)
/*      */         {
/*      */           public boolean isRequired() {
/* 1615 */             return false;
/*      */           }
/*      */           
/*      */           public Object resolveCandidate(String beanName, Class<?> requiredType, BeanFactory beanFactory) {
/* 1619 */             return !ObjectUtils.isEmpty(args) ? beanFactory.getBean(beanName, new Object[] { requiredType, this.val$args }) : super
/* 1620 */               .resolveCandidate(beanName, requiredType, beanFactory);
/*      */           }
/*      */         };
/* 1623 */       return Optional.ofNullable(DefaultListableBeanFactory.this.doResolveDependency(descriptorToUse, beanName, (Set<String>)null, (TypeConverter)null));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class DependencyObjectProvider
/*      */     implements ObjectProvider<Object>, Serializable
/*      */   {
/*      */     private final DependencyDescriptor descriptor;
/*      */     
/*      */     private final boolean optional;
/*      */     
/*      */     private final String beanName;
/*      */ 
/*      */     
/*      */     public DependencyObjectProvider(DependencyDescriptor descriptor, String beanName) {
/* 1640 */       this.descriptor = new DefaultListableBeanFactory.NestedDependencyDescriptor(descriptor);
/* 1641 */       this.optional = (this.descriptor.getDependencyType() == DefaultListableBeanFactory.javaUtilOptionalClass);
/* 1642 */       this.beanName = beanName;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getObject() throws BeansException {
/* 1647 */       if (this.optional) {
/* 1648 */         return (new DefaultListableBeanFactory.OptionalDependencyFactory()).createOptionalDependency(this.descriptor, this.beanName, new Object[0]);
/*      */       }
/*      */       
/* 1651 */       return DefaultListableBeanFactory.this.doResolveDependency(this.descriptor, this.beanName, (Set<String>)null, (TypeConverter)null);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getObject(Object... args) throws BeansException {
/* 1657 */       if (this.optional) {
/* 1658 */         return (new DefaultListableBeanFactory.OptionalDependencyFactory()).createOptionalDependency(this.descriptor, this.beanName, args);
/*      */       }
/*      */       
/* 1661 */       DependencyDescriptor descriptorToUse = new DependencyDescriptor(this.descriptor)
/*      */         {
/*      */           public Object resolveCandidate(String beanName, Class<?> requiredType, BeanFactory beanFactory) {
/* 1664 */             return ((AbstractBeanFactory)beanFactory).getBean(beanName, requiredType, args);
/*      */           }
/*      */         };
/* 1667 */       return DefaultListableBeanFactory.this.doResolveDependency(descriptorToUse, this.beanName, (Set<String>)null, (TypeConverter)null);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getIfAvailable() throws BeansException {
/* 1673 */       if (this.optional) {
/* 1674 */         return (new DefaultListableBeanFactory.OptionalDependencyFactory()).createOptionalDependency(this.descriptor, this.beanName, new Object[0]);
/*      */       }
/*      */       
/* 1677 */       DependencyDescriptor descriptorToUse = new DependencyDescriptor(this.descriptor)
/*      */         {
/*      */           public boolean isRequired() {
/* 1680 */             return false;
/*      */           }
/*      */         };
/* 1683 */       return DefaultListableBeanFactory.this.doResolveDependency(descriptorToUse, this.beanName, (Set<String>)null, (TypeConverter)null);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getIfUnique() throws BeansException {
/* 1689 */       DependencyDescriptor descriptorToUse = new DependencyDescriptor(this.descriptor)
/*      */         {
/*      */           public boolean isRequired() {
/* 1692 */             return false;
/*      */           }
/*      */           
/*      */           public Object resolveNotUnique(Class<?> type, Map<String, Object> matchingBeans) {
/* 1696 */             return null;
/*      */           }
/*      */         };
/* 1699 */       if (this.optional) {
/* 1700 */         return (new DefaultListableBeanFactory.OptionalDependencyFactory()).createOptionalDependency(descriptorToUse, this.beanName, new Object[0]);
/*      */       }
/*      */       
/* 1703 */       return DefaultListableBeanFactory.this.doResolveDependency(descriptorToUse, this.beanName, (Set<String>)null, (TypeConverter)null);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class Jsr330DependencyProvider
/*      */     extends DependencyObjectProvider
/*      */     implements Provider<Object>
/*      */   {
/*      */     public Jsr330DependencyProvider(DependencyDescriptor descriptor, String beanName) {
/* 1715 */       super(descriptor, beanName);
/*      */     }
/*      */ 
/*      */     
/*      */     public Object get() throws BeansException {
/* 1720 */       return getObject();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private class Jsr330ProviderFactory
/*      */   {
/*      */     private Jsr330ProviderFactory() {}
/*      */ 
/*      */     
/*      */     public Object createDependencyProvider(DependencyDescriptor descriptor, String beanName) {
/* 1731 */       return new DefaultListableBeanFactory.Jsr330DependencyProvider(descriptor, beanName);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class FactoryAwareOrderSourceProvider
/*      */     implements OrderComparator.OrderSourceProvider
/*      */   {
/*      */     private final Map<Object, String> instancesToBeanNames;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public FactoryAwareOrderSourceProvider(Map<Object, String> instancesToBeanNames) {
/* 1748 */       this.instancesToBeanNames = instancesToBeanNames;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getOrderSource(Object obj) {
/* 1753 */       RootBeanDefinition beanDefinition = getRootBeanDefinition(this.instancesToBeanNames.get(obj));
/* 1754 */       if (beanDefinition == null) {
/* 1755 */         return null;
/*      */       }
/* 1757 */       List<Object> sources = new ArrayList(2);
/* 1758 */       Method factoryMethod = beanDefinition.getResolvedFactoryMethod();
/* 1759 */       if (factoryMethod != null) {
/* 1760 */         sources.add(factoryMethod);
/*      */       }
/* 1762 */       Class<?> targetType = beanDefinition.getTargetType();
/* 1763 */       if (targetType != null && targetType != obj.getClass()) {
/* 1764 */         sources.add(targetType);
/*      */       }
/* 1766 */       return sources.toArray();
/*      */     }
/*      */     
/*      */     private RootBeanDefinition getRootBeanDefinition(String beanName) {
/* 1770 */       if (beanName != null && DefaultListableBeanFactory.this.containsBeanDefinition(beanName)) {
/* 1771 */         BeanDefinition bd = DefaultListableBeanFactory.this.getMergedBeanDefinition(beanName);
/* 1772 */         if (bd instanceof RootBeanDefinition) {
/* 1773 */           return (RootBeanDefinition)bd;
/*      */         }
/*      */       } 
/* 1776 */       return null;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\DefaultListableBeanFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */