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
/*    */ public class ServletContextParameterFactoryBean
/*    */   implements FactoryBean<String>, ServletContextAware
/*    */ {
/*    */   private String initParamName;
/*    */   private String paramValue;
/*    */   
/*    */   public void setInitParamName(String initParamName) {
/* 50 */     this.initParamName = initParamName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setServletContext(ServletContext servletContext) {
/* 55 */     if (this.initParamName == null) {
/* 56 */       throw new IllegalArgumentException("initParamName is required");
/*    */     }
/* 58 */     this.paramValue = servletContext.getInitParameter(this.initParamName);
/* 59 */     if (this.paramValue == null) {
/* 60 */       throw new IllegalStateException("No ServletContext init parameter '" + this.initParamName + "' found");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getObject() {
/* 67 */     return this.paramValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<String> getObjectType() {
/* 72 */     return String.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSingleton() {
/* 77 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\support\ServletContextParameterFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */