/*     */ package org.springframework.validation.beanvalidation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Set;
/*     */ import javax.validation.ConstraintViolation;
/*     */ import javax.validation.ConstraintViolationException;
/*     */ import javax.validation.Validation;
/*     */ import javax.validation.Validator;
/*     */ import javax.validation.ValidatorFactory;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.hibernate.validator.HibernateValidator;
/*     */ import org.hibernate.validator.HibernateValidatorConfiguration;
/*     */ import org.hibernate.validator.method.MethodConstraintViolation;
/*     */ import org.hibernate.validator.method.MethodConstraintViolationException;
/*     */ import org.hibernate.validator.method.MethodValidator;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.SmartFactoryBean;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public class MethodValidationInterceptor
/*     */   implements MethodInterceptor
/*     */ {
/*     */   private static Method forExecutablesMethod;
/*     */   private static Method validateParametersMethod;
/*     */   private static Method validateReturnValueMethod;
/*     */   private volatile Validator validator;
/*     */   
/*     */   static {
/*     */     try {
/*  73 */       forExecutablesMethod = Validator.class.getMethod("forExecutables", new Class[0]);
/*  74 */       Class<?> executableValidatorClass = forExecutablesMethod.getReturnType();
/*  75 */       validateParametersMethod = executableValidatorClass.getMethod("validateParameters", new Class[] { Object.class, Method.class, Object[].class, Class[].class });
/*     */       
/*  77 */       validateReturnValueMethod = executableValidatorClass.getMethod("validateReturnValue", new Class[] { Object.class, Method.class, Object.class, Class[].class });
/*     */     
/*     */     }
/*  80 */     catch (Exception exception) {}
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
/*     */   
/*     */   public MethodValidationInterceptor() {
/*  93 */     this((forExecutablesMethod != null) ? Validation.buildDefaultValidatorFactory() : 
/*  94 */         HibernateValidatorDelegate.buildValidatorFactory());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodValidationInterceptor(ValidatorFactory validatorFactory) {
/* 102 */     this(validatorFactory.getValidator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodValidationInterceptor(Validator validator) {
/* 110 */     this.validator = validator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(MethodInvocation invocation) throws Throwable {
/* 118 */     if (isFactoryBeanMetadataMethod(invocation.getMethod())) {
/* 119 */       return invocation.proceed();
/*     */     }
/*     */     
/* 122 */     Class<?>[] groups = determineValidationGroups(invocation);
/*     */     
/* 124 */     if (forExecutablesMethod != null) {
/*     */       Object execVal;
/*     */       
/*     */       try {
/* 128 */         execVal = ReflectionUtils.invokeMethod(forExecutablesMethod, this.validator);
/*     */       }
/* 130 */       catch (AbstractMethodError err) {
/*     */         
/* 132 */         Validator nativeValidator = (Validator)this.validator.unwrap(Validator.class);
/* 133 */         execVal = ReflectionUtils.invokeMethod(forExecutablesMethod, nativeValidator);
/*     */         
/* 135 */         this.validator = nativeValidator;
/*     */       } 
/*     */       
/* 138 */       Method methodToValidate = invocation.getMethod();
/*     */ 
/*     */       
/*     */       try {
/* 142 */         result = (Set<ConstraintViolation<?>>)ReflectionUtils.invokeMethod(validateParametersMethod, execVal, new Object[] { invocation
/* 143 */               .getThis(), methodToValidate, invocation.getArguments(), groups });
/*     */       }
/* 145 */       catch (IllegalArgumentException ex) {
/*     */ 
/*     */         
/* 148 */         methodToValidate = BridgeMethodResolver.findBridgedMethod(
/* 149 */             ClassUtils.getMostSpecificMethod(invocation.getMethod(), invocation.getThis().getClass()));
/* 150 */         result = (Set<ConstraintViolation<?>>)ReflectionUtils.invokeMethod(validateParametersMethod, execVal, new Object[] { invocation
/* 151 */               .getThis(), methodToValidate, invocation.getArguments(), groups });
/*     */       } 
/* 153 */       if (!result.isEmpty()) {
/* 154 */         throw new ConstraintViolationException(result);
/*     */       }
/*     */       
/* 157 */       Object returnValue = invocation.proceed();
/* 158 */       Set<ConstraintViolation<?>> result = (Set<ConstraintViolation<?>>)ReflectionUtils.invokeMethod(validateReturnValueMethod, execVal, new Object[] { invocation
/* 159 */             .getThis(), methodToValidate, returnValue, groups });
/* 160 */       if (!result.isEmpty()) {
/* 161 */         throw new ConstraintViolationException(result);
/*     */       }
/* 163 */       return returnValue;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 168 */     return HibernateValidatorDelegate.invokeWithinValidation(invocation, this.validator, groups);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isFactoryBeanMetadataMethod(Method method) {
/* 173 */     Class<?> clazz = method.getDeclaringClass();
/* 174 */     return ((clazz == FactoryBean.class || clazz == SmartFactoryBean.class) && 
/* 175 */       !method.getName().equals("getObject"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?>[] determineValidationGroups(MethodInvocation invocation) {
/* 186 */     Validated validatedAnn = (Validated)AnnotationUtils.findAnnotation(invocation.getMethod(), Validated.class);
/* 187 */     if (validatedAnn == null) {
/* 188 */       validatedAnn = (Validated)AnnotationUtils.findAnnotation(invocation.getThis().getClass(), Validated.class);
/*     */     }
/* 190 */     return (validatedAnn != null) ? validatedAnn.value() : new Class[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class HibernateValidatorDelegate
/*     */   {
/*     */     public static ValidatorFactory buildValidatorFactory() {
/* 200 */       return ((HibernateValidatorConfiguration)Validation.byProvider(HibernateValidator.class).configure()).buildValidatorFactory();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Object invokeWithinValidation(MethodInvocation invocation, Validator validator, Class<?>[] groups) throws Throwable {
/* 208 */       MethodValidator methodValidator = (MethodValidator)validator.unwrap(MethodValidator.class);
/*     */       
/* 210 */       Set<MethodConstraintViolation<Object>> result = methodValidator.validateAllParameters(invocation
/* 211 */           .getThis(), invocation.getMethod(), invocation.getArguments(), groups);
/* 212 */       if (!result.isEmpty()) {
/* 213 */         throw new MethodConstraintViolationException(result);
/*     */       }
/* 215 */       Object returnValue = invocation.proceed();
/* 216 */       result = methodValidator.validateReturnValue(invocation
/* 217 */           .getThis(), invocation.getMethod(), returnValue, groups);
/* 218 */       if (!result.isEmpty()) {
/* 219 */         throw new MethodConstraintViolationException(result);
/*     */       }
/* 221 */       return returnValue;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\beanvalidation\MethodValidationInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */