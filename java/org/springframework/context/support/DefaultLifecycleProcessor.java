/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.context.Lifecycle;
/*     */ import org.springframework.context.LifecycleProcessor;
/*     */ import org.springframework.context.Phased;
/*     */ import org.springframework.context.SmartLifecycle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultLifecycleProcessor
/*     */   implements LifecycleProcessor, BeanFactoryAware
/*     */ {
/*  52 */   private final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  54 */   private volatile long timeoutPerShutdownPhase = 30000L;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean running;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile ConfigurableListableBeanFactory beanFactory;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeoutPerShutdownPhase(long timeoutPerShutdownPhase) {
/*  67 */     this.timeoutPerShutdownPhase = timeoutPerShutdownPhase;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/*  72 */     if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
/*  73 */       throw new IllegalArgumentException("DefaultLifecycleProcessor requires a ConfigurableListableBeanFactory: " + beanFactory);
/*     */     }
/*     */     
/*  76 */     this.beanFactory = (ConfigurableListableBeanFactory)beanFactory;
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
/*     */   public void start() {
/*  92 */     startBeans(false);
/*  93 */     this.running = true;
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
/*     */   public void stop() {
/* 106 */     stopBeans();
/* 107 */     this.running = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRefresh() {
/* 112 */     startBeans(true);
/* 113 */     this.running = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 118 */     stopBeans();
/* 119 */     this.running = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRunning() {
/* 124 */     return this.running;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void startBeans(boolean autoStartupOnly) {
/* 131 */     Map<String, Lifecycle> lifecycleBeans = getLifecycleBeans();
/* 132 */     Map<Integer, LifecycleGroup> phases = new HashMap<Integer, LifecycleGroup>();
/* 133 */     for (Map.Entry<String, ? extends Lifecycle> entry : lifecycleBeans.entrySet()) {
/* 134 */       Lifecycle bean = entry.getValue();
/* 135 */       if (!autoStartupOnly || (bean instanceof SmartLifecycle && ((SmartLifecycle)bean).isAutoStartup())) {
/* 136 */         int phase = getPhase(bean);
/* 137 */         LifecycleGroup group = phases.get(Integer.valueOf(phase));
/* 138 */         if (group == null) {
/* 139 */           group = new LifecycleGroup(phase, this.timeoutPerShutdownPhase, lifecycleBeans, autoStartupOnly);
/* 140 */           phases.put(Integer.valueOf(phase), group);
/*     */         } 
/* 142 */         group.add(entry.getKey(), bean);
/*     */       } 
/*     */     } 
/* 145 */     if (!phases.isEmpty()) {
/* 146 */       List<Integer> keys = new ArrayList<Integer>(phases.keySet());
/* 147 */       Collections.sort(keys);
/* 148 */       for (Integer key : keys) {
/* 149 */         ((LifecycleGroup)phases.get(key)).start();
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
/*     */   private void doStart(Map<String, ? extends Lifecycle> lifecycleBeans, String beanName, boolean autoStartupOnly) {
/* 161 */     Lifecycle bean = lifecycleBeans.remove(beanName);
/* 162 */     if (bean != null && bean != this) {
/* 163 */       String[] dependenciesForBean = this.beanFactory.getDependenciesForBean(beanName);
/* 164 */       for (String dependency : dependenciesForBean) {
/* 165 */         doStart(lifecycleBeans, dependency, autoStartupOnly);
/*     */       }
/* 167 */       if (!bean.isRunning() && (!autoStartupOnly || !(bean instanceof SmartLifecycle) || ((SmartLifecycle)bean)
/* 168 */         .isAutoStartup())) {
/* 169 */         if (this.logger.isDebugEnabled()) {
/* 170 */           this.logger.debug("Starting bean '" + beanName + "' of type [" + bean.getClass().getName() + "]");
/*     */         }
/*     */         try {
/* 173 */           bean.start();
/*     */         }
/* 175 */         catch (Throwable ex) {
/* 176 */           throw new ApplicationContextException("Failed to start bean '" + beanName + "'", ex);
/*     */         } 
/* 178 */         if (this.logger.isDebugEnabled()) {
/* 179 */           this.logger.debug("Successfully started bean '" + beanName + "'");
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void stopBeans() {
/* 186 */     Map<String, Lifecycle> lifecycleBeans = getLifecycleBeans();
/* 187 */     Map<Integer, LifecycleGroup> phases = new HashMap<Integer, LifecycleGroup>();
/* 188 */     for (Map.Entry<String, Lifecycle> entry : lifecycleBeans.entrySet()) {
/* 189 */       Lifecycle bean = entry.getValue();
/* 190 */       int shutdownPhase = getPhase(bean);
/* 191 */       LifecycleGroup group = phases.get(Integer.valueOf(shutdownPhase));
/* 192 */       if (group == null) {
/* 193 */         group = new LifecycleGroup(shutdownPhase, this.timeoutPerShutdownPhase, lifecycleBeans, false);
/* 194 */         phases.put(Integer.valueOf(shutdownPhase), group);
/*     */       } 
/* 196 */       group.add(entry.getKey(), bean);
/*     */     } 
/* 198 */     if (!phases.isEmpty()) {
/* 199 */       List<Integer> keys = new ArrayList<Integer>(phases.keySet());
/* 200 */       Collections.sort(keys, Collections.reverseOrder());
/* 201 */       for (Integer key : keys) {
/* 202 */         ((LifecycleGroup)phases.get(key)).stop();
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
/*     */   private void doStop(Map<String, ? extends Lifecycle> lifecycleBeans, final String beanName, final CountDownLatch latch, final Set<String> countDownBeanNames) {
/* 216 */     Lifecycle bean = lifecycleBeans.remove(beanName);
/* 217 */     if (bean != null) {
/* 218 */       String[] dependentBeans = this.beanFactory.getDependentBeans(beanName);
/* 219 */       for (String dependentBean : dependentBeans) {
/* 220 */         doStop(lifecycleBeans, dependentBean, latch, countDownBeanNames);
/*     */       }
/*     */       try {
/* 223 */         if (bean.isRunning()) {
/* 224 */           if (bean instanceof SmartLifecycle) {
/* 225 */             if (this.logger.isDebugEnabled()) {
/* 226 */               this.logger.debug("Asking bean '" + beanName + "' of type [" + bean
/* 227 */                   .getClass().getName() + "] to stop");
/*     */             }
/* 229 */             countDownBeanNames.add(beanName);
/* 230 */             ((SmartLifecycle)bean).stop(new Runnable()
/*     */                 {
/*     */                   public void run() {
/* 233 */                     latch.countDown();
/* 234 */                     countDownBeanNames.remove(beanName);
/* 235 */                     if (DefaultLifecycleProcessor.this.logger.isDebugEnabled()) {
/* 236 */                       DefaultLifecycleProcessor.this.logger.debug("Bean '" + beanName + "' completed its stop procedure");
/*     */                     }
/*     */                   }
/*     */                 });
/*     */           } else {
/*     */             
/* 242 */             if (this.logger.isDebugEnabled()) {
/* 243 */               this.logger.debug("Stopping bean '" + beanName + "' of type [" + bean
/* 244 */                   .getClass().getName() + "]");
/*     */             }
/* 246 */             bean.stop();
/* 247 */             if (this.logger.isDebugEnabled()) {
/* 248 */               this.logger.debug("Successfully stopped bean '" + beanName + "'");
/*     */             }
/*     */           }
/*     */         
/* 252 */         } else if (bean instanceof SmartLifecycle) {
/*     */           
/* 254 */           latch.countDown();
/*     */         }
/*     */       
/* 257 */       } catch (Throwable ex) {
/* 258 */         if (this.logger.isWarnEnabled()) {
/* 259 */           this.logger.warn("Failed to stop bean '" + beanName + "'", ex);
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
/*     */   
/*     */   protected Map<String, Lifecycle> getLifecycleBeans() {
/* 274 */     Map<String, Lifecycle> beans = new LinkedHashMap<String, Lifecycle>();
/* 275 */     String[] beanNames = this.beanFactory.getBeanNamesForType(Lifecycle.class, false, false);
/* 276 */     for (String beanName : beanNames) {
/* 277 */       String beanNameToRegister = BeanFactoryUtils.transformedBeanName(beanName);
/* 278 */       boolean isFactoryBean = this.beanFactory.isFactoryBean(beanNameToRegister);
/* 279 */       String beanNameToCheck = isFactoryBean ? ("&" + beanName) : beanName;
/* 280 */       if ((this.beanFactory.containsSingleton(beanNameToRegister) && (!isFactoryBean || Lifecycle.class
/* 281 */         .isAssignableFrom(this.beanFactory.getType(beanNameToCheck)))) || SmartLifecycle.class
/* 282 */         .isAssignableFrom(this.beanFactory.getType(beanNameToCheck))) {
/* 283 */         Lifecycle bean = (Lifecycle)this.beanFactory.getBean(beanNameToCheck, Lifecycle.class);
/* 284 */         if (bean != this) {
/* 285 */           beans.put(beanNameToRegister, bean);
/*     */         }
/*     */       } 
/*     */     } 
/* 289 */     return beans;
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
/*     */   protected int getPhase(Lifecycle bean) {
/* 302 */     return (bean instanceof Phased) ? ((Phased)bean).getPhase() : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class LifecycleGroup
/*     */   {
/*     */     private final int phase;
/*     */ 
/*     */     
/*     */     private final long timeout;
/*     */ 
/*     */     
/*     */     private final Map<String, ? extends Lifecycle> lifecycleBeans;
/*     */ 
/*     */     
/*     */     private final boolean autoStartupOnly;
/*     */     
/* 320 */     private final List<DefaultLifecycleProcessor.LifecycleGroupMember> members = new ArrayList<DefaultLifecycleProcessor.LifecycleGroupMember>();
/*     */ 
/*     */     
/*     */     private int smartMemberCount;
/*     */ 
/*     */     
/*     */     public LifecycleGroup(int phase, long timeout, Map<String, ? extends Lifecycle> lifecycleBeans, boolean autoStartupOnly) {
/* 327 */       this.phase = phase;
/* 328 */       this.timeout = timeout;
/* 329 */       this.lifecycleBeans = lifecycleBeans;
/* 330 */       this.autoStartupOnly = autoStartupOnly;
/*     */     }
/*     */     
/*     */     public void add(String name, Lifecycle bean) {
/* 334 */       this.members.add(new DefaultLifecycleProcessor.LifecycleGroupMember(name, bean));
/* 335 */       if (bean instanceof SmartLifecycle) {
/* 336 */         this.smartMemberCount++;
/*     */       }
/*     */     }
/*     */     
/*     */     public void start() {
/* 341 */       if (this.members.isEmpty()) {
/*     */         return;
/*     */       }
/* 344 */       if (DefaultLifecycleProcessor.this.logger.isInfoEnabled()) {
/* 345 */         DefaultLifecycleProcessor.this.logger.info("Starting beans in phase " + this.phase);
/*     */       }
/* 347 */       Collections.sort(this.members);
/* 348 */       for (DefaultLifecycleProcessor.LifecycleGroupMember member : this.members) {
/* 349 */         if (this.lifecycleBeans.containsKey(member.name)) {
/* 350 */           DefaultLifecycleProcessor.this.doStart(this.lifecycleBeans, member.name, this.autoStartupOnly);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     public void stop() {
/* 356 */       if (this.members.isEmpty()) {
/*     */         return;
/*     */       }
/* 359 */       if (DefaultLifecycleProcessor.this.logger.isInfoEnabled()) {
/* 360 */         DefaultLifecycleProcessor.this.logger.info("Stopping beans in phase " + this.phase);
/*     */       }
/* 362 */       Collections.sort(this.members, Collections.reverseOrder());
/* 363 */       CountDownLatch latch = new CountDownLatch(this.smartMemberCount);
/* 364 */       Set<String> countDownBeanNames = Collections.synchronizedSet(new LinkedHashSet<String>());
/* 365 */       for (DefaultLifecycleProcessor.LifecycleGroupMember member : this.members) {
/* 366 */         if (this.lifecycleBeans.containsKey(member.name)) {
/* 367 */           DefaultLifecycleProcessor.this.doStop(this.lifecycleBeans, member.name, latch, countDownBeanNames); continue;
/*     */         } 
/* 369 */         if (member.bean instanceof SmartLifecycle)
/*     */         {
/* 371 */           latch.countDown();
/*     */         }
/*     */       } 
/*     */       try {
/* 375 */         latch.await(this.timeout, TimeUnit.MILLISECONDS);
/* 376 */         if (latch.getCount() > 0L && !countDownBeanNames.isEmpty() && DefaultLifecycleProcessor.this.logger.isWarnEnabled()) {
/* 377 */           DefaultLifecycleProcessor.this.logger.warn("Failed to shut down " + countDownBeanNames.size() + " bean" + (
/* 378 */               (countDownBeanNames.size() > 1) ? "s" : "") + " with phase value " + this.phase + " within timeout of " + this.timeout + ": " + countDownBeanNames);
/*     */         
/*     */         }
/*     */       }
/* 382 */       catch (InterruptedException ex) {
/* 383 */         Thread.currentThread().interrupt();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class LifecycleGroupMember
/*     */     implements Comparable<LifecycleGroupMember>
/*     */   {
/*     */     private final String name;
/*     */     
/*     */     private final Lifecycle bean;
/*     */ 
/*     */     
/*     */     LifecycleGroupMember(String name, Lifecycle bean) {
/* 399 */       this.name = name;
/* 400 */       this.bean = bean;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(LifecycleGroupMember other) {
/* 405 */       int thisPhase = DefaultLifecycleProcessor.this.getPhase(this.bean);
/* 406 */       int otherPhase = DefaultLifecycleProcessor.this.getPhase(other.bean);
/* 407 */       return (thisPhase == otherPhase) ? 0 : ((thisPhase < otherPhase) ? -1 : 1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\DefaultLifecycleProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */