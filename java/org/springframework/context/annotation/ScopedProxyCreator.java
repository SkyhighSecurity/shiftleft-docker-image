/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import org.springframework.aop.scope.ScopedProxyUtils;
/*    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ScopedProxyCreator
/*    */ {
/*    */   public static BeanDefinitionHolder createScopedProxy(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry, boolean proxyTargetClass) {
/* 36 */     return ScopedProxyUtils.createScopedProxy(definitionHolder, registry, proxyTargetClass);
/*    */   }
/*    */   
/*    */   public static String getTargetBeanName(String originalBeanName) {
/* 40 */     return ScopedProxyUtils.getTargetBeanName(originalBeanName);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\ScopedProxyCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */