/*      */ package org.springframework.core.annotation;
/*      */ 
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.reflect.AnnotatedElement;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.springframework.core.BridgeMethodResolver;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.CollectionUtils;
/*      */ import org.springframework.util.LinkedMultiValueMap;
/*      */ import org.springframework.util.MultiValueMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class AnnotatedElementUtils
/*      */ {
/*  101 */   private static final Boolean CONTINUE = null;
/*      */   
/*  103 */   private static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];
/*      */   
/*  105 */   private static final Processor<Boolean> alwaysTrueAnnotationProcessor = new AlwaysTrueBooleanAnnotationProcessor();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AnnotatedElement forAnnotations(Annotation... annotations) {
/*  115 */     return new AnnotatedElement()
/*      */       {
/*      */         public <T extends Annotation> T getAnnotation(Class<T> annotationClass)
/*      */         {
/*  119 */           for (Annotation ann : annotations) {
/*  120 */             if (ann.annotationType() == annotationClass) {
/*  121 */               return (T)ann;
/*      */             }
/*      */           } 
/*  124 */           return null;
/*      */         }
/*      */         
/*      */         public Annotation[] getAnnotations() {
/*  128 */           return annotations;
/*      */         }
/*      */         
/*      */         public Annotation[] getDeclaredAnnotations() {
/*  132 */           return annotations;
/*      */         }
/*      */       };
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
/*      */   public static Set<String> getMetaAnnotationTypes(AnnotatedElement element, Class<? extends Annotation> annotationType) {
/*  152 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*  153 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*      */     
/*  155 */     return getMetaAnnotationTypes(element, element.getAnnotation((Class)annotationType));
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
/*      */   public static Set<String> getMetaAnnotationTypes(AnnotatedElement element, String annotationName) {
/*  173 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*  174 */     Assert.hasLength(annotationName, "'annotationName' must not be null or empty");
/*      */     
/*  176 */     return getMetaAnnotationTypes(element, AnnotationUtils.getAnnotation(element, annotationName));
/*      */   }
/*      */   
/*      */   private static Set<String> getMetaAnnotationTypes(AnnotatedElement element, Annotation composed) {
/*  180 */     if (composed == null) {
/*  181 */       return null;
/*      */     }
/*      */     
/*      */     try {
/*  185 */       final Set<String> types = new LinkedHashSet<String>();
/*  186 */       searchWithGetSemantics(composed.annotationType(), null, null, null, new SimpleAnnotationProcessor(true)
/*      */           {
/*      */             public Object process(AnnotatedElement annotatedElement, Annotation annotation, int metaDepth) {
/*  189 */               types.add(annotation.annotationType().getName());
/*  190 */               return AnnotatedElementUtils.CONTINUE;
/*      */             }
/*      */           }new HashSet<AnnotatedElement>(), 1);
/*  193 */       return !types.isEmpty() ? types : null;
/*      */     }
/*  195 */     catch (Throwable ex) {
/*  196 */       AnnotationUtils.rethrowAnnotationConfigurationException(ex);
/*  197 */       throw new IllegalStateException("Failed to introspect annotations on " + element, ex);
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
/*      */   public static boolean hasMetaAnnotationTypes(AnnotatedElement element, Class<? extends Annotation> annotationType) {
/*  214 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*  215 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*      */     
/*  217 */     return hasMetaAnnotationTypes(element, annotationType, null);
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
/*      */   public static boolean hasMetaAnnotationTypes(AnnotatedElement element, String annotationName) {
/*  233 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*  234 */     Assert.hasLength(annotationName, "'annotationName' must not be null or empty");
/*      */     
/*  236 */     return hasMetaAnnotationTypes(element, null, annotationName);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean hasMetaAnnotationTypes(AnnotatedElement element, Class<? extends Annotation> annotationType, String annotationName) {
/*  242 */     return Boolean.TRUE.equals(
/*  243 */         searchWithGetSemantics(element, annotationType, annotationName, new SimpleAnnotationProcessor<Boolean>()
/*      */           {
/*      */             public Boolean process(AnnotatedElement annotatedElement, Annotation annotation, int metaDepth) {
/*  246 */               return (metaDepth > 0) ? Boolean.TRUE : AnnotatedElementUtils.CONTINUE;
/*      */             }
/*      */           }));
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
/*      */   public static boolean isAnnotated(AnnotatedElement element, Class<? extends Annotation> annotationType) {
/*  266 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*  267 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*      */ 
/*      */     
/*  270 */     if (element.isAnnotationPresent(annotationType)) {
/*  271 */       return true;
/*      */     }
/*  273 */     return Boolean.TRUE.equals(searchWithGetSemantics(element, annotationType, null, alwaysTrueAnnotationProcessor));
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
/*      */   public static boolean isAnnotated(AnnotatedElement element, String annotationName) {
/*  289 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*  290 */     Assert.hasLength(annotationName, "'annotationName' must not be null or empty");
/*      */     
/*  292 */     return Boolean.TRUE.equals(searchWithGetSemantics(element, null, annotationName, alwaysTrueAnnotationProcessor));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static AnnotationAttributes getAnnotationAttributes(AnnotatedElement element, String annotationName) {
/*  300 */     return getMergedAnnotationAttributes(element, annotationName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static AnnotationAttributes getAnnotationAttributes(AnnotatedElement element, String annotationName, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/*  310 */     return getMergedAnnotationAttributes(element, annotationName, classValuesAsString, nestedAnnotationsAsMap);
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
/*      */   public static AnnotationAttributes getMergedAnnotationAttributes(AnnotatedElement element, Class<? extends Annotation> annotationType) {
/*  333 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*  334 */     AnnotationAttributes attributes = searchWithGetSemantics(element, annotationType, null, new MergedAnnotationAttributesProcessor());
/*      */     
/*  336 */     AnnotationUtils.postProcessAnnotationAttributes(element, attributes, false, false);
/*  337 */     return attributes;
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
/*      */   public static AnnotationAttributes getMergedAnnotationAttributes(AnnotatedElement element, String annotationName) {
/*  359 */     return getMergedAnnotationAttributes(element, annotationName, false, false);
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
/*      */   public static AnnotationAttributes getMergedAnnotationAttributes(AnnotatedElement element, String annotationName, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/*  391 */     Assert.hasLength(annotationName, "'annotationName' must not be null or empty");
/*  392 */     AnnotationAttributes attributes = searchWithGetSemantics(element, null, annotationName, new MergedAnnotationAttributesProcessor(classValuesAsString, nestedAnnotationsAsMap));
/*      */     
/*  394 */     AnnotationUtils.postProcessAnnotationAttributes(element, attributes, classValuesAsString, nestedAnnotationsAsMap);
/*  395 */     return attributes;
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
/*      */   public static <A extends Annotation> A getMergedAnnotation(AnnotatedElement element, Class<A> annotationType) {
/*  417 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*      */ 
/*      */     
/*  420 */     if (!(element instanceof Class)) {
/*      */ 
/*      */       
/*  423 */       A annotation = element.getAnnotation(annotationType);
/*  424 */       if (annotation != null) {
/*  425 */         return AnnotationUtils.synthesizeAnnotation(annotation, element);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  430 */     AnnotationAttributes attributes = getMergedAnnotationAttributes(element, annotationType);
/*  431 */     return AnnotationUtils.synthesizeAnnotation(attributes, annotationType, element);
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
/*      */   public static <A extends Annotation> Set<A> getAllMergedAnnotations(AnnotatedElement element, Class<A> annotationType) {
/*  457 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*  458 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*      */     
/*  460 */     MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(false, false, true);
/*  461 */     searchWithGetSemantics(element, annotationType, null, processor);
/*  462 */     return postProcessAndSynthesizeAggregatedResults(element, annotationType, processor.getAggregatedResults());
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
/*      */   public static <A extends Annotation> Set<A> getMergedRepeatableAnnotations(AnnotatedElement element, Class<A> annotationType) {
/*  492 */     return getMergedRepeatableAnnotations(element, annotationType, null);
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
/*      */   public static <A extends Annotation> Set<A> getMergedRepeatableAnnotations(AnnotatedElement element, Class<A> annotationType, Class<? extends Annotation> containerType) {
/*  524 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*  525 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*      */     
/*  527 */     if (containerType == null) {
/*  528 */       containerType = resolveContainerType(annotationType);
/*      */     } else {
/*      */       
/*  531 */       validateContainerType(annotationType, containerType);
/*      */     } 
/*      */     
/*  534 */     MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(false, false, true);
/*  535 */     searchWithGetSemantics(element, annotationType, null, containerType, processor);
/*  536 */     return postProcessAndSynthesizeAggregatedResults(element, annotationType, processor.getAggregatedResults());
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
/*      */   public static MultiValueMap<String, Object> getAllAnnotationAttributes(AnnotatedElement element, String annotationName) {
/*  554 */     return getAllAnnotationAttributes(element, annotationName, false, false);
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
/*      */   public static MultiValueMap<String, Object> getAllAnnotationAttributes(AnnotatedElement element, String annotationName, final boolean classValuesAsString, final boolean nestedAnnotationsAsMap) {
/*  578 */     final LinkedMultiValueMap attributesMap = new LinkedMultiValueMap();
/*      */     
/*  580 */     searchWithGetSemantics(element, null, annotationName, new SimpleAnnotationProcessor()
/*      */         {
/*      */           public Object process(AnnotatedElement annotatedElement, Annotation annotation, int metaDepth) {
/*  583 */             AnnotationAttributes annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotation, classValuesAsString, nestedAnnotationsAsMap);
/*      */             
/*  585 */             for (Map.Entry<String, Object> entry : annotationAttributes.entrySet()) {
/*  586 */               attributesMap.add(entry.getKey(), entry.getValue());
/*      */             }
/*  588 */             return AnnotatedElementUtils.CONTINUE;
/*      */           }
/*      */         });
/*      */     
/*  592 */     return !linkedMultiValueMap.isEmpty() ? (MultiValueMap<String, Object>)linkedMultiValueMap : null;
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
/*      */   public static boolean hasAnnotation(AnnotatedElement element, Class<? extends Annotation> annotationType) {
/*  610 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*  611 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*      */ 
/*      */     
/*  614 */     if (element.isAnnotationPresent(annotationType)) {
/*  615 */       return true;
/*      */     }
/*  617 */     return Boolean.TRUE.equals(searchWithFindSemantics(element, annotationType, null, alwaysTrueAnnotationProcessor));
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
/*      */   public static AnnotationAttributes findMergedAnnotationAttributes(AnnotatedElement element, Class<? extends Annotation> annotationType, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/*  650 */     AnnotationAttributes attributes = searchWithFindSemantics(element, annotationType, null, new MergedAnnotationAttributesProcessor(classValuesAsString, nestedAnnotationsAsMap));
/*      */     
/*  652 */     AnnotationUtils.postProcessAnnotationAttributes(element, attributes, classValuesAsString, nestedAnnotationsAsMap);
/*  653 */     return attributes;
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
/*      */   public static AnnotationAttributes findMergedAnnotationAttributes(AnnotatedElement element, String annotationName, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/*  686 */     AnnotationAttributes attributes = searchWithFindSemantics(element, null, annotationName, new MergedAnnotationAttributesProcessor(classValuesAsString, nestedAnnotationsAsMap));
/*      */     
/*  688 */     AnnotationUtils.postProcessAnnotationAttributes(element, attributes, classValuesAsString, nestedAnnotationsAsMap);
/*  689 */     return attributes;
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
/*      */   public static <A extends Annotation> A findMergedAnnotation(AnnotatedElement element, Class<A> annotationType) {
/*  711 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*      */ 
/*      */     
/*  714 */     if (!(element instanceof Class)) {
/*      */ 
/*      */       
/*  717 */       A annotation = element.getAnnotation(annotationType);
/*  718 */       if (annotation != null) {
/*  719 */         return AnnotationUtils.synthesizeAnnotation(annotation, element);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  724 */     AnnotationAttributes attributes = findMergedAnnotationAttributes(element, annotationType, false, false);
/*  725 */     return AnnotationUtils.synthesizeAnnotation(attributes, annotationType, element);
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
/*      */   @Deprecated
/*      */   public static <A extends Annotation> A findMergedAnnotation(AnnotatedElement element, String annotationName) {
/*  753 */     AnnotationAttributes attributes = findMergedAnnotationAttributes(element, annotationName, false, false);
/*  754 */     return AnnotationUtils.synthesizeAnnotation(attributes, (Class)attributes.annotationType(), element);
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
/*      */   public static <A extends Annotation> Set<A> findAllMergedAnnotations(AnnotatedElement element, Class<A> annotationType) {
/*  779 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*  780 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*      */     
/*  782 */     MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(false, false, true);
/*  783 */     searchWithFindSemantics(element, annotationType, null, processor);
/*  784 */     return postProcessAndSynthesizeAggregatedResults(element, annotationType, processor.getAggregatedResults());
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
/*      */   public static <A extends Annotation> Set<A> findMergedRepeatableAnnotations(AnnotatedElement element, Class<A> annotationType) {
/*  814 */     return findMergedRepeatableAnnotations(element, annotationType, null);
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
/*      */   public static <A extends Annotation> Set<A> findMergedRepeatableAnnotations(AnnotatedElement element, Class<A> annotationType, Class<? extends Annotation> containerType) {
/*  846 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*  847 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*      */     
/*  849 */     if (containerType == null) {
/*  850 */       containerType = resolveContainerType(annotationType);
/*      */     } else {
/*      */       
/*  853 */       validateContainerType(annotationType, containerType);
/*      */     } 
/*      */     
/*  856 */     MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(false, false, true);
/*  857 */     searchWithFindSemantics(element, annotationType, null, containerType, processor);
/*  858 */     return postProcessAndSynthesizeAggregatedResults(element, annotationType, processor.getAggregatedResults());
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
/*      */   private static <T> T searchWithGetSemantics(AnnotatedElement element, Class<? extends Annotation> annotationType, String annotationName, Processor<T> processor) {
/*  875 */     return searchWithGetSemantics(element, annotationType, annotationName, null, processor);
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
/*      */   private static <T> T searchWithGetSemantics(AnnotatedElement element, Class<? extends Annotation> annotationType, String annotationName, Class<? extends Annotation> containerType, Processor<T> processor) {
/*      */     try {
/*  897 */       return searchWithGetSemantics(element, annotationType, annotationName, containerType, processor, new HashSet<AnnotatedElement>(), 0);
/*      */     
/*      */     }
/*  900 */     catch (Throwable ex) {
/*  901 */       AnnotationUtils.rethrowAnnotationConfigurationException(ex);
/*  902 */       throw new IllegalStateException("Failed to introspect annotations on " + element, ex);
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
/*      */ 
/*      */   
/*      */   private static <T> T searchWithGetSemantics(AnnotatedElement element, Class<? extends Annotation> annotationType, String annotationName, Class<? extends Annotation> containerType, Processor<T> processor, Set<AnnotatedElement> visited, int metaDepth) {
/*  928 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*      */     
/*  930 */     if (visited.add(element)) {
/*      */       
/*      */       try {
/*  933 */         List<Annotation> declaredAnnotations = Arrays.asList(element.getDeclaredAnnotations());
/*  934 */         T result = searchWithGetSemanticsInAnnotations(element, declaredAnnotations, annotationType, annotationName, containerType, processor, visited, metaDepth);
/*      */         
/*  936 */         if (result != null) {
/*  937 */           return result;
/*      */         }
/*      */         
/*  940 */         if (element instanceof Class) {
/*  941 */           List<Annotation> inheritedAnnotations = new ArrayList<Annotation>();
/*  942 */           for (Annotation annotation : element.getAnnotations()) {
/*  943 */             if (!declaredAnnotations.contains(annotation)) {
/*  944 */               inheritedAnnotations.add(annotation);
/*      */             }
/*      */           } 
/*      */ 
/*      */           
/*  949 */           result = searchWithGetSemanticsInAnnotations(element, inheritedAnnotations, annotationType, annotationName, containerType, processor, visited, metaDepth);
/*      */           
/*  951 */           if (result != null) {
/*  952 */             return result;
/*      */           }
/*      */         }
/*      */       
/*  956 */       } catch (Throwable ex) {
/*  957 */         AnnotationUtils.handleIntrospectionFailure(element, ex);
/*      */       } 
/*      */     }
/*      */     
/*  961 */     return null;
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
/*      */   private static <T> T searchWithGetSemanticsInAnnotations(AnnotatedElement element, List<Annotation> annotations, Class<? extends Annotation> annotationType, String annotationName, Class<? extends Annotation> containerType, Processor<T> processor, Set<AnnotatedElement> visited, int metaDepth) {
/*  992 */     for (Annotation annotation : annotations) {
/*  993 */       Class<? extends Annotation> currentAnnotationType = annotation.annotationType();
/*  994 */       if (!AnnotationUtils.isInJavaLangAnnotationPackage(currentAnnotationType)) {
/*  995 */         if (currentAnnotationType == annotationType || currentAnnotationType
/*  996 */           .getName().equals(annotationName) || processor
/*  997 */           .alwaysProcesses()) {
/*  998 */           T result = processor.process(element, annotation, metaDepth);
/*  999 */           if (result != null) {
/* 1000 */             if (processor.aggregates() && metaDepth == 0) {
/* 1001 */               processor.getAggregatedResults().add(result);
/*      */               continue;
/*      */             } 
/* 1004 */             return result;
/*      */           } 
/*      */           
/*      */           continue;
/*      */         } 
/* 1009 */         if (currentAnnotationType == containerType) {
/* 1010 */           for (Object object : getRawAnnotationsFromContainer(element, annotation)) {
/* 1011 */             T result = processor.process(element, (Annotation)object, metaDepth);
/* 1012 */             if (result != null)
/*      */             {
/*      */               
/* 1015 */               processor.getAggregatedResults().add(result);
/*      */             }
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1023 */     for (Annotation annotation : annotations) {
/* 1024 */       Class<? extends Annotation> currentAnnotationType = annotation.annotationType();
/* 1025 */       if (!AnnotationUtils.isInJavaLangAnnotationPackage(currentAnnotationType)) {
/* 1026 */         T result = searchWithGetSemantics(currentAnnotationType, annotationType, annotationName, containerType, processor, visited, metaDepth + 1);
/*      */         
/* 1028 */         if (result != null) {
/* 1029 */           processor.postProcess(element, annotation, result);
/* 1030 */           if (processor.aggregates() && metaDepth == 0) {
/* 1031 */             processor.getAggregatedResults().add(result);
/*      */             continue;
/*      */           } 
/* 1034 */           return result;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1040 */     return null;
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
/*      */   private static <T> T searchWithFindSemantics(AnnotatedElement element, Class<? extends Annotation> annotationType, String annotationName, Processor<T> processor) {
/* 1059 */     return searchWithFindSemantics(element, annotationType, annotationName, null, processor);
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
/*      */   private static <T> T searchWithFindSemantics(AnnotatedElement element, Class<? extends Annotation> annotationType, String annotationName, Class<? extends Annotation> containerType, Processor<T> processor) {
/* 1080 */     if (containerType != null && !processor.aggregates()) {
/* 1081 */       throw new IllegalArgumentException("Searches for repeatable annotations must supply an aggregating Processor");
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/* 1086 */       return searchWithFindSemantics(element, annotationType, annotationName, containerType, processor, new HashSet<AnnotatedElement>(), 0);
/*      */     
/*      */     }
/* 1089 */     catch (Throwable ex) {
/* 1090 */       AnnotationUtils.rethrowAnnotationConfigurationException(ex);
/* 1091 */       throw new IllegalStateException("Failed to introspect annotations on " + element, ex);
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
/*      */ 
/*      */   
/*      */   private static <T> T searchWithFindSemantics(AnnotatedElement element, Class<? extends Annotation> annotationType, String annotationName, Class<? extends Annotation> containerType, Processor<T> processor, Set<AnnotatedElement> visited, int metaDepth) {
/* 1117 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*      */     
/* 1119 */     if (visited.add(element)) {
/*      */       
/*      */       try {
/* 1122 */         Annotation[] annotations = element.getDeclaredAnnotations();
/* 1123 */         if (annotations.length > 0) {
/* 1124 */           List<T> aggregatedResults = processor.aggregates() ? new ArrayList<T>() : null;
/*      */ 
/*      */           
/* 1127 */           for (Annotation annotation : annotations) {
/* 1128 */             Class<? extends Annotation> currentAnnotationType = annotation.annotationType();
/* 1129 */             if (!AnnotationUtils.isInJavaLangAnnotationPackage(currentAnnotationType)) {
/* 1130 */               if (currentAnnotationType == annotationType || currentAnnotationType
/* 1131 */                 .getName().equals(annotationName) || processor
/* 1132 */                 .alwaysProcesses()) {
/* 1133 */                 T result = processor.process(element, annotation, metaDepth);
/* 1134 */                 if (result != null) {
/* 1135 */                   if (aggregatedResults != null && metaDepth == 0) {
/* 1136 */                     aggregatedResults.add(result);
/*      */                   } else {
/*      */                     
/* 1139 */                     return result;
/*      */                   }
/*      */                 
/*      */                 }
/*      */               }
/* 1144 */               else if (currentAnnotationType == containerType) {
/* 1145 */                 for (Object object : getRawAnnotationsFromContainer(element, annotation)) {
/* 1146 */                   T result = processor.process(element, (Annotation)object, metaDepth);
/* 1147 */                   if (aggregatedResults != null && result != null)
/*      */                   {
/*      */                     
/* 1150 */                     aggregatedResults.add(result);
/*      */                   }
/*      */                 } 
/*      */               } 
/*      */             }
/*      */           } 
/*      */ 
/*      */           
/* 1158 */           for (Annotation annotation : annotations) {
/* 1159 */             Class<? extends Annotation> currentAnnotationType = annotation.annotationType();
/* 1160 */             if (!AnnotationUtils.isInJavaLangAnnotationPackage(currentAnnotationType)) {
/* 1161 */               T result = searchWithFindSemantics(currentAnnotationType, annotationType, annotationName, containerType, processor, visited, metaDepth + 1);
/*      */               
/* 1163 */               if (result != null) {
/* 1164 */                 processor.postProcess(currentAnnotationType, annotation, result);
/* 1165 */                 if (aggregatedResults != null && metaDepth == 0) {
/* 1166 */                   aggregatedResults.add(result);
/*      */                 } else {
/*      */                   
/* 1169 */                   return result;
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/* 1175 */           if (!CollectionUtils.isEmpty(aggregatedResults))
/*      */           {
/* 1177 */             processor.getAggregatedResults().addAll(0, aggregatedResults);
/*      */           }
/*      */         } 
/*      */         
/* 1181 */         if (element instanceof Method) {
/* 1182 */           Method method = (Method)element;
/*      */ 
/*      */ 
/*      */           
/* 1186 */           Method resolvedMethod = BridgeMethodResolver.findBridgedMethod(method);
/* 1187 */           if (resolvedMethod != method) {
/* 1188 */             T result = searchWithFindSemantics(resolvedMethod, annotationType, annotationName, containerType, processor, visited, metaDepth);
/*      */             
/* 1190 */             if (result != null) {
/* 1191 */               return result;
/*      */             }
/*      */           } 
/*      */ 
/*      */           
/* 1196 */           Class<?>[] ifcs = method.getDeclaringClass().getInterfaces();
/* 1197 */           if (ifcs.length > 0) {
/* 1198 */             T result = searchOnInterfaces(method, annotationType, annotationName, containerType, processor, visited, metaDepth, ifcs);
/*      */             
/* 1200 */             if (result != null) {
/* 1201 */               return result;
/*      */             }
/*      */           } 
/*      */ 
/*      */           
/* 1206 */           Class<?> clazz = method.getDeclaringClass();
/*      */           while (true) {
/* 1208 */             clazz = clazz.getSuperclass();
/* 1209 */             if (clazz == null || Object.class == clazz) {
/*      */               break;
/*      */             }
/*      */             try {
/* 1213 */               Method equivalentMethod = clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
/* 1214 */               Method resolvedEquivalentMethod = BridgeMethodResolver.findBridgedMethod(equivalentMethod);
/* 1215 */               T t = searchWithFindSemantics(resolvedEquivalentMethod, annotationType, annotationName, containerType, processor, visited, metaDepth);
/*      */               
/* 1217 */               if (t != null) {
/* 1218 */                 return t;
/*      */               }
/*      */             }
/* 1221 */             catch (NoSuchMethodException noSuchMethodException) {}
/*      */ 
/*      */ 
/*      */             
/* 1225 */             T result = searchOnInterfaces(method, annotationType, annotationName, containerType, processor, visited, metaDepth, clazz
/* 1226 */                 .getInterfaces());
/* 1227 */             if (result != null) {
/* 1228 */               return result;
/*      */             }
/*      */           }
/*      */         
/* 1232 */         } else if (element instanceof Class) {
/* 1233 */           Class<?> clazz = (Class)element;
/*      */ 
/*      */           
/* 1236 */           for (Class<?> ifc : clazz.getInterfaces()) {
/* 1237 */             T result = searchWithFindSemantics(ifc, annotationType, annotationName, containerType, processor, visited, metaDepth);
/*      */             
/* 1239 */             if (result != null) {
/* 1240 */               return result;
/*      */             }
/*      */           } 
/*      */ 
/*      */           
/* 1245 */           Class<?> superclass = clazz.getSuperclass();
/* 1246 */           if (superclass != null && Object.class != superclass) {
/* 1247 */             T result = searchWithFindSemantics(superclass, annotationType, annotationName, containerType, processor, visited, metaDepth);
/*      */             
/* 1249 */             if (result != null) {
/* 1250 */               return result;
/*      */             }
/*      */           }
/*      */         
/*      */         } 
/* 1255 */       } catch (Throwable ex) {
/* 1256 */         AnnotationUtils.handleIntrospectionFailure(element, ex);
/*      */       } 
/*      */     }
/* 1259 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <T> T searchOnInterfaces(Method method, Class<? extends Annotation> annotationType, String annotationName, Class<? extends Annotation> containerType, Processor<T> processor, Set<AnnotatedElement> visited, int metaDepth, Class<?>[] ifcs) {
/* 1266 */     for (Class<?> iface : ifcs) {
/* 1267 */       if (AnnotationUtils.isInterfaceWithAnnotatedMethods(iface)) {
/*      */         try {
/* 1269 */           Method equivalentMethod = iface.getMethod(method.getName(), method.getParameterTypes());
/* 1270 */           T result = searchWithFindSemantics(equivalentMethod, annotationType, annotationName, containerType, processor, visited, metaDepth);
/*      */           
/* 1272 */           if (result != null) {
/* 1273 */             return result;
/*      */           }
/*      */         }
/* 1276 */         catch (NoSuchMethodException noSuchMethodException) {}
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1282 */     return null;
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
/*      */   private static <A extends Annotation> A[] getRawAnnotationsFromContainer(AnnotatedElement element, Annotation container) {
/*      */     try {
/* 1295 */       return (A[])AnnotationUtils.getValue(container);
/*      */     }
/* 1297 */     catch (Throwable ex) {
/* 1298 */       AnnotationUtils.handleIntrospectionFailure(element, ex);
/*      */ 
/*      */       
/* 1301 */       return (A[])EMPTY_ANNOTATION_ARRAY;
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
/*      */   private static Class<? extends Annotation> resolveContainerType(Class<? extends Annotation> annotationType) {
/* 1313 */     Class<? extends Annotation> containerType = AnnotationUtils.resolveContainerAnnotationType(annotationType);
/* 1314 */     if (containerType == null) {
/* 1315 */       throw new IllegalArgumentException("Annotation type must be a repeatable annotation: failed to resolve container type for " + annotationType
/*      */           
/* 1317 */           .getName());
/*      */     }
/* 1319 */     return containerType;
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
/*      */   private static void validateContainerType(Class<? extends Annotation> annotationType, Class<? extends Annotation> containerType) {
/*      */     try {
/* 1335 */       Method method = containerType.getDeclaredMethod("value", new Class[0]);
/* 1336 */       Class<?> returnType = method.getReturnType();
/* 1337 */       if (!returnType.isArray() || returnType.getComponentType() != annotationType) {
/* 1338 */         String msg = String.format("Container type [%s] must declare a 'value' attribute for an array of type [%s]", new Object[] { containerType
/*      */               
/* 1340 */               .getName(), annotationType.getName() });
/* 1341 */         throw new AnnotationConfigurationException(msg);
/*      */       }
/*      */     
/* 1344 */     } catch (Throwable ex) {
/* 1345 */       AnnotationUtils.rethrowAnnotationConfigurationException(ex);
/* 1346 */       String msg = String.format("Invalid declaration of container type [%s] for repeatable annotation [%s]", new Object[] { containerType
/* 1347 */             .getName(), annotationType.getName() });
/* 1348 */       throw new AnnotationConfigurationException(msg, ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static <A extends Annotation> Set<A> postProcessAndSynthesizeAggregatedResults(AnnotatedElement element, Class<A> annotationType, List<AnnotationAttributes> aggregatedResults) {
/* 1355 */     Set<A> annotations = new LinkedHashSet<A>();
/* 1356 */     for (AnnotationAttributes attributes : aggregatedResults) {
/* 1357 */       AnnotationUtils.postProcessAnnotationAttributes(element, attributes, false, false);
/* 1358 */       annotations.add(AnnotationUtils.synthesizeAnnotation(attributes, annotationType, element));
/*      */     } 
/* 1360 */     return annotations;
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
/*      */   private static interface Processor<T>
/*      */   {
/*      */     T process(AnnotatedElement param1AnnotatedElement, Annotation param1Annotation, int param1Int);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void postProcess(AnnotatedElement param1AnnotatedElement, Annotation param1Annotation, T param1T);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean alwaysProcesses();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean aggregates();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     List<T> getAggregatedResults();
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
/*      */   private static abstract class SimpleAnnotationProcessor<T>
/*      */     implements Processor<T>
/*      */   {
/*      */     private final boolean alwaysProcesses;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SimpleAnnotationProcessor() {
/* 1475 */       this(false);
/*      */     }
/*      */     
/*      */     public SimpleAnnotationProcessor(boolean alwaysProcesses) {
/* 1479 */       this.alwaysProcesses = alwaysProcesses;
/*      */     }
/*      */ 
/*      */     
/*      */     public final boolean alwaysProcesses() {
/* 1484 */       return this.alwaysProcesses;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public final void postProcess(AnnotatedElement annotatedElement, Annotation annotation, T result) {}
/*      */ 
/*      */ 
/*      */     
/*      */     public final boolean aggregates() {
/* 1494 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public final List<T> getAggregatedResults() {
/* 1499 */       throw new UnsupportedOperationException("SimpleAnnotationProcessor does not support aggregated results");
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
/*      */   static class AlwaysTrueBooleanAnnotationProcessor
/*      */     extends SimpleAnnotationProcessor<Boolean>
/*      */   {
/*      */     public final Boolean process(AnnotatedElement annotatedElement, Annotation annotation, int metaDepth) {
/* 1514 */       return Boolean.TRUE;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class MergedAnnotationAttributesProcessor
/*      */     implements Processor<AnnotationAttributes>
/*      */   {
/*      */     private final boolean classValuesAsString;
/*      */ 
/*      */ 
/*      */     
/*      */     private final boolean nestedAnnotationsAsMap;
/*      */ 
/*      */ 
/*      */     
/*      */     private final boolean aggregates;
/*      */ 
/*      */ 
/*      */     
/*      */     private final List<AnnotationAttributes> aggregatedResults;
/*      */ 
/*      */ 
/*      */     
/*      */     MergedAnnotationAttributesProcessor() {
/* 1541 */       this(false, false, false);
/*      */     }
/*      */     
/*      */     MergedAnnotationAttributesProcessor(boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/* 1545 */       this(classValuesAsString, nestedAnnotationsAsMap, false);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     MergedAnnotationAttributesProcessor(boolean classValuesAsString, boolean nestedAnnotationsAsMap, boolean aggregates) {
/* 1551 */       this.classValuesAsString = classValuesAsString;
/* 1552 */       this.nestedAnnotationsAsMap = nestedAnnotationsAsMap;
/* 1553 */       this.aggregates = aggregates;
/* 1554 */       this.aggregatedResults = aggregates ? new ArrayList<AnnotationAttributes>() : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean alwaysProcesses() {
/* 1559 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean aggregates() {
/* 1564 */       return this.aggregates;
/*      */     }
/*      */ 
/*      */     
/*      */     public List<AnnotationAttributes> getAggregatedResults() {
/* 1569 */       return this.aggregatedResults;
/*      */     }
/*      */ 
/*      */     
/*      */     public AnnotationAttributes process(AnnotatedElement annotatedElement, Annotation annotation, int metaDepth) {
/* 1574 */       return AnnotationUtils.retrieveAnnotationAttributes(annotatedElement, annotation, this.classValuesAsString, this.nestedAnnotationsAsMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(AnnotatedElement element, Annotation annotation, AnnotationAttributes attributes) {
/* 1580 */       annotation = AnnotationUtils.synthesizeAnnotation(annotation, element);
/* 1581 */       Class<? extends Annotation> targetAnnotationType = attributes.annotationType();
/*      */ 
/*      */ 
/*      */       
/* 1585 */       Set<String> valuesAlreadyReplaced = new HashSet<String>();
/*      */       
/* 1587 */       for (Method attributeMethod : AnnotationUtils.getAttributeMethods(annotation.annotationType())) {
/* 1588 */         String attributeName = attributeMethod.getName();
/* 1589 */         String attributeOverrideName = AnnotationUtils.getAttributeOverrideName(attributeMethod, targetAnnotationType);
/*      */ 
/*      */         
/* 1592 */         if (attributeOverrideName != null) {
/* 1593 */           if (valuesAlreadyReplaced.contains(attributeOverrideName)) {
/*      */             continue;
/*      */           }
/*      */           
/* 1597 */           List<String> targetAttributeNames = new ArrayList<String>();
/* 1598 */           targetAttributeNames.add(attributeOverrideName);
/* 1599 */           valuesAlreadyReplaced.add(attributeOverrideName);
/*      */ 
/*      */           
/* 1602 */           List<String> aliases = AnnotationUtils.getAttributeAliasMap(targetAnnotationType).get(attributeOverrideName);
/* 1603 */           if (aliases != null) {
/* 1604 */             for (String alias : aliases) {
/* 1605 */               if (!valuesAlreadyReplaced.contains(alias)) {
/* 1606 */                 targetAttributeNames.add(alias);
/* 1607 */                 valuesAlreadyReplaced.add(alias);
/*      */               } 
/*      */             } 
/*      */           }
/*      */           
/* 1612 */           overrideAttributes(element, annotation, attributes, attributeName, targetAttributeNames);
/*      */           continue;
/*      */         } 
/* 1615 */         if (!"value".equals(attributeName) && attributes.containsKey(attributeName)) {
/* 1616 */           overrideAttribute(element, annotation, attributes, attributeName, attributeName);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void overrideAttributes(AnnotatedElement element, Annotation annotation, AnnotationAttributes attributes, String sourceAttributeName, List<String> targetAttributeNames) {
/* 1624 */       Object adaptedValue = getAdaptedValue(element, annotation, sourceAttributeName);
/*      */       
/* 1626 */       for (String targetAttributeName : targetAttributeNames) {
/* 1627 */         attributes.put(targetAttributeName, adaptedValue);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void overrideAttribute(AnnotatedElement element, Annotation annotation, AnnotationAttributes attributes, String sourceAttributeName, String targetAttributeName) {
/* 1634 */       attributes.put(targetAttributeName, getAdaptedValue(element, annotation, sourceAttributeName));
/*      */     }
/*      */     
/*      */     private Object getAdaptedValue(AnnotatedElement element, Annotation annotation, String sourceAttributeName) {
/* 1638 */       Object value = AnnotationUtils.getValue(annotation, sourceAttributeName);
/* 1639 */       return AnnotationUtils.adaptValue(element, value, this.classValuesAsString, this.nestedAnnotationsAsMap);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\annotation\AnnotatedElementUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */