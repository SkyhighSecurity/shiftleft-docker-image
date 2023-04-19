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
/*     */ public class OpMinus
/*     */   extends Operator
/*     */ {
/*     */   public OpMinus(int pos, SpelNodeImpl... operands) {
/*  50 */     super("-", pos, operands);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  56 */     SpelNodeImpl leftOp = getLeftOperand();
/*  57 */     SpelNodeImpl rightOp = getRightOperand();
/*     */     
/*  59 */     if (rightOp == null) {
/*  60 */       Object operand = leftOp.getValueInternal(state).getValue();
/*  61 */       if (operand instanceof Number) {
/*  62 */         if (operand instanceof BigDecimal) {
/*  63 */           return new TypedValue(((BigDecimal)operand).negate());
/*     */         }
/*  65 */         if (operand instanceof Double) {
/*  66 */           this.exitTypeDescriptor = "D";
/*  67 */           return new TypedValue(Double.valueOf(0.0D - ((Number)operand).doubleValue()));
/*     */         } 
/*  69 */         if (operand instanceof Float) {
/*  70 */           this.exitTypeDescriptor = "F";
/*  71 */           return new TypedValue(Float.valueOf(0.0F - ((Number)operand).floatValue()));
/*     */         } 
/*  73 */         if (operand instanceof BigInteger) {
/*  74 */           return new TypedValue(((BigInteger)operand).negate());
/*     */         }
/*  76 */         if (operand instanceof Long) {
/*  77 */           this.exitTypeDescriptor = "J";
/*  78 */           return new TypedValue(Long.valueOf(0L - ((Number)operand).longValue()));
/*     */         } 
/*  80 */         if (operand instanceof Integer) {
/*  81 */           this.exitTypeDescriptor = "I";
/*  82 */           return new TypedValue(Integer.valueOf(0 - ((Number)operand).intValue()));
/*     */         } 
/*  84 */         if (operand instanceof Short) {
/*  85 */           return new TypedValue(Integer.valueOf(0 - ((Number)operand).shortValue()));
/*     */         }
/*  87 */         if (operand instanceof Byte) {
/*  88 */           return new TypedValue(Integer.valueOf(0 - ((Number)operand).byteValue()));
/*     */         }
/*     */ 
/*     */         
/*  92 */         return new TypedValue(Double.valueOf(0.0D - ((Number)operand).doubleValue()));
/*     */       } 
/*     */       
/*  95 */       return state.operate(Operation.SUBTRACT, operand, null);
/*     */     } 
/*     */     
/*  98 */     Object left = leftOp.getValueInternal(state).getValue();
/*  99 */     Object right = rightOp.getValueInternal(state).getValue();
/*     */     
/* 101 */     if (left instanceof Number && right instanceof Number) {
/* 102 */       Number leftNumber = (Number)left;
/* 103 */       Number rightNumber = (Number)right;
/*     */       
/* 105 */       if (leftNumber instanceof BigDecimal || rightNumber instanceof BigDecimal) {
/* 106 */         BigDecimal leftBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
/* 107 */         BigDecimal rightBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
/* 108 */         return new TypedValue(leftBigDecimal.subtract(rightBigDecimal));
/*     */       } 
/* 110 */       if (leftNumber instanceof Double || rightNumber instanceof Double) {
/* 111 */         this.exitTypeDescriptor = "D";
/* 112 */         return new TypedValue(Double.valueOf(leftNumber.doubleValue() - rightNumber.doubleValue()));
/*     */       } 
/* 114 */       if (leftNumber instanceof Float || rightNumber instanceof Float) {
/* 115 */         this.exitTypeDescriptor = "F";
/* 116 */         return new TypedValue(Float.valueOf(leftNumber.floatValue() - rightNumber.floatValue()));
/*     */       } 
/* 118 */       if (leftNumber instanceof BigInteger || rightNumber instanceof BigInteger) {
/* 119 */         BigInteger leftBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
/* 120 */         BigInteger rightBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
/* 121 */         return new TypedValue(leftBigInteger.subtract(rightBigInteger));
/*     */       } 
/* 123 */       if (leftNumber instanceof Long || rightNumber instanceof Long) {
/* 124 */         this.exitTypeDescriptor = "J";
/* 125 */         return new TypedValue(Long.valueOf(leftNumber.longValue() - rightNumber.longValue()));
/*     */       } 
/* 127 */       if (CodeFlow.isIntegerForNumericOp(leftNumber) || CodeFlow.isIntegerForNumericOp(rightNumber)) {
/* 128 */         this.exitTypeDescriptor = "I";
/* 129 */         return new TypedValue(Integer.valueOf(leftNumber.intValue() - rightNumber.intValue()));
/*     */       } 
/*     */ 
/*     */       
/* 133 */       return new TypedValue(Double.valueOf(leftNumber.doubleValue() - rightNumber.doubleValue()));
/*     */     } 
/*     */ 
/*     */     
/* 137 */     if (left instanceof String && right instanceof Integer && ((String)left).length() == 1) {
/* 138 */       String theString = (String)left;
/* 139 */       Integer theInteger = (Integer)right;
/*     */       
/* 141 */       return new TypedValue(Character.toString((char)(theString.charAt(0) - theInteger.intValue())));
/*     */     } 
/*     */     
/* 144 */     return state.operate(Operation.SUBTRACT, left, right);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 149 */     if (getRightOperand() == null) {
/* 150 */       return "-" + getLeftOperand().toStringAST();
/*     */     }
/* 152 */     return super.toStringAST();
/*     */   }
/*     */ 
/*     */   
/*     */   public SpelNodeImpl getRightOperand() {
/* 157 */     if (this.children.length < 2) {
/* 158 */       return null;
/*     */     }
/* 160 */     return this.children[1];
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 165 */     if (!getLeftOperand().isCompilable()) {
/* 166 */       return false;
/*     */     }
/* 168 */     if (this.children.length > 1 && 
/* 169 */       !getRightOperand().isCompilable()) {
/* 170 */       return false;
/*     */     }
/*     */     
/* 173 */     return (this.exitTypeDescriptor != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 178 */     getLeftOperand().generateCode(mv, cf);
/* 179 */     String leftDesc = (getLeftOperand()).exitTypeDescriptor;
/* 180 */     CodeFlow.insertNumericUnboxOrPrimitiveTypeCoercion(mv, leftDesc, this.exitTypeDescriptor.charAt(0));
/* 181 */     if (this.children.length > 1) {
/* 182 */       cf.enterCompilationScope();
/* 183 */       getRightOperand().generateCode(mv, cf);
/* 184 */       String rightDesc = (getRightOperand()).exitTypeDescriptor;
/* 185 */       cf.exitCompilationScope();
/* 186 */       CodeFlow.insertNumericUnboxOrPrimitiveTypeCoercion(mv, rightDesc, this.exitTypeDescriptor.charAt(0));
/* 187 */       switch (this.exitTypeDescriptor.charAt(0)) {
/*     */         case 'I':
/* 189 */           mv.visitInsn(100);
/*     */           break;
/*     */         case 'J':
/* 192 */           mv.visitInsn(101);
/*     */           break;
/*     */         case 'F':
/* 195 */           mv.visitInsn(102);
/*     */           break;
/*     */         case 'D':
/* 198 */           mv.visitInsn(103);
/*     */           break;
/*     */         default:
/* 201 */           throw new IllegalStateException("Unrecognized exit type descriptor: '" + this.exitTypeDescriptor + "'");
/*     */       } 
/*     */ 
/*     */     
/*     */     } else {
/* 206 */       switch (this.exitTypeDescriptor.charAt(0)) {
/*     */         case 'I':
/* 208 */           mv.visitInsn(116);
/*     */           break;
/*     */         case 'J':
/* 211 */           mv.visitInsn(117);
/*     */           break;
/*     */         case 'F':
/* 214 */           mv.visitInsn(118);
/*     */           break;
/*     */         case 'D':
/* 217 */           mv.visitInsn(119);
/*     */           break;
/*     */         default:
/* 220 */           throw new IllegalStateException("Unrecognized exit type descriptor: '" + this.exitTypeDescriptor + "'");
/*     */       } 
/*     */     
/*     */     } 
/* 224 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\OpMinus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */