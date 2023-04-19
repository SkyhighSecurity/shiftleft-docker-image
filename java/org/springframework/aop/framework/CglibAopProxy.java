/*      */ package org.springframework.aop.framework;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.UndeclaredThrowableException;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.WeakHashMap;
/*      */ import org.aopalliance.aop.Advice;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.springframework.aop.Advisor;
/*      */ import org.springframework.aop.AopInvocationException;
/*      */ import org.springframework.aop.PointcutAdvisor;
/*      */ import org.springframework.aop.RawTargetAccess;
/*      */ import org.springframework.aop.TargetSource;
/*      */ import org.springframework.aop.support.AopUtils;
/*      */ import org.springframework.cglib.core.ClassGenerator;
/*      */ import org.springframework.cglib.core.CodeGenerationException;
/*      */ import org.springframework.cglib.core.GeneratorStrategy;
/*      */ import org.springframework.cglib.core.NamingPolicy;
/*      */ import org.springframework.cglib.core.SpringNamingPolicy;
/*      */ import org.springframework.cglib.proxy.Callback;
/*      */ import org.springframework.cglib.proxy.CallbackFilter;
/*      */ import org.springframework.cglib.proxy.Dispatcher;
/*      */ import org.springframework.cglib.proxy.Enhancer;
/*      */ import org.springframework.cglib.proxy.Factory;
/*      */ import org.springframework.cglib.proxy.MethodInterceptor;
/*      */ import org.springframework.cglib.proxy.MethodProxy;
/*      */ import org.springframework.cglib.proxy.NoOp;
/*      */ import org.springframework.cglib.transform.impl.UndeclaredThrowableStrategy;
/*      */ import org.springframework.core.SmartClassLoader;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class CglibAopProxy
/*      */   implements AopProxy, Serializable
/*      */ {
/*      */   private static final int AOP_PROXY = 0;
/*      */   private static final int INVOKE_TARGET = 1;
/*      */   private static final int NO_OVERRIDE = 2;
/*      */   private static final int DISPATCH_TARGET = 3;
/*      */   private static final int DISPATCH_ADVISED = 4;
/*      */   private static final int INVOKE_EQUALS = 5;
/*      */   private static final int INVOKE_HASHCODE = 6;
/*   95 */   protected static final Log logger = LogFactory.getLog(CglibAopProxy.class);
/*      */ 
/*      */   
/*   98 */   private static final Map<Class<?>, Boolean> validatedClasses = new WeakHashMap<Class<?>, Boolean>();
/*      */ 
/*      */ 
/*      */   
/*      */   protected final AdvisedSupport advised;
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object[] constructorArgs;
/*      */ 
/*      */   
/*      */   protected Class<?>[] constructorArgTypes;
/*      */ 
/*      */   
/*      */   private final transient AdvisedDispatcher advisedDispatcher;
/*      */ 
/*      */   
/*      */   private transient Map<String, Integer> fixedInterceptorMap;
/*      */ 
/*      */   
/*      */   private transient int fixedInterceptorOffset;
/*      */ 
/*      */ 
/*      */   
/*      */   public CglibAopProxy(AdvisedSupport config) throws AopConfigException {
/*  123 */     Assert.notNull(config, "AdvisedSupport must not be null");
/*  124 */     if ((config.getAdvisors()).length == 0 && config.getTargetSource() == AdvisedSupport.EMPTY_TARGET_SOURCE) {
/*  125 */       throw new AopConfigException("No advisors and no TargetSource specified");
/*      */     }
/*  127 */     this.advised = config;
/*  128 */     this.advisedDispatcher = new AdvisedDispatcher(this.advised);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConstructorArguments(Object[] constructorArgs, Class<?>[] constructorArgTypes) {
/*  137 */     if (constructorArgs == null || constructorArgTypes == null) {
/*  138 */       throw new IllegalArgumentException("Both 'constructorArgs' and 'constructorArgTypes' need to be specified");
/*      */     }
/*  140 */     if (constructorArgs.length != constructorArgTypes.length) {
/*  141 */       throw new IllegalArgumentException("Number of 'constructorArgs' (" + constructorArgs.length + ") must match number of 'constructorArgTypes' (" + constructorArgTypes.length + ")");
/*      */     }
/*      */     
/*  144 */     this.constructorArgs = constructorArgs;
/*  145 */     this.constructorArgTypes = constructorArgTypes;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getProxy() {
/*  151 */     return getProxy(null);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getProxy(ClassLoader classLoader) {
/*  156 */     if (logger.isDebugEnabled()) {
/*  157 */       logger.debug("Creating CGLIB proxy: target source is " + this.advised.getTargetSource());
/*      */     }
/*      */     
/*      */     try {
/*  161 */       Class<?> rootClass = this.advised.getTargetClass();
/*  162 */       Assert.state((rootClass != null), "Target class must be available for creating a CGLIB proxy");
/*      */       
/*  164 */       Class<?> proxySuperClass = rootClass;
/*  165 */       if (ClassUtils.isCglibProxyClass(rootClass)) {
/*  166 */         proxySuperClass = rootClass.getSuperclass();
/*  167 */         Class<?>[] additionalInterfaces = rootClass.getInterfaces();
/*  168 */         for (Class<?> additionalInterface : additionalInterfaces) {
/*  169 */           this.advised.addInterface(additionalInterface);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  174 */       validateClassIfNecessary(proxySuperClass, classLoader);
/*      */ 
/*      */       
/*  177 */       Enhancer enhancer = createEnhancer();
/*  178 */       if (classLoader != null) {
/*  179 */         enhancer.setClassLoader(classLoader);
/*  180 */         if (classLoader instanceof SmartClassLoader && ((SmartClassLoader)classLoader)
/*  181 */           .isClassReloadable(proxySuperClass)) {
/*  182 */           enhancer.setUseCache(false);
/*      */         }
/*      */       } 
/*  185 */       enhancer.setSuperclass(proxySuperClass);
/*  186 */       enhancer.setInterfaces(AopProxyUtils.completeProxiedInterfaces(this.advised));
/*  187 */       enhancer.setNamingPolicy((NamingPolicy)SpringNamingPolicy.INSTANCE);
/*  188 */       enhancer.setStrategy((GeneratorStrategy)new ClassLoaderAwareUndeclaredThrowableStrategy(classLoader));
/*      */       
/*  190 */       Callback[] callbacks = getCallbacks(rootClass);
/*  191 */       Class<?>[] types = new Class[callbacks.length];
/*  192 */       for (int x = 0; x < types.length; x++) {
/*  193 */         types[x] = callbacks[x].getClass();
/*      */       }
/*      */       
/*  196 */       enhancer.setCallbackFilter(new ProxyCallbackFilter(this.advised
/*  197 */             .getConfigurationOnlyCopy(), this.fixedInterceptorMap, this.fixedInterceptorOffset));
/*  198 */       enhancer.setCallbackTypes(types);
/*      */ 
/*      */       
/*  201 */       return createProxyClassAndInstance(enhancer, callbacks);
/*      */     }
/*  203 */     catch (CodeGenerationException ex) {
/*  204 */       throw new AopConfigException("Could not generate CGLIB subclass of " + this.advised.getTargetClass() + ": Common causes of this problem include using a final class or a non-visible class", ex);
/*      */ 
/*      */     
/*      */     }
/*  208 */     catch (IllegalArgumentException ex) {
/*  209 */       throw new AopConfigException("Could not generate CGLIB subclass of " + this.advised.getTargetClass() + ": Common causes of this problem include using a final class or a non-visible class", ex);
/*      */ 
/*      */     
/*      */     }
/*  213 */     catch (Throwable ex) {
/*      */       
/*  215 */       throw new AopConfigException("Unexpected AOP exception", ex);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected Object createProxyClassAndInstance(Enhancer enhancer, Callback[] callbacks) {
/*  220 */     enhancer.setInterceptDuringConstruction(false);
/*  221 */     enhancer.setCallbacks(callbacks);
/*  222 */     return (this.constructorArgs != null) ? enhancer
/*  223 */       .create(this.constructorArgTypes, this.constructorArgs) : enhancer
/*  224 */       .create();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Enhancer createEnhancer() {
/*  232 */     return new Enhancer();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void validateClassIfNecessary(Class<?> proxySuperClass, ClassLoader proxyClassLoader) {
/*  240 */     if (logger.isWarnEnabled()) {
/*  241 */       synchronized (validatedClasses) {
/*  242 */         if (!validatedClasses.containsKey(proxySuperClass)) {
/*  243 */           doValidateClass(proxySuperClass, proxyClassLoader, 
/*  244 */               ClassUtils.getAllInterfacesForClassAsSet(proxySuperClass));
/*  245 */           validatedClasses.put(proxySuperClass, Boolean.TRUE);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void doValidateClass(Class<?> proxySuperClass, ClassLoader proxyClassLoader, Set<Class<?>> ifcs) {
/*  256 */     if (proxySuperClass != Object.class) {
/*  257 */       Method[] methods = proxySuperClass.getDeclaredMethods();
/*  258 */       for (Method method : methods) {
/*  259 */         int mod = method.getModifiers();
/*  260 */         if (!Modifier.isStatic(mod) && !Modifier.isPrivate(mod)) {
/*  261 */           if (Modifier.isFinal(mod)) {
/*  262 */             if (implementsInterface(method, ifcs)) {
/*  263 */               logger.warn("Unable to proxy interface-implementing method [" + method + "] because it is marked as final: Consider using interface-based JDK proxies instead!");
/*      */             }
/*      */             
/*  266 */             logger.info("Final method [" + method + "] cannot get proxied via CGLIB: Calls to this method will NOT be routed to the target instance and might lead to NPEs against uninitialized fields in the proxy instance.");
/*      */ 
/*      */           
/*      */           }
/*  270 */           else if (!Modifier.isPublic(mod) && !Modifier.isProtected(mod) && proxyClassLoader != null && proxySuperClass
/*  271 */             .getClassLoader() != proxyClassLoader) {
/*  272 */             logger.info("Method [" + method + "] is package-visible across different ClassLoaders and cannot get proxied via CGLIB: Declare this method as public or protected if you need to support invocations through the proxy.");
/*      */           } 
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  278 */       doValidateClass(proxySuperClass.getSuperclass(), proxyClassLoader, ifcs);
/*      */     } 
/*      */   }
/*      */   
/*      */   private Callback[] getCallbacks(Class<?> rootClass) throws Exception {
/*      */     Callback targetInterceptor, callbacks[];
/*  284 */     boolean exposeProxy = this.advised.isExposeProxy();
/*  285 */     boolean isFrozen = this.advised.isFrozen();
/*  286 */     boolean isStatic = this.advised.getTargetSource().isStatic();
/*      */ 
/*      */     
/*  289 */     DynamicAdvisedInterceptor dynamicAdvisedInterceptor = new DynamicAdvisedInterceptor(this.advised);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  294 */     if (exposeProxy) {
/*      */ 
/*      */       
/*  297 */       targetInterceptor = isStatic ? (Callback)new StaticUnadvisedExposedInterceptor(this.advised.getTargetSource().getTarget()) : (Callback)new DynamicUnadvisedExposedInterceptor(this.advised.getTargetSource());
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  302 */       targetInterceptor = isStatic ? (Callback)new StaticUnadvisedInterceptor(this.advised.getTargetSource().getTarget()) : (Callback)new DynamicUnadvisedInterceptor(this.advised.getTargetSource());
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  308 */     Callback targetDispatcher = isStatic ? (Callback)new StaticDispatcher(this.advised.getTargetSource().getTarget()) : (Callback)new SerializableNoOp();
/*      */     
/*  310 */     Callback[] mainCallbacks = { (Callback)dynamicAdvisedInterceptor, targetInterceptor, (Callback)new SerializableNoOp(), targetDispatcher, (Callback)this.advisedDispatcher, (Callback)new EqualsInterceptor(this.advised), (Callback)new HashCodeInterceptor(this.advised) };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  324 */     if (isStatic && isFrozen) {
/*  325 */       Method[] methods = rootClass.getMethods();
/*  326 */       Callback[] fixedCallbacks = new Callback[methods.length];
/*  327 */       this.fixedInterceptorMap = new HashMap<String, Integer>(methods.length);
/*      */ 
/*      */       
/*  330 */       for (int x = 0; x < methods.length; x++) {
/*  331 */         List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(methods[x], rootClass);
/*  332 */         fixedCallbacks[x] = (Callback)new FixedChainStaticTargetInterceptor(chain, this.advised
/*  333 */             .getTargetSource().getTarget(), this.advised.getTargetClass());
/*  334 */         this.fixedInterceptorMap.put(methods[x].toString(), Integer.valueOf(x));
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  339 */       callbacks = new Callback[mainCallbacks.length + fixedCallbacks.length];
/*  340 */       System.arraycopy(mainCallbacks, 0, callbacks, 0, mainCallbacks.length);
/*  341 */       System.arraycopy(fixedCallbacks, 0, callbacks, mainCallbacks.length, fixedCallbacks.length);
/*  342 */       this.fixedInterceptorOffset = mainCallbacks.length;
/*      */     } else {
/*      */       
/*  345 */       callbacks = mainCallbacks;
/*      */     } 
/*  347 */     return callbacks;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object other) {
/*  353 */     return (this == other || (other instanceof CglibAopProxy && 
/*  354 */       AopProxyUtils.equalsInProxy(this.advised, ((CglibAopProxy)other).advised)));
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  359 */     return CglibAopProxy.class.hashCode() * 13 + this.advised.getTargetSource().hashCode();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean implementsInterface(Method method, Set<Class<?>> ifcs) {
/*  367 */     for (Class<?> ifc : ifcs) {
/*  368 */       if (ClassUtils.hasMethod(ifc, method.getName(), method.getParameterTypes())) {
/*  369 */         return true;
/*      */       }
/*      */     } 
/*  372 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Object processReturnType(Object proxy, Object target, Method method, Object retVal) {
/*  381 */     if (retVal != null && retVal == target && 
/*  382 */       !RawTargetAccess.class.isAssignableFrom(method.getDeclaringClass()))
/*      */     {
/*      */       
/*  385 */       retVal = proxy;
/*      */     }
/*  387 */     Class<?> returnType = method.getReturnType();
/*  388 */     if (retVal == null && returnType != void.class && returnType.isPrimitive()) {
/*  389 */       throw new AopInvocationException("Null return value from advice does not match primitive return type for: " + method);
/*      */     }
/*      */     
/*  392 */     return retVal;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class SerializableNoOp
/*      */     implements NoOp, Serializable {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class StaticUnadvisedInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     private final Object target;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public StaticUnadvisedInterceptor(Object target) {
/*  415 */       this.target = target;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
/*  420 */       Object retVal = methodProxy.invoke(this.target, args);
/*  421 */       return CglibAopProxy.processReturnType(proxy, this.target, method, retVal);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class StaticUnadvisedExposedInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     private final Object target;
/*      */ 
/*      */ 
/*      */     
/*      */     public StaticUnadvisedExposedInterceptor(Object target) {
/*  435 */       this.target = target;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
/*  440 */       Object oldProxy = null;
/*      */       try {
/*  442 */         oldProxy = AopContext.setCurrentProxy(proxy);
/*  443 */         Object retVal = methodProxy.invoke(this.target, args);
/*  444 */         return CglibAopProxy.processReturnType(proxy, this.target, method, retVal);
/*      */       } finally {
/*      */         
/*  447 */         AopContext.setCurrentProxy(oldProxy);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class DynamicUnadvisedInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     private final TargetSource targetSource;
/*      */ 
/*      */ 
/*      */     
/*      */     public DynamicUnadvisedInterceptor(TargetSource targetSource) {
/*  463 */       this.targetSource = targetSource;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
/*  468 */       Object target = this.targetSource.getTarget();
/*      */       try {
/*  470 */         Object retVal = methodProxy.invoke(target, args);
/*  471 */         return CglibAopProxy.processReturnType(proxy, target, method, retVal);
/*      */       } finally {
/*      */         
/*  474 */         this.targetSource.releaseTarget(target);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class DynamicUnadvisedExposedInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     private final TargetSource targetSource;
/*      */ 
/*      */     
/*      */     public DynamicUnadvisedExposedInterceptor(TargetSource targetSource) {
/*  488 */       this.targetSource = targetSource;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
/*  493 */       Object oldProxy = null;
/*  494 */       Object target = this.targetSource.getTarget();
/*      */       try {
/*  496 */         oldProxy = AopContext.setCurrentProxy(proxy);
/*  497 */         Object retVal = methodProxy.invoke(target, args);
/*  498 */         return CglibAopProxy.processReturnType(proxy, target, method, retVal);
/*      */       } finally {
/*      */         
/*  501 */         AopContext.setCurrentProxy(oldProxy);
/*  502 */         this.targetSource.releaseTarget(target);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class StaticDispatcher
/*      */     implements Dispatcher, Serializable
/*      */   {
/*      */     private Object target;
/*      */ 
/*      */ 
/*      */     
/*      */     public StaticDispatcher(Object target) {
/*  518 */       this.target = target;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object loadObject() {
/*  523 */       return this.target;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class AdvisedDispatcher
/*      */     implements Dispatcher, Serializable
/*      */   {
/*      */     private final AdvisedSupport advised;
/*      */ 
/*      */     
/*      */     public AdvisedDispatcher(AdvisedSupport advised) {
/*  536 */       this.advised = advised;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object loadObject() throws Exception {
/*  541 */       return this.advised;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class EqualsInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     private final AdvisedSupport advised;
/*      */ 
/*      */ 
/*      */     
/*      */     public EqualsInterceptor(AdvisedSupport advised) {
/*  555 */       this.advised = advised;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) {
/*  560 */       Object other = args[0];
/*  561 */       if (proxy == other) {
/*  562 */         return Boolean.valueOf(true);
/*      */       }
/*  564 */       if (other instanceof Factory) {
/*  565 */         Callback callback = ((Factory)other).getCallback(5);
/*  566 */         if (!(callback instanceof EqualsInterceptor)) {
/*  567 */           return Boolean.valueOf(false);
/*      */         }
/*  569 */         AdvisedSupport otherAdvised = ((EqualsInterceptor)callback).advised;
/*  570 */         return Boolean.valueOf(AopProxyUtils.equalsInProxy(this.advised, otherAdvised));
/*      */       } 
/*      */       
/*  573 */       return Boolean.valueOf(false);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class HashCodeInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     private final AdvisedSupport advised;
/*      */ 
/*      */ 
/*      */     
/*      */     public HashCodeInterceptor(AdvisedSupport advised) {
/*  588 */       this.advised = advised;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) {
/*  593 */       return Integer.valueOf(CglibAopProxy.class.hashCode() * 13 + this.advised.getTargetSource().hashCode());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class FixedChainStaticTargetInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     private final List<Object> adviceChain;
/*      */     
/*      */     private final Object target;
/*      */     
/*      */     private final Class<?> targetClass;
/*      */ 
/*      */     
/*      */     public FixedChainStaticTargetInterceptor(List<Object> adviceChain, Object target, Class<?> targetClass) {
/*  610 */       this.adviceChain = adviceChain;
/*  611 */       this.target = target;
/*  612 */       this.targetClass = targetClass;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
/*  617 */       CglibAopProxy.CglibMethodInvocation cglibMethodInvocation = new CglibAopProxy.CglibMethodInvocation(proxy, this.target, method, args, this.targetClass, this.adviceChain, methodProxy);
/*      */ 
/*      */       
/*  620 */       Object retVal = cglibMethodInvocation.proceed();
/*  621 */       retVal = CglibAopProxy.processReturnType(proxy, this.target, method, retVal);
/*  622 */       return retVal;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class DynamicAdvisedInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     private final AdvisedSupport advised;
/*      */ 
/*      */ 
/*      */     
/*      */     public DynamicAdvisedInterceptor(AdvisedSupport advised) {
/*  636 */       this.advised = advised;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
/*  641 */       Object oldProxy = null;
/*  642 */       boolean setProxyContext = false;
/*  643 */       Class<?> targetClass = null;
/*  644 */       Object target = null;
/*      */       try {
/*  646 */         if (this.advised.exposeProxy) {
/*      */           
/*  648 */           oldProxy = AopContext.setCurrentProxy(proxy);
/*  649 */           setProxyContext = true;
/*      */         } 
/*      */ 
/*      */         
/*  653 */         target = getTarget();
/*  654 */         if (target != null) {
/*  655 */           targetClass = target.getClass();
/*      */         }
/*  657 */         List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
/*      */ 
/*      */ 
/*      */         
/*  661 */         if (chain.isEmpty() && Modifier.isPublic(method.getModifiers())) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  666 */           Object[] argsToUse = AopProxyUtils.adaptArgumentsIfNecessary(method, args);
/*  667 */           retVal = methodProxy.invoke(target, argsToUse);
/*      */         }
/*      */         else {
/*      */           
/*  671 */           retVal = (new CglibAopProxy.CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy)).proceed();
/*      */         } 
/*  673 */         Object retVal = CglibAopProxy.processReturnType(proxy, target, method, retVal);
/*  674 */         return retVal;
/*      */       } finally {
/*      */         
/*  677 */         if (target != null) {
/*  678 */           releaseTarget(target);
/*      */         }
/*  680 */         if (setProxyContext)
/*      */         {
/*  682 */           AopContext.setCurrentProxy(oldProxy);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object other) {
/*  689 */       return (this == other || (other instanceof DynamicAdvisedInterceptor && this.advised
/*      */         
/*  691 */         .equals(((DynamicAdvisedInterceptor)other).advised)));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  699 */       return this.advised.hashCode();
/*      */     }
/*      */     
/*      */     protected Object getTarget() throws Exception {
/*  703 */       return this.advised.getTargetSource().getTarget();
/*      */     }
/*      */     
/*      */     protected void releaseTarget(Object target) throws Exception {
/*  707 */       this.advised.getTargetSource().releaseTarget(target);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class CglibMethodInvocation
/*      */     extends ReflectiveMethodInvocation
/*      */   {
/*      */     private final MethodProxy methodProxy;
/*      */ 
/*      */     
/*      */     private final boolean publicMethod;
/*      */ 
/*      */ 
/*      */     
/*      */     public CglibMethodInvocation(Object proxy, Object target, Method method, Object[] arguments, Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers, MethodProxy methodProxy) {
/*  724 */       super(proxy, target, method, arguments, targetClass, interceptorsAndDynamicMethodMatchers);
/*  725 */       this.methodProxy = methodProxy;
/*  726 */       this.publicMethod = Modifier.isPublic(method.getModifiers());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected Object invokeJoinpoint() throws Throwable {
/*  735 */       if (this.publicMethod) {
/*  736 */         return this.methodProxy.invoke(this.target, this.arguments);
/*      */       }
/*      */       
/*  739 */       return super.invokeJoinpoint();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class ProxyCallbackFilter
/*      */     implements CallbackFilter
/*      */   {
/*      */     private final AdvisedSupport advised;
/*      */ 
/*      */     
/*      */     private final Map<String, Integer> fixedInterceptorMap;
/*      */     
/*      */     private final int fixedInterceptorOffset;
/*      */ 
/*      */     
/*      */     public ProxyCallbackFilter(AdvisedSupport advised, Map<String, Integer> fixedInterceptorMap, int fixedInterceptorOffset) {
/*  757 */       this.advised = advised;
/*  758 */       this.fixedInterceptorMap = fixedInterceptorMap;
/*  759 */       this.fixedInterceptorOffset = fixedInterceptorOffset;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int accept(Method method) {
/*  800 */       if (AopUtils.isFinalizeMethod(method)) {
/*  801 */         CglibAopProxy.logger.debug("Found finalize() method - using NO_OVERRIDE");
/*  802 */         return 2;
/*      */       } 
/*  804 */       if (!this.advised.isOpaque() && method.getDeclaringClass().isInterface() && method
/*  805 */         .getDeclaringClass().isAssignableFrom(Advised.class)) {
/*  806 */         if (CglibAopProxy.logger.isDebugEnabled()) {
/*  807 */           CglibAopProxy.logger.debug("Method is declared on Advised interface: " + method);
/*      */         }
/*  809 */         return 4;
/*      */       } 
/*      */       
/*  812 */       if (AopUtils.isEqualsMethod(method)) {
/*  813 */         if (CglibAopProxy.logger.isDebugEnabled()) {
/*  814 */           CglibAopProxy.logger.debug("Found 'equals' method: " + method);
/*      */         }
/*  816 */         return 5;
/*      */       } 
/*      */       
/*  819 */       if (AopUtils.isHashCodeMethod(method)) {
/*  820 */         if (CglibAopProxy.logger.isDebugEnabled()) {
/*  821 */           CglibAopProxy.logger.debug("Found 'hashCode' method: " + method);
/*      */         }
/*  823 */         return 6;
/*      */       } 
/*  825 */       Class<?> targetClass = this.advised.getTargetClass();
/*      */       
/*  827 */       List<?> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
/*  828 */       boolean haveAdvice = !chain.isEmpty();
/*  829 */       boolean exposeProxy = this.advised.isExposeProxy();
/*  830 */       boolean isStatic = this.advised.getTargetSource().isStatic();
/*  831 */       boolean isFrozen = this.advised.isFrozen();
/*  832 */       if (haveAdvice || !isFrozen) {
/*      */         
/*  834 */         if (exposeProxy) {
/*  835 */           if (CglibAopProxy.logger.isDebugEnabled()) {
/*  836 */             CglibAopProxy.logger.debug("Must expose proxy on advised method: " + method);
/*      */           }
/*  838 */           return 0;
/*      */         } 
/*  840 */         String key = method.toString();
/*      */ 
/*      */         
/*  843 */         if (isStatic && isFrozen && this.fixedInterceptorMap.containsKey(key)) {
/*  844 */           if (CglibAopProxy.logger.isDebugEnabled()) {
/*  845 */             CglibAopProxy.logger.debug("Method has advice and optimizations are enabled: " + method);
/*      */           }
/*      */           
/*  848 */           int index = ((Integer)this.fixedInterceptorMap.get(key)).intValue();
/*  849 */           return index + this.fixedInterceptorOffset;
/*      */         } 
/*      */         
/*  852 */         if (CglibAopProxy.logger.isDebugEnabled()) {
/*  853 */           CglibAopProxy.logger.debug("Unable to apply any optimizations to advised method: " + method);
/*      */         }
/*  855 */         return 0;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  864 */       if (exposeProxy || !isStatic) {
/*  865 */         return 1;
/*      */       }
/*  867 */       Class<?> returnType = method.getReturnType();
/*  868 */       if (returnType.isAssignableFrom(targetClass)) {
/*  869 */         if (CglibAopProxy.logger.isDebugEnabled()) {
/*  870 */           CglibAopProxy.logger.debug("Method return type is assignable from target type and may therefore return 'this' - using INVOKE_TARGET: " + method);
/*      */         }
/*      */         
/*  873 */         return 1;
/*      */       } 
/*      */       
/*  876 */       if (CglibAopProxy.logger.isDebugEnabled()) {
/*  877 */         CglibAopProxy.logger.debug("Method return type ensures 'this' cannot be returned - using DISPATCH_TARGET: " + method);
/*      */       }
/*      */       
/*  880 */       return 3;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object other) {
/*  887 */       if (this == other) {
/*  888 */         return true;
/*      */       }
/*  890 */       if (!(other instanceof ProxyCallbackFilter)) {
/*  891 */         return false;
/*      */       }
/*  893 */       ProxyCallbackFilter otherCallbackFilter = (ProxyCallbackFilter)other;
/*  894 */       AdvisedSupport otherAdvised = otherCallbackFilter.advised;
/*  895 */       if (this.advised == null || otherAdvised == null) {
/*  896 */         return false;
/*      */       }
/*  898 */       if (this.advised.isFrozen() != otherAdvised.isFrozen()) {
/*  899 */         return false;
/*      */       }
/*  901 */       if (this.advised.isExposeProxy() != otherAdvised.isExposeProxy()) {
/*  902 */         return false;
/*      */       }
/*  904 */       if (this.advised.getTargetSource().isStatic() != otherAdvised.getTargetSource().isStatic()) {
/*  905 */         return false;
/*      */       }
/*  907 */       if (!AopProxyUtils.equalsProxiedInterfaces(this.advised, otherAdvised)) {
/*  908 */         return false;
/*      */       }
/*      */ 
/*      */       
/*  912 */       Advisor[] thisAdvisors = this.advised.getAdvisors();
/*  913 */       Advisor[] thatAdvisors = otherAdvised.getAdvisors();
/*  914 */       if (thisAdvisors.length != thatAdvisors.length) {
/*  915 */         return false;
/*      */       }
/*  917 */       for (int i = 0; i < thisAdvisors.length; i++) {
/*  918 */         Advisor thisAdvisor = thisAdvisors[i];
/*  919 */         Advisor thatAdvisor = thatAdvisors[i];
/*  920 */         if (!equalsAdviceClasses(thisAdvisor, thatAdvisor)) {
/*  921 */           return false;
/*      */         }
/*  923 */         if (!equalsPointcuts(thisAdvisor, thatAdvisor)) {
/*  924 */           return false;
/*      */         }
/*      */       } 
/*  927 */       return true;
/*      */     }
/*      */     
/*      */     private boolean equalsAdviceClasses(Advisor a, Advisor b) {
/*  931 */       Advice aa = a.getAdvice();
/*  932 */       Advice ba = b.getAdvice();
/*  933 */       if (aa == null || ba == null) {
/*  934 */         return (aa == ba);
/*      */       }
/*  936 */       return (aa.getClass() == ba.getClass());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean equalsPointcuts(Advisor a, Advisor b) {
/*  942 */       return (!(a instanceof PointcutAdvisor) || (b instanceof PointcutAdvisor && 
/*      */         
/*  944 */         ObjectUtils.nullSafeEquals(((PointcutAdvisor)a).getPointcut(), ((PointcutAdvisor)b).getPointcut())));
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  949 */       int hashCode = 0;
/*  950 */       Advisor[] advisors = this.advised.getAdvisors();
/*  951 */       for (Advisor advisor : advisors) {
/*  952 */         Advice advice = advisor.getAdvice();
/*  953 */         if (advice != null) {
/*  954 */           hashCode = 13 * hashCode + advice.getClass().hashCode();
/*      */         }
/*      */       } 
/*  957 */       hashCode = 13 * hashCode + (this.advised.isFrozen() ? 1 : 0);
/*  958 */       hashCode = 13 * hashCode + (this.advised.isExposeProxy() ? 1 : 0);
/*  959 */       hashCode = 13 * hashCode + (this.advised.isOptimize() ? 1 : 0);
/*  960 */       hashCode = 13 * hashCode + (this.advised.isOpaque() ? 1 : 0);
/*  961 */       return hashCode;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class ClassLoaderAwareUndeclaredThrowableStrategy
/*      */     extends UndeclaredThrowableStrategy
/*      */   {
/*      */     private final ClassLoader classLoader;
/*      */ 
/*      */ 
/*      */     
/*      */     public ClassLoaderAwareUndeclaredThrowableStrategy(ClassLoader classLoader) {
/*  976 */       super(UndeclaredThrowableException.class);
/*  977 */       this.classLoader = classLoader;
/*      */     }
/*      */     
/*      */     public byte[] generate(ClassGenerator cg) throws Exception {
/*      */       ClassLoader threadContextClassLoader;
/*  982 */       if (this.classLoader == null) {
/*  983 */         return super.generate(cg);
/*      */       }
/*      */       
/*  986 */       Thread currentThread = Thread.currentThread();
/*      */       
/*      */       try {
/*  989 */         threadContextClassLoader = currentThread.getContextClassLoader();
/*      */       }
/*  991 */       catch (Throwable ex) {
/*      */         
/*  993 */         return super.generate(cg);
/*      */       } 
/*      */       
/*  996 */       boolean overrideClassLoader = !this.classLoader.equals(threadContextClassLoader);
/*  997 */       if (overrideClassLoader) {
/*  998 */         currentThread.setContextClassLoader(this.classLoader);
/*      */       }
/*      */       try {
/* 1001 */         return super.generate(cg);
/*      */       } finally {
/*      */         
/* 1004 */         if (overrideClassLoader)
/*      */         {
/* 1006 */           currentThread.setContextClassLoader(threadContextClassLoader);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\CglibAopProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */