/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.Queue;
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
/*    */ public abstract class ForwardingQueue<E>
/*    */   extends ForwardingCollection<E>
/*    */   implements Queue<E>
/*    */ {
/*    */   public boolean offer(E o) {
/* 39 */     return delegate().offer(o);
/*    */   }
/*    */   
/*    */   public E poll() {
/* 43 */     return delegate().poll();
/*    */   }
/*    */   
/*    */   public E remove() {
/* 47 */     return delegate().remove();
/*    */   }
/*    */   
/*    */   public E peek() {
/* 51 */     return delegate().peek();
/*    */   }
/*    */   
/*    */   public E element() {
/* 55 */     return delegate().element();
/*    */   }
/*    */   
/*    */   protected abstract Queue<E> delegate();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ForwardingQueue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */