/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.concurrent.ForkJoinPool;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.lang.UsesJava7;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @UsesJava7
/*     */ public class ForkJoinPoolFactoryBean
/*     */   implements FactoryBean<ForkJoinPool>, InitializingBean, DisposableBean
/*     */ {
/*     */   private boolean commonPool = false;
/*  46 */   private int parallelism = Runtime.getRuntime().availableProcessors();
/*     */   
/*  48 */   private ForkJoinPool.ForkJoinWorkerThreadFactory threadFactory = ForkJoinPool.defaultForkJoinWorkerThreadFactory;
/*     */   
/*     */   private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
/*     */   
/*     */   private boolean asyncMode = false;
/*     */   
/*  54 */   private int awaitTerminationSeconds = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ForkJoinPool forkJoinPool;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommonPool(boolean commonPool) {
/*  73 */     this.commonPool = commonPool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParallelism(int parallelism) {
/*  80 */     this.parallelism = parallelism;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThreadFactory(ForkJoinPool.ForkJoinWorkerThreadFactory threadFactory) {
/*  88 */     this.threadFactory = threadFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
/*  96 */     this.uncaughtExceptionHandler = uncaughtExceptionHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsyncMode(boolean asyncMode) {
/* 106 */     this.asyncMode = asyncMode;
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
/*     */   public void setAwaitTerminationSeconds(int awaitTerminationSeconds) {
/* 127 */     this.awaitTerminationSeconds = awaitTerminationSeconds;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 132 */     this.forkJoinPool = this.commonPool ? ForkJoinPool.commonPool() : new ForkJoinPool(this.parallelism, this.threadFactory, this.uncaughtExceptionHandler, this.asyncMode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ForkJoinPool getObject() {
/* 139 */     return this.forkJoinPool;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 144 */     return ForkJoinPool.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 149 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 156 */     this.forkJoinPool.shutdown();
/*     */ 
/*     */     
/* 159 */     if (this.awaitTerminationSeconds > 0)
/*     */       try {
/* 161 */         this.forkJoinPool.awaitTermination(this.awaitTerminationSeconds, TimeUnit.SECONDS);
/*     */       }
/* 163 */       catch (InterruptedException ex) {
/* 164 */         Thread.currentThread().interrupt();
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\concurrent\ForkJoinPoolFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */