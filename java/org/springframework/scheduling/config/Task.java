/*    */ package org.springframework.scheduling.config;
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
/*    */ public class Task
/*    */ {
/*    */   private final Runnable runnable;
/*    */   
/*    */   public Task(Runnable runnable) {
/* 36 */     this.runnable = runnable;
/*    */   }
/*    */ 
/*    */   
/*    */   public Runnable getRunnable() {
/* 41 */     return this.runnable;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\config\Task.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */