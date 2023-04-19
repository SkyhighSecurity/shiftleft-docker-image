/*     */ package org.springframework.validation;
/*     */ 
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FieldError
/*     */   extends ObjectError
/*     */ {
/*     */   private final String field;
/*     */   private final Object rejectedValue;
/*     */   private final boolean bindingFailure;
/*     */   
/*     */   public FieldError(String objectName, String field, String defaultMessage) {
/*  51 */     this(objectName, field, (Object)null, false, (String[])null, (Object[])null, defaultMessage);
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
/*     */   public FieldError(String objectName, String field, Object rejectedValue, boolean bindingFailure, String[] codes, Object[] arguments, String defaultMessage) {
/*  68 */     super(objectName, codes, arguments, defaultMessage);
/*  69 */     Assert.notNull(field, "Field must not be null");
/*  70 */     this.field = field;
/*  71 */     this.rejectedValue = rejectedValue;
/*  72 */     this.bindingFailure = bindingFailure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getField() {
/*  80 */     return this.field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getRejectedValue() {
/*  87 */     return this.rejectedValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBindingFailure() {
/*  95 */     return this.bindingFailure;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 101 */     if (this == other) {
/* 102 */       return true;
/*     */     }
/* 104 */     if (!super.equals(other)) {
/* 105 */       return false;
/*     */     }
/* 107 */     FieldError otherError = (FieldError)other;
/* 108 */     return (getField().equals(otherError.getField()) && 
/* 109 */       ObjectUtils.nullSafeEquals(getRejectedValue(), otherError.getRejectedValue()) && 
/* 110 */       isBindingFailure() == otherError.isBindingFailure());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 115 */     int hashCode = super.hashCode();
/* 116 */     hashCode = 29 * hashCode + getField().hashCode();
/* 117 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(getRejectedValue());
/* 118 */     hashCode = 29 * hashCode + (isBindingFailure() ? 1 : 0);
/* 119 */     return hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 124 */     return "Field error in object '" + getObjectName() + "' on field '" + this.field + "': rejected value [" + 
/* 125 */       ObjectUtils.nullSafeToString(this.rejectedValue) + "]; " + 
/* 126 */       resolvableToString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\FieldError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */