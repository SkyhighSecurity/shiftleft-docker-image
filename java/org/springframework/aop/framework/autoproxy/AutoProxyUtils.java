/*     */ package org.springframework.aop.framework.autoproxy;
/*     */ 
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.core.Conventions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AutoProxyUtils
/*     */ {
/*  43 */   public static final String PRESERVE_TARGET_CLASS_ATTRIBUTE = Conventions.getQualifiedAttributeName(AutoProxyUtils.class, "preserveTargetClass");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public static final String ORIGINAL_TARGET_CLASS_ATTRIBUTE = Conventions.getQualifiedAttributeName(AutoProxyUtils.class, "originalTargetClass");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean shouldProxyTargetClass(ConfigurableListableBeanFactory beanFactory, String beanName) {
/*  66 */     if (beanName != null && beanFactory.containsBeanDefinition(beanName)) {
/*  67 */       BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
/*  68 */       return Boolean.TRUE.equals(bd.getAttribute(PRESERVE_TARGET_CLASS_ATTRIBUTE));
/*     */     } 
/*  70 */     return false;
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
/*     */   public static Class<?> determineTargetClass(ConfigurableListableBeanFactory beanFactory, String beanName) {
/*  83 */     if (beanName == null) {
/*  84 */       return null;
/*     */     }
/*  86 */     if (beanFactory.containsBeanDefinition(beanName)) {
/*  87 */       BeanDefinition bd = beanFactory.getMergedBeanDefinition(beanName);
/*  88 */       Class<?> targetClass = (Class)bd.getAttribute(ORIGINAL_TARGET_CLASS_ATTRIBUTE);
/*  89 */       if (targetClass != null) {
/*  90 */         return targetClass;
/*     */       }
/*     */     } 
/*  93 */     return beanFactory.getType(beanName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void exposeTargetClass(ConfigurableListableBeanFactory beanFactory, String beanName, Class<?> targetClass) {
/* 104 */     if (beanName != null && beanFactory.containsBeanDefinition(beanName))
/* 105 */       beanFactory.getMergedBeanDefinition(beanName).setAttribute(ORIGINAL_TARGET_CLASS_ATTRIBUTE, targetClass); 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\autoproxy\AutoProxyUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */