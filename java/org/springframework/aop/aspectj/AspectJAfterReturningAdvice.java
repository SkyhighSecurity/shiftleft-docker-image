/*     */ package org.springframework.aop.aspectj;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import org.springframework.aop.AfterAdvice;
/*     */ import org.springframework.aop.AfterReturningAdvice;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.TypeUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AspectJAfterReturningAdvice
/*     */   extends AbstractAspectJAdvice
/*     */   implements AfterReturningAdvice, AfterAdvice, Serializable
/*     */ {
/*     */   public AspectJAfterReturningAdvice(Method aspectJBeforeAdviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory aif) {
/*  43 */     super(aspectJBeforeAdviceMethod, pointcut, aif);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBeforeAdvice() {
/*  49 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAfterAdvice() {
/*  54 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setReturningName(String name) {
/*  59 */     setReturningNameNoCheck(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
/*  64 */     if (shouldInvokeOnReturnValueOf(method, returnValue)) {
/*  65 */       invokeAdviceMethod(getJoinPointMatch(), returnValue, null);
/*     */     }
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
/*     */   private boolean shouldInvokeOnReturnValueOf(Method method, Object returnValue) {
/*  79 */     Class<?> type = getDiscoveredReturningType();
/*  80 */     Type genericType = getDiscoveredReturningGenericType();
/*     */     
/*  82 */     return (matchesReturnValue(type, method, returnValue) && (genericType == null || genericType == type || 
/*     */       
/*  84 */       TypeUtils.isAssignable(genericType, method.getGenericReturnType())));
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
/*     */   private boolean matchesReturnValue(Class<?> type, Method method, Object returnValue) {
/*  98 */     if (returnValue != null) {
/*  99 */       return ClassUtils.isAssignableValue(type, returnValue);
/*     */     }
/* 101 */     if (Object.class == type && void.class == method.getReturnType()) {
/* 102 */       return true;
/*     */     }
/*     */     
/* 105 */     return ClassUtils.isAssignable(type, method.getReturnType());
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\AspectJAfterReturningAdvice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */