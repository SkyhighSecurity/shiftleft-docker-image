/*    */ package org.springframework.aop.support;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.springframework.aop.ClassFilter;
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
/*    */ public class RootClassFilter
/*    */   implements ClassFilter, Serializable
/*    */ {
/*    */   private Class<?> clazz;
/*    */   
/*    */   public RootClassFilter(Class<?> clazz) {
/* 35 */     this.clazz = clazz;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(Class<?> candidate) {
/* 41 */     return this.clazz.isAssignableFrom(candidate);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\RootClassFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */