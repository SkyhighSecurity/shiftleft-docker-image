/*     */ package org.springframework.validation;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public abstract class ValidationUtils
/*     */ {
/*  41 */   private static final Log logger = LogFactory.getLog(ValidationUtils.class);
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
/*     */   public static void invokeValidator(Validator validator, Object obj, Errors errors) {
/*  55 */     invokeValidator(validator, obj, errors, (Object[])null);
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
/*     */ 
/*     */   
/*     */   public static void invokeValidator(Validator validator, Object obj, Errors errors, Object... validationHints) {
/*  70 */     Assert.notNull(validator, "Validator must not be null");
/*  71 */     Assert.notNull(errors, "Errors object must not be null");
/*  72 */     if (logger.isDebugEnabled()) {
/*  73 */       logger.debug("Invoking validator [" + validator + "]");
/*     */     }
/*  75 */     if (obj != null && !validator.supports(obj.getClass())) {
/*  76 */       throw new IllegalArgumentException("Validator [" + validator
/*  77 */           .getClass() + "] does not support [" + obj.getClass() + "]");
/*     */     }
/*  79 */     if (!ObjectUtils.isEmpty(validationHints) && validator instanceof SmartValidator) {
/*  80 */       ((SmartValidator)validator).validate(obj, errors, validationHints);
/*     */     } else {
/*     */       
/*  83 */       validator.validate(obj, errors);
/*     */     } 
/*  85 */     if (logger.isDebugEnabled()) {
/*  86 */       if (errors.hasErrors()) {
/*  87 */         logger.debug("Validator found " + errors.getErrorCount() + " errors");
/*     */       } else {
/*     */         
/*  90 */         logger.debug("Validator found no errors");
/*     */       } 
/*     */     }
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rejectIfEmpty(Errors errors, String field, String errorCode) {
/* 108 */     rejectIfEmpty(errors, field, errorCode, null, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rejectIfEmpty(Errors errors, String field, String errorCode, String defaultMessage) {
/* 125 */     rejectIfEmpty(errors, field, errorCode, null, defaultMessage);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rejectIfEmpty(Errors errors, String field, String errorCode, Object[] errorArgs) {
/* 143 */     rejectIfEmpty(errors, field, errorCode, errorArgs, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rejectIfEmpty(Errors errors, String field, String errorCode, Object[] errorArgs, String defaultMessage) {
/* 164 */     Assert.notNull(errors, "Errors object must not be null");
/* 165 */     Object value = errors.getFieldValue(field);
/* 166 */     if (value == null || !StringUtils.hasLength(value.toString())) {
/* 167 */       errors.rejectValue(field, errorCode, errorArgs, defaultMessage);
/*     */     }
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rejectIfEmptyOrWhitespace(Errors errors, String field, String errorCode) {
/* 184 */     rejectIfEmptyOrWhitespace(errors, field, errorCode, null, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rejectIfEmptyOrWhitespace(Errors errors, String field, String errorCode, String defaultMessage) {
/* 203 */     rejectIfEmptyOrWhitespace(errors, field, errorCode, null, defaultMessage);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rejectIfEmptyOrWhitespace(Errors errors, String field, String errorCode, Object[] errorArgs) {
/* 223 */     rejectIfEmptyOrWhitespace(errors, field, errorCode, errorArgs, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rejectIfEmptyOrWhitespace(Errors errors, String field, String errorCode, Object[] errorArgs, String defaultMessage) {
/* 244 */     Assert.notNull(errors, "Errors object must not be null");
/* 245 */     Object value = errors.getFieldValue(field);
/* 246 */     if (value == null || !StringUtils.hasText(value.toString()))
/* 247 */       errors.rejectValue(field, errorCode, errorArgs, defaultMessage); 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\ValidationUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */