/*     */ package org.springframework.beans.propertyeditors;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import java.text.NumberFormat;
/*     */ import org.springframework.util.NumberUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CustomNumberEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*     */   private final Class<? extends Number> numberClass;
/*     */   private final NumberFormat numberFormat;
/*     */   private final boolean allowEmpty;
/*     */   
/*     */   public CustomNumberEditor(Class<? extends Number> numberClass, boolean allowEmpty) throws IllegalArgumentException {
/*  69 */     this(numberClass, null, allowEmpty);
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
/*     */   public CustomNumberEditor(Class<? extends Number> numberClass, NumberFormat numberFormat, boolean allowEmpty) throws IllegalArgumentException {
/*  89 */     if (numberClass == null || !Number.class.isAssignableFrom(numberClass)) {
/*  90 */       throw new IllegalArgumentException("Property class must be a subclass of Number");
/*     */     }
/*  92 */     this.numberClass = numberClass;
/*  93 */     this.numberFormat = numberFormat;
/*  94 */     this.allowEmpty = allowEmpty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsText(String text) throws IllegalArgumentException {
/* 103 */     if (this.allowEmpty && !StringUtils.hasText(text)) {
/*     */       
/* 105 */       setValue(null);
/*     */     }
/* 107 */     else if (this.numberFormat != null) {
/*     */       
/* 109 */       setValue(NumberUtils.parseNumber(text, this.numberClass, this.numberFormat));
/*     */     }
/*     */     else {
/*     */       
/* 113 */       setValue(NumberUtils.parseNumber(text, this.numberClass));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(Object value) {
/* 122 */     if (value instanceof Number) {
/* 123 */       super.setValue(NumberUtils.convertNumberToTargetClass((Number)value, this.numberClass));
/*     */     } else {
/*     */       
/* 126 */       super.setValue(value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAsText() {
/* 135 */     Object value = getValue();
/* 136 */     if (value == null) {
/* 137 */       return "";
/*     */     }
/* 139 */     if (this.numberFormat != null)
/*     */     {
/* 141 */       return this.numberFormat.format(value);
/*     */     }
/*     */ 
/*     */     
/* 145 */     return value.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\propertyeditors\CustomNumberEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */