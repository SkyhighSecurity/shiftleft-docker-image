/*     */ package org.springframework.ejb.access;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.rmi.RemoteException;
/*     */ import javax.ejb.EJBObject;
/*     */ import javax.naming.NamingException;
/*     */ import javax.rmi.PortableRemoteObject;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.remoting.RemoteConnectFailureException;
/*     */ import org.springframework.remoting.RemoteLookupFailureException;
/*     */ import org.springframework.remoting.rmi.RmiClientInterceptorUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractRemoteSlsbInvokerInterceptor
/*     */   extends AbstractSlsbInvokerInterceptor
/*     */ {
/*     */   private Class<?> homeInterface;
/*     */   private boolean refreshHomeOnConnectFailure = false;
/*     */   private volatile boolean homeAsComponent = false;
/*     */   
/*     */   public void setHomeInterface(Class<?> homeInterface) {
/*  63 */     if (homeInterface != null && !homeInterface.isInterface()) {
/*  64 */       throw new IllegalArgumentException("Home interface class [" + homeInterface
/*  65 */           .getClass() + "] is not an interface");
/*     */     }
/*  67 */     this.homeInterface = homeInterface;
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
/*     */   public void setRefreshHomeOnConnectFailure(boolean refreshHomeOnConnectFailure) {
/*  82 */     this.refreshHomeOnConnectFailure = refreshHomeOnConnectFailure;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isHomeRefreshable() {
/*  87 */     return this.refreshHomeOnConnectFailure;
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
/*     */   protected Object lookup() throws NamingException {
/*  99 */     Object homeObject = super.lookup();
/* 100 */     if (this.homeInterface != null) {
/*     */       try {
/* 102 */         homeObject = PortableRemoteObject.narrow(homeObject, this.homeInterface);
/*     */       }
/* 104 */       catch (ClassCastException ex) {
/* 105 */         throw new RemoteLookupFailureException("Could not narrow EJB home stub to home interface [" + this.homeInterface
/* 106 */             .getName() + "]", ex);
/*     */       } 
/*     */     }
/* 109 */     return homeObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Method getCreateMethod(Object home) throws EjbAccessException {
/* 117 */     if (this.homeAsComponent) {
/* 118 */       return null;
/*     */     }
/* 120 */     if (!(home instanceof javax.ejb.EJBHome)) {
/*     */       
/* 122 */       this.homeAsComponent = true;
/* 123 */       return null;
/*     */     } 
/* 125 */     return super.getCreateMethod(home);
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
/*     */   public Object invokeInContext(MethodInvocation invocation) throws Throwable {
/*     */     try {
/* 140 */       return doInvoke(invocation);
/*     */     }
/* 142 */     catch (RemoteConnectFailureException ex) {
/* 143 */       return handleRemoteConnectFailure(invocation, (Exception)ex);
/*     */     }
/* 145 */     catch (RemoteException ex) {
/* 146 */       if (isConnectFailure(ex)) {
/* 147 */         return handleRemoteConnectFailure(invocation, ex);
/*     */       }
/*     */       
/* 150 */       throw ex;
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
/* 163 */     return RmiClientInterceptorUtils.isConnectFailure(ex);
/*     */   }
/*     */   
/*     */   private Object handleRemoteConnectFailure(MethodInvocation invocation, Exception ex) throws Throwable {
/* 167 */     if (this.refreshHomeOnConnectFailure) {
/* 168 */       if (this.logger.isDebugEnabled()) {
/* 169 */         this.logger.debug("Could not connect to remote EJB [" + getJndiName() + "] - retrying", ex);
/*     */       }
/* 171 */       else if (this.logger.isWarnEnabled()) {
/* 172 */         this.logger.warn("Could not connect to remote EJB [" + getJndiName() + "] - retrying");
/*     */       } 
/* 174 */       return refreshAndRetry(invocation);
/*     */     } 
/*     */     
/* 177 */     throw ex;
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
/*     */     try {
/* 191 */       refreshHome();
/*     */     }
/* 193 */     catch (NamingException ex) {
/* 194 */       throw new RemoteLookupFailureException("Failed to locate remote EJB [" + getJndiName() + "]", ex);
/*     */     } 
/* 196 */     return doInvoke(invocation);
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
/*     */   protected abstract Object doInvoke(MethodInvocation paramMethodInvocation) throws Throwable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object newSessionBeanInstance() throws NamingException, InvocationTargetException {
/* 221 */     if (this.logger.isDebugEnabled()) {
/* 222 */       this.logger.debug("Trying to create reference to remote EJB");
/*     */     }
/* 224 */     Object ejbInstance = create();
/* 225 */     if (this.logger.isDebugEnabled()) {
/* 226 */       this.logger.debug("Obtained reference to remote EJB: " + ejbInstance);
/*     */     }
/* 228 */     return ejbInstance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void removeSessionBeanInstance(EJBObject ejb) {
/* 238 */     if (ejb != null && !this.homeAsComponent)
/*     */       try {
/* 240 */         ejb.remove();
/*     */       }
/* 242 */       catch (Throwable ex) {
/* 243 */         this.logger.warn("Could not invoke 'remove' on remote EJB proxy", ex);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\ejb\access\AbstractRemoteSlsbInvokerInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */