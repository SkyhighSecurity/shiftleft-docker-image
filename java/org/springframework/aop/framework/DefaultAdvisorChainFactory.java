/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.IntroductionAdvisor;
/*     */ import org.springframework.aop.MethodMatcher;
/*     */ import org.springframework.aop.PointcutAdvisor;
/*     */ import org.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
/*     */ import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
/*     */ import org.springframework.aop.support.MethodMatchers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultAdvisorChainFactory
/*     */   implements AdvisorChainFactory, Serializable
/*     */ {
/*     */   public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Advised config, Method method, Class<?> targetClass) {
/*  55 */     List<Object> interceptorList = new ArrayList((config.getAdvisors()).length);
/*  56 */     Class<?> actualClass = (targetClass != null) ? targetClass : method.getDeclaringClass();
/*  57 */     boolean hasIntroductions = hasMatchingIntroductions(config, actualClass);
/*  58 */     AdvisorAdapterRegistry registry = GlobalAdvisorAdapterRegistry.getInstance();
/*     */     
/*  60 */     for (Advisor advisor : config.getAdvisors()) {
/*  61 */       if (advisor instanceof PointcutAdvisor) {
/*     */         
/*  63 */         PointcutAdvisor pointcutAdvisor = (PointcutAdvisor)advisor;
/*  64 */         if (config.isPreFiltered() || pointcutAdvisor.getPointcut().getClassFilter().matches(actualClass)) {
/*  65 */           MethodMatcher mm = pointcutAdvisor.getPointcut().getMethodMatcher();
/*  66 */           if (MethodMatchers.matches(mm, method, actualClass, hasIntroductions)) {
/*  67 */             MethodInterceptor[] interceptors = registry.getInterceptors(advisor);
/*  68 */             if (mm.isRuntime()) {
/*     */ 
/*     */               
/*  71 */               for (MethodInterceptor interceptor : interceptors) {
/*  72 */                 interceptorList.add(new InterceptorAndDynamicMethodMatcher(interceptor, mm));
/*     */               }
/*     */             } else {
/*     */               
/*  76 */               interceptorList.addAll(Arrays.asList((Object[])interceptors));
/*     */             }
/*     */           
/*     */           } 
/*     */         } 
/*  81 */       } else if (advisor instanceof IntroductionAdvisor) {
/*  82 */         IntroductionAdvisor ia = (IntroductionAdvisor)advisor;
/*  83 */         if (config.isPreFiltered() || ia.getClassFilter().matches(actualClass)) {
/*  84 */           MethodInterceptor[] arrayOfMethodInterceptor = registry.getInterceptors(advisor);
/*  85 */           interceptorList.addAll(Arrays.asList((Object[])arrayOfMethodInterceptor));
/*     */         } 
/*     */       } else {
/*     */         
/*  89 */         MethodInterceptor[] arrayOfMethodInterceptor = registry.getInterceptors(advisor);
/*  90 */         interceptorList.addAll(Arrays.asList((Object[])arrayOfMethodInterceptor));
/*     */       } 
/*     */     } 
/*     */     
/*  94 */     return interceptorList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean hasMatchingIntroductions(Advised config, Class<?> actualClass) {
/* 101 */     for (Advisor advisor : config.getAdvisors()) {
/* 102 */       if (advisor instanceof IntroductionAdvisor) {
/* 103 */         IntroductionAdvisor ia = (IntroductionAdvisor)advisor;
/* 104 */         if (ia.getClassFilter().matches(actualClass)) {
/* 105 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 109 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\DefaultAdvisorChainFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */