/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ public class ImmutableSortedMap<K, V>
/*     */   extends ImmutableSortedMapFauxverideShim<K, V>
/*     */   implements SortedMap<K, V>
/*     */ {
/*  63 */   private static final Comparator NATURAL_ORDER = Ordering.natural();
/*  64 */   private static final Map.Entry<?, ?>[] EMPTY_ARRAY = (Map.Entry<?, ?>[])new Map.Entry[0];
/*     */ 
/*     */   
/*  67 */   private static final ImmutableMap<Object, Object> NATURAL_EMPTY_MAP = new ImmutableSortedMap(EMPTY_ARRAY, NATURAL_ORDER);
/*     */   
/*     */   private final transient Map.Entry<K, V>[] entries;
/*     */   
/*     */   private final transient Comparator<? super K> comparator;
/*     */   private final transient int fromIndex;
/*     */   private final transient int toIndex;
/*     */   
/*     */   public static <K, V> ImmutableSortedMap<K, V> of() {
/*  76 */     return (ImmutableSortedMap)NATURAL_EMPTY_MAP;
/*     */   }
/*     */   private transient ImmutableSet<Map.Entry<K, V>> entrySet; private transient ImmutableSortedSet<K> keySet; private transient ImmutableCollection<V> values; private static final long serialVersionUID = 0L;
/*     */   
/*     */   private static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> comparator) {
/*  81 */     if (NATURAL_ORDER.equals(comparator)) {
/*  82 */       return of();
/*     */     }
/*  84 */     return new ImmutableSortedMap<K, V>(EMPTY_ARRAY, comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1) {
/*  93 */     Map.Entry[] arrayOfEntry = { entryOf(k1, v1) };
/*  94 */     return new ImmutableSortedMap<K, V>((Map.Entry<?, ?>[])arrayOfEntry, Ordering.natural());
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
/*     */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2) {
/* 106 */     return (new Builder<K, V>(Ordering.natural())).put(k1, v1).put(k2, v2).build();
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
/*     */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/* 119 */     return (new Builder<K, V>(Ordering.natural())).put(k1, v1).put(k2, v2).put(k3, v3).build();
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
/*     */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 132 */     return (new Builder<K, V>(Ordering.natural())).put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).build();
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
/*     */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 145 */     return (new Builder<K, V>(Ordering.natural())).put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).build();
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
/*     */   public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
/* 170 */     Ordering<K> naturalOrder = Ordering.natural();
/* 171 */     return copyOfInternal(map, naturalOrder);
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
/*     */   public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
/* 187 */     return copyOfInternal(map, (Comparator<? super K>)Preconditions.checkNotNull(comparator));
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
/*     */   public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(SortedMap<K, ? extends V> map) {
/* 204 */     Comparator<? super K> comparator = (map.comparator() == null) ? NATURAL_ORDER : map.comparator();
/*     */     
/* 206 */     return copyOfInternal(map, comparator);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
/* 211 */     boolean sameComparator = false;
/* 212 */     if (map instanceof SortedMap) {
/* 213 */       SortedMap<?, ?> sortedMap = (SortedMap<?, ?>)map;
/* 214 */       Comparator<?> comparator2 = sortedMap.comparator();
/* 215 */       sameComparator = (comparator2 == null) ? ((comparator == NATURAL_ORDER)) : comparator.equals(comparator2);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 220 */     if (sameComparator && map instanceof ImmutableSortedMap) {
/*     */ 
/*     */ 
/*     */       
/* 224 */       ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap)map;
/* 225 */       return kvMap;
/*     */     } 
/*     */ 
/*     */     
/* 229 */     List<Map.Entry<?, ?>> list = Lists.newArrayListWithCapacity(map.size());
/* 230 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 231 */       list.add(entryOf(entry.getKey(), entry.getValue()));
/*     */     }
/* 233 */     Map.Entry[] arrayOfEntry = list.<Map.Entry>toArray(new Map.Entry[list.size()]);
/*     */     
/* 235 */     if (!sameComparator) {
/* 236 */       sortEntries((Map.Entry<?, ?>[])arrayOfEntry, comparator);
/* 237 */       validateEntries((Map.Entry<?, ?>[])arrayOfEntry, comparator);
/*     */     } 
/*     */     
/* 240 */     return new ImmutableSortedMap<K, V>((Map.Entry<?, ?>[])arrayOfEntry, comparator);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void sortEntries(Map.Entry<?, ?>[] entryArray, final Comparator<?> comparator) {
/* 245 */     Comparator<Map.Entry<?, ?>> entryComparator = new Comparator<Map.Entry<?, ?>>() {
/*     */         public int compare(Map.Entry<?, ?> entry1, Map.Entry<?, ?> entry2) {
/* 247 */           return ImmutableSortedSet.unsafeCompare(comparator, entry1.getKey(), entry2.getKey());
/*     */         }
/*     */       };
/*     */     
/* 251 */     Arrays.sort(entryArray, entryComparator);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void validateEntries(Map.Entry<?, ?>[] entryArray, Comparator<?> comparator) {
/* 256 */     for (int i = 1; i < entryArray.length; i++) {
/* 257 */       if (ImmutableSortedSet.unsafeCompare(comparator, entryArray[i - 1].getKey(), entryArray[i].getKey()) == 0)
/*     */       {
/* 259 */         throw new IllegalArgumentException("Duplicate keys in mappings " + entryArray[i - 1] + " and " + entryArray[i]);
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
/*     */   public static <K extends Comparable<K>, V> Builder<K, V> naturalOrder() {
/* 277 */     return new Builder<K, V>(Ordering.natural());
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
/*     */   public static <K, V> Builder<K, V> orderedBy(Comparator<K> comparator) {
/* 289 */     return new Builder<K, V>(comparator);
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
/*     */   public static <K extends Comparable<K>, V> Builder<K, V> reverseOrder() {
/* 302 */     return new Builder<K, V>(Ordering.<Comparable>natural().reverse());
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
/*     */   public static final class Builder<K, V>
/*     */     extends ImmutableMap.Builder<K, V>
/*     */   {
/*     */     private final Comparator<? super K> comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder(Comparator<? super K> comparator) {
/* 331 */       this.comparator = (Comparator<? super K>)Preconditions.checkNotNull(comparator);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> put(K key, V value) {
/* 340 */       this.entries.add(ImmutableMap.entryOf(key, value));
/* 341 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
/* 352 */       for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 353 */         put(entry.getKey(), entry.getValue());
/*     */       }
/* 355 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableSortedMap<K, V> build() {
/* 365 */       Map.Entry[] arrayOfEntry = this.entries.<Map.Entry>toArray(new Map.Entry[this.entries.size()]);
/*     */       
/* 367 */       ImmutableSortedMap.sortEntries((Map.Entry<?, ?>[])arrayOfEntry, this.comparator);
/* 368 */       ImmutableSortedMap.validateEntries((Map.Entry<?, ?>[])arrayOfEntry, this.comparator);
/* 369 */       return new ImmutableSortedMap<K, V>((Map.Entry<?, ?>[])arrayOfEntry, this.comparator);
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
/*     */   private ImmutableSortedMap(Map.Entry<?, ?>[] entries, Comparator<? super K> comparator, int fromIndex, int toIndex) {
/* 382 */     Map.Entry[] arrayOfEntry = (Map.Entry[])entries;
/* 383 */     this.entries = (Map.Entry<K, V>[])arrayOfEntry;
/* 384 */     this.comparator = comparator;
/* 385 */     this.fromIndex = fromIndex;
/* 386 */     this.toIndex = toIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSortedMap(Map.Entry<?, ?>[] entries, Comparator<? super K> comparator) {
/* 391 */     this(entries, comparator, 0, entries.length);
/*     */   }
/*     */   
/*     */   public int size() {
/* 395 */     return this.toIndex - this.fromIndex;
/*     */   }
/*     */   public V get(@Nullable Object key) {
/*     */     int i;
/* 399 */     if (key == null) {
/* 400 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 404 */       i = binarySearch(key);
/* 405 */     } catch (ClassCastException e) {
/* 406 */       return null;
/*     */     } 
/* 408 */     return (i >= 0) ? this.entries[i].getValue() : null;
/*     */   }
/*     */   
/*     */   private int binarySearch(Object key) {
/* 412 */     int lower = this.fromIndex;
/* 413 */     int upper = this.toIndex - 1;
/*     */     
/* 415 */     while (lower <= upper) {
/* 416 */       int middle = lower + (upper - lower) / 2;
/* 417 */       int c = ImmutableSortedSet.unsafeCompare(this.comparator, key, this.entries[middle].getKey());
/*     */       
/* 419 */       if (c < 0) {
/* 420 */         upper = middle - 1; continue;
/* 421 */       }  if (c > 0) {
/* 422 */         lower = middle + 1; continue;
/*     */       } 
/* 424 */       return middle;
/*     */     } 
/*     */ 
/*     */     
/* 428 */     return -lower - 1;
/*     */   }
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/* 432 */     if (value == null) {
/* 433 */       return false;
/*     */     }
/* 435 */     for (int i = this.fromIndex; i < this.toIndex; i++) {
/* 436 */       if (this.entries[i].getValue().equals(value)) {
/* 437 */         return true;
/*     */       }
/*     */     } 
/* 440 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<Map.Entry<K, V>> entrySet() {
/* 450 */     ImmutableSet<Map.Entry<K, V>> es = this.entrySet;
/* 451 */     return (es == null) ? (this.entrySet = createEntrySet()) : es;
/*     */   }
/*     */   
/*     */   private ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/* 455 */     return isEmpty() ? ImmutableSet.<Map.Entry<K, V>>of() : new EntrySet<K, V>(this);
/*     */   }
/*     */   
/*     */   private static class EntrySet<K, V>
/*     */     extends ImmutableSet<Map.Entry<K, V>>
/*     */   {
/*     */     final transient ImmutableSortedMap<K, V> map;
/*     */     
/*     */     EntrySet(ImmutableSortedMap<K, V> map) {
/* 464 */       this.map = map;
/*     */     }
/*     */     
/*     */     public int size() {
/* 468 */       return this.map.size();
/*     */     }
/*     */     
/*     */     public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/* 472 */       return Iterators.forArray((Map.Entry<K, V>[])this.map.entries, this.map.fromIndex, size());
/*     */     }
/*     */     
/*     */     public boolean contains(Object target) {
/* 476 */       if (target instanceof Map.Entry) {
/* 477 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)target;
/* 478 */         V mappedValue = this.map.get(entry.getKey());
/* 479 */         return (mappedValue != null && mappedValue.equals(entry.getValue()));
/*     */       } 
/* 481 */       return false;
/*     */     }
/*     */     
/*     */     Object writeReplace() {
/* 485 */       return new ImmutableSortedMap.EntrySetSerializedForm<K, V>(this.map);
/*     */     } }
/*     */   
/*     */   private static class EntrySetSerializedForm<K, V> implements Serializable { final ImmutableSortedMap<K, V> map;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     EntrySetSerializedForm(ImmutableSortedMap<K, V> map) {
/* 492 */       this.map = map;
/*     */     }
/*     */     Object readResolve() {
/* 495 */       return this.map.entrySet();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedSet<K> keySet() {
/* 506 */     ImmutableSortedSet<K> ks = this.keySet;
/* 507 */     return (ks == null) ? (this.keySet = createKeySet()) : ks;
/*     */   }
/*     */   
/*     */   private ImmutableSortedSet<K> createKeySet() {
/* 511 */     if (isEmpty()) {
/* 512 */       return ImmutableSortedSet.emptySet(this.comparator);
/*     */     }
/*     */ 
/*     */     
/* 516 */     Object[] array = new Object[size()];
/* 517 */     for (int i = this.fromIndex; i < this.toIndex; i++) {
/* 518 */       array[i - this.fromIndex] = this.entries[i].getKey();
/*     */     }
/* 520 */     return new RegularImmutableSortedSet<K>(array, this.comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> values() {
/* 530 */     ImmutableCollection<V> v = this.values;
/* 531 */     return (v == null) ? (this.values = new Values<V>(this)) : v;
/*     */   }
/*     */   
/*     */   private static class Values<V>
/*     */     extends ImmutableCollection<V> {
/*     */     private final ImmutableSortedMap<?, V> map;
/*     */     
/*     */     Values(ImmutableSortedMap<?, V> map) {
/* 539 */       this.map = map;
/*     */     }
/*     */     
/*     */     public int size() {
/* 543 */       return this.map.size();
/*     */     }
/*     */     
/*     */     public UnmodifiableIterator<V> iterator() {
/* 547 */       return new AbstractIterator<V>() {
/* 548 */           int index = ImmutableSortedMap.Values.this.map.fromIndex;
/*     */           protected V computeNext() {
/* 550 */             return (this.index < ImmutableSortedMap.Values.this.map.toIndex) ? ImmutableSortedMap.Values.this.map.entries[this.index++].getValue() : endOfData();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 558 */       return this.map.containsValue(target);
/*     */     }
/*     */     
/*     */     Object writeReplace() {
/* 562 */       return new ImmutableSortedMap.ValuesSerializedForm<V>(this.map);
/*     */     } }
/*     */   
/*     */   private static class ValuesSerializedForm<V> implements Serializable { final ImmutableSortedMap<?, V> map;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ValuesSerializedForm(ImmutableSortedMap<?, V> map) {
/* 569 */       this.map = map;
/*     */     }
/*     */     Object readResolve() {
/* 572 */       return this.map.values();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<? super K> comparator() {
/* 584 */     return this.comparator;
/*     */   }
/*     */   
/*     */   public K firstKey() {
/* 588 */     if (isEmpty()) {
/* 589 */       throw new NoSuchElementException();
/*     */     }
/* 591 */     return this.entries[this.fromIndex].getKey();
/*     */   }
/*     */   
/*     */   public K lastKey() {
/* 595 */     if (isEmpty()) {
/* 596 */       throw new NoSuchElementException();
/*     */     }
/* 598 */     return this.entries[this.toIndex - 1].getKey();
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
/*     */   public ImmutableSortedMap<K, V> headMap(K toKey) {
/* 612 */     int newToIndex = findSubmapIndex((K)Preconditions.checkNotNull(toKey));
/* 613 */     return createSubmap(this.fromIndex, newToIndex);
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
/*     */   public ImmutableSortedMap<K, V> subMap(K fromKey, K toKey) {
/* 630 */     Preconditions.checkNotNull(fromKey);
/* 631 */     Preconditions.checkNotNull(toKey);
/* 632 */     Preconditions.checkArgument((this.comparator.compare(fromKey, toKey) <= 0));
/* 633 */     int newFromIndex = findSubmapIndex(fromKey);
/* 634 */     int newToIndex = findSubmapIndex(toKey);
/* 635 */     return createSubmap(newFromIndex, newToIndex);
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
/*     */   public ImmutableSortedMap<K, V> tailMap(K fromKey) {
/* 649 */     int newFromIndex = findSubmapIndex((K)Preconditions.checkNotNull(fromKey));
/* 650 */     return createSubmap(newFromIndex, this.toIndex);
/*     */   }
/*     */   
/*     */   private int findSubmapIndex(K key) {
/* 654 */     int index = binarySearch(key);
/* 655 */     return (index >= 0) ? index : (-index - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   private ImmutableSortedMap<K, V> createSubmap(int newFromIndex, int newToIndex) {
/* 660 */     if (newFromIndex < newToIndex) {
/* 661 */       return new ImmutableSortedMap((Map.Entry<?, ?>[])this.entries, this.comparator, newFromIndex, newToIndex);
/*     */     }
/*     */     
/* 664 */     return emptyMap(this.comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SerializedForm
/*     */     extends ImmutableMap.SerializedForm
/*     */   {
/*     */     private final Comparator<Object> comparator;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     
/*     */     SerializedForm(ImmutableSortedMap<?, ?> sortedMap) {
/* 678 */       super(sortedMap);
/* 679 */       this.comparator = (Comparator)sortedMap.comparator();
/*     */     }
/*     */     Object readResolve() {
/* 682 */       ImmutableSortedMap.Builder<Object, Object> builder = new ImmutableSortedMap.Builder<Object, Object>(this.comparator);
/* 683 */       return createMap(builder);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 689 */     return new SerializedForm(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ImmutableSortedMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */