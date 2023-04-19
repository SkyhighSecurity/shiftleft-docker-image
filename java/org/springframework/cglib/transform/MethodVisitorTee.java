/*     */ package org.springframework.cglib.transform;
/*     */ 
/*     */ import org.springframework.asm.AnnotationVisitor;
/*     */ import org.springframework.asm.Attribute;
/*     */ import org.springframework.asm.Handle;
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.TypePath;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodVisitorTee
/*     */   extends MethodVisitor
/*     */ {
/*     */   private final MethodVisitor mv1;
/*     */   private final MethodVisitor mv2;
/*     */   
/*     */   public MethodVisitorTee(MethodVisitor mv1, MethodVisitor mv2) {
/*  25 */     super(393216);
/*  26 */     this.mv1 = mv1;
/*  27 */     this.mv2 = mv2;
/*     */   }
/*     */   
/*     */   public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
/*  31 */     this.mv1.visitFrame(type, nLocal, local, nStack, stack);
/*  32 */     this.mv2.visitFrame(type, nLocal, local, nStack, stack);
/*     */   }
/*     */   
/*     */   public AnnotationVisitor visitAnnotationDefault() {
/*  36 */     return AnnotationVisitorTee.getInstance(this.mv1.visitAnnotationDefault(), this.mv2
/*  37 */         .visitAnnotationDefault());
/*     */   }
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/*  41 */     return AnnotationVisitorTee.getInstance(this.mv1.visitAnnotation(desc, visible), this.mv2
/*  42 */         .visitAnnotation(desc, visible));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
/*  48 */     return AnnotationVisitorTee.getInstance(this.mv1.visitParameterAnnotation(parameter, desc, visible), this.mv2
/*  49 */         .visitParameterAnnotation(parameter, desc, visible));
/*     */   }
/*     */   
/*     */   public void visitAttribute(Attribute attr) {
/*  53 */     this.mv1.visitAttribute(attr);
/*  54 */     this.mv2.visitAttribute(attr);
/*     */   }
/*     */   
/*     */   public void visitCode() {
/*  58 */     this.mv1.visitCode();
/*  59 */     this.mv2.visitCode();
/*     */   }
/*     */   
/*     */   public void visitInsn(int opcode) {
/*  63 */     this.mv1.visitInsn(opcode);
/*  64 */     this.mv2.visitInsn(opcode);
/*     */   }
/*     */   
/*     */   public void visitIntInsn(int opcode, int operand) {
/*  68 */     this.mv1.visitIntInsn(opcode, operand);
/*  69 */     this.mv2.visitIntInsn(opcode, operand);
/*     */   }
/*     */   
/*     */   public void visitVarInsn(int opcode, int var) {
/*  73 */     this.mv1.visitVarInsn(opcode, var);
/*  74 */     this.mv2.visitVarInsn(opcode, var);
/*     */   }
/*     */   
/*     */   public void visitTypeInsn(int opcode, String desc) {
/*  78 */     this.mv1.visitTypeInsn(opcode, desc);
/*  79 */     this.mv2.visitTypeInsn(opcode, desc);
/*     */   }
/*     */   
/*     */   public void visitFieldInsn(int opcode, String owner, String name, String desc) {
/*  83 */     this.mv1.visitFieldInsn(opcode, owner, name, desc);
/*  84 */     this.mv2.visitFieldInsn(opcode, owner, name, desc);
/*     */   }
/*     */   
/*     */   public void visitMethodInsn(int opcode, String owner, String name, String desc) {
/*  88 */     this.mv1.visitMethodInsn(opcode, owner, name, desc);
/*  89 */     this.mv2.visitMethodInsn(opcode, owner, name, desc);
/*     */   }
/*     */   
/*     */   public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
/*  93 */     this.mv1.visitMethodInsn(opcode, owner, name, desc, itf);
/*  94 */     this.mv2.visitMethodInsn(opcode, owner, name, desc, itf);
/*     */   }
/*     */   
/*     */   public void visitJumpInsn(int opcode, Label label) {
/*  98 */     this.mv1.visitJumpInsn(opcode, label);
/*  99 */     this.mv2.visitJumpInsn(opcode, label);
/*     */   }
/*     */   
/*     */   public void visitLabel(Label label) {
/* 103 */     this.mv1.visitLabel(label);
/* 104 */     this.mv2.visitLabel(label);
/*     */   }
/*     */   
/*     */   public void visitLdcInsn(Object cst) {
/* 108 */     this.mv1.visitLdcInsn(cst);
/* 109 */     this.mv2.visitLdcInsn(cst);
/*     */   }
/*     */   
/*     */   public void visitIincInsn(int var, int increment) {
/* 113 */     this.mv1.visitIincInsn(var, increment);
/* 114 */     this.mv2.visitIincInsn(var, increment);
/*     */   }
/*     */   
/*     */   public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
/* 118 */     this.mv1.visitTableSwitchInsn(min, max, dflt, labels);
/* 119 */     this.mv2.visitTableSwitchInsn(min, max, dflt, labels);
/*     */   }
/*     */   
/*     */   public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
/* 123 */     this.mv1.visitLookupSwitchInsn(dflt, keys, labels);
/* 124 */     this.mv2.visitLookupSwitchInsn(dflt, keys, labels);
/*     */   }
/*     */   
/*     */   public void visitMultiANewArrayInsn(String desc, int dims) {
/* 128 */     this.mv1.visitMultiANewArrayInsn(desc, dims);
/* 129 */     this.mv2.visitMultiANewArrayInsn(desc, dims);
/*     */   }
/*     */   
/*     */   public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
/* 133 */     this.mv1.visitTryCatchBlock(start, end, handler, type);
/* 134 */     this.mv2.visitTryCatchBlock(start, end, handler, type);
/*     */   }
/*     */   
/*     */   public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
/* 138 */     this.mv1.visitLocalVariable(name, desc, signature, start, end, index);
/* 139 */     this.mv2.visitLocalVariable(name, desc, signature, start, end, index);
/*     */   }
/*     */   
/*     */   public void visitLineNumber(int line, Label start) {
/* 143 */     this.mv1.visitLineNumber(line, start);
/* 144 */     this.mv2.visitLineNumber(line, start);
/*     */   }
/*     */   
/*     */   public void visitMaxs(int maxStack, int maxLocals) {
/* 148 */     this.mv1.visitMaxs(maxStack, maxLocals);
/* 149 */     this.mv2.visitMaxs(maxStack, maxLocals);
/*     */   }
/*     */   
/*     */   public void visitEnd() {
/* 153 */     this.mv1.visitEnd();
/* 154 */     this.mv2.visitEnd();
/*     */   }
/*     */   
/*     */   public void visitParameter(String name, int access) {
/* 158 */     this.mv1.visitParameter(name, access);
/* 159 */     this.mv2.visitParameter(name, access);
/*     */   }
/*     */   
/*     */   public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
/* 163 */     return AnnotationVisitorTee.getInstance(this.mv1.visitTypeAnnotation(typeRef, typePath, desc, visible), this.mv2
/* 164 */         .visitTypeAnnotation(typeRef, typePath, desc, visible));
/*     */   }
/*     */   
/*     */   public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
/* 168 */     this.mv1.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
/* 169 */     this.mv2.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
/*     */   }
/*     */   
/*     */   public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
/* 173 */     return AnnotationVisitorTee.getInstance(this.mv1.visitInsnAnnotation(typeRef, typePath, desc, visible), this.mv2
/* 174 */         .visitInsnAnnotation(typeRef, typePath, desc, visible));
/*     */   }
/*     */   
/*     */   public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
/* 178 */     return AnnotationVisitorTee.getInstance(this.mv1.visitTryCatchAnnotation(typeRef, typePath, desc, visible), this.mv2
/* 179 */         .visitTryCatchAnnotation(typeRef, typePath, desc, visible));
/*     */   }
/*     */   
/*     */   public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String desc, boolean visible) {
/* 183 */     return AnnotationVisitorTee.getInstance(this.mv1.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible), this.mv2
/* 184 */         .visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cglib\transform\MethodVisitorTee.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */