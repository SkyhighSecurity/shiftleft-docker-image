/*     */ package org.springframework.util.concurrent;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.Queue;
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
/*     */ 
/*     */ public class ListenableFutureCallbackRegistry<T>
/*     */ {
/*  37 */   private final Queue<SuccessCallback<? super T>> successCallbacks = new LinkedList<SuccessCallback<? super T>>();
/*     */   
/*  39 */   private final Queue<FailureCallback> failureCallbacks = new LinkedList<FailureCallback>();
/*     */   
/*  41 */   private State state = State.NEW;
/*     */   
/*  43 */   private Object result = null;
/*     */   
/*  45 */   private final Object mutex = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCallback(ListenableFutureCallback<? super T> callback) {
/*  53 */     Assert.notNull(callback, "'callback' must not be null");
/*  54 */     synchronized (this.mutex) {
/*  55 */       switch (this.state) {
/*     */         case NEW:
/*  57 */           this.successCallbacks.add(callback);
/*  58 */           this.failureCallbacks.add(callback);
/*     */           break;
/*     */         case SUCCESS:
/*  61 */           notifySuccess(callback);
/*     */           break;
/*     */         case FAILURE:
/*  64 */           notifyFailure(callback);
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void notifySuccess(SuccessCallback<? super T> callback) {
/*     */     try {
/*  73 */       callback.onSuccess((T)this.result);
/*     */     }
/*  75 */     catch (Throwable throwable) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void notifyFailure(FailureCallback callback) {
/*     */     try {
/*  82 */       callback.onFailure((Throwable)this.result);
/*     */     }
/*  84 */     catch (Throwable throwable) {}
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
/*     */   public void addSuccessCallback(SuccessCallback<? super T> callback) {
/*  96 */     Assert.notNull(callback, "'callback' must not be null");
/*  97 */     synchronized (this.mutex) {
/*  98 */       switch (this.state) {
/*     */         case NEW:
/* 100 */           this.successCallbacks.add(callback);
/*     */           break;
/*     */         case SUCCESS:
/* 103 */           notifySuccess(callback);
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFailureCallback(FailureCallback callback) {
/* 115 */     Assert.notNull(callback, "'callback' must not be null");
/* 116 */     synchronized (this.mutex) {
/* 117 */       switch (this.state) {
/*     */         case NEW:
/* 119 */           this.failureCallbacks.add(callback);
/*     */           break;
/*     */         case FAILURE:
/* 122 */           notifyFailure(callback);
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void success(T result) {
/* 134 */     synchronized (this.mutex) {
/* 135 */       this.state = State.SUCCESS;
/* 136 */       this.result = result;
/*     */       SuccessCallback<? super T> callback;
/* 138 */       while ((callback = this.successCallbacks.poll()) != null) {
/* 139 */         notifySuccess(callback);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void failure(Throwable ex) {
/* 150 */     synchronized (this.mutex) {
/* 151 */       this.state = State.FAILURE;
/* 152 */       this.result = ex;
/*     */       FailureCallback callback;
/* 154 */       while ((callback = this.failureCallbacks.poll()) != null)
/* 155 */         notifyFailure(callback); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private enum State
/*     */   {
/* 161 */     NEW, SUCCESS, FAILURE;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\concurrent\ListenableFutureCallbackRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */