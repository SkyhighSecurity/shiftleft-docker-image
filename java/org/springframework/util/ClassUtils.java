/*      */ package org.springframework.util;
/*      */ 
/*      */ import java.beans.Introspector;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.Proxy;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class ClassUtils
/*      */ {
/*      */   public static final String ARRAY_SUFFIX = "[]";
/*      */   private static final String INTERNAL_ARRAY_PREFIX = "[";
/*      */   private static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";
/*      */   private static final char PACKAGE_SEPARATOR = '.';
/*      */   private static final char PATH_SEPARATOR = '/';
/*      */   private static final char INNER_CLASS_SEPARATOR = '$';
/*      */   public static final String CGLIB_CLASS_SEPARATOR = "$$";
/*      */   public static final String CLASS_FILE_SUFFIX = ".class";
/*   79 */   private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new IdentityHashMap<Class<?>, Class<?>>(8);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   85 */   private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new IdentityHashMap<Class<?>, Class<?>>(8);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   91 */   private static final Map<String, Class<?>> primitiveTypeNameMap = new HashMap<String, Class<?>>(32);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   97 */   private static final Map<String, Class<?>> commonClassCache = new HashMap<String, Class<?>>(32);
/*      */ 
/*      */   
/*      */   static {
/*  101 */     primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
/*  102 */     primitiveWrapperTypeMap.put(Byte.class, byte.class);
/*  103 */     primitiveWrapperTypeMap.put(Character.class, char.class);
/*  104 */     primitiveWrapperTypeMap.put(Double.class, double.class);
/*  105 */     primitiveWrapperTypeMap.put(Float.class, float.class);
/*  106 */     primitiveWrapperTypeMap.put(Integer.class, int.class);
/*  107 */     primitiveWrapperTypeMap.put(Long.class, long.class);
/*  108 */     primitiveWrapperTypeMap.put(Short.class, short.class);
/*      */     
/*  110 */     for (Map.Entry<Class<?>, Class<?>> entry : primitiveWrapperTypeMap.entrySet()) {
/*  111 */       primitiveTypeToWrapperMap.put(entry.getValue(), entry.getKey());
/*  112 */       registerCommonClasses(new Class[] { entry.getKey() });
/*      */     } 
/*      */     
/*  115 */     Set<Class<?>> primitiveTypes = new HashSet<Class<?>>(64);
/*  116 */     primitiveTypes.addAll(primitiveWrapperTypeMap.values());
/*  117 */     primitiveTypes.addAll(Arrays.asList(new Class[] { boolean[].class, byte[].class, char[].class, double[].class, float[].class, int[].class, long[].class, short[].class }));
/*      */ 
/*      */     
/*  120 */     primitiveTypes.add(void.class);
/*  121 */     for (Class<?> primitiveType : primitiveTypes) {
/*  122 */       primitiveTypeNameMap.put(primitiveType.getName(), primitiveType);
/*      */     }
/*      */     
/*  125 */     registerCommonClasses(new Class[] { Boolean[].class, Byte[].class, Character[].class, Double[].class, Float[].class, Integer[].class, Long[].class, Short[].class });
/*      */     
/*  127 */     registerCommonClasses(new Class[] { Number.class, Number[].class, String.class, String[].class, Class.class, Class[].class, Object.class, Object[].class });
/*      */     
/*  129 */     registerCommonClasses(new Class[] { Throwable.class, Exception.class, RuntimeException.class, Error.class, StackTraceElement.class, StackTraceElement[].class });
/*      */     
/*  131 */     registerCommonClasses(new Class[] { Enum.class, Iterable.class, Cloneable.class, Comparable.class });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void registerCommonClasses(Class<?>... commonClasses) {
/*  139 */     for (Class<?> clazz : commonClasses) {
/*  140 */       commonClassCache.put(clazz.getName(), clazz);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ClassLoader getDefaultClassLoader() {
/*  159 */     ClassLoader cl = null;
/*      */     try {
/*  161 */       cl = Thread.currentThread().getContextClassLoader();
/*      */     }
/*  163 */     catch (Throwable throwable) {}
/*      */ 
/*      */     
/*  166 */     if (cl == null) {
/*      */       
/*  168 */       cl = ClassUtils.class.getClassLoader();
/*  169 */       if (cl == null) {
/*      */         
/*      */         try {
/*  172 */           cl = ClassLoader.getSystemClassLoader();
/*      */         }
/*  174 */         catch (Throwable throwable) {}
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  179 */     return cl;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ClassLoader overrideThreadContextClassLoader(ClassLoader classLoaderToUse) {
/*  190 */     Thread currentThread = Thread.currentThread();
/*  191 */     ClassLoader threadContextClassLoader = currentThread.getContextClassLoader();
/*  192 */     if (classLoaderToUse != null && !classLoaderToUse.equals(threadContextClassLoader)) {
/*  193 */       currentThread.setContextClassLoader(classLoaderToUse);
/*  194 */       return threadContextClassLoader;
/*      */     } 
/*      */     
/*  197 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> forName(String name, ClassLoader classLoader) throws ClassNotFoundException, LinkageError {
/*  215 */     Assert.notNull(name, "Name must not be null");
/*      */     
/*  217 */     Class<?> clazz = resolvePrimitiveClassName(name);
/*  218 */     if (clazz == null) {
/*  219 */       clazz = commonClassCache.get(name);
/*      */     }
/*  221 */     if (clazz != null) {
/*  222 */       return clazz;
/*      */     }
/*      */ 
/*      */     
/*  226 */     if (name.endsWith("[]")) {
/*  227 */       String elementClassName = name.substring(0, name.length() - "[]".length());
/*  228 */       Class<?> elementClass = forName(elementClassName, classLoader);
/*  229 */       return Array.newInstance(elementClass, 0).getClass();
/*      */     } 
/*      */ 
/*      */     
/*  233 */     if (name.startsWith("[L") && name.endsWith(";")) {
/*  234 */       String elementName = name.substring("[L".length(), name.length() - 1);
/*  235 */       Class<?> elementClass = forName(elementName, classLoader);
/*  236 */       return Array.newInstance(elementClass, 0).getClass();
/*      */     } 
/*      */ 
/*      */     
/*  240 */     if (name.startsWith("[")) {
/*  241 */       String elementName = name.substring("[".length());
/*  242 */       Class<?> elementClass = forName(elementName, classLoader);
/*  243 */       return Array.newInstance(elementClass, 0).getClass();
/*      */     } 
/*      */     
/*  246 */     ClassLoader clToUse = classLoader;
/*  247 */     if (clToUse == null) {
/*  248 */       clToUse = getDefaultClassLoader();
/*      */     }
/*      */     try {
/*  251 */       return (clToUse != null) ? clToUse.loadClass(name) : Class.forName(name);
/*      */     }
/*  253 */     catch (ClassNotFoundException ex) {
/*  254 */       int lastDotIndex = name.lastIndexOf('.');
/*  255 */       if (lastDotIndex != -1) {
/*      */         
/*  257 */         String innerClassName = name.substring(0, lastDotIndex) + '$' + name.substring(lastDotIndex + 1);
/*      */         try {
/*  259 */           return (clToUse != null) ? clToUse.loadClass(innerClassName) : Class.forName(innerClassName);
/*      */         }
/*  261 */         catch (ClassNotFoundException classNotFoundException) {}
/*      */       } 
/*      */ 
/*      */       
/*  265 */       throw ex;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> resolveClassName(String className, ClassLoader classLoader) throws IllegalArgumentException {
/*      */     try {
/*  285 */       return forName(className, classLoader);
/*      */     }
/*  287 */     catch (ClassNotFoundException ex) {
/*  288 */       throw new IllegalArgumentException("Could not find class [" + className + "]", ex);
/*      */     }
/*  290 */     catch (LinkageError err) {
/*  291 */       throw new IllegalArgumentException("Unresolvable class definition for class [" + className + "]", err);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPresent(String className, ClassLoader classLoader) {
/*      */     try {
/*  306 */       forName(className, classLoader);
/*  307 */       return true;
/*      */     }
/*  309 */     catch (Throwable ex) {
/*      */       
/*  311 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isVisible(Class<?> clazz, ClassLoader classLoader) {
/*  322 */     if (classLoader == null) {
/*  323 */       return true;
/*      */     }
/*      */     try {
/*  326 */       return (clazz == classLoader.loadClass(clazz.getName()));
/*      */     
/*      */     }
/*  329 */     catch (ClassNotFoundException ex) {
/*      */       
/*  331 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isCacheSafe(Class<?> clazz, ClassLoader classLoader) {
/*  343 */     Assert.notNull(clazz, "Class must not be null");
/*      */     try {
/*  345 */       ClassLoader target = clazz.getClassLoader();
/*      */       
/*  347 */       if (target == classLoader || target == null) {
/*  348 */         return true;
/*      */       }
/*  350 */       if (classLoader == null) {
/*  351 */         return false;
/*      */       }
/*      */       
/*  354 */       ClassLoader current = classLoader;
/*  355 */       while (current != null) {
/*  356 */         current = current.getParent();
/*  357 */         if (current == target) {
/*  358 */           return true;
/*      */         }
/*      */       } 
/*      */       
/*  362 */       while (target != null) {
/*  363 */         target = target.getParent();
/*  364 */         if (target == classLoader) {
/*  365 */           return false;
/*      */         }
/*      */       }
/*      */     
/*  369 */     } catch (SecurityException securityException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  375 */     return (classLoader != null && isVisible(clazz, classLoader));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> resolvePrimitiveClassName(String name) {
/*  389 */     Class<?> result = null;
/*      */ 
/*      */     
/*  392 */     if (name != null && name.length() <= 8)
/*      */     {
/*  394 */       result = primitiveTypeNameMap.get(name);
/*      */     }
/*  396 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitiveWrapper(Class<?> clazz) {
/*  406 */     Assert.notNull(clazz, "Class must not be null");
/*  407 */     return primitiveWrapperTypeMap.containsKey(clazz);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
/*  418 */     Assert.notNull(clazz, "Class must not be null");
/*  419 */     return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitiveArray(Class<?> clazz) {
/*  429 */     Assert.notNull(clazz, "Class must not be null");
/*  430 */     return (clazz.isArray() && clazz.getComponentType().isPrimitive());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitiveWrapperArray(Class<?> clazz) {
/*  440 */     Assert.notNull(clazz, "Class must not be null");
/*  441 */     return (clazz.isArray() && isPrimitiveWrapper(clazz.getComponentType()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> resolvePrimitiveIfNecessary(Class<?> clazz) {
/*  451 */     Assert.notNull(clazz, "Class must not be null");
/*  452 */     return (clazz.isPrimitive() && clazz != void.class) ? primitiveTypeToWrapperMap.get(clazz) : clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
/*  465 */     Assert.notNull(lhsType, "Left-hand side type must not be null");
/*  466 */     Assert.notNull(rhsType, "Right-hand side type must not be null");
/*  467 */     if (lhsType.isAssignableFrom(rhsType)) {
/*  468 */       return true;
/*      */     }
/*  470 */     if (lhsType.isPrimitive()) {
/*  471 */       Class<?> resolvedPrimitive = primitiveWrapperTypeMap.get(rhsType);
/*  472 */       if (lhsType == resolvedPrimitive) {
/*  473 */         return true;
/*      */       }
/*      */     } else {
/*      */       
/*  477 */       Class<?> resolvedWrapper = primitiveTypeToWrapperMap.get(rhsType);
/*  478 */       if (resolvedWrapper != null && lhsType.isAssignableFrom(resolvedWrapper)) {
/*  479 */         return true;
/*      */       }
/*      */     } 
/*  482 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isAssignableValue(Class<?> type, Object value) {
/*  494 */     Assert.notNull(type, "Type must not be null");
/*  495 */     return (value != null) ? isAssignable(type, value.getClass()) : (!type.isPrimitive());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String convertResourcePathToClassName(String resourcePath) {
/*  504 */     Assert.notNull(resourcePath, "Resource path must not be null");
/*  505 */     return resourcePath.replace('/', '.');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String convertClassNameToResourcePath(String className) {
/*  514 */     Assert.notNull(className, "Class name must not be null");
/*  515 */     return className.replace('.', '/');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String addResourcePathToPackagePath(Class<?> clazz, String resourceName) {
/*  535 */     Assert.notNull(resourceName, "Resource name must not be null");
/*  536 */     if (!resourceName.startsWith("/")) {
/*  537 */       return classPackageAsResourcePath(clazz) + '/' + resourceName;
/*      */     }
/*  539 */     return classPackageAsResourcePath(clazz) + resourceName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String classPackageAsResourcePath(Class<?> clazz) {
/*  557 */     if (clazz == null) {
/*  558 */       return "";
/*      */     }
/*  560 */     String className = clazz.getName();
/*  561 */     int packageEndIndex = className.lastIndexOf('.');
/*  562 */     if (packageEndIndex == -1) {
/*  563 */       return "";
/*      */     }
/*  565 */     String packageName = className.substring(0, packageEndIndex);
/*  566 */     return packageName.replace('.', '/');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String classNamesToString(Class<?>... classes) {
/*  579 */     return classNamesToString(Arrays.asList(classes));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String classNamesToString(Collection<Class<?>> classes) {
/*  592 */     if (CollectionUtils.isEmpty(classes)) {
/*  593 */       return "[]";
/*      */     }
/*  595 */     StringBuilder sb = new StringBuilder("[");
/*  596 */     for (Iterator<Class<?>> it = classes.iterator(); it.hasNext(); ) {
/*  597 */       Class<?> clazz = it.next();
/*  598 */       sb.append(clazz.getName());
/*  599 */       if (it.hasNext()) {
/*  600 */         sb.append(", ");
/*      */       }
/*      */     } 
/*  603 */     sb.append("]");
/*  604 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?>[] toClassArray(Collection<Class<?>> collection) {
/*  616 */     if (collection == null) {
/*  617 */       return null;
/*      */     }
/*  619 */     return (Class[])collection.<Class<?>[]>toArray((Class<?>[][])new Class[collection.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?>[] getAllInterfaces(Object instance) {
/*  629 */     Assert.notNull(instance, "Instance must not be null");
/*  630 */     return getAllInterfacesForClass(instance.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?>[] getAllInterfacesForClass(Class<?> clazz) {
/*  641 */     return getAllInterfacesForClass(clazz, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?>[] getAllInterfacesForClass(Class<?> clazz, ClassLoader classLoader) {
/*  654 */     return toClassArray(getAllInterfacesForClassAsSet(clazz, classLoader));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Set<Class<?>> getAllInterfacesAsSet(Object instance) {
/*  664 */     Assert.notNull(instance, "Instance must not be null");
/*  665 */     return getAllInterfacesForClassAsSet(instance.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Set<Class<?>> getAllInterfacesForClassAsSet(Class<?> clazz) {
/*  676 */     return getAllInterfacesForClassAsSet(clazz, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Set<Class<?>> getAllInterfacesForClassAsSet(Class<?> clazz, ClassLoader classLoader) {
/*  689 */     Assert.notNull(clazz, "Class must not be null");
/*  690 */     if (clazz.isInterface() && isVisible(clazz, classLoader)) {
/*  691 */       return Collections.singleton(clazz);
/*      */     }
/*  693 */     Set<Class<?>> interfaces = new LinkedHashSet<Class<?>>();
/*  694 */     Class<?> current = clazz;
/*  695 */     while (current != null) {
/*  696 */       Class<?>[] ifcs = current.getInterfaces();
/*  697 */       for (Class<?> ifc : ifcs) {
/*  698 */         interfaces.addAll(getAllInterfacesForClassAsSet(ifc, classLoader));
/*      */       }
/*  700 */       current = current.getSuperclass();
/*      */     } 
/*  702 */     return interfaces;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> createCompositeInterface(Class<?>[] interfaces, ClassLoader classLoader) {
/*  715 */     Assert.notEmpty((Object[])interfaces, "Interfaces must not be empty");
/*  716 */     return Proxy.getProxyClass(classLoader, interfaces);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> determineCommonAncestor(Class<?> clazz1, Class<?> clazz2) {
/*  729 */     if (clazz1 == null) {
/*  730 */       return clazz2;
/*      */     }
/*  732 */     if (clazz2 == null) {
/*  733 */       return clazz1;
/*      */     }
/*  735 */     if (clazz1.isAssignableFrom(clazz2)) {
/*  736 */       return clazz1;
/*      */     }
/*  738 */     if (clazz2.isAssignableFrom(clazz1)) {
/*  739 */       return clazz2;
/*      */     }
/*  741 */     Class<?> ancestor = clazz1;
/*      */     while (true) {
/*  743 */       ancestor = ancestor.getSuperclass();
/*  744 */       if (ancestor == null || Object.class == ancestor) {
/*  745 */         return null;
/*      */       }
/*      */       
/*  748 */       if (ancestor.isAssignableFrom(clazz2)) {
/*  749 */         return ancestor;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isCglibProxy(Object object) {
/*  759 */     return isCglibProxyClass(object.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isCglibProxyClass(Class<?> clazz) {
/*  768 */     return (clazz != null && isCglibProxyClassName(clazz.getName()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isCglibProxyClassName(String className) {
/*  776 */     return (className != null && className.contains("$$"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> getUserClass(Object instance) {
/*  787 */     Assert.notNull(instance, "Instance must not be null");
/*  788 */     return getUserClass(instance.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> getUserClass(Class<?> clazz) {
/*  798 */     if (clazz != null && clazz.getName().contains("$$")) {
/*  799 */       Class<?> superclass = clazz.getSuperclass();
/*  800 */       if (superclass != null && Object.class != superclass) {
/*  801 */         return superclass;
/*      */       }
/*      */     } 
/*  804 */     return clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getDescriptiveType(Object value) {
/*  815 */     if (value == null) {
/*  816 */       return null;
/*      */     }
/*  818 */     Class<?> clazz = value.getClass();
/*  819 */     if (Proxy.isProxyClass(clazz)) {
/*  820 */       StringBuilder result = new StringBuilder(clazz.getName());
/*  821 */       result.append(" implementing ");
/*  822 */       Class<?>[] ifcs = clazz.getInterfaces();
/*  823 */       for (int i = 0; i < ifcs.length; i++) {
/*  824 */         result.append(ifcs[i].getName());
/*  825 */         if (i < ifcs.length - 1) {
/*  826 */           result.append(',');
/*      */         }
/*      */       } 
/*  829 */       return result.toString();
/*      */     } 
/*  831 */     if (clazz.isArray()) {
/*  832 */       return getQualifiedNameForArray(clazz);
/*      */     }
/*      */     
/*  835 */     return clazz.getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean matchesTypeName(Class<?> clazz, String typeName) {
/*  845 */     return (typeName != null && (typeName
/*  846 */       .equals(clazz.getName()) || typeName.equals(clazz.getSimpleName()) || (clazz
/*  847 */       .isArray() && typeName.equals(getQualifiedNameForArray(clazz)))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortName(String className) {
/*  857 */     Assert.hasLength(className, "Class name must not be empty");
/*  858 */     int lastDotIndex = className.lastIndexOf('.');
/*  859 */     int nameEndIndex = className.indexOf("$$");
/*  860 */     if (nameEndIndex == -1) {
/*  861 */       nameEndIndex = className.length();
/*      */     }
/*  863 */     String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
/*  864 */     shortName = shortName.replace('$', '.');
/*  865 */     return shortName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortName(Class<?> clazz) {
/*  874 */     return getShortName(getQualifiedName(clazz));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortNameAsProperty(Class<?> clazz) {
/*  885 */     String shortName = getShortName(clazz);
/*  886 */     int dotIndex = shortName.lastIndexOf('.');
/*  887 */     shortName = (dotIndex != -1) ? shortName.substring(dotIndex + 1) : shortName;
/*  888 */     return Introspector.decapitalize(shortName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getClassFileName(Class<?> clazz) {
/*  898 */     Assert.notNull(clazz, "Class must not be null");
/*  899 */     String className = clazz.getName();
/*  900 */     int lastDotIndex = className.lastIndexOf('.');
/*  901 */     return className.substring(lastDotIndex + 1) + ".class";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPackageName(Class<?> clazz) {
/*  912 */     Assert.notNull(clazz, "Class must not be null");
/*  913 */     return getPackageName(clazz.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPackageName(String fqClassName) {
/*  924 */     Assert.notNull(fqClassName, "Class name must not be null");
/*  925 */     int lastDotIndex = fqClassName.lastIndexOf('.');
/*  926 */     return (lastDotIndex != -1) ? fqClassName.substring(0, lastDotIndex) : "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getQualifiedName(Class<?> clazz) {
/*  936 */     Assert.notNull(clazz, "Class must not be null");
/*  937 */     if (clazz.isArray()) {
/*  938 */       return getQualifiedNameForArray(clazz);
/*      */     }
/*      */     
/*  941 */     return clazz.getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String getQualifiedNameForArray(Class<?> clazz) {
/*  952 */     StringBuilder result = new StringBuilder();
/*  953 */     while (clazz.isArray()) {
/*  954 */       clazz = clazz.getComponentType();
/*  955 */       result.append("[]");
/*      */     } 
/*  957 */     result.insert(0, clazz.getName());
/*  958 */     return result.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getQualifiedMethodName(Method method) {
/*  968 */     return getQualifiedMethodName(method, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getQualifiedMethodName(Method method, Class<?> clazz) {
/*  981 */     Assert.notNull(method, "Method must not be null");
/*  982 */     return ((clazz != null) ? clazz : method.getDeclaringClass()).getName() + '.' + method.getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasConstructor(Class<?> clazz, Class<?>... paramTypes) {
/*  994 */     return (getConstructorIfAvailable(clazz, paramTypes) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Constructor<T> getConstructorIfAvailable(Class<T> clazz, Class<?>... paramTypes) {
/* 1007 */     Assert.notNull(clazz, "Class must not be null");
/*      */     try {
/* 1009 */       return clazz.getConstructor(paramTypes);
/*      */     }
/* 1011 */     catch (NoSuchMethodException ex) {
/* 1012 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
/* 1026 */     return (getMethodIfAvailable(clazz, methodName, paramTypes) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Method getMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
/* 1044 */     Assert.notNull(clazz, "Class must not be null");
/* 1045 */     Assert.notNull(methodName, "Method name must not be null");
/* 1046 */     if (paramTypes != null) {
/*      */       try {
/* 1048 */         return clazz.getMethod(methodName, paramTypes);
/*      */       }
/* 1050 */       catch (NoSuchMethodException ex) {
/* 1051 */         throw new IllegalStateException("Expected method not found: " + ex);
/*      */       } 
/*      */     }
/*      */     
/* 1055 */     Set<Method> candidates = new HashSet<Method>(1);
/* 1056 */     Method[] methods = clazz.getMethods();
/* 1057 */     for (Method method : methods) {
/* 1058 */       if (methodName.equals(method.getName())) {
/* 1059 */         candidates.add(method);
/*      */       }
/*      */     } 
/* 1062 */     if (candidates.size() == 1) {
/* 1063 */       return candidates.iterator().next();
/*      */     }
/* 1065 */     if (candidates.isEmpty()) {
/* 1066 */       throw new IllegalStateException("Expected method not found: " + clazz.getName() + '.' + methodName);
/*      */     }
/*      */     
/* 1069 */     throw new IllegalStateException("No unique method found: " + clazz.getName() + '.' + methodName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Method getMethodIfAvailable(Class<?> clazz, String methodName, Class<?>... paramTypes) {
/* 1088 */     Assert.notNull(clazz, "Class must not be null");
/* 1089 */     Assert.notNull(methodName, "Method name must not be null");
/* 1090 */     if (paramTypes != null) {
/*      */       try {
/* 1092 */         return clazz.getMethod(methodName, paramTypes);
/*      */       }
/* 1094 */       catch (NoSuchMethodException ex) {
/* 1095 */         return null;
/*      */       } 
/*      */     }
/*      */     
/* 1099 */     Set<Method> candidates = new HashSet<Method>(1);
/* 1100 */     Method[] methods = clazz.getMethods();
/* 1101 */     for (Method method : methods) {
/* 1102 */       if (methodName.equals(method.getName())) {
/* 1103 */         candidates.add(method);
/*      */       }
/*      */     } 
/* 1106 */     if (candidates.size() == 1) {
/* 1107 */       return candidates.iterator().next();
/*      */     }
/* 1109 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getMethodCountForName(Class<?> clazz, String methodName) {
/* 1121 */     Assert.notNull(clazz, "Class must not be null");
/* 1122 */     Assert.notNull(methodName, "Method name must not be null");
/* 1123 */     int count = 0;
/* 1124 */     Method[] declaredMethods = clazz.getDeclaredMethods();
/* 1125 */     for (Method method : declaredMethods) {
/* 1126 */       if (methodName.equals(method.getName())) {
/* 1127 */         count++;
/*      */       }
/*      */     } 
/* 1130 */     Class<?>[] ifcs = clazz.getInterfaces();
/* 1131 */     for (Class<?> ifc : ifcs) {
/* 1132 */       count += getMethodCountForName(ifc, methodName);
/*      */     }
/* 1134 */     if (clazz.getSuperclass() != null) {
/* 1135 */       count += getMethodCountForName(clazz.getSuperclass(), methodName);
/*      */     }
/* 1137 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasAtLeastOneMethodWithName(Class<?> clazz, String methodName) {
/* 1149 */     Assert.notNull(clazz, "Class must not be null");
/* 1150 */     Assert.notNull(methodName, "Method name must not be null");
/* 1151 */     Method[] declaredMethods = clazz.getDeclaredMethods();
/* 1152 */     for (Method method : declaredMethods) {
/* 1153 */       if (method.getName().equals(methodName)) {
/* 1154 */         return true;
/*      */       }
/*      */     } 
/* 1157 */     Class<?>[] ifcs = clazz.getInterfaces();
/* 1158 */     for (Class<?> ifc : ifcs) {
/* 1159 */       if (hasAtLeastOneMethodWithName(ifc, methodName)) {
/* 1160 */         return true;
/*      */       }
/*      */     } 
/* 1163 */     return (clazz.getSuperclass() != null && hasAtLeastOneMethodWithName(clazz.getSuperclass(), methodName));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Method getMostSpecificMethod(Method method, Class<?> targetClass) {
/* 1187 */     if (method != null && isOverridable(method, targetClass) && targetClass != null && targetClass != method
/* 1188 */       .getDeclaringClass()) {
/*      */       try {
/* 1190 */         if (Modifier.isPublic(method.getModifiers())) {
/*      */           try {
/* 1192 */             return targetClass.getMethod(method.getName(), method.getParameterTypes());
/*      */           }
/* 1194 */           catch (NoSuchMethodException ex) {
/* 1195 */             return method;
/*      */           } 
/*      */         }
/*      */ 
/*      */         
/* 1200 */         Method specificMethod = ReflectionUtils.findMethod(targetClass, method.getName(), method.getParameterTypes());
/* 1201 */         return (specificMethod != null) ? specificMethod : method;
/*      */       
/*      */       }
/* 1204 */       catch (SecurityException securityException) {}
/*      */     }
/*      */ 
/*      */     
/* 1208 */     return method;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isUserLevelMethod(Method method) {
/* 1223 */     Assert.notNull(method, "Method must not be null");
/* 1224 */     return (method.isBridge() || (!method.isSynthetic() && !isGroovyObjectMethod(method)));
/*      */   }
/*      */   
/*      */   private static boolean isGroovyObjectMethod(Method method) {
/* 1228 */     return method.getDeclaringClass().getName().equals("groovy.lang.GroovyObject");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isOverridable(Method method, Class<?> targetClass) {
/* 1237 */     if (Modifier.isPrivate(method.getModifiers())) {
/* 1238 */       return false;
/*      */     }
/* 1240 */     if (Modifier.isPublic(method.getModifiers()) || Modifier.isProtected(method.getModifiers())) {
/* 1241 */       return true;
/*      */     }
/* 1243 */     return getPackageName(method.getDeclaringClass()).equals(getPackageName(targetClass));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Method getStaticMethod(Class<?> clazz, String methodName, Class<?>... args) {
/* 1255 */     Assert.notNull(clazz, "Class must not be null");
/* 1256 */     Assert.notNull(methodName, "Method name must not be null");
/*      */     try {
/* 1258 */       Method method = clazz.getMethod(methodName, args);
/* 1259 */       return Modifier.isStatic(method.getModifiers()) ? method : null;
/*      */     }
/* 1261 */     catch (NoSuchMethodException ex) {
/* 1262 */       return null;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\ClassUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */