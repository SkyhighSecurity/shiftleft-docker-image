/*    */ package org.springframework.beans.factory.config;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import javax.inject.Provider;
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class ProviderCreatingFactoryBean
/*    */   extends AbstractFactoryBean<Provider<Object>>
/*    */ {
/*    */   private String targetBeanName;
/*    */   
/*    */   public void setTargetBeanName(String targetBeanName) {
/* 55 */     this.targetBeanName = targetBeanName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void afterPropertiesSet() throws Exception {
/* 60 */     Assert.hasText(this.targetBeanName, "Property 'targetBeanName' is required");
/* 61 */     super.afterPropertiesSet();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Class<?> getObjectType() {
/* 67 */     return Provider.class;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Provider<Object> createInstance() {
/* 72 */     return new TargetBeanProvider(getBeanFactory(), this.targetBeanName);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static class TargetBeanProvider
/*    */     implements Provider<Object>, Serializable
/*    */   {
/*    */     private final BeanFactory beanFactory;
/*    */ 
/*    */     
/*    */     private final String targetBeanName;
/*    */ 
/*    */     
/*    */     public TargetBeanProvider(BeanFactory beanFactory, String targetBeanName) {
/* 87 */       this.beanFactory = beanFactory;
/* 88 */       this.targetBeanName = targetBeanName;
/*    */     }
/*    */ 
/*    */     
/*    */     public Object get() throws BeansException {
/* 93 */       return this.beanFactory.getBean(this.targetBeanName);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\ProviderCreatingFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */