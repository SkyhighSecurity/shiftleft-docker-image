/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.IntroductionAdvisor;
/*     */ import org.springframework.aop.IntroductionInfo;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.support.DefaultIntroductionAdvisor;
/*     */ import org.springframework.aop.support.DefaultPointcutAdvisor;
/*     */ import org.springframework.aop.target.EmptyTargetSource;
/*     */ import org.springframework.aop.target.SingletonTargetSource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AdvisedSupport
/*     */   extends ProxyConfig
/*     */   implements Advised
/*     */ {
/*     */   private static final long serialVersionUID = 2651364800145442165L;
/*  70 */   public static final TargetSource EMPTY_TARGET_SOURCE = (TargetSource)EmptyTargetSource.INSTANCE;
/*     */ 
/*     */ 
/*     */   
/*  74 */   TargetSource targetSource = EMPTY_TARGET_SOURCE;
/*     */ 
/*     */   
/*     */   private boolean preFiltered = false;
/*     */ 
/*     */   
/*  80 */   AdvisorChainFactory advisorChainFactory = new DefaultAdvisorChainFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient Map<MethodCacheKey, List<Object>> methodCache;
/*     */ 
/*     */ 
/*     */   
/*  89 */   private List<Class<?>> interfaces = new ArrayList<Class<?>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   private List<Advisor> advisors = new ArrayList<Advisor>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   private Advisor[] advisorArray = new Advisor[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AdvisedSupport() {
/* 108 */     initMethodCache();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AdvisedSupport(Class<?>... interfaces) {
/* 116 */     this();
/* 117 */     setInterfaces(interfaces);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initMethodCache() {
/* 124 */     this.methodCache = new ConcurrentHashMap<MethodCacheKey, List<Object>>(32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(Object target) {
/* 135 */     setTargetSource((TargetSource)new SingletonTargetSource(target));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTargetSource(TargetSource targetSource) {
/* 140 */     this.targetSource = (targetSource != null) ? targetSource : EMPTY_TARGET_SOURCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public TargetSource getTargetSource() {
/* 145 */     return this.targetSource;
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
/*     */   public void setTargetClass(Class<?> targetClass) {
/* 162 */     this.targetSource = (TargetSource)EmptyTargetSource.forClass(targetClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getTargetClass() {
/* 167 */     return this.targetSource.getTargetClass();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPreFiltered(boolean preFiltered) {
/* 172 */     this.preFiltered = preFiltered;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPreFiltered() {
/* 177 */     return this.preFiltered;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdvisorChainFactory(AdvisorChainFactory advisorChainFactory) {
/* 185 */     Assert.notNull(advisorChainFactory, "AdvisorChainFactory must not be null");
/* 186 */     this.advisorChainFactory = advisorChainFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AdvisorChainFactory getAdvisorChainFactory() {
/* 193 */     return this.advisorChainFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterfaces(Class<?>... interfaces) {
/* 201 */     Assert.notNull(interfaces, "Interfaces must not be null");
/* 202 */     this.interfaces.clear();
/* 203 */     for (Class<?> ifc : interfaces) {
/* 204 */       addInterface(ifc);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addInterface(Class<?> intf) {
/* 213 */     Assert.notNull(intf, "Interface must not be null");
/* 214 */     if (!intf.isInterface()) {
/* 215 */       throw new IllegalArgumentException("[" + intf.getName() + "] is not an interface");
/*     */     }
/* 217 */     if (!this.interfaces.contains(intf)) {
/* 218 */       this.interfaces.add(intf);
/* 219 */       adviceChanged();
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
/*     */   public boolean removeInterface(Class<?> intf) {
/* 231 */     return this.interfaces.remove(intf);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?>[] getProxiedInterfaces() {
/* 236 */     return ClassUtils.toClassArray(this.interfaces);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInterfaceProxied(Class<?> intf) {
/* 241 */     for (Class<?> proxyIntf : this.interfaces) {
/* 242 */       if (intf.isAssignableFrom(proxyIntf)) {
/* 243 */         return true;
/*     */       }
/*     */     } 
/* 246 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Advisor[] getAdvisors() {
/* 252 */     return this.advisorArray;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdvisor(Advisor advisor) {
/* 257 */     int pos = this.advisors.size();
/* 258 */     addAdvisor(pos, advisor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdvisor(int pos, Advisor advisor) throws AopConfigException {
/* 263 */     if (advisor instanceof IntroductionAdvisor) {
/* 264 */       validateIntroductionAdvisor((IntroductionAdvisor)advisor);
/*     */     }
/* 266 */     addAdvisorInternal(pos, advisor);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAdvisor(Advisor advisor) {
/* 271 */     int index = indexOf(advisor);
/* 272 */     if (index == -1) {
/* 273 */       return false;
/*     */     }
/*     */     
/* 276 */     removeAdvisor(index);
/* 277 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAdvisor(int index) throws AopConfigException {
/* 283 */     if (isFrozen()) {
/* 284 */       throw new AopConfigException("Cannot remove Advisor: Configuration is frozen.");
/*     */     }
/* 286 */     if (index < 0 || index > this.advisors.size() - 1) {
/* 287 */       throw new AopConfigException("Advisor index " + index + " is out of bounds: This configuration only has " + this.advisors
/* 288 */           .size() + " advisors.");
/*     */     }
/*     */     
/* 291 */     Advisor advisor = this.advisors.get(index);
/* 292 */     if (advisor instanceof IntroductionAdvisor) {
/* 293 */       IntroductionAdvisor ia = (IntroductionAdvisor)advisor;
/*     */       
/* 295 */       for (int j = 0; j < (ia.getInterfaces()).length; j++) {
/* 296 */         removeInterface(ia.getInterfaces()[j]);
/*     */       }
/*     */     } 
/*     */     
/* 300 */     this.advisors.remove(index);
/* 301 */     updateAdvisorArray();
/* 302 */     adviceChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(Advisor advisor) {
/* 307 */     Assert.notNull(advisor, "Advisor must not be null");
/* 308 */     return this.advisors.indexOf(advisor);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean replaceAdvisor(Advisor a, Advisor b) throws AopConfigException {
/* 313 */     Assert.notNull(a, "Advisor a must not be null");
/* 314 */     Assert.notNull(b, "Advisor b must not be null");
/* 315 */     int index = indexOf(a);
/* 316 */     if (index == -1) {
/* 317 */       return false;
/*     */     }
/* 319 */     removeAdvisor(index);
/* 320 */     addAdvisor(index, b);
/* 321 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAdvisors(Advisor... advisors) {
/* 329 */     addAdvisors(Arrays.asList(advisors));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAdvisors(Collection<Advisor> advisors) {
/* 337 */     if (isFrozen()) {
/* 338 */       throw new AopConfigException("Cannot add advisor: Configuration is frozen.");
/*     */     }
/* 340 */     if (!CollectionUtils.isEmpty(advisors)) {
/* 341 */       for (Advisor advisor : advisors) {
/* 342 */         if (advisor instanceof IntroductionAdvisor) {
/* 343 */           validateIntroductionAdvisor((IntroductionAdvisor)advisor);
/*     */         }
/* 345 */         Assert.notNull(advisor, "Advisor must not be null");
/* 346 */         this.advisors.add(advisor);
/*     */       } 
/* 348 */       updateAdvisorArray();
/* 349 */       adviceChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void validateIntroductionAdvisor(IntroductionAdvisor advisor) {
/* 354 */     advisor.validateInterfaces();
/*     */     
/* 356 */     Class<?>[] ifcs = advisor.getInterfaces();
/* 357 */     for (Class<?> ifc : ifcs) {
/* 358 */       addInterface(ifc);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addAdvisorInternal(int pos, Advisor advisor) throws AopConfigException {
/* 363 */     Assert.notNull(advisor, "Advisor must not be null");
/* 364 */     if (isFrozen()) {
/* 365 */       throw new AopConfigException("Cannot add advisor: Configuration is frozen.");
/*     */     }
/* 367 */     if (pos > this.advisors.size()) {
/* 368 */       throw new IllegalArgumentException("Illegal position " + pos + " in advisor list with size " + this.advisors
/* 369 */           .size());
/*     */     }
/* 371 */     this.advisors.add(pos, advisor);
/* 372 */     updateAdvisorArray();
/* 373 */     adviceChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void updateAdvisorArray() {
/* 380 */     this.advisorArray = this.advisors.<Advisor>toArray(new Advisor[this.advisors.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final List<Advisor> getAdvisorsInternal() {
/* 389 */     return this.advisors;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAdvice(Advice advice) throws AopConfigException {
/* 395 */     int pos = this.advisors.size();
/* 396 */     addAdvice(pos, advice);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAdvice(int pos, Advice advice) throws AopConfigException {
/* 404 */     Assert.notNull(advice, "Advice must not be null");
/* 405 */     if (advice instanceof IntroductionInfo) {
/*     */ 
/*     */       
/* 408 */       addAdvisor(pos, (Advisor)new DefaultIntroductionAdvisor(advice, (IntroductionInfo)advice));
/*     */     } else {
/* 410 */       if (advice instanceof org.springframework.aop.DynamicIntroductionAdvice)
/*     */       {
/* 412 */         throw new AopConfigException("DynamicIntroductionAdvice may only be added as part of IntroductionAdvisor");
/*     */       }
/*     */       
/* 415 */       addAdvisor(pos, (Advisor)new DefaultPointcutAdvisor(advice));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAdvice(Advice advice) throws AopConfigException {
/* 421 */     int index = indexOf(advice);
/* 422 */     if (index == -1) {
/* 423 */       return false;
/*     */     }
/*     */     
/* 426 */     removeAdvisor(index);
/* 427 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(Advice advice) {
/* 433 */     Assert.notNull(advice, "Advice must not be null");
/* 434 */     for (int i = 0; i < this.advisors.size(); i++) {
/* 435 */       Advisor advisor = this.advisors.get(i);
/* 436 */       if (advisor.getAdvice() == advice) {
/* 437 */         return i;
/*     */       }
/*     */     } 
/* 440 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean adviceIncluded(Advice advice) {
/* 449 */     if (advice != null) {
/* 450 */       for (Advisor advisor : this.advisors) {
/* 451 */         if (advisor.getAdvice() == advice) {
/* 452 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 456 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int countAdvicesOfType(Class<?> adviceClass) {
/* 465 */     int count = 0;
/* 466 */     if (adviceClass != null) {
/* 467 */       for (Advisor advisor : this.advisors) {
/* 468 */         if (adviceClass.isInstance(advisor.getAdvice())) {
/* 469 */           count++;
/*     */         }
/*     */       } 
/*     */     }
/* 473 */     return count;
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
/*     */   public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) {
/* 485 */     MethodCacheKey cacheKey = new MethodCacheKey(method);
/* 486 */     List<Object> cached = this.methodCache.get(cacheKey);
/* 487 */     if (cached == null) {
/* 488 */       cached = this.advisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(this, method, targetClass);
/*     */       
/* 490 */       this.methodCache.put(cacheKey, cached);
/*     */     } 
/* 492 */     return cached;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void adviceChanged() {
/* 499 */     this.methodCache.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void copyConfigurationFrom(AdvisedSupport other) {
/* 508 */     copyConfigurationFrom(other, other.targetSource, new ArrayList<Advisor>(other.advisors));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void copyConfigurationFrom(AdvisedSupport other, TargetSource targetSource, List<Advisor> advisors) {
/* 519 */     copyFrom(other);
/* 520 */     this.targetSource = targetSource;
/* 521 */     this.advisorChainFactory = other.advisorChainFactory;
/* 522 */     this.interfaces = new ArrayList<Class<?>>(other.interfaces);
/* 523 */     for (Advisor advisor : advisors) {
/* 524 */       if (advisor instanceof IntroductionAdvisor) {
/* 525 */         validateIntroductionAdvisor((IntroductionAdvisor)advisor);
/*     */       }
/* 527 */       Assert.notNull(advisor, "Advisor must not be null");
/* 528 */       this.advisors.add(advisor);
/*     */     } 
/* 530 */     updateAdvisorArray();
/* 531 */     adviceChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AdvisedSupport getConfigurationOnlyCopy() {
/* 539 */     AdvisedSupport copy = new AdvisedSupport();
/* 540 */     copy.copyFrom(this);
/* 541 */     copy.targetSource = (TargetSource)EmptyTargetSource.forClass(getTargetClass(), getTargetSource().isStatic());
/* 542 */     copy.advisorChainFactory = this.advisorChainFactory;
/* 543 */     copy.interfaces = this.interfaces;
/* 544 */     copy.advisors = this.advisors;
/* 545 */     copy.updateAdvisorArray();
/* 546 */     return copy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 556 */     ois.defaultReadObject();
/*     */ 
/*     */     
/* 559 */     initMethodCache();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toProxyConfigString() {
/* 565 */     return toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 573 */     StringBuilder sb = new StringBuilder(getClass().getName());
/* 574 */     sb.append(": ").append(this.interfaces.size()).append(" interfaces ");
/* 575 */     sb.append(ClassUtils.classNamesToString(this.interfaces)).append("; ");
/* 576 */     sb.append(this.advisors.size()).append(" advisors ");
/* 577 */     sb.append(this.advisors).append("; ");
/* 578 */     sb.append("targetSource [").append(this.targetSource).append("]; ");
/* 579 */     sb.append(super.toString());
/* 580 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class MethodCacheKey
/*     */     implements Comparable<MethodCacheKey>
/*     */   {
/*     */     private final Method method;
/*     */ 
/*     */     
/*     */     private final int hashCode;
/*     */ 
/*     */     
/*     */     public MethodCacheKey(Method method) {
/* 595 */       this.method = method;
/* 596 */       this.hashCode = method.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 601 */       return (this == other || (other instanceof MethodCacheKey && this.method == ((MethodCacheKey)other).method));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 607 */       return this.hashCode;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 612 */       return this.method.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(MethodCacheKey other) {
/* 617 */       int result = this.method.getName().compareTo(other.method.getName());
/* 618 */       if (result == 0) {
/* 619 */         result = this.method.toString().compareTo(other.method.toString());
/*     */       }
/* 621 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\AdvisedSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */