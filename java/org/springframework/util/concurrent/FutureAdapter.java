/*     */ package org.springframework.util.concurrent;
/*     */ 
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
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
/*     */ public abstract class FutureAdapter<T, S>
/*     */   implements Future<T>
/*     */ {
/*     */   private final Future<S> adaptee;
/*  40 */   private Object result = null;
/*     */   
/*  42 */   private State state = State.NEW;
/*     */   
/*  44 */   private final Object mutex = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FutureAdapter(Future<S> adaptee) {
/*  52 */     Assert.notNull(adaptee, "Delegate must not be null");
/*  53 */     this.adaptee = adaptee;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Future<S> getAdaptee() {
/*  61 */     return this.adaptee;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/*  66 */     return this.adaptee.cancel(mayInterruptIfRunning);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/*  71 */     return this.adaptee.isCancelled();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/*  76 */     return this.adaptee.isDone();
/*     */   }
/*     */ 
/*     */   
/*     */   public T get() throws InterruptedException, ExecutionException {
/*  81 */     return adaptInternal(this.adaptee.get());
/*     */   }
/*     */ 
/*     */   
/*     */   public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*  86 */     return adaptInternal(this.adaptee.get(timeout, unit));
/*     */   }
/*     */ 
/*     */   
/*     */   final T adaptInternal(S adapteeResult) throws ExecutionException {
/*  91 */     synchronized (this.mutex) {
/*  92 */       switch (this.state) {
/*     */         case SUCCESS:
/*  94 */           return (T)this.result;
/*     */         case FAILURE:
/*  96 */           throw (ExecutionException)this.result;
/*     */         case NEW:
/*     */           try {
/*  99 */             T adapted = adapt(adapteeResult);
/* 100 */             this.result = adapted;
/* 101 */             this.state = State.SUCCESS;
/* 102 */             return adapted;
/*     */           }
/* 104 */           catch (ExecutionException ex) {
/* 105 */             this.result = ex;
/* 106 */             this.state = State.FAILURE;
/* 107 */             throw ex;
/*     */           }
/* 109 */           catch (Throwable ex) {
/* 110 */             ExecutionException execEx = new ExecutionException(ex);
/* 111 */             this.result = execEx;
/* 112 */             this.state = State.FAILURE;
/* 113 */             throw execEx;
/*     */           } 
/*     */       } 
/* 116 */       throw new IllegalStateException();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract T adapt(S paramS) throws ExecutionException;
/*     */ 
/*     */ 
/*     */   
/*     */   private enum State
/*     */   {
/* 128 */     NEW, SUCCESS, FAILURE;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\concurrent\FutureAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */