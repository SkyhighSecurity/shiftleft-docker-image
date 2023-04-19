/*     */ package org.springframework.aop.framework.autoproxy;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.Pointcut;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.framework.AopInfrastructureBean;
/*     */ import org.springframework.aop.framework.ProxyConfig;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.aop.framework.ProxyProcessorSupport;
/*     */ import org.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
/*     */ import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
/*     */ import org.springframework.aop.target.SingletonTargetSource;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
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
/*     */ public abstract class AbstractAutoProxyCreator
/*     */   extends ProxyProcessorSupport
/*     */   implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware
/*     */ {
/*  99 */   protected static final Object[] DO_NOT_PROXY = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   protected static final Object[] PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS = new Object[0];
/*     */ 
/*     */ 
/*     */   
/* 110 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/* 113 */   private AdvisorAdapterRegistry advisorAdapterRegistry = GlobalAdvisorAdapterRegistry.getInstance();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean freezeProxy = false;
/*     */ 
/*     */ 
/*     */   
/* 122 */   private String[] interceptorNames = new String[0];
/*     */ 
/*     */   
/*     */   private boolean applyCommonInterceptorsFirst = true;
/*     */   
/*     */   private TargetSourceCreator[] customTargetSourceCreators;
/*     */   
/*     */   private BeanFactory beanFactory;
/*     */   
/* 131 */   private final Set<String> targetSourcedBeans = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>(16));
/*     */ 
/*     */   
/* 134 */   private final Set<Object> earlyProxyReferences = Collections.newSetFromMap(new ConcurrentHashMap<Object, Boolean>(16));
/*     */   
/* 136 */   private final Map<Object, Class<?>> proxyTypes = new ConcurrentHashMap<Object, Class<?>>(16);
/*     */   
/* 138 */   private final Map<Object, Boolean> advisedBeans = new ConcurrentHashMap<Object, Boolean>(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFrozen(boolean frozen) {
/* 149 */     this.freezeProxy = frozen;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFrozen() {
/* 154 */     return this.freezeProxy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdvisorAdapterRegistry(AdvisorAdapterRegistry advisorAdapterRegistry) {
/* 163 */     this.advisorAdapterRegistry = advisorAdapterRegistry;
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
/*     */   public void setCustomTargetSourceCreators(TargetSourceCreator... targetSourceCreators) {
/* 181 */     this.customTargetSourceCreators = targetSourceCreators;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterceptorNames(String... interceptorNames) {
/* 192 */     this.interceptorNames = interceptorNames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setApplyCommonInterceptorsFirst(boolean applyCommonInterceptorsFirst) {
/* 200 */     this.applyCommonInterceptorsFirst = applyCommonInterceptorsFirst;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 205 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanFactory getBeanFactory() {
/* 213 */     return this.beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> predictBeanType(Class<?> beanClass, String beanName) {
/* 219 */     if (this.proxyTypes.isEmpty()) {
/* 220 */       return null;
/*     */     }
/* 222 */     Object cacheKey = getCacheKey(beanClass, beanName);
/* 223 */     return this.proxyTypes.get(cacheKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException {
/* 228 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
/* 233 */     Object cacheKey = getCacheKey(bean.getClass(), beanName);
/* 234 */     if (!this.earlyProxyReferences.contains(cacheKey)) {
/* 235 */       this.earlyProxyReferences.add(cacheKey);
/*     */     }
/* 237 */     return wrapIfNecessary(bean, beanName, cacheKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
/* 242 */     Object cacheKey = getCacheKey(beanClass, beanName);
/*     */     
/* 244 */     if (beanName == null || !this.targetSourcedBeans.contains(beanName)) {
/* 245 */       if (this.advisedBeans.containsKey(cacheKey)) {
/* 246 */         return null;
/*     */       }
/* 248 */       if (isInfrastructureClass(beanClass) || shouldSkip(beanClass, beanName)) {
/* 249 */         this.advisedBeans.put(cacheKey, Boolean.FALSE);
/* 250 */         return null;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 257 */     if (beanName != null) {
/* 258 */       TargetSource targetSource = getCustomTargetSource(beanClass, beanName);
/* 259 */       if (targetSource != null) {
/* 260 */         this.targetSourcedBeans.add(beanName);
/* 261 */         Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(beanClass, beanName, targetSource);
/* 262 */         Object proxy = createProxy(beanClass, beanName, specificInterceptors, targetSource);
/* 263 */         this.proxyTypes.put(cacheKey, proxy.getClass());
/* 264 */         return proxy;
/*     */       } 
/*     */     } 
/*     */     
/* 268 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean postProcessAfterInstantiation(Object bean, String beanName) {
/* 273 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) {
/* 280 */     return pvs;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName) {
/* 285 */     return bean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
/* 295 */     if (bean != null) {
/* 296 */       Object cacheKey = getCacheKey(bean.getClass(), beanName);
/* 297 */       if (!this.earlyProxyReferences.contains(cacheKey)) {
/* 298 */         return wrapIfNecessary(bean, beanName, cacheKey);
/*     */       }
/*     */     } 
/* 301 */     return bean;
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
/*     */   protected Object getCacheKey(Class<?> beanClass, String beanName) {
/* 317 */     if (StringUtils.hasLength(beanName)) {
/* 318 */       return FactoryBean.class.isAssignableFrom(beanClass) ? ("&" + beanName) : beanName;
/*     */     }
/*     */ 
/*     */     
/* 322 */     return beanClass;
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
/*     */   protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
/* 334 */     if (beanName != null && this.targetSourcedBeans.contains(beanName)) {
/* 335 */       return bean;
/*     */     }
/* 337 */     if (Boolean.FALSE.equals(this.advisedBeans.get(cacheKey))) {
/* 338 */       return bean;
/*     */     }
/* 340 */     if (isInfrastructureClass(bean.getClass()) || shouldSkip(bean.getClass(), beanName)) {
/* 341 */       this.advisedBeans.put(cacheKey, Boolean.FALSE);
/* 342 */       return bean;
/*     */     } 
/*     */ 
/*     */     
/* 346 */     Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, (TargetSource)null);
/* 347 */     if (specificInterceptors != DO_NOT_PROXY) {
/* 348 */       this.advisedBeans.put(cacheKey, Boolean.TRUE);
/* 349 */       Object proxy = createProxy(bean
/* 350 */           .getClass(), beanName, specificInterceptors, (TargetSource)new SingletonTargetSource(bean));
/* 351 */       this.proxyTypes.put(cacheKey, proxy.getClass());
/* 352 */       return proxy;
/*     */     } 
/*     */     
/* 355 */     this.advisedBeans.put(cacheKey, Boolean.FALSE);
/* 356 */     return bean;
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
/*     */   protected boolean isInfrastructureClass(Class<?> beanClass) {
/* 375 */     boolean retVal = (Advice.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass) || Advisor.class.isAssignableFrom(beanClass) || AopInfrastructureBean.class.isAssignableFrom(beanClass));
/* 376 */     if (retVal && this.logger.isTraceEnabled()) {
/* 377 */       this.logger.trace("Did not attempt to auto-proxy infrastructure class [" + beanClass.getName() + "]");
/*     */     }
/* 379 */     return retVal;
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
/*     */   protected boolean shouldSkip(Class<?> beanClass, String beanName) {
/* 392 */     return false;
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
/*     */   protected TargetSource getCustomTargetSource(Class<?> beanClass, String beanName) {
/* 407 */     if (this.customTargetSourceCreators != null && this.beanFactory != null && this.beanFactory
/* 408 */       .containsBean(beanName)) {
/* 409 */       for (TargetSourceCreator tsc : this.customTargetSourceCreators) {
/* 410 */         TargetSource ts = tsc.getTargetSource(beanClass, beanName);
/* 411 */         if (ts != null) {
/*     */           
/* 413 */           if (this.logger.isDebugEnabled()) {
/* 414 */             this.logger.debug("TargetSourceCreator [" + tsc + "] found custom TargetSource for bean with name '" + beanName + "'");
/*     */           }
/*     */           
/* 417 */           return ts;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 423 */     return null;
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
/*     */   protected Object createProxy(Class<?> beanClass, String beanName, Object[] specificInterceptors, TargetSource targetSource) {
/* 440 */     if (this.beanFactory instanceof ConfigurableListableBeanFactory) {
/* 441 */       AutoProxyUtils.exposeTargetClass((ConfigurableListableBeanFactory)this.beanFactory, beanName, beanClass);
/*     */     }
/*     */     
/* 444 */     ProxyFactory proxyFactory = new ProxyFactory();
/* 445 */     proxyFactory.copyFrom((ProxyConfig)this);
/*     */     
/* 447 */     if (!proxyFactory.isProxyTargetClass()) {
/* 448 */       if (shouldProxyTargetClass(beanClass, beanName)) {
/* 449 */         proxyFactory.setProxyTargetClass(true);
/*     */       } else {
/*     */         
/* 452 */         evaluateProxyInterfaces(beanClass, proxyFactory);
/*     */       } 
/*     */     }
/*     */     
/* 456 */     Advisor[] advisors = buildAdvisors(beanName, specificInterceptors);
/* 457 */     proxyFactory.addAdvisors(advisors);
/* 458 */     proxyFactory.setTargetSource(targetSource);
/* 459 */     customizeProxyFactory(proxyFactory);
/*     */     
/* 461 */     proxyFactory.setFrozen(this.freezeProxy);
/* 462 */     if (advisorsPreFiltered()) {
/* 463 */       proxyFactory.setPreFiltered(true);
/*     */     }
/*     */     
/* 466 */     return proxyFactory.getProxy(getProxyClassLoader());
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
/*     */   protected boolean shouldProxyTargetClass(Class<?> beanClass, String beanName) {
/* 479 */     return (this.beanFactory instanceof ConfigurableListableBeanFactory && 
/* 480 */       AutoProxyUtils.shouldProxyTargetClass((ConfigurableListableBeanFactory)this.beanFactory, beanName));
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
/*     */   protected boolean advisorsPreFiltered() {
/* 494 */     return false;
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
/*     */   protected Advisor[] buildAdvisors(String beanName, Object[] specificInterceptors) {
/* 507 */     Advisor[] commonInterceptors = resolveInterceptorNames();
/*     */     
/* 509 */     List<Object> allInterceptors = new ArrayList();
/* 510 */     if (specificInterceptors != null) {
/* 511 */       allInterceptors.addAll(Arrays.asList(specificInterceptors));
/* 512 */       if (commonInterceptors.length > 0) {
/* 513 */         if (this.applyCommonInterceptorsFirst) {
/* 514 */           allInterceptors.addAll(0, Arrays.asList((Object[])commonInterceptors));
/*     */         } else {
/*     */           
/* 517 */           allInterceptors.addAll(Arrays.asList((Object[])commonInterceptors));
/*     */         } 
/*     */       }
/*     */     } 
/* 521 */     if (this.logger.isDebugEnabled()) {
/* 522 */       int nrOfCommonInterceptors = commonInterceptors.length;
/* 523 */       int nrOfSpecificInterceptors = (specificInterceptors != null) ? specificInterceptors.length : 0;
/* 524 */       this.logger.debug("Creating implicit proxy for bean '" + beanName + "' with " + nrOfCommonInterceptors + " common interceptors and " + nrOfSpecificInterceptors + " specific interceptors");
/*     */     } 
/*     */ 
/*     */     
/* 528 */     Advisor[] advisors = new Advisor[allInterceptors.size()];
/* 529 */     for (int i = 0; i < allInterceptors.size(); i++) {
/* 530 */       advisors[i] = this.advisorAdapterRegistry.wrap(allInterceptors.get(i));
/*     */     }
/* 532 */     return advisors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Advisor[] resolveInterceptorNames() {
/* 540 */     ConfigurableBeanFactory cbf = (this.beanFactory instanceof ConfigurableBeanFactory) ? (ConfigurableBeanFactory)this.beanFactory : null;
/*     */     
/* 542 */     List<Advisor> advisors = new ArrayList<Advisor>();
/* 543 */     for (String beanName : this.interceptorNames) {
/* 544 */       if (cbf == null || !cbf.isCurrentlyInCreation(beanName)) {
/* 545 */         Object next = this.beanFactory.getBean(beanName);
/* 546 */         advisors.add(this.advisorAdapterRegistry.wrap(next));
/*     */       } 
/*     */     } 
/* 549 */     return advisors.<Advisor>toArray(new Advisor[advisors.size()]);
/*     */   }
/*     */   
/*     */   protected void customizeProxyFactory(ProxyFactory proxyFactory) {}
/*     */   
/*     */   protected abstract Object[] getAdvicesAndAdvisorsForBean(Class<?> paramClass, String paramString, TargetSource paramTargetSource) throws BeansException;
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\autoproxy\AbstractAutoProxyCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */