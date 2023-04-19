/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.SynchronousQueue;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.FactoryBean;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ThreadPoolExecutorFactoryBean
/*     */   extends ExecutorConfigurationSupport
/*     */   implements FactoryBean<ExecutorService>, InitializingBean, DisposableBean
/*     */ {
/*  69 */   private int corePoolSize = 1;
/*     */   
/*  71 */   private int maxPoolSize = Integer.MAX_VALUE;
/*     */   
/*  73 */   private int keepAliveSeconds = 60;
/*     */   
/*     */   private boolean allowCoreThreadTimeOut = false;
/*     */   
/*  77 */   private int queueCapacity = Integer.MAX_VALUE;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean exposeUnconfigurableExecutor = false;
/*     */ 
/*     */   
/*     */   private ExecutorService exposedExecutor;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCorePoolSize(int corePoolSize) {
/*  89 */     this.corePoolSize = corePoolSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxPoolSize(int maxPoolSize) {
/*  97 */     this.maxPoolSize = maxPoolSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeepAliveSeconds(int keepAliveSeconds) {
/* 105 */     this.keepAliveSeconds = keepAliveSeconds;
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
/* 116 */     this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
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
/* 128 */     this.queueCapacity = queueCapacity;
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
/* 140 */     this.exposeUnconfigurableExecutor = exposeUnconfigurableExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ExecutorService initializeExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
/* 148 */     BlockingQueue<Runnable> queue = createQueue(this.queueCapacity);
/* 149 */     ThreadPoolExecutor executor = createExecutor(this.corePoolSize, this.maxPoolSize, this.keepAliveSeconds, queue, threadFactory, rejectedExecutionHandler);
/*     */     
/* 151 */     if (this.allowCoreThreadTimeOut) {
/* 152 */       executor.allowCoreThreadTimeOut(true);
/*     */     }
/*     */ 
/*     */     
/* 156 */     this
/* 157 */       .exposedExecutor = this.exposeUnconfigurableExecutor ? Executors.unconfigurableExecutorService(executor) : executor;
/*     */     
/* 159 */     return executor;
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
/*     */   protected ThreadPoolExecutor createExecutor(int corePoolSize, int maxPoolSize, int keepAliveSeconds, BlockingQueue<Runnable> queue, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
/* 179 */     return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveSeconds, TimeUnit.SECONDS, queue, threadFactory, rejectedExecutionHandler);
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
/*     */   protected BlockingQueue<Runnable> createQueue(int queueCapacity) {
/* 193 */     if (queueCapacity > 0) {
/* 194 */       return new LinkedBlockingQueue<Runnable>(queueCapacity);
/*     */     }
/*     */     
/* 197 */     return new SynchronousQueue<Runnable>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExecutorService getObject() {
/* 204 */     return this.exposedExecutor;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<? extends ExecutorService> getObjectType() {
/* 209 */     return (this.exposedExecutor != null) ? (Class)this.exposedExecutor.getClass() : ExecutorService.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 214 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\concurrent\ThreadPoolExecutorFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */