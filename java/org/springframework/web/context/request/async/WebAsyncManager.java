/*     */ package org.springframework.web.context.request.async;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.task.AsyncTaskExecutor;
/*     */ import org.springframework.core.task.SimpleAsyncTaskExecutor;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.util.UrlPathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class WebAsyncManager
/*     */ {
/*  62 */   private static final Object RESULT_NONE = new Object();
/*     */   
/*  64 */   private static final Log logger = LogFactory.getLog(WebAsyncManager.class);
/*     */   
/*  66 */   private static final UrlPathHelper urlPathHelper = new UrlPathHelper();
/*     */   
/*  68 */   private static final CallableProcessingInterceptor timeoutCallableInterceptor = new TimeoutCallableProcessingInterceptor();
/*     */ 
/*     */   
/*  71 */   private static final DeferredResultProcessingInterceptor timeoutDeferredResultInterceptor = new TimeoutDeferredResultProcessingInterceptor();
/*     */ 
/*     */   
/*     */   private AsyncWebRequest asyncWebRequest;
/*     */ 
/*     */   
/*  77 */   private AsyncTaskExecutor taskExecutor = (AsyncTaskExecutor)new SimpleAsyncTaskExecutor(getClass().getSimpleName());
/*     */   
/*  79 */   private volatile Object concurrentResult = RESULT_NONE;
/*     */   
/*     */   private volatile Object[] concurrentResultContext;
/*     */   
/*  83 */   private final Map<Object, CallableProcessingInterceptor> callableInterceptors = new LinkedHashMap<Object, CallableProcessingInterceptor>();
/*     */ 
/*     */   
/*  86 */   private final Map<Object, DeferredResultProcessingInterceptor> deferredResultInterceptors = new LinkedHashMap<Object, DeferredResultProcessingInterceptor>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsyncWebRequest(final AsyncWebRequest asyncWebRequest) {
/* 109 */     Assert.notNull(asyncWebRequest, "AsyncWebRequest must not be null");
/* 110 */     this.asyncWebRequest = asyncWebRequest;
/* 111 */     this.asyncWebRequest.addCompletionHandler(new Runnable()
/*     */         {
/*     */           public void run() {
/* 114 */             asyncWebRequest.removeAttribute(WebAsyncUtils.WEB_ASYNC_MANAGER_ATTRIBUTE, 0);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTaskExecutor(AsyncTaskExecutor taskExecutor) {
/* 125 */     this.taskExecutor = taskExecutor;
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
/*     */   public boolean isConcurrentHandlingStarted() {
/* 137 */     return (this.asyncWebRequest != null && this.asyncWebRequest.isAsyncStarted());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasConcurrentResult() {
/* 144 */     return (this.concurrentResult != RESULT_NONE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getConcurrentResult() {
/* 154 */     return this.concurrentResult;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getConcurrentResultContext() {
/* 163 */     return this.concurrentResultContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CallableProcessingInterceptor getCallableInterceptor(Object key) {
/* 172 */     return this.callableInterceptors.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeferredResultProcessingInterceptor getDeferredResultInterceptor(Object key) {
/* 181 */     return this.deferredResultInterceptors.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerCallableInterceptor(Object key, CallableProcessingInterceptor interceptor) {
/* 190 */     Assert.notNull(key, "Key is required");
/* 191 */     Assert.notNull(interceptor, "CallableProcessingInterceptor  is required");
/* 192 */     this.callableInterceptors.put(key, interceptor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerCallableInterceptors(CallableProcessingInterceptor... interceptors) {
/* 201 */     Assert.notNull(interceptors, "A CallableProcessingInterceptor is required");
/* 202 */     for (CallableProcessingInterceptor interceptor : interceptors) {
/* 203 */       String key = interceptor.getClass().getName() + ":" + interceptor.hashCode();
/* 204 */       this.callableInterceptors.put(key, interceptor);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerDeferredResultInterceptor(Object key, DeferredResultProcessingInterceptor interceptor) {
/* 214 */     Assert.notNull(key, "Key is required");
/* 215 */     Assert.notNull(interceptor, "DeferredResultProcessingInterceptor is required");
/* 216 */     this.deferredResultInterceptors.put(key, interceptor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerDeferredResultInterceptors(DeferredResultProcessingInterceptor... interceptors) {
/* 225 */     Assert.notNull(interceptors, "A DeferredResultProcessingInterceptor is required");
/* 226 */     for (DeferredResultProcessingInterceptor interceptor : interceptors) {
/* 227 */       String key = interceptor.getClass().getName() + ":" + interceptor.hashCode();
/* 228 */       this.deferredResultInterceptors.put(key, interceptor);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearConcurrentResult() {
/* 237 */     synchronized (this) {
/* 238 */       this.concurrentResult = RESULT_NONE;
/* 239 */       this.concurrentResultContext = null;
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
/*     */   
/*     */   public void startCallableProcessing(Callable<?> callable, Object... processingContext) throws Exception {
/* 258 */     Assert.notNull(callable, "Callable must not be null");
/* 259 */     startCallableProcessing(new WebAsyncTask(callable), processingContext);
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
/*     */   public void startCallableProcessing(WebAsyncTask<?> webAsyncTask, Object... processingContext) throws Exception {
/* 274 */     Assert.notNull(webAsyncTask, "WebAsyncTask must not be null");
/* 275 */     Assert.state((this.asyncWebRequest != null), "AsyncWebRequest must not be null");
/*     */     
/* 277 */     Long timeout = webAsyncTask.getTimeout();
/* 278 */     if (timeout != null) {
/* 279 */       this.asyncWebRequest.setTimeout(timeout);
/*     */     }
/*     */     
/* 282 */     AsyncTaskExecutor executor = webAsyncTask.getExecutor();
/* 283 */     if (executor != null) {
/* 284 */       this.taskExecutor = executor;
/*     */     }
/*     */     
/* 287 */     List<CallableProcessingInterceptor> interceptors = new ArrayList<CallableProcessingInterceptor>();
/* 288 */     interceptors.add(webAsyncTask.getInterceptor());
/* 289 */     interceptors.addAll(this.callableInterceptors.values());
/* 290 */     interceptors.add(timeoutCallableInterceptor);
/*     */     
/* 292 */     final Callable<?> callable = webAsyncTask.getCallable();
/* 293 */     final CallableInterceptorChain interceptorChain = new CallableInterceptorChain(interceptors);
/*     */     
/* 295 */     this.asyncWebRequest.addTimeoutHandler(new Runnable()
/*     */         {
/*     */           public void run() {
/* 298 */             WebAsyncManager.logger.debug("Processing timeout");
/* 299 */             Object result = interceptorChain.triggerAfterTimeout(WebAsyncManager.this.asyncWebRequest, callable);
/* 300 */             if (result != CallableProcessingInterceptor.RESULT_NONE) {
/* 301 */               WebAsyncManager.this.setConcurrentResultAndDispatch(result);
/*     */             }
/*     */           }
/*     */         });
/*     */     
/* 306 */     if (this.asyncWebRequest instanceof StandardServletAsyncWebRequest) {
/* 307 */       ((StandardServletAsyncWebRequest)this.asyncWebRequest).setErrorHandler(new StandardServletAsyncWebRequest.ErrorHandler()
/*     */           {
/*     */             public void handle(Throwable ex)
/*     */             {
/* 311 */               WebAsyncManager.this.setConcurrentResultAndDispatch(ex);
/*     */             }
/*     */           });
/*     */     }
/*     */     
/* 316 */     this.asyncWebRequest.addCompletionHandler(new Runnable()
/*     */         {
/*     */           public void run() {
/* 319 */             interceptorChain.triggerAfterCompletion(WebAsyncManager.this.asyncWebRequest, callable);
/*     */           }
/*     */         });
/*     */     
/* 323 */     interceptorChain.applyBeforeConcurrentHandling(this.asyncWebRequest, callable);
/* 324 */     startAsyncProcessing(processingContext);
/*     */     try {
/* 326 */       Future<?> future = this.taskExecutor.submit(new Runnable()
/*     */           {
/*     */             public void run() {
/* 329 */               Object result = null;
/*     */               try {
/* 331 */                 interceptorChain.applyPreProcess(WebAsyncManager.this.asyncWebRequest, callable);
/* 332 */                 result = callable.call();
/*     */               }
/* 334 */               catch (Throwable ex) {
/* 335 */                 result = ex;
/*     */               } finally {
/*     */                 
/* 338 */                 result = interceptorChain.applyPostProcess(WebAsyncManager.this.asyncWebRequest, callable, result);
/*     */               } 
/* 340 */               WebAsyncManager.this.setConcurrentResultAndDispatch(result);
/*     */             }
/*     */           });
/* 343 */       interceptorChain.setTaskFuture(future);
/*     */     }
/* 345 */     catch (RejectedExecutionException ex) {
/* 346 */       Object result = interceptorChain.applyPostProcess(this.asyncWebRequest, callable, ex);
/* 347 */       setConcurrentResultAndDispatch(result);
/* 348 */       throw ex;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setConcurrentResultAndDispatch(Object result) {
/* 353 */     synchronized (this) {
/* 354 */       if (this.concurrentResult != RESULT_NONE) {
/*     */         return;
/*     */       }
/* 357 */       this.concurrentResult = result;
/*     */     } 
/*     */     
/* 360 */     if (this.asyncWebRequest.isAsyncComplete()) {
/* 361 */       logger.error("Could not complete async processing due to timeout or network error");
/*     */       
/*     */       return;
/*     */     } 
/* 365 */     if (logger.isDebugEnabled()) {
/* 366 */       logger.debug("Concurrent result value [" + this.concurrentResult + "] - dispatching request to resume processing");
/*     */     }
/*     */     
/* 369 */     this.asyncWebRequest.dispatch();
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
/*     */   public void startDeferredResultProcessing(final DeferredResult<?> deferredResult, Object... processingContext) throws Exception {
/* 389 */     Assert.notNull(deferredResult, "DeferredResult must not be null");
/* 390 */     Assert.state((this.asyncWebRequest != null), "AsyncWebRequest must not be null");
/*     */     
/* 392 */     Long timeout = deferredResult.getTimeoutValue();
/* 393 */     if (timeout != null) {
/* 394 */       this.asyncWebRequest.setTimeout(timeout);
/*     */     }
/*     */     
/* 397 */     List<DeferredResultProcessingInterceptor> interceptors = new ArrayList<DeferredResultProcessingInterceptor>();
/* 398 */     interceptors.add(deferredResult.getInterceptor());
/* 399 */     interceptors.addAll(this.deferredResultInterceptors.values());
/* 400 */     interceptors.add(timeoutDeferredResultInterceptor);
/*     */     
/* 402 */     final DeferredResultInterceptorChain interceptorChain = new DeferredResultInterceptorChain(interceptors);
/*     */     
/* 404 */     this.asyncWebRequest.addTimeoutHandler(new Runnable()
/*     */         {
/*     */           public void run() {
/*     */             try {
/* 408 */               interceptorChain.triggerAfterTimeout(WebAsyncManager.this.asyncWebRequest, deferredResult);
/*     */             }
/* 410 */             catch (Throwable ex) {
/* 411 */               WebAsyncManager.this.setConcurrentResultAndDispatch(ex);
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 416 */     if (this.asyncWebRequest instanceof StandardServletAsyncWebRequest) {
/* 417 */       ((StandardServletAsyncWebRequest)this.asyncWebRequest).setErrorHandler(new StandardServletAsyncWebRequest.ErrorHandler()
/*     */           {
/*     */             public void handle(Throwable ex)
/*     */             {
/* 421 */               deferredResult.setErrorResult(ex);
/*     */             }
/*     */           });
/*     */     }
/*     */     
/* 426 */     this.asyncWebRequest.addCompletionHandler(new Runnable()
/*     */         {
/*     */           public void run() {
/* 429 */             interceptorChain.triggerAfterCompletion(WebAsyncManager.this.asyncWebRequest, deferredResult);
/*     */           }
/*     */         });
/*     */     
/* 433 */     interceptorChain.applyBeforeConcurrentHandling(this.asyncWebRequest, deferredResult);
/* 434 */     startAsyncProcessing(processingContext);
/*     */     
/*     */     try {
/* 437 */       interceptorChain.applyPreProcess(this.asyncWebRequest, deferredResult);
/* 438 */       deferredResult.setResultHandler(new DeferredResult.DeferredResultHandler()
/*     */           {
/*     */             public void handleResult(Object result) {
/* 441 */               result = interceptorChain.applyPostProcess(WebAsyncManager.this.asyncWebRequest, deferredResult, result);
/* 442 */               WebAsyncManager.this.setConcurrentResultAndDispatch(result);
/*     */             }
/*     */           });
/*     */     }
/* 446 */     catch (Throwable ex) {
/* 447 */       setConcurrentResultAndDispatch(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void startAsyncProcessing(Object[] processingContext) {
/* 452 */     synchronized (this) {
/* 453 */       this.concurrentResult = RESULT_NONE;
/* 454 */       this.concurrentResultContext = processingContext;
/*     */     } 
/* 456 */     this.asyncWebRequest.startAsync();
/*     */     
/* 458 */     if (logger.isDebugEnabled()) {
/* 459 */       HttpServletRequest request = (HttpServletRequest)this.asyncWebRequest.getNativeRequest(HttpServletRequest.class);
/* 460 */       String requestUri = urlPathHelper.getRequestUri(request);
/* 461 */       logger.debug("Concurrent handling starting for " + request.getMethod() + " [" + requestUri + "]");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\async\WebAsyncManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */