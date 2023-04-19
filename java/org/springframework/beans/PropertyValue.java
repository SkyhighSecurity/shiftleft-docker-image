/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.springframework.core.AttributeAccessor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertyValue
/*     */   extends BeanMetadataAttributeAccessor
/*     */   implements Serializable
/*     */ {
/*     */   private final String name;
/*     */   private final Object value;
/*     */   private boolean optional = false;
/*     */   private boolean converted = false;
/*     */   private Object convertedValue;
/*     */   volatile Boolean conversionNecessary;
/*     */   volatile transient Object resolvedTokens;
/*     */   
/*     */   public PropertyValue(String name, Object value) {
/*  67 */     this.name = name;
/*  68 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyValue(PropertyValue original) {
/*  76 */     Assert.notNull(original, "Original must not be null");
/*  77 */     this.name = original.getName();
/*  78 */     this.value = original.getValue();
/*  79 */     this.optional = original.isOptional();
/*  80 */     this.converted = original.converted;
/*  81 */     this.convertedValue = original.convertedValue;
/*  82 */     this.conversionNecessary = original.conversionNecessary;
/*  83 */     this.resolvedTokens = original.resolvedTokens;
/*  84 */     setSource(original.getSource());
/*  85 */     copyAttributesFrom((AttributeAccessor)original);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyValue(PropertyValue original, Object newValue) {
/*  95 */     Assert.notNull(original, "Original must not be null");
/*  96 */     this.name = original.getName();
/*  97 */     this.value = newValue;
/*  98 */     this.optional = original.isOptional();
/*  99 */     this.conversionNecessary = original.conversionNecessary;
/* 100 */     this.resolvedTokens = original.resolvedTokens;
/* 101 */     setSource(original);
/* 102 */     copyAttributesFrom((AttributeAccessor)original);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 110 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getValue() {
/* 120 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyValue getOriginalPropertyValue() {
/* 129 */     PropertyValue original = this;
/* 130 */     Object source = getSource();
/* 131 */     while (source instanceof PropertyValue && source != original) {
/* 132 */       original = (PropertyValue)source;
/* 133 */       source = original.getSource();
/*     */     } 
/* 135 */     return original;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOptional(boolean optional) {
/* 144 */     this.optional = optional;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOptional() {
/* 153 */     return this.optional;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isConverted() {
/* 161 */     return this.converted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setConvertedValue(Object value) {
/* 169 */     this.converted = true;
/* 170 */     this.convertedValue = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Object getConvertedValue() {
/* 178 */     return this.convertedValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 184 */     if (this == other) {
/* 185 */       return true;
/*     */     }
/* 187 */     if (!(other instanceof PropertyValue)) {
/* 188 */       return false;
/*     */     }
/* 190 */     PropertyValue otherPv = (PropertyValue)other;
/* 191 */     return (this.name.equals(otherPv.name) && 
/* 192 */       ObjectUtils.nullSafeEquals(this.value, otherPv.value) && 
/* 193 */       ObjectUtils.nullSafeEquals(getSource(), otherPv.getSource()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 198 */     return this.name.hashCode() * 29 + ObjectUtils.nullSafeHashCode(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 203 */     return "bean property '" + this.name + "'";
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\PropertyValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */