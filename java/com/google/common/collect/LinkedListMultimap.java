/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.AbstractSequentialList;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class LinkedListMultimap<K, V>
/*     */   implements ListMultimap<K, V>, Serializable
/*     */ {
/*     */   private transient Node<K, V> head;
/*     */   private transient Node<K, V> tail;
/*     */   private transient Multiset<K> keyCount;
/*     */   private transient Map<K, Node<K, V>> keyToKeyHead;
/*     */   private transient Map<K, Node<K, V>> keyToKeyTail;
/*     */   private transient Set<K> keySet;
/*     */   private transient Multiset<K> keys;
/*     */   private transient Collection<V> valuesCollection;
/*     */   private transient Collection<Map.Entry<K, V>> entries;
/*     */   private transient Map<K, Collection<V>> map;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   private static final class Node<K, V>
/*     */   {
/*     */     final K key;
/*     */     V value;
/*     */     Node<K, V> next;
/*     */     Node<K, V> previous;
/*     */     Node<K, V> nextSibling;
/*     */     Node<K, V> previousSibling;
/*     */     
/*     */     Node(@Nullable K key, @Nullable V value) {
/* 113 */       this.key = key;
/* 114 */       this.value = value;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 118 */       return (new StringBuilder()).append(this.key).append("=").append(this.value).toString();
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
/*     */   public static <K, V> LinkedListMultimap<K, V> create() {
/* 133 */     return new LinkedListMultimap<K, V>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> LinkedListMultimap<K, V> create(int expectedKeys) {
/* 144 */     return new LinkedListMultimap<K, V>(expectedKeys);
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
/*     */   public static <K, V> LinkedListMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
/* 156 */     return new LinkedListMultimap<K, V>(multimap);
/*     */   }
/*     */   
/*     */   private LinkedListMultimap() {
/* 160 */     this.keyCount = LinkedHashMultiset.create();
/* 161 */     this.keyToKeyHead = Maps.newHashMap();
/* 162 */     this.keyToKeyTail = Maps.newHashMap();
/*     */   }
/*     */   
/*     */   private LinkedListMultimap(int expectedKeys) {
/* 166 */     this.keyCount = LinkedHashMultiset.create(expectedKeys);
/* 167 */     this.keyToKeyHead = Maps.newHashMapWithExpectedSize(expectedKeys);
/* 168 */     this.keyToKeyTail = Maps.newHashMapWithExpectedSize(expectedKeys);
/*     */   }
/*     */   
/*     */   private LinkedListMultimap(Multimap<? extends K, ? extends V> multimap) {
/* 172 */     this(multimap.keySet().size());
/* 173 */     putAll(multimap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Node<K, V> addNode(@Nullable K key, @Nullable V value, @Nullable Node<K, V> nextSibling) {
/* 184 */     Node<K, V> node = new Node<K, V>(key, value);
/* 185 */     if (this.head == null) {
/* 186 */       this.head = this.tail = node;
/* 187 */       this.keyToKeyHead.put(key, node);
/* 188 */       this.keyToKeyTail.put(key, node);
/* 189 */     } else if (nextSibling == null) {
/* 190 */       this.tail.next = node;
/* 191 */       node.previous = this.tail;
/* 192 */       Node<K, V> keyTail = this.keyToKeyTail.get(key);
/* 193 */       if (keyTail == null) {
/* 194 */         this.keyToKeyHead.put(key, node);
/*     */       } else {
/* 196 */         keyTail.nextSibling = node;
/* 197 */         node.previousSibling = keyTail;
/*     */       } 
/* 199 */       this.keyToKeyTail.put(key, node);
/* 200 */       this.tail = node;
/*     */     } else {
/* 202 */       node.previous = nextSibling.previous;
/* 203 */       node.previousSibling = nextSibling.previousSibling;
/* 204 */       node.next = nextSibling;
/* 205 */       node.nextSibling = nextSibling;
/* 206 */       if (nextSibling.previousSibling == null) {
/* 207 */         this.keyToKeyHead.put(key, node);
/*     */       } else {
/* 209 */         nextSibling.previousSibling.nextSibling = node;
/*     */       } 
/* 211 */       if (nextSibling.previous == null) {
/* 212 */         this.head = node;
/*     */       } else {
/* 214 */         nextSibling.previous.next = node;
/*     */       } 
/* 216 */       nextSibling.previous = node;
/* 217 */       nextSibling.previousSibling = node;
/*     */     } 
/* 219 */     this.keyCount.add(key);
/* 220 */     return node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeNode(Node<K, V> node) {
/* 229 */     if (node.previous != null) {
/* 230 */       node.previous.next = node.next;
/*     */     } else {
/* 232 */       this.head = node.next;
/*     */     } 
/* 234 */     if (node.next != null) {
/* 235 */       node.next.previous = node.previous;
/*     */     } else {
/* 237 */       this.tail = node.previous;
/*     */     } 
/* 239 */     if (node.previousSibling != null) {
/* 240 */       node.previousSibling.nextSibling = node.nextSibling;
/* 241 */     } else if (node.nextSibling != null) {
/* 242 */       this.keyToKeyHead.put(node.key, node.nextSibling);
/*     */     } else {
/* 244 */       this.keyToKeyHead.remove(node.key);
/*     */     } 
/* 246 */     if (node.nextSibling != null) {
/* 247 */       node.nextSibling.previousSibling = node.previousSibling;
/* 248 */     } else if (node.previousSibling != null) {
/* 249 */       this.keyToKeyTail.put(node.key, node.previousSibling);
/*     */     } else {
/* 251 */       this.keyToKeyTail.remove(node.key);
/*     */     } 
/* 253 */     this.keyCount.remove(node.key);
/*     */   }
/*     */ 
/*     */   
/*     */   private void removeAllNodes(@Nullable Object key) {
/* 258 */     for (Iterator<V> i = new ValueForKeyIterator(key); i.hasNext(); ) {
/* 259 */       i.next();
/* 260 */       i.remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void checkElement(@Nullable Object node) {
/* 266 */     if (node == null)
/* 267 */       throw new NoSuchElementException(); 
/*     */   }
/*     */   
/*     */   private class NodeIterator
/*     */     implements Iterator<Node<K, V>>
/*     */   {
/* 273 */     LinkedListMultimap.Node<K, V> next = LinkedListMultimap.this.head;
/*     */     LinkedListMultimap.Node<K, V> current;
/*     */     
/*     */     public boolean hasNext() {
/* 277 */       return (this.next != null);
/*     */     }
/*     */     public LinkedListMultimap.Node<K, V> next() {
/* 280 */       LinkedListMultimap.checkElement(this.next);
/* 281 */       this.current = this.next;
/* 282 */       this.next = this.next.next;
/* 283 */       return this.current;
/*     */     }
/*     */     public void remove() {
/* 286 */       Preconditions.checkState((this.current != null));
/* 287 */       LinkedListMultimap.this.removeNode(this.current);
/* 288 */       this.current = null;
/*     */     }
/*     */     
/*     */     private NodeIterator() {} }
/*     */   
/*     */   private class DistinctKeyIterator implements Iterator<K> {
/* 294 */     final Set<K> seenKeys = new HashSet<K>(Maps.capacity(LinkedListMultimap.this.keySet().size()));
/* 295 */     LinkedListMultimap.Node<K, V> next = LinkedListMultimap.this.head;
/*     */     LinkedListMultimap.Node<K, V> current;
/*     */     
/*     */     public boolean hasNext() {
/* 299 */       return (this.next != null);
/*     */     }
/*     */     public K next() {
/* 302 */       LinkedListMultimap.checkElement(this.next);
/* 303 */       this.current = this.next;
/* 304 */       this.seenKeys.add(this.current.key);
/*     */       do {
/* 306 */         this.next = this.next.next;
/* 307 */       } while (this.next != null && !this.seenKeys.add(this.next.key));
/* 308 */       return this.current.key;
/*     */     }
/*     */     public void remove() {
/* 311 */       Preconditions.checkState((this.current != null));
/* 312 */       LinkedListMultimap.this.removeAllNodes(this.current.key);
/* 313 */       this.current = null;
/*     */     }
/*     */     
/*     */     private DistinctKeyIterator() {}
/*     */   }
/*     */   
/*     */   private class ValueForKeyIterator implements ListIterator<V> {
/*     */     final Object key;
/*     */     int nextIndex;
/*     */     LinkedListMultimap.Node<K, V> next;
/*     */     LinkedListMultimap.Node<K, V> current;
/*     */     LinkedListMultimap.Node<K, V> previous;
/*     */     
/*     */     ValueForKeyIterator(Object key) {
/* 327 */       this.key = key;
/* 328 */       this.next = (LinkedListMultimap.Node<K, V>)LinkedListMultimap.this.keyToKeyHead.get(key);
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
/*     */     public ValueForKeyIterator(Object key, int index) {
/* 341 */       int size = LinkedListMultimap.this.keyCount.count(key);
/* 342 */       Preconditions.checkPositionIndex(index, size);
/* 343 */       if (index >= size / 2) {
/* 344 */         this.previous = (LinkedListMultimap.Node<K, V>)LinkedListMultimap.this.keyToKeyTail.get(key);
/* 345 */         this.nextIndex = size;
/* 346 */         while (index++ < size) {
/* 347 */           previous();
/*     */         }
/*     */       } else {
/* 350 */         this.next = (LinkedListMultimap.Node<K, V>)LinkedListMultimap.this.keyToKeyHead.get(key);
/* 351 */         while (index-- > 0) {
/* 352 */           next();
/*     */         }
/*     */       } 
/* 355 */       this.key = key;
/* 356 */       this.current = null;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 360 */       return (this.next != null);
/*     */     }
/*     */     
/*     */     public V next() {
/* 364 */       LinkedListMultimap.checkElement(this.next);
/* 365 */       this.previous = this.current = this.next;
/* 366 */       this.next = this.next.nextSibling;
/* 367 */       this.nextIndex++;
/* 368 */       return this.current.value;
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 372 */       return (this.previous != null);
/*     */     }
/*     */     
/*     */     public V previous() {
/* 376 */       LinkedListMultimap.checkElement(this.previous);
/* 377 */       this.next = this.current = this.previous;
/* 378 */       this.previous = this.previous.previousSibling;
/* 379 */       this.nextIndex--;
/* 380 */       return this.current.value;
/*     */     }
/*     */     
/*     */     public int nextIndex() {
/* 384 */       return this.nextIndex;
/*     */     }
/*     */     
/*     */     public int previousIndex() {
/* 388 */       return this.nextIndex - 1;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 392 */       Preconditions.checkState((this.current != null));
/* 393 */       if (this.current != this.next) {
/* 394 */         this.previous = this.current.previousSibling;
/* 395 */         this.nextIndex--;
/*     */       } else {
/* 397 */         this.next = this.current.nextSibling;
/*     */       } 
/* 399 */       LinkedListMultimap.this.removeNode(this.current);
/* 400 */       this.current = null;
/*     */     }
/*     */     
/*     */     public void set(V value) {
/* 404 */       Preconditions.checkState((this.current != null));
/* 405 */       this.current.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(V value) {
/* 410 */       this.previous = LinkedListMultimap.this.addNode((K)this.key, value, this.next);
/* 411 */       this.nextIndex++;
/* 412 */       this.current = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 419 */     return this.keyCount.size();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 423 */     return (this.head == null);
/*     */   }
/*     */   
/*     */   public boolean containsKey(@Nullable Object key) {
/* 427 */     return this.keyToKeyHead.containsKey(key);
/*     */   }
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/* 431 */     for (Iterator<Node<K, V>> i = new NodeIterator(); i.hasNext();) {
/* 432 */       if (Objects.equal(((Node)i.next()).value, value)) {
/* 433 */         return true;
/*     */       }
/*     */     } 
/* 436 */     return false;
/*     */   }
/*     */   
/*     */   public boolean containsEntry(@Nullable Object key, @Nullable Object value) {
/* 440 */     for (Iterator<V> i = new ValueForKeyIterator(key); i.hasNext();) {
/* 441 */       if (Objects.equal(i.next(), value)) {
/* 442 */         return true;
/*     */       }
/*     */     } 
/* 445 */     return false;
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
/*     */   public boolean put(@Nullable K key, @Nullable V value) {
/* 458 */     addNode(key, value, null);
/* 459 */     return true;
/*     */   }
/*     */   
/*     */   public boolean remove(@Nullable Object key, @Nullable Object value) {
/* 463 */     Iterator<V> values = new ValueForKeyIterator(key);
/* 464 */     while (values.hasNext()) {
/* 465 */       if (Objects.equal(values.next(), value)) {
/* 466 */         values.remove();
/* 467 */         return true;
/*     */       } 
/*     */     } 
/* 470 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean putAll(@Nullable K key, Iterable<? extends V> values) {
/* 476 */     boolean changed = false;
/* 477 */     for (V value : values) {
/* 478 */       changed |= put(key, value);
/*     */     }
/* 480 */     return changed;
/*     */   }
/*     */   
/*     */   public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/* 484 */     boolean changed = false;
/* 485 */     for (Map.Entry<? extends K, ? extends V> entry : multimap.entries()) {
/* 486 */       changed |= put(entry.getKey(), entry.getValue());
/*     */     }
/* 488 */     return changed;
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
/*     */   public List<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
/* 502 */     List<V> oldValues = getCopy(key);
/* 503 */     ListIterator<V> keyValues = new ValueForKeyIterator(key);
/* 504 */     Iterator<? extends V> newValues = values.iterator();
/*     */ 
/*     */     
/* 507 */     while (keyValues.hasNext() && newValues.hasNext()) {
/* 508 */       keyValues.next();
/* 509 */       keyValues.set(newValues.next());
/*     */     } 
/*     */ 
/*     */     
/* 513 */     while (keyValues.hasNext()) {
/* 514 */       keyValues.next();
/* 515 */       keyValues.remove();
/*     */     } 
/*     */ 
/*     */     
/* 519 */     while (newValues.hasNext()) {
/* 520 */       keyValues.add(newValues.next());
/*     */     }
/*     */     
/* 523 */     return oldValues;
/*     */   }
/*     */   
/*     */   private List<V> getCopy(@Nullable Object key) {
/* 527 */     return Collections.unmodifiableList(Lists.newArrayList(new ValueForKeyIterator(key)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<V> removeAll(@Nullable Object key) {
/* 537 */     List<V> oldValues = getCopy(key);
/* 538 */     removeAllNodes(key);
/* 539 */     return oldValues;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 543 */     this.head = null;
/* 544 */     this.tail = null;
/* 545 */     this.keyCount.clear();
/* 546 */     this.keyToKeyHead.clear();
/* 547 */     this.keyToKeyTail.clear();
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
/*     */   public List<V> get(@Nullable final K key) {
/* 562 */     return new AbstractSequentialList<V>() {
/*     */         public int size() {
/* 564 */           return LinkedListMultimap.this.keyCount.count(key);
/*     */         }
/*     */         public ListIterator<V> listIterator(int index) {
/* 567 */           return new LinkedListMultimap.ValueForKeyIterator(key, index);
/*     */         }
/*     */         public boolean removeAll(Collection<?> c) {
/* 570 */           return Iterators.removeAll(iterator(), c);
/*     */         }
/*     */         public boolean retainAll(Collection<?> c) {
/* 573 */           return Iterators.retainAll(iterator(), c);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 581 */     Set<K> result = this.keySet;
/* 582 */     if (result == null) {
/* 583 */       this.keySet = result = new AbstractSet<K>() {
/*     */           public int size() {
/* 585 */             return LinkedListMultimap.this.keyCount.elementSet().size();
/*     */           }
/*     */           public Iterator<K> iterator() {
/* 588 */             return new LinkedListMultimap.DistinctKeyIterator();
/*     */           }
/*     */           public boolean contains(Object key) {
/* 591 */             return LinkedListMultimap.this.keyCount.contains(key);
/*     */           }
/*     */         };
/*     */     }
/* 595 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Multiset<K> keys() {
/* 601 */     Multiset<K> result = this.keys;
/* 602 */     if (result == null) {
/* 603 */       this.keys = result = new MultisetView();
/*     */     }
/* 605 */     return result;
/*     */   }
/*     */   
/*     */   private class MultisetView extends AbstractCollection<K> implements Multiset<K> {
/*     */     private MultisetView() {}
/*     */     
/*     */     public int size() {
/* 612 */       return LinkedListMultimap.this.keyCount.size();
/*     */     }
/*     */     
/*     */     public Iterator<K> iterator() {
/* 616 */       final Iterator<LinkedListMultimap.Node<K, V>> nodes = new LinkedListMultimap.NodeIterator();
/* 617 */       return new Iterator<K>() {
/*     */           public boolean hasNext() {
/* 619 */             return nodes.hasNext();
/*     */           }
/*     */           public K next() {
/* 622 */             return ((LinkedListMultimap.Node)nodes.next()).key;
/*     */           }
/*     */           public void remove() {
/* 625 */             nodes.remove();
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public int count(@Nullable Object key) {
/* 631 */       return LinkedListMultimap.this.keyCount.count(key);
/*     */     }
/*     */     
/*     */     public int add(@Nullable K key, int occurrences) {
/* 635 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int remove(@Nullable Object key, int occurrences) {
/* 639 */       Preconditions.checkArgument((occurrences >= 0));
/* 640 */       int oldCount = count(key);
/* 641 */       Iterator<V> values = new LinkedListMultimap.ValueForKeyIterator(key);
/* 642 */       while (occurrences-- > 0 && values.hasNext()) {
/* 643 */         values.next();
/* 644 */         values.remove();
/*     */       } 
/* 646 */       return oldCount;
/*     */     }
/*     */     
/*     */     public int setCount(K element, int count) {
/* 650 */       return Multisets.setCountImpl(this, element, count);
/*     */     }
/*     */     
/*     */     public boolean setCount(K element, int oldCount, int newCount) {
/* 654 */       return Multisets.setCountImpl(this, element, oldCount, newCount);
/*     */     }
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 658 */       return Iterators.removeAll(iterator(), c);
/*     */     }
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 662 */       return Iterators.retainAll(iterator(), c);
/*     */     }
/*     */     
/*     */     public Set<K> elementSet() {
/* 666 */       return LinkedListMultimap.this.keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Multiset.Entry<K>> entrySet() {
/* 671 */       return new AbstractSet<Multiset.Entry<K>>() {
/*     */           public int size() {
/* 673 */             return LinkedListMultimap.this.keyCount.elementSet().size();
/*     */           }
/*     */           
/*     */           public Iterator<Multiset.Entry<K>> iterator() {
/* 677 */             final Iterator<K> keyIterator = new LinkedListMultimap.DistinctKeyIterator();
/* 678 */             return new Iterator<Multiset.Entry<K>>() {
/*     */                 public boolean hasNext() {
/* 680 */                   return keyIterator.hasNext();
/*     */                 }
/*     */                 public Multiset.Entry<K> next() {
/* 683 */                   final K key = keyIterator.next();
/* 684 */                   return new Multisets.AbstractEntry<K>() {
/*     */                       public K getElement() {
/* 686 */                         return (K)key;
/*     */                       }
/*     */                       public int getCount() {
/* 689 */                         return LinkedListMultimap.this.keyCount.count(key);
/*     */                       }
/*     */                     };
/*     */                 }
/*     */                 public void remove() {
/* 694 */                   keyIterator.remove();
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 702 */       return LinkedListMultimap.this.keyCount.equals(object);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 706 */       return LinkedListMultimap.this.keyCount.hashCode();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 710 */       return LinkedListMultimap.this.keyCount.toString();
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
/*     */   public Collection<V> values() {
/* 723 */     Collection<V> result = this.valuesCollection;
/* 724 */     if (result == null) {
/* 725 */       this.valuesCollection = result = new AbstractCollection<V>() {
/*     */           public int size() {
/* 727 */             return LinkedListMultimap.this.keyCount.size();
/*     */           }
/*     */           public Iterator<V> iterator() {
/* 730 */             final Iterator<LinkedListMultimap.Node<K, V>> nodes = new LinkedListMultimap.NodeIterator();
/* 731 */             return new Iterator<V>() {
/*     */                 public boolean hasNext() {
/* 733 */                   return nodes.hasNext();
/*     */                 }
/*     */                 public V next() {
/* 736 */                   return ((LinkedListMultimap.Node)nodes.next()).value;
/*     */                 }
/*     */                 public void remove() {
/* 739 */                   nodes.remove();
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/* 745 */     return result;
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
/*     */   public Collection<Map.Entry<K, V>> entries() {
/* 766 */     Collection<Map.Entry<K, V>> result = this.entries;
/* 767 */     if (result == null) {
/* 768 */       this.entries = result = new AbstractCollection<Map.Entry<K, V>>() {
/*     */           public int size() {
/* 770 */             return LinkedListMultimap.this.keyCount.size();
/*     */           }
/*     */           
/*     */           public Iterator<Map.Entry<K, V>> iterator() {
/* 774 */             final Iterator<LinkedListMultimap.Node<K, V>> nodes = new LinkedListMultimap.NodeIterator();
/* 775 */             return new Iterator<Map.Entry<K, V>>() {
/*     */                 public boolean hasNext() {
/* 777 */                   return nodes.hasNext();
/*     */                 }
/*     */                 
/*     */                 public Map.Entry<K, V> next() {
/* 781 */                   final LinkedListMultimap.Node<K, V> node = nodes.next();
/* 782 */                   return new AbstractMapEntry<K, V>() {
/*     */                       public K getKey() {
/* 784 */                         return node.key;
/*     */                       }
/*     */                       public V getValue() {
/* 787 */                         return node.value;
/*     */                       }
/*     */                       public V setValue(V value) {
/* 790 */                         V oldValue = node.value;
/* 791 */                         node.value = value;
/* 792 */                         return oldValue;
/*     */                       }
/*     */                     };
/*     */                 }
/*     */                 
/*     */                 public void remove() {
/* 798 */                   nodes.remove();
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/* 804 */     return result;
/*     */   }
/*     */   
/*     */   private class AsMapEntries
/*     */     extends AbstractSet<Map.Entry<K, Collection<V>>> {
/*     */     private AsMapEntries() {}
/*     */     
/*     */     public int size() {
/* 812 */       return LinkedListMultimap.this.keyCount.elementSet().size();
/*     */     }
/*     */     
/*     */     public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/* 816 */       final Iterator<K> keyIterator = new LinkedListMultimap.DistinctKeyIterator();
/* 817 */       return new Iterator<Map.Entry<K, Collection<V>>>() {
/*     */           public boolean hasNext() {
/* 819 */             return keyIterator.hasNext();
/*     */           }
/*     */           
/*     */           public Map.Entry<K, Collection<V>> next() {
/* 823 */             final K key = keyIterator.next();
/* 824 */             return (Map.Entry)new AbstractMapEntry<K, Collection<Collection<V>>>() {
/*     */                 public K getKey() {
/* 826 */                   return (K)key;
/*     */                 }
/*     */                 
/*     */                 public Collection<V> getValue() {
/* 830 */                   return LinkedListMultimap.this.get(key);
/*     */                 }
/*     */               };
/*     */           }
/*     */           
/*     */           public void remove() {
/* 836 */             keyIterator.remove();
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<K, Collection<V>> asMap() {
/* 845 */     Map<K, Collection<V>> result = this.map;
/* 846 */     if (result == null) {
/* 847 */       this.map = result = (Map)new AbstractMap<K, Collection<Collection<Collection<Collection<V>>>>>() {
/*     */           Set<Map.Entry<K, Collection<V>>> entrySet;
/*     */           
/*     */           public Set<Map.Entry<K, Collection<V>>> entrySet() {
/* 851 */             Set<Map.Entry<K, Collection<V>>> result = this.entrySet;
/* 852 */             if (result == null) {
/* 853 */               this.entrySet = result = new LinkedListMultimap.AsMapEntries();
/*     */             }
/* 855 */             return result;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public boolean containsKey(@Nullable Object key) {
/* 861 */             return LinkedListMultimap.this.containsKey(key);
/*     */           }
/*     */ 
/*     */           
/*     */           public Collection<V> get(@Nullable Object key) {
/* 866 */             Collection<V> collection = LinkedListMultimap.this.get(key);
/* 867 */             return collection.isEmpty() ? null : collection;
/*     */           }
/*     */           
/*     */           public Collection<V> remove(@Nullable Object key) {
/* 871 */             Collection<V> collection = LinkedListMultimap.this.removeAll(key);
/* 872 */             return collection.isEmpty() ? null : collection;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/* 877 */     return result;
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
/*     */   public boolean equals(@Nullable Object other) {
/* 890 */     if (other == this) {
/* 891 */       return true;
/*     */     }
/* 893 */     if (other instanceof Multimap) {
/* 894 */       Multimap<?, ?> that = (Multimap<?, ?>)other;
/* 895 */       return asMap().equals(that.asMap());
/*     */     } 
/* 897 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 907 */     return asMap().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 917 */     return asMap().toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 926 */     stream.defaultWriteObject();
/* 927 */     stream.writeInt(size());
/* 928 */     for (Map.Entry<K, V> entry : entries()) {
/* 929 */       stream.writeObject(entry.getKey());
/* 930 */       stream.writeObject(entry.getValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 936 */     stream.defaultReadObject();
/* 937 */     this.keyCount = LinkedHashMultiset.create();
/* 938 */     this.keyToKeyHead = Maps.newHashMap();
/* 939 */     this.keyToKeyTail = Maps.newHashMap();
/* 940 */     int size = stream.readInt();
/* 941 */     for (int i = 0; i < size; i++) {
/*     */       
/* 943 */       K key = (K)stream.readObject();
/*     */       
/* 945 */       V value = (V)stream.readObject();
/* 946 */       put(key, value);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\LinkedListMultimap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */