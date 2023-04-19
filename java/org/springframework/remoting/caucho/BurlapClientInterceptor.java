/*     */ package org.springframework.remoting.caucho;
/*     */ 
/*     */ import com.caucho.burlap.client.BurlapProxyFactory;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.UndeclaredThrowableException;
/*     */ import java.net.MalformedURLException;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.remoting.RemoteAccessException;
/*     */ import org.springframework.remoting.RemoteConnectFailureException;
/*     */ import org.springframework.remoting.RemoteLookupFailureException;
/*     */ import org.springframework.remoting.RemoteProxyFailureException;
/*     */ import org.springframework.remoting.support.UrlBasedRemoteAccessor;
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
/*     */ @Deprecated
/*     */ public class BurlapClientInterceptor
/*     */   extends UrlBasedRemoteAccessor
/*     */   implements MethodInterceptor
/*     */ {
/*  66 */   private BurlapProxyFactory proxyFactory = new BurlapProxyFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object burlapProxy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyFactory(BurlapProxyFactory proxyFactory) {
/*  78 */     this.proxyFactory = (proxyFactory != null) ? proxyFactory : new BurlapProxyFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUsername(String username) {
/*  88 */     this.proxyFactory.setUser(username);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassword(String password) {
/*  98 */     this.proxyFactory.setPassword(password);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOverloadEnabled(boolean overloadEnabled) {
/* 107 */     this.proxyFactory.setOverloadEnabled(overloadEnabled);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 113 */     super.afterPropertiesSet();
/* 114 */     prepare();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() throws RemoteLookupFailureException {
/*     */     try {
/* 123 */       this.burlapProxy = createBurlapProxy(this.proxyFactory);
/*     */     }
/* 125 */     catch (MalformedURLException ex) {
/* 126 */       throw new RemoteLookupFailureException("Service URL [" + getServiceUrl() + "] is invalid", ex);
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
/*     */   protected Object createBurlapProxy(BurlapProxyFactory proxyFactory) throws MalformedURLException {
/* 138 */     Assert.notNull(getServiceInterface(), "Property 'serviceInterface' is required");
/* 139 */     return proxyFactory.create(getServiceInterface(), getServiceUrl());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(MethodInvocation invocation) throws Throwable {
/* 145 */     if (this.burlapProxy == null) {
/* 146 */       throw new IllegalStateException("BurlapClientInterceptor is not properly initialized - invoke 'prepare' before attempting any operations");
/*     */     }
/*     */ 
/*     */     
/* 150 */     ClassLoader originalClassLoader = overrideThreadContextClassLoader();
/*     */     try {
/* 152 */       return invocation.getMethod().invoke(this.burlapProxy, invocation.getArguments());
/*     */     }
/* 154 */     catch (InvocationTargetException ex) {
/* 155 */       Throwable targetEx = ex.getTargetException();
/* 156 */       if (targetEx instanceof com.caucho.burlap.client.BurlapRuntimeException) {
/* 157 */         Throwable cause = targetEx.getCause();
/* 158 */         throw convertBurlapAccessException((cause != null) ? cause : targetEx);
/*     */       } 
/* 160 */       if (targetEx instanceof UndeclaredThrowableException) {
/* 161 */         UndeclaredThrowableException utex = (UndeclaredThrowableException)targetEx;
/* 162 */         throw convertBurlapAccessException(utex.getUndeclaredThrowable());
/*     */       } 
/*     */       
/* 165 */       throw targetEx;
/*     */     
/*     */     }
/* 168 */     catch (Throwable ex) {
/* 169 */       throw new RemoteProxyFailureException("Failed to invoke Burlap proxy for remote service [" + 
/* 170 */           getServiceUrl() + "]", ex);
/*     */     } finally {
/*     */       
/* 173 */       resetThreadContextClassLoader(originalClassLoader);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RemoteAccessException convertBurlapAccessException(Throwable ex) {
/* 184 */     if (ex instanceof java.net.ConnectException) {
/* 185 */       return (RemoteAccessException)new RemoteConnectFailureException("Cannot connect to Burlap remote service at [" + 
/* 186 */           getServiceUrl() + "]", ex);
/*     */     }
/*     */     
/* 189 */     return new RemoteAccessException("Cannot access Burlap remote service at [" + 
/* 190 */         getServiceUrl() + "]", ex);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\caucho\BurlapClientInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */