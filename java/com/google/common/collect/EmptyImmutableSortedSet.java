/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ class EmptyImmutableSortedSet<E>
/*     */   extends ImmutableSortedSet<E>
/*     */ {
/*     */   EmptyImmutableSortedSet(Comparator<? super E> comparator) {
/*  37 */     super(comparator);
/*     */   }
/*     */   
/*     */   public int size() {
/*  41 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  45 */     return true;
/*     */   }
/*     */   
/*     */   public boolean contains(Object target) {
/*  49 */     return false;
/*     */   }
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/*  53 */     return Iterators.emptyIterator();
/*     */   }
/*     */   
/*  56 */   private static final Object[] EMPTY_ARRAY = new Object[0];
/*     */   
/*     */   public Object[] toArray() {
/*  59 */     return EMPTY_ARRAY;
/*     */   }
/*     */   
/*     */   public <T> T[] toArray(T[] a) {
/*  63 */     if (a.length > 0) {
/*  64 */       a[0] = null;
/*     */     }
/*  66 */     return a;
/*     */   }
/*     */   
/*     */   public boolean containsAll(Collection<?> targets) {
/*  70 */     return targets.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/*  74 */     if (object instanceof Set) {
/*  75 */       Set<?> that = (Set)object;
/*  76 */       return that.isEmpty();
/*     */     } 
/*  78 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  82 */     return 0;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  86 */     return "[]";
/*     */   }
/*     */   
/*     */   public E first() {
/*  90 */     throw new NoSuchElementException();
/*     */   }
/*     */   
/*     */   public E last() {
/*  94 */     throw new NoSuchElementException();
/*     */   }
/*     */   
/*     */   ImmutableSortedSet<E> headSetImpl(E toElement) {
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   ImmutableSortedSet<E> subSetImpl(E fromElement, E toElement) {
/* 102 */     return this;
/*     */   }
/*     */   
/*     */   ImmutableSortedSet<E> tailSetImpl(E fromElement) {
/* 106 */     return this;
/*     */   }
/*     */   
/*     */   boolean hasPartialArray() {
/* 110 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\EmptyImmutableSortedSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */