/*     */ package org.springframework.validation;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.PropertyEditorRegistry;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class BindException
/*     */   extends Exception
/*     */   implements BindingResult
/*     */ {
/*     */   private final BindingResult bindingResult;
/*     */   
/*     */   public BindException(BindingResult bindingResult) {
/*  54 */     Assert.notNull(bindingResult, "BindingResult must not be null");
/*  55 */     this.bindingResult = bindingResult;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BindException(Object target, String objectName) {
/*  65 */     Assert.notNull(target, "Target object must not be null");
/*  66 */     this.bindingResult = new BeanPropertyBindingResult(target, objectName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final BindingResult getBindingResult() {
/*  76 */     return this.bindingResult;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getObjectName() {
/*  82 */     return this.bindingResult.getObjectName();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNestedPath(String nestedPath) {
/*  87 */     this.bindingResult.setNestedPath(nestedPath);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNestedPath() {
/*  92 */     return this.bindingResult.getNestedPath();
/*     */   }
/*     */ 
/*     */   
/*     */   public void pushNestedPath(String subPath) {
/*  97 */     this.bindingResult.pushNestedPath(subPath);
/*     */   }
/*     */ 
/*     */   
/*     */   public void popNestedPath() throws IllegalStateException {
/* 102 */     this.bindingResult.popNestedPath();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void reject(String errorCode) {
/* 108 */     this.bindingResult.reject(errorCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reject(String errorCode, String defaultMessage) {
/* 113 */     this.bindingResult.reject(errorCode, defaultMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reject(String errorCode, Object[] errorArgs, String defaultMessage) {
/* 118 */     this.bindingResult.reject(errorCode, errorArgs, defaultMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rejectValue(String field, String errorCode) {
/* 123 */     this.bindingResult.rejectValue(field, errorCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rejectValue(String field, String errorCode, String defaultMessage) {
/* 128 */     this.bindingResult.rejectValue(field, errorCode, defaultMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rejectValue(String field, String errorCode, Object[] errorArgs, String defaultMessage) {
/* 133 */     this.bindingResult.rejectValue(field, errorCode, errorArgs, defaultMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAllErrors(Errors errors) {
/* 138 */     this.bindingResult.addAllErrors(errors);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasErrors() {
/* 144 */     return this.bindingResult.hasErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getErrorCount() {
/* 149 */     return this.bindingResult.getErrorCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ObjectError> getAllErrors() {
/* 154 */     return this.bindingResult.getAllErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasGlobalErrors() {
/* 159 */     return this.bindingResult.hasGlobalErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGlobalErrorCount() {
/* 164 */     return this.bindingResult.getGlobalErrorCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ObjectError> getGlobalErrors() {
/* 169 */     return this.bindingResult.getGlobalErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   public ObjectError getGlobalError() {
/* 174 */     return this.bindingResult.getGlobalError();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasFieldErrors() {
/* 179 */     return this.bindingResult.hasFieldErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldErrorCount() {
/* 184 */     return this.bindingResult.getFieldErrorCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FieldError> getFieldErrors() {
/* 189 */     return this.bindingResult.getFieldErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   public FieldError getFieldError() {
/* 194 */     return this.bindingResult.getFieldError();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasFieldErrors(String field) {
/* 199 */     return this.bindingResult.hasFieldErrors(field);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldErrorCount(String field) {
/* 204 */     return this.bindingResult.getFieldErrorCount(field);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FieldError> getFieldErrors(String field) {
/* 209 */     return this.bindingResult.getFieldErrors(field);
/*     */   }
/*     */ 
/*     */   
/*     */   public FieldError getFieldError(String field) {
/* 214 */     return this.bindingResult.getFieldError(field);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getFieldValue(String field) {
/* 219 */     return this.bindingResult.getFieldValue(field);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getFieldType(String field) {
/* 224 */     return this.bindingResult.getFieldType(field);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getTarget() {
/* 229 */     return this.bindingResult.getTarget();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> getModel() {
/* 234 */     return this.bindingResult.getModel();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getRawFieldValue(String field) {
/* 239 */     return this.bindingResult.getRawFieldValue(field);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyEditor findEditor(String field, Class<?> valueType) {
/* 245 */     return this.bindingResult.findEditor(field, valueType);
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyEditorRegistry getPropertyEditorRegistry() {
/* 250 */     return this.bindingResult.getPropertyEditorRegistry();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addError(ObjectError error) {
/* 255 */     this.bindingResult.addError(error);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] resolveMessageCodes(String errorCode) {
/* 260 */     return this.bindingResult.resolveMessageCodes(errorCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] resolveMessageCodes(String errorCode, String field) {
/* 265 */     return this.bindingResult.resolveMessageCodes(errorCode, field);
/*     */   }
/*     */ 
/*     */   
/*     */   public void recordSuppressedField(String field) {
/* 270 */     this.bindingResult.recordSuppressedField(field);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getSuppressedFields() {
/* 275 */     return this.bindingResult.getSuppressedFields();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/* 284 */     return this.bindingResult.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 289 */     return (this == other || this.bindingResult.equals(other));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 294 */     return this.bindingResult.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\BindException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */