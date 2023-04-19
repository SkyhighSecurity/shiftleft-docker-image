/*     */ package org.springframework.ejb.access;
/*     */ 
/*     */ import javax.naming.NamingException;
/*     */ import org.aopalliance.intercept.Interceptor;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleRemoteStatelessSessionProxyFactoryBean
/*     */   extends SimpleRemoteSlsbInvokerInterceptor
/*     */   implements FactoryBean<Object>, BeanClassLoaderAware
/*     */ {
/*     */   private Class<?> businessInterface;
/*  67 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object proxy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBusinessInterface(Class<?> businessInterface) {
/*  84 */     this.businessInterface = businessInterface;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getBusinessInterface() {
/*  91 */     return this.businessInterface;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/*  96 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws NamingException {
/* 101 */     super.afterPropertiesSet();
/* 102 */     if (this.businessInterface == null) {
/* 103 */       throw new IllegalArgumentException("businessInterface is required");
/*     */     }
/* 105 */     this.proxy = (new ProxyFactory(this.businessInterface, (Interceptor)this)).getProxy(this.beanClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getObject() {
/* 111 */     return this.proxy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 116 */     return this.businessInterface;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 121 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\ejb\access\SimpleRemoteStatelessSessionProxyFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */