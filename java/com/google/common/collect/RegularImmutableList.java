/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ final class RegularImmutableList<E>
/*     */   extends ImmutableList<E>
/*     */ {
/*     */   private final transient int offset;
/*     */   private final transient int size;
/*     */   private final transient Object[] array;
/*     */   
/*     */   RegularImmutableList(Object[] array, int offset, int size) {
/*  41 */     this.offset = offset;
/*  42 */     this.size = size;
/*  43 */     this.array = array;
/*     */   }
/*     */   
/*     */   RegularImmutableList(Object[] array) {
/*  47 */     this(array, 0, array.length);
/*     */   }
/*     */   
/*     */   public int size() {
/*  51 */     return this.size;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  55 */     return false;
/*     */   }
/*     */   
/*     */   public boolean contains(Object target) {
/*  59 */     return (indexOf(target) != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/*  65 */     return Iterators.forArray((E[])this.array, this.offset, this.size);
/*     */   }
/*     */   
/*     */   public Object[] toArray() {
/*  69 */     Object[] newArray = new Object[size()];
/*  70 */     System.arraycopy(this.array, this.offset, newArray, 0, this.size);
/*  71 */     return newArray;
/*     */   }
/*     */   
/*     */   public <T> T[] toArray(T[] other) {
/*  75 */     if (other.length < this.size) {
/*  76 */       other = ObjectArrays.newArray(other, this.size);
/*  77 */     } else if (other.length > this.size) {
/*  78 */       other[this.size] = null;
/*     */     } 
/*  80 */     System.arraycopy(this.array, this.offset, other, 0, this.size);
/*  81 */     return other;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E get(int index) {
/*  87 */     Preconditions.checkElementIndex(index, this.size);
/*  88 */     return (E)this.array[index + this.offset];
/*     */   }
/*     */   
/*     */   public int indexOf(Object target) {
/*  92 */     if (target != null) {
/*  93 */       for (int i = this.offset; i < this.offset + this.size; i++) {
/*  94 */         if (this.array[i].equals(target)) {
/*  95 */           return i - this.offset;
/*     */         }
/*     */       } 
/*     */     }
/*  99 */     return -1;
/*     */   }
/*     */   
/*     */   public int lastIndexOf(Object target) {
/* 103 */     if (target != null) {
/* 104 */       for (int i = this.offset + this.size - 1; i >= this.offset; i--) {
/* 105 */         if (this.array[i].equals(target)) {
/* 106 */           return i - this.offset;
/*     */         }
/*     */       } 
/*     */     }
/* 110 */     return -1;
/*     */   }
/*     */   
/*     */   public ImmutableList<E> subList(int fromIndex, int toIndex) {
/* 114 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, this.size);
/* 115 */     return (fromIndex == toIndex) ? ImmutableList.<E>of() : new RegularImmutableList(this.array, this.offset + fromIndex, toIndex - fromIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator() {
/* 122 */     return listIterator(0);
/*     */   }
/*     */   
/*     */   public ListIterator<E> listIterator(final int start) {
/* 126 */     Preconditions.checkPositionIndex(start, this.size);
/*     */     
/* 128 */     return new ListIterator<E>() {
/* 129 */         int index = start;
/*     */         
/*     */         public boolean hasNext() {
/* 132 */           return (this.index < RegularImmutableList.this.size);
/*     */         }
/*     */         public boolean hasPrevious() {
/* 135 */           return (this.index > 0);
/*     */         }
/*     */         
/*     */         public int nextIndex() {
/* 139 */           return this.index;
/*     */         }
/*     */         public int previousIndex() {
/* 142 */           return this.index - 1;
/*     */         }
/*     */         
/*     */         public E next() {
/*     */           E result;
/*     */           try {
/* 148 */             result = RegularImmutableList.this.get(this.index);
/* 149 */           } catch (IndexOutOfBoundsException rethrown) {
/* 150 */             throw new NoSuchElementException();
/*     */           } 
/* 152 */           this.index++;
/* 153 */           return result;
/*     */         }
/*     */         public E previous() {
/*     */           E result;
/*     */           try {
/* 158 */             result = RegularImmutableList.this.get(this.index - 1);
/* 159 */           } catch (IndexOutOfBoundsException rethrown) {
/* 160 */             throw new NoSuchElementException();
/*     */           } 
/* 162 */           this.index--;
/* 163 */           return result;
/*     */         }
/*     */         
/*     */         public void set(E o) {
/* 167 */           throw new UnsupportedOperationException();
/*     */         }
/*     */         public void add(E o) {
/* 170 */           throw new UnsupportedOperationException();
/*     */         }
/*     */         public void remove() {
/* 173 */           throw new UnsupportedOperationException();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 179 */     if (object == this) {
/* 180 */       return true;
/*     */     }
/* 182 */     if (!(object instanceof List)) {
/* 183 */       return false;
/*     */     }
/*     */     
/* 186 */     List<?> that = (List)object;
/* 187 */     if (size() != that.size()) {
/* 188 */       return false;
/*     */     }
/*     */     
/* 191 */     int index = this.offset;
/* 192 */     if (object instanceof RegularImmutableList) {
/* 193 */       RegularImmutableList<?> other = (RegularImmutableList)object;
/* 194 */       for (int i = other.offset; i < other.offset + other.size; i++) {
/* 195 */         if (!this.array[index++].equals(other.array[i])) {
/* 196 */           return false;
/*     */         }
/*     */       } 
/*     */     } else {
/* 200 */       for (Object element : that) {
/* 201 */         if (!this.array[index++].equals(element)) {
/* 202 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/* 206 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 212 */     int hashCode = 1;
/* 213 */     for (int i = this.offset; i < this.offset + this.size; i++) {
/* 214 */       hashCode = 31 * hashCode + this.array[i].hashCode();
/*     */     }
/* 216 */     return hashCode;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 220 */     StringBuilder sb = new StringBuilder(size() * 16);
/* 221 */     sb.append('[').append(this.array[this.offset]);
/* 222 */     for (int i = this.offset + 1; i < this.offset + this.size; i++) {
/* 223 */       sb.append(", ").append(this.array[i]);
/*     */     }
/* 225 */     return sb.append(']').toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\RegularImmutableList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */