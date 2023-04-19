/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ final class RegularImmutableSortedSet<E>
/*     */   extends ImmutableSortedSet<E>
/*     */ {
/*     */   private final Object[] elements;
/*     */   private final int fromIndex;
/*     */   private final int toIndex;
/*     */   
/*     */   RegularImmutableSortedSet(Object[] elements, Comparator<? super E> comparator) {
/*  54 */     super(comparator);
/*  55 */     this.elements = elements;
/*  56 */     this.fromIndex = 0;
/*  57 */     this.toIndex = elements.length;
/*     */   }
/*     */ 
/*     */   
/*     */   RegularImmutableSortedSet(Object[] elements, Comparator<? super E> comparator, int fromIndex, int toIndex) {
/*  62 */     super(comparator);
/*  63 */     this.elements = elements;
/*  64 */     this.fromIndex = fromIndex;
/*  65 */     this.toIndex = toIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/*  71 */     return Iterators.forArray((E[])this.elements, this.fromIndex, size());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  76 */     return false;
/*     */   }
/*     */   
/*     */   public int size() {
/*  80 */     return this.toIndex - this.fromIndex;
/*     */   }
/*     */   
/*     */   public boolean contains(Object o) {
/*  84 */     if (o == null) {
/*  85 */       return false;
/*     */     }
/*     */     try {
/*  88 */       return (binarySearch(o) >= 0);
/*  89 */     } catch (ClassCastException e) {
/*  90 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> targets) {
/*  97 */     if (!hasSameComparator(targets, comparator()) || targets.size() <= 1) {
/*  98 */       return super.containsAll(targets);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     int i = this.fromIndex;
/* 106 */     Iterator<?> iterator = targets.iterator();
/* 107 */     Object target = iterator.next();
/*     */     
/*     */     while (true) {
/* 110 */       if (i >= this.toIndex) {
/* 111 */         return false;
/*     */       }
/*     */       
/* 114 */       int cmp = unsafeCompare(this.elements[i], target);
/*     */       
/* 116 */       if (cmp < 0) {
/* 117 */         i++; continue;
/* 118 */       }  if (cmp == 0) {
/* 119 */         if (!iterator.hasNext()) {
/* 120 */           return true;
/*     */         }
/* 122 */         target = iterator.next();
/* 123 */         i++; continue;
/* 124 */       }  if (cmp > 0) {
/* 125 */         return false;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private int binarySearch(Object key) {
/* 131 */     int lower = this.fromIndex;
/* 132 */     int upper = this.toIndex - 1;
/*     */     
/* 134 */     while (lower <= upper) {
/* 135 */       int middle = lower + (upper - lower) / 2;
/* 136 */       int c = unsafeCompare(key, this.elements[middle]);
/* 137 */       if (c < 0) {
/* 138 */         upper = middle - 1; continue;
/* 139 */       }  if (c > 0) {
/* 140 */         lower = middle + 1; continue;
/*     */       } 
/* 142 */       return middle;
/*     */     } 
/*     */ 
/*     */     
/* 146 */     return -lower - 1;
/*     */   }
/*     */   
/*     */   public Object[] toArray() {
/* 150 */     Object[] array = new Object[size()];
/* 151 */     System.arraycopy(this.elements, this.fromIndex, array, 0, size());
/* 152 */     return array;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] array) {
/* 157 */     int size = size();
/* 158 */     if (array.length < size) {
/* 159 */       array = ObjectArrays.newArray(array, size);
/* 160 */     } else if (array.length > size) {
/* 161 */       array[size] = null;
/*     */     } 
/* 163 */     System.arraycopy(this.elements, this.fromIndex, array, 0, size);
/* 164 */     return array;
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 168 */     if (object == this) {
/* 169 */       return true;
/*     */     }
/* 171 */     if (!(object instanceof Set)) {
/* 172 */       return false;
/*     */     }
/*     */     
/* 175 */     Set<?> that = (Set)object;
/* 176 */     if (size() != that.size()) {
/* 177 */       return false;
/*     */     }
/*     */     
/* 180 */     if (hasSameComparator(that, this.comparator)) {
/* 181 */       Iterator<?> iterator = that.iterator();
/*     */       try {
/* 183 */         for (int i = this.fromIndex; i < this.toIndex; i++) {
/* 184 */           Object otherElement = iterator.next();
/* 185 */           if (otherElement == null || unsafeCompare(this.elements[i], otherElement) != 0)
/*     */           {
/* 187 */             return false;
/*     */           }
/*     */         } 
/* 190 */         return true;
/* 191 */       } catch (ClassCastException e) {
/* 192 */         return false;
/* 193 */       } catch (NoSuchElementException e) {
/* 194 */         return false;
/*     */       } 
/*     */     } 
/* 197 */     return containsAll(that);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 203 */     int hash = 0;
/* 204 */     for (int i = this.fromIndex; i < this.toIndex; i++) {
/* 205 */       hash += this.elements[i].hashCode();
/*     */     }
/* 207 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E first() {
/* 213 */     return (E)this.elements[this.fromIndex];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E last() {
/* 219 */     return (E)this.elements[this.toIndex - 1];
/*     */   }
/*     */   
/*     */   ImmutableSortedSet<E> headSetImpl(E toElement) {
/* 223 */     return createSubset(this.fromIndex, findSubsetIndex(toElement));
/*     */   }
/*     */   
/*     */   ImmutableSortedSet<E> subSetImpl(E fromElement, E toElement) {
/* 227 */     return createSubset(findSubsetIndex(fromElement), findSubsetIndex(toElement));
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> tailSetImpl(E fromElement) {
/* 232 */     return createSubset(findSubsetIndex(fromElement), this.toIndex);
/*     */   }
/*     */   
/*     */   private int findSubsetIndex(E element) {
/* 236 */     int index = binarySearch(element);
/* 237 */     return (index >= 0) ? index : (-index - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   private ImmutableSortedSet<E> createSubset(int newFromIndex, int newToIndex) {
/* 242 */     if (newFromIndex < newToIndex) {
/* 243 */       return new RegularImmutableSortedSet(this.elements, this.comparator, newFromIndex, newToIndex);
/*     */     }
/*     */     
/* 246 */     return emptySet(this.comparator);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean hasPartialArray() {
/* 251 */     return (this.fromIndex != 0 || this.toIndex != this.elements.length);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\RegularImmutableSortedSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */