/*     */ package org.springframework.web.context.request.async;
/*     */ 
/*     */ import java.util.concurrent.Callable;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.core.task.AsyncTaskExecutor;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WebAsyncTask<V>
/*     */   implements BeanFactoryAware
/*     */ {
/*     */   private final Callable<V> callable;
/*     */   private Long timeout;
/*     */   private AsyncTaskExecutor executor;
/*     */   private String executorName;
/*     */   private BeanFactory beanFactory;
/*     */   private Callable<V> timeoutCallback;
/*     */   private Runnable completionCallback;
/*     */   
/*     */   public WebAsyncTask(Callable<V> callable) {
/*  56 */     Assert.notNull(callable, "Callable must not be null");
/*  57 */     this.callable = callable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebAsyncTask(long timeout, Callable<V> callable) {
/*  66 */     this(callable);
/*  67 */     this.timeout = Long.valueOf(timeout);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebAsyncTask(Long timeout, String executorName, Callable<V> callable) {
/*  77 */     this(callable);
/*  78 */     Assert.notNull(executorName, "Executor name must not be null");
/*  79 */     this.executorName = executorName;
/*  80 */     this.timeout = timeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebAsyncTask(Long timeout, AsyncTaskExecutor executor, Callable<V> callable) {
/*  90 */     this(callable);
/*  91 */     Assert.notNull(executor, "Executor must not be null");
/*  92 */     this.executor = executor;
/*  93 */     this.timeout = timeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Callable<?> getCallable() {
/* 101 */     return this.callable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Long getTimeout() {
/* 108 */     return this.timeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 117 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncTaskExecutor getExecutor() {
/* 125 */     if (this.executor != null) {
/* 126 */       return this.executor;
/*     */     }
/* 128 */     if (this.executorName != null) {
/* 129 */       Assert.state((this.beanFactory != null), "BeanFactory is required to look up an executor bean by name");
/* 130 */       return (AsyncTaskExecutor)this.beanFactory.getBean(this.executorName, AsyncTaskExecutor.class);
/*     */     } 
/*     */     
/* 133 */     return null;
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
/*     */   public void onTimeout(Callable<V> callback) {
/* 147 */     this.timeoutCallback = callback;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onCompletion(Runnable callback) {
/* 156 */     this.completionCallback = callback;
/*     */   }
/*     */   
/*     */   CallableProcessingInterceptor getInterceptor() {
/* 160 */     return new CallableProcessingInterceptorAdapter()
/*     */       {
/*     */         public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
/* 163 */           return (WebAsyncTask.this.timeoutCallback != null) ? WebAsyncTask.this.timeoutCallback.call() : CallableProcessingInterceptor.RESULT_NONE;
/*     */         }
/*     */         
/*     */         public <T> void afterCompletion(NativeWebRequest request, Callable<T> task) throws Exception {
/* 167 */           if (WebAsyncTask.this.completionCallback != null)
/* 168 */             WebAsyncTask.this.completionCallback.run(); 
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\async\WebAsyncTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */