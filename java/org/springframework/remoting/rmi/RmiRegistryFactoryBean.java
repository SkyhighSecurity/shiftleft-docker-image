/*     */ package org.springframework.remoting.rmi;
/*     */ 
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.registry.LocateRegistry;
/*     */ import java.rmi.registry.Registry;
/*     */ import java.rmi.server.RMIClientSocketFactory;
/*     */ import java.rmi.server.RMIServerSocketFactory;
/*     */ import java.rmi.server.UnicastRemoteObject;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RmiRegistryFactoryBean
/*     */   implements FactoryBean<Registry>, InitializingBean, DisposableBean
/*     */ {
/*  65 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private String host;
/*     */   
/*  69 */   private int port = 1099;
/*     */ 
/*     */   
/*     */   private RMIClientSocketFactory clientSocketFactory;
/*     */ 
/*     */   
/*     */   private RMIServerSocketFactory serverSocketFactory;
/*     */ 
/*     */   
/*     */   private Registry registry;
/*     */ 
/*     */   
/*     */   private boolean alwaysCreate = false;
/*     */ 
/*     */   
/*     */   private boolean created = false;
/*     */ 
/*     */   
/*     */   public void setHost(String host) {
/*  88 */     this.host = host;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHost() {
/*  95 */     return this.host;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPort(int port) {
/* 104 */     this.port = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPort() {
/* 111 */     return this.port;
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
/*     */   public void setClientSocketFactory(RMIClientSocketFactory clientSocketFactory) {
/* 124 */     this.clientSocketFactory = clientSocketFactory;
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
/*     */   public void setServerSocketFactory(RMIServerSocketFactory serverSocketFactory) {
/* 137 */     this.serverSocketFactory = serverSocketFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlwaysCreate(boolean alwaysCreate) {
/* 148 */     this.alwaysCreate = alwaysCreate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws Exception {
/* 155 */     if (this.clientSocketFactory instanceof RMIServerSocketFactory) {
/* 156 */       this.serverSocketFactory = (RMIServerSocketFactory)this.clientSocketFactory;
/*     */     }
/* 158 */     if ((this.clientSocketFactory != null && this.serverSocketFactory == null) || (this.clientSocketFactory == null && this.serverSocketFactory != null))
/*     */     {
/* 160 */       throw new IllegalArgumentException("Both RMIClientSocketFactory and RMIServerSocketFactory or none required");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 165 */     this.registry = getRegistry(this.host, this.port, this.clientSocketFactory, this.serverSocketFactory);
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
/*     */   protected Registry getRegistry(String registryHost, int registryPort, RMIClientSocketFactory clientSocketFactory, RMIServerSocketFactory serverSocketFactory) throws RemoteException {
/* 183 */     if (registryHost != null) {
/*     */       
/* 185 */       if (this.logger.isInfoEnabled()) {
/* 186 */         this.logger.info("Looking for RMI registry at port '" + registryPort + "' of host [" + registryHost + "]");
/*     */       }
/* 188 */       Registry reg = LocateRegistry.getRegistry(registryHost, registryPort, clientSocketFactory);
/* 189 */       testRegistry(reg);
/* 190 */       return reg;
/*     */     } 
/*     */ 
/*     */     
/* 194 */     return getRegistry(registryPort, clientSocketFactory, serverSocketFactory);
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
/*     */   protected Registry getRegistry(int registryPort, RMIClientSocketFactory clientSocketFactory, RMIServerSocketFactory serverSocketFactory) throws RemoteException {
/* 210 */     if (clientSocketFactory != null) {
/* 211 */       if (this.alwaysCreate) {
/* 212 */         this.logger.info("Creating new RMI registry");
/* 213 */         this.created = true;
/* 214 */         return LocateRegistry.createRegistry(registryPort, clientSocketFactory, serverSocketFactory);
/*     */       } 
/* 216 */       if (this.logger.isInfoEnabled()) {
/* 217 */         this.logger.info("Looking for RMI registry at port '" + registryPort + "', using custom socket factory");
/*     */       }
/* 219 */       synchronized (LocateRegistry.class) {
/*     */ 
/*     */         
/* 222 */         Registry reg = LocateRegistry.getRegistry(null, registryPort, clientSocketFactory);
/* 223 */         testRegistry(reg);
/* 224 */         return reg;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 237 */     return getRegistry(registryPort);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Registry getRegistry(int registryPort) throws RemoteException {
/* 248 */     if (this.alwaysCreate) {
/* 249 */       this.logger.info("Creating new RMI registry");
/* 250 */       this.created = true;
/* 251 */       return LocateRegistry.createRegistry(registryPort);
/*     */     } 
/* 253 */     if (this.logger.isInfoEnabled()) {
/* 254 */       this.logger.info("Looking for RMI registry at port '" + registryPort + "'");
/*     */     }
/* 256 */     synchronized (LocateRegistry.class) {
/*     */ 
/*     */       
/* 259 */       Registry reg = LocateRegistry.getRegistry(registryPort);
/* 260 */       testRegistry(reg);
/* 261 */       return reg;
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
/*     */   
/*     */   protected void testRegistry(Registry registry) throws RemoteException {
/* 282 */     registry.list();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Registry getObject() throws Exception {
/* 288 */     return this.registry;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<? extends Registry> getObjectType() {
/* 293 */     return (this.registry != null) ? (Class)this.registry.getClass() : Registry.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 298 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws RemoteException {
/* 308 */     if (this.created) {
/* 309 */       this.logger.info("Unexporting RMI registry");
/* 310 */       UnicastRemoteObject.unexportObject(this.registry, true);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\rmi\RmiRegistryFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */