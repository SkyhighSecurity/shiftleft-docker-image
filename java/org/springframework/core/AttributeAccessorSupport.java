/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.util.Assert;
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
/*     */ public abstract class AttributeAccessorSupport
/*     */   implements AttributeAccessor, Serializable
/*     */ {
/*  40 */   private final Map<String, Object> attributes = new LinkedHashMap<String, Object>(0);
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(String name, Object value) {
/*  45 */     Assert.notNull(name, "Name must not be null");
/*  46 */     if (value != null) {
/*  47 */       this.attributes.put(name, value);
/*     */     } else {
/*     */       
/*  50 */       removeAttribute(name);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getAttribute(String name) {
/*  56 */     Assert.notNull(name, "Name must not be null");
/*  57 */     return this.attributes.get(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object removeAttribute(String name) {
/*  62 */     Assert.notNull(name, "Name must not be null");
/*  63 */     return this.attributes.remove(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAttribute(String name) {
/*  68 */     Assert.notNull(name, "Name must not be null");
/*  69 */     return this.attributes.containsKey(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] attributeNames() {
/*  74 */     return StringUtils.toStringArray(this.attributes.keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void copyAttributesFrom(AttributeAccessor source) {
/*  83 */     Assert.notNull(source, "Source must not be null");
/*  84 */     String[] attributeNames = source.attributeNames();
/*  85 */     for (String attributeName : attributeNames) {
/*  86 */       setAttribute(attributeName, source.getAttribute(attributeName));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/*  93 */     if (this == other) {
/*  94 */       return true;
/*     */     }
/*  96 */     if (!(other instanceof AttributeAccessorSupport)) {
/*  97 */       return false;
/*     */     }
/*  99 */     AttributeAccessorSupport that = (AttributeAccessorSupport)other;
/* 100 */     return this.attributes.equals(that.attributes);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 105 */     return this.attributes.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\AttributeAccessorSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */