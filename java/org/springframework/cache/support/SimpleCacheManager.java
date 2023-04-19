/*    */ package org.springframework.cache.support;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import org.springframework.cache.Cache;
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
/*    */ public class SimpleCacheManager
/*    */   extends AbstractCacheManager
/*    */ {
/* 33 */   private Collection<? extends Cache> caches = Collections.emptySet();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCaches(Collection<? extends Cache> caches) {
/* 40 */     this.caches = caches;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Collection<? extends Cache> loadCaches() {
/* 45 */     return this.caches;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\support\SimpleCacheManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */