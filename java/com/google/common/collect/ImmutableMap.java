/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ public abstract class ImmutableMap<K, V>
/*     */   implements Map<K, V>, Serializable
/*     */ {
/*     */   public static <K, V> ImmutableMap<K, V> of() {
/*  61 */     return EmptyImmutableMap.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1) {
/*  71 */     return new SingletonImmutableMap<K, V>((K)Preconditions.checkNotNull(k1), (V)Preconditions.checkNotNull(v1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2) {
/*  81 */     return new RegularImmutableMap<K, V>((Map.Entry<?, ?>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/*  91 */     return new RegularImmutableMap<K, V>((Map.Entry<?, ?>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 102 */     return new RegularImmutableMap<K, V>((Map.Entry<?, ?>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 113 */     return new RegularImmutableMap<K, V>((Map.Entry<?, ?>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 124 */     return new Builder<K, V>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> Map.Entry<K, V> entryOf(K key, V value) {
/* 135 */     return Maps.immutableEntry((K)Preconditions.checkNotNull(key), (V)Preconditions.checkNotNull(value));
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
/*     */   public static class Builder<K, V>
/*     */   {
/* 157 */     final List<Map.Entry<K, V>> entries = Lists.newArrayList();
/*     */ 
/*     */ 
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
/* 170 */       this.entries.add(ImmutableMap.entryOf(key, value));
/* 171 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
/* 181 */       for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 182 */         put(entry.getKey(), entry.getValue());
/*     */       }
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
/*     */     
/*     */     public ImmutableMap<K, V> build() {
/* 196 */       return fromEntryList(this.entries);
/*     */     }
/*     */ 
/*     */     
/*     */     private static <K, V> ImmutableMap<K, V> fromEntryList(List<Map.Entry<K, V>> entries) {
/* 201 */       int size = entries.size();
/* 202 */       switch (size) {
/*     */         case 0:
/* 204 */           return ImmutableMap.of();
/*     */         case 1:
/* 206 */           return new SingletonImmutableMap<K, V>(Iterables.<Map.Entry<K, V>>getOnlyElement(entries));
/*     */       } 
/* 208 */       Map.Entry[] arrayOfEntry = entries.<Map.Entry>toArray(new Map.Entry[entries.size()]);
/*     */       
/* 210 */       return new RegularImmutableMap<K, V>((Map.Entry<?, ?>[])arrayOfEntry);
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
/*     */   public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
/* 229 */     if (map instanceof ImmutableMap && !(map instanceof ImmutableSortedMap)) {
/*     */       
/* 231 */       ImmutableMap<K, V> kvMap = (ImmutableMap)map;
/* 232 */       return kvMap;
/*     */     } 
/*     */ 
/*     */     
/* 236 */     Map.Entry[] arrayOfEntry = (Map.Entry[])map.entrySet().toArray((Object[])new Map.Entry[0]);
/* 237 */     switch (arrayOfEntry.length) {
/*     */       case 0:
/* 239 */         return of();
/*     */       case 1:
/* 241 */         return new SingletonImmutableMap<K, V>(entryOf(arrayOfEntry[0].getKey(), arrayOfEntry[0].getValue()));
/*     */     } 
/*     */     
/* 244 */     for (int i = 0; i < arrayOfEntry.length; i++) {
/* 245 */       K k = arrayOfEntry[i].getKey();
/* 246 */       V v = arrayOfEntry[i].getValue();
/* 247 */       arrayOfEntry[i] = entryOf(k, v);
/*     */     } 
/* 249 */     return new RegularImmutableMap<K, V>((Map.Entry<?, ?>[])arrayOfEntry);
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
/*     */   public final V put(K k, V v) {
/* 261 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final V remove(Object o) {
/* 270 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void putAll(Map<? extends K, ? extends V> map) {
/* 279 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void clear() {
/* 288 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 292 */     return (size() == 0);
/*     */   }
/*     */   
/*     */   public boolean containsKey(@Nullable Object key) {
/* 296 */     return (get(key) != null);
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
/*     */   public boolean equals(@Nullable Object object) {
/* 324 */     if (object == this) {
/* 325 */       return true;
/*     */     }
/* 327 */     if (object instanceof Map) {
/* 328 */       Map<?, ?> that = (Map<?, ?>)object;
/* 329 */       return entrySet().equals(that.entrySet());
/*     */     } 
/* 331 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 337 */     return entrySet().hashCode();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 341 */     StringBuilder result = (new StringBuilder(size() * 16)).append('{');
/* 342 */     Maps.standardJoiner.appendTo(result, this);
/* 343 */     return result.append('}').toString();
/*     */   }
/*     */ 
/*     */   
/*     */   static class SerializedForm
/*     */     implements Serializable
/*     */   {
/*     */     private final Object[] keys;
/*     */     private final Object[] values;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(ImmutableMap<?, ?> map) {
/* 355 */       this.keys = new Object[map.size()];
/* 356 */       this.values = new Object[map.size()];
/* 357 */       int i = 0;
/* 358 */       for (Map.Entry<?, ?> entry : map.entrySet()) {
/* 359 */         this.keys[i] = entry.getKey();
/* 360 */         this.values[i] = entry.getValue();
/* 361 */         i++;
/*     */       } 
/*     */     }
/*     */     Object readResolve() {
/* 365 */       ImmutableMap.Builder<Object, Object> builder = new ImmutableMap.Builder<Object, Object>();
/* 366 */       return createMap(builder);
/*     */     }
/*     */     Object createMap(ImmutableMap.Builder<Object, Object> builder) {
/* 369 */       for (int i = 0; i < this.keys.length; i++) {
/* 370 */         builder.put(this.keys[i], this.values[i]);
/*     */       }
/* 372 */       return builder.build();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 378 */     return new SerializedForm(this);
/*     */   }
/*     */   
/*     */   public abstract boolean containsValue(@Nullable Object paramObject);
/*     */   
/*     */   public abstract V get(@Nullable Object paramObject);
/*     */   
/*     */   public abstract ImmutableSet<Map.Entry<K, V>> entrySet();
/*     */   
/*     */   public abstract ImmutableSet<K> keySet();
/*     */   
/*     */   public abstract ImmutableCollection<V> values();
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ImmutableMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */