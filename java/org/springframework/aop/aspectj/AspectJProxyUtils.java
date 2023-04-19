/*    */ package org.springframework.aop.aspectj;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.aop.Advisor;
/*    */ import org.springframework.aop.PointcutAdvisor;
/*    */ import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
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
/*    */ 
/*    */ public abstract class AspectJProxyUtils
/*    */ {
/*    */   public static boolean makeAdvisorChainAspectJCapableIfNecessary(List<Advisor> advisors) {
/* 44 */     if (!advisors.isEmpty()) {
/* 45 */       boolean foundAspectJAdvice = false;
/* 46 */       for (Advisor advisor : advisors) {
/*    */ 
/*    */         
/* 49 */         if (isAspectJAdvice(advisor)) {
/* 50 */           foundAspectJAdvice = true;
/*    */         }
/*    */       } 
/* 53 */       if (foundAspectJAdvice && !advisors.contains(ExposeInvocationInterceptor.ADVISOR)) {
/* 54 */         advisors.add(0, ExposeInvocationInterceptor.ADVISOR);
/* 55 */         return true;
/*    */       } 
/*    */     } 
/* 58 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean isAspectJAdvice(Advisor advisor) {
/* 66 */     return (advisor instanceof InstantiationModelAwarePointcutAdvisor || advisor
/* 67 */       .getAdvice() instanceof AbstractAspectJAdvice || (advisor instanceof PointcutAdvisor && ((PointcutAdvisor)advisor)
/*    */       
/* 69 */       .getPointcut() instanceof AspectJExpressionPointcut));
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\AspectJProxyUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */