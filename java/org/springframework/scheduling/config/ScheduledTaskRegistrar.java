/*     */ package org.springframework.scheduling.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.scheduling.TaskScheduler;
/*     */ import org.springframework.scheduling.Trigger;
/*     */ import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class ScheduledTaskRegistrar
/*     */   implements InitializingBean, DisposableBean
/*     */ {
/*     */   private TaskScheduler taskScheduler;
/*     */   private ScheduledExecutorService localExecutor;
/*     */   private List<TriggerTask> triggerTasks;
/*     */   private List<CronTask> cronTasks;
/*     */   private List<IntervalTask> fixedRateTasks;
/*     */   private List<IntervalTask> fixedDelayTasks;
/*  70 */   private final Map<Task, ScheduledTask> unresolvedTasks = new HashMap<Task, ScheduledTask>(16);
/*     */   
/*  72 */   private final Set<ScheduledTask> scheduledTasks = new LinkedHashSet<ScheduledTask>(16);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTaskScheduler(TaskScheduler taskScheduler) {
/*  79 */     Assert.notNull(taskScheduler, "TaskScheduler must not be null");
/*  80 */     this.taskScheduler = taskScheduler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScheduler(Object scheduler) {
/*  89 */     Assert.notNull(scheduler, "Scheduler object must not be null");
/*  90 */     if (scheduler instanceof TaskScheduler) {
/*  91 */       this.taskScheduler = (TaskScheduler)scheduler;
/*     */     }
/*  93 */     else if (scheduler instanceof ScheduledExecutorService) {
/*  94 */       this.taskScheduler = (TaskScheduler)new ConcurrentTaskScheduler((ScheduledExecutorService)scheduler);
/*     */     } else {
/*     */       
/*  97 */       throw new IllegalArgumentException("Unsupported scheduler type: " + scheduler.getClass());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TaskScheduler getScheduler() {
/* 105 */     return this.taskScheduler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTriggerTasks(Map<Runnable, Trigger> triggerTasks) {
/* 114 */     this.triggerTasks = new ArrayList<TriggerTask>();
/* 115 */     for (Map.Entry<Runnable, Trigger> task : triggerTasks.entrySet()) {
/* 116 */       addTriggerTask(new TriggerTask(task.getKey(), task.getValue()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTriggerTasksList(List<TriggerTask> triggerTasks) {
/* 127 */     this.triggerTasks = triggerTasks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<TriggerTask> getTriggerTaskList() {
/* 136 */     return (this.triggerTasks != null) ? Collections.<TriggerTask>unmodifiableList(this.triggerTasks) : 
/* 137 */       Collections.<TriggerTask>emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCronTasks(Map<Runnable, String> cronTasks) {
/* 145 */     this.cronTasks = new ArrayList<CronTask>();
/* 146 */     for (Map.Entry<Runnable, String> task : cronTasks.entrySet()) {
/* 147 */       addCronTask(task.getKey(), task.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCronTasksList(List<CronTask> cronTasks) {
/* 158 */     this.cronTasks = cronTasks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<CronTask> getCronTaskList() {
/* 167 */     return (this.cronTasks != null) ? Collections.<CronTask>unmodifiableList(this.cronTasks) : 
/* 168 */       Collections.<CronTask>emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFixedRateTasks(Map<Runnable, Long> fixedRateTasks) {
/* 176 */     this.fixedRateTasks = new ArrayList<IntervalTask>();
/* 177 */     for (Map.Entry<Runnable, Long> task : fixedRateTasks.entrySet()) {
/* 178 */       addFixedRateTask(task.getKey(), ((Long)task.getValue()).longValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFixedRateTasksList(List<IntervalTask> fixedRateTasks) {
/* 189 */     this.fixedRateTasks = fixedRateTasks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<IntervalTask> getFixedRateTaskList() {
/* 198 */     return (this.fixedRateTasks != null) ? Collections.<IntervalTask>unmodifiableList(this.fixedRateTasks) : 
/* 199 */       Collections.<IntervalTask>emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFixedDelayTasks(Map<Runnable, Long> fixedDelayTasks) {
/* 207 */     this.fixedDelayTasks = new ArrayList<IntervalTask>();
/* 208 */     for (Map.Entry<Runnable, Long> task : fixedDelayTasks.entrySet()) {
/* 209 */       addFixedDelayTask(task.getKey(), ((Long)task.getValue()).longValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFixedDelayTasksList(List<IntervalTask> fixedDelayTasks) {
/* 220 */     this.fixedDelayTasks = fixedDelayTasks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<IntervalTask> getFixedDelayTaskList() {
/* 229 */     return (this.fixedDelayTasks != null) ? Collections.<IntervalTask>unmodifiableList(this.fixedDelayTasks) : 
/* 230 */       Collections.<IntervalTask>emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTriggerTask(Runnable task, Trigger trigger) {
/* 239 */     addTriggerTask(new TriggerTask(task, trigger));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTriggerTask(TriggerTask task) {
/* 248 */     if (this.triggerTasks == null) {
/* 249 */       this.triggerTasks = new ArrayList<TriggerTask>();
/*     */     }
/* 251 */     this.triggerTasks.add(task);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCronTask(Runnable task, String expression) {
/* 258 */     addCronTask(new CronTask(task, expression));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCronTask(CronTask task) {
/* 266 */     if (this.cronTasks == null) {
/* 267 */       this.cronTasks = new ArrayList<CronTask>();
/*     */     }
/* 269 */     this.cronTasks.add(task);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFixedRateTask(Runnable task, long interval) {
/* 277 */     addFixedRateTask(new IntervalTask(task, interval, 0L));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFixedRateTask(IntervalTask task) {
/* 286 */     if (this.fixedRateTasks == null) {
/* 287 */       this.fixedRateTasks = new ArrayList<IntervalTask>();
/*     */     }
/* 289 */     this.fixedRateTasks.add(task);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFixedDelayTask(Runnable task, long delay) {
/* 297 */     addFixedDelayTask(new IntervalTask(task, delay, 0L));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFixedDelayTask(IntervalTask task) {
/* 306 */     if (this.fixedDelayTasks == null) {
/* 307 */       this.fixedDelayTasks = new ArrayList<IntervalTask>();
/*     */     }
/* 309 */     this.fixedDelayTasks.add(task);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasTasks() {
/* 318 */     return (!CollectionUtils.isEmpty(this.triggerTasks) || 
/* 319 */       !CollectionUtils.isEmpty(this.cronTasks) || 
/* 320 */       !CollectionUtils.isEmpty(this.fixedRateTasks) || 
/* 321 */       !CollectionUtils.isEmpty(this.fixedDelayTasks));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 330 */     scheduleTasks();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void scheduleTasks() {
/* 338 */     if (this.taskScheduler == null) {
/* 339 */       this.localExecutor = Executors.newSingleThreadScheduledExecutor();
/* 340 */       this.taskScheduler = (TaskScheduler)new ConcurrentTaskScheduler(this.localExecutor);
/*     */     } 
/* 342 */     if (this.triggerTasks != null) {
/* 343 */       for (TriggerTask task : this.triggerTasks) {
/* 344 */         addScheduledTask(scheduleTriggerTask(task));
/*     */       }
/*     */     }
/* 347 */     if (this.cronTasks != null) {
/* 348 */       for (CronTask task : this.cronTasks) {
/* 349 */         addScheduledTask(scheduleCronTask(task));
/*     */       }
/*     */     }
/* 352 */     if (this.fixedRateTasks != null) {
/* 353 */       for (IntervalTask task : this.fixedRateTasks) {
/* 354 */         addScheduledTask(scheduleFixedRateTask(task));
/*     */       }
/*     */     }
/* 357 */     if (this.fixedDelayTasks != null) {
/* 358 */       for (IntervalTask task : this.fixedDelayTasks) {
/* 359 */         addScheduledTask(scheduleFixedDelayTask(task));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void addScheduledTask(ScheduledTask task) {
/* 365 */     if (task != null) {
/* 366 */       this.scheduledTasks.add(task);
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
/*     */   public ScheduledTask scheduleTriggerTask(TriggerTask task) {
/* 378 */     ScheduledTask scheduledTask = this.unresolvedTasks.remove(task);
/* 379 */     boolean newTask = false;
/* 380 */     if (scheduledTask == null) {
/* 381 */       scheduledTask = new ScheduledTask();
/* 382 */       newTask = true;
/*     */     } 
/* 384 */     if (this.taskScheduler != null) {
/* 385 */       scheduledTask.future = this.taskScheduler.schedule(task.getRunnable(), task.getTrigger());
/*     */     } else {
/*     */       
/* 388 */       addTriggerTask(task);
/* 389 */       this.unresolvedTasks.put(task, scheduledTask);
/*     */     } 
/* 391 */     return newTask ? scheduledTask : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScheduledTask scheduleCronTask(CronTask task) {
/* 402 */     ScheduledTask scheduledTask = this.unresolvedTasks.remove(task);
/* 403 */     boolean newTask = false;
/* 404 */     if (scheduledTask == null) {
/* 405 */       scheduledTask = new ScheduledTask();
/* 406 */       newTask = true;
/*     */     } 
/* 408 */     if (this.taskScheduler != null) {
/* 409 */       scheduledTask.future = this.taskScheduler.schedule(task.getRunnable(), task.getTrigger());
/*     */     } else {
/*     */       
/* 412 */       addCronTask(task);
/* 413 */       this.unresolvedTasks.put(task, scheduledTask);
/*     */     } 
/* 415 */     return newTask ? scheduledTask : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScheduledTask scheduleFixedRateTask(IntervalTask task) {
/* 426 */     ScheduledTask scheduledTask = this.unresolvedTasks.remove(task);
/* 427 */     boolean newTask = false;
/* 428 */     if (scheduledTask == null) {
/* 429 */       scheduledTask = new ScheduledTask();
/* 430 */       newTask = true;
/*     */     } 
/* 432 */     if (this.taskScheduler != null) {
/* 433 */       if (task.getInitialDelay() > 0L) {
/* 434 */         Date startTime = new Date(System.currentTimeMillis() + task.getInitialDelay());
/* 435 */         scheduledTask
/* 436 */           .future = this.taskScheduler.scheduleAtFixedRate(task.getRunnable(), startTime, task.getInterval());
/*     */       } else {
/*     */         
/* 439 */         scheduledTask
/* 440 */           .future = this.taskScheduler.scheduleAtFixedRate(task.getRunnable(), task.getInterval());
/*     */       } 
/*     */     } else {
/*     */       
/* 444 */       addFixedRateTask(task);
/* 445 */       this.unresolvedTasks.put(task, scheduledTask);
/*     */     } 
/* 447 */     return newTask ? scheduledTask : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScheduledTask scheduleFixedDelayTask(IntervalTask task) {
/* 458 */     ScheduledTask scheduledTask = this.unresolvedTasks.remove(task);
/* 459 */     boolean newTask = false;
/* 460 */     if (scheduledTask == null) {
/* 461 */       scheduledTask = new ScheduledTask();
/* 462 */       newTask = true;
/*     */     } 
/* 464 */     if (this.taskScheduler != null) {
/* 465 */       if (task.getInitialDelay() > 0L) {
/* 466 */         Date startTime = new Date(System.currentTimeMillis() + task.getInitialDelay());
/* 467 */         scheduledTask
/* 468 */           .future = this.taskScheduler.scheduleWithFixedDelay(task.getRunnable(), startTime, task.getInterval());
/*     */       } else {
/*     */         
/* 471 */         scheduledTask
/* 472 */           .future = this.taskScheduler.scheduleWithFixedDelay(task.getRunnable(), task.getInterval());
/*     */       } 
/*     */     } else {
/*     */       
/* 476 */       addFixedDelayTask(task);
/* 477 */       this.unresolvedTasks.put(task, scheduledTask);
/*     */     } 
/* 479 */     return newTask ? scheduledTask : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 485 */     for (ScheduledTask task : this.scheduledTasks) {
/* 486 */       task.cancel();
/*     */     }
/* 488 */     if (this.localExecutor != null)
/* 489 */       this.localExecutor.shutdownNow(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\config\ScheduledTaskRegistrar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */