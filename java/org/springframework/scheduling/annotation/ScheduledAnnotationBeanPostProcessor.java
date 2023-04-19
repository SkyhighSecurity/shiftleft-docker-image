/*     */ package org.springframework.scheduling.annotation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.framework.AopProxyUtils;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*     */ import org.springframework.beans.factory.SmartInitializingSingleton;
/*     */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
/*     */ import org.springframework.beans.factory.config.NamedBeanHolder;
/*     */ import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.context.EmbeddedValueResolverAware;
/*     */ import org.springframework.context.event.ContextRefreshedEvent;
/*     */ import org.springframework.core.MethodIntrospector;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.scheduling.TaskScheduler;
/*     */ import org.springframework.scheduling.config.CronTask;
/*     */ import org.springframework.scheduling.config.IntervalTask;
/*     */ import org.springframework.scheduling.config.ScheduledTask;
/*     */ import org.springframework.scheduling.config.ScheduledTaskRegistrar;
/*     */ import org.springframework.scheduling.support.CronTrigger;
/*     */ import org.springframework.scheduling.support.ScheduledMethodRunnable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class ScheduledAnnotationBeanPostProcessor
/*     */   implements MergedBeanDefinitionPostProcessor, DestructionAwareBeanPostProcessor, Ordered, EmbeddedValueResolverAware, BeanNameAware, BeanFactoryAware, ApplicationContextAware, SmartInitializingSingleton, ApplicationListener<ContextRefreshedEvent>, DisposableBean
/*     */ {
/*     */   public static final String DEFAULT_TASK_SCHEDULER_BEAN_NAME = "taskScheduler";
/* 113 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private Object scheduler;
/*     */   
/*     */   private StringValueResolver embeddedValueResolver;
/*     */   
/*     */   private String beanName;
/*     */   
/*     */   private BeanFactory beanFactory;
/*     */   
/*     */   private ApplicationContext applicationContext;
/*     */   
/* 125 */   private final ScheduledTaskRegistrar registrar = new ScheduledTaskRegistrar();
/*     */ 
/*     */   
/* 128 */   private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<Class<?>, Boolean>(64));
/*     */   
/* 130 */   private final Map<Object, Set<ScheduledTask>> scheduledTasks = new IdentityHashMap<Object, Set<ScheduledTask>>(16);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 136 */     return Integer.MAX_VALUE;
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
/*     */   public void setScheduler(Object scheduler) {
/* 151 */     this.scheduler = scheduler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEmbeddedValueResolver(StringValueResolver resolver) {
/* 156 */     this.embeddedValueResolver = resolver;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanName(String beanName) {
/* 161 */     this.beanName = beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 171 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext) {
/* 181 */     this.applicationContext = applicationContext;
/* 182 */     if (this.beanFactory == null) {
/* 183 */       this.beanFactory = (BeanFactory)applicationContext;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterSingletonsInstantiated() {
/* 191 */     this.nonAnnotatedClasses.clear();
/*     */     
/* 193 */     if (this.applicationContext == null)
/*     */     {
/* 195 */       finishRegistration();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onApplicationEvent(ContextRefreshedEvent event) {
/* 201 */     if (event.getApplicationContext() == this.applicationContext)
/*     */     {
/*     */ 
/*     */       
/* 205 */       finishRegistration();
/*     */     }
/*     */   }
/*     */   
/*     */   private void finishRegistration() {
/* 210 */     if (this.scheduler != null) {
/* 211 */       this.registrar.setScheduler(this.scheduler);
/*     */     }
/*     */     
/* 214 */     if (this.beanFactory instanceof ListableBeanFactory) {
/*     */       
/* 216 */       Map<String, SchedulingConfigurer> beans = ((ListableBeanFactory)this.beanFactory).getBeansOfType(SchedulingConfigurer.class);
/* 217 */       List<SchedulingConfigurer> configurers = new ArrayList<SchedulingConfigurer>(beans.values());
/* 218 */       AnnotationAwareOrderComparator.sort(configurers);
/* 219 */       for (SchedulingConfigurer configurer : configurers) {
/* 220 */         configurer.configureTasks(this.registrar);
/*     */       }
/*     */     } 
/*     */     
/* 224 */     if (this.registrar.hasTasks() && this.registrar.getScheduler() == null) {
/* 225 */       Assert.state((this.beanFactory != null), "BeanFactory must be set to find scheduler by type");
/*     */       
/*     */       try {
/* 228 */         this.registrar.setTaskScheduler(resolveSchedulerBean(TaskScheduler.class, false));
/*     */       }
/* 230 */       catch (NoUniqueBeanDefinitionException ex) {
/* 231 */         this.logger.debug("Could not find unique TaskScheduler bean", (Throwable)ex);
/*     */         try {
/* 233 */           this.registrar.setTaskScheduler(resolveSchedulerBean(TaskScheduler.class, true));
/*     */         }
/* 235 */         catch (NoSuchBeanDefinitionException ex2) {
/* 236 */           if (this.logger.isInfoEnabled()) {
/* 237 */             this.logger.info("More than one TaskScheduler bean exists within the context, and none is named 'taskScheduler'. Mark one of them as primary or name it 'taskScheduler' (possibly as an alias); or implement the SchedulingConfigurer interface and call ScheduledTaskRegistrar#setScheduler explicitly within the configureTasks() callback: " + ex
/*     */ 
/*     */ 
/*     */                 
/* 241 */                 .getBeanNamesFound());
/*     */           }
/*     */         }
/*     */       
/* 245 */       } catch (NoSuchBeanDefinitionException ex) {
/* 246 */         this.logger.debug("Could not find default TaskScheduler bean", (Throwable)ex);
/*     */         
/*     */         try {
/* 249 */           this.registrar.setScheduler(resolveSchedulerBean(ScheduledExecutorService.class, false));
/*     */         }
/* 251 */         catch (NoUniqueBeanDefinitionException ex2) {
/* 252 */           this.logger.debug("Could not find unique ScheduledExecutorService bean", (Throwable)ex2);
/*     */           try {
/* 254 */             this.registrar.setScheduler(resolveSchedulerBean(ScheduledExecutorService.class, true));
/*     */           }
/* 256 */           catch (NoSuchBeanDefinitionException ex3) {
/* 257 */             if (this.logger.isInfoEnabled()) {
/* 258 */               this.logger.info("More than one ScheduledExecutorService bean exists within the context, and none is named 'taskScheduler'. Mark one of them as primary or name it 'taskScheduler' (possibly as an alias); or implement the SchedulingConfigurer interface and call ScheduledTaskRegistrar#setScheduler explicitly within the configureTasks() callback: " + ex2
/*     */ 
/*     */ 
/*     */                   
/* 262 */                   .getBeanNamesFound());
/*     */             }
/*     */           }
/*     */         
/* 266 */         } catch (NoSuchBeanDefinitionException ex2) {
/* 267 */           this.logger.debug("Could not find default ScheduledExecutorService bean", (Throwable)ex2);
/*     */           
/* 269 */           this.logger.info("No TaskScheduler/ScheduledExecutorService bean found for scheduled processing");
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 274 */     this.registrar.afterPropertiesSet();
/*     */   }
/*     */   
/*     */   private <T> T resolveSchedulerBean(Class<T> schedulerType, boolean byName) {
/* 278 */     if (byName) {
/* 279 */       T scheduler = (T)this.beanFactory.getBean("taskScheduler", schedulerType);
/* 280 */       if (this.beanFactory instanceof ConfigurableBeanFactory) {
/* 281 */         ((ConfigurableBeanFactory)this.beanFactory).registerDependentBean("taskScheduler", this.beanName);
/*     */       }
/*     */       
/* 284 */       return scheduler;
/*     */     } 
/* 286 */     if (this.beanFactory instanceof AutowireCapableBeanFactory) {
/* 287 */       NamedBeanHolder<T> holder = ((AutowireCapableBeanFactory)this.beanFactory).resolveNamedBean(schedulerType);
/* 288 */       if (this.beanFactory instanceof ConfigurableBeanFactory) {
/* 289 */         ((ConfigurableBeanFactory)this.beanFactory).registerDependentBean(holder
/* 290 */             .getBeanName(), this.beanName);
/*     */       }
/* 292 */       return (T)holder.getBeanInstance();
/*     */     } 
/*     */     
/* 295 */     return (T)this.beanFactory.getBean(schedulerType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName) {
/* 306 */     return bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName) {
/* 311 */     if (bean instanceof org.springframework.aop.framework.AopInfrastructureBean)
/*     */     {
/* 313 */       return bean;
/*     */     }
/*     */     
/* 316 */     Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
/* 317 */     if (!this.nonAnnotatedClasses.contains(targetClass)) {
/* 318 */       Map<Method, Set<Scheduled>> annotatedMethods = MethodIntrospector.selectMethods(targetClass, new MethodIntrospector.MetadataLookup<Set<Scheduled>>()
/*     */           {
/*     */             public Set<Scheduled> inspect(Method method)
/*     */             {
/* 322 */               Set<Scheduled> scheduledMethods = AnnotatedElementUtils.getMergedRepeatableAnnotations(method, Scheduled.class, Schedules.class);
/*     */               
/* 324 */               return !scheduledMethods.isEmpty() ? scheduledMethods : null;
/*     */             }
/*     */           });
/* 327 */       if (annotatedMethods.isEmpty()) {
/* 328 */         this.nonAnnotatedClasses.add(targetClass);
/* 329 */         if (this.logger.isTraceEnabled()) {
/* 330 */           this.logger.trace("No @Scheduled annotations found on bean class: " + targetClass);
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 335 */         for (Map.Entry<Method, Set<Scheduled>> entry : annotatedMethods.entrySet()) {
/* 336 */           Method method = entry.getKey();
/* 337 */           for (Scheduled scheduled : entry.getValue()) {
/* 338 */             processScheduled(scheduled, method, bean);
/*     */           }
/*     */         } 
/* 341 */         if (this.logger.isDebugEnabled()) {
/* 342 */           this.logger.debug(annotatedMethods.size() + " @Scheduled methods processed on bean '" + beanName + "': " + annotatedMethods);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 347 */     return bean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processScheduled(Scheduled scheduled, Method method, Object bean) {
/*     */     try {
/* 358 */       Assert.isTrue(((method.getParameterTypes()).length == 0), "Only no-arg methods may be annotated with @Scheduled");
/*     */ 
/*     */       
/* 361 */       Method invocableMethod = AopUtils.selectInvocableMethod(method, bean.getClass());
/* 362 */       ScheduledMethodRunnable scheduledMethodRunnable = new ScheduledMethodRunnable(bean, invocableMethod);
/* 363 */       boolean processedSchedule = false;
/* 364 */       String errorMessage = "Exactly one of the 'cron', 'fixedDelay(String)', or 'fixedRate(String)' attributes is required";
/*     */ 
/*     */       
/* 367 */       Set<ScheduledTask> tasks = new LinkedHashSet<ScheduledTask>(4);
/*     */ 
/*     */       
/* 370 */       long initialDelay = scheduled.initialDelay();
/* 371 */       String initialDelayString = scheduled.initialDelayString();
/* 372 */       if (StringUtils.hasText(initialDelayString)) {
/* 373 */         Assert.isTrue((initialDelay < 0L), "Specify 'initialDelay' or 'initialDelayString', not both");
/* 374 */         if (this.embeddedValueResolver != null) {
/* 375 */           initialDelayString = this.embeddedValueResolver.resolveStringValue(initialDelayString);
/*     */         }
/*     */         try {
/* 378 */           initialDelay = Long.parseLong(initialDelayString);
/*     */         }
/* 380 */         catch (NumberFormatException ex) {
/* 381 */           throw new IllegalArgumentException("Invalid initialDelayString value \"" + initialDelayString + "\" - cannot parse into integer");
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 387 */       String cron = scheduled.cron();
/* 388 */       if (StringUtils.hasText(cron)) {
/* 389 */         TimeZone timeZone; Assert.isTrue((initialDelay == -1L), "'initialDelay' not supported for cron triggers");
/* 390 */         processedSchedule = true;
/* 391 */         String zone = scheduled.zone();
/* 392 */         if (this.embeddedValueResolver != null) {
/* 393 */           cron = this.embeddedValueResolver.resolveStringValue(cron);
/* 394 */           zone = this.embeddedValueResolver.resolveStringValue(zone);
/*     */         } 
/*     */         
/* 397 */         if (StringUtils.hasText(zone)) {
/* 398 */           timeZone = StringUtils.parseTimeZoneString(zone);
/*     */         } else {
/*     */           
/* 401 */           timeZone = TimeZone.getDefault();
/*     */         } 
/* 403 */         tasks.add(this.registrar.scheduleCronTask(new CronTask((Runnable)scheduledMethodRunnable, new CronTrigger(cron, timeZone))));
/*     */       } 
/*     */ 
/*     */       
/* 407 */       if (initialDelay < 0L) {
/* 408 */         initialDelay = 0L;
/*     */       }
/*     */ 
/*     */       
/* 412 */       long fixedDelay = scheduled.fixedDelay();
/* 413 */       if (fixedDelay >= 0L) {
/* 414 */         Assert.isTrue(!processedSchedule, errorMessage);
/* 415 */         processedSchedule = true;
/* 416 */         tasks.add(this.registrar.scheduleFixedDelayTask(new IntervalTask((Runnable)scheduledMethodRunnable, fixedDelay, initialDelay)));
/*     */       } 
/* 418 */       String fixedDelayString = scheduled.fixedDelayString();
/* 419 */       if (StringUtils.hasText(fixedDelayString)) {
/* 420 */         Assert.isTrue(!processedSchedule, errorMessage);
/* 421 */         processedSchedule = true;
/* 422 */         if (this.embeddedValueResolver != null) {
/* 423 */           fixedDelayString = this.embeddedValueResolver.resolveStringValue(fixedDelayString);
/*     */         }
/*     */         try {
/* 426 */           fixedDelay = Long.parseLong(fixedDelayString);
/*     */         }
/* 428 */         catch (NumberFormatException ex) {
/* 429 */           throw new IllegalArgumentException("Invalid fixedDelayString value \"" + fixedDelayString + "\" - cannot parse into integer");
/*     */         } 
/*     */         
/* 432 */         tasks.add(this.registrar.scheduleFixedDelayTask(new IntervalTask((Runnable)scheduledMethodRunnable, fixedDelay, initialDelay)));
/*     */       } 
/*     */ 
/*     */       
/* 436 */       long fixedRate = scheduled.fixedRate();
/* 437 */       if (fixedRate >= 0L) {
/* 438 */         Assert.isTrue(!processedSchedule, errorMessage);
/* 439 */         processedSchedule = true;
/* 440 */         tasks.add(this.registrar.scheduleFixedRateTask(new IntervalTask((Runnable)scheduledMethodRunnable, fixedRate, initialDelay)));
/*     */       } 
/* 442 */       String fixedRateString = scheduled.fixedRateString();
/* 443 */       if (StringUtils.hasText(fixedRateString)) {
/* 444 */         Assert.isTrue(!processedSchedule, errorMessage);
/* 445 */         processedSchedule = true;
/* 446 */         if (this.embeddedValueResolver != null) {
/* 447 */           fixedRateString = this.embeddedValueResolver.resolveStringValue(fixedRateString);
/*     */         }
/*     */         try {
/* 450 */           fixedRate = Long.parseLong(fixedRateString);
/*     */         }
/* 452 */         catch (NumberFormatException ex) {
/* 453 */           throw new IllegalArgumentException("Invalid fixedRateString value \"" + fixedRateString + "\" - cannot parse into integer");
/*     */         } 
/*     */         
/* 456 */         tasks.add(this.registrar.scheduleFixedRateTask(new IntervalTask((Runnable)scheduledMethodRunnable, fixedRate, initialDelay)));
/*     */       } 
/*     */ 
/*     */       
/* 460 */       Assert.isTrue(processedSchedule, errorMessage);
/*     */ 
/*     */       
/* 463 */       synchronized (this.scheduledTasks) {
/* 464 */         Set<ScheduledTask> registeredTasks = this.scheduledTasks.get(bean);
/* 465 */         if (registeredTasks == null) {
/* 466 */           registeredTasks = new LinkedHashSet<ScheduledTask>(4);
/* 467 */           this.scheduledTasks.put(bean, registeredTasks);
/*     */         } 
/* 469 */         registeredTasks.addAll(tasks);
/*     */       }
/*     */     
/* 472 */     } catch (IllegalArgumentException ex) {
/* 473 */       throw new IllegalStateException("Encountered invalid @Scheduled method '" + method
/* 474 */           .getName() + "': " + ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessBeforeDestruction(Object bean, String beanName) {
/*     */     Set<ScheduledTask> tasks;
/* 482 */     synchronized (this.scheduledTasks) {
/* 483 */       tasks = this.scheduledTasks.remove(bean);
/*     */     } 
/* 485 */     if (tasks != null) {
/* 486 */       for (ScheduledTask task : tasks) {
/* 487 */         task.cancel();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresDestruction(Object bean) {
/* 494 */     synchronized (this.scheduledTasks) {
/* 495 */       return this.scheduledTasks.containsKey(bean);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 501 */     synchronized (this.scheduledTasks) {
/* 502 */       Collection<Set<ScheduledTask>> allTasks = this.scheduledTasks.values();
/* 503 */       for (Set<ScheduledTask> tasks : allTasks) {
/* 504 */         for (ScheduledTask task : tasks) {
/* 505 */           task.cancel();
/*     */         }
/*     */       } 
/* 508 */       this.scheduledTasks.clear();
/*     */     } 
/* 510 */     this.registrar.destroy();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\annotation\ScheduledAnnotationBeanPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */