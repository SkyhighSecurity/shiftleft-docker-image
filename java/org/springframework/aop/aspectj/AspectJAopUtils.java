/*    */ package org.springframework.aop.aspectj;
/*    */ 
/*    */ import org.aopalliance.aop.Advice;
/*    */ import org.springframework.aop.Advisor;
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
/*    */ public abstract class AspectJAopUtils
/*    */ {
/*    */   public static boolean isBeforeAdvice(Advisor anAdvisor) {
/* 38 */     AspectJPrecedenceInformation precedenceInfo = getAspectJPrecedenceInformationFor(anAdvisor);
/* 39 */     if (precedenceInfo != null) {
/* 40 */       return precedenceInfo.isBeforeAdvice();
/*    */     }
/* 42 */     return anAdvisor.getAdvice() instanceof org.springframework.aop.BeforeAdvice;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isAfterAdvice(Advisor anAdvisor) {
/* 49 */     AspectJPrecedenceInformation precedenceInfo = getAspectJPrecedenceInformationFor(anAdvisor);
/* 50 */     if (precedenceInfo != null) {
/* 51 */       return precedenceInfo.isAfterAdvice();
/*    */     }
/* 53 */     return anAdvisor.getAdvice() instanceof org.springframework.aop.AfterAdvice;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static AspectJPrecedenceInformation getAspectJPrecedenceInformationFor(Advisor anAdvisor) {
/* 62 */     if (anAdvisor instanceof AspectJPrecedenceInformation) {
/* 63 */       return (AspectJPrecedenceInformation)anAdvisor;
/*    */     }
/* 65 */     Advice advice = anAdvisor.getAdvice();
/* 66 */     if (advice instanceof AspectJPrecedenceInformation) {
/* 67 */       return (AspectJPrecedenceInformation)advice;
/*    */     }
/* 69 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\AspectJAopUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */