/*     */ package org.springframework.core.task.support;
/*     */ 
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.FutureTask;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import org.springframework.core.task.AsyncListenableTaskExecutor;
/*     */ import org.springframework.core.task.TaskDecorator;
/*     */ import org.springframework.core.task.TaskRejectedException;
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
/*     */ public class TaskExecutorAdapter
/*     */   implements AsyncListenableTaskExecutor
/*     */ {
/*     */   private final Executor concurrentExecutor;
/*     */   private TaskDecorator taskDecorator;
/*     */   
/*     */   public TaskExecutorAdapter(Executor concurrentExecutor) {
/*  58 */     Assert.notNull(concurrentExecutor, "Executor must not be null");
/*  59 */     this.concurrentExecutor = concurrentExecutor;
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
/*     */   public final void setTaskDecorator(TaskDecorator taskDecorator) {
/*  74 */     this.taskDecorator = taskDecorator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Runnable task) {
/*     */     try {
/*  85 */       doExecute(this.concurrentExecutor, this.taskDecorator, task);
/*     */     }
/*  87 */     catch (RejectedExecutionException ex) {
/*  88 */       throw new TaskRejectedException("Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Runnable task, long startTimeout) {
/*  95 */     execute(task);
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> submit(Runnable task) {
/*     */     try {
/* 101 */       if (this.taskDecorator == null && this.concurrentExecutor instanceof ExecutorService) {
/* 102 */         return ((ExecutorService)this.concurrentExecutor).submit(task);
/*     */       }
/*     */       
/* 105 */       FutureTask<Object> future = new FutureTask(task, null);
/* 106 */       doExecute(this.concurrentExecutor, this.taskDecorator, future);
/* 107 */       return future;
/*     */     
/*     */     }
/* 110 */     catch (RejectedExecutionException ex) {
/* 111 */       throw new TaskRejectedException("Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Future<T> submit(Callable<T> task) {
/*     */     try {
/* 119 */       if (this.taskDecorator == null && this.concurrentExecutor instanceof ExecutorService) {
/* 120 */         return ((ExecutorService)this.concurrentExecutor).submit(task);
/*     */       }
/*     */       
/* 123 */       FutureTask<T> future = new FutureTask<T>(task);
/* 124 */       doExecute(this.concurrentExecutor, this.taskDecorator, future);
/* 125 */       return future;
/*     */     
/*     */     }
/* 128 */     catch (RejectedExecutionException ex) {
/* 129 */       throw new TaskRejectedException("Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> submitListenable(Runnable task) {
/*     */     try {
/* 137 */       ListenableFutureTask<Object> future = new ListenableFutureTask(task, null);
/* 138 */       doExecute(this.concurrentExecutor, this.taskDecorator, (Runnable)future);
/* 139 */       return (ListenableFuture<?>)future;
/*     */     }
/* 141 */     catch (RejectedExecutionException ex) {
/* 142 */       throw new TaskRejectedException("Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
/*     */     try {
/* 150 */       ListenableFutureTask<T> future = new ListenableFutureTask(task);
/* 151 */       doExecute(this.concurrentExecutor, this.taskDecorator, (Runnable)future);
/* 152 */       return (ListenableFuture<T>)future;
/*     */     }
/* 154 */     catch (RejectedExecutionException ex) {
/* 155 */       throw new TaskRejectedException("Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
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
/*     */ 
/*     */   
/*     */   protected void doExecute(Executor concurrentExecutor, TaskDecorator taskDecorator, Runnable runnable) throws RejectedExecutionException {
/* 173 */     concurrentExecutor.execute((taskDecorator != null) ? taskDecorator.decorate(runnable) : runnable);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\task\support\TaskExecutorAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */