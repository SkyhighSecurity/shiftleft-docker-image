/*    */ package org.springframework.instrument.classloading.jboss;
/*    */ 
/*    */ import java.lang.instrument.ClassFileTransformer;
/*    */ import java.lang.reflect.InvocationHandler;
/*    */ import java.lang.reflect.Method;
/*    */ import java.security.ProtectionDomain;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class JBossMCTranslatorAdapter
/*    */   implements InvocationHandler
/*    */ {
/*    */   private final ClassFileTransformer transformer;
/*    */   
/*    */   public JBossMCTranslatorAdapter(ClassFileTransformer transformer) {
/* 40 */     this.transformer = transformer;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 46 */     String name = method.getName();
/* 47 */     if ("equals".equals(name)) {
/* 48 */       return Boolean.valueOf((proxy == args[0]));
/*    */     }
/* 50 */     if ("hashCode".equals(name)) {
/* 51 */       return Integer.valueOf(hashCode());
/*    */     }
/* 53 */     if ("toString".equals(name)) {
/* 54 */       return toString();
/*    */     }
/* 56 */     if ("transform".equals(name)) {
/* 57 */       return transform((ClassLoader)args[0], (String)args[1], (Class)args[2], (ProtectionDomain)args[3], (byte[])args[4]);
/*    */     }
/*    */     
/* 60 */     if ("unregisterClassLoader".equals(name)) {
/* 61 */       unregisterClassLoader((ClassLoader)args[0]);
/* 62 */       return null;
/*    */     } 
/*    */     
/* 65 */     throw new IllegalArgumentException("Unknown method: " + method);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws Exception {
/* 72 */     return this.transformer.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void unregisterClassLoader(ClassLoader loader) {}
/*    */ 
/*    */   
/*    */   public String toString() {
/* 81 */     return getClass().getName() + " for transformer: " + this.transformer;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\instrument\classloading\jboss\JBossMCTranslatorAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */