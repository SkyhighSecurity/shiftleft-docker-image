/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import org.springframework.asm.Label;
/*    */ import org.springframework.asm.MethodVisitor;
/*    */ import org.springframework.expression.EvaluationException;
/*    */ import org.springframework.expression.TypedValue;
/*    */ import org.springframework.expression.spel.CodeFlow;
/*    */ import org.springframework.expression.spel.ExpressionState;
/*    */ import org.springframework.expression.spel.SpelEvaluationException;
/*    */ import org.springframework.expression.spel.SpelMessage;
/*    */ import org.springframework.expression.spel.support.BooleanTypedValue;
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
/*    */ public class OpAnd
/*    */   extends Operator
/*    */ {
/*    */   public OpAnd(int pos, SpelNodeImpl... operands) {
/* 40 */     super("and", pos, operands);
/* 41 */     this.exitTypeDescriptor = "Z";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/* 47 */     if (!getBooleanValue(state, getLeftOperand()))
/*    */     {
/* 49 */       return (TypedValue)BooleanTypedValue.FALSE;
/*    */     }
/* 51 */     return (TypedValue)BooleanTypedValue.forValue(getBooleanValue(state, getRightOperand()));
/*    */   }
/*    */   
/*    */   private boolean getBooleanValue(ExpressionState state, SpelNodeImpl operand) {
/*    */     try {
/* 56 */       Boolean value = operand.<Boolean>getValue(state, Boolean.class);
/* 57 */       assertValueNotNull(value);
/* 58 */       return value.booleanValue();
/*    */     }
/* 60 */     catch (SpelEvaluationException ex) {
/* 61 */       ex.setPosition(operand.getStartPosition());
/* 62 */       throw ex;
/*    */     } 
/*    */   }
/*    */   
/*    */   private void assertValueNotNull(Boolean value) {
/* 67 */     if (value == null) {
/* 68 */       throw new SpelEvaluationException(SpelMessage.TYPE_CONVERSION_ERROR, new Object[] { "null", "boolean" });
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCompilable() {
/* 74 */     SpelNodeImpl left = getLeftOperand();
/* 75 */     SpelNodeImpl right = getRightOperand();
/* 76 */     return (left.isCompilable() && right.isCompilable() && 
/* 77 */       CodeFlow.isBooleanCompatible(left.exitTypeDescriptor) && 
/* 78 */       CodeFlow.isBooleanCompatible(right.exitTypeDescriptor));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 84 */     Label elseTarget = new Label();
/* 85 */     Label endOfIf = new Label();
/* 86 */     cf.enterCompilationScope();
/* 87 */     getLeftOperand().generateCode(mv, cf);
/* 88 */     cf.unboxBooleanIfNecessary(mv);
/* 89 */     cf.exitCompilationScope();
/* 90 */     mv.visitJumpInsn(154, elseTarget);
/* 91 */     mv.visitLdcInsn(Integer.valueOf(0));
/* 92 */     mv.visitJumpInsn(167, endOfIf);
/* 93 */     mv.visitLabel(elseTarget);
/* 94 */     cf.enterCompilationScope();
/* 95 */     getRightOperand().generateCode(mv, cf);
/* 96 */     cf.unboxBooleanIfNecessary(mv);
/* 97 */     cf.exitCompilationScope();
/* 98 */     mv.visitLabel(endOfIf);
/* 99 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\OpAnd.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */