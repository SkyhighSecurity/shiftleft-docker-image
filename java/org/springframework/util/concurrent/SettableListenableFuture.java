/*     */ package org.springframework.util.concurrent;
/*     */ 
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ public class SettableListenableFuture<T>
/*     */   implements ListenableFuture<T>
/*     */ {
/*  39 */   private static final Callable<Object> DUMMY_CALLABLE = new Callable()
/*     */     {
/*     */       public Object call() throws Exception {
/*  42 */         throw new IllegalStateException("Should never be called");
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  47 */   private final SettableTask<T> settableTask = new SettableTask<T>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean set(T value) {
/*  58 */     return this.settableTask.setResultValue(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setException(Throwable exception) {
/*  69 */     Assert.notNull(exception, "Exception must not be null");
/*  70 */     return this.settableTask.setExceptionResult(exception);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCallback(ListenableFutureCallback<? super T> callback) {
/*  75 */     this.settableTask.addCallback(callback);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCallback(SuccessCallback<? super T> successCallback, FailureCallback failureCallback) {
/*  80 */     this.settableTask.addCallback(successCallback, failureCallback);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/*  85 */     boolean cancelled = this.settableTask.cancel(mayInterruptIfRunning);
/*  86 */     if (cancelled && mayInterruptIfRunning) {
/*  87 */       interruptTask();
/*     */     }
/*  89 */     return cancelled;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/*  94 */     return this.settableTask.isCancelled();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/*  99 */     return this.settableTask.isDone();
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
/*     */   public T get() throws InterruptedException, ExecutionException {
/* 112 */     return this.settableTask.get();
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
/*     */   public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/* 127 */     return this.settableTask.get(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void interruptTask() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SettableTask<T>
/*     */     extends ListenableFutureTask<T>
/*     */   {
/*     */     private volatile Thread completingThread;
/*     */ 
/*     */ 
/*     */     
/*     */     public SettableTask() {
/* 146 */       super((Callable)SettableListenableFuture.DUMMY_CALLABLE);
/*     */     }
/*     */     
/*     */     public boolean setResultValue(T value) {
/* 150 */       set(value);
/* 151 */       return checkCompletingThread();
/*     */     }
/*     */     
/*     */     public boolean setExceptionResult(Throwable exception) {
/* 155 */       setException(exception);
/* 156 */       return checkCompletingThread();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void done() {
/* 161 */       if (!isCancelled())
/*     */       {
/*     */ 
/*     */         
/* 165 */         this.completingThread = Thread.currentThread();
/*     */       }
/* 167 */       super.done();
/*     */     }
/*     */     
/*     */     private boolean checkCompletingThread() {
/* 171 */       boolean check = (this.completingThread == Thread.currentThread());
/* 172 */       if (check) {
/* 173 */         this.completingThread = null;
/*     */       }
/* 175 */       return check;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\concurrent\SettableListenableFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */