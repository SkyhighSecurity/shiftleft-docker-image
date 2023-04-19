/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @GwtCompatible(serializable = true)
/*    */ final class UsingToStringOrdering
/*    */   extends Ordering<Object>
/*    */   implements Serializable
/*    */ {
/* 27 */   static final UsingToStringOrdering INSTANCE = new UsingToStringOrdering();
/*    */   
/*    */   public int compare(Object left, Object right) {
/* 30 */     return left.toString().compareTo(right.toString());
/*    */   }
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   private Object readResolve() {
/* 35 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 39 */     return "Ordering.usingToString()";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\UsingToStringOrdering.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */