/*     */ package org.springframework.remoting.rmi;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.rmi.RemoteException;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.springframework.remoting.RemoteAccessException;
/*     */ import org.springframework.remoting.RemoteConnectFailureException;
/*     */ import org.springframework.remoting.RemoteProxyFailureException;
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
/*     */ public abstract class RmiClientInterceptorUtils
/*     */ {
/*  53 */   private static final Log logger = LogFactory.getLog(RmiClientInterceptorUtils.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object invokeRemoteMethod(MethodInvocation invocation, Object stub) throws InvocationTargetException {
/*  67 */     Method method = invocation.getMethod();
/*     */     try {
/*  69 */       if (method.getDeclaringClass().isInstance(stub))
/*     */       {
/*  71 */         return method.invoke(stub, invocation.getArguments());
/*     */       }
/*     */ 
/*     */       
/*  75 */       Method stubMethod = stub.getClass().getMethod(method.getName(), method.getParameterTypes());
/*  76 */       return stubMethod.invoke(stub, invocation.getArguments());
/*     */     
/*     */     }
/*  79 */     catch (InvocationTargetException ex) {
/*  80 */       throw ex;
/*     */     }
/*  82 */     catch (NoSuchMethodException ex) {
/*  83 */       throw new RemoteProxyFailureException("No matching RMI stub method found for: " + method, ex);
/*     */     }
/*  85 */     catch (Throwable ex) {
/*  86 */       throw new RemoteProxyFailureException("Invocation of RMI stub method failed: " + method, ex);
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
/*     */   public static Exception convertRmiAccessException(Method method, Throwable ex, String message) {
/* 104 */     if (logger.isDebugEnabled()) {
/* 105 */       logger.debug(message, ex);
/*     */     }
/* 107 */     if (ReflectionUtils.declaresException(method, RemoteException.class)) {
/* 108 */       return new RemoteException(message, ex);
/*     */     }
/*     */     
/* 111 */     return (Exception)new RemoteAccessException(message, ex);
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
/*     */   public static Exception convertRmiAccessException(Method method, RemoteException ex, String serviceName) {
/* 125 */     return convertRmiAccessException(method, ex, isConnectFailure(ex), serviceName);
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
/*     */   public static Exception convertRmiAccessException(Method method, RemoteException ex, boolean isConnectFailure, String serviceName) {
/* 142 */     if (logger.isDebugEnabled()) {
/* 143 */       logger.debug("Remote service [" + serviceName + "] threw exception", ex);
/*     */     }
/* 145 */     if (ReflectionUtils.declaresException(method, ex.getClass())) {
/* 146 */       return ex;
/*     */     }
/*     */     
/* 149 */     if (isConnectFailure) {
/* 150 */       return (Exception)new RemoteConnectFailureException("Could not connect to remote service [" + serviceName + "]", ex);
/*     */     }
/*     */     
/* 153 */     return (Exception)new RemoteAccessException("Could not access remote service [" + serviceName + "]", ex);
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
/*     */   public static boolean isConnectFailure(RemoteException ex) {
/* 171 */     return (ex instanceof java.rmi.ConnectException || ex instanceof java.rmi.ConnectIOException || ex instanceof java.rmi.UnknownHostException || ex instanceof java.rmi.NoSuchObjectException || ex instanceof java.rmi.StubNotFoundException || ex
/*     */       
/* 173 */       .getCause() instanceof java.net.SocketException || 
/* 174 */       isCorbaConnectFailure(ex.getCause()));
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
/*     */   private static boolean isCorbaConnectFailure(Throwable ex) {
/* 187 */     return ((ex instanceof org.omg.CORBA.COMM_FAILURE || ex instanceof org.omg.CORBA.NO_RESPONSE) && ((SystemException)ex).completed == CompletionStatus.COMPLETED_NO);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\rmi\RmiClientInterceptorUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */