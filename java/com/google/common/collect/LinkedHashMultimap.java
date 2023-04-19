/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ public final class LinkedHashMultimap<K, V>
/*     */   extends AbstractSetMultimap<K, V>
/*     */ {
/*     */   private static final int DEFAULT_VALUES_PER_KEY = 8;
/*     */   @VisibleForTesting
/*  72 */   transient int expectedValuesPerKey = 8;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   transient Collection<Map.Entry<K, V>> linkedEntries;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> LinkedHashMultimap<K, V> create() {
/*  87 */     return new LinkedHashMultimap<K, V>();
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
/*     */   public static <K, V> LinkedHashMultimap<K, V> create(int expectedKeys, int expectedValuesPerKey) {
/* 101 */     return new LinkedHashMultimap<K, V>(expectedKeys, expectedValuesPerKey);
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
/*     */   public static <K, V> LinkedHashMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
/* 115 */     return new LinkedHashMultimap<K, V>(multimap);
/*     */   }
/*     */   
/*     */   private LinkedHashMultimap() {
/* 119 */     super(new LinkedHashMap<K, Collection<V>>());
/* 120 */     this.linkedEntries = Sets.newLinkedHashSet();
/*     */   }
/*     */   
/*     */   private LinkedHashMultimap(int expectedKeys, int expectedValuesPerKey) {
/* 124 */     super(new LinkedHashMap<K, Collection<V>>(expectedKeys));
/* 125 */     Preconditions.checkArgument((expectedValuesPerKey >= 0));
/* 126 */     this.expectedValuesPerKey = expectedValuesPerKey;
/* 127 */     this.linkedEntries = new LinkedHashSet<Map.Entry<K, V>>(expectedKeys * expectedValuesPerKey);
/*     */   }
/*     */ 
/*     */   
/*     */   private LinkedHashMultimap(Multimap<? extends K, ? extends V> multimap) {
/* 132 */     super(new LinkedHashMap<K, Collection<V>>(Maps.capacity(multimap.keySet().size())));
/*     */     
/* 134 */     this.linkedEntries = new LinkedHashSet<Map.Entry<K, V>>(Maps.capacity(multimap.size()));
/*     */     
/* 136 */     putAll(multimap);
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
/*     */   Set<V> createCollection() {
/* 149 */     return new LinkedHashSet<V>(Maps.capacity(this.expectedValuesPerKey));
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
/*     */   Collection<V> createCollection(@Nullable K key) {
/* 163 */     return new SetDecorator(key, createCollection());
/*     */   }
/*     */   
/*     */   private class SetDecorator extends ForwardingSet<V> {
/*     */     final Set<V> delegate;
/*     */     final K key;
/*     */     
/*     */     SetDecorator(K key, Set<V> delegate) {
/* 171 */       this.delegate = delegate;
/* 172 */       this.key = key;
/*     */     }
/*     */     
/*     */     protected Set<V> delegate() {
/* 176 */       return this.delegate;
/*     */     }
/*     */     
/*     */     <E> Map.Entry<K, E> createEntry(@Nullable E value) {
/* 180 */       return Maps.immutableEntry(this.key, value);
/*     */     }
/*     */ 
/*     */     
/*     */     <E> Collection<Map.Entry<K, E>> createEntries(Collection<E> values) {
/* 185 */       Collection<Map.Entry<K, E>> entries = Lists.newArrayListWithExpectedSize(values.size());
/*     */       
/* 187 */       for (E value : values) {
/* 188 */         entries.add(createEntry(value));
/*     */       }
/* 190 */       return entries;
/*     */     }
/*     */     
/*     */     public boolean add(@Nullable V value) {
/* 194 */       boolean changed = this.delegate.add(value);
/* 195 */       if (changed) {
/* 196 */         LinkedHashMultimap.this.linkedEntries.add(createEntry(value));
/*     */       }
/* 198 */       return changed;
/*     */     }
/*     */     
/*     */     public boolean addAll(Collection<? extends V> values) {
/* 202 */       boolean changed = this.delegate.addAll(values);
/* 203 */       if (changed) {
/* 204 */         LinkedHashMultimap.this.linkedEntries.addAll(createEntries(delegate()));
/*     */       }
/* 206 */       return changed;
/*     */     }
/*     */     
/*     */     public void clear() {
/* 210 */       LinkedHashMultimap.this.linkedEntries.removeAll(createEntries(delegate()));
/* 211 */       this.delegate.clear();
/*     */     }
/*     */     
/*     */     public Iterator<V> iterator() {
/* 215 */       final Iterator<V> delegateIterator = this.delegate.iterator();
/* 216 */       return new Iterator<V>() {
/*     */           V value;
/*     */           
/*     */           public boolean hasNext() {
/* 220 */             return delegateIterator.hasNext();
/*     */           }
/*     */           public V next() {
/* 223 */             this.value = delegateIterator.next();
/* 224 */             return this.value;
/*     */           }
/*     */           public void remove() {
/* 227 */             delegateIterator.remove();
/* 228 */             LinkedHashMultimap.this.linkedEntries.remove(LinkedHashMultimap.SetDecorator.this.createEntry(this.value));
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public boolean remove(@Nullable Object value) {
/* 234 */       boolean changed = this.delegate.remove(value);
/* 235 */       if (changed)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 240 */         LinkedHashMultimap.this.linkedEntries.remove(createEntry(value));
/*     */       }
/* 242 */       return changed;
/*     */     }
/*     */     
/*     */     public boolean removeAll(Collection<?> values) {
/* 246 */       boolean changed = this.delegate.removeAll(values);
/* 247 */       if (changed) {
/* 248 */         LinkedHashMultimap.this.linkedEntries.removeAll(createEntries(values));
/*     */       }
/* 250 */       return changed;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> values) {
/* 258 */       boolean changed = false;
/* 259 */       Iterator<V> iterator = this.delegate.iterator();
/* 260 */       while (iterator.hasNext()) {
/* 261 */         V value = iterator.next();
/* 262 */         if (!values.contains(value)) {
/* 263 */           iterator.remove();
/* 264 */           LinkedHashMultimap.this.linkedEntries.remove(Maps.immutableEntry(this.key, value));
/* 265 */           changed = true;
/*     */         } 
/*     */       } 
/* 268 */       return changed;
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
/*     */   Iterator<Map.Entry<K, V>> createEntryIterator() {
/* 281 */     final Iterator<Map.Entry<K, V>> delegateIterator = this.linkedEntries.iterator();
/*     */     
/* 283 */     return new Iterator<Map.Entry<K, V>>() {
/*     */         Map.Entry<K, V> entry;
/*     */         
/*     */         public boolean hasNext() {
/* 287 */           return delegateIterator.hasNext();
/*     */         }
/*     */         
/*     */         public Map.Entry<K, V> next() {
/* 291 */           this.entry = delegateIterator.next();
/* 292 */           return this.entry;
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 297 */           delegateIterator.remove();
/* 298 */           LinkedHashMultimap.this.remove(this.entry.getKey(), this.entry.getValue());
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
/*     */   public Set<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
/* 313 */     return super.replaceValues(key, values);
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
/*     */   public Set<Map.Entry<K, V>> entries() {
/* 329 */     return super.entries();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 340 */     return super.values();
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
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 352 */     stream.defaultWriteObject();
/* 353 */     stream.writeInt(this.expectedValuesPerKey);
/* 354 */     Serialization.writeMultimap(this, stream);
/* 355 */     for (Map.Entry<K, V> entry : this.linkedEntries) {
/* 356 */       stream.writeObject(entry.getKey());
/* 357 */       stream.writeObject(entry.getValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 363 */     stream.defaultReadObject();
/* 364 */     this.expectedValuesPerKey = stream.readInt();
/* 365 */     int distinctKeys = Serialization.readCount(stream);
/* 366 */     setMap(new LinkedHashMap<K, Collection<V>>(Maps.capacity(distinctKeys)));
/* 367 */     this.linkedEntries = new LinkedHashSet<Map.Entry<K, V>>(distinctKeys * this.expectedValuesPerKey);
/*     */     
/* 369 */     Serialization.populateMultimap(this, stream, distinctKeys);
/* 370 */     this.linkedEntries.clear();
/* 371 */     for (int i = 0; i < size(); i++) {
/*     */       
/* 373 */       K key = (K)stream.readObject();
/*     */       
/* 375 */       V value = (V)stream.readObject();
/* 376 */       this.linkedEntries.add(Maps.immutableEntry(key, value));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\LinkedHashMultimap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */