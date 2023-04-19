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
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingMultimap<K, V>
/*     */   extends ForwardingObject
/*     */   implements Multimap<K, V>
/*     */ {
/*     */   public Map<K, Collection<V>> asMap() {
/*  44 */     return delegate().asMap();
/*     */   }
/*     */   
/*     */   public void clear() {
/*  48 */     delegate().clear();
/*     */   }
/*     */   
/*     */   public boolean containsEntry(@Nullable Object key, @Nullable Object value) {
/*  52 */     return delegate().containsEntry(key, value);
/*     */   }
/*     */   
/*     */   public boolean containsKey(@Nullable Object key) {
/*  56 */     return delegate().containsKey(key);
/*     */   }
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/*  60 */     return delegate().containsValue(value);
/*     */   }
/*     */   
/*     */   public Collection<Map.Entry<K, V>> entries() {
/*  64 */     return delegate().entries();
/*     */   }
/*     */   
/*     */   public Collection<V> get(@Nullable K key) {
/*  68 */     return delegate().get(key);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  72 */     return delegate().isEmpty();
/*     */   }
/*     */   
/*     */   public Multiset<K> keys() {
/*  76 */     return delegate().keys();
/*     */   }
/*     */   
/*     */   public Set<K> keySet() {
/*  80 */     return delegate().keySet();
/*     */   }
/*     */   
/*     */   public boolean put(K key, V value) {
/*  84 */     return delegate().put(key, value);
/*     */   }
/*     */   
/*     */   public boolean putAll(K key, Iterable<? extends V> values) {
/*  88 */     return delegate().putAll(key, values);
/*     */   }
/*     */   
/*     */   public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/*  92 */     return delegate().putAll(multimap);
/*     */   }
/*     */   
/*     */   public boolean remove(@Nullable Object key, @Nullable Object value) {
/*  96 */     return delegate().remove(key, value);
/*     */   }
/*     */   
/*     */   public Collection<V> removeAll(@Nullable Object key) {
/* 100 */     return delegate().removeAll(key);
/*     */   }
/*     */   
/*     */   public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
/* 104 */     return delegate().replaceValues(key, values);
/*     */   }
/*     */   
/*     */   public int size() {
/* 108 */     return delegate().size();
/*     */   }
/*     */   
/*     */   public Collection<V> values() {
/* 112 */     return delegate().values();
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 116 */     return (object == this || delegate().equals(object));
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 120 */     return delegate().hashCode();
/*     */   }
/*     */   
/*     */   protected abstract Multimap<K, V> delegate();
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ForwardingMultimap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */