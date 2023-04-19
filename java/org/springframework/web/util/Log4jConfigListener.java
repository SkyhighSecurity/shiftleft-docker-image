/*    */ package org.springframework.web.util;
/*    */ 
/*    */ import javax.servlet.ServletContextEvent;
/*    */ import javax.servlet.ServletContextListener;
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
/*    */ @Deprecated
/*    */ public class Log4jConfigListener
/*    */   implements ServletContextListener
/*    */ {
/*    */   public void contextInitialized(ServletContextEvent event) {
/* 49 */     Log4jWebConfigurer.initLogging(event.getServletContext());
/*    */   }
/*    */ 
/*    */   
/*    */   public void contextDestroyed(ServletContextEvent event) {
/* 54 */     Log4jWebConfigurer.shutdownLogging(event.getServletContext());
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\Log4jConfigListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */