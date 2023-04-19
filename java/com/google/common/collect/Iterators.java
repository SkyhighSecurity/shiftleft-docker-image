/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
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
/*     */ @GwtCompatible
/*     */ public final class Iterators
/*     */ {
/*  52 */   static final UnmodifiableIterator<Object> EMPTY_ITERATOR = new UnmodifiableIterator()
/*     */     {
/*     */       public boolean hasNext() {
/*  55 */         return false;
/*     */       }
/*     */       public Object next() {
/*  58 */         throw new NoSuchElementException();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> UnmodifiableIterator<T> emptyIterator() {
/*  71 */     return (UnmodifiableIterator)EMPTY_ITERATOR;
/*     */   }
/*     */   
/*  74 */   private static final Iterator<Object> EMPTY_MODIFIABLE_ITERATOR = new Iterator()
/*     */     {
/*     */       public boolean hasNext() {
/*  77 */         return false;
/*     */       }
/*     */       
/*     */       public Object next() {
/*  81 */         throw new NoSuchElementException();
/*     */       }
/*     */       
/*     */       public void remove() {
/*  85 */         throw new IllegalStateException();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> Iterator<T> emptyModifiableIterator() {
/*  98 */     return (Iterator)EMPTY_MODIFIABLE_ITERATOR;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> UnmodifiableIterator<T> unmodifiableIterator(final Iterator<T> iterator) {
/* 104 */     Preconditions.checkNotNull(iterator);
/* 105 */     return new UnmodifiableIterator<T>() {
/*     */         public boolean hasNext() {
/* 107 */           return iterator.hasNext();
/*     */         }
/*     */         public T next() {
/* 110 */           return iterator.next();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int size(Iterator<?> iterator) {
/* 121 */     int count = 0;
/* 122 */     while (iterator.hasNext()) {
/* 123 */       iterator.next();
/* 124 */       count++;
/*     */     } 
/* 126 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean contains(Iterator<?> iterator, @Nullable Object element) {
/* 134 */     if (element == null) {
/* 135 */       while (iterator.hasNext()) {
/* 136 */         if (iterator.next() == null) {
/* 137 */           return true;
/*     */         }
/*     */       } 
/*     */     } else {
/* 141 */       while (iterator.hasNext()) {
/* 142 */         if (element.equals(iterator.next())) {
/* 143 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 147 */     return false;
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
/*     */   public static boolean removeAll(Iterator<?> removeFrom, Collection<?> elementsToRemove) {
/* 161 */     Preconditions.checkNotNull(elementsToRemove);
/* 162 */     boolean modified = false;
/* 163 */     while (removeFrom.hasNext()) {
/* 164 */       if (elementsToRemove.contains(removeFrom.next())) {
/* 165 */         removeFrom.remove();
/* 166 */         modified = true;
/*     */       } 
/*     */     } 
/* 169 */     return modified;
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
/*     */   static <T> boolean removeIf(Iterator<T> removeFrom, Predicate<? super T> predicate) {
/* 185 */     Preconditions.checkNotNull(predicate);
/* 186 */     boolean modified = false;
/* 187 */     while (removeFrom.hasNext()) {
/* 188 */       if (predicate.apply(removeFrom.next())) {
/* 189 */         removeFrom.remove();
/* 190 */         modified = true;
/*     */       } 
/*     */     } 
/* 193 */     return modified;
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
/*     */   public static boolean retainAll(Iterator<?> removeFrom, Collection<?> elementsToRetain) {
/* 207 */     Preconditions.checkNotNull(elementsToRetain);
/* 208 */     boolean modified = false;
/* 209 */     while (removeFrom.hasNext()) {
/* 210 */       if (!elementsToRetain.contains(removeFrom.next())) {
/* 211 */         removeFrom.remove();
/* 212 */         modified = true;
/*     */       } 
/*     */     } 
/* 215 */     return modified;
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
/*     */   public static boolean elementsEqual(Iterator<?> iterator1, Iterator<?> iterator2) {
/* 230 */     while (iterator1.hasNext()) {
/* 231 */       if (!iterator2.hasNext()) {
/* 232 */         return false;
/*     */       }
/* 234 */       Object o1 = iterator1.next();
/* 235 */       Object o2 = iterator2.next();
/* 236 */       if (!Objects.equal(o1, o2)) {
/* 237 */         return false;
/*     */       }
/*     */     } 
/* 240 */     return !iterator2.hasNext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(Iterator<?> iterator) {
/* 249 */     if (!iterator.hasNext()) {
/* 250 */       return "[]";
/*     */     }
/* 252 */     StringBuilder builder = new StringBuilder();
/* 253 */     builder.append('[').append(iterator.next());
/* 254 */     while (iterator.hasNext()) {
/* 255 */       builder.append(", ").append(iterator.next());
/*     */     }
/* 257 */     return builder.append(']').toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T getOnlyElement(Iterator<T> iterator) {
/* 268 */     T first = iterator.next();
/* 269 */     if (!iterator.hasNext()) {
/* 270 */       return first;
/*     */     }
/*     */     
/* 273 */     StringBuilder sb = new StringBuilder();
/* 274 */     sb.append("expected one element but was: <" + first);
/* 275 */     for (int i = 0; i < 4 && iterator.hasNext(); i++) {
/* 276 */       sb.append(", " + iterator.next());
/*     */     }
/* 278 */     if (iterator.hasNext()) {
/* 279 */       sb.append(", ...");
/*     */     }
/* 281 */     sb.append(">");
/*     */     
/* 283 */     throw new IllegalArgumentException(sb.toString());
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
/*     */   public static <T> T getOnlyElement(Iterator<T> iterator, @Nullable T defaultValue) {
/* 295 */     return iterator.hasNext() ? getOnlyElement(iterator) : defaultValue;
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
/*     */   @GwtIncompatible("Array.newArray")
/*     */   public static <T> T[] toArray(Iterator<? extends T> iterator, Class<T> type) {
/* 310 */     List<T> list = Lists.newArrayList(iterator);
/* 311 */     return Iterables.toArray(list, type);
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
/*     */   public static <T> boolean addAll(Collection<T> addTo, Iterator<? extends T> iterator) {
/* 324 */     Preconditions.checkNotNull(addTo);
/* 325 */     boolean wasModified = false;
/* 326 */     while (iterator.hasNext()) {
/* 327 */       wasModified |= addTo.add(iterator.next());
/*     */     }
/* 329 */     return wasModified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int frequency(Iterator<?> iterator, @Nullable Object element) {
/* 340 */     int result = 0;
/* 341 */     if (element == null) {
/* 342 */       while (iterator.hasNext()) {
/* 343 */         if (iterator.next() == null) {
/* 344 */           result++;
/*     */         }
/*     */       } 
/*     */     } else {
/* 348 */       while (iterator.hasNext()) {
/* 349 */         if (element.equals(iterator.next())) {
/* 350 */           result++;
/*     */         }
/*     */       } 
/*     */     } 
/* 354 */     return result;
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
/*     */   public static <T> Iterator<T> cycle(final Iterable<T> iterable) {
/* 372 */     Preconditions.checkNotNull(iterable);
/* 373 */     return new Iterator<T>() {
/* 374 */         Iterator<T> iterator = Iterators.emptyIterator();
/*     */         Iterator<T> removeFrom;
/*     */         
/*     */         public boolean hasNext() {
/* 378 */           if (!this.iterator.hasNext()) {
/* 379 */             this.iterator = iterable.iterator();
/*     */           }
/* 381 */           return this.iterator.hasNext();
/*     */         }
/*     */         public T next() {
/* 384 */           if (!hasNext()) {
/* 385 */             throw new NoSuchElementException();
/*     */           }
/* 387 */           this.removeFrom = this.iterator;
/* 388 */           return this.iterator.next();
/*     */         }
/*     */         public void remove() {
/* 391 */           Preconditions.checkState((this.removeFrom != null), "no calls to next() since last call to remove()");
/*     */           
/* 393 */           this.removeFrom.remove();
/* 394 */           this.removeFrom = null;
/*     */         }
/*     */       };
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
/*     */   public static <T> Iterator<T> cycle(T... elements) {
/* 413 */     return cycle(Lists.newArrayList(elements));
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
/*     */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b) {
/* 427 */     Preconditions.checkNotNull(a);
/* 428 */     Preconditions.checkNotNull(b);
/* 429 */     return concat(Arrays.<Iterator<? extends T>>asList((Iterator<? extends T>[])new Iterator[] { a, b }).iterator());
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
/*     */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b, Iterator<? extends T> c) {
/* 444 */     Preconditions.checkNotNull(a);
/* 445 */     Preconditions.checkNotNull(b);
/* 446 */     Preconditions.checkNotNull(c);
/* 447 */     return concat(Arrays.<Iterator<? extends T>>asList((Iterator<? extends T>[])new Iterator[] { a, b, c }).iterator());
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
/*     */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b, Iterator<? extends T> c, Iterator<? extends T> d) {
/* 463 */     Preconditions.checkNotNull(a);
/* 464 */     Preconditions.checkNotNull(b);
/* 465 */     Preconditions.checkNotNull(c);
/* 466 */     Preconditions.checkNotNull(d);
/* 467 */     return concat(Arrays.<Iterator<? extends T>>asList((Iterator<? extends T>[])new Iterator[] { a, b, c, d }).iterator());
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
/*     */   public static <T> Iterator<T> concat(Iterator<? extends T>... inputs) {
/* 481 */     return concat(ImmutableList.<Iterator<? extends T>>of(inputs).iterator());
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
/*     */   public static <T> Iterator<T> concat(final Iterator<? extends Iterator<? extends T>> inputs) {
/* 495 */     Preconditions.checkNotNull(inputs);
/* 496 */     return new Iterator<T>() {
/* 497 */         Iterator<? extends T> current = Iterators.emptyIterator();
/*     */         
/*     */         Iterator<? extends T> removeFrom;
/*     */ 
/*     */         
/*     */         public boolean hasNext() {
/*     */           boolean currentHasNext;
/* 504 */           while (!(currentHasNext = this.current.hasNext()) && inputs.hasNext()) {
/* 505 */             this.current = inputs.next();
/*     */           }
/* 507 */           return currentHasNext;
/*     */         }
/*     */         public T next() {
/* 510 */           if (!hasNext()) {
/* 511 */             throw new NoSuchElementException();
/*     */           }
/* 513 */           this.removeFrom = this.current;
/* 514 */           return this.current.next();
/*     */         }
/*     */         public void remove() {
/* 517 */           Preconditions.checkState((this.removeFrom != null), "no calls to next() since last call to remove()");
/*     */           
/* 519 */           this.removeFrom.remove();
/* 520 */           this.removeFrom = null;
/*     */         }
/*     */       };
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
/*     */   public static <T> UnmodifiableIterator<List<T>> partition(Iterator<T> iterator, int size) {
/* 542 */     return partitionImpl(iterator, size, false);
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
/*     */   public static <T> UnmodifiableIterator<List<T>> paddedPartition(Iterator<T> iterator, int size) {
/* 563 */     return partitionImpl(iterator, size, true);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> UnmodifiableIterator<List<T>> partitionImpl(final Iterator<T> iterator, final int size, final boolean pad) {
/* 568 */     Preconditions.checkNotNull(iterator);
/* 569 */     Preconditions.checkArgument((size > 0));
/* 570 */     return new UnmodifiableIterator<List<T>>() {
/*     */         public boolean hasNext() {
/* 572 */           return iterator.hasNext();
/*     */         }
/*     */         public List<T> next() {
/* 575 */           if (!hasNext()) {
/* 576 */             throw new NoSuchElementException();
/*     */           }
/* 578 */           Object[] array = new Object[size];
/* 579 */           int count = 0;
/* 580 */           for (; count < size && iterator.hasNext(); count++) {
/* 581 */             array[count] = iterator.next();
/*     */           }
/*     */ 
/*     */           
/* 585 */           List<T> list = Collections.unmodifiableList(Arrays.asList((T[])array));
/*     */           
/* 587 */           return (pad || count == size) ? list : Platform.<T>subList(list, 0, count);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> UnmodifiableIterator<T> filter(final Iterator<T> unfiltered, final Predicate<? super T> predicate) {
/* 597 */     Preconditions.checkNotNull(unfiltered);
/* 598 */     Preconditions.checkNotNull(predicate);
/* 599 */     return new AbstractIterator<T>() {
/*     */         protected T computeNext() {
/* 601 */           while (unfiltered.hasNext()) {
/* 602 */             T element = unfiltered.next();
/* 603 */             if (predicate.apply(element)) {
/* 604 */               return element;
/*     */             }
/*     */           } 
/* 607 */           return endOfData();
/*     */         }
/*     */       };
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
/*     */   @GwtIncompatible("Class.isInstance")
/*     */   public static <T> UnmodifiableIterator<T> filter(Iterator<?> unfiltered, Class<T> type) {
/* 626 */     return filter((Iterator)unfiltered, Predicates.instanceOf(type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> boolean any(Iterator<T> iterator, Predicate<? super T> predicate) {
/* 636 */     Preconditions.checkNotNull(predicate);
/* 637 */     while (iterator.hasNext()) {
/* 638 */       T element = iterator.next();
/* 639 */       if (predicate.apply(element)) {
/* 640 */         return true;
/*     */       }
/*     */     } 
/* 643 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> boolean all(Iterator<T> iterator, Predicate<? super T> predicate) {
/* 653 */     Preconditions.checkNotNull(predicate);
/* 654 */     while (iterator.hasNext()) {
/* 655 */       T element = iterator.next();
/* 656 */       if (!predicate.apply(element)) {
/* 657 */         return false;
/*     */       }
/*     */     } 
/* 660 */     return true;
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
/*     */   public static <T> T find(Iterator<T> iterator, Predicate<? super T> predicate) {
/* 676 */     return filter(iterator, predicate).next();
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
/*     */   public static <F, T> Iterator<T> transform(final Iterator<F> fromIterator, final Function<? super F, ? extends T> function) {
/* 689 */     Preconditions.checkNotNull(fromIterator);
/* 690 */     Preconditions.checkNotNull(function);
/* 691 */     return new Iterator<T>() {
/*     */         public boolean hasNext() {
/* 693 */           return fromIterator.hasNext();
/*     */         }
/*     */         public T next() {
/* 696 */           F from = fromIterator.next();
/* 697 */           return (T)function.apply(from);
/*     */         }
/*     */         public void remove() {
/* 700 */           fromIterator.remove();
/*     */         }
/*     */       };
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
/*     */   public static <T> T get(Iterator<T> iterator, int position) {
/* 716 */     if (position < 0) {
/* 717 */       throw new IndexOutOfBoundsException("position (" + position + ") must not be negative");
/*     */     }
/*     */ 
/*     */     
/* 721 */     int skipped = 0;
/* 722 */     while (iterator.hasNext()) {
/* 723 */       T t = iterator.next();
/* 724 */       if (skipped++ == position) {
/* 725 */         return t;
/*     */       }
/*     */     } 
/*     */     
/* 729 */     throw new IndexOutOfBoundsException("position (" + position + ") must be less than the number of elements that remained (" + skipped + ")");
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
/*     */   public static <T> T getLast(Iterator<T> iterator) {
/*     */     while (true) {
/* 742 */       T current = iterator.next();
/* 743 */       if (!iterator.hasNext()) {
/* 744 */         return current;
/*     */       }
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
/*     */   public static <T> UnmodifiableIterator<T> forArray(T... array) {
/* 765 */     return new UnmodifiableIterator<T>() {
/* 766 */         final int length = array.length;
/* 767 */         int i = 0;
/*     */         public boolean hasNext() {
/* 769 */           return (this.i < this.length);
/*     */         }
/*     */         
/*     */         public T next() {
/*     */           try {
/* 774 */             T t = (T)array[this.i];
/* 775 */             this.i++;
/* 776 */             return t;
/* 777 */           } catch (ArrayIndexOutOfBoundsException e) {
/* 778 */             throw new NoSuchElementException();
/*     */           } 
/*     */         }
/*     */       };
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
/*     */   static <T> UnmodifiableIterator<T> forArray(final T[] array, final int offset, int length) {
/* 801 */     Preconditions.checkArgument((length >= 0));
/* 802 */     final int end = offset + length;
/*     */ 
/*     */     
/* 805 */     Preconditions.checkPositionIndexes(offset, end, array.length);
/*     */ 
/*     */ 
/*     */     
/* 809 */     return new UnmodifiableIterator<T>() {
/* 810 */         int i = offset;
/*     */         public boolean hasNext() {
/* 812 */           return (this.i < end);
/*     */         }
/*     */         public T next() {
/* 815 */           if (!hasNext()) {
/* 816 */             throw new NoSuchElementException();
/*     */           }
/* 818 */           return (T)array[this.i++];
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> UnmodifiableIterator<T> singletonIterator(@Nullable final T value) {
/* 831 */     return new UnmodifiableIterator<T>() { boolean done;
/*     */         
/*     */         public boolean hasNext() {
/* 834 */           return !this.done;
/*     */         }
/*     */         public T next() {
/* 837 */           if (this.done) {
/* 838 */             throw new NoSuchElementException();
/*     */           }
/* 840 */           this.done = true;
/* 841 */           return (T)value;
/*     */         } }
/*     */       ;
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
/*     */   public static <T> UnmodifiableIterator<T> forEnumeration(final Enumeration<T> enumeration) {
/* 856 */     Preconditions.checkNotNull(enumeration);
/* 857 */     return new UnmodifiableIterator<T>() {
/*     */         public boolean hasNext() {
/* 859 */           return enumeration.hasMoreElements();
/*     */         }
/*     */         public T next() {
/* 862 */           return enumeration.nextElement();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Enumeration<T> asEnumeration(final Iterator<T> iterator) {
/* 875 */     Preconditions.checkNotNull(iterator);
/* 876 */     return new Enumeration<T>() {
/*     */         public boolean hasMoreElements() {
/* 878 */           return iterator.hasNext();
/*     */         }
/*     */         public T nextElement() {
/* 881 */           return iterator.next();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   private static class PeekingImpl<E>
/*     */     implements PeekingIterator<E>
/*     */   {
/*     */     private final Iterator<? extends E> iterator;
/*     */     
/*     */     private boolean hasPeeked;
/*     */     private E peekedElement;
/*     */     
/*     */     public PeekingImpl(Iterator<? extends E> iterator) {
/* 896 */       this.iterator = (Iterator<? extends E>)Preconditions.checkNotNull(iterator);
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 900 */       return (this.hasPeeked || this.iterator.hasNext());
/*     */     }
/*     */     
/*     */     public E next() {
/* 904 */       if (!this.hasPeeked) {
/* 905 */         return this.iterator.next();
/*     */       }
/* 907 */       E result = this.peekedElement;
/* 908 */       this.hasPeeked = false;
/* 909 */       this.peekedElement = null;
/* 910 */       return result;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 914 */       Preconditions.checkState(!this.hasPeeked, "Can't remove after you've peeked at next");
/* 915 */       this.iterator.remove();
/*     */     }
/*     */     
/*     */     public E peek() {
/* 919 */       if (!this.hasPeeked) {
/* 920 */         this.peekedElement = this.iterator.next();
/* 921 */         this.hasPeeked = true;
/*     */       } 
/* 923 */       return this.peekedElement;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> PeekingIterator<T> peekingIterator(Iterator<? extends T> iterator) {
/* 967 */     if (iterator instanceof PeekingImpl) {
/*     */ 
/*     */ 
/*     */       
/* 971 */       PeekingImpl<T> peeking = (PeekingImpl)iterator;
/* 972 */       return peeking;
/*     */     } 
/* 974 */     return new PeekingImpl<T>(iterator);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\Iterators.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */