/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.core.DecoratingClassLoader;
/*     */ import org.springframework.core.OverridingClassLoader;
/*     */ import org.springframework.core.SmartClassLoader;
/*     */ import org.springframework.lang.UsesJava7;
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
/*     */ @UsesJava7
/*     */ class ContextTypeMatchClassLoader
/*     */   extends DecoratingClassLoader
/*     */   implements SmartClassLoader
/*     */ {
/*     */   private static Method findLoadedClassMethod;
/*     */   
/*     */   static {
/*  44 */     if (parallelCapableClassLoaderAvailable) {
/*  45 */       ClassLoader.registerAsParallelCapable();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  54 */       findLoadedClassMethod = ClassLoader.class.getDeclaredMethod("findLoadedClass", new Class[] { String.class });
/*     */     }
/*  56 */     catch (NoSuchMethodException ex) {
/*  57 */       throw new IllegalStateException("Invalid [java.lang.ClassLoader] class: no 'findLoadedClass' method defined!");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  63 */   private final Map<String, byte[]> bytesCache = (Map)new ConcurrentHashMap<String, byte>(256);
/*     */ 
/*     */   
/*     */   public ContextTypeMatchClassLoader(ClassLoader parent) {
/*  67 */     super(parent);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> loadClass(String name) throws ClassNotFoundException {
/*  72 */     return (new ContextOverridingClassLoader(getParent())).loadClass(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isClassReloadable(Class<?> clazz) {
/*  77 */     return clazz.getClassLoader() instanceof ContextOverridingClassLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ContextOverridingClassLoader
/*     */     extends OverridingClassLoader
/*     */   {
/*     */     public ContextOverridingClassLoader(ClassLoader parent) {
/*  88 */       super(parent);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isEligibleForOverriding(String className) {
/*  93 */       if (isExcluded(className) || ContextTypeMatchClassLoader.this.isExcluded(className)) {
/*  94 */         return false;
/*     */       }
/*  96 */       ReflectionUtils.makeAccessible(ContextTypeMatchClassLoader.findLoadedClassMethod);
/*  97 */       ClassLoader parent = getParent();
/*  98 */       while (parent != null) {
/*  99 */         if (ReflectionUtils.invokeMethod(ContextTypeMatchClassLoader.findLoadedClassMethod, parent, new Object[] { className }) != null) {
/* 100 */           return false;
/*     */         }
/* 102 */         parent = parent.getParent();
/*     */       } 
/* 104 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Class<?> loadClassForOverriding(String name) throws ClassNotFoundException {
/* 109 */       byte[] bytes = (byte[])ContextTypeMatchClassLoader.this.bytesCache.get(name);
/* 110 */       if (bytes == null) {
/* 111 */         bytes = loadBytesForClass(name);
/* 112 */         if (bytes != null) {
/* 113 */           ContextTypeMatchClassLoader.this.bytesCache.put(name, bytes);
/*     */         } else {
/*     */           
/* 116 */           return null;
/*     */         } 
/*     */       } 
/* 119 */       return defineClass(name, bytes, 0, bytes.length);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\ContextTypeMatchClassLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */