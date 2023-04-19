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
/*    */ public class HessianServiceExporter
/*    */   extends HessianExporter
/*    */   implements HttpRequestHandler
/*    */ {
/*    */   public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/* 59 */     if (!"POST".equals(request.getMethod())) {
/* 60 */       throw new HttpRequestMethodNotSupportedException(request.getMethod(), new String[] { "POST" }, "HessianServiceExporter only supports POST requests");
/*    */     }
/*    */ 
/*    */     
/* 64 */     response.setContentType("application/x-hessian");
/*    */     try {
/* 66 */       invoke((InputStream)request.getInputStream(), (OutputStream)response.getOutputStream());
/*    */     }
/* 68 */     catch (Throwable ex) {
/* 69 */       throw new NestedServletException("Hessian skeleton invocation failed", ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\caucho\HessianServiceExporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */