/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.Collection;
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
/*     */ @GwtCompatible
/*     */ public final class Collections2
/*     */ {
/*     */   static boolean containsAll(Collection<?> self, Collection<?> c) {
/*  59 */     Preconditions.checkNotNull(self);
/*  60 */     for (Object o : c) {
/*  61 */       if (!self.contains(o)) {
/*  62 */         return false;
/*     */       }
/*     */     } 
/*  65 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> Collection<E> toCollection(Iterable<E> iterable) {
/*  74 */     return (iterable instanceof Collection) ? (Collection<E>)iterable : Lists.<E>newArrayList(iterable);
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
/*     */   public static <E> Collection<E> filter(Collection<E> unfiltered, Predicate<? super E> predicate) {
/* 102 */     if (unfiltered instanceof FilteredCollection)
/*     */     {
/*     */       
/* 105 */       return ((FilteredCollection<E>)unfiltered).createCombined(predicate);
/*     */     }
/*     */     
/* 108 */     return new FilteredCollection<E>((Collection<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
/*     */   }
/*     */   
/*     */   static class FilteredCollection<E>
/*     */     implements Collection<E>
/*     */   {
/*     */     final Collection<E> unfiltered;
/*     */     final Predicate<? super E> predicate;
/*     */     
/*     */     FilteredCollection(Collection<E> unfiltered, Predicate<? super E> predicate) {
/* 118 */       this.unfiltered = unfiltered;
/* 119 */       this.predicate = predicate;
/*     */     }
/*     */     
/*     */     FilteredCollection<E> createCombined(Predicate<? super E> newPredicate) {
/* 123 */       return new FilteredCollection(this.unfiltered, Predicates.and(this.predicate, newPredicate));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean add(E element) {
/* 129 */       Preconditions.checkArgument(this.predicate.apply(element));
/* 130 */       return this.unfiltered.add(element);
/*     */     }
/*     */     
/*     */     public boolean addAll(Collection<? extends E> collection) {
/* 134 */       for (E element : collection) {
/* 135 */         Preconditions.checkArgument(this.predicate.apply(element));
/*     */       }
/* 137 */       return this.unfiltered.addAll(collection);
/*     */     }
/*     */     
/*     */     public void clear() {
/* 141 */       Iterables.removeIf(this.unfiltered, this.predicate);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object element) {
/*     */       try {
/* 149 */         E e = (E)element;
/* 150 */         return (this.predicate.apply(e) && this.unfiltered.contains(element));
/* 151 */       } catch (NullPointerException e) {
/* 152 */         return false;
/* 153 */       } catch (ClassCastException e) {
/* 154 */         return false;
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean containsAll(Collection<?> collection) {
/* 159 */       for (Object element : collection) {
/* 160 */         if (!contains(element)) {
/* 161 */           return false;
/*     */         }
/*     */       } 
/* 164 */       return true;
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 168 */       return !Iterators.any(this.unfiltered.iterator(), this.predicate);
/*     */     }
/*     */     
/*     */     public Iterator<E> iterator() {
/* 172 */       return Iterators.filter(this.unfiltered.iterator(), this.predicate);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean remove(Object element) {
/*     */       try {
/* 180 */         E e = (E)element;
/* 181 */         return (this.predicate.apply(e) && this.unfiltered.remove(element));
/* 182 */       } catch (NullPointerException e) {
/* 183 */         return false;
/* 184 */       } catch (ClassCastException e) {
/* 185 */         return false;
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean removeAll(final Collection<?> collection) {
/* 190 */       Preconditions.checkNotNull(collection);
/* 191 */       Predicate<E> combinedPredicate = new Predicate<E>() {
/*     */           public boolean apply(E input) {
/* 193 */             return (Collections2.FilteredCollection.this.predicate.apply(input) && collection.contains(input));
/*     */           }
/*     */         };
/* 196 */       return Iterables.removeIf(this.unfiltered, combinedPredicate);
/*     */     }
/*     */     
/*     */     public boolean retainAll(final Collection<?> collection) {
/* 200 */       Preconditions.checkNotNull(collection);
/* 201 */       Predicate<E> combinedPredicate = new Predicate<E>() {
/*     */           public boolean apply(E input) {
/* 203 */             return (Collections2.FilteredCollection.this.predicate.apply(input) && !collection.contains(input));
/*     */           }
/*     */         };
/* 206 */       return Iterables.removeIf(this.unfiltered, combinedPredicate);
/*     */     }
/*     */     
/*     */     public int size() {
/* 210 */       return Iterators.size(iterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 215 */       return Lists.<E>newArrayList(iterator()).toArray();
/*     */     }
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 219 */       return (T[])Lists.<E>newArrayList(iterator()).toArray((Object[])array);
/*     */     }
/*     */     
/*     */     public String toString() {
/* 223 */       return Iterators.toString(iterator());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <F, T> Collection<T> transform(Collection<F> fromCollection, Function<? super F, T> function) {
/* 244 */     return new TransformedCollection<F, T>(fromCollection, function);
/*     */   }
/*     */   
/*     */   static class TransformedCollection<F, T>
/*     */     extends AbstractCollection<T> {
/*     */     final Collection<F> fromCollection;
/*     */     final Function<? super F, ? extends T> function;
/*     */     
/*     */     TransformedCollection(Collection<F> fromCollection, Function<? super F, ? extends T> function) {
/* 253 */       this.fromCollection = (Collection<F>)Preconditions.checkNotNull(fromCollection);
/* 254 */       this.function = (Function<? super F, ? extends T>)Preconditions.checkNotNull(function);
/*     */     }
/*     */     
/*     */     public void clear() {
/* 258 */       this.fromCollection.clear();
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 262 */       return this.fromCollection.isEmpty();
/*     */     }
/*     */     
/*     */     public Iterator<T> iterator() {
/* 266 */       return Iterators.transform(this.fromCollection.iterator(), this.function);
/*     */     }
/*     */     
/*     */     public int size() {
/* 270 */       return this.fromCollection.size();
/*     */     }
/*     */   }
/*     */   
/*     */   static boolean setEquals(Set<?> thisSet, @Nullable Object object) {
/* 275 */     if (object == thisSet) {
/* 276 */       return true;
/*     */     }
/* 278 */     if (object instanceof Set) {
/* 279 */       Set<?> thatSet = (Set)object;
/* 280 */       return (thisSet.size() == thatSet.size() && thisSet.containsAll(thatSet));
/*     */     } 
/*     */     
/* 283 */     return false;
/*     */   }
/*     */   
/* 286 */   static final Joiner standardJoiner = Joiner.on(", ");
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\Collections2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */