/*     */ package org.springframework.expression.spel.standard;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.asm.ClassWriter;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Opcodes;
/*     */ import org.springframework.expression.Expression;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.CompiledExpression;
/*     */ import org.springframework.expression.spel.ast.SpelNodeImpl;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
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
/*     */ public class SpelCompiler
/*     */   implements Opcodes
/*     */ {
/*  70 */   private static final Log logger = LogFactory.getLog(SpelCompiler.class);
/*     */ 
/*     */ 
/*     */   
/*  74 */   private static final Map<ClassLoader, SpelCompiler> compilers = (Map<ClassLoader, SpelCompiler>)new ConcurrentReferenceHashMap();
/*     */ 
/*     */ 
/*     */   
/*     */   private final ChildClassLoader ccl;
/*     */ 
/*     */ 
/*     */   
/*  82 */   private final AtomicInteger suffixId = new AtomicInteger(1);
/*     */ 
/*     */   
/*     */   private SpelCompiler(ClassLoader classloader) {
/*  86 */     this.ccl = new ChildClassLoader(classloader);
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
/*     */   public CompiledExpression compile(SpelNodeImpl expression) {
/* 100 */     if (expression.isCompilable()) {
/* 101 */       if (logger.isDebugEnabled()) {
/* 102 */         logger.debug("SpEL: compiling " + expression.toStringAST());
/*     */       }
/* 104 */       Class<? extends CompiledExpression> clazz = createExpressionClass(expression);
/* 105 */       if (clazz != null) {
/*     */         try {
/* 107 */           return clazz.newInstance();
/*     */         }
/* 109 */         catch (Throwable ex) {
/* 110 */           throw new IllegalStateException("Failed to instantiate CompiledExpression", ex);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 115 */     if (logger.isDebugEnabled()) {
/* 116 */       logger.debug("SpEL: unable to compile " + expression.toStringAST());
/*     */     }
/* 118 */     return null;
/*     */   }
/*     */   
/*     */   private int getNextSuffix() {
/* 122 */     return this.suffixId.incrementAndGet();
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
/*     */   private Class<? extends CompiledExpression> createExpressionClass(SpelNodeImpl expressionToCompile) {
/* 135 */     String clazzName = "spel/Ex" + getNextSuffix();
/* 136 */     ClassWriter cw = new ExpressionClassWriter();
/* 137 */     cw.visit(49, 1, clazzName, null, "org/springframework/expression/spel/CompiledExpression", null);
/*     */ 
/*     */     
/* 140 */     MethodVisitor mv = cw.visitMethod(1, "<init>", "()V", null, null);
/* 141 */     mv.visitCode();
/* 142 */     mv.visitVarInsn(25, 0);
/* 143 */     mv.visitMethodInsn(183, "org/springframework/expression/spel/CompiledExpression", "<init>", "()V", false);
/*     */     
/* 145 */     mv.visitInsn(177);
/* 146 */     mv.visitMaxs(1, 1);
/* 147 */     mv.visitEnd();
/*     */ 
/*     */     
/* 150 */     mv = cw.visitMethod(1, "getValue", "(Ljava/lang/Object;Lorg/springframework/expression/EvaluationContext;)Ljava/lang/Object;", null, new String[] { "org/springframework/expression/EvaluationException" });
/*     */ 
/*     */     
/* 153 */     mv.visitCode();
/*     */     
/* 155 */     CodeFlow cf = new CodeFlow(clazzName, cw);
/*     */ 
/*     */     
/*     */     try {
/* 159 */       expressionToCompile.generateCode(mv, cf);
/*     */     }
/* 161 */     catch (IllegalStateException ex) {
/* 162 */       if (logger.isDebugEnabled()) {
/* 163 */         logger.debug(expressionToCompile.getClass().getSimpleName() + ".generateCode opted out of compilation: " + ex
/* 164 */             .getMessage());
/*     */       }
/* 166 */       return null;
/*     */     } 
/*     */     
/* 169 */     CodeFlow.insertBoxIfNecessary(mv, cf.lastDescriptor());
/* 170 */     if ("V".equals(cf.lastDescriptor())) {
/* 171 */       mv.visitInsn(1);
/*     */     }
/* 173 */     mv.visitInsn(176);
/*     */     
/* 175 */     mv.visitMaxs(0, 0);
/* 176 */     mv.visitEnd();
/* 177 */     cw.visitEnd();
/*     */     
/* 179 */     cf.finish();
/*     */     
/* 181 */     byte[] data = cw.toByteArray();
/*     */ 
/*     */     
/* 184 */     return (Class)this.ccl.defineClass(clazzName.replaceAll("/", "."), data);
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
/*     */   public static SpelCompiler getCompiler(ClassLoader classLoader) {
/* 196 */     ClassLoader clToUse = (classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader();
/* 197 */     synchronized (compilers) {
/* 198 */       SpelCompiler compiler = compilers.get(clToUse);
/* 199 */       if (compiler == null) {
/* 200 */         compiler = new SpelCompiler(clToUse);
/* 201 */         compilers.put(clToUse, compiler);
/*     */       } 
/* 203 */       return compiler;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean compile(Expression expression) {
/* 214 */     return (expression instanceof SpelExpression && ((SpelExpression)expression).compileExpression());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void revertToInterpreted(Expression expression) {
/* 223 */     if (expression instanceof SpelExpression) {
/* 224 */       ((SpelExpression)expression).revertToInterpreted();
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
/*     */   private static void dump(String expressionText, String name, byte[] bytecode) {
/* 237 */     String nameToUse = name.replace('.', '/');
/* 238 */     String dir = (nameToUse.indexOf('/') != -1) ? nameToUse.substring(0, nameToUse.lastIndexOf('/')) : "";
/* 239 */     String dumpLocation = null;
/*     */     try {
/* 241 */       File tempFile = File.createTempFile("tmp", null);
/* 242 */       dumpLocation = tempFile + File.separator + nameToUse + ".class";
/* 243 */       tempFile.delete();
/* 244 */       File f = new File(tempFile, dir);
/* 245 */       f.mkdirs();
/*     */       
/* 247 */       if (logger.isDebugEnabled()) {
/* 248 */         logger.debug("Expression '" + expressionText + "' compiled code dumped to " + dumpLocation);
/*     */       }
/* 250 */       f = new File(dumpLocation);
/* 251 */       FileOutputStream fos = new FileOutputStream(f);
/* 252 */       fos.write(bytecode);
/* 253 */       fos.flush();
/* 254 */       fos.close();
/*     */     }
/* 256 */     catch (IOException ex) {
/* 257 */       throw new IllegalStateException("Unexpected problem dumping class '" + nameToUse + "' into " + dumpLocation, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ChildClassLoader
/*     */     extends URLClassLoader
/*     */   {
/* 268 */     private static final URL[] NO_URLS = new URL[0];
/*     */     
/*     */     public ChildClassLoader(ClassLoader classLoader) {
/* 271 */       super(NO_URLS, classLoader);
/*     */     }
/*     */     
/*     */     public Class<?> defineClass(String name, byte[] bytes) {
/* 275 */       return defineClass(name, bytes, 0, bytes.length);
/*     */     }
/*     */   }
/*     */   
/*     */   private class ExpressionClassWriter
/*     */     extends ClassWriter
/*     */   {
/*     */     public ExpressionClassWriter() {
/* 283 */       super(3);
/*     */     }
/*     */ 
/*     */     
/*     */     protected ClassLoader getClassLoader() {
/* 288 */       return SpelCompiler.this.ccl;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\standard\SpelCompiler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */