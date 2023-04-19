/*     */ package org.springframework.aop.scope;
/*     */ 
/*     */ import org.springframework.aop.framework.autoproxy.AutoProxyUtils;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ScopedProxyUtils
/*     */ {
/*     */   private static final String TARGET_NAME_PREFIX = "scopedTarget.";
/*     */   
/*     */   public static BeanDefinitionHolder createScopedProxy(BeanDefinitionHolder definition, BeanDefinitionRegistry registry, boolean proxyTargetClass) {
/*  51 */     String originalBeanName = definition.getBeanName();
/*  52 */     BeanDefinition targetDefinition = definition.getBeanDefinition();
/*  53 */     String targetBeanName = getTargetBeanName(originalBeanName);
/*     */ 
/*     */ 
/*     */     
/*  57 */     RootBeanDefinition proxyDefinition = new RootBeanDefinition(ScopedProxyFactoryBean.class);
/*  58 */     proxyDefinition.setDecoratedDefinition(new BeanDefinitionHolder(targetDefinition, targetBeanName));
/*  59 */     proxyDefinition.setOriginatingBeanDefinition(targetDefinition);
/*  60 */     proxyDefinition.setSource(definition.getSource());
/*  61 */     proxyDefinition.setRole(targetDefinition.getRole());
/*     */     
/*  63 */     proxyDefinition.getPropertyValues().add("targetBeanName", targetBeanName);
/*  64 */     if (proxyTargetClass) {
/*  65 */       targetDefinition.setAttribute(AutoProxyUtils.PRESERVE_TARGET_CLASS_ATTRIBUTE, Boolean.TRUE);
/*     */     }
/*     */     else {
/*     */       
/*  69 */       proxyDefinition.getPropertyValues().add("proxyTargetClass", Boolean.FALSE);
/*     */     } 
/*     */ 
/*     */     
/*  73 */     proxyDefinition.setAutowireCandidate(targetDefinition.isAutowireCandidate());
/*  74 */     proxyDefinition.setPrimary(targetDefinition.isPrimary());
/*  75 */     if (targetDefinition instanceof AbstractBeanDefinition) {
/*  76 */       proxyDefinition.copyQualifiersFrom((AbstractBeanDefinition)targetDefinition);
/*     */     }
/*     */ 
/*     */     
/*  80 */     targetDefinition.setAutowireCandidate(false);
/*  81 */     targetDefinition.setPrimary(false);
/*     */ 
/*     */     
/*  84 */     registry.registerBeanDefinition(targetBeanName, targetDefinition);
/*     */ 
/*     */ 
/*     */     
/*  88 */     return new BeanDefinitionHolder((BeanDefinition)proxyDefinition, originalBeanName, definition.getAliases());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getTargetBeanName(String originalBeanName) {
/*  97 */     return "scopedTarget." + originalBeanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isScopedTarget(String beanName) {
/* 106 */     return (beanName != null && beanName.startsWith("scopedTarget."));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\scope\ScopedProxyUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */