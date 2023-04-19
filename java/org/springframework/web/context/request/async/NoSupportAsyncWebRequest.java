/*    */ package org.springframework.web.context.request.async;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.web.context.request.ServletWebRequest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NoSupportAsyncWebRequest
/*    */   extends ServletWebRequest
/*    */   implements AsyncWebRequest
/*    */ {
/*    */   public NoSupportAsyncWebRequest(HttpServletRequest request, HttpServletResponse response) {
/* 33 */     super(request, response);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addCompletionHandler(Runnable runnable) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTimeout(Long timeout) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addTimeoutHandler(Runnable runnable) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isAsyncStarted() {
/* 54 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void startAsync() {
/* 62 */     throw new UnsupportedOperationException("No async support in a pre-Servlet 3.0 runtime");
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAsyncComplete() {
/* 67 */     throw new UnsupportedOperationException("No async support in a pre-Servlet 3.0 runtime");
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispatch() {
/* 72 */     throw new UnsupportedOperationException("No async support in a pre-Servlet 3.0 runtime");
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\async\NoSupportAsyncWebRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */