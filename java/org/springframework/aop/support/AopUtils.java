/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.AopInvocationException;
/*     */ import org.springframework.aop.IntroductionAdvisor;
/*     */ import org.springframework.aop.IntroductionAwareMethodMatcher;
/*     */ import org.springframework.aop.MethodMatcher;
/*     */ import org.springframework.aop.Pointcut;
/*     */ import org.springframework.aop.PointcutAdvisor;
/*     */ import org.springframework.aop.SpringProxy;
/*     */ import org.springframework.aop.TargetClassAware;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.MethodIntrospector;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AopUtils
/*     */ {
/*     */   public static boolean isAopProxy(Object object) {
/*  68 */     return (object instanceof SpringProxy && (
/*  69 */       Proxy.isProxyClass(object.getClass()) || ClassUtils.isCglibProxyClass(object.getClass())));
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
/*     */   public static boolean isJdkDynamicProxy(Object object) {
/*  81 */     return (object instanceof SpringProxy && Proxy.isProxyClass(object.getClass()));
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
/*     */   public static boolean isCglibProxy(Object object) {
/*  93 */     return (object instanceof SpringProxy && ClassUtils.isCglibProxy(object));
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
/*     */   public static Class<?> getTargetClass(Object candidate) {
/* 106 */     Assert.notNull(candidate, "Candidate object must not be null");
/* 107 */     Class<?> result = null;
/* 108 */     if (candidate instanceof TargetClassAware) {
/* 109 */       result = ((TargetClassAware)candidate).getTargetClass();
/*     */     }
/* 111 */     if (result == null) {
/* 112 */       result = isCglibProxy(candidate) ? candidate.getClass().getSuperclass() : candidate.getClass();
/*     */     }
/* 114 */     return result;
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
/*     */   public static Method selectInvocableMethod(Method method, Class<?> targetType) {
/* 130 */     Method methodToUse = MethodIntrospector.selectInvocableMethod(method, targetType);
/* 131 */     if (Modifier.isPrivate(methodToUse.getModifiers()) && !Modifier.isStatic(methodToUse.getModifiers()) && SpringProxy.class
/* 132 */       .isAssignableFrom(targetType)) {
/* 133 */       throw new IllegalStateException(String.format("Need to invoke method '%s' found on proxy for target class '%s' but cannot be delegated to target bean. Switch its visibility to package or protected.", new Object[] { method
/*     */ 
/*     */               
/* 136 */               .getName(), method.getDeclaringClass().getSimpleName() }));
/*     */     }
/* 138 */     return methodToUse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEqualsMethod(Method method) {
/* 146 */     return ReflectionUtils.isEqualsMethod(method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isHashCodeMethod(Method method) {
/* 154 */     return ReflectionUtils.isHashCodeMethod(method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isToStringMethod(Method method) {
/* 162 */     return ReflectionUtils.isToStringMethod(method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFinalizeMethod(Method method) {
/* 170 */     return (method != null && method.getName().equals("finalize") && (method
/* 171 */       .getParameterTypes()).length == 0);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method getMostSpecificMethod(Method method, Class<?> targetClass) {
/* 191 */     Method resolvedMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
/*     */     
/* 193 */     return BridgeMethodResolver.findBridgedMethod(resolvedMethod);
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
/*     */   public static boolean canApply(Pointcut pc, Class<?> targetClass) {
/* 205 */     return canApply(pc, targetClass, false);
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
/*     */   public static boolean canApply(Pointcut pc, Class<?> targetClass, boolean hasIntroductions) {
/* 219 */     Assert.notNull(pc, "Pointcut must not be null");
/* 220 */     if (!pc.getClassFilter().matches(targetClass)) {
/* 221 */       return false;
/*     */     }
/*     */     
/* 224 */     MethodMatcher methodMatcher = pc.getMethodMatcher();
/* 225 */     if (methodMatcher == MethodMatcher.TRUE)
/*     */     {
/* 227 */       return true;
/*     */     }
/*     */     
/* 230 */     IntroductionAwareMethodMatcher introductionAwareMethodMatcher = null;
/* 231 */     if (methodMatcher instanceof IntroductionAwareMethodMatcher) {
/* 232 */       introductionAwareMethodMatcher = (IntroductionAwareMethodMatcher)methodMatcher;
/*     */     }
/*     */     
/* 235 */     Set<Class<?>> classes = new LinkedHashSet<Class<?>>(ClassUtils.getAllInterfacesForClassAsSet(targetClass));
/* 236 */     classes.add(targetClass);
/* 237 */     for (Class<?> clazz : classes) {
/* 238 */       Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
/* 239 */       for (Method method : methods) {
/* 240 */         if ((introductionAwareMethodMatcher != null && introductionAwareMethodMatcher
/* 241 */           .matches(method, targetClass, hasIntroductions)) || methodMatcher
/* 242 */           .matches(method, targetClass)) {
/* 243 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 248 */     return false;
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
/*     */   public static boolean canApply(Advisor advisor, Class<?> targetClass) {
/* 260 */     return canApply(advisor, targetClass, false);
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
/*     */   public static boolean canApply(Advisor advisor, Class<?> targetClass, boolean hasIntroductions) {
/* 274 */     if (advisor instanceof IntroductionAdvisor) {
/* 275 */       return ((IntroductionAdvisor)advisor).getClassFilter().matches(targetClass);
/*     */     }
/* 277 */     if (advisor instanceof PointcutAdvisor) {
/* 278 */       PointcutAdvisor pca = (PointcutAdvisor)advisor;
/* 279 */       return canApply(pca.getPointcut(), targetClass, hasIntroductions);
/*     */     } 
/*     */ 
/*     */     
/* 283 */     return true;
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
/*     */   public static List<Advisor> findAdvisorsThatCanApply(List<Advisor> candidateAdvisors, Class<?> clazz) {
/* 296 */     if (candidateAdvisors.isEmpty()) {
/* 297 */       return candidateAdvisors;
/*     */     }
/* 299 */     List<Advisor> eligibleAdvisors = new LinkedList<Advisor>();
/* 300 */     for (Advisor candidate : candidateAdvisors) {
/* 301 */       if (candidate instanceof IntroductionAdvisor && canApply(candidate, clazz)) {
/* 302 */         eligibleAdvisors.add(candidate);
/*     */       }
/*     */     } 
/* 305 */     boolean hasIntroductions = !eligibleAdvisors.isEmpty();
/* 306 */     for (Advisor candidate : candidateAdvisors) {
/* 307 */       if (candidate instanceof IntroductionAdvisor) {
/*     */         continue;
/*     */       }
/*     */       
/* 311 */       if (canApply(candidate, clazz, hasIntroductions)) {
/* 312 */         eligibleAdvisors.add(candidate);
/*     */       }
/*     */     } 
/* 315 */     return eligibleAdvisors;
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
/*     */   public static Object invokeJoinpointUsingReflection(Object target, Method method, Object[] args) throws Throwable {
/*     */     try {
/* 332 */       ReflectionUtils.makeAccessible(method);
/* 333 */       return method.invoke(target, args);
/*     */     }
/* 335 */     catch (InvocationTargetException ex) {
/*     */ 
/*     */       
/* 338 */       throw ex.getTargetException();
/*     */     }
/* 340 */     catch (IllegalArgumentException ex) {
/* 341 */       throw new AopInvocationException("AOP configuration seems to be invalid: tried calling method [" + method + "] on target [" + target + "]", ex);
/*     */     
/*     */     }
/* 344 */     catch (IllegalAccessException ex) {
/* 345 */       throw new AopInvocationException("Could not access method [" + method + "]", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\AopUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */