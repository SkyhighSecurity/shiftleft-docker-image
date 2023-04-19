/*     */ package org.springframework.aop.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
/*     */ import org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator;
/*     */ import org.springframework.aop.framework.autoproxy.InfrastructureAdvisorAutoProxyCreator;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AopConfigUtils
/*     */ {
/*     */   public static final String AUTO_PROXY_CREATOR_BEAN_NAME = "org.springframework.aop.config.internalAutoProxyCreator";
/*  56 */   private static final List<Class<?>> APC_PRIORITY_LIST = new ArrayList<Class<?>>(3);
/*     */ 
/*     */   
/*     */   static {
/*  60 */     APC_PRIORITY_LIST.add(InfrastructureAdvisorAutoProxyCreator.class);
/*  61 */     APC_PRIORITY_LIST.add(AspectJAwareAdvisorAutoProxyCreator.class);
/*  62 */     APC_PRIORITY_LIST.add(AnnotationAwareAspectJAutoProxyCreator.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BeanDefinition registerAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry) {
/*  67 */     return registerAutoProxyCreatorIfNecessary(registry, null);
/*     */   }
/*     */   
/*     */   public static BeanDefinition registerAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry, Object source) {
/*  71 */     return registerOrEscalateApcAsRequired(InfrastructureAdvisorAutoProxyCreator.class, registry, source);
/*     */   }
/*     */   
/*     */   public static BeanDefinition registerAspectJAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry) {
/*  75 */     return registerAspectJAutoProxyCreatorIfNecessary(registry, null);
/*     */   }
/*     */   
/*     */   public static BeanDefinition registerAspectJAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry, Object source) {
/*  79 */     return registerOrEscalateApcAsRequired(AspectJAwareAdvisorAutoProxyCreator.class, registry, source);
/*     */   }
/*     */   
/*     */   public static BeanDefinition registerAspectJAnnotationAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry) {
/*  83 */     return registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry, null);
/*     */   }
/*     */   
/*     */   public static BeanDefinition registerAspectJAnnotationAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry, Object source) {
/*  87 */     return registerOrEscalateApcAsRequired(AnnotationAwareAspectJAutoProxyCreator.class, registry, source);
/*     */   }
/*     */   
/*     */   public static void forceAutoProxyCreatorToUseClassProxying(BeanDefinitionRegistry registry) {
/*  91 */     if (registry.containsBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator")) {
/*  92 */       BeanDefinition definition = registry.getBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator");
/*  93 */       definition.getPropertyValues().add("proxyTargetClass", Boolean.TRUE);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void forceAutoProxyCreatorToExposeProxy(BeanDefinitionRegistry registry) {
/*  98 */     if (registry.containsBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator")) {
/*  99 */       BeanDefinition definition = registry.getBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator");
/* 100 */       definition.getPropertyValues().add("exposeProxy", Boolean.TRUE);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static BeanDefinition registerOrEscalateApcAsRequired(Class<?> cls, BeanDefinitionRegistry registry, Object source) {
/* 106 */     Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
/*     */     
/* 108 */     if (registry.containsBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator")) {
/* 109 */       BeanDefinition apcDefinition = registry.getBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator");
/* 110 */       if (!cls.getName().equals(apcDefinition.getBeanClassName())) {
/* 111 */         int currentPriority = findPriorityForClass(apcDefinition.getBeanClassName());
/* 112 */         int requiredPriority = findPriorityForClass(cls);
/* 113 */         if (currentPriority < requiredPriority) {
/* 114 */           apcDefinition.setBeanClassName(cls.getName());
/*     */         }
/*     */       } 
/* 117 */       return null;
/*     */     } 
/*     */     
/* 120 */     RootBeanDefinition beanDefinition = new RootBeanDefinition(cls);
/* 121 */     beanDefinition.setSource(source);
/* 122 */     beanDefinition.getPropertyValues().add("order", Integer.valueOf(-2147483648));
/* 123 */     beanDefinition.setRole(2);
/* 124 */     registry.registerBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator", (BeanDefinition)beanDefinition);
/* 125 */     return (BeanDefinition)beanDefinition;
/*     */   }
/*     */   
/*     */   private static int findPriorityForClass(Class<?> clazz) {
/* 129 */     return APC_PRIORITY_LIST.indexOf(clazz);
/*     */   }
/*     */   
/*     */   private static int findPriorityForClass(String className) {
/* 133 */     for (int i = 0; i < APC_PRIORITY_LIST.size(); i++) {
/* 134 */       Class<?> clazz = APC_PRIORITY_LIST.get(i);
/* 135 */       if (clazz.getName().equals(className)) {
/* 136 */         return i;
/*     */       }
/*     */     } 
/* 139 */     throw new IllegalArgumentException("Class name [" + className + "] is not a known auto-proxy creator class");
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\config\AopConfigUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */