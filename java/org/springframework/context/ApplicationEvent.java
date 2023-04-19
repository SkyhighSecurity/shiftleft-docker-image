/*    */ package org.springframework.context;
/*    */ 
/*    */ import java.util.EventObject;
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
/*    */ public abstract class ApplicationEvent
/*    */   extends EventObject
/*    */ {
/*    */   private static final long serialVersionUID = 7099057708183571937L;
/*    */   private final long timestamp;
/*    */   
/*    */   public ApplicationEvent(Object source) {
/* 42 */     super(source);
/* 43 */     this.timestamp = System.currentTimeMillis();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final long getTimestamp() {
/* 51 */     return this.timestamp;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\ApplicationEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */