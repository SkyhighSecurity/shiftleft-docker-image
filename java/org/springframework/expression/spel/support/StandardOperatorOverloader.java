/*    */ package org.springframework.expression.spel.support;
/*    */ 
/*    */ import org.springframework.expression.EvaluationException;
/*    */ import org.springframework.expression.Operation;
/*    */ import org.springframework.expression.OperatorOverloader;
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
/*    */ public class StandardOperatorOverloader
/*    */   implements OperatorOverloader
/*    */ {
/*    */   public boolean overridesOperation(Operation operation, Object leftOperand, Object rightOperand) throws EvaluationException {
/* 32 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object operate(Operation operation, Object leftOperand, Object rightOperand) throws EvaluationException {
/* 37 */     throw new EvaluationException("No operation overloaded by default");
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\support\StandardOperatorOverloader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */