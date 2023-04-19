/*     */ package org.springframework.ui;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.Conventions;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModelMap
/*     */   extends LinkedHashMap<String, Object>
/*     */ {
/*     */   public ModelMap() {}
/*     */   
/*     */   public ModelMap(String attributeName, Object attributeValue) {
/*  56 */     addAttribute(attributeName, attributeValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelMap(Object attributeValue) {
/*  66 */     addAttribute(attributeValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelMap addAttribute(String attributeName, Object attributeValue) {
/*  76 */     Assert.notNull(attributeName, "Model attribute name must not be null");
/*  77 */     put(attributeName, attributeValue);
/*  78 */     return this;
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
/*     */   public ModelMap addAttribute(Object attributeValue) {
/*  91 */     Assert.notNull(attributeValue, "Model object must not be null");
/*  92 */     if (attributeValue instanceof Collection && ((Collection)attributeValue).isEmpty()) {
/*  93 */       return this;
/*     */     }
/*  95 */     return addAttribute(Conventions.getVariableName(attributeValue), attributeValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelMap addAllAttributes(Collection<?> attributeValues) {
/* 104 */     if (attributeValues != null) {
/* 105 */       for (Object attributeValue : attributeValues) {
/* 106 */         addAttribute(attributeValue);
/*     */       }
/*     */     }
/* 109 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelMap addAllAttributes(Map<String, ?> attributes) {
/* 117 */     if (attributes != null) {
/* 118 */       putAll(attributes);
/*     */     }
/* 120 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelMap mergeAttributes(Map<String, ?> attributes) {
/* 129 */     if (attributes != null) {
/* 130 */       for (Map.Entry<String, ?> entry : attributes.entrySet()) {
/* 131 */         String key = entry.getKey();
/* 132 */         if (!containsKey(key)) {
/* 133 */           put(key, entry.getValue());
/*     */         }
/*     */       } 
/*     */     }
/* 137 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAttribute(String attributeName) {
/* 146 */     return containsKey(attributeName);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\ui\ModelMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */