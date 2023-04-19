/*     */ package org.springframework.core.type;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardAnnotationMetadata
/*     */   extends StandardClassMetadata
/*     */   implements AnnotationMetadata
/*     */ {
/*     */   private final Annotation[] annotations;
/*     */   private final boolean nestedAnnotationsAsMap;
/*     */   
/*     */   public StandardAnnotationMetadata(Class<?> introspectedClass) {
/*  52 */     this(introspectedClass, false);
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
/*     */   public StandardAnnotationMetadata(Class<?> introspectedClass, boolean nestedAnnotationsAsMap) {
/*  67 */     super(introspectedClass);
/*  68 */     this.annotations = introspectedClass.getAnnotations();
/*  69 */     this.nestedAnnotationsAsMap = nestedAnnotationsAsMap;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getAnnotationTypes() {
/*  75 */     Set<String> types = new LinkedHashSet<String>();
/*  76 */     for (Annotation ann : this.annotations) {
/*  77 */       types.add(ann.annotationType().getName());
/*     */     }
/*  79 */     return types;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getMetaAnnotationTypes(String annotationName) {
/*  84 */     return (this.annotations.length > 0) ? 
/*  85 */       AnnotatedElementUtils.getMetaAnnotationTypes(getIntrospectedClass(), annotationName) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnnotation(String annotationName) {
/*  90 */     for (Annotation ann : this.annotations) {
/*  91 */       if (ann.annotationType().getName().equals(annotationName)) {
/*  92 */         return true;
/*     */       }
/*     */     } 
/*  95 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasMetaAnnotation(String annotationName) {
/* 100 */     return (this.annotations.length > 0 && 
/* 101 */       AnnotatedElementUtils.hasMetaAnnotationTypes(getIntrospectedClass(), annotationName));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnotated(String annotationName) {
/* 106 */     return (this.annotations.length > 0 && 
/* 107 */       AnnotatedElementUtils.isAnnotated(getIntrospectedClass(), annotationName));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> getAnnotationAttributes(String annotationName) {
/* 112 */     return getAnnotationAttributes(annotationName, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> getAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 117 */     return (this.annotations.length > 0) ? (Map<String, Object>)AnnotatedElementUtils.getMergedAnnotationAttributes(
/* 118 */         getIntrospectedClass(), annotationName, classValuesAsString, this.nestedAnnotationsAsMap) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName) {
/* 123 */     return getAllAnnotationAttributes(annotationName, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 128 */     return (this.annotations.length > 0) ? AnnotatedElementUtils.getAllAnnotationAttributes(
/* 129 */         getIntrospectedClass(), annotationName, classValuesAsString, this.nestedAnnotationsAsMap) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnnotatedMethods(String annotationName) {
/*     */     try {
/* 135 */       Method[] methods = getIntrospectedClass().getDeclaredMethods();
/* 136 */       for (Method method : methods) {
/* 137 */         if (!method.isBridge() && (method.getAnnotations()).length > 0 && 
/* 138 */           AnnotatedElementUtils.isAnnotated(method, annotationName)) {
/* 139 */           return true;
/*     */         }
/*     */       } 
/* 142 */       return false;
/*     */     }
/* 144 */     catch (Throwable ex) {
/* 145 */       throw new IllegalStateException("Failed to introspect annotated methods on " + getIntrospectedClass(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<MethodMetadata> getAnnotatedMethods(String annotationName) {
/*     */     try {
/* 152 */       Method[] methods = getIntrospectedClass().getDeclaredMethods();
/* 153 */       Set<MethodMetadata> annotatedMethods = new LinkedHashSet<MethodMetadata>(4);
/* 154 */       for (Method method : methods) {
/* 155 */         if (!method.isBridge() && (method.getAnnotations()).length > 0 && 
/* 156 */           AnnotatedElementUtils.isAnnotated(method, annotationName)) {
/* 157 */           annotatedMethods.add(new StandardMethodMetadata(method, this.nestedAnnotationsAsMap));
/*     */         }
/*     */       } 
/* 160 */       return annotatedMethods;
/*     */     }
/* 162 */     catch (Throwable ex) {
/* 163 */       throw new IllegalStateException("Failed to introspect annotated methods on " + getIntrospectedClass(), ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\type\StandardAnnotationMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */