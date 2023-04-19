/*     */ package org.springframework.scheduling.support;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.springframework.scheduling.Trigger;
/*     */ import org.springframework.scheduling.TriggerContext;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PeriodicTrigger
/*     */   implements Trigger
/*     */ {
/*     */   private final long period;
/*     */   private final TimeUnit timeUnit;
/*  52 */   private volatile long initialDelay = 0L;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean fixedRate = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public PeriodicTrigger(long period) {
/*  61 */     this(period, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PeriodicTrigger(long period, TimeUnit timeUnit) {
/*  70 */     Assert.isTrue((period >= 0L), "period must not be negative");
/*  71 */     this.timeUnit = (timeUnit != null) ? timeUnit : TimeUnit.MILLISECONDS;
/*  72 */     this.period = this.timeUnit.toMillis(period);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInitialDelay(long initialDelay) {
/*  82 */     this.initialDelay = this.timeUnit.toMillis(initialDelay);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFixedRate(boolean fixedRate) {
/*  91 */     this.fixedRate = fixedRate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date nextExecutionTime(TriggerContext triggerContext) {
/* 100 */     if (triggerContext.lastScheduledExecutionTime() == null) {
/* 101 */       return new Date(System.currentTimeMillis() + this.initialDelay);
/*     */     }
/* 103 */     if (this.fixedRate) {
/* 104 */       return new Date(triggerContext.lastScheduledExecutionTime().getTime() + this.period);
/*     */     }
/* 106 */     return new Date(triggerContext.lastCompletionTime().getTime() + this.period);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 112 */     if (this == obj) {
/* 113 */       return true;
/*     */     }
/* 115 */     if (!(obj instanceof PeriodicTrigger)) {
/* 116 */       return false;
/*     */     }
/* 118 */     PeriodicTrigger other = (PeriodicTrigger)obj;
/* 119 */     return (this.fixedRate == other.fixedRate && this.initialDelay == other.initialDelay && this.period == other.period);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 125 */     return (this.fixedRate ? 17 : 29) + (int)(37L * this.period) + (int)(41L * this.initialDelay);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\support\PeriodicTrigger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */