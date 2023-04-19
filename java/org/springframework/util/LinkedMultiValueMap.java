/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LinkedMultiValueMap<K, V>
/*     */   implements MultiValueMap<K, V>, Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = 3801124242820219131L;
/*     */   private final Map<K, List<V>> targetMap;
/*     */   
/*     */   public LinkedMultiValueMap() {
/*  49 */     this.targetMap = new LinkedHashMap<K, List<V>>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedMultiValueMap(int initialCapacity) {
/*  58 */     this.targetMap = new LinkedHashMap<K, List<V>>(initialCapacity);
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
/*     */   public LinkedMultiValueMap(Map<K, List<V>> otherMap) {
/*  70 */     this.targetMap = new LinkedHashMap<K, List<V>>(otherMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(K key, V value) {
/*  78 */     List<V> values = this.targetMap.get(key);
/*  79 */     if (values == null) {
/*  80 */       values = new LinkedList<V>();
/*  81 */       this.targetMap.put(key, values);
/*     */     } 
/*  83 */     values.add(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public V getFirst(K key) {
/*  88 */     List<V> values = this.targetMap.get(key);
/*  89 */     return (values != null) ? values.get(0) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(K key, V value) {
/*  94 */     List<V> values = new LinkedList<V>();
/*  95 */     values.add(value);
/*  96 */     this.targetMap.put(key, values);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAll(Map<K, V> values) {
/* 101 */     for (Map.Entry<K, V> entry : values.entrySet()) {
/* 102 */       set(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<K, V> toSingleValueMap() {
/* 108 */     LinkedHashMap<K, V> singleValueMap = new LinkedHashMap<K, V>(this.targetMap.size());
/* 109 */     for (Map.Entry<K, List<V>> entry : this.targetMap.entrySet()) {
/* 110 */       singleValueMap.put(entry.getKey(), ((List<V>)entry.getValue()).get(0));
/*     */     }
/* 112 */     return singleValueMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 120 */     return this.targetMap.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 125 */     return this.targetMap.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 130 */     return this.targetMap.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 135 */     return this.targetMap.containsValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<V> get(Object key) {
/* 140 */     return this.targetMap.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<V> put(K key, List<V> value) {
/* 145 */     return this.targetMap.put(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<V> remove(Object key) {
/* 150 */     return this.targetMap.remove(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends List<V>> map) {
/* 155 */     this.targetMap.putAll(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 160 */     this.targetMap.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 165 */     return this.targetMap.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<List<V>> values() {
/* 170 */     return this.targetMap.values();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, List<V>>> entrySet() {
/* 175 */     return this.targetMap.entrySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedMultiValueMap<K, V> deepCopy() {
/* 186 */     LinkedMultiValueMap<K, V> copy = new LinkedMultiValueMap(this.targetMap.size());
/* 187 */     for (Map.Entry<K, List<V>> entry : this.targetMap.entrySet()) {
/* 188 */       copy.put(entry.getKey(), new LinkedList<V>(entry.getValue()));
/*     */     }
/* 190 */     return copy;
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
/*     */   public LinkedMultiValueMap<K, V> clone() {
/* 202 */     return new LinkedMultiValueMap(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 207 */     return this.targetMap.equals(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 212 */     return this.targetMap.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 217 */     return this.targetMap.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\LinkedMultiValueMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */