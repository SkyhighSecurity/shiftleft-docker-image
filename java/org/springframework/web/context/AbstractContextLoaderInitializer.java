/*    */ package org.springframework.web.context;
/*    */ 
/*    */ import java.util.EventListener;
/*    */ import javax.servlet.ServletContext;
/*    */ import javax.servlet.ServletException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.context.ApplicationContextInitializer;
/*    */ import org.springframework.web.WebApplicationInitializer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractContextLoaderInitializer
/*    */   implements WebApplicationInitializer
/*    */ {
/* 44 */   protected final Log logger = LogFactory.getLog(getClass());
/*    */ 
/*    */ 
/*    */   
/*    */   public void onStartup(ServletContext servletContext) throws ServletException {
/* 49 */     registerContextLoaderListener(servletContext);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void registerContextLoaderListener(ServletContext servletContext) {
/* 59 */     WebApplicationContext rootAppContext = createRootApplicationContext();
/* 60 */     if (rootAppContext != null) {
/* 61 */       ContextLoaderListener listener = new ContextLoaderListener(rootAppContext);
/* 62 */       listener.setContextInitializers(getRootApplicationContextInitializers());
/* 63 */       servletContext.addListener((EventListener)listener);
/*    */     } else {
/*    */       
/* 66 */       this.logger.debug("No ContextLoaderListener registered, as createRootApplicationContext() did not return an application context");
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract WebApplicationContext createRootApplicationContext();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected ApplicationContextInitializer<?>[] getRootApplicationContextInitializers() {
/* 92 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\AbstractContextLoaderInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */