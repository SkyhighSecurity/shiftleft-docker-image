/*     */ package org.springframework.core.type;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class StandardMethodMetadata
/*     */   implements MethodMetadata
/*     */ {
/*     */   private final Method introspectedMethod;
/*     */   private final boolean nestedAnnotationsAsMap;
/*     */   
/*     */   public StandardMethodMetadata(Method introspectedMethod) {
/*  49 */     this(introspectedMethod, false);
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
/*     */   public StandardMethodMetadata(Method introspectedMethod, boolean nestedAnnotationsAsMap) {
/*  64 */     Assert.notNull(introspectedMethod, "Method must not be null");
/*  65 */     this.introspectedMethod = introspectedMethod;
/*  66 */     this.nestedAnnotationsAsMap = nestedAnnotationsAsMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Method getIntrospectedMethod() {
/*  74 */     return this.introspectedMethod;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMethodName() {
/*  79 */     return this.introspectedMethod.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDeclaringClassName() {
/*  84 */     return this.introspectedMethod.getDeclaringClass().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getReturnTypeName() {
/*  89 */     return this.introspectedMethod.getReturnType().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/*  94 */     return Modifier.isAbstract(this.introspectedMethod.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/*  99 */     return Modifier.isStatic(this.introspectedMethod.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/* 104 */     return Modifier.isFinal(this.introspectedMethod.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOverridable() {
/* 109 */     return (!isStatic() && !isFinal() && !Modifier.isPrivate(this.introspectedMethod.getModifiers()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnotated(String annotationName) {
/* 114 */     return AnnotatedElementUtils.isAnnotated(this.introspectedMethod, annotationName);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> getAnnotationAttributes(String annotationName) {
/* 119 */     return getAnnotationAttributes(annotationName, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> getAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 124 */     return (Map<String, Object>)AnnotatedElementUtils.getMergedAnnotationAttributes(this.introspectedMethod, annotationName, classValuesAsString, this.nestedAnnotationsAsMap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName) {
/* 130 */     return getAllAnnotationAttributes(annotationName, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 135 */     return AnnotatedElementUtils.getAllAnnotationAttributes(this.introspectedMethod, annotationName, classValuesAsString, this.nestedAnnotationsAsMap);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\type\StandardMethodMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */