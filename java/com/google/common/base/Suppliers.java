/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.Serializable;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Suppliers
/*     */ {
/*     */   public static <F, T> Supplier<T> compose(Function<? super F, T> function, Supplier<F> first) {
/*  46 */     Preconditions.checkNotNull(function);
/*  47 */     Preconditions.checkNotNull(first);
/*  48 */     return new SupplierComposition<F, T>(function, first);
/*     */   }
/*     */   
/*     */   private static class SupplierComposition<F, T>
/*     */     implements Supplier<T>, Serializable {
/*     */     final Function<? super F, ? extends T> function;
/*     */     final Supplier<? extends F> first;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SupplierComposition(Function<? super F, ? extends T> function, Supplier<? extends F> first) {
/*  58 */       this.function = function;
/*  59 */       this.first = first;
/*     */     }
/*     */     public T get() {
/*  62 */       return this.function.apply(this.first.get());
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
/*     */   public static <T> Supplier<T> memoize(Supplier<T> delegate) {
/*  78 */     return new MemoizingSupplier<T>(Preconditions.<Supplier<T>>checkNotNull(delegate));
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static class MemoizingSupplier<T> implements Supplier<T>, Serializable { final Supplier<T> delegate;
/*     */     transient boolean initialized;
/*     */     transient T value;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     MemoizingSupplier(Supplier<T> delegate) {
/*  88 */       this.delegate = delegate;
/*     */     }
/*     */     
/*     */     public synchronized T get() {
/*  92 */       if (!this.initialized) {
/*  93 */         this.value = this.delegate.get();
/*  94 */         this.initialized = true;
/*     */       } 
/*  96 */       return this.value;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Supplier<T> ofInstance(@Nullable T instance) {
/* 106 */     return new SupplierOfInstance<T>(instance);
/*     */   }
/*     */   
/*     */   private static class SupplierOfInstance<T> implements Supplier<T>, Serializable {
/*     */     final T instance;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SupplierOfInstance(T instance) {
/* 114 */       this.instance = instance;
/*     */     }
/*     */     public T get() {
/* 117 */       return this.instance;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Supplier<T> synchronizedSupplier(Supplier<T> delegate) {
/* 127 */     return new ThreadSafeSupplier<T>(Preconditions.<Supplier<T>>checkNotNull(delegate));
/*     */   }
/*     */   
/*     */   private static class ThreadSafeSupplier<T> implements Supplier<T>, Serializable {
/*     */     final Supplier<T> delegate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ThreadSafeSupplier(Supplier<T> delegate) {
/* 135 */       this.delegate = delegate;
/*     */     }
/*     */     public T get() {
/* 138 */       synchronized (this.delegate) {
/* 139 */         return this.delegate.get();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\base\Suppliers.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */