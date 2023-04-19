/*    */ package org.springframework.scheduling.config;
/*    */ 
/*    */ import org.springframework.beans.factory.SmartInitializingSingleton;
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
/*    */ public class ContextLifecycleScheduledTaskRegistrar
/*    */   extends ScheduledTaskRegistrar
/*    */   implements SmartInitializingSingleton
/*    */ {
/*    */   public void afterPropertiesSet() {}
/*    */   
/*    */   public void afterSingletonsInstantiated() {
/* 37 */     scheduleTasks();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\config\ContextLifecycleScheduledTaskRegistrar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */