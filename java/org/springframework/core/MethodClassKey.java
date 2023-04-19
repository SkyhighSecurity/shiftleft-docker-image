/*    */ package org.springframework.core;
/*    */ 
/*    */ import java.lang.reflect.Method;
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
/*    */ 
/*    */ public final class MethodClassKey
/*    */   implements Comparable<MethodClassKey>
/*    */ {
/*    */   private final Method method;
/*    */   private final Class<?> targetClass;
/*    */   
/*    */   public MethodClassKey(Method method, Class<?> targetClass) {
/* 45 */     this.method = method;
/* 46 */     this.targetClass = targetClass;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 52 */     if (this == other) {
/* 53 */       return true;
/*    */     }
/* 55 */     if (!(other instanceof MethodClassKey)) {
/* 56 */       return false;
/*    */     }
/* 58 */     MethodClassKey otherKey = (MethodClassKey)other;
/* 59 */     return (this.method.equals(otherKey.method) && 
/* 60 */       ObjectUtils.nullSafeEquals(this.targetClass, otherKey.targetClass));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 65 */     return this.method.hashCode() + ((this.targetClass != null) ? (this.targetClass.hashCode() * 29) : 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 70 */     return this.method + ((this.targetClass != null) ? (" on " + this.targetClass) : "");
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(MethodClassKey other) {
/* 75 */     int result = this.method.getName().compareTo(other.method.getName());
/* 76 */     if (result == 0) {
/* 77 */       result = this.method.toString().compareTo(other.method.toString());
/* 78 */       if (result == 0 && this.targetClass != null && other.targetClass != null) {
/* 79 */         result = this.targetClass.getName().compareTo(other.targetClass.getName());
/*    */       }
/*    */     } 
/* 82 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\MethodClassKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */