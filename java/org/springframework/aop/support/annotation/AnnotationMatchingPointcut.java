/*     */ package org.springframework.aop.support.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import org.springframework.aop.ClassFilter;
/*     */ import org.springframework.aop.MethodMatcher;
/*     */ import org.springframework.aop.Pointcut;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class AnnotationMatchingPointcut
/*     */   implements Pointcut
/*     */ {
/*     */   private final ClassFilter classFilter;
/*     */   private final MethodMatcher methodMatcher;
/*     */   
/*     */   public AnnotationMatchingPointcut(Class<? extends Annotation> classAnnotationType) {
/*  48 */     this(classAnnotationType, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationMatchingPointcut(Class<? extends Annotation> classAnnotationType, boolean checkInherited) {
/*  59 */     this.classFilter = new AnnotationClassFilter(classAnnotationType, checkInherited);
/*  60 */     this.methodMatcher = MethodMatcher.TRUE;
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
/*     */   public AnnotationMatchingPointcut(Class<? extends Annotation> classAnnotationType, Class<? extends Annotation> methodAnnotationType) {
/*  73 */     Assert.isTrue((classAnnotationType != null || methodAnnotationType != null), "Either Class annotation type or Method annotation type needs to be specified (or both)");
/*     */ 
/*     */     
/*  76 */     if (classAnnotationType != null) {
/*  77 */       this.classFilter = new AnnotationClassFilter(classAnnotationType);
/*     */     } else {
/*     */       
/*  80 */       this.classFilter = ClassFilter.TRUE;
/*     */     } 
/*     */     
/*  83 */     if (methodAnnotationType != null) {
/*  84 */       this.methodMatcher = (MethodMatcher)new AnnotationMethodMatcher(methodAnnotationType);
/*     */     } else {
/*     */       
/*  87 */       this.methodMatcher = MethodMatcher.TRUE;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFilter getClassFilter() {
/*  94 */     return this.classFilter;
/*     */   }
/*     */ 
/*     */   
/*     */   public MethodMatcher getMethodMatcher() {
/*  99 */     return this.methodMatcher;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 104 */     if (this == other) {
/* 105 */       return true;
/*     */     }
/* 107 */     if (!(other instanceof AnnotationMatchingPointcut)) {
/* 108 */       return false;
/*     */     }
/* 110 */     AnnotationMatchingPointcut otherPointcut = (AnnotationMatchingPointcut)other;
/* 111 */     return (this.classFilter.equals(otherPointcut.classFilter) && this.methodMatcher
/* 112 */       .equals(otherPointcut.methodMatcher));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 117 */     return this.classFilter.hashCode() * 37 + this.methodMatcher.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 122 */     return "AnnotationMatchingPointcut: " + this.classFilter + ", " + this.methodMatcher;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AnnotationMatchingPointcut forClassAnnotation(Class<? extends Annotation> annotationType) {
/* 133 */     Assert.notNull(annotationType, "Annotation type must not be null");
/* 134 */     return new AnnotationMatchingPointcut(annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AnnotationMatchingPointcut forMethodAnnotation(Class<? extends Annotation> annotationType) {
/* 144 */     Assert.notNull(annotationType, "Annotation type must not be null");
/* 145 */     return new AnnotationMatchingPointcut(null, annotationType);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\annotation\AnnotationMatchingPointcut.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */