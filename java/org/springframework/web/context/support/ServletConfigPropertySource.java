/*    */ package org.springframework.web.context.support;
/*    */ 
/*    */ import javax.servlet.ServletConfig;
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
/*    */ public class ServletConfigPropertySource
/*    */   extends EnumerablePropertySource<ServletConfig>
/*    */ {
/*    */   public ServletConfigPropertySource(String name, ServletConfig servletConfig) {
/* 35 */     super(name, servletConfig);
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getPropertyNames() {
/* 40 */     return StringUtils.toStringArray(((ServletConfig)this.source).getInitParameterNames());
/*    */   }
/*    */ 
/*    */   
/*    */   public String getProperty(String name) {
/* 45 */     return ((ServletConfig)this.source).getInitParameter(name);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\support\ServletConfigPropertySource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */