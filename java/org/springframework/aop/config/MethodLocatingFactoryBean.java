/*    */ package org.springframework.aop.config;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.beans.BeanUtils;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.BeanFactoryAware;
/*    */ import org.springframework.beans.factory.FactoryBean;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public class MethodLocatingFactoryBean
/*    */   implements FactoryBean<Method>, BeanFactoryAware
/*    */ {
/*    */   private String targetBeanName;
/*    */   private String methodName;
/*    */   private Method method;
/*    */   
/*    */   public void setTargetBeanName(String targetBeanName) {
/* 48 */     this.targetBeanName = targetBeanName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setMethodName(String methodName) {
/* 57 */     this.methodName = methodName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setBeanFactory(BeanFactory beanFactory) {
/* 62 */     if (!StringUtils.hasText(this.targetBeanName)) {
/* 63 */       throw new IllegalArgumentException("Property 'targetBeanName' is required");
/*    */     }
/* 65 */     if (!StringUtils.hasText(this.methodName)) {
/* 66 */       throw new IllegalArgumentException("Property 'methodName' is required");
/*    */     }
/*    */     
/* 69 */     Class<?> beanClass = beanFactory.getType(this.targetBeanName);
/* 70 */     if (beanClass == null) {
/* 71 */       throw new IllegalArgumentException("Can't determine type of bean with name '" + this.targetBeanName + "'");
/*    */     }
/* 73 */     this.method = BeanUtils.resolveSignature(this.methodName, beanClass);
/*    */     
/* 75 */     if (this.method == null) {
/* 76 */       throw new IllegalArgumentException("Unable to locate method [" + this.methodName + "] on bean [" + this.targetBeanName + "]");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Method getObject() throws Exception {
/* 84 */     return this.method;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<Method> getObjectType() {
/* 89 */     return Method.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSingleton() {
/* 94 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\config\MethodLocatingFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */