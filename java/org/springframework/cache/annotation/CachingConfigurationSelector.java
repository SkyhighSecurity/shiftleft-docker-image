/*     */ package org.springframework.cache.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.context.annotation.AdviceMode;
/*     */ import org.springframework.context.annotation.AdviceModeImportSelector;
/*     */ import org.springframework.context.annotation.AutoProxyRegistrar;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class CachingConfigurationSelector
/*     */   extends AdviceModeImportSelector<EnableCaching>
/*     */ {
/*     */   private static final String PROXY_JCACHE_CONFIGURATION_CLASS = "org.springframework.cache.jcache.config.ProxyJCacheConfiguration";
/*     */   private static final String CACHE_ASPECT_CONFIGURATION_CLASS_NAME = "org.springframework.cache.aspectj.AspectJCachingConfiguration";
/*     */   private static final String JCACHE_ASPECT_CONFIGURATION_CLASS_NAME = "org.springframework.cache.aspectj.AspectJJCacheConfiguration";
/*  53 */   private static final boolean jsr107Present = ClassUtils.isPresent("javax.cache.Cache", CachingConfigurationSelector.class
/*  54 */       .getClassLoader());
/*     */   
/*  56 */   private static final boolean jcacheImplPresent = ClassUtils.isPresent("org.springframework.cache.jcache.config.ProxyJCacheConfiguration", CachingConfigurationSelector.class
/*  57 */       .getClassLoader());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] selectImports(AdviceMode adviceMode) {
/*  67 */     switch (adviceMode) {
/*     */       case PROXY:
/*  69 */         return getProxyImports();
/*     */       case ASPECTJ:
/*  71 */         return getAspectJImports();
/*     */     } 
/*  73 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String[] getProxyImports() {
/*  82 */     List<String> result = new ArrayList<String>(3);
/*  83 */     result.add(AutoProxyRegistrar.class.getName());
/*  84 */     result.add(ProxyCachingConfiguration.class.getName());
/*  85 */     if (jsr107Present && jcacheImplPresent) {
/*  86 */       result.add("org.springframework.cache.jcache.config.ProxyJCacheConfiguration");
/*     */     }
/*  88 */     return StringUtils.toStringArray(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String[] getAspectJImports() {
/*  96 */     List<String> result = new ArrayList<String>(2);
/*  97 */     result.add("org.springframework.cache.aspectj.AspectJCachingConfiguration");
/*  98 */     if (jsr107Present && jcacheImplPresent) {
/*  99 */       result.add("org.springframework.cache.aspectj.AspectJJCacheConfiguration");
/*     */     }
/* 101 */     return StringUtils.toStringArray(result);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\annotation\CachingConfigurationSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */