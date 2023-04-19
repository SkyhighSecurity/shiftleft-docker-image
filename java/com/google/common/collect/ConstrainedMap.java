/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
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
/*     */ class ConstrainedMap<K, V>
/*     */   extends ForwardingMap<K, V>
/*     */ {
/*     */   final Map<K, V> delegate;
/*     */   final MapConstraint<? super K, ? super V> constraint;
/*     */   private volatile Set<Map.Entry<K, V>> entrySet;
/*     */   
/*     */   ConstrainedMap(Map<K, V> delegate, MapConstraint<? super K, ? super V> constraint) {
/*  42 */     this.delegate = (Map<K, V>)Preconditions.checkNotNull(delegate);
/*  43 */     this.constraint = (MapConstraint<? super K, ? super V>)Preconditions.checkNotNull(constraint);
/*     */   }
/*     */   
/*     */   protected Map<K, V> delegate() {
/*  47 */     return this.delegate;
/*     */   }
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/*  50 */     if (this.entrySet == null) {
/*  51 */       this.entrySet = constrainedEntrySet(this.delegate.entrySet(), this.constraint);
/*     */     }
/*  53 */     return this.entrySet;
/*     */   }
/*     */   public V put(K key, V value) {
/*  56 */     this.constraint.checkKeyValue(key, value);
/*  57 */     return this.delegate.put(key, value);
/*     */   }
/*     */   public void putAll(Map<? extends K, ? extends V> map) {
/*  60 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/*  61 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K, V> Map.Entry<K, V> constrainedEntry(final Map.Entry<K, V> entry, final MapConstraint<? super K, ? super V> constraint) {
/*  68 */     Preconditions.checkNotNull(entry);
/*  69 */     Preconditions.checkNotNull(constraint);
/*  70 */     return new ForwardingMapEntry<K, V>() {
/*     */         protected Map.Entry<K, V> delegate() {
/*  72 */           return entry;
/*     */         }
/*     */         public V setValue(V value) {
/*  75 */           constraint.checkKeyValue(getKey(), value);
/*  76 */           return (V)entry.setValue(value);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K, V> Set<Map.Entry<K, V>> constrainedEntrySet(Set<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
/*  84 */     return new ConstrainedEntrySet<K, V>(entries, constraint);
/*     */   }
/*     */   
/*     */   private static class ConstrainedEntries<K, V>
/*     */     extends ForwardingCollection<Map.Entry<K, V>>
/*     */   {
/*     */     final MapConstraint<? super K, ? super V> constraint;
/*     */     final Collection<Map.Entry<K, V>> entries;
/*     */     
/*     */     ConstrainedEntries(Collection<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
/*  94 */       this.entries = entries;
/*  95 */       this.constraint = constraint;
/*     */     }
/*     */     protected Collection<Map.Entry<K, V>> delegate() {
/*  98 */       return this.entries;
/*     */     }
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 102 */       final Iterator<Map.Entry<K, V>> iterator = this.entries.iterator();
/* 103 */       return new ForwardingIterator<Map.Entry<K, V>>() {
/*     */           public Map.Entry<K, V> next() {
/* 105 */             return ConstrainedMap.constrainedEntry(iterator.next(), ConstrainedMap.ConstrainedEntries.this.constraint);
/*     */           }
/*     */           protected Iterator<Map.Entry<K, V>> delegate() {
/* 108 */             return iterator;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 116 */       return ObjectArrays.toArrayImpl(this);
/*     */     }
/*     */     public <T> T[] toArray(T[] array) {
/* 119 */       return ObjectArrays.toArrayImpl(this, array);
/*     */     }
/*     */     public boolean contains(Object o) {
/* 122 */       return Maps.containsEntryImpl(delegate(), o);
/*     */     }
/*     */     public boolean containsAll(Collection<?> c) {
/* 125 */       return Collections2.containsAll(this, c);
/*     */     }
/*     */     public boolean remove(Object o) {
/* 128 */       return Maps.removeEntryImpl(delegate(), o);
/*     */     }
/*     */     public boolean removeAll(Collection<?> c) {
/* 131 */       return Iterators.removeAll(iterator(), c);
/*     */     }
/*     */     public boolean retainAll(Collection<?> c) {
/* 134 */       return Iterators.retainAll(iterator(), c);
/*     */     }
/*     */   }
/*     */   
/*     */   static class ConstrainedEntrySet<K, V>
/*     */     extends ConstrainedEntries<K, V>
/*     */     implements Set<Map.Entry<K, V>> {
/*     */     ConstrainedEntrySet(Set<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
/* 142 */       super(entries, constraint);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 148 */       return Collections2.setEquals(this, object);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 152 */       return Sets.hashCodeImpl(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ConstrainedMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */