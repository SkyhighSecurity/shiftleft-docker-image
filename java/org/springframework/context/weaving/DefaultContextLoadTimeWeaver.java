/*     */ package org.springframework.context.weaving;
/*     */ 
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
/*     */ import org.springframework.instrument.classloading.LoadTimeWeaver;
/*     */ import org.springframework.instrument.classloading.ReflectiveLoadTimeWeaver;
/*     */ import org.springframework.instrument.classloading.glassfish.GlassFishLoadTimeWeaver;
/*     */ import org.springframework.instrument.classloading.jboss.JBossLoadTimeWeaver;
/*     */ import org.springframework.instrument.classloading.tomcat.TomcatLoadTimeWeaver;
/*     */ import org.springframework.instrument.classloading.weblogic.WebLogicLoadTimeWeaver;
/*     */ import org.springframework.instrument.classloading.websphere.WebSphereLoadTimeWeaver;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultContextLoadTimeWeaver
/*     */   implements LoadTimeWeaver, BeanClassLoaderAware, DisposableBean
/*     */ {
/*  58 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private LoadTimeWeaver loadTimeWeaver;
/*     */ 
/*     */   
/*     */   public DefaultContextLoadTimeWeaver() {}
/*     */ 
/*     */   
/*     */   public DefaultContextLoadTimeWeaver(ClassLoader beanClassLoader) {
/*  67 */     setBeanClassLoader(beanClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/*  73 */     LoadTimeWeaver serverSpecificLoadTimeWeaver = createServerSpecificLoadTimeWeaver(classLoader);
/*  74 */     if (serverSpecificLoadTimeWeaver != null) {
/*  75 */       if (this.logger.isInfoEnabled()) {
/*  76 */         this.logger.info("Determined server-specific load-time weaver: " + serverSpecificLoadTimeWeaver
/*  77 */             .getClass().getName());
/*     */       }
/*  79 */       this.loadTimeWeaver = serverSpecificLoadTimeWeaver;
/*     */     }
/*  81 */     else if (InstrumentationLoadTimeWeaver.isInstrumentationAvailable()) {
/*  82 */       this.logger.info("Found Spring's JVM agent for instrumentation");
/*  83 */       this.loadTimeWeaver = (LoadTimeWeaver)new InstrumentationLoadTimeWeaver(classLoader);
/*     */     } else {
/*     */       
/*     */       try {
/*  87 */         this.loadTimeWeaver = (LoadTimeWeaver)new ReflectiveLoadTimeWeaver(classLoader);
/*  88 */         if (this.logger.isInfoEnabled()) {
/*  89 */           this.logger.info("Using a reflective load-time weaver for class loader: " + this.loadTimeWeaver
/*  90 */               .getInstrumentableClassLoader().getClass().getName());
/*     */         }
/*     */       }
/*  93 */       catch (IllegalStateException ex) {
/*  94 */         throw new IllegalStateException(ex.getMessage() + " Specify a custom LoadTimeWeaver or start your Java virtual machine with Spring's agent: -javaagent:org.springframework.instrument.jar");
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected LoadTimeWeaver createServerSpecificLoadTimeWeaver(ClassLoader classLoader) {
/* 110 */     String name = classLoader.getClass().getName();
/*     */     try {
/* 112 */       if (name.startsWith("org.apache.catalina")) {
/* 113 */         return (LoadTimeWeaver)new TomcatLoadTimeWeaver(classLoader);
/*     */       }
/* 115 */       if (name.startsWith("org.glassfish")) {
/* 116 */         return (LoadTimeWeaver)new GlassFishLoadTimeWeaver(classLoader);
/*     */       }
/* 118 */       if (name.startsWith("org.jboss")) {
/* 119 */         return (LoadTimeWeaver)new JBossLoadTimeWeaver(classLoader);
/*     */       }
/* 121 */       if (name.startsWith("com.ibm")) {
/* 122 */         return (LoadTimeWeaver)new WebSphereLoadTimeWeaver(classLoader);
/*     */       }
/* 124 */       if (name.startsWith("weblogic")) {
/* 125 */         return (LoadTimeWeaver)new WebLogicLoadTimeWeaver(classLoader);
/*     */       }
/*     */     }
/* 128 */     catch (Exception ex) {
/* 129 */       if (this.logger.isInfoEnabled()) {
/* 130 */         this.logger.info("Could not obtain server-specific LoadTimeWeaver: " + ex.getMessage());
/*     */       }
/*     */     } 
/* 133 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 138 */     if (this.loadTimeWeaver instanceof InstrumentationLoadTimeWeaver) {
/* 139 */       if (this.logger.isInfoEnabled()) {
/* 140 */         this.logger.info("Removing all registered transformers for class loader: " + this.loadTimeWeaver
/* 141 */             .getInstrumentableClassLoader().getClass().getName());
/*     */       }
/* 143 */       ((InstrumentationLoadTimeWeaver)this.loadTimeWeaver).removeTransformers();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTransformer(ClassFileTransformer transformer) {
/* 150 */     this.loadTimeWeaver.addTransformer(transformer);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getInstrumentableClassLoader() {
/* 155 */     return this.loadTimeWeaver.getInstrumentableClassLoader();
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getThrowawayClassLoader() {
/* 160 */     return this.loadTimeWeaver.getThrowawayClassLoader();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\weaving\DefaultContextLoadTimeWeaver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */