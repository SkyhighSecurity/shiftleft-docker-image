/*    */ package org.springframework.web.filter;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.apache.log4j.NDC;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/*    */ public class Log4jNestedDiagnosticContextFilter
/*    */   extends AbstractRequestLoggingFilter
/*    */ {
/* 46 */   protected final Logger log4jLogger = Logger.getLogger(getClass());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void beforeRequest(HttpServletRequest request, String message) {
/* 55 */     if (this.log4jLogger.isDebugEnabled()) {
/* 56 */       this.log4jLogger.debug(message);
/*    */     }
/* 58 */     NDC.push(getNestedDiagnosticContextMessage(request));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getNestedDiagnosticContextMessage(HttpServletRequest request) {
/* 69 */     return createMessage(request, "", "");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void afterRequest(HttpServletRequest request, String message) {
/* 78 */     NDC.pop();
/* 79 */     if (NDC.getDepth() == 0) {
/* 80 */       NDC.remove();
/*    */     }
/* 82 */     if (this.log4jLogger.isDebugEnabled())
/* 83 */       this.log4jLogger.debug(message); 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\filter\Log4jNestedDiagnosticContextFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */