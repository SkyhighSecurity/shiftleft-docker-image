/*     */ package org.springframework.cache.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.cache.Cache;
/*     */ import org.springframework.cache.CacheManager;
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
/*     */ public class CompositeCacheManager
/*     */   implements CacheManager, InitializingBean
/*     */ {
/*  55 */   private final List<CacheManager> cacheManagers = new ArrayList<CacheManager>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean fallbackToNoOpCache = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeCacheManager() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeCacheManager(CacheManager... cacheManagers) {
/*  72 */     setCacheManagers(Arrays.asList(cacheManagers));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheManagers(Collection<CacheManager> cacheManagers) {
/*  80 */     this.cacheManagers.addAll(cacheManagers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFallbackToNoOpCache(boolean fallbackToNoOpCache) {
/*  89 */     this.fallbackToNoOpCache = fallbackToNoOpCache;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/*  94 */     if (this.fallbackToNoOpCache) {
/*  95 */       this.cacheManagers.add(new NoOpCacheManager());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Cache getCache(String name) {
/* 102 */     for (CacheManager cacheManager : this.cacheManagers) {
/* 103 */       Cache cache = cacheManager.getCache(name);
/* 104 */       if (cache != null) {
/* 105 */         return cache;
/*     */       }
/*     */     } 
/* 108 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getCacheNames() {
/* 113 */     Set<String> names = new LinkedHashSet<String>();
/* 114 */     for (CacheManager manager : this.cacheManagers) {
/* 115 */       names.addAll(manager.getCacheNames());
/*     */     }
/* 117 */     return Collections.unmodifiableSet(names);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\support\CompositeCacheManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */