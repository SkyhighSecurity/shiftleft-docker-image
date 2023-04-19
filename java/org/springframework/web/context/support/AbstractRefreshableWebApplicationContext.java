/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.ResourcePatternResolver;
/*     */ import org.springframework.ui.context.Theme;
/*     */ import org.springframework.ui.context.ThemeSource;
/*     */ import org.springframework.ui.context.support.UiApplicationContextUtils;
/*     */ import org.springframework.web.context.ConfigurableWebApplicationContext;
/*     */ import org.springframework.web.context.ConfigurableWebEnvironment;
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
/*     */ public abstract class AbstractRefreshableWebApplicationContext
/*     */   extends AbstractRefreshableConfigApplicationContext
/*     */   implements ConfigurableWebApplicationContext, ThemeSource
/*     */ {
/*     */   private ServletContext servletContext;
/*     */   private ServletConfig servletConfig;
/*     */   private String namespace;
/*     */   private ThemeSource themeSource;
/*     */   
/*     */   public AbstractRefreshableWebApplicationContext() {
/*  96 */     setDisplayName("Root WebApplicationContext");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServletContext(ServletContext servletContext) {
/* 102 */     this.servletContext = servletContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServletContext getServletContext() {
/* 107 */     return this.servletContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setServletConfig(ServletConfig servletConfig) {
/* 112 */     this.servletConfig = servletConfig;
/* 113 */     if (servletConfig != null && this.servletContext == null) {
/* 114 */       setServletContext(servletConfig.getServletContext());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ServletConfig getServletConfig() {
/* 120 */     return this.servletConfig;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNamespace(String namespace) {
/* 125 */     this.namespace = namespace;
/* 126 */     if (namespace != null) {
/* 127 */       setDisplayName("WebApplicationContext for namespace '" + namespace + "'");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNamespace() {
/* 133 */     return this.namespace;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getConfigLocations() {
/* 138 */     return super.getConfigLocations();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getApplicationName() {
/* 143 */     return (this.servletContext != null) ? this.servletContext.getContextPath() : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConfigurableEnvironment createEnvironment() {
/* 152 */     return (ConfigurableEnvironment)new StandardServletEnvironment();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
/* 160 */     beanFactory.addBeanPostProcessor(new ServletContextAwareProcessor(this.servletContext, this.servletConfig));
/* 161 */     beanFactory.ignoreDependencyInterface(ServletContextAware.class);
/* 162 */     beanFactory.ignoreDependencyInterface(ServletConfigAware.class);
/*     */     
/* 164 */     WebApplicationContextUtils.registerWebApplicationScopes(beanFactory, this.servletContext);
/* 165 */     WebApplicationContextUtils.registerEnvironmentBeans(beanFactory, this.servletContext, this.servletConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Resource getResourceByPath(String path) {
/* 174 */     return (Resource)new ServletContextResource(this.servletContext, path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ResourcePatternResolver getResourcePatternResolver() {
/* 183 */     return (ResourcePatternResolver)new ServletContextResourcePatternResolver((ResourceLoader)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onRefresh() {
/* 191 */     this.themeSource = UiApplicationContextUtils.initThemeSource((ApplicationContext)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initPropertySources() {
/* 200 */     ConfigurableEnvironment env = getEnvironment();
/* 201 */     if (env instanceof ConfigurableWebEnvironment) {
/* 202 */       ((ConfigurableWebEnvironment)env).initPropertySources(this.servletContext, this.servletConfig);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Theme getTheme(String themeName) {
/* 208 */     return this.themeSource.getTheme(themeName);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\support\AbstractRefreshableWebApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */