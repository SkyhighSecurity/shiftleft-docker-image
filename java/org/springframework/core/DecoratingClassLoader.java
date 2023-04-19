/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.lang.UsesJava7;
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
/*     */ @UsesJava7
/*     */ public abstract class DecoratingClassLoader
/*     */   extends ClassLoader
/*     */ {
/*  44 */   protected static final boolean parallelCapableClassLoaderAvailable = ClassUtils.hasMethod(ClassLoader.class, "registerAsParallelCapable", new Class[0]);
/*     */   
/*     */   static {
/*  47 */     if (parallelCapableClassLoaderAvailable) {
/*  48 */       ClassLoader.registerAsParallelCapable();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  54 */   private final Set<String> excludedPackages = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>(8));
/*     */ 
/*     */   
/*  57 */   private final Set<String> excludedClasses = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>(8));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DecoratingClassLoader(ClassLoader parent) {
/*  71 */     super(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void excludePackage(String packageName) {
/*  82 */     Assert.notNull(packageName, "Package name must not be null");
/*  83 */     this.excludedPackages.add(packageName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void excludeClass(String className) {
/*  93 */     Assert.notNull(className, "Class name must not be null");
/*  94 */     this.excludedClasses.add(className);
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
/*     */   protected boolean isExcluded(String className) {
/* 107 */     if (this.excludedClasses.contains(className)) {
/* 108 */       return true;
/*     */     }
/* 110 */     for (String packageName : this.excludedPackages) {
/* 111 */       if (className.startsWith(packageName)) {
/* 112 */         return true;
/*     */       }
/*     */     } 
/* 115 */     return false;
/*     */   }
/*     */   
/*     */   public DecoratingClassLoader() {}
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\DecoratingClassLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */