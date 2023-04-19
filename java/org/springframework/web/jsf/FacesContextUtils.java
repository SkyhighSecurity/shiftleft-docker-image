/*     */ package org.springframework.web.jsf;
/*     */ 
/*     */ import javax.faces.context.ExternalContext;
/*     */ import javax.faces.context.FacesContext;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class FacesContextUtils
/*     */ {
/*     */   public static WebApplicationContext getWebApplicationContext(FacesContext fc) {
/*  50 */     Assert.notNull(fc, "FacesContext must not be null");
/*  51 */     Object attr = fc.getExternalContext().getApplicationMap().get(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
/*     */     
/*  53 */     if (attr == null) {
/*  54 */       return null;
/*     */     }
/*  56 */     if (attr instanceof RuntimeException) {
/*  57 */       throw (RuntimeException)attr;
/*     */     }
/*  59 */     if (attr instanceof Error) {
/*  60 */       throw (Error)attr;
/*     */     }
/*  62 */     if (!(attr instanceof WebApplicationContext)) {
/*  63 */       throw new IllegalStateException("Root context attribute is not of type WebApplicationContext: " + attr);
/*     */     }
/*  65 */     return (WebApplicationContext)attr;
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
/*     */   public static WebApplicationContext getRequiredWebApplicationContext(FacesContext fc) throws IllegalStateException {
/*  79 */     WebApplicationContext wac = getWebApplicationContext(fc);
/*  80 */     if (wac == null) {
/*  81 */       throw new IllegalStateException("No WebApplicationContext found: no ContextLoaderListener registered?");
/*     */     }
/*  83 */     return wac;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getSessionMutex(FacesContext fc) {
/* 107 */     Assert.notNull(fc, "FacesContext must not be null");
/* 108 */     ExternalContext ec = fc.getExternalContext();
/* 109 */     Object mutex = ec.getSessionMap().get(WebUtils.SESSION_MUTEX_ATTRIBUTE);
/* 110 */     if (mutex == null) {
/* 111 */       mutex = ec.getSession(true);
/*     */     }
/* 113 */     return mutex;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\jsf\FacesContextUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */