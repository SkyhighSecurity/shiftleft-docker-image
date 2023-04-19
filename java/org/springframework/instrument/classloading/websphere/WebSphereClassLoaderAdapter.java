/*     */ package org.springframework.instrument.classloading.websphere;
/*     */ 
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.List;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class WebSphereClassLoaderAdapter
/*     */ {
/*     */   private static final String COMPOUND_CLASS_LOADER_NAME = "com.ibm.ws.classloader.CompoundClassLoader";
/*     */   private static final String CLASS_PRE_PROCESSOR_NAME = "com.ibm.websphere.classloader.ClassLoaderInstancePreDefinePlugin";
/*     */   private static final String PLUGINS_FIELD = "preDefinePlugins";
/*     */   private ClassLoader classLoader;
/*     */   private Class<?> wsPreProcessorClass;
/*     */   private Method addPreDefinePlugin;
/*     */   private Constructor<? extends ClassLoader> cloneConstructor;
/*     */   private Field transformerList;
/*     */   
/*     */   public WebSphereClassLoaderAdapter(ClassLoader classLoader) {
/*     */     Class<?> wsCompoundClassLoaderClass;
/*     */     try {
/*  62 */       wsCompoundClassLoaderClass = classLoader.loadClass("com.ibm.ws.classloader.CompoundClassLoader");
/*  63 */       this.cloneConstructor = (Constructor)classLoader.getClass().getDeclaredConstructor(new Class[] { wsCompoundClassLoaderClass });
/*  64 */       this.cloneConstructor.setAccessible(true);
/*     */       
/*  66 */       this.wsPreProcessorClass = classLoader.loadClass("com.ibm.websphere.classloader.ClassLoaderInstancePreDefinePlugin");
/*  67 */       this.addPreDefinePlugin = classLoader.getClass().getMethod("addPreDefinePlugin", new Class[] { this.wsPreProcessorClass });
/*  68 */       this.transformerList = wsCompoundClassLoaderClass.getDeclaredField("preDefinePlugins");
/*  69 */       this.transformerList.setAccessible(true);
/*     */     }
/*  71 */     catch (Throwable ex) {
/*  72 */       throw new IllegalStateException("Could not initialize WebSphere LoadTimeWeaver because WebSphere API classes are not available", ex);
/*     */     } 
/*     */ 
/*     */     
/*  76 */     if (!wsCompoundClassLoaderClass.isInstance(classLoader)) {
/*  77 */       throw new IllegalArgumentException("ClassLoader must be instance of com.ibm.ws.classloader.CompoundClassLoader");
/*     */     }
/*  79 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getClassLoader() {
/*  84 */     return this.classLoader;
/*     */   }
/*     */   
/*     */   public void addTransformer(ClassFileTransformer transformer) {
/*  88 */     Assert.notNull(transformer, "ClassFileTransformer must not be null");
/*     */     try {
/*  90 */       InvocationHandler adapter = new WebSphereClassPreDefinePlugin(transformer);
/*  91 */       Object adapterInstance = Proxy.newProxyInstance(this.wsPreProcessorClass.getClassLoader(), new Class[] { this.wsPreProcessorClass }, adapter);
/*     */       
/*  93 */       this.addPreDefinePlugin.invoke(this.classLoader, new Object[] { adapterInstance });
/*     */     }
/*  95 */     catch (InvocationTargetException ex) {
/*  96 */       throw new IllegalStateException("WebSphere addPreDefinePlugin method threw exception", ex.getCause());
/*     */     }
/*  98 */     catch (Throwable ex) {
/*  99 */       throw new IllegalStateException("Could not invoke WebSphere addPreDefinePlugin method", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public ClassLoader getThrowawayClassLoader() {
/*     */     try {
/* 105 */       ClassLoader loader = this.cloneConstructor.newInstance(new Object[] { getClassLoader() });
/*     */       
/* 107 */       List<?> list = (List)this.transformerList.get(loader);
/* 108 */       list.clear();
/* 109 */       return loader;
/*     */     }
/* 111 */     catch (InvocationTargetException ex) {
/* 112 */       throw new IllegalStateException("WebSphere CompoundClassLoader constructor failed", ex.getCause());
/*     */     }
/* 114 */     catch (Throwable ex) {
/* 115 */       throw new IllegalStateException("Could not construct WebSphere CompoundClassLoader", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\instrument\classloading\websphere\WebSphereClassLoaderAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */