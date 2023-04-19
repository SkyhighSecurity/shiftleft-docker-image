/*     */ package org.springframework.core.task;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.FutureTask;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ConcurrencyThrottleSupport;
/*     */ import org.springframework.util.CustomizableThreadCreator;
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
/*     */ public class SimpleAsyncTaskExecutor
/*     */   extends CustomizableThreadCreator
/*     */   implements AsyncListenableTaskExecutor, Serializable
/*     */ {
/*     */   public static final int UNBOUNDED_CONCURRENCY = -1;
/*     */   public static final int NO_CONCURRENCY = 0;
/*  67 */   private final ConcurrencyThrottleAdapter concurrencyThrottle = new ConcurrencyThrottleAdapter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ThreadFactory threadFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TaskDecorator taskDecorator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleAsyncTaskExecutor(String threadNamePrefix) {
/*  86 */     super(threadNamePrefix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleAsyncTaskExecutor(ThreadFactory threadFactory) {
/*  94 */     this.threadFactory = threadFactory;
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
/*     */   public void setThreadFactory(ThreadFactory threadFactory) {
/* 107 */     this.threadFactory = threadFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ThreadFactory getThreadFactory() {
/* 114 */     return this.threadFactory;
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
/*     */   public final void setTaskDecorator(TaskDecorator taskDecorator) {
/* 128 */     this.taskDecorator = taskDecorator;
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
/*     */   public void setConcurrencyLimit(int concurrencyLimit) {
/* 142 */     this.concurrencyThrottle.setConcurrencyLimit(concurrencyLimit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getConcurrencyLimit() {
/* 149 */     return this.concurrencyThrottle.getConcurrencyLimit();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isThrottleActive() {
/* 159 */     return this.concurrencyThrottle.isThrottleActive();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Runnable task) {
/* 170 */     execute(task, Long.MAX_VALUE);
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
/*     */   public void execute(Runnable task, long startTimeout) {
/* 184 */     Assert.notNull(task, "Runnable must not be null");
/* 185 */     Runnable taskToUse = (this.taskDecorator != null) ? this.taskDecorator.decorate(task) : task;
/* 186 */     if (isThrottleActive() && startTimeout > 0L) {
/* 187 */       this.concurrencyThrottle.beforeAccess();
/* 188 */       doExecute(new ConcurrencyThrottlingRunnable(taskToUse));
/*     */     } else {
/*     */       
/* 191 */       doExecute(taskToUse);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> submit(Runnable task) {
/* 197 */     FutureTask<Object> future = new FutureTask(task, null);
/* 198 */     execute(future, Long.MAX_VALUE);
/* 199 */     return future;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Future<T> submit(Callable<T> task) {
/* 204 */     FutureTask<T> future = new FutureTask<T>(task);
/* 205 */     execute(future, Long.MAX_VALUE);
/* 206 */     return future;
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> submitListenable(Runnable task) {
/* 211 */     ListenableFutureTask<Object> future = new ListenableFutureTask(task, null);
/* 212 */     execute((Runnable)future, Long.MAX_VALUE);
/* 213 */     return (ListenableFuture<?>)future;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
/* 218 */     ListenableFutureTask<T> future = new ListenableFutureTask(task);
/* 219 */     execute((Runnable)future, Long.MAX_VALUE);
/* 220 */     return (ListenableFuture<T>)future;
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
/*     */   protected void doExecute(Runnable task) {
/* 232 */     Thread thread = (this.threadFactory != null) ? this.threadFactory.newThread(task) : createThread(task);
/* 233 */     thread.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleAsyncTaskExecutor() {}
/*     */ 
/*     */   
/*     */   private static class ConcurrencyThrottleAdapter
/*     */     extends ConcurrencyThrottleSupport
/*     */   {
/*     */     private ConcurrencyThrottleAdapter() {}
/*     */     
/*     */     protected void beforeAccess() {
/* 246 */       super.beforeAccess();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void afterAccess() {
/* 251 */       super.afterAccess();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class ConcurrencyThrottlingRunnable
/*     */     implements Runnable
/*     */   {
/*     */     private final Runnable target;
/*     */ 
/*     */ 
/*     */     
/*     */     public ConcurrencyThrottlingRunnable(Runnable target) {
/* 265 */       this.target = target;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 271 */         this.target.run();
/*     */       } finally {
/*     */         
/* 274 */         SimpleAsyncTaskExecutor.this.concurrencyThrottle.afterAccess();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\task\SimpleAsyncTaskExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */