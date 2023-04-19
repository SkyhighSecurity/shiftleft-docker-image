/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.math.RoundingMode;
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
/*     */ public class OpDivide
/*     */   extends Operator
/*     */ {
/*     */   public OpDivide(int pos, SpelNodeImpl... operands) {
/*  42 */     super("/", pos, operands);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  48 */     Object leftOperand = getLeftOperand().getValueInternal(state).getValue();
/*  49 */     Object rightOperand = getRightOperand().getValueInternal(state).getValue();
/*     */     
/*  51 */     if (leftOperand instanceof Number && rightOperand instanceof Number) {
/*  52 */       Number leftNumber = (Number)leftOperand;
/*  53 */       Number rightNumber = (Number)rightOperand;
/*     */       
/*  55 */       if (leftNumber instanceof BigDecimal || rightNumber instanceof BigDecimal) {
/*  56 */         BigDecimal leftBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
/*  57 */         BigDecimal rightBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
/*  58 */         int scale = Math.max(leftBigDecimal.scale(), rightBigDecimal.scale());
/*  59 */         return new TypedValue(leftBigDecimal.divide(rightBigDecimal, scale, RoundingMode.HALF_EVEN));
/*     */       } 
/*  61 */       if (leftNumber instanceof Double || rightNumber instanceof Double) {
/*  62 */         this.exitTypeDescriptor = "D";
/*  63 */         return new TypedValue(Double.valueOf(leftNumber.doubleValue() / rightNumber.doubleValue()));
/*     */       } 
/*  65 */       if (leftNumber instanceof Float || rightNumber instanceof Float) {
/*  66 */         this.exitTypeDescriptor = "F";
/*  67 */         return new TypedValue(Float.valueOf(leftNumber.floatValue() / rightNumber.floatValue()));
/*     */       } 
/*  69 */       if (leftNumber instanceof BigInteger || rightNumber instanceof BigInteger) {
/*  70 */         BigInteger leftBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
/*  71 */         BigInteger rightBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
/*  72 */         return new TypedValue(leftBigInteger.divide(rightBigInteger));
/*     */       } 
/*  74 */       if (leftNumber instanceof Long || rightNumber instanceof Long) {
/*  75 */         this.exitTypeDescriptor = "J";
/*  76 */         return new TypedValue(Long.valueOf(leftNumber.longValue() / rightNumber.longValue()));
/*     */       } 
/*  78 */       if (CodeFlow.isIntegerForNumericOp(leftNumber) || CodeFlow.isIntegerForNumericOp(rightNumber)) {
/*  79 */         this.exitTypeDescriptor = "I";
/*  80 */         return new TypedValue(Integer.valueOf(leftNumber.intValue() / rightNumber.intValue()));
/*     */       } 
/*     */ 
/*     */       
/*  84 */       return new TypedValue(Double.valueOf(leftNumber.doubleValue() / rightNumber.doubleValue()));
/*     */     } 
/*     */ 
/*     */     
/*  88 */     return state.operate(Operation.DIVIDE, leftOperand, rightOperand);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/*  93 */     if (!getLeftOperand().isCompilable()) {
/*  94 */       return false;
/*     */     }
/*  96 */     if (this.children.length > 1 && 
/*  97 */       !getRightOperand().isCompilable()) {
/*  98 */       return false;
/*     */     }
/*     */     
/* 101 */     return (this.exitTypeDescriptor != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 106 */     getLeftOperand().generateCode(mv, cf);
/* 107 */     String leftDesc = (getLeftOperand()).exitTypeDescriptor;
/* 108 */     CodeFlow.insertNumericUnboxOrPrimitiveTypeCoercion(mv, leftDesc, this.exitTypeDescriptor.charAt(0));
/* 109 */     if (this.children.length > 1) {
/* 110 */       cf.enterCompilationScope();
/* 111 */       getRightOperand().generateCode(mv, cf);
/* 112 */       String rightDesc = (getRightOperand()).exitTypeDescriptor;
/* 113 */       cf.exitCompilationScope();
/* 114 */       CodeFlow.insertNumericUnboxOrPrimitiveTypeCoercion(mv, rightDesc, this.exitTypeDescriptor.charAt(0));
/* 115 */       switch (this.exitTypeDescriptor.charAt(0)) {
/*     */         case 'I':
/* 117 */           mv.visitInsn(108);
/*     */           break;
/*     */         case 'J':
/* 120 */           mv.visitInsn(109);
/*     */           break;
/*     */         case 'F':
/* 123 */           mv.visitInsn(110);
/*     */           break;
/*     */         case 'D':
/* 126 */           mv.visitInsn(111);
/*     */           break;
/*     */         default:
/* 129 */           throw new IllegalStateException("Unrecognized exit type descriptor: '" + this.exitTypeDescriptor + "'");
/*     */       } 
/*     */     
/*     */     } 
/* 133 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\OpDivide.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */