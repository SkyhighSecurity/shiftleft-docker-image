/*    */ package org.springframework.web.filter;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServletContextRequestLoggingFilter
/*    */   extends AbstractRequestLoggingFilter
/*    */ {
/*    */   protected void beforeRequest(HttpServletRequest request, String message) {
/* 41 */     getServletContext().log(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void afterRequest(HttpServletRequest request, String message) {
/* 49 */     getServletContext().log(message);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\filter\ServletContextRequestLoggingFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */