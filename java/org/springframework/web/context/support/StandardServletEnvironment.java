/*    */ package org.springframework.web.context.support;
/*    */ 
/*    */ import javax.servlet.ServletConfig;
/*    */ import javax.servlet.ServletContext;
/*    */ import org.springframework.core.env.MutablePropertySources;
/*    */ import org.springframework.core.env.PropertySource;
/*    */ import org.springframework.core.env.StandardEnvironment;
/*    */ import org.springframework.jndi.JndiLocatorDelegate;
/*    */ import org.springframework.jndi.JndiPropertySource;
/*    */ import org.springframework.web.context.ConfigurableWebEnvironment;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StandardServletEnvironment
/*    */   extends StandardEnvironment
/*    */   implements ConfigurableWebEnvironment
/*    */ {
/*    */   public static final String SERVLET_CONTEXT_PROPERTY_SOURCE_NAME = "servletContextInitParams";
/*    */   public static final String SERVLET_CONFIG_PROPERTY_SOURCE_NAME = "servletConfigInitParams";
/*    */   public static final String JNDI_PROPERTY_SOURCE_NAME = "jndiProperties";
/*    */   
/*    */   protected void customizePropertySources(MutablePropertySources propertySources) {
/* 84 */     propertySources.addLast((PropertySource)new PropertySource.StubPropertySource("servletConfigInitParams"));
/* 85 */     propertySources.addLast((PropertySource)new PropertySource.StubPropertySource("servletContextInitParams"));
/* 86 */     if (JndiLocatorDelegate.isDefaultJndiEnvironmentAvailable()) {
/* 87 */       propertySources.addLast((PropertySource)new JndiPropertySource("jndiProperties"));
/*    */     }
/* 89 */     super.customizePropertySources(propertySources);
/*    */   }
/*    */ 
/*    */   
/*    */   public void initPropertySources(ServletContext servletContext, ServletConfig servletConfig) {
/* 94 */     WebApplicationContextUtils.initServletPropertySources(getPropertySources(), servletContext, servletConfig);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\support\StandardServletEnvironment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */