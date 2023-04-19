/*      */ package org.springframework.util;
/*      */ 
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Array;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ConcurrentReferenceHashMap<K, V>
/*      */   extends AbstractMap<K, V>
/*      */   implements ConcurrentMap<K, V>
/*      */ {
/*      */   private static final int DEFAULT_INITIAL_CAPACITY = 16;
/*      */   private static final float DEFAULT_LOAD_FACTOR = 0.75F;
/*      */   private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
/*   68 */   private static final ReferenceType DEFAULT_REFERENCE_TYPE = ReferenceType.SOFT;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int MAXIMUM_CONCURRENCY_LEVEL = 65536;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int MAXIMUM_SEGMENT_SIZE = 1073741824;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final Segment[] segments;
/*      */ 
/*      */ 
/*      */   
/*      */   private final float loadFactor;
/*      */ 
/*      */ 
/*      */   
/*      */   private final ReferenceType referenceType;
/*      */ 
/*      */ 
/*      */   
/*      */   private final int shift;
/*      */ 
/*      */ 
/*      */   
/*      */   private volatile Set<Map.Entry<K, V>> entrySet;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConcurrentReferenceHashMap() {
/*  105 */     this(16, 0.75F, 16, DEFAULT_REFERENCE_TYPE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConcurrentReferenceHashMap(int initialCapacity) {
/*  113 */     this(initialCapacity, 0.75F, 16, DEFAULT_REFERENCE_TYPE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor) {
/*  123 */     this(initialCapacity, loadFactor, 16, DEFAULT_REFERENCE_TYPE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConcurrentReferenceHashMap(int initialCapacity, int concurrencyLevel) {
/*  133 */     this(initialCapacity, 0.75F, concurrencyLevel, DEFAULT_REFERENCE_TYPE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConcurrentReferenceHashMap(int initialCapacity, ReferenceType referenceType) {
/*  142 */     this(initialCapacity, 0.75F, 16, referenceType);
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
/*      */   public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
/*  154 */     this(initialCapacity, loadFactor, concurrencyLevel, DEFAULT_REFERENCE_TYPE);
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
/*      */   public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel, ReferenceType referenceType) {
/*  170 */     Assert.isTrue((initialCapacity >= 0), "Initial capacity must not be negative");
/*  171 */     Assert.isTrue((loadFactor > 0.0F), "Load factor must be positive");
/*  172 */     Assert.isTrue((concurrencyLevel > 0), "Concurrency level must be positive");
/*  173 */     Assert.notNull(referenceType, "Reference type must not be null");
/*  174 */     this.loadFactor = loadFactor;
/*  175 */     this.shift = calculateShift(concurrencyLevel, 65536);
/*  176 */     int size = 1 << this.shift;
/*  177 */     this.referenceType = referenceType;
/*  178 */     int roundedUpSegmentCapacity = (int)(((initialCapacity + size) - 1L) / size);
/*  179 */     this.segments = (Segment[])Array.newInstance(Segment.class, size);
/*  180 */     for (int i = 0; i < this.segments.length; i++) {
/*  181 */       this.segments[i] = new Segment(roundedUpSegmentCapacity);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected final float getLoadFactor() {
/*  187 */     return this.loadFactor;
/*      */   }
/*      */   
/*      */   protected final int getSegmentsSize() {
/*  191 */     return this.segments.length;
/*      */   }
/*      */   
/*      */   protected final Segment getSegment(int index) {
/*  195 */     return this.segments[index];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ReferenceManager createReferenceManager() {
/*  204 */     return new ReferenceManager();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int getHash(Object o) {
/*  215 */     int hash = (o != null) ? o.hashCode() : 0;
/*  216 */     hash += hash << 15 ^ 0xFFFFCD7D;
/*  217 */     hash ^= hash >>> 10;
/*  218 */     hash += hash << 3;
/*  219 */     hash ^= hash >>> 6;
/*  220 */     hash += (hash << 2) + (hash << 14);
/*  221 */     hash ^= hash >>> 16;
/*  222 */     return hash;
/*      */   }
/*      */ 
/*      */   
/*      */   public V get(Object key) {
/*  227 */     Entry<K, V> entry = getEntryIfAvailable(key);
/*  228 */     return (entry != null) ? entry.getValue() : null;
/*      */   }
/*      */ 
/*      */   
/*      */   public V getOrDefault(Object key, V defaultValue) {
/*  233 */     Entry<K, V> entry = getEntryIfAvailable(key);
/*  234 */     return (entry != null) ? entry.getValue() : defaultValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsKey(Object key) {
/*  239 */     Entry<K, V> entry = getEntryIfAvailable(key);
/*  240 */     return (entry != null && ObjectUtils.nullSafeEquals(entry.getKey(), key));
/*      */   }
/*      */   
/*      */   private Entry<K, V> getEntryIfAvailable(Object key) {
/*  244 */     Reference<K, V> ref = getReference(key, Restructure.WHEN_NECESSARY);
/*  245 */     return (ref != null) ? ref.get() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Reference<K, V> getReference(Object key, Restructure restructure) {
/*  256 */     int hash = getHash(key);
/*  257 */     return getSegmentForHash(hash).getReference(key, hash, restructure);
/*      */   }
/*      */ 
/*      */   
/*      */   public V put(K key, V value) {
/*  262 */     return put(key, value, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public V putIfAbsent(K key, V value) {
/*  267 */     return put(key, value, false);
/*      */   }
/*      */   
/*      */   private V put(K key, final V value, final boolean overwriteExisting) {
/*  271 */     return doTask(key, new Task<V>(new TaskOption[] { TaskOption.RESTRUCTURE_BEFORE, TaskOption.RESIZE })
/*      */         {
/*      */           protected V execute(ConcurrentReferenceHashMap.Reference<K, V> ref, ConcurrentReferenceHashMap.Entry<K, V> entry, ConcurrentReferenceHashMap<K, V>.Entries entries) {
/*  274 */             if (entry != null) {
/*  275 */               V oldValue = entry.getValue();
/*  276 */               if (overwriteExisting) {
/*  277 */                 entry.setValue((V)value);
/*      */               }
/*  279 */               return oldValue;
/*      */             } 
/*  281 */             entries.add((V)value);
/*  282 */             return null;
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   public V remove(Object key) {
/*  289 */     return doTask(key, new Task<V>(new TaskOption[] { TaskOption.RESTRUCTURE_AFTER, TaskOption.SKIP_IF_EMPTY })
/*      */         {
/*      */           protected V execute(ConcurrentReferenceHashMap.Reference<K, V> ref, ConcurrentReferenceHashMap.Entry<K, V> entry) {
/*  292 */             if (entry != null) {
/*  293 */               ref.release();
/*  294 */               return entry.value;
/*      */             } 
/*  296 */             return null;
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean remove(Object key, final Object value) {
/*  303 */     return ((Boolean)doTask(key, new Task<Boolean>(new TaskOption[] { TaskOption.RESTRUCTURE_AFTER, TaskOption.SKIP_IF_EMPTY })
/*      */         {
/*      */           protected Boolean execute(ConcurrentReferenceHashMap.Reference<K, V> ref, ConcurrentReferenceHashMap.Entry<K, V> entry) {
/*  306 */             if (entry != null && ObjectUtils.nullSafeEquals(entry.getValue(), value)) {
/*  307 */               ref.release();
/*  308 */               return Boolean.valueOf(true);
/*      */             } 
/*  310 */             return Boolean.valueOf(false);
/*      */           }
/*      */         })).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(K key, final V oldValue, final V newValue) {
/*  317 */     return ((Boolean)doTask(key, new Task<Boolean>(new TaskOption[] { TaskOption.RESTRUCTURE_BEFORE, TaskOption.SKIP_IF_EMPTY })
/*      */         {
/*      */           protected Boolean execute(ConcurrentReferenceHashMap.Reference<K, V> ref, ConcurrentReferenceHashMap.Entry<K, V> entry) {
/*  320 */             if (entry != null && ObjectUtils.nullSafeEquals(entry.getValue(), oldValue)) {
/*  321 */               entry.setValue((V)newValue);
/*  322 */               return Boolean.valueOf(true);
/*      */             } 
/*  324 */             return Boolean.valueOf(false);
/*      */           }
/*      */         })).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public V replace(K key, final V value) {
/*  331 */     return doTask(key, new Task<V>(new TaskOption[] { TaskOption.RESTRUCTURE_BEFORE, TaskOption.SKIP_IF_EMPTY })
/*      */         {
/*      */           protected V execute(ConcurrentReferenceHashMap.Reference<K, V> ref, ConcurrentReferenceHashMap.Entry<K, V> entry) {
/*  334 */             if (entry != null) {
/*  335 */               V oldValue = entry.getValue();
/*  336 */               entry.setValue((V)value);
/*  337 */               return oldValue;
/*      */             } 
/*  339 */             return null;
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   public void clear() {
/*  346 */     for (Segment segment : this.segments) {
/*  347 */       segment.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void purgeUnreferencedEntries() {
/*  358 */     for (Segment segment : this.segments) {
/*  359 */       segment.restructureIfNecessary(false);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  366 */     int size = 0;
/*  367 */     for (Segment segment : this.segments) {
/*  368 */       size += segment.getCount();
/*      */     }
/*  370 */     return size;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/*  375 */     for (Segment segment : this.segments) {
/*  376 */       if (segment.getCount() > 0) {
/*  377 */         return false;
/*      */       }
/*      */     } 
/*  380 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<Map.Entry<K, V>> entrySet() {
/*  385 */     Set<Map.Entry<K, V>> entrySet = this.entrySet;
/*  386 */     if (entrySet == null) {
/*  387 */       entrySet = new EntrySet();
/*  388 */       this.entrySet = entrySet;
/*      */     } 
/*  390 */     return entrySet;
/*      */   }
/*      */   
/*      */   private <T> T doTask(Object key, Task<T> task) {
/*  394 */     int hash = getHash(key);
/*  395 */     return getSegmentForHash(hash).doTask(hash, key, task);
/*      */   }
/*      */   
/*      */   private Segment getSegmentForHash(int hash) {
/*  399 */     return this.segments[hash >>> 32 - this.shift & this.segments.length - 1];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static int calculateShift(int minimumValue, int maximumValue) {
/*  410 */     int shift = 0;
/*  411 */     int value = 1;
/*  412 */     while (value < minimumValue && value < maximumValue) {
/*  413 */       value <<= 1;
/*  414 */       shift++;
/*      */     } 
/*  416 */     return shift;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum ReferenceType
/*      */   {
/*  426 */     SOFT,
/*      */ 
/*      */     
/*  429 */     WEAK;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final class Segment
/*      */     extends ReentrantLock
/*      */   {
/*      */     private final ConcurrentReferenceHashMap<K, V>.ReferenceManager referenceManager;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int initialSize;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private volatile ConcurrentReferenceHashMap.Reference<K, V>[] references;
/*      */ 
/*      */ 
/*      */     
/*  453 */     private volatile int count = 0;
/*      */ 
/*      */ 
/*      */     
/*      */     private int resizeThreshold;
/*      */ 
/*      */ 
/*      */     
/*      */     public Segment(int initialCapacity) {
/*  462 */       this.referenceManager = ConcurrentReferenceHashMap.this.createReferenceManager();
/*  463 */       this.initialSize = 1 << ConcurrentReferenceHashMap.calculateShift(initialCapacity, 1073741824);
/*  464 */       setReferences(createReferenceArray(this.initialSize));
/*      */     }
/*      */     
/*      */     public ConcurrentReferenceHashMap.Reference<K, V> getReference(Object key, int hash, ConcurrentReferenceHashMap.Restructure restructure) {
/*  468 */       if (restructure == ConcurrentReferenceHashMap.Restructure.WHEN_NECESSARY) {
/*  469 */         restructureIfNecessary(false);
/*      */       }
/*  471 */       if (this.count == 0) {
/*  472 */         return null;
/*      */       }
/*      */       
/*  475 */       ConcurrentReferenceHashMap.Reference<K, V>[] references = this.references;
/*  476 */       int index = getIndex(hash, references);
/*  477 */       ConcurrentReferenceHashMap.Reference<K, V> head = references[index];
/*  478 */       return findInChain(head, key, hash);
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
/*      */     public <T> T doTask(final int hash, final Object key, ConcurrentReferenceHashMap<K, V>.Task<T> task) {
/*  490 */       boolean resize = task.hasOption(ConcurrentReferenceHashMap.TaskOption.RESIZE);
/*  491 */       if (task.hasOption(ConcurrentReferenceHashMap.TaskOption.RESTRUCTURE_BEFORE)) {
/*  492 */         restructureIfNecessary(resize);
/*      */       }
/*  494 */       if (task.hasOption(ConcurrentReferenceHashMap.TaskOption.SKIP_IF_EMPTY) && this.count == 0) {
/*  495 */         return task.execute((ConcurrentReferenceHashMap.Reference<K, V>)null, (ConcurrentReferenceHashMap.Entry<K, V>)null, (ConcurrentReferenceHashMap<K, V>.Entries)null);
/*      */       }
/*  497 */       lock();
/*      */       try {
/*  499 */         final int index = getIndex(hash, this.references);
/*  500 */         final ConcurrentReferenceHashMap.Reference<K, V> head = this.references[index];
/*  501 */         ConcurrentReferenceHashMap.Reference<K, V> ref = findInChain(head, key, hash);
/*  502 */         ConcurrentReferenceHashMap.Entry<K, V> entry = (ref != null) ? ref.get() : null;
/*  503 */         ConcurrentReferenceHashMap<K, V>.Entries entries = new ConcurrentReferenceHashMap<K, V>.Entries()
/*      */           {
/*      */             public void add(V value)
/*      */             {
/*  507 */               ConcurrentReferenceHashMap.Entry<K, V> newEntry = new ConcurrentReferenceHashMap.Entry<K, V>((K)key, value);
/*  508 */               ConcurrentReferenceHashMap.Reference<K, V> newReference = ConcurrentReferenceHashMap.Segment.this.referenceManager.createReference(newEntry, hash, head);
/*  509 */               ConcurrentReferenceHashMap.Segment.this.references[index] = newReference;
/*  510 */               ConcurrentReferenceHashMap.Segment.this.count++;
/*      */             }
/*      */           };
/*  513 */         return task.execute(ref, entry, entries);
/*      */       } finally {
/*      */         
/*  516 */         unlock();
/*  517 */         if (task.hasOption(ConcurrentReferenceHashMap.TaskOption.RESTRUCTURE_AFTER)) {
/*  518 */           restructureIfNecessary(resize);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/*  527 */       if (this.count == 0) {
/*      */         return;
/*      */       }
/*  530 */       lock();
/*      */       try {
/*  532 */         setReferences(createReferenceArray(this.initialSize));
/*  533 */         this.count = 0;
/*      */       } finally {
/*      */         
/*  536 */         unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected final void restructureIfNecessary(boolean allowResize) {
/*  547 */       boolean needsResize = (this.count > 0 && this.count >= this.resizeThreshold);
/*  548 */       ConcurrentReferenceHashMap.Reference<K, V> ref = this.referenceManager.pollForPurge();
/*  549 */       if (ref != null || (needsResize && allowResize)) {
/*  550 */         lock();
/*      */         try {
/*  552 */           int countAfterRestructure = this.count;
/*  553 */           Set<ConcurrentReferenceHashMap.Reference<K, V>> toPurge = Collections.emptySet();
/*  554 */           if (ref != null) {
/*  555 */             toPurge = new HashSet<ConcurrentReferenceHashMap.Reference<K, V>>();
/*  556 */             while (ref != null) {
/*  557 */               toPurge.add(ref);
/*  558 */               ref = this.referenceManager.pollForPurge();
/*      */             } 
/*      */           } 
/*  561 */           countAfterRestructure -= toPurge.size();
/*      */ 
/*      */ 
/*      */           
/*  565 */           needsResize = (countAfterRestructure > 0 && countAfterRestructure >= this.resizeThreshold);
/*  566 */           boolean resizing = false;
/*  567 */           int restructureSize = this.references.length;
/*  568 */           if (allowResize && needsResize && restructureSize < 1073741824) {
/*  569 */             restructureSize <<= 1;
/*  570 */             resizing = true;
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  575 */           ConcurrentReferenceHashMap.Reference<K, V>[] restructured = resizing ? createReferenceArray(restructureSize) : this.references;
/*      */ 
/*      */           
/*  578 */           for (int i = 0; i < this.references.length; i++) {
/*  579 */             ref = this.references[i];
/*  580 */             if (!resizing) {
/*  581 */               restructured[i] = null;
/*      */             }
/*  583 */             while (ref != null) {
/*  584 */               if (!toPurge.contains(ref) && ref.get() != null) {
/*  585 */                 int index = getIndex(ref.getHash(), restructured);
/*  586 */                 restructured[index] = this.referenceManager.createReference(ref
/*  587 */                     .get(), ref.getHash(), restructured[index]);
/*      */               } 
/*  589 */               ref = ref.getNext();
/*      */             } 
/*      */           } 
/*      */ 
/*      */           
/*  594 */           if (resizing) {
/*  595 */             setReferences(restructured);
/*      */           }
/*  597 */           this.count = Math.max(countAfterRestructure, 0);
/*      */         } finally {
/*      */           
/*  600 */           unlock();
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     private ConcurrentReferenceHashMap.Reference<K, V> findInChain(ConcurrentReferenceHashMap.Reference<K, V> ref, Object key, int hash) {
/*  606 */       ConcurrentReferenceHashMap.Reference<K, V> currRef = ref;
/*  607 */       while (currRef != null) {
/*  608 */         if (currRef.getHash() == hash) {
/*  609 */           ConcurrentReferenceHashMap.Entry<K, V> entry = currRef.get();
/*  610 */           if (entry != null) {
/*  611 */             K entryKey = entry.getKey();
/*  612 */             if (ObjectUtils.nullSafeEquals(entryKey, key)) {
/*  613 */               return currRef;
/*      */             }
/*      */           } 
/*      */         } 
/*  617 */         currRef = currRef.getNext();
/*      */       } 
/*  619 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     private ConcurrentReferenceHashMap.Reference<K, V>[] createReferenceArray(int size) {
/*  624 */       return (ConcurrentReferenceHashMap.Reference<K, V>[])new ConcurrentReferenceHashMap.Reference[size];
/*      */     }
/*      */     
/*      */     private int getIndex(int hash, ConcurrentReferenceHashMap.Reference<K, V>[] references) {
/*  628 */       return hash & references.length - 1;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void setReferences(ConcurrentReferenceHashMap.Reference<K, V>[] references) {
/*  636 */       this.references = references;
/*  637 */       this.resizeThreshold = (int)(references.length * ConcurrentReferenceHashMap.this.getLoadFactor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final int getSize() {
/*  644 */       return this.references.length;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final int getCount() {
/*  651 */       return this.count;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static interface Reference<K, V>
/*      */   {
/*      */     ConcurrentReferenceHashMap.Entry<K, V> get();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int getHash();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Reference<K, V> getNext();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void release();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final class Entry<K, V>
/*      */     implements Map.Entry<K, V>
/*      */   {
/*      */     private final K key;
/*      */ 
/*      */ 
/*      */     
/*      */     private volatile V value;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Entry(K key, V value) {
/*  695 */       this.key = key;
/*  696 */       this.value = value;
/*      */     }
/*      */ 
/*      */     
/*      */     public K getKey() {
/*  701 */       return this.key;
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/*  706 */       return this.value;
/*      */     }
/*      */ 
/*      */     
/*      */     public V setValue(V value) {
/*  711 */       V previous = this.value;
/*  712 */       this.value = value;
/*  713 */       return previous;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  718 */       return (new StringBuilder()).append(this.key).append("=").append(this.value).toString();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public final boolean equals(Object other) {
/*  724 */       if (this == other) {
/*  725 */         return true;
/*      */       }
/*  727 */       if (!(other instanceof Map.Entry)) {
/*  728 */         return false;
/*      */       }
/*  730 */       Map.Entry otherEntry = (Map.Entry)other;
/*  731 */       return (ObjectUtils.nullSafeEquals(getKey(), otherEntry.getKey()) && 
/*  732 */         ObjectUtils.nullSafeEquals(getValue(), otherEntry.getValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public final int hashCode() {
/*  737 */       return ObjectUtils.nullSafeHashCode(this.key) ^ ObjectUtils.nullSafeHashCode(this.value);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private abstract class Task<T>
/*      */   {
/*      */     private final EnumSet<ConcurrentReferenceHashMap.TaskOption> options;
/*      */ 
/*      */ 
/*      */     
/*      */     public Task(ConcurrentReferenceHashMap.TaskOption... options) {
/*  750 */       this.options = (options.length == 0) ? EnumSet.<ConcurrentReferenceHashMap.TaskOption>noneOf(ConcurrentReferenceHashMap.TaskOption.class) : EnumSet.<ConcurrentReferenceHashMap.TaskOption>of(options[0], options);
/*      */     }
/*      */     
/*      */     public boolean hasOption(ConcurrentReferenceHashMap.TaskOption option) {
/*  754 */       return this.options.contains(option);
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
/*      */     protected T execute(ConcurrentReferenceHashMap.Reference<K, V> ref, ConcurrentReferenceHashMap.Entry<K, V> entry, ConcurrentReferenceHashMap<K, V>.Entries entries) {
/*  766 */       return execute(ref, entry);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected T execute(ConcurrentReferenceHashMap.Reference<K, V> ref, ConcurrentReferenceHashMap.Entry<K, V> entry) {
/*  777 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private enum TaskOption
/*      */   {
/*  787 */     RESTRUCTURE_BEFORE, RESTRUCTURE_AFTER, SKIP_IF_EMPTY, RESIZE;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private abstract class Entries
/*      */   {
/*      */     private Entries() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public abstract void add(V param1V);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class EntrySet
/*      */     extends AbstractSet<Map.Entry<K, V>>
/*      */   {
/*      */     private EntrySet() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/*  811 */       return new ConcurrentReferenceHashMap.EntryIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  816 */       if (o instanceof Map.Entry) {
/*  817 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/*  818 */         ConcurrentReferenceHashMap.Reference<K, V> ref = ConcurrentReferenceHashMap.this.getReference(entry.getKey(), ConcurrentReferenceHashMap.Restructure.NEVER);
/*  819 */         ConcurrentReferenceHashMap.Entry<K, V> otherEntry = (ref != null) ? ref.get() : null;
/*  820 */         if (otherEntry != null) {
/*  821 */           return ObjectUtils.nullSafeEquals(otherEntry.getValue(), otherEntry.getValue());
/*      */         }
/*      */       } 
/*  824 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/*  829 */       if (o instanceof Map.Entry) {
/*  830 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/*  831 */         return ConcurrentReferenceHashMap.this.remove(entry.getKey(), entry.getValue());
/*      */       } 
/*  833 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  838 */       return ConcurrentReferenceHashMap.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  843 */       ConcurrentReferenceHashMap.this.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class EntryIterator
/*      */     implements Iterator<Map.Entry<K, V>>
/*      */   {
/*      */     private int segmentIndex;
/*      */     
/*      */     private int referenceIndex;
/*      */     
/*      */     private ConcurrentReferenceHashMap.Reference<K, V>[] references;
/*      */     
/*      */     private ConcurrentReferenceHashMap.Reference<K, V> reference;
/*      */     
/*      */     private ConcurrentReferenceHashMap.Entry<K, V> next;
/*      */     
/*      */     private ConcurrentReferenceHashMap.Entry<K, V> last;
/*      */ 
/*      */     
/*      */     public EntryIterator() {
/*  866 */       moveToNextSegment();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/*  871 */       getNextIfNecessary();
/*  872 */       return (this.next != null);
/*      */     }
/*      */ 
/*      */     
/*      */     public ConcurrentReferenceHashMap.Entry<K, V> next() {
/*  877 */       getNextIfNecessary();
/*  878 */       if (this.next == null) {
/*  879 */         throw new NoSuchElementException();
/*      */       }
/*  881 */       this.last = this.next;
/*  882 */       this.next = null;
/*  883 */       return this.last;
/*      */     }
/*      */     
/*      */     private void getNextIfNecessary() {
/*  887 */       while (this.next == null) {
/*  888 */         moveToNextReference();
/*  889 */         if (this.reference == null) {
/*      */           return;
/*      */         }
/*  892 */         this.next = this.reference.get();
/*      */       } 
/*      */     }
/*      */     
/*      */     private void moveToNextReference() {
/*  897 */       if (this.reference != null) {
/*  898 */         this.reference = this.reference.getNext();
/*      */       }
/*  900 */       while (this.reference == null && this.references != null) {
/*  901 */         if (this.referenceIndex >= this.references.length) {
/*  902 */           moveToNextSegment();
/*  903 */           this.referenceIndex = 0;
/*      */           continue;
/*      */         } 
/*  906 */         this.reference = this.references[this.referenceIndex];
/*  907 */         this.referenceIndex++;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private void moveToNextSegment() {
/*  913 */       this.reference = null;
/*  914 */       this.references = null;
/*  915 */       if (this.segmentIndex < ConcurrentReferenceHashMap.this.segments.length) {
/*  916 */         this.references = (ConcurrentReferenceHashMap.this.segments[this.segmentIndex]).references;
/*  917 */         this.segmentIndex++;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/*  923 */       Assert.state((this.last != null), "No element to remove");
/*  924 */       ConcurrentReferenceHashMap.this.remove(this.last.getKey());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected enum Restructure
/*      */   {
/*  934 */     WHEN_NECESSARY, NEVER;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected class ReferenceManager
/*      */   {
/*  944 */     private final ReferenceQueue<ConcurrentReferenceHashMap.Entry<K, V>> queue = new ReferenceQueue<ConcurrentReferenceHashMap.Entry<K, V>>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ConcurrentReferenceHashMap.Reference<K, V> createReference(ConcurrentReferenceHashMap.Entry<K, V> entry, int hash, ConcurrentReferenceHashMap.Reference<K, V> next) {
/*  954 */       if (ConcurrentReferenceHashMap.this.referenceType == ConcurrentReferenceHashMap.ReferenceType.WEAK) {
/*  955 */         return new ConcurrentReferenceHashMap.WeakEntryReference<K, V>(entry, hash, next, this.queue);
/*      */       }
/*  957 */       return new ConcurrentReferenceHashMap.SoftEntryReference<K, V>(entry, hash, next, this.queue);
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
/*      */     public ConcurrentReferenceHashMap.Reference<K, V> pollForPurge() {
/*  969 */       return (ConcurrentReferenceHashMap.Reference)this.queue.poll();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class SoftEntryReference<K, V>
/*      */     extends SoftReference<Entry<K, V>>
/*      */     implements Reference<K, V>
/*      */   {
/*      */     private final int hash;
/*      */     
/*      */     private final ConcurrentReferenceHashMap.Reference<K, V> nextReference;
/*      */ 
/*      */     
/*      */     public SoftEntryReference(ConcurrentReferenceHashMap.Entry<K, V> entry, int hash, ConcurrentReferenceHashMap.Reference<K, V> next, ReferenceQueue<ConcurrentReferenceHashMap.Entry<K, V>> queue) {
/*  984 */       super(entry, queue);
/*  985 */       this.hash = hash;
/*  986 */       this.nextReference = next;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/*  991 */       return this.hash;
/*      */     }
/*      */ 
/*      */     
/*      */     public ConcurrentReferenceHashMap.Reference<K, V> getNext() {
/*  996 */       return this.nextReference;
/*      */     }
/*      */ 
/*      */     
/*      */     public void release() {
/* 1001 */       enqueue();
/* 1002 */       clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class WeakEntryReference<K, V>
/*      */     extends WeakReference<Entry<K, V>>
/*      */     implements Reference<K, V>
/*      */   {
/*      */     private final int hash;
/*      */     
/*      */     private final ConcurrentReferenceHashMap.Reference<K, V> nextReference;
/*      */ 
/*      */     
/*      */     public WeakEntryReference(ConcurrentReferenceHashMap.Entry<K, V> entry, int hash, ConcurrentReferenceHashMap.Reference<K, V> next, ReferenceQueue<ConcurrentReferenceHashMap.Entry<K, V>> queue) {
/* 1017 */       super(entry, queue);
/* 1018 */       this.hash = hash;
/* 1019 */       this.nextReference = next;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/* 1024 */       return this.hash;
/*      */     }
/*      */ 
/*      */     
/*      */     public ConcurrentReferenceHashMap.Reference<K, V> getNext() {
/* 1029 */       return this.nextReference;
/*      */     }
/*      */ 
/*      */     
/*      */     public void release() {
/* 1034 */       enqueue();
/* 1035 */       clear();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\ConcurrentReferenceHashMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */