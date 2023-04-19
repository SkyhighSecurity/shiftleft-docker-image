/*    */ package org.springframework.web.context.support;
/*    */ 
/*    */ import javax.servlet.ServletContext;
/*    */ import org.springframework.beans.factory.FactoryBean;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServletContextAttributeFactoryBean
/*    */   implements FactoryBean<Object>, ServletContextAware
/*    */ {
/*    */   private String attributeName;
/*    */   private Object attribute;
/*    */   
/*    */   public void setAttributeName(String attributeName) {
/* 55 */     this.attributeName = attributeName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setServletContext(ServletContext servletContext) {
/* 60 */     if (this.attributeName == null) {
/* 61 */       throw new IllegalArgumentException("Property 'attributeName' is required");
/*    */     }
/* 63 */     this.attribute = servletContext.getAttribute(this.attributeName);
/* 64 */     if (this.attribute == null) {
/* 65 */       throw new IllegalStateException("No ServletContext attribute '" + this.attributeName + "' found");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getObject() throws Exception {
/* 72 */     return this.attribute;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<?> getObjectType() {
/* 77 */     return (this.attribute != null) ? this.attribute.getClass() : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSingleton() {
/* 82 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\support\ServletContextAttributeFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */