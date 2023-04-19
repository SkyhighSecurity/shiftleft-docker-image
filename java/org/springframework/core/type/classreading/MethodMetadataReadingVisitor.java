/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.asm.AnnotationVisitor;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodMetadataReadingVisitor
/*     */   extends MethodVisitor
/*     */   implements MethodMetadata
/*     */ {
/*     */   protected final String methodName;
/*     */   protected final int access;
/*     */   protected final String declaringClassName;
/*     */   protected final String returnTypeName;
/*     */   protected final ClassLoader classLoader;
/*     */   protected final Set<MethodMetadata> methodMetadataSet;
/*  59 */   protected final Map<String, Set<String>> metaAnnotationMap = new LinkedHashMap<String, Set<String>>(4);
/*     */   
/*  61 */   protected final LinkedMultiValueMap<String, AnnotationAttributes> attributesMap = new LinkedMultiValueMap(4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodMetadataReadingVisitor(String methodName, int access, String declaringClassName, String returnTypeName, ClassLoader classLoader, Set<MethodMetadata> methodMetadataSet) {
/*  68 */     super(393216);
/*  69 */     this.methodName = methodName;
/*  70 */     this.access = access;
/*  71 */     this.declaringClassName = declaringClassName;
/*  72 */     this.returnTypeName = returnTypeName;
/*  73 */     this.classLoader = classLoader;
/*  74 */     this.methodMetadataSet = methodMetadataSet;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/*  80 */     this.methodMetadataSet.add(this);
/*  81 */     String className = Type.getType(desc).getClassName();
/*  82 */     return new AnnotationAttributesReadingVisitor(className, (MultiValueMap<String, AnnotationAttributes>)this.attributesMap, this.metaAnnotationMap, this.classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodName() {
/*  89 */     return this.methodName;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/*  94 */     return ((this.access & 0x400) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/*  99 */     return ((this.access & 0x8) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/* 104 */     return ((this.access & 0x10) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOverridable() {
/* 109 */     return (!isStatic() && !isFinal() && (this.access & 0x2) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnotated(String annotationName) {
/* 114 */     return this.attributesMap.containsKey(annotationName);
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationAttributes getAnnotationAttributes(String annotationName) {
/* 119 */     return getAnnotationAttributes(annotationName, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationAttributes getAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 124 */     AnnotationAttributes raw = AnnotationReadingVisitorUtils.getMergedAnnotationAttributes(this.attributesMap, this.metaAnnotationMap, annotationName);
/*     */     
/* 126 */     return AnnotationReadingVisitorUtils.convertClassValues("method '" + 
/* 127 */         getMethodName() + "'", this.classLoader, raw, classValuesAsString);
/*     */   }
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName) {
/* 132 */     return getAllAnnotationAttributes(annotationName, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 137 */     if (!this.attributesMap.containsKey(annotationName)) {
/* 138 */       return null;
/*     */     }
/* 140 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 141 */     for (AnnotationAttributes annotationAttributes : this.attributesMap.get(annotationName)) {
/* 142 */       AnnotationAttributes convertedAttributes = AnnotationReadingVisitorUtils.convertClassValues("method '" + 
/* 143 */           getMethodName() + "'", this.classLoader, annotationAttributes, classValuesAsString);
/* 144 */       for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)convertedAttributes.entrySet()) {
/* 145 */         linkedMultiValueMap.add(entry.getKey(), entry.getValue());
/*     */       }
/*     */     } 
/* 148 */     return (MultiValueMap<String, Object>)linkedMultiValueMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDeclaringClassName() {
/* 153 */     return this.declaringClassName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getReturnTypeName() {
/* 158 */     return this.returnTypeName;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\type\classreading\MethodMetadataReadingVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */