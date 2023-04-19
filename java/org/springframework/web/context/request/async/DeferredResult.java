/*     */ package org.springframework.web.context.request.async;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DeferredResult<T>
/*     */ {
/*  52 */   private static final Object RESULT_NONE = new Object();
/*     */   
/*  54 */   private static final Log logger = LogFactory.getLog(DeferredResult.class);
/*     */ 
/*     */   
/*     */   private final Long timeout;
/*     */   
/*     */   private final Object timeoutResult;
/*     */   
/*     */   private Runnable timeoutCallback;
/*     */   
/*     */   private Runnable completionCallback;
/*     */   
/*     */   private DeferredResultHandler resultHandler;
/*     */   
/*  67 */   private volatile Object result = RESULT_NONE;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean expired = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public DeferredResult() {
/*  76 */     this(null, RESULT_NONE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeferredResult(Long timeout) {
/*  87 */     this(timeout, RESULT_NONE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeferredResult(Long timeout, Object timeoutResult) {
/*  97 */     this.timeoutResult = timeoutResult;
/*  98 */     this.timeout = timeout;
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
/*     */   public final boolean isSetOrExpired() {
/* 111 */     return (this.result != RESULT_NONE || this.expired);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasResult() {
/* 119 */     return (this.result != RESULT_NONE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getResult() {
/* 129 */     Object resultToCheck = this.result;
/* 130 */     return (resultToCheck != RESULT_NONE) ? resultToCheck : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final Long getTimeoutValue() {
/* 137 */     return this.timeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onTimeout(Runnable callback) {
/* 148 */     this.timeoutCallback = callback;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onCompletion(Runnable callback) {
/* 158 */     this.completionCallback = callback;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setResultHandler(DeferredResultHandler resultHandler) {
/*     */     Object resultToHandle;
/* 167 */     Assert.notNull(resultHandler, "DeferredResultHandler is required");
/*     */     
/* 169 */     if (this.expired) {
/*     */       return;
/*     */     }
/*     */     
/* 173 */     synchronized (this) {
/*     */       
/* 175 */       if (this.expired) {
/*     */         return;
/*     */       }
/* 178 */       resultToHandle = this.result;
/* 179 */       if (resultToHandle == RESULT_NONE) {
/*     */         
/* 181 */         this.resultHandler = resultHandler;
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 189 */       resultHandler.handleResult(resultToHandle);
/*     */     }
/* 191 */     catch (Throwable ex) {
/* 192 */       logger.debug("Failed to handle existing result", ex);
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
/*     */   public boolean setResult(T result) {
/* 204 */     return setResultInternal(result);
/*     */   }
/*     */   
/*     */   private boolean setResultInternal(Object result) {
/*     */     DeferredResultHandler resultHandlerToUse;
/* 209 */     if (isSetOrExpired()) {
/* 210 */       return false;
/*     */     }
/*     */     
/* 213 */     synchronized (this) {
/*     */       
/* 215 */       if (isSetOrExpired()) {
/* 216 */         return false;
/*     */       }
/*     */       
/* 219 */       this.result = result;
/* 220 */       resultHandlerToUse = this.resultHandler;
/* 221 */       if (resultHandlerToUse == null)
/*     */       {
/*     */         
/* 224 */         return true;
/*     */       }
/*     */ 
/*     */       
/* 228 */       this.resultHandler = null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 233 */     resultHandlerToUse.handleResult(result);
/* 234 */     return true;
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
/*     */   public boolean setErrorResult(Object result) {
/* 248 */     return setResultInternal(result);
/*     */   }
/*     */ 
/*     */   
/*     */   final DeferredResultProcessingInterceptor getInterceptor() {
/* 253 */     return new DeferredResultProcessingInterceptorAdapter()
/*     */       {
/*     */         public <S> boolean handleTimeout(NativeWebRequest request, DeferredResult<S> deferredResult) {
/* 256 */           boolean continueProcessing = true;
/*     */           try {
/* 258 */             if (DeferredResult.this.timeoutCallback != null) {
/* 259 */               DeferredResult.this.timeoutCallback.run();
/*     */             }
/*     */           } finally {
/*     */             
/* 263 */             if (DeferredResult.this.timeoutResult != DeferredResult.RESULT_NONE) {
/* 264 */               continueProcessing = false;
/*     */               try {
/* 266 */                 DeferredResult.this.setResultInternal(DeferredResult.this.timeoutResult);
/*     */               }
/* 268 */               catch (Throwable ex) {
/* 269 */                 DeferredResult.logger.debug("Failed to handle timeout result", ex);
/*     */               } 
/*     */             } 
/*     */           } 
/* 273 */           return continueProcessing;
/*     */         }
/*     */         
/*     */         public <S> void afterCompletion(NativeWebRequest request, DeferredResult<S> deferredResult) {
/* 277 */           DeferredResult.this.expired = true;
/* 278 */           if (DeferredResult.this.completionCallback != null)
/* 279 */             DeferredResult.this.completionCallback.run(); 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static interface DeferredResultHandler {
/*     */     void handleResult(Object param1Object);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\async\DeferredResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */