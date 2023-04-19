/*    */ package com.google.common.base;
/*    */ 
/*    */ import java.lang.ref.WeakReference;
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
/*    */ 
/*    */ 
/*    */ public abstract class FinalizableWeakReference<T>
/*    */   extends WeakReference<T>
/*    */   implements FinalizableReference
/*    */ {
/*    */   protected FinalizableWeakReference(T referent, FinalizableReferenceQueue queue) {
/* 39 */     super(referent, queue.queue);
/* 40 */     queue.cleanUp();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\base\FinalizableWeakReference.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */