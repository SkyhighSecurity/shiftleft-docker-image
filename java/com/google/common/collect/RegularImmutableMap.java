/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ final class RegularImmutableMap<K, V>
/*     */   extends ImmutableMap<K, V>
/*     */ {
/*     */   private final transient Map.Entry<K, V>[] entries;
/*     */   private final transient Object[] table;
/*     */   private final transient int mask;
/*     */   private final transient int keySetHashCode;
/*     */   private transient ImmutableSet<Map.Entry<K, V>> entrySet;
/*     */   private transient ImmutableSet<K> keySet;
/*     */   private transient ImmutableCollection<V> values;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   RegularImmutableMap(Map.Entry<?, ?>... immutableEntries) {
/*  41 */     Map.Entry[] arrayOfEntry = (Map.Entry[])immutableEntries;
/*  42 */     this.entries = (Map.Entry<K, V>[])arrayOfEntry;
/*     */     
/*  44 */     int tableSize = Hashing.chooseTableSize(immutableEntries.length);
/*  45 */     this.table = new Object[tableSize * 2];
/*  46 */     this.mask = tableSize - 1;
/*     */     
/*  48 */     int keySetHashCodeMutable = 0; Map.Entry<K, V>[] arrayOfEntry1; int len$, i$;
/*  49 */     for (arrayOfEntry1 = this.entries, len$ = arrayOfEntry1.length, i$ = 0; i$ < len$; ) { Map.Entry<K, V> entry = arrayOfEntry1[i$];
/*  50 */       K key = entry.getKey();
/*  51 */       int keyHashCode = key.hashCode();
/*  52 */       int i = Hashing.smear(keyHashCode); for (;; i$++) {
/*  53 */         int index = (i & this.mask) * 2;
/*  54 */         Object existing = this.table[index];
/*  55 */         if (existing == null) {
/*  56 */           V value = entry.getValue();
/*  57 */           this.table[index] = key;
/*  58 */           this.table[index + 1] = value;
/*  59 */           keySetHashCodeMutable += keyHashCode;
/*     */         } else {
/*  61 */           if (existing.equals(key))
/*  62 */             throw new IllegalArgumentException("duplicate key: " + key);  i++;
/*     */         } 
/*     */       }  }
/*     */     
/*  66 */     this.keySetHashCode = keySetHashCodeMutable;
/*     */   }
/*     */   
/*     */   public V get(Object key) {
/*  70 */     if (key == null) {
/*  71 */       return null;
/*     */     }
/*  73 */     for (int i = Hashing.smear(key.hashCode());; i++) {
/*  74 */       int index = (i & this.mask) * 2;
/*  75 */       Object candidate = this.table[index];
/*  76 */       if (candidate == null) {
/*  77 */         return null;
/*     */       }
/*  79 */       if (candidate.equals(key)) {
/*     */ 
/*     */         
/*  82 */         V value = (V)this.table[index + 1];
/*  83 */         return value;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int size() {
/*  89 */     return this.entries.length;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  93 */     return false;
/*     */   }
/*     */   
/*     */   public boolean containsValue(Object value) {
/*  97 */     if (value == null) {
/*  98 */       return false;
/*     */     }
/* 100 */     for (Map.Entry<K, V> entry : this.entries) {
/* 101 */       if (entry.getValue().equals(value)) {
/* 102 */         return true;
/*     */       }
/*     */     } 
/* 105 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<Map.Entry<K, V>> entrySet() {
/* 116 */     ImmutableSet<Map.Entry<K, V>> es = this.entrySet;
/* 117 */     return (es == null) ? (this.entrySet = new EntrySet<K, V>(this)) : es;
/*     */   }
/*     */   
/*     */   private static class EntrySet<K, V>
/*     */     extends ImmutableSet.ArrayImmutableSet<Map.Entry<K, V>> {
/*     */     final transient RegularImmutableMap<K, V> map;
/*     */     
/*     */     EntrySet(RegularImmutableMap<K, V> map) {
/* 125 */       super((Object[])map.entries);
/* 126 */       this.map = map;
/*     */     }
/*     */     
/*     */     public boolean contains(Object target) {
/* 130 */       if (target instanceof Map.Entry) {
/* 131 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)target;
/* 132 */         V mappedValue = this.map.get(entry.getKey());
/* 133 */         return (mappedValue != null && mappedValue.equals(entry.getValue()));
/*     */       } 
/* 135 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<K> keySet() {
/* 142 */     ImmutableSet<K> ks = this.keySet;
/* 143 */     return (ks == null) ? (this.keySet = new KeySet<K, V>(this)) : ks;
/*     */   }
/*     */   
/*     */   private static class KeySet<K, V>
/*     */     extends ImmutableSet.TransformedImmutableSet<Map.Entry<K, V>, K>
/*     */   {
/*     */     final RegularImmutableMap<K, V> map;
/*     */     
/*     */     KeySet(RegularImmutableMap<K, V> map) {
/* 152 */       super((Map.Entry<K, V>[])map.entries, map.keySetHashCode);
/* 153 */       this.map = map;
/*     */     }
/*     */     
/*     */     K transform(Map.Entry<K, V> element) {
/* 157 */       return element.getKey();
/*     */     }
/*     */     
/*     */     public boolean contains(Object target) {
/* 161 */       return this.map.containsKey(target);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> values() {
/* 168 */     ImmutableCollection<V> v = this.values;
/* 169 */     return (v == null) ? (this.values = new Values<V>(this)) : v;
/*     */   }
/*     */   
/*     */   private static class Values<V>
/*     */     extends ImmutableCollection<V> {
/*     */     final RegularImmutableMap<?, V> map;
/*     */     
/*     */     Values(RegularImmutableMap<?, V> map) {
/* 177 */       this.map = map;
/*     */     }
/*     */     
/*     */     public int size() {
/* 181 */       return this.map.entries.length;
/*     */     }
/*     */     
/*     */     public UnmodifiableIterator<V> iterator() {
/* 185 */       return new AbstractIterator<V>() {
/* 186 */           int index = 0;
/*     */           protected V computeNext() {
/* 188 */             return (this.index < RegularImmutableMap.Values.this.map.entries.length) ? RegularImmutableMap.Values.this.map.entries[this.index++].getValue() : endOfData();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 196 */       return this.map.containsValue(target);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/* 201 */     StringBuilder result = (new StringBuilder(size() * 16)).append('{');
/* 202 */     Collections2.standardJoiner.appendTo(result, (Object[])this.entries);
/* 203 */     return result.append('}').toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\RegularImmutableMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */