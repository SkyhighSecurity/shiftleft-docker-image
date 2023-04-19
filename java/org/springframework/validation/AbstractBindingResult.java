/*     */ package org.springframework.validation;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.PropertyEditorRegistry;
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
/*     */ public abstract class AbstractBindingResult
/*     */   extends AbstractErrors
/*     */   implements BindingResult, Serializable
/*     */ {
/*     */   private final String objectName;
/*  49 */   private MessageCodesResolver messageCodesResolver = new DefaultMessageCodesResolver();
/*     */   
/*  51 */   private final List<ObjectError> errors = new LinkedList<ObjectError>();
/*     */   
/*  53 */   private final Set<String> suppressedFields = new HashSet<String>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractBindingResult(String objectName) {
/*  62 */     this.objectName = objectName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageCodesResolver(MessageCodesResolver messageCodesResolver) {
/*  71 */     Assert.notNull(messageCodesResolver, "MessageCodesResolver must not be null");
/*  72 */     this.messageCodesResolver = messageCodesResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageCodesResolver getMessageCodesResolver() {
/*  79 */     return this.messageCodesResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getObjectName() {
/*  89 */     return this.objectName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void reject(String errorCode, Object[] errorArgs, String defaultMessage) {
/*  95 */     addError(new ObjectError(getObjectName(), resolveMessageCodes(errorCode), errorArgs, defaultMessage));
/*     */   }
/*     */ 
/*     */   
/*     */   public void rejectValue(String field, String errorCode, Object[] errorArgs, String defaultMessage) {
/* 100 */     if ("".equals(getNestedPath()) && !StringUtils.hasLength(field)) {
/*     */ 
/*     */ 
/*     */       
/* 104 */       reject(errorCode, errorArgs, defaultMessage);
/*     */       return;
/*     */     } 
/* 107 */     String fixedField = fixedField(field);
/* 108 */     Object newVal = getActualFieldValue(fixedField);
/*     */ 
/*     */     
/* 111 */     FieldError fe = new FieldError(getObjectName(), fixedField, newVal, false, resolveMessageCodes(errorCode, field), errorArgs, defaultMessage);
/* 112 */     addError(fe);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addError(ObjectError error) {
/* 117 */     this.errors.add(error);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAllErrors(Errors errors) {
/* 122 */     if (!errors.getObjectName().equals(getObjectName())) {
/* 123 */       throw new IllegalArgumentException("Errors object needs to have same object name");
/*     */     }
/* 125 */     this.errors.addAll(errors.getAllErrors());
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] resolveMessageCodes(String errorCode) {
/* 130 */     return getMessageCodesResolver().resolveMessageCodes(errorCode, getObjectName());
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] resolveMessageCodes(String errorCode, String field) {
/* 135 */     Class<?> fieldType = getFieldType(field);
/* 136 */     return getMessageCodesResolver().resolveMessageCodes(errorCode, 
/* 137 */         getObjectName(), fixedField(field), fieldType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasErrors() {
/* 143 */     return !this.errors.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getErrorCount() {
/* 148 */     return this.errors.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ObjectError> getAllErrors() {
/* 153 */     return Collections.unmodifiableList(this.errors);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ObjectError> getGlobalErrors() {
/* 158 */     List<ObjectError> result = new LinkedList<ObjectError>();
/* 159 */     for (ObjectError objectError : this.errors) {
/* 160 */       if (!(objectError instanceof FieldError)) {
/* 161 */         result.add(objectError);
/*     */       }
/*     */     } 
/* 164 */     return Collections.unmodifiableList(result);
/*     */   }
/*     */ 
/*     */   
/*     */   public ObjectError getGlobalError() {
/* 169 */     for (ObjectError objectError : this.errors) {
/* 170 */       if (!(objectError instanceof FieldError)) {
/* 171 */         return objectError;
/*     */       }
/*     */     } 
/* 174 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FieldError> getFieldErrors() {
/* 179 */     List<FieldError> result = new LinkedList<FieldError>();
/* 180 */     for (ObjectError objectError : this.errors) {
/* 181 */       if (objectError instanceof FieldError) {
/* 182 */         result.add((FieldError)objectError);
/*     */       }
/*     */     } 
/* 185 */     return Collections.unmodifiableList(result);
/*     */   }
/*     */ 
/*     */   
/*     */   public FieldError getFieldError() {
/* 190 */     for (ObjectError objectError : this.errors) {
/* 191 */       if (objectError instanceof FieldError) {
/* 192 */         return (FieldError)objectError;
/*     */       }
/*     */     } 
/* 195 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FieldError> getFieldErrors(String field) {
/* 200 */     List<FieldError> result = new LinkedList<FieldError>();
/* 201 */     String fixedField = fixedField(field);
/* 202 */     for (ObjectError objectError : this.errors) {
/* 203 */       if (objectError instanceof FieldError && isMatchingFieldError(fixedField, (FieldError)objectError)) {
/* 204 */         result.add((FieldError)objectError);
/*     */       }
/*     */     } 
/* 207 */     return Collections.unmodifiableList(result);
/*     */   }
/*     */ 
/*     */   
/*     */   public FieldError getFieldError(String field) {
/* 212 */     String fixedField = fixedField(field);
/* 213 */     for (ObjectError objectError : this.errors) {
/* 214 */       if (objectError instanceof FieldError) {
/* 215 */         FieldError fieldError = (FieldError)objectError;
/* 216 */         if (isMatchingFieldError(fixedField, fieldError)) {
/* 217 */           return fieldError;
/*     */         }
/*     */       } 
/*     */     } 
/* 221 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getFieldValue(String field) {
/* 226 */     FieldError fieldError = getFieldError(field);
/*     */ 
/*     */     
/* 229 */     Object value = (fieldError != null) ? fieldError.getRejectedValue() : getActualFieldValue(fixedField(field));
/*     */     
/* 231 */     if (fieldError == null || !fieldError.isBindingFailure()) {
/* 232 */       value = formatFieldValue(field, value);
/*     */     }
/* 234 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getFieldType(String field) {
/* 245 */     Object value = getActualFieldValue(fixedField(field));
/* 246 */     if (value != null) {
/* 247 */       return value.getClass();
/*     */     }
/* 249 */     return null;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getModel() {
/* 273 */     Map<String, Object> model = new LinkedHashMap<String, Object>(2);
/*     */     
/* 275 */     model.put(getObjectName(), getTarget());
/*     */     
/* 277 */     model.put(MODEL_KEY_PREFIX + getObjectName(), this);
/* 278 */     return model;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getRawFieldValue(String field) {
/* 283 */     return getActualFieldValue(fixedField(field));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyEditor findEditor(String field, Class<?> valueType) {
/* 293 */     PropertyEditorRegistry editorRegistry = getPropertyEditorRegistry();
/* 294 */     if (editorRegistry != null) {
/* 295 */       Class<?> valueTypeToUse = valueType;
/* 296 */       if (valueTypeToUse == null) {
/* 297 */         valueTypeToUse = getFieldType(field);
/*     */       }
/* 299 */       return editorRegistry.findCustomEditor(valueTypeToUse, fixedField(field));
/*     */     } 
/*     */     
/* 302 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyEditorRegistry getPropertyEditorRegistry() {
/* 311 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void recordSuppressedField(String field) {
/* 322 */     this.suppressedFields.add(field);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getSuppressedFields() {
/* 333 */     return StringUtils.toStringArray(this.suppressedFields);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 339 */     if (this == other) {
/* 340 */       return true;
/*     */     }
/* 342 */     if (!(other instanceof BindingResult)) {
/* 343 */       return false;
/*     */     }
/* 345 */     BindingResult otherResult = (BindingResult)other;
/* 346 */     return (getObjectName().equals(otherResult.getObjectName()) && 
/* 347 */       ObjectUtils.nullSafeEquals(getTarget(), otherResult.getTarget()) && 
/* 348 */       getAllErrors().equals(otherResult.getAllErrors()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 353 */     return getObjectName().hashCode();
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
/*     */   public abstract Object getTarget();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Object getActualFieldValue(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object formatFieldValue(String field, Object value) {
/* 383 */     return value;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\AbstractBindingResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */