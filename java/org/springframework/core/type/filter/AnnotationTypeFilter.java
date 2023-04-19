/*     */ package org.springframework.core.type.filter;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Inherited;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.classreading.MetadataReader;
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
/*     */ 
/*     */ 
/*     */ public class AnnotationTypeFilter
/*     */   extends AbstractTypeHierarchyTraversingFilter
/*     */ {
/*     */   private final Class<? extends Annotation> annotationType;
/*     */   private final boolean considerMetaAnnotations;
/*     */   
/*     */   public AnnotationTypeFilter(Class<? extends Annotation> annotationType) {
/*  54 */     this(annotationType, true, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationTypeFilter(Class<? extends Annotation> annotationType, boolean considerMetaAnnotations) {
/*  64 */     this(annotationType, considerMetaAnnotations, false);
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
/*     */   public AnnotationTypeFilter(Class<? extends Annotation> annotationType, boolean considerMetaAnnotations, boolean considerInterfaces) {
/*  76 */     super(annotationType.isAnnotationPresent((Class)Inherited.class), considerInterfaces);
/*  77 */     this.annotationType = annotationType;
/*  78 */     this.considerMetaAnnotations = considerMetaAnnotations;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean matchSelf(MetadataReader metadataReader) {
/*  84 */     AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
/*  85 */     return (metadata.hasAnnotation(this.annotationType.getName()) || (this.considerMetaAnnotations && metadata
/*  86 */       .hasMetaAnnotation(this.annotationType.getName())));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Boolean matchSuperClass(String superClassName) {
/*  91 */     return hasAnnotation(superClassName);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Boolean matchInterface(String interfaceName) {
/*  96 */     return hasAnnotation(interfaceName);
/*     */   }
/*     */   
/*     */   protected Boolean hasAnnotation(String typeName) {
/* 100 */     if (Object.class.getName().equals(typeName)) {
/* 101 */       return Boolean.valueOf(false);
/*     */     }
/* 103 */     if (typeName.startsWith("java")) {
/* 104 */       if (!this.annotationType.getName().startsWith("java"))
/*     */       {
/*     */         
/* 107 */         return Boolean.valueOf(false);
/*     */       }
/*     */       try {
/* 110 */         Class<?> clazz = ClassUtils.forName(typeName, getClass().getClassLoader());
/* 111 */         return Boolean.valueOf(
/* 112 */             ((this.considerMetaAnnotations ? AnnotationUtils.getAnnotation(clazz, this.annotationType) : clazz.<Annotation>getAnnotation(this.annotationType)) != null));
/*     */       }
/* 114 */       catch (Throwable throwable) {}
/*     */     } 
/*     */ 
/*     */     
/* 118 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\type\filter\AnnotationTypeFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */