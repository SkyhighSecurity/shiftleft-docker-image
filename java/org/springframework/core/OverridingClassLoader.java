/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.springframework.lang.UsesJava7;
/*     */ import org.springframework.util.FileCopyUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class OverridingClassLoader
/*     */   extends DecoratingClassLoader
/*     */ {
/*  41 */   public static final String[] DEFAULT_EXCLUDED_PACKAGES = new String[] { "java.", "javax.", "sun.", "oracle.", "javassist.", "org.aspectj.", "net.sf.cglib." };
/*     */   
/*     */   private static final String CLASS_FILE_SUFFIX = ".class";
/*     */   private final ClassLoader overrideDelegate;
/*     */   
/*     */   static {
/*  47 */     if (parallelCapableClassLoaderAvailable) {
/*  48 */       ClassLoader.registerAsParallelCapable();
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
/*     */   public OverridingClassLoader(ClassLoader parent) {
/*  61 */     this(parent, (ClassLoader)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OverridingClassLoader(ClassLoader parent, ClassLoader overrideDelegate) {
/*  71 */     super(parent);
/*  72 */     this.overrideDelegate = overrideDelegate;
/*  73 */     for (String packageName : DEFAULT_EXCLUDED_PACKAGES) {
/*  74 */       excludePackage(packageName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> loadClass(String name) throws ClassNotFoundException {
/*  81 */     if (this.overrideDelegate != null && isEligibleForOverriding(name)) {
/*  82 */       return this.overrideDelegate.loadClass(name);
/*     */     }
/*  84 */     return super.loadClass(name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
/*  89 */     if (isEligibleForOverriding(name)) {
/*  90 */       Class<?> result = loadClassForOverriding(name);
/*  91 */       if (result != null) {
/*  92 */         if (resolve) {
/*  93 */           resolveClass(result);
/*     */         }
/*  95 */         return result;
/*     */       } 
/*     */     } 
/*  98 */     return super.loadClass(name, resolve);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isEligibleForOverriding(String className) {
/* 109 */     return !isExcluded(className);
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
/*     */   protected Class<?> loadClassForOverriding(String name) throws ClassNotFoundException {
/* 121 */     Class<?> result = findLoadedClass(name);
/* 122 */     if (result == null) {
/* 123 */       byte[] bytes = loadBytesForClass(name);
/* 124 */       if (bytes != null) {
/* 125 */         result = defineClass(name, bytes, 0, bytes.length);
/*     */       }
/*     */     } 
/* 128 */     return result;
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
/*     */   protected byte[] loadBytesForClass(String name) throws ClassNotFoundException {
/* 142 */     InputStream is = openStreamForClass(name);
/* 143 */     if (is == null) {
/* 144 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 148 */       byte[] bytes = FileCopyUtils.copyToByteArray(is);
/*     */       
/* 150 */       return transformIfNecessary(name, bytes);
/*     */     }
/* 152 */     catch (IOException ex) {
/* 153 */       throw new ClassNotFoundException("Cannot load resource for class [" + name + "]", ex);
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
/*     */   protected InputStream openStreamForClass(String name) {
/* 165 */     String internalName = name.replace('.', '/') + ".class";
/* 166 */     return getParent().getResourceAsStream(internalName);
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
/*     */   protected byte[] transformIfNecessary(String name, byte[] bytes) {
/* 179 */     return bytes;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\OverridingClassLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */