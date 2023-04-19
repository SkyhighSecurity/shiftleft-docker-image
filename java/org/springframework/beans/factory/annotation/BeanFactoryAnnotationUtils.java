/*     */ package org.springframework.beans.factory.annotation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.AutowireCandidateQualifier;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
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
/*     */ public abstract class BeanFactoryAnnotationUtils
/*     */ {
/*     */   public static <T> T qualifiedBeanOfType(BeanFactory beanFactory, Class<T> beanType, String qualifier) throws BeansException {
/*  62 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/*     */     
/*  64 */     if (beanFactory instanceof ConfigurableListableBeanFactory)
/*     */     {
/*  66 */       return qualifiedBeanOfType((ConfigurableListableBeanFactory)beanFactory, beanType, qualifier);
/*     */     }
/*  68 */     if (beanFactory.containsBean(qualifier))
/*     */     {
/*  70 */       return (T)beanFactory.getBean(qualifier, beanType);
/*     */     }
/*     */     
/*  73 */     throw new NoSuchBeanDefinitionException(qualifier, "No matching " + beanType.getSimpleName() + " bean found for bean name '" + qualifier + "'! (Note: Qualifier matching not supported because given BeanFactory does not implement ConfigurableListableBeanFactory.)");
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
/*     */   private static <T> T qualifiedBeanOfType(ConfigurableListableBeanFactory bf, Class<T> beanType, String qualifier) {
/*  89 */     String[] candidateBeans = BeanFactoryUtils.beanNamesForTypeIncludingAncestors((ListableBeanFactory)bf, beanType);
/*  90 */     String matchingBean = null;
/*  91 */     for (String beanName : candidateBeans) {
/*  92 */       if (isQualifierMatch(qualifier, beanName, bf)) {
/*  93 */         if (matchingBean != null) {
/*  94 */           throw new NoUniqueBeanDefinitionException(beanType, new String[] { matchingBean, beanName });
/*     */         }
/*  96 */         matchingBean = beanName;
/*     */       } 
/*     */     } 
/*  99 */     if (matchingBean != null) {
/* 100 */       return (T)bf.getBean(matchingBean, beanType);
/*     */     }
/* 102 */     if (bf.containsBean(qualifier))
/*     */     {
/* 104 */       return (T)bf.getBean(qualifier, beanType);
/*     */     }
/*     */     
/* 107 */     throw new NoSuchBeanDefinitionException(qualifier, "No matching " + beanType.getSimpleName() + " bean found for qualifier '" + qualifier + "' - neither qualifier match nor bean name match!");
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
/*     */   private static boolean isQualifierMatch(String qualifier, String beanName, ConfigurableListableBeanFactory bf) {
/* 122 */     if (bf.containsBean(beanName)) {
/*     */       try {
/* 124 */         BeanDefinition bd = bf.getMergedBeanDefinition(beanName);
/*     */         
/* 126 */         if (bd instanceof AbstractBeanDefinition) {
/* 127 */           AbstractBeanDefinition abd = (AbstractBeanDefinition)bd;
/* 128 */           AutowireCandidateQualifier candidate = abd.getQualifier(Qualifier.class.getName());
/* 129 */           if ((candidate != null && qualifier.equals(candidate.getAttribute("value"))) || qualifier
/* 130 */             .equals(beanName) || ObjectUtils.containsElement((Object[])bf.getAliases(beanName), qualifier)) {
/* 131 */             return true;
/*     */           }
/*     */         } 
/*     */         
/* 135 */         if (bd instanceof RootBeanDefinition) {
/* 136 */           Method factoryMethod = ((RootBeanDefinition)bd).getResolvedFactoryMethod();
/* 137 */           if (factoryMethod != null) {
/* 138 */             Qualifier targetAnnotation = (Qualifier)AnnotationUtils.getAnnotation(factoryMethod, Qualifier.class);
/* 139 */             if (targetAnnotation != null) {
/* 140 */               return qualifier.equals(targetAnnotation.value());
/*     */             }
/*     */           } 
/*     */         } 
/*     */         
/* 145 */         Class<?> beanType = bf.getType(beanName);
/* 146 */         if (beanType != null) {
/* 147 */           Qualifier targetAnnotation = (Qualifier)AnnotationUtils.getAnnotation(beanType, Qualifier.class);
/* 148 */           if (targetAnnotation != null) {
/* 149 */             return qualifier.equals(targetAnnotation.value());
/*     */           }
/*     */         }
/*     */       
/* 153 */       } catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {}
/*     */     }
/*     */ 
/*     */     
/* 157 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\annotation\BeanFactoryAnnotationUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */