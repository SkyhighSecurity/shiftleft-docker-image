/*     */ package org.springframework.objenesis.instantiator.util;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.ProtectionDomain;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ClassDefinitionUtils
/*     */ {
/*     */   public static final byte OPS_aload_0 = 42;
/*     */   public static final byte OPS_invokespecial = -73;
/*     */   public static final byte OPS_return = -79;
/*     */   public static final byte OPS_new = -69;
/*     */   public static final byte OPS_dup = 89;
/*     */   public static final byte OPS_areturn = -80;
/*     */   public static final int CONSTANT_Utf8 = 1;
/*     */   public static final int CONSTANT_Integer = 3;
/*     */   public static final int CONSTANT_Float = 4;
/*     */   public static final int CONSTANT_Long = 5;
/*     */   public static final int CONSTANT_Double = 6;
/*     */   public static final int CONSTANT_Class = 7;
/*     */   public static final int CONSTANT_String = 8;
/*     */   public static final int CONSTANT_Fieldref = 9;
/*     */   public static final int CONSTANT_Methodref = 10;
/*     */   public static final int CONSTANT_InterfaceMethodref = 11;
/*     */   public static final int CONSTANT_NameAndType = 12;
/*     */   public static final int CONSTANT_MethodHandle = 15;
/*     */   public static final int CONSTANT_MethodType = 16;
/*     */   public static final int CONSTANT_InvokeDynamic = 18;
/*     */   public static final int ACC_PUBLIC = 1;
/*     */   public static final int ACC_FINAL = 16;
/*     */   public static final int ACC_SUPER = 32;
/*     */   public static final int ACC_INTERFACE = 512;
/*     */   public static final int ACC_ABSTRACT = 1024;
/*     */   public static final int ACC_SYNTHETIC = 4096;
/*     */   public static final int ACC_ANNOTATION = 8192;
/*     */   public static final int ACC_ENUM = 16384;
/*  70 */   public static final byte[] MAGIC = new byte[] { -54, -2, -70, -66 };
/*  71 */   public static final byte[] VERSION = new byte[] { 0, 0, 0, 49 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   private static final ProtectionDomain PROTECTION_DOMAIN = AccessController.<ProtectionDomain>doPrivileged(new PrivilegedAction<ProtectionDomain>() {
/*     */         public ProtectionDomain run() {
/*  81 */           return ClassDefinitionUtils.class.getProtectionDomain();
/*     */         }
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Class<T> defineClass(String className, byte[] b, ClassLoader loader) throws Exception {
/* 100 */     Class<T> c = (Class)UnsafeUtils.getUnsafe().defineClass(className, b, 0, b.length, loader, PROTECTION_DOMAIN);
/*     */     
/* 102 */     Class.forName(className, true, loader);
/* 103 */     return c;
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
/*     */   public static byte[] readClass(String className) throws IOException {
/*     */     int length;
/* 116 */     className = classNameToResource(className);
/*     */     
/* 118 */     byte[] b = new byte[2500];
/*     */ 
/*     */ 
/*     */     
/* 122 */     InputStream in = ClassDefinitionUtils.class.getClassLoader().getResourceAsStream(className);
/*     */     try {
/* 124 */       length = in.read(b);
/*     */     } finally {
/*     */       
/* 127 */       in.close();
/*     */     } 
/*     */     
/* 130 */     if (length >= 2500) {
/* 131 */       throw new IllegalArgumentException("The class is longer that 2500 bytes which is currently unsupported");
/*     */     }
/*     */     
/* 134 */     byte[] copy = new byte[length];
/* 135 */     System.arraycopy(b, 0, copy, 0, length);
/* 136 */     return copy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeClass(String fileName, byte[] bytes) throws IOException {
/* 147 */     BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fileName));
/*     */     try {
/* 149 */       out.write(bytes);
/*     */     } finally {
/*     */       
/* 152 */       out.close();
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
/*     */   public static String classNameToInternalClassName(String className) {
/* 164 */     return className.replace('.', '/');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String classNameToResource(String className) {
/* 175 */     return classNameToInternalClassName(className) + ".class";
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
/*     */   public static <T> Class<T> getExistingClass(ClassLoader classLoader, String className) {
/*     */     try {
/* 189 */       return (Class)Class.forName(className, true, classLoader);
/*     */     }
/* 191 */     catch (ClassNotFoundException e) {
/* 192 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\objenesis\instantiato\\util\ClassDefinitionUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */