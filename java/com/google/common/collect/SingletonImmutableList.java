/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
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
/*     */ final class SingletonImmutableList<E>
/*     */   extends ImmutableList<E>
/*     */ {
/*     */   final transient E element;
/*     */   
/*     */   SingletonImmutableList(E element) {
/*  40 */     this.element = (E)Preconditions.checkNotNull(element);
/*     */   }
/*     */   
/*     */   public E get(int index) {
/*  44 */     Preconditions.checkElementIndex(index, 1);
/*  45 */     return this.element;
/*     */   }
/*     */   
/*     */   public int indexOf(@Nullable Object object) {
/*  49 */     return this.element.equals(object) ? 0 : -1;
/*     */   }
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/*  53 */     return Iterators.singletonIterator(this.element);
/*     */   }
/*     */   
/*     */   public int lastIndexOf(@Nullable Object object) {
/*  57 */     return this.element.equals(object) ? 0 : -1;
/*     */   }
/*     */   
/*     */   public ListIterator<E> listIterator() {
/*  61 */     return listIterator(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator(int start) {
/*  66 */     return Collections.<E>singletonList(this.element).listIterator(start);
/*     */   }
/*     */   
/*     */   public int size() {
/*  70 */     return 1;
/*     */   }
/*     */   
/*     */   public ImmutableList<E> subList(int fromIndex, int toIndex) {
/*  74 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, 1);
/*  75 */     return (fromIndex == toIndex) ? ImmutableList.<E>of() : this;
/*     */   }
/*     */   
/*     */   public boolean contains(@Nullable Object object) {
/*  79 */     return this.element.equals(object);
/*     */   }
/*     */   
/*     */   public boolean equals(Object object) {
/*  83 */     if (object == this) {
/*  84 */       return true;
/*     */     }
/*  86 */     if (object instanceof List) {
/*  87 */       List<?> that = (List)object;
/*  88 */       return (that.size() == 1 && this.element.equals(that.get(0)));
/*     */     } 
/*  90 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  96 */     return 31 + this.element.hashCode();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 100 */     return false;
/*     */   }
/*     */   
/*     */   public Object[] toArray() {
/* 104 */     return new Object[] { this.element };
/*     */   }
/*     */   
/*     */   public <T> T[] toArray(T[] array) {
/* 108 */     if (array.length == 0) {
/* 109 */       array = ObjectArrays.newArray(array, 1);
/* 110 */     } else if (array.length > 1) {
/* 111 */       array[1] = null;
/*     */     } 
/*     */     
/* 114 */     T[] arrayOfT = array;
/* 115 */     arrayOfT[0] = (T)this.element;
/* 116 */     return array;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\SingletonImmutableList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */