/*     */ package org.springframework.validation.beanvalidation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import javax.validation.ConstraintViolation;
/*     */ import javax.validation.ValidationException;
/*     */ import javax.validation.Validator;
/*     */ import javax.validation.metadata.BeanDescriptor;
/*     */ import javax.validation.metadata.ConstraintDescriptor;
/*     */ import org.springframework.beans.NotReadablePropertyException;
/*     */ import org.springframework.context.MessageSourceResolvable;
/*     */ import org.springframework.context.support.DefaultMessageSourceResolvable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.validation.Errors;
/*     */ import org.springframework.validation.FieldError;
/*     */ import org.springframework.validation.ObjectError;
/*     */ import org.springframework.validation.SmartValidator;
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
/*     */ public class SpringValidatorAdapter
/*     */   implements SmartValidator, Validator
/*     */ {
/*  63 */   private static final Set<String> internalAnnotationAttributes = new HashSet<String>(3);
/*     */   
/*     */   static {
/*  66 */     internalAnnotationAttributes.add("message");
/*  67 */     internalAnnotationAttributes.add("groups");
/*  68 */     internalAnnotationAttributes.add("payload");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Validator targetValidator;
/*     */ 
/*     */ 
/*     */   
/*     */   public SpringValidatorAdapter(Validator targetValidator) {
/*  79 */     Assert.notNull(targetValidator, "Target Validator must not be null");
/*  80 */     this.targetValidator = targetValidator;
/*     */   }
/*     */ 
/*     */   
/*     */   SpringValidatorAdapter() {}
/*     */   
/*     */   void setTargetValidator(Validator targetValidator) {
/*  87 */     this.targetValidator = targetValidator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supports(Class<?> clazz) {
/*  97 */     return (this.targetValidator != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void validate(Object target, Errors errors) {
/* 102 */     if (this.targetValidator != null) {
/* 103 */       processConstraintViolations(this.targetValidator.validate(target, new Class[0]), errors);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void validate(Object target, Errors errors, Object... validationHints) {
/* 109 */     if (this.targetValidator != null) {
/* 110 */       Set<Class<?>> groups = new LinkedHashSet<Class<?>>();
/* 111 */       if (validationHints != null) {
/* 112 */         for (Object hint : validationHints) {
/* 113 */           if (hint instanceof Class) {
/* 114 */             groups.add((Class)hint);
/*     */           }
/*     */         } 
/*     */       }
/* 118 */       processConstraintViolations(this.targetValidator
/* 119 */           .validate(target, ClassUtils.toClassArray(groups)), errors);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processConstraintViolations(Set<ConstraintViolation<Object>> violations, Errors errors) {
/* 130 */     for (ConstraintViolation<Object> violation : violations) {
/* 131 */       String field = determineField(violation);
/* 132 */       FieldError fieldError = errors.getFieldError(field);
/* 133 */       if (fieldError == null || !fieldError.isBindingFailure()) {
/*     */         try {
/* 135 */           ConstraintDescriptor<?> cd = violation.getConstraintDescriptor();
/* 136 */           String errorCode = determineErrorCode(cd);
/* 137 */           Object[] errorArgs = getArgumentsForConstraint(errors.getObjectName(), field, cd);
/* 138 */           if (errors instanceof BindingResult) {
/*     */ 
/*     */             
/* 141 */             BindingResult bindingResult = (BindingResult)errors;
/* 142 */             String nestedField = bindingResult.getNestedPath() + field;
/* 143 */             if ("".equals(nestedField)) {
/* 144 */               String[] arrayOfString = bindingResult.resolveMessageCodes(errorCode);
/* 145 */               bindingResult.addError(new ObjectError(errors
/* 146 */                     .getObjectName(), arrayOfString, errorArgs, violation.getMessage()));
/*     */               continue;
/*     */             } 
/* 149 */             Object rejectedValue = getRejectedValue(field, violation, bindingResult);
/* 150 */             String[] errorCodes = bindingResult.resolveMessageCodes(errorCode, field);
/* 151 */             bindingResult.addError((ObjectError)new FieldError(errors
/* 152 */                   .getObjectName(), nestedField, rejectedValue, false, errorCodes, errorArgs, violation
/* 153 */                   .getMessage()));
/*     */ 
/*     */             
/*     */             continue;
/*     */           } 
/*     */           
/* 159 */           errors.rejectValue(field, errorCode, errorArgs, violation.getMessage());
/*     */         
/*     */         }
/* 162 */         catch (NotReadablePropertyException ex) {
/* 163 */           throw new IllegalStateException("JSR-303 validated property '" + field + "' does not have a corresponding accessor for Spring data binding - check your DataBinder's configuration (bean property versus direct field access)", ex);
/*     */         } 
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
/*     */   protected String determineField(ConstraintViolation<Object> violation) {
/* 181 */     String path = violation.getPropertyPath().toString();
/* 182 */     int elementIndex = path.indexOf(".<");
/* 183 */     return (elementIndex >= 0) ? path.substring(0, elementIndex) : path;
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
/*     */   protected String determineErrorCode(ConstraintDescriptor<?> descriptor) {
/* 199 */     return descriptor.getAnnotation().annotationType().getSimpleName();
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
/*     */   protected Object[] getArgumentsForConstraint(String objectName, String field, ConstraintDescriptor<?> descriptor) {
/* 219 */     List<Object> arguments = new ArrayList();
/* 220 */     arguments.add(getResolvableField(objectName, field));
/*     */     
/* 222 */     Map<String, Object> attributesToExpose = new TreeMap<String, Object>();
/* 223 */     for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)descriptor.getAttributes().entrySet()) {
/* 224 */       String attributeName = entry.getKey();
/* 225 */       Object attributeValue = entry.getValue();
/* 226 */       if (!internalAnnotationAttributes.contains(attributeName)) {
/* 227 */         if (attributeValue instanceof String) {
/* 228 */           attributeValue = new ResolvableAttribute(attributeValue.toString());
/*     */         }
/* 230 */         attributesToExpose.put(attributeName, attributeValue);
/*     */       } 
/*     */     } 
/* 233 */     arguments.addAll(attributesToExpose.values());
/* 234 */     return arguments.toArray();
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
/*     */   protected MessageSourceResolvable getResolvableField(String objectName, String field) {
/* 249 */     String[] codes = { objectName + "." + field, field };
/* 250 */     return (MessageSourceResolvable)new DefaultMessageSourceResolvable(codes, field);
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
/*     */   protected Object getRejectedValue(String field, ConstraintViolation<Object> violation, BindingResult bindingResult) {
/* 266 */     Object invalidValue = violation.getInvalidValue();
/* 267 */     if (!"".equals(field) && !field.contains("[]") && (invalidValue == violation
/* 268 */       .getLeafBean() || field.contains("[") || field.contains(".")))
/*     */     {
/*     */       
/* 271 */       invalidValue = bindingResult.getRawFieldValue(field);
/*     */     }
/* 273 */     return invalidValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
/* 283 */     Assert.state((this.targetValidator != null), "No target Validator set");
/* 284 */     return this.targetValidator.validate(object, groups);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName, Class<?>... groups) {
/* 289 */     Assert.state((this.targetValidator != null), "No target Validator set");
/* 290 */     return this.targetValidator.validateProperty(object, propertyName, groups);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups) {
/* 297 */     Assert.state((this.targetValidator != null), "No target Validator set");
/* 298 */     return this.targetValidator.validateValue(beanType, propertyName, value, groups);
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanDescriptor getConstraintsForClass(Class<?> clazz) {
/* 303 */     Assert.state((this.targetValidator != null), "No target Validator set");
/* 304 */     return this.targetValidator.getConstraintsForClass(clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T unwrap(Class<T> type) {
/* 310 */     Assert.state((this.targetValidator != null), "No target Validator set");
/*     */     try {
/* 312 */       return (type != null) ? (T)this.targetValidator.unwrap(type) : (T)this.targetValidator;
/*     */     }
/* 314 */     catch (ValidationException ex) {
/*     */       
/* 316 */       if (Validator.class == type) {
/* 317 */         return (T)this.targetValidator;
/*     */       }
/* 319 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ResolvableAttribute
/*     */     implements MessageSourceResolvable, Serializable
/*     */   {
/*     */     private final String resolvableString;
/*     */ 
/*     */ 
/*     */     
/*     */     public ResolvableAttribute(String resolvableString) {
/* 334 */       this.resolvableString = resolvableString;
/*     */     }
/*     */ 
/*     */     
/*     */     public String[] getCodes() {
/* 339 */       return new String[] { this.resolvableString };
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] getArguments() {
/* 344 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getDefaultMessage() {
/* 349 */       return this.resolvableString;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\beanvalidation\SpringValidatorAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */