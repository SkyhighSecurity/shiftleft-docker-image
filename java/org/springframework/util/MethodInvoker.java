/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodInvoker
/*     */ {
/*     */   private Class<?> targetClass;
/*     */   private Object targetObject;
/*     */   private String targetMethod;
/*     */   private String staticMethod;
/*     */   private Object[] arguments;
/*     */   private Method methodObject;
/*     */   
/*     */   public void setTargetClass(Class<?> targetClass) {
/*  61 */     this.targetClass = targetClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getTargetClass() {
/*  68 */     return this.targetClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetObject(Object targetObject) {
/*  79 */     this.targetObject = targetObject;
/*  80 */     if (targetObject != null) {
/*  81 */       this.targetClass = targetObject.getClass();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getTargetObject() {
/*  89 */     return this.targetObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetMethod(String targetMethod) {
/* 100 */     this.targetMethod = targetMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTargetMethod() {
/* 107 */     return this.targetMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStaticMethod(String staticMethod) {
/* 118 */     this.staticMethod = staticMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArguments(Object... arguments) {
/* 126 */     this.arguments = arguments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getArguments() {
/* 133 */     return (this.arguments != null) ? this.arguments : new Object[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() throws ClassNotFoundException, NoSuchMethodException {
/* 144 */     if (this.staticMethod != null) {
/* 145 */       int lastDotIndex = this.staticMethod.lastIndexOf('.');
/* 146 */       if (lastDotIndex == -1 || lastDotIndex == this.staticMethod.length()) {
/* 147 */         throw new IllegalArgumentException("staticMethod must be a fully qualified class plus method name: e.g. 'example.MyExampleClass.myExampleMethod'");
/*     */       }
/*     */ 
/*     */       
/* 151 */       String className = this.staticMethod.substring(0, lastDotIndex);
/* 152 */       String methodName = this.staticMethod.substring(lastDotIndex + 1);
/* 153 */       this.targetClass = resolveClassName(className);
/* 154 */       this.targetMethod = methodName;
/*     */     } 
/*     */     
/* 157 */     Class<?> targetClass = getTargetClass();
/* 158 */     String targetMethod = getTargetMethod();
/* 159 */     Assert.notNull(targetClass, "Either 'targetClass' or 'targetObject' is required");
/* 160 */     Assert.notNull(targetMethod, "Property 'targetMethod' is required");
/*     */     
/* 162 */     Object[] arguments = getArguments();
/* 163 */     Class<?>[] argTypes = new Class[arguments.length];
/* 164 */     for (int i = 0; i < arguments.length; i++) {
/* 165 */       argTypes[i] = (arguments[i] != null) ? arguments[i].getClass() : Object.class;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 170 */       this.methodObject = targetClass.getMethod(targetMethod, argTypes);
/*     */     }
/* 172 */     catch (NoSuchMethodException ex) {
/*     */       
/* 174 */       this.methodObject = findMatchingMethod();
/* 175 */       if (this.methodObject == null) {
/* 176 */         throw ex;
/*     */       }
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
/*     */   protected Class<?> resolveClassName(String className) throws ClassNotFoundException {
/* 190 */     return ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Method findMatchingMethod() {
/* 201 */     String targetMethod = getTargetMethod();
/* 202 */     Object[] arguments = getArguments();
/* 203 */     int argCount = arguments.length;
/*     */     
/* 205 */     Method[] candidates = ReflectionUtils.getAllDeclaredMethods(getTargetClass());
/* 206 */     int minTypeDiffWeight = Integer.MAX_VALUE;
/* 207 */     Method matchingMethod = null;
/*     */     
/* 209 */     for (Method candidate : candidates) {
/* 210 */       if (candidate.getName().equals(targetMethod)) {
/* 211 */         Class<?>[] paramTypes = candidate.getParameterTypes();
/* 212 */         if (paramTypes.length == argCount) {
/* 213 */           int typeDiffWeight = getTypeDifferenceWeight(paramTypes, arguments);
/* 214 */           if (typeDiffWeight < minTypeDiffWeight) {
/* 215 */             minTypeDiffWeight = typeDiffWeight;
/* 216 */             matchingMethod = candidate;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 222 */     return matchingMethod;
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
/*     */   public Method getPreparedMethod() throws IllegalStateException {
/* 234 */     if (this.methodObject == null) {
/* 235 */       throw new IllegalStateException("prepare() must be called prior to invoke() on MethodInvoker");
/*     */     }
/* 237 */     return this.methodObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPrepared() {
/* 245 */     return (this.methodObject != null);
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
/*     */   public Object invoke() throws InvocationTargetException, IllegalAccessException {
/* 259 */     Object targetObject = getTargetObject();
/* 260 */     Method preparedMethod = getPreparedMethod();
/* 261 */     if (targetObject == null && !Modifier.isStatic(preparedMethod.getModifiers())) {
/* 262 */       throw new IllegalArgumentException("Target method must not be non-static without a target");
/*     */     }
/* 264 */     ReflectionUtils.makeAccessible(preparedMethod);
/* 265 */     return preparedMethod.invoke(targetObject, getArguments());
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
/*     */   public static int getTypeDifferenceWeight(Class<?>[] paramTypes, Object[] args) {
/* 290 */     int result = 0;
/* 291 */     for (int i = 0; i < paramTypes.length; i++) {
/* 292 */       if (!ClassUtils.isAssignableValue(paramTypes[i], args[i])) {
/* 293 */         return Integer.MAX_VALUE;
/*     */       }
/* 295 */       if (args[i] != null) {
/* 296 */         Class<?> paramType = paramTypes[i];
/* 297 */         Class<?> superClass = args[i].getClass().getSuperclass();
/* 298 */         while (superClass != null) {
/* 299 */           if (paramType.equals(superClass)) {
/* 300 */             result += 2;
/* 301 */             superClass = null; continue;
/*     */           } 
/* 303 */           if (ClassUtils.isAssignable(paramType, superClass)) {
/* 304 */             result += 2;
/* 305 */             superClass = superClass.getSuperclass();
/*     */             continue;
/*     */           } 
/* 308 */           superClass = null;
/*     */         } 
/*     */         
/* 311 */         if (paramType.isInterface()) {
/* 312 */           result++;
/*     */         }
/*     */       } 
/*     */     } 
/* 316 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\MethodInvoker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */