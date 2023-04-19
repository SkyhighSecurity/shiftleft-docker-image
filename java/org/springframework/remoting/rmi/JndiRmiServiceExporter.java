/*     */ package org.springframework.remoting.rmi;
/*     */ 
/*     */ import java.rmi.NoSuchObjectException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.util.Properties;
/*     */ import javax.naming.NamingException;
/*     */ import javax.rmi.PortableRemoteObject;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JndiRmiServiceExporter
/*     */   extends RmiBasedExporter
/*     */   implements InitializingBean, DisposableBean
/*     */ {
/*  70 */   private JndiTemplate jndiTemplate = new JndiTemplate();
/*     */ 
/*     */ 
/*     */   
/*     */   private String jndiName;
/*     */ 
/*     */ 
/*     */   
/*     */   private Remote exportedObject;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJndiTemplate(JndiTemplate jndiTemplate) {
/*  83 */     this.jndiTemplate = (jndiTemplate != null) ? jndiTemplate : new JndiTemplate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJndiEnvironment(Properties jndiEnvironment) {
/*  92 */     this.jndiTemplate = new JndiTemplate(jndiEnvironment);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJndiName(String jndiName) {
/*  99 */     this.jndiName = jndiName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws NamingException, RemoteException {
/* 105 */     prepare();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() throws NamingException, RemoteException {
/* 114 */     if (this.jndiName == null) {
/* 115 */       throw new IllegalArgumentException("Property 'jndiName' is required");
/*     */     }
/*     */ 
/*     */     
/* 119 */     this.exportedObject = getObjectToExport();
/* 120 */     PortableRemoteObject.exportObject(this.exportedObject);
/*     */     
/* 122 */     rebind();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rebind() throws NamingException {
/* 131 */     if (this.logger.isInfoEnabled()) {
/* 132 */       this.logger.info("Binding RMI service to JNDI location [" + this.jndiName + "]");
/*     */     }
/* 134 */     this.jndiTemplate.rebind(this.jndiName, this.exportedObject);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws NamingException, NoSuchObjectException {
/* 142 */     if (this.logger.isInfoEnabled()) {
/* 143 */       this.logger.info("Unbinding RMI service from JNDI location [" + this.jndiName + "]");
/*     */     }
/* 145 */     this.jndiTemplate.unbind(this.jndiName);
/* 146 */     PortableRemoteObject.unexportObject(this.exportedObject);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\rmi\JndiRmiServiceExporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */