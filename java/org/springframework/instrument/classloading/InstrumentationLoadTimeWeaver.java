/*     */ package org.springframework.instrument.classloading;
/*     */ 
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import java.lang.instrument.IllegalClassFormatException;
/*     */ import java.lang.instrument.Instrumentation;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.instrument.InstrumentationSavingAgent;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class InstrumentationLoadTimeWeaver
/*     */   implements LoadTimeWeaver
/*     */ {
/*  51 */   private static final boolean AGENT_CLASS_PRESENT = ClassUtils.isPresent("org.springframework.instrument.InstrumentationSavingAgent", InstrumentationLoadTimeWeaver.class
/*     */       
/*  53 */       .getClassLoader());
/*     */ 
/*     */   
/*     */   private final ClassLoader classLoader;
/*     */   
/*     */   private final Instrumentation instrumentation;
/*     */   
/*  60 */   private final List<ClassFileTransformer> transformers = new ArrayList<ClassFileTransformer>(4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InstrumentationLoadTimeWeaver() {
/*  67 */     this(ClassUtils.getDefaultClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InstrumentationLoadTimeWeaver(ClassLoader classLoader) {
/*  75 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/*  76 */     this.classLoader = classLoader;
/*  77 */     this.instrumentation = getInstrumentation();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTransformer(ClassFileTransformer transformer) {
/*  83 */     Assert.notNull(transformer, "Transformer must not be null");
/*  84 */     FilteringClassFileTransformer actualTransformer = new FilteringClassFileTransformer(transformer, this.classLoader);
/*     */     
/*  86 */     synchronized (this.transformers) {
/*  87 */       if (this.instrumentation == null) {
/*  88 */         throw new IllegalStateException("Must start with Java agent to use InstrumentationLoadTimeWeaver. See Spring documentation.");
/*     */       }
/*     */       
/*  91 */       this.instrumentation.addTransformer(actualTransformer);
/*  92 */       this.transformers.add(actualTransformer);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassLoader getInstrumentableClassLoader() {
/* 103 */     return this.classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassLoader getThrowawayClassLoader() {
/* 111 */     return (ClassLoader)new SimpleThrowawayClassLoader(getInstrumentableClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeTransformers() {
/* 118 */     synchronized (this.transformers) {
/* 119 */       if (!this.transformers.isEmpty()) {
/* 120 */         for (int i = this.transformers.size() - 1; i >= 0; i--) {
/* 121 */           this.instrumentation.removeTransformer(this.transformers.get(i));
/*     */         }
/* 123 */         this.transformers.clear();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isInstrumentationAvailable() {
/* 134 */     return (getInstrumentation() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Instrumentation getInstrumentation() {
/* 143 */     if (AGENT_CLASS_PRESENT) {
/* 144 */       return InstrumentationAccessor.getInstrumentation();
/*     */     }
/*     */     
/* 147 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class InstrumentationAccessor
/*     */   {
/*     */     public static Instrumentation getInstrumentation() {
/* 158 */       return InstrumentationSavingAgent.getInstrumentation();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FilteringClassFileTransformer
/*     */     implements ClassFileTransformer
/*     */   {
/*     */     private final ClassFileTransformer targetTransformer;
/*     */     
/*     */     private final ClassLoader targetClassLoader;
/*     */ 
/*     */     
/*     */     public FilteringClassFileTransformer(ClassFileTransformer targetTransformer, ClassLoader targetClassLoader) {
/* 173 */       this.targetTransformer = targetTransformer;
/* 174 */       this.targetClassLoader = targetClassLoader;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
/* 181 */       if (!this.targetClassLoader.equals(loader)) {
/* 182 */         return null;
/*     */       }
/* 184 */       return this.targetTransformer.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 190 */       return "FilteringClassFileTransformer for: " + this.targetTransformer.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\instrument\classloading\InstrumentationLoadTimeWeaver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */