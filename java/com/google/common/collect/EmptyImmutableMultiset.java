/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ final class EmptyImmutableMultiset
/*    */   extends ImmutableMultiset<Object>
/*    */ {
/* 28 */   static final EmptyImmutableMultiset INSTANCE = new EmptyImmutableMultiset();
/*    */   
/*    */   private EmptyImmutableMultiset() {
/* 31 */     super(ImmutableMap.of(), 0);
/*    */   }
/*    */   private static final long serialVersionUID = 0L;
/*    */   Object readResolve() {
/* 35 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\EmptyImmutableMultiset.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */