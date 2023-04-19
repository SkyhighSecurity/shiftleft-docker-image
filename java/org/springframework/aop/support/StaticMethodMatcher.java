/*    */ package org.springframework.aop.support;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.aop.MethodMatcher;
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
/*    */ public abstract class StaticMethodMatcher
/*    */   implements MethodMatcher
/*    */ {
/*    */   public final boolean isRuntime() {
/* 33 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean matches(Method method, Class<?> targetClass, Object... args) {
/* 39 */     throw new UnsupportedOperationException("Illegal MethodMatcher usage");
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\StaticMethodMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */