/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.asm.AnnotationVisitor;
/*     */ import org.springframework.asm.Attribute;
/*     */ import org.springframework.asm.FieldVisitor;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.MethodMetadata;
/*     */ import org.springframework.util.LinkedMultiValueMap;
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
/*     */ public class AnnotationMetadataReadingVisitor
/*     */   extends ClassMetadataReadingVisitor
/*     */   implements AnnotationMetadata
/*     */ {
/*     */   protected final ClassLoader classLoader;
/*  53 */   protected final Set<String> annotationSet = new LinkedHashSet<String>(4);
/*     */   
/*  55 */   protected final Map<String, Set<String>> metaAnnotationMap = new LinkedHashMap<String, Set<String>>(4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   protected final LinkedMultiValueMap<String, AnnotationAttributes> attributesMap = new LinkedMultiValueMap(4);
/*     */ 
/*     */   
/*  65 */   protected final Set<MethodMetadata> methodMetadataSet = new LinkedHashSet<MethodMetadata>(4);
/*     */ 
/*     */   
/*     */   public AnnotationMetadataReadingVisitor(ClassLoader classLoader) {
/*  69 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/*  77 */     if ((access & 0x40) != 0) {
/*  78 */       return super.visitMethod(access, name, desc, signature, exceptions);
/*     */     }
/*  80 */     return new MethodMetadataReadingVisitor(name, access, getClassName(), 
/*  81 */         Type.getReturnType(desc).getClassName(), this.classLoader, this.methodMetadataSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/*  86 */     String className = Type.getType(desc).getClassName();
/*  87 */     this.annotationSet.add(className);
/*  88 */     return new AnnotationAttributesReadingVisitor(className, (MultiValueMap<String, AnnotationAttributes>)this.attributesMap, this.metaAnnotationMap, this.classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getAnnotationTypes() {
/*  95 */     return this.annotationSet;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getMetaAnnotationTypes(String annotationName) {
/* 100 */     return this.metaAnnotationMap.get(annotationName);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnnotation(String annotationName) {
/* 105 */     return this.annotationSet.contains(annotationName);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasMetaAnnotation(String metaAnnotationType) {
/* 110 */     Collection<Set<String>> allMetaTypes = this.metaAnnotationMap.values();
/* 111 */     for (Set<String> metaTypes : allMetaTypes) {
/* 112 */       if (metaTypes.contains(metaAnnotationType)) {
/* 113 */         return true;
/*     */       }
/*     */     } 
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnotated(String annotationName) {
/* 121 */     return (!AnnotationUtils.isInJavaLangAnnotationPackage(annotationName) && this.attributesMap
/* 122 */       .containsKey(annotationName));
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationAttributes getAnnotationAttributes(String annotationName) {
/* 127 */     return getAnnotationAttributes(annotationName, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationAttributes getAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 132 */     AnnotationAttributes raw = AnnotationReadingVisitorUtils.getMergedAnnotationAttributes(this.attributesMap, this.metaAnnotationMap, annotationName);
/*     */     
/* 134 */     return AnnotationReadingVisitorUtils.convertClassValues("class '" + 
/* 135 */         getClassName() + "'", this.classLoader, raw, classValuesAsString);
/*     */   }
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName) {
/* 140 */     return getAllAnnotationAttributes(annotationName, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 145 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 146 */     List<AnnotationAttributes> attributes = this.attributesMap.get(annotationName);
/* 147 */     if (attributes == null) {
/* 148 */       return null;
/*     */     }
/* 150 */     for (AnnotationAttributes raw : attributes) {
/* 151 */       for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)AnnotationReadingVisitorUtils.convertClassValues("class '" + 
/* 152 */           getClassName() + "'", this.classLoader, raw, classValuesAsString).entrySet()) {
/* 153 */         linkedMultiValueMap.add(entry.getKey(), entry.getValue());
/*     */       }
/*     */     } 
/* 156 */     return (MultiValueMap<String, Object>)linkedMultiValueMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnnotatedMethods(String annotationName) {
/* 161 */     for (MethodMetadata methodMetadata : this.methodMetadataSet) {
/* 162 */       if (methodMetadata.isAnnotated(annotationName)) {
/* 163 */         return true;
/*     */       }
/*     */     } 
/* 166 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<MethodMetadata> getAnnotatedMethods(String annotationName) {
/* 171 */     Set<MethodMetadata> annotatedMethods = new LinkedHashSet<MethodMetadata>(4);
/* 172 */     for (MethodMetadata methodMetadata : this.methodMetadataSet) {
/* 173 */       if (methodMetadata.isAnnotated(annotationName)) {
/* 174 */         annotatedMethods.add(methodMetadata);
/*     */       }
/*     */     } 
/* 177 */     return annotatedMethods;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\type\classreading\AnnotationMetadataReadingVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */