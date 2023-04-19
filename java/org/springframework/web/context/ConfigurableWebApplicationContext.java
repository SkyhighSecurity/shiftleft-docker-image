/*    */ package org.springframework.web.context;
/*    */ 
/*    */ import javax.servlet.ServletConfig;
/*    */ import javax.servlet.ServletContext;
/*    */ import org.springframework.context.ConfigurableApplicationContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ConfigurableWebApplicationContext
/*    */   extends WebApplicationContext, ConfigurableApplicationContext
/*    */ {
/* 45 */   public static final String APPLICATION_CONTEXT_ID_PREFIX = WebApplicationContext.class.getName() + ":";
/*    */   public static final String SERVLET_CONFIG_BEAN_NAME = "servletConfig";
/*    */   
/*    */   void setServletContext(ServletContext paramServletContext);
/*    */   
/*    */   void setServletConfig(ServletConfig paramServletConfig);
/*    */   
/*    */   ServletConfig getServletConfig();
/*    */   
/*    */   void setNamespace(String paramString);
/*    */   
/*    */   String getNamespace();
/*    */   
/*    */   void setConfigLocation(String paramString);
/*    */   
/*    */   void setConfigLocations(String... paramVarArgs);
/*    */   
/*    */   String[] getConfigLocations();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\ConfigurableWebApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */