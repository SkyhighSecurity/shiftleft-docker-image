/*     */ package org.springframework.ejb.access;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.NamingException;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.jndi.JndiObjectLocator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractSlsbInvokerInterceptor
/*     */   extends JndiObjectLocator
/*     */   implements MethodInterceptor
/*     */ {
/*     */   private boolean lookupHomeOnStartup = true;
/*     */   private boolean cacheHome = true;
/*     */   private boolean exposeAccessContext = false;
/*     */   private Object cachedHome;
/*     */   private Method createMethod;
/*  60 */   private final Object homeMonitor = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLookupHomeOnStartup(boolean lookupHomeOnStartup) {
/*  71 */     this.lookupHomeOnStartup = lookupHomeOnStartup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheHome(boolean cacheHome) {
/*  82 */     this.cacheHome = cacheHome;
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
/*     */   public void setExposeAccessContext(boolean exposeAccessContext) {
/*  94 */     this.exposeAccessContext = exposeAccessContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws NamingException {
/* 105 */     super.afterPropertiesSet();
/* 106 */     if (this.lookupHomeOnStartup)
/*     */     {
/* 108 */       refreshHome();
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
/*     */   protected void refreshHome() throws NamingException {
/* 120 */     synchronized (this.homeMonitor) {
/* 121 */       Object home = lookup();
/* 122 */       if (this.cacheHome) {
/* 123 */         this.cachedHome = home;
/* 124 */         this.createMethod = getCreateMethod(home);
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
/*     */   protected Method getCreateMethod(Object home) throws EjbAccessException {
/*     */     try {
/* 138 */       return home.getClass().getMethod("create", new Class[0]);
/*     */     }
/* 140 */     catch (NoSuchMethodException ex) {
/* 141 */       throw new EjbAccessException("EJB home [" + home + "] has no no-arg create() method");
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
/*     */ 
/*     */   
/*     */   protected Object getHome() throws NamingException {
/* 158 */     if (!this.cacheHome || (this.lookupHomeOnStartup && !isHomeRefreshable())) {
/* 159 */       return (this.cachedHome != null) ? this.cachedHome : lookup();
/*     */     }
/*     */     
/* 162 */     synchronized (this.homeMonitor) {
/* 163 */       if (this.cachedHome == null) {
/* 164 */         this.cachedHome = lookup();
/* 165 */         this.createMethod = getCreateMethod(this.cachedHome);
/*     */       } 
/* 167 */       return this.cachedHome;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isHomeRefreshable() {
/* 177 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(MethodInvocation invocation) throws Throwable {
/* 187 */     Context ctx = this.exposeAccessContext ? getJndiTemplate().getContext() : null;
/*     */     try {
/* 189 */       return invokeInContext(invocation);
/*     */     } finally {
/*     */       
/* 192 */       getJndiTemplate().releaseContext(ctx);
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
/*     */   protected abstract Object invokeInContext(MethodInvocation paramMethodInvocation) throws Throwable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object create() throws NamingException, InvocationTargetException {
/*     */     try {
/* 215 */       Object home = getHome();
/* 216 */       Method createMethodToUse = this.createMethod;
/* 217 */       if (createMethodToUse == null) {
/* 218 */         createMethodToUse = getCreateMethod(home);
/*     */       }
/* 220 */       if (createMethodToUse == null) {
/* 221 */         return home;
/*     */       }
/*     */       
/* 224 */       return createMethodToUse.invoke(home, (Object[])null);
/*     */     }
/* 226 */     catch (IllegalAccessException ex) {
/* 227 */       throw new EjbAccessException("Could not access EJB home create() method", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\ejb\access\AbstractSlsbInvokerInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */