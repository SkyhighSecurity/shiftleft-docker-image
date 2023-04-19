/*     */ package org.springframework.cache.interceptor;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.MethodClassKey;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public abstract class AbstractFallbackCacheOperationSource
/*     */   implements CacheOperationSource
/*     */ {
/*  58 */   private static final Collection<CacheOperation> NULL_CACHING_ATTRIBUTE = Collections.emptyList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   private final Map<Object, Collection<CacheOperation>> attributeCache = new ConcurrentHashMap<Object, Collection<CacheOperation>>(1024);
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
/*     */   public Collection<CacheOperation> getCacheOperations(Method method, Class<?> targetClass) {
/*  87 */     if (method.getDeclaringClass() == Object.class) {
/*  88 */       return null;
/*     */     }
/*     */     
/*  91 */     Object cacheKey = getCacheKey(method, targetClass);
/*  92 */     Collection<CacheOperation> cached = this.attributeCache.get(cacheKey);
/*     */     
/*  94 */     if (cached != null) {
/*  95 */       return (cached != NULL_CACHING_ATTRIBUTE) ? cached : null;
/*     */     }
/*     */     
/*  98 */     Collection<CacheOperation> cacheOps = computeCacheOperations(method, targetClass);
/*  99 */     if (cacheOps != null) {
/* 100 */       if (this.logger.isDebugEnabled()) {
/* 101 */         this.logger.debug("Adding cacheable method '" + method.getName() + "' with attribute: " + cacheOps);
/*     */       }
/* 103 */       this.attributeCache.put(cacheKey, cacheOps);
/*     */     } else {
/*     */       
/* 106 */       this.attributeCache.put(cacheKey, NULL_CACHING_ATTRIBUTE);
/*     */     } 
/* 108 */     return cacheOps;
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
/*     */   protected Object getCacheKey(Method method, Class<?> targetClass) {
/* 121 */     return new MethodClassKey(method, targetClass);
/*     */   }
/*     */ 
/*     */   
/*     */   private Collection<CacheOperation> computeCacheOperations(Method method, Class<?> targetClass) {
/* 126 */     if (allowPublicMethodsOnly() && !Modifier.isPublic(method.getModifiers())) {
/* 127 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 132 */     Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
/*     */     
/* 134 */     specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
/*     */ 
/*     */     
/* 137 */     Collection<CacheOperation> opDef = findCacheOperations(specificMethod);
/* 138 */     if (opDef != null) {
/* 139 */       return opDef;
/*     */     }
/*     */ 
/*     */     
/* 143 */     opDef = findCacheOperations(specificMethod.getDeclaringClass());
/* 144 */     if (opDef != null && ClassUtils.isUserLevelMethod(method)) {
/* 145 */       return opDef;
/*     */     }
/*     */     
/* 148 */     if (specificMethod != method) {
/*     */       
/* 150 */       opDef = findCacheOperations(method);
/* 151 */       if (opDef != null) {
/* 152 */         return opDef;
/*     */       }
/*     */       
/* 155 */       opDef = findCacheOperations(method.getDeclaringClass());
/* 156 */       if (opDef != null && ClassUtils.isUserLevelMethod(method)) {
/* 157 */         return opDef;
/*     */       }
/*     */     } 
/*     */     
/* 161 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Collection<CacheOperation> findCacheOperations(Class<?> paramClass);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Collection<CacheOperation> findCacheOperations(Method paramMethod);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean allowPublicMethodsOnly() {
/* 186 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\interceptor\AbstractFallbackCacheOperationSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */