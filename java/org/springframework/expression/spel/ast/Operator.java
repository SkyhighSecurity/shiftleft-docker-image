/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.NumberUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public abstract class Operator
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private final String operatorName;
/*     */   protected String leftActualDescriptor;
/*     */   protected String rightActualDescriptor;
/*     */   
/*     */   public Operator(String payload, int pos, SpelNodeImpl... operands) {
/*  55 */     super(pos, operands);
/*  56 */     this.operatorName = payload;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpelNodeImpl getLeftOperand() {
/*  61 */     return this.children[0];
/*     */   }
/*     */   
/*     */   public SpelNodeImpl getRightOperand() {
/*  65 */     return this.children[1];
/*     */   }
/*     */   
/*     */   public final String getOperatorName() {
/*  69 */     return this.operatorName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/*  78 */     StringBuilder sb = new StringBuilder("(");
/*  79 */     sb.append(getChild(0).toStringAST());
/*  80 */     for (int i = 1; i < getChildCount(); i++) {
/*  81 */       sb.append(" ").append(getOperatorName()).append(" ");
/*  82 */       sb.append(getChild(i).toStringAST());
/*     */     } 
/*  84 */     sb.append(")");
/*  85 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isCompilableOperatorUsingNumerics() {
/*  90 */     SpelNodeImpl left = getLeftOperand();
/*  91 */     SpelNodeImpl right = getRightOperand();
/*  92 */     if (!left.isCompilable() || !right.isCompilable()) {
/*  93 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  97 */     String leftDesc = left.exitTypeDescriptor;
/*  98 */     String rightDesc = right.exitTypeDescriptor;
/*  99 */     DescriptorComparison dc = DescriptorComparison.checkNumericCompatibility(leftDesc, rightDesc, this.leftActualDescriptor, this.rightActualDescriptor);
/*     */     
/* 101 */     return (dc.areNumbers && dc.areCompatible);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateComparisonCode(MethodVisitor mv, CodeFlow cf, int compInstruction1, int compInstruction2) {
/* 109 */     String leftDesc = (getLeftOperand()).exitTypeDescriptor;
/* 110 */     String rightDesc = (getRightOperand()).exitTypeDescriptor;
/*     */     
/* 112 */     boolean unboxLeft = !CodeFlow.isPrimitive(leftDesc);
/* 113 */     boolean unboxRight = !CodeFlow.isPrimitive(rightDesc);
/* 114 */     DescriptorComparison dc = DescriptorComparison.checkNumericCompatibility(leftDesc, rightDesc, this.leftActualDescriptor, this.rightActualDescriptor);
/*     */     
/* 116 */     char targetType = dc.compatibleType;
/*     */     
/* 118 */     cf.enterCompilationScope();
/* 119 */     getLeftOperand().generateCode(mv, cf);
/* 120 */     cf.exitCompilationScope();
/* 121 */     if (unboxLeft) {
/* 122 */       CodeFlow.insertUnboxInsns(mv, targetType, leftDesc);
/*     */     }
/*     */     
/* 125 */     cf.enterCompilationScope();
/* 126 */     getRightOperand().generateCode(mv, cf);
/* 127 */     cf.exitCompilationScope();
/* 128 */     if (unboxRight) {
/* 129 */       CodeFlow.insertUnboxInsns(mv, targetType, rightDesc);
/*     */     }
/*     */ 
/*     */     
/* 133 */     Label elseTarget = new Label();
/* 134 */     Label endOfIf = new Label();
/* 135 */     if (targetType == 'D') {
/* 136 */       mv.visitInsn(152);
/* 137 */       mv.visitJumpInsn(compInstruction1, elseTarget);
/*     */     }
/* 139 */     else if (targetType == 'F') {
/* 140 */       mv.visitInsn(150);
/* 141 */       mv.visitJumpInsn(compInstruction1, elseTarget);
/*     */     }
/* 143 */     else if (targetType == 'J') {
/* 144 */       mv.visitInsn(148);
/* 145 */       mv.visitJumpInsn(compInstruction1, elseTarget);
/*     */     }
/* 147 */     else if (targetType == 'I') {
/* 148 */       mv.visitJumpInsn(compInstruction2, elseTarget);
/*     */     } else {
/*     */       
/* 151 */       throw new IllegalStateException("Unexpected descriptor " + leftDesc);
/*     */     } 
/*     */ 
/*     */     
/* 155 */     mv.visitInsn(4);
/* 156 */     mv.visitJumpInsn(167, endOfIf);
/* 157 */     mv.visitLabel(elseTarget);
/* 158 */     mv.visitInsn(3);
/* 159 */     mv.visitLabel(endOfIf);
/* 160 */     cf.pushDescriptor("Z");
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
/*     */   public static boolean equalityCheck(EvaluationContext context, Object left, Object right) {
/* 174 */     if (left instanceof Number && right instanceof Number) {
/* 175 */       Number leftNumber = (Number)left;
/* 176 */       Number rightNumber = (Number)right;
/*     */       
/* 178 */       if (leftNumber instanceof BigDecimal || rightNumber instanceof BigDecimal) {
/* 179 */         BigDecimal leftBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
/* 180 */         BigDecimal rightBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
/* 181 */         return (leftBigDecimal == null) ? ((rightBigDecimal == null)) : ((leftBigDecimal.compareTo(rightBigDecimal) == 0));
/*     */       } 
/* 183 */       if (leftNumber instanceof Double || rightNumber instanceof Double) {
/* 184 */         return (leftNumber.doubleValue() == rightNumber.doubleValue());
/*     */       }
/* 186 */       if (leftNumber instanceof Float || rightNumber instanceof Float) {
/* 187 */         return (leftNumber.floatValue() == rightNumber.floatValue());
/*     */       }
/* 189 */       if (leftNumber instanceof BigInteger || rightNumber instanceof BigInteger) {
/* 190 */         BigInteger leftBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
/* 191 */         BigInteger rightBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
/* 192 */         return (leftBigInteger == null) ? ((rightBigInteger == null)) : ((leftBigInteger.compareTo(rightBigInteger) == 0));
/*     */       } 
/* 194 */       if (leftNumber instanceof Long || rightNumber instanceof Long) {
/* 195 */         return (leftNumber.longValue() == rightNumber.longValue());
/*     */       }
/* 197 */       if (leftNumber instanceof Integer || rightNumber instanceof Integer) {
/* 198 */         return (leftNumber.intValue() == rightNumber.intValue());
/*     */       }
/* 200 */       if (leftNumber instanceof Short || rightNumber instanceof Short) {
/* 201 */         return (leftNumber.shortValue() == rightNumber.shortValue());
/*     */       }
/* 203 */       if (leftNumber instanceof Byte || rightNumber instanceof Byte) {
/* 204 */         return (leftNumber.byteValue() == rightNumber.byteValue());
/*     */       }
/*     */ 
/*     */       
/* 208 */       return (leftNumber.doubleValue() == rightNumber.doubleValue());
/*     */     } 
/*     */ 
/*     */     
/* 212 */     if (left instanceof CharSequence && right instanceof CharSequence) {
/* 213 */       return left.toString().equals(right.toString());
/*     */     }
/*     */     
/* 216 */     if (left instanceof Boolean && right instanceof Boolean) {
/* 217 */       return left.equals(right);
/*     */     }
/*     */     
/* 220 */     if (ObjectUtils.nullSafeEquals(left, right)) {
/* 221 */       return true;
/*     */     }
/*     */     
/* 224 */     if (left instanceof Comparable && right instanceof Comparable) {
/* 225 */       Class<?> ancestor = ClassUtils.determineCommonAncestor(left.getClass(), right.getClass());
/* 226 */       if (ancestor != null && Comparable.class.isAssignableFrom(ancestor)) {
/* 227 */         return (context.getTypeComparator().compare(left, right) == 0);
/*     */       }
/*     */     } 
/*     */     
/* 231 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class DescriptorComparison
/*     */   {
/* 241 */     static final DescriptorComparison NOT_NUMBERS = new DescriptorComparison(false, false, ' ');
/*     */     
/* 243 */     static final DescriptorComparison INCOMPATIBLE_NUMBERS = new DescriptorComparison(true, false, ' ');
/*     */     
/*     */     final boolean areNumbers;
/*     */     
/*     */     final boolean areCompatible;
/*     */     
/*     */     final char compatibleType;
/*     */     
/*     */     private DescriptorComparison(boolean areNumbers, boolean areCompatible, char compatibleType) {
/* 252 */       this.areNumbers = areNumbers;
/* 253 */       this.areCompatible = areCompatible;
/* 254 */       this.compatibleType = compatibleType;
/*     */     }
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
/*     */     public static DescriptorComparison checkNumericCompatibility(String leftDeclaredDescriptor, String rightDeclaredDescriptor, String leftActualDescriptor, String rightActualDescriptor) {
/* 274 */       String ld = leftDeclaredDescriptor;
/* 275 */       String rd = rightDeclaredDescriptor;
/*     */       
/* 277 */       boolean leftNumeric = CodeFlow.isPrimitiveOrUnboxableSupportedNumberOrBoolean(ld);
/* 278 */       boolean rightNumeric = CodeFlow.isPrimitiveOrUnboxableSupportedNumberOrBoolean(rd);
/*     */ 
/*     */       
/* 281 */       if (!leftNumeric && !ObjectUtils.nullSafeEquals(ld, leftActualDescriptor)) {
/* 282 */         ld = leftActualDescriptor;
/* 283 */         leftNumeric = CodeFlow.isPrimitiveOrUnboxableSupportedNumberOrBoolean(ld);
/*     */       } 
/* 285 */       if (!rightNumeric && !ObjectUtils.nullSafeEquals(rd, rightActualDescriptor)) {
/* 286 */         rd = rightActualDescriptor;
/* 287 */         rightNumeric = CodeFlow.isPrimitiveOrUnboxableSupportedNumberOrBoolean(rd);
/*     */       } 
/*     */       
/* 290 */       if (leftNumeric && rightNumeric) {
/* 291 */         if (CodeFlow.areBoxingCompatible(ld, rd)) {
/* 292 */           return new DescriptorComparison(true, true, CodeFlow.toPrimitiveTargetDesc(ld));
/*     */         }
/*     */         
/* 295 */         return INCOMPATIBLE_NUMBERS;
/*     */       } 
/*     */ 
/*     */       
/* 299 */       return NOT_NUMBERS;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\Operator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */