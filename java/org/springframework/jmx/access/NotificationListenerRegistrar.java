/*     */ package org.springframework.jmx.access;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import javax.management.MBeanServerConnection;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.remote.JMXServiceURL;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.jmx.JmxException;
/*     */ import org.springframework.jmx.MBeanServerNotFoundException;
/*     */ import org.springframework.jmx.support.NotificationListenerHolder;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NotificationListenerRegistrar
/*     */   extends NotificationListenerHolder
/*     */   implements InitializingBean, DisposableBean
/*     */ {
/*  52 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private MBeanServerConnection server;
/*     */   
/*     */   private JMXServiceURL serviceUrl;
/*     */   
/*     */   private Map<String, ?> environment;
/*     */   
/*     */   private String agentId;
/*     */   
/*  62 */   private final ConnectorDelegate connector = new ConnectorDelegate();
/*     */ 
/*     */ 
/*     */   
/*     */   private ObjectName[] actualObjectNames;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServer(MBeanServerConnection server) {
/*  72 */     this.server = server;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironment(Map<String, ?> environment) {
/*  80 */     this.environment = environment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, ?> getEnvironment() {
/*  91 */     return this.environment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServiceUrl(String url) throws MalformedURLException {
/*  98 */     this.serviceUrl = new JMXServiceURL(url);
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
/*     */   public void setAgentId(String agentId) {
/* 110 */     this.agentId = agentId;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 116 */     if (getNotificationListener() == null) {
/* 117 */       throw new IllegalArgumentException("Property 'notificationListener' is required");
/*     */     }
/* 119 */     if (CollectionUtils.isEmpty(this.mappedObjectNames)) {
/* 120 */       throw new IllegalArgumentException("Property 'mappedObjectName' is required");
/*     */     }
/* 122 */     prepare();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() {
/* 131 */     if (this.server == null) {
/* 132 */       this.server = this.connector.connect(this.serviceUrl, this.environment, this.agentId);
/*     */     }
/*     */     try {
/* 135 */       this.actualObjectNames = getResolvedObjectNames();
/* 136 */       if (this.logger.isDebugEnabled()) {
/* 137 */         this.logger.debug("Registering NotificationListener for MBeans " + Arrays.<ObjectName>asList(this.actualObjectNames));
/*     */       }
/* 139 */       for (ObjectName actualObjectName : this.actualObjectNames) {
/* 140 */         this.server.addNotificationListener(actualObjectName, 
/* 141 */             getNotificationListener(), getNotificationFilter(), getHandback());
/*     */       }
/*     */     }
/* 144 */     catch (IOException ex) {
/* 145 */       throw new MBeanServerNotFoundException("Could not connect to remote MBeanServer at URL [" + this.serviceUrl + "]", ex);
/*     */     
/*     */     }
/* 148 */     catch (Exception ex) {
/* 149 */       throw new JmxException("Unable to register NotificationListener", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/*     */     try {
/* 159 */       if (this.actualObjectNames != null) {
/* 160 */         for (ObjectName actualObjectName : this.actualObjectNames) {
/*     */           try {
/* 162 */             this.server.removeNotificationListener(actualObjectName, 
/* 163 */                 getNotificationListener(), getNotificationFilter(), getHandback());
/*     */           }
/* 165 */           catch (Exception ex) {
/* 166 */             if (this.logger.isDebugEnabled()) {
/* 167 */               this.logger.debug("Unable to unregister NotificationListener", ex);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } finally {
/*     */       
/* 174 */       this.connector.close();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\access\NotificationListenerRegistrar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */