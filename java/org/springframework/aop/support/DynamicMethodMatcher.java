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
/*    */ public abstract class DynamicMethodMatcher
/*    */   implements MethodMatcher
/*    */ {
/*    */   public final boolean isRuntime() {
/* 33 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(Method method, Class<?> targetClass) {
/* 42 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\DynamicMethodMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */