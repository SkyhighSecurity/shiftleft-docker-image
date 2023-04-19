/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import org.springframework.beans.BeanMetadataElement;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypedStringValue
/*     */   implements BeanMetadataElement
/*     */ {
/*     */   private String value;
/*     */   private volatile Object targetType;
/*     */   private Object source;
/*     */   private String specifiedTypeName;
/*     */   private volatile boolean dynamic;
/*     */   
/*     */   public TypedStringValue(String value) {
/*  55 */     setValue(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedStringValue(String value, Class<?> targetType) {
/*  65 */     setValue(value);
/*  66 */     setTargetType(targetType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedStringValue(String value, String targetTypeName) {
/*  76 */     setValue(value);
/*  77 */     setTargetTypeName(targetTypeName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(String value) {
/*  88 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  95 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetType(Class<?> targetType) {
/* 105 */     Assert.notNull(targetType, "'targetType' must not be null");
/* 106 */     this.targetType = targetType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getTargetType() {
/* 113 */     Object targetTypeValue = this.targetType;
/* 114 */     if (!(targetTypeValue instanceof Class)) {
/* 115 */       throw new IllegalStateException("Typed String value does not carry a resolved target type");
/*     */     }
/* 117 */     return (Class)targetTypeValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetTypeName(String targetTypeName) {
/* 124 */     Assert.notNull(targetTypeName, "'targetTypeName' must not be null");
/* 125 */     this.targetType = targetTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTargetTypeName() {
/* 132 */     Object targetTypeValue = this.targetType;
/* 133 */     if (targetTypeValue instanceof Class) {
/* 134 */       return ((Class)targetTypeValue).getName();
/*     */     }
/*     */     
/* 137 */     return (String)targetTypeValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasTargetType() {
/* 145 */     return this.targetType instanceof Class;
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
/*     */   public Class<?> resolveTargetType(ClassLoader classLoader) throws ClassNotFoundException {
/* 157 */     if (this.targetType == null) {
/* 158 */       return null;
/*     */     }
/* 160 */     Class<?> resolvedClass = ClassUtils.forName(getTargetTypeName(), classLoader);
/* 161 */     this.targetType = resolvedClass;
/* 162 */     return resolvedClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSource(Object source) {
/* 171 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getSource() {
/* 176 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSpecifiedTypeName(String specifiedTypeName) {
/* 183 */     this.specifiedTypeName = specifiedTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSpecifiedTypeName() {
/* 190 */     return this.specifiedTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDynamic() {
/* 198 */     this.dynamic = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDynamic() {
/* 205 */     return this.dynamic;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 211 */     if (this == other) {
/* 212 */       return true;
/*     */     }
/* 214 */     if (!(other instanceof TypedStringValue)) {
/* 215 */       return false;
/*     */     }
/* 217 */     TypedStringValue otherValue = (TypedStringValue)other;
/* 218 */     return (ObjectUtils.nullSafeEquals(this.value, otherValue.value) && 
/* 219 */       ObjectUtils.nullSafeEquals(this.targetType, otherValue.targetType));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 224 */     return ObjectUtils.nullSafeHashCode(this.value) * 29 + ObjectUtils.nullSafeHashCode(this.targetType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 229 */     return "TypedStringValue: value [" + this.value + "], target type [" + this.targetType + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\TypedStringValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */