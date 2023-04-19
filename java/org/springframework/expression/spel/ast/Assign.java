/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import org.springframework.expression.EvaluationException;
/*    */ import org.springframework.expression.TypedValue;
/*    */ import org.springframework.expression.spel.ExpressionState;
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
/*    */ public class Assign
/*    */   extends SpelNodeImpl
/*    */ {
/*    */   public Assign(int pos, SpelNodeImpl... operands) {
/* 35 */     super(pos, operands);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/* 41 */     TypedValue newValue = this.children[1].getValueInternal(state);
/* 42 */     getChild(0).setValue(state, newValue.getValue());
/* 43 */     return newValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toStringAST() {
/* 48 */     return getChild(0).toStringAST() + "=" + getChild(1).toStringAST();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\Assign.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */