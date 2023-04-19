/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.VisibleForTesting;
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
/*    */ @GwtCompatible(serializable = true)
/*    */ final class RegularImmutableSet<E>
/*    */   extends ImmutableSet.ArrayImmutableSet<E>
/*    */ {
/*    */   @VisibleForTesting
/*    */   final transient Object[] table;
/*    */   private final transient int mask;
/*    */   private final transient int hashCode;
/*    */   
/*    */   RegularImmutableSet(Object[] elements, int hashCode, Object[] table, int mask) {
/* 39 */     super(elements);
/* 40 */     this.table = table;
/* 41 */     this.mask = mask;
/* 42 */     this.hashCode = hashCode;
/*    */   }
/*    */   
/*    */   public boolean contains(Object target) {
/* 46 */     if (target == null) {
/* 47 */       return false;
/*    */     }
/* 49 */     for (int i = Hashing.smear(target.hashCode());; i++) {
/* 50 */       Object candidate = this.table[i & this.mask];
/* 51 */       if (candidate == null) {
/* 52 */         return false;
/*    */       }
/* 54 */       if (candidate.equals(target)) {
/* 55 */         return true;
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 61 */     return this.hashCode;
/*    */   }
/*    */   
/*    */   boolean isHashCodeFast() {
/* 65 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\RegularImmutableSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */