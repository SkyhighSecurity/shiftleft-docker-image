/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractList;
/*     */ import java.util.AbstractSequentialList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.RandomAccess;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class Lists
/*     */ {
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <E> ArrayList<E> newArrayList() {
/*  64 */     return new ArrayList<E>();
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <E> ArrayList<E> newArrayList(E... elements) {
/*  79 */     Preconditions.checkNotNull(elements);
/*     */     
/*  81 */     int capacity = computeArrayListCapacity(elements.length);
/*  82 */     ArrayList<E> list = new ArrayList<E>(capacity);
/*  83 */     Collections.addAll(list, elements);
/*  84 */     return list;
/*     */   }
/*     */   @VisibleForTesting
/*     */   static int computeArrayListCapacity(int arraySize) {
/*  88 */     Preconditions.checkArgument((arraySize >= 0));
/*     */ 
/*     */     
/*  91 */     return (int)Math.min(5L + arraySize + (arraySize / 10), 2147483647L);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
/* 106 */     Preconditions.checkNotNull(elements);
/*     */     
/* 108 */     if (elements instanceof Collection) {
/*     */       
/* 110 */       Collection<? extends E> collection = (Collection<? extends E>)elements;
/* 111 */       return new ArrayList<E>(collection);
/*     */     } 
/* 113 */     return newArrayList(elements.iterator());
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
/* 129 */     Preconditions.checkNotNull(elements);
/* 130 */     ArrayList<E> list = newArrayList();
/* 131 */     while (elements.hasNext()) {
/* 132 */       list.add(elements.next());
/*     */     }
/* 134 */     return list;
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <E> ArrayList<E> newArrayListWithCapacity(int initialArraySize) {
/* 160 */     return new ArrayList<E>(initialArraySize);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <E> ArrayList<E> newArrayListWithExpectedSize(int estimatedSize) {
/* 181 */     return new ArrayList<E>(computeArrayListCapacity(estimatedSize));
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <E> LinkedList<E> newLinkedList() {
/* 196 */     return new LinkedList<E>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <E> LinkedList<E> newLinkedList(Iterable<? extends E> elements) {
/* 208 */     LinkedList<E> list = newLinkedList();
/* 209 */     for (E element : elements) {
/* 210 */       list.add(element);
/*     */     }
/* 212 */     return list;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> List<E> asList(@Nullable E first, E[] rest) {
/* 232 */     return new OnePlusArrayList<E>(first, rest);
/*     */   }
/*     */   
/*     */   private static class OnePlusArrayList<E>
/*     */     extends AbstractList<E> implements Serializable, RandomAccess {
/*     */     final E first;
/*     */     final E[] rest;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     OnePlusArrayList(@Nullable E first, E[] rest) {
/* 242 */       this.first = first;
/* 243 */       this.rest = (E[])Preconditions.checkNotNull(rest);
/*     */     }
/*     */     public int size() {
/* 246 */       return this.rest.length + 1;
/*     */     }
/*     */     
/*     */     public E get(int index) {
/* 250 */       Preconditions.checkElementIndex(index, size());
/* 251 */       return (index == 0) ? this.first : this.rest[index - 1];
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> List<E> asList(@Nullable E first, @Nullable E second, E[] rest) {
/* 275 */     return new TwoPlusArrayList<E>(first, second, rest);
/*     */   }
/*     */   
/*     */   private static class TwoPlusArrayList<E>
/*     */     extends AbstractList<E> implements Serializable, RandomAccess {
/*     */     final E first;
/*     */     final E second;
/*     */     final E[] rest;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     TwoPlusArrayList(@Nullable E first, @Nullable E second, E[] rest) {
/* 286 */       this.first = first;
/* 287 */       this.second = second;
/* 288 */       this.rest = (E[])Preconditions.checkNotNull(rest);
/*     */     }
/*     */     public int size() {
/* 291 */       return this.rest.length + 2;
/*     */     }
/*     */     public E get(int index) {
/* 294 */       switch (index) {
/*     */         case 0:
/* 296 */           return this.first;
/*     */         case 1:
/* 298 */           return this.second;
/*     */       } 
/*     */       
/* 301 */       Preconditions.checkElementIndex(index, size());
/* 302 */       return this.rest[index - 2];
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
/*     */   public static <F, T> List<T> transform(List<F> fromList, Function<? super F, ? extends T> function) {
/* 334 */     return (fromList instanceof RandomAccess) ? new TransformingRandomAccessList<F, T>(fromList, function) : new TransformingSequentialList<F, T>(fromList, function);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class TransformingSequentialList<F, T>
/*     */     extends AbstractSequentialList<T>
/*     */     implements Serializable
/*     */   {
/*     */     final List<F> fromList;
/*     */     
/*     */     final Function<? super F, ? extends T> function;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     
/*     */     TransformingSequentialList(List<F> fromList, Function<? super F, ? extends T> function) {
/* 351 */       this.fromList = (List<F>)Preconditions.checkNotNull(fromList);
/* 352 */       this.function = (Function<? super F, ? extends T>)Preconditions.checkNotNull(function);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void clear() {
/* 360 */       this.fromList.clear();
/*     */     }
/*     */     public int size() {
/* 363 */       return this.fromList.size();
/*     */     }
/*     */     public ListIterator<T> listIterator(int index) {
/* 366 */       final ListIterator<F> delegate = this.fromList.listIterator(index);
/* 367 */       return new ListIterator<T>() {
/*     */           public void add(T e) {
/* 369 */             throw new UnsupportedOperationException();
/*     */           }
/*     */           
/*     */           public boolean hasNext() {
/* 373 */             return delegate.hasNext();
/*     */           }
/*     */           
/*     */           public boolean hasPrevious() {
/* 377 */             return delegate.hasPrevious();
/*     */           }
/*     */           
/*     */           public T next() {
/* 381 */             return (T)Lists.TransformingSequentialList.this.function.apply(delegate.next());
/*     */           }
/*     */           
/*     */           public int nextIndex() {
/* 385 */             return delegate.nextIndex();
/*     */           }
/*     */           
/*     */           public T previous() {
/* 389 */             return (T)Lists.TransformingSequentialList.this.function.apply(delegate.previous());
/*     */           }
/*     */           
/*     */           public int previousIndex() {
/* 393 */             return delegate.previousIndex();
/*     */           }
/*     */           
/*     */           public void remove() {
/* 397 */             delegate.remove();
/*     */           }
/*     */           
/*     */           public void set(T e) {
/* 401 */             throw new UnsupportedOperationException("not supported");
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class TransformingRandomAccessList<F, T>
/*     */     extends AbstractList<T>
/*     */     implements RandomAccess, Serializable
/*     */   {
/*     */     final List<F> fromList;
/*     */ 
/*     */     
/*     */     final Function<? super F, ? extends T> function;
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */     
/*     */     TransformingRandomAccessList(List<F> fromList, Function<? super F, ? extends T> function) {
/* 424 */       this.fromList = (List<F>)Preconditions.checkNotNull(fromList);
/* 425 */       this.function = (Function<? super F, ? extends T>)Preconditions.checkNotNull(function);
/*     */     }
/*     */     public void clear() {
/* 428 */       this.fromList.clear();
/*     */     }
/*     */     public T get(int index) {
/* 431 */       return (T)this.function.apply(this.fromList.get(index));
/*     */     }
/*     */     public boolean isEmpty() {
/* 434 */       return this.fromList.isEmpty();
/*     */     }
/*     */     public T remove(int index) {
/* 437 */       return (T)this.function.apply(this.fromList.remove(index));
/*     */     }
/*     */     public int size() {
/* 440 */       return this.fromList.size();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> List<List<T>> partition(List<T> list, int size) {
/* 464 */     Preconditions.checkNotNull(list);
/* 465 */     Preconditions.checkArgument((size > 0));
/* 466 */     return (list instanceof RandomAccess) ? new RandomAccessPartition<T>(list, size) : new Partition<T>(list, size);
/*     */   }
/*     */   
/*     */   private static class Partition<T>
/*     */     extends AbstractList<List<T>>
/*     */   {
/*     */     final List<T> list;
/*     */     final int size;
/*     */     
/*     */     Partition(List<T> list, int size) {
/* 476 */       this.list = list;
/* 477 */       this.size = size;
/*     */     }
/*     */     
/*     */     public List<T> get(int index) {
/* 481 */       int listSize = size();
/* 482 */       Preconditions.checkElementIndex(index, listSize);
/* 483 */       int start = index * this.size;
/* 484 */       int end = Math.min(start + this.size, this.list.size());
/* 485 */       return Platform.subList(this.list, start, end);
/*     */     }
/*     */     
/*     */     public int size() {
/* 489 */       return (this.list.size() + this.size - 1) / this.size;
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 493 */       return this.list.isEmpty();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class RandomAccessPartition<T>
/*     */     extends Partition<T> implements RandomAccess {
/*     */     RandomAccessPartition(List<T> list, int size) {
/* 500 */       super(list, size);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\Lists.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */