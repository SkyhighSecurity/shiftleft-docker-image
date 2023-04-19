/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
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
/*     */ public class ServletContextAwareProcessor
/*     */   implements BeanPostProcessor
/*     */ {
/*     */   private ServletContext servletContext;
/*     */   private ServletConfig servletConfig;
/*     */   
/*     */   protected ServletContextAwareProcessor() {}
/*     */   
/*     */   public ServletContextAwareProcessor(ServletContext servletContext) {
/*  60 */     this(servletContext, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletContextAwareProcessor(ServletConfig servletConfig) {
/*  67 */     this(null, servletConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletContextAwareProcessor(ServletContext servletContext, ServletConfig servletConfig) {
/*  74 */     this.servletContext = servletContext;
/*  75 */     this.servletConfig = servletConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ServletContext getServletContext() {
/*  85 */     if (this.servletContext == null && getServletConfig() != null) {
/*  86 */       return getServletConfig().getServletContext();
/*     */     }
/*  88 */     return this.servletContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ServletConfig getServletConfig() {
/*  97 */     return this.servletConfig;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
/* 102 */     if (getServletContext() != null && bean instanceof ServletContextAware) {
/* 103 */       ((ServletContextAware)bean).setServletContext(getServletContext());
/*     */     }
/* 105 */     if (getServletConfig() != null && bean instanceof ServletConfigAware) {
/* 106 */       ((ServletConfigAware)bean).setServletConfig(getServletConfig());
/*     */     }
/* 108 */     return bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName) {
/* 113 */     return bean;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\support\ServletContextAwareProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */