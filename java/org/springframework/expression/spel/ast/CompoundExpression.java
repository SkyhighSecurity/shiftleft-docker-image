/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
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
/*     */ public class CompoundExpression
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   public CompoundExpression(int pos, SpelNodeImpl... expressionComponents) {
/*  35 */     super(pos, expressionComponents);
/*  36 */     if (expressionComponents.length < 2) {
/*  37 */       throw new IllegalStateException("Do not build compound expressions with less than two entries: " + expressionComponents.length);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ValueRef getValueRef(ExpressionState state) throws EvaluationException {
/*  45 */     if (getChildCount() == 1) {
/*  46 */       return this.children[0].getValueRef(state);
/*     */     }
/*     */     
/*  49 */     SpelNodeImpl nextNode = this.children[0];
/*     */     try {
/*  51 */       TypedValue result = nextNode.getValueInternal(state);
/*  52 */       int cc = getChildCount();
/*  53 */       for (int i = 1; i < cc - 1; i++) {
/*     */         
/*  55 */         try { state.pushActiveContextObject(result);
/*  56 */           nextNode = this.children[i];
/*  57 */           result = nextNode.getValueInternal(state);
/*     */ 
/*     */           
/*  60 */           state.popActiveContextObject(); } finally { state.popActiveContextObject(); }
/*     */       
/*     */       } 
/*     */       try {
/*  64 */         state.pushActiveContextObject(result);
/*  65 */         nextNode = this.children[cc - 1];
/*  66 */         return nextNode.getValueRef(state);
/*     */       } finally {
/*     */         
/*  69 */         state.popActiveContextObject();
/*     */       }
/*     */     
/*  72 */     } catch (SpelEvaluationException ex) {
/*     */       
/*  74 */       ex.setPosition(nextNode.getStartPosition());
/*  75 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  87 */     ValueRef ref = getValueRef(state);
/*  88 */     TypedValue result = ref.getValue();
/*  89 */     this.exitTypeDescriptor = (this.children[this.children.length - 1]).exitTypeDescriptor;
/*  90 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(ExpressionState state, Object value) throws EvaluationException {
/*  95 */     getValueRef(state).setValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(ExpressionState state) throws EvaluationException {
/* 100 */     return getValueRef(state).isWritable();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 105 */     StringBuilder sb = new StringBuilder();
/* 106 */     for (int i = 0; i < getChildCount(); i++) {
/* 107 */       if (i > 0) {
/* 108 */         sb.append(".");
/*     */       }
/* 110 */       sb.append(getChild(i).toStringAST());
/*     */     } 
/* 112 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 117 */     for (SpelNodeImpl child : this.children) {
/* 118 */       if (!child.isCompilable()) {
/* 119 */         return false;
/*     */       }
/*     */     } 
/* 122 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 127 */     for (int i = 0; i < this.children.length; i++) {
/* 128 */       this.children[i].generateCode(mv, cf);
/*     */     }
/* 130 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\CompoundExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */