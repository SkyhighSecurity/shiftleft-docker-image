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
/*    */ public class OpOr
/*    */   extends Operator
/*    */ {
/*    */   public OpOr(int pos, SpelNodeImpl... operands) {
/* 39 */     super("or", pos, operands);
/* 40 */     this.exitTypeDescriptor = "Z";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BooleanTypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/* 46 */     if (getBooleanValue(state, getLeftOperand()))
/*    */     {
/* 48 */       return BooleanTypedValue.TRUE;
/*    */     }
/* 50 */     return BooleanTypedValue.forValue(getBooleanValue(state, getRightOperand()));
/*    */   }
/*    */   
/*    */   private boolean getBooleanValue(ExpressionState state, SpelNodeImpl operand) {
/*    */     try {
/* 55 */       Boolean value = operand.<Boolean>getValue(state, Boolean.class);
/* 56 */       assertValueNotNull(value);
/* 57 */       return value.booleanValue();
/*    */     }
/* 59 */     catch (SpelEvaluationException ee) {
/* 60 */       ee.setPosition(operand.getStartPosition());
/* 61 */       throw ee;
/*    */     } 
/*    */   }
/*    */   
/*    */   private void assertValueNotNull(Boolean value) {
/* 66 */     if (value == null) {
/* 67 */       throw new SpelEvaluationException(SpelMessage.TYPE_CONVERSION_ERROR, new Object[] { "null", "boolean" });
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCompilable() {
/* 73 */     SpelNodeImpl left = getLeftOperand();
/* 74 */     SpelNodeImpl right = getRightOperand();
/* 75 */     return (left.isCompilable() && right.isCompilable() && 
/* 76 */       CodeFlow.isBooleanCompatible(left.exitTypeDescriptor) && 
/* 77 */       CodeFlow.isBooleanCompatible(right.exitTypeDescriptor));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 83 */     Label elseTarget = new Label();
/* 84 */     Label endOfIf = new Label();
/* 85 */     cf.enterCompilationScope();
/* 86 */     getLeftOperand().generateCode(mv, cf);
/* 87 */     cf.unboxBooleanIfNecessary(mv);
/* 88 */     cf.exitCompilationScope();
/* 89 */     mv.visitJumpInsn(153, elseTarget);
/* 90 */     mv.visitLdcInsn(Integer.valueOf(1));
/* 91 */     mv.visitJumpInsn(167, endOfIf);
/* 92 */     mv.visitLabel(elseTarget);
/* 93 */     cf.enterCompilationScope();
/* 94 */     getRightOperand().generateCode(mv, cf);
/* 95 */     cf.unboxBooleanIfNecessary(mv);
/* 96 */     cf.exitCompilationScope();
/* 97 */     mv.visitLabel(endOfIf);
/* 98 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\OpOr.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */