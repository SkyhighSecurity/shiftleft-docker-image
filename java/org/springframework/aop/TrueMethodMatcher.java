/*    */ package org.springframework.aop;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.lang.reflect.Method;
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
/*    */ class TrueMethodMatcher
/*    */   implements MethodMatcher, Serializable
/*    */ {
/* 30 */   public static final TrueMethodMatcher INSTANCE = new TrueMethodMatcher();
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
/*    */   public boolean isRuntime() {
/* 42 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(Method method, Class<?> targetClass) {
/* 47 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(Method method, Class<?> targetClass, Object... args) {
/* 53 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 59 */     return "MethodMatcher.TRUE";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Object readResolve() {
/* 68 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\TrueMethodMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */