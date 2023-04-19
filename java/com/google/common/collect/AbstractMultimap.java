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
/*      */ abstract class AbstractMultimap<K, V>
/*      */   implements Multimap<K, V>, Serializable
/*      */ {
/*      */   private transient Map<K, Collection<V>> map;
/*      */   private transient int totalSize;
/*      */   private transient Set<K> keySet;
/*      */   private transient Multiset<K> multiset;
/*      */   private transient Collection<V> valuesCollection;
/*      */   private transient Collection<Map.Entry<K, V>> entries;
/*      */   private transient Map<K, Collection<V>> asMap;
/*      */   private static final long serialVersionUID = 2447537837011683357L;
/*      */   
/*      */   protected AbstractMultimap(Map<K, Collection<V>> map) {
/*  118 */     Preconditions.checkArgument(map.isEmpty());
/*  119 */     this.map = map;
/*      */   }
/*      */ 
/*      */   
/*      */   final void setMap(Map<K, Collection<V>> map) {
/*  124 */     this.map = map;
/*  125 */     this.totalSize = 0;
/*  126 */     for (Collection<V> values : map.values()) {
/*  127 */       Preconditions.checkArgument(!values.isEmpty());
/*  128 */       this.totalSize += values.size();
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
/*  155 */     return createCollection();
/*      */   }
/*      */   
/*      */   Map<K, Collection<V>> backingMap() {
/*  159 */     return this.map;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  165 */     return this.totalSize;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  169 */     return (this.totalSize == 0);
/*      */   }
/*      */   
/*      */   public boolean containsKey(@Nullable Object key) {
/*  173 */     return this.map.containsKey(key);
/*      */   }
/*      */   
/*      */   public boolean containsValue(@Nullable Object value) {
/*  177 */     for (Collection<V> collection : this.map.values()) {
/*  178 */       if (collection.contains(value)) {
/*  179 */         return true;
/*      */       }
/*      */     } 
/*      */     
/*  183 */     return false;
/*      */   }
/*      */   
/*      */   public boolean containsEntry(@Nullable Object key, @Nullable Object value) {
/*  187 */     Collection<V> collection = this.map.get(key);
/*  188 */     return (collection != null && collection.contains(value));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean put(@Nullable K key, @Nullable V value) {
/*  194 */     Collection<V> collection = getOrCreateCollection(key);
/*      */     
/*  196 */     if (collection.add(value)) {
/*  197 */       this.totalSize++;
/*  198 */       return true;
/*      */     } 
/*  200 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private Collection<V> getOrCreateCollection(@Nullable K key) {
/*  205 */     Collection<V> collection = this.map.get(key);
/*  206 */     if (collection == null) {
/*  207 */       collection = createCollection(key);
/*  208 */       this.map.put(key, collection);
/*      */     } 
/*  210 */     return collection;
/*      */   }
/*      */   
/*      */   public boolean remove(@Nullable Object key, @Nullable Object value) {
/*  214 */     Collection<V> collection = this.map.get(key);
/*  215 */     if (collection == null) {
/*  216 */       return false;
/*      */     }
/*      */     
/*  219 */     boolean changed = collection.remove(value);
/*  220 */     if (changed) {
/*  221 */       this.totalSize--;
/*  222 */       if (collection.isEmpty()) {
/*  223 */         this.map.remove(key);
/*      */       }
/*      */     } 
/*  226 */     return changed;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean putAll(@Nullable K key, Iterable<? extends V> values) {
/*  232 */     if (!values.iterator().hasNext()) {
/*  233 */       return false;
/*      */     }
/*  235 */     Collection<V> collection = getOrCreateCollection(key);
/*  236 */     int oldSize = collection.size();
/*      */     
/*  238 */     boolean changed = false;
/*  239 */     if (values instanceof Collection) {
/*      */       
/*  241 */       Collection<? extends V> c = (Collection<? extends V>)values;
/*  242 */       changed = collection.addAll(c);
/*      */     } else {
/*  244 */       for (V value : values) {
/*  245 */         changed |= collection.add(value);
/*      */       }
/*      */     } 
/*      */     
/*  249 */     this.totalSize += collection.size() - oldSize;
/*  250 */     return changed;
/*      */   }
/*      */   
/*      */   public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/*  254 */     boolean changed = false;
/*  255 */     for (Map.Entry<? extends K, ? extends V> entry : multimap.entries()) {
/*  256 */       changed |= put(entry.getKey(), entry.getValue());
/*      */     }
/*  258 */     return changed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
/*  268 */     Iterator<? extends V> iterator = values.iterator();
/*  269 */     if (!iterator.hasNext()) {
/*  270 */       return removeAll(key);
/*      */     }
/*      */     
/*  273 */     Collection<V> collection = getOrCreateCollection(key);
/*  274 */     Collection<V> oldValues = createCollection();
/*  275 */     oldValues.addAll(collection);
/*      */     
/*  277 */     this.totalSize -= collection.size();
/*  278 */     collection.clear();
/*      */     
/*  280 */     while (iterator.hasNext()) {
/*  281 */       if (collection.add(iterator.next())) {
/*  282 */         this.totalSize++;
/*      */       }
/*      */     } 
/*      */     
/*  286 */     return unmodifiableCollectionSubclass(oldValues);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> removeAll(@Nullable Object key) {
/*  295 */     Collection<V> collection = this.map.remove(key);
/*  296 */     Collection<V> output = createCollection();
/*      */     
/*  298 */     if (collection != null) {
/*  299 */       output.addAll(collection);
/*  300 */       this.totalSize -= collection.size();
/*  301 */       collection.clear();
/*      */     } 
/*      */     
/*  304 */     return unmodifiableCollectionSubclass(output);
/*      */   }
/*      */ 
/*      */   
/*      */   private Collection<V> unmodifiableCollectionSubclass(Collection<V> collection) {
/*  309 */     if (collection instanceof SortedSet)
/*  310 */       return Collections.unmodifiableSortedSet((SortedSet<V>)collection); 
/*  311 */     if (collection instanceof Set)
/*  312 */       return Collections.unmodifiableSet((Set<? extends V>)collection); 
/*  313 */     if (collection instanceof List) {
/*  314 */       return Collections.unmodifiableList((List<? extends V>)collection);
/*      */     }
/*  316 */     return Collections.unmodifiableCollection(collection);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear() {
/*  322 */     for (Collection<V> collection : this.map.values()) {
/*  323 */       collection.clear();
/*      */     }
/*  325 */     this.map.clear();
/*  326 */     this.totalSize = 0;
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
/*  337 */     Collection<V> collection = this.map.get(key);
/*  338 */     if (collection == null) {
/*  339 */       collection = createCollection(key);
/*      */     }
/*  341 */     return wrapCollection(key, collection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Collection<V> wrapCollection(@Nullable K key, Collection<V> collection) {
/*  351 */     if (collection instanceof SortedSet)
/*  352 */       return new WrappedSortedSet(key, (SortedSet<V>)collection, null); 
/*  353 */     if (collection instanceof Set)
/*  354 */       return new WrappedSet(key, (Set<V>)collection); 
/*  355 */     if (collection instanceof List) {
/*  356 */       return wrapList(key, (List<V>)collection, null);
/*      */     }
/*  358 */     return new WrappedCollection(key, collection, null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private List<V> wrapList(K key, List<V> list, @Nullable WrappedCollection ancestor) {
/*  364 */     return (list instanceof RandomAccess) ? new RandomAccessWrappedList(key, list, ancestor) : new WrappedList(key, list, ancestor);
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
/*  394 */       this.key = key;
/*  395 */       this.delegate = delegate;
/*  396 */       this.ancestor = ancestor;
/*  397 */       this.ancestorDelegate = (ancestor == null) ? null : ancestor.getDelegate();
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
/*  409 */       if (this.ancestor != null) {
/*  410 */         this.ancestor.refreshIfEmpty();
/*  411 */         if (this.ancestor.getDelegate() != this.ancestorDelegate) {
/*  412 */           throw new ConcurrentModificationException();
/*      */         }
/*  414 */       } else if (this.delegate.isEmpty()) {
/*  415 */         Collection<V> newDelegate = (Collection<V>)AbstractMultimap.this.map.get(this.key);
/*  416 */         if (newDelegate != null) {
/*  417 */           this.delegate = newDelegate;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void removeIfEmpty() {
/*  427 */       if (this.ancestor != null) {
/*  428 */         this.ancestor.removeIfEmpty();
/*  429 */       } else if (this.delegate.isEmpty()) {
/*  430 */         AbstractMultimap.this.map.remove(this.key);
/*      */       } 
/*      */     }
/*      */     
/*      */     K getKey() {
/*  435 */       return this.key;
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
/*  446 */       if (this.ancestor != null) {
/*  447 */         this.ancestor.addToMap();
/*      */       } else {
/*  449 */         AbstractMultimap.this.map.put(this.key, this.delegate);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/*  454 */       refreshIfEmpty();
/*  455 */       return this.delegate.size();
/*      */     }
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/*  459 */       if (object == this) {
/*  460 */         return true;
/*      */       }
/*  462 */       refreshIfEmpty();
/*  463 */       return this.delegate.equals(object);
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  467 */       refreshIfEmpty();
/*  468 */       return this.delegate.hashCode();
/*      */     }
/*      */     
/*      */     public String toString() {
/*  472 */       refreshIfEmpty();
/*  473 */       return this.delegate.toString();
/*      */     }
/*      */     
/*      */     Collection<V> getDelegate() {
/*  477 */       return this.delegate;
/*      */     }
/*      */     
/*      */     public Iterator<V> iterator() {
/*  481 */       refreshIfEmpty();
/*  482 */       return new WrappedIterator();
/*      */     }
/*      */     
/*      */     class WrappedIterator
/*      */       implements Iterator<V> {
/*      */       final Iterator<V> delegateIterator;
/*  488 */       final Collection<V> originalDelegate = AbstractMultimap.WrappedCollection.this.delegate;
/*      */       
/*      */       WrappedIterator() {
/*  491 */         this.delegateIterator = AbstractMultimap.this.iteratorOrListIterator(AbstractMultimap.WrappedCollection.this.delegate);
/*      */       }
/*      */       
/*      */       WrappedIterator(Iterator<V> delegateIterator) {
/*  495 */         this.delegateIterator = delegateIterator;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       void validateIterator() {
/*  503 */         AbstractMultimap.WrappedCollection.this.refreshIfEmpty();
/*  504 */         if (AbstractMultimap.WrappedCollection.this.delegate != this.originalDelegate) {
/*  505 */           throw new ConcurrentModificationException();
/*      */         }
/*      */       }
/*      */       
/*      */       public boolean hasNext() {
/*  510 */         validateIterator();
/*  511 */         return this.delegateIterator.hasNext();
/*      */       }
/*      */       
/*      */       public V next() {
/*  515 */         validateIterator();
/*  516 */         return this.delegateIterator.next();
/*      */       }
/*      */       
/*      */       public void remove() {
/*  520 */         this.delegateIterator.remove();
/*  521 */         AbstractMultimap.this.totalSize--;
/*  522 */         AbstractMultimap.WrappedCollection.this.removeIfEmpty();
/*      */       }
/*      */       
/*      */       Iterator<V> getDelegateIterator() {
/*  526 */         validateIterator();
/*  527 */         return this.delegateIterator;
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean add(V value) {
/*  532 */       refreshIfEmpty();
/*  533 */       boolean wasEmpty = this.delegate.isEmpty();
/*  534 */       boolean changed = this.delegate.add(value);
/*  535 */       if (changed) {
/*  536 */         AbstractMultimap.this.totalSize++;
/*  537 */         if (wasEmpty) {
/*  538 */           addToMap();
/*      */         }
/*      */       } 
/*  541 */       return changed;
/*      */     }
/*      */     
/*      */     WrappedCollection getAncestor() {
/*  545 */       return this.ancestor;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean addAll(Collection<? extends V> collection) {
/*  551 */       if (collection.isEmpty()) {
/*  552 */         return false;
/*      */       }
/*  554 */       int oldSize = size();
/*  555 */       boolean changed = this.delegate.addAll(collection);
/*  556 */       if (changed) {
/*  557 */         int newSize = this.delegate.size();
/*  558 */         AbstractMultimap.this.totalSize += newSize - oldSize;
/*  559 */         if (oldSize == 0) {
/*  560 */           addToMap();
/*      */         }
/*      */       } 
/*  563 */       return changed;
/*      */     }
/*      */     
/*      */     public boolean contains(Object o) {
/*  567 */       refreshIfEmpty();
/*  568 */       return this.delegate.contains(o);
/*      */     }
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  572 */       refreshIfEmpty();
/*  573 */       return this.delegate.containsAll(c);
/*      */     }
/*      */     
/*      */     public void clear() {
/*  577 */       int oldSize = size();
/*  578 */       if (oldSize == 0) {
/*      */         return;
/*      */       }
/*  581 */       this.delegate.clear();
/*  582 */       AbstractMultimap.this.totalSize -= oldSize;
/*  583 */       removeIfEmpty();
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/*  587 */       refreshIfEmpty();
/*  588 */       boolean changed = this.delegate.remove(o);
/*  589 */       if (changed) {
/*  590 */         AbstractMultimap.this.totalSize--;
/*  591 */         removeIfEmpty();
/*      */       } 
/*  593 */       return changed;
/*      */     }
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  597 */       if (c.isEmpty()) {
/*  598 */         return false;
/*      */       }
/*  600 */       int oldSize = size();
/*  601 */       boolean changed = this.delegate.removeAll(c);
/*  602 */       if (changed) {
/*  603 */         int newSize = this.delegate.size();
/*  604 */         AbstractMultimap.this.totalSize += newSize - oldSize;
/*  605 */         removeIfEmpty();
/*      */       } 
/*  607 */       return changed;
/*      */     }
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*  611 */       Preconditions.checkNotNull(c);
/*  612 */       int oldSize = size();
/*  613 */       boolean changed = this.delegate.retainAll(c);
/*  614 */       if (changed) {
/*  615 */         int newSize = this.delegate.size();
/*  616 */         AbstractMultimap.this.totalSize += newSize - oldSize;
/*  617 */         removeIfEmpty();
/*      */       } 
/*  619 */       return changed;
/*      */     }
/*      */   }
/*      */   
/*      */   private Iterator<V> iteratorOrListIterator(Collection<V> collection) {
/*  624 */     return (collection instanceof List) ? ((List<V>)collection).listIterator() : collection.iterator();
/*      */   }
/*      */   
/*      */   private class WrappedSet
/*      */     extends WrappedCollection
/*      */     implements Set<V>
/*      */   {
/*      */     WrappedSet(K key, Set<V> delegate) {
/*  632 */       super(key, delegate, null);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class WrappedSortedSet
/*      */     extends WrappedCollection
/*      */     implements SortedSet<V>
/*      */   {
/*      */     WrappedSortedSet(K key, @Nullable SortedSet<V> delegate, AbstractMultimap<K, V>.WrappedCollection ancestor) {
/*  643 */       super(key, delegate, ancestor);
/*      */     }
/*      */     
/*      */     SortedSet<V> getSortedSetDelegate() {
/*  647 */       return (SortedSet<V>)getDelegate();
/*      */     }
/*      */     
/*      */     public Comparator<? super V> comparator() {
/*  651 */       return getSortedSetDelegate().comparator();
/*      */     }
/*      */     
/*      */     public V first() {
/*  655 */       refreshIfEmpty();
/*  656 */       return getSortedSetDelegate().first();
/*      */     }
/*      */     
/*      */     public V last() {
/*  660 */       refreshIfEmpty();
/*  661 */       return getSortedSetDelegate().last();
/*      */     }
/*      */     
/*      */     public SortedSet<V> headSet(V toElement) {
/*  665 */       refreshIfEmpty();
/*  666 */       return new WrappedSortedSet(getKey(), getSortedSetDelegate().headSet(toElement), (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public SortedSet<V> subSet(V fromElement, V toElement) {
/*  672 */       refreshIfEmpty();
/*  673 */       return new WrappedSortedSet(getKey(), getSortedSetDelegate().subSet(fromElement, toElement), (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public SortedSet<V> tailSet(V fromElement) {
/*  679 */       refreshIfEmpty();
/*  680 */       return new WrappedSortedSet(getKey(), getSortedSetDelegate().tailSet(fromElement), (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */   }
/*      */   
/*      */   private class WrappedList
/*      */     extends WrappedCollection
/*      */     implements List<V>
/*      */   {
/*      */     WrappedList(K key, @Nullable List<V> delegate, AbstractMultimap<K, V>.WrappedCollection ancestor) {
/*  689 */       super(key, delegate, ancestor);
/*      */     }
/*      */     
/*      */     List<V> getListDelegate() {
/*  693 */       return (List<V>)getDelegate();
/*      */     }
/*      */     
/*      */     public boolean addAll(int index, Collection<? extends V> c) {
/*  697 */       if (c.isEmpty()) {
/*  698 */         return false;
/*      */       }
/*  700 */       int oldSize = size();
/*  701 */       boolean changed = getListDelegate().addAll(index, c);
/*  702 */       if (changed) {
/*  703 */         int newSize = getDelegate().size();
/*  704 */         AbstractMultimap.this.totalSize += newSize - oldSize;
/*  705 */         if (oldSize == 0) {
/*  706 */           addToMap();
/*      */         }
/*      */       } 
/*  709 */       return changed;
/*      */     }
/*      */     
/*      */     public V get(int index) {
/*  713 */       refreshIfEmpty();
/*  714 */       return getListDelegate().get(index);
/*      */     }
/*      */     
/*      */     public V set(int index, V element) {
/*  718 */       refreshIfEmpty();
/*  719 */       return getListDelegate().set(index, element);
/*      */     }
/*      */     
/*      */     public void add(int index, V element) {
/*  723 */       refreshIfEmpty();
/*  724 */       boolean wasEmpty = getDelegate().isEmpty();
/*  725 */       getListDelegate().add(index, element);
/*  726 */       AbstractMultimap.this.totalSize++;
/*  727 */       if (wasEmpty) {
/*  728 */         addToMap();
/*      */       }
/*      */     }
/*      */     
/*      */     public V remove(int index) {
/*  733 */       refreshIfEmpty();
/*  734 */       V value = getListDelegate().remove(index);
/*  735 */       AbstractMultimap.this.totalSize--;
/*  736 */       removeIfEmpty();
/*  737 */       return value;
/*      */     }
/*      */     
/*      */     public int indexOf(Object o) {
/*  741 */       refreshIfEmpty();
/*  742 */       return getListDelegate().indexOf(o);
/*      */     }
/*      */     
/*      */     public int lastIndexOf(Object o) {
/*  746 */       refreshIfEmpty();
/*  747 */       return getListDelegate().lastIndexOf(o);
/*      */     }
/*      */     
/*      */     public ListIterator<V> listIterator() {
/*  751 */       refreshIfEmpty();
/*  752 */       return new WrappedListIterator();
/*      */     }
/*      */     
/*      */     public ListIterator<V> listIterator(int index) {
/*  756 */       refreshIfEmpty();
/*  757 */       return new WrappedListIterator(index);
/*      */     }
/*      */     
/*      */     @GwtIncompatible("List.subList")
/*      */     public List<V> subList(int fromIndex, int toIndex) {
/*  762 */       refreshIfEmpty();
/*  763 */       return AbstractMultimap.this.wrapList(getKey(), Platform.subList(getListDelegate(), fromIndex, toIndex), (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */     
/*      */     private class WrappedListIterator
/*      */       extends AbstractMultimap<K, V>.WrappedCollection.WrappedIterator
/*      */       implements ListIterator<V>
/*      */     {
/*      */       WrappedListIterator() {}
/*      */       
/*      */       public WrappedListIterator(int index) {
/*  774 */         super(AbstractMultimap.WrappedList.this.getListDelegate().listIterator(index));
/*      */       }
/*      */       
/*      */       private ListIterator<V> getDelegateListIterator() {
/*  778 */         return (ListIterator<V>)getDelegateIterator();
/*      */       }
/*      */       
/*      */       public boolean hasPrevious() {
/*  782 */         return getDelegateListIterator().hasPrevious();
/*      */       }
/*      */       
/*      */       public V previous() {
/*  786 */         return getDelegateListIterator().previous();
/*      */       }
/*      */       
/*      */       public int nextIndex() {
/*  790 */         return getDelegateListIterator().nextIndex();
/*      */       }
/*      */       
/*      */       public int previousIndex() {
/*  794 */         return getDelegateListIterator().previousIndex();
/*      */       }
/*      */       
/*      */       public void set(V value) {
/*  798 */         getDelegateListIterator().set(value);
/*      */       }
/*      */       
/*      */       public void add(V value) {
/*  802 */         boolean wasEmpty = AbstractMultimap.WrappedList.this.isEmpty();
/*  803 */         getDelegateListIterator().add(value);
/*  804 */         AbstractMultimap.this.totalSize++;
/*  805 */         if (wasEmpty) {
/*  806 */           AbstractMultimap.WrappedList.this.addToMap();
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
/*      */     RandomAccessWrappedList(K key, @Nullable List<V> delegate, AbstractMultimap<K, V>.WrappedCollection ancestor) {
/*  820 */       super(key, delegate, ancestor);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<K> keySet() {
/*  827 */     Set<K> result = this.keySet;
/*  828 */     return (result == null) ? (this.keySet = createKeySet()) : result;
/*      */   }
/*      */   
/*      */   private Set<K> createKeySet() {
/*  832 */     return (this.map instanceof SortedMap) ? new SortedKeySet((SortedMap<K, Collection<V>>)this.map) : new KeySet(this.map);
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
/*  845 */       this.subMap = subMap;
/*      */     }
/*      */     
/*      */     public int size() {
/*  849 */       return this.subMap.size();
/*      */     }
/*      */     
/*      */     public Iterator<K> iterator() {
/*  853 */       return new Iterator<K>() {
/*  854 */           final Iterator<Map.Entry<K, Collection<V>>> entryIterator = AbstractMultimap.KeySet.this.subMap.entrySet().iterator();
/*      */           
/*      */           Map.Entry<K, Collection<V>> entry;
/*      */           
/*      */           public boolean hasNext() {
/*  859 */             return this.entryIterator.hasNext();
/*      */           }
/*      */           public K next() {
/*  862 */             this.entry = this.entryIterator.next();
/*  863 */             return this.entry.getKey();
/*      */           }
/*      */           public void remove() {
/*  866 */             Preconditions.checkState((this.entry != null));
/*  867 */             Collection<V> collection = this.entry.getValue();
/*  868 */             this.entryIterator.remove();
/*  869 */             AbstractMultimap.this.totalSize -= collection.size();
/*  870 */             collection.clear();
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean contains(Object key) {
/*  878 */       return this.subMap.containsKey(key);
/*      */     }
/*      */     
/*      */     public boolean remove(Object key) {
/*  882 */       int count = 0;
/*  883 */       Collection<V> collection = this.subMap.remove(key);
/*  884 */       if (collection != null) {
/*  885 */         count = collection.size();
/*  886 */         collection.clear();
/*  887 */         AbstractMultimap.this.totalSize -= count;
/*      */       } 
/*  889 */       return (count > 0);
/*      */     }
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  893 */       return this.subMap.keySet().containsAll(c);
/*      */     }
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/*  897 */       return (this == object || this.subMap.keySet().equals(object));
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  901 */       return this.subMap.keySet().hashCode();
/*      */     }
/*      */   }
/*      */   
/*      */   private class SortedKeySet
/*      */     extends KeySet implements SortedSet<K> {
/*      */     SortedKeySet(SortedMap<K, Collection<V>> subMap) {
/*  908 */       super(subMap);
/*      */     }
/*      */     
/*      */     SortedMap<K, Collection<V>> sortedMap() {
/*  912 */       return (SortedMap<K, Collection<V>>)this.subMap;
/*      */     }
/*      */     
/*      */     public Comparator<? super K> comparator() {
/*  916 */       return sortedMap().comparator();
/*      */     }
/*      */     
/*      */     public K first() {
/*  920 */       return sortedMap().firstKey();
/*      */     }
/*      */     
/*      */     public SortedSet<K> headSet(K toElement) {
/*  924 */       return new SortedKeySet(sortedMap().headMap(toElement));
/*      */     }
/*      */     
/*      */     public K last() {
/*  928 */       return sortedMap().lastKey();
/*      */     }
/*      */     
/*      */     public SortedSet<K> subSet(K fromElement, K toElement) {
/*  932 */       return new SortedKeySet(sortedMap().subMap(fromElement, toElement));
/*      */     }
/*      */     
/*      */     public SortedSet<K> tailSet(K fromElement) {
/*  936 */       return new SortedKeySet(sortedMap().tailMap(fromElement));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Multiset<K> keys() {
/*  943 */     Multiset<K> result = this.multiset;
/*  944 */     return (result == null) ? (this.multiset = new MultisetView()) : result;
/*      */   }
/*      */   private class MultisetView extends AbstractMultiset<K> { transient Set<Multiset.Entry<K>> entrySet;
/*      */     private MultisetView() {}
/*      */     
/*      */     public int remove(Object key, int occurrences) {
/*      */       Collection<V> collection;
/*  951 */       if (occurrences == 0) {
/*  952 */         return count(key);
/*      */       }
/*  954 */       Preconditions.checkArgument((occurrences > 0));
/*      */ 
/*      */       
/*      */       try {
/*  958 */         collection = (Collection<V>)AbstractMultimap.this.map.get(key);
/*  959 */       } catch (NullPointerException e) {
/*  960 */         return 0;
/*  961 */       } catch (ClassCastException e) {
/*  962 */         return 0;
/*      */       } 
/*      */       
/*  965 */       if (collection == null) {
/*  966 */         return 0;
/*      */       }
/*  968 */       int count = collection.size();
/*      */       
/*  970 */       if (occurrences >= count) {
/*  971 */         return AbstractMultimap.this.removeValuesForKey(key);
/*      */       }
/*      */       
/*  974 */       Iterator<V> iterator = collection.iterator();
/*  975 */       for (int i = 0; i < occurrences; i++) {
/*  976 */         iterator.next();
/*  977 */         iterator.remove();
/*      */       } 
/*  979 */       AbstractMultimap.this.totalSize -= occurrences;
/*  980 */       return count;
/*      */     }
/*      */     
/*      */     public Set<K> elementSet() {
/*  984 */       return AbstractMultimap.this.keySet();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Multiset.Entry<K>> entrySet() {
/*  990 */       Set<Multiset.Entry<K>> result = this.entrySet;
/*  991 */       return (result == null) ? (this.entrySet = new EntrySet()) : result;
/*      */     }
/*      */     private class EntrySet extends AbstractSet<Multiset.Entry<K>> { private EntrySet() {}
/*      */       
/*      */       public Iterator<Multiset.Entry<K>> iterator() {
/*  996 */         return new AbstractMultimap.MultisetEntryIterator();
/*      */       }
/*      */       public int size() {
/*  999 */         return AbstractMultimap.this.map.size();
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean contains(Object o) {
/* 1005 */         if (!(o instanceof Multiset.Entry)) {
/* 1006 */           return false;
/*      */         }
/* 1008 */         Multiset.Entry<?> entry = (Multiset.Entry)o;
/* 1009 */         Collection<V> collection = (Collection<V>)AbstractMultimap.this.map.get(entry.getElement());
/* 1010 */         return (collection != null && collection.size() == entry.getCount());
/*      */       }
/*      */       
/*      */       public void clear() {
/* 1014 */         AbstractMultimap.this.clear();
/*      */       }
/*      */       public boolean remove(Object o) {
/* 1017 */         return (contains(o) && AbstractMultimap.this.removeValuesForKey(((Multiset.Entry)o).getElement()) > 0);
/*      */       } }
/*      */ 
/*      */ 
/*      */     
/*      */     public Iterator<K> iterator() {
/* 1023 */       return new AbstractMultimap.MultisetKeyIterator();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int count(Object key) {
/*      */       try {
/* 1030 */         Collection<V> collection = (Collection<V>)AbstractMultimap.this.map.get(key);
/* 1031 */         return (collection == null) ? 0 : collection.size();
/* 1032 */       } catch (NullPointerException e) {
/* 1033 */         return 0;
/* 1034 */       } catch (ClassCastException e) {
/* 1035 */         return 0;
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/* 1040 */       return AbstractMultimap.this.totalSize;
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1044 */       AbstractMultimap.this.clear();
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
/* 1055 */       collection = this.map.remove(key);
/* 1056 */     } catch (NullPointerException e) {
/* 1057 */       return 0;
/* 1058 */     } catch (ClassCastException e) {
/* 1059 */       return 0;
/*      */     } 
/*      */     
/* 1062 */     int count = 0;
/* 1063 */     if (collection != null) {
/* 1064 */       count = collection.size();
/* 1065 */       collection.clear();
/* 1066 */       this.totalSize -= count;
/*      */     } 
/* 1068 */     return count;
/*      */   }
/*      */   
/*      */   private class MultisetEntryIterator
/*      */     implements Iterator<Multiset.Entry<K>> {
/* 1073 */     final Iterator<Map.Entry<K, Collection<V>>> asMapIterator = AbstractMultimap.this.asMap().entrySet().iterator();
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1077 */       return this.asMapIterator.hasNext();
/*      */     }
/*      */     public Multiset.Entry<K> next() {
/* 1080 */       return new AbstractMultimap.MultisetEntry(this.asMapIterator.next());
/*      */     }
/*      */     public void remove() {
/* 1083 */       this.asMapIterator.remove();
/*      */     }
/*      */     
/*      */     private MultisetEntryIterator() {} }
/*      */   
/*      */   private class MultisetEntry extends Multisets.AbstractEntry<K> { final Map.Entry<K, Collection<V>> entry;
/*      */     
/*      */     public MultisetEntry(Map.Entry<K, Collection<V>> entry) {
/* 1091 */       this.entry = entry;
/*      */     }
/*      */     public K getElement() {
/* 1094 */       return this.entry.getKey();
/*      */     }
/*      */     public int getCount() {
/* 1097 */       return ((Collection)this.entry.getValue()).size();
/*      */     } }
/*      */ 
/*      */   
/*      */   private class MultisetKeyIterator
/*      */     implements Iterator<K> {
/* 1103 */     final Iterator<Map.Entry<K, V>> entryIterator = AbstractMultimap.this.entries().iterator();
/*      */     
/*      */     public boolean hasNext() {
/* 1106 */       return this.entryIterator.hasNext();
/*      */     }
/*      */     public K next() {
/* 1109 */       return (K)((Map.Entry)this.entryIterator.next()).getKey();
/*      */     }
/*      */     public void remove() {
/* 1112 */       this.entryIterator.remove();
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
/* 1125 */     Collection<V> result = this.valuesCollection;
/* 1126 */     return (result == null) ? (this.valuesCollection = new Values()) : result;
/*      */   }
/*      */   private class Values extends AbstractCollection<V> { private Values() {}
/*      */     
/*      */     public Iterator<V> iterator() {
/* 1131 */       return new AbstractMultimap.ValueIterator();
/*      */     }
/*      */     public int size() {
/* 1134 */       return AbstractMultimap.this.totalSize;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1140 */       AbstractMultimap.this.clear();
/*      */     }
/*      */     
/*      */     public boolean contains(Object value) {
/* 1144 */       return AbstractMultimap.this.containsValue(value);
/*      */     } }
/*      */ 
/*      */   
/*      */   private class ValueIterator
/*      */     implements Iterator<V> {
/* 1150 */     final Iterator<Map.Entry<K, V>> entryIterator = AbstractMultimap.this.createEntryIterator();
/*      */     
/*      */     public boolean hasNext() {
/* 1153 */       return this.entryIterator.hasNext();
/*      */     }
/*      */     public V next() {
/* 1156 */       return (V)((Map.Entry)this.entryIterator.next()).getValue();
/*      */     }
/*      */     public void remove() {
/* 1159 */       this.entryIterator.remove();
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
/* 1180 */     Collection<Map.Entry<K, V>> result = this.entries;
/* 1181 */     return (this.entries == null) ? (this.entries = createEntries()) : result;
/*      */   }
/*      */ 
/*      */   
/*      */   private Collection<Map.Entry<K, V>> createEntries() {
/* 1186 */     return (this instanceof SetMultimap) ? new EntrySet() : new Entries();
/*      */   }
/*      */   
/*      */   private class Entries extends AbstractCollection<Map.Entry<K, V>> { private Entries() {}
/*      */     
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/* 1192 */       return AbstractMultimap.this.createEntryIterator();
/*      */     }
/*      */     public int size() {
/* 1195 */       return AbstractMultimap.this.totalSize;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1201 */       if (!(o instanceof Map.Entry)) {
/* 1202 */         return false;
/*      */       }
/* 1204 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1205 */       return AbstractMultimap.this.containsEntry(entry.getKey(), entry.getValue());
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1209 */       AbstractMultimap.this.clear();
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/* 1213 */       if (!(o instanceof Map.Entry)) {
/* 1214 */         return false;
/*      */       }
/* 1216 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1217 */       return AbstractMultimap.this.remove(entry.getKey(), entry.getValue());
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
/* 1230 */     return new EntryIterator();
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
/* 1241 */     final Iterator<Map.Entry<K, Collection<V>>> keyIterator = AbstractMultimap.this.map.entrySet().iterator(); K key; EntryIterator() {
/* 1242 */       if (this.keyIterator.hasNext()) {
/* 1243 */         findValueIteratorAndKey();
/*      */       } else {
/* 1245 */         this.valueIterator = Iterators.emptyModifiableIterator();
/*      */       } 
/*      */     }
/*      */     Collection<V> collection; Iterator<V> valueIterator;
/*      */     void findValueIteratorAndKey() {
/* 1250 */       Map.Entry<K, Collection<V>> entry = this.keyIterator.next();
/* 1251 */       this.key = entry.getKey();
/* 1252 */       this.collection = entry.getValue();
/* 1253 */       this.valueIterator = this.collection.iterator();
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/* 1257 */       return (this.keyIterator.hasNext() || this.valueIterator.hasNext());
/*      */     }
/*      */     
/*      */     public Map.Entry<K, V> next() {
/* 1261 */       if (!this.valueIterator.hasNext()) {
/* 1262 */         findValueIteratorAndKey();
/*      */       }
/* 1264 */       return Maps.immutableEntry(this.key, this.valueIterator.next());
/*      */     }
/*      */     
/*      */     public void remove() {
/* 1268 */       this.valueIterator.remove();
/* 1269 */       if (this.collection.isEmpty()) {
/* 1270 */         this.keyIterator.remove();
/*      */       }
/* 1272 */       AbstractMultimap.this.totalSize--;
/*      */     } }
/*      */   
/*      */   private class EntrySet extends Entries implements Set<Map.Entry<K, V>> {
/*      */     private EntrySet() {}
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/* 1279 */       return Collections2.setEquals(this, object);
/*      */     }
/*      */     public int hashCode() {
/* 1282 */       return Sets.hashCodeImpl(this);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<K, Collection<V>> asMap() {
/* 1289 */     Map<K, Collection<V>> result = this.asMap;
/* 1290 */     return (result == null) ? (this.asMap = createAsMap()) : result;
/*      */   }
/*      */   
/*      */   private Map<K, Collection<V>> createAsMap() {
/* 1294 */     return (this.map instanceof SortedMap) ? new SortedAsMap((SortedMap<K, Collection<V>>)this.map) : new AsMap(this.map);
/*      */   }
/*      */ 
/*      */   
/*      */   private class AsMap
/*      */     extends AbstractMap<K, Collection<V>>
/*      */   {
/*      */     final transient Map<K, Collection<V>> submap;
/*      */     
/*      */     transient Set<Map.Entry<K, Collection<V>>> entrySet;
/*      */     
/*      */     AsMap(Map<K, Collection<V>> submap) {
/* 1306 */       this.submap = submap;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, Collection<V>>> entrySet() {
/* 1312 */       Set<Map.Entry<K, Collection<V>>> result = this.entrySet;
/* 1313 */       return (this.entrySet == null) ? (this.entrySet = new AsMapEntries()) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 1319 */       return this.submap.containsKey(key);
/*      */     }
/*      */     
/*      */     public Collection<V> get(Object key) {
/* 1323 */       Collection<V> collection = this.submap.get(key);
/* 1324 */       if (collection == null) {
/* 1325 */         return null;
/*      */       }
/*      */       
/* 1328 */       K k = (K)key;
/* 1329 */       return AbstractMultimap.this.wrapCollection(k, collection);
/*      */     }
/*      */     
/*      */     public Set<K> keySet() {
/* 1333 */       return AbstractMultimap.this.keySet();
/*      */     }
/*      */     
/*      */     public Collection<V> remove(Object key) {
/* 1337 */       Collection<V> collection = this.submap.remove(key);
/* 1338 */       if (collection == null) {
/* 1339 */         return null;
/*      */       }
/*      */       
/* 1342 */       Collection<V> output = AbstractMultimap.this.createCollection();
/* 1343 */       output.addAll(collection);
/* 1344 */       AbstractMultimap.this.totalSize -= collection.size();
/* 1345 */       collection.clear();
/* 1346 */       return output;
/*      */     }
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/* 1350 */       return (this == object || this.submap.equals(object));
/*      */     }
/*      */     
/*      */     public int hashCode() {
/* 1354 */       return this.submap.hashCode();
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1358 */       return this.submap.toString();
/*      */     }
/*      */     
/*      */     class AsMapEntries extends AbstractSet<Map.Entry<K, Collection<V>>> {
/*      */       public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/* 1363 */         return new AbstractMultimap.AsMap.AsMapIterator();
/*      */       }
/*      */       
/*      */       public int size() {
/* 1367 */         return AbstractMultimap.AsMap.this.submap.size();
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean contains(Object o) {
/* 1373 */         return AbstractMultimap.AsMap.this.submap.entrySet().contains(o);
/*      */       }
/*      */       
/*      */       public boolean remove(Object o) {
/* 1377 */         if (!contains(o)) {
/* 1378 */           return false;
/*      */         }
/* 1380 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1381 */         AbstractMultimap.this.removeValuesForKey(entry.getKey());
/* 1382 */         return true;
/*      */       }
/*      */     }
/*      */     
/*      */     class AsMapIterator
/*      */       implements Iterator<Map.Entry<K, Collection<V>>> {
/* 1388 */       final Iterator<Map.Entry<K, Collection<V>>> delegateIterator = AbstractMultimap.AsMap.this.submap.entrySet().iterator();
/*      */       
/*      */       Collection<V> collection;
/*      */       
/*      */       public boolean hasNext() {
/* 1393 */         return this.delegateIterator.hasNext();
/*      */       }
/*      */       
/*      */       public Map.Entry<K, Collection<V>> next() {
/* 1397 */         Map.Entry<K, Collection<V>> entry = this.delegateIterator.next();
/* 1398 */         K key = entry.getKey();
/* 1399 */         this.collection = entry.getValue();
/* 1400 */         return Maps.immutableEntry(key, AbstractMultimap.this.wrapCollection(key, this.collection));
/*      */       }
/*      */       
/*      */       public void remove() {
/* 1404 */         this.delegateIterator.remove();
/* 1405 */         AbstractMultimap.this.totalSize -= this.collection.size();
/* 1406 */         this.collection.clear();
/*      */       } }
/*      */   }
/*      */   
/*      */   private class SortedAsMap extends AsMap implements SortedMap<K, Collection<V>> {
/*      */     SortedSet<K> sortedKeySet;
/*      */     
/*      */     SortedAsMap(SortedMap<K, Collection<V>> submap) {
/* 1414 */       super(submap);
/*      */     }
/*      */     
/*      */     SortedMap<K, Collection<V>> sortedMap() {
/* 1418 */       return (SortedMap<K, Collection<V>>)this.submap;
/*      */     }
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 1422 */       return sortedMap().comparator();
/*      */     }
/*      */     
/*      */     public K firstKey() {
/* 1426 */       return sortedMap().firstKey();
/*      */     }
/*      */     
/*      */     public K lastKey() {
/* 1430 */       return sortedMap().lastKey();
/*      */     }
/*      */     
/*      */     public SortedMap<K, Collection<V>> headMap(K toKey) {
/* 1434 */       return new SortedAsMap(sortedMap().headMap(toKey));
/*      */     }
/*      */     
/*      */     public SortedMap<K, Collection<V>> subMap(K fromKey, K toKey) {
/* 1438 */       return new SortedAsMap(sortedMap().subMap(fromKey, toKey));
/*      */     }
/*      */     
/*      */     public SortedMap<K, Collection<V>> tailMap(K fromKey) {
/* 1442 */       return new SortedAsMap(sortedMap().tailMap(fromKey));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SortedSet<K> keySet() {
/* 1450 */       SortedSet<K> result = this.sortedKeySet;
/* 1451 */       return (result == null) ? (this.sortedKeySet = new AbstractMultimap.SortedKeySet(sortedMap())) : result;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(@Nullable Object object) {
/* 1459 */     if (object == this) {
/* 1460 */       return true;
/*      */     }
/* 1462 */     if (object instanceof Multimap) {
/* 1463 */       Multimap<?, ?> that = (Multimap<?, ?>)object;
/* 1464 */       return this.map.equals(that.asMap());
/*      */     } 
/* 1466 */     return false;
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
/* 1478 */     return this.map.hashCode();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1488 */     return this.map.toString();
/*      */   }
/*      */   
/*      */   abstract Collection<V> createCollection();
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\AbstractMultimap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */