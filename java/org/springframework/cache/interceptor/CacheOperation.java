/*     */ package org.springframework.cache.interceptor;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.util.Assert;
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
/*     */ public abstract class CacheOperation
/*     */   implements BasicOperation
/*     */ {
/*     */   private final String name;
/*     */   private final Set<String> cacheNames;
/*     */   private final String key;
/*     */   private final String keyGenerator;
/*     */   private final String cacheManager;
/*     */   private final String cacheResolver;
/*     */   private final String condition;
/*     */   private final String toString;
/*     */   
/*     */   protected CacheOperation(Builder b) {
/*  56 */     this.name = b.name;
/*  57 */     this.cacheNames = b.cacheNames;
/*  58 */     this.key = b.key;
/*  59 */     this.keyGenerator = b.keyGenerator;
/*  60 */     this.cacheManager = b.cacheManager;
/*  61 */     this.cacheResolver = b.cacheResolver;
/*  62 */     this.condition = b.condition;
/*  63 */     this.toString = b.getOperationDescription().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  68 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getCacheNames() {
/*  73 */     return this.cacheNames;
/*     */   }
/*     */   
/*     */   public String getKey() {
/*  77 */     return this.key;
/*     */   }
/*     */   
/*     */   public String getKeyGenerator() {
/*  81 */     return this.keyGenerator;
/*     */   }
/*     */   
/*     */   public String getCacheManager() {
/*  85 */     return this.cacheManager;
/*     */   }
/*     */   
/*     */   public String getCacheResolver() {
/*  89 */     return this.cacheResolver;
/*     */   }
/*     */   
/*     */   public String getCondition() {
/*  93 */     return this.condition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 103 */     return (other instanceof CacheOperation && toString().equals(other.toString()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 112 */     return toString().hashCode();
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
/*     */   public final String toString() {
/* 124 */     return this.toString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class Builder
/*     */   {
/* 133 */     private String name = "";
/*     */     
/* 135 */     private Set<String> cacheNames = Collections.emptySet();
/*     */     
/* 137 */     private String key = "";
/*     */     
/* 139 */     private String keyGenerator = "";
/*     */     
/* 141 */     private String cacheManager = "";
/*     */     
/* 143 */     private String cacheResolver = "";
/*     */     
/* 145 */     private String condition = "";
/*     */     
/*     */     public void setName(String name) {
/* 148 */       Assert.hasText(name, "Name must not be empty");
/* 149 */       this.name = name;
/*     */     }
/*     */     
/*     */     public void setCacheName(String cacheName) {
/* 153 */       Assert.hasText(cacheName, "Cache name must not be empty");
/* 154 */       this.cacheNames = Collections.singleton(cacheName);
/*     */     }
/*     */     
/*     */     public void setCacheNames(String... cacheNames) {
/* 158 */       this.cacheNames = new LinkedHashSet<String>(cacheNames.length);
/* 159 */       for (String cacheName : cacheNames) {
/* 160 */         Assert.hasText(cacheName, "Cache name must be non-empty if specified");
/* 161 */         this.cacheNames.add(cacheName);
/*     */       } 
/*     */     }
/*     */     
/*     */     public Set<String> getCacheNames() {
/* 166 */       return this.cacheNames;
/*     */     }
/*     */     
/*     */     public void setKey(String key) {
/* 170 */       Assert.notNull(key, "Key must not be null");
/* 171 */       this.key = key;
/*     */     }
/*     */     
/*     */     public String getKey() {
/* 175 */       return this.key;
/*     */     }
/*     */     
/*     */     public String getKeyGenerator() {
/* 179 */       return this.keyGenerator;
/*     */     }
/*     */     
/*     */     public String getCacheManager() {
/* 183 */       return this.cacheManager;
/*     */     }
/*     */     
/*     */     public String getCacheResolver() {
/* 187 */       return this.cacheResolver;
/*     */     }
/*     */     
/*     */     public void setKeyGenerator(String keyGenerator) {
/* 191 */       Assert.notNull(keyGenerator, "KeyGenerator name must not be null");
/* 192 */       this.keyGenerator = keyGenerator;
/*     */     }
/*     */     
/*     */     public void setCacheManager(String cacheManager) {
/* 196 */       Assert.notNull(cacheManager, "CacheManager name must not be null");
/* 197 */       this.cacheManager = cacheManager;
/*     */     }
/*     */     
/*     */     public void setCacheResolver(String cacheResolver) {
/* 201 */       Assert.notNull(cacheResolver, "CacheResolver name must not be null");
/* 202 */       this.cacheResolver = cacheResolver;
/*     */     }
/*     */     
/*     */     public void setCondition(String condition) {
/* 206 */       Assert.notNull(condition, "Condition must not be null");
/* 207 */       this.condition = condition;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected StringBuilder getOperationDescription() {
/* 215 */       StringBuilder result = new StringBuilder(getClass().getSimpleName());
/* 216 */       result.append("[").append(this.name);
/* 217 */       result.append("] caches=").append(this.cacheNames);
/* 218 */       result.append(" | key='").append(this.key);
/* 219 */       result.append("' | keyGenerator='").append(this.keyGenerator);
/* 220 */       result.append("' | cacheManager='").append(this.cacheManager);
/* 221 */       result.append("' | cacheResolver='").append(this.cacheResolver);
/* 222 */       result.append("' | condition='").append(this.condition).append("'");
/* 223 */       return result;
/*     */     }
/*     */     
/*     */     public abstract CacheOperation build();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\interceptor\CacheOperation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */