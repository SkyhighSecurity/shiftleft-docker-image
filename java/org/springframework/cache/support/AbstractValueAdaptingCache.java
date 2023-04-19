/*     */ package org.springframework.cache.support;
/*     */ 
/*     */ import org.springframework.cache.Cache;
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
/*     */ public abstract class AbstractValueAdaptingCache
/*     */   implements Cache
/*     */ {
/*     */   private final boolean allowNullValues;
/*     */   
/*     */   protected AbstractValueAdaptingCache(boolean allowNullValues) {
/*  43 */     this.allowNullValues = allowNullValues;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isAllowNullValues() {
/*  51 */     return this.allowNullValues;
/*     */   }
/*     */ 
/*     */   
/*     */   public Cache.ValueWrapper get(Object key) {
/*  56 */     Object value = lookup(key);
/*  57 */     return toValueWrapper(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T get(Object key, Class<T> type) {
/*  63 */     Object value = fromStoreValue(lookup(key));
/*  64 */     if (value != null && type != null && !type.isInstance(value)) {
/*  65 */       throw new IllegalStateException("Cached value is not of required type [" + type
/*  66 */           .getName() + "]: " + value);
/*     */     }
/*  68 */     return (T)value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Object lookup(Object paramObject);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object fromStoreValue(Object storeValue) {
/*  86 */     if (this.allowNullValues && storeValue == NullValue.INSTANCE) {
/*  87 */       return null;
/*     */     }
/*  89 */     return storeValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object toStoreValue(Object userValue) {
/*  99 */     if (this.allowNullValues && userValue == null) {
/* 100 */       return NullValue.INSTANCE;
/*     */     }
/* 102 */     return userValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Cache.ValueWrapper toValueWrapper(Object storeValue) {
/* 113 */     return (storeValue != null) ? new SimpleValueWrapper(fromStoreValue(storeValue)) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\support\AbstractValueAdaptingCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */