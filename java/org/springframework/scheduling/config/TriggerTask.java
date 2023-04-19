/*    */ package org.springframework.scheduling.config;
/*    */ 
/*    */ import org.springframework.scheduling.Trigger;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TriggerTask
/*    */   extends Task
/*    */ {
/*    */   private final Trigger trigger;
/*    */   
/*    */   public TriggerTask(Runnable runnable, Trigger trigger) {
/* 42 */     super(runnable);
/* 43 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public Trigger getTrigger() {
/* 48 */     return this.trigger;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\config\TriggerTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */