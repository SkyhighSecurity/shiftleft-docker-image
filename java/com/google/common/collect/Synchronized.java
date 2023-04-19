/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.RandomAccess;
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
/*      */ @GwtCompatible
/*      */ final class Synchronized
/*      */ {
/*      */   static class SynchronizedObject
/*      */     implements Serializable
/*      */   {
/*      */     private final Object delegate;
/*      */     protected final Object mutex;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     public SynchronizedObject(Object delegate, @Nullable Object mutex) {
/*   61 */       this.delegate = Preconditions.checkNotNull(delegate);
/*   62 */       this.mutex = (mutex == null) ? this : mutex;
/*      */     }
/*      */     
/*      */     protected Object delegate() {
/*   66 */       return this.delegate;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*   72 */       synchronized (this.mutex) {
/*   73 */         return this.delegate.toString();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*   83 */       synchronized (this.mutex) {
/*   84 */         stream.defaultWriteObject();
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
/*      */   static <E> Collection<E> collection(Collection<E> collection, @Nullable Object mutex) {
/*  117 */     return new SynchronizedCollection<E>(collection, mutex);
/*      */   }
/*      */   
/*      */   static class SynchronizedCollection<E>
/*      */     extends SynchronizedObject implements Collection<E> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     public SynchronizedCollection(Collection<E> delegate, @Nullable Object mutex) {
/*  125 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     protected Collection<E> delegate() {
/*  130 */       return (Collection<E>)super.delegate();
/*      */     }
/*      */     
/*      */     public boolean add(E e) {
/*  134 */       synchronized (this.mutex) {
/*  135 */         return delegate().add(e);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean addAll(Collection<? extends E> c) {
/*  140 */       synchronized (this.mutex) {
/*  141 */         return delegate().addAll(c);
/*      */       } 
/*      */     }
/*      */     
/*      */     public void clear() {
/*  146 */       synchronized (this.mutex) {
/*  147 */         delegate().clear();
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean contains(Object o) {
/*  152 */       synchronized (this.mutex) {
/*  153 */         return delegate().contains(o);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  158 */       synchronized (this.mutex) {
/*  159 */         return delegate().containsAll(c);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean isEmpty() {
/*  164 */       synchronized (this.mutex) {
/*  165 */         return delegate().isEmpty();
/*      */       } 
/*      */     }
/*      */     
/*      */     public Iterator<E> iterator() {
/*  170 */       return delegate().iterator();
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/*  174 */       synchronized (this.mutex) {
/*  175 */         return delegate().remove(o);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  180 */       synchronized (this.mutex) {
/*  181 */         return delegate().removeAll(c);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*  186 */       synchronized (this.mutex) {
/*  187 */         return delegate().retainAll(c);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/*  192 */       synchronized (this.mutex) {
/*  193 */         return delegate().size();
/*      */       } 
/*      */     }
/*      */     
/*      */     public Object[] toArray() {
/*  198 */       synchronized (this.mutex) {
/*  199 */         return delegate().toArray();
/*      */       } 
/*      */     }
/*      */     
/*      */     public <T> T[] toArray(T[] a) {
/*  204 */       synchronized (this.mutex) {
/*  205 */         return delegate().toArray(a);
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
/*      */   public static <E> Set<E> set(Set<E> set, @Nullable Object mutex) {
/*  236 */     return new SynchronizedSet<E>(set, mutex);
/*      */   }
/*      */   
/*      */   static class SynchronizedSet<E> extends SynchronizedCollection<E> implements Set<E> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     public SynchronizedSet(Set<E> delegate, @Nullable Object mutex) {
/*  243 */       super(delegate, mutex);
/*      */     }
/*      */     
/*      */     protected Set<E> delegate() {
/*  247 */       return (Set<E>)super.delegate();
/*      */     }
/*      */     
/*      */     public boolean equals(Object o) {
/*  251 */       if (o == this) {
/*  252 */         return true;
/*      */       }
/*  254 */       synchronized (this.mutex) {
/*  255 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  260 */       synchronized (this.mutex) {
/*  261 */         return delegate().hashCode();
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
/*      */   static <E> SortedSet<E> sortedSet(SortedSet<E> set, @Nullable Object mutex) {
/*  293 */     return new SynchronizedSortedSet<E>(set, mutex);
/*      */   }
/*      */   
/*      */   static class SynchronizedSortedSet<E>
/*      */     extends SynchronizedSet<E> implements SortedSet<E> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     public SynchronizedSortedSet(SortedSet<E> delegate, @Nullable Object mutex) {
/*  301 */       super(delegate, mutex);
/*      */     }
/*      */     
/*      */     protected SortedSet<E> delegate() {
/*  305 */       return (SortedSet<E>)super.delegate();
/*      */     }
/*      */     
/*      */     public Comparator<? super E> comparator() {
/*  309 */       synchronized (this.mutex) {
/*  310 */         return delegate().comparator();
/*      */       } 
/*      */     }
/*      */     
/*      */     public SortedSet<E> subSet(E fromElement, E toElement) {
/*  315 */       synchronized (this.mutex) {
/*  316 */         return Synchronized.sortedSet(delegate().subSet(fromElement, toElement), this.mutex);
/*      */       } 
/*      */     }
/*      */     
/*      */     public SortedSet<E> headSet(E toElement) {
/*  321 */       synchronized (this.mutex) {
/*  322 */         return Synchronized.sortedSet(delegate().headSet(toElement), this.mutex);
/*      */       } 
/*      */     }
/*      */     
/*      */     public SortedSet<E> tailSet(E fromElement) {
/*  327 */       synchronized (this.mutex) {
/*  328 */         return Synchronized.sortedSet(delegate().tailSet(fromElement), this.mutex);
/*      */       } 
/*      */     }
/*      */     
/*      */     public E first() {
/*  333 */       synchronized (this.mutex) {
/*  334 */         return delegate().first();
/*      */       } 
/*      */     }
/*      */     
/*      */     public E last() {
/*  339 */       synchronized (this.mutex) {
/*  340 */         return delegate().last();
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
/*      */   static <E> List<E> list(List<E> list, @Nullable Object mutex) {
/*  374 */     return (list instanceof RandomAccess) ? new SynchronizedRandomAccessList<E>(list, mutex) : new SynchronizedList<E>(list, mutex);
/*      */   }
/*      */   
/*      */   static class SynchronizedList<E>
/*      */     extends SynchronizedCollection<E>
/*      */     implements List<E> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     public SynchronizedList(List<E> delegate, @Nullable Object mutex) {
/*  383 */       super(delegate, mutex);
/*      */     }
/*      */     
/*      */     protected List<E> delegate() {
/*  387 */       return (List<E>)super.delegate();
/*      */     }
/*      */     
/*      */     public void add(int index, E element) {
/*  391 */       synchronized (this.mutex) {
/*  392 */         delegate().add(index, element);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean addAll(int index, Collection<? extends E> c) {
/*  397 */       synchronized (this.mutex) {
/*  398 */         return delegate().addAll(index, c);
/*      */       } 
/*      */     }
/*      */     
/*      */     public E get(int index) {
/*  403 */       synchronized (this.mutex) {
/*  404 */         return delegate().get(index);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int indexOf(Object o) {
/*  409 */       synchronized (this.mutex) {
/*  410 */         return delegate().indexOf(o);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int lastIndexOf(Object o) {
/*  415 */       synchronized (this.mutex) {
/*  416 */         return delegate().lastIndexOf(o);
/*      */       } 
/*      */     }
/*      */     
/*      */     public ListIterator<E> listIterator() {
/*  421 */       return delegate().listIterator();
/*      */     }
/*      */     
/*      */     public ListIterator<E> listIterator(int index) {
/*  425 */       return delegate().listIterator(index);
/*      */     }
/*      */     
/*      */     public E remove(int index) {
/*  429 */       synchronized (this.mutex) {
/*  430 */         return delegate().remove(index);
/*      */       } 
/*      */     }
/*      */     
/*      */     public E set(int index, E element) {
/*  435 */       synchronized (this.mutex) {
/*  436 */         return delegate().set(index, element);
/*      */       } 
/*      */     }
/*      */     
/*      */     @GwtIncompatible("List.subList")
/*      */     public List<E> subList(int fromIndex, int toIndex) {
/*  442 */       synchronized (this.mutex) {
/*  443 */         return Synchronized.list(Platform.subList(delegate(), fromIndex, toIndex), this.mutex);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean equals(Object o) {
/*  448 */       if (o == this) {
/*  449 */         return true;
/*      */       }
/*  451 */       synchronized (this.mutex) {
/*  452 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  457 */       synchronized (this.mutex) {
/*  458 */         return delegate().hashCode();
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   static class SynchronizedRandomAccessList<E>
/*      */     extends SynchronizedList<E>
/*      */     implements RandomAccess {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     public SynchronizedRandomAccessList(List<E> list, @Nullable Object mutex) {
/*  469 */       super(list, mutex);
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
/*      */   private static <E> Multiset<E> multiset(Multiset<E> multiset, @Nullable Object mutex) {
/*  500 */     return new SynchronizedMultiset<E>(multiset, mutex);
/*      */   }
/*      */   
/*      */   static class SynchronizedMultiset<E>
/*      */     extends SynchronizedCollection<E> implements Multiset<E> {
/*      */     private transient Set<E> elementSet;
/*      */     private transient Set<Multiset.Entry<E>> entrySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     public SynchronizedMultiset(Multiset<E> delegate, @Nullable Object mutex) {
/*  510 */       super(delegate, mutex);
/*      */     }
/*      */     
/*      */     protected Multiset<E> delegate() {
/*  514 */       return (Multiset<E>)super.delegate();
/*      */     }
/*      */     
/*      */     public int count(Object o) {
/*  518 */       synchronized (this.mutex) {
/*  519 */         return delegate().count(o);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int add(E e, int n) {
/*  524 */       synchronized (this.mutex) {
/*  525 */         return delegate().add(e, n);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int remove(Object o, int n) {
/*  530 */       synchronized (this.mutex) {
/*  531 */         return delegate().remove(o, n);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int setCount(E element, int count) {
/*  536 */       synchronized (this.mutex) {
/*  537 */         return delegate().setCount(element, count);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean setCount(E element, int oldCount, int newCount) {
/*  542 */       synchronized (this.mutex) {
/*  543 */         return delegate().setCount(element, oldCount, newCount);
/*      */       } 
/*      */     }
/*      */     
/*      */     public Set<E> elementSet() {
/*  548 */       synchronized (this.mutex) {
/*  549 */         if (this.elementSet == null) {
/*  550 */           this.elementSet = Synchronized.typePreservingSet(delegate().elementSet(), this.mutex);
/*      */         }
/*  552 */         return this.elementSet;
/*      */       } 
/*      */     }
/*      */     
/*      */     public Set<Multiset.Entry<E>> entrySet() {
/*  557 */       synchronized (this.mutex) {
/*  558 */         if (this.entrySet == null) {
/*  559 */           this.entrySet = Synchronized.typePreservingSet(delegate().entrySet(), this.mutex);
/*      */         }
/*  561 */         return this.entrySet;
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean equals(Object o) {
/*  566 */       if (o == this) {
/*  567 */         return true;
/*      */       }
/*  569 */       synchronized (this.mutex) {
/*  570 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  575 */       synchronized (this.mutex) {
/*  576 */         return delegate().hashCode();
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
/*      */   public static <K, V> Multimap<K, V> multimap(Multimap<K, V> multimap, @Nullable Object mutex) {
/*  612 */     return new SynchronizedMultimap<K, V>(multimap, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedMultimap<K, V>
/*      */     extends SynchronizedObject
/*      */     implements Multimap<K, V> {
/*      */     transient Set<K> keySet;
/*      */     transient Collection<V> valuesCollection;
/*      */     transient Collection<Map.Entry<K, V>> entries;
/*      */     transient Map<K, Collection<V>> asMap;
/*      */     transient Multiset<K> keys;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     protected Multimap<K, V> delegate() {
/*  626 */       return (Multimap<K, V>)super.delegate();
/*      */     }
/*      */     
/*      */     SynchronizedMultimap(Multimap<K, V> delegate, @Nullable Object mutex) {
/*  630 */       super(delegate, mutex);
/*      */     }
/*      */     
/*      */     public int size() {
/*  634 */       synchronized (this.mutex) {
/*  635 */         return delegate().size();
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean isEmpty() {
/*  640 */       synchronized (this.mutex) {
/*  641 */         return delegate().isEmpty();
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean containsKey(Object key) {
/*  646 */       synchronized (this.mutex) {
/*  647 */         return delegate().containsKey(key);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean containsValue(Object value) {
/*  652 */       synchronized (this.mutex) {
/*  653 */         return delegate().containsValue(value);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean containsEntry(Object key, Object value) {
/*  658 */       synchronized (this.mutex) {
/*  659 */         return delegate().containsEntry(key, value);
/*      */       } 
/*      */     }
/*      */     
/*      */     public Collection<V> get(K key) {
/*  664 */       synchronized (this.mutex) {
/*  665 */         return Synchronized.typePreservingCollection(delegate().get(key), this.mutex);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean put(K key, V value) {
/*  670 */       synchronized (this.mutex) {
/*  671 */         return delegate().put(key, value);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean putAll(K key, Iterable<? extends V> values) {
/*  676 */       synchronized (this.mutex) {
/*  677 */         return delegate().putAll(key, values);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/*  682 */       synchronized (this.mutex) {
/*  683 */         return delegate().putAll(multimap);
/*      */       } 
/*      */     }
/*      */     
/*      */     public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
/*  688 */       synchronized (this.mutex) {
/*  689 */         return delegate().replaceValues(key, values);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object key, Object value) {
/*  694 */       synchronized (this.mutex) {
/*  695 */         return delegate().remove(key, value);
/*      */       } 
/*      */     }
/*      */     
/*      */     public Collection<V> removeAll(Object key) {
/*  700 */       synchronized (this.mutex) {
/*  701 */         return delegate().removeAll(key);
/*      */       } 
/*      */     }
/*      */     
/*      */     public void clear() {
/*  706 */       synchronized (this.mutex) {
/*  707 */         delegate().clear();
/*      */       } 
/*      */     }
/*      */     
/*      */     public Set<K> keySet() {
/*  712 */       synchronized (this.mutex) {
/*  713 */         if (this.keySet == null) {
/*  714 */           this.keySet = Synchronized.typePreservingSet(delegate().keySet(), this.mutex);
/*      */         }
/*  716 */         return this.keySet;
/*      */       } 
/*      */     }
/*      */     
/*      */     public Collection<V> values() {
/*  721 */       synchronized (this.mutex) {
/*  722 */         if (this.valuesCollection == null) {
/*  723 */           this.valuesCollection = Synchronized.collection(delegate().values(), this.mutex);
/*      */         }
/*  725 */         return this.valuesCollection;
/*      */       } 
/*      */     }
/*      */     
/*      */     public Collection<Map.Entry<K, V>> entries() {
/*  730 */       synchronized (this.mutex) {
/*  731 */         if (this.entries == null) {
/*  732 */           this.entries = (Collection)Synchronized.typePreservingCollection((Collection)delegate().entries(), this.mutex);
/*      */         }
/*  734 */         return this.entries;
/*      */       } 
/*      */     }
/*      */     
/*      */     public Map<K, Collection<V>> asMap() {
/*  739 */       synchronized (this.mutex) {
/*  740 */         if (this.asMap == null) {
/*  741 */           this.asMap = new Synchronized.SynchronizedAsMap<K, V>(delegate().asMap(), this.mutex);
/*      */         }
/*  743 */         return this.asMap;
/*      */       } 
/*      */     }
/*      */     
/*      */     public Multiset<K> keys() {
/*  748 */       synchronized (this.mutex) {
/*  749 */         if (this.keys == null) {
/*  750 */           this.keys = Synchronized.multiset(delegate().keys(), this.mutex);
/*      */         }
/*  752 */         return this.keys;
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean equals(Object o) {
/*  757 */       if (o == this) {
/*  758 */         return true;
/*      */       }
/*  760 */       synchronized (this.mutex) {
/*  761 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  766 */       synchronized (this.mutex) {
/*  767 */         return delegate().hashCode();
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
/*      */   public static <K, V> ListMultimap<K, V> listMultimap(ListMultimap<K, V> multimap, @Nullable Object mutex) {
/*  785 */     return new SynchronizedListMultimap<K, V>(multimap, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedListMultimap<K, V>
/*      */     extends SynchronizedMultimap<K, V> implements ListMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedListMultimap(ListMultimap<K, V> delegate, @Nullable Object mutex) {
/*  793 */       super(delegate, mutex);
/*      */     }
/*      */     protected ListMultimap<K, V> delegate() {
/*  796 */       return (ListMultimap<K, V>)super.delegate();
/*      */     }
/*      */     public List<V> get(K key) {
/*  799 */       synchronized (this.mutex) {
/*  800 */         return Synchronized.list(delegate().get(key), this.mutex);
/*      */       } 
/*      */     }
/*      */     public List<V> removeAll(Object key) {
/*  804 */       synchronized (this.mutex) {
/*  805 */         return delegate().removeAll(key);
/*      */       } 
/*      */     }
/*      */     
/*      */     public List<V> replaceValues(K key, Iterable<? extends V> values) {
/*  810 */       synchronized (this.mutex) {
/*  811 */         return delegate().replaceValues(key, values);
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
/*      */   public static <K, V> SetMultimap<K, V> setMultimap(SetMultimap<K, V> multimap, @Nullable Object mutex) {
/*  828 */     return new SynchronizedSetMultimap<K, V>(multimap, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedSetMultimap<K, V>
/*      */     extends SynchronizedMultimap<K, V> implements SetMultimap<K, V> {
/*      */     transient Set<Map.Entry<K, V>> entrySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedSetMultimap(SetMultimap<K, V> delegate, @Nullable Object mutex) {
/*  837 */       super(delegate, mutex);
/*      */     }
/*      */     protected SetMultimap<K, V> delegate() {
/*  840 */       return (SetMultimap<K, V>)super.delegate();
/*      */     }
/*      */     public Set<V> get(K key) {
/*  843 */       synchronized (this.mutex) {
/*  844 */         return Synchronized.set(delegate().get(key), this.mutex);
/*      */       } 
/*      */     }
/*      */     public Set<V> removeAll(Object key) {
/*  848 */       synchronized (this.mutex) {
/*  849 */         return delegate().removeAll(key);
/*      */       } 
/*      */     }
/*      */     
/*      */     public Set<V> replaceValues(K key, Iterable<? extends V> values) {
/*  854 */       synchronized (this.mutex) {
/*  855 */         return delegate().replaceValues(key, values);
/*      */       } 
/*      */     }
/*      */     public Set<Map.Entry<K, V>> entries() {
/*  859 */       synchronized (this.mutex) {
/*  860 */         if (this.entrySet == null) {
/*  861 */           this.entrySet = Synchronized.set(delegate().entries(), this.mutex);
/*      */         }
/*  863 */         return this.entrySet;
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
/*      */   public static <K, V> SortedSetMultimap<K, V> sortedSetMultimap(SortedSetMultimap<K, V> multimap, @Nullable Object mutex) {
/*  880 */     return new SynchronizedSortedSetMultimap<K, V>(multimap, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedSortedSetMultimap<K, V>
/*      */     extends SynchronizedSetMultimap<K, V> implements SortedSetMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedSortedSetMultimap(SortedSetMultimap<K, V> delegate, @Nullable Object mutex) {
/*  888 */       super(delegate, mutex);
/*      */     }
/*      */     protected SortedSetMultimap<K, V> delegate() {
/*  891 */       return (SortedSetMultimap<K, V>)super.delegate();
/*      */     }
/*      */     public SortedSet<V> get(K key) {
/*  894 */       synchronized (this.mutex) {
/*  895 */         return Synchronized.sortedSet(delegate().get(key), this.mutex);
/*      */       } 
/*      */     }
/*      */     public SortedSet<V> removeAll(Object key) {
/*  899 */       synchronized (this.mutex) {
/*  900 */         return delegate().removeAll(key);
/*      */       } 
/*      */     }
/*      */     
/*      */     public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) {
/*  905 */       synchronized (this.mutex) {
/*  906 */         return delegate().replaceValues(key, values);
/*      */       } 
/*      */     }
/*      */     public Comparator<? super V> valueComparator() {
/*  910 */       synchronized (this.mutex) {
/*  911 */         return delegate().valueComparator();
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
/*      */   private static <E> Collection<E> typePreservingCollection(Collection<E> collection, @Nullable Object mutex) {
/*  947 */     if (collection instanceof SortedSet)
/*  948 */       return sortedSet((SortedSet<E>)collection, mutex); 
/*  949 */     if (collection instanceof Set)
/*  950 */       return set((Set<E>)collection, mutex); 
/*  951 */     if (collection instanceof List) {
/*  952 */       return list((List<E>)collection, mutex);
/*      */     }
/*  954 */     return collection(collection, mutex);
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
/*      */   public static <E> Set<E> typePreservingSet(Set<E> set, @Nullable Object mutex) {
/*  987 */     if (set instanceof SortedSet) {
/*  988 */       return sortedSet((SortedSet<E>)set, mutex);
/*      */     }
/*  990 */     return set(set, mutex);
/*      */   }
/*      */   
/*      */   static class SynchronizedAsMapEntries<K, V>
/*      */     extends SynchronizedSet<Map.Entry<K, Collection<V>>>
/*      */   {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     public SynchronizedAsMapEntries(Set<Map.Entry<K, Collection<V>>> delegate, @Nullable Object mutex) {
/*  999 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/* 1004 */       final Iterator<Map.Entry<K, Collection<V>>> iterator = super.iterator();
/* 1005 */       return new ForwardingIterator<Map.Entry<K, Collection<V>>>() {
/*      */           protected Iterator<Map.Entry<K, Collection<V>>> delegate() {
/* 1007 */             return iterator;
/*      */           }
/*      */           
/*      */           public Map.Entry<K, Collection<V>> next() {
/* 1011 */             final Map.Entry<K, Collection<V>> entry = iterator.next();
/* 1012 */             return (Map.Entry)new ForwardingMapEntry<K, Collection<Collection<V>>>() {
/*      */                 protected Map.Entry<K, Collection<V>> delegate() {
/* 1014 */                   return entry;
/*      */                 }
/*      */                 public Collection<V> getValue() {
/* 1017 */                   return Synchronized.typePreservingCollection((Collection)entry.getValue(), Synchronized.SynchronizedAsMapEntries.this.mutex);
/*      */                 }
/*      */               };
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 1027 */       synchronized (this.mutex) {
/* 1028 */         return ObjectArrays.toArrayImpl(delegate());
/*      */       } 
/*      */     }
/*      */     public <T> T[] toArray(T[] array) {
/* 1032 */       synchronized (this.mutex) {
/* 1033 */         return ObjectArrays.toArrayImpl(delegate(), array);
/*      */       } 
/*      */     }
/*      */     public boolean contains(Object o) {
/* 1037 */       synchronized (this.mutex) {
/* 1038 */         return Maps.containsEntryImpl(delegate(), o);
/*      */       } 
/*      */     }
/*      */     public boolean containsAll(Collection<?> c) {
/* 1042 */       synchronized (this.mutex) {
/* 1043 */         return Collections2.containsAll(delegate(), c);
/*      */       } 
/*      */     }
/*      */     public boolean equals(Object o) {
/* 1047 */       if (o == this) {
/* 1048 */         return true;
/*      */       }
/* 1050 */       synchronized (this.mutex) {
/* 1051 */         return Collections2.setEquals(delegate(), o);
/*      */       } 
/*      */     }
/*      */     public boolean remove(Object o) {
/* 1055 */       synchronized (this.mutex) {
/* 1056 */         return Maps.removeEntryImpl(delegate(), o);
/*      */       } 
/*      */     }
/*      */     public boolean removeAll(Collection<?> c) {
/* 1060 */       synchronized (this.mutex) {
/* 1061 */         return Iterators.removeAll(delegate().iterator(), c);
/*      */       } 
/*      */     }
/*      */     public boolean retainAll(Collection<?> c) {
/* 1065 */       synchronized (this.mutex) {
/* 1066 */         return Iterators.retainAll(delegate().iterator(), c);
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
/*      */   public static <K, V> Map<K, V> map(Map<K, V> map, @Nullable Object mutex) {
/* 1101 */     return new SynchronizedMap<K, V>(map, mutex);
/*      */   }
/*      */   
/*      */   static class SynchronizedMap<K, V>
/*      */     extends SynchronizedObject implements Map<K, V> {
/*      */     private transient Set<K> keySet;
/*      */     private transient Collection<V> values;
/*      */     private transient Set<Map.Entry<K, V>> entrySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     public SynchronizedMap(Map<K, V> delegate, @Nullable Object mutex) {
/* 1112 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     protected Map<K, V> delegate() {
/* 1117 */       return (Map<K, V>)super.delegate();
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1121 */       synchronized (this.mutex) {
/* 1122 */         delegate().clear();
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 1127 */       synchronized (this.mutex) {
/* 1128 */         return delegate().containsKey(key);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean containsValue(Object value) {
/* 1133 */       synchronized (this.mutex) {
/* 1134 */         return delegate().containsValue(value);
/*      */       } 
/*      */     }
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 1139 */       synchronized (this.mutex) {
/* 1140 */         if (this.entrySet == null) {
/* 1141 */           this.entrySet = Synchronized.set(delegate().entrySet(), this.mutex);
/*      */         }
/* 1143 */         return this.entrySet;
/*      */       } 
/*      */     }
/*      */     
/*      */     public V get(Object key) {
/* 1148 */       synchronized (this.mutex) {
/* 1149 */         return delegate().get(key);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean isEmpty() {
/* 1154 */       synchronized (this.mutex) {
/* 1155 */         return delegate().isEmpty();
/*      */       } 
/*      */     }
/*      */     
/*      */     public Set<K> keySet() {
/* 1160 */       synchronized (this.mutex) {
/* 1161 */         if (this.keySet == null) {
/* 1162 */           this.keySet = Synchronized.set(delegate().keySet(), this.mutex);
/*      */         }
/* 1164 */         return this.keySet;
/*      */       } 
/*      */     }
/*      */     
/*      */     public V put(K key, V value) {
/* 1169 */       synchronized (this.mutex) {
/* 1170 */         return delegate().put(key, value);
/*      */       } 
/*      */     }
/*      */     
/*      */     public void putAll(Map<? extends K, ? extends V> map) {
/* 1175 */       synchronized (this.mutex) {
/* 1176 */         delegate().putAll(map);
/*      */       } 
/*      */     }
/*      */     
/*      */     public V remove(Object key) {
/* 1181 */       synchronized (this.mutex) {
/* 1182 */         return delegate().remove(key);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/* 1187 */       synchronized (this.mutex) {
/* 1188 */         return delegate().size();
/*      */       } 
/*      */     }
/*      */     
/*      */     public Collection<V> values() {
/* 1193 */       synchronized (this.mutex) {
/* 1194 */         if (this.values == null) {
/* 1195 */           this.values = Synchronized.collection(delegate().values(), this.mutex);
/*      */         }
/* 1197 */         return this.values;
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean equals(Object o) {
/* 1202 */       if (o == this) {
/* 1203 */         return true;
/*      */       }
/* 1205 */       synchronized (this.mutex) {
/* 1206 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int hashCode() {
/* 1211 */       synchronized (this.mutex) {
/* 1212 */         return delegate().hashCode();
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
/*      */   public static <K, V> BiMap<K, V> biMap(BiMap<K, V> bimap, @Nullable Object mutex) {
/* 1248 */     return new SynchronizedBiMap<K, V>(bimap, mutex, null);
/*      */   }
/*      */   
/*      */   static class SynchronizedBiMap<K, V>
/*      */     extends SynchronizedMap<K, V>
/*      */     implements BiMap<K, V>, Serializable
/*      */   {
/*      */     private transient Set<V> valueSet;
/*      */     private transient BiMap<V, K> inverse;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     public SynchronizedBiMap(BiMap<K, V> delegate, @Nullable Object mutex, @Nullable BiMap<V, K> inverse) {
/* 1260 */       super(delegate, mutex);
/* 1261 */       this.inverse = inverse;
/*      */     }
/*      */     
/*      */     protected BiMap<K, V> delegate() {
/* 1265 */       return (BiMap<K, V>)super.delegate();
/*      */     }
/*      */     
/*      */     public Set<V> values() {
/* 1269 */       synchronized (this.mutex) {
/* 1270 */         if (this.valueSet == null) {
/* 1271 */           this.valueSet = Synchronized.set(delegate().values(), this.mutex);
/*      */         }
/* 1273 */         return this.valueSet;
/*      */       } 
/*      */     }
/*      */     
/*      */     public V forcePut(K key, V value) {
/* 1278 */       synchronized (this.mutex) {
/* 1279 */         return delegate().forcePut(key, value);
/*      */       } 
/*      */     }
/*      */     
/*      */     public BiMap<V, K> inverse() {
/* 1284 */       synchronized (this.mutex) {
/* 1285 */         if (this.inverse == null) {
/* 1286 */           this.inverse = new SynchronizedBiMap(delegate().inverse(), this.mutex, this);
/*      */         }
/*      */         
/* 1289 */         return this.inverse;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class SynchronizedAsMap<K, V>
/*      */     extends SynchronizedMap<K, Collection<V>>
/*      */   {
/*      */     private transient Set<Map.Entry<K, Collection<V>>> asMapEntrySet;
/*      */     
/*      */     private transient Collection<Collection<V>> asMapValues;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     public SynchronizedAsMap(Map<K, Collection<V>> delegate, @Nullable Object mutex) {
/* 1304 */       super(delegate, mutex);
/*      */     }
/*      */     
/*      */     public Collection<V> get(Object key) {
/* 1308 */       synchronized (this.mutex) {
/* 1309 */         Collection<V> collection = super.get(key);
/* 1310 */         return (collection == null) ? null : Synchronized.typePreservingCollection(collection, this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, Collection<V>>> entrySet() {
/* 1316 */       synchronized (this.mutex) {
/* 1317 */         if (this.asMapEntrySet == null) {
/* 1318 */           this.asMapEntrySet = new Synchronized.SynchronizedAsMapEntries<K, V>(delegate().entrySet(), this.mutex);
/*      */         }
/*      */         
/* 1321 */         return this.asMapEntrySet;
/*      */       } 
/*      */     }
/*      */     
/*      */     public Collection<Collection<V>> values() {
/* 1326 */       synchronized (this.mutex) {
/* 1327 */         if (this.asMapValues == null) {
/* 1328 */           this.asMapValues = new Synchronized.SynchronizedAsMapValues<V>(delegate().values(), this.mutex);
/*      */         }
/*      */         
/* 1331 */         return this.asMapValues;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsValue(Object o) {
/* 1337 */       return values().contains(o);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class SynchronizedAsMapValues<V>
/*      */     extends SynchronizedCollection<Collection<V>>
/*      */   {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedAsMapValues(Collection<Collection<V>> delegate, @Nullable Object mutex) {
/* 1348 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<Collection<V>> iterator() {
/* 1353 */       final Iterator<Collection<V>> iterator = super.iterator();
/* 1354 */       return new ForwardingIterator<Collection<V>>() {
/*      */           protected Iterator<Collection<V>> delegate() {
/* 1356 */             return iterator;
/*      */           }
/*      */           public Collection<V> next() {
/* 1359 */             return Synchronized.typePreservingCollection(iterator.next(), Synchronized.SynchronizedAsMapValues.this.mutex);
/*      */           }
/*      */         };
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\Synchronized.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */