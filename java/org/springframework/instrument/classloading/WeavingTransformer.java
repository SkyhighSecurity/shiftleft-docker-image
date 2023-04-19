/*     */ package org.springframework.instrument.classloading;
/*     */ 
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import java.lang.instrument.IllegalClassFormatException;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class WeavingTransformer
/*     */ {
/*     */   private final ClassLoader classLoader;
/*  41 */   private final List<ClassFileTransformer> transformers = new ArrayList<ClassFileTransformer>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WeavingTransformer(ClassLoader classLoader) {
/*  49 */     if (classLoader == null) {
/*  50 */       throw new IllegalArgumentException("ClassLoader must not be null");
/*     */     }
/*  52 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTransformer(ClassFileTransformer transformer) {
/*  61 */     if (transformer == null) {
/*  62 */       throw new IllegalArgumentException("Transformer must not be null");
/*     */     }
/*  64 */     this.transformers.add(transformer);
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
/*     */   public byte[] transformIfNecessary(String className, byte[] bytes) {
/*  77 */     String internalName = className.replace(".", "/");
/*  78 */     return transformIfNecessary(className, internalName, bytes, null);
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
/*     */   public byte[] transformIfNecessary(String className, String internalName, byte[] bytes, ProtectionDomain pd) {
/*  92 */     byte[] result = bytes;
/*  93 */     for (ClassFileTransformer cft : this.transformers) {
/*     */       try {
/*  95 */         byte[] transformed = cft.transform(this.classLoader, internalName, null, pd, result);
/*  96 */         if (transformed != null) {
/*  97 */           result = transformed;
/*     */         }
/*     */       }
/* 100 */       catch (IllegalClassFormatException ex) {
/* 101 */         throw new IllegalStateException("Class file transformation failed", ex);
/*     */       } 
/*     */     } 
/* 104 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\instrument\classloading\WeavingTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */