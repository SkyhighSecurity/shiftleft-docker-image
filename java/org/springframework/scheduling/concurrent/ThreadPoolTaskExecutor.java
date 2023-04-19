/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.SynchronousQueue;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.springframework.core.task.AsyncListenableTaskExecutor;
/*     */ import org.springframework.core.task.TaskDecorator;
/*     */ import org.springframework.core.task.TaskRejectedException;
/*     */ import org.springframework.scheduling.SchedulingTaskExecutor;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ThreadPoolTaskExecutor
/*     */   extends ExecutorConfigurationSupport
/*     */   implements AsyncListenableTaskExecutor, SchedulingTaskExecutor
/*     */ {
/*  82 */   private final Object poolSizeMonitor = new Object();
/*     */   
/*  84 */   private int corePoolSize = 1;
/*     */   
/*  86 */   private int maxPoolSize = Integer.MAX_VALUE;
/*     */   
/*  88 */   private int keepAliveSeconds = 60;
/*     */   
/*  90 */   private int queueCapacity = Integer.MAX_VALUE;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean allowCoreThreadTimeOut = false;
/*     */ 
/*     */   
/*     */   private TaskDecorator taskDecorator;
/*     */ 
/*     */   
/*     */   private ThreadPoolExecutor threadPoolExecutor;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCorePoolSize(int corePoolSize) {
/* 105 */     synchronized (this.poolSizeMonitor) {
/* 106 */       this.corePoolSize = corePoolSize;
/* 107 */       if (this.threadPoolExecutor != null) {
/* 108 */         this.threadPoolExecutor.setCorePoolSize(corePoolSize);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCorePoolSize() {
/* 117 */     synchronized (this.poolSizeMonitor) {
/* 118 */       return this.corePoolSize;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxPoolSize(int maxPoolSize) {
/* 128 */     synchronized (this.poolSizeMonitor) {
/* 129 */       this.maxPoolSize = maxPoolSize;
/* 130 */       if (this.threadPoolExecutor != null) {
/* 131 */         this.threadPoolExecutor.setMaximumPoolSize(maxPoolSize);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxPoolSize() {
/* 140 */     synchronized (this.poolSizeMonitor) {
/* 141 */       return this.maxPoolSize;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeepAliveSeconds(int keepAliveSeconds) {
/* 151 */     synchronized (this.poolSizeMonitor) {
/* 152 */       this.keepAliveSeconds = keepAliveSeconds;
/* 153 */       if (this.threadPoolExecutor != null) {
/* 154 */         this.threadPoolExecutor.setKeepAliveTime(keepAliveSeconds, TimeUnit.SECONDS);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getKeepAliveSeconds() {
/* 163 */     synchronized (this.poolSizeMonitor) {
/* 164 */       return this.keepAliveSeconds;
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
/*     */   public void setQueueCapacity(int queueCapacity) {
/* 177 */     this.queueCapacity = queueCapacity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
/* 188 */     this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
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
/*     */   public void setTaskDecorator(TaskDecorator taskDecorator) {
/* 202 */     this.taskDecorator = taskDecorator;
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
/*     */   protected ExecutorService initializeExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
/*     */     ThreadPoolExecutor executor;
/* 216 */     BlockingQueue<Runnable> queue = createQueue(this.queueCapacity);
/*     */ 
/*     */     
/* 219 */     if (this.taskDecorator != null) {
/* 220 */       executor = new ThreadPoolExecutor(this.corePoolSize, this.maxPoolSize, this.keepAliveSeconds, TimeUnit.SECONDS, queue, threadFactory, rejectedExecutionHandler)
/*     */         {
/*     */           
/*     */           public void execute(Runnable command)
/*     */           {
/* 225 */             super.execute(ThreadPoolTaskExecutor.this.taskDecorator.decorate(command));
/*     */           }
/*     */         };
/*     */     } else {
/*     */       
/* 230 */       executor = new ThreadPoolExecutor(this.corePoolSize, this.maxPoolSize, this.keepAliveSeconds, TimeUnit.SECONDS, queue, threadFactory, rejectedExecutionHandler);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 236 */     if (this.allowCoreThreadTimeOut) {
/* 237 */       executor.allowCoreThreadTimeOut(true);
/*     */     }
/*     */     
/* 240 */     this.threadPoolExecutor = executor;
/* 241 */     return executor;
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
/*     */   protected BlockingQueue<Runnable> createQueue(int queueCapacity) {
/* 254 */     if (queueCapacity > 0) {
/* 255 */       return new LinkedBlockingQueue<Runnable>(queueCapacity);
/*     */     }
/*     */     
/* 258 */     return new SynchronousQueue<Runnable>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadPoolExecutor getThreadPoolExecutor() throws IllegalStateException {
/* 268 */     Assert.state((this.threadPoolExecutor != null), "ThreadPoolTaskExecutor not initialized");
/* 269 */     return this.threadPoolExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPoolSize() {
/* 277 */     if (this.threadPoolExecutor == null)
/*     */     {
/* 279 */       return this.corePoolSize;
/*     */     }
/* 281 */     return this.threadPoolExecutor.getPoolSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getActiveCount() {
/* 289 */     if (this.threadPoolExecutor == null)
/*     */     {
/* 291 */       return 0;
/*     */     }
/* 293 */     return this.threadPoolExecutor.getActiveCount();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Runnable task) {
/* 299 */     Executor executor = getThreadPoolExecutor();
/*     */     try {
/* 301 */       executor.execute(task);
/*     */     }
/* 303 */     catch (RejectedExecutionException ex) {
/* 304 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(Runnable task, long startTimeout) {
/* 310 */     execute(task);
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> submit(Runnable task) {
/* 315 */     ExecutorService executor = getThreadPoolExecutor();
/*     */     try {
/* 317 */       return executor.submit(task);
/*     */     }
/* 319 */     catch (RejectedExecutionException ex) {
/* 320 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Future<T> submit(Callable<T> task) {
/* 326 */     ExecutorService executor = getThreadPoolExecutor();
/*     */     try {
/* 328 */       return executor.submit(task);
/*     */     }
/* 330 */     catch (RejectedExecutionException ex) {
/* 331 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> submitListenable(Runnable task) {
/* 337 */     ExecutorService executor = getThreadPoolExecutor();
/*     */     try {
/* 339 */       ListenableFutureTask<Object> future = new ListenableFutureTask(task, null);
/* 340 */       executor.execute((Runnable)future);
/* 341 */       return (ListenableFuture<?>)future;
/*     */     }
/* 343 */     catch (RejectedExecutionException ex) {
/* 344 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
/* 350 */     ExecutorService executor = getThreadPoolExecutor();
/*     */     try {
/* 352 */       ListenableFutureTask<T> future = new ListenableFutureTask(task);
/* 353 */       executor.execute((Runnable)future);
/* 354 */       return (ListenableFuture<T>)future;
/*     */     }
/* 356 */     catch (RejectedExecutionException ex) {
/* 357 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean prefersShortLivedTasks() {
/* 366 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\concurrent\ThreadPoolTaskExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */