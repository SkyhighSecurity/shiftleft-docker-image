/*     */ package org.springframework.cache;
/*     */ 
/*     */ import java.util.concurrent.Callable;
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
/*     */ public interface Cache
/*     */ {
/*     */   String getName();
/*     */   
/*     */   Object getNativeCache();
/*     */   
/*     */   ValueWrapper get(Object paramObject);
/*     */   
/*     */   <T> T get(Object paramObject, Class<T> paramClass);
/*     */   
/*     */   <T> T get(Object paramObject, Callable<T> paramCallable);
/*     */   
/*     */   void put(Object paramObject1, Object paramObject2);
/*     */   
/*     */   ValueWrapper putIfAbsent(Object paramObject1, Object paramObject2);
/*     */   
/*     */   void evict(Object paramObject);
/*     */   
/*     */   void clear();
/*     */   
/*     */   public static class ValueRetrievalException
/*     */     extends RuntimeException
/*     */   {
/*     */     private final Object key;
/*     */     
/*     */     public ValueRetrievalException(Object key, Callable<?> loader, Throwable ex) {
/* 168 */       super(String.format("Value for key '%s' could not be loaded using '%s'", new Object[] { key, loader }), ex);
/* 169 */       this.key = key;
/*     */     }
/*     */     
/*     */     public Object getKey() {
/* 173 */       return this.key;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface ValueWrapper {
/*     */     Object get();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\Cache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */