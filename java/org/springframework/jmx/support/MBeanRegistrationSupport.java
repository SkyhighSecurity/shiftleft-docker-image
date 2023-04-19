/*     */ package org.springframework.jmx.support;
/*     */ 
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javax.management.InstanceAlreadyExistsException;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.JMException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectInstance;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.Constants;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MBeanRegistrationSupport
/*     */ {
/*     */   @Deprecated
/*     */   public static final int REGISTRATION_FAIL_ON_EXISTING = 0;
/*     */   @Deprecated
/*     */   public static final int REGISTRATION_IGNORE_EXISTING = 1;
/*     */   @Deprecated
/*     */   public static final int REGISTRATION_REPLACE_EXISTING = 2;
/* 100 */   private static final Constants constants = new Constants(MBeanRegistrationSupport.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MBeanServer server;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   private final Set<ObjectName> registeredBeans = new LinkedHashSet<ObjectName>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   private RegistrationPolicy registrationPolicy = RegistrationPolicy.FAIL_ON_EXISTING;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServer(MBeanServer server) {
/* 130 */     this.server = server;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final MBeanServer getServer() {
/* 137 */     return this.server;
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
/*     */   @Deprecated
/*     */   public void setRegistrationBehaviorName(String registrationBehavior) {
/* 151 */     setRegistrationBehavior(constants.asNumber(registrationBehavior).intValue());
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
/*     */   @Deprecated
/*     */   public void setRegistrationBehavior(int registrationBehavior) {
/* 166 */     setRegistrationPolicy(RegistrationPolicy.valueOf(registrationBehavior));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRegistrationPolicy(RegistrationPolicy registrationPolicy) {
/* 176 */     Assert.notNull(registrationPolicy, "RegistrationPolicy must not be null");
/* 177 */     this.registrationPolicy = registrationPolicy;
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
/*     */   protected void doRegister(Object mbean, ObjectName objectName) throws JMException {
/*     */     ObjectName actualObjectName;
/* 192 */     synchronized (this.registeredBeans) {
/* 193 */       ObjectInstance registeredBean = null;
/*     */       try {
/* 195 */         registeredBean = this.server.registerMBean(mbean, objectName);
/*     */       }
/* 197 */       catch (InstanceAlreadyExistsException ex) {
/* 198 */         if (this.registrationPolicy == RegistrationPolicy.IGNORE_EXISTING) {
/* 199 */           if (this.logger.isDebugEnabled()) {
/* 200 */             this.logger.debug("Ignoring existing MBean at [" + objectName + "]");
/*     */           }
/*     */         }
/* 203 */         else if (this.registrationPolicy == RegistrationPolicy.REPLACE_EXISTING) {
/*     */           try {
/* 205 */             if (this.logger.isDebugEnabled()) {
/* 206 */               this.logger.debug("Replacing existing MBean at [" + objectName + "]");
/*     */             }
/* 208 */             this.server.unregisterMBean(objectName);
/* 209 */             registeredBean = this.server.registerMBean(mbean, objectName);
/*     */           }
/* 211 */           catch (InstanceNotFoundException ex2) {
/* 212 */             if (this.logger.isErrorEnabled()) {
/* 213 */               this.logger.error("Unable to replace existing MBean at [" + objectName + "]", ex2);
/*     */             }
/* 215 */             throw ex;
/*     */           } 
/*     */         } else {
/*     */           
/* 219 */           throw ex;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 224 */       actualObjectName = (registeredBean != null) ? registeredBean.getObjectName() : null;
/* 225 */       if (actualObjectName == null) {
/* 226 */         actualObjectName = objectName;
/*     */       }
/* 228 */       this.registeredBeans.add(actualObjectName);
/*     */     } 
/*     */     
/* 231 */     onRegister(actualObjectName, mbean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void unregisterBeans() {
/*     */     Set<ObjectName> snapshot;
/* 239 */     synchronized (this.registeredBeans) {
/* 240 */       snapshot = new LinkedHashSet<ObjectName>(this.registeredBeans);
/*     */     } 
/* 242 */     if (!snapshot.isEmpty()) {
/* 243 */       this.logger.info("Unregistering JMX-exposed beans");
/* 244 */       for (ObjectName objectName : snapshot) {
/* 245 */         doUnregister(objectName);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doUnregister(ObjectName objectName) {
/* 255 */     boolean actuallyUnregistered = false;
/*     */     
/* 257 */     synchronized (this.registeredBeans) {
/* 258 */       if (this.registeredBeans.remove(objectName)) {
/*     */         
/*     */         try {
/* 261 */           if (this.server.isRegistered(objectName)) {
/* 262 */             this.server.unregisterMBean(objectName);
/* 263 */             actuallyUnregistered = true;
/*     */           
/*     */           }
/* 266 */           else if (this.logger.isWarnEnabled()) {
/* 267 */             this.logger.warn("Could not unregister MBean [" + objectName + "] as said MBean is not registered (perhaps already unregistered by an external process)");
/*     */           
/*     */           }
/*     */         
/*     */         }
/* 272 */         catch (JMException ex) {
/* 273 */           if (this.logger.isErrorEnabled()) {
/* 274 */             this.logger.error("Could not unregister MBean [" + objectName + "]", ex);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 280 */     if (actuallyUnregistered) {
/* 281 */       onUnregister(objectName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ObjectName[] getRegisteredObjectNames() {
/* 289 */     synchronized (this.registeredBeans) {
/* 290 */       return this.registeredBeans.<ObjectName>toArray(new ObjectName[this.registeredBeans.size()]);
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
/*     */   protected void onRegister(ObjectName objectName, Object mbean) {
/* 303 */     onRegister(objectName);
/*     */   }
/*     */   
/*     */   protected void onRegister(ObjectName objectName) {}
/*     */   
/*     */   protected void onUnregister(ObjectName objectName) {}
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\support\MBeanRegistrationSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */