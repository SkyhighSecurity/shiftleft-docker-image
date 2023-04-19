/*    */ package org.springframework.aop.support.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import org.springframework.aop.ClassFilter;
/*    */ import org.springframework.core.annotation.AnnotationUtils;
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
/*    */ public class AnnotationClassFilter
/*    */   implements ClassFilter
/*    */ {
/*    */   private final Class<? extends Annotation> annotationType;
/*    */   private final boolean checkInherited;
/*    */   
/*    */   public AnnotationClassFilter(Class<? extends Annotation> annotationType) {
/* 45 */     this(annotationType, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AnnotationClassFilter(Class<? extends Annotation> annotationType, boolean checkInherited) {
/* 56 */     Assert.notNull(annotationType, "Annotation type must not be null");
/* 57 */     this.annotationType = annotationType;
/* 58 */     this.checkInherited = checkInherited;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(Class<?> clazz) {
/* 64 */     return this.checkInherited ? (
/* 65 */       (AnnotationUtils.findAnnotation(clazz, this.annotationType) != null)) : clazz
/* 66 */       .isAnnotationPresent(this.annotationType);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 71 */     if (this == other) {
/* 72 */       return true;
/*    */     }
/* 74 */     if (!(other instanceof AnnotationClassFilter)) {
/* 75 */       return false;
/*    */     }
/* 77 */     AnnotationClassFilter otherCf = (AnnotationClassFilter)other;
/* 78 */     return (this.annotationType.equals(otherCf.annotationType) && this.checkInherited == otherCf.checkInherited);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 83 */     return this.annotationType.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 88 */     return getClass().getName() + ": " + this.annotationType;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\annotation\AnnotationClassFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */