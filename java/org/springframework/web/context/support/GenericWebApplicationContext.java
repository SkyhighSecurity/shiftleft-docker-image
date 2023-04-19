/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.support.GenericApplicationContext;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.ResourcePatternResolver;
/*     */ import org.springframework.ui.context.Theme;
/*     */ import org.springframework.ui.context.ThemeSource;
/*     */ import org.springframework.ui.context.support.UiApplicationContextUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.context.ConfigurableWebApplicationContext;
/*     */ import org.springframework.web.context.ConfigurableWebEnvironment;
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
/*     */ public class GenericWebApplicationContext
/*     */   extends GenericApplicationContext
/*     */   implements ConfigurableWebApplicationContext, ThemeSource
/*     */ {
/*     */   private ServletContext servletContext;
/*     */   private ThemeSource themeSource;
/*     */   
/*     */   public GenericWebApplicationContext() {}
/*     */   
/*     */   public GenericWebApplicationContext(ServletContext servletContext) {
/*  88 */     this.servletContext = servletContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericWebApplicationContext(DefaultListableBeanFactory beanFactory) {
/*  99 */     super(beanFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericWebApplicationContext(DefaultListableBeanFactory beanFactory, ServletContext servletContext) {
/* 110 */     super(beanFactory);
/* 111 */     this.servletContext = servletContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServletContext(ServletContext servletContext) {
/* 120 */     this.servletContext = servletContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServletContext getServletContext() {
/* 125 */     return this.servletContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getApplicationName() {
/* 130 */     return (this.servletContext != null) ? this.servletContext.getContextPath() : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConfigurableEnvironment createEnvironment() {
/* 138 */     return (ConfigurableEnvironment)new StandardServletEnvironment();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
/* 147 */     beanFactory.addBeanPostProcessor(new ServletContextAwareProcessor(this.servletContext));
/* 148 */     beanFactory.ignoreDependencyInterface(ServletContextAware.class);
/*     */     
/* 150 */     WebApplicationContextUtils.registerWebApplicationScopes(beanFactory, this.servletContext);
/* 151 */     WebApplicationContextUtils.registerEnvironmentBeans(beanFactory, this.servletContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Resource getResourceByPath(String path) {
/* 160 */     return (Resource)new ServletContextResource(this.servletContext, path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ResourcePatternResolver getResourcePatternResolver() {
/* 169 */     return (ResourcePatternResolver)new ServletContextResourcePatternResolver((ResourceLoader)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onRefresh() {
/* 177 */     this.themeSource = UiApplicationContextUtils.initThemeSource((ApplicationContext)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initPropertySources() {
/* 186 */     ConfigurableEnvironment env = getEnvironment();
/* 187 */     if (env instanceof ConfigurableWebEnvironment) {
/* 188 */       ((ConfigurableWebEnvironment)env).initPropertySources(this.servletContext, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Theme getTheme(String themeName) {
/* 194 */     return this.themeSource.getTheme(themeName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServletConfig(ServletConfig servletConfig) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletConfig getServletConfig() {
/* 209 */     throw new UnsupportedOperationException("GenericWebApplicationContext does not support getServletConfig()");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNamespace(String namespace) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNamespace() {
/* 220 */     throw new UnsupportedOperationException("GenericWebApplicationContext does not support getNamespace()");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConfigLocation(String configLocation) {
/* 226 */     if (StringUtils.hasText(configLocation)) {
/* 227 */       throw new UnsupportedOperationException("GenericWebApplicationContext does not support setConfigLocation(). Do you still have an 'contextConfigLocations' init-param set?");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConfigLocations(String... configLocations) {
/* 235 */     if (!ObjectUtils.isEmpty((Object[])configLocations)) {
/* 236 */       throw new UnsupportedOperationException("GenericWebApplicationContext does not support setConfigLocations(). Do you still have an 'contextConfigLocations' init-param set?");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getConfigLocations() {
/* 244 */     throw new UnsupportedOperationException("GenericWebApplicationContext does not support getConfigLocations()");
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\support\GenericWebApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */