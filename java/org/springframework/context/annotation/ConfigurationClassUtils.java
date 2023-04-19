/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.StandardAnnotationMetadata;
/*     */ import org.springframework.core.type.classreading.MetadataReader;
/*     */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class ConfigurationClassUtils
/*     */ {
/*     */   private static final String CONFIGURATION_CLASS_FULL = "full";
/*     */   private static final String CONFIGURATION_CLASS_LITE = "lite";
/*  54 */   private static final String CONFIGURATION_CLASS_ATTRIBUTE = Conventions.getQualifiedAttributeName(ConfigurationClassPostProcessor.class, "configurationClass");
/*     */ 
/*     */   
/*  57 */   private static final String ORDER_ATTRIBUTE = Conventions.getQualifiedAttributeName(ConfigurationClassPostProcessor.class, "order");
/*     */ 
/*     */   
/*  60 */   private static final Log logger = LogFactory.getLog(ConfigurationClassUtils.class);
/*     */   
/*  62 */   private static final Set<String> candidateIndicators = new HashSet<String>(8);
/*     */   
/*     */   static {
/*  65 */     candidateIndicators.add(Component.class.getName());
/*  66 */     candidateIndicators.add(ComponentScan.class.getName());
/*  67 */     candidateIndicators.add(Import.class.getName());
/*  68 */     candidateIndicators.add(ImportResource.class.getName());
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
/*     */   public static boolean checkConfigurationClassCandidate(BeanDefinition beanDef, MetadataReaderFactory metadataReaderFactory) {
/*     */     AnnotationMetadata metadata;
/*  81 */     String className = beanDef.getBeanClassName();
/*  82 */     if (className == null || beanDef.getFactoryMethodName() != null) {
/*  83 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  87 */     if (beanDef instanceof AnnotatedBeanDefinition && className
/*  88 */       .equals(((AnnotatedBeanDefinition)beanDef).getMetadata().getClassName())) {
/*     */       
/*  90 */       metadata = ((AnnotatedBeanDefinition)beanDef).getMetadata();
/*     */     }
/*  92 */     else if (beanDef instanceof AbstractBeanDefinition && ((AbstractBeanDefinition)beanDef).hasBeanClass()) {
/*     */ 
/*     */       
/*  95 */       Class<?> beanClass = ((AbstractBeanDefinition)beanDef).getBeanClass();
/*  96 */       StandardAnnotationMetadata standardAnnotationMetadata = new StandardAnnotationMetadata(beanClass, true);
/*     */     } else {
/*     */       
/*     */       try {
/* 100 */         MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(className);
/* 101 */         metadata = metadataReader.getAnnotationMetadata();
/*     */       }
/* 103 */       catch (IOException ex) {
/* 104 */         if (logger.isDebugEnabled()) {
/* 105 */           logger.debug("Could not find class file for introspecting configuration annotations: " + className, ex);
/*     */         }
/* 107 */         return false;
/*     */       } 
/*     */     } 
/*     */     
/* 111 */     if (isFullConfigurationCandidate(metadata)) {
/* 112 */       beanDef.setAttribute(CONFIGURATION_CLASS_ATTRIBUTE, "full");
/*     */     }
/* 114 */     else if (isLiteConfigurationCandidate(metadata)) {
/* 115 */       beanDef.setAttribute(CONFIGURATION_CLASS_ATTRIBUTE, "lite");
/*     */     } else {
/*     */       
/* 118 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 122 */     Map<String, Object> orderAttributes = metadata.getAnnotationAttributes(Order.class.getName());
/* 123 */     if (orderAttributes != null) {
/* 124 */       beanDef.setAttribute(ORDER_ATTRIBUTE, orderAttributes.get("value"));
/*     */     }
/*     */     
/* 127 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isConfigurationCandidate(AnnotationMetadata metadata) {
/* 138 */     return (isFullConfigurationCandidate(metadata) || isLiteConfigurationCandidate(metadata));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFullConfigurationCandidate(AnnotationMetadata metadata) {
/* 149 */     return metadata.isAnnotated(Configuration.class.getName());
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
/*     */   public static boolean isLiteConfigurationCandidate(AnnotationMetadata metadata) {
/* 162 */     if (metadata.isInterface()) {
/* 163 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 167 */     for (String indicator : candidateIndicators) {
/* 168 */       if (metadata.isAnnotated(indicator)) {
/* 169 */         return true;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 175 */       return metadata.hasAnnotatedMethods(Bean.class.getName());
/*     */     }
/* 177 */     catch (Throwable ex) {
/* 178 */       if (logger.isDebugEnabled()) {
/* 179 */         logger.debug("Failed to introspect @Bean methods on class [" + metadata.getClassName() + "]: " + ex);
/*     */       }
/* 181 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFullConfigurationClass(BeanDefinition beanDef) {
/* 190 */     return "full".equals(beanDef.getAttribute(CONFIGURATION_CLASS_ATTRIBUTE));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isLiteConfigurationClass(BeanDefinition beanDef) {
/* 198 */     return "lite".equals(beanDef.getAttribute(CONFIGURATION_CLASS_ATTRIBUTE));
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
/*     */   public static int getOrder(BeanDefinition beanDef) {
/* 210 */     Integer order = (Integer)beanDef.getAttribute(ORDER_ATTRIBUTE);
/* 211 */     return (order != null) ? order.intValue() : Integer.MAX_VALUE;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\ConfigurationClassUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */