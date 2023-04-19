/*    */ package org.springframework.validation.beanvalidation;
/*    */ 
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorFactory;
/*    */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
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
/*    */ public class SpringConstraintValidatorFactory
/*    */   implements ConstraintValidatorFactory
/*    */ {
/*    */   private final AutowireCapableBeanFactory beanFactory;
/*    */   
/*    */   public SpringConstraintValidatorFactory(AutowireCapableBeanFactory beanFactory) {
/* 49 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/* 50 */     this.beanFactory = beanFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
/* 56 */     return (T)this.beanFactory.createBean(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public void releaseInstance(ConstraintValidator<?, ?> instance) {
/* 61 */     this.beanFactory.destroyBean(instance);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\beanvalidation\SpringConstraintValidatorFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */