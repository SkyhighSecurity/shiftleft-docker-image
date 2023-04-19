/*    */ package org.springframework.aop;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ class TruePointcut
/*    */   implements Pointcut, Serializable
/*    */ {
/* 29 */   public static final TruePointcut INSTANCE = new TruePointcut();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClassFilter getClassFilter() {
/* 39 */     return ClassFilter.TRUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public MethodMatcher getMethodMatcher() {
/* 44 */     return MethodMatcher.TRUE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Object readResolve() {
/* 53 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 58 */     return "Pointcut.TRUE";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\TruePointcut.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */