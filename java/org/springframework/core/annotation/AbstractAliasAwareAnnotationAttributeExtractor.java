/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ abstract class AbstractAliasAwareAnnotationAttributeExtractor<S>
/*     */   implements AnnotationAttributeExtractor<S>
/*     */ {
/*     */   private final Class<? extends Annotation> annotationType;
/*     */   private final Object annotatedElement;
/*     */   private final S source;
/*     */   private final Map<String, List<String>> attributeAliasMap;
/*     */   
/*     */   AbstractAliasAwareAnnotationAttributeExtractor(Class<? extends Annotation> annotationType, Object annotatedElement, S source) {
/*  60 */     Assert.notNull(annotationType, "annotationType must not be null");
/*  61 */     Assert.notNull(source, "source must not be null");
/*  62 */     this.annotationType = annotationType;
/*  63 */     this.annotatedElement = annotatedElement;
/*  64 */     this.source = source;
/*  65 */     this.attributeAliasMap = AnnotationUtils.getAttributeAliasMap(annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class<? extends Annotation> getAnnotationType() {
/*  71 */     return this.annotationType;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Object getAnnotatedElement() {
/*  76 */     return this.annotatedElement;
/*     */   }
/*     */ 
/*     */   
/*     */   public final S getSource() {
/*  81 */     return this.source;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Object getAttributeValue(Method attributeMethod) {
/*  86 */     String attributeName = attributeMethod.getName();
/*  87 */     Object attributeValue = getRawAttributeValue(attributeMethod);
/*     */     
/*  89 */     List<String> aliasNames = this.attributeAliasMap.get(attributeName);
/*  90 */     if (aliasNames != null) {
/*  91 */       Object defaultValue = AnnotationUtils.getDefaultValue(this.annotationType, attributeName);
/*  92 */       for (String aliasName : aliasNames) {
/*  93 */         Object aliasValue = getRawAttributeValue(aliasName);
/*     */         
/*  95 */         if (!ObjectUtils.nullSafeEquals(attributeValue, aliasValue) && 
/*  96 */           !ObjectUtils.nullSafeEquals(attributeValue, defaultValue) && 
/*  97 */           !ObjectUtils.nullSafeEquals(aliasValue, defaultValue)) {
/*  98 */           String elementName = (this.annotatedElement != null) ? this.annotatedElement.toString() : "unknown element";
/*  99 */           throw new AnnotationConfigurationException(String.format("In annotation [%s] declared on %s and synthesized from [%s], attribute '%s' and its alias '%s' are present with values of [%s] and [%s], but only one is permitted.", new Object[] { this.annotationType
/*     */ 
/*     */                   
/* 102 */                   .getName(), elementName, this.source, attributeName, aliasName, 
/* 103 */                   ObjectUtils.nullSafeToString(attributeValue), ObjectUtils.nullSafeToString(aliasValue) }));
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 108 */         if (ObjectUtils.nullSafeEquals(attributeValue, defaultValue)) {
/* 109 */           attributeValue = aliasValue;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 114 */     return attributeValue;
/*     */   }
/*     */   
/*     */   protected abstract Object getRawAttributeValue(Method paramMethod);
/*     */   
/*     */   protected abstract Object getRawAttributeValue(String paramString);
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\annotation\AbstractAliasAwareAnnotationAttributeExtractor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */