/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.support.BooleanTypedValue;
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
/*     */ public class OpNE
/*     */   extends Operator
/*     */ {
/*     */   public OpNE(int pos, SpelNodeImpl... operands) {
/*  36 */     super("!=", pos, operands);
/*  37 */     this.exitTypeDescriptor = "Z";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BooleanTypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  43 */     Object left = getLeftOperand().getValueInternal(state).getValue();
/*  44 */     Object right = getRightOperand().getValueInternal(state).getValue();
/*  45 */     this.leftActualDescriptor = CodeFlow.toDescriptorFromObject(left);
/*  46 */     this.rightActualDescriptor = CodeFlow.toDescriptorFromObject(right);
/*  47 */     return BooleanTypedValue.forValue(
/*  48 */         !equalityCheck(state.getEvaluationContext(), left, right));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/*  55 */     SpelNodeImpl left = getLeftOperand();
/*  56 */     SpelNodeImpl right = getRightOperand();
/*  57 */     if (!left.isCompilable() || !right.isCompilable()) {
/*  58 */       return false;
/*     */     }
/*     */     
/*  61 */     String leftDesc = left.exitTypeDescriptor;
/*  62 */     String rightDesc = right.exitTypeDescriptor;
/*  63 */     Operator.DescriptorComparison dc = Operator.DescriptorComparison.checkNumericCompatibility(leftDesc, rightDesc, this.leftActualDescriptor, this.rightActualDescriptor);
/*     */     
/*  65 */     return (!dc.areNumbers || dc.areCompatible);
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/*  70 */     cf.loadEvaluationContext(mv);
/*  71 */     String leftDesc = (getLeftOperand()).exitTypeDescriptor;
/*  72 */     String rightDesc = (getRightOperand()).exitTypeDescriptor;
/*  73 */     boolean leftPrim = CodeFlow.isPrimitive(leftDesc);
/*  74 */     boolean rightPrim = CodeFlow.isPrimitive(rightDesc);
/*     */     
/*  76 */     cf.enterCompilationScope();
/*  77 */     getLeftOperand().generateCode(mv, cf);
/*  78 */     cf.exitCompilationScope();
/*  79 */     if (leftPrim) {
/*  80 */       CodeFlow.insertBoxIfNecessary(mv, leftDesc.charAt(0));
/*     */     }
/*  82 */     cf.enterCompilationScope();
/*  83 */     getRightOperand().generateCode(mv, cf);
/*  84 */     cf.exitCompilationScope();
/*  85 */     if (rightPrim) {
/*  86 */       CodeFlow.insertBoxIfNecessary(mv, rightDesc.charAt(0));
/*     */     }
/*     */     
/*  89 */     String operatorClassName = Operator.class.getName().replace('.', '/');
/*  90 */     String evaluationContextClassName = EvaluationContext.class.getName().replace('.', '/');
/*  91 */     mv.visitMethodInsn(184, operatorClassName, "equalityCheck", "(L" + evaluationContextClassName + ";Ljava/lang/Object;Ljava/lang/Object;)Z", false);
/*     */ 
/*     */ 
/*     */     
/*  95 */     Label notZero = new Label();
/*  96 */     Label end = new Label();
/*  97 */     mv.visitJumpInsn(154, notZero);
/*  98 */     mv.visitInsn(4);
/*  99 */     mv.visitJumpInsn(167, end);
/* 100 */     mv.visitLabel(notZero);
/* 101 */     mv.visitInsn(3);
/* 102 */     mv.visitLabel(end);
/*     */     
/* 104 */     cf.pushDescriptor("Z");
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\OpNE.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */