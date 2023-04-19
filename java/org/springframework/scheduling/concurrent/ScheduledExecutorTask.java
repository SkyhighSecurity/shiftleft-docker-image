/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScheduledExecutorTask
/*     */ {
/*     */   private Runnable runnable;
/*  43 */   private long delay = 0L;
/*     */   
/*  45 */   private long period = -1L;
/*     */   
/*  47 */   private TimeUnit timeUnit = TimeUnit.MILLISECONDS;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean fixedRate = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScheduledExecutorTask() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScheduledExecutorTask(Runnable executorTask) {
/*  68 */     this.runnable = executorTask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScheduledExecutorTask(Runnable executorTask, long delay) {
/*  78 */     this.runnable = executorTask;
/*  79 */     this.delay = delay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScheduledExecutorTask(Runnable executorTask, long delay, long period, boolean fixedRate) {
/*  90 */     this.runnable = executorTask;
/*  91 */     this.delay = delay;
/*  92 */     this.period = period;
/*  93 */     this.fixedRate = fixedRate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRunnable(Runnable executorTask) {
/* 101 */     this.runnable = executorTask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Runnable getRunnable() {
/* 108 */     return this.runnable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDelay(long delay) {
/* 117 */     this.delay = delay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getDelay() {
/* 124 */     return this.delay;
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
/*     */   
/*     */   public void setPeriod(long period) {
/* 142 */     this.period = period;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getPeriod() {
/* 149 */     return this.period;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOneTimeTask() {
/* 158 */     return (this.period <= 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeUnit(TimeUnit timeUnit) {
/* 168 */     this.timeUnit = (timeUnit != null) ? timeUnit : TimeUnit.MILLISECONDS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeUnit getTimeUnit() {
/* 175 */     return this.timeUnit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFixedRate(boolean fixedRate) {
/* 186 */     this.fixedRate = fixedRate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFixedRate() {
/* 193 */     return this.fixedRate;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\concurrent\ScheduledExecutorTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */