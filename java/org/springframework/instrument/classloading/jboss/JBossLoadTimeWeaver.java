/*    */ package org.springframework.instrument.classloading.jboss;
/*    */ 
/*    */ import java.lang.instrument.ClassFileTransformer;
/*    */ import org.springframework.instrument.classloading.LoadTimeWeaver;
/*    */ import org.springframework.instrument.classloading.SimpleThrowawayClassLoader;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JBossLoadTimeWeaver
/*    */   implements LoadTimeWeaver
/*    */ {
/*    */   private final JBossClassLoaderAdapter adapter;
/*    */   
/*    */   public JBossLoadTimeWeaver() {
/* 53 */     this(ClassUtils.getDefaultClassLoader());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JBossLoadTimeWeaver(ClassLoader classLoader) {
/* 63 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/* 64 */     if (classLoader.getClass().getName().startsWith("org.jboss.modules")) {
/*    */       
/* 66 */       this.adapter = new JBossModulesAdapter(classLoader);
/*    */     }
/*    */     else {
/*    */       
/* 70 */       this.adapter = new JBossMCAdapter(classLoader);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addTransformer(ClassFileTransformer transformer) {
/* 77 */     this.adapter.addTransformer(transformer);
/*    */   }
/*    */ 
/*    */   
/*    */   public ClassLoader getInstrumentableClassLoader() {
/* 82 */     return this.adapter.getInstrumentableClassLoader();
/*    */   }
/*    */ 
/*    */   
/*    */   public ClassLoader getThrowawayClassLoader() {
/* 87 */     return (ClassLoader)new SimpleThrowawayClassLoader(getInstrumentableClassLoader());
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\instrument\classloading\jboss\JBossLoadTimeWeaver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */