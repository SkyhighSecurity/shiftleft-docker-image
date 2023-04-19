/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.RandomAccess;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
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
/*      */ 
/*      */ 
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
/*      */ abstract class StandardMultimap<K, V>
/*      */   implements Multimap<K, V>, Serializable
/*      */ {
/*      */   private transient Map<K, Collection<V>> map;
/*      */   private transient int totalSize;
/*      */   private transient Set<K> keySet;
/*      */   private transient Multiset<K> multiset;
/*      */   private transient Collection<V> valuesCollection;
/*      */   private transient Collection<Map.Entry<K, V>> entries;
/*      */   private transient Map<K, Collection<V>> asMap;
/*      */   
/*      */   protected StandardMultimap(Map<K, Collection<V>> map) {
/*  117 */     Preconditions.checkArgument(map.isEmpty());
/*  118 */     this.map = map;
/*      */   }
/*      */ 
/*      */   
/*      */   final void setMap(Map<K, Collection<V>> map) {
/*  123 */     this.map = map;
/*  124 */     this.totalSize = 0;
/*  125 */     for (Collection<V> values : map.values()) {
/*  126 */       Preconditions.checkArgument(!values.isEmpty());
/*  127 */       this.totalSize += values.size();
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
/*      */   Collection<V> createCollection(@Nullable K key) {
/*  154 */     return createCollection();
/*      */   }
/*      */   
/*      */   Map<K, Collection<V>> backingMap() {
/*  158 */     return this.map;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  164 */     return this.totalSize;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  168 */     return (this.totalSize == 0);
/*      */   }
/*      */   
/*      */   public boolean containsKey(@Nullable Object key) {
/*  172 */     return this.map.containsKey(key);
/*      */   }
/*      */   
/*      */   public boolean containsValue(@Nullable Object value) {
/*  176 */     for (Collection<V> collection : this.map.values()) {
/*  177 */       if (collection.contains(value)) {
/*  178 */         return true;
/*      */       }
/*      */     } 
/*      */     
/*  182 */     return false;
/*      */   }
/*      */   
/*      */   public boolean containsEntry(@Nullable Object key, @Nullable Object value) {
/*  186 */     Collection<V> collection = this.map.get(key);
/*  187 */     return (collection != null && collection.contains(value));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean put(@Nullable K key, @Nullable V value) {
/*  193 */     Collection<V> collection = getOrCreateCollection(key);
/*      */     
/*  195 */     if (collection.add(value)) {
/*  196 */       this.totalSize++;
/*  197 */       return true;
/*      */     } 
/*  199 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private Collection<V> getOrCreateCollection(@Nullable K key) {
/*  204 */     Collection<V> collection = this.map.get(key);
/*  205 */     if (collection == null) {
/*  206 */       collection = createCollection(key);
/*  207 */       this.map.put(key, collection);
/*      */     } 
/*  209 */     return collection;
/*      */   }
/*      */   
/*      */   public boolean remove(@Nullable Object key, @Nullable Object value) {
/*  213 */     Collection<V> collection = this.map.get(key);
/*  214 */     if (collection == null) {
/*  215 */       return false;
/*      */     }
/*      */     
/*  218 */     boolean changed = collection.remove(value);
/*  219 */     if (changed) {
/*  220 */       this.totalSize--;
/*  221 */       if (collection.isEmpty()) {
/*  222 */         this.map.remove(key);
/*      */       }
/*      */     } 
/*  225 */     return changed;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean putAll(@Nullable K key, Iterable<? extends V> values) {
/*  231 */     if (!values.iterator().hasNext()) {
/*  232 */       return false;
/*      */     }
/*  234 */     Collection<V> collection = getOrCreateCollection(key);
/*  235 */     int oldSize = collection.size();
/*      */     
/*  237 */     boolean changed = false;
/*  238 */     if (values instanceof Collection) {
/*      */       
/*  240 */       Collection<? extends V> c = (Collection<? extends V>)values;
/*  241 */       changed = collection.addAll(c);
/*      */     } else {
/*  243 */       for (V value : values) {
/*  244 */         changed |= collection.add(value);
/*      */       }
/*      */     } 
/*      */     
/*  248 */     this.totalSize += collection.size() - oldSize;
/*  249 */     return changed;
/*      */   }
/*      */   
/*      */   public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/*  253 */     boolean changed = false;
/*  254 */     for (Map.Entry<? extends K, ? extends V> entry : multimap.entries()) {
/*  255 */       changed |= put(entry.getKey(), entry.getValue());
/*      */     }
/*  257 */     return changed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
/*  267 */     Iterator<? extends V> iterator = values.iterator();
/*  268 */     if (!iterator.hasNext()) {
/*  269 */       return removeAll(key);
/*      */     }
/*      */     
/*  272 */     Collection<V> collection = getOrCreateCollection(key);
/*  273 */     Collection<V> oldValues = createCollection();
/*  274 */     oldValues.addAll(collection);
/*      */     
/*  276 */     this.totalSize -= collection.size();
/*  277 */     collection.clear();
/*      */     
/*  279 */     while (iterator.hasNext()) {
/*  280 */       if (collection.add(iterator.next())) {
/*  281 */         this.totalSize++;
/*      */       }
/*      */     } 
/*      */     
/*  285 */     return unmodifiableCollectionSubclass(oldValues);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> removeAll(@Nullable Object key) {
/*  294 */     Collection<V> collection = this.map.remove(key);
/*  295 */     Collection<V> output = createCollection();
/*      */     
/*  297 */     if (collection != null) {
/*  298 */       output.addAll(collection);
/*  299 */       this.totalSize -= collection.size();
/*  300 */       collection.clear();
/*      */     } 
/*      */     
/*  303 */     return unmodifiableCollectionSubclass(output);
/*      */   }
/*      */ 
/*      */   
/*      */   private Collection<V> unmodifiableCollectionSubclass(Collection<V> collection) {
/*  308 */     if (collection instanceof SortedSet)
/*  309 */       return Collections.unmodifiableSortedSet((SortedSet<V>)collection); 
/*  310 */     if (collection instanceof Set)
/*  311 */       return Collections.unmodifiableSet((Set<? extends V>)collection); 
/*  312 */     if (collection instanceof List) {
/*  313 */       return Collections.unmodifiableList((List<? extends V>)collection);
/*      */     }
/*  315 */     return Collections.unmodifiableCollection(collection);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear() {
/*  321 */     for (Collection<V> collection : this.map.values()) {
/*  322 */       collection.clear();
/*      */     }
/*  324 */     this.map.clear();
/*  325 */     this.totalSize = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> get(@Nullable K key) {
/*  336 */     Collection<V> collection = this.map.get(key);
/*  337 */     if (collection == null) {
/*  338 */       collection = createCollection(key);
/*      */     }
/*  340 */     return wrapCollection(key, collection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Collection<V> wrapCollection(@Nullable K key, Collection<V> collection) {
/*  350 */     if (collection instanceof SortedSet)
/*  351 */       return new WrappedSortedSet(key, (SortedSet<V>)collection, null); 
/*  352 */     if (collection instanceof Set)
/*  353 */       return new WrappedSet(key, (Set<V>)collection); 
/*  354 */     if (collection instanceof List) {
/*  355 */       return wrapList(key, (List<V>)collection, null);
/*      */     }
/*  357 */     return new WrappedCollection(key, collection, null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private List<V> wrapList(K key, List<V> list, @Nullable WrappedCollection ancestor) {
/*  363 */     return (list instanceof RandomAccess) ? new RandomAccessWrappedList(key, list, ancestor) : new WrappedList(key, list, ancestor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class WrappedCollection
/*      */     extends AbstractCollection<V>
/*      */   {
/*      */     final K key;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Collection<V> delegate;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final WrappedCollection ancestor;
/*      */ 
/*      */ 
/*      */     
/*      */     final Collection<V> ancestorDelegate;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     WrappedCollection(K key, @Nullable Collection<V> delegate, WrappedCollection ancestor) {
/*  393 */       this.key = key;
/*  394 */       this.delegate = delegate;
/*  395 */       this.ancestor = ancestor;
/*  396 */       this.ancestorDelegate = (ancestor == null) ? null : ancestor.getDelegate();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void refreshIfEmpty() {
/*  408 */       if (this.ancestor != null) {
/*  409 */         this.ancestor.refreshIfEmpty();
/*  410 */         if (this.ancestor.getDelegate() != this.ancestorDelegate) {
/*  411 */           throw new ConcurrentModificationException();
/*      */         }
/*  413 */       } else if (this.delegate.isEmpty()) {
/*  414 */         Collection<V> newDelegate = (Collection<V>)StandardMultimap.this.map.get(this.key);
/*  415 */         if (newDelegate != null) {
/*  416 */           this.delegate = newDelegate;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void removeIfEmpty() {
/*  426 */       if (this.ancestor != null) {
/*  427 */         this.ancestor.removeIfEmpty();
/*  428 */       } else if (this.delegate.isEmpty()) {
/*  429 */         StandardMultimap.this.map.remove(this.key);
/*      */       } 
/*      */     }
/*      */     
/*      */     K getKey() {
/*  434 */       return this.key;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void addToMap() {
/*  445 */       if (this.ancestor != null) {
/*  446 */         this.ancestor.addToMap();
/*      */       } else {
/*  448 */         StandardMultimap.this.map.put(this.key, this.delegate);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/*  453 */       refreshIfEmpty();
/*  454 */       return this.delegate.size();
/*      */     }
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/*  458 */       if (object == this) {
/*  459 */         return true;
/*      */       }
/*  461 */       refreshIfEmpty();
/*  462 */       return this.delegate.equals(object);
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  466 */       refreshIfEmpty();
/*  467 */       return this.delegate.hashCode();
/*      */     }
/*      */     
/*      */     public String toString() {
/*  471 */       refreshIfEmpty();
/*  472 */       return this.delegate.toString();
/*      */     }
/*      */     
/*      */     Collection<V> getDelegate() {
/*  476 */       return this.delegate;
/*      */     }
/*      */     
/*      */     public Iterator<V> iterator() {
/*  480 */       refreshIfEmpty();
/*  481 */       return new WrappedIterator();
/*      */     }
/*      */     
/*      */     class WrappedIterator
/*      */       implements Iterator<V> {
/*      */       final Iterator<V> delegateIterator;
/*  487 */       final Collection<V> originalDelegate = StandardMultimap.WrappedCollection.this.delegate;
/*      */       
/*      */       WrappedIterator() {
/*  490 */         this.delegateIterator = StandardMultimap.this.iteratorOrListIterator(StandardMultimap.WrappedCollection.this.delegate);
/*      */       }
/*      */       
/*      */       WrappedIterator(Iterator<V> delegateIterator) {
/*  494 */         this.delegateIterator = delegateIterator;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       void validateIterator() {
/*  502 */         StandardMultimap.WrappedCollection.this.refreshIfEmpty();
/*  503 */         if (StandardMultimap.WrappedCollection.this.delegate != this.originalDelegate) {
/*  504 */           throw new ConcurrentModificationException();
/*      */         }
/*      */       }
/*      */       
/*      */       public boolean hasNext() {
/*  509 */         validateIterator();
/*  510 */         return this.delegateIterator.hasNext();
/*      */       }
/*      */       
/*      */       public V next() {
/*  514 */         validateIterator();
/*  515 */         return this.delegateIterator.next();
/*      */       }
/*      */       
/*      */       public void remove() {
/*  519 */         this.delegateIterator.remove();
/*  520 */         StandardMultimap.this.totalSize--;
/*  521 */         StandardMultimap.WrappedCollection.this.removeIfEmpty();
/*      */       }
/*      */       
/*      */       Iterator<V> getDelegateIterator() {
/*  525 */         validateIterator();
/*  526 */         return this.delegateIterator;
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean add(V value) {
/*  531 */       refreshIfEmpty();
/*  532 */       boolean wasEmpty = this.delegate.isEmpty();
/*  533 */       boolean changed = this.delegate.add(value);
/*  534 */       if (changed) {
/*  535 */         StandardMultimap.this.totalSize++;
/*  536 */         if (wasEmpty) {
/*  537 */           addToMap();
/*      */         }
/*      */       } 
/*  540 */       return changed;
/*      */     }
/*      */     
/*      */     WrappedCollection getAncestor() {
/*  544 */       return this.ancestor;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean addAll(Collection<? extends V> collection) {
/*  550 */       if (collection.isEmpty()) {
/*  551 */         return false;
/*      */       }
/*  553 */       int oldSize = size();
/*  554 */       boolean changed = this.delegate.addAll(collection);
/*  555 */       if (changed) {
/*  556 */         int newSize = this.delegate.size();
/*  557 */         StandardMultimap.this.totalSize += newSize - oldSize;
/*  558 */         if (oldSize == 0) {
/*  559 */           addToMap();
/*      */         }
/*      */       } 
/*  562 */       return changed;
/*      */     }
/*      */     
/*      */     public boolean contains(Object o) {
/*  566 */       refreshIfEmpty();
/*  567 */       return this.delegate.contains(o);
/*      */     }
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  571 */       refreshIfEmpty();
/*  572 */       return this.delegate.containsAll(c);
/*      */     }
/*      */     
/*      */     public void clear() {
/*  576 */       int oldSize = size();
/*  577 */       if (oldSize == 0) {
/*      */         return;
/*      */       }
/*  580 */       this.delegate.clear();
/*  581 */       StandardMultimap.this.totalSize -= oldSize;
/*  582 */       removeIfEmpty();
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/*  586 */       refreshIfEmpty();
/*  587 */       boolean changed = this.delegate.remove(o);
/*  588 */       if (changed) {
/*  589 */         StandardMultimap.this.totalSize--;
/*  590 */         removeIfEmpty();
/*      */       } 
/*  592 */       return changed;
/*      */     }
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  596 */       if (c.isEmpty()) {
/*  597 */         return false;
/*      */       }
/*  599 */       int oldSize = size();
/*  600 */       boolean changed = this.delegate.removeAll(c);
/*  601 */       if (changed) {
/*  602 */         int newSize = this.delegate.size();
/*  603 */         StandardMultimap.this.totalSize += newSize - oldSize;
/*  604 */         removeIfEmpty();
/*      */       } 
/*  606 */       return changed;
/*      */     }
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*  610 */       Preconditions.checkNotNull(c);
/*  611 */       int oldSize = size();
/*  612 */       boolean changed = this.delegate.retainAll(c);
/*  613 */       if (changed) {
/*  614 */         int newSize = this.delegate.size();
/*  615 */         StandardMultimap.this.totalSize += newSize - oldSize;
/*  616 */         removeIfEmpty();
/*      */       } 
/*  618 */       return changed;
/*      */     }
/*      */   }
/*      */   
/*      */   private Iterator<V> iteratorOrListIterator(Collection<V> collection) {
/*  623 */     return (collection instanceof List) ? ((List<V>)collection).listIterator() : collection.iterator();
/*      */   }
/*      */   
/*      */   private class WrappedSet
/*      */     extends WrappedCollection
/*      */     implements Set<V>
/*      */   {
/*      */     WrappedSet(K key, Set<V> delegate) {
/*  631 */       super(key, delegate, null);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class WrappedSortedSet
/*      */     extends WrappedCollection
/*      */     implements SortedSet<V>
/*      */   {
/*      */     WrappedSortedSet(K key, @Nullable SortedSet<V> delegate, StandardMultimap<K, V>.WrappedCollection ancestor) {
/*  642 */       super(key, delegate, ancestor);
/*      */     }
/*      */     
/*      */     SortedSet<V> getSortedSetDelegate() {
/*  646 */       return (SortedSet<V>)getDelegate();
/*      */     }
/*      */     
/*      */     public Comparator<? super V> comparator() {
/*  650 */       return getSortedSetDelegate().comparator();
/*      */     }
/*      */     
/*      */     public V first() {
/*  654 */       refreshIfEmpty();
/*  655 */       return getSortedSetDelegate().first();
/*      */     }
/*      */     
/*      */     public V last() {
/*  659 */       refreshIfEmpty();
/*  660 */       return getSortedSetDelegate().last();
/*      */     }
/*      */     
/*      */     public SortedSet<V> headSet(V toElement) {
/*  664 */       refreshIfEmpty();
/*  665 */       return new WrappedSortedSet(getKey(), getSortedSetDelegate().headSet(toElement), (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public SortedSet<V> subSet(V fromElement, V toElement) {
/*  671 */       refreshIfEmpty();
/*  672 */       return new WrappedSortedSet(getKey(), getSortedSetDelegate().subSet(fromElement, toElement), (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public SortedSet<V> tailSet(V fromElement) {
/*  678 */       refreshIfEmpty();
/*  679 */       return new WrappedSortedSet(getKey(), getSortedSetDelegate().tailSet(fromElement), (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */   }
/*      */   
/*      */   private class WrappedList
/*      */     extends WrappedCollection
/*      */     implements List<V>
/*      */   {
/*      */     WrappedList(K key, @Nullable List<V> delegate, StandardMultimap<K, V>.WrappedCollection ancestor) {
/*  688 */       super(key, delegate, ancestor);
/*      */     }
/*      */     
/*      */     List<V> getListDelegate() {
/*  692 */       return (List<V>)getDelegate();
/*      */     }
/*      */     
/*      */     public boolean addAll(int index, Collection<? extends V> c) {
/*  696 */       if (c.isEmpty()) {
/*  697 */         return false;
/*      */       }
/*  699 */       int oldSize = size();
/*  700 */       boolean changed = getListDelegate().addAll(index, c);
/*  701 */       if (changed) {
/*  702 */         int newSize = getDelegate().size();
/*  703 */         StandardMultimap.this.totalSize += newSize - oldSize;
/*  704 */         if (oldSize == 0) {
/*  705 */           addToMap();
/*      */         }
/*      */       } 
/*  708 */       return changed;
/*      */     }
/*      */     
/*      */     public V get(int index) {
/*  712 */       refreshIfEmpty();
/*  713 */       return getListDelegate().get(index);
/*      */     }
/*      */     
/*      */     public V set(int index, V element) {
/*  717 */       refreshIfEmpty();
/*  718 */       return getListDelegate().set(index, element);
/*      */     }
/*      */     
/*      */     public void add(int index, V element) {
/*  722 */       refreshIfEmpty();
/*  723 */       boolean wasEmpty = getDelegate().isEmpty();
/*  724 */       getListDelegate().add(index, element);
/*  725 */       StandardMultimap.this.totalSize++;
/*  726 */       if (wasEmpty) {
/*  727 */         addToMap();
/*      */       }
/*      */     }
/*      */     
/*      */     public V remove(int index) {
/*  732 */       refreshIfEmpty();
/*  733 */       V value = getListDelegate().remove(index);
/*  734 */       StandardMultimap.this.totalSize--;
/*  735 */       removeIfEmpty();
/*  736 */       return value;
/*      */     }
/*      */     
/*      */     public int indexOf(Object o) {
/*  740 */       refreshIfEmpty();
/*  741 */       return getListDelegate().indexOf(o);
/*      */     }
/*      */     
/*      */     public int lastIndexOf(Object o) {
/*  745 */       refreshIfEmpty();
/*  746 */       return getListDelegate().lastIndexOf(o);
/*      */     }
/*      */     
/*      */     public ListIterator<V> listIterator() {
/*  750 */       refreshIfEmpty();
/*  751 */       return new WrappedListIterator();
/*      */     }
/*      */     
/*      */     public ListIterator<V> listIterator(int index) {
/*  755 */       refreshIfEmpty();
/*  756 */       return new WrappedListIterator(index);
/*      */     }
/*      */     
/*      */     @GwtIncompatible("List.subList")
/*      */     public List<V> subList(int fromIndex, int toIndex) {
/*  761 */       refreshIfEmpty();
/*  762 */       return StandardMultimap.this.wrapList(getKey(), Platform.subList(getListDelegate(), fromIndex, toIndex), (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */     
/*      */     private class WrappedListIterator
/*      */       extends StandardMultimap<K, V>.WrappedCollection.WrappedIterator
/*      */       implements ListIterator<V>
/*      */     {
/*      */       WrappedListIterator() {}
/*      */       
/*      */       public WrappedListIterator(int index) {
/*  773 */         super(StandardMultimap.WrappedList.this.getListDelegate().listIterator(index));
/*      */       }
/*      */       
/*      */       private ListIterator<V> getDelegateListIterator() {
/*  777 */         return (ListIterator<V>)getDelegateIterator();
/*      */       }
/*      */       
/*      */       public boolean hasPrevious() {
/*  781 */         return getDelegateListIterator().hasPrevious();
/*      */       }
/*      */       
/*      */       public V previous() {
/*  785 */         return getDelegateListIterator().previous();
/*      */       }
/*      */       
/*      */       public int nextIndex() {
/*  789 */         return getDelegateListIterator().nextIndex();
/*      */       }
/*      */       
/*      */       public int previousIndex() {
/*  793 */         return getDelegateListIterator().previousIndex();
/*      */       }
/*      */       
/*      */       public void set(V value) {
/*  797 */         getDelegateListIterator().set(value);
/*      */       }
/*      */       
/*      */       public void add(V value) {
/*  801 */         boolean wasEmpty = StandardMultimap.WrappedList.this.isEmpty();
/*  802 */         getDelegateListIterator().add(value);
/*  803 */         StandardMultimap.this.totalSize++;
/*  804 */         if (wasEmpty) {
/*  805 */           StandardMultimap.WrappedList.this.addToMap();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class RandomAccessWrappedList
/*      */     extends WrappedList
/*      */     implements RandomAccess
/*      */   {
/*      */     RandomAccessWrappedList(K key, @Nullable List<V> delegate, StandardMultimap<K, V>.WrappedCollection ancestor) {
/*  819 */       super(key, delegate, ancestor);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<K> keySet() {
/*  826 */     Set<K> result = this.keySet;
/*  827 */     return (result == null) ? (this.keySet = createKeySet()) : result;
/*      */   }
/*      */   
/*      */   private Set<K> createKeySet() {
/*  831 */     return (this.map instanceof SortedMap) ? new SortedKeySet((SortedMap<K, Collection<V>>)this.map) : new KeySet(this.map);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class KeySet
/*      */     extends AbstractSet<K>
/*      */   {
/*      */     final Map<K, Collection<V>> subMap;
/*      */ 
/*      */ 
/*      */     
/*      */     KeySet(Map<K, Collection<V>> subMap) {
/*  844 */       this.subMap = subMap;
/*      */     }
/*      */     
/*      */     public int size() {
/*  848 */       return this.subMap.size();
/*      */     }
/*      */     
/*      */     public Iterator<K> iterator() {
/*  852 */       return new Iterator<K>() {
/*  853 */           final Iterator<Map.Entry<K, Collection<V>>> entryIterator = StandardMultimap.KeySet.this.subMap.entrySet().iterator();
/*      */           
/*      */           Map.Entry<K, Collection<V>> entry;
/*      */           
/*      */           public boolean hasNext() {
/*  858 */             return this.entryIterator.hasNext();
/*      */           }
/*      */           public K next() {
/*  861 */             this.entry = this.entryIterator.next();
/*  862 */             return this.entry.getKey();
/*      */           }
/*      */           public void remove() {
/*  865 */             this.entryIterator.remove();
/*  866 */             StandardMultimap.this.totalSize -= ((Collection)this.entry.getValue()).size();
/*  867 */             ((Collection)this.entry.getValue()).clear();
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean contains(Object key) {
/*  875 */       return this.subMap.containsKey(key);
/*      */     }
/*      */     
/*      */     public boolean remove(Object key) {
/*  879 */       int count = 0;
/*  880 */       Collection<V> collection = this.subMap.remove(key);
/*  881 */       if (collection != null) {
/*  882 */         count = collection.size();
/*  883 */         collection.clear();
/*  884 */         StandardMultimap.this.totalSize -= count;
/*      */       } 
/*  886 */       return (count > 0);
/*      */     }
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  890 */       return this.subMap.keySet().containsAll(c);
/*      */     }
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/*  894 */       return (this == object || this.subMap.keySet().equals(object));
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  898 */       return this.subMap.keySet().hashCode();
/*      */     }
/*      */   }
/*      */   
/*      */   private class SortedKeySet
/*      */     extends KeySet implements SortedSet<K> {
/*      */     SortedKeySet(SortedMap<K, Collection<V>> subMap) {
/*  905 */       super(subMap);
/*      */     }
/*      */     
/*      */     SortedMap<K, Collection<V>> sortedMap() {
/*  909 */       return (SortedMap<K, Collection<V>>)this.subMap;
/*      */     }
/*      */     
/*      */     public Comparator<? super K> comparator() {
/*  913 */       return sortedMap().comparator();
/*      */     }
/*      */     
/*      */     public K first() {
/*  917 */       return sortedMap().firstKey();
/*      */     }
/*      */     
/*      */     public SortedSet<K> headSet(K toElement) {
/*  921 */       return new SortedKeySet(sortedMap().headMap(toElement));
/*      */     }
/*      */     
/*      */     public K last() {
/*  925 */       return sortedMap().lastKey();
/*      */     }
/*      */     
/*      */     public SortedSet<K> subSet(K fromElement, K toElement) {
/*  929 */       return new SortedKeySet(sortedMap().subMap(fromElement, toElement));
/*      */     }
/*      */     
/*      */     public SortedSet<K> tailSet(K fromElement) {
/*  933 */       return new SortedKeySet(sortedMap().tailMap(fromElement));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Multiset<K> keys() {
/*  940 */     Multiset<K> result = this.multiset;
/*  941 */     return (result == null) ? (this.multiset = new MultisetView()) : result;
/*      */   }
/*      */   private class MultisetView extends AbstractMultiset<K> { transient Set<Multiset.Entry<K>> entrySet;
/*      */     private MultisetView() {}
/*      */     
/*      */     public int remove(Object key, int occurrences) {
/*      */       Collection<V> collection;
/*  948 */       if (occurrences == 0) {
/*  949 */         return count(key);
/*      */       }
/*  951 */       Preconditions.checkArgument((occurrences > 0));
/*      */ 
/*      */       
/*      */       try {
/*  955 */         collection = (Collection<V>)StandardMultimap.this.map.get(key);
/*  956 */       } catch (NullPointerException e) {
/*  957 */         return 0;
/*  958 */       } catch (ClassCastException e) {
/*  959 */         return 0;
/*      */       } 
/*      */       
/*  962 */       if (collection == null) {
/*  963 */         return 0;
/*      */       }
/*  965 */       int count = collection.size();
/*      */       
/*  967 */       if (occurrences >= count) {
/*  968 */         return StandardMultimap.this.removeValuesForKey(key);
/*      */       }
/*      */       
/*  971 */       Iterator<V> iterator = collection.iterator();
/*  972 */       for (int i = 0; i < occurrences; i++) {
/*  973 */         iterator.next();
/*  974 */         iterator.remove();
/*      */       } 
/*  976 */       StandardMultimap.this.totalSize -= occurrences;
/*  977 */       return count;
/*      */     }
/*      */     
/*      */     public Set<K> elementSet() {
/*  981 */       return StandardMultimap.this.keySet();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Multiset.Entry<K>> entrySet() {
/*  987 */       Set<Multiset.Entry<K>> result = this.entrySet;
/*  988 */       return (result == null) ? (this.entrySet = new EntrySet()) : result;
/*      */     }
/*      */     private class EntrySet extends AbstractSet<Multiset.Entry<K>> { private EntrySet() {}
/*      */       
/*      */       public Iterator<Multiset.Entry<K>> iterator() {
/*  993 */         return new StandardMultimap.MultisetEntryIterator();
/*      */       }
/*      */       public int size() {
/*  996 */         return StandardMultimap.this.map.size();
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean contains(Object o) {
/* 1002 */         if (!(o instanceof Multiset.Entry)) {
/* 1003 */           return false;
/*      */         }
/* 1005 */         Multiset.Entry<?> entry = (Multiset.Entry)o;
/* 1006 */         Collection<V> collection = (Collection<V>)StandardMultimap.this.map.get(entry.getElement());
/* 1007 */         return (collection != null && collection.size() == entry.getCount());
/*      */       }
/*      */       
/*      */       public void clear() {
/* 1011 */         StandardMultimap.this.clear();
/*      */       }
/*      */       public boolean remove(Object o) {
/* 1014 */         return (contains(o) && StandardMultimap.this.removeValuesForKey(((Multiset.Entry)o).getElement()) > 0);
/*      */       } }
/*      */ 
/*      */ 
/*      */     
/*      */     public Iterator<K> iterator() {
/* 1020 */       return new StandardMultimap.MultisetKeyIterator();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int count(Object key) {
/*      */       try {
/* 1027 */         Collection<V> collection = (Collection<V>)StandardMultimap.this.map.get(key);
/* 1028 */         return (collection == null) ? 0 : collection.size();
/* 1029 */       } catch (NullPointerException e) {
/* 1030 */         return 0;
/* 1031 */       } catch (ClassCastException e) {
/* 1032 */         return 0;
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/* 1037 */       return StandardMultimap.this.totalSize;
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1041 */       StandardMultimap.this.clear();
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int removeValuesForKey(Object key) {
/*      */     Collection<V> collection;
/*      */     try {
/* 1052 */       collection = this.map.remove(key);
/* 1053 */     } catch (NullPointerException e) {
/* 1054 */       return 0;
/* 1055 */     } catch (ClassCastException e) {
/* 1056 */       return 0;
/*      */     } 
/*      */     
/* 1059 */     int count = 0;
/* 1060 */     if (collection != null) {
/* 1061 */       count = collection.size();
/* 1062 */       collection.clear();
/* 1063 */       this.totalSize -= count;
/*      */     } 
/* 1065 */     return count;
/*      */   }
/*      */   
/*      */   private class MultisetEntryIterator
/*      */     implements Iterator<Multiset.Entry<K>> {
/* 1070 */     final Iterator<Map.Entry<K, Collection<V>>> asMapIterator = StandardMultimap.this.asMap().entrySet().iterator();
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1074 */       return this.asMapIterator.hasNext();
/*      */     }
/*      */     public Multiset.Entry<K> next() {
/* 1077 */       return new StandardMultimap.MultisetEntry(this.asMapIterator.next());
/*      */     }
/*      */     public void remove() {
/* 1080 */       this.asMapIterator.remove();
/*      */     }
/*      */     
/*      */     private MultisetEntryIterator() {} }
/*      */   
/*      */   private class MultisetEntry extends Multisets.AbstractEntry<K> { final Map.Entry<K, Collection<V>> entry;
/*      */     
/*      */     public MultisetEntry(Map.Entry<K, Collection<V>> entry) {
/* 1088 */       this.entry = entry;
/*      */     }
/*      */     public K getElement() {
/* 1091 */       return this.entry.getKey();
/*      */     }
/*      */     public int getCount() {
/* 1094 */       return ((Collection)this.entry.getValue()).size();
/*      */     } }
/*      */ 
/*      */   
/*      */   private class MultisetKeyIterator
/*      */     implements Iterator<K> {
/* 1100 */     final Iterator<Map.Entry<K, V>> entryIterator = StandardMultimap.this.entries().iterator();
/*      */     
/*      */     public boolean hasNext() {
/* 1103 */       return this.entryIterator.hasNext();
/*      */     }
/*      */     public K next() {
/* 1106 */       return (K)((Map.Entry)this.entryIterator.next()).getKey();
/*      */     }
/*      */     public void remove() {
/* 1109 */       this.entryIterator.remove();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private MultisetKeyIterator() {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> values() {
/* 1122 */     Collection<V> result = this.valuesCollection;
/* 1123 */     return (result == null) ? (this.valuesCollection = new Values()) : result;
/*      */   }
/*      */   private class Values extends AbstractCollection<V> { private Values() {}
/*      */     
/*      */     public Iterator<V> iterator() {
/* 1128 */       return new StandardMultimap.ValueIterator();
/*      */     }
/*      */     public int size() {
/* 1131 */       return StandardMultimap.this.totalSize;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1137 */       StandardMultimap.this.clear();
/*      */     }
/*      */     
/*      */     public boolean contains(Object value) {
/* 1141 */       return StandardMultimap.this.containsValue(value);
/*      */     } }
/*      */ 
/*      */   
/*      */   private class ValueIterator
/*      */     implements Iterator<V> {
/* 1147 */     final Iterator<Map.Entry<K, V>> entryIterator = StandardMultimap.this.createEntryIterator();
/*      */     
/*      */     public boolean hasNext() {
/* 1150 */       return this.entryIterator.hasNext();
/*      */     }
/*      */     public V next() {
/* 1153 */       return (V)((Map.Entry)this.entryIterator.next()).getValue();
/*      */     }
/*      */     public void remove() {
/* 1156 */       this.entryIterator.remove();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private ValueIterator() {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<Map.Entry<K, V>> entries() {
/* 1177 */     Collection<Map.Entry<K, V>> result = this.entries;
/* 1178 */     return (this.entries == null) ? (this.entries = createEntries()) : result;
/*      */   }
/*      */ 
/*      */   
/*      */   private Collection<Map.Entry<K, V>> createEntries() {
/* 1183 */     return (this instanceof SetMultimap) ? new EntrySet() : new Entries();
/*      */   }
/*      */   
/*      */   private class Entries extends AbstractCollection<Map.Entry<K, V>> { private Entries() {}
/*      */     
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/* 1189 */       return StandardMultimap.this.createEntryIterator();
/*      */     }
/*      */     public int size() {
/* 1192 */       return StandardMultimap.this.totalSize;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1198 */       if (!(o instanceof Map.Entry)) {
/* 1199 */         return false;
/*      */       }
/* 1201 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1202 */       return StandardMultimap.this.containsEntry(entry.getKey(), entry.getValue());
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1206 */       StandardMultimap.this.clear();
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/* 1210 */       if (!(o instanceof Map.Entry)) {
/* 1211 */         return false;
/*      */       }
/* 1213 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1214 */       return StandardMultimap.this.remove(entry.getKey(), entry.getValue());
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Iterator<Map.Entry<K, V>> createEntryIterator() {
/* 1227 */     return new EntryIterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class EntryIterator
/*      */     implements Iterator<Map.Entry<K, V>>
/*      */   {
/* 1238 */     final Iterator<Map.Entry<K, Collection<V>>> keyIterator = StandardMultimap.this.map.entrySet().iterator(); K key; EntryIterator() {
/* 1239 */       if (this.keyIterator.hasNext()) {
/* 1240 */         findValueIteratorAndKey();
/*      */       } else {
/* 1242 */         this.valueIterator = Iterators.emptyModifiableIterator();
/*      */       } 
/*      */     }
/*      */     Collection<V> collection; Iterator<V> valueIterator;
/*      */     void findValueIteratorAndKey() {
/* 1247 */       Map.Entry<K, Collection<V>> entry = this.keyIterator.next();
/* 1248 */       this.key = entry.getKey();
/* 1249 */       this.collection = entry.getValue();
/* 1250 */       this.valueIterator = this.collection.iterator();
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/* 1254 */       return (this.keyIterator.hasNext() || this.valueIterator.hasNext());
/*      */     }
/*      */     
/*      */     public Map.Entry<K, V> next() {
/* 1258 */       if (!this.valueIterator.hasNext()) {
/* 1259 */         findValueIteratorAndKey();
/*      */       }
/* 1261 */       return Maps.immutableEntry(this.key, this.valueIterator.next());
/*      */     }
/*      */     
/*      */     public void remove() {
/* 1265 */       this.valueIterator.remove();
/* 1266 */       if (this.collection.isEmpty()) {
/* 1267 */         this.keyIterator.remove();
/*      */       }
/* 1269 */       StandardMultimap.this.totalSize--;
/*      */     } }
/*      */   
/*      */   private class EntrySet extends Entries implements Set<Map.Entry<K, V>> {
/*      */     private EntrySet() {}
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/* 1276 */       return Collections2.setEquals(this, object);
/*      */     }
/*      */     public int hashCode() {
/* 1279 */       return Sets.hashCodeImpl(this);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<K, Collection<V>> asMap() {
/* 1286 */     Map<K, Collection<V>> result = this.asMap;
/* 1287 */     return (result == null) ? (this.asMap = createAsMap()) : result;
/*      */   }
/*      */   
/*      */   private Map<K, Collection<V>> createAsMap() {
/* 1291 */     return (this.map instanceof SortedMap) ? new SortedAsMap((SortedMap<K, Collection<V>>)this.map) : new AsMap(this.map);
/*      */   }
/*      */ 
/*      */   
/*      */   private class AsMap
/*      */     extends AbstractMap<K, Collection<V>>
/*      */   {
/*      */     transient Map<K, Collection<V>> submap;
/*      */     
/*      */     transient Set<Map.Entry<K, Collection<V>>> entrySet;
/*      */     
/*      */     AsMap(Map<K, Collection<V>> submap) {
/* 1303 */       this.submap = submap;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, Collection<V>>> entrySet() {
/* 1309 */       Set<Map.Entry<K, Collection<V>>> result = this.entrySet;
/* 1310 */       return (this.entrySet == null) ? (this.entrySet = new AsMapEntries()) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 1316 */       return this.submap.containsKey(key);
/*      */     }
/*      */     
/*      */     public Collection<V> get(Object key) {
/* 1320 */       Collection<V> collection = this.submap.get(key);
/* 1321 */       if (collection == null) {
/* 1322 */         return null;
/*      */       }
/*      */       
/* 1325 */       K k = (K)key;
/* 1326 */       return StandardMultimap.this.wrapCollection(k, collection);
/*      */     }
/*      */     
/*      */     public Set<K> keySet() {
/* 1330 */       return StandardMultimap.this.keySet();
/*      */     }
/*      */     
/*      */     public Collection<V> remove(Object key) {
/* 1334 */       Collection<V> collection = this.submap.remove(key);
/* 1335 */       if (collection == null) {
/* 1336 */         return null;
/*      */       }
/*      */       
/* 1339 */       Collection<V> output = StandardMultimap.this.createCollection();
/* 1340 */       output.addAll(collection);
/* 1341 */       StandardMultimap.this.totalSize -= collection.size();
/* 1342 */       collection.clear();
/* 1343 */       return output;
/*      */     }
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/* 1347 */       return (this == object || this.submap.equals(object));
/*      */     }
/*      */     
/*      */     public int hashCode() {
/* 1351 */       return this.submap.hashCode();
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1355 */       return this.submap.toString();
/*      */     }
/*      */     
/*      */     class AsMapEntries extends AbstractSet<Map.Entry<K, Collection<V>>> {
/*      */       public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/* 1360 */         return new StandardMultimap.AsMap.AsMapIterator();
/*      */       }
/*      */       
/*      */       public int size() {
/* 1364 */         return StandardMultimap.AsMap.this.submap.size();
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean contains(Object o) {
/* 1370 */         return StandardMultimap.AsMap.this.submap.entrySet().contains(o);
/*      */       }
/*      */       
/*      */       public boolean remove(Object o) {
/* 1374 */         if (!contains(o)) {
/* 1375 */           return false;
/*      */         }
/* 1377 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1378 */         StandardMultimap.this.removeValuesForKey(entry.getKey());
/* 1379 */         return true;
/*      */       }
/*      */     }
/*      */     
/*      */     class AsMapIterator
/*      */       implements Iterator<Map.Entry<K, Collection<V>>> {
/* 1385 */       final Iterator<Map.Entry<K, Collection<V>>> delegateIterator = StandardMultimap.AsMap.this.submap.entrySet().iterator();
/*      */       
/*      */       Collection<V> collection;
/*      */       
/*      */       public boolean hasNext() {
/* 1390 */         return this.delegateIterator.hasNext();
/*      */       }
/*      */       
/*      */       public Map.Entry<K, Collection<V>> next() {
/* 1394 */         Map.Entry<K, Collection<V>> entry = this.delegateIterator.next();
/* 1395 */         K key = entry.getKey();
/* 1396 */         this.collection = entry.getValue();
/* 1397 */         return Maps.immutableEntry(key, StandardMultimap.this.wrapCollection(key, this.collection));
/*      */       }
/*      */       
/*      */       public void remove() {
/* 1401 */         this.delegateIterator.remove();
/* 1402 */         StandardMultimap.this.totalSize -= this.collection.size();
/* 1403 */         this.collection.clear();
/*      */       } }
/*      */   }
/*      */   
/*      */   private class SortedAsMap extends AsMap implements SortedMap<K, Collection<V>> {
/*      */     SortedSet<K> sortedKeySet;
/*      */     
/*      */     SortedAsMap(SortedMap<K, Collection<V>> submap) {
/* 1411 */       super(submap);
/*      */     }
/*      */     
/*      */     SortedMap<K, Collection<V>> sortedMap() {
/* 1415 */       return (SortedMap<K, Collection<V>>)this.submap;
/*      */     }
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 1419 */       return sortedMap().comparator();
/*      */     }
/*      */     
/*      */     public K firstKey() {
/* 1423 */       return sortedMap().firstKey();
/*      */     }
/*      */     
/*      */     public K lastKey() {
/* 1427 */       return sortedMap().lastKey();
/*      */     }
/*      */     
/*      */     public SortedMap<K, Collection<V>> headMap(K toKey) {
/* 1431 */       return new SortedAsMap(sortedMap().headMap(toKey));
/*      */     }
/*      */     
/*      */     public SortedMap<K, Collection<V>> subMap(K fromKey, K toKey) {
/* 1435 */       return new SortedAsMap(sortedMap().subMap(fromKey, toKey));
/*      */     }
/*      */     
/*      */     public SortedMap<K, Collection<V>> tailMap(K fromKey) {
/* 1439 */       return new SortedAsMap(sortedMap().tailMap(fromKey));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SortedSet<K> keySet() {
/* 1447 */       SortedSet<K> result = this.sortedKeySet;
/* 1448 */       return (result == null) ? (this.sortedKeySet = new StandardMultimap.SortedKeySet(sortedMap())) : result;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(@Nullable Object object) {
/* 1456 */     if (object == this) {
/* 1457 */       return true;
/*      */     }
/* 1459 */     if (object instanceof Multimap) {
/* 1460 */       Multimap<?, ?> that = (Multimap<?, ?>)object;
/* 1461 */       return this.map.equals(that.asMap());
/*      */     } 
/* 1463 */     return false;
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
/*      */   public int hashCode() {
/* 1475 */     return this.map.hashCode();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1485 */     return this.map.toString();
/*      */   }
/*      */   
/*      */   abstract Collection<V> createCollection();
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\StandardMultimap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */