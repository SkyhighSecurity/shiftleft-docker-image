/*     */ package org.springframework.jmx.export.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.annotation.AnnotationBeanUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.config.EmbeddedValueResolver;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.jmx.export.metadata.InvalidMetadataException;
/*     */ import org.springframework.jmx.export.metadata.JmxAttributeSource;
/*     */ import org.springframework.jmx.export.metadata.ManagedAttribute;
/*     */ import org.springframework.jmx.export.metadata.ManagedMetric;
/*     */ import org.springframework.jmx.export.metadata.ManagedNotification;
/*     */ import org.springframework.jmx.export.metadata.ManagedOperation;
/*     */ import org.springframework.jmx.export.metadata.ManagedOperationParameter;
/*     */ import org.springframework.jmx.export.metadata.ManagedResource;
/*     */ import org.springframework.util.StringValueResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationJmxAttributeSource
/*     */   implements JmxAttributeSource, BeanFactoryAware
/*     */ {
/*     */   private StringValueResolver embeddedValueResolver;
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/*  57 */     if (beanFactory instanceof ConfigurableBeanFactory) {
/*  58 */       this.embeddedValueResolver = (StringValueResolver)new EmbeddedValueResolver((ConfigurableBeanFactory)beanFactory);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ManagedResource getManagedResource(Class<?> beanClass) throws InvalidMetadataException {
/*  65 */     ManagedResource ann = (ManagedResource)AnnotationUtils.findAnnotation(beanClass, ManagedResource.class);
/*  66 */     if (ann == null) {
/*  67 */       return null;
/*     */     }
/*  69 */     Class<?> declaringClass = AnnotationUtils.findAnnotationDeclaringClass(ManagedResource.class, beanClass);
/*  70 */     Class<?> target = (declaringClass != null && !declaringClass.isInterface()) ? declaringClass : beanClass;
/*  71 */     if (!Modifier.isPublic(target.getModifiers())) {
/*  72 */       throw new InvalidMetadataException("@ManagedResource class '" + target.getName() + "' must be public");
/*     */     }
/*  74 */     ManagedResource managedResource = new ManagedResource();
/*  75 */     AnnotationBeanUtils.copyPropertiesToBean(ann, managedResource, this.embeddedValueResolver, new String[0]);
/*  76 */     return managedResource;
/*     */   }
/*     */ 
/*     */   
/*     */   public ManagedAttribute getManagedAttribute(Method method) throws InvalidMetadataException {
/*  81 */     ManagedAttribute ann = (ManagedAttribute)AnnotationUtils.findAnnotation(method, ManagedAttribute.class);
/*  82 */     if (ann == null) {
/*  83 */       return null;
/*     */     }
/*  85 */     ManagedAttribute managedAttribute = new ManagedAttribute();
/*  86 */     AnnotationBeanUtils.copyPropertiesToBean(ann, managedAttribute, new String[] { "defaultValue" });
/*  87 */     if (ann.defaultValue().length() > 0) {
/*  88 */       managedAttribute.setDefaultValue(ann.defaultValue());
/*     */     }
/*  90 */     return managedAttribute;
/*     */   }
/*     */ 
/*     */   
/*     */   public ManagedMetric getManagedMetric(Method method) throws InvalidMetadataException {
/*  95 */     ManagedMetric ann = (ManagedMetric)AnnotationUtils.findAnnotation(method, ManagedMetric.class);
/*  96 */     return copyPropertiesToBean(ann, ManagedMetric.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public ManagedOperation getManagedOperation(Method method) throws InvalidMetadataException {
/* 101 */     ManagedOperation ann = (ManagedOperation)AnnotationUtils.findAnnotation(method, ManagedOperation.class);
/* 102 */     return copyPropertiesToBean(ann, ManagedOperation.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ManagedOperationParameter[] getManagedOperationParameters(Method method) throws InvalidMetadataException {
/* 109 */     Set<ManagedOperationParameter> anns = AnnotationUtils.getRepeatableAnnotations(method, ManagedOperationParameter.class, ManagedOperationParameters.class);
/*     */     
/* 111 */     return copyPropertiesToBeanArray((Collection)anns, ManagedOperationParameter.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ManagedNotification[] getManagedNotifications(Class<?> clazz) throws InvalidMetadataException {
/* 118 */     Set<ManagedNotification> anns = AnnotationUtils.getRepeatableAnnotations(clazz, ManagedNotification.class, ManagedNotifications.class);
/*     */     
/* 120 */     return copyPropertiesToBeanArray((Collection)anns, ManagedNotification.class);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> T[] copyPropertiesToBeanArray(Collection<? extends Annotation> anns, Class<T> beanClass) {
/* 126 */     T[] beans = (T[])Array.newInstance(beanClass, anns.size());
/* 127 */     int i = 0;
/* 128 */     for (Annotation ann : anns) {
/* 129 */       beans[i++] = copyPropertiesToBean(ann, beanClass);
/*     */     }
/* 131 */     return beans;
/*     */   }
/*     */   
/*     */   private static <T> T copyPropertiesToBean(Annotation ann, Class<T> beanClass) {
/* 135 */     if (ann == null) {
/* 136 */       return null;
/*     */     }
/* 138 */     T bean = (T)BeanUtils.instantiateClass(beanClass);
/* 139 */     AnnotationBeanUtils.copyPropertiesToBean(ann, bean, new String[0]);
/* 140 */     return bean;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\export\annotation\AnnotationJmxAttributeSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */