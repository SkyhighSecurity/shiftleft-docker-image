/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.springframework.core.task.AsyncListenableTaskExecutor;
/*     */ import org.springframework.core.task.TaskRejectedException;
/*     */ import org.springframework.lang.UsesJava7;
/*     */ import org.springframework.scheduling.SchedulingTaskExecutor;
/*     */ import org.springframework.scheduling.TaskScheduler;
/*     */ import org.springframework.scheduling.Trigger;
/*     */ import org.springframework.scheduling.support.TaskUtils;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ErrorHandler;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
/*     */ import org.springframework.util.concurrent.ListenableFutureTask;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ThreadPoolTaskScheduler
/*     */   extends ExecutorConfigurationSupport
/*     */   implements AsyncListenableTaskExecutor, SchedulingTaskExecutor, TaskScheduler
/*     */ {
/*  63 */   private static final boolean setRemoveOnCancelPolicyAvailable = ClassUtils.hasMethod(ScheduledThreadPoolExecutor.class, "setRemoveOnCancelPolicy", new Class[] { boolean.class });
/*     */ 
/*     */   
/*  66 */   private volatile int poolSize = 1;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean removeOnCancelPolicy = false;
/*     */ 
/*     */   
/*     */   private volatile ErrorHandler errorHandler;
/*     */ 
/*     */   
/*     */   private volatile ScheduledExecutorService scheduledExecutor;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPoolSize(int poolSize) {
/*  81 */     Assert.isTrue((poolSize > 0), "'poolSize' must be 1 or higher");
/*  82 */     this.poolSize = poolSize;
/*  83 */     if (this.scheduledExecutor instanceof ScheduledThreadPoolExecutor) {
/*  84 */       ((ScheduledThreadPoolExecutor)this.scheduledExecutor).setCorePoolSize(poolSize);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UsesJava7
/*     */   public void setRemoveOnCancelPolicy(boolean removeOnCancelPolicy) {
/*  96 */     this.removeOnCancelPolicy = removeOnCancelPolicy;
/*  97 */     if (setRemoveOnCancelPolicyAvailable && this.scheduledExecutor instanceof ScheduledThreadPoolExecutor) {
/*  98 */       ((ScheduledThreadPoolExecutor)this.scheduledExecutor).setRemoveOnCancelPolicy(removeOnCancelPolicy);
/*     */     }
/* 100 */     else if (removeOnCancelPolicy && this.scheduledExecutor != null) {
/* 101 */       this.logger.info("Could not apply remove-on-cancel policy - not a Java 7+ ScheduledThreadPoolExecutor");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorHandler(ErrorHandler errorHandler) {
/* 109 */     this.errorHandler = errorHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UsesJava7
/*     */   protected ExecutorService initializeExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
/* 118 */     this.scheduledExecutor = createExecutor(this.poolSize, threadFactory, rejectedExecutionHandler);
/*     */     
/* 120 */     if (this.removeOnCancelPolicy) {
/* 121 */       if (setRemoveOnCancelPolicyAvailable && this.scheduledExecutor instanceof ScheduledThreadPoolExecutor) {
/* 122 */         ((ScheduledThreadPoolExecutor)this.scheduledExecutor).setRemoveOnCancelPolicy(true);
/*     */       } else {
/*     */         
/* 125 */         this.logger.info("Could not apply remove-on-cancel policy - not a Java 7+ ScheduledThreadPoolExecutor");
/*     */       } 
/*     */     }
/*     */     
/* 129 */     return this.scheduledExecutor;
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
/*     */   protected ScheduledExecutorService createExecutor(int poolSize, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
/* 146 */     return new ScheduledThreadPoolExecutor(poolSize, threadFactory, rejectedExecutionHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScheduledExecutorService getScheduledExecutor() throws IllegalStateException {
/* 155 */     Assert.state((this.scheduledExecutor != null), "ThreadPoolTaskScheduler not initialized");
/* 156 */     return this.scheduledExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor() throws IllegalStateException {
/* 167 */     Assert.state(this.scheduledExecutor instanceof ScheduledThreadPoolExecutor, "No ScheduledThreadPoolExecutor available");
/*     */     
/* 169 */     return (ScheduledThreadPoolExecutor)this.scheduledExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPoolSize() {
/* 179 */     if (this.scheduledExecutor == null)
/*     */     {
/* 181 */       return this.poolSize;
/*     */     }
/* 183 */     return getScheduledThreadPoolExecutor().getPoolSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UsesJava7
/*     */   public boolean isRemoveOnCancelPolicy() {
/* 192 */     if (!setRemoveOnCancelPolicyAvailable) {
/* 193 */       return false;
/*     */     }
/* 195 */     if (this.scheduledExecutor == null)
/*     */     {
/* 197 */       return this.removeOnCancelPolicy;
/*     */     }
/* 199 */     return getScheduledThreadPoolExecutor().getRemoveOnCancelPolicy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getActiveCount() {
/* 209 */     if (this.scheduledExecutor == null)
/*     */     {
/* 211 */       return 0;
/*     */     }
/* 213 */     return getScheduledThreadPoolExecutor().getActiveCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Runnable task) {
/* 221 */     Executor executor = getScheduledExecutor();
/*     */     try {
/* 223 */       executor.execute(errorHandlingTask(task, false));
/*     */     }
/* 225 */     catch (RejectedExecutionException ex) {
/* 226 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(Runnable task, long startTimeout) {
/* 232 */     execute(task);
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> submit(Runnable task) {
/* 237 */     ExecutorService executor = getScheduledExecutor();
/*     */     try {
/* 239 */       return executor.submit(errorHandlingTask(task, false));
/*     */     }
/* 241 */     catch (RejectedExecutionException ex) {
/* 242 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Future<T> submit(Callable<T> task) {
/* 248 */     ExecutorService executor = getScheduledExecutor();
/*     */     try {
/* 250 */       Callable<T> taskToUse = task;
/* 251 */       if (this.errorHandler != null) {
/* 252 */         taskToUse = new DelegatingErrorHandlingCallable<T>(task, this.errorHandler);
/*     */       }
/* 254 */       return executor.submit(taskToUse);
/*     */     }
/* 256 */     catch (RejectedExecutionException ex) {
/* 257 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> submitListenable(Runnable task) {
/* 263 */     ExecutorService executor = getScheduledExecutor();
/*     */     try {
/* 265 */       ListenableFutureTask<Object> future = new ListenableFutureTask(task, null);
/* 266 */       executor.execute(errorHandlingTask((Runnable)future, false));
/* 267 */       return (ListenableFuture<?>)future;
/*     */     }
/* 269 */     catch (RejectedExecutionException ex) {
/* 270 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
/* 276 */     ExecutorService executor = getScheduledExecutor();
/*     */     try {
/* 278 */       ListenableFutureTask<T> future = new ListenableFutureTask(task);
/* 279 */       executor.execute(errorHandlingTask((Runnable)future, false));
/* 280 */       return (ListenableFuture<T>)future;
/*     */     }
/* 282 */     catch (RejectedExecutionException ex) {
/* 283 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean prefersShortLivedTasks() {
/* 289 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> schedule(Runnable task, Trigger trigger) {
/* 297 */     ScheduledExecutorService executor = getScheduledExecutor();
/*     */     
/*     */     try {
/* 300 */       ErrorHandler errorHandler = (this.errorHandler != null) ? this.errorHandler : TaskUtils.getDefaultErrorHandler(true);
/* 301 */       return (new ReschedulingRunnable(task, trigger, executor, errorHandler)).schedule();
/*     */     }
/* 303 */     catch (RejectedExecutionException ex) {
/* 304 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> schedule(Runnable task, Date startTime) {
/* 310 */     ScheduledExecutorService executor = getScheduledExecutor();
/* 311 */     long initialDelay = startTime.getTime() - System.currentTimeMillis();
/*     */     try {
/* 313 */       return executor.schedule(errorHandlingTask(task, false), initialDelay, TimeUnit.MILLISECONDS);
/*     */     }
/* 315 */     catch (RejectedExecutionException ex) {
/* 316 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
/* 322 */     ScheduledExecutorService executor = getScheduledExecutor();
/* 323 */     long initialDelay = startTime.getTime() - System.currentTimeMillis();
/*     */     try {
/* 325 */       return executor.scheduleAtFixedRate(errorHandlingTask(task, true), initialDelay, period, TimeUnit.MILLISECONDS);
/*     */     }
/* 327 */     catch (RejectedExecutionException ex) {
/* 328 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
/* 334 */     ScheduledExecutorService executor = getScheduledExecutor();
/*     */     try {
/* 336 */       return executor.scheduleAtFixedRate(errorHandlingTask(task, true), 0L, period, TimeUnit.MILLISECONDS);
/*     */     }
/* 338 */     catch (RejectedExecutionException ex) {
/* 339 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Date startTime, long delay) {
/* 345 */     ScheduledExecutorService executor = getScheduledExecutor();
/* 346 */     long initialDelay = startTime.getTime() - System.currentTimeMillis();
/*     */     try {
/* 348 */       return executor.scheduleWithFixedDelay(errorHandlingTask(task, true), initialDelay, delay, TimeUnit.MILLISECONDS);
/*     */     }
/* 350 */     catch (RejectedExecutionException ex) {
/* 351 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long delay) {
/* 357 */     ScheduledExecutorService executor = getScheduledExecutor();
/*     */     try {
/* 359 */       return executor.scheduleWithFixedDelay(errorHandlingTask(task, true), 0L, delay, TimeUnit.MILLISECONDS);
/*     */     }
/* 361 */     catch (RejectedExecutionException ex) {
/* 362 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Runnable errorHandlingTask(Runnable task, boolean isRepeatingTask) {
/* 368 */     return (Runnable)TaskUtils.decorateTaskWithErrorHandler(task, this.errorHandler, isRepeatingTask);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class DelegatingErrorHandlingCallable<V>
/*     */     implements Callable<V>
/*     */   {
/*     */     private final Callable<V> delegate;
/*     */     private final ErrorHandler errorHandler;
/*     */     
/*     */     public DelegatingErrorHandlingCallable(Callable<V> delegate, ErrorHandler errorHandler) {
/* 379 */       this.delegate = delegate;
/* 380 */       this.errorHandler = errorHandler;
/*     */     }
/*     */ 
/*     */     
/*     */     public V call() throws Exception {
/*     */       try {
/* 386 */         return this.delegate.call();
/*     */       }
/* 388 */       catch (Throwable t) {
/* 389 */         this.errorHandler.handleError(t);
/* 390 */         return null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\concurrent\ThreadPoolTaskScheduler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */