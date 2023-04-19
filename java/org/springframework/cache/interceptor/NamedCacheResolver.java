/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
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
/*    */ public class NamedCacheResolver
/*    */   extends AbstractCacheResolver
/*    */ {
/*    */   private Collection<String> cacheNames;
/*    */   
/*    */   public NamedCacheResolver() {}
/*    */   
/*    */   public NamedCacheResolver(CacheManager cacheManager, String... cacheNames) {
/* 41 */     super(cacheManager);
/* 42 */     this.cacheNames = new ArrayList<String>(Arrays.asList(cacheNames));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCacheNames(Collection<String> cacheNames) {
/* 50 */     this.cacheNames = cacheNames;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
/* 55 */     return this.cacheNames;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\interceptor\NamedCacheResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */