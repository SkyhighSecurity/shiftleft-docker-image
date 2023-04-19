/*     */ package org.springframework.remoting.httpinvoker;
/*     */ 
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.remoting.RemoteAccessException;
/*     */ import org.springframework.remoting.RemoteConnectFailureException;
/*     */ import org.springframework.remoting.RemoteInvocationFailureException;
/*     */ import org.springframework.remoting.support.RemoteInvocation;
/*     */ import org.springframework.remoting.support.RemoteInvocationBasedAccessor;
/*     */ import org.springframework.remoting.support.RemoteInvocationResult;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpInvokerClientInterceptor
/*     */   extends RemoteInvocationBasedAccessor
/*     */   implements MethodInterceptor, HttpInvokerClientConfiguration
/*     */ {
/*     */   private String codebaseUrl;
/*     */   private HttpInvokerRequestExecutor httpInvokerRequestExecutor;
/*     */   
/*     */   public void setCodebaseUrl(String codebaseUrl) {
/*  93 */     this.codebaseUrl = codebaseUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCodebaseUrl() {
/* 101 */     return this.codebaseUrl;
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
/*     */   public void setHttpInvokerRequestExecutor(HttpInvokerRequestExecutor httpInvokerRequestExecutor) {
/* 114 */     this.httpInvokerRequestExecutor = httpInvokerRequestExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpInvokerRequestExecutor getHttpInvokerRequestExecutor() {
/* 123 */     if (this.httpInvokerRequestExecutor == null) {
/* 124 */       SimpleHttpInvokerRequestExecutor executor = new SimpleHttpInvokerRequestExecutor();
/* 125 */       executor.setBeanClassLoader(getBeanClassLoader());
/* 126 */       this.httpInvokerRequestExecutor = executor;
/*     */     } 
/* 128 */     return this.httpInvokerRequestExecutor;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 133 */     super.afterPropertiesSet();
/*     */ 
/*     */     
/* 136 */     getHttpInvokerRequestExecutor();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object invoke(MethodInvocation methodInvocation) throws Throwable {
/*     */     RemoteInvocationResult result;
/* 142 */     if (AopUtils.isToStringMethod(methodInvocation.getMethod())) {
/* 143 */       return "HTTP invoker proxy for service URL [" + getServiceUrl() + "]";
/*     */     }
/*     */     
/* 146 */     RemoteInvocation invocation = createRemoteInvocation(methodInvocation);
/*     */ 
/*     */     
/*     */     try {
/* 150 */       result = executeRequest(invocation, methodInvocation);
/*     */     }
/* 152 */     catch (Throwable ex) {
/* 153 */       RemoteAccessException rae = convertHttpInvokerAccessException(ex);
/* 154 */       throw (rae != null) ? rae : ex;
/*     */     } 
/*     */     
/*     */     try {
/* 158 */       return recreateRemoteInvocationResult(result);
/*     */     }
/* 160 */     catch (Throwable ex) {
/* 161 */       if (result.hasInvocationTargetException()) {
/* 162 */         throw ex;
/*     */       }
/*     */       
/* 165 */       throw new RemoteInvocationFailureException("Invocation of method [" + methodInvocation.getMethod() + "] failed in HTTP invoker remote service at [" + 
/* 166 */           getServiceUrl() + "]", ex);
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
/*     */   protected RemoteInvocationResult executeRequest(RemoteInvocation invocation, MethodInvocation originalInvocation) throws Exception {
/* 184 */     return executeRequest(invocation);
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
/*     */   protected RemoteInvocationResult executeRequest(RemoteInvocation invocation) throws Exception {
/* 202 */     return getHttpInvokerRequestExecutor().executeRequest(this, invocation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RemoteAccessException convertHttpInvokerAccessException(Throwable ex) {
/* 213 */     if (ex instanceof java.net.ConnectException) {
/* 214 */       return (RemoteAccessException)new RemoteConnectFailureException("Could not connect to HTTP invoker remote service at [" + 
/* 215 */           getServiceUrl() + "]", ex);
/*     */     }
/*     */     
/* 218 */     if (ex instanceof ClassNotFoundException || ex instanceof NoClassDefFoundError || ex instanceof java.io.InvalidClassException)
/*     */     {
/* 220 */       return new RemoteAccessException("Could not deserialize result from HTTP invoker remote service [" + 
/* 221 */           getServiceUrl() + "]", ex);
/*     */     }
/*     */     
/* 224 */     if (ex instanceof Exception) {
/* 225 */       return new RemoteAccessException("Could not access HTTP invoker remote service at [" + 
/* 226 */           getServiceUrl() + "]", ex);
/*     */     }
/*     */ 
/*     */     
/* 230 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\httpinvoker\HttpInvokerClientInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */