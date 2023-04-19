/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Joiner;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumMap;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.TreeMap;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible
/*      */ public final class Maps
/*      */ {
/*      */   public static <K, V> HashMap<K, V> newHashMap() {
/*   74 */     return new HashMap<K, V>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int expectedSize) {
/*   93 */     return new HashMap<K, V>(capacity(expectedSize));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int capacity(int expectedSize) {
/*  105 */     Preconditions.checkArgument((expectedSize >= 0));
/*  106 */     return Math.max(expectedSize * 2, 16);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> HashMap<K, V> newHashMap(Map<? extends K, ? extends V> map) {
/*  125 */     return new HashMap<K, V>(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
/*  138 */     return new LinkedHashMap<K, V>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Map<? extends K, ? extends V> map) {
/*  154 */     return new LinkedHashMap<K, V>(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K extends Comparable, V> TreeMap<K, V> newTreeMap() {
/*  168 */     return new TreeMap<K, V>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, ? extends V> map) {
/*  184 */     return new TreeMap<K, V>(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <C, K extends C, V> TreeMap<K, V> newTreeMap(@Nullable Comparator<C> comparator) {
/*  204 */     return new TreeMap<K, V>(comparator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> type) {
/*  214 */     return new EnumMap<K, V>((Class<K>)Preconditions.checkNotNull(type));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Map<K, ? extends V> map) {
/*  228 */     return new EnumMap<K, V>(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
/*  237 */     return new IdentityHashMap<K, V>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> BiMap<K, V> synchronizedBiMap(BiMap<K, V> bimap) {
/*  269 */     return Synchronized.biMap(bimap, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right) {
/*  290 */     Map<K, V> onlyOnLeft = newHashMap();
/*  291 */     Map<K, V> onlyOnRight = new HashMap<K, V>(right);
/*  292 */     Map<K, V> onBoth = newHashMap();
/*  293 */     Map<K, MapDifference.ValueDifference<V>> differences = newHashMap();
/*  294 */     boolean eq = true;
/*      */     
/*  296 */     for (Map.Entry<? extends K, ? extends V> entry : left.entrySet()) {
/*  297 */       K leftKey = entry.getKey();
/*  298 */       V leftValue = entry.getValue();
/*  299 */       if (right.containsKey(leftKey)) {
/*  300 */         V rightValue = onlyOnRight.remove(leftKey);
/*  301 */         if (Objects.equal(leftValue, rightValue)) {
/*  302 */           onBoth.put(leftKey, leftValue); continue;
/*      */         } 
/*  304 */         eq = false;
/*  305 */         differences.put(leftKey, new ValueDifferenceImpl<V>(leftValue, rightValue));
/*      */         
/*      */         continue;
/*      */       } 
/*  309 */       eq = false;
/*  310 */       onlyOnLeft.put(leftKey, leftValue);
/*      */     } 
/*      */ 
/*      */     
/*  314 */     boolean areEqual = (eq && onlyOnRight.isEmpty());
/*  315 */     return new MapDifferenceImpl<K, V>(areEqual, onlyOnLeft, onlyOnRight, onBoth, differences);
/*      */   }
/*      */ 
/*      */   
/*      */   private static class MapDifferenceImpl<K, V>
/*      */     implements MapDifference<K, V>
/*      */   {
/*      */     final boolean areEqual;
/*      */     
/*      */     final Map<K, V> onlyOnLeft;
/*      */     final Map<K, V> onlyOnRight;
/*      */     final Map<K, V> onBoth;
/*      */     final Map<K, MapDifference.ValueDifference<V>> differences;
/*      */     
/*      */     MapDifferenceImpl(boolean areEqual, Map<K, V> onlyOnLeft, Map<K, V> onlyOnRight, Map<K, V> onBoth, Map<K, MapDifference.ValueDifference<V>> differences) {
/*  330 */       this.areEqual = areEqual;
/*  331 */       this.onlyOnLeft = Collections.unmodifiableMap(onlyOnLeft);
/*  332 */       this.onlyOnRight = Collections.unmodifiableMap(onlyOnRight);
/*  333 */       this.onBoth = Collections.unmodifiableMap(onBoth);
/*  334 */       this.differences = Collections.unmodifiableMap(differences);
/*      */     }
/*      */     
/*      */     public boolean areEqual() {
/*  338 */       return this.areEqual;
/*      */     }
/*      */     
/*      */     public Map<K, V> entriesOnlyOnLeft() {
/*  342 */       return this.onlyOnLeft;
/*      */     }
/*      */     
/*      */     public Map<K, V> entriesOnlyOnRight() {
/*  346 */       return this.onlyOnRight;
/*      */     }
/*      */     
/*      */     public Map<K, V> entriesInCommon() {
/*  350 */       return this.onBoth;
/*      */     }
/*      */     
/*      */     public Map<K, MapDifference.ValueDifference<V>> entriesDiffering() {
/*  354 */       return this.differences;
/*      */     }
/*      */     
/*      */     public boolean equals(Object object) {
/*  358 */       if (object == this) {
/*  359 */         return true;
/*      */       }
/*  361 */       if (object instanceof MapDifference) {
/*  362 */         MapDifference<?, ?> other = (MapDifference<?, ?>)object;
/*  363 */         return (entriesOnlyOnLeft().equals(other.entriesOnlyOnLeft()) && entriesOnlyOnRight().equals(other.entriesOnlyOnRight()) && entriesInCommon().equals(other.entriesInCommon()) && entriesDiffering().equals(other.entriesDiffering()));
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  368 */       return false;
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  372 */       return Objects.hashCode(new Object[] { entriesOnlyOnLeft(), entriesOnlyOnRight(), entriesInCommon(), entriesDiffering() });
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  377 */       if (this.areEqual) {
/*  378 */         return "equal";
/*      */       }
/*      */       
/*  381 */       StringBuilder result = new StringBuilder("not equal");
/*  382 */       if (!this.onlyOnLeft.isEmpty()) {
/*  383 */         result.append(": only on left=").append(this.onlyOnLeft);
/*      */       }
/*  385 */       if (!this.onlyOnRight.isEmpty()) {
/*  386 */         result.append(": only on right=").append(this.onlyOnRight);
/*      */       }
/*  388 */       if (!this.differences.isEmpty()) {
/*  389 */         result.append(": value differences=").append(this.differences);
/*      */       }
/*  391 */       return result.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   static class ValueDifferenceImpl<V>
/*      */     implements MapDifference.ValueDifference<V>
/*      */   {
/*      */     private final V left;
/*      */     private final V right;
/*      */     
/*      */     ValueDifferenceImpl(@Nullable V left, @Nullable V right) {
/*  402 */       this.left = left;
/*  403 */       this.right = right;
/*      */     }
/*      */     
/*      */     public V leftValue() {
/*  407 */       return this.left;
/*      */     }
/*      */     
/*      */     public V rightValue() {
/*  411 */       return this.right;
/*      */     }
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/*  415 */       if (object instanceof MapDifference.ValueDifference) {
/*  416 */         MapDifference.ValueDifference<?> that = (MapDifference.ValueDifference)object;
/*      */         
/*  418 */         return (Objects.equal(this.left, that.leftValue()) && Objects.equal(this.right, that.rightValue()));
/*      */       } 
/*      */       
/*  421 */       return false;
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  425 */       return Objects.hashCode(new Object[] { this.left, this.right });
/*      */     }
/*      */     
/*      */     public String toString() {
/*  429 */       return "(" + this.left + ", " + this.right + ")";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterable<V> values, Function<? super V, K> keyFunction) {
/*  451 */     Preconditions.checkNotNull(keyFunction);
/*  452 */     ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
/*  453 */     for (V value : values) {
/*  454 */       builder.put((K)keyFunction.apply(value), value);
/*      */     }
/*  456 */     return builder.build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ImmutableMap<String, String> fromProperties(Properties properties) {
/*  475 */     ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
/*      */     
/*  477 */     for (Enumeration<?> e = properties.propertyNames(); e.hasMoreElements(); ) {
/*  478 */       String key = (String)e.nextElement();
/*  479 */       builder.put(key, properties.getProperty(key));
/*      */     } 
/*      */     
/*  482 */     return builder.build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Map.Entry<K, V> immutableEntry(@Nullable K key, @Nullable V value) {
/*  496 */     return new ImmutableEntry<K, V>(key, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> Set<Map.Entry<K, V>> unmodifiableEntrySet(Set<Map.Entry<K, V>> entrySet) {
/*  509 */     return new UnmodifiableEntrySet<K, V>(Collections.unmodifiableSet(entrySet));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> Map.Entry<K, V> unmodifiableEntry(final Map.Entry<K, V> entry) {
/*  524 */     Preconditions.checkNotNull(entry);
/*  525 */     return new AbstractMapEntry<K, V>() {
/*      */         public K getKey() {
/*  527 */           return (K)entry.getKey();
/*      */         }
/*      */         public V getValue() {
/*  530 */           return (V)entry.getValue();
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   static class UnmodifiableEntries<K, V>
/*      */     extends ForwardingCollection<Map.Entry<K, V>>
/*      */   {
/*      */     private final Collection<Map.Entry<K, V>> entries;
/*      */     
/*      */     UnmodifiableEntries(Collection<Map.Entry<K, V>> entries) {
/*  541 */       this.entries = entries;
/*      */     }
/*      */     
/*      */     protected Collection<Map.Entry<K, V>> delegate() {
/*  545 */       return this.entries;
/*      */     }
/*      */     
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/*  549 */       final Iterator<Map.Entry<K, V>> delegate = super.iterator();
/*  550 */       return new ForwardingIterator<Map.Entry<K, V>>() {
/*      */           public Map.Entry<K, V> next() {
/*  552 */             return Maps.unmodifiableEntry(super.next());
/*      */           }
/*      */           protected Iterator<Map.Entry<K, V>> delegate() {
/*  555 */             return delegate;
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/*  563 */       return ObjectArrays.toArrayImpl(this);
/*      */     }
/*      */     
/*      */     public <T> T[] toArray(T[] array) {
/*  567 */       return ObjectArrays.toArrayImpl(this, array);
/*      */     }
/*      */     
/*      */     public boolean contains(Object o) {
/*  571 */       return Maps.containsEntryImpl(delegate(), o);
/*      */     }
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  575 */       return Collections2.containsAll(this, c);
/*      */     }
/*      */   }
/*      */   
/*      */   static class UnmodifiableEntrySet<K, V>
/*      */     extends UnmodifiableEntries<K, V>
/*      */     implements Set<Map.Entry<K, V>>
/*      */   {
/*      */     UnmodifiableEntrySet(Set<Map.Entry<K, V>> entries) {
/*  584 */       super(entries);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/*  590 */       return Collections2.setEquals(this, object);
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  594 */       return Sets.hashCodeImpl(this);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> BiMap<K, V> unmodifiableBiMap(BiMap<? extends K, ? extends V> bimap) {
/*  613 */     return new UnmodifiableBiMap<K, V>(bimap, null);
/*      */   }
/*      */   
/*      */   private static class UnmodifiableBiMap<K, V>
/*      */     extends ForwardingMap<K, V>
/*      */     implements BiMap<K, V>, Serializable
/*      */   {
/*      */     final Map<K, V> unmodifiableMap;
/*      */     final BiMap<? extends K, ? extends V> delegate;
/*      */     transient BiMap<V, K> inverse;
/*      */     transient Set<V> values;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableBiMap(BiMap<? extends K, ? extends V> delegate, @Nullable BiMap<V, K> inverse) {
/*  627 */       this.unmodifiableMap = Collections.unmodifiableMap(delegate);
/*  628 */       this.delegate = delegate;
/*  629 */       this.inverse = inverse;
/*      */     }
/*      */     
/*      */     protected Map<K, V> delegate() {
/*  633 */       return this.unmodifiableMap;
/*      */     }
/*      */     
/*      */     public V forcePut(K key, V value) {
/*  637 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public BiMap<V, K> inverse() {
/*  641 */       BiMap<V, K> result = this.inverse;
/*  642 */       return (result == null) ? (this.inverse = new UnmodifiableBiMap(this.delegate.inverse(), this)) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<V> values() {
/*  648 */       Set<V> result = this.values;
/*  649 */       return (result == null) ? (this.values = Collections.unmodifiableSet(this.delegate.values())) : result;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> boolean containsEntryImpl(Collection<Map.Entry<K, V>> c, Object o) {
/*  671 */     if (!(o instanceof Map.Entry)) {
/*  672 */       return false;
/*      */     }
/*  674 */     return c.contains(unmodifiableEntry((Map.Entry<?, ?>)o));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> boolean removeEntryImpl(Collection<Map.Entry<K, V>> c, Object o) {
/*  691 */     if (!(o instanceof Map.Entry)) {
/*  692 */       return false;
/*      */     }
/*  694 */     return c.remove(unmodifiableEntry((Map.Entry<?, ?>)o));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V1, V2> Map<K, V2> transformValues(Map<K, V1> fromMap, Function<? super V1, V2> function) {
/*  735 */     return new TransformedValuesMap<K, V1, V2>(fromMap, function);
/*      */   }
/*      */   
/*      */   private static class TransformedValuesMap<K, V1, V2>
/*      */     extends AbstractMap<K, V2> {
/*      */     final Map<K, V1> fromMap;
/*      */     final Function<? super V1, V2> function;
/*      */     EntrySet entrySet;
/*      */     
/*      */     TransformedValuesMap(Map<K, V1> fromMap, Function<? super V1, V2> function) {
/*  745 */       this.fromMap = (Map<K, V1>)Preconditions.checkNotNull(fromMap);
/*  746 */       this.function = (Function<? super V1, V2>)Preconditions.checkNotNull(function);
/*      */     }
/*      */     
/*      */     public int size() {
/*  750 */       return this.fromMap.size();
/*      */     }
/*      */     
/*      */     public boolean containsKey(Object key) {
/*  754 */       return this.fromMap.containsKey(key);
/*      */     }
/*      */     
/*      */     public V2 get(Object key) {
/*  758 */       V1 value = this.fromMap.get(key);
/*  759 */       return (value != null || this.fromMap.containsKey(key)) ? (V2)this.function.apply(value) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public V2 remove(Object key) {
/*  764 */       return this.fromMap.containsKey(key) ? (V2)this.function.apply(this.fromMap.remove(key)) : null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/*  770 */       this.fromMap.clear();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V2>> entrySet() {
/*  776 */       EntrySet result = this.entrySet;
/*  777 */       if (result == null) {
/*  778 */         this.entrySet = result = new EntrySet();
/*      */       }
/*  780 */       return result;
/*      */     }
/*      */     
/*      */     class EntrySet extends AbstractSet<Map.Entry<K, V2>> {
/*      */       public int size() {
/*  785 */         return Maps.TransformedValuesMap.this.size();
/*      */       }
/*      */       
/*      */       public Iterator<Map.Entry<K, V2>> iterator() {
/*  789 */         final Iterator<Map.Entry<K, V1>> mapIterator = Maps.TransformedValuesMap.this.fromMap.entrySet().iterator();
/*      */ 
/*      */         
/*  792 */         return new Iterator<Map.Entry<K, V2>>() {
/*      */             public boolean hasNext() {
/*  794 */               return mapIterator.hasNext();
/*      */             }
/*      */             
/*      */             public Map.Entry<K, V2> next() {
/*  798 */               final Map.Entry<K, V1> entry = mapIterator.next();
/*  799 */               return new AbstractMapEntry<K, V2>() {
/*      */                   public K getKey() {
/*  801 */                     return (K)entry.getKey();
/*      */                   }
/*      */                   public V2 getValue() {
/*  804 */                     return (V2)Maps.TransformedValuesMap.this.function.apply(entry.getValue());
/*      */                   }
/*      */                 };
/*      */             }
/*      */             
/*      */             public void remove() {
/*  810 */               mapIterator.remove();
/*      */             }
/*      */           };
/*      */       }
/*      */       
/*      */       public void clear() {
/*  816 */         Maps.TransformedValuesMap.this.fromMap.clear();
/*      */       }
/*      */       
/*      */       public boolean contains(Object o) {
/*  820 */         if (!(o instanceof Map.Entry)) {
/*  821 */           return false;
/*      */         }
/*  823 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/*  824 */         Object entryKey = entry.getKey();
/*  825 */         Object entryValue = entry.getValue();
/*  826 */         V2 mapValue = (V2)Maps.TransformedValuesMap.this.get(entryKey);
/*  827 */         if (mapValue != null) {
/*  828 */           return mapValue.equals(entryValue);
/*      */         }
/*  830 */         return (entryValue == null && Maps.TransformedValuesMap.this.containsKey(entryKey));
/*      */       }
/*      */       
/*      */       public boolean remove(Object o) {
/*  834 */         if (contains(o)) {
/*  835 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/*  836 */           Object key = entry.getKey();
/*  837 */           Maps.TransformedValuesMap.this.fromMap.remove(key);
/*  838 */           return true;
/*      */         } 
/*  840 */         return false;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Map<K, V> filterKeys(Map<K, V> unfiltered, final Predicate<? super K> keyPredicate) {
/*  871 */     Preconditions.checkNotNull(keyPredicate);
/*  872 */     Predicate<Map.Entry<K, V>> entryPredicate = new Predicate<Map.Entry<K, V>>() {
/*      */         public boolean apply(Map.Entry<K, V> input) {
/*  874 */           return keyPredicate.apply(input.getKey());
/*      */         }
/*      */       };
/*  877 */     return (unfiltered instanceof AbstractFilteredMap) ? filterFiltered((AbstractFilteredMap<K, V>)unfiltered, entryPredicate) : new FilteredKeyMap<K, V>((Map<K, V>)Preconditions.checkNotNull(unfiltered), keyPredicate, entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Map<K, V> filterValues(Map<K, V> unfiltered, final Predicate<? super V> valuePredicate) {
/*  909 */     Preconditions.checkNotNull(valuePredicate);
/*  910 */     Predicate<Map.Entry<K, V>> entryPredicate = new Predicate<Map.Entry<K, V>>() {
/*      */         public boolean apply(Map.Entry<K, V> input) {
/*  912 */           return valuePredicate.apply(input.getValue());
/*      */         }
/*      */       };
/*  915 */     return filterEntries(unfiltered, entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Map<K, V> filterEntries(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/*  946 */     Preconditions.checkNotNull(entryPredicate);
/*  947 */     return (unfiltered instanceof AbstractFilteredMap) ? filterFiltered((AbstractFilteredMap<K, V>)unfiltered, entryPredicate) : new FilteredEntryMap<K, V>((Map<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> Map<K, V> filterFiltered(AbstractFilteredMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/*  959 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.predicate, entryPredicate);
/*      */     
/*  961 */     return new FilteredEntryMap<K, V>(map.unfiltered, predicate);
/*      */   }
/*      */   
/*      */   private static abstract class AbstractFilteredMap<K, V>
/*      */     extends AbstractMap<K, V>
/*      */   {
/*      */     final Map<K, V> unfiltered;
/*      */     final Predicate<? super Map.Entry<K, V>> predicate;
/*      */     Collection<V> values;
/*      */     
/*      */     AbstractFilteredMap(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate) {
/*  972 */       this.unfiltered = unfiltered;
/*  973 */       this.predicate = predicate;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean apply(Object key, V value) {
/*  980 */       K k = (K)key;
/*  981 */       return this.predicate.apply(Maps.immutableEntry(k, value));
/*      */     }
/*      */     
/*      */     public V put(K key, V value) {
/*  985 */       Preconditions.checkArgument(apply(key, value));
/*  986 */       return this.unfiltered.put(key, value);
/*      */     }
/*      */     
/*      */     public void putAll(Map<? extends K, ? extends V> map) {
/*  990 */       for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/*  991 */         Preconditions.checkArgument(apply(entry.getKey(), entry.getValue()));
/*      */       }
/*  993 */       this.unfiltered.putAll(map);
/*      */     }
/*      */     
/*      */     public boolean containsKey(Object key) {
/*  997 */       return (this.unfiltered.containsKey(key) && apply(key, this.unfiltered.get(key)));
/*      */     }
/*      */     
/*      */     public V get(Object key) {
/* 1001 */       V value = this.unfiltered.get(key);
/* 1002 */       return (value != null && apply(key, value)) ? value : null;
/*      */     }
/*      */     
/*      */     public boolean isEmpty() {
/* 1006 */       return entrySet().isEmpty();
/*      */     }
/*      */     
/*      */     public V remove(Object key) {
/* 1010 */       return containsKey(key) ? this.unfiltered.remove(key) : null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/* 1016 */       Collection<V> result = this.values;
/* 1017 */       return (result == null) ? (this.values = new Values()) : result;
/*      */     }
/*      */     
/*      */     class Values extends AbstractCollection<V> {
/*      */       public Iterator<V> iterator() {
/* 1022 */         final Iterator<Map.Entry<K, V>> entryIterator = Maps.AbstractFilteredMap.this.entrySet().iterator();
/* 1023 */         return new UnmodifiableIterator<V>() {
/*      */             public boolean hasNext() {
/* 1025 */               return entryIterator.hasNext();
/*      */             }
/*      */             public V next() {
/* 1028 */               return (V)((Map.Entry)entryIterator.next()).getValue();
/*      */             }
/*      */           };
/*      */       }
/*      */       
/*      */       public int size() {
/* 1034 */         return Maps.AbstractFilteredMap.this.entrySet().size();
/*      */       }
/*      */       
/*      */       public void clear() {
/* 1038 */         Maps.AbstractFilteredMap.this.entrySet().clear();
/*      */       }
/*      */       
/*      */       public boolean isEmpty() {
/* 1042 */         return Maps.AbstractFilteredMap.this.entrySet().isEmpty();
/*      */       }
/*      */       
/*      */       public boolean remove(Object o) {
/* 1046 */         Iterator<Map.Entry<K, V>> iterator = Maps.AbstractFilteredMap.this.unfiltered.entrySet().iterator();
/* 1047 */         while (iterator.hasNext()) {
/* 1048 */           Map.Entry<K, V> entry = iterator.next();
/* 1049 */           if (Objects.equal(o, entry.getValue()) && Maps.AbstractFilteredMap.this.predicate.apply(entry)) {
/* 1050 */             iterator.remove();
/* 1051 */             return true;
/*      */           } 
/*      */         } 
/* 1054 */         return false;
/*      */       }
/*      */       
/*      */       public boolean removeAll(Collection<?> collection) {
/* 1058 */         Preconditions.checkNotNull(collection);
/* 1059 */         boolean changed = false;
/* 1060 */         Iterator<Map.Entry<K, V>> iterator = Maps.AbstractFilteredMap.this.unfiltered.entrySet().iterator();
/* 1061 */         while (iterator.hasNext()) {
/* 1062 */           Map.Entry<K, V> entry = iterator.next();
/* 1063 */           if (collection.contains(entry.getValue()) && Maps.AbstractFilteredMap.this.predicate.apply(entry)) {
/* 1064 */             iterator.remove();
/* 1065 */             changed = true;
/*      */           } 
/*      */         } 
/* 1068 */         return changed;
/*      */       }
/*      */       
/*      */       public boolean retainAll(Collection<?> collection) {
/* 1072 */         Preconditions.checkNotNull(collection);
/* 1073 */         boolean changed = false;
/* 1074 */         Iterator<Map.Entry<K, V>> iterator = Maps.AbstractFilteredMap.this.unfiltered.entrySet().iterator();
/* 1075 */         while (iterator.hasNext()) {
/* 1076 */           Map.Entry<K, V> entry = iterator.next();
/* 1077 */           if (!collection.contains(entry.getValue()) && Maps.AbstractFilteredMap.this.predicate.apply(entry)) {
/*      */             
/* 1079 */             iterator.remove();
/* 1080 */             changed = true;
/*      */           } 
/*      */         } 
/* 1083 */         return changed;
/*      */       }
/*      */ 
/*      */       
/*      */       public Object[] toArray() {
/* 1088 */         return Lists.<V>newArrayList(iterator()).toArray();
/*      */       }
/*      */       
/*      */       public <T> T[] toArray(T[] array) {
/* 1092 */         return (T[])Lists.<V>newArrayList(iterator()).toArray((Object[])array);
/*      */       } }
/*      */   }
/*      */   
/*      */   private static class FilteredKeyMap<K, V> extends AbstractFilteredMap<K, V> {
/*      */     Predicate<? super K> keyPredicate;
/*      */     Set<Map.Entry<K, V>> entrySet;
/*      */     Set<K> keySet;
/*      */     
/*      */     FilteredKeyMap(Map<K, V> unfiltered, Predicate<? super K> keyPredicate, Predicate<Map.Entry<K, V>> entryPredicate) {
/* 1102 */       super(unfiltered, entryPredicate);
/* 1103 */       this.keyPredicate = keyPredicate;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 1109 */       Set<Map.Entry<K, V>> result = this.entrySet;
/* 1110 */       return (result == null) ? (this.entrySet = Sets.filter(this.unfiltered.entrySet(), this.predicate)) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 1118 */       Set<K> result = this.keySet;
/* 1119 */       return (result == null) ? (this.keySet = Sets.filter(this.unfiltered.keySet(), this.keyPredicate)) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 1128 */       return (this.unfiltered.containsKey(key) && this.keyPredicate.apply(key));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class FilteredEntryMap<K, V>
/*      */     extends AbstractFilteredMap<K, V>
/*      */   {
/*      */     final Set<Map.Entry<K, V>> filteredEntrySet;
/*      */     
/*      */     Set<Map.Entry<K, V>> entrySet;
/*      */     Set<K> keySet;
/*      */     
/*      */     FilteredEntryMap(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 1142 */       super(unfiltered, entryPredicate);
/* 1143 */       this.filteredEntrySet = Sets.filter(unfiltered.entrySet(), this.predicate);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 1149 */       Set<Map.Entry<K, V>> result = this.entrySet;
/* 1150 */       return (result == null) ? (this.entrySet = new EntrySet()) : result;
/*      */     }
/*      */     
/*      */     private class EntrySet extends ForwardingSet<Map.Entry<K, V>> {
/*      */       protected Set<Map.Entry<K, V>> delegate() {
/* 1155 */         return Maps.FilteredEntryMap.this.filteredEntrySet;
/*      */       }
/*      */       private EntrySet() {}
/*      */       public Iterator<Map.Entry<K, V>> iterator() {
/* 1159 */         final Iterator<Map.Entry<K, V>> iterator = Maps.FilteredEntryMap.this.filteredEntrySet.iterator();
/* 1160 */         return new UnmodifiableIterator<Map.Entry<K, V>>() {
/*      */             public boolean hasNext() {
/* 1162 */               return iterator.hasNext();
/*      */             }
/*      */             public Map.Entry<K, V> next() {
/* 1165 */               final Map.Entry<K, V> entry = iterator.next();
/* 1166 */               return new ForwardingMapEntry<K, V>() {
/*      */                   protected Map.Entry<K, V> delegate() {
/* 1168 */                     return entry;
/*      */                   }
/*      */                   public V setValue(V value) {
/* 1171 */                     Preconditions.checkArgument(Maps.FilteredEntryMap.this.apply(entry.getKey(), value));
/* 1172 */                     return super.setValue(value);
/*      */                   }
/*      */                 };
/*      */             }
/*      */           };
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 1183 */       Set<K> result = this.keySet;
/* 1184 */       return (result == null) ? (this.keySet = new KeySet()) : result;
/*      */     }
/*      */     private class KeySet extends AbstractSet<K> { private KeySet() {}
/*      */       
/*      */       public Iterator<K> iterator() {
/* 1189 */         final Iterator<Map.Entry<K, V>> iterator = Maps.FilteredEntryMap.this.filteredEntrySet.iterator();
/* 1190 */         return new UnmodifiableIterator<K>() {
/*      */             public boolean hasNext() {
/* 1192 */               return iterator.hasNext();
/*      */             }
/*      */             public K next() {
/* 1195 */               return (K)((Map.Entry)iterator.next()).getKey();
/*      */             }
/*      */           };
/*      */       }
/*      */       
/*      */       public int size() {
/* 1201 */         return Maps.FilteredEntryMap.this.filteredEntrySet.size();
/*      */       }
/*      */       
/*      */       public void clear() {
/* 1205 */         Maps.FilteredEntryMap.this.filteredEntrySet.clear();
/*      */       }
/*      */       
/*      */       public boolean contains(Object o) {
/* 1209 */         return Maps.FilteredEntryMap.this.containsKey(o);
/*      */       }
/*      */       
/*      */       public boolean remove(Object o) {
/* 1213 */         if (Maps.FilteredEntryMap.this.containsKey(o)) {
/* 1214 */           Maps.FilteredEntryMap.this.unfiltered.remove(o);
/* 1215 */           return true;
/*      */         } 
/* 1217 */         return false;
/*      */       }
/*      */       
/*      */       public boolean removeAll(Collection<?> collection) {
/* 1221 */         Preconditions.checkNotNull(collection);
/* 1222 */         boolean changed = false;
/* 1223 */         for (Object obj : collection) {
/* 1224 */           changed |= remove(obj);
/*      */         }
/* 1226 */         return changed;
/*      */       }
/*      */       
/*      */       public boolean retainAll(Collection<?> collection) {
/* 1230 */         Preconditions.checkNotNull(collection);
/* 1231 */         boolean changed = false;
/* 1232 */         Iterator<Map.Entry<K, V>> iterator = Maps.FilteredEntryMap.this.unfiltered.entrySet().iterator();
/* 1233 */         while (iterator.hasNext()) {
/* 1234 */           Map.Entry<K, V> entry = iterator.next();
/* 1235 */           if (!collection.contains(entry.getKey()) && Maps.FilteredEntryMap.this.predicate.apply(entry)) {
/* 1236 */             iterator.remove();
/* 1237 */             changed = true;
/*      */           } 
/*      */         } 
/* 1240 */         return changed;
/*      */       }
/*      */ 
/*      */       
/*      */       public Object[] toArray() {
/* 1245 */         return Lists.<K>newArrayList(iterator()).toArray();
/*      */       }
/*      */       
/*      */       public <T> T[] toArray(T[] array) {
/* 1249 */         return (T[])Lists.<K>newArrayList(iterator()).toArray((Object[])array);
/*      */       } }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible
/*      */   static abstract class ImprovedAbstractMap<K, V>
/*      */     extends AbstractMap<K, V>
/*      */   {
/*      */     private transient Set<Map.Entry<K, V>> entrySet;
/*      */ 
/*      */     
/*      */     private transient Set<K> keySet;
/*      */ 
/*      */     
/*      */     private transient Collection<V> values;
/*      */ 
/*      */ 
/*      */     
/*      */     protected abstract Set<Map.Entry<K, V>> createEntrySet();
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 1275 */       Set<Map.Entry<K, V>> result = this.entrySet;
/* 1276 */       if (result == null) {
/* 1277 */         this.entrySet = result = createEntrySet();
/*      */       }
/* 1279 */       return result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 1285 */       Set<K> result = this.keySet;
/* 1286 */       if (result == null) {
/* 1287 */         final Set<K> delegate = super.keySet();
/* 1288 */         this.keySet = result = new ForwardingSet<K>() {
/*      */             protected Set<K> delegate() {
/* 1290 */               return delegate;
/*      */             }
/*      */             
/*      */             public boolean isEmpty() {
/* 1294 */               return Maps.ImprovedAbstractMap.this.isEmpty();
/*      */             }
/*      */           };
/*      */       } 
/* 1298 */       return result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/* 1304 */       Collection<V> result = this.values;
/* 1305 */       if (result == null) {
/* 1306 */         final Collection<V> delegate = super.values();
/* 1307 */         this.values = result = new ForwardingCollection<V>() {
/*      */             protected Collection<V> delegate() {
/* 1309 */               return delegate;
/*      */             }
/*      */             
/*      */             public boolean isEmpty() {
/* 1313 */               return Maps.ImprovedAbstractMap.this.isEmpty();
/*      */             }
/*      */           };
/*      */       } 
/* 1317 */       return result;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 1328 */       return entrySet().isEmpty();
/*      */     }
/*      */   }
/*      */   
/* 1332 */   static final Joiner.MapJoiner standardJoiner = Collections2.standardJoiner.withKeyValueSeparator("=");
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\Maps.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */