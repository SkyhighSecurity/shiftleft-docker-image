/*    */ package org.springframework.aop.config;
/*    */ 
/*    */ import org.springframework.beans.factory.parsing.ParseState;
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
/*    */ public class AdviceEntry
/*    */   implements ParseState.Entry
/*    */ {
/*    */   private final String kind;
/*    */   
/*    */   public AdviceEntry(String kind) {
/* 37 */     this.kind = kind;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 42 */     return "Advice (" + this.kind + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\config\AdviceEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */