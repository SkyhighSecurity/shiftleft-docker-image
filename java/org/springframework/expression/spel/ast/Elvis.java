/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class Elvis
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   public Elvis(int pos, SpelNodeImpl... args) {
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
/*  51 */     TypedValue value = this.children[0].getValueInternal(state);
/*     */     
/*  53 */     if (!StringUtils.isEmpty(value.getValue())) {
/*  54 */       return value;
/*     */     }
/*     */     
/*  57 */     TypedValue result = this.children[1].getValueInternal(state);
/*  58 */     computeExitTypeDescriptor();
/*  59 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/*  65 */     return getChild(0).toStringAST() + " ?: " + getChild(1).toStringAST();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/*  70 */     SpelNodeImpl condition = this.children[0];
/*  71 */     SpelNodeImpl ifNullValue = this.children[1];
/*  72 */     return (condition.isCompilable() && ifNullValue.isCompilable() && condition.exitTypeDescriptor != null && ifNullValue.exitTypeDescriptor != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/*  79 */     computeExitTypeDescriptor();
/*  80 */     cf.enterCompilationScope();
/*  81 */     this.children[0].generateCode(mv, cf);
/*  82 */     CodeFlow.insertBoxIfNecessary(mv, cf.lastDescriptor().charAt(0));
/*  83 */     cf.exitCompilationScope();
/*  84 */     Label elseTarget = new Label();
/*  85 */     Label endOfIf = new Label();
/*  86 */     mv.visitInsn(89);
/*  87 */     mv.visitJumpInsn(198, elseTarget);
/*     */     
/*  89 */     mv.visitInsn(89);
/*  90 */     mv.visitLdcInsn("");
/*  91 */     mv.visitInsn(95);
/*  92 */     mv.visitMethodInsn(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
/*  93 */     mv.visitJumpInsn(153, endOfIf);
/*  94 */     mv.visitLabel(elseTarget);
/*  95 */     mv.visitInsn(87);
/*  96 */     cf.enterCompilationScope();
/*  97 */     this.children[1].generateCode(mv, cf);
/*  98 */     if (!CodeFlow.isPrimitive(this.exitTypeDescriptor)) {
/*  99 */       CodeFlow.insertBoxIfNecessary(mv, cf.lastDescriptor().charAt(0));
/*     */     }
/* 101 */     cf.exitCompilationScope();
/* 102 */     mv.visitLabel(endOfIf);
/* 103 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */   
/*     */   private void computeExitTypeDescriptor() {
/* 107 */     if (this.exitTypeDescriptor == null && (this.children[0]).exitTypeDescriptor != null && (this.children[1]).exitTypeDescriptor != null) {
/*     */       
/* 109 */       String conditionDescriptor = (this.children[0]).exitTypeDescriptor;
/* 110 */       String ifNullValueDescriptor = (this.children[1]).exitTypeDescriptor;
/* 111 */       if (conditionDescriptor.equals(ifNullValueDescriptor)) {
/* 112 */         this.exitTypeDescriptor = conditionDescriptor;
/*     */       }
/*     */       else {
/*     */         
/* 116 */         this.exitTypeDescriptor = "Ljava/lang/Object";
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\Elvis.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */