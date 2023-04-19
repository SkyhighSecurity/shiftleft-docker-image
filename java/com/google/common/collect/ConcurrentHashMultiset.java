/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
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
/*     */ public final class ConcurrentHashMultiset<E>
/*     */   extends AbstractMultiset<E>
/*     */   implements Serializable
/*     */ {
/*     */   private final transient ConcurrentMap<E, Integer> countMap;
/*     */   private transient EntrySet entrySet;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   private static class FieldSettersHolder
/*     */   {
/*  63 */     static final Serialization.FieldSetter<ConcurrentHashMultiset> COUNT_MAP_FIELD_SETTER = Serialization.getFieldSetter(ConcurrentHashMultiset.class, "countMap");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ConcurrentHashMultiset<E> create() {
/*  73 */     return new ConcurrentHashMultiset<E>(new ConcurrentHashMap<E, Integer>());
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
/*     */   public static <E> ConcurrentHashMultiset<E> create(Iterable<? extends E> elements) {
/*  85 */     ConcurrentHashMultiset<E> multiset = create();
/*  86 */     Iterables.addAll(multiset, elements);
/*  87 */     return multiset;
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
/*     */   @VisibleForTesting
/*     */   ConcurrentHashMultiset(ConcurrentMap<E, Integer> countMap) {
/* 103 */     Preconditions.checkArgument(countMap.isEmpty());
/* 104 */     this.countMap = countMap;
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
/*     */   public int count(@Nullable Object element) {
/*     */     try {
/* 117 */       return unbox(this.countMap.get(element));
/* 118 */     } catch (NullPointerException e) {
/* 119 */       return 0;
/* 120 */     } catch (ClassCastException e) {
/* 121 */       return 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 133 */     long sum = 0L;
/* 134 */     for (Integer value : this.countMap.values()) {
/* 135 */       sum += value.intValue();
/*     */     }
/* 137 */     return (int)Math.min(sum, 2147483647L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 146 */     return snapshot().toArray();
/*     */   }
/*     */   
/*     */   public <T> T[] toArray(T[] array) {
/* 150 */     return snapshot().toArray(array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<E> snapshot() {
/* 158 */     List<E> list = Lists.newArrayListWithExpectedSize(size());
/* 159 */     for (Multiset.Entry<E> entry : entrySet()) {
/* 160 */       E element = entry.getElement();
/* 161 */       for (int i = entry.getCount(); i > 0; i--) {
/* 162 */         list.add(element);
/*     */       }
/*     */     } 
/* 165 */     return list;
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
/*     */   public int add(E element, int occurrences) {
/* 181 */     if (occurrences == 0) {
/* 182 */       return count(element);
/*     */     }
/* 184 */     Preconditions.checkArgument((occurrences > 0), "Invalid occurrences: %s", new Object[] { Integer.valueOf(occurrences) });
/*     */     
/*     */     while (true) {
/* 187 */       int current = count(element);
/* 188 */       if (current == 0) {
/* 189 */         if (this.countMap.putIfAbsent(element, Integer.valueOf(occurrences)) == null)
/* 190 */           return 0; 
/*     */         continue;
/*     */       } 
/* 193 */       Preconditions.checkArgument((occurrences <= Integer.MAX_VALUE - current), "Overflow adding %s occurrences to a count of %s", new Object[] { Integer.valueOf(occurrences), Integer.valueOf(current) });
/*     */ 
/*     */       
/* 196 */       int next = current + occurrences;
/* 197 */       if (this.countMap.replace(element, Integer.valueOf(current), Integer.valueOf(next))) {
/* 198 */         return current;
/*     */       }
/*     */     } 
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
/*     */   public int remove(@Nullable Object element, int occurrences) {
/* 216 */     if (occurrences == 0) {
/* 217 */       return count(element);
/*     */     }
/* 219 */     Preconditions.checkArgument((occurrences > 0), "Invalid occurrences: %s", new Object[] { Integer.valueOf(occurrences) });
/*     */     
/*     */     while (true) {
/* 222 */       int current = count(element);
/* 223 */       if (current == 0) {
/* 224 */         return 0;
/*     */       }
/* 226 */       if (occurrences >= current) {
/* 227 */         if (this.countMap.remove(element, Integer.valueOf(current))) {
/* 228 */           return current;
/*     */         }
/*     */         
/*     */         continue;
/*     */       } 
/* 233 */       E casted = (E)element;
/*     */       
/* 235 */       if (this.countMap.replace(casted, Integer.valueOf(current), Integer.valueOf(current - occurrences))) {
/* 236 */         return current;
/*     */       }
/*     */     } 
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
/*     */   private int removeAllOccurrences(@Nullable Object element) {
/*     */     try {
/* 253 */       return unbox(this.countMap.remove(element));
/* 254 */     } catch (NullPointerException e) {
/* 255 */       return 0;
/* 256 */     } catch (ClassCastException e) {
/* 257 */       return 0;
/*     */     } 
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
/*     */   public boolean removeExactly(@Nullable Object element, int occurrences) {
/* 274 */     if (occurrences == 0) {
/* 275 */       return true;
/*     */     }
/* 277 */     Preconditions.checkArgument((occurrences > 0), "Invalid occurrences: %s", new Object[] { Integer.valueOf(occurrences) });
/*     */     
/*     */     while (true) {
/* 280 */       int current = count(element);
/* 281 */       if (occurrences > current) {
/* 282 */         return false;
/*     */       }
/* 284 */       if (occurrences == current) {
/* 285 */         if (this.countMap.remove(element, Integer.valueOf(occurrences))) {
/* 286 */           return true;
/*     */         }
/*     */         continue;
/*     */       } 
/* 290 */       E casted = (E)element;
/* 291 */       if (this.countMap.replace(casted, Integer.valueOf(current), Integer.valueOf(current - occurrences))) {
/* 292 */         return true;
/*     */       }
/*     */     } 
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
/*     */   public int setCount(E element, int count) {
/* 307 */     Multisets.checkNonnegative(count, "count");
/* 308 */     return (count == 0) ? removeAllOccurrences(element) : unbox(this.countMap.put(element, Integer.valueOf(count)));
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
/*     */   public boolean setCount(E element, int oldCount, int newCount) {
/* 327 */     Multisets.checkNonnegative(oldCount, "oldCount");
/* 328 */     Multisets.checkNonnegative(newCount, "newCount");
/* 329 */     if (newCount == 0) {
/* 330 */       if (oldCount == 0)
/*     */       {
/* 332 */         return !this.countMap.containsKey(element);
/*     */       }
/* 334 */       return this.countMap.remove(element, Integer.valueOf(oldCount));
/*     */     } 
/*     */     
/* 337 */     if (oldCount == 0) {
/* 338 */       return (this.countMap.putIfAbsent(element, Integer.valueOf(newCount)) == null);
/*     */     }
/* 340 */     return this.countMap.replace(element, Integer.valueOf(oldCount), Integer.valueOf(newCount));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Set<E> createElementSet() {
/* 346 */     final Set<E> delegate = this.countMap.keySet();
/* 347 */     return new ForwardingSet<E>() {
/*     */         protected Set<E> delegate() {
/* 349 */           return delegate;
/*     */         }
/*     */         public boolean remove(Object object) {
/*     */           try {
/* 353 */             return delegate.remove(object);
/* 354 */           } catch (NullPointerException e) {
/* 355 */             return false;
/* 356 */           } catch (ClassCastException e) {
/* 357 */             return false;
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Multiset.Entry<E>> entrySet() {
/* 366 */     EntrySet result = this.entrySet;
/* 367 */     if (result == null) {
/* 368 */       this.entrySet = result = new EntrySet();
/*     */     }
/* 370 */     return result;
/*     */   }
/*     */   
/*     */   private class EntrySet extends AbstractSet<Multiset.Entry<E>> {
/*     */     public int size() {
/* 375 */       return ConcurrentHashMultiset.this.countMap.size();
/*     */     }
/*     */     private EntrySet() {}
/*     */     public boolean isEmpty() {
/* 379 */       return ConcurrentHashMultiset.this.countMap.isEmpty();
/*     */     }
/*     */     
/*     */     public boolean contains(Object object) {
/* 383 */       if (object instanceof Multiset.Entry) {
/* 384 */         Multiset.Entry<?> entry = (Multiset.Entry)object;
/* 385 */         Object element = entry.getElement();
/* 386 */         int entryCount = entry.getCount();
/* 387 */         return (entryCount > 0 && ConcurrentHashMultiset.this.count(element) == entryCount);
/*     */       } 
/* 389 */       return false;
/*     */     }
/*     */     
/*     */     public Iterator<Multiset.Entry<E>> iterator() {
/* 393 */       final Iterator<Map.Entry<E, Integer>> backingIterator = ConcurrentHashMultiset.this.countMap.entrySet().iterator();
/*     */       
/* 395 */       return (Iterator)new Iterator<Multiset.Entry<Multiset.Entry<E>>>() {
/*     */           public boolean hasNext() {
/* 397 */             return backingIterator.hasNext();
/*     */           }
/*     */           
/*     */           public Multiset.Entry<E> next() {
/* 401 */             Map.Entry<E, Integer> backingEntry = backingIterator.next();
/* 402 */             return Multisets.immutableEntry(backingEntry.getKey(), ((Integer)backingEntry.getValue()).intValue());
/*     */           }
/*     */ 
/*     */           
/*     */           public void remove() {
/* 407 */             backingIterator.remove();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 418 */       return snapshot().toArray();
/*     */     }
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 422 */       return snapshot().toArray(array);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private List<Multiset.Entry<E>> snapshot() {
/* 430 */       List<Multiset.Entry<E>> list = Lists.newArrayListWithExpectedSize(size());
/* 431 */       for (Multiset.Entry<E> entry : (Iterable<Multiset.Entry<E>>)this) {
/* 432 */         list.add(entry);
/*     */       }
/* 434 */       return list;
/*     */     }
/*     */     
/*     */     public boolean remove(Object object) {
/* 438 */       if (object instanceof Multiset.Entry) {
/* 439 */         Multiset.Entry<?> entry = (Multiset.Entry)object;
/* 440 */         Object element = entry.getElement();
/* 441 */         int entryCount = entry.getCount();
/* 442 */         return ConcurrentHashMultiset.this.countMap.remove(element, Integer.valueOf(entryCount));
/*     */       } 
/* 444 */       return false;
/*     */     }
/*     */     
/*     */     public void clear() {
/* 448 */       ConcurrentHashMultiset.this.countMap.clear();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 455 */       return ConcurrentHashMultiset.this.countMap.hashCode();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int unbox(Integer i) {
/* 463 */     return (i == null) ? 0 : i.intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 471 */     stream.defaultWriteObject();
/*     */     
/* 473 */     Serialization.writeMultiset(HashMultiset.create(this), stream);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 478 */     stream.defaultReadObject();
/* 479 */     FieldSettersHolder.COUNT_MAP_FIELD_SETTER.set(this, new ConcurrentHashMap<Object, Object>());
/*     */     
/* 481 */     Serialization.populateMultiset(this, stream);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ConcurrentHashMultiset.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */