/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ final class SingletonImmutableSet<E>
/*     */   extends ImmutableSet<E>
/*     */ {
/*     */   final transient E element;
/*     */   private transient Integer cachedHashCode;
/*     */   
/*     */   SingletonImmutableSet(E element) {
/*  43 */     this.element = (E)Preconditions.checkNotNull(element);
/*     */   }
/*     */ 
/*     */   
/*     */   SingletonImmutableSet(E element, int hashCode) {
/*  48 */     this.element = element;
/*  49 */     this.cachedHashCode = Integer.valueOf(hashCode);
/*     */   }
/*     */   
/*     */   public int size() {
/*  53 */     return 1;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  57 */     return false;
/*     */   }
/*     */   
/*     */   public boolean contains(Object target) {
/*  61 */     return this.element.equals(target);
/*     */   }
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/*  65 */     return Iterators.singletonIterator(this.element);
/*     */   }
/*     */   
/*     */   public Object[] toArray() {
/*  69 */     return new Object[] { this.element };
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] array) {
/*  74 */     if (array.length == 0) {
/*  75 */       array = ObjectArrays.newArray(array, 1);
/*  76 */     } else if (array.length > 1) {
/*  77 */       array[1] = null;
/*     */     } 
/*     */     
/*  80 */     T[] arrayOfT = array;
/*  81 */     arrayOfT[0] = (T)this.element;
/*  82 */     return array;
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/*  86 */     if (object == this) {
/*  87 */       return true;
/*     */     }
/*  89 */     if (object instanceof Set) {
/*  90 */       Set<?> that = (Set)object;
/*  91 */       return (that.size() == 1 && this.element.equals(that.iterator().next()));
/*     */     } 
/*  93 */     return false;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*  97 */     Integer code = this.cachedHashCode;
/*  98 */     if (code == null) {
/*  99 */       return (this.cachedHashCode = Integer.valueOf(this.element.hashCode())).intValue();
/*     */     }
/* 101 */     return code.intValue();
/*     */   }
/*     */   
/*     */   boolean isHashCodeFast() {
/* 105 */     return false;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 109 */     String elementToString = this.element.toString();
/* 110 */     return (new StringBuilder(elementToString.length() + 2)).append('[').append(elementToString).append(']').toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\SingletonImmutableSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */