/*    */ package org.springframework.aop.framework.autoproxy;
/*    */ 
/*    */ import org.springframework.aop.framework.AbstractAdvisingBeanPostProcessor;
/*    */ import org.springframework.aop.framework.ProxyFactory;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.BeanFactoryAware;
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
/*    */ public abstract class AbstractBeanFactoryAwareAdvisingPostProcessor
/*    */   extends AbstractAdvisingBeanPostProcessor
/*    */   implements BeanFactoryAware
/*    */ {
/*    */   private ConfigurableListableBeanFactory beanFactory;
/*    */   
/*    */   public void setBeanFactory(BeanFactory beanFactory) {
/* 47 */     this.beanFactory = (beanFactory instanceof ConfigurableListableBeanFactory) ? (ConfigurableListableBeanFactory)beanFactory : null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected ProxyFactory prepareProxyFactory(Object bean, String beanName) {
/* 53 */     if (this.beanFactory != null) {
/* 54 */       AutoProxyUtils.exposeTargetClass(this.beanFactory, beanName, bean.getClass());
/*    */     }
/*    */     
/* 57 */     ProxyFactory proxyFactory = super.prepareProxyFactory(bean, beanName);
/* 58 */     if (!proxyFactory.isProxyTargetClass() && this.beanFactory != null && 
/* 59 */       AutoProxyUtils.shouldProxyTargetClass(this.beanFactory, beanName)) {
/* 60 */       proxyFactory.setProxyTargetClass(true);
/*    */     }
/* 62 */     return proxyFactory;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\autoproxy\AbstractBeanFactoryAwareAdvisingPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */