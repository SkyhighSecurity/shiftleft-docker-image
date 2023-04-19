/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanCreationNotAllowedException;
/*     */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.ObjectFactory;
/*     */ import org.springframework.beans.factory.config.SingletonBeanRegistry;
/*     */ import org.springframework.core.SimpleAliasRegistry;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class DefaultSingletonBeanRegistry
/*     */   extends SimpleAliasRegistry
/*     */   implements SingletonBeanRegistry
/*     */ {
/*  79 */   protected static final Object NULL_OBJECT = new Object();
/*     */ 
/*     */ 
/*     */   
/*  83 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*  86 */   private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(256);
/*     */ 
/*     */   
/*  89 */   private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<String, ObjectFactory<?>>(16);
/*     */ 
/*     */   
/*  92 */   private final Map<String, Object> earlySingletonObjects = new HashMap<String, Object>(16);
/*     */ 
/*     */   
/*  95 */   private final Set<String> registeredSingletons = new LinkedHashSet<String>(256);
/*     */ 
/*     */ 
/*     */   
/*  99 */   private final Set<String> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>(16));
/*     */ 
/*     */ 
/*     */   
/* 103 */   private final Set<String> inCreationCheckExclusions = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>(16));
/*     */ 
/*     */   
/*     */   private Set<Exception> suppressedExceptions;
/*     */ 
/*     */   
/*     */   private boolean singletonsCurrentlyInDestruction = false;
/*     */ 
/*     */   
/* 112 */   private final Map<String, Object> disposableBeans = new LinkedHashMap<String, Object>();
/*     */ 
/*     */   
/* 115 */   private final Map<String, Set<String>> containedBeanMap = new ConcurrentHashMap<String, Set<String>>(16);
/*     */ 
/*     */   
/* 118 */   private final Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap<String, Set<String>>(64);
/*     */ 
/*     */   
/* 121 */   private final Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<String, Set<String>>(64);
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerSingleton(String beanName, Object singletonObject) throws IllegalStateException {
/* 126 */     Assert.notNull(beanName, "'beanName' must not be null");
/* 127 */     synchronized (this.singletonObjects) {
/* 128 */       Object oldObject = this.singletonObjects.get(beanName);
/* 129 */       if (oldObject != null) {
/* 130 */         throw new IllegalStateException("Could not register object [" + singletonObject + "] under bean name '" + beanName + "': there is already object [" + oldObject + "] bound");
/*     */       }
/*     */       
/* 133 */       addSingleton(beanName, singletonObject);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addSingleton(String beanName, Object singletonObject) {
/* 144 */     synchronized (this.singletonObjects) {
/* 145 */       this.singletonObjects.put(beanName, (singletonObject != null) ? singletonObject : NULL_OBJECT);
/* 146 */       this.singletonFactories.remove(beanName);
/* 147 */       this.earlySingletonObjects.remove(beanName);
/* 148 */       this.registeredSingletons.add(beanName);
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
/*     */   protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
/* 161 */     Assert.notNull(singletonFactory, "Singleton factory must not be null");
/* 162 */     synchronized (this.singletonObjects) {
/* 163 */       if (!this.singletonObjects.containsKey(beanName)) {
/* 164 */         this.singletonFactories.put(beanName, singletonFactory);
/* 165 */         this.earlySingletonObjects.remove(beanName);
/* 166 */         this.registeredSingletons.add(beanName);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getSingleton(String beanName) {
/* 173 */     return getSingleton(beanName, true);
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
/*     */   protected Object getSingleton(String beanName, boolean allowEarlyReference) {
/* 185 */     Object singletonObject = this.singletonObjects.get(beanName);
/* 186 */     if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
/* 187 */       synchronized (this.singletonObjects) {
/* 188 */         singletonObject = this.earlySingletonObjects.get(beanName);
/* 189 */         if (singletonObject == null && allowEarlyReference) {
/* 190 */           ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
/* 191 */           if (singletonFactory != null) {
/* 192 */             singletonObject = singletonFactory.getObject();
/* 193 */             this.earlySingletonObjects.put(beanName, singletonObject);
/* 194 */             this.singletonFactories.remove(beanName);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/* 199 */     return (singletonObject != NULL_OBJECT) ? singletonObject : null;
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
/*     */   public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {
/* 211 */     Assert.notNull(beanName, "'beanName' must not be null");
/* 212 */     synchronized (this.singletonObjects) {
/* 213 */       Object singletonObject = this.singletonObjects.get(beanName);
/* 214 */       if (singletonObject == null) {
/* 215 */         if (this.singletonsCurrentlyInDestruction) {
/* 216 */           throw new BeanCreationNotAllowedException(beanName, "Singleton bean creation not allowed while singletons of this factory are in destruction (Do not request a bean from a BeanFactory in a destroy method implementation!)");
/*     */         }
/*     */ 
/*     */         
/* 220 */         if (this.logger.isDebugEnabled()) {
/* 221 */           this.logger.debug("Creating shared instance of singleton bean '" + beanName + "'");
/*     */         }
/* 223 */         beforeSingletonCreation(beanName);
/* 224 */         boolean newSingleton = false;
/* 225 */         boolean recordSuppressedExceptions = (this.suppressedExceptions == null);
/* 226 */         if (recordSuppressedExceptions) {
/* 227 */           this.suppressedExceptions = new LinkedHashSet<Exception>();
/*     */         }
/*     */         try {
/* 230 */           singletonObject = singletonFactory.getObject();
/* 231 */           newSingleton = true;
/*     */         }
/* 233 */         catch (IllegalStateException ex) {
/*     */ 
/*     */           
/* 236 */           singletonObject = this.singletonObjects.get(beanName);
/* 237 */           if (singletonObject == null) {
/* 238 */             throw ex;
/*     */           }
/*     */         }
/* 241 */         catch (BeanCreationException ex) {
/* 242 */           if (recordSuppressedExceptions) {
/* 243 */             for (Exception suppressedException : this.suppressedExceptions) {
/* 244 */               ex.addRelatedCause(suppressedException);
/*     */             }
/*     */           }
/* 247 */           throw ex;
/*     */         } finally {
/*     */           
/* 250 */           if (recordSuppressedExceptions) {
/* 251 */             this.suppressedExceptions = null;
/*     */           }
/* 253 */           afterSingletonCreation(beanName);
/*     */         } 
/* 255 */         if (newSingleton) {
/* 256 */           addSingleton(beanName, singletonObject);
/*     */         }
/*     */       } 
/* 259 */       return (singletonObject != NULL_OBJECT) ? singletonObject : null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onSuppressedException(Exception ex) {
/* 269 */     synchronized (this.singletonObjects) {
/* 270 */       if (this.suppressedExceptions != null) {
/* 271 */         this.suppressedExceptions.add(ex);
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
/*     */   protected void removeSingleton(String beanName) {
/* 283 */     synchronized (this.singletonObjects) {
/* 284 */       this.singletonObjects.remove(beanName);
/* 285 */       this.singletonFactories.remove(beanName);
/* 286 */       this.earlySingletonObjects.remove(beanName);
/* 287 */       this.registeredSingletons.remove(beanName);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsSingleton(String beanName) {
/* 293 */     return this.singletonObjects.containsKey(beanName);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getSingletonNames() {
/* 298 */     synchronized (this.singletonObjects) {
/* 299 */       return StringUtils.toStringArray(this.registeredSingletons);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSingletonCount() {
/* 305 */     synchronized (this.singletonObjects) {
/* 306 */       return this.registeredSingletons.size();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCurrentlyInCreation(String beanName, boolean inCreation) {
/* 312 */     Assert.notNull(beanName, "Bean name must not be null");
/* 313 */     if (!inCreation) {
/* 314 */       this.inCreationCheckExclusions.add(beanName);
/*     */     } else {
/*     */       
/* 317 */       this.inCreationCheckExclusions.remove(beanName);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isCurrentlyInCreation(String beanName) {
/* 322 */     Assert.notNull(beanName, "Bean name must not be null");
/* 323 */     return (!this.inCreationCheckExclusions.contains(beanName) && isActuallyInCreation(beanName));
/*     */   }
/*     */   
/*     */   protected boolean isActuallyInCreation(String beanName) {
/* 327 */     return isSingletonCurrentlyInCreation(beanName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSingletonCurrentlyInCreation(String beanName) {
/* 336 */     return this.singletonsCurrentlyInCreation.contains(beanName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void beforeSingletonCreation(String beanName) {
/* 346 */     if (!this.inCreationCheckExclusions.contains(beanName) && !this.singletonsCurrentlyInCreation.add(beanName)) {
/* 347 */       throw new BeanCurrentlyInCreationException(beanName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void afterSingletonCreation(String beanName) {
/* 358 */     if (!this.inCreationCheckExclusions.contains(beanName) && !this.singletonsCurrentlyInCreation.remove(beanName)) {
/* 359 */       throw new IllegalStateException("Singleton '" + beanName + "' isn't currently in creation");
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
/*     */   public void registerDisposableBean(String beanName, DisposableBean bean) {
/* 374 */     synchronized (this.disposableBeans) {
/* 375 */       this.disposableBeans.put(beanName, bean);
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
/*     */   public void registerContainedBean(String containedBeanName, String containingBeanName) {
/* 389 */     synchronized (this.containedBeanMap) {
/* 390 */       Set<String> containedBeans = this.containedBeanMap.get(containingBeanName);
/* 391 */       if (containedBeans == null) {
/* 392 */         containedBeans = new LinkedHashSet<String>(8);
/* 393 */         this.containedBeanMap.put(containingBeanName, containedBeans);
/*     */       } 
/* 395 */       containedBeans.add(containedBeanName);
/*     */     } 
/* 397 */     registerDependentBean(containedBeanName, containingBeanName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerDependentBean(String beanName, String dependentBeanName) {
/* 407 */     String canonicalName = canonicalName(beanName);
/*     */     
/* 409 */     synchronized (this.dependentBeanMap) {
/* 410 */       Set<String> dependentBeans = this.dependentBeanMap.get(canonicalName);
/* 411 */       if (dependentBeans == null) {
/* 412 */         dependentBeans = new LinkedHashSet<String>(8);
/* 413 */         this.dependentBeanMap.put(canonicalName, dependentBeans);
/*     */       } 
/* 415 */       dependentBeans.add(dependentBeanName);
/*     */     } 
/* 417 */     synchronized (this.dependenciesForBeanMap) {
/* 418 */       Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(dependentBeanName);
/* 419 */       if (dependenciesForBean == null) {
/* 420 */         dependenciesForBean = new LinkedHashSet<String>(8);
/* 421 */         this.dependenciesForBeanMap.put(dependentBeanName, dependenciesForBean);
/*     */       } 
/* 423 */       dependenciesForBean.add(canonicalName);
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
/*     */   protected boolean isDependent(String beanName, String dependentBeanName) {
/* 435 */     synchronized (this.dependentBeanMap) {
/* 436 */       return isDependent(beanName, dependentBeanName, (Set<String>)null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isDependent(String beanName, String dependentBeanName, Set<String> alreadySeen) {
/* 441 */     if (alreadySeen != null && alreadySeen.contains(beanName)) {
/* 442 */       return false;
/*     */     }
/* 444 */     String canonicalName = canonicalName(beanName);
/* 445 */     Set<String> dependentBeans = this.dependentBeanMap.get(canonicalName);
/* 446 */     if (dependentBeans == null) {
/* 447 */       return false;
/*     */     }
/* 449 */     if (dependentBeans.contains(dependentBeanName)) {
/* 450 */       return true;
/*     */     }
/* 452 */     for (String transitiveDependency : dependentBeans) {
/* 453 */       if (alreadySeen == null) {
/* 454 */         alreadySeen = new HashSet<String>();
/*     */       }
/* 456 */       alreadySeen.add(beanName);
/* 457 */       if (isDependent(transitiveDependency, dependentBeanName, alreadySeen)) {
/* 458 */         return true;
/*     */       }
/*     */     } 
/* 461 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean hasDependentBean(String beanName) {
/* 469 */     return this.dependentBeanMap.containsKey(beanName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getDependentBeans(String beanName) {
/* 478 */     Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
/* 479 */     if (dependentBeans == null) {
/* 480 */       return new String[0];
/*     */     }
/* 482 */     synchronized (this.dependentBeanMap) {
/* 483 */       return StringUtils.toStringArray(dependentBeans);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getDependenciesForBean(String beanName) {
/* 494 */     Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(beanName);
/* 495 */     if (dependenciesForBean == null) {
/* 496 */       return new String[0];
/*     */     }
/* 498 */     synchronized (this.dependenciesForBeanMap) {
/* 499 */       return StringUtils.toStringArray(dependenciesForBean);
/*     */     } 
/*     */   }
/*     */   public void destroySingletons() {
/*     */     String[] disposableBeanNames;
/* 504 */     if (this.logger.isDebugEnabled()) {
/* 505 */       this.logger.debug("Destroying singletons in " + this);
/*     */     }
/* 507 */     synchronized (this.singletonObjects) {
/* 508 */       this.singletonsCurrentlyInDestruction = true;
/*     */     } 
/*     */ 
/*     */     
/* 512 */     synchronized (this.disposableBeans) {
/* 513 */       disposableBeanNames = StringUtils.toStringArray(this.disposableBeans.keySet());
/*     */     } 
/* 515 */     for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
/* 516 */       destroySingleton(disposableBeanNames[i]);
/*     */     }
/*     */     
/* 519 */     this.containedBeanMap.clear();
/* 520 */     this.dependentBeanMap.clear();
/* 521 */     this.dependenciesForBeanMap.clear();
/*     */     
/* 523 */     clearSingletonCache();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void clearSingletonCache() {
/* 531 */     synchronized (this.singletonObjects) {
/* 532 */       this.singletonObjects.clear();
/* 533 */       this.singletonFactories.clear();
/* 534 */       this.earlySingletonObjects.clear();
/* 535 */       this.registeredSingletons.clear();
/* 536 */       this.singletonsCurrentlyInDestruction = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroySingleton(String beanName) {
/*     */     DisposableBean disposableBean;
/* 548 */     removeSingleton(beanName);
/*     */ 
/*     */ 
/*     */     
/* 552 */     synchronized (this.disposableBeans) {
/* 553 */       disposableBean = (DisposableBean)this.disposableBeans.remove(beanName);
/*     */     } 
/* 555 */     destroyBean(beanName, disposableBean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void destroyBean(String beanName, DisposableBean bean) {
/*     */     Set<String> dependencies, containedBeans;
/* 567 */     synchronized (this.dependentBeanMap) {
/*     */       
/* 569 */       dependencies = this.dependentBeanMap.remove(beanName);
/*     */     } 
/* 571 */     if (dependencies != null) {
/* 572 */       if (this.logger.isDebugEnabled()) {
/* 573 */         this.logger.debug("Retrieved dependent beans for bean '" + beanName + "': " + dependencies);
/*     */       }
/* 575 */       for (String dependentBeanName : dependencies) {
/* 576 */         destroySingleton(dependentBeanName);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 581 */     if (bean != null) {
/*     */       try {
/* 583 */         bean.destroy();
/*     */       }
/* 585 */       catch (Throwable ex) {
/* 586 */         this.logger.error("Destroy method on bean with name '" + beanName + "' threw an exception", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 592 */     synchronized (this.containedBeanMap) {
/*     */       
/* 594 */       containedBeans = this.containedBeanMap.remove(beanName);
/*     */     } 
/* 596 */     if (containedBeans != null) {
/* 597 */       for (String containedBeanName : containedBeans) {
/* 598 */         destroySingleton(containedBeanName);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 603 */     synchronized (this.dependentBeanMap) {
/* 604 */       for (Iterator<Map.Entry<String, Set<String>>> it = this.dependentBeanMap.entrySet().iterator(); it.hasNext(); ) {
/* 605 */         Map.Entry<String, Set<String>> entry = it.next();
/* 606 */         Set<String> dependenciesToClean = entry.getValue();
/* 607 */         dependenciesToClean.remove(beanName);
/* 608 */         if (dependenciesToClean.isEmpty()) {
/* 609 */           it.remove();
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 615 */     this.dependenciesForBeanMap.remove(beanName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object getSingletonMutex() {
/* 626 */     return this.singletonObjects;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\DefaultSingletonBeanRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */