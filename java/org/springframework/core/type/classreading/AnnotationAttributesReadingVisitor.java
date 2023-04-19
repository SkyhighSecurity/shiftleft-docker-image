/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class AnnotationAttributesReadingVisitor
/*     */   extends RecursiveAnnotationAttributesVisitor
/*     */ {
/*     */   private final MultiValueMap<String, AnnotationAttributes> attributesMap;
/*     */   private final Map<String, Set<String>> metaAnnotationMap;
/*     */   
/*     */   public AnnotationAttributesReadingVisitor(String annotationType, MultiValueMap<String, AnnotationAttributes> attributesMap, Map<String, Set<String>> metaAnnotationMap, ClassLoader classLoader) {
/*  55 */     super(annotationType, new AnnotationAttributes(annotationType, classLoader), classLoader);
/*  56 */     this.attributesMap = attributesMap;
/*  57 */     this.metaAnnotationMap = metaAnnotationMap;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/*  63 */     super.visitEnd();
/*     */     
/*  65 */     Class<? extends Annotation> annotationClass = this.attributes.annotationType();
/*  66 */     if (annotationClass != null) {
/*  67 */       List<AnnotationAttributes> attributeList = (List<AnnotationAttributes>)this.attributesMap.get(this.annotationType);
/*  68 */       if (attributeList == null) {
/*  69 */         this.attributesMap.add(this.annotationType, this.attributes);
/*     */       } else {
/*     */         
/*  72 */         attributeList.add(0, this.attributes);
/*     */       } 
/*  74 */       if (!AnnotationUtils.isInJavaLangAnnotationPackage(annotationClass.getName())) {
/*  75 */         Set<Annotation> visited = new LinkedHashSet<Annotation>();
/*  76 */         Annotation[] metaAnnotations = AnnotationUtils.getAnnotations(annotationClass);
/*  77 */         if (!ObjectUtils.isEmpty((Object[])metaAnnotations)) {
/*  78 */           for (Annotation metaAnnotation : metaAnnotations) {
/*  79 */             recursivelyCollectMetaAnnotations(visited, metaAnnotation);
/*     */           }
/*     */         }
/*  82 */         if (this.metaAnnotationMap != null) {
/*  83 */           Set<String> metaAnnotationTypeNames = new LinkedHashSet<String>(visited.size());
/*  84 */           for (Annotation ann : visited) {
/*  85 */             metaAnnotationTypeNames.add(ann.annotationType().getName());
/*     */           }
/*  87 */           this.metaAnnotationMap.put(annotationClass.getName(), metaAnnotationTypeNames);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void recursivelyCollectMetaAnnotations(Set<Annotation> visited, Annotation annotation) {
/*  94 */     Class<? extends Annotation> annotationType = annotation.annotationType();
/*  95 */     String annotationName = annotationType.getName();
/*  96 */     if (!AnnotationUtils.isInJavaLangAnnotationPackage(annotationName) && visited.add(annotation))
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 101 */         if (Modifier.isPublic(annotationType.getModifiers())) {
/* 102 */           this.attributesMap.add(annotationName, 
/* 103 */               AnnotationUtils.getAnnotationAttributes(annotation, false, true));
/*     */         }
/* 105 */         for (Annotation metaMetaAnnotation : annotationType.getAnnotations()) {
/* 106 */           recursivelyCollectMetaAnnotations(visited, metaMetaAnnotation);
/*     */         }
/*     */       }
/* 109 */       catch (Throwable ex) {
/* 110 */         if (this.logger.isDebugEnabled())
/* 111 */           this.logger.debug("Failed to introspect meta-annotations on " + annotation + ": " + ex); 
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\type\classreading\AnnotationAttributesReadingVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */