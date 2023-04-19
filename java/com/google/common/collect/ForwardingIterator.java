/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Iterator;
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
/*    */ 
/*    */ @GwtCompatible
/*    */ public abstract class ForwardingIterator<T>
/*    */   extends ForwardingObject
/*    */   implements Iterator<T>
/*    */ {
/*    */   public boolean hasNext() {
/* 39 */     return delegate().hasNext();
/*    */   }
/*    */   
/*    */   public T next() {
/* 43 */     return delegate().next();
/*    */   }
/*    */   
/*    */   public void remove() {
/* 47 */     delegate().remove();
/*    */   }
/*    */   
/*    */   protected abstract Iterator<T> delegate();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ForwardingIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */