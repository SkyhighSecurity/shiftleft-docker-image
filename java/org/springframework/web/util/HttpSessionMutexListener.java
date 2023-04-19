/*    */ package org.springframework.web.util;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import javax.servlet.http.HttpSessionEvent;
/*    */ import javax.servlet.http.HttpSessionListener;
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
/*    */ 
/*    */ 
/*    */ public class HttpSessionMutexListener
/*    */   implements HttpSessionListener
/*    */ {
/*    */   public void sessionCreated(HttpSessionEvent event) {
/* 48 */     event.getSession().setAttribute(WebUtils.SESSION_MUTEX_ATTRIBUTE, new Mutex());
/*    */   }
/*    */ 
/*    */   
/*    */   public void sessionDestroyed(HttpSessionEvent event) {
/* 53 */     event.getSession().removeAttribute(WebUtils.SESSION_MUTEX_ATTRIBUTE);
/*    */   }
/*    */   
/*    */   private static class Mutex implements Serializable {
/*    */     private Mutex() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\HttpSessionMutexListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */