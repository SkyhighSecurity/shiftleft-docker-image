/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ final class EmptyImmutableList
/*     */   extends ImmutableList<Object>
/*     */ {
/*  38 */   static final EmptyImmutableList INSTANCE = new EmptyImmutableList();
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  43 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  47 */     return true;
/*     */   }
/*     */   
/*     */   public boolean contains(Object target) {
/*  51 */     return false;
/*     */   }
/*     */   
/*     */   public UnmodifiableIterator<Object> iterator() {
/*  55 */     return Iterators.emptyIterator();
/*     */   }
/*     */   
/*  58 */   private static final Object[] EMPTY_ARRAY = new Object[0];
/*     */   
/*     */   public Object[] toArray() {
/*  61 */     return EMPTY_ARRAY;
/*     */   }
/*     */   private static final long serialVersionUID = 0L;
/*     */   public <T> T[] toArray(T[] a) {
/*  65 */     if (a.length > 0) {
/*  66 */       a[0] = null;
/*     */     }
/*  68 */     return a;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object get(int index) {
/*  73 */     Preconditions.checkElementIndex(index, 0);
/*  74 */     throw new AssertionError("unreachable");
/*     */   }
/*     */   
/*     */   public int indexOf(Object target) {
/*  78 */     return -1;
/*     */   }
/*     */   
/*     */   public int lastIndexOf(Object target) {
/*  82 */     return -1;
/*     */   }
/*     */   
/*     */   public ImmutableList<Object> subList(int fromIndex, int toIndex) {
/*  86 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, 0);
/*  87 */     return this;
/*     */   }
/*     */   
/*     */   public ListIterator<Object> listIterator() {
/*  91 */     return Collections.<Object>emptyList().listIterator();
/*     */   }
/*     */   
/*     */   public ListIterator<Object> listIterator(int start) {
/*  95 */     Preconditions.checkPositionIndex(start, 0);
/*  96 */     return Collections.<Object>emptyList().listIterator();
/*     */   }
/*     */   
/*     */   public boolean containsAll(Collection<?> targets) {
/* 100 */     return targets.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 104 */     if (object instanceof List) {
/* 105 */       List<?> that = (List)object;
/* 106 */       return that.isEmpty();
/*     */     } 
/* 108 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 112 */     return 1;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 116 */     return "[]";
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 120 */     return INSTANCE;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\EmptyImmutableList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */