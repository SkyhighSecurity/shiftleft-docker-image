/*     */ package org.springframework.instrument.classloading;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import java.lang.instrument.IllegalClassFormatException;
/*     */ import java.net.URL;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.DecoratingClassLoader;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.FileCopyUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShadowingClassLoader
/*     */   extends DecoratingClassLoader
/*     */ {
/*  49 */   public static final String[] DEFAULT_EXCLUDED_PACKAGES = new String[] { "java.", "javax.", "sun.", "oracle.", "com.sun.", "com.ibm.", "COM.ibm.", "org.w3c.", "org.xml.", "org.dom4j.", "org.eclipse", "org.aspectj.", "net.sf.cglib", "org.springframework.cglib", "org.apache.xerces.", "org.apache.commons.logging." };
/*     */ 
/*     */ 
/*     */   
/*     */   private final ClassLoader enclosingClassLoader;
/*     */ 
/*     */ 
/*     */   
/*  57 */   private final List<ClassFileTransformer> classFileTransformers = new LinkedList<ClassFileTransformer>();
/*     */   
/*  59 */   private final Map<String, Class<?>> classCache = new HashMap<String, Class<?>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShadowingClassLoader(ClassLoader enclosingClassLoader) {
/*  69 */     this(enclosingClassLoader, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShadowingClassLoader(ClassLoader enclosingClassLoader, boolean defaultExcludes) {
/*  79 */     Assert.notNull(enclosingClassLoader, "Enclosing ClassLoader must not be null");
/*  80 */     this.enclosingClassLoader = enclosingClassLoader;
/*  81 */     if (defaultExcludes) {
/*  82 */       for (String excludedPackage : DEFAULT_EXCLUDED_PACKAGES) {
/*  83 */         excludePackage(excludedPackage);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTransformer(ClassFileTransformer transformer) {
/*  95 */     Assert.notNull(transformer, "Transformer must not be null");
/*  96 */     this.classFileTransformers.add(transformer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyTransformers(ShadowingClassLoader other) {
/* 105 */     Assert.notNull(other, "Other ClassLoader must not be null");
/* 106 */     this.classFileTransformers.addAll(other.classFileTransformers);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> loadClass(String name) throws ClassNotFoundException {
/* 112 */     if (shouldShadow(name)) {
/* 113 */       Class<?> cls = this.classCache.get(name);
/* 114 */       if (cls != null) {
/* 115 */         return cls;
/*     */       }
/* 117 */       return doLoadClass(name);
/*     */     } 
/*     */     
/* 120 */     return this.enclosingClassLoader.loadClass(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean shouldShadow(String className) {
/* 130 */     return (!className.equals(getClass().getName()) && !className.endsWith("ShadowingClassLoader") && 
/* 131 */       isEligibleForShadowing(className));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isEligibleForShadowing(String className) {
/* 142 */     return !isExcluded(className);
/*     */   }
/*     */ 
/*     */   
/*     */   private Class<?> doLoadClass(String name) throws ClassNotFoundException {
/* 147 */     String internalName = StringUtils.replace(name, ".", "/") + ".class";
/* 148 */     InputStream is = this.enclosingClassLoader.getResourceAsStream(internalName);
/* 149 */     if (is == null) {
/* 150 */       throw new ClassNotFoundException(name);
/*     */     }
/*     */     try {
/* 153 */       byte[] bytes = FileCopyUtils.copyToByteArray(is);
/* 154 */       bytes = applyTransformers(name, bytes);
/* 155 */       Class<?> cls = defineClass(name, bytes, 0, bytes.length);
/*     */       
/* 157 */       if (cls.getPackage() == null) {
/* 158 */         int packageSeparator = name.lastIndexOf('.');
/* 159 */         if (packageSeparator != -1) {
/* 160 */           String packageName = name.substring(0, packageSeparator);
/* 161 */           definePackage(packageName, null, null, null, null, null, null, null);
/*     */         } 
/*     */       } 
/* 164 */       this.classCache.put(name, cls);
/* 165 */       return cls;
/*     */     }
/* 167 */     catch (IOException ex) {
/* 168 */       throw new ClassNotFoundException("Cannot load resource for class [" + name + "]", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private byte[] applyTransformers(String name, byte[] bytes) {
/* 173 */     String internalName = StringUtils.replace(name, ".", "/");
/*     */     try {
/* 175 */       for (ClassFileTransformer transformer : this.classFileTransformers) {
/* 176 */         byte[] transformed = transformer.transform((ClassLoader)this, internalName, null, null, bytes);
/* 177 */         bytes = (transformed != null) ? transformed : bytes;
/*     */       } 
/* 179 */       return bytes;
/*     */     }
/* 181 */     catch (IllegalClassFormatException ex) {
/* 182 */       throw new IllegalStateException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getResource(String name) {
/* 189 */     return this.enclosingClassLoader.getResource(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getResourceAsStream(String name) {
/* 194 */     return this.enclosingClassLoader.getResourceAsStream(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Enumeration<URL> getResources(String name) throws IOException {
/* 199 */     return this.enclosingClassLoader.getResources(name);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\instrument\classloading\ShadowingClassLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */