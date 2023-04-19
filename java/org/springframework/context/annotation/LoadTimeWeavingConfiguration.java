/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.context.weaving.AspectJWeavingEnabler;
/*     */ import org.springframework.context.weaving.DefaultContextLoadTimeWeaver;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.core.type.AnnotationMetadata;
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
/*     */ @Configuration
/*     */ public class LoadTimeWeavingConfiguration
/*     */   implements ImportAware, BeanClassLoaderAware
/*     */ {
/*     */   private AnnotationAttributes enableLTW;
/*     */   private LoadTimeWeavingConfigurer ltwConfigurer;
/*     */   private ClassLoader beanClassLoader;
/*     */   
/*     */   public void setImportMetadata(AnnotationMetadata importMetadata) {
/*  54 */     this.enableLTW = AnnotationConfigUtils.attributesFor((AnnotatedTypeMetadata)importMetadata, EnableLoadTimeWeaving.class);
/*  55 */     if (this.enableLTW == null) {
/*  56 */       throw new IllegalArgumentException("@EnableLoadTimeWeaving is not present on importing class " + importMetadata
/*  57 */           .getClassName());
/*     */     }
/*     */   }
/*     */   
/*     */   @Autowired(required = false)
/*     */   public void setLoadTimeWeavingConfigurer(LoadTimeWeavingConfigurer ltwConfigurer) {
/*  63 */     this.ltwConfigurer = ltwConfigurer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader beanClassLoader) {
/*  68 */     this.beanClassLoader = beanClassLoader;
/*     */   }
/*     */   
/*     */   @Bean(name = {"loadTimeWeaver"})
/*     */   @Role(2)
/*     */   public LoadTimeWeaver loadTimeWeaver() {
/*     */     DefaultContextLoadTimeWeaver defaultContextLoadTimeWeaver;
/*  75 */     LoadTimeWeaver loadTimeWeaver = null;
/*     */     
/*  77 */     if (this.ltwConfigurer != null)
/*     */     {
/*  79 */       loadTimeWeaver = this.ltwConfigurer.getLoadTimeWeaver();
/*     */     }
/*     */     
/*  82 */     if (loadTimeWeaver == null)
/*     */     {
/*  84 */       defaultContextLoadTimeWeaver = new DefaultContextLoadTimeWeaver(this.beanClassLoader);
/*     */     }
/*     */     
/*  87 */     EnableLoadTimeWeaving.AspectJWeaving aspectJWeaving = (EnableLoadTimeWeaving.AspectJWeaving)this.enableLTW.getEnum("aspectjWeaving");
/*  88 */     switch (aspectJWeaving) {
/*     */ 
/*     */ 
/*     */       
/*     */       case AUTODETECT:
/*  93 */         if (this.beanClassLoader.getResource("META-INF/aop.xml") == null) {
/*     */           break;
/*     */         }
/*     */ 
/*     */         
/*  98 */         AspectJWeavingEnabler.enableAspectJWeaving((LoadTimeWeaver)defaultContextLoadTimeWeaver, this.beanClassLoader);
/*     */         break;
/*     */       case ENABLED:
/* 101 */         AspectJWeavingEnabler.enableAspectJWeaving((LoadTimeWeaver)defaultContextLoadTimeWeaver, this.beanClassLoader);
/*     */         break;
/*     */     } 
/*     */     
/* 105 */     return (LoadTimeWeaver)defaultContextLoadTimeWeaver;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\LoadTimeWeavingConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */