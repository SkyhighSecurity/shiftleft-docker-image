/*     */ package org.springframework.instrument.classloading.tomcat;
/*     */ 
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.core.OverridingClassLoader;
/*     */ import org.springframework.instrument.classloading.LoadTimeWeaver;
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
/*     */ public class TomcatLoadTimeWeaver
/*     */   implements LoadTimeWeaver
/*     */ {
/*     */   private static final String INSTRUMENTABLE_LOADER_CLASS_NAME = "org.apache.tomcat.InstrumentableClassLoader";
/*     */   private final ClassLoader classLoader;
/*     */   private final Method addTransformerMethod;
/*     */   private final Method copyMethod;
/*     */   
/*     */   public TomcatLoadTimeWeaver() {
/*  49 */     this(ClassUtils.getDefaultClassLoader());
/*     */   }
/*     */   public TomcatLoadTimeWeaver(ClassLoader classLoader) {
/*     */     Class<?> instrumentableLoaderClass;
/*  53 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/*  54 */     this.classLoader = classLoader;
/*     */ 
/*     */     
/*     */     try {
/*  58 */       instrumentableLoaderClass = classLoader.loadClass("org.apache.tomcat.InstrumentableClassLoader");
/*  59 */       if (!instrumentableLoaderClass.isInstance(classLoader))
/*     */       {
/*  61 */         instrumentableLoaderClass = classLoader.getClass();
/*     */       }
/*     */     }
/*  64 */     catch (ClassNotFoundException ex) {
/*     */       
/*  66 */       instrumentableLoaderClass = classLoader.getClass();
/*     */     } 
/*     */     
/*     */     try {
/*  70 */       this.addTransformerMethod = instrumentableLoaderClass.getMethod("addTransformer", new Class[] { ClassFileTransformer.class });
/*     */       
/*  72 */       Method copyMethod = ClassUtils.getMethodIfAvailable(instrumentableLoaderClass, "copyWithoutTransformers", new Class[0]);
/*  73 */       if (copyMethod == null)
/*     */       {
/*  75 */         copyMethod = instrumentableLoaderClass.getMethod("getThrowawayClassLoader", new Class[0]);
/*     */       }
/*  77 */       this.copyMethod = copyMethod;
/*     */     }
/*  79 */     catch (Throwable ex) {
/*  80 */       throw new IllegalStateException("Could not initialize TomcatLoadTimeWeaver because Tomcat API classes are not available", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTransformer(ClassFileTransformer transformer) {
/*     */     try {
/*  89 */       this.addTransformerMethod.invoke(this.classLoader, new Object[] { transformer });
/*     */     }
/*  91 */     catch (InvocationTargetException ex) {
/*  92 */       throw new IllegalStateException("Tomcat addTransformer method threw exception", ex.getCause());
/*     */     }
/*  94 */     catch (Throwable ex) {
/*  95 */       throw new IllegalStateException("Could not invoke Tomcat addTransformer method", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getInstrumentableClassLoader() {
/* 101 */     return this.classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getThrowawayClassLoader() {
/*     */     try {
/* 107 */       return (ClassLoader)new OverridingClassLoader(this.classLoader, (ClassLoader)this.copyMethod.invoke(this.classLoader, new Object[0]));
/*     */     }
/* 109 */     catch (InvocationTargetException ex) {
/* 110 */       throw new IllegalStateException("Tomcat copy method threw exception", ex.getCause());
/*     */     }
/* 112 */     catch (Throwable ex) {
/* 113 */       throw new IllegalStateException("Could not invoke Tomcat copy method", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\instrument\classloading\tomcat\TomcatLoadTimeWeaver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */