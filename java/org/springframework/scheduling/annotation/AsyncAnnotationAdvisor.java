/*     */ package org.springframework.scheduling.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.aop.Pointcut;
/*     */ import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
/*     */ import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
/*     */ import org.springframework.aop.support.AbstractPointcutAdvisor;
/*     */ import org.springframework.aop.support.ComposablePointcut;
/*     */ import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsyncAnnotationAdvisor
/*     */   extends AbstractPointcutAdvisor
/*     */   implements BeanFactoryAware
/*     */ {
/*     */   private AsyncUncaughtExceptionHandler exceptionHandler;
/*     */   private Advice advice;
/*     */   private Pointcut pointcut;
/*     */   
/*     */   public AsyncAnnotationAdvisor() {
/*  69 */     this(null, null);
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
/*     */   public AsyncAnnotationAdvisor(Executor executor, AsyncUncaughtExceptionHandler exceptionHandler) {
/*  82 */     Set<Class<? extends Annotation>> asyncAnnotationTypes = new LinkedHashSet<Class<? extends Annotation>>(2);
/*  83 */     asyncAnnotationTypes.add(Async.class);
/*     */     try {
/*  85 */       asyncAnnotationTypes.add(
/*  86 */           ClassUtils.forName("javax.ejb.Asynchronous", AsyncAnnotationAdvisor.class.getClassLoader()));
/*     */     }
/*  88 */     catch (ClassNotFoundException classNotFoundException) {}
/*     */ 
/*     */     
/*  91 */     if (exceptionHandler != null) {
/*  92 */       this.exceptionHandler = exceptionHandler;
/*     */     } else {
/*     */       
/*  95 */       this.exceptionHandler = (AsyncUncaughtExceptionHandler)new SimpleAsyncUncaughtExceptionHandler();
/*     */     } 
/*  97 */     this.advice = buildAdvice(executor, this.exceptionHandler);
/*  98 */     this.pointcut = buildPointcut(asyncAnnotationTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTaskExecutor(Executor executor) {
/* 106 */     this.advice = buildAdvice(executor, this.exceptionHandler);
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
/*     */   public void setAsyncAnnotationType(Class<? extends Annotation> asyncAnnotationType) {
/* 119 */     Assert.notNull(asyncAnnotationType, "'asyncAnnotationType' must not be null");
/* 120 */     Set<Class<? extends Annotation>> asyncAnnotationTypes = new HashSet<Class<? extends Annotation>>();
/* 121 */     asyncAnnotationTypes.add(asyncAnnotationType);
/* 122 */     this.pointcut = buildPointcut(asyncAnnotationTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 130 */     if (this.advice instanceof BeanFactoryAware) {
/* 131 */       ((BeanFactoryAware)this.advice).setBeanFactory(beanFactory);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Advice getAdvice() {
/* 138 */     return this.advice;
/*     */   }
/*     */ 
/*     */   
/*     */   public Pointcut getPointcut() {
/* 143 */     return this.pointcut;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Advice buildAdvice(Executor executor, AsyncUncaughtExceptionHandler exceptionHandler) {
/* 148 */     return (Advice)new AnnotationAsyncExecutionInterceptor(executor, exceptionHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Pointcut buildPointcut(Set<Class<? extends Annotation>> asyncAnnotationTypes) {
/* 157 */     ComposablePointcut result = null;
/* 158 */     for (Class<? extends Annotation> asyncAnnotationType : asyncAnnotationTypes) {
/* 159 */       AnnotationMatchingPointcut annotationMatchingPointcut1 = new AnnotationMatchingPointcut(asyncAnnotationType, true);
/* 160 */       AnnotationMatchingPointcut annotationMatchingPointcut2 = AnnotationMatchingPointcut.forMethodAnnotation(asyncAnnotationType);
/* 161 */       if (result == null) {
/* 162 */         result = new ComposablePointcut((Pointcut)annotationMatchingPointcut1);
/*     */       } else {
/*     */         
/* 165 */         result.union((Pointcut)annotationMatchingPointcut1);
/*     */       } 
/* 167 */       result = result.union((Pointcut)annotationMatchingPointcut2);
/*     */     } 
/* 169 */     return (Pointcut)result;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\annotation\AsyncAnnotationAdvisor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */