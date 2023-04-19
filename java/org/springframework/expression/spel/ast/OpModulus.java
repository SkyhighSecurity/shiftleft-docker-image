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
/*     */ public class OpModulus
/*     */   extends Operator
/*     */ {
/*     */   public OpModulus(int pos, SpelNodeImpl... operands) {
/*  41 */     super("%", pos, operands);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  47 */     Object leftOperand = getLeftOperand().getValueInternal(state).getValue();
/*  48 */     Object rightOperand = getRightOperand().getValueInternal(state).getValue();
/*     */     
/*  50 */     if (leftOperand instanceof Number && rightOperand instanceof Number) {
/*  51 */       Number leftNumber = (Number)leftOperand;
/*  52 */       Number rightNumber = (Number)rightOperand;
/*     */       
/*  54 */       if (leftNumber instanceof BigDecimal || rightNumber instanceof BigDecimal) {
/*  55 */         BigDecimal leftBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
/*  56 */         BigDecimal rightBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
/*  57 */         return new TypedValue(leftBigDecimal.remainder(rightBigDecimal));
/*     */       } 
/*  59 */       if (leftNumber instanceof Double || rightNumber instanceof Double) {
/*  60 */         this.exitTypeDescriptor = "D";
/*  61 */         return new TypedValue(Double.valueOf(leftNumber.doubleValue() % rightNumber.doubleValue()));
/*     */       } 
/*  63 */       if (leftNumber instanceof Float || rightNumber instanceof Float) {
/*  64 */         this.exitTypeDescriptor = "F";
/*  65 */         return new TypedValue(Float.valueOf(leftNumber.floatValue() % rightNumber.floatValue()));
/*     */       } 
/*  67 */       if (leftNumber instanceof BigInteger || rightNumber instanceof BigInteger) {
/*  68 */         BigInteger leftBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
/*  69 */         BigInteger rightBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
/*  70 */         return new TypedValue(leftBigInteger.remainder(rightBigInteger));
/*     */       } 
/*  72 */       if (leftNumber instanceof Long || rightNumber instanceof Long) {
/*  73 */         this.exitTypeDescriptor = "J";
/*  74 */         return new TypedValue(Long.valueOf(leftNumber.longValue() % rightNumber.longValue()));
/*     */       } 
/*  76 */       if (CodeFlow.isIntegerForNumericOp(leftNumber) || CodeFlow.isIntegerForNumericOp(rightNumber)) {
/*  77 */         this.exitTypeDescriptor = "I";
/*  78 */         return new TypedValue(Integer.valueOf(leftNumber.intValue() % rightNumber.intValue()));
/*     */       } 
/*     */ 
/*     */       
/*  82 */       return new TypedValue(Double.valueOf(leftNumber.doubleValue() % rightNumber.doubleValue()));
/*     */     } 
/*     */ 
/*     */     
/*  86 */     return state.operate(Operation.MODULUS, leftOperand, rightOperand);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/*  91 */     if (!getLeftOperand().isCompilable()) {
/*  92 */       return false;
/*     */     }
/*  94 */     if (this.children.length > 1 && 
/*  95 */       !getRightOperand().isCompilable()) {
/*  96 */       return false;
/*     */     }
/*     */     
/*  99 */     return (this.exitTypeDescriptor != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 104 */     getLeftOperand().generateCode(mv, cf);
/* 105 */     String leftDesc = (getLeftOperand()).exitTypeDescriptor;
/* 106 */     CodeFlow.insertNumericUnboxOrPrimitiveTypeCoercion(mv, leftDesc, this.exitTypeDescriptor.charAt(0));
/* 107 */     if (this.children.length > 1) {
/* 108 */       cf.enterCompilationScope();
/* 109 */       getRightOperand().generateCode(mv, cf);
/* 110 */       String rightDesc = (getRightOperand()).exitTypeDescriptor;
/* 111 */       cf.exitCompilationScope();
/* 112 */       CodeFlow.insertNumericUnboxOrPrimitiveTypeCoercion(mv, rightDesc, this.exitTypeDescriptor.charAt(0));
/* 113 */       switch (this.exitTypeDescriptor.charAt(0)) {
/*     */         case 'I':
/* 115 */           mv.visitInsn(112);
/*     */           break;
/*     */         case 'J':
/* 118 */           mv.visitInsn(113);
/*     */           break;
/*     */         case 'F':
/* 121 */           mv.visitInsn(114);
/*     */           break;
/*     */         case 'D':
/* 124 */           mv.visitInsn(115);
/*     */           break;
/*     */         default:
/* 127 */           throw new IllegalStateException("Unrecognized exit type descriptor: '" + this.exitTypeDescriptor + "'");
/*     */       } 
/*     */     
/*     */     } 
/* 131 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\OpModulus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */