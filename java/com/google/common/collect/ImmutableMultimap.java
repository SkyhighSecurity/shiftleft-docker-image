/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
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
/*     */ @GwtCompatible
/*     */ public abstract class ImmutableMultimap<K, V>
/*     */   implements Multimap<K, V>, Serializable
/*     */ {
/*     */   final transient ImmutableMap<K, ? extends ImmutableCollection<V>> map;
/*     */   final transient int size;
/*     */   private transient ImmutableCollection<Map.Entry<K, V>> entries;
/*     */   private transient ImmutableMultiset<K> keys;
/*     */   private transient ImmutableCollection<V> values;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of() {
/*  54 */     return ImmutableListMultimap.of();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1) {
/*  61 */     return ImmutableListMultimap.of(k1, v1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2) {
/*  68 */     return ImmutableListMultimap.of(k1, v1, k2, v2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/*  76 */     return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/*  84 */     return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/*  92 */     return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 102 */     return new Builder<K, V>();
/*     */   }
/*     */ 
/*     */   
/*     */   private static class BuilderMultimap<K, V>
/*     */     extends AbstractMultimap<K, V>
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     BuilderMultimap() {
/* 112 */       super(new LinkedHashMap<K, Collection<V>>());
/*     */     }
/*     */     Collection<V> createCollection() {
/* 115 */       return Lists.newArrayList();
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
/*     */   public static class Builder<K, V>
/*     */   {
/* 137 */     private final Multimap<K, V> builderMultimap = new ImmutableMultimap.BuilderMultimap<K, V>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> put(K key, V value) {
/* 149 */       this.builderMultimap.put((K)Preconditions.checkNotNull(key), (V)Preconditions.checkNotNull(value));
/* 150 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
/* 161 */       Collection<V> valueList = this.builderMultimap.get((K)Preconditions.checkNotNull(key));
/* 162 */       for (V value : values) {
/* 163 */         valueList.add((V)Preconditions.checkNotNull(value));
/*     */       }
/* 165 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> putAll(K key, V... values) {
/* 175 */       return putAll(key, Arrays.asList(values));
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
/*     */     
/*     */     public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
/* 189 */       for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : (Iterable<Map.Entry<? extends K, ? extends Collection<? extends V>>>)multimap.asMap().entrySet()) {
/* 190 */         putAll(entry.getKey(), entry.getValue());
/*     */       }
/* 192 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableMultimap<K, V> build() {
/* 199 */       return ImmutableMultimap.copyOf(this.builderMultimap);
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
/*     */   public static <K, V> ImmutableMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
/* 217 */     if (multimap instanceof ImmutableMultimap) {
/*     */       
/* 219 */       ImmutableMultimap<K, V> kvMultimap = (ImmutableMultimap)multimap;
/*     */       
/* 221 */       return kvMultimap;
/*     */     } 
/* 223 */     return ImmutableListMultimap.copyOf(multimap);
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
/*     */   static class FieldSettersHolder
/*     */   {
/* 237 */     static final Serialization.FieldSetter<ImmutableMultimap> MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "map");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 242 */     static final Serialization.FieldSetter<ImmutableMultimap> SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "size");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableMultimap(ImmutableMap<K, ? extends ImmutableCollection<V>> map, int size) {
/* 248 */     this.map = map;
/* 249 */     this.size = size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> removeAll(Object key) {
/* 260 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> replaceValues(K key, Iterable<? extends V> values) {
/* 270 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 279 */     throw new UnsupportedOperationException();
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
/*     */   public boolean put(K key, V value) {
/* 296 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean putAll(K key, Iterable<? extends V> values) {
/* 305 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/* 314 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove(Object key, Object value) {
/* 323 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsEntry(@Nullable Object key, @Nullable Object value) {
/* 329 */     Collection<V> values = this.map.get(key);
/* 330 */     return (values != null && values.contains(value));
/*     */   }
/*     */   
/*     */   public boolean containsKey(@Nullable Object key) {
/* 334 */     return this.map.containsKey(key);
/*     */   }
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/* 338 */     for (Collection<V> valueCollection : this.map.values()) {
/* 339 */       if (valueCollection.contains(value)) {
/* 340 */         return true;
/*     */       }
/*     */     } 
/* 343 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 347 */     return (this.size == 0);
/*     */   }
/*     */   
/*     */   public int size() {
/* 351 */     return this.size;
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 355 */     if (object instanceof Multimap) {
/* 356 */       Multimap<?, ?> that = (Multimap<?, ?>)object;
/* 357 */       return this.map.equals(that.asMap());
/*     */     } 
/* 359 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 363 */     return this.map.hashCode();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 367 */     return this.map.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<K> keySet() {
/* 378 */     return this.map.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<K, Collection<V>> asMap() {
/* 387 */     return (ImmutableMap)this.map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<Map.Entry<K, V>> entries() {
/* 398 */     ImmutableCollection<Map.Entry<K, V>> result = this.entries;
/* 399 */     return (result == null) ? (this.entries = new EntryCollection<K, V>(this)) : result;
/*     */   }
/*     */   
/*     */   private static class EntryCollection<K, V>
/*     */     extends ImmutableCollection<Map.Entry<K, V>> {
/*     */     final ImmutableMultimap<K, V> multimap;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     EntryCollection(ImmutableMultimap<K, V> multimap) {
/* 408 */       this.multimap = multimap;
/*     */     }
/*     */ 
/*     */     
/*     */     public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/* 413 */       final Iterator<? extends Map.Entry<K, ? extends ImmutableCollection<V>>> mapIterator = this.multimap.map.entrySet().iterator();
/*     */       
/* 415 */       return new UnmodifiableIterator<Map.Entry<K, V>>() {
/*     */           K key;
/*     */           Iterator<V> valueIterator;
/*     */           
/*     */           public boolean hasNext() {
/* 420 */             return ((this.key != null && this.valueIterator.hasNext()) || mapIterator.hasNext());
/*     */           }
/*     */ 
/*     */           
/*     */           public Map.Entry<K, V> next() {
/* 425 */             if (this.key == null || !this.valueIterator.hasNext()) {
/* 426 */               Map.Entry<K, ? extends ImmutableCollection<V>> entry = mapIterator.next();
/*     */               
/* 428 */               this.key = entry.getKey();
/* 429 */               this.valueIterator = ((ImmutableCollection<V>)entry.getValue()).iterator();
/*     */             } 
/* 431 */             return Maps.immutableEntry(this.key, this.valueIterator.next());
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public int size() {
/* 437 */       return this.multimap.size();
/*     */     }
/*     */     
/*     */     public boolean contains(Object object) {
/* 441 */       if (object instanceof Map.Entry) {
/* 442 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/* 443 */         return this.multimap.containsEntry(entry.getKey(), entry.getValue());
/*     */       } 
/* 445 */       return false;
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
/*     */   public ImmutableMultiset<K> keys() {
/* 460 */     ImmutableMultiset<K> result = this.keys;
/* 461 */     return (result == null) ? (this.keys = createKeys()) : result;
/*     */   }
/*     */   
/*     */   private ImmutableMultiset<K> createKeys() {
/* 465 */     ImmutableMultiset.Builder<K> builder = ImmutableMultiset.builder();
/*     */     
/* 467 */     for (Map.Entry<K, ? extends ImmutableCollection<V>> entry : this.map.entrySet()) {
/* 468 */       builder.addCopies(entry.getKey(), ((ImmutableCollection)entry.getValue()).size());
/*     */     }
/* 470 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> values() {
/* 481 */     ImmutableCollection<V> result = this.values;
/* 482 */     return (result == null) ? (this.values = new Values<V>(this)) : result;
/*     */   }
/*     */   public abstract ImmutableCollection<V> get(K paramK);
/*     */   
/*     */   private static class Values<V> extends ImmutableCollection<V> { final Multimap<?, V> multimap;
/*     */     
/*     */     Values(Multimap<?, V> multimap) {
/* 489 */       this.multimap = multimap;
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     public UnmodifiableIterator<V> iterator() {
/* 493 */       final Iterator<? extends Map.Entry<?, V>> entryIterator = this.multimap.entries().iterator();
/*     */       
/* 495 */       return new UnmodifiableIterator<V>() {
/*     */           public boolean hasNext() {
/* 497 */             return entryIterator.hasNext();
/*     */           }
/*     */           public V next() {
/* 500 */             return (V)((Map.Entry)entryIterator.next()).getValue();
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public int size() {
/* 506 */       return this.multimap.size();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ImmutableMultimap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */