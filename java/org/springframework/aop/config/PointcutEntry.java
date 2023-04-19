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
/*    */ public class PointcutEntry
/*    */   implements ParseState.Entry
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   public PointcutEntry(String name) {
/* 36 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 41 */     return "Pointcut '" + this.name + "'";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\config\PointcutEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */