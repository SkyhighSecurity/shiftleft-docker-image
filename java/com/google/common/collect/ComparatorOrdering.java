/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.Serializable;
/*    */ import java.util.Collections;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
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
/*    */ final class ComparatorOrdering<T>
/*    */   extends Ordering<T>
/*    */   implements Serializable
/*    */ {
/*    */   final Comparator<T> comparator;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   ComparatorOrdering(Comparator<T> comparator) {
/* 35 */     this.comparator = (Comparator<T>)Preconditions.checkNotNull(comparator);
/*    */   }
/*    */   
/*    */   public int compare(T a, T b) {
/* 39 */     return this.comparator.compare(a, b);
/*    */   }
/*    */ 
/*    */   
/*    */   public int binarySearch(List<? extends T> sortedList, T key) {
/* 44 */     return Collections.binarySearch(sortedList, key, this.comparator);
/*    */   }
/*    */ 
/*    */   
/*    */   public <E extends T> List<E> sortedCopy(Iterable<E> iterable) {
/* 49 */     List<E> list = Lists.newArrayList(iterable);
/* 50 */     Collections.sort(list, this.comparator);
/* 51 */     return list;
/*    */   }
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 55 */     if (object == this) {
/* 56 */       return true;
/*    */     }
/* 58 */     if (object instanceof ComparatorOrdering) {
/* 59 */       ComparatorOrdering<?> that = (ComparatorOrdering)object;
/* 60 */       return this.comparator.equals(that.comparator);
/*    */     } 
/* 62 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 66 */     return this.comparator.hashCode();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 70 */     return this.comparator.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ComparatorOrdering.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */