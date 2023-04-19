/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import org.springframework.beans.factory.Aware;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProxyProcessorSupport
/*     */   extends ProxyConfig
/*     */   implements Ordered, BeanClassLoaderAware, AopInfrastructureBean
/*     */ {
/*  45 */   private int order = Integer.MAX_VALUE;
/*     */   
/*  47 */   private ClassLoader proxyClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean classLoaderConfigured = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOrder(int order) {
/*  59 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/*  64 */     return this.order;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyClassLoader(ClassLoader classLoader) {
/*  74 */     this.proxyClassLoader = classLoader;
/*  75 */     this.classLoaderConfigured = (classLoader != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassLoader getProxyClassLoader() {
/*  82 */     return this.proxyClassLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/*  87 */     if (!this.classLoaderConfigured) {
/*  88 */       this.proxyClassLoader = classLoader;
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
/*     */   protected void evaluateProxyInterfaces(Class<?> beanClass, ProxyFactory proxyFactory) {
/* 102 */     Class<?>[] targetInterfaces = ClassUtils.getAllInterfacesForClass(beanClass, getProxyClassLoader());
/* 103 */     boolean hasReasonableProxyInterface = false;
/* 104 */     for (Class<?> ifc : targetInterfaces) {
/* 105 */       if (!isConfigurationCallbackInterface(ifc) && !isInternalLanguageInterface(ifc) && (ifc
/* 106 */         .getMethods()).length > 0) {
/* 107 */         hasReasonableProxyInterface = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 111 */     if (hasReasonableProxyInterface) {
/*     */       
/* 113 */       for (Class<?> ifc : targetInterfaces) {
/* 114 */         proxyFactory.addInterface(ifc);
/*     */       }
/*     */     } else {
/*     */       
/* 118 */       proxyFactory.setProxyTargetClass(true);
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
/*     */   protected boolean isConfigurationCallbackInterface(Class<?> ifc) {
/* 131 */     return (InitializingBean.class == ifc || DisposableBean.class == ifc || Closeable.class == ifc || "java.lang.AutoCloseable"
/* 132 */       .equals(ifc.getName()) || 
/* 133 */       ObjectUtils.containsElement((Object[])ifc.getInterfaces(), Aware.class));
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
/*     */   protected boolean isInternalLanguageInterface(Class<?> ifc) {
/* 145 */     return (ifc.getName().equals("groovy.lang.GroovyObject") || ifc
/* 146 */       .getName().endsWith(".cglib.proxy.Factory") || ifc
/* 147 */       .getName().endsWith(".bytebuddy.MockAccess"));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\ProxyProcessorSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */