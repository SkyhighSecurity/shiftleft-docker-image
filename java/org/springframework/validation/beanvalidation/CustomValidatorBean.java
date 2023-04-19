/*    */ package org.springframework.validation.beanvalidation;
/*    */ 
/*    */ import javax.validation.MessageInterpolator;
/*    */ import javax.validation.TraversableResolver;
/*    */ import javax.validation.Validation;
/*    */ import javax.validation.Validator;
/*    */ import javax.validation.ValidatorContext;
/*    */ import javax.validation.ValidatorFactory;
/*    */ import org.springframework.beans.factory.InitializingBean;
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
/*    */ public class CustomValidatorBean
/*    */   extends SpringValidatorAdapter
/*    */   implements Validator, InitializingBean
/*    */ {
/*    */   private ValidatorFactory validatorFactory;
/*    */   private MessageInterpolator messageInterpolator;
/*    */   private TraversableResolver traversableResolver;
/*    */   
/*    */   public void setValidatorFactory(ValidatorFactory validatorFactory) {
/* 50 */     this.validatorFactory = validatorFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setMessageInterpolator(MessageInterpolator messageInterpolator) {
/* 57 */     this.messageInterpolator = messageInterpolator;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTraversableResolver(TraversableResolver traversableResolver) {
/* 64 */     this.traversableResolver = traversableResolver;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void afterPropertiesSet() {
/* 70 */     if (this.validatorFactory == null) {
/* 71 */       this.validatorFactory = Validation.buildDefaultValidatorFactory();
/*    */     }
/*    */     
/* 74 */     ValidatorContext validatorContext = this.validatorFactory.usingContext();
/* 75 */     MessageInterpolator targetInterpolator = this.messageInterpolator;
/* 76 */     if (targetInterpolator == null) {
/* 77 */       targetInterpolator = this.validatorFactory.getMessageInterpolator();
/*    */     }
/* 79 */     validatorContext.messageInterpolator(new LocaleContextMessageInterpolator(targetInterpolator));
/* 80 */     if (this.traversableResolver != null) {
/* 81 */       validatorContext.traversableResolver(this.traversableResolver);
/*    */     }
/*    */     
/* 84 */     setTargetValidator(validatorContext.getValidator());
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\beanvalidation\CustomValidatorBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */