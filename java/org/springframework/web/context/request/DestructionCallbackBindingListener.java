/*    */ package org.springframework.web.context.request;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import javax.servlet.http.HttpSessionBindingEvent;
/*    */ import javax.servlet.http.HttpSessionBindingListener;
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
/*    */ public class DestructionCallbackBindingListener
/*    */   implements HttpSessionBindingListener, Serializable
/*    */ {
/*    */   private final Runnable destructionCallback;
/*    */   
/*    */   public DestructionCallbackBindingListener(Runnable destructionCallback) {
/* 44 */     this.destructionCallback = destructionCallback;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void valueBound(HttpSessionBindingEvent event) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void valueUnbound(HttpSessionBindingEvent event) {
/* 54 */     this.destructionCallback.run();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\DestructionCallbackBindingListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */