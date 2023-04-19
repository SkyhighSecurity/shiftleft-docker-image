/*     */ package org.springframework.cglib.transform;
/*     */ 
/*     */ import org.springframework.asm.AnnotationVisitor;
/*     */ import org.springframework.asm.Attribute;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.FieldVisitor;
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
/*     */ public class ClassVisitorTee
/*     */   extends ClassVisitor
/*     */ {
/*     */   private ClassVisitor cv1;
/*     */   private ClassVisitor cv2;
/*     */   
/*     */   public ClassVisitorTee(ClassVisitor cv1, ClassVisitor cv2) {
/*  24 */     super(393216);
/*  25 */     this.cv1 = cv1;
/*  26 */     this.cv2 = cv2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
/*  35 */     this.cv1.visit(version, access, name, signature, superName, interfaces);
/*  36 */     this.cv2.visit(version, access, name, signature, superName, interfaces);
/*     */   }
/*     */   
/*     */   public void visitEnd() {
/*  40 */     this.cv1.visitEnd();
/*  41 */     this.cv2.visitEnd();
/*  42 */     this.cv1 = this.cv2 = null;
/*     */   }
/*     */   
/*     */   public void visitInnerClass(String name, String outerName, String innerName, int access) {
/*  46 */     this.cv1.visitInnerClass(name, outerName, innerName, access);
/*  47 */     this.cv2.visitInnerClass(name, outerName, innerName, access);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
/*  55 */     FieldVisitor fv1 = this.cv1.visitField(access, name, desc, signature, value);
/*  56 */     FieldVisitor fv2 = this.cv2.visitField(access, name, desc, signature, value);
/*  57 */     if (fv1 == null)
/*  58 */       return fv2; 
/*  59 */     if (fv2 == null)
/*  60 */       return fv1; 
/*  61 */     return new FieldVisitorTee(fv1, fv2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/*  70 */     MethodVisitor mv1 = this.cv1.visitMethod(access, name, desc, signature, exceptions);
/*  71 */     MethodVisitor mv2 = this.cv2.visitMethod(access, name, desc, signature, exceptions);
/*  72 */     if (mv1 == null)
/*  73 */       return mv2; 
/*  74 */     if (mv2 == null)
/*  75 */       return mv1; 
/*  76 */     return new MethodVisitorTee(mv1, mv2);
/*     */   }
/*     */   
/*     */   public void visitSource(String source, String debug) {
/*  80 */     this.cv1.visitSource(source, debug);
/*  81 */     this.cv2.visitSource(source, debug);
/*     */   }
/*     */   
/*     */   public void visitOuterClass(String owner, String name, String desc) {
/*  85 */     this.cv1.visitOuterClass(owner, name, desc);
/*  86 */     this.cv2.visitOuterClass(owner, name, desc);
/*     */   }
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/*  90 */     return AnnotationVisitorTee.getInstance(this.cv1.visitAnnotation(desc, visible), this.cv2
/*  91 */         .visitAnnotation(desc, visible));
/*     */   }
/*     */   
/*     */   public void visitAttribute(Attribute attrs) {
/*  95 */     this.cv1.visitAttribute(attrs);
/*  96 */     this.cv2.visitAttribute(attrs);
/*     */   }
/*     */   
/*     */   public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
/* 100 */     return AnnotationVisitorTee.getInstance(this.cv1.visitTypeAnnotation(typeRef, typePath, desc, visible), this.cv2
/* 101 */         .visitTypeAnnotation(typeRef, typePath, desc, visible));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cglib\transform\ClassVisitorTee.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */