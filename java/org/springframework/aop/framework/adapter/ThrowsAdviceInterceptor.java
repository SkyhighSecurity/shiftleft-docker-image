/*     */ package org.springframework.aop.framework.adapter;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.AfterAdvice;
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
/*     */ public class ThrowsAdviceInterceptor
/*     */   implements MethodInterceptor, AfterAdvice
/*     */ {
/*     */   private static final String AFTER_THROWING = "afterThrowing";
/*  60 */   private static final Log logger = LogFactory.getLog(ThrowsAdviceInterceptor.class);
/*     */ 
/*     */   
/*     */   private final Object throwsAdvice;
/*     */ 
/*     */   
/*  66 */   private final Map<Class<?>, Method> exceptionHandlerMap = new HashMap<Class<?>, Method>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThrowsAdviceInterceptor(Object throwsAdvice) {
/*  75 */     Assert.notNull(throwsAdvice, "Advice must not be null");
/*  76 */     this.throwsAdvice = throwsAdvice;
/*     */     
/*  78 */     Method[] methods = throwsAdvice.getClass().getMethods();
/*  79 */     for (Method method : methods) {
/*  80 */       if (method.getName().equals("afterThrowing")) {
/*  81 */         Class<?>[] paramTypes = method.getParameterTypes();
/*  82 */         if (paramTypes.length == 1 || paramTypes.length == 4) {
/*  83 */           Class<?> throwableParam = paramTypes[paramTypes.length - 1];
/*  84 */           if (Throwable.class.isAssignableFrom(throwableParam)) {
/*     */             
/*  86 */             this.exceptionHandlerMap.put(throwableParam, method);
/*  87 */             if (logger.isDebugEnabled()) {
/*  88 */               logger.debug("Found exception handler method on throws advice: " + method);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  95 */     if (this.exceptionHandlerMap.isEmpty()) {
/*  96 */       throw new IllegalArgumentException("At least one handler method must be found in class [" + throwsAdvice
/*  97 */           .getClass() + "]");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getHandlerMethodCount() {
/* 106 */     return this.exceptionHandlerMap.size();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(MethodInvocation mi) throws Throwable {
/*     */     try {
/* 113 */       return mi.proceed();
/*     */     }
/* 115 */     catch (Throwable ex) {
/* 116 */       Method handlerMethod = getExceptionHandler(ex);
/* 117 */       if (handlerMethod != null) {
/* 118 */         invokeHandlerMethod(mi, ex, handlerMethod);
/*     */       }
/* 120 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Method getExceptionHandler(Throwable exception) {
/* 130 */     Class<?> exceptionClass = exception.getClass();
/* 131 */     if (logger.isTraceEnabled()) {
/* 132 */       logger.trace("Trying to find handler for exception of type [" + exceptionClass.getName() + "]");
/*     */     }
/* 134 */     Method handler = this.exceptionHandlerMap.get(exceptionClass);
/* 135 */     while (handler == null && exceptionClass != Throwable.class) {
/* 136 */       exceptionClass = exceptionClass.getSuperclass();
/* 137 */       handler = this.exceptionHandlerMap.get(exceptionClass);
/*     */     } 
/* 139 */     if (handler != null && logger.isDebugEnabled()) {
/* 140 */       logger.debug("Found handler for exception of type [" + exceptionClass.getName() + "]: " + handler);
/*     */     }
/* 142 */     return handler;
/*     */   }
/*     */   
/*     */   private void invokeHandlerMethod(MethodInvocation mi, Throwable ex, Method method) throws Throwable {
/*     */     Object[] handlerArgs;
/* 147 */     if ((method.getParameterTypes()).length == 1) {
/* 148 */       handlerArgs = new Object[] { ex };
/*     */     } else {
/*     */       
/* 151 */       handlerArgs = new Object[] { mi.getMethod(), mi.getArguments(), mi.getThis(), ex };
/*     */     } 
/*     */     try {
/* 154 */       method.invoke(this.throwsAdvice, handlerArgs);
/*     */     }
/* 156 */     catch (InvocationTargetException targetEx) {
/* 157 */       throw targetEx.getTargetException();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\adapter\ThrowsAdviceInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */