/*    */ package org.springframework.instrument.classloading.jboss;
/*    */ 
/*    */ import java.lang.instrument.ClassFileTransformer;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.util.ReflectionUtils;
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
/*    */ class JBossModulesAdapter
/*    */   implements JBossClassLoaderAdapter
/*    */ {
/*    */   private static final String DELEGATING_TRANSFORMER_CLASS_NAME = "org.jboss.as.server.deployment.module.DelegatingClassFileTransformer";
/*    */   private final ClassLoader classLoader;
/*    */   private final Method addTransformer;
/*    */   private final Object delegatingTransformer;
/*    */   
/*    */   public JBossModulesAdapter(ClassLoader classLoader) {
/* 47 */     this.classLoader = classLoader;
/*    */     try {
/* 49 */       Field transformer = ReflectionUtils.findField(classLoader.getClass(), "transformer");
/* 50 */       if (transformer == null) {
/* 51 */         throw new IllegalArgumentException("Could not find 'transformer' field on JBoss ClassLoader: " + classLoader
/* 52 */             .getClass().getName());
/*    */       }
/* 54 */       transformer.setAccessible(true);
/* 55 */       this.delegatingTransformer = transformer.get(classLoader);
/* 56 */       if (!this.delegatingTransformer.getClass().getName().equals("org.jboss.as.server.deployment.module.DelegatingClassFileTransformer")) {
/* 57 */         throw new IllegalStateException("Transformer not of the expected type DelegatingClassFileTransformer: " + this.delegatingTransformer
/*    */             
/* 59 */             .getClass().getName());
/*    */       }
/* 61 */       this.addTransformer = ReflectionUtils.findMethod(this.delegatingTransformer.getClass(), "addTransformer", new Class[] { ClassFileTransformer.class });
/*    */       
/* 63 */       if (this.addTransformer == null) {
/* 64 */         throw new IllegalArgumentException("Could not find 'addTransformer' method on JBoss DelegatingClassFileTransformer: " + this.delegatingTransformer
/*    */             
/* 66 */             .getClass().getName());
/*    */       }
/* 68 */       this.addTransformer.setAccessible(true);
/*    */     }
/* 70 */     catch (Throwable ex) {
/* 71 */       throw new IllegalStateException("Could not initialize JBoss LoadTimeWeaver", ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void addTransformer(ClassFileTransformer transformer) {
/*    */     try {
/* 78 */       this.addTransformer.invoke(this.delegatingTransformer, new Object[] { transformer });
/*    */     }
/* 80 */     catch (Throwable ex) {
/* 81 */       throw new IllegalStateException("Could not add transformer on JBoss 7 ClassLoader " + this.classLoader, ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public ClassLoader getInstrumentableClassLoader() {
/* 87 */     return this.classLoader;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\instrument\classloading\jboss\JBossModulesAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */