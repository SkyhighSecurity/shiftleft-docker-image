/*    */ package org.springframework.aop.framework.autoproxy.target;
/*    */ 
/*    */ import org.springframework.aop.target.AbstractBeanFactoryBasedTargetSource;
/*    */ import org.springframework.aop.target.LazyInitTargetSource;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
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
/*    */ public class LazyInitTargetSourceCreator
/*    */   extends AbstractBeanFactoryBasedTargetSourceCreator
/*    */ {
/*    */   protected boolean isPrototypeBased() {
/* 58 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected AbstractBeanFactoryBasedTargetSource createBeanFactoryBasedTargetSource(Class<?> beanClass, String beanName) {
/* 65 */     if (getBeanFactory() instanceof ConfigurableListableBeanFactory) {
/*    */       
/* 67 */       BeanDefinition definition = ((ConfigurableListableBeanFactory)getBeanFactory()).getBeanDefinition(beanName);
/* 68 */       if (definition.isLazyInit()) {
/* 69 */         return (AbstractBeanFactoryBasedTargetSource)new LazyInitTargetSource();
/*    */       }
/*    */     } 
/* 72 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\autoproxy\target\LazyInitTargetSourceCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */