/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractAdvisingBeanPostProcessor
/*     */   extends ProxyProcessorSupport
/*     */   implements BeanPostProcessor
/*     */ {
/*     */   protected Advisor advisor;
/*     */   protected boolean beforeExistingAdvisors = false;
/*  40 */   private final Map<Class<?>, Boolean> eligibleBeans = new ConcurrentHashMap<Class<?>, Boolean>(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeforeExistingAdvisors(boolean beforeExistingAdvisors) {
/*  53 */     this.beforeExistingAdvisors = beforeExistingAdvisors;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName) {
/*  59 */     return bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName) {
/*  64 */     if (bean instanceof AopInfrastructureBean)
/*     */     {
/*  66 */       return bean;
/*     */     }
/*     */     
/*  69 */     if (bean instanceof Advised) {
/*  70 */       Advised advised = (Advised)bean;
/*  71 */       if (!advised.isFrozen() && isEligible(AopUtils.getTargetClass(bean))) {
/*     */         
/*  73 */         if (this.beforeExistingAdvisors) {
/*  74 */           advised.addAdvisor(0, this.advisor);
/*     */         } else {
/*     */           
/*  77 */           advised.addAdvisor(this.advisor);
/*     */         } 
/*  79 */         return bean;
/*     */       } 
/*     */     } 
/*     */     
/*  83 */     if (isEligible(bean, beanName)) {
/*  84 */       ProxyFactory proxyFactory = prepareProxyFactory(bean, beanName);
/*  85 */       if (!proxyFactory.isProxyTargetClass()) {
/*  86 */         evaluateProxyInterfaces(bean.getClass(), proxyFactory);
/*     */       }
/*  88 */       proxyFactory.addAdvisor(this.advisor);
/*  89 */       customizeProxyFactory(proxyFactory);
/*  90 */       return proxyFactory.getProxy(getProxyClassLoader());
/*     */     } 
/*     */ 
/*     */     
/*  94 */     return bean;
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
/*     */   
/*     */   protected boolean isEligible(Object bean, String beanName) {
/* 113 */     return isEligible(bean.getClass());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isEligible(Class<?> targetClass) {
/* 124 */     Boolean eligible = this.eligibleBeans.get(targetClass);
/* 125 */     if (eligible != null) {
/* 126 */       return eligible.booleanValue();
/*     */     }
/* 128 */     eligible = Boolean.valueOf(AopUtils.canApply(this.advisor, targetClass));
/* 129 */     this.eligibleBeans.put(targetClass, eligible);
/* 130 */     return eligible.booleanValue();
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
/*     */   protected ProxyFactory prepareProxyFactory(Object bean, String beanName) {
/* 148 */     ProxyFactory proxyFactory = new ProxyFactory();
/* 149 */     proxyFactory.copyFrom(this);
/* 150 */     proxyFactory.setTarget(bean);
/* 151 */     return proxyFactory;
/*     */   }
/*     */   
/*     */   protected void customizeProxyFactory(ProxyFactory proxyFactory) {}
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\AbstractAdvisingBeanPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */