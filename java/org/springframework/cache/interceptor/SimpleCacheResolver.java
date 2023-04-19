/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import java.util.Collection;
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
/*    */ public class SimpleCacheResolver
/*    */   extends AbstractCacheResolver
/*    */ {
/*    */   public SimpleCacheResolver() {}
/*    */   
/*    */   public SimpleCacheResolver(CacheManager cacheManager) {
/* 39 */     super(cacheManager);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
/* 44 */     return context.getOperation().getCacheNames();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\interceptor\SimpleCacheResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */