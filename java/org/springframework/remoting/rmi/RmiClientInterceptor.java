/*     */ package org.springframework.remoting.rmi;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLStreamHandler;
/*     */ import java.rmi.Naming;
/*     */ import java.rmi.NotBoundException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.registry.LocateRegistry;
/*     */ import java.rmi.registry.Registry;
/*     */ import java.rmi.server.RMIClientSocketFactory;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.remoting.RemoteConnectFailureException;
/*     */ import org.springframework.remoting.RemoteInvocationFailureException;
/*     */ import org.springframework.remoting.RemoteLookupFailureException;
/*     */ import org.springframework.remoting.support.RemoteInvocationBasedAccessor;
/*     */ import org.springframework.remoting.support.RemoteInvocationUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RmiClientInterceptor
/*     */   extends RemoteInvocationBasedAccessor
/*     */   implements MethodInterceptor
/*     */ {
/*     */   private boolean lookupStubOnStartup = true;
/*     */   private boolean cacheStub = true;
/*     */   private boolean refreshStubOnConnectFailure = false;
/*     */   private RMIClientSocketFactory registryClientSocketFactory;
/*     */   private Remote cachedStub;
/*  83 */   private final Object stubMonitor = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLookupStubOnStartup(boolean lookupStubOnStartup) {
/*  93 */     this.lookupStubOnStartup = lookupStubOnStartup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheStub(boolean cacheStub) {
/* 104 */     this.cacheStub = cacheStub;
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
/*     */   public void setRefreshStubOnConnectFailure(boolean refreshStubOnConnectFailure) {
/* 119 */     this.refreshStubOnConnectFailure = refreshStubOnConnectFailure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRegistryClientSocketFactory(RMIClientSocketFactory registryClientSocketFactory) {
/* 128 */     this.registryClientSocketFactory = registryClientSocketFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 134 */     super.afterPropertiesSet();
/* 135 */     prepare();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() throws RemoteLookupFailureException {
/* 146 */     if (this.lookupStubOnStartup) {
/* 147 */       Remote remoteObj = lookupStub();
/* 148 */       if (this.logger.isDebugEnabled()) {
/* 149 */         if (remoteObj instanceof RmiInvocationHandler) {
/* 150 */           this.logger.debug("RMI stub [" + getServiceUrl() + "] is an RMI invoker");
/*     */         }
/* 152 */         else if (getServiceInterface() != null) {
/* 153 */           boolean isImpl = getServiceInterface().isInstance(remoteObj);
/* 154 */           this.logger.debug("Using service interface [" + getServiceInterface().getName() + "] for RMI stub [" + 
/* 155 */               getServiceUrl() + "] - " + (!isImpl ? "not " : "") + "directly implemented");
/*     */         } 
/*     */       }
/*     */       
/* 159 */       if (this.cacheStub) {
/* 160 */         this.cachedStub = remoteObj;
/*     */       }
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
/*     */   protected Remote lookupStub() throws RemoteLookupFailureException {
/*     */     try {
/* 178 */       Remote stub = null;
/* 179 */       if (this.registryClientSocketFactory != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 184 */         URL url = new URL(null, getServiceUrl(), new DummyURLStreamHandler());
/* 185 */         String protocol = url.getProtocol();
/* 186 */         if (protocol != null && !"rmi".equals(protocol)) {
/* 187 */           throw new MalformedURLException("Invalid URL scheme '" + protocol + "'");
/*     */         }
/* 189 */         String host = url.getHost();
/* 190 */         int port = url.getPort();
/* 191 */         String name = url.getPath();
/* 192 */         if (name != null && name.startsWith("/")) {
/* 193 */           name = name.substring(1);
/*     */         }
/* 195 */         Registry registry = LocateRegistry.getRegistry(host, port, this.registryClientSocketFactory);
/* 196 */         stub = registry.lookup(name);
/*     */       }
/*     */       else {
/*     */         
/* 200 */         stub = Naming.lookup(getServiceUrl());
/*     */       } 
/* 202 */       if (this.logger.isDebugEnabled()) {
/* 203 */         this.logger.debug("Located RMI stub with URL [" + getServiceUrl() + "]");
/*     */       }
/* 205 */       return stub;
/*     */     }
/* 207 */     catch (MalformedURLException ex) {
/* 208 */       throw new RemoteLookupFailureException("Service URL [" + getServiceUrl() + "] is invalid", ex);
/*     */     }
/* 210 */     catch (NotBoundException ex) {
/* 211 */       throw new RemoteLookupFailureException("Could not find RMI service [" + 
/* 212 */           getServiceUrl() + "] in RMI registry", ex);
/*     */     }
/* 214 */     catch (RemoteException ex) {
/* 215 */       throw new RemoteLookupFailureException("Lookup of RMI stub failed", ex);
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
/*     */   protected Remote getStub() throws RemoteLookupFailureException {
/* 231 */     if (!this.cacheStub || (this.lookupStubOnStartup && !this.refreshStubOnConnectFailure)) {
/* 232 */       return (this.cachedStub != null) ? this.cachedStub : lookupStub();
/*     */     }
/*     */     
/* 235 */     synchronized (this.stubMonitor) {
/* 236 */       if (this.cachedStub == null) {
/* 237 */         this.cachedStub = lookupStub();
/*     */       }
/* 239 */       return this.cachedStub;
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
/*     */   public Object invoke(MethodInvocation invocation) throws Throwable {
/* 258 */     Remote stub = getStub();
/*     */     try {
/* 260 */       return doInvoke(invocation, stub);
/*     */     }
/* 262 */     catch (RemoteConnectFailureException ex) {
/* 263 */       return handleRemoteConnectFailure(invocation, (Exception)ex);
/*     */     }
/* 265 */     catch (RemoteException ex) {
/* 266 */       if (isConnectFailure(ex)) {
/* 267 */         return handleRemoteConnectFailure(invocation, ex);
/*     */       }
/*     */       
/* 270 */       throw ex;
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
/*     */   protected boolean isConnectFailure(RemoteException ex) {
/* 283 */     return RmiClientInterceptorUtils.isConnectFailure(ex);
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
/*     */   private Object handleRemoteConnectFailure(MethodInvocation invocation, Exception ex) throws Throwable {
/* 299 */     if (this.refreshStubOnConnectFailure) {
/* 300 */       String msg = "Could not connect to RMI service [" + getServiceUrl() + "] - retrying";
/* 301 */       if (this.logger.isDebugEnabled()) {
/* 302 */         this.logger.warn(msg, ex);
/*     */       }
/* 304 */       else if (this.logger.isWarnEnabled()) {
/* 305 */         this.logger.warn(msg);
/*     */       } 
/* 307 */       return refreshAndRetry(invocation);
/*     */     } 
/*     */     
/* 310 */     throw ex;
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
/*     */   protected Object refreshAndRetry(MethodInvocation invocation) throws Throwable {
/* 323 */     Remote freshStub = null;
/* 324 */     synchronized (this.stubMonitor) {
/* 325 */       this.cachedStub = null;
/* 326 */       freshStub = lookupStub();
/* 327 */       if (this.cacheStub) {
/* 328 */         this.cachedStub = freshStub;
/*     */       }
/*     */     } 
/* 331 */     return doInvoke(invocation, freshStub);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object doInvoke(MethodInvocation invocation, Remote stub) throws Throwable {
/* 342 */     if (stub instanceof RmiInvocationHandler) {
/*     */       
/*     */       try {
/* 345 */         return doInvoke(invocation, (RmiInvocationHandler)stub);
/*     */       }
/* 347 */       catch (RemoteException ex) {
/* 348 */         throw RmiClientInterceptorUtils.convertRmiAccessException(invocation
/* 349 */             .getMethod(), ex, isConnectFailure(ex), getServiceUrl());
/*     */       }
/* 351 */       catch (InvocationTargetException ex) {
/* 352 */         Throwable exToThrow = ex.getTargetException();
/* 353 */         RemoteInvocationUtils.fillInClientStackTraceIfPossible(exToThrow);
/* 354 */         throw exToThrow;
/*     */       }
/* 356 */       catch (Throwable ex) {
/* 357 */         throw new RemoteInvocationFailureException("Invocation of method [" + invocation.getMethod() + "] failed in RMI service [" + 
/* 358 */             getServiceUrl() + "]", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 364 */       return RmiClientInterceptorUtils.invokeRemoteMethod(invocation, stub);
/*     */     }
/* 366 */     catch (InvocationTargetException ex) {
/* 367 */       Throwable targetEx = ex.getTargetException();
/* 368 */       if (targetEx instanceof RemoteException) {
/* 369 */         RemoteException rex = (RemoteException)targetEx;
/* 370 */         throw RmiClientInterceptorUtils.convertRmiAccessException(invocation
/* 371 */             .getMethod(), rex, isConnectFailure(rex), getServiceUrl());
/*     */       } 
/*     */       
/* 374 */       throw targetEx;
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
/*     */   protected Object doInvoke(MethodInvocation methodInvocation, RmiInvocationHandler invocationHandler) throws RemoteException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 395 */     if (AopUtils.isToStringMethod(methodInvocation.getMethod())) {
/* 396 */       return "RMI invoker proxy for service URL [" + getServiceUrl() + "]";
/*     */     }
/*     */     
/* 399 */     return invocationHandler.invoke(createRemoteInvocation(methodInvocation));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class DummyURLStreamHandler
/*     */     extends URLStreamHandler
/*     */   {
/*     */     private DummyURLStreamHandler() {}
/*     */ 
/*     */ 
/*     */     
/*     */     protected URLConnection openConnection(URL url) throws IOException {
/* 412 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\rmi\RmiClientInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */