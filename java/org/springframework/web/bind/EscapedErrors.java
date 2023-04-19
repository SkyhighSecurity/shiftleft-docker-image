/*     */ package org.springframework.web.bind;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.validation.Errors;
/*     */ import org.springframework.validation.FieldError;
/*     */ import org.springframework.validation.ObjectError;
/*     */ import org.springframework.web.util.HtmlUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EscapedErrors
/*     */   implements Errors
/*     */ {
/*     */   private final Errors source;
/*     */   
/*     */   public EscapedErrors(Errors source) {
/*  51 */     Assert.notNull(source, "Errors source must not be null");
/*  52 */     this.source = source;
/*     */   }
/*     */   
/*     */   public Errors getSource() {
/*  56 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getObjectName() {
/*  62 */     return this.source.getObjectName();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNestedPath(String nestedPath) {
/*  67 */     this.source.setNestedPath(nestedPath);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNestedPath() {
/*  72 */     return this.source.getNestedPath();
/*     */   }
/*     */ 
/*     */   
/*     */   public void pushNestedPath(String subPath) {
/*  77 */     this.source.pushNestedPath(subPath);
/*     */   }
/*     */ 
/*     */   
/*     */   public void popNestedPath() throws IllegalStateException {
/*  82 */     this.source.popNestedPath();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void reject(String errorCode) {
/*  88 */     this.source.reject(errorCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reject(String errorCode, String defaultMessage) {
/*  93 */     this.source.reject(errorCode, defaultMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reject(String errorCode, Object[] errorArgs, String defaultMessage) {
/*  98 */     this.source.reject(errorCode, errorArgs, defaultMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rejectValue(String field, String errorCode) {
/* 103 */     this.source.rejectValue(field, errorCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rejectValue(String field, String errorCode, String defaultMessage) {
/* 108 */     this.source.rejectValue(field, errorCode, defaultMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rejectValue(String field, String errorCode, Object[] errorArgs, String defaultMessage) {
/* 113 */     this.source.rejectValue(field, errorCode, errorArgs, defaultMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAllErrors(Errors errors) {
/* 118 */     this.source.addAllErrors(errors);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasErrors() {
/* 124 */     return this.source.hasErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getErrorCount() {
/* 129 */     return this.source.getErrorCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ObjectError> getAllErrors() {
/* 134 */     return escapeObjectErrors(this.source.getAllErrors());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasGlobalErrors() {
/* 139 */     return this.source.hasGlobalErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGlobalErrorCount() {
/* 144 */     return this.source.getGlobalErrorCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ObjectError> getGlobalErrors() {
/* 149 */     return escapeObjectErrors(this.source.getGlobalErrors());
/*     */   }
/*     */ 
/*     */   
/*     */   public ObjectError getGlobalError() {
/* 154 */     return escapeObjectError(this.source.getGlobalError());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasFieldErrors() {
/* 159 */     return this.source.hasFieldErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldErrorCount() {
/* 164 */     return this.source.getFieldErrorCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FieldError> getFieldErrors() {
/* 169 */     return this.source.getFieldErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   public FieldError getFieldError() {
/* 174 */     return this.source.getFieldError();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasFieldErrors(String field) {
/* 179 */     return this.source.hasFieldErrors(field);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldErrorCount(String field) {
/* 184 */     return this.source.getFieldErrorCount(field);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FieldError> getFieldErrors(String field) {
/* 189 */     return escapeObjectErrors(this.source.getFieldErrors(field));
/*     */   }
/*     */ 
/*     */   
/*     */   public FieldError getFieldError(String field) {
/* 194 */     return escapeObjectError(this.source.getFieldError(field));
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getFieldValue(String field) {
/* 199 */     Object value = this.source.getFieldValue(field);
/* 200 */     return (value instanceof String) ? HtmlUtils.htmlEscape((String)value) : value;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getFieldType(String field) {
/* 205 */     return this.source.getFieldType(field);
/*     */   }
/*     */ 
/*     */   
/*     */   private <T extends ObjectError> T escapeObjectError(T source) {
/* 210 */     if (source == null) {
/* 211 */       return null;
/*     */     }
/* 213 */     if (source instanceof FieldError) {
/* 214 */       FieldError fieldError = (FieldError)source;
/* 215 */       Object value = fieldError.getRejectedValue();
/* 216 */       if (value instanceof String) {
/* 217 */         value = HtmlUtils.htmlEscape((String)value);
/*     */       }
/* 219 */       return (T)new FieldError(fieldError
/* 220 */           .getObjectName(), fieldError.getField(), value, fieldError
/* 221 */           .isBindingFailure(), fieldError.getCodes(), fieldError
/* 222 */           .getArguments(), HtmlUtils.htmlEscape(fieldError.getDefaultMessage()));
/*     */     } 
/*     */     
/* 225 */     return (T)new ObjectError(source
/* 226 */         .getObjectName(), source.getCodes(), source.getArguments(), 
/* 227 */         HtmlUtils.htmlEscape(source.getDefaultMessage()));
/*     */   }
/*     */ 
/*     */   
/*     */   private <T extends ObjectError> List<T> escapeObjectErrors(List<T> source) {
/* 232 */     List<T> escaped = new ArrayList<T>(source.size());
/* 233 */     for (ObjectError objectError : source) {
/* 234 */       escaped.add(escapeObjectError((T)objectError));
/*     */     }
/* 236 */     return escaped;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\bind\EscapedErrors.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */