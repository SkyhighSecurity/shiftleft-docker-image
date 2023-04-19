/*    */ package org.springframework.remoting.caucho;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.web.HttpRequestHandler;
/*    */ import org.springframework.web.HttpRequestMethodNotSupportedException;
/*    */ import org.springframework.web.util.NestedServletException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/*    */ public class BurlapServiceExporter
/*    */   extends BurlapExporter
/*    */   implements HttpRequestHandler
/*    */ {
/*    */   public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/* 63 */     if (!"POST".equals(request.getMethod())) {
/* 64 */       throw new HttpRequestMethodNotSupportedException(request.getMethod(), new String[] { "POST" }, "BurlapServiceExporter only supports POST requests");
/*    */     }
/*    */ 
/*    */     
/*    */     try {
/* 69 */       invoke((InputStream)request.getInputStream(), (OutputStream)response.getOutputStream());
/*    */     }
/* 71 */     catch (Throwable ex) {
/* 72 */       throw new NestedServletException("Burlap skeleton invocation failed", ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\caucho\BurlapServiceExporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */