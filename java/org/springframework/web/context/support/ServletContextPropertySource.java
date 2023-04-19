/*    */ package org.springframework.web.context.support;
/*    */ 
/*    */ import javax.servlet.ServletContext;
/*    */ import org.springframework.core.env.EnumerablePropertySource;
/*    */ import org.springframework.util.StringUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServletContextPropertySource
/*    */   extends EnumerablePropertySource<ServletContext>
/*    */ {
/*    */   public ServletContextPropertySource(String name, ServletContext servletContext) {
/* 35 */     super(name, servletContext);
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getPropertyNames() {
/* 40 */     return StringUtils.toStringArray(((ServletContext)this.source).getInitParameterNames());
/*    */   }
/*    */ 
/*    */   
/*    */   public String getProperty(String name) {
/* 45 */     return ((ServletContext)this.source).getInitParameter(name);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\support\ServletContextPropertySource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */