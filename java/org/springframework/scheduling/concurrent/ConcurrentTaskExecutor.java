/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import javax.enterprise.concurrent.ManagedExecutors;
/*     */ import org.springframework.core.task.AsyncListenableTaskExecutor;
/*     */ import org.springframework.core.task.TaskDecorator;
/*     */ import org.springframework.core.task.support.TaskExecutorAdapter;
/*     */ import org.springframework.scheduling.SchedulingAwareRunnable;
/*     */ import org.springframework.scheduling.SchedulingTaskExecutor;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConcurrentTaskExecutor
/*     */   implements AsyncListenableTaskExecutor, SchedulingTaskExecutor
/*     */ {
/*     */   private static Class<?> managedExecutorServiceClass;
/*     */   private Executor concurrentExecutor;
/*     */   private TaskExecutorAdapter adaptedExecutor;
/*     */   
/*     */   static {
/*     */     try {
/*  69 */       managedExecutorServiceClass = ClassUtils.forName("javax.enterprise.concurrent.ManagedExecutorService", ConcurrentTaskScheduler.class
/*     */           
/*  71 */           .getClassLoader());
/*     */     }
/*  73 */     catch (ClassNotFoundException ex) {
/*     */       
/*  75 */       managedExecutorServiceClass = null;
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
/*     */   public ConcurrentTaskExecutor() {
/*  89 */     setConcurrentExecutor(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcurrentTaskExecutor(Executor concurrentExecutor) {
/*  99 */     setConcurrentExecutor(concurrentExecutor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setConcurrentExecutor(Executor concurrentExecutor) {
/* 109 */     if (concurrentExecutor != null) {
/* 110 */       this.concurrentExecutor = concurrentExecutor;
/* 111 */       if (managedExecutorServiceClass != null && managedExecutorServiceClass.isInstance(concurrentExecutor)) {
/* 112 */         this.adaptedExecutor = new ManagedTaskExecutorAdapter(concurrentExecutor);
/*     */       } else {
/*     */         
/* 115 */         this.adaptedExecutor = new TaskExecutorAdapter(concurrentExecutor);
/*     */       } 
/*     */     } else {
/*     */       
/* 119 */       this.concurrentExecutor = Executors.newSingleThreadExecutor();
/* 120 */       this.adaptedExecutor = new TaskExecutorAdapter(this.concurrentExecutor);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Executor getConcurrentExecutor() {
/* 128 */     return this.concurrentExecutor;
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
/* 142 */     this.adaptedExecutor.setTaskDecorator(taskDecorator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Runnable task) {
/* 148 */     this.adaptedExecutor.execute(task);
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(Runnable task, long startTimeout) {
/* 153 */     this.adaptedExecutor.execute(task, startTimeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> submit(Runnable task) {
/* 158 */     return this.adaptedExecutor.submit(task);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Future<T> submit(Callable<T> task) {
/* 163 */     return this.adaptedExecutor.submit(task);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> submitListenable(Runnable task) {
/* 168 */     return this.adaptedExecutor.submitListenable(task);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
/* 173 */     return this.adaptedExecutor.submitListenable(task);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean prefersShortLivedTasks() {
/* 181 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ManagedTaskExecutorAdapter
/*     */     extends TaskExecutorAdapter
/*     */   {
/*     */     public ManagedTaskExecutorAdapter(Executor concurrentExecutor) {
/* 194 */       super(concurrentExecutor);
/*     */     }
/*     */ 
/*     */     
/*     */     public void execute(Runnable task) {
/* 199 */       super.execute(ConcurrentTaskExecutor.ManagedTaskBuilder.buildManagedTask(task, task.toString()));
/*     */     }
/*     */ 
/*     */     
/*     */     public Future<?> submit(Runnable task) {
/* 204 */       return super.submit(ConcurrentTaskExecutor.ManagedTaskBuilder.buildManagedTask(task, task.toString()));
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Future<T> submit(Callable<T> task) {
/* 209 */       return super.submit(ConcurrentTaskExecutor.ManagedTaskBuilder.buildManagedTask(task, task.toString()));
/*     */     }
/*     */ 
/*     */     
/*     */     public ListenableFuture<?> submitListenable(Runnable task) {
/* 214 */       return super.submitListenable(ConcurrentTaskExecutor.ManagedTaskBuilder.buildManagedTask(task, task.toString()));
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
/* 219 */       return super.submitListenable(ConcurrentTaskExecutor.ManagedTaskBuilder.buildManagedTask(task, task.toString()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class ManagedTaskBuilder
/*     */   {
/*     */     public static Runnable buildManagedTask(Runnable task, String identityName) {
/* 232 */       Map<String, String> properties = new HashMap<String, String>(2);
/* 233 */       if (task instanceof SchedulingAwareRunnable) {
/* 234 */         properties.put("javax.enterprise.concurrent.LONGRUNNING_HINT", 
/* 235 */             Boolean.toString(((SchedulingAwareRunnable)task).isLongLived()));
/*     */       }
/* 237 */       properties.put("javax.enterprise.concurrent.IDENTITY_NAME", identityName);
/* 238 */       return ManagedExecutors.managedTask(task, properties, null);
/*     */     }
/*     */     
/*     */     public static <T> Callable<T> buildManagedTask(Callable<T> task, String identityName) {
/* 242 */       Map<String, String> properties = new HashMap<String, String>(1);
/* 243 */       properties.put("javax.enterprise.concurrent.IDENTITY_NAME", identityName);
/* 244 */       return ManagedExecutors.managedTask(task, properties, null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\concurrent\ConcurrentTaskExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */