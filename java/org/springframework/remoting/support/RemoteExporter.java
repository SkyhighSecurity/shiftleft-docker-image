/*     */ package org.springframework.remoting.support;
/*     */ 
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
/*     */ import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class RemoteExporter
/*     */   extends RemotingSupport
/*     */ {
/*     */   private Object service;
/*     */   private Class<?> serviceInterface;
/*     */   private Boolean registerTraceInterceptor;
/*     */   private Object[] interceptors;
/*     */   
/*     */   public void setService(Object service) {
/*  51 */     this.service = service;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getService() {
/*  58 */     return this.service;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServiceInterface(Class<?> serviceInterface) {
/*  66 */     if (serviceInterface != null && !serviceInterface.isInterface()) {
/*  67 */       throw new IllegalArgumentException("'serviceInterface' must be an interface");
/*     */     }
/*  69 */     this.serviceInterface = serviceInterface;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getServiceInterface() {
/*  76 */     return this.serviceInterface;
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
/*     */   public void setRegisterTraceInterceptor(boolean registerTraceInterceptor) {
/*  92 */     this.registerTraceInterceptor = Boolean.valueOf(registerTraceInterceptor);
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
/*     */   public void setInterceptors(Object[] interceptors) {
/* 104 */     this.interceptors = interceptors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkService() throws IllegalArgumentException {
/* 113 */     if (getService() == null) {
/* 114 */       throw new IllegalArgumentException("Property 'service' is required");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkServiceInterface() throws IllegalArgumentException {
/* 125 */     Class<?> serviceInterface = getServiceInterface();
/* 126 */     Object service = getService();
/* 127 */     if (serviceInterface == null) {
/* 128 */       throw new IllegalArgumentException("Property 'serviceInterface' is required");
/*     */     }
/* 130 */     if (service instanceof String) {
/* 131 */       throw new IllegalArgumentException("Service [" + service + "] is a String rather than an actual service reference: Have you accidentally specified the service bean name as value instead of as reference?");
/*     */     }
/*     */ 
/*     */     
/* 135 */     if (!serviceInterface.isInstance(service)) {
/* 136 */       throw new IllegalArgumentException("Service interface [" + serviceInterface.getName() + "] needs to be implemented by service [" + service + "] of class [" + service
/*     */           
/* 138 */           .getClass().getName() + "]");
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
/*     */   protected Object getProxyForService() {
/* 154 */     checkService();
/* 155 */     checkServiceInterface();
/*     */     
/* 157 */     ProxyFactory proxyFactory = new ProxyFactory();
/* 158 */     proxyFactory.addInterface(getServiceInterface());
/*     */     
/* 160 */     if ((this.registerTraceInterceptor != null) ? this.registerTraceInterceptor.booleanValue() : (this.interceptors == null)) {
/* 161 */       proxyFactory.addAdvice((Advice)new RemoteInvocationTraceInterceptor(getExporterName()));
/*     */     }
/* 163 */     if (this.interceptors != null) {
/* 164 */       AdvisorAdapterRegistry adapterRegistry = GlobalAdvisorAdapterRegistry.getInstance();
/* 165 */       for (Object interceptor : this.interceptors) {
/* 166 */         proxyFactory.addAdvisor(adapterRegistry.wrap(interceptor));
/*     */       }
/*     */     } 
/*     */     
/* 170 */     proxyFactory.setTarget(getService());
/* 171 */     proxyFactory.setOpaque(true);
/*     */     
/* 173 */     return proxyFactory.getProxy(getBeanClassLoader());
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
/*     */   protected String getExporterName() {
/* 186 */     return ClassUtils.getShortName(getClass());
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\support\RemoteExporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */