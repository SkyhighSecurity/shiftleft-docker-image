/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public abstract class MethodIntrospector
/*     */ {
/*     */   public static <T> Map<Method, T> selectMethods(Class<?> targetType, final MetadataLookup<T> metadataLookup) {
/*  55 */     final Map<Method, T> methodMap = new LinkedHashMap<Method, T>();
/*  56 */     Set<Class<?>> handlerTypes = new LinkedHashSet<Class<?>>();
/*  57 */     Class<?> specificHandlerType = null;
/*     */     
/*  59 */     if (!Proxy.isProxyClass(targetType)) {
/*  60 */       handlerTypes.add(targetType);
/*  61 */       specificHandlerType = targetType;
/*     */     } 
/*  63 */     handlerTypes.addAll(Arrays.asList(targetType.getInterfaces()));
/*     */     
/*  65 */     for (Class<?> currentHandlerType : handlerTypes) {
/*  66 */       final Class<?> targetClass = (specificHandlerType != null) ? specificHandlerType : currentHandlerType;
/*     */       
/*  68 */       ReflectionUtils.doWithMethods(currentHandlerType, new ReflectionUtils.MethodCallback()
/*     */           {
/*     */             public void doWith(Method method) {
/*  71 */               Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
/*  72 */               T result = metadataLookup.inspect(specificMethod);
/*  73 */               if (result != null) {
/*  74 */                 Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
/*  75 */                 if (bridgedMethod == specificMethod || metadataLookup.inspect(bridgedMethod) == null) {
/*  76 */                   methodMap.put(specificMethod, result);
/*     */                 }
/*     */               } 
/*     */             }
/*     */           }ReflectionUtils.USER_DECLARED_METHODS);
/*     */     } 
/*     */     
/*  83 */     return methodMap;
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
/*     */   public static Set<Method> selectMethods(Class<?> targetType, final ReflectionUtils.MethodFilter methodFilter) {
/*  95 */     return selectMethods(targetType, new MetadataLookup<Boolean>()
/*     */         {
/*     */           public Boolean inspect(Method method) {
/*  98 */             return methodFilter.matches(method) ? Boolean.TRUE : null;
/*     */           }
/* 100 */         }).keySet();
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
/*     */   public static Method selectInvocableMethod(Method method, Class<?> targetType) {
/* 117 */     if (method.getDeclaringClass().isAssignableFrom(targetType)) {
/* 118 */       return method;
/*     */     }
/*     */     try {
/* 121 */       String methodName = method.getName();
/* 122 */       Class<?>[] parameterTypes = method.getParameterTypes();
/* 123 */       for (Class<?> ifc : targetType.getInterfaces()) {
/*     */         try {
/* 125 */           return ifc.getMethod(methodName, parameterTypes);
/*     */         }
/* 127 */         catch (NoSuchMethodException noSuchMethodException) {}
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 132 */       return targetType.getMethod(methodName, parameterTypes);
/*     */     }
/* 134 */     catch (NoSuchMethodException ex) {
/* 135 */       throw new IllegalStateException(String.format("Need to invoke method '%s' declared on target class '%s', but not found in any interface(s) of the exposed proxy type. Either pull the method up to an interface or switch to CGLIB proxies by enforcing proxy-target-class mode in your configuration.", new Object[] { method
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 140 */               .getName(), method.getDeclaringClass().getSimpleName() }));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static interface MetadataLookup<T> {
/*     */     T inspect(Method param1Method);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\MethodIntrospector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */