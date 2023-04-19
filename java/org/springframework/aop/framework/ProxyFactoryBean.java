/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.aopalliance.intercept.Interceptor;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
/*     */ import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
/*     */ import org.springframework.aop.framework.adapter.UnknownAdviceTypeException;
/*     */ import org.springframework.aop.target.SingletonTargetSource;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.FactoryBeanNotInitializedException;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProxyFactoryBean
/*     */   extends ProxyCreatorSupport
/*     */   implements FactoryBean<Object>, BeanClassLoaderAware, BeanFactoryAware
/*     */ {
/*     */   public static final String GLOBAL_SUFFIX = "*";
/* 101 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private String[] interceptorNames;
/*     */   
/*     */   private String targetName;
/*     */   
/*     */   private boolean autodetectInterfaces = true;
/*     */   
/*     */   private boolean singleton = true;
/*     */   
/* 111 */   private AdvisorAdapterRegistry advisorAdapterRegistry = GlobalAdvisorAdapterRegistry.getInstance();
/*     */   
/*     */   private boolean freezeProxy = false;
/*     */   
/* 115 */   private transient ClassLoader proxyClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */ 
/*     */   
/*     */   private transient boolean classLoaderConfigured = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private transient BeanFactory beanFactory;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean advisorChainInitialized = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private Object singletonInstance;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyInterfaces(Class<?>[] proxyInterfaces) throws ClassNotFoundException {
/* 137 */     setInterfaces(proxyInterfaces);
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
/*     */   public void setInterceptorNames(String... interceptorNames) {
/* 158 */     this.interceptorNames = interceptorNames;
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
/*     */   public void setTargetName(String targetName) {
/* 171 */     this.targetName = targetName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutodetectInterfaces(boolean autodetectInterfaces) {
/* 181 */     this.autodetectInterfaces = autodetectInterfaces;
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
/*     */   public void setSingleton(boolean singleton) {
/* 193 */     this.singleton = singleton;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdvisorAdapterRegistry(AdvisorAdapterRegistry advisorAdapterRegistry) {
/* 202 */     this.advisorAdapterRegistry = advisorAdapterRegistry;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFrozen(boolean frozen) {
/* 207 */     this.freezeProxy = frozen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyClassLoader(ClassLoader classLoader) {
/* 217 */     this.proxyClassLoader = classLoader;
/* 218 */     this.classLoaderConfigured = (classLoader != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 223 */     if (!this.classLoaderConfigured) {
/* 224 */       this.proxyClassLoader = classLoader;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 230 */     this.beanFactory = beanFactory;
/* 231 */     checkInterceptorNames();
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
/*     */   public Object getObject() throws BeansException {
/* 244 */     initializeAdvisorChain();
/* 245 */     if (isSingleton()) {
/* 246 */       return getSingletonInstance();
/*     */     }
/*     */     
/* 249 */     if (this.targetName == null) {
/* 250 */       this.logger.warn("Using non-singleton proxies with singleton targets is often undesirable. Enable prototype proxies by setting the 'targetName' property.");
/*     */     }
/*     */     
/* 253 */     return newPrototypeInstance();
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
/*     */   public Class<?> getObjectType() {
/* 265 */     synchronized (this) {
/* 266 */       if (this.singletonInstance != null) {
/* 267 */         return this.singletonInstance.getClass();
/*     */       }
/*     */     } 
/* 270 */     Class<?>[] ifcs = getProxiedInterfaces();
/* 271 */     if (ifcs.length == 1) {
/* 272 */       return ifcs[0];
/*     */     }
/* 274 */     if (ifcs.length > 1) {
/* 275 */       return createCompositeInterface(ifcs);
/*     */     }
/* 277 */     if (this.targetName != null && this.beanFactory != null) {
/* 278 */       return this.beanFactory.getType(this.targetName);
/*     */     }
/*     */     
/* 281 */     return getTargetClass();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 287 */     return this.singleton;
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
/*     */   protected Class<?> createCompositeInterface(Class<?>[] interfaces) {
/* 301 */     return ClassUtils.createCompositeInterface(interfaces, this.proxyClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized Object getSingletonInstance() {
/* 310 */     if (this.singletonInstance == null) {
/* 311 */       this.targetSource = freshTargetSource();
/* 312 */       if (this.autodetectInterfaces && (getProxiedInterfaces()).length == 0 && !isProxyTargetClass()) {
/*     */         
/* 314 */         Class<?> targetClass = getTargetClass();
/* 315 */         if (targetClass == null) {
/* 316 */           throw new FactoryBeanNotInitializedException("Cannot determine target class for proxy");
/*     */         }
/* 318 */         setInterfaces(ClassUtils.getAllInterfacesForClass(targetClass, this.proxyClassLoader));
/*     */       } 
/*     */       
/* 321 */       super.setFrozen(this.freezeProxy);
/* 322 */       this.singletonInstance = getProxy(createAopProxy());
/*     */     } 
/* 324 */     return this.singletonInstance;
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
/*     */   private synchronized Object newPrototypeInstance() {
/* 337 */     if (this.logger.isTraceEnabled()) {
/* 338 */       this.logger.trace("Creating copy of prototype ProxyFactoryBean config: " + this);
/*     */     }
/*     */     
/* 341 */     ProxyCreatorSupport copy = new ProxyCreatorSupport(getAopProxyFactory());
/*     */     
/* 343 */     TargetSource targetSource = freshTargetSource();
/* 344 */     copy.copyConfigurationFrom(this, targetSource, freshAdvisorChain());
/* 345 */     if (this.autodetectInterfaces && (getProxiedInterfaces()).length == 0 && !isProxyTargetClass())
/*     */     {
/* 347 */       copy.setInterfaces(
/* 348 */           ClassUtils.getAllInterfacesForClass(targetSource.getTargetClass(), this.proxyClassLoader));
/*     */     }
/* 350 */     copy.setFrozen(this.freezeProxy);
/*     */     
/* 352 */     if (this.logger.isTraceEnabled()) {
/* 353 */       this.logger.trace("Using ProxyCreatorSupport copy: " + copy);
/*     */     }
/* 355 */     return getProxy(copy.createAopProxy());
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
/*     */   protected Object getProxy(AopProxy aopProxy) {
/* 368 */     return aopProxy.getProxy(this.proxyClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkInterceptorNames() {
/* 376 */     if (!ObjectUtils.isEmpty((Object[])this.interceptorNames)) {
/* 377 */       String finalName = this.interceptorNames[this.interceptorNames.length - 1];
/* 378 */       if (this.targetName == null && this.targetSource == EMPTY_TARGET_SOURCE)
/*     */       {
/*     */         
/* 381 */         if (!finalName.endsWith("*") && !isNamedBeanAnAdvisorOrAdvice(finalName)) {
/*     */           
/* 383 */           this.targetName = finalName;
/* 384 */           if (this.logger.isDebugEnabled()) {
/* 385 */             this.logger.debug("Bean with name '" + finalName + "' concluding interceptor chain is not an advisor class: treating it as a target or TargetSource");
/*     */           }
/*     */           
/* 388 */           String[] newNames = new String[this.interceptorNames.length - 1];
/* 389 */           System.arraycopy(this.interceptorNames, 0, newNames, 0, newNames.length);
/* 390 */           this.interceptorNames = newNames;
/*     */         } 
/*     */       }
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
/*     */   private boolean isNamedBeanAnAdvisorOrAdvice(String beanName) {
/* 404 */     Class<?> namedBeanClass = this.beanFactory.getType(beanName);
/* 405 */     if (namedBeanClass != null) {
/* 406 */       return (Advisor.class.isAssignableFrom(namedBeanClass) || Advice.class.isAssignableFrom(namedBeanClass));
/*     */     }
/*     */     
/* 409 */     if (this.logger.isDebugEnabled()) {
/* 410 */       this.logger.debug("Could not determine type of bean with name '" + beanName + "' - assuming it is neither an Advisor nor an Advice");
/*     */     }
/*     */     
/* 413 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized void initializeAdvisorChain() throws AopConfigException, BeansException {
/* 423 */     if (this.advisorChainInitialized) {
/*     */       return;
/*     */     }
/*     */     
/* 427 */     if (!ObjectUtils.isEmpty((Object[])this.interceptorNames)) {
/* 428 */       if (this.beanFactory == null) {
/* 429 */         throw new IllegalStateException("No BeanFactory available anymore (probably due to serialization) - cannot resolve interceptor names " + 
/* 430 */             Arrays.asList(this.interceptorNames));
/*     */       }
/*     */ 
/*     */       
/* 434 */       if (this.interceptorNames[this.interceptorNames.length - 1].endsWith("*") && this.targetName == null && this.targetSource == EMPTY_TARGET_SOURCE)
/*     */       {
/* 436 */         throw new AopConfigException("Target required after globals");
/*     */       }
/*     */ 
/*     */       
/* 440 */       for (String name : this.interceptorNames) {
/* 441 */         if (this.logger.isTraceEnabled()) {
/* 442 */           this.logger.trace("Configuring advisor or advice '" + name + "'");
/*     */         }
/*     */         
/* 445 */         if (name.endsWith("*")) {
/* 446 */           if (!(this.beanFactory instanceof ListableBeanFactory)) {
/* 447 */             throw new AopConfigException("Can only use global advisors or interceptors with a ListableBeanFactory");
/*     */           }
/*     */           
/* 450 */           addGlobalAdvisor((ListableBeanFactory)this.beanFactory, name
/* 451 */               .substring(0, name.length() - "*".length()));
/*     */         } else {
/*     */           Object advice;
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 458 */           if (this.singleton || this.beanFactory.isSingleton(name)) {
/*     */             
/* 460 */             advice = this.beanFactory.getBean(name);
/*     */           
/*     */           }
/*     */           else {
/*     */             
/* 465 */             advice = new PrototypePlaceholderAdvisor(name);
/*     */           } 
/* 467 */           addAdvisorOnChainCreation(advice, name);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 472 */     this.advisorChainInitialized = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Advisor> freshAdvisorChain() {
/* 482 */     Advisor[] advisors = getAdvisors();
/* 483 */     List<Advisor> freshAdvisors = new ArrayList<Advisor>(advisors.length);
/* 484 */     for (Advisor advisor : advisors) {
/* 485 */       if (advisor instanceof PrototypePlaceholderAdvisor) {
/* 486 */         PrototypePlaceholderAdvisor pa = (PrototypePlaceholderAdvisor)advisor;
/* 487 */         if (this.logger.isDebugEnabled()) {
/* 488 */           this.logger.debug("Refreshing bean named '" + pa.getBeanName() + "'");
/*     */         }
/*     */ 
/*     */         
/* 492 */         if (this.beanFactory == null) {
/* 493 */           throw new IllegalStateException("No BeanFactory available anymore (probably due to serialization) - cannot resolve prototype advisor '" + pa
/* 494 */               .getBeanName() + "'");
/*     */         }
/* 496 */         Object bean = this.beanFactory.getBean(pa.getBeanName());
/* 497 */         Advisor refreshedAdvisor = namedBeanToAdvisor(bean);
/* 498 */         freshAdvisors.add(refreshedAdvisor);
/*     */       }
/*     */       else {
/*     */         
/* 502 */         freshAdvisors.add(advisor);
/*     */       } 
/*     */     } 
/* 505 */     return freshAdvisors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addGlobalAdvisor(ListableBeanFactory beanFactory, String prefix) {
/* 513 */     String[] globalAdvisorNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, Advisor.class);
/*     */     
/* 515 */     String[] globalInterceptorNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, Interceptor.class);
/* 516 */     List<Object> beans = new ArrayList(globalAdvisorNames.length + globalInterceptorNames.length);
/* 517 */     Map<Object, String> names = new HashMap<Object, String>(beans.size());
/* 518 */     for (String name : globalAdvisorNames) {
/* 519 */       Object bean = beanFactory.getBean(name);
/* 520 */       beans.add(bean);
/* 521 */       names.put(bean, name);
/*     */     } 
/* 523 */     for (String name : globalInterceptorNames) {
/* 524 */       Object bean = beanFactory.getBean(name);
/* 525 */       beans.add(bean);
/* 526 */       names.put(bean, name);
/*     */     } 
/* 528 */     AnnotationAwareOrderComparator.sort(beans);
/* 529 */     for (Object bean : beans) {
/* 530 */       String name = names.get(bean);
/* 531 */       if (name.startsWith(prefix)) {
/* 532 */         addAdvisorOnChainCreation(bean, name);
/*     */       }
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
/*     */   private void addAdvisorOnChainCreation(Object next, String name) {
/* 549 */     Advisor advisor = namedBeanToAdvisor(next);
/* 550 */     if (this.logger.isTraceEnabled()) {
/* 551 */       this.logger.trace("Adding advisor with name '" + name + "'");
/*     */     }
/* 553 */     addAdvisor(advisor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TargetSource freshTargetSource() {
/* 563 */     if (this.targetName == null) {
/* 564 */       if (this.logger.isTraceEnabled()) {
/* 565 */         this.logger.trace("Not refreshing target: Bean name not specified in 'interceptorNames'.");
/*     */       }
/* 567 */       return this.targetSource;
/*     */     } 
/*     */     
/* 570 */     if (this.beanFactory == null) {
/* 571 */       throw new IllegalStateException("No BeanFactory available anymore (probably due to serialization) - cannot resolve target with name '" + this.targetName + "'");
/*     */     }
/*     */     
/* 574 */     if (this.logger.isDebugEnabled()) {
/* 575 */       this.logger.debug("Refreshing target with name '" + this.targetName + "'");
/*     */     }
/* 577 */     Object target = this.beanFactory.getBean(this.targetName);
/* 578 */     return (target instanceof TargetSource) ? (TargetSource)target : (TargetSource)new SingletonTargetSource(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Advisor namedBeanToAdvisor(Object next) {
/*     */     try {
/* 588 */       return this.advisorAdapterRegistry.wrap(next);
/*     */     }
/* 590 */     catch (UnknownAdviceTypeException ex) {
/*     */ 
/*     */       
/* 593 */       throw new AopConfigException("Unknown advisor type " + next.getClass() + "; Can only include Advisor or Advice type beans in interceptorNames chain except for last entry,which may also be target or TargetSource", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void adviceChanged() {
/* 604 */     super.adviceChanged();
/* 605 */     if (this.singleton) {
/* 606 */       this.logger.debug("Advice has changed; recaching singleton instance");
/* 607 */       synchronized (this) {
/* 608 */         this.singletonInstance = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 620 */     ois.defaultReadObject();
/*     */ 
/*     */     
/* 623 */     this.proxyClassLoader = ClassUtils.getDefaultClassLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PrototypePlaceholderAdvisor
/*     */     implements Advisor, Serializable
/*     */   {
/*     */     private final String beanName;
/*     */ 
/*     */     
/*     */     private final String message;
/*     */ 
/*     */     
/*     */     public PrototypePlaceholderAdvisor(String beanName) {
/* 638 */       this.beanName = beanName;
/* 639 */       this.message = "Placeholder for prototype Advisor/Advice with bean name '" + beanName + "'";
/*     */     }
/*     */     
/*     */     public String getBeanName() {
/* 643 */       return this.beanName;
/*     */     }
/*     */ 
/*     */     
/*     */     public Advice getAdvice() {
/* 648 */       throw new UnsupportedOperationException("Cannot invoke methods: " + this.message);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isPerInstance() {
/* 653 */       throw new UnsupportedOperationException("Cannot invoke methods: " + this.message);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 658 */       return this.message;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\ProxyFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */