/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ public class ImmutableListMultimap<K, V>
/*     */   extends ImmutableMultimap<K, V>
/*     */   implements ListMultimap<K, V>
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of() {
/*  57 */     return EmptyImmutableListMultimap.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1) {
/*  64 */     Builder<K, V> builder = builder();
/*     */     
/*  66 */     builder.put(k1, v1);
/*  67 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2) {
/*  74 */     Builder<K, V> builder = builder();
/*     */     
/*  76 */     builder.put(k1, v1);
/*  77 */     builder.put(k2, v2);
/*  78 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/*  86 */     Builder<K, V> builder = builder();
/*     */     
/*  88 */     builder.put(k1, v1);
/*  89 */     builder.put(k2, v2);
/*  90 */     builder.put(k3, v3);
/*  91 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/*  99 */     Builder<K, V> builder = builder();
/*     */     
/* 101 */     builder.put(k1, v1);
/* 102 */     builder.put(k2, v2);
/* 103 */     builder.put(k3, v3);
/* 104 */     builder.put(k4, v4);
/* 105 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 113 */     Builder<K, V> builder = builder();
/*     */     
/* 115 */     builder.put(k1, v1);
/* 116 */     builder.put(k2, v2);
/* 117 */     builder.put(k3, v3);
/* 118 */     builder.put(k4, v4);
/* 119 */     builder.put(k5, v5);
/* 120 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 130 */     return new Builder<K, V>();
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
/*     */     public Builder<K, V> put(K key, V value) {
/* 161 */       super.put(key, value);
/* 162 */       return this;
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
/* 173 */       super.putAll(key, values);
/* 174 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> putAll(K key, V... values) {
/* 184 */       super.putAll(key, values);
/* 185 */       return this;
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
/* 199 */       super.putAll(multimap);
/* 200 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableListMultimap<K, V> build() {
/* 207 */       return (ImmutableListMultimap<K, V>)super.build();
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
/*     */   public static <K, V> ImmutableListMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
/* 225 */     if (multimap.isEmpty()) {
/* 226 */       return of();
/*     */     }
/*     */     
/* 229 */     if (multimap instanceof ImmutableListMultimap) {
/*     */       
/* 231 */       ImmutableListMultimap<K, V> kvMultimap = (ImmutableListMultimap)multimap;
/*     */       
/* 233 */       return kvMultimap;
/*     */     } 
/*     */     
/* 236 */     ImmutableMap.Builder<K, ImmutableList<V>> builder = ImmutableMap.builder();
/* 237 */     int size = 0;
/*     */ 
/*     */     
/* 240 */     for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : (Iterable<Map.Entry<? extends K, ? extends Collection<? extends V>>>)multimap.asMap().entrySet()) {
/* 241 */       ImmutableList<V> list = ImmutableList.copyOf(entry.getValue());
/* 242 */       if (!list.isEmpty()) {
/* 243 */         builder.put(entry.getKey(), list);
/* 244 */         size += list.size();
/*     */       } 
/*     */     } 
/*     */     
/* 248 */     return new ImmutableListMultimap<K, V>(builder.build(), size);
/*     */   }
/*     */   
/*     */   ImmutableListMultimap(ImmutableMap<K, ImmutableList<V>> map, int size) {
/* 252 */     super((ImmutableMap)map, size);
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
/*     */   public ImmutableList<V> get(@Nullable K key) {
/* 265 */     ImmutableList<V> list = (ImmutableList<V>)this.map.get(key);
/* 266 */     return (list == null) ? ImmutableList.<V>of() : list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<V> removeAll(Object key) {
/* 275 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<V> replaceValues(K key, Iterable<? extends V> values) {
/* 285 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 293 */     stream.defaultWriteObject();
/* 294 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*     */     ImmutableMap<Object, ImmutableList<Object>> tmpMap;
/* 299 */     stream.defaultReadObject();
/* 300 */     int keyCount = stream.readInt();
/* 301 */     if (keyCount < 0) {
/* 302 */       throw new InvalidObjectException("Invalid key count " + keyCount);
/*     */     }
/* 304 */     ImmutableMap.Builder<Object, ImmutableList<Object>> builder = ImmutableMap.builder();
/*     */     
/* 306 */     int tmpSize = 0;
/*     */     
/* 308 */     for (int i = 0; i < keyCount; i++) {
/* 309 */       Object key = stream.readObject();
/* 310 */       int valueCount = stream.readInt();
/* 311 */       if (valueCount <= 0) {
/* 312 */         throw new InvalidObjectException("Invalid value count " + valueCount);
/*     */       }
/*     */       
/* 315 */       Object[] array = new Object[valueCount];
/* 316 */       for (int j = 0; j < valueCount; j++) {
/* 317 */         array[j] = stream.readObject();
/*     */       }
/* 319 */       builder.put(key, ImmutableList.of(array));
/* 320 */       tmpSize += valueCount;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 325 */       tmpMap = builder.build();
/* 326 */     } catch (IllegalArgumentException e) {
/* 327 */       throw (InvalidObjectException)(new InvalidObjectException(e.getMessage())).initCause(e);
/*     */     } 
/*     */ 
/*     */     
/* 331 */     ImmutableMultimap.FieldSettersHolder.MAP_FIELD_SETTER.set(this, tmpMap);
/* 332 */     ImmutableMultimap.FieldSettersHolder.SIZE_FIELD_SETTER.set(this, tmpSize);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ImmutableListMultimap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */