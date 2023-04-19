/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.concurrent.Delayed;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import org.springframework.scheduling.Trigger;
/*     */ import org.springframework.scheduling.TriggerContext;
/*     */ import org.springframework.scheduling.support.DelegatingErrorHandlingRunnable;
/*     */ import org.springframework.scheduling.support.SimpleTriggerContext;
/*     */ import org.springframework.util.ErrorHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ReschedulingRunnable
/*     */   extends DelegatingErrorHandlingRunnable
/*     */   implements ScheduledFuture<Object>
/*     */ {
/*     */   private final Trigger trigger;
/*  48 */   private final SimpleTriggerContext triggerContext = new SimpleTriggerContext();
/*     */   
/*     */   private final ScheduledExecutorService executor;
/*     */   
/*     */   private ScheduledFuture<?> currentFuture;
/*     */   
/*     */   private Date scheduledExecutionTime;
/*     */   
/*  56 */   private final Object triggerContextMonitor = new Object();
/*     */ 
/*     */   
/*     */   public ReschedulingRunnable(Runnable delegate, Trigger trigger, ScheduledExecutorService executor, ErrorHandler errorHandler) {
/*  60 */     super(delegate, errorHandler);
/*  61 */     this.trigger = trigger;
/*  62 */     this.executor = executor;
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> schedule() {
/*  67 */     synchronized (this.triggerContextMonitor) {
/*  68 */       this.scheduledExecutionTime = this.trigger.nextExecutionTime((TriggerContext)this.triggerContext);
/*  69 */       if (this.scheduledExecutionTime == null) {
/*  70 */         return null;
/*     */       }
/*  72 */       long initialDelay = this.scheduledExecutionTime.getTime() - System.currentTimeMillis();
/*  73 */       this.currentFuture = this.executor.schedule((Runnable)this, initialDelay, TimeUnit.MILLISECONDS);
/*  74 */       return this;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  80 */     Date actualExecutionTime = new Date();
/*  81 */     super.run();
/*  82 */     Date completionTime = new Date();
/*  83 */     synchronized (this.triggerContextMonitor) {
/*  84 */       this.triggerContext.update(this.scheduledExecutionTime, actualExecutionTime, completionTime);
/*  85 */       if (!this.currentFuture.isCancelled()) {
/*  86 */         schedule();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/*  94 */     synchronized (this.triggerContextMonitor) {
/*  95 */       return this.currentFuture.cancel(mayInterruptIfRunning);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/* 101 */     synchronized (this.triggerContextMonitor) {
/* 102 */       return this.currentFuture.isCancelled();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/* 108 */     synchronized (this.triggerContextMonitor) {
/* 109 */       return this.currentFuture.isDone();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object get() throws InterruptedException, ExecutionException {
/*     */     ScheduledFuture<?> curr;
/* 116 */     synchronized (this.triggerContextMonitor) {
/* 117 */       curr = this.currentFuture;
/*     */     } 
/* 119 */     return curr.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*     */     ScheduledFuture<?> curr;
/* 125 */     synchronized (this.triggerContextMonitor) {
/* 126 */       curr = this.currentFuture;
/*     */     } 
/* 128 */     return curr.get(timeout, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getDelay(TimeUnit unit) {
/*     */     ScheduledFuture<?> curr;
/* 134 */     synchronized (this.triggerContextMonitor) {
/* 135 */       curr = this.currentFuture;
/*     */     } 
/* 137 */     return curr.getDelay(unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Delayed other) {
/* 142 */     if (this == other) {
/* 143 */       return 0;
/*     */     }
/* 145 */     long diff = getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS);
/* 146 */     return (diff == 0L) ? 0 : ((diff < 0L) ? -1 : 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\concurrent\ReschedulingRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */