/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.NotSerializableException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectStreamClass;
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
/*     */ public class ConfigurableObjectInputStream
/*     */   extends ObjectInputStream
/*     */ {
/*     */   private final ClassLoader classLoader;
/*     */   private final boolean acceptProxyClasses;
/*     */   
/*     */   public ConfigurableObjectInputStream(InputStream in, ClassLoader classLoader) throws IOException {
/*  49 */     this(in, classLoader, true);
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
/*     */   public ConfigurableObjectInputStream(InputStream in, ClassLoader classLoader, boolean acceptProxyClasses) throws IOException {
/*  63 */     super(in);
/*  64 */     this.classLoader = classLoader;
/*  65 */     this.acceptProxyClasses = acceptProxyClasses;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> resolveClass(ObjectStreamClass classDesc) throws IOException, ClassNotFoundException {
/*     */     try {
/*  72 */       if (this.classLoader != null)
/*     */       {
/*  74 */         return ClassUtils.forName(classDesc.getName(), this.classLoader);
/*     */       }
/*     */ 
/*     */       
/*  78 */       return super.resolveClass(classDesc);
/*     */     
/*     */     }
/*  81 */     catch (ClassNotFoundException ex) {
/*  82 */       return resolveFallbackIfPossible(classDesc.getName(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Class<?> resolveProxyClass(String[] interfaces) throws IOException, ClassNotFoundException {
/*  88 */     if (!this.acceptProxyClasses) {
/*  89 */       throw new NotSerializableException("Not allowed to accept serialized proxy classes");
/*     */     }
/*  91 */     if (this.classLoader != null) {
/*     */       
/*  93 */       Class<?>[] resolvedInterfaces = new Class[interfaces.length];
/*  94 */       for (int i = 0; i < interfaces.length; i++) {
/*     */         try {
/*  96 */           resolvedInterfaces[i] = ClassUtils.forName(interfaces[i], this.classLoader);
/*     */         }
/*  98 */         catch (ClassNotFoundException ex) {
/*  99 */           resolvedInterfaces[i] = resolveFallbackIfPossible(interfaces[i], ex);
/*     */         } 
/*     */       } 
/*     */       try {
/* 103 */         return ClassUtils.createCompositeInterface(resolvedInterfaces, this.classLoader);
/*     */       }
/* 105 */       catch (IllegalArgumentException ex) {
/* 106 */         throw new ClassNotFoundException(null, ex);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 112 */       return super.resolveProxyClass(interfaces);
/*     */     }
/* 114 */     catch (ClassNotFoundException ex) {
/* 115 */       Class<?>[] resolvedInterfaces = new Class[interfaces.length];
/* 116 */       for (int i = 0; i < interfaces.length; i++) {
/* 117 */         resolvedInterfaces[i] = resolveFallbackIfPossible(interfaces[i], ex);
/*     */       }
/* 119 */       return ClassUtils.createCompositeInterface(resolvedInterfaces, getFallbackClassLoader());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> resolveFallbackIfPossible(String className, ClassNotFoundException ex) throws IOException, ClassNotFoundException {
/* 136 */     throw ex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassLoader getFallbackClassLoader() throws IOException {
/* 146 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\ConfigurableObjectInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */