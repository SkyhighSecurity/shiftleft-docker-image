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
/*     */ public class LocalStatelessSessionProxyFactoryBean
/*     */   extends LocalSlsbInvokerInterceptor
/*     */   implements FactoryBean<Object>, BeanClassLoaderAware
/*     */ {
/*     */   private Class<?> businessInterface;
/*  57 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
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
/*     */   public void setBusinessInterface(Class<?> businessInterface) {
/*  70 */     this.businessInterface = businessInterface;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getBusinessInterface() {
/*  77 */     return this.businessInterface;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/*  82 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws NamingException {
/*  87 */     super.afterPropertiesSet();
/*  88 */     if (this.businessInterface == null) {
/*  89 */       throw new IllegalArgumentException("businessInterface is required");
/*     */     }
/*  91 */     this.proxy = (new ProxyFactory(this.businessInterface, (Interceptor)this)).getProxy(this.beanClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getObject() {
/*  97 */     return this.proxy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 102 */     return this.businessInterface;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 107 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\ejb\access\LocalStatelessSessionProxyFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */