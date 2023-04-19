/*     */ package org.springframework.validation.beanvalidation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import javax.validation.Validator;
/*     */ import javax.validation.ValidatorFactory;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.Pointcut;
/*     */ import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
/*     */ import org.springframework.aop.support.DefaultPointcutAdvisor;
/*     */ import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.validation.annotation.Validated;
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
/*     */ public class MethodValidationPostProcessor
/*     */   extends AbstractBeanFactoryAwareAdvisingPostProcessor
/*     */   implements InitializingBean
/*     */ {
/*  65 */   private Class<? extends Annotation> validatedAnnotationType = (Class)Validated.class;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Validator validator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidatedAnnotationType(Class<? extends Annotation> validatedAnnotationType) {
/*  79 */     Assert.notNull(validatedAnnotationType, "'validatedAnnotationType' must not be null");
/*  80 */     this.validatedAnnotationType = validatedAnnotationType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidator(Validator validator) {
/*  89 */     if (validator instanceof LocalValidatorFactoryBean) {
/*  90 */       this.validator = ((LocalValidatorFactoryBean)validator).getValidator();
/*     */     }
/*  92 */     else if (validator instanceof SpringValidatorAdapter) {
/*  93 */       this.validator = (Validator)validator.unwrap(Validator.class);
/*     */     } else {
/*     */       
/*  96 */       this.validator = validator;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidatorFactory(ValidatorFactory validatorFactory) {
/* 107 */     this.validator = validatorFactory.getValidator();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 113 */     AnnotationMatchingPointcut annotationMatchingPointcut = new AnnotationMatchingPointcut(this.validatedAnnotationType, true);
/* 114 */     this.advisor = (Advisor)new DefaultPointcutAdvisor((Pointcut)annotationMatchingPointcut, createMethodValidationAdvice(this.validator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Advice createMethodValidationAdvice(Validator validator) {
/* 126 */     return (validator != null) ? (Advice)new MethodValidationInterceptor(validator) : (Advice)new MethodValidationInterceptor();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\beanvalidation\MethodValidationPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */