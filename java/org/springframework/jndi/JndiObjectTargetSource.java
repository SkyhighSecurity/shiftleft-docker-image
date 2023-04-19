/*     */ package org.springframework.jndi;
/*     */ 
/*     */ import javax.naming.NamingException;
/*     */ import org.springframework.aop.TargetSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JndiObjectTargetSource
/*     */   extends JndiObjectLocator
/*     */   implements TargetSource
/*     */ {
/*     */   private boolean lookupOnStartup = true;
/*     */   private boolean cache = true;
/*     */   private Object cachedObject;
/*     */   private Class<?> targetClass;
/*     */   
/*     */   public void setLookupOnStartup(boolean lookupOnStartup) {
/*  79 */     this.lookupOnStartup = lookupOnStartup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCache(boolean cache) {
/*  90 */     this.cache = cache;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws NamingException {
/*  95 */     super.afterPropertiesSet();
/*  96 */     if (this.lookupOnStartup) {
/*  97 */       Object object = lookup();
/*  98 */       if (this.cache) {
/*  99 */         this.cachedObject = object;
/*     */       } else {
/*     */         
/* 102 */         this.targetClass = object.getClass();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getTargetClass() {
/* 110 */     if (this.cachedObject != null) {
/* 111 */       return this.cachedObject.getClass();
/*     */     }
/* 113 */     if (this.targetClass != null) {
/* 114 */       return this.targetClass;
/*     */     }
/*     */     
/* 117 */     return getExpectedType();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/* 123 */     return (this.cachedObject != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getTarget() {
/*     */     try {
/* 129 */       if (this.lookupOnStartup || !this.cache) {
/* 130 */         return (this.cachedObject != null) ? this.cachedObject : lookup();
/*     */       }
/*     */       
/* 133 */       synchronized (this) {
/* 134 */         if (this.cachedObject == null) {
/* 135 */           this.cachedObject = lookup();
/*     */         }
/* 137 */         return this.cachedObject;
/*     */       }
/*     */     
/*     */     }
/* 141 */     catch (NamingException ex) {
/* 142 */       throw new JndiLookupFailureException("JndiObjectTargetSource failed to obtain new target object", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void releaseTarget(Object target) {}
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jndi\JndiObjectTargetSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */