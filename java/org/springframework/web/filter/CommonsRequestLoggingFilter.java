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
/*    */ public class CommonsRequestLoggingFilter
/*    */   extends AbstractRequestLoggingFilter
/*    */ {
/*    */   protected boolean shouldLog(HttpServletRequest request) {
/* 39 */     return this.logger.isDebugEnabled();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void beforeRequest(HttpServletRequest request, String message) {
/* 47 */     this.logger.debug(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void afterRequest(HttpServletRequest request, String message) {
/* 55 */     this.logger.debug(message);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\filter\CommonsRequestLoggingFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */