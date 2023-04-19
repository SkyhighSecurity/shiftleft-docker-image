/*    */ package org.springframework.aop.config;
/*    */ 
/*    */ import org.springframework.aop.aspectj.AspectInstanceFactory;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.BeanFactoryAware;
/*    */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ public class SimpleBeanFactoryAwareAspectInstanceFactory
/*    */   implements AspectInstanceFactory, BeanFactoryAware
/*    */ {
/*    */   private String aspectBeanName;
/*    */   private BeanFactory beanFactory;
/*    */   
/*    */   public void setAspectBeanName(String aspectBeanName) {
/* 47 */     this.aspectBeanName = aspectBeanName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setBeanFactory(BeanFactory beanFactory) {
/* 52 */     this.beanFactory = beanFactory;
/* 53 */     Assert.notNull(this.aspectBeanName, "'aspectBeanName' is required");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getAspectInstance() {
/* 63 */     return this.beanFactory.getBean(this.aspectBeanName);
/*    */   }
/*    */ 
/*    */   
/*    */   public ClassLoader getAspectClassLoader() {
/* 68 */     if (this.beanFactory instanceof ConfigurableBeanFactory) {
/* 69 */       return ((ConfigurableBeanFactory)this.beanFactory).getBeanClassLoader();
/*    */     }
/*    */     
/* 72 */     return ClassUtils.getDefaultClassLoader();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getOrder() {
/* 78 */     if (this.beanFactory.isSingleton(this.aspectBeanName) && this.beanFactory
/* 79 */       .isTypeMatch(this.aspectBeanName, Ordered.class)) {
/* 80 */       return ((Ordered)this.beanFactory.getBean(this.aspectBeanName)).getOrder();
/*    */     }
/* 82 */     return Integer.MAX_VALUE;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\config\SimpleBeanFactoryAwareAspectInstanceFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */