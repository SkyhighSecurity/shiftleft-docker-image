/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Locale;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LinkedCaseInsensitiveMap<V>
/*     */   implements Map<String, V>, Serializable, Cloneable
/*     */ {
/*     */   private final LinkedHashMap<String, V> targetMap;
/*     */   private final HashMap<String, String> caseInsensitiveKeys;
/*     */   private final Locale locale;
/*     */   
/*     */   public LinkedCaseInsensitiveMap() {
/*  55 */     this((Locale)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedCaseInsensitiveMap(Locale locale) {
/*  65 */     this(16, locale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedCaseInsensitiveMap(int initialCapacity) {
/*  76 */     this(initialCapacity, null);
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
/*     */   public LinkedCaseInsensitiveMap(int initialCapacity, Locale locale) {
/*  88 */     this.targetMap = new LinkedHashMap<String, V>(initialCapacity)
/*     */       {
/*     */         public boolean containsKey(Object key) {
/*  91 */           return LinkedCaseInsensitiveMap.this.containsKey(key);
/*     */         }
/*     */         
/*     */         protected boolean removeEldestEntry(Map.Entry<String, V> eldest) {
/*  95 */           boolean doRemove = LinkedCaseInsensitiveMap.this.removeEldestEntry(eldest);
/*  96 */           if (doRemove) {
/*  97 */             LinkedCaseInsensitiveMap.this.caseInsensitiveKeys.remove(LinkedCaseInsensitiveMap.this.convertKey(eldest.getKey()));
/*     */           }
/*  99 */           return doRemove;
/*     */         }
/*     */       };
/* 102 */     this.caseInsensitiveKeys = new HashMap<String, String>(initialCapacity);
/* 103 */     this.locale = (locale != null) ? locale : Locale.getDefault();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private LinkedCaseInsensitiveMap(LinkedCaseInsensitiveMap<V> other) {
/* 111 */     this.targetMap = (LinkedHashMap<String, V>)other.targetMap.clone();
/* 112 */     this.caseInsensitiveKeys = (HashMap<String, String>)other.caseInsensitiveKeys.clone();
/* 113 */     this.locale = other.locale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 121 */     return this.targetMap.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 126 */     return this.targetMap.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 131 */     return (key instanceof String && this.caseInsensitiveKeys.containsKey(convertKey((String)key)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 136 */     return this.targetMap.containsValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/* 141 */     if (key instanceof String) {
/* 142 */       String caseInsensitiveKey = this.caseInsensitiveKeys.get(convertKey((String)key));
/* 143 */       if (caseInsensitiveKey != null) {
/* 144 */         return this.targetMap.get(caseInsensitiveKey);
/*     */       }
/*     */     } 
/* 147 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public V getOrDefault(Object key, V defaultValue) {
/* 152 */     if (key instanceof String) {
/* 153 */       String caseInsensitiveKey = this.caseInsensitiveKeys.get(convertKey((String)key));
/* 154 */       if (caseInsensitiveKey != null) {
/* 155 */         return this.targetMap.get(caseInsensitiveKey);
/*     */       }
/*     */     } 
/* 158 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public V put(String key, V value) {
/* 163 */     String oldKey = this.caseInsensitiveKeys.put(convertKey(key), key);
/* 164 */     if (oldKey != null && !oldKey.equals(key)) {
/* 165 */       this.targetMap.remove(oldKey);
/*     */     }
/* 167 */     return this.targetMap.put(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends String, ? extends V> map) {
/* 172 */     if (map.isEmpty()) {
/*     */       return;
/*     */     }
/* 175 */     for (Map.Entry<? extends String, ? extends V> entry : map.entrySet()) {
/* 176 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public V remove(Object key) {
/* 182 */     if (key instanceof String) {
/* 183 */       String caseInsensitiveKey = this.caseInsensitiveKeys.remove(convertKey((String)key));
/* 184 */       if (caseInsensitiveKey != null) {
/* 185 */         return this.targetMap.remove(caseInsensitiveKey);
/*     */       }
/*     */     } 
/* 188 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 193 */     this.caseInsensitiveKeys.clear();
/* 194 */     this.targetMap.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> keySet() {
/* 199 */     return this.targetMap.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 204 */     return this.targetMap.values();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<String, V>> entrySet() {
/* 209 */     return this.targetMap.entrySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public LinkedCaseInsensitiveMap<V> clone() {
/* 214 */     return new LinkedCaseInsensitiveMap(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 219 */     return this.targetMap.equals(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 224 */     return this.targetMap.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 229 */     return this.targetMap.toString();
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
/*     */   public Locale getLocale() {
/* 243 */     return this.locale;
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
/*     */   protected String convertKey(String key) {
/* 255 */     return key.toLowerCase(getLocale());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean removeEldestEntry(Map.Entry<String, V> eldest) {
/* 265 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\LinkedCaseInsensitiveMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */