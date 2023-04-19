/*     */ package org.springframework.cache.support;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
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
/*     */ public abstract class AbstractCacheManager
/*     */   implements CacheManager, InitializingBean
/*     */ {
/*  41 */   private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>(16);
/*     */   
/*  43 */   private volatile Set<String> cacheNames = Collections.emptySet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/*  50 */     initializeCaches();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initializeCaches() {
/*  61 */     Collection<? extends Cache> caches = loadCaches();
/*     */     
/*  63 */     synchronized (this.cacheMap) {
/*  64 */       this.cacheNames = Collections.emptySet();
/*  65 */       this.cacheMap.clear();
/*  66 */       Set<String> cacheNames = new LinkedHashSet<String>(caches.size());
/*  67 */       for (Cache cache : caches) {
/*  68 */         String name = cache.getName();
/*  69 */         this.cacheMap.put(name, decorateCache(cache));
/*  70 */         cacheNames.add(name);
/*     */       } 
/*  72 */       this.cacheNames = Collections.unmodifiableSet(cacheNames);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Collection<? extends Cache> loadCaches();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cache getCache(String name) {
/*  88 */     Cache cache = this.cacheMap.get(name);
/*  89 */     if (cache != null) {
/*  90 */       return cache;
/*     */     }
/*     */ 
/*     */     
/*  94 */     synchronized (this.cacheMap) {
/*  95 */       cache = this.cacheMap.get(name);
/*  96 */       if (cache == null) {
/*  97 */         cache = getMissingCache(name);
/*  98 */         if (cache != null) {
/*  99 */           cache = decorateCache(cache);
/* 100 */           this.cacheMap.put(name, cache);
/* 101 */           updateCacheNames(name);
/*     */         } 
/*     */       } 
/* 104 */       return cache;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<String> getCacheNames() {
/* 111 */     return this.cacheNames;
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Cache lookupCache(String name) {
/* 128 */     return this.cacheMap.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected final void addCache(Cache cache) {
/* 138 */     String name = cache.getName();
/* 139 */     synchronized (this.cacheMap) {
/* 140 */       if (this.cacheMap.put(name, decorateCache(cache)) == null) {
/* 141 */         updateCacheNames(name);
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
/*     */   private void updateCacheNames(String name) {
/* 154 */     Set<String> cacheNames = new LinkedHashSet<String>(this.cacheNames.size() + 1);
/* 155 */     cacheNames.addAll(this.cacheNames);
/* 156 */     cacheNames.add(name);
/* 157 */     this.cacheNames = Collections.unmodifiableSet(cacheNames);
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
/*     */   protected Cache decorateCache(Cache cache) {
/* 170 */     return cache;
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected Cache getMissingCache(String name) {
/* 187 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\support\AbstractCacheManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */