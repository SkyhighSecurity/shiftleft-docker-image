/*      */ package org.springframework.core.annotation;
/*      */ 
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.reflect.AnnotatedElement;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.InvocationHandler;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.Proxy;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.springframework.core.BridgeMethodResolver;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ConcurrentReferenceHashMap;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.ReflectionUtils;
/*      */ import org.springframework.util.StringUtils;
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
/*      */ public abstract class AnnotationUtils
/*      */ {
/*      */   public static final String VALUE = "value";
/*      */   private static final String REPEATABLE_CLASS_NAME = "java.lang.annotation.Repeatable";
/*  117 */   private static final Map<AnnotationCacheKey, Annotation> findAnnotationCache = (Map<AnnotationCacheKey, Annotation>)new ConcurrentReferenceHashMap(256);
/*      */ 
/*      */   
/*  120 */   private static final Map<AnnotationCacheKey, Boolean> metaPresentCache = (Map<AnnotationCacheKey, Boolean>)new ConcurrentReferenceHashMap(256);
/*      */ 
/*      */   
/*  123 */   private static final Map<Class<?>, Boolean> annotatedInterfaceCache = (Map<Class<?>, Boolean>)new ConcurrentReferenceHashMap(256);
/*      */ 
/*      */   
/*  126 */   private static final Map<Class<? extends Annotation>, Boolean> synthesizableCache = (Map<Class<? extends Annotation>, Boolean>)new ConcurrentReferenceHashMap(256);
/*      */ 
/*      */   
/*  129 */   private static final Map<Class<? extends Annotation>, Map<String, List<String>>> attributeAliasesCache = (Map<Class<? extends Annotation>, Map<String, List<String>>>)new ConcurrentReferenceHashMap(256);
/*      */ 
/*      */   
/*  132 */   private static final Map<Class<? extends Annotation>, List<Method>> attributeMethodsCache = (Map<Class<? extends Annotation>, List<Method>>)new ConcurrentReferenceHashMap(256);
/*      */ 
/*      */   
/*  135 */   private static final Map<Method, AliasDescriptor> aliasDescriptorCache = (Map<Method, AliasDescriptor>)new ConcurrentReferenceHashMap(256);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static transient Log logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <A extends Annotation> A getAnnotation(Annotation annotation, Class<A> annotationType) {
/*  155 */     if (annotationType.isInstance(annotation)) {
/*  156 */       return synthesizeAnnotation((A)annotation);
/*      */     }
/*  158 */     Class<? extends Annotation> annotatedElement = annotation.annotationType();
/*      */     try {
/*  160 */       return synthesizeAnnotation(annotatedElement.getAnnotation(annotationType), annotatedElement);
/*      */     }
/*  162 */     catch (Throwable ex) {
/*  163 */       handleIntrospectionFailure(annotatedElement, ex);
/*  164 */       return null;
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
/*      */   public static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType) {
/*      */     try {
/*  182 */       A annotation = annotatedElement.getAnnotation(annotationType);
/*  183 */       if (annotation == null) {
/*  184 */         for (Annotation metaAnn : annotatedElement.getAnnotations()) {
/*  185 */           annotation = metaAnn.annotationType().getAnnotation(annotationType);
/*  186 */           if (annotation != null) {
/*      */             break;
/*      */           }
/*      */         } 
/*      */       }
/*  191 */       return synthesizeAnnotation(annotation, annotatedElement);
/*      */     }
/*  193 */     catch (Throwable ex) {
/*  194 */       handleIntrospectionFailure(annotatedElement, ex);
/*  195 */       return null;
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
/*      */   public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {
/*  214 */     Method resolvedMethod = BridgeMethodResolver.findBridgedMethod(method);
/*  215 */     return getAnnotation(resolvedMethod, annotationType);
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
/*      */   public static Annotation[] getAnnotations(AnnotatedElement annotatedElement) {
/*      */     try {
/*  231 */       return synthesizeAnnotationArray(annotatedElement.getAnnotations(), annotatedElement);
/*      */     }
/*  233 */     catch (Throwable ex) {
/*  234 */       handleIntrospectionFailure(annotatedElement, ex);
/*  235 */       return null;
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
/*      */   public static Annotation[] getAnnotations(Method method) {
/*      */     try {
/*  253 */       return synthesizeAnnotationArray(BridgeMethodResolver.findBridgedMethod(method).getAnnotations(), method);
/*      */     }
/*  255 */     catch (Throwable ex) {
/*  256 */       handleIntrospectionFailure(method, ex);
/*  257 */       return null;
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
/*      */   @Deprecated
/*      */   public static <A extends Annotation> Set<A> getRepeatableAnnotation(Method method, Class<? extends Annotation> containerAnnotationType, Class<A> annotationType) {
/*  273 */     return getRepeatableAnnotations(method, annotationType, containerAnnotationType);
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
/*      */   @Deprecated
/*      */   public static <A extends Annotation> Set<A> getRepeatableAnnotation(AnnotatedElement annotatedElement, Class<? extends Annotation> containerAnnotationType, Class<A> annotationType) {
/*  288 */     return getRepeatableAnnotations(annotatedElement, annotationType, containerAnnotationType);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <A extends Annotation> Set<A> getRepeatableAnnotations(AnnotatedElement annotatedElement, Class<A> annotationType) {
/*  321 */     return getRepeatableAnnotations(annotatedElement, annotationType, null);
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
/*      */   public static <A extends Annotation> Set<A> getRepeatableAnnotations(AnnotatedElement annotatedElement, Class<A> annotationType, Class<? extends Annotation> containerAnnotationType) {
/*  357 */     Set<A> annotations = getDeclaredRepeatableAnnotations(annotatedElement, annotationType, containerAnnotationType);
/*  358 */     if (!annotations.isEmpty()) {
/*  359 */       return annotations;
/*      */     }
/*      */     
/*  362 */     if (annotatedElement instanceof Class) {
/*  363 */       Class<?> superclass = ((Class)annotatedElement).getSuperclass();
/*  364 */       if (superclass != null && Object.class != superclass) {
/*  365 */         return getRepeatableAnnotations(superclass, annotationType, containerAnnotationType);
/*      */       }
/*      */     } 
/*      */     
/*  369 */     return getRepeatableAnnotations(annotatedElement, annotationType, containerAnnotationType, false);
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
/*      */   public static <A extends Annotation> Set<A> getDeclaredRepeatableAnnotations(AnnotatedElement annotatedElement, Class<A> annotationType) {
/*  403 */     return getDeclaredRepeatableAnnotations(annotatedElement, annotationType, null);
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
/*      */   public static <A extends Annotation> Set<A> getDeclaredRepeatableAnnotations(AnnotatedElement annotatedElement, Class<A> annotationType, Class<? extends Annotation> containerAnnotationType) {
/*  439 */     return getRepeatableAnnotations(annotatedElement, annotationType, containerAnnotationType, true);
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
/*      */ 
/*      */   
/*      */   private static <A extends Annotation> Set<A> getRepeatableAnnotations(AnnotatedElement annotatedElement, Class<A> annotationType, Class<? extends Annotation> containerAnnotationType, boolean declaredMode) {
/*  465 */     Assert.notNull(annotatedElement, "AnnotatedElement must not be null");
/*  466 */     Assert.notNull(annotationType, "Annotation type must not be null");
/*      */     
/*      */     try {
/*  469 */       if (annotatedElement instanceof Method) {
/*  470 */         annotatedElement = BridgeMethodResolver.findBridgedMethod((Method)annotatedElement);
/*      */       }
/*  472 */       return (new AnnotationCollector<A>(annotationType, containerAnnotationType, declaredMode)).getResult(annotatedElement);
/*      */     }
/*  474 */     catch (Throwable ex) {
/*  475 */       handleIntrospectionFailure(annotatedElement, ex);
/*  476 */       return Collections.emptySet();
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
/*      */ 
/*      */   
/*      */   public static <A extends Annotation> A findAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType) {
/*  497 */     Assert.notNull(annotatedElement, "AnnotatedElement must not be null");
/*  498 */     if (annotationType == null) {
/*  499 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  504 */     A ann = findAnnotation(annotatedElement, annotationType, new HashSet<Annotation>());
/*  505 */     return synthesizeAnnotation(ann, annotatedElement);
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
/*      */   private static <A extends Annotation> A findAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType, Set<Annotation> visited) {
/*      */     try {
/*  522 */       Annotation[] anns = annotatedElement.getDeclaredAnnotations();
/*  523 */       for (Annotation ann : anns) {
/*  524 */         if (ann.annotationType() == annotationType) {
/*  525 */           return (A)ann;
/*      */         }
/*      */       } 
/*  528 */       for (Annotation ann : anns) {
/*  529 */         if (!isInJavaLangAnnotationPackage(ann) && visited.add(ann)) {
/*  530 */           A annotation = findAnnotation(ann.annotationType(), annotationType, visited);
/*  531 */           if (annotation != null) {
/*  532 */             return annotation;
/*      */           }
/*      */         }
/*      */       
/*      */       } 
/*  537 */     } catch (Throwable ex) {
/*  538 */       handleIntrospectionFailure(annotatedElement, ex);
/*      */     } 
/*  540 */     return null;
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
/*      */   public static <A extends Annotation> A findAnnotation(Method method, Class<A> annotationType) {
/*  560 */     Assert.notNull(method, "Method must not be null");
/*  561 */     if (annotationType == null) {
/*  562 */       return null;
/*      */     }
/*      */     
/*  565 */     AnnotationCacheKey cacheKey = new AnnotationCacheKey(method, annotationType);
/*  566 */     Annotation annotation = findAnnotationCache.get(cacheKey);
/*      */     
/*  568 */     if (annotation == null) {
/*  569 */       Method resolvedMethod = BridgeMethodResolver.findBridgedMethod(method);
/*  570 */       annotation = findAnnotation(resolvedMethod, annotationType);
/*  571 */       if (annotation == null) {
/*  572 */         annotation = searchOnInterfaces(method, annotationType, method.getDeclaringClass().getInterfaces());
/*      */       }
/*      */       
/*  575 */       Class<?> clazz = method.getDeclaringClass();
/*  576 */       while (annotation == null) {
/*  577 */         clazz = clazz.getSuperclass();
/*  578 */         if (clazz == null || Object.class == clazz) {
/*      */           break;
/*      */         }
/*      */         try {
/*  582 */           Method equivalentMethod = clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
/*  583 */           Method resolvedEquivalentMethod = BridgeMethodResolver.findBridgedMethod(equivalentMethod);
/*  584 */           annotation = findAnnotation(resolvedEquivalentMethod, annotationType);
/*      */         }
/*  586 */         catch (NoSuchMethodException noSuchMethodException) {}
/*      */ 
/*      */         
/*  589 */         if (annotation == null) {
/*  590 */           annotation = searchOnInterfaces(method, annotationType, clazz.getInterfaces());
/*      */         }
/*      */       } 
/*      */       
/*  594 */       if (annotation != null) {
/*  595 */         annotation = synthesizeAnnotation(annotation, method);
/*  596 */         findAnnotationCache.put(cacheKey, annotation);
/*      */       } 
/*      */     } 
/*      */     
/*  600 */     return (A)annotation;
/*      */   }
/*      */   
/*      */   private static <A extends Annotation> A searchOnInterfaces(Method method, Class<A> annotationType, Class<?>... ifcs) {
/*  604 */     A annotation = null;
/*  605 */     for (Class<?> ifc : ifcs) {
/*  606 */       if (isInterfaceWithAnnotatedMethods(ifc)) {
/*      */         try {
/*  608 */           Method equivalentMethod = ifc.getMethod(method.getName(), method.getParameterTypes());
/*  609 */           annotation = getAnnotation(equivalentMethod, annotationType);
/*      */         }
/*  611 */         catch (NoSuchMethodException noSuchMethodException) {}
/*      */ 
/*      */         
/*  614 */         if (annotation != null) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     } 
/*  619 */     return annotation;
/*      */   }
/*      */   
/*      */   static boolean isInterfaceWithAnnotatedMethods(Class<?> ifc) {
/*  623 */     Boolean found = annotatedInterfaceCache.get(ifc);
/*  624 */     if (found != null) {
/*  625 */       return found.booleanValue();
/*      */     }
/*  627 */     found = Boolean.FALSE;
/*  628 */     for (Method ifcMethod : ifc.getMethods()) {
/*      */       try {
/*  630 */         if ((ifcMethod.getAnnotations()).length > 0) {
/*  631 */           found = Boolean.TRUE;
/*      */           
/*      */           break;
/*      */         } 
/*  635 */       } catch (Throwable ex) {
/*  636 */         handleIntrospectionFailure(ifcMethod, ex);
/*      */       } 
/*      */     } 
/*  639 */     annotatedInterfaceCache.put(ifc, found);
/*  640 */     return found.booleanValue();
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
/*      */ 
/*      */   
/*      */   public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType) {
/*  666 */     return findAnnotation(clazz, annotationType, true);
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
/*      */   private static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType, boolean synthesize) {
/*  681 */     Assert.notNull(clazz, "Class must not be null");
/*  682 */     if (annotationType == null) {
/*  683 */       return null;
/*      */     }
/*      */     
/*  686 */     AnnotationCacheKey cacheKey = new AnnotationCacheKey(clazz, annotationType);
/*  687 */     Annotation annotation = findAnnotationCache.get(cacheKey);
/*  688 */     if (annotation == null) {
/*  689 */       annotation = findAnnotation(clazz, annotationType, new HashSet<Annotation>());
/*  690 */       if (annotation != null && synthesize) {
/*  691 */         annotation = synthesizeAnnotation(annotation, clazz);
/*  692 */         findAnnotationCache.put(cacheKey, annotation);
/*      */       } 
/*      */     } 
/*  695 */     return (A)annotation;
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
/*      */   private static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType, Set<Annotation> visited) {
/*      */     try {
/*  710 */       Annotation[] anns = clazz.getDeclaredAnnotations();
/*  711 */       for (Annotation ann : anns) {
/*  712 */         if (ann.annotationType() == annotationType) {
/*  713 */           return (A)ann;
/*      */         }
/*      */       } 
/*  716 */       for (Annotation ann : anns) {
/*  717 */         if (!isInJavaLangAnnotationPackage(ann) && visited.add(ann)) {
/*  718 */           A annotation = findAnnotation(ann.annotationType(), annotationType, visited);
/*  719 */           if (annotation != null) {
/*  720 */             return annotation;
/*      */           }
/*      */         }
/*      */       
/*      */       } 
/*  725 */     } catch (Throwable ex) {
/*  726 */       handleIntrospectionFailure(clazz, ex);
/*  727 */       return null;
/*      */     } 
/*      */     
/*  730 */     for (Class<?> ifc : clazz.getInterfaces()) {
/*  731 */       A annotation = findAnnotation(ifc, annotationType, visited);
/*  732 */       if (annotation != null) {
/*  733 */         return annotation;
/*      */       }
/*      */     } 
/*      */     
/*  737 */     Class<?> superclass = clazz.getSuperclass();
/*  738 */     if (superclass == null || Object.class == superclass) {
/*  739 */       return null;
/*      */     }
/*  741 */     return findAnnotation(superclass, annotationType, visited);
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
/*      */ 
/*      */   
/*      */   public static Class<?> findAnnotationDeclaringClass(Class<? extends Annotation> annotationType, Class<?> clazz) {
/*  767 */     Assert.notNull(annotationType, "Annotation type must not be null");
/*  768 */     if (clazz == null || Object.class == clazz) {
/*  769 */       return null;
/*      */     }
/*  771 */     if (isAnnotationDeclaredLocally(annotationType, clazz)) {
/*  772 */       return clazz;
/*      */     }
/*  774 */     return findAnnotationDeclaringClass(annotationType, clazz.getSuperclass());
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> findAnnotationDeclaringClassForTypes(List<Class<? extends Annotation>> annotationTypes, Class<?> clazz) {
/*  802 */     Assert.notEmpty(annotationTypes, "List of annotation types must not be empty");
/*  803 */     if (clazz == null || Object.class == clazz) {
/*  804 */       return null;
/*      */     }
/*  806 */     for (Class<? extends Annotation> annotationType : annotationTypes) {
/*  807 */       if (isAnnotationDeclaredLocally(annotationType, clazz)) {
/*  808 */         return clazz;
/*      */       }
/*      */     } 
/*  811 */     return findAnnotationDeclaringClassForTypes(annotationTypes, clazz.getSuperclass());
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
/*      */   public static boolean isAnnotationDeclaredLocally(Class<? extends Annotation> annotationType, Class<?> clazz) {
/*  833 */     Assert.notNull(annotationType, "Annotation type must not be null");
/*  834 */     Assert.notNull(clazz, "Class must not be null");
/*      */     try {
/*  836 */       for (Annotation ann : clazz.getDeclaredAnnotations()) {
/*  837 */         if (ann.annotationType() == annotationType) {
/*  838 */           return true;
/*      */         }
/*      */       }
/*      */     
/*  842 */     } catch (Throwable ex) {
/*  843 */       handleIntrospectionFailure(clazz, ex);
/*      */     } 
/*  845 */     return false;
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
/*      */   public static boolean isAnnotationInherited(Class<? extends Annotation> annotationType, Class<?> clazz) {
/*  868 */     Assert.notNull(annotationType, "Annotation type must not be null");
/*  869 */     Assert.notNull(clazz, "Class must not be null");
/*  870 */     return (clazz.isAnnotationPresent(annotationType) && !isAnnotationDeclaredLocally(annotationType, clazz));
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
/*      */   public static boolean isAnnotationMetaPresent(Class<? extends Annotation> annotationType, Class<? extends Annotation> metaAnnotationType) {
/*  884 */     Assert.notNull(annotationType, "Annotation type must not be null");
/*  885 */     if (metaAnnotationType == null) {
/*  886 */       return false;
/*      */     }
/*      */     
/*  889 */     AnnotationCacheKey cacheKey = new AnnotationCacheKey(annotationType, metaAnnotationType);
/*  890 */     Boolean metaPresent = metaPresentCache.get(cacheKey);
/*  891 */     if (metaPresent != null) {
/*  892 */       return metaPresent.booleanValue();
/*      */     }
/*  894 */     metaPresent = Boolean.FALSE;
/*  895 */     if (findAnnotation(annotationType, metaAnnotationType, false) != null) {
/*  896 */       metaPresent = Boolean.TRUE;
/*      */     }
/*  898 */     metaPresentCache.put(cacheKey, metaPresent);
/*  899 */     return metaPresent.booleanValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isInJavaLangAnnotationPackage(Annotation annotation) {
/*  909 */     return (annotation != null && isInJavaLangAnnotationPackage(annotation.annotationType()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean isInJavaLangAnnotationPackage(Class<? extends Annotation> annotationType) {
/*  920 */     return (annotationType != null && isInJavaLangAnnotationPackage(annotationType.getName()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isInJavaLangAnnotationPackage(String annotationType) {
/*  931 */     return (annotationType != null && annotationType.startsWith("java.lang.annotation"));
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
/*      */   public static void validateAnnotation(Annotation annotation) {
/*  947 */     for (Method method : getAttributeMethods(annotation.annotationType())) {
/*  948 */       Class<?> returnType = method.getReturnType();
/*  949 */       if (returnType == Class.class || returnType == Class[].class) {
/*      */         try {
/*  951 */           method.invoke(annotation, new Object[0]);
/*      */         }
/*  953 */         catch (Throwable ex) {
/*  954 */           throw new IllegalStateException("Could not obtain annotation attribute value for " + method, ex);
/*      */         } 
/*      */       }
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
/*      */   
/*      */   public static Map<String, Object> getAnnotationAttributes(Annotation annotation) {
/*  976 */     return getAnnotationAttributes((AnnotatedElement)null, annotation);
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
/*      */   public static Map<String, Object> getAnnotationAttributes(Annotation annotation, boolean classValuesAsString) {
/*  994 */     return getAnnotationAttributes(annotation, classValuesAsString, false);
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
/*      */   public static AnnotationAttributes getAnnotationAttributes(Annotation annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/* 1016 */     return getAnnotationAttributes((AnnotatedElement)null, annotation, classValuesAsString, nestedAnnotationsAsMap);
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
/*      */   public static AnnotationAttributes getAnnotationAttributes(AnnotatedElement annotatedElement, Annotation annotation) {
/* 1033 */     return getAnnotationAttributes(annotatedElement, annotation, false, false);
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
/*      */   public static AnnotationAttributes getAnnotationAttributes(AnnotatedElement annotatedElement, Annotation annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/* 1057 */     return getAnnotationAttributes(annotatedElement, annotation, classValuesAsString, nestedAnnotationsAsMap);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static AnnotationAttributes getAnnotationAttributes(Object annotatedElement, Annotation annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/* 1065 */     AnnotationAttributes attributes = retrieveAnnotationAttributes(annotatedElement, annotation, classValuesAsString, nestedAnnotationsAsMap);
/* 1066 */     postProcessAnnotationAttributes(annotatedElement, attributes, classValuesAsString, nestedAnnotationsAsMap);
/* 1067 */     return attributes;
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
/*      */   static AnnotationAttributes retrieveAnnotationAttributes(Object annotatedElement, Annotation annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/* 1101 */     Class<? extends Annotation> annotationType = annotation.annotationType();
/* 1102 */     AnnotationAttributes attributes = new AnnotationAttributes(annotationType);
/*      */     
/* 1104 */     for (Method method : getAttributeMethods(annotationType)) {
/*      */       try {
/* 1106 */         Object attributeValue = method.invoke(annotation, new Object[0]);
/* 1107 */         Object defaultValue = method.getDefaultValue();
/* 1108 */         if (defaultValue != null && ObjectUtils.nullSafeEquals(attributeValue, defaultValue)) {
/* 1109 */           attributeValue = new DefaultValueHolder(defaultValue);
/*      */         }
/* 1111 */         attributes.put(method.getName(), 
/* 1112 */             adaptValue(annotatedElement, attributeValue, classValuesAsString, nestedAnnotationsAsMap));
/*      */       }
/* 1114 */       catch (Throwable ex) {
/* 1115 */         if (ex instanceof InvocationTargetException) {
/* 1116 */           Throwable targetException = ((InvocationTargetException)ex).getTargetException();
/* 1117 */           rethrowAnnotationConfigurationException(targetException);
/*      */         } 
/* 1119 */         throw new IllegalStateException("Could not obtain annotation attribute value for " + method, ex);
/*      */       } 
/*      */     } 
/*      */     
/* 1123 */     return attributes;
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
/*      */   static Object adaptValue(Object annotatedElement, Object value, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/* 1145 */     if (classValuesAsString) {
/* 1146 */       if (value instanceof Class) {
/* 1147 */         return ((Class)value).getName();
/*      */       }
/* 1149 */       if (value instanceof Class[]) {
/* 1150 */         Class<?>[] clazzArray = (Class[])value;
/* 1151 */         String[] classNames = new String[clazzArray.length];
/* 1152 */         for (int i = 0; i < clazzArray.length; i++) {
/* 1153 */           classNames[i] = clazzArray[i].getName();
/*      */         }
/* 1155 */         return classNames;
/*      */       } 
/*      */     } 
/*      */     
/* 1159 */     if (value instanceof Annotation) {
/* 1160 */       Annotation annotation = (Annotation)value;
/* 1161 */       if (nestedAnnotationsAsMap) {
/* 1162 */         return getAnnotationAttributes(annotatedElement, annotation, classValuesAsString, true);
/*      */       }
/*      */       
/* 1165 */       return synthesizeAnnotation(annotation, annotatedElement);
/*      */     } 
/*      */ 
/*      */     
/* 1169 */     if (value instanceof Annotation[]) {
/* 1170 */       Annotation[] annotations = (Annotation[])value;
/* 1171 */       if (nestedAnnotationsAsMap) {
/* 1172 */         AnnotationAttributes[] mappedAnnotations = new AnnotationAttributes[annotations.length];
/* 1173 */         for (int i = 0; i < annotations.length; i++) {
/* 1174 */           mappedAnnotations[i] = 
/* 1175 */             getAnnotationAttributes(annotatedElement, annotations[i], classValuesAsString, true);
/*      */         }
/* 1177 */         return mappedAnnotations;
/*      */       } 
/*      */       
/* 1180 */       return synthesizeAnnotationArray(annotations, annotatedElement);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1185 */     return value;
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
/*      */   public static void registerDefaultValues(AnnotationAttributes attributes) {
/* 1198 */     Class<? extends Annotation> annotationType = attributes.annotationType();
/* 1199 */     if (annotationType != null && Modifier.isPublic(annotationType.getModifiers()))
/*      */     {
/* 1201 */       for (Method annotationAttribute : getAttributeMethods(annotationType)) {
/* 1202 */         String attributeName = annotationAttribute.getName();
/* 1203 */         Object defaultValue = annotationAttribute.getDefaultValue();
/* 1204 */         if (defaultValue != null && !attributes.containsKey(attributeName)) {
/* 1205 */           if (defaultValue instanceof Annotation) {
/* 1206 */             defaultValue = getAnnotationAttributes((Annotation)defaultValue, false, true);
/*      */           }
/* 1208 */           else if (defaultValue instanceof Annotation[]) {
/* 1209 */             Annotation[] realAnnotations = (Annotation[])defaultValue;
/* 1210 */             AnnotationAttributes[] mappedAnnotations = new AnnotationAttributes[realAnnotations.length];
/* 1211 */             for (int i = 0; i < realAnnotations.length; i++) {
/* 1212 */               mappedAnnotations[i] = getAnnotationAttributes(realAnnotations[i], false, true);
/*      */             }
/* 1214 */             defaultValue = mappedAnnotations;
/*      */           } 
/* 1216 */           attributes.put(attributeName, new DefaultValueHolder(defaultValue));
/*      */         } 
/*      */       } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void postProcessAnnotationAttributes(Object annotatedElement, AnnotationAttributes attributes, boolean classValuesAsString) {
/* 1242 */     postProcessAnnotationAttributes(annotatedElement, attributes, classValuesAsString, false);
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
/*      */ 
/*      */ 
/*      */   
/*      */   static void postProcessAnnotationAttributes(Object annotatedElement, AnnotationAttributes attributes, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/* 1269 */     if (attributes == null) {
/*      */       return;
/*      */     }
/*      */     
/* 1273 */     Class<? extends Annotation> annotationType = attributes.annotationType();
/*      */ 
/*      */ 
/*      */     
/* 1277 */     Set<String> valuesAlreadyReplaced = new HashSet<String>();
/*      */     
/* 1279 */     if (!attributes.validated) {
/*      */       
/* 1281 */       Map<String, List<String>> aliasMap = getAttributeAliasMap(annotationType);
/* 1282 */       for (String attributeName : aliasMap.keySet()) {
/* 1283 */         if (valuesAlreadyReplaced.contains(attributeName)) {
/*      */           continue;
/*      */         }
/* 1286 */         Object value = attributes.get(attributeName);
/* 1287 */         boolean valuePresent = (value != null && !(value instanceof DefaultValueHolder));
/* 1288 */         for (String aliasedAttributeName : aliasMap.get(attributeName)) {
/* 1289 */           if (valuesAlreadyReplaced.contains(aliasedAttributeName)) {
/*      */             continue;
/*      */           }
/* 1292 */           Object aliasedValue = attributes.get(aliasedAttributeName);
/* 1293 */           boolean aliasPresent = (aliasedValue != null && !(aliasedValue instanceof DefaultValueHolder));
/*      */           
/* 1295 */           if (valuePresent || aliasPresent) {
/* 1296 */             if (valuePresent && aliasPresent) {
/*      */               
/* 1298 */               if (!ObjectUtils.nullSafeEquals(value, aliasedValue)) {
/*      */                 
/* 1300 */                 String elementAsString = (annotatedElement != null) ? annotatedElement.toString() : "unknown element";
/* 1301 */                 throw new AnnotationConfigurationException(String.format("In AnnotationAttributes for annotation [%s] declared on %s, attribute '%s' and its alias '%s' are declared with values of [%s] and [%s], but only one is permitted.", new Object[] { annotationType
/*      */ 
/*      */                         
/* 1304 */                         .getName(), elementAsString, attributeName, aliasedAttributeName, 
/* 1305 */                         ObjectUtils.nullSafeToString(value), 
/* 1306 */                         ObjectUtils.nullSafeToString(aliasedValue) }));
/*      */               }  continue;
/*      */             } 
/* 1309 */             if (aliasPresent) {
/*      */               
/* 1311 */               attributes.put(attributeName, 
/* 1312 */                   adaptValue(annotatedElement, aliasedValue, classValuesAsString, nestedAnnotationsAsMap));
/* 1313 */               valuesAlreadyReplaced.add(attributeName);
/*      */               
/*      */               continue;
/*      */             } 
/* 1317 */             attributes.put(aliasedAttributeName, 
/* 1318 */                 adaptValue(annotatedElement, value, classValuesAsString, nestedAnnotationsAsMap));
/* 1319 */             valuesAlreadyReplaced.add(aliasedAttributeName);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 1324 */       attributes.validated = true;
/*      */     } 
/*      */ 
/*      */     
/* 1328 */     for (String attributeName : attributes.keySet()) {
/* 1329 */       if (valuesAlreadyReplaced.contains(attributeName)) {
/*      */         continue;
/*      */       }
/* 1332 */       Object value = attributes.get(attributeName);
/* 1333 */       if (value instanceof DefaultValueHolder) {
/* 1334 */         value = ((DefaultValueHolder)value).defaultValue;
/* 1335 */         attributes.put(attributeName, 
/* 1336 */             adaptValue(annotatedElement, value, classValuesAsString, nestedAnnotationsAsMap));
/*      */       } 
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
/*      */   public static Object getValue(Annotation annotation) {
/* 1351 */     return getValue(annotation, "value");
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
/*      */   public static Object getValue(Annotation annotation, String attributeName) {
/* 1365 */     if (annotation == null || !StringUtils.hasText(attributeName)) {
/* 1366 */       return null;
/*      */     }
/*      */     try {
/* 1369 */       Method method = annotation.annotationType().getDeclaredMethod(attributeName, new Class[0]);
/* 1370 */       ReflectionUtils.makeAccessible(method);
/* 1371 */       return method.invoke(annotation, new Object[0]);
/*      */     }
/* 1373 */     catch (NoSuchMethodException ex) {
/* 1374 */       return null;
/*      */     }
/* 1376 */     catch (InvocationTargetException ex) {
/* 1377 */       rethrowAnnotationConfigurationException(ex.getTargetException());
/* 1378 */       throw new IllegalStateException("Could not obtain value for annotation attribute '" + attributeName + "' in " + annotation, ex);
/*      */     
/*      */     }
/* 1381 */     catch (Throwable ex) {
/* 1382 */       handleIntrospectionFailure(annotation.getClass(), ex);
/* 1383 */       return null;
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
/*      */   public static Object getDefaultValue(Annotation annotation) {
/* 1395 */     return getDefaultValue(annotation, "value");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object getDefaultValue(Annotation annotation, String attributeName) {
/* 1406 */     if (annotation == null) {
/* 1407 */       return null;
/*      */     }
/* 1409 */     return getDefaultValue(annotation.annotationType(), attributeName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object getDefaultValue(Class<? extends Annotation> annotationType) {
/* 1420 */     return getDefaultValue(annotationType, "value");
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
/*      */   public static Object getDefaultValue(Class<? extends Annotation> annotationType, String attributeName) {
/* 1432 */     if (annotationType == null || !StringUtils.hasText(attributeName)) {
/* 1433 */       return null;
/*      */     }
/*      */     try {
/* 1436 */       return annotationType.getDeclaredMethod(attributeName, new Class[0]).getDefaultValue();
/*      */     }
/* 1438 */     catch (Throwable ex) {
/* 1439 */       handleIntrospectionFailure(annotationType, ex);
/* 1440 */       return null;
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
/*      */   static <A extends Annotation> A synthesizeAnnotation(A annotation) {
/* 1459 */     return synthesizeAnnotation(annotation, (AnnotatedElement)null);
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
/*      */   public static <A extends Annotation> A synthesizeAnnotation(A annotation, AnnotatedElement annotatedElement) {
/* 1480 */     return synthesizeAnnotation(annotation, annotatedElement);
/*      */   }
/*      */ 
/*      */   
/*      */   static <A extends Annotation> A synthesizeAnnotation(A annotation, Object annotatedElement) {
/* 1485 */     if (annotation == null) {
/* 1486 */       return null;
/*      */     }
/* 1488 */     if (annotation instanceof SynthesizedAnnotation) {
/* 1489 */       return annotation;
/*      */     }
/*      */     
/* 1492 */     Class<? extends Annotation> annotationType = annotation.annotationType();
/* 1493 */     if (!isSynthesizable(annotationType)) {
/* 1494 */       return annotation;
/*      */     }
/*      */     
/* 1497 */     DefaultAnnotationAttributeExtractor attributeExtractor = new DefaultAnnotationAttributeExtractor((Annotation)annotation, annotatedElement);
/*      */     
/* 1499 */     InvocationHandler handler = new SynthesizedAnnotationInvocationHandler(attributeExtractor);
/*      */ 
/*      */ 
/*      */     
/* 1503 */     Class<?>[] exposedInterfaces = new Class[] { annotationType, SynthesizedAnnotation.class };
/* 1504 */     return (A)Proxy.newProxyInstance(annotation.getClass().getClassLoader(), exposedInterfaces, handler);
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
/*      */   public static <A extends Annotation> A synthesizeAnnotation(Map<String, Object> attributes, Class<A> annotationType, AnnotatedElement annotatedElement) {
/* 1541 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/* 1542 */     if (attributes == null) {
/* 1543 */       return null;
/*      */     }
/*      */     
/* 1546 */     MapAnnotationAttributeExtractor attributeExtractor = new MapAnnotationAttributeExtractor(attributes, annotationType, annotatedElement);
/*      */     
/* 1548 */     InvocationHandler handler = new SynthesizedAnnotationInvocationHandler(attributeExtractor);
/* 1549 */     (new Class[2])[0] = annotationType; (new Class[2])[1] = SynthesizedAnnotation.class; (new Class[1])[0] = annotationType; Class<?>[] exposedInterfaces = canExposeSynthesizedMarker(annotationType) ? new Class[2] : new Class[1];
/*      */     
/* 1551 */     return (A)Proxy.newProxyInstance(annotationType.getClassLoader(), exposedInterfaces, handler);
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
/*      */   public static <A extends Annotation> A synthesizeAnnotation(Class<A> annotationType) {
/* 1570 */     return synthesizeAnnotation(Collections.emptyMap(), annotationType, null);
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
/*      */   static Annotation[] synthesizeAnnotationArray(Annotation[] annotations, Object annotatedElement) {
/* 1590 */     if (annotations == null) {
/* 1591 */       return null;
/*      */     }
/*      */     
/* 1594 */     Annotation[] synthesized = (Annotation[])Array.newInstance(annotations
/* 1595 */         .getClass().getComponentType(), annotations.length);
/* 1596 */     for (int i = 0; i < annotations.length; i++) {
/* 1597 */       synthesized[i] = synthesizeAnnotation(annotations[i], annotatedElement);
/*      */     }
/* 1599 */     return synthesized;
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
/*      */   static <A extends Annotation> A[] synthesizeAnnotationArray(Map<String, Object>[] maps, Class<A> annotationType) {
/* 1621 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/* 1622 */     if (maps == null) {
/* 1623 */       return null;
/*      */     }
/*      */     
/* 1626 */     Annotation[] arrayOfAnnotation = (Annotation[])Array.newInstance(annotationType, maps.length);
/* 1627 */     for (int i = 0; i < maps.length; i++) {
/* 1628 */       arrayOfAnnotation[i] = synthesizeAnnotation(maps[i], annotationType, null);
/*      */     }
/* 1630 */     return (A[])arrayOfAnnotation;
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
/*      */   static Map<String, List<String>> getAttributeAliasMap(Class<? extends Annotation> annotationType) {
/* 1653 */     if (annotationType == null) {
/* 1654 */       return Collections.emptyMap();
/*      */     }
/*      */     
/* 1657 */     Map<String, List<String>> map = attributeAliasesCache.get(annotationType);
/* 1658 */     if (map != null) {
/* 1659 */       return map;
/*      */     }
/*      */     
/* 1662 */     map = new LinkedHashMap<String, List<String>>();
/* 1663 */     for (Method attribute : getAttributeMethods(annotationType)) {
/* 1664 */       List<String> aliasNames = getAttributeAliasNames(attribute);
/* 1665 */       if (!aliasNames.isEmpty()) {
/* 1666 */         map.put(attribute.getName(), aliasNames);
/*      */       }
/*      */     } 
/*      */     
/* 1670 */     attributeAliasesCache.put(annotationType, map);
/* 1671 */     return map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean canExposeSynthesizedMarker(Class<? extends Annotation> annotationType) {
/*      */     try {
/* 1680 */       return (Class.forName(SynthesizedAnnotation.class.getName(), false, annotationType.getClassLoader()) == SynthesizedAnnotation.class);
/*      */     
/*      */     }
/* 1683 */     catch (ClassNotFoundException ex) {
/* 1684 */       return false;
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
/*      */   private static boolean isSynthesizable(Class<? extends Annotation> annotationType) {
/* 1703 */     Boolean synthesizable = synthesizableCache.get(annotationType);
/* 1704 */     if (synthesizable != null) {
/* 1705 */       return synthesizable.booleanValue();
/*      */     }
/*      */     
/* 1708 */     synthesizable = Boolean.FALSE;
/* 1709 */     for (Method attribute : getAttributeMethods(annotationType)) {
/* 1710 */       if (!getAttributeAliasNames(attribute).isEmpty()) {
/* 1711 */         synthesizable = Boolean.TRUE;
/*      */         break;
/*      */       } 
/* 1714 */       Class<?> returnType = attribute.getReturnType();
/* 1715 */       if (Annotation[].class.isAssignableFrom(returnType)) {
/*      */         
/* 1717 */         Class<? extends Annotation> nestedAnnotationType = (Class)returnType.getComponentType();
/* 1718 */         if (isSynthesizable(nestedAnnotationType)) {
/* 1719 */           synthesizable = Boolean.TRUE; break;
/*      */         } 
/*      */         continue;
/*      */       } 
/* 1723 */       if (Annotation.class.isAssignableFrom(returnType)) {
/* 1724 */         Class<? extends Annotation> nestedAnnotationType = (Class)returnType;
/* 1725 */         if (isSynthesizable(nestedAnnotationType)) {
/* 1726 */           synthesizable = Boolean.TRUE;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1732 */     synthesizableCache.put(annotationType, synthesizable);
/* 1733 */     return synthesizable.booleanValue();
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
/*      */   static List<String> getAttributeAliasNames(Method attribute) {
/* 1750 */     Assert.notNull(attribute, "attribute must not be null");
/* 1751 */     AliasDescriptor descriptor = AliasDescriptor.from(attribute);
/* 1752 */     return (descriptor != null) ? descriptor.getAttributeAliasNames() : Collections.<String>emptyList();
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
/*      */   static String getAttributeOverrideName(Method attribute, Class<? extends Annotation> metaAnnotationType) {
/* 1772 */     Assert.notNull(attribute, "attribute must not be null");
/* 1773 */     Assert.notNull(metaAnnotationType, "metaAnnotationType must not be null");
/* 1774 */     Assert.isTrue((Annotation.class != metaAnnotationType), "metaAnnotationType must not be [java.lang.annotation.Annotation]");
/*      */ 
/*      */     
/* 1777 */     AliasDescriptor descriptor = AliasDescriptor.from(attribute);
/* 1778 */     return (descriptor != null) ? descriptor.getAttributeOverrideName(metaAnnotationType) : null;
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
/*      */   static List<Method> getAttributeMethods(Class<? extends Annotation> annotationType) {
/* 1793 */     List<Method> methods = attributeMethodsCache.get(annotationType);
/* 1794 */     if (methods != null) {
/* 1795 */       return methods;
/*      */     }
/*      */     
/* 1798 */     methods = new ArrayList<Method>();
/* 1799 */     for (Method method : annotationType.getDeclaredMethods()) {
/* 1800 */       if (isAttributeMethod(method)) {
/* 1801 */         ReflectionUtils.makeAccessible(method);
/* 1802 */         methods.add(method);
/*      */       } 
/*      */     } 
/*      */     
/* 1806 */     attributeMethodsCache.put(annotationType, methods);
/* 1807 */     return methods;
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
/*      */   static Annotation getAnnotation(AnnotatedElement element, String annotationName) {
/* 1820 */     for (Annotation annotation : element.getAnnotations()) {
/* 1821 */       if (annotation.annotationType().getName().equals(annotationName)) {
/* 1822 */         return annotation;
/*      */       }
/*      */     } 
/* 1825 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean isAttributeMethod(Method method) {
/* 1835 */     return (method != null && (method.getParameterTypes()).length == 0 && method.getReturnType() != void.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean isAnnotationTypeMethod(Method method) {
/* 1845 */     return (method != null && method.getName().equals("annotationType") && (method.getParameterTypes()).length == 0);
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
/*      */   static Class<? extends Annotation> resolveContainerAnnotationType(Class<? extends Annotation> annotationType) {
/*      */     try {
/* 1859 */       Annotation repeatable = getAnnotation(annotationType, "java.lang.annotation.Repeatable");
/* 1860 */       if (repeatable != null) {
/* 1861 */         Object value = getValue(repeatable);
/* 1862 */         return (Class<? extends Annotation>)value;
/*      */       }
/*      */     
/* 1865 */     } catch (Exception ex) {
/* 1866 */       handleIntrospectionFailure(annotationType, ex);
/*      */     } 
/* 1868 */     return null;
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
/*      */   static void rethrowAnnotationConfigurationException(Throwable ex) {
/* 1880 */     if (ex instanceof AnnotationConfigurationException) {
/* 1881 */       throw (AnnotationConfigurationException)ex;
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
/*      */   static void handleIntrospectionFailure(AnnotatedElement element, Throwable ex) {
/* 1900 */     rethrowAnnotationConfigurationException(ex);
/*      */     
/* 1902 */     Log loggerToUse = logger;
/* 1903 */     if (loggerToUse == null) {
/* 1904 */       loggerToUse = LogFactory.getLog(AnnotationUtils.class);
/* 1905 */       logger = loggerToUse;
/*      */     } 
/* 1907 */     if (element instanceof Class && Annotation.class.isAssignableFrom((Class)element)) {
/*      */       
/* 1909 */       if (loggerToUse.isDebugEnabled()) {
/* 1910 */         loggerToUse.debug("Failed to meta-introspect annotation " + element + ": " + ex);
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1915 */     else if (loggerToUse.isInfoEnabled()) {
/* 1916 */       loggerToUse.info("Failed to introspect annotations on " + element + ": " + ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void clearCache() {
/* 1926 */     findAnnotationCache.clear();
/* 1927 */     metaPresentCache.clear();
/* 1928 */     annotatedInterfaceCache.clear();
/* 1929 */     synthesizableCache.clear();
/* 1930 */     attributeAliasesCache.clear();
/* 1931 */     attributeMethodsCache.clear();
/* 1932 */     aliasDescriptorCache.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class AnnotationCacheKey
/*      */     implements Comparable<AnnotationCacheKey>
/*      */   {
/*      */     private final AnnotatedElement element;
/*      */     
/*      */     private final Class<? extends Annotation> annotationType;
/*      */ 
/*      */     
/*      */     public AnnotationCacheKey(AnnotatedElement element, Class<? extends Annotation> annotationType) {
/* 1946 */       this.element = element;
/* 1947 */       this.annotationType = annotationType;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object other) {
/* 1952 */       if (this == other) {
/* 1953 */         return true;
/*      */       }
/* 1955 */       if (!(other instanceof AnnotationCacheKey)) {
/* 1956 */         return false;
/*      */       }
/* 1958 */       AnnotationCacheKey otherKey = (AnnotationCacheKey)other;
/* 1959 */       return (this.element.equals(otherKey.element) && this.annotationType.equals(otherKey.annotationType));
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1964 */       return this.element.hashCode() * 29 + this.annotationType.hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1969 */       return "@" + this.annotationType + " on " + this.element;
/*      */     }
/*      */ 
/*      */     
/*      */     public int compareTo(AnnotationCacheKey other) {
/* 1974 */       int result = this.element.toString().compareTo(other.element.toString());
/* 1975 */       if (result == 0) {
/* 1976 */         result = this.annotationType.getName().compareTo(other.annotationType.getName());
/*      */       }
/* 1978 */       return result;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class AnnotationCollector<A extends Annotation>
/*      */   {
/*      */     private final Class<A> annotationType;
/*      */     
/*      */     private final Class<? extends Annotation> containerAnnotationType;
/*      */     
/*      */     private final boolean declaredMode;
/*      */     
/* 1991 */     private final Set<AnnotatedElement> visited = new HashSet<AnnotatedElement>();
/*      */     
/* 1993 */     private final Set<A> result = new LinkedHashSet<A>();
/*      */     
/*      */     AnnotationCollector(Class<A> annotationType, Class<? extends Annotation> containerAnnotationType, boolean declaredMode) {
/* 1996 */       this.annotationType = annotationType;
/* 1997 */       this
/* 1998 */         .containerAnnotationType = (containerAnnotationType != null) ? containerAnnotationType : AnnotationUtils.resolveContainerAnnotationType(annotationType);
/* 1999 */       this.declaredMode = declaredMode;
/*      */     }
/*      */     
/*      */     Set<A> getResult(AnnotatedElement element) {
/* 2003 */       process(element);
/* 2004 */       return Collections.unmodifiableSet(this.result);
/*      */     }
/*      */ 
/*      */     
/*      */     private void process(AnnotatedElement element) {
/* 2009 */       if (this.visited.add(element)) {
/*      */         try {
/* 2011 */           Annotation[] annotations = this.declaredMode ? element.getDeclaredAnnotations() : element.getAnnotations();
/* 2012 */           for (Annotation ann : annotations) {
/* 2013 */             Class<? extends Annotation> currentAnnotationType = ann.annotationType();
/* 2014 */             if (ObjectUtils.nullSafeEquals(this.annotationType, currentAnnotationType)) {
/* 2015 */               this.result.add(AnnotationUtils.synthesizeAnnotation((A)ann, element));
/*      */             }
/* 2017 */             else if (ObjectUtils.nullSafeEquals(this.containerAnnotationType, currentAnnotationType)) {
/* 2018 */               this.result.addAll(getValue(element, ann));
/*      */             }
/* 2020 */             else if (!AnnotationUtils.isInJavaLangAnnotationPackage(currentAnnotationType)) {
/* 2021 */               process(currentAnnotationType);
/*      */             }
/*      */           
/*      */           } 
/* 2025 */         } catch (Throwable ex) {
/* 2026 */           AnnotationUtils.handleIntrospectionFailure(element, ex);
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     private List<A> getValue(AnnotatedElement element, Annotation annotation) {
/*      */       try {
/* 2034 */         List<A> synthesizedAnnotations = new ArrayList<A>();
/* 2035 */         for (Annotation annotation1 : (Annotation[])AnnotationUtils.getValue(annotation)) {
/* 2036 */           synthesizedAnnotations.add(AnnotationUtils.synthesizeAnnotation((A)annotation1, element));
/*      */         }
/* 2038 */         return synthesizedAnnotations;
/*      */       }
/* 2040 */       catch (Throwable ex) {
/* 2041 */         AnnotationUtils.handleIntrospectionFailure(element, ex);
/*      */ 
/*      */         
/* 2044 */         return Collections.emptyList();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class AliasDescriptor
/*      */   {
/*      */     private final Method sourceAttribute;
/*      */ 
/*      */ 
/*      */     
/*      */     private final Class<? extends Annotation> sourceAnnotationType;
/*      */ 
/*      */ 
/*      */     
/*      */     private final String sourceAttributeName;
/*      */ 
/*      */ 
/*      */     
/*      */     private final Method aliasedAttribute;
/*      */ 
/*      */ 
/*      */     
/*      */     private final Class<? extends Annotation> aliasedAnnotationType;
/*      */ 
/*      */ 
/*      */     
/*      */     private final String aliasedAttributeName;
/*      */ 
/*      */ 
/*      */     
/*      */     private final boolean isAliasPair;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static AliasDescriptor from(Method attribute) {
/* 2085 */       AliasDescriptor descriptor = (AliasDescriptor)AnnotationUtils.aliasDescriptorCache.get(attribute);
/* 2086 */       if (descriptor != null) {
/* 2087 */         return descriptor;
/*      */       }
/*      */       
/* 2090 */       AliasFor aliasFor = attribute.<AliasFor>getAnnotation(AliasFor.class);
/* 2091 */       if (aliasFor == null) {
/* 2092 */         return null;
/*      */       }
/*      */       
/* 2095 */       descriptor = new AliasDescriptor(attribute, aliasFor);
/* 2096 */       descriptor.validate();
/* 2097 */       AnnotationUtils.aliasDescriptorCache.put(attribute, descriptor);
/* 2098 */       return descriptor;
/*      */     }
/*      */ 
/*      */     
/*      */     private AliasDescriptor(Method sourceAttribute, AliasFor aliasFor) {
/* 2103 */       Class<?> declaringClass = sourceAttribute.getDeclaringClass();
/* 2104 */       Assert.isTrue(declaringClass.isAnnotation(), "sourceAttribute must be from an annotation");
/*      */       
/* 2106 */       this.sourceAttribute = sourceAttribute;
/* 2107 */       this.sourceAnnotationType = (Class)declaringClass;
/* 2108 */       this.sourceAttributeName = sourceAttribute.getName();
/*      */       
/* 2110 */       this
/* 2111 */         .aliasedAnnotationType = (Annotation.class == aliasFor.annotation()) ? this.sourceAnnotationType : aliasFor.annotation();
/* 2112 */       this.aliasedAttributeName = getAliasedAttributeName(aliasFor, sourceAttribute);
/* 2113 */       if (this.aliasedAnnotationType == this.sourceAnnotationType && this.aliasedAttributeName
/* 2114 */         .equals(this.sourceAttributeName)) {
/* 2115 */         String msg = String.format("@AliasFor declaration on attribute '%s' in annotation [%s] points to itself. Specify 'annotation' to point to a same-named attribute on a meta-annotation.", new Object[] { sourceAttribute
/*      */               
/* 2117 */               .getName(), declaringClass.getName() });
/* 2118 */         throw new AnnotationConfigurationException(msg);
/*      */       } 
/*      */       try {
/* 2121 */         this.aliasedAttribute = this.aliasedAnnotationType.getDeclaredMethod(this.aliasedAttributeName, new Class[0]);
/*      */       }
/* 2123 */       catch (NoSuchMethodException ex) {
/* 2124 */         String msg = String.format("Attribute '%s' in annotation [%s] is declared as an @AliasFor nonexistent attribute '%s' in annotation [%s].", new Object[] { this.sourceAttributeName, this.sourceAnnotationType
/*      */               
/* 2126 */               .getName(), this.aliasedAttributeName, this.aliasedAnnotationType
/* 2127 */               .getName() });
/* 2128 */         throw new AnnotationConfigurationException(msg, ex);
/*      */       } 
/*      */       
/* 2131 */       this.isAliasPair = (this.sourceAnnotationType == this.aliasedAnnotationType);
/*      */     }
/*      */ 
/*      */     
/*      */     private void validate() {
/* 2136 */       if (!this.isAliasPair && !AnnotationUtils.isAnnotationMetaPresent(this.sourceAnnotationType, this.aliasedAnnotationType)) {
/* 2137 */         String msg = String.format("@AliasFor declaration on attribute '%s' in annotation [%s] declares an alias for attribute '%s' in meta-annotation [%s] which is not meta-present.", new Object[] { this.sourceAttributeName, this.sourceAnnotationType
/*      */               
/* 2139 */               .getName(), this.aliasedAttributeName, this.aliasedAnnotationType
/* 2140 */               .getName() });
/* 2141 */         throw new AnnotationConfigurationException(msg);
/*      */       } 
/*      */       
/* 2144 */       if (this.isAliasPair) {
/* 2145 */         AliasFor mirrorAliasFor = this.aliasedAttribute.<AliasFor>getAnnotation(AliasFor.class);
/* 2146 */         if (mirrorAliasFor == null) {
/* 2147 */           String msg = String.format("Attribute '%s' in annotation [%s] must be declared as an @AliasFor [%s].", new Object[] { this.aliasedAttributeName, this.sourceAnnotationType
/* 2148 */                 .getName(), this.sourceAttributeName });
/* 2149 */           throw new AnnotationConfigurationException(msg);
/*      */         } 
/*      */         
/* 2152 */         String mirrorAliasedAttributeName = getAliasedAttributeName(mirrorAliasFor, this.aliasedAttribute);
/* 2153 */         if (!this.sourceAttributeName.equals(mirrorAliasedAttributeName)) {
/* 2154 */           String msg = String.format("Attribute '%s' in annotation [%s] must be declared as an @AliasFor [%s], not [%s].", new Object[] { this.aliasedAttributeName, this.sourceAnnotationType
/* 2155 */                 .getName(), this.sourceAttributeName, mirrorAliasedAttributeName });
/*      */           
/* 2157 */           throw new AnnotationConfigurationException(msg);
/*      */         } 
/*      */       } 
/*      */       
/* 2161 */       Class<?> returnType = this.sourceAttribute.getReturnType();
/* 2162 */       Class<?> aliasedReturnType = this.aliasedAttribute.getReturnType();
/* 2163 */       if (returnType != aliasedReturnType && (
/* 2164 */         !aliasedReturnType.isArray() || returnType != aliasedReturnType.getComponentType())) {
/* 2165 */         String msg = String.format("Misconfigured aliases: attribute '%s' in annotation [%s] and attribute '%s' in annotation [%s] must declare the same return type.", new Object[] { this.sourceAttributeName, this.sourceAnnotationType
/*      */               
/* 2167 */               .getName(), this.aliasedAttributeName, this.aliasedAnnotationType
/* 2168 */               .getName() });
/* 2169 */         throw new AnnotationConfigurationException(msg);
/*      */       } 
/*      */       
/* 2172 */       if (this.isAliasPair) {
/* 2173 */         validateDefaultValueConfiguration(this.aliasedAttribute);
/*      */       }
/*      */     }
/*      */     
/*      */     private void validateDefaultValueConfiguration(Method aliasedAttribute) {
/* 2178 */       Assert.notNull(aliasedAttribute, "aliasedAttribute must not be null");
/* 2179 */       Object defaultValue = this.sourceAttribute.getDefaultValue();
/* 2180 */       Object aliasedDefaultValue = aliasedAttribute.getDefaultValue();
/*      */       
/* 2182 */       if (defaultValue == null || aliasedDefaultValue == null) {
/* 2183 */         String msg = String.format("Misconfigured aliases: attribute '%s' in annotation [%s] and attribute '%s' in annotation [%s] must declare default values.", new Object[] { this.sourceAttributeName, this.sourceAnnotationType
/*      */               
/* 2185 */               .getName(), aliasedAttribute.getName(), aliasedAttribute
/* 2186 */               .getDeclaringClass().getName() });
/* 2187 */         throw new AnnotationConfigurationException(msg);
/*      */       } 
/*      */       
/* 2190 */       if (!ObjectUtils.nullSafeEquals(defaultValue, aliasedDefaultValue)) {
/* 2191 */         String msg = String.format("Misconfigured aliases: attribute '%s' in annotation [%s] and attribute '%s' in annotation [%s] must declare the same default value.", new Object[] { this.sourceAttributeName, this.sourceAnnotationType
/*      */               
/* 2193 */               .getName(), aliasedAttribute.getName(), aliasedAttribute
/* 2194 */               .getDeclaringClass().getName() });
/* 2195 */         throw new AnnotationConfigurationException(msg);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void validateAgainst(AliasDescriptor otherDescriptor) {
/* 2206 */       validateDefaultValueConfiguration(otherDescriptor.sourceAttribute);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean isOverrideFor(Class<? extends Annotation> metaAnnotationType) {
/* 2215 */       return (this.aliasedAnnotationType == metaAnnotationType);
/*      */     }
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
/*      */     private boolean isAliasFor(AliasDescriptor otherDescriptor) {
/* 2230 */       for (AliasDescriptor lhs = this; lhs != null; lhs = lhs.getAttributeOverrideDescriptor()) {
/* 2231 */         for (AliasDescriptor rhs = otherDescriptor; rhs != null; rhs = rhs.getAttributeOverrideDescriptor()) {
/* 2232 */           if (lhs.aliasedAttribute.equals(rhs.aliasedAttribute)) {
/* 2233 */             return true;
/*      */           }
/*      */         } 
/*      */       } 
/* 2237 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public List<String> getAttributeAliasNames() {
/* 2242 */       if (this.isAliasPair) {
/* 2243 */         return Collections.singletonList(this.aliasedAttributeName);
/*      */       }
/*      */ 
/*      */       
/* 2247 */       List<String> aliases = new ArrayList<String>();
/* 2248 */       for (AliasDescriptor otherDescriptor : getOtherDescriptors()) {
/* 2249 */         if (isAliasFor(otherDescriptor)) {
/* 2250 */           validateAgainst(otherDescriptor);
/* 2251 */           aliases.add(otherDescriptor.sourceAttributeName);
/*      */         } 
/*      */       } 
/* 2254 */       return aliases;
/*      */     }
/*      */     
/*      */     private List<AliasDescriptor> getOtherDescriptors() {
/* 2258 */       List<AliasDescriptor> otherDescriptors = new ArrayList<AliasDescriptor>();
/* 2259 */       for (Method currentAttribute : AnnotationUtils.getAttributeMethods(this.sourceAnnotationType)) {
/* 2260 */         if (!this.sourceAttribute.equals(currentAttribute)) {
/* 2261 */           AliasDescriptor otherDescriptor = from(currentAttribute);
/* 2262 */           if (otherDescriptor != null) {
/* 2263 */             otherDescriptors.add(otherDescriptor);
/*      */           }
/*      */         } 
/*      */       } 
/* 2267 */       return otherDescriptors;
/*      */     }
/*      */     
/*      */     public String getAttributeOverrideName(Class<? extends Annotation> metaAnnotationType) {
/* 2271 */       Assert.notNull(metaAnnotationType, "metaAnnotationType must not be null");
/* 2272 */       Assert.isTrue((Annotation.class != metaAnnotationType), "metaAnnotationType must not be [java.lang.annotation.Annotation]");
/*      */ 
/*      */ 
/*      */       
/* 2276 */       for (AliasDescriptor desc = this; desc != null; desc = desc.getAttributeOverrideDescriptor()) {
/* 2277 */         if (desc.isOverrideFor(metaAnnotationType)) {
/* 2278 */           return desc.aliasedAttributeName;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 2283 */       return null;
/*      */     }
/*      */     
/*      */     private AliasDescriptor getAttributeOverrideDescriptor() {
/* 2287 */       if (this.isAliasPair) {
/* 2288 */         return null;
/*      */       }
/* 2290 */       return from(this.aliasedAttribute);
/*      */     }
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
/*      */     private String getAliasedAttributeName(AliasFor aliasFor, Method attribute) {
/* 2310 */       String attributeName = aliasFor.attribute();
/* 2311 */       String value = aliasFor.value();
/* 2312 */       boolean attributeDeclared = StringUtils.hasText(attributeName);
/* 2313 */       boolean valueDeclared = StringUtils.hasText(value);
/*      */ 
/*      */       
/* 2316 */       if (attributeDeclared && valueDeclared) {
/* 2317 */         String msg = String.format("In @AliasFor declared on attribute '%s' in annotation [%s], attribute 'attribute' and its alias 'value' are present with values of [%s] and [%s], but only one is permitted.", new Object[] { attribute
/*      */               
/* 2319 */               .getName(), attribute.getDeclaringClass().getName(), attributeName, value });
/* 2320 */         throw new AnnotationConfigurationException(msg);
/*      */       } 
/*      */ 
/*      */       
/* 2324 */       attributeName = attributeDeclared ? attributeName : value;
/* 2325 */       return StringUtils.hasText(attributeName) ? attributeName.trim() : attribute.getName();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 2330 */       return String.format("%s: @%s(%s) is an alias for @%s(%s)", new Object[] { getClass().getSimpleName(), this.sourceAnnotationType
/* 2331 */             .getSimpleName(), this.sourceAttributeName, this.aliasedAnnotationType
/* 2332 */             .getSimpleName(), this.aliasedAttributeName });
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class DefaultValueHolder
/*      */   {
/*      */     final Object defaultValue;
/*      */     
/*      */     public DefaultValueHolder(Object defaultValue) {
/* 2342 */       this.defaultValue = defaultValue;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\annotation\AnnotationUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */