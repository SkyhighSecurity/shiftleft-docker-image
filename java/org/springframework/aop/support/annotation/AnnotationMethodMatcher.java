/*    */ package org.springframework.aop.support.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.aop.support.AopUtils;
/*    */ import org.springframework.aop.support.StaticMethodMatcher;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AnnotationMethodMatcher
/*    */   extends StaticMethodMatcher
/*    */ {
/*    */   private final Class<? extends Annotation> annotationType;
/*    */   
/*    */   public AnnotationMethodMatcher(Class<? extends Annotation> annotationType) {
/* 45 */     Assert.notNull(annotationType, "Annotation type must not be null");
/* 46 */     this.annotationType = annotationType;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(Method method, Class<?> targetClass) {
/* 52 */     if (method.isAnnotationPresent(this.annotationType)) {
/* 53 */       return true;
/*    */     }
/*    */     
/* 56 */     Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
/* 57 */     return (specificMethod != method && specificMethod.isAnnotationPresent(this.annotationType));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 62 */     if (this == other) {
/* 63 */       return true;
/*    */     }
/* 65 */     if (!(other instanceof AnnotationMethodMatcher)) {
/* 66 */       return false;
/*    */     }
/* 68 */     AnnotationMethodMatcher otherMm = (AnnotationMethodMatcher)other;
/* 69 */     return this.annotationType.equals(otherMm.annotationType);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 74 */     return this.annotationType.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 79 */     return getClass().getName() + ": " + this.annotationType;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\annotation\AnnotationMethodMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */