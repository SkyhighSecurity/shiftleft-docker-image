/*     */ package org.springframework.cglib.proxy;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.asm.ClassReader;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.cglib.core.Signature;
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
/*     */ class BridgeMethodResolver
/*     */ {
/*     */   private final Map declToBridge;
/*     */   private final ClassLoader classLoader;
/*     */   
/*     */   public BridgeMethodResolver(Map declToBridge, ClassLoader classLoader) {
/*  49 */     this.declToBridge = declToBridge;
/*  50 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map resolveAll() {
/*  58 */     Map<Object, Object> resolved = new HashMap<Object, Object>();
/*  59 */     for (Iterator<Map.Entry> entryIter = this.declToBridge.entrySet().iterator(); entryIter.hasNext(); ) {
/*  60 */       Map.Entry entry = entryIter.next();
/*  61 */       Class owner = (Class)entry.getKey();
/*  62 */       Set bridges = (Set)entry.getValue();
/*     */       try {
/*  64 */         InputStream is = this.classLoader.getResourceAsStream(owner.getName().replace('.', '/') + ".class");
/*  65 */         if (is == null) {
/*  66 */           return resolved;
/*     */         }
/*     */         try {
/*  69 */           (new ClassReader(is))
/*  70 */             .accept(new BridgedFinder(bridges, resolved), 6);
/*     */         } finally {
/*     */           
/*  73 */           is.close();
/*     */         } 
/*  75 */       } catch (IOException iOException) {}
/*     */     } 
/*  77 */     return resolved;
/*     */   }
/*     */   
/*     */   private static class BridgedFinder
/*     */     extends ClassVisitor {
/*     */     private Map resolved;
/*     */     private Set eligibleMethods;
/*  84 */     private Signature currentMethod = null;
/*     */     
/*     */     BridgedFinder(Set eligibleMethods, Map resolved) {
/*  87 */       super(393216);
/*  88 */       this.resolved = resolved;
/*  89 */       this.eligibleMethods = eligibleMethods;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {}
/*     */ 
/*     */     
/*     */     public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/*  98 */       Signature sig = new Signature(name, desc);
/*  99 */       if (this.eligibleMethods.remove(sig)) {
/* 100 */         this.currentMethod = sig;
/* 101 */         return new MethodVisitor(393216)
/*     */           {
/*     */             public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
/* 104 */               if (opcode == 183 && BridgeMethodResolver.BridgedFinder.this.currentMethod != null) {
/* 105 */                 Signature target = new Signature(name, desc);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 112 */                 if (!target.equals(BridgeMethodResolver.BridgedFinder.this.currentMethod)) {
/* 113 */                   BridgeMethodResolver.BridgedFinder.this.resolved.put(BridgeMethodResolver.BridgedFinder.this.currentMethod, target);
/*     */                 }
/* 115 */                 BridgeMethodResolver.BridgedFinder.this.currentMethod = null;
/*     */               } 
/*     */             }
/*     */           };
/*     */       } 
/* 120 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cglib\proxy\BridgeMethodResolver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */