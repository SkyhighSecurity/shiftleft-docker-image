/*     */ package org.springframework.core.style;
/*     */ 
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class DefaultToStringStyler
/*     */   implements ToStringStyler
/*     */ {
/*     */   private final ValueStyler valueStyler;
/*     */   
/*     */   public DefaultToStringStyler(ValueStyler valueStyler) {
/*  43 */     Assert.notNull(valueStyler, "ValueStyler must not be null");
/*  44 */     this.valueStyler = valueStyler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ValueStyler getValueStyler() {
/*  51 */     return this.valueStyler;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void styleStart(StringBuilder buffer, Object obj) {
/*  57 */     if (!obj.getClass().isArray()) {
/*  58 */       buffer.append('[').append(ClassUtils.getShortName(obj.getClass()));
/*  59 */       styleIdentityHashCode(buffer, obj);
/*     */     } else {
/*     */       
/*  62 */       buffer.append('[');
/*  63 */       styleIdentityHashCode(buffer, obj);
/*  64 */       buffer.append(' ');
/*  65 */       styleValue(buffer, obj);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void styleIdentityHashCode(StringBuilder buffer, Object obj) {
/*  70 */     buffer.append('@');
/*  71 */     buffer.append(ObjectUtils.getIdentityHexString(obj));
/*     */   }
/*     */ 
/*     */   
/*     */   public void styleEnd(StringBuilder buffer, Object o) {
/*  76 */     buffer.append(']');
/*     */   }
/*     */ 
/*     */   
/*     */   public void styleField(StringBuilder buffer, String fieldName, Object value) {
/*  81 */     styleFieldStart(buffer, fieldName);
/*  82 */     styleValue(buffer, value);
/*  83 */     styleFieldEnd(buffer, fieldName);
/*     */   }
/*     */   
/*     */   protected void styleFieldStart(StringBuilder buffer, String fieldName) {
/*  87 */     buffer.append(' ').append(fieldName).append(" = ");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void styleFieldEnd(StringBuilder buffer, String fieldName) {}
/*     */ 
/*     */   
/*     */   public void styleValue(StringBuilder buffer, Object value) {
/*  95 */     buffer.append(this.valueStyler.style(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void styleFieldSeparator(StringBuilder buffer) {
/* 100 */     buffer.append(',');
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\style\DefaultToStringStyler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */