/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.ObjectFactory;
/*     */ import org.springframework.beans.factory.config.TypedStringValue;
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
/*     */ abstract class AutowireUtils
/*     */ {
/*     */   public static void sortConstructors(Constructor<?>[] constructors) {
/*  60 */     Arrays.sort(constructors, new Comparator<Constructor<?>>()
/*     */         {
/*     */           public int compare(Constructor<?> c1, Constructor<?> c2) {
/*  63 */             boolean p1 = Modifier.isPublic(c1.getModifiers());
/*  64 */             boolean p2 = Modifier.isPublic(c2.getModifiers());
/*  65 */             if (p1 != p2) {
/*  66 */               return p1 ? -1 : 1;
/*     */             }
/*  68 */             int c1pl = (c1.getParameterTypes()).length;
/*  69 */             int c2pl = (c2.getParameterTypes()).length;
/*  70 */             return (c1pl < c2pl) ? 1 : ((c1pl > c2pl) ? -1 : 0);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortFactoryMethods(Method[] factoryMethods) {
/*  83 */     Arrays.sort(factoryMethods, new Comparator<Method>()
/*     */         {
/*     */           public int compare(Method fm1, Method fm2) {
/*  86 */             boolean p1 = Modifier.isPublic(fm1.getModifiers());
/*  87 */             boolean p2 = Modifier.isPublic(fm2.getModifiers());
/*  88 */             if (p1 != p2) {
/*  89 */               return p1 ? -1 : 1;
/*     */             }
/*  91 */             int c1pl = (fm1.getParameterTypes()).length;
/*  92 */             int c2pl = (fm2.getParameterTypes()).length;
/*  93 */             return (c1pl < c2pl) ? 1 : ((c1pl > c2pl) ? -1 : 0);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isExcludedFromDependencyCheck(PropertyDescriptor pd) {
/* 105 */     Method wm = pd.getWriteMethod();
/* 106 */     if (wm == null) {
/* 107 */       return false;
/*     */     }
/* 109 */     if (!wm.getDeclaringClass().getName().contains("$$"))
/*     */     {
/* 111 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 115 */     Class<?> superclass = wm.getDeclaringClass().getSuperclass();
/* 116 */     return !ClassUtils.hasMethod(superclass, wm.getName(), wm.getParameterTypes());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSetterDefinedInInterface(PropertyDescriptor pd, Set<Class<?>> interfaces) {
/* 127 */     Method setter = pd.getWriteMethod();
/* 128 */     if (setter != null) {
/* 129 */       Class<?> targetClass = setter.getDeclaringClass();
/* 130 */       for (Class<?> ifc : interfaces) {
/* 131 */         if (ifc.isAssignableFrom(targetClass) && 
/* 132 */           ClassUtils.hasMethod(ifc, setter.getName(), setter.getParameterTypes())) {
/* 133 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 137 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object resolveAutowiringValue(Object autowiringValue, Class<?> requiredType) {
/* 148 */     if (autowiringValue instanceof ObjectFactory && !requiredType.isInstance(autowiringValue)) {
/* 149 */       ObjectFactory<?> factory = (ObjectFactory)autowiringValue;
/* 150 */       if (autowiringValue instanceof Serializable && requiredType.isInterface()) {
/* 151 */         autowiringValue = Proxy.newProxyInstance(requiredType.getClassLoader(), new Class[] { requiredType }, new ObjectFactoryDelegatingInvocationHandler(factory));
/*     */       }
/*     */       else {
/*     */         
/* 155 */         return factory.getObject();
/*     */       } 
/*     */     } 
/* 158 */     return autowiringValue;
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
/*     */   public static Class<?> resolveReturnTypeForFactoryMethod(Method method, Object[] args, ClassLoader classLoader) {
/* 193 */     Assert.notNull(method, "Method must not be null");
/* 194 */     Assert.notNull(args, "Argument array must not be null");
/* 195 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/*     */     
/* 197 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])method.getTypeParameters();
/* 198 */     Type genericReturnType = method.getGenericReturnType();
/* 199 */     Type[] methodParameterTypes = method.getGenericParameterTypes();
/* 200 */     Assert.isTrue((args.length == methodParameterTypes.length), "Argument array does not match parameter count");
/*     */ 
/*     */ 
/*     */     
/* 204 */     boolean locallyDeclaredTypeVariableMatchesReturnType = false;
/* 205 */     for (TypeVariable<Method> currentTypeVariable : arrayOfTypeVariable) {
/* 206 */       if (currentTypeVariable.equals(genericReturnType)) {
/* 207 */         locallyDeclaredTypeVariableMatchesReturnType = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 212 */     if (locallyDeclaredTypeVariableMatchesReturnType) {
/* 213 */       for (int i = 0; i < methodParameterTypes.length; i++) {
/* 214 */         Type methodParameterType = methodParameterTypes[i];
/* 215 */         Object arg = args[i];
/* 216 */         if (methodParameterType.equals(genericReturnType)) {
/* 217 */           if (arg instanceof TypedStringValue) {
/* 218 */             TypedStringValue typedValue = (TypedStringValue)arg;
/* 219 */             if (typedValue.hasTargetType()) {
/* 220 */               return typedValue.getTargetType();
/*     */             }
/*     */             try {
/* 223 */               return typedValue.resolveTargetType(classLoader);
/*     */             }
/* 225 */             catch (ClassNotFoundException ex) {
/* 226 */               throw new IllegalStateException("Failed to resolve value type [" + typedValue
/* 227 */                   .getTargetTypeName() + "] for factory method argument", ex);
/*     */             } 
/*     */           } 
/*     */           
/* 231 */           if (arg != null && !(arg instanceof org.springframework.beans.BeanMetadataElement)) {
/* 232 */             return arg.getClass();
/*     */           }
/* 234 */           return method.getReturnType();
/*     */         } 
/* 236 */         if (methodParameterType instanceof ParameterizedType) {
/* 237 */           ParameterizedType parameterizedType = (ParameterizedType)methodParameterType;
/* 238 */           Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
/* 239 */           for (Type typeArg : actualTypeArguments) {
/* 240 */             if (typeArg.equals(genericReturnType)) {
/* 241 */               if (arg instanceof Class) {
/* 242 */                 return (Class)arg;
/*     */               }
/*     */               
/* 245 */               String className = null;
/* 246 */               if (arg instanceof String) {
/* 247 */                 className = (String)arg;
/*     */               }
/* 249 */               else if (arg instanceof TypedStringValue) {
/* 250 */                 TypedStringValue typedValue = (TypedStringValue)arg;
/* 251 */                 String targetTypeName = typedValue.getTargetTypeName();
/* 252 */                 if (targetTypeName == null || Class.class.getName().equals(targetTypeName)) {
/* 253 */                   className = typedValue.getValue();
/*     */                 }
/*     */               } 
/* 256 */               if (className != null) {
/*     */                 try {
/* 258 */                   return ClassUtils.forName(className, classLoader);
/*     */                 }
/* 260 */                 catch (ClassNotFoundException ex) {
/* 261 */                   throw new IllegalStateException("Could not resolve class name [" + arg + "] for factory method argument", ex);
/*     */                 } 
/*     */               }
/*     */ 
/*     */ 
/*     */               
/* 267 */               return method.getReturnType();
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 276 */     return method.getReturnType();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ObjectFactoryDelegatingInvocationHandler
/*     */     implements InvocationHandler, Serializable
/*     */   {
/*     */     private final ObjectFactory<?> objectFactory;
/*     */ 
/*     */ 
/*     */     
/*     */     public ObjectFactoryDelegatingInvocationHandler(ObjectFactory<?> objectFactory) {
/* 289 */       this.objectFactory = objectFactory;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 294 */       String methodName = method.getName();
/* 295 */       if (methodName.equals("equals"))
/*     */       {
/* 297 */         return Boolean.valueOf((proxy == args[0]));
/*     */       }
/* 299 */       if (methodName.equals("hashCode"))
/*     */       {
/* 301 */         return Integer.valueOf(System.identityHashCode(proxy));
/*     */       }
/* 303 */       if (methodName.equals("toString")) {
/* 304 */         return this.objectFactory.toString();
/*     */       }
/*     */       try {
/* 307 */         return method.invoke(this.objectFactory.getObject(), args);
/*     */       }
/* 309 */       catch (InvocationTargetException ex) {
/* 310 */         throw ex.getTargetException();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\AutowireUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */