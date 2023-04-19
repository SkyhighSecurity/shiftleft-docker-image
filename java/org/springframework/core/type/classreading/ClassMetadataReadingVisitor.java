/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.asm.AnnotationVisitor;
/*     */ import org.springframework.asm.Attribute;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.FieldVisitor;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.type.ClassMetadata;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ class ClassMetadataReadingVisitor
/*     */   extends ClassVisitor
/*     */   implements ClassMetadata
/*     */ {
/*     */   private String className;
/*     */   private boolean isInterface;
/*     */   private boolean isAnnotation;
/*     */   private boolean isAbstract;
/*     */   private boolean isFinal;
/*     */   private String enclosingClassName;
/*     */   private boolean independentInnerClass;
/*     */   private String superClassName;
/*     */   private String[] interfaces;
/*  65 */   private Set<String> memberClassNames = new LinkedHashSet<String>(4);
/*     */ 
/*     */   
/*     */   public ClassMetadataReadingVisitor() {
/*  69 */     super(393216);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(int version, int access, String name, String signature, String supername, String[] interfaces) {
/*  75 */     this.className = ClassUtils.convertResourcePathToClassName(name);
/*  76 */     this.isInterface = ((access & 0x200) != 0);
/*  77 */     this.isAnnotation = ((access & 0x2000) != 0);
/*  78 */     this.isAbstract = ((access & 0x400) != 0);
/*  79 */     this.isFinal = ((access & 0x10) != 0);
/*  80 */     if (supername != null && !this.isInterface) {
/*  81 */       this.superClassName = ClassUtils.convertResourcePathToClassName(supername);
/*     */     }
/*  83 */     this.interfaces = new String[interfaces.length];
/*  84 */     for (int i = 0; i < interfaces.length; i++) {
/*  85 */       this.interfaces[i] = ClassUtils.convertResourcePathToClassName(interfaces[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitOuterClass(String owner, String name, String desc) {
/*  91 */     this.enclosingClassName = ClassUtils.convertResourcePathToClassName(owner);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitInnerClass(String name, String outerName, String innerName, int access) {
/*  96 */     if (outerName != null) {
/*  97 */       String fqName = ClassUtils.convertResourcePathToClassName(name);
/*  98 */       String fqOuterName = ClassUtils.convertResourcePathToClassName(outerName);
/*  99 */       if (this.className.equals(fqName)) {
/* 100 */         this.enclosingClassName = fqOuterName;
/* 101 */         this.independentInnerClass = ((access & 0x8) != 0);
/*     */       }
/* 103 */       else if (this.className.equals(fqOuterName)) {
/* 104 */         this.memberClassNames.add(fqName);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitSource(String source, String debug) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/* 117 */     return new EmptyAnnotationVisitor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitAttribute(Attribute attr) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
/* 128 */     return new EmptyFieldVisitor();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/* 134 */     return new EmptyMethodVisitor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitEnd() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/* 145 */     return this.className;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInterface() {
/* 150 */     return this.isInterface;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnotation() {
/* 155 */     return this.isAnnotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/* 160 */     return this.isAbstract;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isConcrete() {
/* 165 */     return (!this.isInterface && !this.isAbstract);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/* 170 */     return this.isFinal;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIndependent() {
/* 175 */     return (this.enclosingClassName == null || this.independentInnerClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEnclosingClass() {
/* 180 */     return (this.enclosingClassName != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getEnclosingClassName() {
/* 185 */     return this.enclosingClassName;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSuperClass() {
/* 190 */     return (this.superClassName != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSuperClassName() {
/* 195 */     return this.superClassName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getInterfaceNames() {
/* 200 */     return this.interfaces;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getMemberClassNames() {
/* 205 */     return StringUtils.toStringArray(this.memberClassNames);
/*     */   }
/*     */   
/*     */   private static class EmptyAnnotationVisitor
/*     */     extends AnnotationVisitor
/*     */   {
/*     */     public EmptyAnnotationVisitor() {
/* 212 */       super(393216);
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotationVisitor visitAnnotation(String name, String desc) {
/* 217 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotationVisitor visitArray(String name) {
/* 222 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class EmptyMethodVisitor
/*     */     extends MethodVisitor
/*     */   {
/*     */     public EmptyMethodVisitor() {
/* 230 */       super(393216);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class EmptyFieldVisitor
/*     */     extends FieldVisitor
/*     */   {
/*     */     public EmptyFieldVisitor() {
/* 238 */       super(393216);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\type\classreading\ClassMetadataReadingVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */