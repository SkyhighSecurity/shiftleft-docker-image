/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class Sets
/*     */ {
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(E anElement, E... otherElements) {
/*  73 */     return new ImmutableEnumSet<E>(EnumSet.of(anElement, otherElements));
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(Iterable<E> elements) {
/*  91 */     Iterator<E> iterator = elements.iterator();
/*  92 */     if (!iterator.hasNext()) {
/*  93 */       return ImmutableSet.of();
/*     */     }
/*  95 */     if (elements instanceof EnumSet) {
/*  96 */       EnumSet<E> enumSetClone = EnumSet.copyOf((EnumSet<E>)elements);
/*  97 */       return new ImmutableEnumSet<E>(enumSetClone);
/*     */     } 
/*  99 */     Enum enum_ = (Enum)iterator.next();
/* 100 */     EnumSet<E> set = EnumSet.of((E)enum_);
/* 101 */     while (iterator.hasNext()) {
/* 102 */       set.add(iterator.next());
/*     */     }
/* 104 */     return new ImmutableEnumSet<E>(set);
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
/*     */   public static <E extends Enum<E>> EnumSet<E> newEnumSet(Iterable<E> iterable, Class<E> elementType) {
/* 128 */     Preconditions.checkNotNull(iterable);
/* 129 */     EnumSet<E> set = EnumSet.noneOf(elementType);
/* 130 */     Iterables.addAll(set, iterable);
/* 131 */     return set;
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
/*     */   public static <E> HashSet<E> newHashSet() {
/* 148 */     return new HashSet<E>();
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
/*     */   public static <E> HashSet<E> newHashSet(E... elements) {
/* 165 */     int capacity = Maps.capacity(elements.length);
/* 166 */     HashSet<E> set = new HashSet<E>(capacity);
/* 167 */     Collections.addAll(set, elements);
/* 168 */     return set;
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
/*     */   public static <E> HashSet<E> newHashSetWithExpectedSize(int expectedSize) {
/* 181 */     return new HashSet<E>(Maps.capacity(expectedSize));
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
/*     */   public static <E> HashSet<E> newHashSet(Iterable<? extends E> elements) {
/* 198 */     if (elements instanceof Collection) {
/*     */       
/* 200 */       Collection<? extends E> collection = (Collection<? extends E>)elements;
/* 201 */       return new HashSet<E>(collection);
/*     */     } 
/* 203 */     return newHashSet(elements.iterator());
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
/*     */   public static <E> HashSet<E> newHashSet(Iterator<? extends E> elements) {
/* 221 */     HashSet<E> set = newHashSet();
/* 222 */     while (elements.hasNext()) {
/* 223 */       set.add(elements.next());
/*     */     }
/* 225 */     return set;
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
/*     */   public static <E> LinkedHashSet<E> newLinkedHashSet() {
/* 239 */     return new LinkedHashSet<E>();
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
/*     */   public static <E> LinkedHashSet<E> newLinkedHashSet(Iterable<? extends E> elements) {
/* 255 */     if (elements instanceof Collection) {
/*     */       
/* 257 */       Collection<? extends E> collection = (Collection<? extends E>)elements;
/* 258 */       return new LinkedHashSet<E>(collection);
/*     */     } 
/* 260 */     LinkedHashSet<E> set = newLinkedHashSet();
/* 261 */     for (E element : elements) {
/* 262 */       set.add(element);
/*     */     }
/* 264 */     return set;
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
/*     */   public static <E extends Comparable> TreeSet<E> newTreeSet() {
/* 281 */     return new TreeSet<E>();
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
/*     */   public static <E extends Comparable> TreeSet<E> newTreeSet(Iterable<? extends E> elements) {
/* 302 */     TreeSet<E> set = newTreeSet();
/* 303 */     for (Comparable comparable : elements) {
/* 304 */       set.add((E)comparable);
/*     */     }
/* 306 */     return set;
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
/*     */   public static <E> TreeSet<E> newTreeSet(Comparator<? super E> comparator) {
/* 321 */     return new TreeSet<E>((Comparator<? super E>)Preconditions.checkNotNull(comparator));
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
/*     */   public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection) {
/* 341 */     if (collection instanceof EnumSet) {
/* 342 */       return EnumSet.complementOf((EnumSet<E>)collection);
/*     */     }
/* 344 */     Preconditions.checkArgument(!collection.isEmpty(), "collection is empty; use the other version of this method");
/*     */     
/* 346 */     Class<E> type = ((Enum<E>)collection.iterator().next()).getDeclaringClass();
/* 347 */     return makeComplementByHand(collection, type);
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
/*     */   public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection, Class<E> type) {
/* 364 */     Preconditions.checkNotNull(collection);
/* 365 */     return (collection instanceof EnumSet) ? EnumSet.<E>complementOf((EnumSet<E>)collection) : makeComplementByHand(collection, type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <E extends Enum<E>> EnumSet<E> makeComplementByHand(Collection<E> collection, Class<E> type) {
/* 372 */     EnumSet<E> result = EnumSet.allOf(type);
/* 373 */     result.removeAll(collection);
/* 374 */     return result;
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
/*     */   public static <E> Set<E> newSetFromMap(Map<E, Boolean> map) {
/* 417 */     return new SetFromMap<E>(map);
/*     */   }
/*     */   
/*     */   private static class SetFromMap<E> extends AbstractSet<E> implements Set<E>, Serializable {
/*     */     private final Map<E, Boolean> m;
/*     */     private transient Set<E> s;
/*     */     static final long serialVersionUID = 0L;
/*     */     
/*     */     SetFromMap(Map<E, Boolean> map) {
/* 426 */       Preconditions.checkArgument(map.isEmpty(), "Map is non-empty");
/* 427 */       this.m = map;
/* 428 */       this.s = map.keySet();
/*     */     }
/*     */     
/*     */     public void clear() {
/* 432 */       this.m.clear();
/*     */     }
/*     */     public int size() {
/* 435 */       return this.m.size();
/*     */     }
/*     */     public boolean isEmpty() {
/* 438 */       return this.m.isEmpty();
/*     */     }
/*     */     public boolean contains(Object o) {
/* 441 */       return this.m.containsKey(o);
/*     */     }
/*     */     public boolean remove(Object o) {
/* 444 */       return (this.m.remove(o) != null);
/*     */     }
/*     */     public boolean add(E e) {
/* 447 */       return (this.m.put(e, Boolean.TRUE) == null);
/*     */     }
/*     */     public Iterator<E> iterator() {
/* 450 */       return this.s.iterator();
/*     */     }
/*     */     public Object[] toArray() {
/* 453 */       return this.s.toArray();
/*     */     }
/*     */     public <T> T[] toArray(T[] a) {
/* 456 */       return this.s.toArray(a);
/*     */     }
/*     */     public String toString() {
/* 459 */       return this.s.toString();
/*     */     }
/*     */     public int hashCode() {
/* 462 */       return this.s.hashCode();
/*     */     }
/*     */     public boolean equals(@Nullable Object object) {
/* 465 */       return (this == object || this.s.equals(object));
/*     */     }
/*     */     public boolean containsAll(Collection<?> c) {
/* 468 */       return this.s.containsAll(c);
/*     */     }
/*     */     public boolean removeAll(Collection<?> c) {
/* 471 */       return this.s.removeAll(c);
/*     */     }
/*     */     public boolean retainAll(Collection<?> c) {
/* 474 */       return this.s.retainAll(c);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 483 */       stream.defaultReadObject();
/* 484 */       this.s = this.m.keySet();
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
/*     */   public static abstract class SetView<E>
/*     */     extends AbstractSet<E>
/*     */   {
/*     */     private SetView() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableSet<E> immutableCopy() {
/* 509 */       return ImmutableSet.copyOf(this);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <S extends Set<E>> S copyInto(S set) {
/* 520 */       set.addAll(this);
/* 521 */       return set;
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
/*     */   public static <E> SetView<E> union(final Set<? extends E> set1, final Set<? extends E> set2) {
/* 542 */     Preconditions.checkNotNull(set1, "set1");
/* 543 */     Preconditions.checkNotNull(set2, "set2");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 548 */     final Set<? extends E> set2minus1 = difference(set2, set1);
/*     */     
/* 550 */     return new SetView<E>() {
/*     */         public int size() {
/* 552 */           return set1.size() + set2minus1.size();
/*     */         }
/*     */         public boolean isEmpty() {
/* 555 */           return (set1.isEmpty() && set2.isEmpty());
/*     */         }
/*     */         public Iterator<E> iterator() {
/* 558 */           return Iterators.unmodifiableIterator(Iterators.concat(set1.iterator(), set2minus1.iterator()));
/*     */         }
/*     */         
/*     */         public boolean contains(Object object) {
/* 562 */           return (set1.contains(object) || set2.contains(object));
/*     */         }
/*     */         public <S extends Set<E>> S copyInto(S set) {
/* 565 */           set.addAll(set1);
/* 566 */           set.addAll(set2);
/* 567 */           return set;
/*     */         }
/*     */         public ImmutableSet<E> immutableCopy() {
/* 570 */           return (new ImmutableSet.Builder<E>()).addAll(set1).addAll(set2).build();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> SetView<E> intersection(final Set<E> set1, final Set<?> set2) {
/* 604 */     Preconditions.checkNotNull(set1, "set1");
/* 605 */     Preconditions.checkNotNull(set2, "set2");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 610 */     final Predicate<Object> inSet2 = Predicates.in(set2);
/* 611 */     return new SetView<E>() {
/*     */         public Iterator<E> iterator() {
/* 613 */           return Iterators.filter(set1.iterator(), inSet2);
/*     */         }
/*     */         public int size() {
/* 616 */           return Iterators.size(iterator());
/*     */         }
/*     */         public boolean isEmpty() {
/* 619 */           return !iterator().hasNext();
/*     */         }
/*     */         public boolean contains(Object object) {
/* 622 */           return (set1.contains(object) && set2.contains(object));
/*     */         }
/*     */         public boolean containsAll(Collection<?> collection) {
/* 625 */           return (set1.containsAll(collection) && set2.containsAll(collection));
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
/*     */   public static <E> SetView<E> difference(final Set<E> set1, final Set<?> set2) {
/* 644 */     Preconditions.checkNotNull(set1, "set1");
/* 645 */     Preconditions.checkNotNull(set2, "set2");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 650 */     final Predicate<Object> notInSet2 = Predicates.not(Predicates.in(set2));
/* 651 */     return new SetView<E>() {
/*     */         public Iterator<E> iterator() {
/* 653 */           return Iterators.filter(set1.iterator(), notInSet2);
/*     */         }
/*     */         public int size() {
/* 656 */           return Iterators.size(iterator());
/*     */         }
/*     */         public boolean isEmpty() {
/* 659 */           return set2.containsAll(set1);
/*     */         }
/*     */         public boolean contains(Object element) {
/* 662 */           return (set1.contains(element) && !set2.contains(element));
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
/*     */   
/*     */   public static <E> Set<E> filter(Set<E> unfiltered, Predicate<? super E> predicate) {
/* 690 */     if (unfiltered instanceof FilteredSet) {
/*     */ 
/*     */       
/* 693 */       FilteredSet<E> filtered = (FilteredSet<E>)unfiltered;
/* 694 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/*     */       
/* 696 */       return new FilteredSet<E>((Set<E>)filtered.unfiltered, combinedPredicate);
/*     */     } 
/*     */ 
/*     */     
/* 700 */     return new FilteredSet<E>((Set<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
/*     */   }
/*     */   
/*     */   private static class FilteredSet<E>
/*     */     extends Collections2.FilteredCollection<E>
/*     */     implements Set<E> {
/*     */     FilteredSet(Set<E> unfiltered, Predicate<? super E> predicate) {
/* 707 */       super(unfiltered, predicate);
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 711 */       return Collections2.setEquals(this, object);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 715 */       return Sets.hashCodeImpl(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int hashCodeImpl(Set<?> s) {
/* 723 */     int hashCode = 0;
/* 724 */     for (Object o : s) {
/* 725 */       hashCode += (o != null) ? o.hashCode() : 0;
/*     */     }
/* 727 */     return hashCode;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\Sets.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */