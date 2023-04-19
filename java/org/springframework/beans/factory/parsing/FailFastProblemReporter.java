/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ public class FailFastProblemReporter
/*    */   implements ProblemReporter
/*    */ {
/* 39 */   private Log logger = LogFactory.getLog(getClass());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLogger(Log logger) {
/* 49 */     this.logger = (logger != null) ? logger : LogFactory.getLog(getClass());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void fatal(Problem problem) {
/* 60 */     throw new BeanDefinitionParsingException(problem);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void error(Problem problem) {
/* 70 */     throw new BeanDefinitionParsingException(problem);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void warning(Problem problem) {
/* 79 */     this.logger.warn(problem, problem.getRootCause());
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\parsing\FailFastProblemReporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */