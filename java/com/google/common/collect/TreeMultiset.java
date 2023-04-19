/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
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
/*     */ 
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
/*     */ public final class TreeMultiset<E>
/*     */   extends AbstractMapBasedMultiset<E>
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <E extends Comparable> TreeMultiset<E> create() {
/*  70 */     return new TreeMultiset<E>();
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
/*     */   public static <E> TreeMultiset<E> create(Comparator<? super E> comparator) {
/*  87 */     return new TreeMultiset<E>(comparator);
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
/*     */   public static <E extends Comparable> TreeMultiset<E> create(Iterable<? extends E> elements) {
/* 101 */     TreeMultiset<E> multiset = create();
/* 102 */     Iterables.addAll(multiset, elements);
/* 103 */     return multiset;
/*     */   }
/*     */   
/*     */   private TreeMultiset() {
/* 107 */     super(new TreeMap<E, AtomicInteger>());
/*     */   }
/*     */   
/*     */   private TreeMultiset(Comparator<? super E> comparator) {
/* 111 */     super(new TreeMap<E, AtomicInteger>(comparator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedSet<E> elementSet() {
/* 121 */     return (SortedSet<E>)super.elementSet();
/*     */   }
/*     */   
/*     */   public int count(@Nullable Object element) {
/*     */     try {
/* 126 */       return super.count(element);
/* 127 */     } catch (NullPointerException e) {
/* 128 */       return 0;
/* 129 */     } catch (ClassCastException e) {
/* 130 */       return 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   Set<E> createElementSet() {
/* 135 */     return new SortedMapBasedElementSet((SortedMap<E, AtomicInteger>)backingMap());
/*     */   }
/*     */   
/*     */   private class SortedMapBasedElementSet
/*     */     extends AbstractMapBasedMultiset<E>.MapBasedElementSet
/*     */     implements SortedSet<E>
/*     */   {
/*     */     SortedMapBasedElementSet(SortedMap<E, AtomicInteger> map) {
/* 143 */       super(map);
/*     */     }
/*     */     
/*     */     SortedMap<E, AtomicInteger> sortedMap() {
/* 147 */       return (SortedMap<E, AtomicInteger>)getMap();
/*     */     }
/*     */     
/*     */     public Comparator<? super E> comparator() {
/* 151 */       return sortedMap().comparator();
/*     */     }
/*     */     
/*     */     public E first() {
/* 155 */       return sortedMap().firstKey();
/*     */     }
/*     */     
/*     */     public E last() {
/* 159 */       return sortedMap().lastKey();
/*     */     }
/*     */     
/*     */     public SortedSet<E> headSet(E toElement) {
/* 163 */       return new SortedMapBasedElementSet(sortedMap().headMap(toElement));
/*     */     }
/*     */     
/*     */     public SortedSet<E> subSet(E fromElement, E toElement) {
/* 167 */       return new SortedMapBasedElementSet(sortedMap().subMap(fromElement, toElement));
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSet<E> tailSet(E fromElement) {
/* 172 */       return new SortedMapBasedElementSet(sortedMap().tailMap(fromElement));
/*     */     }
/*     */     
/*     */     public boolean remove(Object element) {
/*     */       try {
/* 177 */         return super.remove(element);
/* 178 */       } catch (NullPointerException e) {
/* 179 */         return false;
/* 180 */       } catch (ClassCastException e) {
/* 181 */         return false;
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
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 198 */     stream.defaultWriteObject();
/* 199 */     stream.writeObject(elementSet().comparator());
/* 200 */     Serialization.writeMultiset(this, stream);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 205 */     stream.defaultReadObject();
/*     */     
/* 207 */     Comparator<? super E> comparator = (Comparator<? super E>)stream.readObject();
/*     */     
/* 209 */     setBackingMap(new TreeMap<E, AtomicInteger>(comparator));
/* 210 */     Serialization.populateMultiset(this, stream);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\TreeMultiset.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */