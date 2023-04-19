/*     */ package org.springframework.jmx.access;
/*     */ 
/*     */ import org.aopalliance.intercept.Interceptor;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.jmx.MBeanServerNotFoundException;
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
/*     */ 
/*     */ 
/*     */ public class MBeanProxyFactoryBean
/*     */   extends MBeanClientInterceptor
/*     */   implements FactoryBean<Object>, BeanClassLoaderAware, InitializingBean
/*     */ {
/*     */   private Class<?> proxyInterface;
/*  53 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object mbeanProxy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyInterface(Class<?> proxyInterface) {
/*  66 */     this.proxyInterface = proxyInterface;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/*  71 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws MBeanServerNotFoundException, MBeanInfoRetrievalException {
/*  80 */     super.afterPropertiesSet();
/*     */     
/*  82 */     if (this.proxyInterface == null) {
/*  83 */       this.proxyInterface = getManagementInterface();
/*  84 */       if (this.proxyInterface == null) {
/*  85 */         throw new IllegalArgumentException("Property 'proxyInterface' or 'managementInterface' is required");
/*     */       
/*     */       }
/*     */     }
/*  89 */     else if (getManagementInterface() == null) {
/*  90 */       setManagementInterface(this.proxyInterface);
/*     */     } 
/*     */     
/*  93 */     this.mbeanProxy = (new ProxyFactory(this.proxyInterface, (Interceptor)this)).getProxy(this.beanClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getObject() {
/*  99 */     return this.mbeanProxy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 104 */     return this.proxyInterface;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 109 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\access\MBeanProxyFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */