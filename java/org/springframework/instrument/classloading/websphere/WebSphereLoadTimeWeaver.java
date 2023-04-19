/*    */ package org.springframework.instrument.classloading.websphere;
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
/*    */ public class WebSphereLoadTimeWeaver
/*    */   implements LoadTimeWeaver
/*    */ {
/*    */   private final WebSphereClassLoaderAdapter classLoader;
/*    */   
/*    */   public WebSphereLoadTimeWeaver() {
/* 44 */     this(ClassUtils.getDefaultClassLoader());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WebSphereLoadTimeWeaver(ClassLoader classLoader) {
/* 53 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/* 54 */     this.classLoader = new WebSphereClassLoaderAdapter(classLoader);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addTransformer(ClassFileTransformer transformer) {
/* 60 */     this.classLoader.addTransformer(transformer);
/*    */   }
/*    */ 
/*    */   
/*    */   public ClassLoader getInstrumentableClassLoader() {
/* 65 */     return this.classLoader.getClassLoader();
/*    */   }
/*    */ 
/*    */   
/*    */   public ClassLoader getThrowawayClassLoader() {
/* 70 */     return (ClassLoader)new OverridingClassLoader(this.classLoader.getClassLoader(), this.classLoader
/* 71 */         .getThrowawayClassLoader());
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\instrument\classloading\websphere\WebSphereLoadTimeWeaver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */