/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import javax.naming.NamingException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.jndi.JndiLocatorDelegate;
/*     */ import org.springframework.jndi.JndiTemplate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultManagedAwareThreadFactory
/*     */   extends CustomizableThreadFactory
/*     */   implements InitializingBean
/*     */ {
/*  50 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  52 */   private JndiLocatorDelegate jndiLocator = new JndiLocatorDelegate();
/*     */   
/*  54 */   private String jndiName = "java:comp/DefaultManagedThreadFactory";
/*     */ 
/*     */ 
/*     */   
/*     */   private ThreadFactory threadFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJndiTemplate(JndiTemplate jndiTemplate) {
/*  64 */     this.jndiLocator.setJndiTemplate(jndiTemplate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJndiEnvironment(Properties jndiEnvironment) {
/*  72 */     this.jndiLocator.setJndiEnvironment(jndiEnvironment);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResourceRef(boolean resourceRef) {
/*  82 */     this.jndiLocator.setResourceRef(resourceRef);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJndiName(String jndiName) {
/*  93 */     this.jndiName = jndiName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws NamingException {
/*  98 */     if (this.jndiName != null) {
/*     */       try {
/* 100 */         this.threadFactory = (ThreadFactory)this.jndiLocator.lookup(this.jndiName, ThreadFactory.class);
/*     */       }
/* 102 */       catch (NamingException ex) {
/* 103 */         if (this.logger.isDebugEnabled()) {
/* 104 */           this.logger.debug("Failed to retrieve [" + this.jndiName + "] from JNDI", ex);
/*     */         }
/* 106 */         this.logger.info("Could not find default managed thread factory in JNDI - proceeding with default local thread factory");
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Thread newThread(Runnable runnable) {
/* 115 */     if (this.threadFactory != null) {
/* 116 */       return this.threadFactory.newThread(runnable);
/*     */     }
/*     */     
/* 119 */     return super.newThread(runnable);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\concurrent\DefaultManagedAwareThreadFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */