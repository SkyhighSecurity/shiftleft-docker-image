/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*    */ import org.springframework.beans.factory.config.DependencyDescriptor;
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
/*    */ public class SimpleAutowireCandidateResolver
/*    */   implements AutowireCandidateResolver
/*    */ {
/*    */   public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
/* 34 */     return bdHolder.getBeanDefinition().isAutowireCandidate();
/*    */   }
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
/*    */   public boolean isRequired(DependencyDescriptor descriptor) {
/* 47 */     return descriptor.isRequired();
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getSuggestedValue(DependencyDescriptor descriptor) {
/* 52 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getLazyResolutionProxyIfNecessary(DependencyDescriptor descriptor, String beanName) {
/* 57 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\SimpleAutowireCandidateResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */