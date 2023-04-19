/*     */ package org.springframework.scheduling.annotation;
/*     */ 
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.springframework.util.concurrent.FailureCallback;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
/*     */ import org.springframework.util.concurrent.ListenableFutureCallback;
/*     */ import org.springframework.util.concurrent.SuccessCallback;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsyncResult<V>
/*     */   implements ListenableFuture<V>
/*     */ {
/*     */   private final V value;
/*     */   private final ExecutionException executionException;
/*     */   
/*     */   public AsyncResult(V value) {
/*  58 */     this(value, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AsyncResult(V value, ExecutionException ex) {
/*  66 */     this.value = value;
/*  67 */     this.executionException = ex;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/*  73 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/*  78 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/*  83 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public V get() throws ExecutionException {
/*  88 */     if (this.executionException != null) {
/*  89 */       throw this.executionException;
/*     */     }
/*  91 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(long timeout, TimeUnit unit) throws ExecutionException {
/*  96 */     return get();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCallback(ListenableFutureCallback<? super V> callback) {
/* 101 */     addCallback((SuccessCallback<? super V>)callback, (FailureCallback)callback);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCallback(SuccessCallback<? super V> successCallback, FailureCallback failureCallback) {
/*     */     try {
/* 107 */       if (this.executionException != null) {
/* 108 */         Throwable cause = this.executionException.getCause();
/* 109 */         failureCallback.onFailure((cause != null) ? cause : this.executionException);
/*     */       } else {
/*     */         
/* 112 */         successCallback.onSuccess(this.value);
/*     */       }
/*     */     
/* 115 */     } catch (Throwable throwable) {}
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
/*     */   public static <V> ListenableFuture<V> forValue(V value) {
/* 128 */     return new AsyncResult<V>(value, null);
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
/*     */   public static <V> ListenableFuture<V> forExecutionException(Throwable ex) {
/* 140 */     return new AsyncResult<V>(null, (ex instanceof ExecutionException) ? (ExecutionException)ex : new ExecutionException(ex));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\annotation\AsyncResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */