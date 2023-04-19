/*     */ package org.springframework.jndi;
/*     */ 
/*     */ import javax.naming.NamingException;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JndiLocatorSupport
/*     */   extends JndiAccessor
/*     */ {
/*     */   public static final String CONTAINER_PREFIX = "java:comp/env/";
/*     */   private boolean resourceRef = false;
/*     */   
/*     */   public void setResourceRef(boolean resourceRef) {
/*  56 */     this.resourceRef = resourceRef;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isResourceRef() {
/*  63 */     return this.resourceRef;
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
/*     */   protected Object lookup(String jndiName) throws NamingException {
/*  77 */     return lookup(jndiName, (Class<?>)null);
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
/*     */   protected <T> T lookup(String jndiName, Class<T> requiredType) throws NamingException {
/*     */     T jndiObject;
/*  91 */     Assert.notNull(jndiName, "'jndiName' must not be null");
/*  92 */     String convertedName = convertJndiName(jndiName);
/*     */     
/*     */     try {
/*  95 */       jndiObject = getJndiTemplate().lookup(convertedName, requiredType);
/*     */     }
/*  97 */     catch (NamingException ex) {
/*  98 */       if (!convertedName.equals(jndiName)) {
/*     */         
/* 100 */         if (this.logger.isDebugEnabled()) {
/* 101 */           this.logger.debug("Converted JNDI name [" + convertedName + "] not found - trying original name [" + jndiName + "]. " + ex);
/*     */         }
/*     */         
/* 104 */         jndiObject = getJndiTemplate().lookup(jndiName, requiredType);
/*     */       } else {
/*     */         
/* 107 */         throw ex;
/*     */       } 
/*     */     } 
/* 110 */     if (this.logger.isDebugEnabled()) {
/* 111 */       this.logger.debug("Located object with JNDI name [" + convertedName + "]");
/*     */     }
/* 113 */     return jndiObject;
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
/*     */   protected String convertJndiName(String jndiName) {
/* 127 */     if (isResourceRef() && !jndiName.startsWith("java:comp/env/") && jndiName.indexOf(':') == -1) {
/* 128 */       jndiName = "java:comp/env/" + jndiName;
/*     */     }
/* 130 */     return jndiName;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jndi\JndiLocatorSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */