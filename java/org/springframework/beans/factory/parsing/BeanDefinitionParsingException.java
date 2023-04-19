/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
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
/*    */ public class BeanDefinitionParsingException
/*    */   extends BeanDefinitionStoreException
/*    */ {
/*    */   public BeanDefinitionParsingException(Problem problem) {
/* 37 */     super(problem.getResourceDescription(), problem.toString(), problem.getRootCause());
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\parsing\BeanDefinitionParsingException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */