/*    */ package org.springframework.util;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.NoSuchElementException;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CompositeIterator<E>
/*    */   implements Iterator<E>
/*    */ {
/* 37 */   private final Set<Iterator<E>> iterators = new LinkedHashSet<Iterator<E>>();
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean inUse = false;
/*    */ 
/*    */ 
/*    */   
/*    */   public void add(Iterator<E> iterator) {
/* 46 */     Assert.state(!this.inUse, "You can no longer add iterators to a composite iterator that's already in use");
/* 47 */     if (this.iterators.contains(iterator)) {
/* 48 */       throw new IllegalArgumentException("You cannot add the same iterator twice");
/*    */     }
/* 50 */     this.iterators.add(iterator);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasNext() {
/* 55 */     this.inUse = true;
/* 56 */     for (Iterator<E> iterator : this.iterators) {
/* 57 */       if (iterator.hasNext()) {
/* 58 */         return true;
/*    */       }
/*    */     } 
/* 61 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public E next() {
/* 66 */     this.inUse = true;
/* 67 */     for (Iterator<E> iterator : this.iterators) {
/* 68 */       if (iterator.hasNext()) {
/* 69 */         return iterator.next();
/*    */       }
/*    */     } 
/* 72 */     throw new NoSuchElementException("All iterators exhausted");
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove() {
/* 77 */     throw new UnsupportedOperationException("CompositeIterator does not support remove()");
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\CompositeIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */