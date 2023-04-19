/*     */ package org.springframework.core.style;
/*     */ 
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
/*     */ public class ToStringCreator
/*     */ {
/*  35 */   private static final ToStringStyler DEFAULT_TO_STRING_STYLER = new DefaultToStringStyler(StylerUtils.DEFAULT_VALUE_STYLER);
/*     */ 
/*     */ 
/*     */   
/*  39 */   private final StringBuilder buffer = new StringBuilder(256);
/*     */ 
/*     */   
/*     */   private final ToStringStyler styler;
/*     */ 
/*     */   
/*     */   private final Object object;
/*     */ 
/*     */   
/*     */   private boolean styledFirstField;
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator(Object obj) {
/*  53 */     this(obj, (ToStringStyler)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator(Object obj, ValueStyler styler) {
/*  62 */     this(obj, new DefaultToStringStyler((styler != null) ? styler : StylerUtils.DEFAULT_VALUE_STYLER));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator(Object obj, ToStringStyler styler) {
/*  71 */     Assert.notNull(obj, "The object to be styled must not be null");
/*  72 */     this.object = obj;
/*  73 */     this.styler = (styler != null) ? styler : DEFAULT_TO_STRING_STYLER;
/*  74 */     this.styler.styleStart(this.buffer, this.object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator append(String fieldName, byte value) {
/*  85 */     return append(fieldName, Byte.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator append(String fieldName, short value) {
/*  95 */     return append(fieldName, Short.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator append(String fieldName, int value) {
/* 105 */     return append(fieldName, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator append(String fieldName, long value) {
/* 115 */     return append(fieldName, Long.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator append(String fieldName, float value) {
/* 125 */     return append(fieldName, Float.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator append(String fieldName, double value) {
/* 135 */     return append(fieldName, Double.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator append(String fieldName, boolean value) {
/* 145 */     return append(fieldName, Boolean.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator append(String fieldName, Object value) {
/* 155 */     printFieldSeparatorIfNecessary();
/* 156 */     this.styler.styleField(this.buffer, fieldName, value);
/* 157 */     return this;
/*     */   }
/*     */   
/*     */   private void printFieldSeparatorIfNecessary() {
/* 161 */     if (this.styledFirstField) {
/* 162 */       this.styler.styleFieldSeparator(this.buffer);
/*     */     } else {
/*     */       
/* 165 */       this.styledFirstField = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator append(Object value) {
/* 175 */     this.styler.styleValue(this.buffer, value);
/* 176 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 185 */     this.styler.styleEnd(this.buffer, this.object);
/* 186 */     return this.buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\style\ToStringCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */