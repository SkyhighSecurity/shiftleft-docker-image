/*    */ package org.springframework.cache.support;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import org.springframework.cache.Cache;
/*    */ import org.springframework.cache.CacheManager;
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
/*    */ public class NoOpCacheManager
/*    */   implements CacheManager
/*    */ {
/* 43 */   private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>(16);
/*    */   
/* 45 */   private final Set<String> cacheNames = new LinkedHashSet<String>(16);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Cache getCache(String name) {
/* 54 */     Cache cache = this.caches.get(name);
/* 55 */     if (cache == null) {
/* 56 */       this.caches.putIfAbsent(name, new NoOpCache(name));
/* 57 */       synchronized (this.cacheNames) {
/* 58 */         this.cacheNames.add(name);
/*    */       } 
/*    */     } 
/*    */     
/* 62 */     return this.caches.get(name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<String> getCacheNames() {
/* 70 */     synchronized (this.cacheNames) {
/* 71 */       return Collections.unmodifiableSet(this.cacheNames);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\support\NoOpCacheManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */