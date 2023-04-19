/*     */ package org.springframework.jndi;
/*     */ 
/*     */ import javax.naming.NamingException;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JndiObjectLocator
/*     */   extends JndiLocatorSupport
/*     */   implements InitializingBean
/*     */ {
/*     */   private String jndiName;
/*     */   private Class<?> expectedType;
/*     */   
/*     */   public void setJndiName(String jndiName) {
/*  62 */     this.jndiName = jndiName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getJndiName() {
/*  69 */     return this.jndiName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpectedType(Class<?> expectedType) {
/*  77 */     this.expectedType = expectedType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getExpectedType() {
/*  85 */     return this.expectedType;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws IllegalArgumentException, NamingException {
/*  90 */     if (!StringUtils.hasLength(getJndiName())) {
/*  91 */       throw new IllegalArgumentException("Property 'jndiName' is required");
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
/*     */   protected Object lookup() throws NamingException {
/* 106 */     return lookup(getJndiName(), getExpectedType());
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jndi\JndiObjectLocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */