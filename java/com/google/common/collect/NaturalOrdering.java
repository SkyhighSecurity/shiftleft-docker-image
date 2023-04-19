/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.Serializable;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ final class NaturalOrdering
/*    */   extends Ordering<Comparable>
/*    */   implements Serializable
/*    */ {
/* 31 */   static final NaturalOrdering INSTANCE = new NaturalOrdering();
/*    */   
/*    */   public int compare(Comparable<Comparable> left, Comparable right) {
/* 34 */     Preconditions.checkNotNull(right);
/* 35 */     if (left == right) {
/* 36 */       return 0;
/*    */     }
/*    */ 
/*    */     
/* 40 */     int result = left.compareTo(right);
/* 41 */     return result;
/*    */   }
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public <S extends Comparable> Ordering<S> reverse() {
/* 46 */     return ReverseNaturalOrdering.INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int binarySearch(List<? extends Comparable> sortedList, Comparable key) {
/* 53 */     return Collections.binarySearch((List)sortedList, key);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <E extends Comparable> List<E> sortedCopy(Iterable<E> iterable) {
/* 59 */     List<E> list = Lists.newArrayList(iterable);
/* 60 */     Collections.sort(list);
/* 61 */     return list;
/*    */   }
/*    */ 
/*    */   
/*    */   private Object readResolve() {
/* 66 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 70 */     return "Ordering.natural()";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\NaturalOrdering.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */