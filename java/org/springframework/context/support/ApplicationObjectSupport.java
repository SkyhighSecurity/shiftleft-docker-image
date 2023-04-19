/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.context.MessageSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ApplicationObjectSupport
/*     */   implements ApplicationContextAware
/*     */ {
/*  50 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   private ApplicationContext applicationContext;
/*     */ 
/*     */   
/*     */   private MessageSourceAccessor messageSourceAccessor;
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setApplicationContext(ApplicationContext context) throws BeansException {
/*  61 */     if (context == null && !isContextRequired()) {
/*     */       
/*  63 */       this.applicationContext = null;
/*  64 */       this.messageSourceAccessor = null;
/*     */     }
/*  66 */     else if (this.applicationContext == null) {
/*     */       
/*  68 */       if (!requiredContextClass().isInstance(context)) {
/*  69 */         throw new ApplicationContextException("Invalid application context: needs to be of type [" + 
/*  70 */             requiredContextClass().getName() + "]");
/*     */       }
/*  72 */       this.applicationContext = context;
/*  73 */       this.messageSourceAccessor = new MessageSourceAccessor((MessageSource)context);
/*  74 */       initApplicationContext(context);
/*     */ 
/*     */     
/*     */     }
/*  78 */     else if (this.applicationContext != context) {
/*  79 */       throw new ApplicationContextException("Cannot reinitialize with different application context: current one is [" + this.applicationContext + "], passed-in one is [" + context + "]");
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
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> requiredContextClass() {
/* 104 */     return ApplicationContext.class;
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
/*     */   protected void initApplicationContext(ApplicationContext context) throws BeansException {
/* 120 */     initApplicationContext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initApplicationContext() throws BeansException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ApplicationContext getApplicationContext() throws IllegalStateException {
/* 140 */     if (this.applicationContext == null && isContextRequired()) {
/* 141 */       throw new IllegalStateException("ApplicationObjectSupport instance [" + this + "] does not run in an ApplicationContext");
/*     */     }
/*     */     
/* 144 */     return this.applicationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final MessageSourceAccessor getMessageSourceAccessor() throws IllegalStateException {
/* 153 */     if (this.messageSourceAccessor == null && isContextRequired()) {
/* 154 */       throw new IllegalStateException("ApplicationObjectSupport instance [" + this + "] does not run in an ApplicationContext");
/*     */     }
/*     */     
/* 157 */     return this.messageSourceAccessor;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\ApplicationObjectSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */