/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.RandomAccess;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ public abstract class ImmutableList<E>
/*     */   extends ImmutableCollection<E>
/*     */   implements List<E>, RandomAccess
/*     */ {
/*     */   public static <E> ImmutableList<E> of() {
/*  65 */     return EmptyImmutableList.INSTANCE;
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
/*     */   public static <E> ImmutableList<E> of(E element) {
/*  77 */     return new SingletonImmutableList<E>(element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2) {
/*  86 */     return new RegularImmutableList<E>(copyIntoArray(new Object[] { e1, e2 }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3) {
/*  95 */     return new RegularImmutableList<E>(copyIntoArray(new Object[] { e1, e2, e3 }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4) {
/* 104 */     return new RegularImmutableList<E>(copyIntoArray(new Object[] { e1, e2, e3, e4 }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5) {
/* 113 */     return new RegularImmutableList<E>(copyIntoArray(new Object[] { e1, e2, e3, e4, e5 }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
/* 122 */     return new RegularImmutableList<E>(copyIntoArray(new Object[] { e1, e2, e3, e4, e5, e6 }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
/* 132 */     return new RegularImmutableList<E>(copyIntoArray(new Object[] { e1, e2, e3, e4, e5, e6, e7 }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
/* 143 */     return new RegularImmutableList<E>(copyIntoArray(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8 }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
/* 154 */     return new RegularImmutableList<E>(copyIntoArray(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9 }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
/* 165 */     return new RegularImmutableList<E>(copyIntoArray(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9, e10 }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11) {
/* 176 */     return new RegularImmutableList<E>(copyIntoArray(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11 }));
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
/*     */   public static <E> ImmutableList<E> of(E... elements) {
/* 189 */     Preconditions.checkNotNull(elements);
/* 190 */     switch (elements.length) {
/*     */       case 0:
/* 192 */         return of();
/*     */       case 1:
/* 194 */         return new SingletonImmutableList<E>(elements[0]);
/*     */     } 
/* 196 */     return new RegularImmutableList<E>(copyIntoArray((Object[])elements));
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
/*     */   public static <E> ImmutableList<E> copyOf(Iterable<? extends E> elements) {
/* 216 */     if (elements instanceof ImmutableList) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 222 */       ImmutableList<E> list = (ImmutableList)elements;
/* 223 */       return list;
/* 224 */     }  if (elements instanceof Collection) {
/*     */       
/* 226 */       Collection<? extends E> coll = (Collection<? extends E>)elements;
/* 227 */       return copyOfInternal(coll);
/*     */     } 
/* 229 */     return copyOfInternal(Lists.newArrayList(elements));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> copyOf(Iterator<? extends E> elements) {
/* 239 */     return copyOfInternal(Lists.newArrayList(elements));
/*     */   }
/*     */ 
/*     */   
/*     */   private static <E> ImmutableList<E> copyOfInternal(ArrayList<? extends E> list) {
/* 244 */     switch (list.size()) {
/*     */       case 0:
/* 246 */         return of();
/*     */       case 1:
/* 248 */         return new SingletonImmutableList<E>(list.iterator().next());
/*     */     } 
/* 250 */     return new RegularImmutableList<E>(nullChecked(list.toArray()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object[] nullChecked(Object[] array) {
/* 261 */     for (int i = 0, len = array.length; i < len; i++) {
/* 262 */       if (array[i] == null) {
/* 263 */         throw new NullPointerException("at index " + i);
/*     */       }
/*     */     } 
/* 266 */     return array;
/*     */   }
/*     */ 
/*     */   
/*     */   private static <E> ImmutableList<E> copyOfInternal(Collection<? extends E> collection) {
/* 271 */     int size = collection.size();
/* 272 */     return (size == 0) ? of() : createFromIterable(collection, size);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean addAll(int index, Collection<? extends E> newElements) {
/* 305 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final E set(int index, E element) {
/* 314 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void add(int index, E element) {
/* 323 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final E remove(int index) {
/* 332 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   private static Object[] copyIntoArray(Object... source) {
/* 336 */     Object[] array = new Object[source.length];
/* 337 */     int index = 0;
/* 338 */     for (Object element : source) {
/* 339 */       if (element == null) {
/* 340 */         throw new NullPointerException("at index " + index);
/*     */       }
/* 342 */       array[index++] = element;
/*     */     } 
/* 344 */     return array;
/*     */   }
/*     */ 
/*     */   
/*     */   private static <E> ImmutableList<E> createFromIterable(Iterable<? extends E> source, int estimatedSize) {
/* 349 */     Object[] array = new Object[estimatedSize];
/* 350 */     int index = 0;
/*     */     
/* 352 */     for (E element : source) {
/* 353 */       if (index == estimatedSize) {
/*     */         
/* 355 */         estimatedSize = (estimatedSize / 2 + 1) * 3;
/* 356 */         array = copyOf(array, estimatedSize);
/*     */       } 
/* 358 */       if (element == null) {
/* 359 */         throw new NullPointerException("at index " + index);
/*     */       }
/* 361 */       array[index++] = element;
/*     */     } 
/*     */     
/* 364 */     if (index == 0)
/* 365 */       return of(); 
/* 366 */     if (index == 1) {
/*     */ 
/*     */       
/* 369 */       E element = (E)array[0];
/* 370 */       return of(element);
/*     */     } 
/*     */     
/* 373 */     if (index != estimatedSize) {
/* 374 */       array = copyOf(array, index);
/*     */     }
/*     */     
/* 377 */     return new RegularImmutableList<E>(array, 0, index);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Object[] copyOf(Object[] oldArray, int newSize) {
/* 382 */     Object[] newArray = new Object[newSize];
/* 383 */     System.arraycopy(oldArray, 0, newArray, 0, Math.min(oldArray.length, newSize));
/*     */     
/* 385 */     return newArray;
/*     */   }
/*     */   
/*     */   private static class SerializedForm
/*     */     implements Serializable
/*     */   {
/*     */     final Object[] elements;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(Object[] elements) {
/* 395 */       this.elements = elements;
/*     */     }
/*     */     Object readResolve() {
/* 398 */       return ImmutableList.of(this.elements);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 405 */     throw new InvalidObjectException("Use SerializedForm");
/*     */   }
/*     */   
/*     */   Object writeReplace() {
/* 409 */     return new SerializedForm(toArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Builder<E> builder() {
/* 417 */     return new Builder<E>();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract UnmodifiableIterator<E> iterator();
/*     */ 
/*     */   
/*     */   public abstract int indexOf(@Nullable Object paramObject);
/*     */ 
/*     */   
/*     */   public abstract int lastIndexOf(@Nullable Object paramObject);
/*     */ 
/*     */   
/*     */   public abstract ImmutableList<E> subList(int paramInt1, int paramInt2);
/*     */ 
/*     */   
/*     */   public static final class Builder<E>
/*     */     extends ImmutableCollection.Builder<E>
/*     */   {
/* 437 */     private final ArrayList<E> contents = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 453 */       this.contents.add((E)Preconditions.checkNotNull(element));
/* 454 */       return this;
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
/*     */     public Builder<E> addAll(Iterable<? extends E> elements) {
/* 466 */       if (elements instanceof Collection) {
/* 467 */         Collection<?> collection = (Collection)elements;
/* 468 */         this.contents.ensureCapacity(this.contents.size() + collection.size());
/*     */       } 
/* 470 */       super.addAll(elements);
/* 471 */       return this;
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
/*     */     public Builder<E> add(E... elements) {
/* 483 */       Preconditions.checkNotNull(elements);
/* 484 */       this.contents.ensureCapacity(this.contents.size() + elements.length);
/* 485 */       super.add(elements);
/* 486 */       return this;
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
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 498 */       super.addAll(elements);
/* 499 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableList<E> build() {
/* 507 */       return ImmutableList.copyOf(this.contents);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ImmutableList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */