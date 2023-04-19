/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BridgeMethodResolver
/*     */ {
/*     */   public static Method findBridgedMethod(Method bridgeMethod) {
/*  59 */     if (bridgeMethod == null || !bridgeMethod.isBridge()) {
/*  60 */       return bridgeMethod;
/*     */     }
/*     */ 
/*     */     
/*  64 */     List<Method> candidateMethods = new ArrayList<Method>();
/*  65 */     Method[] methods = ReflectionUtils.getAllDeclaredMethods(bridgeMethod.getDeclaringClass());
/*  66 */     for (Method candidateMethod : methods) {
/*  67 */       if (isBridgedCandidateFor(candidateMethod, bridgeMethod)) {
/*  68 */         candidateMethods.add(candidateMethod);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  73 */     if (candidateMethods.size() == 1) {
/*  74 */       return candidateMethods.get(0);
/*     */     }
/*     */ 
/*     */     
/*  78 */     Method bridgedMethod = searchCandidates(candidateMethods, bridgeMethod);
/*  79 */     if (bridgedMethod != null)
/*     */     {
/*  81 */       return bridgedMethod;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  86 */     return bridgeMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isBridgedCandidateFor(Method candidateMethod, Method bridgeMethod) {
/*  97 */     return (!candidateMethod.isBridge() && !candidateMethod.equals(bridgeMethod) && candidateMethod
/*  98 */       .getName().equals(bridgeMethod.getName()) && (candidateMethod
/*  99 */       .getParameterTypes()).length == (bridgeMethod.getParameterTypes()).length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Method searchCandidates(List<Method> candidateMethods, Method bridgeMethod) {
/* 109 */     if (candidateMethods.isEmpty()) {
/* 110 */       return null;
/*     */     }
/* 112 */     Method previousMethod = null;
/* 113 */     boolean sameSig = true;
/* 114 */     for (Method candidateMethod : candidateMethods) {
/* 115 */       if (isBridgeMethodFor(bridgeMethod, candidateMethod, bridgeMethod.getDeclaringClass())) {
/* 116 */         return candidateMethod;
/*     */       }
/* 118 */       if (previousMethod != null)
/*     */       {
/* 120 */         sameSig = (sameSig && Arrays.equals((Object[])candidateMethod.getGenericParameterTypes(), (Object[])previousMethod.getGenericParameterTypes()));
/*     */       }
/* 122 */       previousMethod = candidateMethod;
/*     */     } 
/* 124 */     return sameSig ? candidateMethods.get(0) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isBridgeMethodFor(Method bridgeMethod, Method candidateMethod, Class<?> declaringClass) {
/* 132 */     if (isResolvedTypeMatch(candidateMethod, bridgeMethod, declaringClass)) {
/* 133 */       return true;
/*     */     }
/* 135 */     Method method = findGenericDeclaration(bridgeMethod);
/* 136 */     return (method != null && isResolvedTypeMatch(method, candidateMethod, declaringClass));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isResolvedTypeMatch(Method genericMethod, Method candidateMethod, Class<?> declaringClass) {
/* 146 */     Type[] genericParameters = genericMethod.getGenericParameterTypes();
/* 147 */     Class<?>[] candidateParameters = candidateMethod.getParameterTypes();
/* 148 */     if (genericParameters.length != candidateParameters.length) {
/* 149 */       return false;
/*     */     }
/* 151 */     for (int i = 0; i < candidateParameters.length; i++) {
/* 152 */       ResolvableType genericParameter = ResolvableType.forMethodParameter(genericMethod, i, declaringClass);
/* 153 */       Class<?> candidateParameter = candidateParameters[i];
/* 154 */       if (candidateParameter.isArray())
/*     */       {
/* 156 */         if (!candidateParameter.getComponentType().equals(genericParameter.getComponentType().resolve(Object.class))) {
/* 157 */           return false;
/*     */         }
/*     */       }
/*     */       
/* 161 */       if (!candidateParameter.equals(genericParameter.resolve(Object.class))) {
/* 162 */         return false;
/*     */       }
/*     */     } 
/* 165 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Method findGenericDeclaration(Method bridgeMethod) {
/* 175 */     Class<?> superclass = bridgeMethod.getDeclaringClass().getSuperclass();
/* 176 */     while (superclass != null && Object.class != superclass) {
/* 177 */       Method method = searchForMatch(superclass, bridgeMethod);
/* 178 */       if (method != null && !method.isBridge()) {
/* 179 */         return method;
/*     */       }
/* 181 */       superclass = superclass.getSuperclass();
/*     */     } 
/*     */     
/* 184 */     Class<?>[] interfaces = ClassUtils.getAllInterfacesForClass(bridgeMethod.getDeclaringClass());
/* 185 */     return searchInterfaces(interfaces, bridgeMethod);
/*     */   }
/*     */   
/*     */   private static Method searchInterfaces(Class<?>[] interfaces, Method bridgeMethod) {
/* 189 */     for (Class<?> ifc : interfaces) {
/* 190 */       Method method = searchForMatch(ifc, bridgeMethod);
/* 191 */       if (method != null && !method.isBridge()) {
/* 192 */         return method;
/*     */       }
/*     */       
/* 195 */       method = searchInterfaces(ifc.getInterfaces(), bridgeMethod);
/* 196 */       if (method != null) {
/* 197 */         return method;
/*     */       }
/*     */     } 
/*     */     
/* 201 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Method searchForMatch(Class<?> type, Method bridgeMethod) {
/*     */     try {
/* 211 */       return type.getDeclaredMethod(bridgeMethod.getName(), bridgeMethod.getParameterTypes());
/*     */     }
/* 213 */     catch (NoSuchMethodException ex) {
/* 214 */       return null;
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
/*     */   public static boolean isVisibilityBridgeMethodPair(Method bridgeMethod, Method bridgedMethod) {
/* 226 */     if (bridgeMethod == bridgedMethod) {
/* 227 */       return true;
/*     */     }
/* 229 */     return (Arrays.equals((Object[])bridgeMethod.getParameterTypes(), (Object[])bridgedMethod.getParameterTypes()) && bridgeMethod
/* 230 */       .getReturnType().equals(bridgedMethod.getReturnType()));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\BridgeMethodResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */