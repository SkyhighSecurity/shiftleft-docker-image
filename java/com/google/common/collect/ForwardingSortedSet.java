/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.Comparator;
/*    */ import java.util.Set;
/*    */ import java.util.SortedSet;
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
/*    */ @GwtCompatible
/*    */ public abstract class ForwardingSortedSet<E>
/*    */   extends ForwardingSet<E>
/*    */   implements SortedSet<E>
/*    */ {
/*    */   public Comparator<? super E> comparator() {
/* 40 */     return delegate().comparator();
/*    */   }
/*    */   
/*    */   public E first() {
/* 44 */     return delegate().first();
/*    */   }
/*    */   
/*    */   public SortedSet<E> headSet(E toElement) {
/* 48 */     return delegate().headSet(toElement);
/*    */   }
/*    */   
/*    */   public E last() {
/* 52 */     return delegate().last();
/*    */   }
/*    */   
/*    */   public SortedSet<E> subSet(E fromElement, E toElement) {
/* 56 */     return delegate().subSet(fromElement, toElement);
/*    */   }
/*    */   
/*    */   public SortedSet<E> tailSet(E fromElement) {
/* 60 */     return delegate().tailSet(fromElement);
/*    */   }
/*    */   
/*    */   protected abstract SortedSet<E> delegate();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ForwardingSortedSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */