/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.lang.UsesJava7;
/*     */ import org.springframework.scheduling.support.DelegatingErrorHandlingRunnable;
/*     */ import org.springframework.scheduling.support.TaskUtils;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScheduledExecutorFactoryBean
/*     */   extends ExecutorConfigurationSupport
/*     */   implements FactoryBean<ScheduledExecutorService>
/*     */ {
/*  80 */   private static final boolean setRemoveOnCancelPolicyAvailable = ClassUtils.hasMethod(ScheduledThreadPoolExecutor.class, "setRemoveOnCancelPolicy", new Class[] { boolean.class });
/*     */ 
/*     */   
/*  83 */   private int poolSize = 1;
/*     */ 
/*     */   
/*     */   private ScheduledExecutorTask[] scheduledExecutorTasks;
/*     */ 
/*     */   
/*     */   private boolean removeOnCancelPolicy = false;
/*     */ 
/*     */   
/*     */   private boolean continueScheduledExecutionAfterException = false;
/*     */ 
/*     */   
/*     */   private boolean exposeUnconfigurableExecutor = false;
/*     */   
/*     */   private ScheduledExecutorService exposedExecutor;
/*     */ 
/*     */   
/*     */   public void setPoolSize(int poolSize) {
/* 101 */     Assert.isTrue((poolSize > 0), "'poolSize' must be 1 or higher");
/* 102 */     this.poolSize = poolSize;
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
/*     */   public void setScheduledExecutorTasks(ScheduledExecutorTask... scheduledExecutorTasks) {
/* 114 */     this.scheduledExecutorTasks = scheduledExecutorTasks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoveOnCancelPolicy(boolean removeOnCancelPolicy) {
/* 123 */     this.removeOnCancelPolicy = removeOnCancelPolicy;
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
/*     */   public void setContinueScheduledExecutionAfterException(boolean continueScheduledExecutionAfterException) {
/* 136 */     this.continueScheduledExecutionAfterException = continueScheduledExecutionAfterException;
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
/*     */   public void setExposeUnconfigurableExecutor(boolean exposeUnconfigurableExecutor) {
/* 148 */     this.exposeUnconfigurableExecutor = exposeUnconfigurableExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UsesJava7
/*     */   protected ExecutorService initializeExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
/* 158 */     ScheduledExecutorService executor = createExecutor(this.poolSize, threadFactory, rejectedExecutionHandler);
/*     */     
/* 160 */     if (this.removeOnCancelPolicy) {
/* 161 */       if (setRemoveOnCancelPolicyAvailable && executor instanceof ScheduledThreadPoolExecutor) {
/* 162 */         ((ScheduledThreadPoolExecutor)executor).setRemoveOnCancelPolicy(true);
/*     */       } else {
/*     */         
/* 165 */         this.logger.info("Could not apply remove-on-cancel policy - not a Java 7+ ScheduledThreadPoolExecutor");
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 170 */     if (!ObjectUtils.isEmpty((Object[])this.scheduledExecutorTasks)) {
/* 171 */       registerTasks(this.scheduledExecutorTasks, executor);
/*     */     }
/*     */ 
/*     */     
/* 175 */     this
/* 176 */       .exposedExecutor = this.exposeUnconfigurableExecutor ? Executors.unconfigurableScheduledExecutorService(executor) : executor;
/*     */     
/* 178 */     return executor;
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
/* 195 */     return new ScheduledThreadPoolExecutor(poolSize, threadFactory, rejectedExecutionHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerTasks(ScheduledExecutorTask[] tasks, ScheduledExecutorService executor) {
/* 205 */     for (ScheduledExecutorTask task : tasks) {
/* 206 */       Runnable runnable = getRunnableToSchedule(task);
/* 207 */       if (task.isOneTimeTask()) {
/* 208 */         executor.schedule(runnable, task.getDelay(), task.getTimeUnit());
/*     */       
/*     */       }
/* 211 */       else if (task.isFixedRate()) {
/* 212 */         executor.scheduleAtFixedRate(runnable, task.getDelay(), task.getPeriod(), task.getTimeUnit());
/*     */       } else {
/*     */         
/* 215 */         executor.scheduleWithFixedDelay(runnable, task.getDelay(), task.getPeriod(), task.getTimeUnit());
/*     */       } 
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
/*     */   protected Runnable getRunnableToSchedule(ScheduledExecutorTask task) {
/* 233 */     return this.continueScheduledExecutionAfterException ? (Runnable)new DelegatingErrorHandlingRunnable(task
/* 234 */         .getRunnable(), TaskUtils.LOG_AND_SUPPRESS_ERROR_HANDLER) : (Runnable)new DelegatingErrorHandlingRunnable(task
/* 235 */         .getRunnable(), TaskUtils.LOG_AND_PROPAGATE_ERROR_HANDLER);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ScheduledExecutorService getObject() {
/* 241 */     return this.exposedExecutor;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<? extends ScheduledExecutorService> getObjectType() {
/* 246 */     return (this.exposedExecutor != null) ? (Class)this.exposedExecutor.getClass() : ScheduledExecutorService.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 251 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\concurrent\ScheduledExecutorFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */