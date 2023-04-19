/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class Ordering<T>
/*     */   implements Comparator<T>
/*     */ {
/*     */   static final int LEFT_IS_GREATER = 1;
/*     */   static final int RIGHT_IS_GREATER = -1;
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <C extends Comparable> Ordering<C> natural() {
/*  71 */     return NaturalOrdering.INSTANCE;
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Ordering<T> from(Comparator<T> comparator) {
/*  84 */     return (comparator instanceof Ordering) ? (Ordering<T>)comparator : new ComparatorOrdering<T>(comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Ordering<T> from(Ordering<T> ordering) {
/*  96 */     return (Ordering<T>)Preconditions.checkNotNull(ordering);
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
/*     */   public static <T> Ordering<T> explicit(List<T> valuesInOrder) {
/* 122 */     return new ExplicitOrdering<T>(valuesInOrder);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Ordering<T> explicit(T leastValue, T... remainingValuesInOrder) {
/* 151 */     return explicit(Lists.asList(leastValue, remainingValuesInOrder));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static class IncomparableValueException
/*     */     extends ClassCastException
/*     */   {
/*     */     final Object value;
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     
/*     */     IncomparableValueException(Object value) {
/* 167 */       super("Cannot compare value: " + value);
/* 168 */       this.value = value;
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static Ordering<Object> usingToString() {
/* 183 */     return UsingToStringOrdering.INSTANCE;
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
/*     */   public static <T> Ordering<T> compound(Iterable<? extends Comparator<? super T>> comparators) {
/* 204 */     return new CompoundOrdering<T>(comparators);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public <U extends T> Ordering<U> compound(Comparator<? super U> secondaryComparator) {
/* 229 */     return new CompoundOrdering<U>(this, (Comparator<? super U>)Preconditions.checkNotNull(secondaryComparator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public <S extends T> Ordering<S> reverse() {
/* 240 */     return new ReverseOrdering<S>(this);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public <F> Ordering<F> onResultOf(Function<F, ? extends T> function) {
/* 254 */     return new ByFunctionOrdering<F, T>(function, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public <S extends T> Ordering<S> nullsFirst() {
/* 265 */     return new NullsFirstOrdering<S>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public <S extends T> Ordering<S> nullsLast() {
/* 276 */     return new NullsLastOrdering<S>(this);
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
/*     */   public int binarySearch(List<? extends T> sortedList, T key) {
/* 288 */     return Collections.binarySearch(sortedList, key, this);
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
/*     */   public <E extends T> List<E> sortedCopy(Iterable<E> iterable) {
/* 304 */     List<E> list = Lists.newArrayList(iterable);
/* 305 */     Collections.sort(list, this);
/* 306 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOrdered(Iterable<? extends T> iterable) {
/* 316 */     Iterator<? extends T> it = iterable.iterator();
/* 317 */     if (it.hasNext()) {
/* 318 */       T prev = it.next();
/* 319 */       while (it.hasNext()) {
/* 320 */         T next = it.next();
/* 321 */         if (compare(prev, next) > 0) {
/* 322 */           return false;
/*     */         }
/* 324 */         prev = next;
/*     */       } 
/*     */     } 
/* 327 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStrictlyOrdered(Iterable<? extends T> iterable) {
/* 337 */     Iterator<? extends T> it = iterable.iterator();
/* 338 */     if (it.hasNext()) {
/* 339 */       T prev = it.next();
/* 340 */       while (it.hasNext()) {
/* 341 */         T next = it.next();
/* 342 */         if (compare(prev, next) >= 0) {
/* 343 */           return false;
/*     */         }
/* 345 */         prev = next;
/*     */       } 
/*     */     } 
/* 348 */     return true;
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
/*     */   public <E extends T> E max(Iterable<E> iterable) {
/* 361 */     Iterator<E> iterator = iterable.iterator();
/*     */ 
/*     */     
/* 364 */     E maxSoFar = iterator.next();
/*     */     
/* 366 */     while (iterator.hasNext()) {
/* 367 */       maxSoFar = max(maxSoFar, iterator.next());
/*     */     }
/*     */     
/* 370 */     return maxSoFar;
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
/*     */   public <E extends T> E max(E a, E b, E c, E... rest) {
/* 385 */     E maxSoFar = max(max(a, b), c);
/*     */     
/* 387 */     for (E r : rest) {
/* 388 */       maxSoFar = max(maxSoFar, r);
/*     */     }
/*     */     
/* 391 */     return maxSoFar;
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
/*     */   public <E extends T> E max(E a, E b) {
/* 408 */     return (compare((T)a, (T)b) >= 0) ? a : b;
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
/*     */   public <E extends T> E min(Iterable<E> iterable) {
/* 421 */     Iterator<E> iterator = iterable.iterator();
/*     */ 
/*     */     
/* 424 */     E minSoFar = iterator.next();
/*     */     
/* 426 */     while (iterator.hasNext()) {
/* 427 */       minSoFar = min(minSoFar, iterator.next());
/*     */     }
/*     */     
/* 430 */     return minSoFar;
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
/*     */   public <E extends T> E min(E a, E b, E c, E... rest) {
/* 445 */     E minSoFar = min(min(a, b), c);
/*     */     
/* 447 */     for (E r : rest) {
/* 448 */       minSoFar = min(minSoFar, r);
/*     */     }
/*     */     
/* 451 */     return minSoFar;
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
/*     */   public <E extends T> E min(E a, E b) {
/* 468 */     return (compare((T)a, (T)b) <= 0) ? a : b;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\Ordering.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */