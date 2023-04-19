/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.support.StaticApplicationContext;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.ResourcePatternResolver;
/*     */ import org.springframework.ui.context.Theme;
/*     */ import org.springframework.ui.context.ThemeSource;
/*     */ import org.springframework.ui.context.support.UiApplicationContextUtils;
/*     */ import org.springframework.web.context.ConfigurableWebApplicationContext;
/*     */ import org.springframework.web.context.ServletConfigAware;
/*     */ import org.springframework.web.context.ServletContextAware;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StaticWebApplicationContext
/*     */   extends StaticApplicationContext
/*     */   implements ConfigurableWebApplicationContext, ThemeSource
/*     */ {
/*     */   private ServletContext servletContext;
/*     */   private ServletConfig servletConfig;
/*     */   private String namespace;
/*     */   private ThemeSource themeSource;
/*     */   
/*     */   public StaticWebApplicationContext() {
/*  69 */     setDisplayName("Root WebApplicationContext");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServletContext(ServletContext servletContext) {
/*  78 */     this.servletContext = servletContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServletContext getServletContext() {
/*  83 */     return this.servletContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setServletConfig(ServletConfig servletConfig) {
/*  88 */     this.servletConfig = servletConfig;
/*  89 */     if (servletConfig != null && this.servletContext == null) {
/*  90 */       this.servletContext = servletConfig.getServletContext();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ServletConfig getServletConfig() {
/*  96 */     return this.servletConfig;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNamespace(String namespace) {
/* 101 */     this.namespace = namespace;
/* 102 */     if (namespace != null) {
/* 103 */       setDisplayName("WebApplicationContext for namespace '" + namespace + "'");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNamespace() {
/* 109 */     return this.namespace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConfigLocation(String configLocation) {
/* 118 */     if (configLocation != null) {
/* 119 */       throw new UnsupportedOperationException("StaticWebApplicationContext does not support config locations");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConfigLocations(String... configLocations) {
/* 129 */     if (configLocations != null) {
/* 130 */       throw new UnsupportedOperationException("StaticWebApplicationContext does not support config locations");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getConfigLocations() {
/* 136 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
/* 145 */     beanFactory.addBeanPostProcessor(new ServletContextAwareProcessor(this.servletContext, this.servletConfig));
/* 146 */     beanFactory.ignoreDependencyInterface(ServletContextAware.class);
/* 147 */     beanFactory.ignoreDependencyInterface(ServletConfigAware.class);
/*     */     
/* 149 */     WebApplicationContextUtils.registerWebApplicationScopes(beanFactory, this.servletContext);
/* 150 */     WebApplicationContextUtils.registerEnvironmentBeans(beanFactory, this.servletContext, this.servletConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Resource getResourceByPath(String path) {
/* 159 */     return (Resource)new ServletContextResource(this.servletContext, path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ResourcePatternResolver getResourcePatternResolver() {
/* 168 */     return (ResourcePatternResolver)new ServletContextResourcePatternResolver((ResourceLoader)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConfigurableEnvironment createEnvironment() {
/* 176 */     return (ConfigurableEnvironment)new StandardServletEnvironment();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onRefresh() {
/* 184 */     this.themeSource = UiApplicationContextUtils.initThemeSource((ApplicationContext)this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initPropertySources() {
/* 189 */     WebApplicationContextUtils.initServletPropertySources(getEnvironment().getPropertySources(), this.servletContext, this.servletConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Theme getTheme(String themeName) {
/* 195 */     return this.themeSource.getTheme(themeName);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\support\StaticWebApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */