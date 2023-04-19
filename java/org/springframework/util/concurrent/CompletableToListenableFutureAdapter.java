/*     */ package org.springframework.util.concurrent;
/*     */ 
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.function.BiFunction;
/*     */ import org.springframework.lang.UsesJava8;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @UsesJava8
/*     */ public class CompletableToListenableFutureAdapter<T>
/*     */   implements ListenableFuture<T>
/*     */ {
/*     */   private final CompletableFuture<T> completableFuture;
/*  41 */   private final ListenableFutureCallbackRegistry<T> callbacks = new ListenableFutureCallbackRegistry<T>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableToListenableFutureAdapter(CompletionStage<T> completionStage) {
/*  49 */     this(completionStage.toCompletableFuture());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableToListenableFutureAdapter(CompletableFuture<T> completableFuture) {
/*  56 */     this.completableFuture = completableFuture;
/*  57 */     this.completableFuture.handle(new BiFunction<T, Throwable, Object>()
/*     */         {
/*     */           public Object apply(T result, Throwable ex) {
/*  60 */             if (ex != null) {
/*  61 */               CompletableToListenableFutureAdapter.this.callbacks.failure(ex);
/*     */             } else {
/*     */               
/*  64 */               CompletableToListenableFutureAdapter.this.callbacks.success(result);
/*     */             } 
/*  66 */             return null;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCallback(ListenableFutureCallback<? super T> callback) {
/*  74 */     this.callbacks.addCallback(callback);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCallback(SuccessCallback<? super T> successCallback, FailureCallback failureCallback) {
/*  79 */     this.callbacks.addSuccessCallback(successCallback);
/*  80 */     this.callbacks.addFailureCallback(failureCallback);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/*  85 */     return this.completableFuture.cancel(mayInterruptIfRunning);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/*  90 */     return this.completableFuture.isCancelled();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/*  95 */     return this.completableFuture.isDone();
/*     */   }
/*     */ 
/*     */   
/*     */   public T get() throws InterruptedException, ExecutionException {
/* 100 */     return this.completableFuture.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/* 105 */     return this.completableFuture.get(timeout, unit);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\concurrent\CompletableToListenableFutureAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */