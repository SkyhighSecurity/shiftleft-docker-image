/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.Serializable;
/*    */ import javax.annotation.Nullable;
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
/*    */ @GwtCompatible(serializable = true)
/*    */ final class ReverseOrdering<T>
/*    */   extends Ordering<T>
/*    */   implements Serializable
/*    */ {
/*    */   final Ordering<? super T> forwardOrder;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   ReverseOrdering(Ordering<? super T> forwardOrder) {
/* 32 */     this.forwardOrder = (Ordering<? super T>)Preconditions.checkNotNull(forwardOrder);
/*    */   }
/*    */   
/*    */   public int compare(T a, T b) {
/* 36 */     return this.forwardOrder.compare(b, a);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S extends T> Ordering<S> reverse() {
/* 41 */     return (Ordering)this.forwardOrder;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <E extends T> E min(E a, E b) {
/* 47 */     return this.forwardOrder.max(a, b);
/*    */   }
/*    */   
/*    */   public <E extends T> E min(E a, E b, E c, E... rest) {
/* 51 */     return this.forwardOrder.max(a, b, c, rest);
/*    */   }
/*    */   
/*    */   public <E extends T> E min(Iterable<E> iterable) {
/* 55 */     return this.forwardOrder.max(iterable);
/*    */   }
/*    */   
/*    */   public <E extends T> E max(E a, E b) {
/* 59 */     return this.forwardOrder.min(a, b);
/*    */   }
/*    */   
/*    */   public <E extends T> E max(E a, E b, E c, E... rest) {
/* 63 */     return this.forwardOrder.min(a, b, c, rest);
/*    */   }
/*    */   
/*    */   public <E extends T> E max(Iterable<E> iterable) {
/* 67 */     return this.forwardOrder.min(iterable);
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 71 */     return -this.forwardOrder.hashCode();
/*    */   }
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 75 */     if (object == this) {
/* 76 */       return true;
/*    */     }
/* 78 */     if (object instanceof ReverseOrdering) {
/* 79 */       ReverseOrdering<?> that = (ReverseOrdering)object;
/* 80 */       return this.forwardOrder.equals(that.forwardOrder);
/*    */     } 
/* 82 */     return false;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 86 */     return this.forwardOrder + ".reverse()";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ReverseOrdering.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */