/*    */ package org.springframework.aop.aspectj;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.aop.MethodBeforeAdvice;
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
/*    */ public class AspectJMethodBeforeAdvice
/*    */   extends AbstractAspectJAdvice
/*    */   implements MethodBeforeAdvice, Serializable
/*    */ {
/*    */   public AspectJMethodBeforeAdvice(Method aspectJBeforeAdviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory aif) {
/* 37 */     super(aspectJBeforeAdviceMethod, pointcut, aif);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void before(Method method, Object[] args, Object target) throws Throwable {
/* 43 */     invokeAdviceMethod(getJoinPointMatch(), null, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBeforeAdvice() {
/* 48 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAfterAdvice() {
/* 53 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\AspectJMethodBeforeAdvice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */