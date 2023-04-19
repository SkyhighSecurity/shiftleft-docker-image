/*     */ package org.springframework.jmx.support;
/*     */ 
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.MBeanServerFactory;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.jmx.MBeanServerNotFoundException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MBeanServerFactoryBean
/*     */   implements FactoryBean<MBeanServer>, InitializingBean, DisposableBean
/*     */ {
/*  56 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   private boolean locateExistingServerIfPossible = false;
/*     */ 
/*     */   
/*     */   private String agentId;
/*     */ 
/*     */   
/*     */   private String defaultDomain;
/*     */ 
/*     */   
/*     */   private boolean registerWithFactory = true;
/*     */ 
/*     */   
/*     */   private MBeanServer server;
/*     */   
/*     */   private boolean newlyRegistered = false;
/*     */ 
/*     */   
/*     */   public void setLocateExistingServerIfPossible(boolean locateExistingServerIfPossible) {
/*  77 */     this.locateExistingServerIfPossible = locateExistingServerIfPossible;
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
/*     */   public void setAgentId(String agentId) {
/*  91 */     this.agentId = agentId;
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
/*     */   public void setDefaultDomain(String defaultDomain) {
/* 103 */     this.defaultDomain = defaultDomain;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRegisterWithFactory(boolean registerWithFactory) {
/* 114 */     this.registerWithFactory = registerWithFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws MBeanServerNotFoundException {
/* 124 */     if (this.locateExistingServerIfPossible || this.agentId != null) {
/*     */       try {
/* 126 */         this.server = locateMBeanServer(this.agentId);
/*     */       }
/* 128 */       catch (MBeanServerNotFoundException ex) {
/*     */ 
/*     */         
/* 131 */         if (this.agentId != null) {
/* 132 */           throw ex;
/*     */         }
/* 134 */         this.logger.info("No existing MBeanServer found - creating new one");
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 139 */     if (this.server == null) {
/* 140 */       this.server = createMBeanServer(this.defaultDomain, this.registerWithFactory);
/* 141 */       this.newlyRegistered = this.registerWithFactory;
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected MBeanServer locateMBeanServer(String agentId) throws MBeanServerNotFoundException {
/* 161 */     return JmxUtils.locateMBeanServer(agentId);
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
/*     */   protected MBeanServer createMBeanServer(String defaultDomain, boolean registerWithFactory) {
/* 174 */     if (registerWithFactory) {
/* 175 */       return MBeanServerFactory.createMBeanServer(defaultDomain);
/*     */     }
/*     */     
/* 178 */     return MBeanServerFactory.newMBeanServer(defaultDomain);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MBeanServer getObject() {
/* 185 */     return this.server;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<? extends MBeanServer> getObjectType() {
/* 190 */     return (this.server != null) ? (Class)this.server.getClass() : MBeanServer.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 195 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 204 */     if (this.newlyRegistered)
/* 205 */       MBeanServerFactory.releaseMBeanServer(this.server); 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\support\MBeanServerFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */