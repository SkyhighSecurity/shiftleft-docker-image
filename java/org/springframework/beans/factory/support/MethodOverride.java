/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.beans.BeanMetadataElement;
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
/*     */ public abstract class MethodOverride
/*     */   implements BeanMetadataElement
/*     */ {
/*     */   private final String methodName;
/*     */   private boolean overloaded = true;
/*     */   private Object source;
/*     */   
/*     */   protected MethodOverride(String methodName) {
/*  51 */     Assert.notNull(methodName, "Method name must not be null");
/*  52 */     this.methodName = methodName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodName() {
/*  60 */     return this.methodName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setOverloaded(boolean overloaded) {
/*  70 */     this.overloaded = overloaded;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isOverloaded() {
/*  78 */     return this.overloaded;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSource(Object source) {
/*  86 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getSource() {
/*  91 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean matches(Method paramMethod);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 106 */     if (this == other) {
/* 107 */       return true;
/*     */     }
/* 109 */     if (!(other instanceof MethodOverride)) {
/* 110 */       return false;
/*     */     }
/* 112 */     MethodOverride that = (MethodOverride)other;
/* 113 */     return (ObjectUtils.nullSafeEquals(this.methodName, that.methodName) && 
/* 114 */       ObjectUtils.nullSafeEquals(this.source, that.source));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 119 */     int hashCode = ObjectUtils.nullSafeHashCode(this.methodName);
/* 120 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.source);
/* 121 */     return hashCode;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\MethodOverride.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */