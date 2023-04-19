/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true)
/*     */ public class ImmutableSetMultimap<K, V>
/*     */   extends ImmutableMultimap<K, V>
/*     */   implements SetMultimap<K, V>
/*     */ {
/*     */   private transient ImmutableSet<Map.Entry<K, V>> entries;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of() {
/*  60 */     return EmptyImmutableSetMultimap.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1) {
/*  67 */     Builder<K, V> builder = builder();
/*  68 */     builder.put(k1, v1);
/*  69 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2) {
/*  78 */     Builder<K, V> builder = builder();
/*  79 */     builder.put(k1, v1);
/*  80 */     builder.put(k2, v2);
/*  81 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/*  91 */     Builder<K, V> builder = builder();
/*  92 */     builder.put(k1, v1);
/*  93 */     builder.put(k2, v2);
/*  94 */     builder.put(k3, v3);
/*  95 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 105 */     Builder<K, V> builder = builder();
/* 106 */     builder.put(k1, v1);
/* 107 */     builder.put(k2, v2);
/* 108 */     builder.put(k3, v3);
/* 109 */     builder.put(k4, v4);
/* 110 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 120 */     Builder<K, V> builder = builder();
/* 121 */     builder.put(k1, v1);
/* 122 */     builder.put(k2, v2);
/* 123 */     builder.put(k3, v3);
/* 124 */     builder.put(k4, v4);
/* 125 */     builder.put(k5, v5);
/* 126 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 135 */     return new Builder<K, V>();
/*     */   }
/*     */   
/*     */   private static class BuilderMultimap<K, V>
/*     */     extends AbstractMultimap<K, V>
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     BuilderMultimap() {
/* 144 */       super(new LinkedHashMap<K, Collection<V>>());
/*     */     }
/*     */     Collection<V> createCollection() {
/* 147 */       return Sets.newLinkedHashSet();
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
/*     */   public static final class Builder<K, V>
/*     */     extends ImmutableMultimap.Builder<K, V>
/*     */   {
/* 170 */     private final Multimap<K, V> builderMultimap = new ImmutableSetMultimap.BuilderMultimap<K, V>();
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
/*     */     public Builder<K, V> put(K key, V value) {
/* 183 */       this.builderMultimap.put((K)Preconditions.checkNotNull(key), (V)Preconditions.checkNotNull(value));
/* 184 */       return this;
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
/* 195 */       Collection<V> collection = this.builderMultimap.get((K)Preconditions.checkNotNull(key));
/* 196 */       for (V value : values) {
/* 197 */         collection.add((V)Preconditions.checkNotNull(value));
/*     */       }
/* 199 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> putAll(K key, V... values) {
/* 209 */       return putAll(key, Arrays.asList(values));
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
/*     */     
/*     */     public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
/* 224 */       for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : (Iterable<Map.Entry<? extends K, ? extends Collection<? extends V>>>)multimap.asMap().entrySet()) {
/* 225 */         putAll(entry.getKey(), entry.getValue());
/*     */       }
/* 227 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableSetMultimap<K, V> build() {
/* 234 */       return ImmutableSetMultimap.copyOf(this.builderMultimap);
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
/*     */   public static <K, V> ImmutableSetMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
/* 254 */     if (multimap.isEmpty()) {
/* 255 */       return of();
/*     */     }
/*     */     
/* 258 */     if (multimap instanceof ImmutableSetMultimap) {
/*     */       
/* 260 */       ImmutableSetMultimap<K, V> kvMultimap = (ImmutableSetMultimap)multimap;
/*     */       
/* 262 */       return kvMultimap;
/*     */     } 
/*     */     
/* 265 */     ImmutableMap.Builder<K, ImmutableSet<V>> builder = ImmutableMap.builder();
/* 266 */     int size = 0;
/*     */ 
/*     */     
/* 269 */     for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : (Iterable<Map.Entry<? extends K, ? extends Collection<? extends V>>>)multimap.asMap().entrySet()) {
/* 270 */       K key = entry.getKey();
/* 271 */       Collection<? extends V> values = entry.getValue();
/* 272 */       ImmutableSet<V> set = ImmutableSet.copyOf(values);
/* 273 */       if (!set.isEmpty()) {
/* 274 */         builder.put(key, set);
/* 275 */         size += set.size();
/*     */       } 
/*     */     } 
/*     */     
/* 279 */     return new ImmutableSetMultimap<K, V>(builder.build(), size);
/*     */   }
/*     */   
/*     */   ImmutableSetMultimap(ImmutableMap<K, ImmutableSet<V>> map, int size) {
/* 283 */     super((ImmutableMap)map, size);
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
/*     */   public ImmutableSet<V> get(@Nullable K key) {
/* 296 */     ImmutableSet<V> set = (ImmutableSet<V>)this.map.get(key);
/* 297 */     return (set == null) ? ImmutableSet.<V>of() : set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<V> removeAll(Object key) {
/* 306 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<V> replaceValues(K key, Iterable<? extends V> values) {
/* 316 */     throw new UnsupportedOperationException();
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
/*     */   public ImmutableSet<Map.Entry<K, V>> entries() {
/* 328 */     ImmutableSet<Map.Entry<K, V>> result = this.entries;
/* 329 */     return (result == null) ? (this.entries = ImmutableSet.copyOf(super.entries())) : result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 339 */     stream.defaultWriteObject();
/* 340 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*     */     ImmutableMap<Object, ImmutableSet<Object>> tmpMap;
/* 345 */     stream.defaultReadObject();
/* 346 */     int keyCount = stream.readInt();
/* 347 */     if (keyCount < 0) {
/* 348 */       throw new InvalidObjectException("Invalid key count " + keyCount);
/*     */     }
/* 350 */     ImmutableMap.Builder<Object, ImmutableSet<Object>> builder = ImmutableMap.builder();
/*     */     
/* 352 */     int tmpSize = 0;
/*     */     
/* 354 */     for (int i = 0; i < keyCount; i++) {
/* 355 */       Object key = stream.readObject();
/* 356 */       int valueCount = stream.readInt();
/* 357 */       if (valueCount <= 0) {
/* 358 */         throw new InvalidObjectException("Invalid value count " + valueCount);
/*     */       }
/*     */       
/* 361 */       Object[] array = new Object[valueCount];
/* 362 */       for (int j = 0; j < valueCount; j++) {
/* 363 */         array[j] = stream.readObject();
/*     */       }
/* 365 */       ImmutableSet<Object> valueSet = ImmutableSet.of(array);
/* 366 */       if (valueSet.size() != array.length) {
/* 367 */         throw new InvalidObjectException("Duplicate key-value pairs exist for key " + key);
/*     */       }
/*     */       
/* 370 */       builder.put(key, valueSet);
/* 371 */       tmpSize += valueCount;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 376 */       tmpMap = builder.build();
/* 377 */     } catch (IllegalArgumentException e) {
/* 378 */       throw (InvalidObjectException)(new InvalidObjectException(e.getMessage())).initCause(e);
/*     */     } 
/*     */ 
/*     */     
/* 382 */     ImmutableMultimap.FieldSettersHolder.MAP_FIELD_SETTER.set(this, tmpMap);
/* 383 */     ImmutableMultimap.FieldSettersHolder.SIZE_FIELD_SETTER.set(this, tmpSize);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ImmutableSetMultimap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */