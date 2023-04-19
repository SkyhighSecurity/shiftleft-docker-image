/*     */ package org.springframework.aop.scope;
/*     */ 
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.framework.AopInfrastructureBean;
/*     */ import org.springframework.aop.framework.ProxyConfig;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.aop.support.DelegatingIntroductionInterceptor;
/*     */ import org.springframework.aop.target.SimpleBeanTargetSource;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.FactoryBeanNotInitializedException;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
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
/*     */ public class ScopedProxyFactoryBean
/*     */   extends ProxyConfig
/*     */   implements FactoryBean<Object>, BeanFactoryAware, AopInfrastructureBean
/*     */ {
/*  57 */   private final SimpleBeanTargetSource scopedTargetSource = new SimpleBeanTargetSource();
/*     */ 
/*     */ 
/*     */   
/*     */   private String targetBeanName;
/*     */ 
/*     */ 
/*     */   
/*     */   private Object proxy;
/*     */ 
/*     */ 
/*     */   
/*     */   public ScopedProxyFactoryBean() {
/*  70 */     setProxyTargetClass(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetBeanName(String targetBeanName) {
/*  78 */     this.targetBeanName = targetBeanName;
/*  79 */     this.scopedTargetSource.setTargetBeanName(targetBeanName);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/*  84 */     if (!(beanFactory instanceof ConfigurableBeanFactory)) {
/*  85 */       throw new IllegalStateException("Not running in a ConfigurableBeanFactory: " + beanFactory);
/*     */     }
/*  87 */     ConfigurableBeanFactory cbf = (ConfigurableBeanFactory)beanFactory;
/*     */     
/*  89 */     this.scopedTargetSource.setBeanFactory(beanFactory);
/*     */     
/*  91 */     ProxyFactory pf = new ProxyFactory();
/*  92 */     pf.copyFrom(this);
/*  93 */     pf.setTargetSource((TargetSource)this.scopedTargetSource);
/*     */     
/*  95 */     Class<?> beanType = beanFactory.getType(this.targetBeanName);
/*  96 */     if (beanType == null) {
/*  97 */       throw new IllegalStateException("Cannot create scoped proxy for bean '" + this.targetBeanName + "': Target type could not be determined at the time of proxy creation.");
/*     */     }
/*     */     
/* 100 */     if (!isProxyTargetClass() || beanType.isInterface() || Modifier.isPrivate(beanType.getModifiers())) {
/* 101 */       pf.setInterfaces(ClassUtils.getAllInterfacesForClass(beanType, cbf.getBeanClassLoader()));
/*     */     }
/*     */ 
/*     */     
/* 105 */     ScopedObject scopedObject = new DefaultScopedObject(cbf, this.scopedTargetSource.getTargetBeanName());
/* 106 */     pf.addAdvice((Advice)new DelegatingIntroductionInterceptor(scopedObject));
/*     */ 
/*     */ 
/*     */     
/* 110 */     pf.addInterface(AopInfrastructureBean.class);
/*     */     
/* 112 */     this.proxy = pf.getProxy(cbf.getBeanClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getObject() {
/* 118 */     if (this.proxy == null) {
/* 119 */       throw new FactoryBeanNotInitializedException();
/*     */     }
/* 121 */     return this.proxy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 126 */     if (this.proxy != null) {
/* 127 */       return this.proxy.getClass();
/*     */     }
/* 129 */     return this.scopedTargetSource.getTargetClass();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 134 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\scope\ScopedProxyFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */