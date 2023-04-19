/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Joiner;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Supplier;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.SortedSet;
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
/*      */ public final class Multimaps
/*      */ {
/*      */   public static <K, V> Multimap<K, V> newMultimap(Map<K, Collection<V>> map, Supplier<? extends Collection<V>> factory) {
/*   98 */     return new CustomMultimap<K, V>(map, factory);
/*      */   }
/*      */   
/*      */   private static class CustomMultimap<K, V> extends AbstractMultimap<K, V> {
/*      */     transient Supplier<? extends Collection<V>> factory;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomMultimap(Map<K, Collection<V>> map, Supplier<? extends Collection<V>> factory) {
/*  106 */       super(map);
/*  107 */       this.factory = (Supplier<? extends Collection<V>>)Preconditions.checkNotNull(factory);
/*      */     }
/*      */     
/*      */     protected Collection<V> createCollection() {
/*  111 */       return (Collection<V>)this.factory.get();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  119 */       stream.defaultWriteObject();
/*  120 */       stream.writeObject(this.factory);
/*  121 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  127 */       stream.defaultReadObject();
/*  128 */       this.factory = (Supplier<? extends Collection<V>>)stream.readObject();
/*  129 */       Map<K, Collection<V>> map = (Map<K, Collection<V>>)stream.readObject();
/*  130 */       setMap(map);
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
/*      */   public static <K, V> ListMultimap<K, V> newListMultimap(Map<K, Collection<V>> map, Supplier<? extends List<V>> factory) {
/*  175 */     return new CustomListMultimap<K, V>(map, factory);
/*      */   }
/*      */   
/*      */   private static class CustomListMultimap<K, V>
/*      */     extends AbstractListMultimap<K, V> {
/*      */     transient Supplier<? extends List<V>> factory;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomListMultimap(Map<K, Collection<V>> map, Supplier<? extends List<V>> factory) {
/*  184 */       super(map);
/*  185 */       this.factory = (Supplier<? extends List<V>>)Preconditions.checkNotNull(factory);
/*      */     }
/*      */     
/*      */     protected List<V> createCollection() {
/*  189 */       return (List<V>)this.factory.get();
/*      */     }
/*      */ 
/*      */     
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  194 */       stream.defaultWriteObject();
/*  195 */       stream.writeObject(this.factory);
/*  196 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  202 */       stream.defaultReadObject();
/*  203 */       this.factory = (Supplier<? extends List<V>>)stream.readObject();
/*  204 */       Map<K, Collection<V>> map = (Map<K, Collection<V>>)stream.readObject();
/*  205 */       setMap(map);
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
/*      */   public static <K, V> SetMultimap<K, V> newSetMultimap(Map<K, Collection<V>> map, Supplier<? extends Set<V>> factory) {
/*  250 */     return new CustomSetMultimap<K, V>(map, factory);
/*      */   }
/*      */   
/*      */   private static class CustomSetMultimap<K, V>
/*      */     extends AbstractSetMultimap<K, V> {
/*      */     transient Supplier<? extends Set<V>> factory;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomSetMultimap(Map<K, Collection<V>> map, Supplier<? extends Set<V>> factory) {
/*  259 */       super(map);
/*  260 */       this.factory = (Supplier<? extends Set<V>>)Preconditions.checkNotNull(factory);
/*      */     }
/*      */     
/*      */     protected Set<V> createCollection() {
/*  264 */       return (Set<V>)this.factory.get();
/*      */     }
/*      */ 
/*      */     
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  269 */       stream.defaultWriteObject();
/*  270 */       stream.writeObject(this.factory);
/*  271 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  277 */       stream.defaultReadObject();
/*  278 */       this.factory = (Supplier<? extends Set<V>>)stream.readObject();
/*  279 */       Map<K, Collection<V>> map = (Map<K, Collection<V>>)stream.readObject();
/*  280 */       setMap(map);
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
/*      */   public static <K, V> SortedSetMultimap<K, V> newSortedSetMultimap(Map<K, Collection<V>> map, Supplier<? extends SortedSet<V>> factory) {
/*  325 */     return new CustomSortedSetMultimap<K, V>(map, factory);
/*      */   }
/*      */   
/*      */   private static class CustomSortedSetMultimap<K, V>
/*      */     extends AbstractSortedSetMultimap<K, V> {
/*      */     transient Supplier<? extends SortedSet<V>> factory;
/*      */     transient Comparator<? super V> valueComparator;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomSortedSetMultimap(Map<K, Collection<V>> map, Supplier<? extends SortedSet<V>> factory) {
/*  335 */       super(map);
/*  336 */       this.factory = (Supplier<? extends SortedSet<V>>)Preconditions.checkNotNull(factory);
/*  337 */       this.valueComparator = ((SortedSet<V>)factory.get()).comparator();
/*      */     }
/*      */     
/*      */     protected SortedSet<V> createCollection() {
/*  341 */       return (SortedSet<V>)this.factory.get();
/*      */     }
/*      */     
/*      */     public Comparator<? super V> valueComparator() {
/*  345 */       return this.valueComparator;
/*      */     }
/*      */ 
/*      */     
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  350 */       stream.defaultWriteObject();
/*  351 */       stream.writeObject(this.factory);
/*  352 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  358 */       stream.defaultReadObject();
/*  359 */       this.factory = (Supplier<? extends SortedSet<V>>)stream.readObject();
/*  360 */       this.valueComparator = ((SortedSet<V>)this.factory.get()).comparator();
/*  361 */       Map<K, Collection<V>> map = (Map<K, Collection<V>>)stream.readObject();
/*  362 */       setMap(map);
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
/*      */   public static <K, V, M extends Multimap<K, V>> M invertFrom(Multimap<? extends V, ? extends K> source, M dest) {
/*  378 */     for (Map.Entry<? extends V, ? extends K> entry : source.entries()) {
/*  379 */       dest.put(entry.getValue(), entry.getKey());
/*      */     }
/*  381 */     return dest;
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
/*      */   public static <K, V> Multimap<K, V> synchronizedMultimap(Multimap<K, V> multimap) {
/*  419 */     return Synchronized.multimap(multimap, null);
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
/*      */   public static <K, V> Multimap<K, V> unmodifiableMultimap(Multimap<K, V> delegate) {
/*  441 */     return new UnmodifiableMultimap<K, V>(delegate);
/*      */   }
/*      */   
/*      */   private static class UnmodifiableMultimap<K, V> extends ForwardingMultimap<K, V> implements Serializable {
/*      */     final Multimap<K, V> delegate;
/*      */     transient Collection<Map.Entry<K, V>> entries;
/*      */     transient Multiset<K> keys;
/*      */     transient Set<K> keySet;
/*      */     transient Collection<V> values;
/*      */     transient Map<K, Collection<V>> map;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableMultimap(Multimap<K, V> delegate) {
/*  454 */       this.delegate = delegate;
/*      */     }
/*      */     
/*      */     protected Multimap<K, V> delegate() {
/*  458 */       return this.delegate;
/*      */     }
/*      */     
/*      */     public void clear() {
/*  462 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Map<K, Collection<V>> asMap() {
/*  466 */       Map<K, Collection<V>> result = this.map;
/*  467 */       if (result == null) {
/*  468 */         final Map<K, Collection<V>> unmodifiableMap = Collections.unmodifiableMap(this.delegate.asMap());
/*      */         
/*  470 */         this.map = result = (Map)new ForwardingMap<K, Collection<Collection<Collection<Collection<V>>>>>() {
/*      */             protected Map<K, Collection<V>> delegate() {
/*  472 */               return unmodifiableMap;
/*      */             }
/*      */             
/*      */             Set<Map.Entry<K, Collection<V>>> entrySet;
/*      */             
/*      */             public Set<Map.Entry<K, Collection<V>>> entrySet() {
/*  478 */               Set<Map.Entry<K, Collection<V>>> result = this.entrySet;
/*  479 */               return (result == null) ? (this.entrySet = Multimaps.unmodifiableAsMapEntries(unmodifiableMap.entrySet())) : result;
/*      */             }
/*      */ 
/*      */             
/*      */             Collection<Collection<V>> asMapValues;
/*      */             
/*      */             public Collection<V> get(Object key) {
/*  486 */               Collection<V> collection = (Collection<V>)unmodifiableMap.get(key);
/*  487 */               return (collection == null) ? null : Multimaps.unmodifiableValueCollection(collection);
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             public Collection<Collection<V>> values() {
/*  494 */               Collection<Collection<V>> result = this.asMapValues;
/*  495 */               return (result == null) ? (this.asMapValues = new Multimaps.UnmodifiableAsMapValues<V>(unmodifiableMap.values())) : result;
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             public boolean containsValue(Object o) {
/*  502 */               return values().contains(o);
/*      */             }
/*      */           };
/*      */       } 
/*  506 */       return result;
/*      */     }
/*      */     
/*      */     public Collection<Map.Entry<K, V>> entries() {
/*  510 */       Collection<Map.Entry<K, V>> result = this.entries;
/*  511 */       if (result == null) {
/*  512 */         this.entries = result = Multimaps.unmodifiableEntries(this.delegate.entries());
/*      */       }
/*  514 */       return result;
/*      */     }
/*      */     
/*      */     public Collection<V> get(K key) {
/*  518 */       return Multimaps.unmodifiableValueCollection(this.delegate.get(key));
/*      */     }
/*      */     
/*      */     public Multiset<K> keys() {
/*  522 */       Multiset<K> result = this.keys;
/*  523 */       if (result == null) {
/*  524 */         this.keys = result = Multisets.unmodifiableMultiset(this.delegate.keys());
/*      */       }
/*  526 */       return result;
/*      */     }
/*      */     
/*      */     public Set<K> keySet() {
/*  530 */       Set<K> result = this.keySet;
/*  531 */       if (result == null) {
/*  532 */         this.keySet = result = Collections.unmodifiableSet(this.delegate.keySet());
/*      */       }
/*  534 */       return result;
/*      */     }
/*      */     
/*      */     public boolean put(K key, V value) {
/*  538 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(K key, Iterable<? extends V> values) {
/*  543 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/*  548 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean remove(Object key, Object value) {
/*  552 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Collection<V> removeAll(Object key) {
/*  556 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
/*  561 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Collection<V> values() {
/*  565 */       Collection<V> result = this.values;
/*  566 */       if (result == null) {
/*  567 */         this.values = result = Collections.unmodifiableCollection(this.delegate.values());
/*      */       }
/*  569 */       return result;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class UnmodifiableAsMapValues<V>
/*      */     extends ForwardingCollection<Collection<V>>
/*      */   {
/*      */     UnmodifiableAsMapValues(Collection<Collection<V>> delegate) {
/*  579 */       this.delegate = Collections.unmodifiableCollection(delegate);
/*      */     } final Collection<Collection<V>> delegate;
/*      */     protected Collection<Collection<V>> delegate() {
/*  582 */       return this.delegate;
/*      */     }
/*      */     public Iterator<Collection<V>> iterator() {
/*  585 */       final Iterator<Collection<V>> iterator = this.delegate.iterator();
/*  586 */       return new Iterator<Collection<V>>() {
/*      */           public boolean hasNext() {
/*  588 */             return iterator.hasNext();
/*      */           }
/*      */           public Collection<V> next() {
/*  591 */             return Multimaps.unmodifiableValueCollection(iterator.next());
/*      */           }
/*      */           public void remove() {
/*  594 */             throw new UnsupportedOperationException();
/*      */           }
/*      */         };
/*      */     }
/*      */     public Object[] toArray() {
/*  599 */       return ObjectArrays.toArrayImpl(this);
/*      */     }
/*      */     public <T> T[] toArray(T[] array) {
/*  602 */       return ObjectArrays.toArrayImpl(this, array);
/*      */     }
/*      */     public boolean contains(Object o) {
/*  605 */       return Iterators.contains(iterator(), o);
/*      */     }
/*      */     public boolean containsAll(Collection<?> c) {
/*  608 */       return Collections2.containsAll(this, c);
/*      */     } }
/*      */   
/*      */   private static class UnmodifiableListMultimap<K, V> extends UnmodifiableMultimap<K, V> implements ListMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableListMultimap(ListMultimap<K, V> delegate) {
/*  615 */       super(delegate);
/*      */     }
/*      */     public ListMultimap<K, V> delegate() {
/*  618 */       return (ListMultimap<K, V>)super.delegate();
/*      */     }
/*      */     public List<V> get(K key) {
/*  621 */       return Collections.unmodifiableList(delegate().get(key));
/*      */     }
/*      */     public List<V> removeAll(Object key) {
/*  624 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public List<V> replaceValues(K key, Iterable<? extends V> values) {
/*  628 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class UnmodifiableSetMultimap<K, V> extends UnmodifiableMultimap<K, V> implements SetMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableSetMultimap(SetMultimap<K, V> delegate) {
/*  636 */       super(delegate);
/*      */     }
/*      */     public SetMultimap<K, V> delegate() {
/*  639 */       return (SetMultimap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<V> get(K key) {
/*  646 */       return Collections.unmodifiableSet(delegate().get(key));
/*      */     }
/*      */     public Set<Map.Entry<K, V>> entries() {
/*  649 */       return Maps.unmodifiableEntrySet(delegate().entries());
/*      */     }
/*      */     public Set<V> removeAll(Object key) {
/*  652 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Set<V> replaceValues(K key, Iterable<? extends V> values) {
/*  656 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class UnmodifiableSortedSetMultimap<K, V> extends UnmodifiableSetMultimap<K, V> implements SortedSetMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableSortedSetMultimap(SortedSetMultimap<K, V> delegate) {
/*  664 */       super(delegate);
/*      */     }
/*      */     public SortedSetMultimap<K, V> delegate() {
/*  667 */       return (SortedSetMultimap<K, V>)super.delegate();
/*      */     }
/*      */     public SortedSet<V> get(K key) {
/*  670 */       return Collections.unmodifiableSortedSet(delegate().get(key));
/*      */     }
/*      */     public SortedSet<V> removeAll(Object key) {
/*  673 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) {
/*  677 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     public Comparator<? super V> valueComparator() {
/*  680 */       return delegate().valueComparator();
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
/*      */   public static <K, V> SetMultimap<K, V> synchronizedSetMultimap(SetMultimap<K, V> multimap) {
/*  699 */     return Synchronized.setMultimap(multimap, null);
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
/*      */   public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(SetMultimap<K, V> delegate) {
/*  722 */     return new UnmodifiableSetMultimap<K, V>(delegate);
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
/*      */   public static <K, V> SortedSetMultimap<K, V> synchronizedSortedSetMultimap(SortedSetMultimap<K, V> multimap) {
/*  739 */     return Synchronized.sortedSetMultimap(multimap, null);
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
/*      */   public static <K, V> SortedSetMultimap<K, V> unmodifiableSortedSetMultimap(SortedSetMultimap<K, V> delegate) {
/*  762 */     return new UnmodifiableSortedSetMultimap<K, V>(delegate);
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
/*      */   public static <K, V> ListMultimap<K, V> synchronizedListMultimap(ListMultimap<K, V> multimap) {
/*  776 */     return Synchronized.listMultimap(multimap, null);
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
/*      */   public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(ListMultimap<K, V> delegate) {
/*  799 */     return new UnmodifiableListMultimap<K, V>(delegate);
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
/*      */   private static <V> Collection<V> unmodifiableValueCollection(Collection<V> collection) {
/*  812 */     if (collection instanceof SortedSet)
/*  813 */       return Collections.unmodifiableSortedSet((SortedSet<V>)collection); 
/*  814 */     if (collection instanceof Set)
/*  815 */       return Collections.unmodifiableSet((Set<? extends V>)collection); 
/*  816 */     if (collection instanceof List) {
/*  817 */       return Collections.unmodifiableList((List<? extends V>)collection);
/*      */     }
/*  819 */     return Collections.unmodifiableCollection(collection);
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
/*      */   private static <K, V> Map.Entry<K, Collection<V>> unmodifiableAsMapEntry(final Map.Entry<K, Collection<V>> entry) {
/*  835 */     Preconditions.checkNotNull(entry);
/*  836 */     return (Map.Entry)new AbstractMapEntry<K, Collection<Collection<V>>>() {
/*      */         public K getKey() {
/*  838 */           return (K)entry.getKey();
/*      */         }
/*      */         
/*      */         public Collection<V> getValue() {
/*  842 */           return Multimaps.unmodifiableValueCollection((Collection)entry.getValue());
/*      */         }
/*      */       };
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
/*      */   private static <K, V> Collection<Map.Entry<K, V>> unmodifiableEntries(Collection<Map.Entry<K, V>> entries) {
/*  858 */     if (entries instanceof Set) {
/*  859 */       return Maps.unmodifiableEntrySet((Set<Map.Entry<K, V>>)entries);
/*      */     }
/*  861 */     return new Maps.UnmodifiableEntries<K, V>(Collections.unmodifiableCollection(entries));
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
/*      */   private static <K, V> Set<Map.Entry<K, Collection<V>>> unmodifiableAsMapEntries(Set<Map.Entry<K, Collection<V>>> asMapEntries) {
/*  877 */     return new UnmodifiableAsMapEntries<K, V>(Collections.unmodifiableSet(asMapEntries));
/*      */   }
/*      */   
/*      */   static class UnmodifiableAsMapEntries<K, V>
/*      */     extends ForwardingSet<Map.Entry<K, Collection<V>>>
/*      */   {
/*      */     private final Set<Map.Entry<K, Collection<V>>> delegate;
/*      */     
/*      */     UnmodifiableAsMapEntries(Set<Map.Entry<K, Collection<V>>> delegate) {
/*  886 */       this.delegate = delegate;
/*      */     }
/*      */     
/*      */     protected Set<Map.Entry<K, Collection<V>>> delegate() {
/*  890 */       return this.delegate;
/*      */     }
/*      */     
/*      */     public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/*  894 */       final Iterator<Map.Entry<K, Collection<V>>> iterator = this.delegate.iterator();
/*  895 */       return new ForwardingIterator<Map.Entry<K, Collection<V>>>() {
/*      */           protected Iterator<Map.Entry<K, Collection<V>>> delegate() {
/*  897 */             return iterator;
/*      */           }
/*      */           public Map.Entry<K, Collection<V>> next() {
/*  900 */             return Multimaps.unmodifiableAsMapEntry(iterator.next());
/*      */           }
/*      */         };
/*      */     }
/*      */     
/*      */     public Object[] toArray() {
/*  906 */       return ObjectArrays.toArrayImpl(this);
/*      */     }
/*      */     
/*      */     public <T> T[] toArray(T[] array) {
/*  910 */       return ObjectArrays.toArrayImpl(this, array);
/*      */     }
/*      */     
/*      */     public boolean contains(Object o) {
/*  914 */       return Maps.containsEntryImpl(delegate(), o);
/*      */     }
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  918 */       return Collections2.containsAll(this, c);
/*      */     }
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/*  922 */       return Collections2.setEquals(this, object);
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
/*      */   public static <K, V> SetMultimap<K, V> forMap(Map<K, V> map) {
/*  944 */     return new MapMultimap<K, V>(map);
/*      */   }
/*      */   
/*      */   private static class MapMultimap<K, V>
/*      */     implements SetMultimap<K, V>, Serializable
/*      */   {
/*      */     final Map<K, V> map;
/*      */     transient Map<K, Collection<V>> asMap;
/*      */     
/*      */     MapMultimap(Map<K, V> map) {
/*  954 */       this.map = (Map<K, V>)Preconditions.checkNotNull(map);
/*      */     }
/*      */     
/*      */     public int size() {
/*  958 */       return this.map.size();
/*      */     }
/*      */     
/*      */     public boolean isEmpty() {
/*  962 */       return this.map.isEmpty();
/*      */     }
/*      */     
/*      */     public boolean containsKey(Object key) {
/*  966 */       return this.map.containsKey(key);
/*      */     }
/*      */     
/*      */     public boolean containsValue(Object value) {
/*  970 */       return this.map.containsValue(value);
/*      */     }
/*      */     
/*      */     public boolean containsEntry(Object key, Object value) {
/*  974 */       return this.map.entrySet().contains(Maps.immutableEntry(key, value));
/*      */     }
/*      */     
/*      */     public Set<V> get(final K key) {
/*  978 */       return new AbstractSet<V>() {
/*      */           public Iterator<V> iterator() {
/*  980 */             return new Iterator<V>() {
/*      */                 int i;
/*      */                 
/*      */                 public boolean hasNext() {
/*  984 */                   return (this.i == 0 && Multimaps.MapMultimap.this.map.containsKey(key));
/*      */                 }
/*      */                 
/*      */                 public V next() {
/*  988 */                   if (!hasNext()) {
/*  989 */                     throw new NoSuchElementException();
/*      */                   }
/*  991 */                   this.i++;
/*  992 */                   return (V)Multimaps.MapMultimap.this.map.get(key);
/*      */                 }
/*      */                 
/*      */                 public void remove() {
/*  996 */                   Preconditions.checkState((this.i == 1));
/*  997 */                   this.i = -1;
/*  998 */                   Multimaps.MapMultimap.this.map.remove(key);
/*      */                 }
/*      */               };
/*      */           }
/*      */           
/*      */           public int size() {
/* 1004 */             return Multimaps.MapMultimap.this.map.containsKey(key) ? 1 : 0;
/*      */           }
/*      */         };
/*      */     }
/*      */     
/*      */     public boolean put(K key, V value) {
/* 1010 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean putAll(K key, Iterable<? extends V> values) {
/* 1014 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/* 1018 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Set<V> replaceValues(K key, Iterable<? extends V> values) {
/* 1022 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean remove(Object key, Object value) {
/* 1026 */       return this.map.entrySet().remove(Maps.immutableEntry(key, value));
/*      */     }
/*      */     
/*      */     public Set<V> removeAll(Object key) {
/* 1030 */       Set<V> values = new HashSet<V>(2);
/* 1031 */       if (!this.map.containsKey(key)) {
/* 1032 */         return values;
/*      */       }
/* 1034 */       values.add(this.map.remove(key));
/* 1035 */       return values;
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1039 */       this.map.clear();
/*      */     }
/*      */     
/*      */     public Set<K> keySet() {
/* 1043 */       return this.map.keySet();
/*      */     }
/*      */     
/*      */     public Multiset<K> keys() {
/* 1047 */       return Multisets.forSet(this.map.keySet());
/*      */     }
/*      */     
/*      */     public Collection<V> values() {
/* 1051 */       return this.map.values();
/*      */     }
/*      */     
/*      */     public Set<Map.Entry<K, V>> entries() {
/* 1055 */       return this.map.entrySet();
/*      */     }
/*      */     
/*      */     public Map<K, Collection<V>> asMap() {
/* 1059 */       Map<K, Collection<V>> result = this.asMap;
/* 1060 */       if (result == null) {
/* 1061 */         this.asMap = result = new AsMap();
/*      */       }
/* 1063 */       return result;
/*      */     }
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/* 1067 */       if (object == this) {
/* 1068 */         return true;
/*      */       }
/* 1070 */       if (object instanceof Multimap) {
/* 1071 */         Multimap<?, ?> that = (Multimap<?, ?>)object;
/* 1072 */         return (size() == that.size() && asMap().equals(that.asMap()));
/*      */       } 
/* 1074 */       return false;
/*      */     }
/*      */     
/*      */     public int hashCode() {
/* 1078 */       return this.map.hashCode();
/*      */     }
/*      */     
/* 1081 */     private static final Joiner.MapJoiner joiner = Joiner.on("], ").withKeyValueSeparator("=[").useForNull("null");
/*      */     private static final long serialVersionUID = 7845222491160860175L;
/*      */     
/*      */     public String toString() {
/* 1085 */       if (this.map.isEmpty()) {
/* 1086 */         return "{}";
/*      */       }
/* 1088 */       StringBuilder builder = (new StringBuilder(this.map.size() * 16)).append('{');
/* 1089 */       joiner.appendTo(builder, this.map);
/* 1090 */       return builder.append("]}").toString();
/*      */     }
/*      */     
/*      */     class AsMapEntries
/*      */       extends AbstractSet<Map.Entry<K, Collection<V>>> {
/*      */       public int size() {
/* 1096 */         return Multimaps.MapMultimap.this.map.size();
/*      */       }
/*      */       
/*      */       public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/* 1100 */         return new Iterator<Map.Entry<K, Collection<V>>>() {
/* 1101 */             final Iterator<K> keys = Multimaps.MapMultimap.this.map.keySet().iterator();
/*      */             
/*      */             public boolean hasNext() {
/* 1104 */               return this.keys.hasNext();
/*      */             }
/*      */             public Map.Entry<K, Collection<V>> next() {
/* 1107 */               final K key = this.keys.next();
/* 1108 */               return (Map.Entry)new AbstractMapEntry<K, Collection<Collection<V>>>() {
/*      */                   public K getKey() {
/* 1110 */                     return (K)key;
/*      */                   }
/*      */                   public Collection<V> getValue() {
/* 1113 */                     return Multimaps.MapMultimap.this.get(key);
/*      */                   }
/*      */                 };
/*      */             }
/*      */             public void remove() {
/* 1118 */               this.keys.remove();
/*      */             }
/*      */           };
/*      */       }
/*      */       
/*      */       public boolean contains(Object o) {
/* 1124 */         if (!(o instanceof Map.Entry)) {
/* 1125 */           return false;
/*      */         }
/* 1127 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1128 */         if (!(entry.getValue() instanceof Set)) {
/* 1129 */           return false;
/*      */         }
/* 1131 */         Set<?> set = (Set)entry.getValue();
/* 1132 */         return (set.size() == 1 && Multimaps.MapMultimap.this.containsEntry(entry.getKey(), set.iterator().next()));
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean remove(Object o) {
/* 1137 */         if (!(o instanceof Map.Entry)) {
/* 1138 */           return false;
/*      */         }
/* 1140 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1141 */         if (!(entry.getValue() instanceof Set)) {
/* 1142 */           return false;
/*      */         }
/* 1144 */         Set<?> set = (Set)entry.getValue();
/* 1145 */         return (set.size() == 1 && Multimaps.MapMultimap.this.map.entrySet().remove(Maps.immutableEntry(entry.getKey(), set.iterator().next())));
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     class AsMap
/*      */       extends Maps.ImprovedAbstractMap<K, Collection<V>>
/*      */     {
/*      */       protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
/* 1154 */         return new Multimaps.MapMultimap.AsMapEntries();
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean containsKey(Object key) {
/* 1160 */         return Multimaps.MapMultimap.this.map.containsKey(key);
/*      */       }
/*      */ 
/*      */       
/*      */       public Collection<V> get(Object key) {
/* 1165 */         Collection<V> collection = Multimaps.MapMultimap.this.get(key);
/* 1166 */         return collection.isEmpty() ? null : collection;
/*      */       }
/*      */       
/*      */       public Collection<V> remove(Object key) {
/* 1170 */         Collection<V> collection = Multimaps.MapMultimap.this.removeAll(key);
/* 1171 */         return collection.isEmpty() ? null : collection;
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
/*      */   public static <K, V> ImmutableListMultimap<K, V> index(Iterable<V> values, Function<? super V, K> keyFunction) {
/* 1218 */     Preconditions.checkNotNull(keyFunction);
/* 1219 */     ImmutableListMultimap.Builder<K, V> builder = ImmutableListMultimap.builder();
/*      */     
/* 1221 */     for (V value : values) {
/* 1222 */       Preconditions.checkNotNull(value, values);
/* 1223 */       builder.put((K)keyFunction.apply(value), value);
/*      */     } 
/* 1225 */     return builder.build();
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\Multimaps.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */