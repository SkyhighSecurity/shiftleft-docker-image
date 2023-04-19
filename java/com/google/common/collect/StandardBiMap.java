/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ class StandardBiMap<K, V>
/*     */   extends ForwardingMap<K, V>
/*     */   implements BiMap<K, V>, Serializable
/*     */ {
/*     */   private transient Map<K, V> delegate;
/*     */   private transient StandardBiMap<V, K> inverse;
/*     */   private volatile transient Set<K> keySet;
/*     */   private volatile transient Set<V> valueSet;
/*     */   private volatile transient Set<Map.Entry<K, V>> entrySet;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   StandardBiMap(Map<K, V> forward, Map<V, K> backward) {
/*  51 */     setDelegates(forward, backward);
/*     */   }
/*     */ 
/*     */   
/*     */   private StandardBiMap(Map<K, V> backward, StandardBiMap<V, K> forward) {
/*  56 */     this.delegate = backward;
/*  57 */     this.inverse = forward;
/*     */   }
/*     */   
/*     */   protected Map<K, V> delegate() {
/*  61 */     return this.delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setDelegates(Map<K, V> forward, Map<V, K> backward) {
/*  69 */     Preconditions.checkState((this.delegate == null));
/*  70 */     Preconditions.checkState((this.inverse == null));
/*  71 */     Preconditions.checkArgument(forward.isEmpty());
/*  72 */     Preconditions.checkArgument(backward.isEmpty());
/*  73 */     Preconditions.checkArgument((forward != backward));
/*  74 */     this.delegate = forward;
/*  75 */     this.inverse = new Inverse<V, K>(backward, this);
/*     */   }
/*     */   
/*     */   void setInverse(StandardBiMap<V, K> inverse) {
/*  79 */     this.inverse = inverse;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/*  85 */     return this.inverse.containsKey(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V put(K key, V value) {
/*  91 */     return putInBothMaps(key, value, false);
/*     */   }
/*     */   
/*     */   public V forcePut(K key, V value) {
/*  95 */     return putInBothMaps(key, value, true);
/*     */   }
/*     */   
/*     */   private V putInBothMaps(@Nullable K key, @Nullable V value, boolean force) {
/*  99 */     boolean containedKey = containsKey(key);
/* 100 */     if (containedKey && Objects.equal(value, get(key))) {
/* 101 */       return value;
/*     */     }
/* 103 */     if (force) {
/* 104 */       inverse().remove(value);
/*     */     } else {
/* 106 */       Preconditions.checkArgument(!containsValue(value), "value already present: %s", new Object[] { value });
/*     */     } 
/* 108 */     V oldValue = this.delegate.put(key, value);
/* 109 */     updateInverseMap(key, containedKey, oldValue, value);
/* 110 */     return oldValue;
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateInverseMap(K key, boolean containedKey, V oldValue, V newValue) {
/* 115 */     if (containedKey) {
/* 116 */       removeFromInverseMap(oldValue);
/*     */     }
/* 118 */     this.inverse.delegate.put((K)newValue, (V)key);
/*     */   }
/*     */   
/*     */   public V remove(Object key) {
/* 122 */     return containsKey(key) ? removeFromBothMaps(key) : null;
/*     */   }
/*     */   
/*     */   private V removeFromBothMaps(Object key) {
/* 126 */     V oldValue = this.delegate.remove(key);
/* 127 */     removeFromInverseMap(oldValue);
/* 128 */     return oldValue;
/*     */   }
/*     */   
/*     */   private void removeFromInverseMap(V oldValue) {
/* 132 */     this.inverse.delegate.remove(oldValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> map) {
/* 138 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 139 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear() {
/* 144 */     this.delegate.clear();
/* 145 */     this.inverse.delegate.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BiMap<V, K> inverse() {
/* 151 */     return this.inverse;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 157 */     Set<K> result = this.keySet;
/* 158 */     return (result == null) ? (this.keySet = new KeySet()) : this.keySet;
/*     */   }
/*     */   
/*     */   private class KeySet extends ForwardingSet<K> {
/*     */     protected Set<K> delegate() {
/* 163 */       return StandardBiMap.this.delegate.keySet();
/*     */     }
/*     */     private KeySet() {}
/*     */     public void clear() {
/* 167 */       StandardBiMap.this.clear();
/*     */     }
/*     */     
/*     */     public boolean remove(Object key) {
/* 171 */       if (!contains(key)) {
/* 172 */         return false;
/*     */       }
/* 174 */       StandardBiMap.this.removeFromBothMaps(key);
/* 175 */       return true;
/*     */     }
/*     */     
/*     */     public boolean removeAll(Collection<?> keysToRemove) {
/* 179 */       return Iterators.removeAll(iterator(), keysToRemove);
/*     */     }
/*     */     
/*     */     public boolean retainAll(Collection<?> keysToRetain) {
/* 183 */       return Iterators.retainAll(iterator(), keysToRetain);
/*     */     }
/*     */     
/*     */     public Iterator<K> iterator() {
/* 187 */       final Iterator<Map.Entry<K, V>> iterator = StandardBiMap.this.delegate.entrySet().iterator();
/* 188 */       return new Iterator<K>() {
/*     */           Map.Entry<K, V> entry;
/*     */           
/*     */           public boolean hasNext() {
/* 192 */             return iterator.hasNext();
/*     */           }
/*     */           public K next() {
/* 195 */             this.entry = iterator.next();
/* 196 */             return this.entry.getKey();
/*     */           }
/*     */           public void remove() {
/* 199 */             iterator.remove();
/* 200 */             StandardBiMap.this.removeFromInverseMap(this.entry.getValue());
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<V> values() {
/* 213 */     Set<V> result = this.valueSet;
/* 214 */     return (result == null) ? (this.valueSet = new ValueSet()) : this.valueSet;
/*     */   }
/*     */   
/*     */   private class ValueSet extends ForwardingSet<V> {
/* 218 */     final Set<V> valuesDelegate = StandardBiMap.this.inverse.keySet();
/*     */     
/*     */     protected Set<V> delegate() {
/* 221 */       return this.valuesDelegate;
/*     */     }
/*     */     
/*     */     public Iterator<V> iterator() {
/* 225 */       final Iterator<V> iterator = StandardBiMap.this.delegate.values().iterator();
/* 226 */       return new Iterator<V>() {
/*     */           V valueToRemove;
/*     */           
/*     */           public boolean hasNext() {
/* 230 */             return iterator.hasNext();
/*     */           }
/*     */           
/*     */           public V next() {
/* 234 */             return this.valueToRemove = (V)iterator.next();
/*     */           }
/*     */           
/*     */           public void remove() {
/* 238 */             iterator.remove();
/* 239 */             StandardBiMap.this.removeFromInverseMap(this.valueToRemove);
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public Object[] toArray() {
/* 245 */       return ObjectArrays.toArrayImpl(this);
/*     */     }
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 249 */       return ObjectArrays.toArrayImpl(this, array);
/*     */     }
/*     */     
/*     */     public String toString() {
/* 253 */       return Iterators.toString(iterator());
/*     */     }
/*     */     
/*     */     private ValueSet() {}
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 260 */     Set<Map.Entry<K, V>> result = this.entrySet;
/* 261 */     return (result == null) ? (this.entrySet = new EntrySet()) : this.entrySet;
/*     */   }
/*     */   
/*     */   private class EntrySet extends ForwardingSet<Map.Entry<K, V>> {
/* 265 */     final Set<Map.Entry<K, V>> esDelegate = StandardBiMap.this.delegate.entrySet();
/*     */     
/*     */     protected Set<Map.Entry<K, V>> delegate() {
/* 268 */       return this.esDelegate;
/*     */     }
/*     */     
/*     */     public void clear() {
/* 272 */       StandardBiMap.this.clear();
/*     */     }
/*     */     
/*     */     public boolean remove(Object object) {
/* 276 */       if (!this.esDelegate.remove(object)) {
/* 277 */         return false;
/*     */       }
/* 279 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/* 280 */       StandardBiMap.this.inverse.delegate.remove(entry.getValue());
/* 281 */       return true;
/*     */     }
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 285 */       final Iterator<Map.Entry<K, V>> iterator = this.esDelegate.iterator();
/* 286 */       return new Iterator<Map.Entry<K, V>>() {
/*     */           Map.Entry<K, V> entry;
/*     */           
/*     */           public boolean hasNext() {
/* 290 */             return iterator.hasNext();
/*     */           }
/*     */           
/*     */           public Map.Entry<K, V> next() {
/* 294 */             this.entry = iterator.next();
/* 295 */             final Map.Entry<K, V> finalEntry = this.entry;
/*     */             
/* 297 */             return new ForwardingMapEntry<K, V>() {
/*     */                 protected Map.Entry<K, V> delegate() {
/* 299 */                   return finalEntry;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public V setValue(V value) {
/* 304 */                   Preconditions.checkState(StandardBiMap.EntrySet.this.contains(this), "entry no longer in map");
/*     */                   
/* 306 */                   if (Objects.equal(value, getValue())) {
/* 307 */                     return value;
/*     */                   }
/* 309 */                   Preconditions.checkArgument(!StandardBiMap.this.containsValue(value), "value already present: %s", new Object[] { value });
/*     */                   
/* 311 */                   V oldValue = (V)finalEntry.setValue(value);
/* 312 */                   Preconditions.checkState(Objects.equal(value, StandardBiMap.this.get(getKey())), "entry no longer in map");
/*     */                   
/* 314 */                   StandardBiMap.this.updateInverseMap(getKey(), true, oldValue, value);
/* 315 */                   return oldValue;
/*     */                 }
/*     */               };
/*     */           }
/*     */           
/*     */           public void remove() {
/* 321 */             iterator.remove();
/* 322 */             StandardBiMap.this.removeFromInverseMap(this.entry.getValue());
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 330 */       return ObjectArrays.toArrayImpl(this);
/*     */     }
/*     */     public <T> T[] toArray(T[] array) {
/* 333 */       return ObjectArrays.toArrayImpl(this, array);
/*     */     }
/*     */     public boolean contains(Object o) {
/* 336 */       return Maps.containsEntryImpl(delegate(), o);
/*     */     }
/*     */     public boolean containsAll(Collection<?> c) {
/* 339 */       return Collections2.containsAll(this, c);
/*     */     }
/*     */     public boolean removeAll(Collection<?> c) {
/* 342 */       return Iterators.removeAll(iterator(), c);
/*     */     }
/*     */     public boolean retainAll(Collection<?> c) {
/* 345 */       return Iterators.retainAll(iterator(), c);
/*     */     }
/*     */     
/*     */     private EntrySet() {} }
/*     */   
/*     */   private static class Inverse<K, V> extends StandardBiMap<K, V> {
/*     */     private Inverse(Map<K, V> backward, StandardBiMap<V, K> forward) {
/* 352 */       super(backward, forward);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void writeObject(ObjectOutputStream stream) throws IOException {
/* 368 */       stream.defaultWriteObject();
/* 369 */       stream.writeObject(inverse());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 375 */       stream.defaultReadObject();
/* 376 */       setInverse((StandardBiMap<V, K>)stream.readObject());
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 380 */       return inverse().inverse();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\StandardBiMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */