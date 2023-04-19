/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ExecutorConfigurationSupport
/*     */   extends CustomizableThreadFactory
/*     */   implements BeanNameAware, InitializingBean, DisposableBean
/*     */ {
/*  49 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  51 */   private ThreadFactory threadFactory = this;
/*     */   
/*     */   private boolean threadNamePrefixSet = false;
/*     */   
/*  55 */   private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
/*     */   
/*     */   private boolean waitForTasksToCompleteOnShutdown = false;
/*     */   
/*  59 */   private int awaitTerminationSeconds = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String beanName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ExecutorService executor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThreadFactory(ThreadFactory threadFactory) {
/*  81 */     this.threadFactory = (threadFactory != null) ? threadFactory : this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setThreadNamePrefix(String threadNamePrefix) {
/*  86 */     super.setThreadNamePrefix(threadNamePrefix);
/*  87 */     this.threadNamePrefixSet = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
/*  96 */     this.rejectedExecutionHandler = (rejectedExecutionHandler != null) ? rejectedExecutionHandler : new ThreadPoolExecutor.AbortPolicy();
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
/*     */ 
/*     */   
/*     */   public void setWaitForTasksToCompleteOnShutdown(boolean waitForJobsToCompleteOnShutdown) {
/* 116 */     this.waitForTasksToCompleteOnShutdown = waitForJobsToCompleteOnShutdown;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAwaitTerminationSeconds(int awaitTerminationSeconds) {
/* 143 */     this.awaitTerminationSeconds = awaitTerminationSeconds;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanName(String name) {
/* 148 */     this.beanName = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 158 */     initialize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize() {
/* 165 */     if (this.logger.isInfoEnabled()) {
/* 166 */       this.logger.info("Initializing ExecutorService" + ((this.beanName != null) ? (" '" + this.beanName + "'") : ""));
/*     */     }
/* 168 */     if (!this.threadNamePrefixSet && this.beanName != null) {
/* 169 */       setThreadNamePrefix(this.beanName + "-");
/*     */     }
/* 171 */     this.executor = initializeExecutor(this.threadFactory, this.rejectedExecutionHandler);
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
/*     */   protected abstract ExecutorService initializeExecutor(ThreadFactory paramThreadFactory, RejectedExecutionHandler paramRejectedExecutionHandler);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 193 */     shutdown();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 202 */     if (this.logger.isInfoEnabled()) {
/* 203 */       this.logger.info("Shutting down ExecutorService" + ((this.beanName != null) ? (" '" + this.beanName + "'") : ""));
/*     */     }
/* 205 */     if (this.executor != null) {
/* 206 */       if (this.waitForTasksToCompleteOnShutdown) {
/* 207 */         this.executor.shutdown();
/*     */       } else {
/*     */         
/* 210 */         this.executor.shutdownNow();
/*     */       } 
/* 212 */       awaitTerminationIfNecessary(this.executor);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void awaitTerminationIfNecessary(ExecutorService executor) {
/* 221 */     if (this.awaitTerminationSeconds > 0)
/*     */       try {
/* 223 */         if (!executor.awaitTermination(this.awaitTerminationSeconds, TimeUnit.SECONDS) && 
/* 224 */           this.logger.isWarnEnabled()) {
/* 225 */           this.logger.warn("Timed out while waiting for executor" + ((this.beanName != null) ? (" '" + this.beanName + "'") : "") + " to terminate");
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 230 */       catch (InterruptedException ex) {
/* 231 */         if (this.logger.isWarnEnabled()) {
/* 232 */           this.logger.warn("Interrupted while waiting for executor" + ((this.beanName != null) ? (" '" + this.beanName + "'") : "") + " to terminate");
/*     */         }
/*     */         
/* 235 */         Thread.currentThread().interrupt();
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\concurrent\ExecutorConfigurationSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */