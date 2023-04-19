/*    */ package org.springframework.instrument.classloading.weblogic;
/*    */ 
/*    */ import java.lang.instrument.ClassFileTransformer;
/*    */ import java.lang.instrument.IllegalClassFormatException;
/*    */ import java.lang.reflect.InvocationHandler;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Hashtable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class WebLogicClassPreProcessorAdapter
/*    */   implements InvocationHandler
/*    */ {
/*    */   private final ClassFileTransformer transformer;
/*    */   private final ClassLoader loader;
/*    */   
/*    */   public WebLogicClassPreProcessorAdapter(ClassFileTransformer transformer, ClassLoader loader) {
/* 47 */     this.transformer = transformer;
/* 48 */     this.loader = loader;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 54 */     String name = method.getName();
/* 55 */     if ("equals".equals(name)) {
/* 56 */       return Boolean.valueOf((proxy == args[0]));
/*    */     }
/* 58 */     if ("hashCode".equals(name)) {
/* 59 */       return Integer.valueOf(hashCode());
/*    */     }
/* 61 */     if ("toString".equals(name)) {
/* 62 */       return toString();
/*    */     }
/* 64 */     if ("initialize".equals(name)) {
/* 65 */       initialize((Hashtable<?, ?>)args[0]);
/* 66 */       return null;
/*    */     } 
/* 68 */     if ("preProcess".equals(name)) {
/* 69 */       return preProcess((String)args[0], (byte[])args[1]);
/*    */     }
/*    */     
/* 72 */     throw new IllegalArgumentException("Unknown method: " + method);
/*    */   }
/*    */ 
/*    */   
/*    */   public void initialize(Hashtable<?, ?> params) {}
/*    */ 
/*    */   
/*    */   public byte[] preProcess(String className, byte[] classBytes) {
/*    */     try {
/* 81 */       byte[] result = this.transformer.transform(this.loader, className, null, null, classBytes);
/* 82 */       return (result != null) ? result : classBytes;
/*    */     }
/* 84 */     catch (IllegalClassFormatException ex) {
/* 85 */       throw new IllegalStateException("Cannot transform due to illegal class format", ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 91 */     return getClass().getName() + " for transformer: " + this.transformer;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\instrument\classloading\weblogic\WebLogicClassPreProcessorAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */