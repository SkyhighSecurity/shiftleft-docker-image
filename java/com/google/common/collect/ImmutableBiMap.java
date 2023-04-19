/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ public abstract class ImmutableBiMap<K, V>
/*     */   extends ImmutableMap<K, V>
/*     */   implements BiMap<K, V>
/*     */ {
/*  45 */   private static final ImmutableBiMap<Object, Object> EMPTY_IMMUTABLE_BIMAP = new EmptyBiMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of() {
/*  54 */     return (ImmutableBiMap)EMPTY_IMMUTABLE_BIMAP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1) {
/*  61 */     return new RegularImmutableBiMap<K, V>(ImmutableMap.of(k1, v1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2) {
/*  70 */     return new RegularImmutableBiMap<K, V>(ImmutableMap.of(k1, v1, k2, v2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/*  80 */     return new RegularImmutableBiMap<K, V>(ImmutableMap.of(k1, v1, k2, v2, k3, v3));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/*  91 */     return new RegularImmutableBiMap<K, V>(ImmutableMap.of(k1, v1, k2, v2, k3, v3, k4, v4));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 102 */     return new RegularImmutableBiMap<K, V>(ImmutableMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
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
/* 113 */     return new Builder<K, V>();
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Builder<K, V>
/*     */     extends ImmutableMap.Builder<K, V>
/*     */   {
/*     */     public Builder<K, V> put(K key, V value) {
/* 147 */       super.put(key, value);
/* 148 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
/* 159 */       super.putAll(map);
/* 160 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableBiMap<K, V> build() {
/* 169 */       ImmutableMap<K, V> map = super.build();
/* 170 */       if (map.isEmpty()) {
/* 171 */         return ImmutableBiMap.of();
/*     */       }
/* 173 */       return new RegularImmutableBiMap<K, V>(super.build());
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
/*     */   public static <K, V> ImmutableBiMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
/* 191 */     if (map instanceof ImmutableBiMap) {
/*     */       
/* 193 */       ImmutableBiMap<K, V> bimap = (ImmutableBiMap)map;
/* 194 */       return bimap;
/*     */     } 
/*     */     
/* 197 */     if (map.isEmpty()) {
/* 198 */       return of();
/*     */     }
/*     */     
/* 201 */     ImmutableMap<K, V> immutableMap = ImmutableMap.copyOf(map);
/* 202 */     return new RegularImmutableBiMap<K, V>(immutableMap);
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
/*     */   public boolean containsKey(@Nullable Object key) {
/* 218 */     return delegate().containsKey(key);
/*     */   }
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/* 222 */     return inverse().containsKey(value);
/*     */   }
/*     */   
/*     */   public ImmutableSet<Map.Entry<K, V>> entrySet() {
/* 226 */     return delegate().entrySet();
/*     */   }
/*     */   
/*     */   public V get(@Nullable Object key) {
/* 230 */     return delegate().get(key);
/*     */   }
/*     */   
/*     */   public ImmutableSet<K> keySet() {
/* 234 */     return delegate().keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<V> values() {
/* 242 */     return inverse().keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V forcePut(K key, V value) {
/* 251 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 255 */     return delegate().isEmpty();
/*     */   }
/*     */   
/*     */   public int size() {
/* 259 */     return delegate().size();
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 263 */     return (object == this || delegate().equals(object));
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 267 */     return delegate().hashCode();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 271 */     return delegate().toString();
/*     */   }
/*     */   
/*     */   static class EmptyBiMap
/*     */     extends ImmutableBiMap<Object, Object>
/*     */   {
/*     */     ImmutableMap<Object, Object> delegate() {
/* 278 */       return ImmutableMap.of();
/*     */     }
/*     */     public ImmutableBiMap<Object, Object> inverse() {
/* 281 */       return this;
/*     */     }
/*     */     Object readResolve() {
/* 284 */       return ImmutableBiMap.EMPTY_IMMUTABLE_BIMAP;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SerializedForm
/*     */     extends ImmutableMap.SerializedForm
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */     
/*     */     SerializedForm(ImmutableBiMap<?, ?> bimap) {
/* 299 */       super(bimap);
/*     */     }
/*     */     Object readResolve() {
/* 302 */       ImmutableBiMap.Builder<Object, Object> builder = new ImmutableBiMap.Builder<Object, Object>();
/* 303 */       return createMap(builder);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 309 */     return new SerializedForm(this);
/*     */   }
/*     */   
/*     */   abstract ImmutableMap<K, V> delegate();
/*     */   
/*     */   public abstract ImmutableBiMap<V, K> inverse();
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ImmutableBiMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */