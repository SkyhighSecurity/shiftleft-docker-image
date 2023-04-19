/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.SortedSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ImmutableSortedSet<E>
/*     */   extends ImmutableSortedSetFauxverideShim<E>
/*     */   implements SortedSet<E>
/*     */ {
/*  88 */   private static final Comparator NATURAL_ORDER = Ordering.natural();
/*     */ 
/*     */   
/*  91 */   private static final ImmutableSortedSet<Object> NATURAL_EMPTY_SET = new EmptyImmutableSortedSet(NATURAL_ORDER);
/*     */   
/*     */   final transient Comparator<? super E> comparator;
/*     */   
/*     */   private static <E> ImmutableSortedSet<E> emptySet() {
/*  96 */     return (ImmutableSortedSet)NATURAL_EMPTY_SET;
/*     */   }
/*     */ 
/*     */   
/*     */   static <E> ImmutableSortedSet<E> emptySet(Comparator<? super E> comparator) {
/* 101 */     if (NATURAL_ORDER.equals(comparator)) {
/* 102 */       return emptySet();
/*     */     }
/* 104 */     return new EmptyImmutableSortedSet<E>(comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSortedSet<E> of() {
/* 112 */     return emptySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E element) {
/* 120 */     Object[] array = { Preconditions.checkNotNull(element) };
/* 121 */     return new RegularImmutableSortedSet<E>(array, Ordering.natural());
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2) {
/* 134 */     return ofInternal(Ordering.natural(), (E[])new Comparable[] { (Comparable)e1, (Comparable)e2 });
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3) {
/* 147 */     return ofInternal(Ordering.natural(), (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3 });
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4) {
/* 160 */     return ofInternal(Ordering.natural(), (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4 });
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5) {
/* 173 */     return ofInternal(Ordering.natural(), (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4, (Comparable)e5 });
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E... elements) {
/* 188 */     return ofInternal(Ordering.natural(), elements);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <E> ImmutableSortedSet<E> ofInternal(Comparator<? super E> comparator, E... elements) {
/* 193 */     Preconditions.checkNotNull(elements);
/* 194 */     switch (elements.length) {
/*     */       case 0:
/* 196 */         return emptySet(comparator);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 203 */     Object[] array = new Object[elements.length];
/* 204 */     for (int i = 0; i < elements.length; i++) {
/* 205 */       array[i] = Preconditions.checkNotNull(elements[i]);
/*     */     }
/* 207 */     sort(array, comparator);
/* 208 */     array = removeDupes(array, comparator);
/* 209 */     return new RegularImmutableSortedSet<E>(array, comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <E> void sort(Object[] array, Comparator<? super E> comparator) {
/* 217 */     Arrays.sort(array, (Comparator)comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <E> Object[] removeDupes(Object[] array, Comparator<? super E> comparator) {
/* 227 */     int size = 1;
/* 228 */     for (int i = 1; i < array.length; i++) {
/* 229 */       Object element = array[i];
/* 230 */       if (unsafeCompare(comparator, array[size - 1], element) != 0) {
/* 231 */         array[size] = element;
/* 232 */         size++;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 237 */     if (size == array.length) {
/* 238 */       return array;
/*     */     }
/* 240 */     Object[] copy = new Object[size];
/* 241 */     System.arraycopy(array, 0, copy, 0, size);
/* 242 */     return copy;
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Iterable<? extends E> elements) {
/* 275 */     Ordering<E> naturalOrder = Ordering.natural();
/* 276 */     return copyOfInternal(naturalOrder, elements, false);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Iterator<? extends E> elements) {
/* 295 */     Ordering<E> naturalOrder = Ordering.natural();
/* 296 */     return copyOfInternal(naturalOrder, elements);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
/* 313 */     Preconditions.checkNotNull(comparator);
/* 314 */     return copyOfInternal(comparator, elements, false);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> elements) {
/* 328 */     Preconditions.checkNotNull(comparator);
/* 329 */     return copyOfInternal(comparator, elements);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOfSorted(SortedSet<E> sortedSet) {
/* 345 */     Comparator<? super E> comparator = sortedSet.comparator();
/* 346 */     if (comparator == null) {
/* 347 */       comparator = NATURAL_ORDER;
/*     */     }
/* 349 */     return copyOfInternal(comparator, sortedSet, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <E> ImmutableSortedSet<E> copyOfInternal(Comparator<? super E> comparator, Iterable<? extends E> elements, boolean fromSortedSet) {
/* 355 */     boolean hasSameComparator = (fromSortedSet || hasSameComparator(elements, comparator));
/*     */ 
/*     */     
/* 358 */     if (hasSameComparator && elements instanceof ImmutableSortedSet) {
/*     */       
/* 360 */       ImmutableSortedSet<E> result = (ImmutableSortedSet)elements;
/* 361 */       if (!result.hasPartialArray()) {
/* 362 */         return result;
/*     */       }
/*     */     } 
/*     */     
/* 366 */     Object[] array = newObjectArray(elements);
/* 367 */     if (array.length == 0) {
/* 368 */       return emptySet(comparator);
/*     */     }
/*     */     
/* 371 */     for (Object e : array) {
/* 372 */       Preconditions.checkNotNull(e);
/*     */     }
/* 374 */     if (!hasSameComparator) {
/* 375 */       sort(array, comparator);
/* 376 */       array = removeDupes(array, comparator);
/*     */     } 
/* 378 */     return new RegularImmutableSortedSet<E>(array, comparator);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> Object[] newObjectArray(Iterable<T> iterable) {
/* 383 */     Collection<T> collection = (iterable instanceof Collection) ? (Collection<T>)iterable : Lists.<T>newArrayList(iterable);
/*     */     
/* 385 */     Object[] array = new Object[collection.size()];
/* 386 */     return collection.toArray(array);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <E> ImmutableSortedSet<E> copyOfInternal(Comparator<? super E> comparator, Iterator<? extends E> elements) {
/* 391 */     if (!elements.hasNext()) {
/* 392 */       return emptySet(comparator);
/*     */     }
/* 394 */     List<E> list = Lists.newArrayList();
/* 395 */     while (elements.hasNext()) {
/* 396 */       list.add((E)Preconditions.checkNotNull(elements.next()));
/*     */     }
/* 398 */     Object[] array = list.toArray();
/* 399 */     sort(array, comparator);
/* 400 */     array = removeDupes(array, comparator);
/* 401 */     return new RegularImmutableSortedSet<E>(array, comparator);
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
/*     */   static boolean hasSameComparator(Iterable<?> elements, Comparator<?> comparator) {
/* 413 */     if (elements instanceof SortedSet) {
/* 414 */       SortedSet<?> sortedSet = (SortedSet)elements;
/* 415 */       Comparator<?> comparator2 = sortedSet.comparator();
/* 416 */       return (comparator2 == null) ? ((comparator == Ordering.natural())) : comparator.equals(comparator2);
/*     */     } 
/*     */ 
/*     */     
/* 420 */     return false;
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
/*     */   public static <E> Builder<E> orderedBy(Comparator<E> comparator) {
/* 432 */     return new Builder<E>(comparator);
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
/*     */   public static <E extends Comparable<E>> Builder<E> reverseOrder() {
/* 445 */     return new Builder<E>(Ordering.<Comparable>natural().reverse());
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
/*     */   public static <E extends Comparable<E>> Builder<E> naturalOrder() {
/* 461 */     return new Builder<E>(Ordering.natural());
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
/*     */   public static final class Builder<E>
/*     */     extends ImmutableSet.Builder<E>
/*     */   {
/*     */     private final Comparator<? super E> comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder(Comparator<? super E> comparator) {
/* 489 */       this.comparator = (Comparator<? super E>)Preconditions.checkNotNull(comparator);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> add(E element) {
/* 503 */       super.add(element);
/* 504 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> add(E... elements) {
/* 516 */       super.add(elements);
/* 517 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> addAll(Iterable<? extends E> elements) {
/* 529 */       super.addAll(elements);
/* 530 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 542 */       super.addAll(elements);
/* 543 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableSortedSet<E> build() {
/* 551 */       return ImmutableSortedSet.copyOfInternal(this.comparator, this.contents.iterator());
/*     */     }
/*     */   }
/*     */   
/*     */   int unsafeCompare(Object a, Object b) {
/* 556 */     return unsafeCompare(this.comparator, a, b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int unsafeCompare(Comparator<?> comparator, Object a, Object b) {
/* 565 */     Comparator<Object> unsafeComparator = (Comparator)comparator;
/* 566 */     return unsafeComparator.compare(a, b);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableSortedSet(Comparator<? super E> comparator) {
/* 572 */     this.comparator = comparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<? super E> comparator() {
/* 583 */     return this.comparator;
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
/*     */   public ImmutableSortedSet<E> headSet(E toElement) {
/* 598 */     return headSetImpl((E)Preconditions.checkNotNull(toElement));
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
/*     */   public ImmutableSortedSet<E> subSet(E fromElement, E toElement) {
/* 615 */     Preconditions.checkNotNull(fromElement);
/* 616 */     Preconditions.checkNotNull(toElement);
/* 617 */     Preconditions.checkArgument((this.comparator.compare(fromElement, toElement) <= 0));
/* 618 */     return subSetImpl(fromElement, toElement);
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
/*     */   public ImmutableSortedSet<E> tailSet(E fromElement) {
/* 633 */     return tailSetImpl((E)Preconditions.checkNotNull(fromElement));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SerializedForm<E>
/*     */     implements Serializable
/*     */   {
/*     */     final Comparator<? super E> comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final Object[] elements;
/*     */ 
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SerializedForm(Comparator<? super E> comparator, Object[] elements) {
/* 658 */       this.comparator = comparator;
/* 659 */       this.elements = elements;
/*     */     }
/*     */ 
/*     */     
/*     */     Object readResolve() {
/* 664 */       return (new ImmutableSortedSet.Builder((Comparator)this.comparator)).add(this.elements).build();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 672 */     throw new InvalidObjectException("Use SerializedForm");
/*     */   }
/*     */   
/*     */   Object writeReplace() {
/* 676 */     return new SerializedForm<E>(this.comparator, toArray());
/*     */   }
/*     */   
/*     */   abstract ImmutableSortedSet<E> headSetImpl(E paramE);
/*     */   
/*     */   abstract ImmutableSortedSet<E> subSetImpl(E paramE1, E paramE2);
/*     */   
/*     */   abstract ImmutableSortedSet<E> tailSetImpl(E paramE);
/*     */   
/*     */   abstract boolean hasPartialArray();
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ImmutableSortedSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */