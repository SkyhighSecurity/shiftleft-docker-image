/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import org.springframework.beans.factory.InitializingBean;
/*    */ import org.springframework.cache.Cache;
/*    */ import org.springframework.cache.CacheManager;
/*    */ import org.springframework.util.Assert;
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
/*    */ public abstract class AbstractCacheResolver
/*    */   implements CacheResolver, InitializingBean
/*    */ {
/*    */   private CacheManager cacheManager;
/*    */   
/*    */   protected AbstractCacheResolver() {}
/*    */   
/*    */   protected AbstractCacheResolver(CacheManager cacheManager) {
/* 54 */     this.cacheManager = cacheManager;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCacheManager(CacheManager cacheManager) {
/* 62 */     this.cacheManager = cacheManager;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CacheManager getCacheManager() {
/* 69 */     return this.cacheManager;
/*    */   }
/*    */ 
/*    */   
/*    */   public void afterPropertiesSet() {
/* 74 */     Assert.notNull(this.cacheManager, "CacheManager is required");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
/* 80 */     Collection<String> cacheNames = getCacheNames(context);
/* 81 */     if (cacheNames == null) {
/* 82 */       return Collections.emptyList();
/*    */     }
/* 84 */     Collection<Cache> result = new ArrayList<Cache>(cacheNames.size());
/* 85 */     for (String cacheName : cacheNames) {
/* 86 */       Cache cache = getCacheManager().getCache(cacheName);
/* 87 */       if (cache == null) {
/* 88 */         throw new IllegalArgumentException("Cannot find cache named '" + cacheName + "' for " + context
/* 89 */             .getOperation());
/*    */       }
/* 91 */       result.add(cache);
/*    */     } 
/* 93 */     return result;
/*    */   }
/*    */   
/*    */   protected abstract Collection<String> getCacheNames(CacheOperationInvocationContext<?> paramCacheOperationInvocationContext);
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\interceptor\AbstractCacheResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */