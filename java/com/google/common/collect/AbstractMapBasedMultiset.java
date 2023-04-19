/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ abstract class AbstractMapBasedMultiset<E>
/*     */   extends AbstractMultiset<E>
/*     */   implements Serializable
/*     */ {
/*     */   private transient Map<E, AtomicInteger> backingMap;
/*     */   private transient long size;
/*     */   private transient EntrySet entrySet;
/*     */   private static final long serialVersionUID = -2250766705698539974L;
/*     */   
/*     */   protected AbstractMapBasedMultiset(Map<E, AtomicInteger> backingMap) {
/*  64 */     this.backingMap = (Map<E, AtomicInteger>)Preconditions.checkNotNull(backingMap);
/*  65 */     this.size = super.size();
/*     */   }
/*     */   
/*     */   Map<E, AtomicInteger> backingMap() {
/*  69 */     return this.backingMap;
/*     */   }
/*     */ 
/*     */   
/*     */   void setBackingMap(Map<E, AtomicInteger> backingMap) {
/*  74 */     this.backingMap = backingMap;
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
/*     */   public Set<Multiset.Entry<E>> entrySet() {
/*  89 */     EntrySet result = this.entrySet;
/*  90 */     if (result == null) {
/*  91 */       this.entrySet = result = new EntrySet();
/*     */     }
/*  93 */     return result;
/*     */   }
/*     */   
/*     */   private class EntrySet extends AbstractSet<Multiset.Entry<E>> {
/*     */     public Iterator<Multiset.Entry<E>> iterator() {
/*  98 */       final Iterator<Map.Entry<E, AtomicInteger>> backingEntries = AbstractMapBasedMultiset.this.backingMap.entrySet().iterator();
/*     */       
/* 100 */       return (Iterator)new Iterator<Multiset.Entry<Multiset.Entry<E>>>() {
/*     */           Map.Entry<E, AtomicInteger> toRemove;
/*     */           
/*     */           public boolean hasNext() {
/* 104 */             return backingEntries.hasNext();
/*     */           }
/*     */           
/*     */           public Multiset.Entry<E> next() {
/* 108 */             final Map.Entry<E, AtomicInteger> mapEntry = backingEntries.next();
/* 109 */             this.toRemove = mapEntry;
/* 110 */             return new Multisets.AbstractEntry<E>() {
/*     */                 public E getElement() {
/* 112 */                   return (E)mapEntry.getKey();
/*     */                 }
/*     */                 public int getCount() {
/* 115 */                   int count = ((AtomicInteger)mapEntry.getValue()).get();
/* 116 */                   if (count == 0) {
/* 117 */                     AtomicInteger frequency = (AtomicInteger)AbstractMapBasedMultiset.this.backingMap.get(getElement());
/* 118 */                     if (frequency != null) {
/* 119 */                       count = frequency.get();
/*     */                     }
/*     */                   } 
/* 122 */                   return count;
/*     */                 }
/*     */               };
/*     */           }
/*     */           
/*     */           public void remove() {
/* 128 */             Preconditions.checkState((this.toRemove != null), "no calls to next() since the last call to remove()");
/*     */             
/* 130 */             AbstractMapBasedMultiset.this.size -= ((AtomicInteger)this.toRemove.getValue()).getAndSet(0);
/* 131 */             backingEntries.remove();
/* 132 */             this.toRemove = null;
/*     */           }
/*     */         };
/*     */     }
/*     */     private EntrySet() {}
/*     */     public int size() {
/* 138 */       return AbstractMapBasedMultiset.this.backingMap.size();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void clear() {
/* 144 */       for (AtomicInteger frequency : AbstractMapBasedMultiset.this.backingMap.values()) {
/* 145 */         frequency.set(0);
/*     */       }
/* 147 */       AbstractMapBasedMultiset.this.backingMap.clear();
/* 148 */       AbstractMapBasedMultiset.this.size = 0L;
/*     */     }
/*     */     
/*     */     public boolean contains(Object o) {
/* 152 */       if (o instanceof Multiset.Entry) {
/* 153 */         Multiset.Entry<?> entry = (Multiset.Entry)o;
/* 154 */         int count = AbstractMapBasedMultiset.this.count(entry.getElement());
/* 155 */         return (count == entry.getCount() && count > 0);
/*     */       } 
/* 157 */       return false;
/*     */     }
/*     */     
/*     */     public boolean remove(Object o) {
/* 161 */       if (contains(o)) {
/* 162 */         Multiset.Entry<?> entry = (Multiset.Entry)o;
/* 163 */         AtomicInteger frequency = (AtomicInteger)AbstractMapBasedMultiset.this.backingMap.remove(entry.getElement());
/* 164 */         int numberRemoved = frequency.getAndSet(0);
/* 165 */         AbstractMapBasedMultiset.this.size -= numberRemoved;
/* 166 */         return true;
/*     */       } 
/* 168 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 175 */     return (int)Math.min(this.size, 2147483647L);
/*     */   }
/*     */   
/*     */   public Iterator<E> iterator() {
/* 179 */     return new MapBasedMultisetIterator();
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
/*     */   private class MapBasedMultisetIterator
/*     */     implements Iterator<E>
/*     */   {
/* 194 */     final Iterator<Map.Entry<E, AtomicInteger>> entryIterator = AbstractMapBasedMultiset.this.backingMap.entrySet().iterator(); Map.Entry<E, AtomicInteger> currentEntry; int occurrencesLeft;
/*     */     boolean canRemove;
/*     */     
/*     */     public boolean hasNext() {
/* 198 */       return (this.occurrencesLeft > 0 || this.entryIterator.hasNext());
/*     */     }
/*     */     
/*     */     public E next() {
/* 202 */       if (this.occurrencesLeft == 0) {
/* 203 */         this.currentEntry = this.entryIterator.next();
/* 204 */         this.occurrencesLeft = ((AtomicInteger)this.currentEntry.getValue()).get();
/*     */       } 
/* 206 */       this.occurrencesLeft--;
/* 207 */       this.canRemove = true;
/* 208 */       return this.currentEntry.getKey();
/*     */     }
/*     */     
/*     */     public void remove() {
/* 212 */       Preconditions.checkState(this.canRemove, "no calls to next() since the last call to remove()");
/*     */       
/* 214 */       int frequency = ((AtomicInteger)this.currentEntry.getValue()).get();
/* 215 */       if (frequency <= 0) {
/* 216 */         throw new ConcurrentModificationException();
/*     */       }
/* 218 */       if (((AtomicInteger)this.currentEntry.getValue()).addAndGet(-1) == 0) {
/* 219 */         this.entryIterator.remove();
/*     */       }
/* 221 */       AbstractMapBasedMultiset.this.size--;
/* 222 */       this.canRemove = false;
/*     */     }
/*     */   }
/*     */   
/*     */   public int count(@Nullable Object element) {
/* 227 */     AtomicInteger frequency = this.backingMap.get(element);
/* 228 */     return (frequency == null) ? 0 : frequency.get();
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
/*     */   public int add(@Nullable E element, int occurrences) {
/*     */     int oldCount;
/* 241 */     if (occurrences == 0) {
/* 242 */       return count(element);
/*     */     }
/* 244 */     Preconditions.checkArgument((occurrences > 0), "occurrences cannot be negative: %s", new Object[] { Integer.valueOf(occurrences) });
/*     */     
/* 246 */     AtomicInteger frequency = this.backingMap.get(element);
/*     */     
/* 248 */     if (frequency == null) {
/* 249 */       oldCount = 0;
/* 250 */       this.backingMap.put(element, new AtomicInteger(occurrences));
/*     */     } else {
/* 252 */       oldCount = frequency.get();
/* 253 */       long newCount = oldCount + occurrences;
/* 254 */       Preconditions.checkArgument((newCount <= 2147483647L), "too many occurrences: %s", new Object[] { Long.valueOf(newCount) });
/*     */       
/* 256 */       frequency.getAndAdd(occurrences);
/*     */     } 
/* 258 */     this.size += occurrences;
/* 259 */     return oldCount;
/*     */   }
/*     */   public int remove(@Nullable Object element, int occurrences) {
/*     */     int numberRemoved;
/* 263 */     if (occurrences == 0) {
/* 264 */       return count(element);
/*     */     }
/* 266 */     Preconditions.checkArgument((occurrences > 0), "occurrences cannot be negative: %s", new Object[] { Integer.valueOf(occurrences) });
/*     */     
/* 268 */     AtomicInteger frequency = this.backingMap.get(element);
/* 269 */     if (frequency == null) {
/* 270 */       return 0;
/*     */     }
/*     */     
/* 273 */     int oldCount = frequency.get();
/*     */ 
/*     */     
/* 276 */     if (oldCount > occurrences) {
/* 277 */       numberRemoved = occurrences;
/*     */     } else {
/* 279 */       numberRemoved = oldCount;
/* 280 */       this.backingMap.remove(element);
/*     */     } 
/*     */     
/* 283 */     frequency.addAndGet(-numberRemoved);
/* 284 */     this.size -= numberRemoved;
/* 285 */     return oldCount;
/*     */   }
/*     */   
/*     */   public int setCount(E element, int count) {
/*     */     int oldCount;
/* 290 */     Multisets.checkNonnegative(count, "count");
/*     */ 
/*     */ 
/*     */     
/* 294 */     if (count == 0) {
/* 295 */       AtomicInteger existingCounter = this.backingMap.remove(element);
/* 296 */       oldCount = getAndSet(existingCounter, count);
/*     */     } else {
/* 298 */       AtomicInteger existingCounter = this.backingMap.get(element);
/* 299 */       oldCount = getAndSet(existingCounter, count);
/*     */       
/* 301 */       if (existingCounter == null) {
/* 302 */         this.backingMap.put(element, new AtomicInteger(count));
/*     */       }
/*     */     } 
/*     */     
/* 306 */     this.size += (count - oldCount);
/* 307 */     return oldCount;
/*     */   }
/*     */   
/*     */   private static int getAndSet(AtomicInteger i, int count) {
/* 311 */     if (i == null) {
/* 312 */       return 0;
/*     */     }
/*     */     
/* 315 */     return i.getAndSet(count);
/*     */   }
/*     */ 
/*     */   
/*     */   private int removeAllOccurrences(@Nullable Object element, Map<E, AtomicInteger> map) {
/* 320 */     AtomicInteger frequency = map.remove(element);
/* 321 */     if (frequency == null) {
/* 322 */       return 0;
/*     */     }
/* 324 */     int numberRemoved = frequency.getAndSet(0);
/* 325 */     this.size -= numberRemoved;
/* 326 */     return numberRemoved;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Set<E> createElementSet() {
/* 332 */     return new MapBasedElementSet(this.backingMap);
/*     */   }
/*     */ 
/*     */   
/*     */   class MapBasedElementSet
/*     */     extends ForwardingSet<E>
/*     */   {
/*     */     private final Map<E, AtomicInteger> map;
/*     */     private final Set<E> delegate;
/*     */     
/*     */     MapBasedElementSet(Map<E, AtomicInteger> map) {
/* 343 */       this.map = map;
/* 344 */       this.delegate = map.keySet();
/*     */     }
/*     */     
/*     */     protected Set<E> delegate() {
/* 348 */       return this.delegate;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Iterator<E> iterator() {
/* 354 */       final Iterator<Map.Entry<E, AtomicInteger>> entries = this.map.entrySet().iterator();
/*     */       
/* 356 */       return new Iterator<E>() {
/*     */           Map.Entry<E, AtomicInteger> toRemove;
/*     */           
/*     */           public boolean hasNext() {
/* 360 */             return entries.hasNext();
/*     */           }
/*     */           
/*     */           public E next() {
/* 364 */             this.toRemove = entries.next();
/* 365 */             return this.toRemove.getKey();
/*     */           }
/*     */           
/*     */           public void remove() {
/* 369 */             Preconditions.checkState((this.toRemove != null), "no calls to next() since the last call to remove()");
/*     */             
/* 371 */             AbstractMapBasedMultiset.this.size -= ((AtomicInteger)this.toRemove.getValue()).getAndSet(0);
/* 372 */             entries.remove();
/* 373 */             this.toRemove = null;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public boolean remove(Object element) {
/* 379 */       return (AbstractMapBasedMultiset.this.removeAllOccurrences(element, this.map) != 0);
/*     */     }
/*     */     
/*     */     public boolean removeAll(Collection<?> elementsToRemove) {
/* 383 */       return Iterators.removeAll(iterator(), elementsToRemove);
/*     */     }
/*     */     
/*     */     public boolean retainAll(Collection<?> elementsToRetain) {
/* 387 */       return Iterators.retainAll(iterator(), elementsToRetain);
/*     */     }
/*     */     
/*     */     public void clear() {
/* 391 */       if (this.map == AbstractMapBasedMultiset.this.backingMap) {
/* 392 */         AbstractMapBasedMultiset.this.clear();
/*     */       } else {
/* 394 */         Iterator<E> i = iterator();
/* 395 */         while (i.hasNext()) {
/* 396 */           i.next();
/* 397 */           i.remove();
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public Map<E, AtomicInteger> getMap() {
/* 403 */       return this.map;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObjectNoData() throws ObjectStreamException {
/* 410 */     throw new InvalidObjectException("Stream data required");
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\AbstractMapBasedMultiset.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */