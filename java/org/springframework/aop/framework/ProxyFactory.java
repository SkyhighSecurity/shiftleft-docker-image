/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.aopalliance.intercept.Interceptor;
/*     */ import org.springframework.aop.TargetSource;
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
/*     */ 
/*     */ 
/*     */ public class ProxyFactory
/*     */   extends ProxyCreatorSupport
/*     */ {
/*     */   public ProxyFactory() {}
/*     */   
/*     */   public ProxyFactory(Object target) {
/*  49 */     setTarget(target);
/*  50 */     setInterfaces(ClassUtils.getAllInterfaces(target));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProxyFactory(Class<?>... proxyInterfaces) {
/*  59 */     setInterfaces(proxyInterfaces);
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
/*     */   public ProxyFactory(Class<?> proxyInterface, Interceptor interceptor) {
/*  71 */     addInterface(proxyInterface);
/*  72 */     addAdvice((Advice)interceptor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProxyFactory(Class<?> proxyInterface, TargetSource targetSource) {
/*  82 */     addInterface(proxyInterface);
/*  83 */     setTargetSource(targetSource);
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
/*     */   public Object getProxy() {
/*  96 */     return createAopProxy().getProxy();
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
/*     */   public Object getProxy(ClassLoader classLoader) {
/* 109 */     return createAopProxy().getProxy(classLoader);
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
/*     */   public static <T> T getProxy(Class<T> proxyInterface, Interceptor interceptor) {
/* 125 */     return (T)(new ProxyFactory(proxyInterface, interceptor)).getProxy();
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
/*     */   public static <T> T getProxy(Class<T> proxyInterface, TargetSource targetSource) {
/* 138 */     return (T)(new ProxyFactory(proxyInterface, targetSource)).getProxy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getProxy(TargetSource targetSource) {
/* 148 */     if (targetSource.getTargetClass() == null) {
/* 149 */       throw new IllegalArgumentException("Cannot create class proxy for TargetSource with null target class");
/*     */     }
/* 151 */     ProxyFactory proxyFactory = new ProxyFactory();
/* 152 */     proxyFactory.setTargetSource(targetSource);
/* 153 */     proxyFactory.setProxyTargetClass(true);
/* 154 */     return proxyFactory.getProxy();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\ProxyFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */