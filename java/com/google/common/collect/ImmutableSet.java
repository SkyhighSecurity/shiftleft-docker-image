/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ImmutableSet<E>
/*     */   extends ImmutableCollection<E>
/*     */   implements Set<E>
/*     */ {
/*     */   public static <E> ImmutableSet<E> of() {
/*  76 */     return EmptyImmutableSet.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> of(E element) {
/*  86 */     return new SingletonImmutableSet<E>(element);
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
/*     */   public static <E> ImmutableSet<E> of(E e1, E e2) {
/*  98 */     return create((E[])new Object[] { e1, e2 });
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
/*     */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3) {
/* 110 */     return create((E[])new Object[] { e1, e2, e3 });
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
/*     */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4) {
/* 122 */     return create((E[])new Object[] { e1, e2, e3, e4 });
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
/*     */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5) {
/* 134 */     return create((E[])new Object[] { e1, e2, e3, e4, e5 });
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
/*     */   public static <E> ImmutableSet<E> of(E... elements) {
/* 146 */     Preconditions.checkNotNull(elements);
/* 147 */     switch (elements.length) {
/*     */       case 0:
/* 149 */         return of();
/*     */       case 1:
/* 151 */         return of(elements[0]);
/*     */     } 
/* 153 */     return create(elements);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> copyOf(Iterable<? extends E> elements) {
/* 177 */     if (elements instanceof ImmutableSet && !(elements instanceof ImmutableSortedSet)) {
/*     */ 
/*     */       
/* 180 */       ImmutableSet<E> set = (ImmutableSet)elements;
/* 181 */       return set;
/*     */     } 
/* 183 */     return copyOfInternal(Collections2.toCollection(elements));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> copyOf(Iterator<? extends E> elements) {
/* 194 */     Collection<E> list = Lists.newArrayList(elements);
/* 195 */     return copyOfInternal(list);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <E> ImmutableSet<E> copyOfInternal(Collection<? extends E> collection) {
/* 202 */     switch (collection.size()) {
/*     */       case 0:
/* 204 */         return of();
/*     */       
/*     */       case 1:
/* 207 */         return of(collection.iterator().next());
/*     */     } 
/* 209 */     return create(collection, collection.size());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isHashCodeFast() {
/* 217 */     return false;
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 221 */     if (object == this) {
/* 222 */       return true;
/*     */     }
/* 224 */     if (object instanceof ImmutableSet && isHashCodeFast() && ((ImmutableSet)object).isHashCodeFast() && hashCode() != object.hashCode())
/*     */     {
/*     */ 
/*     */       
/* 228 */       return false;
/*     */     }
/* 230 */     return Collections2.setEquals(this, object);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 234 */     int hashCode = 0;
/* 235 */     for (E o : this) {
/* 236 */       hashCode += o.hashCode();
/*     */     }
/* 238 */     return hashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <E> ImmutableSet<E> create(E... elements) {
/* 246 */     return create(Arrays.asList(elements), elements.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <E> ImmutableSet<E> create(Iterable<? extends E> iterable, int count) {
/* 252 */     int tableSize = Hashing.chooseTableSize(count);
/* 253 */     Object[] table = new Object[tableSize];
/* 254 */     int mask = tableSize - 1;
/*     */     
/* 256 */     List<E> elements = new ArrayList<E>(count);
/* 257 */     int hashCode = 0;
/*     */     
/* 259 */     label21: for (E element : iterable) {
/* 260 */       Preconditions.checkNotNull(element);
/* 261 */       int hash = element.hashCode();
/* 262 */       for (int i = Hashing.smear(hash);; i++) {
/* 263 */         int index = i & mask;
/* 264 */         Object value = table[index];
/* 265 */         if (value == null) {
/*     */           
/* 267 */           table[index] = element;
/* 268 */           elements.add(element);
/* 269 */           hashCode += hash; continue label21;
/*     */         } 
/* 271 */         if (value.equals(element)) {
/*     */           continue label21;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 277 */     if (elements.size() == 1)
/*     */     {
/* 279 */       return new SingletonImmutableSet<E>(elements.get(0), hashCode); } 
/* 280 */     if (tableSize > Hashing.chooseTableSize(elements.size()))
/*     */     {
/* 282 */       return create(elements, elements.size());
/*     */     }
/* 284 */     return new RegularImmutableSet<E>(elements.toArray(), hashCode, table, mask);
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class ArrayImmutableSet<E>
/*     */     extends ImmutableSet<E>
/*     */   {
/*     */     final transient Object[] elements;
/*     */     
/*     */     ArrayImmutableSet(Object[] elements) {
/* 294 */       this.elements = elements;
/*     */     }
/*     */     
/*     */     public int size() {
/* 298 */       return this.elements.length;
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 302 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public UnmodifiableIterator<E> iterator() {
/* 311 */       return Iterators.forArray((E[])this.elements);
/*     */     }
/*     */     
/*     */     public Object[] toArray() {
/* 315 */       Object[] array = new Object[size()];
/* 316 */       System.arraycopy(this.elements, 0, array, 0, size());
/* 317 */       return array;
/*     */     }
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 321 */       int size = size();
/* 322 */       if (array.length < size) {
/* 323 */         array = ObjectArrays.newArray(array, size);
/* 324 */       } else if (array.length > size) {
/* 325 */         array[size] = null;
/*     */       } 
/* 327 */       System.arraycopy(this.elements, 0, array, 0, size);
/* 328 */       return array;
/*     */     }
/*     */     
/*     */     public boolean containsAll(Collection<?> targets) {
/* 332 */       if (targets == this) {
/* 333 */         return true;
/*     */       }
/* 335 */       if (!(targets instanceof ArrayImmutableSet)) {
/* 336 */         return super.containsAll(targets);
/*     */       }
/* 338 */       if (targets.size() > size()) {
/* 339 */         return false;
/*     */       }
/* 341 */       for (Object target : ((ArrayImmutableSet)targets).elements) {
/* 342 */         if (!contains(target)) {
/* 343 */           return false;
/*     */         }
/*     */       } 
/* 346 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract class TransformedImmutableSet<D, E>
/*     */     extends ImmutableSet<E> {
/*     */     final D[] source;
/*     */     final int hashCode;
/*     */     
/*     */     TransformedImmutableSet(D[] source, int hashCode) {
/* 356 */       this.source = source;
/* 357 */       this.hashCode = hashCode;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 363 */       return this.source.length;
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 367 */       return false;
/*     */     }
/*     */     
/*     */     public UnmodifiableIterator<E> iterator() {
/* 371 */       return new AbstractIterator<E>() {
/* 372 */           int index = 0;
/*     */           protected E computeNext() {
/* 374 */             return (this.index < ImmutableSet.TransformedImmutableSet.this.source.length) ? ImmutableSet.TransformedImmutableSet.this.transform(ImmutableSet.TransformedImmutableSet.this.source[this.index++]) : endOfData();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 382 */       return toArray(new Object[size()]);
/*     */     }
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 386 */       int size = size();
/* 387 */       if (array.length < size) {
/* 388 */         array = ObjectArrays.newArray(array, size);
/* 389 */       } else if (array.length > size) {
/* 390 */         array[size] = null;
/*     */       } 
/*     */ 
/*     */       
/* 394 */       T[] arrayOfT = array;
/* 395 */       for (int i = 0; i < this.source.length; i++) {
/* 396 */         arrayOfT[i] = (T)transform(this.source[i]);
/*     */       }
/* 398 */       return array;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/* 402 */       return this.hashCode;
/*     */     }
/*     */     
/*     */     boolean isHashCodeFast() {
/* 406 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     abstract E transform(D param1D);
/*     */   }
/*     */   
/*     */   private static class SerializedForm
/*     */     implements Serializable
/*     */   {
/*     */     final Object[] elements;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(Object[] elements) {
/* 420 */       this.elements = elements;
/*     */     }
/*     */     Object readResolve() {
/* 423 */       return ImmutableSet.of(this.elements);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 429 */     return new SerializedForm(toArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Builder<E> builder() {
/* 437 */     return new Builder<E>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract UnmodifiableIterator<E> iterator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<E>
/*     */     extends ImmutableCollection.Builder<E>
/*     */   {
/* 458 */     final ArrayList<E> contents = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> add(E element) {
/* 476 */       this.contents.add((E)Preconditions.checkNotNull(element));
/* 477 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> add(E... elements) {
/* 490 */       Preconditions.checkNotNull(elements);
/* 491 */       this.contents.ensureCapacity(this.contents.size() + elements.length);
/* 492 */       super.add(elements);
/* 493 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> addAll(Iterable<? extends E> elements) {
/* 506 */       if (elements instanceof Collection) {
/* 507 */         Collection<?> collection = (Collection)elements;
/* 508 */         this.contents.ensureCapacity(this.contents.size() + collection.size());
/*     */       } 
/* 510 */       super.addAll(elements);
/* 511 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 524 */       super.addAll(elements);
/* 525 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableSet<E> build() {
/* 533 */       return ImmutableSet.copyOf(this.contents);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ImmutableSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */