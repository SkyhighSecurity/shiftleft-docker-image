/*    */ package org.springframework.instrument.classloading.weblogic;
/*    */ 
/*    */ import java.lang.instrument.ClassFileTransformer;
/*    */ import org.springframework.core.OverridingClassLoader;
/*    */ import org.springframework.instrument.classloading.LoadTimeWeaver;
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
/*    */ public class WebLogicLoadTimeWeaver
/*    */   implements LoadTimeWeaver
/*    */ {
/*    */   private final WebLogicClassLoaderAdapter classLoader;
/*    */   
/*    */   public WebLogicLoadTimeWeaver() {
/* 47 */     this(ClassUtils.getDefaultClassLoader());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WebLogicLoadTimeWeaver(ClassLoader classLoader) {
/* 57 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/* 58 */     this.classLoader = new WebLogicClassLoaderAdapter(classLoader);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addTransformer(ClassFileTransformer transformer) {
/* 64 */     this.classLoader.addTransformer(transformer);
/*    */   }
/*    */ 
/*    */   
/*    */   public ClassLoader getInstrumentableClassLoader() {
/* 69 */     return this.classLoader.getClassLoader();
/*    */   }
/*    */ 
/*    */   
/*    */   public ClassLoader getThrowawayClassLoader() {
/* 74 */     return (ClassLoader)new OverridingClassLoader(this.classLoader.getClassLoader(), this.classLoader
/* 75 */         .getThrowawayClassLoader());
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\instrument\classloading\weblogic\WebLogicLoadTimeWeaver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */