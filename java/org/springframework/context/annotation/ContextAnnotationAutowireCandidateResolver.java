/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
/*     */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.core.MethodParameter;
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
/*     */ public class ContextAnnotationAutowireCandidateResolver
/*     */   extends QualifierAnnotationAutowireCandidateResolver
/*     */ {
/*     */   public Object getLazyResolutionProxyIfNecessary(DependencyDescriptor descriptor, String beanName) {
/*  50 */     return isLazy(descriptor) ? buildLazyResolutionProxy(descriptor, beanName) : null;
/*     */   }
/*     */   
/*     */   protected boolean isLazy(DependencyDescriptor descriptor) {
/*  54 */     for (Annotation ann : descriptor.getAnnotations()) {
/*  55 */       Lazy lazy = (Lazy)AnnotationUtils.getAnnotation(ann, Lazy.class);
/*  56 */       if (lazy != null && lazy.value()) {
/*  57 */         return true;
/*     */       }
/*     */     } 
/*  60 */     MethodParameter methodParam = descriptor.getMethodParameter();
/*  61 */     if (methodParam != null) {
/*  62 */       Method method = methodParam.getMethod();
/*  63 */       if (method == null || void.class == method.getReturnType()) {
/*  64 */         Lazy lazy = (Lazy)AnnotationUtils.getAnnotation(methodParam.getAnnotatedElement(), Lazy.class);
/*  65 */         if (lazy != null && lazy.value()) {
/*  66 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*  70 */     return false;
/*     */   }
/*     */   
/*     */   protected Object buildLazyResolutionProxy(final DependencyDescriptor descriptor, final String beanName) {
/*  74 */     Assert.state(getBeanFactory() instanceof DefaultListableBeanFactory, "BeanFactory needs to be a DefaultListableBeanFactory");
/*     */     
/*  76 */     final DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)getBeanFactory();
/*  77 */     TargetSource ts = new TargetSource()
/*     */       {
/*     */         public Class<?> getTargetClass() {
/*  80 */           return descriptor.getDependencyType();
/*     */         }
/*     */         
/*     */         public boolean isStatic() {
/*  84 */           return false;
/*     */         }
/*     */         
/*     */         public Object getTarget() {
/*  88 */           Object target = beanFactory.doResolveDependency(descriptor, beanName, null, null);
/*  89 */           if (target == null) {
/*  90 */             Class<?> type = getTargetClass();
/*  91 */             if (Map.class == type) {
/*  92 */               return Collections.EMPTY_MAP;
/*     */             }
/*  94 */             if (List.class == type) {
/*  95 */               return Collections.EMPTY_LIST;
/*     */             }
/*  97 */             if (Set.class == type || Collection.class == type) {
/*  98 */               return Collections.EMPTY_SET;
/*     */             }
/* 100 */             throw new NoSuchBeanDefinitionException(descriptor.getResolvableType(), "Optional dependency not present for lazy injection point");
/*     */           } 
/*     */           
/* 103 */           return target;
/*     */         }
/*     */ 
/*     */         
/*     */         public void releaseTarget(Object target) {}
/*     */       };
/* 109 */     ProxyFactory pf = new ProxyFactory();
/* 110 */     pf.setTargetSource(ts);
/* 111 */     Class<?> dependencyType = descriptor.getDependencyType();
/* 112 */     if (dependencyType.isInterface()) {
/* 113 */       pf.addInterface(dependencyType);
/*     */     }
/* 115 */     return pf.getProxy(beanFactory.getBeanClassLoader());
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\ContextAnnotationAutowireCandidateResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */