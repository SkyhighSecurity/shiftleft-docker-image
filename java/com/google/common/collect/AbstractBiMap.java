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
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ abstract class AbstractBiMap<K, V>
/*     */   extends ForwardingMap<K, V>
/*     */   implements BiMap<K, V>, Serializable
/*     */ {
/*     */   private transient Map<K, V> delegate;
/*     */   private transient AbstractBiMap<V, K> inverse;
/*     */   private transient Set<K> keySet;
/*     */   private transient Set<V> valueSet;
/*     */   private transient Set<Map.Entry<K, V>> entrySet;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   AbstractBiMap(Map<K, V> forward, Map<V, K> backward) {
/*  54 */     setDelegates(forward, backward);
/*     */   }
/*     */ 
/*     */   
/*     */   private AbstractBiMap(Map<K, V> backward, AbstractBiMap<V, K> forward) {
/*  59 */     this.delegate = backward;
/*  60 */     this.inverse = forward;
/*     */   }
/*     */   
/*     */   protected Map<K, V> delegate() {
/*  64 */     return this.delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setDelegates(Map<K, V> forward, Map<V, K> backward) {
/*  72 */     Preconditions.checkState((this.delegate == null));
/*  73 */     Preconditions.checkState((this.inverse == null));
/*  74 */     Preconditions.checkArgument(forward.isEmpty());
/*  75 */     Preconditions.checkArgument(backward.isEmpty());
/*  76 */     Preconditions.checkArgument((forward != backward));
/*  77 */     this.delegate = forward;
/*  78 */     this.inverse = new Inverse<V, K>(backward, this);
/*     */   }
/*     */   
/*     */   void setInverse(AbstractBiMap<V, K> inverse) {
/*  82 */     this.inverse = inverse;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/*  88 */     return this.inverse.containsKey(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V put(K key, V value) {
/*  94 */     return putInBothMaps(key, value, false);
/*     */   }
/*     */   
/*     */   public V forcePut(K key, V value) {
/*  98 */     return putInBothMaps(key, value, true);
/*     */   }
/*     */   
/*     */   private V putInBothMaps(@Nullable K key, @Nullable V value, boolean force) {
/* 102 */     boolean containedKey = containsKey(key);
/* 103 */     if (containedKey && Objects.equal(value, get(key))) {
/* 104 */       return value;
/*     */     }
/* 106 */     if (force) {
/* 107 */       inverse().remove(value);
/*     */     } else {
/* 109 */       Preconditions.checkArgument(!containsValue(value), "value already present: %s", new Object[] { value });
/*     */     } 
/* 111 */     V oldValue = this.delegate.put(key, value);
/* 112 */     updateInverseMap(key, containedKey, oldValue, value);
/* 113 */     return oldValue;
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateInverseMap(K key, boolean containedKey, V oldValue, V newValue) {
/* 118 */     if (containedKey) {
/* 119 */       removeFromInverseMap(oldValue);
/*     */     }
/* 121 */     this.inverse.delegate.put((K)newValue, (V)key);
/*     */   }
/*     */   
/*     */   public V remove(Object key) {
/* 125 */     return containsKey(key) ? removeFromBothMaps(key) : null;
/*     */   }
/*     */   
/*     */   private V removeFromBothMaps(Object key) {
/* 129 */     V oldValue = this.delegate.remove(key);
/* 130 */     removeFromInverseMap(oldValue);
/* 131 */     return oldValue;
/*     */   }
/*     */   
/*     */   private void removeFromInverseMap(V oldValue) {
/* 135 */     this.inverse.delegate.remove(oldValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> map) {
/* 141 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 142 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear() {
/* 147 */     this.delegate.clear();
/* 148 */     this.inverse.delegate.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BiMap<V, K> inverse() {
/* 154 */     return this.inverse;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 160 */     Set<K> result = this.keySet;
/* 161 */     return (result == null) ? (this.keySet = new KeySet()) : result;
/*     */   }
/*     */   
/*     */   private class KeySet extends ForwardingSet<K> {
/*     */     protected Set<K> delegate() {
/* 166 */       return AbstractBiMap.this.delegate.keySet();
/*     */     }
/*     */     private KeySet() {}
/*     */     public void clear() {
/* 170 */       AbstractBiMap.this.clear();
/*     */     }
/*     */     
/*     */     public boolean remove(Object key) {
/* 174 */       if (!contains(key)) {
/* 175 */         return false;
/*     */       }
/* 177 */       AbstractBiMap.this.removeFromBothMaps(key);
/* 178 */       return true;
/*     */     }
/*     */     
/*     */     public boolean removeAll(Collection<?> keysToRemove) {
/* 182 */       return Iterators.removeAll(iterator(), keysToRemove);
/*     */     }
/*     */     
/*     */     public boolean retainAll(Collection<?> keysToRetain) {
/* 186 */       return Iterators.retainAll(iterator(), keysToRetain);
/*     */     }
/*     */     
/*     */     public Iterator<K> iterator() {
/* 190 */       final Iterator<Map.Entry<K, V>> iterator = AbstractBiMap.this.delegate.entrySet().iterator();
/* 191 */       return new Iterator<K>() {
/*     */           Map.Entry<K, V> entry;
/*     */           
/*     */           public boolean hasNext() {
/* 195 */             return iterator.hasNext();
/*     */           }
/*     */           public K next() {
/* 198 */             this.entry = iterator.next();
/* 199 */             return this.entry.getKey();
/*     */           }
/*     */           public void remove() {
/* 202 */             Preconditions.checkState((this.entry != null));
/* 203 */             V value = this.entry.getValue();
/* 204 */             iterator.remove();
/* 205 */             AbstractBiMap.this.removeFromInverseMap(value);
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
/* 218 */     Set<V> result = this.valueSet;
/* 219 */     return (result == null) ? (this.valueSet = new ValueSet()) : result;
/*     */   }
/*     */   
/*     */   private class ValueSet extends ForwardingSet<V> {
/* 223 */     final Set<V> valuesDelegate = AbstractBiMap.this.inverse.keySet();
/*     */     
/*     */     protected Set<V> delegate() {
/* 226 */       return this.valuesDelegate;
/*     */     }
/*     */     
/*     */     public Iterator<V> iterator() {
/* 230 */       final Iterator<V> iterator = AbstractBiMap.this.delegate.values().iterator();
/* 231 */       return new Iterator<V>() {
/*     */           V valueToRemove;
/*     */           
/*     */           public boolean hasNext() {
/* 235 */             return iterator.hasNext();
/*     */           }
/*     */           
/*     */           public V next() {
/* 239 */             return this.valueToRemove = (V)iterator.next();
/*     */           }
/*     */           
/*     */           public void remove() {
/* 243 */             iterator.remove();
/* 244 */             AbstractBiMap.this.removeFromInverseMap(this.valueToRemove);
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public Object[] toArray() {
/* 250 */       return ObjectArrays.toArrayImpl(this);
/*     */     }
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 254 */       return ObjectArrays.toArrayImpl(this, array);
/*     */     }
/*     */     
/*     */     public String toString() {
/* 258 */       return Iterators.toString(iterator());
/*     */     }
/*     */     
/*     */     private ValueSet() {}
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 265 */     Set<Map.Entry<K, V>> result = this.entrySet;
/* 266 */     return (result == null) ? (this.entrySet = new EntrySet()) : result;
/*     */   }
/*     */   
/*     */   private class EntrySet extends ForwardingSet<Map.Entry<K, V>> {
/* 270 */     final Set<Map.Entry<K, V>> esDelegate = AbstractBiMap.this.delegate.entrySet();
/*     */     
/*     */     protected Set<Map.Entry<K, V>> delegate() {
/* 273 */       return this.esDelegate;
/*     */     }
/*     */     
/*     */     public void clear() {
/* 277 */       AbstractBiMap.this.clear();
/*     */     }
/*     */     
/*     */     public boolean remove(Object object) {
/* 281 */       if (!this.esDelegate.remove(object)) {
/* 282 */         return false;
/*     */       }
/* 284 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/* 285 */       AbstractBiMap.this.inverse.delegate.remove(entry.getValue());
/* 286 */       return true;
/*     */     }
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 290 */       final Iterator<Map.Entry<K, V>> iterator = this.esDelegate.iterator();
/* 291 */       return new Iterator<Map.Entry<K, V>>() {
/*     */           Map.Entry<K, V> entry;
/*     */           
/*     */           public boolean hasNext() {
/* 295 */             return iterator.hasNext();
/*     */           }
/*     */           
/*     */           public Map.Entry<K, V> next() {
/* 299 */             this.entry = iterator.next();
/* 300 */             final Map.Entry<K, V> finalEntry = this.entry;
/*     */             
/* 302 */             return new ForwardingMapEntry<K, V>() {
/*     */                 protected Map.Entry<K, V> delegate() {
/* 304 */                   return finalEntry;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public V setValue(V value) {
/* 309 */                   Preconditions.checkState(AbstractBiMap.EntrySet.this.contains(this), "entry no longer in map");
/*     */                   
/* 311 */                   if (Objects.equal(value, getValue())) {
/* 312 */                     return value;
/*     */                   }
/* 314 */                   Preconditions.checkArgument(!AbstractBiMap.this.containsValue(value), "value already present: %s", new Object[] { value });
/*     */                   
/* 316 */                   V oldValue = (V)finalEntry.setValue(value);
/* 317 */                   Preconditions.checkState(Objects.equal(value, AbstractBiMap.this.get(getKey())), "entry no longer in map");
/*     */                   
/* 319 */                   AbstractBiMap.this.updateInverseMap(getKey(), true, oldValue, value);
/* 320 */                   return oldValue;
/*     */                 }
/*     */               };
/*     */           }
/*     */           
/*     */           public void remove() {
/* 326 */             Preconditions.checkState((this.entry != null));
/* 327 */             V value = this.entry.getValue();
/* 328 */             iterator.remove();
/* 329 */             AbstractBiMap.this.removeFromInverseMap(value);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 337 */       return ObjectArrays.toArrayImpl(this);
/*     */     }
/*     */     public <T> T[] toArray(T[] array) {
/* 340 */       return ObjectArrays.toArrayImpl(this, array);
/*     */     }
/*     */     public boolean contains(Object o) {
/* 343 */       return Maps.containsEntryImpl(delegate(), o);
/*     */     }
/*     */     public boolean containsAll(Collection<?> c) {
/* 346 */       return Collections2.containsAll(this, c);
/*     */     }
/*     */     public boolean removeAll(Collection<?> c) {
/* 349 */       return Iterators.removeAll(iterator(), c);
/*     */     }
/*     */     public boolean retainAll(Collection<?> c) {
/* 352 */       return Iterators.retainAll(iterator(), c);
/*     */     }
/*     */     
/*     */     private EntrySet() {} }
/*     */   
/*     */   private static class Inverse<K, V> extends AbstractBiMap<K, V> {
/*     */     private Inverse(Map<K, V> backward, AbstractBiMap<V, K> forward) {
/* 359 */       super(backward, forward);
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
/* 375 */       stream.defaultWriteObject();
/* 376 */       stream.writeObject(inverse());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 382 */       stream.defaultReadObject();
/* 383 */       setInverse((AbstractBiMap<V, K>)stream.readObject());
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 387 */       return inverse().inverse();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\AbstractBiMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */