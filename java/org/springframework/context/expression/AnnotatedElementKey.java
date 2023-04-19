/*    */ package org.springframework.context.expression;
/*    */ 
/*    */ import java.lang.reflect.AnnotatedElement;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.ObjectUtils;
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
/*    */ 
/*    */ public final class AnnotatedElementKey
/*    */   implements Comparable<AnnotatedElementKey>
/*    */ {
/*    */   private final AnnotatedElement element;
/*    */   private final Class<?> targetClass;
/*    */   
/*    */   public AnnotatedElementKey(AnnotatedElement element, Class<?> targetClass) {
/* 45 */     Assert.notNull(element, "AnnotatedElement must not be null");
/* 46 */     this.element = element;
/* 47 */     this.targetClass = targetClass;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 53 */     if (this == other) {
/* 54 */       return true;
/*    */     }
/* 56 */     if (!(other instanceof AnnotatedElementKey)) {
/* 57 */       return false;
/*    */     }
/* 59 */     AnnotatedElementKey otherKey = (AnnotatedElementKey)other;
/* 60 */     return (this.element.equals(otherKey.element) && 
/* 61 */       ObjectUtils.nullSafeEquals(this.targetClass, otherKey.targetClass));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 66 */     return this.element.hashCode() + ((this.targetClass != null) ? (this.targetClass.hashCode() * 29) : 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 71 */     return this.element + ((this.targetClass != null) ? (" on " + this.targetClass) : "");
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(AnnotatedElementKey other) {
/* 76 */     int result = this.element.toString().compareTo(other.element.toString());
/* 77 */     if (result == 0 && this.targetClass != null) {
/* 78 */       if (other.targetClass == null) {
/* 79 */         return 1;
/*    */       }
/* 81 */       result = this.targetClass.getName().compareTo(other.targetClass.getName());
/*    */     } 
/* 83 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\expression\AnnotatedElementKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */