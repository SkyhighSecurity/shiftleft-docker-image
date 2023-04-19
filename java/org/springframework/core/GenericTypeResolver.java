/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class GenericTypeResolver
/*     */ {
/*  47 */   private static final Map<Class<?>, Map<TypeVariable, Type>> typeVariableCache = (Map<Class<?>, Map<TypeVariable, Type>>)new ConcurrentReferenceHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static Type getTargetType(MethodParameter methodParameter) {
/*  59 */     Assert.notNull(methodParameter, "MethodParameter must not be null");
/*  60 */     return methodParameter.getGenericParameterType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> resolveParameterType(MethodParameter methodParameter, Class<?> implementationClass) {
/*  70 */     Assert.notNull(methodParameter, "MethodParameter must not be null");
/*  71 */     Assert.notNull(implementationClass, "Class must not be null");
/*  72 */     methodParameter.setContainingClass(implementationClass);
/*  73 */     ResolvableType.resolveMethodParameter(methodParameter);
/*  74 */     return methodParameter.getParameterType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> resolveReturnType(Method method, Class<?> clazz) {
/*  85 */     Assert.notNull(method, "Method must not be null");
/*  86 */     Assert.notNull(clazz, "Class must not be null");
/*  87 */     return ResolvableType.forMethodReturnType(method, clazz).resolve(method.getReturnType());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static Class<?> resolveReturnTypeForGenericMethod(Method method, Object[] args, ClassLoader classLoader) {
/* 124 */     Assert.notNull(method, "Method must not be null");
/* 125 */     Assert.notNull(args, "Argument array must not be null");
/*     */     
/* 127 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])method.getTypeParameters();
/* 128 */     Type genericReturnType = method.getGenericReturnType();
/* 129 */     Type[] methodArgumentTypes = method.getGenericParameterTypes();
/*     */ 
/*     */     
/* 132 */     if (arrayOfTypeVariable.length == 0) {
/* 133 */       return method.getReturnType();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 138 */     if (args.length < methodArgumentTypes.length) {
/* 139 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 144 */     boolean locallyDeclaredTypeVariableMatchesReturnType = false;
/* 145 */     for (TypeVariable<Method> currentTypeVariable : arrayOfTypeVariable) {
/* 146 */       if (currentTypeVariable.equals(genericReturnType)) {
/* 147 */         locallyDeclaredTypeVariableMatchesReturnType = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 152 */     if (locallyDeclaredTypeVariableMatchesReturnType) {
/* 153 */       for (int i = 0; i < methodArgumentTypes.length; i++) {
/* 154 */         Type currentMethodArgumentType = methodArgumentTypes[i];
/* 155 */         if (currentMethodArgumentType.equals(genericReturnType)) {
/* 156 */           return args[i].getClass();
/*     */         }
/* 158 */         if (currentMethodArgumentType instanceof ParameterizedType) {
/* 159 */           ParameterizedType parameterizedType = (ParameterizedType)currentMethodArgumentType;
/* 160 */           Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
/* 161 */           for (Type typeArg : actualTypeArguments) {
/* 162 */             if (typeArg.equals(genericReturnType)) {
/* 163 */               Object arg = args[i];
/* 164 */               if (arg instanceof Class) {
/* 165 */                 return (Class)arg;
/*     */               }
/* 167 */               if (arg instanceof String && classLoader != null) {
/*     */                 try {
/* 169 */                   return classLoader.loadClass((String)arg);
/*     */                 }
/* 171 */                 catch (ClassNotFoundException ex) {
/* 172 */                   throw new IllegalStateException("Could not resolve specific class name argument [" + arg + "]", ex);
/*     */                 } 
/*     */               }
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 179 */               return method.getReturnType();
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 188 */     return method.getReturnType();
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
/*     */   public static Class<?> resolveReturnTypeArgument(Method method, Class<?> genericIfc) {
/* 201 */     Assert.notNull(method, "Method must not be null");
/* 202 */     ResolvableType resolvableType = ResolvableType.forMethodReturnType(method).as(genericIfc);
/* 203 */     if (!resolvableType.hasGenerics() || resolvableType.getType() instanceof java.lang.reflect.WildcardType) {
/* 204 */       return null;
/*     */     }
/* 206 */     return getSingleGeneric(resolvableType);
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
/*     */   public static Class<?> resolveTypeArgument(Class<?> clazz, Class<?> genericIfc) {
/* 218 */     ResolvableType resolvableType = ResolvableType.forClass(clazz).as(genericIfc);
/* 219 */     if (!resolvableType.hasGenerics()) {
/* 220 */       return null;
/*     */     }
/* 222 */     return getSingleGeneric(resolvableType);
/*     */   }
/*     */   
/*     */   private static Class<?> getSingleGeneric(ResolvableType resolvableType) {
/* 226 */     if ((resolvableType.getGenerics()).length > 1) {
/* 227 */       throw new IllegalArgumentException("Expected 1 type argument on generic interface [" + resolvableType + "] but found " + (resolvableType
/* 228 */           .getGenerics()).length);
/*     */     }
/* 230 */     return resolvableType.getGeneric(new int[0]).resolve();
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
/*     */   public static Class<?>[] resolveTypeArguments(Class<?> clazz, Class<?> genericIfc) {
/* 244 */     ResolvableType type = ResolvableType.forClass(clazz).as(genericIfc);
/* 245 */     if (!type.hasGenerics() || type.isEntirelyUnresolvable()) {
/* 246 */       return null;
/*     */     }
/* 248 */     return type.resolveGenerics(Object.class);
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
/*     */   public static Class<?> resolveType(Type genericType, Map<TypeVariable, Type> map) {
/* 260 */     return ResolvableType.forType(genericType, new TypeVariableMapVariableResolver(map)).resolve(Object.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<TypeVariable, Type> getTypeVariableMap(Class<?> clazz) {
/* 271 */     Map<TypeVariable, Type> typeVariableMap = typeVariableCache.get(clazz);
/* 272 */     if (typeVariableMap == null) {
/* 273 */       typeVariableMap = new HashMap<TypeVariable, Type>();
/* 274 */       buildTypeVariableMap(ResolvableType.forClass(clazz), typeVariableMap);
/* 275 */       typeVariableCache.put(clazz, Collections.unmodifiableMap(typeVariableMap));
/*     */     } 
/* 277 */     return typeVariableMap;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void buildTypeVariableMap(ResolvableType type, Map<TypeVariable, Type> typeVariableMap) {
/* 282 */     if (type != ResolvableType.NONE) {
/* 283 */       if (type.getType() instanceof ParameterizedType) {
/* 284 */         TypeVariable[] arrayOfTypeVariable = (TypeVariable[])type.resolve().getTypeParameters();
/* 285 */         for (int i = 0; i < arrayOfTypeVariable.length; i++) {
/* 286 */           ResolvableType generic = type.getGeneric(new int[] { i });
/* 287 */           while (generic.getType() instanceof TypeVariable) {
/* 288 */             generic = generic.resolveType();
/*     */           }
/* 290 */           if (generic != ResolvableType.NONE) {
/* 291 */             typeVariableMap.put(arrayOfTypeVariable[i], generic.getType());
/*     */           }
/*     */         } 
/*     */       } 
/* 295 */       buildTypeVariableMap(type.getSuperType(), typeVariableMap);
/* 296 */       for (ResolvableType interfaceType : type.getInterfaces()) {
/* 297 */         buildTypeVariableMap(interfaceType, typeVariableMap);
/*     */       }
/* 299 */       if (type.resolve().isMemberClass()) {
/* 300 */         buildTypeVariableMap(ResolvableType.forClass(type.resolve().getEnclosingClass()), typeVariableMap);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static class TypeVariableMapVariableResolver
/*     */     implements ResolvableType.VariableResolver
/*     */   {
/*     */     private final Map<TypeVariable, Type> typeVariableMap;
/*     */     
/*     */     public TypeVariableMapVariableResolver(Map<TypeVariable, Type> typeVariableMap) {
/* 312 */       this.typeVariableMap = typeVariableMap;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResolvableType resolveVariable(TypeVariable<?> variable) {
/* 317 */       Type type = this.typeVariableMap.get(variable);
/* 318 */       return (type != null) ? ResolvableType.forType(type) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getSource() {
/* 323 */       return this.typeVariableMap;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\GenericTypeResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */