/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.asm.ClassWriter;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelNode;
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
/*     */ public class InlineList
/*     */   extends SpelNodeImpl
/*     */ {
/*  40 */   private TypedValue constant = null;
/*     */ 
/*     */   
/*     */   public InlineList(int pos, SpelNodeImpl... args) {
/*  44 */     super(pos, args);
/*  45 */     checkIfConstant();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkIfConstant() {
/*  55 */     boolean isConstant = true;
/*  56 */     for (int c = 0, max = getChildCount(); c < max; c++) {
/*  57 */       SpelNode child = getChild(c);
/*  58 */       if (!(child instanceof Literal)) {
/*  59 */         if (child instanceof InlineList) {
/*  60 */           InlineList inlineList = (InlineList)child;
/*  61 */           if (!inlineList.isConstant()) {
/*  62 */             isConstant = false;
/*     */           }
/*     */         } else {
/*     */           
/*  66 */           isConstant = false;
/*     */         } 
/*     */       }
/*     */     } 
/*  70 */     if (isConstant) {
/*  71 */       List<Object> constantList = new ArrayList();
/*  72 */       int childcount = getChildCount();
/*  73 */       for (int i = 0; i < childcount; i++) {
/*  74 */         SpelNode child = getChild(i);
/*  75 */         if (child instanceof Literal) {
/*  76 */           constantList.add(((Literal)child).getLiteralValue().getValue());
/*     */         }
/*  78 */         else if (child instanceof InlineList) {
/*  79 */           constantList.add(((InlineList)child).getConstantValue());
/*     */         } 
/*     */       } 
/*  82 */       this.constant = new TypedValue(Collections.unmodifiableList(constantList));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState expressionState) throws EvaluationException {
/*  88 */     if (this.constant != null) {
/*  89 */       return this.constant;
/*     */     }
/*     */     
/*  92 */     List<Object> returnValue = new ArrayList();
/*  93 */     int childCount = getChildCount();
/*  94 */     for (int c = 0; c < childCount; c++) {
/*  95 */       returnValue.add(getChild(c).getValue(expressionState));
/*     */     }
/*  97 */     return new TypedValue(returnValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 103 */     StringBuilder sb = new StringBuilder("{");
/*     */     
/* 105 */     int count = getChildCount();
/* 106 */     for (int c = 0; c < count; c++) {
/* 107 */       if (c > 0) {
/* 108 */         sb.append(",");
/*     */       }
/* 110 */       sb.append(getChild(c).toStringAST());
/*     */     } 
/* 112 */     sb.append("}");
/* 113 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstant() {
/* 120 */     return (this.constant != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Object> getConstantValue() {
/* 125 */     return (List<Object>)this.constant.getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 130 */     return isConstant();
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow codeflow) {
/* 135 */     final String constantFieldName = "inlineList$" + codeflow.nextFieldId();
/* 136 */     final String className = codeflow.getClassName();
/*     */     
/* 138 */     codeflow.registerNewField(new CodeFlow.FieldAdder() {
/*     */           public void generateField(ClassWriter cw, CodeFlow codeflow) {
/* 140 */             cw.visitField(26, constantFieldName, "Ljava/util/List;", null, null);
/*     */           }
/*     */         });
/*     */     
/* 144 */     codeflow.registerNewClinit(new CodeFlow.ClinitAdder() {
/*     */           public void generateCode(MethodVisitor mv, CodeFlow codeflow) {
/* 146 */             InlineList.this.generateClinitCode(className, constantFieldName, mv, codeflow, false);
/*     */           }
/*     */         });
/*     */     
/* 150 */     mv.visitFieldInsn(178, className, constantFieldName, "Ljava/util/List;");
/* 151 */     codeflow.pushDescriptor("Ljava/util/List");
/*     */   }
/*     */   
/*     */   void generateClinitCode(String clazzname, String constantFieldName, MethodVisitor mv, CodeFlow codeflow, boolean nested) {
/* 155 */     mv.visitTypeInsn(187, "java/util/ArrayList");
/* 156 */     mv.visitInsn(89);
/* 157 */     mv.visitMethodInsn(183, "java/util/ArrayList", "<init>", "()V", false);
/* 158 */     if (!nested) {
/* 159 */       mv.visitFieldInsn(179, clazzname, constantFieldName, "Ljava/util/List;");
/*     */     }
/* 161 */     int childCount = getChildCount();
/* 162 */     for (int c = 0; c < childCount; c++) {
/* 163 */       if (!nested) {
/* 164 */         mv.visitFieldInsn(178, clazzname, constantFieldName, "Ljava/util/List;");
/*     */       } else {
/*     */         
/* 167 */         mv.visitInsn(89);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 172 */       if (this.children[c] instanceof InlineList) {
/* 173 */         ((InlineList)this.children[c]).generateClinitCode(clazzname, constantFieldName, mv, codeflow, true);
/*     */       } else {
/*     */         
/* 176 */         this.children[c].generateCode(mv, codeflow);
/* 177 */         if (CodeFlow.isPrimitive(codeflow.lastDescriptor())) {
/* 178 */           CodeFlow.insertBoxIfNecessary(mv, codeflow.lastDescriptor().charAt(0));
/*     */         }
/*     */       } 
/* 181 */       mv.visitMethodInsn(185, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
/* 182 */       mv.visitInsn(87);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\InlineList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */