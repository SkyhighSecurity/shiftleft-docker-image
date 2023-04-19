/*     */ package org.springframework.beans.factory.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.AutowireCandidateResolver;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CustomAutowireConfigurer
/*     */   implements BeanFactoryPostProcessor, BeanClassLoaderAware, Ordered
/*     */ {
/*  51 */   private int order = Integer.MAX_VALUE;
/*     */   
/*     */   private Set<?> customQualifierTypes;
/*     */   
/*  55 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */   
/*     */   public void setOrder(int order) {
/*  59 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/*  64 */     return this.order;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader beanClassLoader) {
/*  69 */     this.beanClassLoader = beanClassLoader;
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
/*     */   public void setCustomQualifierTypes(Set<?> customQualifierTypes) {
/*  83 */     this.customQualifierTypes = customQualifierTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
/*  90 */     if (this.customQualifierTypes != null) {
/*  91 */       if (!(beanFactory instanceof DefaultListableBeanFactory)) {
/*  92 */         throw new IllegalStateException("CustomAutowireConfigurer needs to operate on a DefaultListableBeanFactory");
/*     */       }
/*     */       
/*  95 */       DefaultListableBeanFactory dlbf = (DefaultListableBeanFactory)beanFactory;
/*  96 */       if (!(dlbf.getAutowireCandidateResolver() instanceof QualifierAnnotationAutowireCandidateResolver)) {
/*  97 */         dlbf.setAutowireCandidateResolver((AutowireCandidateResolver)new QualifierAnnotationAutowireCandidateResolver());
/*     */       }
/*     */       
/* 100 */       QualifierAnnotationAutowireCandidateResolver resolver = (QualifierAnnotationAutowireCandidateResolver)dlbf.getAutowireCandidateResolver();
/* 101 */       for (Object value : this.customQualifierTypes) {
/* 102 */         Class<? extends Annotation> customType = null;
/* 103 */         if (value instanceof Class) {
/* 104 */           customType = (Class<? extends Annotation>)value;
/*     */         }
/* 106 */         else if (value instanceof String) {
/* 107 */           String className = (String)value;
/* 108 */           customType = ClassUtils.resolveClassName(className, this.beanClassLoader);
/*     */         } else {
/*     */           
/* 111 */           throw new IllegalArgumentException("Invalid value [" + value + "] for custom qualifier type: needs to be Class or String.");
/*     */         } 
/*     */         
/* 114 */         if (!Annotation.class.isAssignableFrom(customType)) {
/* 115 */           throw new IllegalArgumentException("Qualifier type [" + customType
/* 116 */               .getName() + "] needs to be annotation type");
/*     */         }
/* 118 */         resolver.addQualifierType(customType);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\annotation\CustomAutowireConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */