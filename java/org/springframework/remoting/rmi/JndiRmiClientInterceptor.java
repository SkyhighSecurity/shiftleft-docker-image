/*     */ package org.springframework.remoting.rmi;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.rmi.RemoteException;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.NamingException;
/*     */ import javax.rmi.PortableRemoteObject;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.jndi.JndiObjectLocator;
/*     */ import org.springframework.remoting.RemoteAccessException;
/*     */ import org.springframework.remoting.RemoteConnectFailureException;
/*     */ import org.springframework.remoting.RemoteInvocationFailureException;
/*     */ import org.springframework.remoting.RemoteLookupFailureException;
/*     */ import org.springframework.remoting.support.DefaultRemoteInvocationFactory;
/*     */ import org.springframework.remoting.support.RemoteInvocation;
/*     */ import org.springframework.remoting.support.RemoteInvocationFactory;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JndiRmiClientInterceptor
/*     */   extends JndiObjectLocator
/*     */   implements MethodInterceptor, InitializingBean
/*     */ {
/*     */   private Class<?> serviceInterface;
/*  83 */   private RemoteInvocationFactory remoteInvocationFactory = (RemoteInvocationFactory)new DefaultRemoteInvocationFactory();
/*     */   
/*     */   private boolean lookupStubOnStartup = true;
/*     */   
/*     */   private boolean cacheStub = true;
/*     */   
/*     */   private boolean refreshStubOnConnectFailure = false;
/*     */   
/*     */   private boolean exposeAccessContext = false;
/*     */   
/*     */   private Object cachedStub;
/*     */   
/*  95 */   private final Object stubMonitor = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServiceInterface(Class<?> serviceInterface) {
/* 105 */     if (serviceInterface != null && !serviceInterface.isInterface()) {
/* 106 */       throw new IllegalArgumentException("'serviceInterface' must be an interface");
/*     */     }
/* 108 */     this.serviceInterface = serviceInterface;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getServiceInterface() {
/* 115 */     return this.serviceInterface;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoteInvocationFactory(RemoteInvocationFactory remoteInvocationFactory) {
/* 125 */     this.remoteInvocationFactory = remoteInvocationFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RemoteInvocationFactory getRemoteInvocationFactory() {
/* 132 */     return this.remoteInvocationFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLookupStubOnStartup(boolean lookupStubOnStartup) {
/* 142 */     this.lookupStubOnStartup = lookupStubOnStartup;
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
/* 153 */     this.cacheStub = cacheStub;
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
/* 168 */     this.refreshStubOnConnectFailure = refreshStubOnConnectFailure;
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
/*     */   public void setExposeAccessContext(boolean exposeAccessContext) {
/* 180 */     this.exposeAccessContext = exposeAccessContext;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws NamingException {
/* 186 */     super.afterPropertiesSet();
/* 187 */     prepare();
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
/* 198 */     if (this.lookupStubOnStartup) {
/* 199 */       Object remoteObj = lookupStub();
/* 200 */       if (this.logger.isDebugEnabled()) {
/* 201 */         if (remoteObj instanceof RmiInvocationHandler) {
/* 202 */           this.logger.debug("JNDI RMI object [" + getJndiName() + "] is an RMI invoker");
/*     */         }
/* 204 */         else if (getServiceInterface() != null) {
/* 205 */           boolean isImpl = getServiceInterface().isInstance(remoteObj);
/* 206 */           this.logger.debug("Using service interface [" + getServiceInterface().getName() + "] for JNDI RMI object [" + 
/* 207 */               getJndiName() + "] - " + (!isImpl ? "not " : "") + "directly implemented");
/*     */         } 
/*     */       }
/*     */       
/* 211 */       if (this.cacheStub) {
/* 212 */         this.cachedStub = remoteObj;
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
/*     */   protected Object lookupStub() throws RemoteLookupFailureException {
/*     */     try {
/* 230 */       Object stub = lookup();
/* 231 */       if (getServiceInterface() != null && !(stub instanceof RmiInvocationHandler)) {
/*     */         try {
/* 233 */           stub = PortableRemoteObject.narrow(stub, getServiceInterface());
/*     */         }
/* 235 */         catch (ClassCastException ex) {
/* 236 */           throw new RemoteLookupFailureException("Could not narrow RMI stub to service interface [" + 
/* 237 */               getServiceInterface().getName() + "]", ex);
/*     */         } 
/*     */       }
/* 240 */       return stub;
/*     */     }
/* 242 */     catch (NamingException ex) {
/* 243 */       throw new RemoteLookupFailureException("JNDI lookup for RMI service [" + getJndiName() + "] failed", ex);
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
/*     */   protected Object getStub() throws NamingException, RemoteLookupFailureException {
/* 259 */     if (!this.cacheStub || (this.lookupStubOnStartup && !this.refreshStubOnConnectFailure)) {
/* 260 */       return (this.cachedStub != null) ? this.cachedStub : lookupStub();
/*     */     }
/*     */     
/* 263 */     synchronized (this.stubMonitor) {
/* 264 */       if (this.cachedStub == null) {
/* 265 */         this.cachedStub = lookupStub();
/*     */       }
/* 267 */       return this.cachedStub;
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
/*     */     Object stub;
/*     */     try {
/* 288 */       stub = getStub();
/*     */     }
/* 290 */     catch (NamingException ex) {
/* 291 */       throw new RemoteLookupFailureException("JNDI lookup for RMI service [" + getJndiName() + "] failed", ex);
/*     */     } 
/*     */     
/* 294 */     Context ctx = this.exposeAccessContext ? getJndiTemplate().getContext() : null;
/*     */     try {
/* 296 */       return doInvoke(invocation, stub);
/*     */     }
/* 298 */     catch (RemoteConnectFailureException ex) {
/* 299 */       return handleRemoteConnectFailure(invocation, (Exception)ex);
/*     */     }
/* 301 */     catch (RemoteException ex) {
/* 302 */       if (isConnectFailure(ex)) {
/* 303 */         return handleRemoteConnectFailure(invocation, ex);
/*     */       }
/*     */       
/* 306 */       throw ex;
/*     */     
/*     */     }
/* 309 */     catch (SystemException ex) {
/* 310 */       if (isConnectFailure(ex)) {
/* 311 */         return handleRemoteConnectFailure(invocation, ex);
/*     */       }
/*     */       
/* 314 */       throw ex;
/*     */     }
/*     */     finally {
/*     */       
/* 318 */       getJndiTemplate().releaseContext(ctx);
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
/*     */   protected boolean isConnectFailure(RemoteException ex) {
/* 330 */     return RmiClientInterceptorUtils.isConnectFailure(ex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isConnectFailure(SystemException ex) {
/* 341 */     return ex instanceof org.omg.CORBA.OBJECT_NOT_EXIST;
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
/*     */   private Object handleRemoteConnectFailure(MethodInvocation invocation, Exception ex) throws Throwable {
/* 354 */     if (this.refreshStubOnConnectFailure) {
/* 355 */       if (this.logger.isDebugEnabled()) {
/* 356 */         this.logger.debug("Could not connect to RMI service [" + getJndiName() + "] - retrying", ex);
/*     */       }
/* 358 */       else if (this.logger.isWarnEnabled()) {
/* 359 */         this.logger.warn("Could not connect to RMI service [" + getJndiName() + "] - retrying");
/*     */       } 
/* 361 */       return refreshAndRetry(invocation);
/*     */     } 
/*     */     
/* 364 */     throw ex;
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
/*     */     Object freshStub;
/* 378 */     synchronized (this.stubMonitor) {
/* 379 */       this.cachedStub = null;
/* 380 */       freshStub = lookupStub();
/* 381 */       if (this.cacheStub) {
/* 382 */         this.cachedStub = freshStub;
/*     */       }
/*     */     } 
/* 385 */     return doInvoke(invocation, freshStub);
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
/*     */   protected Object doInvoke(MethodInvocation invocation, Object stub) throws Throwable {
/* 397 */     if (stub instanceof RmiInvocationHandler) {
/*     */       
/*     */       try {
/* 400 */         return doInvoke(invocation, (RmiInvocationHandler)stub);
/*     */       }
/* 402 */       catch (RemoteException ex) {
/* 403 */         throw convertRmiAccessException(ex, invocation.getMethod());
/*     */       }
/* 405 */       catch (SystemException ex) {
/* 406 */         throw convertCorbaAccessException(ex, invocation.getMethod());
/*     */       }
/* 408 */       catch (InvocationTargetException ex) {
/* 409 */         throw ex.getTargetException();
/*     */       }
/* 411 */       catch (Throwable ex) {
/* 412 */         throw new RemoteInvocationFailureException("Invocation of method [" + invocation.getMethod() + "] failed in RMI service [" + 
/* 413 */             getJndiName() + "]", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 419 */       return RmiClientInterceptorUtils.invokeRemoteMethod(invocation, stub);
/*     */     }
/* 421 */     catch (InvocationTargetException ex) {
/* 422 */       Throwable targetEx = ex.getTargetException();
/* 423 */       if (targetEx instanceof RemoteException) {
/* 424 */         throw convertRmiAccessException((RemoteException)targetEx, invocation.getMethod());
/*     */       }
/* 426 */       if (targetEx instanceof SystemException) {
/* 427 */         throw convertCorbaAccessException((SystemException)targetEx, invocation.getMethod());
/*     */       }
/*     */       
/* 430 */       throw targetEx;
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
/* 451 */     if (AopUtils.isToStringMethod(methodInvocation.getMethod())) {
/* 452 */       return "RMI invoker proxy for service URL [" + getJndiName() + "]";
/*     */     }
/*     */     
/* 455 */     return invocationHandler.invoke(createRemoteInvocation(methodInvocation));
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
/*     */   protected RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation) {
/* 471 */     return getRemoteInvocationFactory().createRemoteInvocation(methodInvocation);
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
/*     */   private Exception convertRmiAccessException(RemoteException ex, Method method) {
/* 483 */     return RmiClientInterceptorUtils.convertRmiAccessException(method, ex, isConnectFailure(ex), getJndiName());
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
/*     */   private Exception convertCorbaAccessException(SystemException ex, Method method) {
/* 495 */     if (ReflectionUtils.declaresException(method, RemoteException.class))
/*     */     {
/* 497 */       return new RemoteException("Failed to access CORBA service [" + getJndiName() + "]", ex);
/*     */     }
/*     */     
/* 500 */     if (isConnectFailure(ex)) {
/* 501 */       return (Exception)new RemoteConnectFailureException("Could not connect to CORBA service [" + getJndiName() + "]", ex);
/*     */     }
/*     */     
/* 504 */     return (Exception)new RemoteAccessException("Could not access CORBA service [" + getJndiName() + "]", ex);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\rmi\JndiRmiClientInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */