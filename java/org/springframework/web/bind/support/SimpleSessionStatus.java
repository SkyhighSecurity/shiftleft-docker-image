/*    */ package org.springframework.web.bind.support;
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
/*    */ public class SimpleSessionStatus
/*    */   implements SessionStatus
/*    */ {
/*    */   private boolean complete = false;
/*    */   
/*    */   public void setComplete() {
/* 33 */     this.complete = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isComplete() {
/* 38 */     return this.complete;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\bind\support\SimpleSessionStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */