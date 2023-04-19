/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.Operation;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.util.NumberUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OpMultiply
/*     */   extends Operator
/*     */ {
/*     */   public OpMultiply(int pos, SpelNodeImpl... operands) {
/*  55 */     super("*", pos, operands);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  71 */     Object leftOperand = getLeftOperand().getValueInternal(state).getValue();
/*  72 */     Object rightOperand = getRightOperand().getValueInternal(state).getValue();
/*     */     
/*  74 */     if (leftOperand instanceof Number && rightOperand instanceof Number) {
/*  75 */       Number leftNumber = (Number)leftOperand;
/*  76 */       Number rightNumber = (Number)rightOperand;
/*     */       
/*  78 */       if (leftNumber instanceof BigDecimal || rightNumber instanceof BigDecimal) {
/*  79 */         BigDecimal leftBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
/*  80 */         BigDecimal rightBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
/*  81 */         return new TypedValue(leftBigDecimal.multiply(rightBigDecimal));
/*     */       } 
/*  83 */       if (leftNumber instanceof Double || rightNumber instanceof Double) {
/*  84 */         this.exitTypeDescriptor = "D";
/*  85 */         return new TypedValue(Double.valueOf(leftNumber.doubleValue() * rightNumber.doubleValue()));
/*     */       } 
/*  87 */       if (leftNumber instanceof Float || rightNumber instanceof Float) {
/*  88 */         this.exitTypeDescriptor = "F";
/*  89 */         return new TypedValue(Float.valueOf(leftNumber.floatValue() * rightNumber.floatValue()));
/*     */       } 
/*  91 */       if (leftNumber instanceof BigInteger || rightNumber instanceof BigInteger) {
/*  92 */         BigInteger leftBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
/*  93 */         BigInteger rightBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
/*  94 */         return new TypedValue(leftBigInteger.multiply(rightBigInteger));
/*     */       } 
/*  96 */       if (leftNumber instanceof Long || rightNumber instanceof Long) {
/*  97 */         this.exitTypeDescriptor = "J";
/*  98 */         return new TypedValue(Long.valueOf(leftNumber.longValue() * rightNumber.longValue()));
/*     */       } 
/* 100 */       if (CodeFlow.isIntegerForNumericOp(leftNumber) || CodeFlow.isIntegerForNumericOp(rightNumber)) {
/* 101 */         this.exitTypeDescriptor = "I";
/* 102 */         return new TypedValue(Integer.valueOf(leftNumber.intValue() * rightNumber.intValue()));
/*     */       } 
/*     */ 
/*     */       
/* 106 */       return new TypedValue(Double.valueOf(leftNumber.doubleValue() * rightNumber.doubleValue()));
/*     */     } 
/*     */ 
/*     */     
/* 110 */     if (leftOperand instanceof String && rightOperand instanceof Integer) {
/* 111 */       int repeats = ((Integer)rightOperand).intValue();
/* 112 */       StringBuilder result = new StringBuilder();
/* 113 */       for (int i = 0; i < repeats; i++) {
/* 114 */         result.append(leftOperand);
/*     */       }
/* 116 */       return new TypedValue(result.toString());
/*     */     } 
/*     */     
/* 119 */     return state.operate(Operation.MULTIPLY, leftOperand, rightOperand);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 124 */     if (!getLeftOperand().isCompilable()) {
/* 125 */       return false;
/*     */     }
/* 127 */     if (this.children.length > 1 && 
/* 128 */       !getRightOperand().isCompilable()) {
/* 129 */       return false;
/*     */     }
/*     */     
/* 132 */     return (this.exitTypeDescriptor != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 137 */     getLeftOperand().generateCode(mv, cf);
/* 138 */     String leftDesc = (getLeftOperand()).exitTypeDescriptor;
/* 139 */     CodeFlow.insertNumericUnboxOrPrimitiveTypeCoercion(mv, leftDesc, this.exitTypeDescriptor.charAt(0));
/* 140 */     if (this.children.length > 1) {
/* 141 */       cf.enterCompilationScope();
/* 142 */       getRightOperand().generateCode(mv, cf);
/* 143 */       String rightDesc = (getRightOperand()).exitTypeDescriptor;
/* 144 */       cf.exitCompilationScope();
/* 145 */       CodeFlow.insertNumericUnboxOrPrimitiveTypeCoercion(mv, rightDesc, this.exitTypeDescriptor.charAt(0));
/* 146 */       switch (this.exitTypeDescriptor.charAt(0)) {
/*     */         case 'I':
/* 148 */           mv.visitInsn(104);
/*     */           break;
/*     */         case 'J':
/* 151 */           mv.visitInsn(105);
/*     */           break;
/*     */         case 'F':
/* 154 */           mv.visitInsn(106);
/*     */           break;
/*     */         case 'D':
/* 157 */           mv.visitInsn(107);
/*     */           break;
/*     */         default:
/* 160 */           throw new IllegalStateException("Unrecognized exit type descriptor: '" + this.exitTypeDescriptor + "'");
/*     */       } 
/*     */     
/*     */     } 
/* 164 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\OpMultiply.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */