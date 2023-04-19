/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class Multisets
/*     */ {
/*     */   public static <E> Multiset<E> unmodifiableMultiset(Multiset<? extends E> multiset) {
/*  59 */     return new UnmodifiableMultiset<E>(multiset);
/*     */   }
/*     */   
/*     */   private static class UnmodifiableMultiset<E> extends ForwardingMultiset<E> implements Serializable {
/*     */     final Multiset<? extends E> delegate;
/*     */     transient Set<E> elementSet;
/*     */     
/*     */     UnmodifiableMultiset(Multiset<? extends E> delegate) {
/*  67 */       this.delegate = delegate;
/*     */     }
/*     */     
/*     */     transient Set<Multiset.Entry<E>> entrySet;
/*     */     
/*     */     protected Multiset<E> delegate() {
/*  73 */       return (Multiset)this.delegate;
/*     */     }
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public Set<E> elementSet() {
/*  79 */       Set<E> es = this.elementSet;
/*  80 */       return (es == null) ? (this.elementSet = Collections.unmodifiableSet(this.delegate.elementSet())) : es;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Set<Multiset.Entry<E>> entrySet() {
/*  89 */       Set<Multiset.Entry<E>> es = this.entrySet;
/*  90 */       return (es == null) ? (this.entrySet = Collections.unmodifiableSet(this.delegate.entrySet())) : es;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Iterator<E> iterator() {
/* 100 */       return Iterators.unmodifiableIterator((Iterator)this.delegate.iterator());
/*     */     }
/*     */     
/*     */     public boolean add(E element) {
/* 104 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int add(E element, int occurences) {
/* 108 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(Collection<? extends E> elementsToAdd) {
/* 112 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean remove(Object element) {
/* 116 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int remove(Object element, int occurrences) {
/* 120 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean removeAll(Collection<?> elementsToRemove) {
/* 124 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean retainAll(Collection<?> elementsToRetain) {
/* 128 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void clear() {
/* 132 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int setCount(E element, int count) {
/* 136 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean setCount(E element, int oldCount, int newCount) {
/* 140 */       throw new UnsupportedOperationException();
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
/*     */   public static <E> Multiset.Entry<E> immutableEntry(@Nullable final E e, final int n) {
/* 155 */     Preconditions.checkArgument((n >= 0));
/* 156 */     return new AbstractEntry<E>() {
/*     */         public E getElement() {
/* 158 */           return (E)e;
/*     */         }
/*     */         public int getCount() {
/* 161 */           return n;
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
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> Multiset<E> forSet(Set<E> set) {
/* 184 */     return new SetMultiset<E>(set);
/*     */   }
/*     */   
/*     */   private static class SetMultiset<E> extends ForwardingCollection<E> implements Multiset<E>, Serializable { final Set<E> delegate;
/*     */     transient Set<E> elementSet;
/*     */     transient Set<Multiset.Entry<E>> entrySet;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SetMultiset(Set<E> set) {
/* 193 */       this.delegate = (Set<E>)Preconditions.checkNotNull(set);
/*     */     }
/*     */     
/*     */     protected Set<E> delegate() {
/* 197 */       return this.delegate;
/*     */     }
/*     */     
/*     */     public int count(Object element) {
/* 201 */       return this.delegate.contains(element) ? 1 : 0;
/*     */     }
/*     */     
/*     */     public int add(E element, int occurrences) {
/* 205 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int remove(Object element, int occurrences) {
/* 209 */       if (occurrences == 0) {
/* 210 */         return count(element);
/*     */       }
/* 212 */       Preconditions.checkArgument((occurrences > 0));
/* 213 */       return this.delegate.remove(element) ? 1 : 0;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Set<E> elementSet() {
/* 219 */       Set<E> es = this.elementSet;
/* 220 */       return (es == null) ? (this.elementSet = new ElementSet()) : es;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Set<Multiset.Entry<E>> entrySet() {
/* 226 */       Set<Multiset.Entry<E>> es = this.entrySet;
/* 227 */       return (es == null) ? (this.entrySet = new EntrySet()) : es;
/*     */     }
/*     */     
/*     */     public boolean add(E o) {
/* 231 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(Collection<? extends E> c) {
/* 235 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int setCount(E element, int count) {
/* 239 */       Multisets.checkNonnegative(count, "count");
/*     */       
/* 241 */       if (count == count(element))
/* 242 */         return count; 
/* 243 */       if (count == 0) {
/* 244 */         remove(element);
/* 245 */         return 1;
/*     */       } 
/* 247 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean setCount(E element, int oldCount, int newCount) {
/* 252 */       return Multisets.setCountImpl(this, element, oldCount, newCount);
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 256 */       if (object instanceof Multiset) {
/* 257 */         Multiset<?> that = (Multiset)object;
/* 258 */         return (size() == that.size() && this.delegate.equals(that.elementSet()));
/*     */       } 
/* 260 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 264 */       int sum = 0;
/* 265 */       for (E e : this) {
/* 266 */         sum += ((e == null) ? 0 : e.hashCode()) ^ 0x1;
/*     */       }
/* 268 */       return sum;
/*     */     }
/*     */     
/*     */     class ElementSet
/*     */       extends ForwardingSet<E> {
/*     */       protected Set<E> delegate() {
/* 274 */         return Multisets.SetMultiset.this.delegate;
/*     */       }
/*     */       
/*     */       public boolean add(E o) {
/* 278 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public boolean addAll(Collection<? extends E> c) {
/* 282 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     }
/*     */     
/*     */     class EntrySet
/*     */       extends AbstractSet<Multiset.Entry<E>> {
/*     */       public int size() {
/* 289 */         return Multisets.SetMultiset.this.delegate.size();
/*     */       }
/*     */       public Iterator<Multiset.Entry<E>> iterator() {
/* 292 */         return (Iterator)new Iterator<Multiset.Entry<Multiset.Entry<E>>>() {
/* 293 */             final Iterator<E> elements = Multisets.SetMultiset.this.delegate.iterator();
/*     */             
/*     */             public boolean hasNext() {
/* 296 */               return this.elements.hasNext();
/*     */             }
/*     */             public Multiset.Entry<E> next() {
/* 299 */               return Multisets.immutableEntry(this.elements.next(), 1);
/*     */             }
/*     */             public void remove() {
/* 302 */               this.elements.remove();
/*     */             }
/*     */           };
/*     */       }
/*     */     } }
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
/*     */   static int inferDistinctElements(Iterable<?> elements) {
/* 319 */     if (elements instanceof Multiset) {
/* 320 */       return ((Multiset)elements).elementSet().size();
/*     */     }
/* 322 */     return 11;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static abstract class AbstractEntry<E>
/*     */     implements Multiset.Entry<E>
/*     */   {
/*     */     public boolean equals(@Nullable Object object) {
/* 335 */       if (object instanceof Multiset.Entry) {
/* 336 */         Multiset.Entry<?> that = (Multiset.Entry)object;
/* 337 */         return (getCount() == that.getCount() && Objects.equal(getElement(), that.getElement()));
/*     */       } 
/*     */       
/* 340 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 348 */       E e = getElement();
/* 349 */       return ((e == null) ? 0 : e.hashCode()) ^ getCount();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 360 */       String text = String.valueOf(getElement());
/* 361 */       int n = getCount();
/* 362 */       return (n == 1) ? text : (text + " x " + n);
/*     */     }
/*     */   }
/*     */   
/*     */   static <E> int setCountImpl(Multiset<E> self, E element, int count) {
/* 367 */     checkNonnegative(count, "count");
/*     */     
/* 369 */     int oldCount = self.count(element);
/*     */     
/* 371 */     int delta = count - oldCount;
/* 372 */     if (delta > 0) {
/* 373 */       self.add(element, delta);
/* 374 */     } else if (delta < 0) {
/* 375 */       self.remove(element, -delta);
/*     */     } 
/*     */     
/* 378 */     return oldCount;
/*     */   }
/*     */ 
/*     */   
/*     */   static <E> boolean setCountImpl(Multiset<E> self, E element, int oldCount, int newCount) {
/* 383 */     checkNonnegative(oldCount, "oldCount");
/* 384 */     checkNonnegative(newCount, "newCount");
/*     */     
/* 386 */     if (self.count(element) == oldCount) {
/* 387 */       self.setCount(element, newCount);
/* 388 */       return true;
/*     */     } 
/* 390 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   static void checkNonnegative(int count, String name) {
/* 395 */     Preconditions.checkArgument((count >= 0), "%s cannot be negative: %s", new Object[] { name, Integer.valueOf(count) });
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\Multisets.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */