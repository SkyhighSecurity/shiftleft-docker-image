/*    */ package org.springframework.scheduling.support;
/*    */ 
/*    */ import java.util.Date;
/*    */ import org.springframework.scheduling.TriggerContext;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleTriggerContext
/*    */   implements TriggerContext
/*    */ {
/*    */   private volatile Date lastScheduledExecutionTime;
/*    */   private volatile Date lastActualExecutionTime;
/*    */   private volatile Date lastCompletionTime;
/*    */   
/*    */   public SimpleTriggerContext() {}
/*    */   
/*    */   public SimpleTriggerContext(Date lastScheduledExecutionTime, Date lastActualExecutionTime, Date lastCompletionTime) {
/* 51 */     this.lastScheduledExecutionTime = lastScheduledExecutionTime;
/* 52 */     this.lastActualExecutionTime = lastActualExecutionTime;
/* 53 */     this.lastCompletionTime = lastCompletionTime;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void update(Date lastScheduledExecutionTime, Date lastActualExecutionTime, Date lastCompletionTime) {
/* 64 */     this.lastScheduledExecutionTime = lastScheduledExecutionTime;
/* 65 */     this.lastActualExecutionTime = lastActualExecutionTime;
/* 66 */     this.lastCompletionTime = lastCompletionTime;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Date lastScheduledExecutionTime() {
/* 72 */     return this.lastScheduledExecutionTime;
/*    */   }
/*    */ 
/*    */   
/*    */   public Date lastActualExecutionTime() {
/* 77 */     return this.lastActualExecutionTime;
/*    */   }
/*    */ 
/*    */   
/*    */   public Date lastCompletionTime() {
/* 82 */     return this.lastCompletionTime;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\support\SimpleTriggerContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */