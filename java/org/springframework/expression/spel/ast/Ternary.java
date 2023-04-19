/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
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
/*     */ public class Ternary
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   public Ternary(int pos, SpelNodeImpl... args) {
/*  38 */     super(pos, args);
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
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  51 */     Boolean value = this.children[0].<Boolean>getValue(state, Boolean.class);
/*  52 */     if (value == null) {
/*  53 */       throw new SpelEvaluationException(getChild(0).getStartPosition(), SpelMessage.TYPE_CONVERSION_ERROR, new Object[] { "null", "boolean" });
/*     */     }
/*     */     
/*  56 */     TypedValue result = this.children[value.booleanValue() ? 1 : 2].getValueInternal(state);
/*  57 */     computeExitTypeDescriptor();
/*  58 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/*  63 */     return getChild(0).toStringAST() + " ? " + getChild(1).toStringAST() + " : " + getChild(2).toStringAST();
/*     */   }
/*     */   
/*     */   private void computeExitTypeDescriptor() {
/*  67 */     if (this.exitTypeDescriptor == null && (this.children[1]).exitTypeDescriptor != null && (this.children[2]).exitTypeDescriptor != null) {
/*     */       
/*  69 */       String leftDescriptor = (this.children[1]).exitTypeDescriptor;
/*  70 */       String rightDescriptor = (this.children[2]).exitTypeDescriptor;
/*  71 */       if (leftDescriptor.equals(rightDescriptor)) {
/*  72 */         this.exitTypeDescriptor = leftDescriptor;
/*     */       }
/*     */       else {
/*     */         
/*  76 */         this.exitTypeDescriptor = "Ljava/lang/Object";
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/*  83 */     SpelNodeImpl condition = this.children[0];
/*  84 */     SpelNodeImpl left = this.children[1];
/*  85 */     SpelNodeImpl right = this.children[2];
/*  86 */     return (condition.isCompilable() && left.isCompilable() && right.isCompilable() && 
/*  87 */       CodeFlow.isBooleanCompatible(condition.exitTypeDescriptor) && left.exitTypeDescriptor != null && right.exitTypeDescriptor != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/*  94 */     computeExitTypeDescriptor();
/*  95 */     cf.enterCompilationScope();
/*  96 */     this.children[0].generateCode(mv, cf);
/*  97 */     if (!CodeFlow.isPrimitive(cf.lastDescriptor())) {
/*  98 */       CodeFlow.insertUnboxInsns(mv, 'Z', cf.lastDescriptor());
/*     */     }
/* 100 */     cf.exitCompilationScope();
/* 101 */     Label elseTarget = new Label();
/* 102 */     Label endOfIf = new Label();
/* 103 */     mv.visitJumpInsn(153, elseTarget);
/* 104 */     cf.enterCompilationScope();
/* 105 */     this.children[1].generateCode(mv, cf);
/* 106 */     if (!CodeFlow.isPrimitive(this.exitTypeDescriptor)) {
/* 107 */       CodeFlow.insertBoxIfNecessary(mv, cf.lastDescriptor().charAt(0));
/*     */     }
/* 109 */     cf.exitCompilationScope();
/* 110 */     mv.visitJumpInsn(167, endOfIf);
/* 111 */     mv.visitLabel(elseTarget);
/* 112 */     cf.enterCompilationScope();
/* 113 */     this.children[2].generateCode(mv, cf);
/* 114 */     if (!CodeFlow.isPrimitive(this.exitTypeDescriptor)) {
/* 115 */       CodeFlow.insertBoxIfNecessary(mv, cf.lastDescriptor().charAt(0));
/*     */     }
/* 117 */     cf.exitCompilationScope();
/* 118 */     mv.visitLabel(endOfIf);
/* 119 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\Ternary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */