/*     */ package org.springframework.beans.factory;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import org.springframework.core.MethodParameter;
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
/*     */ 
/*     */ public class InjectionPoint
/*     */ {
/*     */   protected MethodParameter methodParameter;
/*     */   protected Field field;
/*     */   private volatile Annotation[] fieldAnnotations;
/*     */   
/*     */   public InjectionPoint(MethodParameter methodParameter) {
/*  50 */     Assert.notNull(methodParameter, "MethodParameter must not be null");
/*  51 */     this.methodParameter = methodParameter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InjectionPoint(Field field) {
/*  59 */     Assert.notNull(field, "Field must not be null");
/*  60 */     this.field = field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InjectionPoint(InjectionPoint original) {
/*  68 */     this.methodParameter = (original.methodParameter != null) ? new MethodParameter(original.methodParameter) : null;
/*     */     
/*  70 */     this.field = original.field;
/*  71 */     this.fieldAnnotations = original.fieldAnnotations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InjectionPoint() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodParameter getMethodParameter() {
/*  87 */     return this.methodParameter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Field getField() {
/*  96 */     return this.field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation[] getAnnotations() {
/* 103 */     if (this.field != null) {
/* 104 */       if (this.fieldAnnotations == null) {
/* 105 */         this.fieldAnnotations = this.field.getAnnotations();
/*     */       }
/* 107 */       return this.fieldAnnotations;
/*     */     } 
/*     */     
/* 110 */     return this.methodParameter.getParameterAnnotations();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
/* 121 */     return (this.field != null) ? this.field.<A>getAnnotation(annotationType) : (A)this.methodParameter
/* 122 */       .getParameterAnnotation(annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getDeclaredType() {
/* 130 */     return (this.field != null) ? this.field.getType() : this.methodParameter.getParameterType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Member getMember() {
/* 138 */     return (this.field != null) ? this.field : this.methodParameter.getMember();
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
/*     */   public AnnotatedElement getAnnotatedElement() {
/* 151 */     return (this.field != null) ? this.field : this.methodParameter.getAnnotatedElement();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 157 */     if (this == other) {
/* 158 */       return true;
/*     */     }
/* 160 */     if (getClass() != other.getClass()) {
/* 161 */       return false;
/*     */     }
/* 163 */     InjectionPoint otherPoint = (InjectionPoint)other;
/* 164 */     return (this.field != null) ? this.field.equals(otherPoint.field) : this.methodParameter
/* 165 */       .equals(otherPoint.methodParameter);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 170 */     return (this.field != null) ? this.field.hashCode() : this.methodParameter.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 175 */     return (this.field != null) ? ("field '" + this.field.getName() + "'") : this.methodParameter.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\InjectionPoint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */