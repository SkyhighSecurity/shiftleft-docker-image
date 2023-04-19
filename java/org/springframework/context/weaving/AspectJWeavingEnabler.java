/*     */ package org.springframework.context.weaving;
/*     */ 
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import java.lang.instrument.IllegalClassFormatException;
/*     */ import java.security.ProtectionDomain;
/*     */ import org.aspectj.weaver.loadtime.ClassPreProcessorAgentAdapter;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
/*     */ import org.springframework.instrument.classloading.LoadTimeWeaver;
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
/*     */ 
/*     */ public class AspectJWeavingEnabler
/*     */   implements BeanFactoryPostProcessor, BeanClassLoaderAware, LoadTimeWeaverAware, Ordered
/*     */ {
/*     */   public static final String ASPECTJ_AOP_XML_RESOURCE = "META-INF/aop.xml";
/*     */   private ClassLoader beanClassLoader;
/*     */   private LoadTimeWeaver loadTimeWeaver;
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/*  56 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLoadTimeWeaver(LoadTimeWeaver loadTimeWeaver) {
/*  61 */     this.loadTimeWeaver = loadTimeWeaver;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/*  66 */     return Integer.MIN_VALUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
/*  71 */     enableAspectJWeaving(this.loadTimeWeaver, this.beanClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void enableAspectJWeaving(LoadTimeWeaver weaverToUse, ClassLoader beanClassLoader) {
/*     */     InstrumentationLoadTimeWeaver instrumentationLoadTimeWeaver;
/*  81 */     if (weaverToUse == null) {
/*  82 */       if (InstrumentationLoadTimeWeaver.isInstrumentationAvailable()) {
/*  83 */         instrumentationLoadTimeWeaver = new InstrumentationLoadTimeWeaver(beanClassLoader);
/*     */       } else {
/*     */         
/*  86 */         throw new IllegalStateException("No LoadTimeWeaver available");
/*     */       } 
/*     */     }
/*  89 */     instrumentationLoadTimeWeaver.addTransformer(new AspectJClassBypassingClassFileTransformer((ClassFileTransformer)new ClassPreProcessorAgentAdapter()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class AspectJClassBypassingClassFileTransformer
/*     */     implements ClassFileTransformer
/*     */   {
/*     */     private final ClassFileTransformer delegate;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AspectJClassBypassingClassFileTransformer(ClassFileTransformer delegate) {
/* 104 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
/* 111 */       if (className.startsWith("org.aspectj") || className.startsWith("org/aspectj")) {
/* 112 */         return classfileBuffer;
/*     */       }
/* 114 */       return this.delegate.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\weaving\AspectJWeavingEnabler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */