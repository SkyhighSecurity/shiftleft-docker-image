/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ final class SingletonImmutableMap<K, V>
/*     */   extends ImmutableMap<K, V>
/*     */ {
/*     */   final transient K singleKey;
/*     */   final transient V singleValue;
/*     */   private transient Map.Entry<K, V> entry;
/*     */   private transient ImmutableSet<Map.Entry<K, V>> entrySet;
/*     */   private transient ImmutableSet<K> keySet;
/*     */   private transient ImmutableCollection<V> values;
/*     */   
/*     */   SingletonImmutableMap(K singleKey, V singleValue) {
/*  40 */     this.singleKey = singleKey;
/*  41 */     this.singleValue = singleValue;
/*     */   }
/*     */   
/*     */   SingletonImmutableMap(Map.Entry<K, V> entry) {
/*  45 */     this.entry = entry;
/*  46 */     this.singleKey = entry.getKey();
/*  47 */     this.singleValue = entry.getValue();
/*     */   }
/*     */   
/*     */   private Map.Entry<K, V> entry() {
/*  51 */     Map.Entry<K, V> e = this.entry;
/*  52 */     return (e == null) ? (this.entry = Maps.<K, V>immutableEntry(this.singleKey, this.singleValue)) : e;
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/*  57 */     return this.singleKey.equals(key) ? this.singleValue : null;
/*     */   }
/*     */   
/*     */   public int size() {
/*  61 */     return 1;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  65 */     return false;
/*     */   }
/*     */   
/*     */   public boolean containsKey(Object key) {
/*  69 */     return this.singleKey.equals(key);
/*     */   }
/*     */   
/*     */   public boolean containsValue(Object value) {
/*  73 */     return this.singleValue.equals(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<Map.Entry<K, V>> entrySet() {
/*  79 */     ImmutableSet<Map.Entry<K, V>> es = this.entrySet;
/*  80 */     return (es == null) ? (this.entrySet = ImmutableSet.of(entry())) : es;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<K> keySet() {
/*  86 */     ImmutableSet<K> ks = this.keySet;
/*  87 */     return (ks == null) ? (this.keySet = ImmutableSet.of(this.singleKey)) : ks;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> values() {
/*  93 */     ImmutableCollection<V> v = this.values;
/*  94 */     return (v == null) ? (this.values = new Values<V>(this.singleValue)) : v;
/*     */   }
/*     */   
/*     */   private static class Values<V>
/*     */     extends ImmutableCollection<V> {
/*     */     final V singleValue;
/*     */     
/*     */     Values(V singleValue) {
/* 102 */       this.singleValue = singleValue;
/*     */     }
/*     */     
/*     */     public boolean contains(Object object) {
/* 106 */       return this.singleValue.equals(object);
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 110 */       return false;
/*     */     }
/*     */     
/*     */     public int size() {
/* 114 */       return 1;
/*     */     }
/*     */     
/*     */     public UnmodifiableIterator<V> iterator() {
/* 118 */       return Iterators.singletonIterator(this.singleValue);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 123 */     if (object == this) {
/* 124 */       return true;
/*     */     }
/* 126 */     if (object instanceof Map) {
/* 127 */       Map<?, ?> that = (Map<?, ?>)object;
/* 128 */       if (that.size() != 1) {
/* 129 */         return false;
/*     */       }
/* 131 */       Map.Entry<?, ?> entry = that.entrySet().iterator().next();
/* 132 */       return (this.singleKey.equals(entry.getKey()) && this.singleValue.equals(entry.getValue()));
/*     */     } 
/*     */     
/* 135 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 139 */     return this.singleKey.hashCode() ^ this.singleValue.hashCode();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 143 */     return '{' + this.singleKey.toString() + '=' + this.singleValue.toString() + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\SingletonImmutableMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */