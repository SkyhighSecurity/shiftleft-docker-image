/*     */ package org.springframework.instrument.classloading.jboss;
/*     */ 
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
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
/*     */ class JBossMCAdapter
/*     */   implements JBossClassLoaderAdapter
/*     */ {
/*     */   private static final String LOADER_NAME = "org.jboss.classloader.spi.base.BaseClassLoader";
/*     */   private static final String TRANSLATOR_NAME = "org.jboss.util.loading.Translator";
/*     */   private final ClassLoader classLoader;
/*     */   private final Object target;
/*     */   private final Class<?> translatorClass;
/*     */   private final Method addTranslator;
/*     */   
/*     */   public JBossMCAdapter(ClassLoader classLoader) {
/*     */     try {
/*  53 */       Class<?> clazzLoaderType = classLoader.loadClass("org.jboss.classloader.spi.base.BaseClassLoader");
/*     */       
/*  55 */       ClassLoader clazzLoader = null;
/*     */       
/*  57 */       for (ClassLoader cl = classLoader; cl != null && clazzLoader == null; cl = cl.getParent()) {
/*  58 */         if (clazzLoaderType.isInstance(cl)) {
/*  59 */           clazzLoader = cl;
/*     */         }
/*     */       } 
/*     */       
/*  63 */       if (clazzLoader == null) {
/*  64 */         throw new IllegalArgumentException(classLoader + " and its parents are not suitable ClassLoaders: A [" + "org.jboss.classloader.spi.base.BaseClassLoader" + "] implementation is required.");
/*     */       }
/*     */ 
/*     */       
/*  68 */       this.classLoader = clazzLoader;
/*     */       
/*  70 */       classLoader = clazzLoader.getClass().getClassLoader();
/*     */ 
/*     */       
/*  73 */       Method method = clazzLoaderType.getDeclaredMethod("getPolicy", new Class[0]);
/*  74 */       ReflectionUtils.makeAccessible(method);
/*  75 */       this.target = method.invoke(this.classLoader, new Object[0]);
/*     */ 
/*     */       
/*  78 */       this.translatorClass = classLoader.loadClass("org.jboss.util.loading.Translator");
/*  79 */       this.addTranslator = this.target.getClass().getMethod("addTranslator", new Class[] { this.translatorClass });
/*     */     }
/*  81 */     catch (Throwable ex) {
/*  82 */       throw new IllegalStateException("Could not initialize JBoss LoadTimeWeaver because the JBoss 6 API classes are not available", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTransformer(ClassFileTransformer transformer) {
/*  89 */     InvocationHandler adapter = new JBossMCTranslatorAdapter(transformer);
/*  90 */     Object adapterInstance = Proxy.newProxyInstance(this.translatorClass.getClassLoader(), new Class[] { this.translatorClass }, adapter);
/*     */     
/*     */     try {
/*  93 */       this.addTranslator.invoke(this.target, new Object[] { adapterInstance });
/*     */     }
/*  95 */     catch (Throwable ex) {
/*  96 */       throw new IllegalStateException("Could not add transformer on JBoss 6 ClassLoader " + this.classLoader, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getInstrumentableClassLoader() {
/* 102 */     return this.classLoader;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\instrument\classloading\jboss\JBossMCAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */