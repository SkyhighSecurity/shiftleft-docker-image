/*    */ package org.springframework.web.context.support;
/*    */ 
/*    */ import java.util.Map;
/*    */ import javax.servlet.ServletContext;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.web.context.ServletContextAware;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServletContextAttributeExporter
/*    */   implements ServletContextAware
/*    */ {
/* 49 */   protected final Log logger = LogFactory.getLog(getClass());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Map<String, Object> attributes;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAttributes(Map<String, Object> attributes) {
/* 63 */     this.attributes = attributes;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setServletContext(ServletContext servletContext) {
/* 68 */     if (this.attributes != null)
/* 69 */       for (Map.Entry<String, Object> entry : this.attributes.entrySet()) {
/* 70 */         String attributeName = entry.getKey();
/* 71 */         if (this.logger.isWarnEnabled() && 
/* 72 */           servletContext.getAttribute(attributeName) != null) {
/* 73 */           this.logger.warn("Replacing existing ServletContext attribute with name '" + attributeName + "'");
/*    */         }
/*    */         
/* 76 */         servletContext.setAttribute(attributeName, entry.getValue());
/* 77 */         if (this.logger.isInfoEnabled())
/* 78 */           this.logger.info("Exported ServletContext attribute with name '" + attributeName + "'"); 
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\support\ServletContextAttributeExporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */