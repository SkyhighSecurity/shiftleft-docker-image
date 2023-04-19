/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Iterator;
/*    */ import java.util.ListIterator;
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
/*    */ @GwtCompatible
/*    */ public abstract class ForwardingListIterator<E>
/*    */   extends ForwardingIterator<E>
/*    */   implements ListIterator<E>
/*    */ {
/*    */   public void add(E element) {
/* 39 */     delegate().add(element);
/*    */   }
/*    */   
/*    */   public boolean hasPrevious() {
/* 43 */     return delegate().hasPrevious();
/*    */   }
/*    */   
/*    */   public int nextIndex() {
/* 47 */     return delegate().nextIndex();
/*    */   }
/*    */   
/*    */   public E previous() {
/* 51 */     return delegate().previous();
/*    */   }
/*    */   
/*    */   public int previousIndex() {
/* 55 */     return delegate().previousIndex();
/*    */   }
/*    */   
/*    */   public void set(E element) {
/* 59 */     delegate().set(element);
/*    */   }
/*    */   
/*    */   protected abstract ListIterator<E> delegate();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ForwardingListIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */