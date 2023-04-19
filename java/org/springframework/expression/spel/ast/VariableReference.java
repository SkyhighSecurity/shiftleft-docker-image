/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationContext;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VariableReference
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private static final String THIS = "this";
/*     */   private static final String ROOT = "root";
/*     */   private final String name;
/*     */   
/*     */   public VariableReference(String variableName, int pos) {
/*  47 */     super(pos, new SpelNodeImpl[0]);
/*  48 */     this.name = variableName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueRef getValueRef(ExpressionState state) throws SpelEvaluationException {
/*  54 */     if (this.name.equals("this")) {
/*  55 */       return new ValueRef.TypedValueHolderValueRef(state.getActiveContextObject(), this);
/*     */     }
/*  57 */     if (this.name.equals("root")) {
/*  58 */       return new ValueRef.TypedValueHolderValueRef(state.getRootContextObject(), this);
/*     */     }
/*  60 */     TypedValue result = state.lookupVariable(this.name);
/*     */     
/*  62 */     return new VariableRef(this.name, result, state.getEvaluationContext());
/*     */   }
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws SpelEvaluationException {
/*  67 */     if (this.name.equals("this")) {
/*  68 */       return state.getActiveContextObject();
/*     */     }
/*  70 */     if (this.name.equals("root")) {
/*  71 */       TypedValue typedValue = state.getRootContextObject();
/*  72 */       this.exitTypeDescriptor = CodeFlow.toDescriptorFromObject(typedValue.getValue());
/*  73 */       return typedValue;
/*     */     } 
/*  75 */     TypedValue result = state.lookupVariable(this.name);
/*  76 */     Object value = result.getValue();
/*  77 */     if (value == null || !Modifier.isPublic(value.getClass().getModifiers())) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  82 */       this.exitTypeDescriptor = "Ljava/lang/Object";
/*     */     } else {
/*     */       
/*  85 */       this.exitTypeDescriptor = CodeFlow.toDescriptorFromObject(value);
/*     */     } 
/*     */     
/*  88 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(ExpressionState state, Object value) throws SpelEvaluationException {
/*  93 */     state.setVariable(this.name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/*  98 */     return "#" + this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(ExpressionState expressionState) throws SpelEvaluationException {
/* 103 */     return (!this.name.equals("this") && !this.name.equals("root"));
/*     */   }
/*     */ 
/*     */   
/*     */   class VariableRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final String name;
/*     */     
/*     */     private final TypedValue value;
/*     */     
/*     */     private final EvaluationContext evaluationContext;
/*     */ 
/*     */     
/*     */     public VariableRef(String name, TypedValue value, EvaluationContext evaluationContext) {
/* 118 */       this.name = name;
/* 119 */       this.value = value;
/* 120 */       this.evaluationContext = evaluationContext;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 126 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(Object newValue) {
/* 131 */       this.evaluationContext.setVariable(this.name, newValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 136 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 142 */     return (this.exitTypeDescriptor != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 147 */     if (this.name.equals("root")) {
/* 148 */       mv.visitVarInsn(25, 1);
/*     */     } else {
/*     */       
/* 151 */       mv.visitVarInsn(25, 2);
/* 152 */       mv.visitLdcInsn(this.name);
/* 153 */       mv.visitMethodInsn(185, "org/springframework/expression/EvaluationContext", "lookupVariable", "(Ljava/lang/String;)Ljava/lang/Object;", true);
/*     */     } 
/* 155 */     CodeFlow.insertCheckCast(mv, this.exitTypeDescriptor);
/* 156 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\VariableReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */