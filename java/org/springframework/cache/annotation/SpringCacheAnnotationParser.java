/*     */ package org.springframework.cache.annotation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import org.springframework.cache.interceptor.CacheEvictOperation;
/*     */ import org.springframework.cache.interceptor.CacheOperation;
/*     */ import org.springframework.cache.interceptor.CachePutOperation;
/*     */ import org.springframework.cache.interceptor.CacheableOperation;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class SpringCacheAnnotationParser
/*     */   implements CacheAnnotationParser, Serializable
/*     */ {
/*     */   public Collection<CacheOperation> parseCacheAnnotations(Class<?> type) {
/*  51 */     DefaultCacheConfig defaultConfig = getDefaultCacheConfig(type);
/*  52 */     return parseCacheAnnotations(defaultConfig, type);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<CacheOperation> parseCacheAnnotations(Method method) {
/*  57 */     DefaultCacheConfig defaultConfig = getDefaultCacheConfig(method.getDeclaringClass());
/*  58 */     return parseCacheAnnotations(defaultConfig, method);
/*     */   }
/*     */   
/*     */   private Collection<CacheOperation> parseCacheAnnotations(DefaultCacheConfig cachingConfig, AnnotatedElement ae) {
/*  62 */     Collection<CacheOperation> ops = null;
/*     */     
/*  64 */     Collection<Cacheable> cacheables = AnnotatedElementUtils.getAllMergedAnnotations(ae, Cacheable.class);
/*  65 */     if (!cacheables.isEmpty()) {
/*  66 */       ops = lazyInit(ops);
/*  67 */       for (Cacheable cacheable : cacheables) {
/*  68 */         ops.add(parseCacheableAnnotation(ae, cachingConfig, cacheable));
/*     */       }
/*     */     } 
/*     */     
/*  72 */     Collection<CacheEvict> evicts = AnnotatedElementUtils.getAllMergedAnnotations(ae, CacheEvict.class);
/*  73 */     if (!evicts.isEmpty()) {
/*  74 */       ops = lazyInit(ops);
/*  75 */       for (CacheEvict evict : evicts) {
/*  76 */         ops.add(parseEvictAnnotation(ae, cachingConfig, evict));
/*     */       }
/*     */     } 
/*     */     
/*  80 */     Collection<CachePut> puts = AnnotatedElementUtils.getAllMergedAnnotations(ae, CachePut.class);
/*  81 */     if (!puts.isEmpty()) {
/*  82 */       ops = lazyInit(ops);
/*  83 */       for (CachePut put : puts) {
/*  84 */         ops.add(parsePutAnnotation(ae, cachingConfig, put));
/*     */       }
/*     */     } 
/*     */     
/*  88 */     Collection<Caching> cachings = AnnotatedElementUtils.getAllMergedAnnotations(ae, Caching.class);
/*  89 */     if (!cachings.isEmpty()) {
/*  90 */       ops = lazyInit(ops);
/*  91 */       for (Caching caching : cachings) {
/*  92 */         Collection<CacheOperation> cachingOps = parseCachingAnnotation(ae, cachingConfig, caching);
/*  93 */         if (cachingOps != null) {
/*  94 */           ops.addAll(cachingOps);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  99 */     return ops;
/*     */   }
/*     */   
/*     */   private <T extends java.lang.annotation.Annotation> Collection<CacheOperation> lazyInit(Collection<CacheOperation> ops) {
/* 103 */     return (ops != null) ? ops : new ArrayList<CacheOperation>(1);
/*     */   }
/*     */   
/*     */   CacheableOperation parseCacheableAnnotation(AnnotatedElement ae, DefaultCacheConfig defaultConfig, Cacheable cacheable) {
/* 107 */     CacheableOperation.Builder builder = new CacheableOperation.Builder();
/*     */     
/* 109 */     builder.setName(ae.toString());
/* 110 */     builder.setCacheNames(cacheable.cacheNames());
/* 111 */     builder.setCondition(cacheable.condition());
/* 112 */     builder.setUnless(cacheable.unless());
/* 113 */     builder.setKey(cacheable.key());
/* 114 */     builder.setKeyGenerator(cacheable.keyGenerator());
/* 115 */     builder.setCacheManager(cacheable.cacheManager());
/* 116 */     builder.setCacheResolver(cacheable.cacheResolver());
/* 117 */     builder.setSync(cacheable.sync());
/*     */     
/* 119 */     defaultConfig.applyDefault((CacheOperation.Builder)builder);
/* 120 */     CacheableOperation op = builder.build();
/* 121 */     validateCacheOperation(ae, (CacheOperation)op);
/*     */     
/* 123 */     return op;
/*     */   }
/*     */   
/*     */   CacheEvictOperation parseEvictAnnotation(AnnotatedElement ae, DefaultCacheConfig defaultConfig, CacheEvict cacheEvict) {
/* 127 */     CacheEvictOperation.Builder builder = new CacheEvictOperation.Builder();
/*     */     
/* 129 */     builder.setName(ae.toString());
/* 130 */     builder.setCacheNames(cacheEvict.cacheNames());
/* 131 */     builder.setCondition(cacheEvict.condition());
/* 132 */     builder.setKey(cacheEvict.key());
/* 133 */     builder.setKeyGenerator(cacheEvict.keyGenerator());
/* 134 */     builder.setCacheManager(cacheEvict.cacheManager());
/* 135 */     builder.setCacheResolver(cacheEvict.cacheResolver());
/* 136 */     builder.setCacheWide(cacheEvict.allEntries());
/* 137 */     builder.setBeforeInvocation(cacheEvict.beforeInvocation());
/*     */     
/* 139 */     defaultConfig.applyDefault((CacheOperation.Builder)builder);
/* 140 */     CacheEvictOperation op = builder.build();
/* 141 */     validateCacheOperation(ae, (CacheOperation)op);
/*     */     
/* 143 */     return op;
/*     */   }
/*     */   
/*     */   CacheOperation parsePutAnnotation(AnnotatedElement ae, DefaultCacheConfig defaultConfig, CachePut cachePut) {
/* 147 */     CachePutOperation.Builder builder = new CachePutOperation.Builder();
/*     */     
/* 149 */     builder.setName(ae.toString());
/* 150 */     builder.setCacheNames(cachePut.cacheNames());
/* 151 */     builder.setCondition(cachePut.condition());
/* 152 */     builder.setUnless(cachePut.unless());
/* 153 */     builder.setKey(cachePut.key());
/* 154 */     builder.setKeyGenerator(cachePut.keyGenerator());
/* 155 */     builder.setCacheManager(cachePut.cacheManager());
/* 156 */     builder.setCacheResolver(cachePut.cacheResolver());
/*     */     
/* 158 */     defaultConfig.applyDefault((CacheOperation.Builder)builder);
/* 159 */     CachePutOperation op = builder.build();
/* 160 */     validateCacheOperation(ae, (CacheOperation)op);
/*     */     
/* 162 */     return (CacheOperation)op;
/*     */   }
/*     */   
/*     */   Collection<CacheOperation> parseCachingAnnotation(AnnotatedElement ae, DefaultCacheConfig defaultConfig, Caching caching) {
/* 166 */     Collection<CacheOperation> ops = null;
/*     */     
/* 168 */     Cacheable[] cacheables = caching.cacheable();
/* 169 */     if (!ObjectUtils.isEmpty((Object[])cacheables)) {
/* 170 */       ops = lazyInit(ops);
/* 171 */       for (Cacheable cacheable : cacheables) {
/* 172 */         ops.add(parseCacheableAnnotation(ae, defaultConfig, cacheable));
/*     */       }
/*     */     } 
/* 175 */     CacheEvict[] cacheEvicts = caching.evict();
/* 176 */     if (!ObjectUtils.isEmpty((Object[])cacheEvicts)) {
/* 177 */       ops = lazyInit(ops);
/* 178 */       for (CacheEvict cacheEvict : cacheEvicts) {
/* 179 */         ops.add(parseEvictAnnotation(ae, defaultConfig, cacheEvict));
/*     */       }
/*     */     } 
/* 182 */     CachePut[] cachePuts = caching.put();
/* 183 */     if (!ObjectUtils.isEmpty((Object[])cachePuts)) {
/* 184 */       ops = lazyInit(ops);
/* 185 */       for (CachePut cachePut : cachePuts) {
/* 186 */         ops.add(parsePutAnnotation(ae, defaultConfig, cachePut));
/*     */       }
/*     */     } 
/*     */     
/* 190 */     return ops;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DefaultCacheConfig getDefaultCacheConfig(Class<?> target) {
/* 199 */     CacheConfig annotation = (CacheConfig)AnnotatedElementUtils.getMergedAnnotation(target, CacheConfig.class);
/* 200 */     if (annotation != null) {
/* 201 */       return new DefaultCacheConfig(annotation.cacheNames(), annotation.keyGenerator(), annotation
/* 202 */           .cacheManager(), annotation.cacheResolver());
/*     */     }
/* 204 */     return new DefaultCacheConfig();
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
/*     */   private void validateCacheOperation(AnnotatedElement ae, CacheOperation operation) {
/* 216 */     if (StringUtils.hasText(operation.getKey()) && StringUtils.hasText(operation.getKeyGenerator())) {
/* 217 */       throw new IllegalStateException("Invalid cache annotation configuration on '" + ae
/* 218 */           .toString() + "'. Both 'key' and 'keyGenerator' attributes have been set. These attributes are mutually exclusive: either set the SpEL expression used tocompute the key at runtime or set the name of the KeyGenerator bean to use.");
/*     */     }
/*     */ 
/*     */     
/* 222 */     if (StringUtils.hasText(operation.getCacheManager()) && StringUtils.hasText(operation.getCacheResolver())) {
/* 223 */       throw new IllegalStateException("Invalid cache annotation configuration on '" + ae
/* 224 */           .toString() + "'. Both 'cacheManager' and 'cacheResolver' attributes have been set. These attributes are mutually exclusive: the cache manager is used to configure adefault cache resolver if none is set. If a cache resolver is set, the cache managerwon't be used.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 233 */     return (this == other || other instanceof SpringCacheAnnotationParser);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 238 */     return SpringCacheAnnotationParser.class.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class DefaultCacheConfig
/*     */   {
/*     */     private final String[] cacheNames;
/*     */ 
/*     */     
/*     */     private final String keyGenerator;
/*     */     
/*     */     private final String cacheManager;
/*     */     
/*     */     private final String cacheResolver;
/*     */ 
/*     */     
/*     */     public DefaultCacheConfig() {
/* 256 */       this(null, null, null, null);
/*     */     }
/*     */     
/*     */     private DefaultCacheConfig(String[] cacheNames, String keyGenerator, String cacheManager, String cacheResolver) {
/* 260 */       this.cacheNames = cacheNames;
/* 261 */       this.keyGenerator = keyGenerator;
/* 262 */       this.cacheManager = cacheManager;
/* 263 */       this.cacheResolver = cacheResolver;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void applyDefault(CacheOperation.Builder builder) {
/* 271 */       if (builder.getCacheNames().isEmpty() && this.cacheNames != null) {
/* 272 */         builder.setCacheNames(this.cacheNames);
/*     */       }
/* 274 */       if (!StringUtils.hasText(builder.getKey()) && !StringUtils.hasText(builder.getKeyGenerator()) && 
/* 275 */         StringUtils.hasText(this.keyGenerator)) {
/* 276 */         builder.setKeyGenerator(this.keyGenerator);
/*     */       }
/*     */       
/* 279 */       if (!StringUtils.hasText(builder.getCacheManager()) && !StringUtils.hasText(builder.getCacheResolver()))
/*     */       {
/*     */         
/* 282 */         if (StringUtils.hasText(this.cacheResolver)) {
/* 283 */           builder.setCacheResolver(this.cacheResolver);
/*     */         }
/* 285 */         else if (StringUtils.hasText(this.cacheManager)) {
/* 286 */           builder.setCacheManager(this.cacheManager);
/*     */         } 
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\annotation\SpringCacheAnnotationParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */