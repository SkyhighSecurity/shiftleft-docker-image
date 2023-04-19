/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
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
/*     */ @GwtCompatible
/*     */ final class MapConstraints
/*     */ {
/*     */   private static <K, V> Map.Entry<K, V> constrainedEntry(final Map.Entry<K, V> entry, final MapConstraint<? super K, ? super V> constraint) {
/*  52 */     Preconditions.checkNotNull(entry);
/*  53 */     Preconditions.checkNotNull(constraint);
/*  54 */     return new ForwardingMapEntry<K, V>() {
/*     */         protected Map.Entry<K, V> delegate() {
/*  56 */           return entry;
/*     */         }
/*     */         public V setValue(V value) {
/*  59 */           constraint.checkKeyValue(getKey(), value);
/*  60 */           return (V)entry.setValue(value);
/*     */         }
/*     */       };
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
/*     */   private static <K, V> Collection<Map.Entry<K, V>> constrainedEntries(Collection<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
/*  80 */     if (entries instanceof Set) {
/*  81 */       return constrainedEntrySet((Set<Map.Entry<K, V>>)entries, constraint);
/*     */     }
/*  83 */     return new ConstrainedEntries<K, V>(entries, constraint);
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
/*     */   private static <K, V> Set<Map.Entry<K, V>> constrainedEntrySet(Set<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
/* 103 */     return new ConstrainedEntrySet<K, V>(entries, constraint);
/*     */   }
/*     */   
/*     */   static class ConstrainedMap<K, V>
/*     */     extends ForwardingMap<K, V> {
/*     */     final Map<K, V> delegate;
/*     */     final MapConstraint<? super K, ? super V> constraint;
/*     */     private volatile transient Set<Map.Entry<K, V>> entrySet;
/*     */     
/*     */     ConstrainedMap(Map<K, V> delegate, MapConstraint<? super K, ? super V> constraint) {
/* 113 */       this.delegate = (Map<K, V>)Preconditions.checkNotNull(delegate);
/* 114 */       this.constraint = (MapConstraint<? super K, ? super V>)Preconditions.checkNotNull(constraint);
/*     */     }
/*     */     protected Map<K, V> delegate() {
/* 117 */       return this.delegate;
/*     */     }
/*     */     public Set<Map.Entry<K, V>> entrySet() {
/* 120 */       if (this.entrySet == null) {
/* 121 */         this.entrySet = MapConstraints.constrainedEntrySet(this.delegate.entrySet(), this.constraint);
/*     */       }
/* 123 */       return this.entrySet;
/*     */     }
/*     */     public V put(K key, V value) {
/* 126 */       this.constraint.checkKeyValue(key, value);
/* 127 */       return this.delegate.put(key, value);
/*     */     }
/*     */     public void putAll(Map<? extends K, ? extends V> map) {
/* 130 */       this.delegate.putAll(MapConstraints.checkMap(map, this.constraint));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ConstrainedEntries<K, V>
/*     */     extends ForwardingCollection<Map.Entry<K, V>>
/*     */   {
/*     */     final MapConstraint<? super K, ? super V> constraint;
/*     */     
/*     */     final Collection<Map.Entry<K, V>> entries;
/*     */     
/*     */     ConstrainedEntries(Collection<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
/* 143 */       this.entries = entries;
/* 144 */       this.constraint = constraint;
/*     */     }
/*     */     protected Collection<Map.Entry<K, V>> delegate() {
/* 147 */       return this.entries;
/*     */     }
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 151 */       final Iterator<Map.Entry<K, V>> iterator = this.entries.iterator();
/* 152 */       return new ForwardingIterator<Map.Entry<K, V>>() {
/*     */           public Map.Entry<K, V> next() {
/* 154 */             return MapConstraints.constrainedEntry(iterator.next(), MapConstraints.ConstrainedEntries.this.constraint);
/*     */           }
/*     */           protected Iterator<Map.Entry<K, V>> delegate() {
/* 157 */             return iterator;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 165 */       return ObjectArrays.toArrayImpl(this);
/*     */     }
/*     */     public <T> T[] toArray(T[] array) {
/* 168 */       return ObjectArrays.toArrayImpl(this, array);
/*     */     }
/*     */     public boolean contains(Object o) {
/* 171 */       return Maps.containsEntryImpl(delegate(), o);
/*     */     }
/*     */     public boolean containsAll(Collection<?> c) {
/* 174 */       return Collections2.containsAll(this, c);
/*     */     }
/*     */     public boolean remove(Object o) {
/* 177 */       return Maps.removeEntryImpl(delegate(), o);
/*     */     }
/*     */     public boolean removeAll(Collection<?> c) {
/* 180 */       return Iterators.removeAll(iterator(), c);
/*     */     }
/*     */     public boolean retainAll(Collection<?> c) {
/* 183 */       return Iterators.retainAll(iterator(), c);
/*     */     }
/*     */   }
/*     */   
/*     */   static class ConstrainedEntrySet<K, V>
/*     */     extends ConstrainedEntries<K, V>
/*     */     implements Set<Map.Entry<K, V>> {
/*     */     ConstrainedEntrySet(Set<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
/* 191 */       super(entries, constraint);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 197 */       return Collections2.setEquals(this, object);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 201 */       return Sets.hashCodeImpl(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static <K, V> Map<K, V> checkMap(Map<? extends K, ? extends V> map, MapConstraint<? super K, ? super V> constraint) {
/* 207 */     Map<K, V> copy = new LinkedHashMap<K, V>(map);
/* 208 */     for (Map.Entry<K, V> entry : copy.entrySet()) {
/* 209 */       constraint.checkKeyValue(entry.getKey(), entry.getValue());
/*     */     }
/* 211 */     return copy;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\MapConstraints.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */