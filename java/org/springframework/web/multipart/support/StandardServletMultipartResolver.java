/*    */ package org.springframework.web.multipart.support;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.Part;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.util.StringUtils;
/*    */ import org.springframework.web.multipart.MultipartException;
/*    */ import org.springframework.web.multipart.MultipartHttpServletRequest;
/*    */ import org.springframework.web.multipart.MultipartResolver;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StandardServletMultipartResolver
/*    */   implements MultipartResolver
/*    */ {
/*    */   private boolean resolveLazily = false;
/*    */   
/*    */   public void setResolveLazily(boolean resolveLazily) {
/* 65 */     this.resolveLazily = resolveLazily;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isMultipart(HttpServletRequest request) {
/* 72 */     if (!"post".equalsIgnoreCase(request.getMethod())) {
/* 73 */       return false;
/*    */     }
/* 75 */     String contentType = request.getContentType();
/* 76 */     return StringUtils.startsWithIgnoreCase(contentType, "multipart/");
/*    */   }
/*    */ 
/*    */   
/*    */   public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request) throws MultipartException {
/* 81 */     return new StandardMultipartHttpServletRequest(request, this.resolveLazily);
/*    */   }
/*    */ 
/*    */   
/*    */   public void cleanupMultipart(MultipartHttpServletRequest request) {
/* 86 */     if (!(request instanceof AbstractMultipartHttpServletRequest) || ((AbstractMultipartHttpServletRequest)request)
/* 87 */       .isResolved())
/*    */       
/*    */       try {
/*    */         
/* 91 */         for (Part part : request.getParts()) {
/* 92 */           if (request.getFile(part.getName()) != null) {
/* 93 */             part.delete();
/*    */           }
/*    */         }
/*    */       
/* 97 */       } catch (Throwable ex) {
/* 98 */         LogFactory.getLog(getClass()).warn("Failed to perform cleanup of multipart items", ex);
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\multipart\support\StandardServletMultipartResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */