/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.beans.Introspector;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class AnnotationBeanNameGenerator
/*     */   implements BeanNameGenerator
/*     */ {
/*     */   private static final String COMPONENT_ANNOTATION_CLASSNAME = "org.springframework.stereotype.Component";
/*     */   
/*     */   public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
/*  69 */     if (definition instanceof AnnotatedBeanDefinition) {
/*  70 */       String beanName = determineBeanNameFromAnnotation((AnnotatedBeanDefinition)definition);
/*  71 */       if (StringUtils.hasText(beanName))
/*     */       {
/*  73 */         return beanName;
/*     */       }
/*     */     } 
/*     */     
/*  77 */     return buildDefaultBeanName(definition, registry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String determineBeanNameFromAnnotation(AnnotatedBeanDefinition annotatedDef) {
/*  86 */     AnnotationMetadata amd = annotatedDef.getMetadata();
/*  87 */     Set<String> types = amd.getAnnotationTypes();
/*  88 */     String beanName = null;
/*  89 */     for (String type : types) {
/*  90 */       AnnotationAttributes attributes = AnnotationConfigUtils.attributesFor((AnnotatedTypeMetadata)amd, type);
/*  91 */       if (isStereotypeWithNameValue(type, amd.getMetaAnnotationTypes(type), (Map<String, Object>)attributes)) {
/*  92 */         Object value = attributes.get("value");
/*  93 */         if (value instanceof String) {
/*  94 */           String strVal = (String)value;
/*  95 */           if (StringUtils.hasLength(strVal)) {
/*  96 */             if (beanName != null && !strVal.equals(beanName)) {
/*  97 */               throw new IllegalStateException("Stereotype annotations suggest inconsistent component names: '" + beanName + "' versus '" + strVal + "'");
/*     */             }
/*     */             
/* 100 */             beanName = strVal;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 105 */     return beanName;
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
/*     */   protected boolean isStereotypeWithNameValue(String annotationType, Set<String> metaAnnotationTypes, Map<String, Object> attributes) {
/* 122 */     boolean isStereotype = (annotationType.equals("org.springframework.stereotype.Component") || (metaAnnotationTypes != null && metaAnnotationTypes.contains("org.springframework.stereotype.Component")) || annotationType.equals("javax.annotation.ManagedBean") || annotationType.equals("javax.inject.Named"));
/*     */     
/* 124 */     return (isStereotype && attributes != null && attributes.containsKey("value"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String buildDefaultBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
/* 135 */     return buildDefaultBeanName(definition);
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
/*     */   protected String buildDefaultBeanName(BeanDefinition definition) {
/* 149 */     String shortClassName = ClassUtils.getShortName(definition.getBeanClassName());
/* 150 */     return Introspector.decapitalize(shortClassName);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\AnnotationBeanNameGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */