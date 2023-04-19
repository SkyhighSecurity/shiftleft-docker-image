/*    */ package org.springframework.aop.framework.autoproxy;
/*    */ 
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
/*    */ public class InfrastructureAdvisorAutoProxyCreator
/*    */   extends AbstractAdvisorAutoProxyCreator
/*    */ {
/*    */   private ConfigurableListableBeanFactory beanFactory;
/*    */   
/*    */   protected void initBeanFactory(ConfigurableListableBeanFactory beanFactory) {
/* 37 */     super.initBeanFactory(beanFactory);
/* 38 */     this.beanFactory = beanFactory;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isEligibleAdvisorBean(String beanName) {
/* 43 */     return (this.beanFactory.containsBeanDefinition(beanName) && this.beanFactory
/* 44 */       .getBeanDefinition(beanName).getRole() == 2);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\autoproxy\InfrastructureAdvisorAutoProxyCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */