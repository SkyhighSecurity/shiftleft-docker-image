/*     */ package org.springframework.jmx.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.management.JMException;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.remote.JMXConnectorServer;
/*     */ import javax.management.remote.JMXConnectorServerFactory;
/*     */ import javax.management.remote.JMXServiceURL;
/*     */ import javax.management.remote.MBeanServerForwarder;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.jmx.JmxException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConnectorServerFactoryBean
/*     */   extends MBeanRegistrationSupport
/*     */   implements FactoryBean<JMXConnectorServer>, InitializingBean, DisposableBean
/*     */ {
/*     */   public static final String DEFAULT_SERVICE_URL = "service:jmx:jmxmp://localhost:9875";
/*  62 */   private String serviceUrl = "service:jmx:jmxmp://localhost:9875";
/*     */   
/*  64 */   private Map<String, Object> environment = new HashMap<String, Object>();
/*     */ 
/*     */   
/*     */   private MBeanServerForwarder forwarder;
/*     */ 
/*     */   
/*     */   private ObjectName objectName;
/*     */ 
/*     */   
/*     */   private boolean threaded = false;
/*     */   
/*     */   private boolean daemon = false;
/*     */   
/*     */   private JMXConnectorServer connectorServer;
/*     */ 
/*     */   
/*     */   public void setServiceUrl(String serviceUrl) {
/*  81 */     this.serviceUrl = serviceUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironment(Properties environment) {
/*  89 */     CollectionUtils.mergePropertiesIntoMap(environment, this.environment);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironmentMap(Map<String, ?> environment) {
/*  97 */     if (environment != null) {
/*  98 */       this.environment.putAll(environment);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setForwarder(MBeanServerForwarder forwarder) {
/* 106 */     this.forwarder = forwarder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setObjectName(Object objectName) throws MalformedObjectNameException {
/* 116 */     this.objectName = ObjectNameManager.getInstance(objectName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThreaded(boolean threaded) {
/* 123 */     this.threaded = threaded;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDaemon(boolean daemon) {
/* 131 */     this.daemon = daemon;
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
/*     */   public void afterPropertiesSet() throws JMException, IOException {
/* 146 */     if (this.server == null) {
/* 147 */       this.server = JmxUtils.locateMBeanServer();
/*     */     }
/*     */ 
/*     */     
/* 151 */     JMXServiceURL url = new JMXServiceURL(this.serviceUrl);
/*     */ 
/*     */     
/* 154 */     this.connectorServer = JMXConnectorServerFactory.newJMXConnectorServer(url, this.environment, this.server);
/*     */ 
/*     */     
/* 157 */     if (this.forwarder != null) {
/* 158 */       this.connectorServer.setMBeanServerForwarder(this.forwarder);
/*     */     }
/*     */ 
/*     */     
/* 162 */     if (this.objectName != null) {
/* 163 */       doRegister(this.connectorServer, this.objectName);
/*     */     }
/*     */     
/*     */     try {
/* 167 */       if (this.threaded) {
/*     */         
/* 169 */         Thread connectorThread = new Thread()
/*     */           {
/*     */             public void run() {
/*     */               try {
/* 173 */                 ConnectorServerFactoryBean.this.connectorServer.start();
/*     */               }
/* 175 */               catch (IOException ex) {
/* 176 */                 throw new JmxException("Could not start JMX connector server after delay", ex);
/*     */               } 
/*     */             }
/*     */           };
/*     */         
/* 181 */         connectorThread.setName("JMX Connector Thread [" + this.serviceUrl + "]");
/* 182 */         connectorThread.setDaemon(this.daemon);
/* 183 */         connectorThread.start();
/*     */       }
/*     */       else {
/*     */         
/* 187 */         this.connectorServer.start();
/*     */       } 
/*     */       
/* 190 */       if (this.logger.isInfoEnabled()) {
/* 191 */         this.logger.info("JMX connector server started: " + this.connectorServer);
/*     */       
/*     */       }
/*     */     }
/* 195 */     catch (IOException ex) {
/*     */       
/* 197 */       unregisterBeans();
/* 198 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JMXConnectorServer getObject() {
/* 205 */     return this.connectorServer;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<? extends JMXConnectorServer> getObjectType() {
/* 210 */     return (this.connectorServer != null) ? (Class)this.connectorServer.getClass() : JMXConnectorServer.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 215 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws IOException {
/* 226 */     if (this.logger.isInfoEnabled()) {
/* 227 */       this.logger.info("Stopping JMX connector server: " + this.connectorServer);
/*     */     }
/*     */     try {
/* 230 */       this.connectorServer.stop();
/*     */     } finally {
/*     */       
/* 233 */       unregisterBeans();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\support\ConnectorServerFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */