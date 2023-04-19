/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible
/*     */ public abstract class ImmutableCollection<E>
/*     */   implements Collection<E>, Serializable
/*     */ {
/*  41 */   static final ImmutableCollection<Object> EMPTY_IMMUTABLE_COLLECTION = new EmptyImmutableCollection();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/*  52 */     Object[] newArray = new Object[size()];
/*  53 */     return toArray(newArray);
/*     */   }
/*     */   
/*     */   public <T> T[] toArray(T[] other) {
/*  57 */     int size = size();
/*  58 */     if (other.length < size) {
/*  59 */       other = ObjectArrays.newArray(other, size);
/*  60 */     } else if (other.length > size) {
/*  61 */       other[size] = null;
/*     */     } 
/*     */ 
/*     */     
/*  65 */     T[] arrayOfT = other;
/*  66 */     int index = 0;
/*  67 */     for (E element : this) {
/*  68 */       arrayOfT[index++] = (T)element;
/*     */     }
/*  70 */     return other;
/*     */   }
/*     */   
/*     */   public boolean contains(@Nullable Object object) {
/*  74 */     if (object == null) {
/*  75 */       return false;
/*     */     }
/*  77 */     for (E element : this) {
/*  78 */       if (element.equals(object)) {
/*  79 */         return true;
/*     */       }
/*     */     } 
/*  82 */     return false;
/*     */   }
/*     */   
/*     */   public boolean containsAll(Collection<?> targets) {
/*  86 */     for (Object target : targets) {
/*  87 */       if (!contains(target)) {
/*  88 */         return false;
/*     */       }
/*     */     } 
/*  91 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  95 */     return (size() == 0);
/*     */   }
/*     */   
/*     */   public String toString() {
/*  99 */     StringBuilder sb = (new StringBuilder(size() * 16)).append('[');
/* 100 */     Collections2.standardJoiner.appendTo(sb, this);
/* 101 */     return sb.append(']').toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean add(E e) {
/* 110 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean remove(Object object) {
/* 119 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean addAll(Collection<? extends E> newElements) {
/* 128 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean removeAll(Collection<?> oldElements) {
/* 137 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean retainAll(Collection<?> elementsToKeep) {
/* 146 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void clear() {
/* 155 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   private static class EmptyImmutableCollection
/*     */     extends ImmutableCollection<Object> {
/*     */     public int size() {
/* 161 */       return 0;
/*     */     }
/*     */     private EmptyImmutableCollection() {}
/*     */     public boolean isEmpty() {
/* 165 */       return true;
/*     */     }
/*     */     
/*     */     public boolean contains(@Nullable Object object) {
/* 169 */       return false;
/*     */     }
/*     */     
/*     */     public UnmodifiableIterator<Object> iterator() {
/* 173 */       return Iterators.EMPTY_ITERATOR;
/*     */     }
/*     */     
/* 176 */     private static final Object[] EMPTY_ARRAY = new Object[0];
/*     */     
/*     */     public Object[] toArray() {
/* 179 */       return EMPTY_ARRAY;
/*     */     }
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 183 */       if (array.length > 0) {
/* 184 */         array[0] = null;
/*     */       }
/* 186 */       return array;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ArrayImmutableCollection<E>
/*     */     extends ImmutableCollection<E> {
/*     */     private final E[] elements;
/*     */     
/*     */     ArrayImmutableCollection(E[] elements) {
/* 195 */       this.elements = elements;
/*     */     }
/*     */     
/*     */     public int size() {
/* 199 */       return this.elements.length;
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 203 */       return false;
/*     */     }
/*     */     
/*     */     public UnmodifiableIterator<E> iterator() {
/* 207 */       return Iterators.forArray(this.elements);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SerializedForm
/*     */     implements Serializable
/*     */   {
/*     */     final Object[] elements;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(Object[] elements) {
/* 218 */       this.elements = elements;
/*     */     }
/*     */     Object readResolve() {
/* 221 */       return (this.elements.length == 0) ? ImmutableCollection.EMPTY_IMMUTABLE_COLLECTION : new ImmutableCollection.ArrayImmutableCollection(Platform.clone(this.elements));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 229 */     return new SerializedForm(toArray());
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
/*     */   public abstract UnmodifiableIterator<E> iterator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static abstract class Builder<E>
/*     */   {
/*     */     public abstract Builder<E> add(E param1E);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> add(E... elements) {
/* 261 */       Preconditions.checkNotNull(elements);
/* 262 */       for (E element : elements) {
/* 263 */         add(element);
/*     */       }
/* 265 */       return this;
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
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> addAll(Iterable<? extends E> elements) {
/* 281 */       Preconditions.checkNotNull(elements);
/* 282 */       for (E element : elements) {
/* 283 */         add(element);
/*     */       }
/* 285 */       return this;
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
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 301 */       Preconditions.checkNotNull(elements);
/* 302 */       while (elements.hasNext()) {
/* 303 */         add(elements.next());
/*     */       }
/* 305 */       return this;
/*     */     }
/*     */     
/*     */     public abstract ImmutableCollection<E> build();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ImmutableCollection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */