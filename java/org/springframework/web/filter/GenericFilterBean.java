/*     */ package org.springframework.web.filter;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.servlet.Filter;
/*     */ import javax.servlet.FilterConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.PropertyAccessorFactory;
/*     */ import org.springframework.beans.PropertyValue;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.EnvironmentAware;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.EnvironmentCapable;
/*     */ import org.springframework.core.env.PropertyResolver;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceEditor;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.context.ServletContextAware;
/*     */ import org.springframework.web.context.support.ServletContextResourceLoader;
/*     */ import org.springframework.web.context.support.StandardServletEnvironment;
/*     */ import org.springframework.web.util.NestedServletException;
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
/*     */ public abstract class GenericFilterBean
/*     */   implements Filter, BeanNameAware, EnvironmentAware, EnvironmentCapable, ServletContextAware, InitializingBean, DisposableBean
/*     */ {
/*  84 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private String beanName;
/*     */   
/*     */   private Environment environment;
/*     */   
/*     */   private ServletContext servletContext;
/*     */   
/*     */   private FilterConfig filterConfig;
/*     */   
/*  94 */   private final Set<String> requiredProperties = new HashSet<String>(4);
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
/*     */   public void setBeanName(String beanName) {
/* 106 */     this.beanName = beanName;
/*     */   }
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
/*     */   public void setEnvironment(Environment environment) {
/* 119 */     this.environment = environment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Environment getEnvironment() {
/* 130 */     if (this.environment == null) {
/* 131 */       this.environment = createEnvironment();
/*     */     }
/* 133 */     return this.environment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Environment createEnvironment() {
/* 143 */     return (Environment)new StandardServletEnvironment();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServletContext(ServletContext servletContext) {
/* 155 */     this.servletContext = servletContext;
/*     */   }
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
/*     */   public void afterPropertiesSet() throws ServletException {
/* 168 */     initFilterBean();
/*     */   }
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
/*     */   public void destroy() {}
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
/*     */   protected final void addRequiredProperty(String property) {
/* 192 */     this.requiredProperties.add(property);
/*     */   }
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
/*     */   public final void init(FilterConfig filterConfig) throws ServletException {
/* 206 */     Assert.notNull(filterConfig, "FilterConfig must not be null");
/* 207 */     if (this.logger.isDebugEnabled()) {
/* 208 */       this.logger.debug("Initializing filter '" + filterConfig.getFilterName() + "'");
/*     */     }
/*     */     
/* 211 */     this.filterConfig = filterConfig;
/*     */ 
/*     */     
/* 214 */     FilterConfigPropertyValues filterConfigPropertyValues = new FilterConfigPropertyValues(filterConfig, this.requiredProperties);
/* 215 */     if (!filterConfigPropertyValues.isEmpty()) {
/*     */       try {
/* 217 */         StandardServletEnvironment standardServletEnvironment; BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
/* 218 */         ServletContextResourceLoader servletContextResourceLoader = new ServletContextResourceLoader(filterConfig.getServletContext());
/* 219 */         Environment env = this.environment;
/* 220 */         if (env == null) {
/* 221 */           standardServletEnvironment = new StandardServletEnvironment();
/*     */         }
/* 223 */         bw.registerCustomEditor(Resource.class, (PropertyEditor)new ResourceEditor((ResourceLoader)servletContextResourceLoader, (PropertyResolver)standardServletEnvironment));
/* 224 */         initBeanWrapper(bw);
/* 225 */         bw.setPropertyValues((PropertyValues)filterConfigPropertyValues, true);
/*     */       }
/* 227 */       catch (BeansException ex) {
/*     */         
/* 229 */         String msg = "Failed to set bean properties on filter '" + filterConfig.getFilterName() + "': " + ex.getMessage();
/* 230 */         this.logger.error(msg, (Throwable)ex);
/* 231 */         throw new NestedServletException(msg, ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 236 */     initFilterBean();
/*     */     
/* 238 */     if (this.logger.isDebugEnabled()) {
/* 239 */       this.logger.debug("Filter '" + filterConfig.getFilterName() + "' configured successfully");
/*     */     }
/*     */   }
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
/*     */   protected void initBeanWrapper(BeanWrapper bw) throws BeansException {}
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
/*     */   protected void initFilterBean() throws ServletException {}
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
/*     */   public final FilterConfig getFilterConfig() {
/* 278 */     return this.filterConfig;
/*     */   }
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
/*     */   protected final String getFilterName() {
/* 293 */     return (this.filterConfig != null) ? this.filterConfig.getFilterName() : this.beanName;
/*     */   }
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
/*     */   protected final ServletContext getServletContext() {
/* 308 */     return (this.filterConfig != null) ? this.filterConfig.getServletContext() : this.servletContext;
/*     */   }
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
/*     */   private static class FilterConfigPropertyValues
/*     */     extends MutablePropertyValues
/*     */   {
/*     */     public FilterConfigPropertyValues(FilterConfig config, Set<String> requiredProperties) throws ServletException {
/* 328 */       Set<String> missingProps = !CollectionUtils.isEmpty(requiredProperties) ? new HashSet<String>(requiredProperties) : null;
/*     */ 
/*     */       
/* 331 */       Enumeration<String> paramNames = config.getInitParameterNames();
/* 332 */       while (paramNames.hasMoreElements()) {
/* 333 */         String property = paramNames.nextElement();
/* 334 */         Object value = config.getInitParameter(property);
/* 335 */         addPropertyValue(new PropertyValue(property, value));
/* 336 */         if (missingProps != null) {
/* 337 */           missingProps.remove(property);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 342 */       if (!CollectionUtils.isEmpty(missingProps))
/* 343 */         throw new ServletException("Initialization from FilterConfig for filter '" + config
/* 344 */             .getFilterName() + "' failed; the following required properties were missing: " + 
/*     */             
/* 346 */             StringUtils.collectionToDelimitedString(missingProps, ", ")); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\filter\GenericFilterBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */