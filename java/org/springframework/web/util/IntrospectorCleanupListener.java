/*    */ package org.springframework.web.util;
/*    */ 
/*    */ import java.beans.Introspector;
/*    */ import javax.servlet.ServletContextEvent;
/*    */ import javax.servlet.ServletContextListener;
/*    */ import org.springframework.beans.CachedIntrospectionResults;
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
/*    */ public class IntrospectorCleanupListener
/*    */   implements ServletContextListener
/*    */ {
/*    */   public void contextInitialized(ServletContextEvent event) {
/* 75 */     CachedIntrospectionResults.acceptClassLoader(Thread.currentThread().getContextClassLoader());
/*    */   }
/*    */ 
/*    */   
/*    */   public void contextDestroyed(ServletContextEvent event) {
/* 80 */     CachedIntrospectionResults.clearClassLoader(Thread.currentThread().getContextClassLoader());
/* 81 */     Introspector.flushCaches();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\IntrospectorCleanupListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */