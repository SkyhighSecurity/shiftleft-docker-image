/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OperatorInstanceof
/*     */   extends Operator
/*     */ {
/*     */   private Class<?> type;
/*     */   
/*     */   public OperatorInstanceof(int pos, SpelNodeImpl... operands) {
/*  42 */     super("instanceof", pos, operands);
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
/*     */   public BooleanTypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*     */     BooleanTypedValue result;
/*  56 */     SpelNodeImpl rightOperand = getRightOperand();
/*  57 */     TypedValue left = getLeftOperand().getValueInternal(state);
/*  58 */     TypedValue right = rightOperand.getValueInternal(state);
/*  59 */     Object leftValue = left.getValue();
/*  60 */     Object rightValue = right.getValue();
/*     */     
/*  62 */     if (rightValue == null || !(rightValue instanceof Class)) {
/*  63 */       throw new SpelEvaluationException(getRightOperand().getStartPosition(), SpelMessage.INSTANCEOF_OPERATOR_NEEDS_CLASS_OPERAND, new Object[] { (rightValue == null) ? "null" : rightValue
/*     */             
/*  65 */             .getClass().getName() });
/*     */     }
/*  67 */     Class<?> rightClass = (Class)rightValue;
/*  68 */     if (leftValue == null) {
/*  69 */       result = BooleanTypedValue.FALSE;
/*     */     } else {
/*     */       
/*  72 */       result = BooleanTypedValue.forValue(rightClass.isAssignableFrom(leftValue.getClass()));
/*     */     } 
/*  74 */     this.type = rightClass;
/*  75 */     if (rightOperand instanceof TypeReference)
/*     */     {
/*     */       
/*  78 */       this.exitTypeDescriptor = "Z";
/*     */     }
/*  80 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/*  85 */     return (this.exitTypeDescriptor != null && getLeftOperand().isCompilable());
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/*  90 */     getLeftOperand().generateCode(mv, cf);
/*  91 */     CodeFlow.insertBoxIfNecessary(mv, cf.lastDescriptor());
/*  92 */     if (this.type.isPrimitive()) {
/*     */ 
/*     */       
/*  95 */       mv.visitInsn(87);
/*  96 */       mv.visitInsn(3);
/*     */     } else {
/*     */       
/*  99 */       mv.visitTypeInsn(193, Type.getInternalName(this.type));
/*     */     } 
/* 101 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\OperatorInstanceof.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */