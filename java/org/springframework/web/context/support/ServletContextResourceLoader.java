/*    */ package org.springframework.web.context.support;
/*    */ 
/*    */ import javax.servlet.ServletContext;
/*    */ import org.springframework.core.io.DefaultResourceLoader;
/*    */ import org.springframework.core.io.Resource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServletContextResourceLoader
/*    */   extends DefaultResourceLoader
/*    */ {
/*    */   private final ServletContext servletContext;
/*    */   
/*    */   public ServletContextResourceLoader(ServletContext servletContext) {
/* 50 */     this.servletContext = servletContext;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Resource getResourceByPath(String path) {
/* 59 */     return (Resource)new ServletContextResource(this.servletContext, path);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\support\ServletContextResourceLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */