/*     */ package org.springframework.ejb.access;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.rmi.RemoteException;
/*     */ import javax.ejb.EJBObject;
/*     */ import javax.naming.NamingException;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.beans.factory.DisposableBean;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleRemoteSlsbInvokerInterceptor
/*     */   extends AbstractRemoteSlsbInvokerInterceptor
/*     */   implements DisposableBean
/*     */ {
/*     */   private boolean cacheSessionBean = false;
/*     */   private Object beanInstance;
/*  71 */   private final Object beanInstanceMonitor = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheSessionBean(boolean cacheSessionBean) {
/*  82 */     this.cacheSessionBean = cacheSessionBean;
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
/*     */   protected Object doInvoke(MethodInvocation invocation) throws Throwable {
/*  95 */     Object ejb = null;
/*     */     try {
/*  97 */       ejb = getSessionBeanInstance();
/*  98 */       return RmiClientInterceptorUtils.invokeRemoteMethod(invocation, ejb);
/*     */     }
/* 100 */     catch (NamingException ex) {
/* 101 */       throw new RemoteLookupFailureException("Failed to locate remote EJB [" + getJndiName() + "]", ex);
/*     */     }
/* 103 */     catch (InvocationTargetException ex) {
/* 104 */       Throwable targetEx = ex.getTargetException();
/* 105 */       if (targetEx instanceof RemoteException) {
/* 106 */         RemoteException rex = (RemoteException)targetEx;
/* 107 */         throw RmiClientInterceptorUtils.convertRmiAccessException(invocation
/* 108 */             .getMethod(), rex, isConnectFailure(rex), getJndiName());
/*     */       } 
/* 110 */       if (targetEx instanceof javax.ejb.CreateException) {
/* 111 */         throw RmiClientInterceptorUtils.convertRmiAccessException(invocation
/* 112 */             .getMethod(), targetEx, "Could not create remote EJB [" + getJndiName() + "]");
/*     */       }
/* 114 */       throw targetEx;
/*     */     } finally {
/*     */       
/* 117 */       if (ejb instanceof EJBObject) {
/* 118 */         releaseSessionBeanInstance((EJBObject)ejb);
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
/*     */   protected Object getSessionBeanInstance() throws NamingException, InvocationTargetException {
/* 132 */     if (this.cacheSessionBean) {
/* 133 */       synchronized (this.beanInstanceMonitor) {
/* 134 */         if (this.beanInstance == null) {
/* 135 */           this.beanInstance = newSessionBeanInstance();
/*     */         }
/* 137 */         return this.beanInstance;
/*     */       } 
/*     */     }
/*     */     
/* 141 */     return newSessionBeanInstance();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void releaseSessionBeanInstance(EJBObject ejb) {
/* 152 */     if (!this.cacheSessionBean) {
/* 153 */       removeSessionBeanInstance(ejb);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void refreshHome() throws NamingException {
/* 162 */     super.refreshHome();
/* 163 */     if (this.cacheSessionBean) {
/* 164 */       synchronized (this.beanInstanceMonitor) {
/* 165 */         this.beanInstance = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 175 */     if (this.cacheSessionBean)
/* 176 */       synchronized (this.beanInstanceMonitor) {
/* 177 */         if (this.beanInstance instanceof EJBObject)
/* 178 */           removeSessionBeanInstance((EJBObject)this.beanInstance); 
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\ejb\access\SimpleRemoteSlsbInvokerInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */