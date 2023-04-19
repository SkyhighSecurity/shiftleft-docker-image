/*     */ package org.springframework.validation.beanvalidation;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.validation.ConstraintViolation;
/*     */ import javax.validation.Validation;
/*     */ import javax.validation.Validator;
/*     */ import javax.validation.ValidatorFactory;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanValidationPostProcessor
/*     */   implements BeanPostProcessor, InitializingBean
/*     */ {
/*     */   private Validator validator;
/*     */   private boolean afterInitialization = false;
/*     */   
/*     */   public void setValidator(Validator validator) {
/*  51 */     this.validator = validator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidatorFactory(ValidatorFactory validatorFactory) {
/*  61 */     this.validator = validatorFactory.getValidator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAfterInitialization(boolean afterInitialization) {
/*  72 */     this.afterInitialization = afterInitialization;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/*  77 */     if (this.validator == null) {
/*  78 */       this.validator = Validation.buildDefaultValidatorFactory().getValidator();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
/*  85 */     if (!this.afterInitialization) {
/*  86 */       doValidate(bean);
/*     */     }
/*  88 */     return bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
/*  93 */     if (this.afterInitialization) {
/*  94 */       doValidate(bean);
/*     */     }
/*  96 */     return bean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doValidate(Object bean) {
/* 106 */     Set<ConstraintViolation<Object>> result = this.validator.validate(bean, new Class[0]);
/* 107 */     if (!result.isEmpty()) {
/* 108 */       StringBuilder sb = new StringBuilder("Bean state is invalid: ");
/* 109 */       for (Iterator<ConstraintViolation<Object>> it = result.iterator(); it.hasNext(); ) {
/* 110 */         ConstraintViolation<Object> violation = it.next();
/* 111 */         sb.append(violation.getPropertyPath()).append(" - ").append(violation.getMessage());
/* 112 */         if (it.hasNext()) {
/* 113 */           sb.append("; ");
/*     */         }
/*     */       } 
/* 116 */       throw new BeanInitializationException(sb.toString());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\beanvalidation\BeanValidationPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */