/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
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
/*     */ public class CachingMetadataReaderFactory
/*     */   extends SimpleMetadataReaderFactory
/*     */ {
/*     */   public static final int DEFAULT_CACHE_LIMIT = 256;
/*  41 */   private volatile int cacheLimit = 256;
/*     */   
/*  43 */   private final Map<Resource, MetadataReader> metadataReaderCache = new LinkedHashMap<Resource, MetadataReader>(256, 0.75F, true)
/*     */     {
/*     */       
/*     */       protected boolean removeEldestEntry(Map.Entry<Resource, MetadataReader> eldest)
/*     */       {
/*  48 */         return (size() > CachingMetadataReaderFactory.this.getCacheLimit());
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CachingMetadataReaderFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CachingMetadataReaderFactory(ResourceLoader resourceLoader) {
/*  66 */     super(resourceLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CachingMetadataReaderFactory(ClassLoader classLoader) {
/*  74 */     super(classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheLimit(int cacheLimit) {
/*  83 */     this.cacheLimit = cacheLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCacheLimit() {
/*  90 */     return this.cacheLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MetadataReader getMetadataReader(Resource resource) throws IOException {
/*  96 */     if (getCacheLimit() <= 0) {
/*  97 */       return super.getMetadataReader(resource);
/*     */     }
/*  99 */     synchronized (this.metadataReaderCache) {
/* 100 */       MetadataReader metadataReader = this.metadataReaderCache.get(resource);
/* 101 */       if (metadataReader == null) {
/* 102 */         metadataReader = super.getMetadataReader(resource);
/* 103 */         this.metadataReaderCache.put(resource, metadataReader);
/*     */       } 
/* 105 */       return metadataReader;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearCache() {
/* 113 */     synchronized (this.metadataReaderCache) {
/* 114 */       this.metadataReaderCache.clear();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\type\classreading\CachingMetadataReaderFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */