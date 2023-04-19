/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
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
/*     */ @GwtCompatible
/*     */ public final class Iterables
/*     */ {
/*     */   public static <T> Iterable<T> unmodifiableIterable(final Iterable<T> iterable) {
/*  57 */     Preconditions.checkNotNull(iterable);
/*  58 */     return new Iterable<T>() {
/*     */         public Iterator<T> iterator() {
/*  60 */           return Iterators.unmodifiableIterator(iterable.iterator());
/*     */         }
/*     */         public String toString() {
/*  63 */           return iterable.toString();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int size(Iterable<?> iterable) {
/*  73 */     return (iterable instanceof Collection) ? ((Collection)iterable).size() : Iterators.size(iterable.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean contains(Iterable<?> iterable, @Nullable Object element) {
/*  84 */     if (iterable instanceof Collection) {
/*  85 */       Collection<?> collection = (Collection)iterable;
/*     */       try {
/*  87 */         return collection.contains(element);
/*  88 */       } catch (NullPointerException e) {
/*  89 */         return false;
/*  90 */       } catch (ClassCastException e) {
/*  91 */         return false;
/*     */       } 
/*     */     } 
/*  94 */     return Iterators.contains(iterable.iterator(), element);
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
/*     */   public static boolean removeAll(Iterable<?> removeFrom, Collection<?> elementsToRemove) {
/* 110 */     return (removeFrom instanceof Collection) ? ((Collection)removeFrom).removeAll((Collection)Preconditions.checkNotNull(elementsToRemove)) : Iterators.removeAll(removeFrom.iterator(), elementsToRemove);
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
/*     */   public static boolean retainAll(Iterable<?> removeFrom, Collection<?> elementsToRetain) {
/* 128 */     return (removeFrom instanceof Collection) ? ((Collection)removeFrom).retainAll((Collection)Preconditions.checkNotNull(elementsToRetain)) : Iterators.retainAll(removeFrom.iterator(), elementsToRetain);
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
/*     */   static <T> boolean removeIf(Iterable<T> removeFrom, Predicate<? super T> predicate) {
/* 147 */     if (removeFrom instanceof java.util.RandomAccess && removeFrom instanceof List) {
/* 148 */       return removeIfFromRandomAccessList((List)removeFrom, (Predicate)Preconditions.checkNotNull(predicate));
/*     */     }
/*     */     
/* 151 */     return Iterators.removeIf(removeFrom.iterator(), predicate);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> boolean removeIfFromRandomAccessList(List<T> list, Predicate<? super T> predicate) {
/* 156 */     int from = 0;
/* 157 */     int to = 0;
/*     */     
/* 159 */     for (; from < list.size(); from++) {
/* 160 */       T element = list.get(from);
/* 161 */       if (!predicate.apply(element)) {
/* 162 */         if (from > to) {
/* 163 */           list.set(to, element);
/*     */         }
/* 165 */         to++;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 172 */     ListIterator<T> iter = list.listIterator(list.size());
/* 173 */     for (int idx = from - to; idx > 0; idx--) {
/* 174 */       iter.previous();
/* 175 */       iter.remove();
/*     */     } 
/*     */     
/* 178 */     return (from != to);
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
/*     */   public static boolean elementsEqual(Iterable<?> iterable1, Iterable<?> iterable2) {
/* 190 */     return Iterators.elementsEqual(iterable1.iterator(), iterable2.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(Iterable<?> iterable) {
/* 198 */     return Iterators.toString(iterable.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T getOnlyElement(Iterable<T> iterable) {
/* 209 */     return Iterators.getOnlyElement(iterable.iterator());
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
/*     */   public static <T> T getOnlyElement(Iterable<T> iterable, @Nullable T defaultValue) {
/* 221 */     return Iterators.getOnlyElement(iterable.iterator(), defaultValue);
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
/*     */   @GwtIncompatible("Array.newInstance(Class, int)")
/*     */   public static <T> T[] toArray(Iterable<? extends T> iterable, Class<T> type) {
/* 235 */     Collection<? extends T> collection = (iterable instanceof Collection) ? (Collection<? extends T>)iterable : Lists.<T>newArrayList(iterable);
/*     */ 
/*     */     
/* 238 */     T[] array = ObjectArrays.newArray(type, collection.size());
/* 239 */     return collection.toArray(array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> boolean addAll(Collection<T> addTo, Iterable<? extends T> elementsToAdd) {
/* 250 */     if (elementsToAdd instanceof Collection) {
/*     */       
/* 252 */       Collection<? extends T> c = (Collection<? extends T>)elementsToAdd;
/* 253 */       return addTo.addAll(c);
/*     */     } 
/* 255 */     return Iterators.addAll(addTo, elementsToAdd.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int frequency(Iterable<?> iterable, @Nullable Object element) {
/* 265 */     if (iterable instanceof Multiset) {
/* 266 */       return ((Multiset)iterable).count(element);
/*     */     }
/* 268 */     if (iterable instanceof Set) {
/* 269 */       return ((Set)iterable).contains(element) ? 1 : 0;
/*     */     }
/* 271 */     return Iterators.frequency(iterable.iterator(), element);
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
/*     */   public static <T> Iterable<T> cycle(final Iterable<T> iterable) {
/* 292 */     Preconditions.checkNotNull(iterable);
/* 293 */     return new Iterable<T>() {
/*     */         public Iterator<T> iterator() {
/* 295 */           return Iterators.cycle(iterable);
/*     */         }
/*     */         public String toString() {
/* 298 */           return iterable.toString() + " (cycled)";
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
/*     */ 
/*     */   
/*     */   public static <T> Iterable<T> cycle(T... elements) {
/* 322 */     return cycle(Lists.newArrayList(elements));
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
/*     */   public static <T> Iterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b) {
/* 336 */     Preconditions.checkNotNull(a);
/* 337 */     Preconditions.checkNotNull(b);
/* 338 */     return concat(Arrays.asList((Iterable<? extends T>[])new Iterable[] { a, b }));
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
/*     */   public static <T> Iterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c) {
/* 353 */     Preconditions.checkNotNull(a);
/* 354 */     Preconditions.checkNotNull(b);
/* 355 */     Preconditions.checkNotNull(c);
/* 356 */     return concat(Arrays.asList((Iterable<? extends T>[])new Iterable[] { a, b, c }));
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
/*     */   public static <T> Iterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c, Iterable<? extends T> d) {
/* 373 */     Preconditions.checkNotNull(a);
/* 374 */     Preconditions.checkNotNull(b);
/* 375 */     Preconditions.checkNotNull(c);
/* 376 */     Preconditions.checkNotNull(d);
/* 377 */     return concat(Arrays.asList((Iterable<? extends T>[])new Iterable[] { a, b, c, d }));
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
/*     */   public static <T> Iterable<T> concat(Iterable<? extends T>... inputs) {
/* 391 */     return concat(ImmutableList.of(inputs));
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
/*     */   public static <T> Iterable<T> concat(Iterable<? extends Iterable<? extends T>> inputs) {
/* 419 */     Function<Iterable<? extends T>, Iterator<? extends T>> function = new Function<Iterable<? extends T>, Iterator<? extends T>>()
/*     */       {
/*     */         public Iterator<? extends T> apply(Iterable<? extends T> from) {
/* 422 */           return from.iterator();
/*     */         }
/*     */       };
/* 425 */     final Iterable<Iterator<? extends T>> iterators = transform(inputs, function);
/*     */     
/* 427 */     return new IterableWithToString<T>() {
/*     */         public Iterator<T> iterator() {
/* 429 */           return Iterators.concat(iterators.iterator());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Iterable<List<T>> partition(final Iterable<T> iterable, final int size) {
/* 456 */     Preconditions.checkNotNull(iterable);
/* 457 */     Preconditions.checkArgument((size > 0));
/* 458 */     return new IterableWithToString<List<T>>() {
/*     */         public Iterator<List<T>> iterator() {
/* 460 */           return Iterators.partition(iterable.iterator(), size);
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
/*     */ 
/*     */   
/*     */   public static <T> Iterable<List<T>> paddedPartition(final Iterable<T> iterable, final int size) {
/* 484 */     Preconditions.checkNotNull(iterable);
/* 485 */     Preconditions.checkArgument((size > 0));
/* 486 */     return new IterableWithToString<List<T>>() {
/*     */         public Iterator<List<T>> iterator() {
/* 488 */           return Iterators.paddedPartition(iterable.iterator(), size);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Iterable<T> filter(final Iterable<T> unfiltered, final Predicate<? super T> predicate) {
/* 499 */     Preconditions.checkNotNull(unfiltered);
/* 500 */     Preconditions.checkNotNull(predicate);
/* 501 */     return new IterableWithToString<T>() {
/*     */         public Iterator<T> iterator() {
/* 503 */           return Iterators.filter(unfiltered.iterator(), predicate);
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
/*     */   public static <T> Iterable<T> filter(final Iterable<?> unfiltered, final Class<T> type) {
/* 522 */     Preconditions.checkNotNull(unfiltered);
/* 523 */     Preconditions.checkNotNull(type);
/* 524 */     return new IterableWithToString<T>() {
/*     */         public Iterator<T> iterator() {
/* 526 */           return Iterators.filter(unfiltered.iterator(), type);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> boolean any(Iterable<T> iterable, Predicate<? super T> predicate) {
/* 537 */     return Iterators.any(iterable.iterator(), predicate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> boolean all(Iterable<T> iterable, Predicate<? super T> predicate) {
/* 546 */     return Iterators.all(iterable.iterator(), predicate);
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
/*     */   public static <T> T find(Iterable<T> iterable, Predicate<? super T> predicate) {
/* 558 */     return Iterators.find(iterable.iterator(), predicate);
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
/*     */   public static <F, T> Iterable<T> transform(final Iterable<F> fromIterable, final Function<? super F, ? extends T> function) {
/* 571 */     Preconditions.checkNotNull(fromIterable);
/* 572 */     Preconditions.checkNotNull(function);
/* 573 */     return new IterableWithToString<T>() {
/*     */         public Iterator<T> iterator() {
/* 575 */           return Iterators.transform(fromIterable.iterator(), function);
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
/*     */   public static <T> T get(Iterable<T> iterable, int position) {
/* 589 */     Preconditions.checkNotNull(iterable);
/* 590 */     if (iterable instanceof List) {
/* 591 */       return ((List<T>)iterable).get(position);
/*     */     }
/*     */     
/* 594 */     if (iterable instanceof Collection) {
/*     */       
/* 596 */       Collection<T> collection = (Collection<T>)iterable;
/* 597 */       Preconditions.checkElementIndex(position, collection.size());
/*     */     
/*     */     }
/* 600 */     else if (position < 0) {
/* 601 */       throw new IndexOutOfBoundsException("position cannot be negative: " + position);
/*     */     } 
/*     */ 
/*     */     
/* 605 */     return Iterators.get(iterable.iterator(), position);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T getLast(Iterable<T> iterable) {
/* 615 */     if (iterable instanceof List) {
/* 616 */       List<T> list = (List<T>)iterable;
/*     */ 
/*     */       
/* 619 */       if (list.isEmpty()) {
/* 620 */         throw new NoSuchElementException();
/*     */       }
/* 622 */       return list.get(list.size() - 1);
/*     */     } 
/*     */     
/* 625 */     if (iterable instanceof SortedSet) {
/* 626 */       SortedSet<T> sortedSet = (SortedSet<T>)iterable;
/* 627 */       return sortedSet.last();
/*     */     } 
/*     */     
/* 630 */     return Iterators.getLast(iterable.iterator());
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
/*     */   public static <T> Iterable<T> reverse(final List<T> list) {
/* 651 */     Preconditions.checkNotNull(list);
/* 652 */     return new IterableWithToString<T>() {
/*     */         public Iterator<T> iterator() {
/* 654 */           final ListIterator<T> listIter = list.listIterator(list.size());
/* 655 */           return new Iterator() {
/*     */               public boolean hasNext() {
/* 657 */                 return listIter.hasPrevious();
/*     */               }
/*     */               public T next() {
/* 660 */                 return listIter.previous();
/*     */               }
/*     */               public void remove() {
/* 663 */                 listIter.remove();
/*     */               }
/*     */             };
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
/*     */   public static <T> boolean isEmpty(Iterable<T> iterable) {
/* 680 */     return !iterable.iterator().hasNext();
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
/*     */   static boolean remove(Iterable<?> iterable, @Nullable Object o) {
/* 702 */     Iterator<?> i = iterable.iterator();
/* 703 */     while (i.hasNext()) {
/* 704 */       if (Objects.equal(i.next(), o)) {
/* 705 */         i.remove();
/* 706 */         return true;
/*     */       } 
/*     */     } 
/* 709 */     return false;
/*     */   }
/*     */   
/*     */   static abstract class IterableWithToString<E> implements Iterable<E> {
/*     */     public String toString() {
/* 714 */       return Iterables.toString(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\Iterables.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */