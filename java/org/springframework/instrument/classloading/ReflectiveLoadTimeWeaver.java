/*     */ package org.springframework.instrument.classloading;
/*     */ 
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import java.lang.reflect.Method;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.OverridingClassLoader;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReflectiveLoadTimeWeaver
/*     */   implements LoadTimeWeaver
/*     */ {
/*     */   private static final String ADD_TRANSFORMER_METHOD_NAME = "addTransformer";
/*     */   private static final String GET_THROWAWAY_CLASS_LOADER_METHOD_NAME = "getThrowawayClassLoader";
/*  73 */   private static final Log logger = LogFactory.getLog(ReflectiveLoadTimeWeaver.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private final ClassLoader classLoader;
/*     */ 
/*     */   
/*     */   private final Method addTransformerMethod;
/*     */ 
/*     */   
/*     */   private final Method getThrowawayClassLoaderMethod;
/*     */ 
/*     */ 
/*     */   
/*     */   public ReflectiveLoadTimeWeaver() {
/*  88 */     this(ClassUtils.getDefaultClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReflectiveLoadTimeWeaver(ClassLoader classLoader) {
/*  99 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/* 100 */     this.classLoader = classLoader;
/* 101 */     this.addTransformerMethod = ClassUtils.getMethodIfAvailable(this.classLoader
/* 102 */         .getClass(), "addTransformer", new Class[] { ClassFileTransformer.class });
/* 103 */     if (this.addTransformerMethod == null) {
/* 104 */       throw new IllegalStateException("ClassLoader [" + classLoader
/* 105 */           .getClass().getName() + "] does NOT provide an 'addTransformer(ClassFileTransformer)' method.");
/*     */     }
/*     */     
/* 108 */     this.getThrowawayClassLoaderMethod = ClassUtils.getMethodIfAvailable(this.classLoader
/* 109 */         .getClass(), "getThrowawayClassLoader", new Class[0]);
/*     */     
/* 111 */     if (this.getThrowawayClassLoaderMethod == null && 
/* 112 */       logger.isInfoEnabled()) {
/* 113 */       logger.info("The ClassLoader [" + classLoader.getClass().getName() + "] does NOT provide a 'getThrowawayClassLoader()' method; SimpleThrowawayClassLoader will be used instead.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTransformer(ClassFileTransformer transformer) {
/* 122 */     Assert.notNull(transformer, "Transformer must not be null");
/* 123 */     ReflectionUtils.invokeMethod(this.addTransformerMethod, this.classLoader, new Object[] { transformer });
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getInstrumentableClassLoader() {
/* 128 */     return this.classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getThrowawayClassLoader() {
/* 133 */     if (this.getThrowawayClassLoaderMethod != null) {
/*     */       
/* 135 */       ClassLoader target = (ClassLoader)ReflectionUtils.invokeMethod(this.getThrowawayClassLoaderMethod, this.classLoader);
/* 136 */       return (target instanceof org.springframework.core.DecoratingClassLoader) ? target : (ClassLoader)new OverridingClassLoader(this.classLoader, target);
/*     */     } 
/*     */ 
/*     */     
/* 140 */     return (ClassLoader)new SimpleThrowawayClassLoader(this.classLoader);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\instrument\classloading\ReflectiveLoadTimeWeaver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */