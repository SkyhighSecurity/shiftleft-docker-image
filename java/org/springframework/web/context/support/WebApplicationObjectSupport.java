/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import java.io.File;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.support.ApplicationObjectSupport;
/*     */ import org.springframework.web.context.ServletContextAware;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.util.WebUtils;
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
/*     */ public abstract class WebApplicationObjectSupport
/*     */   extends ApplicationObjectSupport
/*     */   implements ServletContextAware
/*     */ {
/*     */   private ServletContext servletContext;
/*     */   
/*     */   public final void setServletContext(ServletContext servletContext) {
/*  48 */     if (servletContext != this.servletContext) {
/*  49 */       this.servletContext = servletContext;
/*  50 */       if (servletContext != null) {
/*  51 */         initServletContext(servletContext);
/*     */       }
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
/*     */   protected boolean isContextRequired() {
/*  67 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initApplicationContext(ApplicationContext context) {
/*  76 */     super.initApplicationContext(context);
/*  77 */     if (this.servletContext == null && context instanceof WebApplicationContext) {
/*  78 */       this.servletContext = ((WebApplicationContext)context).getServletContext();
/*  79 */       if (this.servletContext != null) {
/*  80 */         initServletContext(this.servletContext);
/*     */       }
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
/*     */   protected void initServletContext(ServletContext servletContext) {}
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
/*     */   protected final WebApplicationContext getWebApplicationContext() throws IllegalStateException {
/* 107 */     ApplicationContext ctx = getApplicationContext();
/* 108 */     if (ctx instanceof WebApplicationContext) {
/* 109 */       return (WebApplicationContext)getApplicationContext();
/*     */     }
/* 111 */     if (isContextRequired()) {
/* 112 */       throw new IllegalStateException("WebApplicationObjectSupport instance [" + this + "] does not run in a WebApplicationContext but in: " + ctx);
/*     */     }
/*     */ 
/*     */     
/* 116 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ServletContext getServletContext() throws IllegalStateException {
/* 125 */     if (this.servletContext != null) {
/* 126 */       return this.servletContext;
/*     */     }
/* 128 */     WebApplicationContext wac = getWebApplicationContext();
/* 129 */     if (wac == null) {
/* 130 */       return null;
/*     */     }
/* 132 */     ServletContext servletContext = wac.getServletContext();
/* 133 */     if (servletContext == null && isContextRequired()) {
/* 134 */       throw new IllegalStateException("WebApplicationObjectSupport instance [" + this + "] does not run within a ServletContext. Make sure the object is fully configured!");
/*     */     }
/*     */     
/* 137 */     return servletContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final File getTempDir() throws IllegalStateException {
/* 148 */     return WebUtils.getTempDir(getServletContext());
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\support\WebApplicationObjectSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */