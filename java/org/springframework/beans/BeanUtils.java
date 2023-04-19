/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.beans.PropertyEditor;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BeanUtils
/*     */ {
/*  58 */   private static final Log logger = LogFactory.getLog(BeanUtils.class);
/*     */ 
/*     */   
/*  61 */   private static final Set<Class<?>> unknownEditorTypes = Collections.newSetFromMap((Map<Class<?>, Boolean>)new ConcurrentReferenceHashMap(64));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T instantiate(Class<T> clazz) throws BeanInstantiationException {
/*  72 */     Assert.notNull(clazz, "Class must not be null");
/*  73 */     if (clazz.isInterface()) {
/*  74 */       throw new BeanInstantiationException(clazz, "Specified class is an interface");
/*     */     }
/*     */     try {
/*  77 */       return clazz.newInstance();
/*     */     }
/*  79 */     catch (InstantiationException ex) {
/*  80 */       throw new BeanInstantiationException(clazz, "Is it an abstract class?", ex);
/*     */     }
/*  82 */     catch (IllegalAccessException ex) {
/*  83 */       throw new BeanInstantiationException(clazz, "Is the constructor accessible?", ex);
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
/*     */   public static <T> T instantiateClass(Class<T> clazz) throws BeanInstantiationException {
/*  97 */     Assert.notNull(clazz, "Class must not be null");
/*  98 */     if (clazz.isInterface()) {
/*  99 */       throw new BeanInstantiationException(clazz, "Specified class is an interface");
/*     */     }
/*     */     try {
/* 102 */       return instantiateClass(clazz.getDeclaredConstructor(new Class[0]), new Object[0]);
/*     */     }
/* 104 */     catch (NoSuchMethodException ex) {
/* 105 */       throw new BeanInstantiationException(clazz, "No default constructor found", ex);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T instantiateClass(Class<?> clazz, Class<T> assignableTo) throws BeanInstantiationException {
/* 124 */     Assert.isAssignable(assignableTo, clazz);
/* 125 */     return instantiateClass((Class)clazz);
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
/*     */   public static <T> T instantiateClass(Constructor<T> ctor, Object... args) throws BeanInstantiationException {
/* 139 */     Assert.notNull(ctor, "Constructor must not be null");
/*     */     try {
/* 141 */       ReflectionUtils.makeAccessible(ctor);
/* 142 */       return ctor.newInstance(args);
/*     */     }
/* 144 */     catch (InstantiationException ex) {
/* 145 */       throw new BeanInstantiationException(ctor, "Is it an abstract class?", ex);
/*     */     }
/* 147 */     catch (IllegalAccessException ex) {
/* 148 */       throw new BeanInstantiationException(ctor, "Is the constructor accessible?", ex);
/*     */     }
/* 150 */     catch (IllegalArgumentException ex) {
/* 151 */       throw new BeanInstantiationException(ctor, "Illegal arguments for constructor", ex);
/*     */     }
/* 153 */     catch (InvocationTargetException ex) {
/* 154 */       throw new BeanInstantiationException(ctor, "Constructor threw exception", ex.getTargetException());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method findMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
/*     */     try {
/* 174 */       return clazz.getMethod(methodName, paramTypes);
/*     */     }
/* 176 */     catch (NoSuchMethodException ex) {
/* 177 */       return findDeclaredMethod(clazz, methodName, paramTypes);
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
/*     */ 
/*     */   
/*     */   public static Method findDeclaredMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
/*     */     try {
/* 194 */       return clazz.getDeclaredMethod(methodName, paramTypes);
/*     */     }
/* 196 */     catch (NoSuchMethodException ex) {
/* 197 */       if (clazz.getSuperclass() != null) {
/* 198 */         return findDeclaredMethod(clazz.getSuperclass(), methodName, paramTypes);
/*     */       }
/* 200 */       return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method findMethodWithMinimalParameters(Class<?> clazz, String methodName) throws IllegalArgumentException {
/* 222 */     Method targetMethod = findMethodWithMinimalParameters(clazz.getMethods(), methodName);
/* 223 */     if (targetMethod == null) {
/* 224 */       targetMethod = findDeclaredMethodWithMinimalParameters(clazz, methodName);
/*     */     }
/* 226 */     return targetMethod;
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
/*     */   public static Method findDeclaredMethodWithMinimalParameters(Class<?> clazz, String methodName) throws IllegalArgumentException {
/* 244 */     Method targetMethod = findMethodWithMinimalParameters(clazz.getDeclaredMethods(), methodName);
/* 245 */     if (targetMethod == null && clazz.getSuperclass() != null) {
/* 246 */       targetMethod = findDeclaredMethodWithMinimalParameters(clazz.getSuperclass(), methodName);
/*     */     }
/* 248 */     return targetMethod;
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
/*     */   public static Method findMethodWithMinimalParameters(Method[] methods, String methodName) throws IllegalArgumentException {
/* 263 */     Method targetMethod = null;
/* 264 */     int numMethodsFoundWithCurrentMinimumArgs = 0;
/* 265 */     for (Method method : methods) {
/* 266 */       if (method.getName().equals(methodName)) {
/* 267 */         int numParams = (method.getParameterTypes()).length;
/* 268 */         if (targetMethod == null || numParams < (targetMethod.getParameterTypes()).length) {
/* 269 */           targetMethod = method;
/* 270 */           numMethodsFoundWithCurrentMinimumArgs = 1;
/*     */         }
/* 272 */         else if (!method.isBridge() && (targetMethod.getParameterTypes()).length == numParams) {
/* 273 */           if (targetMethod.isBridge()) {
/*     */             
/* 275 */             targetMethod = method;
/*     */           }
/*     */           else {
/*     */             
/* 279 */             numMethodsFoundWithCurrentMinimumArgs++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 284 */     if (numMethodsFoundWithCurrentMinimumArgs > 1) {
/* 285 */       throw new IllegalArgumentException("Cannot resolve method '" + methodName + "' to a unique method. Attempted to resolve to overloaded method with the least number of parameters but there were " + numMethodsFoundWithCurrentMinimumArgs + " candidates.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 290 */     return targetMethod;
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
/*     */   public static Method resolveSignature(String signature, Class<?> clazz) {
/* 312 */     Assert.hasText(signature, "'signature' must not be empty");
/* 313 */     Assert.notNull(clazz, "Class must not be null");
/* 314 */     int startParen = signature.indexOf('(');
/* 315 */     int endParen = signature.indexOf(')');
/* 316 */     if (startParen > -1 && endParen == -1) {
/* 317 */       throw new IllegalArgumentException("Invalid method signature '" + signature + "': expected closing ')' for args list");
/*     */     }
/*     */     
/* 320 */     if (startParen == -1 && endParen > -1) {
/* 321 */       throw new IllegalArgumentException("Invalid method signature '" + signature + "': expected opening '(' for args list");
/*     */     }
/*     */     
/* 324 */     if (startParen == -1 && endParen == -1) {
/* 325 */       return findMethodWithMinimalParameters(clazz, signature);
/*     */     }
/*     */     
/* 328 */     String methodName = signature.substring(0, startParen);
/*     */     
/* 330 */     String[] parameterTypeNames = StringUtils.commaDelimitedListToStringArray(signature.substring(startParen + 1, endParen));
/* 331 */     Class<?>[] parameterTypes = new Class[parameterTypeNames.length];
/* 332 */     for (int i = 0; i < parameterTypeNames.length; i++) {
/* 333 */       String parameterTypeName = parameterTypeNames[i].trim();
/*     */       try {
/* 335 */         parameterTypes[i] = ClassUtils.forName(parameterTypeName, clazz.getClassLoader());
/*     */       }
/* 337 */       catch (Throwable ex) {
/* 338 */         throw new IllegalArgumentException("Invalid method signature: unable to resolve type [" + parameterTypeName + "] for argument " + i + ". Root cause: " + ex);
/*     */       } 
/*     */     } 
/*     */     
/* 342 */     return findMethod(clazz, methodName, parameterTypes);
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
/*     */   public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) throws BeansException {
/* 354 */     CachedIntrospectionResults cr = CachedIntrospectionResults.forClass(clazz);
/* 355 */     return cr.getPropertyDescriptors();
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
/*     */   public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String propertyName) throws BeansException {
/* 368 */     CachedIntrospectionResults cr = CachedIntrospectionResults.forClass(clazz);
/* 369 */     return cr.getPropertyDescriptor(propertyName);
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
/*     */   public static PropertyDescriptor findPropertyForMethod(Method method) throws BeansException {
/* 382 */     return findPropertyForMethod(method, method.getDeclaringClass());
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
/*     */   public static PropertyDescriptor findPropertyForMethod(Method method, Class<?> clazz) throws BeansException {
/* 396 */     Assert.notNull(method, "Method must not be null");
/* 397 */     PropertyDescriptor[] pds = getPropertyDescriptors(clazz);
/* 398 */     for (PropertyDescriptor pd : pds) {
/* 399 */       if (method.equals(pd.getReadMethod()) || method.equals(pd.getWriteMethod())) {
/* 400 */         return pd;
/*     */       }
/*     */     } 
/* 403 */     return null;
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
/*     */   public static PropertyEditor findEditorByConvention(Class<?> targetType) {
/* 416 */     if (targetType == null || targetType.isArray() || unknownEditorTypes.contains(targetType)) {
/* 417 */       return null;
/*     */     }
/* 419 */     ClassLoader cl = targetType.getClassLoader();
/* 420 */     if (cl == null) {
/*     */       try {
/* 422 */         cl = ClassLoader.getSystemClassLoader();
/* 423 */         if (cl == null) {
/* 424 */           return null;
/*     */         }
/*     */       }
/* 427 */       catch (Throwable ex) {
/*     */         
/* 429 */         if (logger.isDebugEnabled()) {
/* 430 */           logger.debug("Could not access system ClassLoader: " + ex);
/*     */         }
/* 432 */         return null;
/*     */       } 
/*     */     }
/* 435 */     String editorName = targetType.getName() + "Editor";
/*     */     try {
/* 437 */       Class<?> editorClass = cl.loadClass(editorName);
/* 438 */       if (!PropertyEditor.class.isAssignableFrom(editorClass)) {
/* 439 */         if (logger.isWarnEnabled()) {
/* 440 */           logger.warn("Editor class [" + editorName + "] does not implement [java.beans.PropertyEditor] interface");
/*     */         }
/*     */         
/* 443 */         unknownEditorTypes.add(targetType);
/* 444 */         return null;
/*     */       } 
/* 446 */       return (PropertyEditor)instantiateClass(editorClass);
/*     */     }
/* 448 */     catch (ClassNotFoundException ex) {
/* 449 */       if (logger.isDebugEnabled()) {
/* 450 */         logger.debug("No property editor [" + editorName + "] found for type " + targetType
/* 451 */             .getName() + " according to 'Editor' suffix convention");
/*     */       }
/* 453 */       unknownEditorTypes.add(targetType);
/* 454 */       return null;
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
/*     */   public static Class<?> findPropertyType(String propertyName, Class<?>... beanClasses) {
/* 466 */     if (beanClasses != null) {
/* 467 */       for (Class<?> beanClass : beanClasses) {
/* 468 */         PropertyDescriptor pd = getPropertyDescriptor(beanClass, propertyName);
/* 469 */         if (pd != null) {
/* 470 */           return pd.getPropertyType();
/*     */         }
/*     */       } 
/*     */     }
/* 474 */     return Object.class;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MethodParameter getWriteMethodParameter(PropertyDescriptor pd) {
/* 484 */     if (pd instanceof GenericTypeAwarePropertyDescriptor) {
/* 485 */       return new MethodParameter(((GenericTypeAwarePropertyDescriptor)pd).getWriteMethodParameter());
/*     */     }
/*     */     
/* 488 */     return new MethodParameter(pd.getWriteMethod(), 0);
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
/*     */   public static boolean isSimpleProperty(Class<?> clazz) {
/* 503 */     Assert.notNull(clazz, "Class must not be null");
/* 504 */     return (isSimpleValueType(clazz) || (clazz.isArray() && isSimpleValueType(clazz.getComponentType())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSimpleValueType(Class<?> clazz) {
/* 515 */     return (ClassUtils.isPrimitiveOrWrapper(clazz) || Enum.class
/* 516 */       .isAssignableFrom(clazz) || CharSequence.class
/* 517 */       .isAssignableFrom(clazz) || Number.class
/* 518 */       .isAssignableFrom(clazz) || Date.class
/* 519 */       .isAssignableFrom(clazz) || URI.class == clazz || URL.class == clazz || Locale.class == clazz || Class.class == clazz);
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
/*     */   public static void copyProperties(Object source, Object target) throws BeansException {
/* 538 */     copyProperties(source, target, null, (String[])null);
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
/*     */   public static void copyProperties(Object source, Object target, Class<?> editable) throws BeansException {
/* 556 */     copyProperties(source, target, editable, (String[])null);
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
/*     */   public static void copyProperties(Object source, Object target, String... ignoreProperties) throws BeansException {
/* 574 */     copyProperties(source, target, null, ignoreProperties);
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
/*     */   private static void copyProperties(Object source, Object target, Class<?> editable, String... ignoreProperties) throws BeansException {
/* 592 */     Assert.notNull(source, "Source must not be null");
/* 593 */     Assert.notNull(target, "Target must not be null");
/*     */     
/* 595 */     Class<?> actualEditable = target.getClass();
/* 596 */     if (editable != null) {
/* 597 */       if (!editable.isInstance(target)) {
/* 598 */         throw new IllegalArgumentException("Target class [" + target.getClass().getName() + "] not assignable to Editable class [" + editable
/* 599 */             .getName() + "]");
/*     */       }
/* 601 */       actualEditable = editable;
/*     */     } 
/* 603 */     PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
/* 604 */     List<String> ignoreList = (ignoreProperties != null) ? Arrays.<String>asList(ignoreProperties) : null;
/*     */     
/* 606 */     for (PropertyDescriptor targetPd : targetPds) {
/* 607 */       Method writeMethod = targetPd.getWriteMethod();
/* 608 */       if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
/* 609 */         PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
/* 610 */         if (sourcePd != null) {
/* 611 */           Method readMethod = sourcePd.getReadMethod();
/* 612 */           if (readMethod != null && 
/* 613 */             ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType()))
/*     */             try {
/* 615 */               if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
/* 616 */                 readMethod.setAccessible(true);
/*     */               }
/* 618 */               Object value = readMethod.invoke(source, new Object[0]);
/* 619 */               if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
/* 620 */                 writeMethod.setAccessible(true);
/*     */               }
/* 622 */               writeMethod.invoke(target, new Object[] { value });
/*     */             }
/* 624 */             catch (Throwable ex) {
/* 625 */               throw new FatalBeanException("Could not copy property '" + targetPd
/* 626 */                   .getName() + "' from source to target", ex);
/*     */             }  
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\BeanUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */