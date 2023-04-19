/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.Serializable;
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
/*    */ @GwtCompatible(serializable = true)
/*    */ final class ReverseNaturalOrdering
/*    */   extends Ordering<Comparable>
/*    */   implements Serializable
/*    */ {
/* 29 */   static final ReverseNaturalOrdering INSTANCE = new ReverseNaturalOrdering();
/*    */   
/*    */   public int compare(Comparable left, Comparable<Comparable> right) {
/* 32 */     Preconditions.checkNotNull(left);
/* 33 */     if (left == right) {
/* 34 */       return 0;
/*    */     }
/*    */ 
/*    */     
/* 38 */     int result = right.compareTo(left);
/* 39 */     return result;
/*    */   }
/*    */   private static final long serialVersionUID = 0L;
/*    */   public <S extends Comparable> Ordering<S> reverse() {
/* 43 */     return Ordering.natural();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <E extends Comparable> E min(E a, E b) {
/* 49 */     return (E)NaturalOrdering.INSTANCE.max(a, b);
/*    */   }
/*    */   
/*    */   public <E extends Comparable> E min(E a, E b, E c, E... rest) {
/* 53 */     return (E)NaturalOrdering.INSTANCE.max(a, b, c, (Object[])rest);
/*    */   }
/*    */   
/*    */   public <E extends Comparable> E min(Iterable<E> iterable) {
/* 57 */     return (E)NaturalOrdering.INSTANCE.max(iterable);
/*    */   }
/*    */   
/*    */   public <E extends Comparable> E max(E a, E b) {
/* 61 */     return (E)NaturalOrdering.INSTANCE.min(a, b);
/*    */   }
/*    */   
/*    */   public <E extends Comparable> E max(E a, E b, E c, E... rest) {
/* 65 */     return (E)NaturalOrdering.INSTANCE.min(a, b, c, (Object[])rest);
/*    */   }
/*    */   
/*    */   public <E extends Comparable> E max(Iterable<E> iterable) {
/* 69 */     return (E)NaturalOrdering.INSTANCE.min(iterable);
/*    */   }
/*    */ 
/*    */   
/*    */   private Object readResolve() {
/* 74 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 78 */     return "Ordering.natural().reverse()";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ReverseNaturalOrdering.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */