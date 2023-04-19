/*     */ package org.springframework.beans.factory.annotation;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
/*     */ import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.core.PriorityOrdered;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RequiredAnnotationBeanPostProcessor
/*     */   extends InstantiationAwareBeanPostProcessorAdapter
/*     */   implements MergedBeanDefinitionPostProcessor, PriorityOrdered, BeanFactoryAware
/*     */ {
/*  84 */   public static final String SKIP_REQUIRED_CHECK_ATTRIBUTE = Conventions.getQualifiedAttributeName(RequiredAnnotationBeanPostProcessor.class, "skipRequiredCheck");
/*     */ 
/*     */   
/*  87 */   private Class<? extends Annotation> requiredAnnotationType = (Class)Required.class;
/*     */   
/*  89 */   private int order = 2147483646;
/*     */ 
/*     */ 
/*     */   
/*     */   private ConfigurableListableBeanFactory beanFactory;
/*     */ 
/*     */ 
/*     */   
/*  97 */   private final Set<String> validatedBeanNames = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>(64));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRequiredAnnotationType(Class<? extends Annotation> requiredAnnotationType) {
/* 110 */     Assert.notNull(requiredAnnotationType, "'requiredAnnotationType' must not be null");
/* 111 */     this.requiredAnnotationType = requiredAnnotationType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<? extends Annotation> getRequiredAnnotationType() {
/* 118 */     return this.requiredAnnotationType;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 123 */     if (beanFactory instanceof ConfigurableListableBeanFactory) {
/* 124 */       this.beanFactory = (ConfigurableListableBeanFactory)beanFactory;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setOrder(int order) {
/* 129 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 134 */     return this.order;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
/* 146 */     if (!this.validatedBeanNames.contains(beanName)) {
/* 147 */       if (!shouldSkip(this.beanFactory, beanName)) {
/* 148 */         List<String> invalidProperties = new ArrayList<String>();
/* 149 */         for (PropertyDescriptor pd : pds) {
/* 150 */           if (isRequiredProperty(pd) && !pvs.contains(pd.getName())) {
/* 151 */             invalidProperties.add(pd.getName());
/*     */           }
/*     */         } 
/* 154 */         if (!invalidProperties.isEmpty()) {
/* 155 */           throw new BeanInitializationException(buildExceptionMessage(invalidProperties, beanName));
/*     */         }
/*     */       } 
/* 158 */       this.validatedBeanNames.add(beanName);
/*     */     } 
/* 160 */     return pvs;
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
/*     */   protected boolean shouldSkip(ConfigurableListableBeanFactory beanFactory, String beanName) {
/* 175 */     if (beanFactory == null || !beanFactory.containsBeanDefinition(beanName)) {
/* 176 */       return false;
/*     */     }
/* 178 */     BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
/* 179 */     if (beanDefinition.getFactoryBeanName() != null) {
/* 180 */       return true;
/*     */     }
/* 182 */     Object value = beanDefinition.getAttribute(SKIP_REQUIRED_CHECK_ATTRIBUTE);
/* 183 */     return (value != null && (Boolean.TRUE.equals(value) || Boolean.valueOf(value.toString()).booleanValue()));
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
/*     */   protected boolean isRequiredProperty(PropertyDescriptor propertyDescriptor) {
/* 196 */     Method setter = propertyDescriptor.getWriteMethod();
/* 197 */     return (setter != null && AnnotationUtils.getAnnotation(setter, getRequiredAnnotationType()) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String buildExceptionMessage(List<String> invalidProperties, String beanName) {
/* 207 */     int size = invalidProperties.size();
/* 208 */     StringBuilder sb = new StringBuilder();
/* 209 */     sb.append((size == 1) ? "Property" : "Properties");
/* 210 */     for (int i = 0; i < size; i++) {
/* 211 */       String propertyName = invalidProperties.get(i);
/* 212 */       if (i > 0) {
/* 213 */         if (i == size - 1) {
/* 214 */           sb.append(" and");
/*     */         } else {
/*     */           
/* 217 */           sb.append(",");
/*     */         } 
/*     */       }
/* 220 */       sb.append(" '").append(propertyName).append("'");
/*     */     } 
/* 222 */     sb.append((size == 1) ? " is" : " are");
/* 223 */     sb.append(" required for bean '").append(beanName).append("'");
/* 224 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\annotation\RequiredAnnotationBeanPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */