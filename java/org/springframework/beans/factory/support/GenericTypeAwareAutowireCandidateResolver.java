/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*     */ import org.springframework.core.ResolvableType;
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
/*     */ public class GenericTypeAwareAutowireCandidateResolver
/*     */   extends SimpleAutowireCandidateResolver
/*     */   implements BeanFactoryAware
/*     */ {
/*     */   private BeanFactory beanFactory;
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/*  51 */     this.beanFactory = beanFactory;
/*     */   }
/*     */   
/*     */   protected final BeanFactory getBeanFactory() {
/*  55 */     return this.beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
/*  61 */     if (!super.isAutowireCandidate(bdHolder, descriptor))
/*     */     {
/*  63 */       return false;
/*     */     }
/*  65 */     return (descriptor == null || checkGenericTypeMatch(bdHolder, descriptor));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkGenericTypeMatch(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
/*  73 */     ResolvableType dependencyType = descriptor.getResolvableType();
/*  74 */     if (dependencyType.getType() instanceof Class)
/*     */     {
/*  76 */       return true;
/*     */     }
/*     */     
/*  79 */     ResolvableType targetType = null;
/*  80 */     boolean cacheType = false;
/*  81 */     RootBeanDefinition rbd = null;
/*  82 */     if (bdHolder.getBeanDefinition() instanceof RootBeanDefinition) {
/*  83 */       rbd = (RootBeanDefinition)bdHolder.getBeanDefinition();
/*     */     }
/*  85 */     if (rbd != null) {
/*  86 */       targetType = rbd.targetType;
/*  87 */       if (targetType == null) {
/*  88 */         cacheType = true;
/*     */         
/*  90 */         targetType = getReturnTypeForFactoryMethod(rbd, descriptor);
/*  91 */         if (targetType == null) {
/*  92 */           RootBeanDefinition dbd = getResolvedDecoratedDefinition(rbd);
/*  93 */           if (dbd != null) {
/*  94 */             targetType = dbd.targetType;
/*  95 */             if (targetType == null) {
/*  96 */               targetType = getReturnTypeForFactoryMethod(dbd, descriptor);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 103 */     if (targetType == null) {
/*     */       
/* 105 */       if (this.beanFactory != null) {
/* 106 */         Class<?> beanType = this.beanFactory.getType(bdHolder.getBeanName());
/* 107 */         if (beanType != null) {
/* 108 */           targetType = ResolvableType.forClass(ClassUtils.getUserClass(beanType));
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 113 */       if (targetType == null && rbd != null && rbd.hasBeanClass() && rbd.getFactoryMethodName() == null) {
/* 114 */         Class<?> beanClass = rbd.getBeanClass();
/* 115 */         if (!FactoryBean.class.isAssignableFrom(beanClass)) {
/* 116 */           targetType = ResolvableType.forClass(ClassUtils.getUserClass(beanClass));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 121 */     if (targetType == null) {
/* 122 */       return true;
/*     */     }
/* 124 */     if (cacheType) {
/* 125 */       rbd.targetType = targetType;
/*     */     }
/* 127 */     if (descriptor.fallbackMatchAllowed() && targetType.hasUnresolvableGenerics()) {
/* 128 */       return true;
/*     */     }
/*     */     
/* 131 */     return dependencyType.isAssignableFrom(targetType);
/*     */   }
/*     */   
/*     */   protected RootBeanDefinition getResolvedDecoratedDefinition(RootBeanDefinition rbd) {
/* 135 */     BeanDefinitionHolder decDef = rbd.getDecoratedDefinition();
/* 136 */     if (decDef != null && this.beanFactory instanceof ConfigurableListableBeanFactory) {
/* 137 */       ConfigurableListableBeanFactory clbf = (ConfigurableListableBeanFactory)this.beanFactory;
/* 138 */       if (clbf.containsBeanDefinition(decDef.getBeanName())) {
/* 139 */         BeanDefinition dbd = clbf.getMergedBeanDefinition(decDef.getBeanName());
/* 140 */         if (dbd instanceof RootBeanDefinition) {
/* 141 */           return (RootBeanDefinition)dbd;
/*     */         }
/*     */       } 
/*     */     } 
/* 145 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ResolvableType getReturnTypeForFactoryMethod(RootBeanDefinition rbd, DependencyDescriptor descriptor) {
/* 151 */     ResolvableType returnType = rbd.factoryMethodReturnType;
/* 152 */     if (returnType == null) {
/* 153 */       Method factoryMethod = rbd.getResolvedFactoryMethod();
/* 154 */       if (factoryMethod != null) {
/* 155 */         returnType = ResolvableType.forMethodReturnType(factoryMethod);
/*     */       }
/*     */     } 
/* 158 */     if (returnType != null) {
/* 159 */       Class<?> resolvedClass = returnType.resolve();
/* 160 */       if (resolvedClass != null && descriptor.getDependencyType().isAssignableFrom(resolvedClass))
/*     */       {
/*     */ 
/*     */         
/* 164 */         return returnType;
/*     */       }
/*     */     } 
/* 167 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\GenericTypeAwareAutowireCandidateResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */