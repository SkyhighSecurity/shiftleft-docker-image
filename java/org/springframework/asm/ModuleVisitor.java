/*     */ package org.springframework.asm;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ModuleVisitor
/*     */ {
/*     */   protected final int api;
/*     */   protected ModuleVisitor mv;
/*     */   
/*     */   public ModuleVisitor(int api) {
/*  56 */     this(api, null);
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
/*     */   public ModuleVisitor(int api, ModuleVisitor mv) {
/*  69 */     if (api != 393216) {
/*  70 */       throw new IllegalArgumentException();
/*     */     }
/*  72 */     this.api = api;
/*  73 */     this.mv = mv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitMainClass(String mainClass) {
/*  82 */     if (this.mv != null) {
/*  83 */       this.mv.visitMainClass(mainClass);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitPackage(String packaze) {
/*  93 */     if (this.mv != null) {
/*  94 */       this.mv.visitPackage(packaze);
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
/*     */ 
/*     */   
/*     */   public void visitRequire(String module, int access, String version) {
/* 108 */     if (this.mv != null) {
/* 109 */       this.mv.visitRequire(module, access, version);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitExport(String packaze, int access, String... modules) {
/* 125 */     if (this.mv != null) {
/* 126 */       this.mv.visitExport(packaze, access, modules);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitOpen(String packaze, int access, String... modules) {
/* 142 */     if (this.mv != null) {
/* 143 */       this.mv.visitOpen(packaze, access, modules);
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
/*     */   public void visitUse(String service) {
/* 155 */     if (this.mv != null) {
/* 156 */       this.mv.visitUse(service);
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
/*     */   public void visitProvide(String service, String... providers) {
/* 168 */     if (this.mv != null) {
/* 169 */       this.mv.visitProvide(service, providers);
/*     */     }
/*     */   }
/*     */   
/*     */   public void visitEnd() {
/* 174 */     if (this.mv != null)
/* 175 */       this.mv.visitEnd(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\asm\ModuleVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */