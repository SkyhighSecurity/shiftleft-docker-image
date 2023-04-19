/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.springframework.util.Assert;
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
/*    */ public class ConstructorArgumentEntry
/*    */   implements ParseState.Entry
/*    */ {
/*    */   private final int index;
/*    */   
/*    */   public ConstructorArgumentEntry() {
/* 39 */     this.index = -1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ConstructorArgumentEntry(int index) {
/* 50 */     Assert.isTrue((index >= 0), "Constructor argument index must be greater than or equal to zero");
/* 51 */     this.index = index;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return "Constructor-arg" + ((this.index >= 0) ? (" #" + this.index) : "");
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\parsing\ConstructorArgumentEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */