/*     */ package org.springframework.jmx.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.management.MBeanServerConnection;
/*     */ import javax.management.remote.JMXConnector;
/*     */ import javax.management.remote.JMXConnectorFactory;
/*     */ import javax.management.remote.JMXServiceURL;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.aop.target.AbstractLazyCreationTargetSource;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class MBeanServerConnectionFactoryBean
/*     */   implements FactoryBean<MBeanServerConnection>, BeanClassLoaderAware, InitializingBean, DisposableBean
/*     */ {
/*     */   private JMXServiceURL serviceUrl;
/*  57 */   private Map<String, Object> environment = new HashMap<String, Object>();
/*     */   
/*     */   private boolean connectOnStartup = true;
/*     */   
/*  61 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */   
/*     */   private JMXConnector connector;
/*     */ 
/*     */   
/*     */   private MBeanServerConnection connection;
/*     */ 
/*     */   
/*     */   private JMXConnectorLazyInitTargetSource connectorTargetSource;
/*     */ 
/*     */   
/*     */   public void setServiceUrl(String url) throws MalformedURLException {
/*  74 */     this.serviceUrl = new JMXServiceURL(url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironment(Properties environment) {
/*  82 */     CollectionUtils.mergePropertiesIntoMap(environment, this.environment);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironmentMap(Map<String, ?> environment) {
/*  90 */     if (environment != null) {
/*  91 */       this.environment.putAll(environment);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectOnStartup(boolean connectOnStartup) {
/* 101 */     this.connectOnStartup = connectOnStartup;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 106 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws IOException {
/* 116 */     if (this.serviceUrl == null) {
/* 117 */       throw new IllegalArgumentException("Property 'serviceUrl' is required");
/*     */     }
/*     */     
/* 120 */     if (this.connectOnStartup) {
/* 121 */       connect();
/*     */     } else {
/*     */       
/* 124 */       createLazyConnection();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void connect() throws IOException {
/* 133 */     this.connector = JMXConnectorFactory.connect(this.serviceUrl, this.environment);
/* 134 */     this.connection = this.connector.getMBeanServerConnection();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createLazyConnection() {
/* 141 */     this.connectorTargetSource = new JMXConnectorLazyInitTargetSource();
/* 142 */     MBeanServerConnectionLazyInitTargetSource mBeanServerConnectionLazyInitTargetSource = new MBeanServerConnectionLazyInitTargetSource();
/*     */     
/* 144 */     this
/* 145 */       .connector = (JMXConnector)(new ProxyFactory(JMXConnector.class, (TargetSource)this.connectorTargetSource)).getProxy(this.beanClassLoader);
/* 146 */     this
/* 147 */       .connection = (MBeanServerConnection)(new ProxyFactory(MBeanServerConnection.class, (TargetSource)mBeanServerConnectionLazyInitTargetSource)).getProxy(this.beanClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MBeanServerConnection getObject() {
/* 153 */     return this.connection;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<? extends MBeanServerConnection> getObjectType() {
/* 158 */     return (this.connection != null) ? (Class)this.connection.getClass() : MBeanServerConnection.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 163 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws IOException {
/* 172 */     if (this.connectorTargetSource == null || this.connectorTargetSource.isInitialized()) {
/* 173 */       this.connector.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class JMXConnectorLazyInitTargetSource
/*     */     extends AbstractLazyCreationTargetSource
/*     */   {
/*     */     private JMXConnectorLazyInitTargetSource() {}
/*     */ 
/*     */ 
/*     */     
/*     */     protected Object createObject() throws Exception {
/* 188 */       return JMXConnectorFactory.connect(MBeanServerConnectionFactoryBean.this.serviceUrl, MBeanServerConnectionFactoryBean.this.environment);
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getTargetClass() {
/* 193 */       return JMXConnector.class;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class MBeanServerConnectionLazyInitTargetSource
/*     */     extends AbstractLazyCreationTargetSource
/*     */   {
/*     */     private MBeanServerConnectionLazyInitTargetSource() {}
/*     */ 
/*     */     
/*     */     protected Object createObject() throws Exception {
/* 205 */       return MBeanServerConnectionFactoryBean.this.connector.getMBeanServerConnection();
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getTargetClass() {
/* 210 */       return MBeanServerConnection.class;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\support\MBeanServerConnectionFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */