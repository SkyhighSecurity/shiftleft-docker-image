/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.core.MethodParameter;
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
/*     */ public class SynthesizingMethodParameter
/*     */   extends MethodParameter
/*     */ {
/*     */   public SynthesizingMethodParameter(Method method, int parameterIndex) {
/*  46 */     super(method, parameterIndex);
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
/*     */   public SynthesizingMethodParameter(Method method, int parameterIndex, int nestingLevel) {
/*  60 */     super(method, parameterIndex, nestingLevel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SynthesizingMethodParameter(Constructor<?> constructor, int parameterIndex) {
/*  70 */     super(constructor, parameterIndex);
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
/*     */   public SynthesizingMethodParameter(Constructor<?> constructor, int parameterIndex, int nestingLevel) {
/*  82 */     super(constructor, parameterIndex, nestingLevel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SynthesizingMethodParameter(SynthesizingMethodParameter original) {
/*  91 */     super(original);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected <A extends Annotation> A adaptAnnotation(A annotation) {
/*  97 */     return AnnotationUtils.synthesizeAnnotation(annotation, getAnnotatedElement());
/*     */   }
/*     */ 
/*     */   
/*     */   protected Annotation[] adaptAnnotationArray(Annotation[] annotations) {
/* 102 */     return AnnotationUtils.synthesizeAnnotationArray(annotations, getAnnotatedElement());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SynthesizingMethodParameter clone() {
/* 108 */     return new SynthesizingMethodParameter(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\annotation\SynthesizingMethodParameter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */