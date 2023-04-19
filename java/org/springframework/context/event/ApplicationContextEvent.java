/*    */ package org.springframework.context.event;
/*    */ 
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.context.ApplicationEvent;
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
/*    */ public abstract class ApplicationContextEvent
/*    */   extends ApplicationEvent
/*    */ {
/*    */   public ApplicationContextEvent(ApplicationContext source) {
/* 37 */     super(source);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final ApplicationContext getApplicationContext() {
/* 44 */     return (ApplicationContext)getSource();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\event\ApplicationContextEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */